"""A tool to parse wikipedia's dictionary of chemical formulas

See it saved at tests/data/.

"""
import argparse
import collections
import itertools
import json
import os
import re
import urllib2

import mako.lookup

import formula as F
import chemical_elements as E
import wikipedia_compound_filter

DEFAULT_URL = "http://en.wikipedia.org/wiki/Dictionary_of_chemical_formulas"
TEMPLATE_DIR = "templates/"
WIKI_USER_AGENT = "urllib"

PARSED_ARGS = None


def main():
    global PARSED_ARGS
    PARSED_ARGS = args = parse_cmdline()
    if args.saved_html:
        fh = open(args.saved_html)
    else:
        fh = wiki_urlopen(DEFAULT_URL)

    final_cond = []
    if args.max_atoms:
        final_cond.append(lambda f: f.atom_count() <= args.max_atoms)
    if args.max_elements:
        final_cond.append(lambda f: f.element_count() <= args.max_elements)
    if args.max_terminals:
        final_cond.append(lambda f: len(f) <= args.max_terminals)

    if args.min_atoms:
        final_cond.append(lambda f: f.atom_count() >= args.min_atoms)
    if args.min_elements:
        final_cond.append(lambda f: f.element_count() >= args.min_elements)
    if args.min_terminals:
        final_cond.append(lambda f: len(f) >= args.min_terminals)

    class Formula(F.Formula):
        is_final = formula_matches(final_cond)

    table = parse_html(fh.read().decode("utf-8"))
    formulas = (row[0].data for row in table
                if row[0].data != "(benzenediols)")
    formulas = F.parse_formulas(formulas, getattr(Formula, args.parser),
                                args.lexer)

    # Explicitly filter Tritium #47
    strict_cond = [lambda f: E.TRITIUM not in f.atoms]
    if args.filter:
        strict_cond.append(lambda f: getattr(f, args.filter))
    if args.atoms:
        allowed_elem = frozenset(E.ELEMENTS[sym] for sym in args.atoms)
        strict_cond.append(lambda f: all(e in allowed_elem for e in f.atoms))
    if args.max_coefficient:
        strict_cond.append(lambda f: all(c <= args.max_coefficient for c in
                                         f.coefficients))
    if args.no_hydrates:
        strict_cond.append(lambda f: all(t.text[0] != "*" for t in f))
    if args.no_opening_brackets:
        strict_cond.append(lambda f: "(" not in f)
    if args.unique_elements:
        strict_cond.append(lambda f: len(f.atoms) == f.element_count())

    formulas = filter(formula_matches(strict_cond), formulas)
    output = args.output(formulas)

    print output.encode("utf-8")


def parse_cmdline():
    """Parse command line"""
    description = "Parse Wikipedia Dictionary of Chemical Formulas " + \
        DEFAULT_URL
    parser = argparse.ArgumentParser(description=description)

    parser.add_argument("saved_html", nargs="?",
                        help="Path to saved Wikipedia html")

    output = parser.add_mutually_exclusive_group()
    output.add_argument("-t", "--text", dest="output",
                        action="store_const", const=dump_text,
                        help="Text formulas")
    output.add_argument("-j", "--json", dest="output",
                        action="store_const", const=dump_json,
                        help="Parsed formulas")
    output.add_argument("--class", dest="output",
                        action="store_const", const=dump_java_class,
                        help="Java class Parsed formulas")
    output.add_argument("--test", dest="output",
                        action="store_const", const=dump_java_test,
                        help="Parsed formulas")
    output.add_argument("-s", "--stats", dest="output",
                        action="store_const", const=dump_stats,
                        help="Formula terminals frequency")

    mode = parser.add_mutually_exclusive_group()
    mode.add_argument("-A", "--all", dest="filter",
                      action="store_const", const=None,
                      help="Only ions")
    mode.add_argument("-C", "--compounds", dest="filter",
                      action="store_const", const="is_compound",
                      help="Only compounds")
    mode.add_argument("-I", "--ions", dest="filter",
                      action="store_const", const="is_ion",
                      help="Only ions")

    parser.add_argument("-atom", default=[], action="append",
                        choices=E.SYMBOLS,
                        help="Limit formulas to those containg"
                        " specified elements")

    parser.add_argument("-period", default=[], action="append", type=int,
                        choices=range(1, len(E.PERIODS) + 1),
                        help="Limit formulas to those containg elements"
                        " of specified groups")

    parser.add_argument("--no-hydrates", default=False, action="store_true",
                        help="Limit formulas to those having no '*xH2O'")
    parser.add_argument("--no-opening-brackets",
                        default=False, action="store_true",
                        help="Limit formulas to those without opening"
                        " parentheses")
    parser.add_argument("--unique-elements", "--no-repeated-elements",
                        default=False, action="store_true",
                        help="Limit formulas to those having each element"
                        " 0 or 1 time, i.e. having no repeated elements")

    parser.add_argument("--max-atoms",
                        default=None, type=int, metavar="N",
                        help="Limit formulas by maximal atom count")
    parser.add_argument("--max-coefficient",
                        default=None, type=int, metavar="N",
                        help="Limit formulas by maximal coefficient value")
    parser.add_argument("--max-elements",
                        default=None, type=int, metavar="N",
                        help="Limit formulas by maximal element count"
                        " (different atoms)")
    parser.add_argument("--max-terminals",
                        default=None, type=int, metavar="N",
                        help="Limit formulas by maximal terminal count")

    parser.add_argument("--min-atoms",
                        default=None, type=int, metavar="N",
                        help="Limit formulas by minimal atom count")
    parser.add_argument("--min-elements",
                        default=None, type=int, metavar="N",
                        help="Limit formulas by minimal element count"
                        " (different atoms)")
    parser.add_argument("--min-terminals",
                        default=2, type=int, metavar="N",
                        help="Limit formulas by minimal terminal count")

    l = parser.add_mutually_exclusive_group()
    l.add_argument("--simple", dest="lexer",
                   action="store_const", const=F.Lexems.plain,
                   help="Let coefficients be separate terminals")
    l.add_argument("--compact", dest="lexer",
                   action="store_const", const=F.Lexems.compact,
                   help="Put coefficients in one terminal"
                   " with an atom or a bracket.")

    p = parser.add_mutually_exclusive_group()
    p.add_argument("--plain", dest="parser",
                   action="store_const", const="parse_plain",
                   help="Parse formula terminals as they are.")
    p.add_argument("--pair-brackets", dest="parser",
                   action="store_const", const="parse_pair_brackets",
                   help="Generate paired parenthesis terminals")
    p.add_argument("--closing-brackets", dest="parser",
                   action="store_const", const="parse_closing_brackets",
                   help="Generate only closing parenthesis terminals."
                   " Opening parenthesis terminals are removed.")

    parser.set_defaults(output=dump_text, filter=None, special=[],
                        parser="parse_plain", lexer=F.Lexems.plain)
    args = parser.parse_args()

    if args.min_terminals < 2:
        parser.error("--min-terminals must be greater or equal to 2")

    args.atoms = frozenset(itertools.chain(
        args.atom, *(map(str, E.PERIODS[i - 1]) for i in args.period)))

    return args


def wiki_urlopen(url):
    return urllib2.urlopen(
        urllib2.Request(url, headers={"User-agent": WIKI_USER_AGENT}))


def parse_html(html):
    parser = wikipedia_compound_filter.TableFilter()
    parser.feed(html)
    parser.close()
    return parser.get_table()


def dump_text(formulas):
    return "\n".join(map(formula2text, formulas))


def formula2text(f):
    mark = "f" if f.is_final() else "-"
    return "{}  {:<16} {}".format(mark, f.text, f.prefix(sep=" "))


def dump_json(formulas):
    props = lambda f: dict(final=f.is_final(), terms=map(str, f.terms))
    return json.dumps(dict((f.text, props(f)) for f in formulas))


def dump_stats(formulas):
    stats = get_term_stats(formulas)
    stats.sort(key=lambda x: x[1])
    return "\n".join("%8i %s" % (count, term) for (term, count) in stats)


def dump_java_class(formulas):
    stats = get_term_stats(f for f in formulas if f.is_final())
    stats.sort(key=lambda x: x[0])
    return render("KnownFormulas.mako", name="KnownFormulas",
                  formulas=formulas, final_formula_stats=stats)


def dump_java_test(formulas):
    return render("KnownFormulasTest.mako", name="KnownFormulasTest",
                  formulas=formulas)


def render(template_name, **namespace):
    lookup = mako.lookup.TemplateLookup(directories=[TEMPLATE_DIR])
    template = lookup.get_template(template_name)
    s = template.render(PARSED_ARGS=PARSED_ARGS, **namespace)
    return re.sub("^\s+$", "", s, flags=re.M)


def get_term_stats(formulas):
    """Return terminals with/sorted by number of occurrences in formulas"""
    Stat = collections.namedtuple("Stat", "term count")
    terms = []
    for f in formulas:
        terms.extend(f.terms)
    terms.sort()
    stats = [Stat(term, len(list(group)))
             for (term, group) in itertools.groupby(terms)]
    return stats


def formula_matches(conditions):
    return lambda formula: all(c(formula) for c in conditions)


if __name__ == "__main__":
    main()
