import html_table_filter
import collections


Synonym = collections.namedtuple("Synonym", "synonym cas_number link")


class Cell(html_table_filter.Cell):

    def __init__(self):
        html_table_filter.Cell.__init__(self)
        self.synonyms = []

    def add_synonym(self, synonym, cas_number, link):
        self.synonyms.append(Synonym(synonym, cas_number, link))


class TempCell(html_table_filter.Cell):

    def __init__(self):
        html_table_filter.Cell.__init__(self)
        self.link = None

    def set_link(self, link):
        self.link = link


class TableFilter(html_table_filter.TableFilter):

    def start_table(self, attrs):
        return dict(attrs).get("class") == "wikitable"

    def create_cell(self):
        return TempCell()

    def starttag_in_cell(self, tag, attrs):
        attrs = dict(attrs)
        if attrs.get("class") != "new":
            self._col_added.set_link(attrs.get("href"))

    def row_finished(self, row):
        cell = Cell()
        cell.add_data(row[0].data)
        cell.add_synonym(row[1].data, row[2].data, row[1].link)
        row[:] = [cell]
        return True
