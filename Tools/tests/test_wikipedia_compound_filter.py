import unittest
import wikipedia_compound_parser


def parse_html(html):
    parser = wikipedia_compound_parser.TableFilter()
    parser.feed(html)
    parser.close()
    return parser.get_table()


class TestWikipediaCompoundParser(unittest.TestCase):

    def testName(self):
        pass


if __name__ == "__main__":
    unittest.main()