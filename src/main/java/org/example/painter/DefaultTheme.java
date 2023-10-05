package org.example.painter;

import org.example.main.WrappedArray;

public class DefaultTheme extends Theme{
    @Override
    public String toString() {
        return "White Bars";
    }
    @Override
    public Painter painter(WrappedArray arr, int mxval, String[] toDisplay) {
        return new DefaultPainter(arr, mxval, toDisplay);
    }
}
