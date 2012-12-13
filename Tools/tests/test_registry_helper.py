import unittest

import formula as F
import registry_helper as R


class Formula(F.Formula):
    def is_final(self):
        return True


F_plain = Formula.parse_plain
F_bra_pair = Formula.parse_pair_brackets
F_bra_close = Formula.parse_closing_brackets


class TestFormulaPlain(unittest.TestCase):

    def test_split_formula_set(self):
        formulas = map(F_plain, ["Aa", "(A", "[A", "B"])
        groups = list(R.split_formula_set(formulas))
        self.assertEquals(len(groups), 2)

    def test_iter_prefixes(self):
        formula = F_plain("Na2O")
        prefixes = list(R.iter_prefixes(formula))
        self.assertEquals(prefixes, ["Na", "Na2"])

    def test_gen_prefixes(self):
        prefixes = R.gen_prefixes([F_plain("Al(OH)3")])
        self.assertEquals(prefixes[-1].prefix, "Al(OH)3")

    def test_gen_productions(self):
        formulas = map(F_plain, ["H2", "H2O", "H2S", "H2SO4"])
        prods = list(R.gen_productions(formulas))
        expect = [("H", "2", "H2"), ("H2", "O", "H2O"), ("H2", "S", "H2S"),
                  ("H2S", "O", "H2SO"), ("H2SO", "4", "H2SO4")]
        self.assertEquals(prods, expect)

    def test_collect_terms(self):
        collected_terms = R.collect_terms(map(F_plain, ["H", "F", "HF"]))
        self.assertEquals(collected_terms, set(["H", "F"]))


class TestFormulaBracketPairs(unittest.TestCase):

    def test_gen_prefixes(self):
        prefixes = R.gen_prefixes([F_bra_pair("Al(OH)3")])
        self.assertEquals(prefixes[-1].prefix, "AlOH()3")


class TestFormulaClosingBracket(unittest.TestCase):

    def test_gen_prefixes(self):
        prefixes = R.gen_prefixes([F_bra_close("Al(OH)3")])
        self.assertEquals(prefixes[-1].prefix, "AlOH)3")


if __name__ == "__main__":
    unittest.main()
