package org.example.painter;

import org.example.main.WrappedArray;

import java.util.ArrayList;
import java.util.List;

public class ThemeFactory {
    public static List<Theme> themes() {
        List<Theme> ls = new ArrayList<>();
        ls.add(new DefaultTheme());
        ls.add(new Theme(){

            @Override
            public String toString() {
                return "Basic White Bars";
            }

            @Override
            public Painter painter(WrappedArray arr, int mxval, String[] toDisplay) {
                return new SimpleBarPainter(arr,mxval,toDisplay);
            }
        });
        ls.add(new Theme() {
            @Override
            public String toString() {
                return "Color Circle";
            }

            @Override
            public Painter painter(WrappedArray arr, int mxval, String[] toDisplay) {
                return new ColorCircle(arr, mxval, toDisplay);
            }
        });
        ls.add(new Theme() {
            @Override
            public String toString() {
                return "Color bars";
            }

            @Override
            public Painter painter(WrappedArray arr, int mxval, String[] toDisplay) {
                return new ColorBars(arr, mxval, toDisplay);
            }
        });
        ls.add(new Theme() {
            @Override
            public String toString() {
                return "Scatter Plot";
            }

            @Override
            public Painter painter(WrappedArray arr, int mxval, String[] toDisplay) {
                return new ScatterDots(arr,mxval,toDisplay);
            }
        });
        ls.add(new Theme() {
            @Override
            public String toString() {
                return "Color Hoops";
            }

            @Override
            public Painter painter(WrappedArray arr, int mxval, String[] toDisplay) {
                return new CHoopP(arr, mxval, toDisplay);
            }
        });
        ls.add(new Theme() {
            @Override
            public String toString() {
                return "Color Spiral";
            }

            @Override
            public Painter painter(WrappedArray arr, int mxval, String[] toDisplay) {
                return new SpiralP(arr, mxval, toDisplay);
            }
        });
        ls.add(new Theme() {
            @Override
            public String toString() {
                return "Color Very Spiral";
            }

            @Override
            public Painter painter(WrappedArray arr, int mxval, String[] toDisplay) {
                return new HyperSpiralP(arr, mxval, toDisplay);
            }
        });
        ls.add(new Theme() {
            @Override
            public String toString() {
                return "White Dot Circle";
            }

            @Override
            public Painter painter(WrappedArray arr, int mxval, String[] toDisplay) {
                return new WhiteDotCircleP(arr, mxval, toDisplay);
            }
        });
        ls.add(new Theme() {
            @Override
            public String toString() {
                return "Disparity Color Circle";
            }

            @Override
            public Painter painter(WrappedArray arr, int mxval, String[] toDisplay) {
                return new DisparityColorCircleP(arr, mxval, toDisplay);
            }
        });
        ls.add(new Theme() {
            @Override
            public String toString() {
                return "Tangling Web";
            }

            @Override
            public Painter painter(WrappedArray arr, int mxval, String[] toDisplay) {
                return new DiametersP(arr, mxval, toDisplay, 2.6);
            }
        });
        ls.add(new Theme() {
            @Override
            public String toString() {
                return "Untangling Web";
            }

            @Override
            public Painter painter(WrappedArray arr, int mxval, String[] toDisplay) {
                return new DiametersP(arr, mxval, toDisplay, 0.01);
            }
        });

        return ls;
    }
}
