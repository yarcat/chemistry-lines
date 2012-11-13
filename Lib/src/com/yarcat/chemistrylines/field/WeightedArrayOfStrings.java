package com.yarcat.chemistrylines.field;

import static java.util.Arrays.binarySearch;

import java.util.ArrayList;

// TODO(luch): make it generic

/** Weighted array is like an array where an item is repeated several times
 *
 * Alternative implementation is to use map like this
 * http://stackoverflow.com/questions/6409652/random-weighted-selection-java-framework
 */
public class WeightedArrayOfStrings {

    public static class OverflowException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public OverflowException(String detailMessage) {
            super(detailMessage);
        }
    }

    private ArrayList<String> mValues;  // It does not allow T[]
    private int[] mBounds;

    public WeightedArrayOfStrings(int maxValues) {
        mValues = new ArrayList<String>();
        mBounds = new int[maxValues];
    }

    /** Adds a value "weight times" */
    public void add(String value, Integer weight) throws OverflowException {
        if (mValues.size() == mBounds.length) {
            throw new OverflowException("WeightedArray overflow");
        }
        if (weight > 0) {
            mBounds[mValues.size()] = weight + size();
            mValues.add(value);
        }
    }

    /** Check whether the container has nothing in it */
    public boolean isEmpty() {
        return mValues.isEmpty();
    }

    /** Returns total weight of the array */
    public int size() {
        return mValues.isEmpty() ? 0 : mBounds[mValues.size() - 1];
    }

    /** Return a value by weighted index */
    public String get(int weightedIdx) {
        int valueIdx;

        if (mValues.isEmpty() || weightedIdx < 0 || weightedIdx >= size()) {
            return null;
        }

        valueIdx = binarySearch(mBounds, 0, mBounds.length, weightedIdx);
        if (valueIdx >= 0) {
            ++valueIdx;
        }
        else {
            valueIdx = -valueIdx - 1;
        }
        return mValues.get(valueIdx);
    }
}
