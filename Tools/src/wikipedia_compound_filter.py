import html_table_filter
import collections

# Character to separate ion's charge from the ion's formula
CHARGE_SEPARATOR = "'"

Synonym = collections.namedtuple("Synonym", "synonym cas_number link")


class Cell(html_table_filter.Cell):

    def __init__(self):
        html_table_filter.Cell.__init__(self)
        self.synonyms = []

    def add_synonym(self, synonym, cas_number, link):
        self.synonyms.append(Synonym(synonym, cas_number, link))


class TempCell(html_table_filter.Cell):

    def __init__(self):
        self._data = None
        html_table_filter.Cell.__init__(self)
        self.link = None
        self.synonyms = []

    def get_data(self):
        return self._data if self._data is None else self._data.strip()

    def set_data(self, data):
        self._data = data

    data = property(get_data, set_data)

    def set_link(self, link):
        self.link = link

    def add_synonym(self):
        self.synonyms.append((self.data, self.link))
        self.data = ""
        self.link = None


class TableFilter(html_table_filter.TableFilter):

    def __init__(self):
        html_table_filter.TableFilter.__init__(self)
        self._rowspan_count = 0
        self._rowspan_cell = None

    def start_table(self, attrs):
        return dict(attrs).get("class") == "wikitable"

    def create_cell(self, attrs):
        if not self._last_row and self._rowspan_count == 0:
            try:
                self._rowspan_count = int(dict(attrs).get("rowspan"))
            except (ValueError, TypeError):
                pass
        return TempCell()

    def starttag_in_cell(self, tag, attrs):
        attrs = dict(attrs)
        if attrs.get("class") != "new":
            self._col_added.set_link(attrs.get("href"))
	if tag == "sup":
	    self._col_added.add_data(CHARGE_SEPARATOR)

    def startendtag_in_cell(self, tag, attrs):
        if tag == "br":
            self._col_added.add_synonym()

    def row_finished(self, row):
        if len(row) == 3:
            cell = Cell()
            if self._rowspan_count:
                self._rowspan_cell = cell
            cell.add_data(row[0].data)
            for synonym, link in row[1].synonyms:
                cell.add_synonym(synonym, row[2].data, link)
            cell.add_synonym(row[1].data, row[2].data, row[1].link)
            row[:] = [cell]
            if self._rowspan_count:
                self._rowspan_count -= 1
            return True
        elif len(row) == 2:
            cell = self._rowspan_cell
            cell.add_synonym(row[0].data, row[1].data, row[0].link)
            if self._rowspan_count:
                self._rowspan_count -= 1
            return False

    def get_ions_table(self):
        return [row for row in self.get_table()
                if row[0].synonyms[0].synonym.endswith(" ion")]
