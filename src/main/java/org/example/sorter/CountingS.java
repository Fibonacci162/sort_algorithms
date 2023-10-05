package org.example.sorter;

import org.example.main.WrappedArray;

public class CountingS extends Sorter{
    @Override
    public String toString() {
        return "Counting";
    }

    @Override
    public int adjust(int d, int n) {
        return (int)((double)d * Math.log(n) / Math.log(2));
    }

    @Override
    public void sort(WrappedArray arr) {
        try {
            int n = arr.size();
            int m = 0;
            for (int d : arr) {
                m = Math.max(d, m);
            }
            m++;
            int[] aux = new int[n];
            int[] count = new int[m];
            for (int i=0; i<n; i++) {
                aux[i] = arr.get(i);
                count[aux[i]]++;
            }
            for (int i=1; i<m; i++) {
                count[i] += count[i-1];
            }
            for(int i=n-1; i>=0; i--) {
                arr.set(--count[aux[i]], aux[i]);
            }
        }
        catch (WrappedArray.WrappedInterrupt e) {
            Thread.currentThread().interrupt();
        }
    }
}
