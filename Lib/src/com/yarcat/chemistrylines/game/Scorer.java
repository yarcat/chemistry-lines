package com.yarcat.chemistrylines.game;

import java.util.Map;
import java.util.TreeMap;

import android.annotation.SuppressLint;

import com.yarcat.chemistrylines.algorithms.CompoundReporter.CompoundReference;

public interface Scorer {

    public String get();

    public void update(CompoundReference ref);

    public void init();

    public interface ScoreListener {
        public void onScoreChange(Scorer scorer);
    }

    public void setScoreListener(ScoreListener l);

    abstract class Base implements Scorer {
        ScoreListener mListener;

        @Override
        public void setScoreListener(ScoreListener l) {
            mListener = l;
        }

        void onScoreChange() {
            if (mListener != null) {
                mListener.onScoreChange(this);
            }
        }

        @Override
        public void init() {
            onScoreChange();
        }
    }

    abstract class BaseInt extends Base {
        int mScore;

        public BaseInt() {
            mScore = 0;
        }

        @Override
        public String get() {
            return String.valueOf(mScore);
        }
    }

    abstract class BaseFloat extends Base {
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
            onScoreChange();
        }
    }

    class AtomExpScore extends BaseInt {

        @Override
        public void update(CompoundReference ref) {
            int n = ref.getCompound().atomCount();
            mScore += n < 4 ? 1 : 1 << (n - 3);
            onScoreChange();
        }
    }

    class AtomicWeightScore extends BaseFloat {

        @Override
        public void update(CompoundReference ref) {
            mScore += ref.getCompound().atomicWeight();
            onScoreChange();
        }
    }

    class ScoreContainer extends Base {

        Map<String, Scorer> mContents;

        public ScoreContainer() {
            mContents = new TreeMap<String, Scorer>();
            mContents.put("Atoms", new AtomScore());
            mContents.put("Max 1, 2^(n-3)", new AtomExpScore());
            mContents.put("Weight", new AtomicWeightScore());
        }

        @Override
        public String get() {
            String r = "";
            for (String name : mContents.keySet()) {
                r =
                    String.format("%s%s\t%s\n", r, name, mContents
                        .get(name).get());
            }
            return r;
        }

        @Override
        public void update(CompoundReference ref) {
            for (Scorer s : mContents.values()) {
                s.update(ref);
            }
            onScoreChange();
        }
    }
}
