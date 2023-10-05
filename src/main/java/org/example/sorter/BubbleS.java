package org.example.sorter;

import org.example.main.WrappedArray;

public class BubbleS extends Sorter{
    @Override
    public String toString() {
        return "Bubble";
    }

    @Override
    public int adjust(int d, int n) {
        return (int)((Math.log(n) / Math.log(2)) * (double)d / (double)n);
    }

    @Override
    public void sort(WrappedArray arr) {
        try {
            int n = arr.size();
            for(int i=0; i<n && !Thread.currentThread().isInterrupted(); i++) {
                for(int j=1; j<n-i; j++) {
                    if (arr.get(j) < arr.get(j-1)) {
                        arr.swap(j, j-1);
                    }
                }
            }
        }
        catch (WrappedArray.WrappedInterrupt e) {
            Thread.currentThread().interrupt();
        }
    }
}
