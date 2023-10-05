package org.example.sorter;

import org.example.main.WrappedArray;

public class WeaveS extends MergeS{
    @Override
    public String toString() {
        return "Weave OOP";
    }

    @Override
    public int adjust(int d, int n) {
        return (int)((Math.log(n) / Math.log(2)) * (double)d / (double)n);
    }

    private void weave(WrappedArray a, int p, int m, int k) {
        int[] aux = new int[k-p+1];
        for(int i=p; i<k; i++) {
            aux[i-p] = a.get(i);
        }
        int i=0;
        int j=m-p;
        int it=p;
        while (it < k) {
            if (i < m-p) {
                a.set(it, aux[i]);
                i++;
                it++;
            }
            if (j < k-p) {
                a.set(it, aux[j]);
                j++;
                it++;
            }
        }
    }

    @Override
    protected void merge(WrappedArray a, int p, int m, int k) {
        weave(a, p, m, k);
        insertion(a,p,k);
    }
}
