# -*- coding: utf-8 -*-
"""A tool to parse wikipedia's dictionary of chemical formulas

See it saved at tests/data/.

"""
import argparse
import collections
import itertools
import json
import operator as op
import os
import re
import urllib2

import mako.template as mt

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

    if args.filter:
        table = filter(lambda row: args.filter(row[0].data), table)
    output = args.output(table)

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

    parser.set_defaults(output=dump_text, filter=None)
    return parser.parse_args()


def wiki_urlopen(url):
    return urllib2.urlopen(
        urllib2.Request(url, headers={"User-agent": WIKI_USER_AGENT}))


def parse_html(html):
    parser = wikipedia_compound_filter.TableFilter()
    parser.feed(html)
    parser.close()
    return parser.get_table()


def dump_text(table):
    return "\n".join("%s: %s" % (formula, " ".join(parse_formula(formula)))
                     for formula in iter_text_formulas(table))


def dump_json(table):
    return json.dumps(dict((formula, parse_formula(formula))
                           for formula in iter_text_formulas(table)))


def dump_stats(table):
    stats = get_term_stats(iter_text_formulas(table))
    stats.sort(key=op.itemgetter(1))
    return "\n".join("%8i %s" % (count, term) for (term, count) in stats)


def dump_java(table):
    Formula = collections.namedtuple("Formula", "text terms")
    formulas = [Formula(item, parse_formula(item))
                for item in iter_text_formulas(table)]
    stats = get_term_stats(iter_text_formulas(table))
    stats.sort(key=op.itemgetter(0))
    template = mt.Template(filename=TEMPLATE_REGISTRY)
    s = template.render(name="KnownFormulas", formulas=formulas, stats=stats)
    s = re.sub("^\s+$", "", s, flags=re.M)
    return s


def get_term_stats(formulas):
    Stat = collections.namedtuple("Stat", "term count")
    terms = []
    for item in formulas:
        terms.extend(parse_formula(item))
    terms.sort()
    stats = [Stat(term, len(list(group)))
             for (term, group) in itertools.groupby(terms)]
    return stats


def iter_text_formulas(table):
    return filter(None, (sanitize_formula(row[0].data) for row in table))


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


def is_compound(formula):
    return not is_ion(formula)


def is_ion(formula):
    return formula[-1] in u"+−-"


if __name__ == "__main__":
    main()
