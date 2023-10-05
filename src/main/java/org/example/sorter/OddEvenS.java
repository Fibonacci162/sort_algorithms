package org.example.sorter;

import org.example.main.WrappedArray;

public class OddEvenS extends Sorter{
    @Override
    public String toString() {
        return "Odd-Even";
    }

    public int adjust(int d, int n) {
        return (int)((Math.log(n) / Math.log(2)) * (double)d / (double)n);
    }

    private boolean odd(WrappedArray arr) {
        boolean res = false;
        int n = arr.size();
        for (int i=1; i<n; i+=2) {
            if (arr.get(i) < arr.get(i-1)) {
                res = true;
                arr.swap(i, i-1);
            }
        }
        return res;
    }

    private boolean even(WrappedArray arr) {
        boolean res = false;
        int n = arr.size();
        for (int i=2; i<n; i+=2) {
            if (arr.get(i) < arr.get(i-1)) {
                res = true;
                arr.swap(i, i-1);
            }
        }
        return res;
    }

    public void sort(WrappedArray arr) {
        try {
            int n = arr.size();
            while (true) {
                boolean b1 = odd(arr);
                boolean b2 = even(arr);
                if(!b1 && !b2) {
                    break;
                }
            }
        }
        catch (WrappedArray.WrappedInterrupt e) {
            Thread.currentThread().interrupt();
        }
    }
}
