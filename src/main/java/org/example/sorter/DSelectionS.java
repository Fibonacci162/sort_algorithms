package org.example.sorter;

import org.example.main.WrappedArray;

public class DSelectionS extends Sorter {
    public String toString() {
        return "Double Selection";
    }

    @Override
    public int adjust(int d, int n) {
        return (int)((Math.log(n) / Math.log(2)) * (double)d / (double)n);
    }

    public void sort(WrappedArray arr) {
        try {
            int beg = 0;
            int en = arr.size();
            while (beg < en) {
                int mni = beg;
                int mxi = beg;
                int mn = arr.get(beg);
                int mx = mn;
                for(int j=beg; j+1<en; j+=2) {
                    int bigger;
                    int smaller;
                    if (arr.get(j) < arr.get(j+1)) {
                        bigger = j+1;
                        smaller = j;
                    }
                    else {
                        bigger = j;
                        smaller = j+1;
                    }
                    int bg = arr.get(bigger);
                    int sm = arr.get(smaller);
                    if (sm < mn) {
                        mni = smaller;
                        mn = sm;
                    }
                    if (bg > mx) {
                        mxi = bigger;
                        mx = bg;
                    }
                }
                int t = arr.get(en-1);
                if (t < mn) {
                    mni = en-1;
                }
                if (t > mx) {
                    mxi = en-1;
                }
                if (mni == mxi) {
                    break;
                }
                if (beg < en-1) {
                    if (mxi == beg && mni == en-1) {
                        arr.swap(beg, en-1);
                    }
                    else if(mxi == beg) {
                        arr.swap(mxi, en-1);
                        arr.swap(mni, beg);
                    }
                    else {
                        arr.swap(mni, beg);
                        arr.swap(mxi, en-1);
                    }
                }
                beg++;
                en--;
            }
        }
        catch (WrappedArray.WrappedInterrupt e) {
            Thread.currentThread().interrupt();
        }
    }
}
