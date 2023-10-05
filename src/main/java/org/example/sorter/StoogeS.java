package org.example.sorter;

import org.example.main.WrappedArray;

public class StoogeS extends Sorter{
    @Override
    public String toString() {
        return "Stooge";
    }

    public int adjust(int d, int n) {
        return (int)((Math.log(n) / Math.log(2)) * (double)d / Math.pow(n, Math.log(3)/Math.log(1.5) - 1));
    }

    private void stooge(WrappedArray arr, int p, int k) {
        if (arr.get(p) > arr.get(k)) {
            arr.swap(p, k);
        }
        if (k - p + 1 > 2) {
            int t = (k - p + 1) / 3;
            stooge(arr, p, k-t);
            stooge(arr, p+t, k);
            stooge(arr, p, k-t);
        }
    }

    public void sort(WrappedArray arr) {
        try {
            int n = arr.size();
            stooge(arr, 0, n-1);
        }
        catch (WrappedArray.WrappedInterrupt e) {
            Thread.currentThread().interrupt();
        }
    }
}
