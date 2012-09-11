import unittest
import wikipedia_cation_filter


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


def parse_html(html):
    parser = wikipedia_cation_filter.TableFilter()
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


if __name__ == "__main__":
    unittest.main()