import html_table_filter


class Cell(html_table_filter.Cell):

    def __init__(self):
        super(Cell, self).__init__()
        self.charge = None
        self.color = None

    def set_charge(self, charge):
        self.charge = charge

    def set_color(self, color):
        self.color = color


class MaricopaTableFilter(html_table_filter.TableFilter):

    def __init__(self):
        html_table_filter.TableFilter.__init__(self)
        self._data_handler = None

    def row_finished(self, row):
        return len(row) == 10

    def create_cell(self):
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