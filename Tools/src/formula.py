# -*- coding: utf-8 -*-
"""Classes and functions to work with formulas and terminals"""

import collections
import re

import chemical_elements


class Lexems(object):
    RE_PLAIN = re.compile(u"[A-Z][a-z]{0,2}|[0-9.+-]+|.")

    @classmethod
    def plain(cls, text):
        return cls.RE_PLAIN.findall(cls.tr_brackets(text))

    RE_COMPACT = \
        re.compile(u"[A-Z][a-z]{0,2}[0-9]*|[*)][0-9x]*|'[0-9.]*[+-]+|[^)]")

    @classmethod
    def compact(cls, text):
        return cls.RE_COMPACT.findall(cls.tr_brackets(text))

    @staticmethod
    def tr_brackets(text):
        return text.replace("[", "(").replace("]", ")")


class Formula(collections.namedtuple("Formula", "text terms")):

    # TODO(luch) rename parse_plain -> parse or parse_asis
    @classmethod
    def parse_plain(cls, text, lex=Lexems.plain):
        return cls.create(text, lex(text))

    @classmethod
    def parse_closing_brackets(cls, text, lex=Lexems.plain):
        """Plain-parsed formula without opening bracket terminals"""
        terms = (t for t in lex(text) if t != "(")
        return cls.create(text, terms)

    @classmethod
    def parse_pair_brackets(cls, text, lex=Lexems.plain):
        terms = ("()%s" % t[1:] if t[0] == ")" else t for t in lex(text)
                 if t != "(")
        return cls.create(text, terms)

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
        return filter(None, [t.atom for t in self.terms])

    @property
    def coefficients(self):
        return [t.coefficient for t in self.terms]

    def prefix(self, size=None, sep=""):
        """Return string prefix of first size terminals separated with sep

        Default size is all formula.

        """
        return sep.join(map(str, self.terms[:size]))

    def atom_count(self):
        """Atom count for formulas without brackets"""
        return sum(self.coefficients) - len([t for t in self if not t.atom])

    def element_count(self):
        return len(set(self.atoms))

    def __contains__(self, term):
        return term in self.terms

    def __len__(self):
        return len(self.terms)

    def __iter__(self):
        return iter(self.terms)

    def __eq__(self, other):
        return self.terms == other.terms

    def __lt__(self, other):
        return self.terms < other.terms


class Terminal(object):

    def __init__(self, text):
        self.text = text

    RE_ATOM = re.compile("[A-Z][a-z]{0,2}")

    @property
    def atom(self):
        match = self.RE_ATOM.match(self.text)
        return match and match.group()
    # ATOMS = frozenset(chemical_elements.ATOMS)
    # @property
    # def atom(self):
    #     t = self.text
    #     if t in ("T", "T2"):
    #         # Handle Tritium explicitly.  See #47.
    #         return "T"
    #     elif t[0].isupper():
    #         for prefix_len in None, 3, 2, 1:
    #             prefix = t[:prefix_len] if prefix_len else t
    #             if prefix in self.ATOMS:
    #                 return prefix
    #
    #     return None

    @property
    def coefficient(self):
        atom = self.atom
        if atom:
            if atom == self.text:
                return 1
            else:
                return int(self.text[len(atom):])
        elif self.text == "0.5":
            return 0
        elif self.text[-1].isdigit():
            for suffix_len in 3, 2, 1:
                suffix = self.text[-suffix_len:]
                if suffix.isdigit():
                    return int(suffix)
        return 0

    @property
    def starts_formula(self):
        return self.atom or self.text == "("

    @property
    def category(self):
        return chemical_elements.category(self.atom)

    @property
    def state_of_matter(self):
        return chemical_elements.state_of_matter(self.atom)

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


def parse_formulas(text_formulas, parser, lex=Lexems.plain):
    return [parser(sanitize(s), lex) for s in text_formulas
            if s and not s.isspace()]
