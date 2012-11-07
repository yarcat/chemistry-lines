# -*- coding: utf-8 -*-
"""A tool to parse wikipedia's dictionary of chemical formulas

See it saved at tests/data/.

"""
import argparse
import itertools
import json
from operator import itemgetter
import re
import urllib2

import wikipedia_compound_filter

urllib2.install_opener(urllib2.build_opener(urllib2.ProxyHandler,
                                            urllib2.HTTPHandler,
                                            urllib2.HTTPRedirectHandler,
                                            urllib2.HTTPDefaultErrorHandler,
                                            urllib2.UnknownHandler))

DEFAULT_URL = "http://en.wikipedia.org/wiki/Dictionary_of_chemical_formulas"


def main():
    args = cmdline()
    if args.saved_html:
        fh = open(args.saved_html)
    else:
        try:
            fh = urllib2.urlopen(DEFAULT_URL)
        except urllib2.HTTPError, _err:
            print _err, dir(_err)
            print _err.geturl()
            print _err.headers
            return

    table = parse_html(fh.read().decode("utf-8"))

    if args.filter:
        table = filter(lambda row: args.filter(row[0].data), table)
    output = args.output(table)

    print output.encode("utf-8")


def cmdline():
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

    parser.set_defaults(output=dump_text, filter=None)
    return parser.parse_args()


def parse_html(html, parser=None, ions=False):
    parser = wikipedia_compound_filter.TableFilter()
    parser.feed(html)
    parser.close()
    return parser.get_table()


def dump_text(table):
    return "\n".join("%s: %s" % (formula, " ".join(parse_formula(formula)))
                     for formula in text_formulas(table))


def dump_json(table):
    return json.dumps(dict((formula, parse_formula(formula))
                           for formula in text_formulas(table)))


def dump_stats(table):
    stats = get_term_stats(text_formulas(table))
    return "\n".join("%8i %s" % (count, term) for (term, count) in stats)


def get_term_stats(formulas):
    terms = []
    for item in formulas:
        terms.extend(parse_formula(item))
    terms.sort()
    stats = [(term, len(list(group)))
             for (term, group) in itertools.groupby(terms)]
    stats.sort(key=itemgetter(1))
    return stats


def text_formulas(table):
    """Iterate over textual formulas"""
    return (tr_formula(row[0].data) for row in table)


TR_UNICODE = {u"·": u"*", u"−": u"-"}


def tr_formula(formula):
    """Remove spaces and translate unicode specials to latin-1"""
    formula = "".join(TR_UNICODE.get(ch, ch) for ch in formula if ch > " ")
    if formula.endswith("(benzenediols)"):
        formula = formula[:-len("(benzenediols)")]
    return formula


term_re = re.compile(u"[A-Z][a-z]{0,2}|[0-9.+−-]+|.")


def parse_formula(formula):
    """Return list of formula terminals"""
    return term_re.findall(formula)


def is_compound(formula):
    """Check whether the formula is a compound"""
    return not is_ion(formula)


def is_ion(formula):
    """Check whether the formula is an ion"""
    return formula[-1] in u"+−-"


if __name__ == "__main__":
    main()
