import unittest

import html_table_filter


EMPTY_ROW = "<table><tr></tr></table>"

EMPTY_ROW_WITH_NEW_LINES = """
<table>
<tr>
</tr>
</table>
"""

SIMPLE_TABLE = """
<html>
 <table>
  <tr>
   <td>value1</td>
   <td>value2</td>
  </tr>
  <tr>
   <td>value3</td>
   <td>value4</td>
  </tr>
 </table>
</html>
"""

SEVERAL_TABLES = """
<html>
 <table class="cls1">
  <tr>
   <td>value1</td>
   <td>value2</td>
  </tr>
 </table>
 <table class="cls2">
  <tr>
   <td>value3</td>
   <td>value4</td>
  </tr>
 </table>
 <table class="cls3">
  <tr>
   <td>value5</td>
   <td>value6</td>
  </tr>
 </table>
</html>
"""

ONE_CELL_TABLE = "<table><tr><td>value</td></tr></table>"

TABLE_WITH_INNER_HTML = ("<table><tr><td>"
                         "before"
                         "<font color='red'>value</font>"
                         "after"
                         "</td></tr></table>")

WITH_STARTEND_TAG = "<table><tr><td>a<br/>b</td></tr></table>"


def get_table_as_list(html, parser=None):
    return [[cell.data for cell in row] for row in get_table(html, parser)]


def get_table(html, parser=None):
    parser = parser or html_table_filter.TableFilter()
    parser.feed(html)
    parser.close()
    return parser.get_table()


class BrFilter(html_table_filter.TableFilter):

    def startendtag_in_cell(self, tag, attrs):
        if tag == "br":
            self._col_added.add_data("\n")


class Value2Filter(html_table_filter.TableFilter):

    def row_finished(self, row):
        return "value2" in [col.data for col in row]


class WithCustomCell(html_table_filter.TableFilter):

    def __init__(self):
        html_table_filter.TableFilter.__init__(self)
        self._create_cell_calls = 0

    def get_create_cell_calls(self):
        return self._create_cell_calls

    def create_cell(self):
        self._create_cell_calls += 1
        return html_table_filter.TableFilter.create_cell(self)


class Cell(html_table_filter.Cell):

    def __init__(self):
        super(Cell, self).__init__()
        self.font_color = None
        self.font_closed = False
        self.font_data = None


class OddClsFilter(html_table_filter.TableFilter):

    def start_table(self, attrs):
        return dict(attrs)["class"] != "cls2"


class InnerHtml(html_table_filter.TableFilter):

    def __init__(self):
        html_table_filter.TableFilter.__init__(self)
        self._in_font = False

    def create_cell(self):
        return Cell()

    def starttag_in_cell(self, tag, attrs):
        if tag == "font":
            self._col_added.font_color = dict(attrs)["color"]
            self._in_font = True

    def endtag_in_cell(self, tag):
        if tag == "font":
            self._col_added.font_closed = True
            self._in_font = False

    def data_in_cell(self, data):
        if self._in_font:
            self._col_added.font_data = data
        else:
            html_table_filter.TableFilter.data_in_cell(self, data)


class TestHTMLTableFilter(unittest.TestCase):

    def testEmptyFeed(self):
        self.assertEquals(get_table(""), None)

    def testNoTable(self):
        self.assertEquals(get_table("<html></html>"), None)
        self.assertEquals(get_table("<title>title</title>"), None)

    def testEmptyTable(self):
        self.assertEquals(get_table("<table></table>"), [])
        self.assertEquals(get_table("<table>garbage</table>"), [])

    def testEmptyRow(self):
        table = get_table_as_list(EMPTY_ROW)
        self.assertEquals(table, [[]])
        table = get_table_as_list(EMPTY_ROW_WITH_NEW_LINES)
        self.assertEquals(table, [[]])

    def testSimpleTable(self):
        self.assertEquals(get_table_as_list(SIMPLE_TABLE),
                          [["value1", "value2"],
                           ["value3", "value4"]])

    def testResultIsValidCell(self):
        table = get_table(ONE_CELL_TABLE)
        self.assertEquals(len(table), 1)
        self.assertEquals(len(table[0]), 1)
        self.assertEquals(table[0][0].data, "value")

    def testOnlyRowContainingValue2Passed(self):
        table = get_table(SIMPLE_TABLE, Value2Filter())
        self.assertEquals(len(table), 1)
        self.assertEquals(table[0][1].data, "value2")

    def testCustomCellCreated(self):
        filter_with_custom_cells = WithCustomCell()
        get_table(SIMPLE_TABLE, filter_with_custom_cells)
        self.assertEquals(filter_with_custom_cells.get_create_cell_calls(), 4)

    def testInternalHtmlHandled(self):
        filter_handling_inner_html = InnerHtml()
        table = get_table(TABLE_WITH_INNER_HTML, filter_handling_inner_html)
        self.assertEquals(len(table), 1)
        self.assertEquals(len(table[0]), 1)
        cell = table[0][0]
        self.assertEquals(cell.font_color, "red")
        self.assertTrue(cell.font_closed)
        self.assertEquals(cell.data, "beforeafter")
        self.assertEquals(cell.font_data, "value")

    def testSeveralTablesAreJoined(self):
        table = get_table_as_list(SEVERAL_TABLES)
        self.assertEquals(table, [["value1", "value2"],
                                  ["value3", "value4"],
                                  ["value5", "value6"]])

    def testTablesAreFiltered(self):
        odd_cls_filter = OddClsFilter()
        table = get_table_as_list(SEVERAL_TABLES, odd_cls_filter)
        self.assertEquals(table, [["value1", "value2"],
                                  ["value5", "value6"]])

    def testStartEndTagIsHandled(self):
        br_filter = BrFilter()
        table = get_table_as_list(WITH_STARTEND_TAG, br_filter)
        self.assertEquals(table, [["a\nb"]])


if __name__ == "__main__":
    unittest.main()
