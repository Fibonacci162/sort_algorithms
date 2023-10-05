package org.example.sorter;

import org.example.main.WrappedArray;

public class ShellS extends Sorter {
    protected int[] gaps() {
        return new int[]{
            1, 2, 3, 4, 6, 8, 9, 12, 16, 18, 24, 27, 32, 36, 48, 54, 64, 72, 81, 96, 108, 128, 144, 162, 192, 216, 243, 256, 288, 324, 384, 432, 486, 512, 576, 648, 729, 768, 864, 972, 1024, 1152, 1296, 1458, 1536, 1728, 1944, 2048, 2187, 2304, 2592, 2916, 3072, 3456, 3888, 4096, 4374, 4608, 5184, 5832, 6144, 6561, 6912, 7776, 8192, 8748, 9216, 10368, 11664, 12288, 13122, 13824, 15552, 16384, 17496, 18432, 19683, 20736, 23328, 24576, 26244, 27648, 31104, 32768, 34992, 36864, 39366, 41472, 46656, 49152, 52488, 55296, 59049, 62208, 65536, 69984, 73728, 78732, 82944, 93312, 98304, 104976, 110592, 118098, 124416, 131072, 139968, 147456, 157464, 165888, 177147, 186624, 196608, 209952, 221184, 236196, 248832, 262144, 279936, 294912, 314928, 331776, 354294, 373248, 393216, 419904, 442368, 472392, 497664, 524288, 531441, 559872, 589824, 629856, 663552, 708588, 746496, 786432, 839808, 884736, 944784, 995328, 1048576, 1062882, 1119744, 1179648
        };
    }

    @Override
    public String toString() {
        return "Shell, 3 smooth";
    }

    @Override
    public int adjust(int d, int n) {
        return (int)((double)d * Math.log(3) / Math.log(n));
    }

    @Override
    public void sort(WrappedArray arr) {
        try {
            int n = arr.size();
            int[] gaps = gaps();
            int gapi = 0;
            while(2*gaps[gapi] < n) {
                gapi++;
            }
            if (gapi>0) {
                gapi--;
            }
            while (gapi >= 0) {
                int gap = gaps[gapi];
                for(int i=gap; i<n && !Thread.currentThread().isInterrupted(); i++) {
                    for(int j=i; j-gap>=0; j-=gap) {
                        if (arr.get(j) >= arr.get(j-gap)) {
                            break;
                        }
                        arr.swap(j, j-gap);
                    }
                }
                gapi--;
            }
        }
        catch (WrappedArray.WrappedInterrupt e) {
            Thread.currentThread().interrupt();
        }
    }
}
