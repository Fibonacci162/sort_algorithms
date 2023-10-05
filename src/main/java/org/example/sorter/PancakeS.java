package org.example.sorter;

import org.example.main.WrappedArray;

public class PancakeS extends Sorter {
    public String toString() {
        return "Pancake";
    }

    @Override
    public int adjust(int d, int n) {
        return (int)((Math.log(n) / Math.log(2)) * (double)d / (double)n);
    }

    public void sort(WrappedArray arr) {
        try {
            int n = arr.size();
            for(int i=0; i<n && !Thread.currentThread().isInterrupted(); i++) {
                int mxi = 0;
                for(int j=1; j<n-i; j++) {
                    if (arr.get(j) > arr.get(mxi)) {
                        mxi = j;
                    }
                }
                arr.flip(0,mxi+1);
                arr.flip(0,n-i);
            }
        }
        catch (WrappedArray.WrappedInterrupt e) {
            Thread.currentThread().interrupt();
        }
    }
}
