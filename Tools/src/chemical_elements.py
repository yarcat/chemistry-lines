"""Define valid chemical elements and groups"""

ATOMS = (
    "H", "He", "Li", "Be", "B", "C", "N", "O", "F", "Ne", "Na", "Mg", "Al",
    "Si", "P", "S", "Cl", "Ar", "K", "Ca", "Sc", "Ti", "V", "Cr", "Mn", "Fe",
    "Co", "Ni", "Cu", "Zn", "Ga", "Ge", "As", "Se", "Br", "Kr", "Rb", "Sr",
    "Y", "Zr", "Nb", "Mo", "Tc", "Ru", "Rh", "Pd", "Ag", "Cd", "In", "Sn",
    "Sb", "Te", "I", "Xe", "Cs", "Ba", "La", "Ce", "Pr", "Nd", "Pm", "Sm",
    "Eu", "Gd", "Tb", "Dy", "Ho", "Er", "Tm", "Yb", "Lu", "Hf", "Ta", "W",
    "Re", "Os", "Ir", "Pt", "Au", "Hg", "Tl", "Pb", "Bi", "Po", "At", "Rn",
    "Fr", "Ra", "Ac", "Th", "Pa", "U", "Np", "Pu", "Am", "Cm", "Bk", "Cf",
    "Es", "Fm", "Md", "No", "Lr", "Rf", "Db", "Sg", "Bh", "Hs", "Mt", "Ds",
    "Rg", "Cn", "Uut", "Fl", "Uup", "Lv", "Uus", "Uuo")

GROUP_STARTS = (0, 2, 10, 19, 37, 55, 87)

GROUPS = tuple(ATOMS[start:end] for start, end in
               zip(GROUP_STARTS, GROUP_STARTS[1:] + (None,)))

def pack2dict(*pairs):
    return dict((it, group) for group, elements in pairs for it in elements)

CATEGORY = pack2dict(
    ("AlkaliMetal", ["Li", "Na", "K", "Rb", "Cs", "Fr"]),
    ("AlkalineMetal", ["Be", "Mg", "Ca", "Sr", "Ba", "Ra"]),
    ("Lanthanoid", ATOMS[56:71]),
    ("Actinoid", ATOMS[88:103]),
    ("TransitionMetal", ATOMS[20:30] + ATOMS[38:48] + ATOMS[71:80]),
    ("PostTransitionMetal", ["Al", "Ga", "In", "Sn", "Tl", "Pb", "Bi"]),
    ("Metalloid", ["B", "Si", "Ge", "As", "Sb", "Te"]),
    ("OtherNonMetal", ["H", "C", "N", "O", "P", "S", "Se"]),
    ("Halogen", ["F", "Cl", "Br", "I", "At"]),
    ("NobleGas", ["He", "Ne", "Ar", "Kr", "Xe", "Rn"]),
)

def category(atom):
    return CATEGORY.get(atom, "Undefined")

GASES = ("H", "He", "N", "O", "F", "Ne", "Cl", "Ar", "Kr", "Rn")
LIQUIDS = ("Br", "Hg")
STATE_OF_MATTER = pack2dict(
    ("Gas", GASES),
    ("Liquid", LIQUIDS),
    ("Solid", set(ATOMS[:104]) - set(GASES) - set(LIQUIDS)),
)

def state_of_matter(atom):
    return STATE_OF_MATTER.get(atom, "Undefined")
