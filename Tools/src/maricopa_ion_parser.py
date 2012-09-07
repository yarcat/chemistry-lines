import html_table_filter


class Cell(html_table_filter.Cell):

    def __init__(self):
        super(Cell, self).__init__()
        self.charge = None
        self.color = None
        self.description = None

    def set_charge(self, charge):
        self.charge = charge

    def set_color(self, color):
        self.color = color

    def set_description(self, description):
        self.description = description


class MaricopaTableFilter(html_table_filter.TableFilter):

    EXPECTED_ROW_LENGTH = 10

    def __init__(self):
        html_table_filter.TableFilter.__init__(self)
        self._data_handler = None

    def row_finished(self, row):
        if len(row) != self.EXPECTED_ROW_LENGTH:
            return False
        result_rows = []
        for element, description in zip(row[::2], row[1::2]):
            if element.color != description.color:
                raise AssertionError("Color mismatch %r != %r" %
                                     (element.color, description.color))
            element.set_description(description.data)
            result_rows.append(element)
        row[:] = result_rows
        return True

    def create_cell(self):
        self._data_handler = None
        return Cell()

    def starttag_in_cell(self, tag, attrs):
        if tag == "font":
            self._col_added.set_color(dict(attrs)["color"])
        elif tag == "sup":
            self._data_handler = self._col_added.set_charge

    def data_in_cell(self, data):
        if self._data_handler is None:
            html_table_filter.TableFilter.data_in_cell(self, data)
        else:
            self._data_handler(data)