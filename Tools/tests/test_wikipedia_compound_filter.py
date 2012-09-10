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
SYNONYMS_WITH_COMMON_CAS = """
<table class="wikitable">
 <tr>
  <td>a</td>
  <td><a href="link">b1</a><br />
b2</td>
  <td>c</td>
 </tr>
</table>
"""
SYNONYMS_WITH_DIFFERENT_CAS = """
<table class="wikitable">
 <tr>
  <td rowspan="2">a</td>
  <td><a href="link">b1</a></td>
  <td>c1</td>
 </tr>
 <tr>
  <td>b2</td>
  <td>c2</td>
 </tr>
</table>
"""


def parse_html_as_list(html, parser=None, ions=False):
    return [[col.data for col in row]
            for row in parse_html(html, parser, ions)]


def parse_html(html, parser=None, ions=False):
    parser = parser or wikipedia_compound_filter.TableFilter()
    parser.feed(html)
    parser.close()
    return parser.get_ions_table() if ions else parser.get_table()


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

    def testSynonymsWithCommonCas(self):
        table = parse_html(SYNONYMS_WITH_COMMON_CAS)
        self.assertEquals(table[0][0].data, "a")
        synonyms = table[0][0].synonyms
        self.assertEquals(len(synonyms), 2)

        for idx, (synonym, cas, link) in enumerate([("b1", "c", "link"),
                                                    ("b2", "c", None)]):
            self.assertEquals(synonyms[idx].synonym, synonym)
            self.assertEquals(synonyms[idx].cas_number, cas)
            self.assertEquals(synonyms[idx].link, link)

    def testSynonymsWithDifferentCas(self):
        table = parse_html(SYNONYMS_WITH_DIFFERENT_CAS)
        self.assertEquals(len(table), 1)
        self.assertEquals(table[0][0].data, "a")
        synonyms = table[0][0].synonyms
        self.assertEquals(len(synonyms), 2)

        for idx, (synonym, cas, link) in enumerate([("b1", "c1", "link"),
                                                    ("b2", "c2", None)]):
            self.assertEquals(synonyms[idx].synonym, synonym)
            self.assertEquals(synonyms[idx].cas_number, cas)
            self.assertEquals(synonyms[idx].link, link)

    def testWikipediaHtml(self):
        table = parse_html_as_list(HTML, ions=True)
        self.assertEquals(len(table), 49)

if __name__ == "__main__":
    unittest.main()
