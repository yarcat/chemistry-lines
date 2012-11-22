import unittest

import formula as F
import registry_helper as R

F_plain = F.Formula.parse_plain

class TestFormula(unittest.TestCase):

    def test_split_formula_set(self):
        formulas = map(F_plain, ["Aa", "(A", "[A", "B"])
        groups = list(R.split_formula_set(formulas))
        self.assertEquals(len(groups), 2)

    def test_iter_prefixes(self):
        formula = F_plain("Na2O")
        prefixes = list(R.iter_prefixes(formula))
        self.assertEquals(prefixes, ["Na", "Na2"])

    def test_gen_productions(self):
        formulas = map(F_plain, ["H2", "H2O", "H2S", "H2SO4"])
        prods = list(R.gen_productions(formulas))
        expect = [("H", "2", "H2"), ("H2", "O", "H2O"), ("H2", "S", "H2S"),
                  ("H2S", "O", "H2SO"), ("H2SO", "4", "H2SO4")]
        self.assertEquals(prods, expect)


if __name__ == "__main__":
    unittest.main()
