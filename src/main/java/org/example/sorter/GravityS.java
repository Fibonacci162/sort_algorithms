package org.example.sorter;

import org.example.main.WrappedArray;

import java.util.BitSet;

public class GravityS extends Sorter{
    @Override
    public String toString() {
        return "Gravity";
    }

    @Override
    public int adjust(int d, int n) {
        return (int)((Math.log(n) / Math.log(2)) * (double)d / (double)n);
    }

    @Override
    public void sort(WrappedArray arr) {
        try {
            int n = arr.size();
            int m = 0;
            for (int d : arr) {
                m = Math.max(m, d);
            }
            // Each bead either exists or doesn't
            // Bitset is used in order to save space
            BitSet[] beads = new BitSet[m+1];
            for (int i=0; i<=m; i++) {
                beads[i] = new BitSet(n);
            }
            for (int i=0; i<n; i++) {
                int v = arr.get(i);
                for (int j=0; j<=m; j++) {
                    if (v >= j) {
                        beads[j].set(i);
                    }
                }
            }

            /* This is what any sane person would write:
            for (int j=0; j<=m; j++) {
                int counter = beads[j].cardinality();
                beads[j].clear();
                if (counter > 0) {
                   beads[j].set(n-counter, n-1);
                }
            }
            for (int i=0; i<n; i++) {
                int counter = 0;
                for (int j=0; j<=m; j++) {
                    if (beads[j].get(i)) {
                        counter++;
                    }
                }
                arr.set(i, counter);
            }
            HOWEVER, this wouldn't be visualised at all.
            So I have to destroy this in order to visualise it.
            */
            for (int j=m; j>=0; j--) {
                int counter = 0;
                for (int i=0; i<n; i++) {
                    if (beads[j].get(i)) {
                        ++counter;
                        arr.set(i, arr.get(i)-1);
                    }
                }
                // Just so the beads actually "fall"
                beads[j].clear();
                if (counter > 0) {
                    beads[j].set(n - counter, n - 1);
                }
                // Visualise!
                for (int i=0; i<counter; i++) {
                    arr.set(n-i-1, arr.get(n-i-1)+1);
                }
            }
        }
        catch (WrappedArray.WrappedInterrupt e) {
            Thread.currentThread().interrupt();
        }
    }
}
