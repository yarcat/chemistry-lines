import unittest

import formula as F

F_plain = F.Formula.parse_plain
F_bra_pair = F.Formula.parse_pair_brackets
F_bra_close = F.Formula.parse_closing_brackets
F_compact = lambda f: F.Formula.parse_plain(f, F.Lexems.compact)

class TestFormulaPlain(unittest.TestCase):

    def test_coefficients(self):
        self.assertEquals(F_plain("HCl").coefficients, [1, 1])
        self.assertEquals(F_plain("H2").coefficients, [1, 2])
        self.assertEquals(F_plain("H2O2").coefficients, [1, 2, 1, 2])
        self.assertEquals(F_plain("H2SO4").coefficients, [1, 2, 1, 1, 4])
        self.assertEquals(F_plain("Al(OH)3").coefficients, [1, 0, 1, 1, 1, 3])

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
        self.assertEquals(atom_count("C2H5OH"), 9)
        self.assertEquals(atom_count("Ca(OH)2"), 5)
        self.assertEquals(atom_count("[Cu(H2O)4]SO4*H2O"), 21)

    def test_repeated_elements(self):
        r = lambda f: [e.symbol for e in F_plain(f).repeated_elements()]
        self.assertEquals(r("Na"), ["Na"])
        self.assertEquals(r("Na2O"), ["Na", "Na", "O"])
        self.assertEquals(r("H2O2"), ["H", "H", "O", "O"])
        self.assertEquals(r("Ca(OH)2"), ["Ca", "O", "H", "O", "H"])

    def test_element_count(self):
        element_count = lambda f: F_plain(f).element_count()
        self.assertEquals(element_count("Na"), 1)
        self.assertEquals(element_count("NaCl"), 2)
        self.assertEquals(element_count("Na2O"), 2)
        self.assertEquals(element_count("H2O2"), 2)
        self.assertEquals(element_count("C2H5OH"), 3)


class TestFormulaCompact(unittest.TestCase):

    def test_coefficients(self):
        self.assertEquals(F_compact("HCl").coefficients, [1, 1])
        self.assertEquals(F_compact("H2").coefficients, [2])
        self.assertEquals(F_compact("H2O2").coefficients, [2, 2])
        self.assertEquals(F_compact("H2SO4").coefficients, [2, 1, 4])
        self.assertEquals(F_compact("Al(OH)3").coefficients, [1, 0, 1, 1, 3])

    def test_compound_ion(self):
        self.assertTrue(F_compact("H2O").is_compound)
        self.assertTrue(F_compact("H+").is_ion)
        self.assertTrue(F_compact("O2-").is_ion)

    def test_contains(self):
        # atoms
        self.assertIn("Na", F_compact("Na"))
        self.assertIn("Na", F_compact("NaCl"))
        self.assertIn("Cl", F_compact("NaCl"))
        self.assertNotIn("C", F_compact("NaCl"))
        self.assertNotIn("a", F_compact("NaCl"))
        self.assertNotIn("aC", F_compact("NaCl"))

        # other terminals
        self.assertIn("(", F_compact("Al(OH)3"))
        self.assertIn(")3", F_compact("Al(OH)3"))

    def test_len(self):
        self.assertEquals(len(F_compact("Na")), 1)
        self.assertEquals(len(F_compact("Na2O")), 2)

    def test_prefix(self):
        f = F_compact("Na2O")
        self.assertEquals(f.prefix(0), "")
        self.assertEquals(f.prefix(1), "Na2")
        self.assertEquals(f.prefix(2), "Na2O")
        self.assertEquals(f.prefix(), "Na2O")
        self.assertEquals(f.prefix(2, " "), "Na2 O")

    def test_atom_count(self):
        atom_count = lambda f: F_compact(f).atom_count()
        self.assertEquals(atom_count("Na"), 1)
        self.assertEquals(atom_count("NaCl"), 2)
        self.assertEquals(atom_count("Na2O"), 3)
        self.assertEquals(atom_count("H2O2"), 4)
        self.assertEquals(atom_count("C2H5OH"), 9)
        self.assertEquals(atom_count("Ca(OH)2"), 5)
        self.assertEquals(atom_count("[Cu(H2O)4]SO4*H2O"), 21)

    def test_repeated_elements(self):
        r = lambda f: [e.symbol for e in F_compact(f).repeated_elements()]
        self.assertEquals(r("Na"), ["Na"])
        self.assertEquals(r("Na2O"), ["Na", "Na", "O"])
        self.assertEquals(r("H2O2"), ["H", "H", "O", "O"])
        self.assertEquals(r("Ca(OH)2"), ["Ca", "O", "H", "O", "H"])

    def test_element_count(self):
        element_count = lambda f: F_compact(f).element_count()
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
        self.assertTrue(F.Terminal("H").element)
        self.assertTrue(F.Terminal("H2").element)
        self.assertIsNone(F.Terminal("h").element)

        self.assertTrue(F.Terminal("Na").element)
        self.assertIsNone(F.Terminal("na").element)
        self.assertIsNone(F.Terminal("nA").element)

        self.assertIsNone(F.Terminal("2").element)
        self.assertIsNone(F.Terminal("*").element)

        self.assertIsNone(F.Terminal("(").element)
        self.assertIsNone(F.Terminal(")").element)
        self.assertIsNone(F.Terminal(")2").element)

        self.assertIsNone(F.Terminal("X").element)
        self.assertIsNone(F.Terminal("X3").element)
        self.assertIsNone(F.Terminal("Xz").element)

    def test_coeffients(self):
        self.assertEquals(F.Terminal("2").coefficient, 2)
        self.assertEquals(F.Terminal("40").coefficient, 40)

        self.assertEquals(F.Terminal("Na").coefficient, 1)
        self.assertEquals(F.Terminal("Na2").coefficient, 2)
        self.assertEquals(F.Terminal(")2").coefficient, 2)

        self.assertEquals(F.Terminal("0.5").coefficient, 0)
        self.assertEquals(F.Terminal("*").coefficient, 0)

    def test_starts_formula(self):
        self.assertTrue(F.Terminal("H").starts_formula)
        self.assertTrue(F.Terminal("(").starts_formula)

        self.assertFalse(F.Terminal("2").starts_formula)
        self.assertFalse(F.Terminal(")").starts_formula)
        self.assertFalse(F.Terminal("*").starts_formula)


class TestBracketTranslation(unittest.TestCase):

    def test_plain(self):
        f = F_plain("Al[OH]3")
        self.assertEquals(f.text, "Al[OH]3")
        self.assertEquals(f, F_plain("Al(OH)3"))

        self.assertIn("(", f)
        self.assertIn(")", f)

        self.assertNotIn("[", f)
        self.assertNotIn("]", f)

    def test_pair_brackets(self):
        f = F_bra_pair("Al[OH]3")
        self.assertEquals(f.text, "Al[OH]3")
        self.assertEquals(f, F_bra_pair("Al(OH)3"))

        self.assertIn("()", f)
        self.assertNotIn("[]", f)

        self.assertNotIn(")", f)
        self.assertNotIn("(", f)
        self.assertNotIn("[", f)
        self.assertNotIn("]", f)


if __name__ == "__main__":
    unittest.main()
