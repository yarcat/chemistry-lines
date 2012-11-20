# --no-opening-brackets
#  * does not match '()' and '[]' of --pair-brackets
#  * does not match anything with --closing-brackets
known_formulas_cmd="python2.7 -B src/wiki_chemical_dict.py \
    -C --no-opening-brackets --max-atoms=7 \
    -group 1 -group 2 -group 3 -group 4 \
    -atom Ag -atom Au -atom I \
    tests/data/dictionary_of_chemical_formulas.html"
$known_formulas_cmd --class "$@" > ../Lib/src/com/yarcat/chemistrylines/field/KnownFormulas.java \
&& $known_formulas_cmd --test "$@" > ../Lib/tests/com/yarcat/tests/chemistrylines/KnownFormulasTest.java
