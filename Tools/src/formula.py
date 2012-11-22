# -*- coding: utf-8 -*-
"""Classes and functions to work with formulas and terminals"""

import collections
import re


class Formula(collections.namedtuple("Formula", "text terms")):

    RE_PLAIN = re.compile(u"[A-Z][a-z]{0,2}|[0-9.+-]+|.")

    @classmethod
    def parse_plain(cls, text):
        return cls.create(text, cls.RE_PLAIN.findall(text))

    @classmethod
    def create(cls, text, terms):
        terms = tuple(Terminal(t) for t in terms if t)
        return cls(text, terms)

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

    def atom_count(self):
        coefs = self.coefficients
        return len(self) - 2 * len(coefs) + sum(coefs)

    def element_count(self):
        return len(set(self.atoms))

    def __contains__(self, term):
        return term in self.terms

    def __len__(self):
        return len(self.terms)

    def __iter__(self):
        return iter(self.terms)


class Terminal(object):
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


def sanitize(formula):
    """Remove spaces and translate unicode specials to latin-1"""
    TR_UNICODE = {u"·": u"*", u"−": u"-"}
    return "".join(TR_UNICODE.get(ch, ch) for ch in formula if ch > " ")


def parse_formulas(parser, text_formulas):
    return [parser(sanitize(s)) for s in text_formulas
            if s and not s.isspace()]
