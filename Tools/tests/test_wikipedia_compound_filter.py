import os
import unittest
import wikipedia_compound_filter


RESOURCE_FILE = os.path.join(os.path.dirname(__file__),
                             "data", "dictionary_of_chemical_formulas.html")
HTML = open(RESOURCE_FILE).read()


def parse_html(html=HTML, parser=None):
    parser = parser or wikipedia_compound_filter.TableFilter()
    parser.feed(html)
    parser.close()
    return parser.get_table()


class TestWikipediaCompoundParser(unittest.TestCase):

    def testNoHeaders(self):
        table = parse_html()
        self.assertTrue(all(len(row) in (2, 3) for row in table))


if __name__ == "__main__":
    unittest.main()