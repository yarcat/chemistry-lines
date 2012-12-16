package com.yarcat.chemistrylines.game;

import java.util.Map;
import java.util.TreeMap;

import com.yarcat.chemistrylines.algorithms.CompoundReporter.CompoundReference;

public interface Scorer {

    public String get();

    public void update(CompoundReference ref);

    abstract class Base implements Scorer {
        int mScore;

        public Base() {
            mScore = 0;
        }

        @Override
        public String get() {
            return String.valueOf(mScore);
        }
    }

    class AtomScore extends Base {

        @Override
        public void update(CompoundReference ref) {
            mScore += ref.getCompound().atomCount();
        }
    }

    class AtomExpScore extends Base {

        @Override
        public void update(CompoundReference ref) {
            int n = ref.getCompound().atomCount();
            mScore += n < 4 ? 1 : 1 << (n - 3);
        }
    }

    class ScoreContainer implements Scorer {

        Map<String, Scorer> mContents;

        public ScoreContainer() {
            mContents = new TreeMap<String, Scorer>();
            mContents.put("Atoms", new AtomScore());
            mContents.put("1 or 2^(n-3)", new AtomExpScore());
        }

        @Override
        public String get() {
            String r="";
            for (String name: mContents.keySet()) {
                r = String.format("%s%s\t%s\n", r, name, mContents.get(name).get());
            }
            return r;
        }

        @Override
        public void update(CompoundReference ref) {
            for(Scorer s : mContents.values()) {
                s.update(ref);
            }
        }
    }
}
