import collections
import unittest
import wiki_chemical_dict


class TestWikiChemicalDict(unittest.TestCase):

    def testIsSimple(self):
        Formula = collections.namedtuple("Formula", ["terms"])
        # Using table-driven testing.
        tests = [("H 2 O", True),
                 ("H 2 S", True),
                 ("H 2 S O 4", True),
                 ("H ( O )", False),  # Contains "(".
                 ("H [ O ]", False),  # Contains "[".
                 ("H C 12 H 17 O N 4 S Cl 2", False)]
        for i, (compound, want) in enumerate(tests):
            got = wiki_chemical_dict.is_simple(Formula(compound.split()))
            self.assertEquals(got, want,
                              "%d. is_simple(%s) got %s, want %s" %
                              (i, compound, got, want))


if __name__ == "__main__":
    unittest.main()
