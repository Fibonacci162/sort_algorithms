package org.example.sorter;

import org.example.main.WrappedArray;

public class InsertionMergeS extends Sorter{
    @Override
    public String toString() {
        return "Slow Merge";
    }

    @Override
    public int adjust(int d, int n) {
        return (int)((Math.log(n) / Math.log(2)) * (double)d * (double)n / Math.pow(n, 2.58));
    }

    private void helper(WrappedArray a, int p, int k) {
        if (k <= p+1) {
            return;
        }
        if (k == p+2) {
            if (a.get(p) > a.get(p+1)) {
                a.swap(p, p+1);
            }
            return;
        }
        int m = (p+k)/2;
        int s1 = (p+m)/2;
        int s2 = (m+k)/2;
        helper(a, p, m);
        helper(a, m, k);
        helper(a, s1, s2);
        helper(a, p, m);
        helper(a, m, k);
        helper(a, s1, s2);
    }

    @Override
    public void sort(WrappedArray arr) {
        try {
            helper(arr, 0, arr.size());
        }
        catch (WrappedArray.WrappedInterrupt e) {
            Thread.currentThread().interrupt();
        }
    }
}
