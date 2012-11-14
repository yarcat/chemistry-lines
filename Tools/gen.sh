python2.7 src/wiki_chemical_dict.py \
    --only-simple \
    -group 1 -group 2 -group 3 -group 4 \
    -atom Ag -atom Au -atom I \
    tests/data/dictionary_of_chemical_formulas.html \
    -Cc \
    > ../Lib/src/com/yarcat/chemistrylines/field/KnownFormulas.java
