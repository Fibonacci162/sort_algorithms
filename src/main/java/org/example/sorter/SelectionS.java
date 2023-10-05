package org.example.sorter;

import org.example.main.WrappedArray;

public class SelectionS extends Sorter{
    public String toString() {
        return "Selection";
    }

    @Override
    public int adjust(int d, int n) {
        return (int)((Math.log(n) / Math.log(2)) * (double)d / (double)n);
    }

    public void sort(WrappedArray arr) {
        try {
            int n = arr.size();
            for(int i=0; i<n && !Thread.currentThread().isInterrupted(); i++) {
                int mni = i;
                for(int j=i+1; j<n; j++) {
                    if (arr.get(j) < arr.get(mni)) {
                        mni = j;
                    }
                }
                arr.swap(mni, i);
            }
        }
        catch (WrappedArray.WrappedInterrupt e) {
            Thread.currentThread().interrupt();
        }
    }
}
