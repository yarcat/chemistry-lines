package com.yarcat.chemistrylines.game;

import java.util.Map;
import java.util.TreeMap;

import android.annotation.SuppressLint;

import com.yarcat.chemistrylines.algorithms.CompoundReporter.CompoundReference;

public interface Scorer {

    public String get();

    public void update(CompoundReference ref);

    abstract class BaseInt implements Scorer {
        int mScore;

        public BaseInt() {
            mScore = 0;
        }

        @Override
        public String get() {
            return String.valueOf(mScore);
        }
    }

    abstract class BaseFloat implements Scorer {
        float mScore;

        public BaseFloat() {
            mScore = 0;
        }

        @SuppressLint("DefaultLocale")
        @Override
        public String get() {
            return String.format("%.4f", mScore);
        }
    }

    class AtomScore extends BaseInt {

        @Override
        public void update(CompoundReference ref) {
            mScore += ref.getCompound().atomCount();
        }
    }

    class AtomExpScore extends BaseInt {

        @Override
        public void update(CompoundReference ref) {
            int n = ref.getCompound().atomCount();
            mScore += n < 4 ? 1 : 1 << (n - 3);
        }
    }

    class AtomicWeightScore extends BaseFloat {

        @Override
        public void update(CompoundReference ref) {
            mScore += ref.getCompound().atomicWeight();
        }
    }

    class ScoreContainer implements Scorer {

        Map<String, Scorer> mContents;

        public ScoreContainer() {
            mContents = new TreeMap<String, Scorer>();
            mContents.put("Atoms", new AtomScore());
            mContents.put("Max 1, 2^(n-3)", new AtomExpScore());
            mContents.put("Weight", new AtomicWeightScore());
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
