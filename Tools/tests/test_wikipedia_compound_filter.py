import os
import unittest
import wikipedia_compound_filter


RESOURCE_FILE = os.path.join(os.path.dirname(__file__),
                             "data", "dictionary_of_chemical_formulas.html")
HTML = open(RESOURCE_FILE).read()


SIMPLE_WIKITABLE = """
<table><tr><td>1</td><td>1</td><td>1</td></tr></table>
<table class="wikitable"><tr><td>a</td><td>b</td><td>c</td></tr></table>
"""

def parse_html_as_list(html, parser=None):
    return [[col.data for col in row] for row in parse_html(html, parser)]


def parse_html(html=HTML, parser=None):
    parser = parser or wikipedia_compound_filter.TableFilter()
    parser.feed(html)
    parser.close()
    return parser.get_table()


class TestWikipediaCompoundParser(unittest.TestCase):

    def testSimpleWikitable(self):
        table = parse_html(SIMPLE_WIKITABLE)
        self.assertEquals(len(table), 1)
        row = table[0]
        self.assertEquals(len(row), 1)
        cell = row[0]
        self.assertEquals(cell.data, "a")
        self.assertEquals(cell.synonyms, [("b", "c")])


if __name__ == "__main__":
    unittest.main()