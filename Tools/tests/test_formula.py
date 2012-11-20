import unittest

import formula as F

F_plain = F.Formula.parse_plain


class TestFormula(unittest.TestCase):

    def test_coefficients(self):
        self.assertEquals(F_plain("HCl").coefficients, [])
        self.assertEquals(F_plain("H2").coefficients, [2])
        self.assertEquals(F_plain("H2O2").coefficients, [2, 2])
        self.assertEquals(F_plain("H2SO4").coefficients, [2, 4])
        self.assertEquals(F_plain("Al(OH)3").coefficients, [3])

    def test_compound_ion(self):
        self.assertTrue(F_plain("H2O").is_compound)
        self.assertTrue(F_plain("H+").is_ion)
        self.assertTrue(F_plain("O2-").is_ion)

    def test_contains(self):
        # atoms
        self.assertIn("Na", F_plain("Na"))
        self.assertIn("Na", F_plain("NaCl"))
        self.assertIn("Cl", F_plain("NaCl"))
        self.assertNotIn("C", F_plain("NaCl"))
        self.assertNotIn("a", F_plain("NaCl"))
        self.assertNotIn("aC", F_plain("NaCl"))

        # coefficients
        self.assertIn("2", F_plain("H2O"))
        self.assertNotIn("1", F_plain("H2O"))

        # other terminals
        self.assertIn("(", F_plain("Al(OH)3"))
        self.assertIn(")", F_plain("Al(OH)3"))
        self.assertNotIn("[", F_plain("Al(OH)3"))

    def test_len(self):
        self.assertEquals(len(F_plain("Na")), 1)
        self.assertEquals(len(F_plain("Na2O")), 3)

    def test_prefix(self):
        f = F_plain("Na2O")
        self.assertEquals(f.prefix(0), "")
        self.assertEquals(f.prefix(1), "Na")
        self.assertEquals(f.prefix(2), "Na2")
        self.assertEquals(f.prefix(3), "Na2O")
        self.assertEquals(f.prefix(), "Na2O")
        self.assertEquals(f.prefix(3, " "), "Na 2 O")


class TestTerminal(unittest.TestCase):

    def test_atoms(self):
        self.assertTrue(F.Terminal("H").is_atom)
        self.assertFalse(F.Terminal("h").is_atom)

        self.assertTrue(F.Terminal("Na").is_atom)
        self.assertFalse(F.Terminal("na").is_atom)
        self.assertFalse(F.Terminal("nA").is_atom)

        self.assertFalse(F.Terminal("2").is_atom)
        self.assertFalse(F.Terminal("*").is_atom)

    def test_coeffients(self):
        self.assertTrue(F.Terminal("2").is_coefficient)
        self.assertTrue(F.Terminal("40").is_coefficient)

        self.assertFalse(F.Terminal("0.5").is_coefficient)
        self.assertFalse(F.Terminal("Na").is_coefficient)
        self.assertFalse(F.Terminal("*").is_coefficient)

    def test_starts_formula(self):
        self.assertTrue(F.Terminal("H").starts_formula)
        self.assertTrue(F.Terminal("(").starts_formula)
        self.assertTrue(F.Terminal("[").starts_formula)

        self.assertFalse(F.Terminal("2").starts_formula)
        self.assertFalse(F.Terminal(")").starts_formula)
        self.assertFalse(F.Terminal("]").starts_formula)
        self.assertFalse(F.Terminal("*").starts_formula)


if __name__ == "__main__":
    unittest.main()
