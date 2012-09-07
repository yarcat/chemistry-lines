import maricopa_ion_parser
import os
import re
import unittest


RESOURCE_FILE = os.path.join(os.path.dirname(__file__),
                             "data", "polyatomicion.html")
HTML = open(RESOURCE_FILE).read()

ONE_DEFINITION = """
<table><tr>
<td><font color = "#00008B">H<SUB>2</SUB>PO<SUB>3</SUB><SUP>-</SUP></font></td>
<td><font color = "#00008B">dihydrogen phosphite</font></td>
</tr></table>"""

ONE_DEFINITION_COLOR_MISMATCH = """
<table><tr>
<td><font color = "#00008B">H<SUB>2</SUB>PO<SUB>3</SUB><SUP>-</SUP></font></td>
<td>dihydrogen phosphite</td>
</tr></table>"""


class IgnoreRowLength(maricopa_ion_parser.MaricopaTableFilter):

    EXPECTED_ROW_LENGTH = 2


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
        expected_row_len = 10 / 2 # Description's merged into elements.
        self.assertEquals(map(len, table), [expected_row_len] * expected_rows)

    def testExpectedFirstRow(self):
        table = parse_html(ONE_DEFINITION, IgnoreRowLength())
        self.assertEquals(len(table[0]), 1) # Description's merged into element.
        cell = table[0][0]
        self.assertEquals(cell.data, "H2PO3")
        self.assertEquals(cell.charge, "-")
        self.assertEquals(cell.color, "#00008B")
        self.assertEquals(cell.description, "dihydrogen phosphite")

    def testColorMismatchRaises(self):
        self.assertRaises(AssertionError, parse_html,
                          ONE_DEFINITION_COLOR_MISMATCH, IgnoreRowLength())


if __name__ == "__main__":
    unittest.main()
