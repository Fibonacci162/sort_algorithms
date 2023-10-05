package org.example.sorter;

import org.example.main.WrappedArray;

public abstract class MergeS extends Sorter {
    @Override
    public abstract String toString();

    @Override
    public int adjust(int d, int n) {
        return d;
    }

    protected void insertion(WrappedArray arr, int p, int k) {
        for(int i=p+1; i<k && !Thread.currentThread().isInterrupted(); i++) {
            int v = arr.get(i);
            for(int j=i-1; j>=p; j--) {
                if (v >= arr.get(j)) {
                    break;
                }
                arr.swap(j, j+1);
            }
        }
    }

    protected abstract void merge(WrappedArray a, int p, int m, int k);

    protected int minS() {
        return 1;
    }

    protected void mergeSort(WrappedArray a, int p, int k) {
        if (k-p <= minS()) {
            insertion(a, p ,k);
        }
        else {
            int m = (p+k)/2;
            mergeSort(a,p,m);
            mergeSort(a,m,k);
            merge(a,p,m,k);
        }
    }

    @Override
    public void sort(WrappedArray arr) {
        try {
            mergeSort(arr, 0, arr.size());
        }
        catch (WrappedArray.WrappedInterrupt e) {
            Thread.currentThread().interrupt();
        }
    }
}
