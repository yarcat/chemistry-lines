# -*- coding: utf-8 -*-
"""Classes and functions to work with formulas and terminals"""

import re


class TextHelper(object):
    def __str__(self):
        return self.text

    def __repr__(self):
        return "%s(%r)" % (type(self).__name__, self.text)

    def __eq__(self, other):
        val = other if isinstance(other, basestring) else other.text
        return self.text == val

    def __lt__(self, other):
        val = other if isinstance(other, basestring) else other.text
        return self.text < val

    def __hash__(self):
        return hash(self.text)


class Formula(TextHelper):
    def __init__(self, text):
        self.text = text
        self.terms = tuple(Terminal(t) for t in parse_formula(self.text))

    @property
    def is_compound(self):
        return not self.is_ion

    @property
    def is_ion(self):
        return self.text[-1] in u"+-"

    @property
    def atoms(self):
        return [t for t in self.terms if t.is_atom]

    @property
    def coefficients(self):
        return [int(t.text) for t in self.terms if t.is_coefficient]

    def prefix(self, size=None, sep=""):
        """Return string prefix of first size terminals separated with sep

        Default size is all formula.

        """
        return sep.join(map(str, self.terms[:size]))

    def __contains__(self, term):
        return term in self.terms

    def __len__(self):
        return len(self.terms)

    def __iter__(self):
        return iter(self.terms)


class Terminal(TextHelper):
    def __init__(self, text):
        self.text = text

    @property
    def is_atom(self):
        return self.text[0].isupper()

    @property
    def is_coefficient(self):
        return self.text.isdigit()

    @property
    def starts_formula(self):
        return self.is_atom or self.text in "(["


def parse_formula(formula,
                  term_re=re.compile(u"[A-Z][a-z]{0,2}|[0-9.+-]+|.")):
    """Return list of formula terminals"""
    return term_re.findall(formula)


def sanitize(formula):
    """Remove spaces and translate unicode specials to latin-1"""
    TR_UNICODE = {u"·": u"*", u"−": u"-"}
    return "".join(TR_UNICODE.get(ch, ch) for ch in formula if ch > " ")


def iter_formulas(text_formulas):
    return (Formula(sanitize(s)) for s in text_formulas
            if s and not s.isspace())
