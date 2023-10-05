package org.example.sorter;

import org.example.main.WrappedArray;

public class CombS extends Sorter {
    @Override
    public String toString() {
        return "Comb";
    }

    @Override
    public int adjust(int d, int n) {
        return (int)((double)d * Math.log(1.3) / Math.log(2));
    }

    private void insertion(WrappedArray arr, int p, int k) {
        for(int i=p+1; i<k && !Thread.currentThread().isInterrupted(); i++) {
            for(int j=i; j>p; j--) {
                if (arr.get(j) >= arr.get(j-1)) {
                    break;
                }
                arr.swap(j, j-1);
            }
        }
    }

    @Override
    public void sort(WrappedArray arr) {
        try {
            int n = arr.size();
            int gap = n/2;
            while (gap > 1) {
                for(int i=gap; i<n && !Thread.currentThread().isInterrupted(); i++) {
                    if (arr.get(i) < arr.get(i-gap)) {
                        arr.swap(i, i-gap);
                    }
                }
                gap = (int)((double)gap / 1.3);
            }
            insertion(arr, 0, n);
        }
        catch (WrappedArray.WrappedInterrupt e) {
            Thread.currentThread().interrupt();
        }
    }
}
