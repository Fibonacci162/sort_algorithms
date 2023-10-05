package org.example.sorter;

import org.example.main.WrappedArray;

public class ExchangeS extends Sorter {
    public String toString() {
        return "Exchange";
    }

    @Override
    public int adjust(int d, int n) {
        return (int)((Math.log(n) / Math.log(2)) * (double)d / (double)n);
    }

    public void sort(WrappedArray arr) {
        try {
            int n = arr.size();
            for(int i=0; i<n && !Thread.currentThread().isInterrupted(); i++) {
                for(int j=i+1; j<n; j++) {
                    if (arr.get(j) < arr.get(i)) {
                        arr.swap(i,j);
                    }
                }
            }
        }
        catch (WrappedArray.WrappedInterrupt e) {
            Thread.currentThread().interrupt();
        }
    }
}
