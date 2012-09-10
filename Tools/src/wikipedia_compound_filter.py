import html_table_filter
import collections


Synonym = collections.namedtuple("Synonym", "synonym cas_number")


class Cell(html_table_filter.Cell):

    def __init__(self):
        html_table_filter.Cell.__init__(self)
        self.synonyms = []

    def add_synonym(self, synonym, cas_number):
        self.synonyms.append(Synonym(synonym, cas_number))


class TableFilter(html_table_filter.TableFilter):

    def start_table(self, attrs):
        return dict(attrs).get("class") == "wikitable"

    def row_finished(self, row):
        cell = Cell()
        cell.add_data(row[0].data)
        cell.add_synonym(row[1].data, row[2].data)
        row[:] = [cell]
        return True
