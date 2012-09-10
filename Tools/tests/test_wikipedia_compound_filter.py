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
WIKITABLE_WITH_LINK = """
<table class="wikitable">
 <tr>
  <td>a</td>
  <td><a href="link">b</a></td>
  <td>c</td>
 </tr>
</table>
"""
WIKITABLE_WITH_NEW_LINK = """
<table class="wikitable">
 <tr>
  <td>a</td>
  <td><a href="link" class="new">b</a></td>
  <td>c</td>
 </tr>
</table>
"""
SEVERAL_SYNONYMS = """
<table class="wikitable">
 <tr>
  <td>a</td>
  <td><a href="link">b1</a><br />
b2</td>
  <td>c</td>
 </tr>
</table>
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
        self.assertEquals(len(table[0]), 1)
        cell = table[0][0]
        self.assertEquals(cell.data, "a")
        self.assertEquals(len(cell.synonyms), 1)
        synonym = cell.synonyms[0]
        self.assertEquals(synonym.synonym, "b")
        self.assertEquals(synonym.cas_number, "c")
        self.assertEquals(synonym.link, None)

    def testWikitableWithLink(self):
        table = parse_html(WIKITABLE_WITH_LINK)
        synonym = table[0][0].synonyms[0]
        self.assertEqual(synonym.link, "link")

    def testWikitableWithNewLink(self):
        table = parse_html(WIKITABLE_WITH_NEW_LINK)
        synonym = table[0][0].synonyms[0]
        self.assertEqual(synonym.link, None)

    def testSeveralSynonyms(self):
        table = parse_html(SEVERAL_SYNONYMS)
        synonyms = table[0][0].synonyms
        self.assertEquals(len(synonyms), 2)

        for idx, (synonym, cas, link) in enumerate([("b1", "c", "link"),
                                                    ("b2", "c", None)]):
            self.assertEquals(synonyms[idx].synonym, synonym)
            self.assertEquals(synonyms[idx].cas_number, cas)
            self.assertEquals(synonyms[idx].link, link)


if __name__ == "__main__":
    unittest.main()