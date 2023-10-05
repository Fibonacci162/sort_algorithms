package org.example.sorter;

import org.example.main.WrappedArray;

public class TimS extends MergeS{
    @Override
    public String toString() {
        return "Tim";
    }

    @Override
    protected int minS() {
        return 16;
    }

    private int binSearch(WrappedArray a, int p, int k, int v) {
        if (p < k-1) {
            int m = (p+k)/2;
            int w = a.get(m);
            if (v == w) {
                return m;
            }
            else if (v > w) {
                return binSearch(a, m+1, k, v);
            }
            else {
                return binSearch(a, p, m, v);
            }
        }
        return p;
    }

    @Override
    protected void merge(WrappedArray a, int p, int m, int k) {
        int p1 = binSearch(a, p, m, a.get(m));
        int k1 = binSearch(a, m, k, a.get(m-1));
        p = Math.max(p,p1-1);
        k = Math.min(k, k1+1);
        if (m-p <= k-m) {
            int[] buf = new int[m-p];
            for (int i=0; i<m-p; i++) {
                buf[i] = a.get(p+i);
            }
            int it1 = 0;
            int it2 = m;
            int it3 = p;
            while(it1 < m-p && it2 < k) {
                int elt = a.get(it2);
                if (buf[it1] < elt) {
                    a.set(it3++, buf[it1++]);
                }
                else {
                    a.set(it3++, elt);
                    it2++;
                }
            }
            while (it1 < m-p) {
                a.set(it3++, buf[it1++]);
            }
        }
        else {
            int[] buf = new int[k-m];
            for (int i=0; i<k-m; i++) {
                buf[i] = a.get(m+i);
            }
            int it1 = m-1;
            int it2 = k-m-1;
            int it3 = k-1;
            while(it1 >= p && it2 >= 0) {
                int elt = a.get(it1);
                if (elt > buf[it2]) {
                    a.set(it3--, elt);
                    it1--;
                }
                else {
                    a.set(it3--, buf[it2--]);
                }
            }
            while (it2 >= 0) {
                a.set(it3--, buf[it2--]);
            }
        }
    }

    @Override
    protected void mergeSort(WrappedArray a, int p, int k) {
        boolean descending = true;
        for (int i=p+1; i<k; i++) {
            if (a.rAccess(i) >= a.rAccess(i-1)) {
                descending = false;
                break;
            }
        }
        if (descending) {
            a.flip(p, k);
        }
        else if (k-p <= minS()) {
            insertion(a, p ,k);
        }
        else {
            int m = (p+k)/2;
            mergeSort(a,p,m);
            mergeSort(a,m,k);
            merge(a,p,m,k);
        }
    }
}
