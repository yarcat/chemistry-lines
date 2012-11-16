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

    table = parse_html(fh.read().decode("utf-8"))
    formulas = list(F.iter_formulas(row[0].data for row in table
                    if row[0].data != "(benzenediols)"))

    if args.filter:
        formulas = filter(args.filter, formulas)

    if args.atoms:
        formulas = filter_by_atoms(formulas, args.atoms)

    if args.only_simple:
        formulas = filter(is_simple, formulas)

    if args.min_elements:
        formulas = filter(lambda f: len(f.atoms) >= args.min_elements,
                          formulas)
    if args.min_terminals:
        formulas = filter(lambda f: len(f) >= args.min_terminals, formulas)

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

    parser.add_argument("--only-simple", default=False, action="store_true",
                        help="Limit formulas to simple ones")

    limit = parser.add_mutually_exclusive_group()
    limit.add_argument("--min-terminals", default=None, type=int,
                       help="Limit formulas by terminal count")
    limit.add_argument("--min-elements", default=None, type=int,
                       help="Limit formulas by element count"
                       " (different atoms)")

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
    return "\n".join("%s: %s" % (f.text, f.prefix(sep=" ")) for f in formulas)


def dump_json(formulas):
    return json.dumps(dict((f.text, map(str, f.terms)) for f in formulas))


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


def is_simple(formula, max_atom_count=7):
    if "(" in formula or "[" in formula:
        return False

    coefs = formula.coefficients

    # Count atoms without coefficients & add all coefficients
    count = len(formula) - 2 * len(coefs) + sum(coefs)
    return count <= max_atom_count


if __name__ == "__main__":
    main()
