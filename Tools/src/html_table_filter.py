import HTMLParser


class Cell(object):

    def __init__(self):
        self.data = ""

    def add_data(self, data):
        self.data += data


class TableFilter(HTMLParser.HTMLParser):

    def __init__(self):
        HTMLParser.HTMLParser.__init__(self)
        self._table = None
        self._last_row = None
        self._col_added  = None

    def row_finished(self, row):
        return True

    def create_cell(self):
        return Cell()

    def starttag_in_cell(self, tag, attrs):
        pass

    def endtag_in_cell(self, tag):
        pass

    def data_in_cell(self, data):
        self._col_added.add_data(data)

    def get_table(self):
        return self._table

    def handle_starttag(self, tag, attrs):
        if tag == "table":
            self._table = []
        elif tag == "tr":
            self._last_row = []
        elif tag == "td":
            col = self._col_added = self.create_cell()
            self._last_row.append(col)
        elif self._col_added is not None:
            self.starttag_in_cell(tag, attrs)

    def handle_endtag(self, tag):
        if tag == "tr":
            if self.row_finished(self._last_row):
                self._table.append(self._last_row)
            self._last_row = None
        elif tag == "td":
            self._col_added = None
        elif self._col_added is not None:
            self.endtag_in_cell(tag)

    def handle_data(self, data):
        if self._col_added is not None:
            self.data_in_cell(data)
