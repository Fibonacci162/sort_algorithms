package org.example.shuffler;

import org.example.main.WrappedArray;

public abstract class Shuffler {

    public abstract void shuffle(WrappedArray arr, int m);

    public abstract boolean respectLimit();

    @Override
    public abstract String toString();
}
