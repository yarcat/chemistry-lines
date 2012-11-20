import unittest

import formula as F

F_plain = F.Formula.parse_plain
F_bra_pair = F.Formula.parse_pair_brackets
F_bra_close = F.Formula.parse_closing_brackets


class TestFormulaPlain(unittest.TestCase):

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

    def test_atom_count(self):
        atom_count = lambda f: F_plain(f).atom_count()
        self.assertEquals(atom_count("Na"), 1)
        self.assertEquals(atom_count("NaCl"), 2)
        self.assertEquals(atom_count("Na2O"), 3)
        self.assertEquals(atom_count("H2O2"), 4)

    def test_element_count(self):
        element_count = lambda f: F_plain(f).element_count()
        self.assertEquals(element_count("Na"), 1)
        self.assertEquals(element_count("NaCl"), 2)
        self.assertEquals(element_count("Na2O"), 2)
        self.assertEquals(element_count("H2O2"), 2)
        self.assertEquals(element_count("C2H5OH"), 3)


class TestFormulaBracketPairs(unittest.TestCase):

    def test_contains(self):
        f = F_bra_pair("Ca(OH)2")

        self.assertIn("Ca", f)
        self.assertIn("O", f)
        self.assertIn("H", f)
        self.assertIn("()", f)
        self.assertIn("2", f)

        self.assertNotIn("(", f)
        self.assertNotIn(")", f)

    def test_prefix(self):
        f = F_bra_pair("Ca(OH)2")
        self.assertEquals(f.prefix(), "CaOH()2")


class TestFormulaClosingBracket(unittest.TestCase):

    def test_contains(self):
        f = F_bra_close("Ca(OH)2")

        self.assertIn("Ca", f)
        self.assertIn("O", f)
        self.assertIn("H", f)
        self.assertIn("2", f)

        self.assertNotIn("(", f)
        self.assertIn(")", f)

    def test_prefix(self):
        f = F_bra_close("Ca(OH)2")
        self.assertEquals(f.prefix(), "CaOH)2")


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
