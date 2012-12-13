"""Code for FormulaRegistry template"""

import collections
import itertools


def split_formula_set(formulas):
    """Split all formulas into groups to pass java method code limit 64K

    Group formulas by first letter.

    """
    def group_key(formula):
        return formula.terms[formula.terms[0] == "("].text[0]

    formulas = sorted(formulas, key=lambda f: (group_key(f), f.text))
    for key, grp in itertools.groupby(formulas, key=group_key):
        yield key, tuple(grp)


def iter_prefixes(formula, start=1, end=0):
    """All formula prefixes as strings"""
    return map(formula.prefix, range(start, len(formula) + end))


def gen_prefixes(formulas):
    Item = collections.namedtuple("Item", "prefix setters formula")

    prefixes = collections.defaultdict(set)
    backlink = {}
    for f in formulas:
        prefix = f.prefix()
        prefixes[prefix] = set()
        if f.is_final():
            prefixes[prefix].add(".isFinal(true)")
            prefixes[prefix].add(".atomCount(%i)" % f.atom_count())
        backlink[prefix] = f
    for f in formulas:
        for p in iter_prefixes(f, start=2):
            prefixes[p].add(".startsCompound(true)")

    prefixes = [Item(p, sorted(s), backlink.get(p))
                for p, s in prefixes.iteritems()]
    prefixes.sort(key=lambda x: x.prefix)

    return prefixes


def gen_productions(formulas):
    Prod = collections.namedtuple("Prod", "prefix term result")
    produced = set()
    for f in formulas:
        for i in range(1, len(f)):
            p = Prod(f.prefix(i), f.terms[i], f.prefix(i + 1))
            if p not in produced:
                yield p
                produced.add(p)


def collect_terms(formulas):
    terms = set()
    for f in formulas:
        terms.update(f.terms)
    return terms
