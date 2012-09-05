import maricopa_ion_parser
import os
import re
import unittest


RESOURCE_FILE = os.path.join(os.path.dirname(__file__),
                             "data", "polyatomicion.html")
HTML = open(RESOURCE_FILE).read()

ONE_CELL = """
<table><tr>
<td><font color = "#00008B">H<SUB>2</SUB>PO<SUB>3</SUB><SUP>-</SUP></font></td>
</tr></table>"""


class IgnoreRowLength(maricopa_ion_parser.MaricopaTableFilter):

    def row_finished(self, row):
        return True


def parse_html(html=HTML, parser=None):
    parser = parser or maricopa_ion_parser.MaricopaTableFilter()
    parser.feed(html)
    parser.close()
    return parser.get_table()


class TestMaricopaTableParser(unittest.TestCase):

    def testDoesntRaise(self):
        table = parse_html()
        self.assertTrue(table is not None)
        self.assertTrue(len(table) > 0)

    def testHeaderIgnored(self):
        table = parse_html()
        rows = len(re.findall("<tr>", HTML))
        expected_rows = rows - 2 # Two header lines.
        self.assertEquals(map(len, table), [10] * expected_rows)

    def testExpectedFirstRow(self):
        table = parse_html(ONE_CELL, IgnoreRowLength())
        cell = table[0][0]
        self.assertEquals(cell.data, "H2PO3")
        self.assertEquals(cell.charge, "-")
        self.assertEquals(cell.color, "#00008B")


if __name__ == "__main__":
    unittest.main()