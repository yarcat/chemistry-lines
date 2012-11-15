import collections
import unittest

import formula as F
import wiki_chemical_dict


class TestWikiChemicalDict(unittest.TestCase):

    def testIsSimple(self):
        # Using table-driven testing.
        tests = [("H2O", True),
                 ("H2S", True),
                 ("H2SO4", True),
                 ("H(O)", False),  # Contains "(".
                 ("H[O]", False),  # Contains "[".
                 ("HC12H17ON4SCl2", False)]
        for i, (compound, want) in enumerate(tests):
            got = wiki_chemical_dict.is_simple(F.Formula(compound))
            self.assertEquals(got, want,
                              "%d. is_simple(%s) got %s, want %s" %
                              (i, compound, got, want))


if __name__ == "__main__":
    unittest.main()
