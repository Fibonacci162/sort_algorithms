package org.example.sorter;

import org.example.main.WrappedArray;

public class InsertionS extends Sorter {
    public String toString() {
        return "Insertion";
    }

    @Override
    public int adjust(int d, int n) {
        return (int)((Math.log(n) / Math.log(2)) * (double)d / (double)n);
    }

    private void insertion(WrappedArray arr, int p, int k) {
        for(int i=p+1; i<k && !Thread.currentThread().isInterrupted(); i++) {
            int v = arr.get(i);
            for(int j=i-1; j>=p; j--) {
                if (v >= arr.get(j)) {
                    break;
                }
                arr.swap(j, j+1);
            }
        }
    }

    public void sort(WrappedArray arr) {
        try {
            insertion(arr, 0, arr.size());
        }
        catch (WrappedArray.WrappedInterrupt e) {
            Thread.currentThread().interrupt();
        }
    }
}
