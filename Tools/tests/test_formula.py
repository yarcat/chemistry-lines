import unittest

import formula as F


class TestFormula(unittest.TestCase):

    def test_coefficients(self):
        self.assertEquals(F.Formula("HCl").coefficients, [])
        self.assertEquals(F.Formula("H2").coefficients, [2])
        self.assertEquals(F.Formula("H2O2").coefficients, [2, 2])
        self.assertEquals(F.Formula("H2SO4").coefficients, [2, 4])
        self.assertEquals(F.Formula("Al(OH)3").coefficients, [3])

    def test_compound_ion(self):
        self.assertTrue(F.Formula("H2O").is_compound)
        self.assertTrue(F.Formula("H+").is_ion)
        self.assertTrue(F.Formula("O2-").is_ion)

    def test_contains(self):
        # atoms
        self.assertIn("Na", F.Formula("Na"))
        self.assertIn("Na", F.Formula("NaCl"))
        self.assertIn("Cl", F.Formula("NaCl"))
        self.assertNotIn("C", F.Formula("NaCl"))
        self.assertNotIn("a", F.Formula("NaCl"))
        self.assertNotIn("aC", F.Formula("NaCl"))

        # coefficients
        self.assertIn("2", F.Formula("H2O"))
        self.assertNotIn("1", F.Formula("H2O"))

        # other terminals
        self.assertIn("(", F.Formula("Al(OH)3"))
        self.assertIn(")", F.Formula("Al(OH)3"))
        self.assertNotIn("[", F.Formula("Al(OH)3"))

    def test_len(self):
        self.assertEquals(len(F.Formula("Na")), 1)
        self.assertEquals(len(F.Formula("Na2O")), 3)

    def test_prefix(self):
        f = F.Formula("Na2O")
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
