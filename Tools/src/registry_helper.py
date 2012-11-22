"""Code for FormulaRegistry template"""

import collections
import itertools


def split_formula_set(formulas):
    """Split all formulas into groups to pass java method code limit 64K

    Group formulas by first letter.

    """
    def group_key(formula):
        s = formula.text
        return s[1] if s[0] in "([" else s[0]

    formulas = sorted(formulas, key=lambda f: (group_key(f), f.text))
    for key, grp in itertools.groupby(formulas, key=group_key):
        yield key, tuple(grp)


def iter_prefixes(formula, start=1, end=0):
    """All formula prefixes as strings"""
    return map(formula.prefix, range(start, len(formula) + end))


def gen_prefixes(formulas):
    Item = collections.namedtuple("Item", "prefix setters")

    prefixes = collections.defaultdict(set)
    for f in formulas:
        prefixes[f.text] = set()
    for f in formulas:
        for p in iter_prefixes(f, start=2):
            prefixes[p].add(".startsCompound(true)")
        if f.is_final():
            prefixes[f.text].add(".isFinal(true)")

    prefixes = [Item(p, sorted(s)) for p, s in prefixes.iteritems()]
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
