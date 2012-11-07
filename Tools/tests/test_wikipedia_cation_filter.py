import html_table_filter
import os
import unittest
import wikipedia_cation_filter


RESOURCE_FILE = os.path.join(os.path.dirname(__file__),
                             "data", "cations.html")
HTML = open(RESOURCE_FILE).read()


SIMPLE_TABLE = """
<table class="some other class"><tr><td></td></tr></table>
<table class="wikitable">
<caption>Common <b>Cations</b></caption>
<tr>
<th style="text-align: left">Common Name</th>
<th style="text-align: left">Formula</th>
<th style="text-align: left">Historic Name</th>
</tr>
<tr>
<th colspan="3" style="background:aliceblue;"><i>Simple Cations</i></th>
</tr>
<tr>
<td><a href="/wiki/Aluminium" title="Aluminium">Aluminium</a></td>
<td>Al<sup>3+</sup></td>
<td></td>
</tr>
</table>
"""


def parse_html(html, parser=None):
    parser = parser or wikipedia_cation_filter.TableFilter()
    parser.feed(html)
    parser.close()
    return parser.get_table()


class TestWikipediaCationFilter(unittest.TestCase):

    def testSimpleTable(self):
        table = parse_html(SIMPLE_TABLE)
        self.assertEquals(len(table), 1)
        self.assertEquals(len(table[0]), 1)
        cell = table[0][0]
        self.assertEquals(cell.name, "Aluminium")
        self.assertEquals(cell.data, "Al")
        self.assertEquals(cell.charge, "3+")

    def testWikitablesReadCorrectly(self):
        table_filter = html_table_filter.InnerTableFilter(
            wikipedia_cation_filter.TableFilter())
        table = parse_html(HTML, table_filter)
        self.assertEquals(len(table), 28)


if __name__ == "__main__":
    unittest.main()
