import html_table_filter


class Cell(html_table_filter.Cell):

    def __init__(self):
        super(Cell, self).__init__()
        self.name = None
        self.charge = None

    def set_name(self, name):
        self.name = name

    def set_charge(self, charge):
        self.charge = charge


class TempCell(html_table_filter.Cell):

    def __init__(self):
        super(TempCell, self).__init__()
        self.charge = None

    def add_to_charge(self):
        self.add_data = self.set_charge

    def set_charge(self, charge):
        self.charge = charge


class TableFilter(html_table_filter.TableFilter):

    def create_cell(self, attrs):
        return TempCell()

    def start_table(self, attrs):
        return dict(attrs).get("class") == "wikitable"

    def starttag_in_cell(self, tag, attrs):
        if tag == "sup":
            self._col_added.add_to_charge()

    def row_finished(self, row):
        if len(row) != 3:
            return False
        cell = Cell()
        cell.set_name(row[0].data)
        cell.add_data(row[1].data)
        cell.set_charge(row[1].charge)
        row[:] = [cell]
        return True
