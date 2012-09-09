import html_table_filter


class TableFilter(html_table_filter.TableFilter):

    def start_table(self, attrs):
        return dict(attrs).get("class") == "wikitable"

    def row_finished(self, row):
        return len(row)
