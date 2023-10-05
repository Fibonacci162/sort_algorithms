package org.example.painter;

import org.example.main.WrappedArray;

public abstract class Theme {
    @Override
    public abstract String toString();
    public abstract Painter painter(WrappedArray arr, int mxval, String[] toDisplay);
}