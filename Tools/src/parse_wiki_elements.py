"""Define valid chemical elements and groups"""

import collections

import html_table_filter


Element = collections.namedtuple("Element",
     "symbol number name group period weight category state_of_matter")


class Element(Element):
    def __str__(self):
        return self.symbol


Row = collections.namedtuple("Row", "z symbol name origin group period weight"
    " density melt boil heat neg abudance")


def get_elements():
    fh = open("tests/data/list_of_elements.html")
    table = [Row(*[c.data for c in r])
             for r in parse_html(fh.read().decode("utf-8"))
             if len(r) == len(Row._fields)]
    return map(row2element, table)


def parse_html(html):
    parser = html_table_filter.TableFilter()
    parser.feed(html)
    parser.close()
    return parser.get_table()


def row2element(r):
    n = int(r.z)
    g = int(r.group) if r.group else 3

    w = r.weight.strip()
    if "!" in w:
        w = w.split("!", 1)[1]
    if w.startswith("["):
        w = w[1:].split("]", 1)[0]
    w = float(w.split("(", 1)[0])

    return Element(str(r.symbol), n, str(r.name), g, int(r.period), w,
                   category(r.symbol, n), state_of_matter(r.symbol, n))


def category(symbol, number):
    if symbol in ("Li", "Na", "K", "Rb", "Cs", "Fr"):
        return "AlkaliMetal"
    if symbol in ("Be", "Mg", "Ca", "Sr", "Ba", "Ra"):
        return "AlkalineMetal"
    if 57 <= number <= 71:
        return "Lanthanoid"
    if 89 <= number <= 103:
        return "Actinoid"
    if symbol in ("Al", "Ga", "In", "Sn", "Tl", "Pb", "Bi"):
        return "PostTransitionMetal"
    if symbol in ("B", "Si", "Ge", "As", "Sb", "Te"):
        return "Metalloid"
    if symbol in ("H", "C", "N", "O", "P", "S", "Se"):
        return "OtherNonMetal"
    if symbol in ("F", "Cl", "Br", "I", "At"):
        return "Halogen"
    if symbol in ("He", "Ne", "Ar", "Kr", "Xe", "Rn"):
        return "NobleGas"
    if number <= 112 and symbol not in ("Mt", "Ds", "Rg"):
        return "TransitionMetal"
    return "Undefined"


def state_of_matter(number, symbol):
    if symbol in ("H", "He", "N", "O", "F", "Ne", "Cl", "Ar", "Kr", "Rn"):
        return "Gas"
    if symbol in ("Br", "Hg"):
        return "Liquid"
    if number < 104:
        return "Solid"
    return "Undefined"


def main():
    for e in get_elements():
        print repr(e)

if __name__ == "__main__":
    main()
