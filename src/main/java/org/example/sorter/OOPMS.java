package org.example.sorter;

import org.example.main.WrappedArray;

public class OOPMS extends MergeS {
    @Override
    public String toString() {
        return "Merge";
    }

    @Override
    protected void merge(WrappedArray a, int p, int m, int k) {
        int[] res = new int[k-p];
        int it1 = p;
        int it2 = m;
        int it3 = 0;
        while (it1 < m && it2 < k) {
            int elt1 = a.get(it1);
            int elt2 = a.get(it2);
            if (elt1 < elt2) {
                res[it3++] = elt1;
                it1++;
            }
            else {
                res[it3++] = elt2;
                it2++;
            }
        }
        while (it1 < m) {
            res[it3++] = a.get(it1++);
        }
        while (it2 < k) {
            res[it3++] = a.get(it2++);
        }
        for (int i = 0; i < k-p; i++) {
            a.set(p+i, res[i]);
        }
    }
}
