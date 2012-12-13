"""Define valid chemical elements and groups"""
import itertools

import parse_wiki_elements


ELEMENT_LIST = parse_wiki_elements.get_elements()

ELEMENTS = dict((e.symbol, e) for e in ELEMENT_LIST)

SYMBOLS = tuple(ELEMENTS)

PERIODS = tuple(tuple(grp) for p, grp in
                itertools.groupby(ELEMENT_LIST, key=lambda e:e.period))

def tritium():
    d = ELEMENTS["H"]._asdict()
    d.symbol = "T"
    return parse_wiki_elements.Element(**d)


TRITIUM = tritium()

