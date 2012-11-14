# -*- coding: utf-8 -*-
"""A tool to parse wikipedia's dictionary of chemical formulas

See it saved at tests/data/.

"""
import argparse
import collections
import itertools
import json
import operator as op
import re
import urllib2

import mako.template as mt

import chemical_elements as E
import wikipedia_compound_filter

DEFAULT_URL = "http://en.wikipedia.org/wiki/Dictionary_of_chemical_formulas"
TEMPLATE_REGISTRY = "templates/registry.mako"
WIKI_USER_AGENT = "urllib"


def main():
    args = parse_cmdline()
    if args.saved_html:
        fh = open(args.saved_html)
    else:
        fh = wiki_urlopen(DEFAULT_URL)

    table = parse_html(fh.read().decode("utf-8"))
    formulas = list(iter_formulas(table))

    if args.filter:
        formulas = filter(args.filter, formulas)

    if args.atoms:
        formulas = filter_by_atoms(formulas, args.atoms)

    if args.only_simple:
        formulas = filter(is_simple, formulas)

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
    output.add_argument("-c", "--class", dest="output",
                        action="store_const", const=dump_java,
                        help="Parsed formulas")
    output.add_argument("-s", "--stats", dest="output",
                        action="store_const", const=dump_stats,
                        help="Formula terminals frequency")

    mode = parser.add_mutually_exclusive_group()
    mode.add_argument("-A", "--all", dest="filter",
                      action="store_const", const=None,
                      help="Only ions")
    mode.add_argument("-C", "--compounds", dest="filter",
                      action="store_const", const=is_compound,
                      help="Only compounds")
    mode.add_argument("-I", "--ions", dest="filter",
                      action="store_const", const=is_ion,
                      help="Only ions")

    parser.add_argument("-atom", default=[], action="append",
                        choices=E.ATOMS,
                        help="Limit formulas to those containg"
                        " specified elements")

    parser.add_argument("-group", default=[], action="append", type=int,
                        choices=range(1, len(E.GROUPS) + 1),
                        help="Limit formulas to those containg elements"
                        " of specified groups")

    parser.add_argument("--only-simple", default=False, action="store_true",
                        help="Limit formula to simple ones")

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
    return "\n".join("%s: %s" % (f.text, " ".join(f.terms)) for f in formulas)


def dump_json(formulas):
    return json.dumps(dict(formulas))


def dump_stats(formulas):
    stats = get_term_stats(formulas)
    stats.sort(key=op.itemgetter(1))
    return "\n".join("%8i %s" % (count, term) for (term, count) in stats)


def dump_java(formulas):
    stats = get_term_stats(formulas)
    stats.sort(key=op.itemgetter(0))
    template = mt.Template(filename=TEMPLATE_REGISTRY)
    s = template.render(name="KnownFormulas", formulas=formulas, stats=stats)
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


def iter_formulas(table):
    Formula = collections.namedtuple("Formula", "text terms")
    text_formulas = (sanitize_formula(row[0].data) for row in table)
    return (Formula(it, parse_formula(it)) for it in text_formulas if it)


def sanitize_formula(formula):
    """Remove spaces and translate unicode specials to latin-1"""
    TR_UNICODE = {u"·": u"*", u"−": u"-"}
    formula = "".join(TR_UNICODE.get(ch, ch) for ch in formula if ch > " ")
    if formula.endswith("(benzenediols)"):
        formula = formula[:-len("(benzenediols)")]
    return formula


def parse_formula(formula,
                  term_re=re.compile(u"[A-Z][a-z]{0,2}|[0-9.+−-]+|.")):
    """Return list of formula terminals"""
    return term_re.findall(formula)


def filter_by_atoms(formulas, atoms):
    return [f for f in formulas
            if all(not is_atom(term) or term in atoms for term in f.terms)]


def is_simple(formula, max_atom_count=7):
    terms = formula.terms
    if "(" in terms or "[" in terms:
        return False
    coefs = [int(t) for t in terms if is_coefficient(t)]
    count = (len(terms) - len(coefs) * 2  # Atoms without coefficients.
             + sum(coefs))
    return count <= max_atom_count


def is_compound(formula):
    return not is_ion(formula)


def is_ion(formula):
    return formula.text[-1] in u"+−-"


def is_atom(term):
    return "A" <= term[0] <= "Z"


def is_coefficient(term):
    return term.isdigit()


if __name__ == "__main__":
    main()
