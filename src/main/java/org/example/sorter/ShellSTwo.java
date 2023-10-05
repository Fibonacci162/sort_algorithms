package org.example.sorter;

public class ShellSTwo extends ShellS {
    protected int[] gaps() {
        return new int[]{1, 5, 19, 41, 109, 209, 505, 929, 2161, 3905, 8929, 16001};
    }

    @Override
    public String toString() {
        return "Shell, A033622";
    }

    @Override
    public int adjust(int d, int n) {
        return (int)((double)d * Math.log(n) * Math.pow(n, -0.33333) / Math.log(2));
    }
}
