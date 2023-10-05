package org.example.painter;

public class Disparity {

     static double linear(int i, int v, int n) {
        double r = Math.abs(i-v);
         r /= (double) n;
        return 1-Math.min(r, 1);
    }

    static double cyclic(int i, int v, int n) {
        double r = Math.min(Math.abs(i-v), Math.abs(v-i-n));
        r /= (double) n;
        return 1-Math.min(2*r, 1);
    }
}
