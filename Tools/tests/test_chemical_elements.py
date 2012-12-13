import unittest

import chemical_elements as E


class TestChemicalElements(unittest.TestCase):

    def test_count(self):
        N = 118
        self.assertEqual(len(E.ELEMENTS), N)
        self.assertEqual([e.number for e in E.ELEMENT_LIST], range(1, N + 1))

    def test_tritium(self):
        self.assertTrue("T" not in E.ELEMENTS)

    def test_weights_are_numbers(self):
        bad = []
        for e in E.ELEMENT_LIST:
            try:
                float(e.weight)
            except ValueError:
                bad.append((e.symbol, e.weight))
        self.assertEqual(bad, [])


if __name__ == "__main__":
    unittest.main()
