package org.example.sorter;

import org.example.main.WrappedArray;

public class GnomeS extends Sorter {
    public String toString() {
        return "Gnome";
    }

    @Override
    public int adjust(int d, int n) {
        return (int)((Math.log(n) / Math.log(2)) * (double)d / (double)n);
    }

    public void sort(WrappedArray arr) {
        try {
            int n = arr.size();
            int i = 1;
            while (i < n) {
                if (i>0) {
                    if (arr.get(i) < arr.get(i-1)) {
                        arr.swap(i, i-1);
                        i--;
                        continue;
                    }
                }
                i++;
            }
        }
        catch (WrappedArray.WrappedInterrupt e) {
            Thread.currentThread().interrupt();
        }
    }
}
