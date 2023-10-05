package org.example.sorter;

import org.example.main.WrappedArray;

public class BitonicS extends Sorter {
    @Override
    public String toString() {
        return "Bitonic";
    }

    @Override
    public int adjust(int d, int n) {
        return (int)(Math.log(2) * (double)d / Math.log(n));
    }

    private void compAndSwap(WrappedArray a, int i, int j, boolean dir) {
        if ((a.get(i) < a.get(j)) == dir) {
            a.swap(i,j);
        }
    }

    private void performRun(WrappedArray a, int i, int length, boolean dir) {
        int gap = length / 2;
        if (gap > 0) {
            for (int j=i; j+gap < i+length; j++) {
                compAndSwap(a, j, j+gap, dir);
            }
            performRun(a, i, gap, dir);
            performRun(a, i+gap, gap, dir);
        }
    }

    private void bitonStep(WrappedArray a, int step) {
        boolean dir = false;
        for(int i=0; i < a.size(); i+=step) {
            performRun(a, i, step, dir);
            dir = !dir;
        }
    }

    private int nearPowOfTwo(int n) {
        int res = 1;
        while (res < n) {
            res <<= 1;
        }
        return res;
    }

    @Override
    public void sort(WrappedArray a) {
        try {
            int n = a.size();
            int m = nearPowOfTwo(n);
            for(int i=n; i<m; i++) {
                a.add(Integer.MAX_VALUE);
            }
            for (int step = 2; step <= m; step *= 2) {
                bitonStep(a, step);
            }
            if (m > n) {
                a.subList(n, m).clear();
            }
        }
        catch (WrappedArray.WrappedInterrupt e) {
            Thread.currentThread().interrupt();
        }
    }
}
