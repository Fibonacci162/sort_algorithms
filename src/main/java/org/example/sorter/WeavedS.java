package org.example.sorter;

import org.example.main.WrappedArray;

public class WeavedS extends Sorter{
    @Override
    public String toString() {
        return "Weave IP";
    }

    @Override
    public int adjust(int d, int n) {
        return (int)((Math.log(n) / Math.log(2)) * (double)d / (double)n);
    }

    private void insertion(WrappedArray arr, int den, int mod) {
        int n = arr.size();
        for(int i=mod; i<n; i+=den) {
            int v = arr.get(i);
            for(int j=i-den; j>=0; j-=den) {
                if (v >= arr.get(j)) {
                    break;
                }
                arr.swap(j, j+den);
            }
        }
    }

    private void helper(WrappedArray arr, int den, int mod) {
        if (den >= arr.size()) {
            return;
        }
        helper(arr, den*2, mod);
        helper(arr, den*2, mod+den);
        insertion(arr, den, mod);
    }

    @Override
    public void sort(WrappedArray arr) {
        try{
            helper(arr,1,0);
        }
        catch (WrappedArray.WrappedInterrupt e) {
            Thread.currentThread().interrupt();
        }
    }
}
