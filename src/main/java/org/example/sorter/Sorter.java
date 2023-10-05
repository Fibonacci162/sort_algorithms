package org.example.sorter;

import org.example.main.WrappedArray;

public abstract class Sorter {
    @Override
    public abstract String toString();

    public abstract int adjust(int d, int n);

    public abstract void sort(WrappedArray arr);
}
