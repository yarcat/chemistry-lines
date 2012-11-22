"""A tool to parse wikipedia's dictionary of chemical formulas

See it saved at tests/data/.

"""
import argparse
import collections
import itertools
import json
import re
import urllib2

import mako.lookup

import formula as F
import chemical_elements as E
import wikipedia_compound_filter

DEFAULT_URL = "http://en.wikipedia.org/wiki/Dictionary_of_chemical_formulas"
TEMPLATE_DIR = "templates/"
WIKI_USER_AGENT = "urllib"


def main():
    args = parse_cmdline()
    if args.saved_html:
        fh = open(args.saved_html)
    else:
        fh = wiki_urlopen(DEFAULT_URL)

    final_cond = []
    if args.no_opening_brackets:
        final_cond.append(no_opening_brackets)

    if args.max_atoms:
        final_cond.append(lambda f: f.atom_count() <= args.max_atoms)
    if args.max_elements:
        final_cond.append(lambda f: f.elememt_count() <= args.max_elements)
    if args.max_terminals:
        final_cond.append(lambda f: len(f) <= args.max_terminals)

    if args.min_atoms:
        final_cond.append(lambda f: f.atom_count() >= args.min_atoms)
    if args.min_elements:
        final_cond.append(lambda f: f.elememt_count() >= args.min_elements)
    if args.min_terminals:
        final_cond.append(lambda f: len(f) >= args.min_terminals)

    class Formula(F.Formula):
        is_final = lambda self: all(c(self) for c in final_cond)

    table = parse_html(fh.read().decode("utf-8"))
    formulas = (row[0].data for row in table
                if row[0].data != "(benzenediols)")
    formulas = F.parse_formulas(Formula.parse_plain, formulas)

    if args.filter:
        formulas = filter(args.filter, formulas)

    if args.atoms:
        formulas = filter_by_atoms(formulas, args.atoms)

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
                      action="store_const", const=lambda f: f.is_compound,
                      help="Only compounds")
    mode.add_argument("-I", "--ions", dest="filter",
                      action="store_const", const=lambda f: f.is_ion,
                      help="Only ions")

    parser.add_argument("-atom", default=[], action="append",
                        choices=E.ATOMS,
                        help="Limit formulas to those containg"
                        " specified elements")

    parser.add_argument("-group", default=[], action="append", type=int,
                        choices=range(1, len(E.GROUPS) + 1),
                        help="Limit formulas to those containg elements"
                        " of specified groups")

    parser.add_argument("--no-opening-brackets",
                        default=False, action="store_true",
                        help="Limit formulas to those without opening"
                        " parentheses")

    max_limit = parser.add_mutually_exclusive_group()
    max_limit.add_argument("--max-atoms",
                           default=None, type=int, metavar="N",
                           help="Limit formulas by maximal atom count")
    max_limit.add_argument("--max-elements",
                           default=None, type=int, metavar="N",
                           help="Limit formulas by maximal element count"
                           " (different atoms)")
    max_limit.add_argument("--max-terminals",
                           default=None, type=int, metavar="N",
                           help="Limit formulas by maximal terminal count")

    min_limit = parser.add_mutually_exclusive_group()
    min_limit.add_argument("--min-atoms",
                           default=None, type=int, metavar="N",
                           help="Limit formulas by minimal atom count")
    min_limit.add_argument("--min-elements",
                           default=None, type=int, metavar="N",
                           help="Limit formulas by minimal element count"
                           " (different atoms)")
    min_limit.add_argument("--min-terminals",
                           default=None, type=int, metavar="N",
                           help="Limit formulas by minimal terminal count")

    parser.set_defaults(output=dump_text, filter=None, special=[])
    args = parser.parse_args()

    args.atoms = frozenset(itertools.chain(
        args.atom, *(E.GROUPS[i - 1] for i in args.group)))

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
    stats = get_term_stats(formulas)
    stats.sort(key=lambda x: x[0])
    return render("KnownFormulas.mako", name="KnownFormulas",
                  formulas=formulas, stats=stats)


def dump_java_test(formulas):
    return render("KnownFormulasTest.mako", name="KnownFormulasTest",
                  formulas=formulas)


def render(template_name, **namespace):
    lookup = mako.lookup.TemplateLookup(directories=[TEMPLATE_DIR])
    template = lookup.get_template(template_name)
    s = template.render(**namespace)
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


def filter_by_atoms(formulas, atoms):
    return [f for f in formulas
            if all(not term.is_atom or term in atoms for term in f.terms)]


def no_opening_brackets(formula):
    return "(" not in formula and "[" not in formula


if __name__ == "__main__":
    main()
