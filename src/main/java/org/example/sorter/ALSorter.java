package org.example.sorter;

import org.example.main.WrappedArray;

public class ALSorter extends Sorter {
    @Override
    public String toString() {
        return "ArrayList::sort";
    }

    @Override
    public int adjust(int d, int n) {
        return d;
    }

    @Override
    public void sort(WrappedArray arr) {
        try {
            arr.sort(Integer::compareTo);
        }
        catch (WrappedArray.WrappedInterrupt e) {
            Thread.currentThread().interrupt();
        }
    }
}
