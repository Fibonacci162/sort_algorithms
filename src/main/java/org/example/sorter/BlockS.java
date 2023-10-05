package org.example.sorter;

/* **********************************************************
 WikiSort (public domain license)
 https://github.com/BonzaiThePenguin/WikiSort
 ********************************************************/

import org.example.main.WrappedArray;

import java.util.Comparator;

public class BlockS extends Sorter {
    @Override
    public String toString() {
        return "Block Merge";
    }

    @Override
    public int adjust(int d, int n) {
        return d;
    }

    @Override
    public void sort(WrappedArray arr) {
        try {
            WikiSorter ws = new WikiSorter();
            ws.Sort(arr);
        } catch (WrappedArray.WrappedInterrupt e) {
            Thread.currentThread().interrupt();
        }
    }

// the performance of WikiSort here seems to be completely at the mercy of the JIT compiler
// sometimes it's 40% as fast, sometimes 80%, and either way it's a lot slower than the C code



}

// class to test stable sorting (index will contain its original index in the array, to make sure it doesn't switch places with other items)
class Test {
    public int value;
    public int index;
}

class TestComparator implements Comparator<Test> {
    static int comparisons = 0;

    public int compare(Test a, Test b) {
        comparisons++;
        if (a.value < b.value) return -1;
        if (a.value > b.value) return 1;
        return 0;
    }
}

// structure to represent ranges within the array
class Range {
    public int start;
    public int end;

    public Range(int start1, int end1) {
        start = start1;
        end = end1;
    }

    public Range() {
        start = 0;
        end = 0;
    }

    void set(int start1, int end1) {
        start = start1;
        end = end1;
    }

    int length() {
        return end - start;
    }
}

class Pull {
    public int from, to, count;
    public Range range;

    public Pull() {
        range = new Range(0, 0);
    }

    void reset() {
        range.set(0, 0);
        from = 0;
        to = 0;
        count = 0;
    }
}

// calculate how to scale the index value to the range within the array
// the bottom-up merge sort only operates on values that are powers of two,
// so scale down to that power of two, then use a fraction to scale back again
class Iterator {
    public int size, power_of_two;
    public int numerator, decimal;
    public int denominator, decimal_step, numerator_step;

    // 63 -> 32, 64 -> 64, etc.
    // this comes from Hacker's Delight
    static int FloorPowerOfTwo(int value) {
        int x = value;
        x = x | (x >> 1);
        x = x | (x >> 2);
        x = x | (x >> 4);
        x = x | (x >> 8);
        x = x | (x >> 16);
        return x - (x >> 1);
    }

    Iterator(int size2, int min_level) {
        size = size2;
        power_of_two = FloorPowerOfTwo(size);
        denominator = power_of_two / min_level;
        numerator_step = size % denominator;
        decimal_step = size / denominator;
        begin();
    }

    void begin() {
        numerator = decimal = 0;
    }

    Range nextRange() {
        int start = decimal;

        decimal += decimal_step;
        numerator += numerator_step;
        if (numerator >= denominator) {
            numerator -= denominator;
            decimal++;
        }

        return new Range(start, decimal);
    }

    boolean finished() {
        return (decimal >= size);
    }

    boolean nextLevel() {
        decimal_step += decimal_step;
        numerator_step += numerator_step;
        if (numerator_step >= denominator) {
            numerator_step -= denominator;
            decimal_step++;
        }

        return (decimal_step < size);
    }

    int length() {
        return decimal_step;
    }
}

class WikiSorter {
    // use a small cache to speed up some of the operations
    // since the cache size is fixed, it's still O(1) memory!
    // just keep in mind that making it too small ruins the point (nothing will fit into it),
    // and making it too large also ruins the point (so much for "low memory"!)
    private static int cache_size = 64;
    private int[] cache;

    // note that you can easily modify the above to allocate a dynamically sized cache
    // good choices for the cache size are:
    // (size + 1)/2 – turns into a full-speed standard merge sort since everything fits into the cache
    // sqrt((size + 1)/2) + 1 – this will be the size of the A blocks at the largest level of merges,
    // so a buffer of this size would allow it to skip using internal or in-place merges for anything
    // 512 – chosen from careful testing as a good balance between fixed-size memory use and run time
    // 0 – if the system simply cannot allocate any extra memory whatsoever, no memory works just fine

    public WikiSorter() {
        cache = new int[cache_size];
    }

    // toolbox functions used by the sorter

    // find the index of the first value within the range that is equal to array[index]

    int BinaryFirst(WrappedArray array, int value, Range range) {
        int start = range.start, end = range.end - 1;
        while (start < end) {
            int mid = start + (end - start) / 2;
            if ((array.get(mid) < value))
                start = mid + 1;
            else
                end = mid;
        }
        if (start == range.end - 1 && (array.get(start) < value)) start++;
        return start;
    }

    // find the index of the last value within the range that is equal to array[index], plus 1

    int BinaryLast(WrappedArray array, int value, Range range) {
        int start = range.start, end = range.end - 1;
        while (start < end) {
            int mid = start + (end - start) / 2;
            if (value >= array.get(mid))
                start = mid + 1;
            else
                end = mid;
        }
        if (start == range.end - 1 && (value >= array.get(start))) start++;
        return start;
    }

    // combine a linear search with a binary search to reduce the number of comparisons in situations
    // where have some idea as to how many unique values there are and where the next value might be
    int FindFirstForward(WrappedArray array, int value, Range range, int unique) {
        if (range.length() == 0) return range.start;
        int index, skip = Math.max(range.length() / unique, 1);

        for (index = range.start + skip; (array.get(index - 1) < value); index += skip)
            if (index >= range.end - skip)
                return BinaryFirst(array, value, new Range(index, range.end));

        return BinaryFirst(array, value, new Range(index - skip, index));
    }

    int FindLastForward(WrappedArray array, int value, Range range, int unique) {
        if (range.length() == 0) return range.start;
        int index, skip = Math.max(range.length() / unique, 1);

        for (index = range.start + skip; (value >= array.get(index - 1)); index += skip)
            if (index >= range.end - skip)
                return BinaryLast(array, value, new Range(index, range.end));

        return BinaryLast(array, value, new Range(index - skip, index));
    }

    int FindFirstBackward(WrappedArray array, int value, Range range, int unique) {
        if (range.length() == 0) return range.start;
        int index, skip = Math.max(range.length() / unique, 1);

        for (index = range.end - skip; index > range.start && (array.get(index - 1) >= value); index -= skip)
            if (index < range.start + skip)
                return BinaryFirst(array, value, new Range(range.start, index));

        return BinaryFirst(array, value, new Range(index, index + skip));
    }

    int FindLastBackward(WrappedArray array, int value, Range range, int unique) {
        if (range.length() == 0) return range.start;
        int index, skip = Math.max(range.length() / unique, 1);

        for (index = range.end - skip; index > range.start && (value < array.get(index - 1)); index -= skip)
            if (index < range.start + skip)
                return BinaryLast(array, value, new Range(range.start, index));

        return BinaryLast(array, value, new Range(index, index + skip));
    }

    // n^2 sorting algorithm used to sort tiny chunks of the full array
    void InsertionSort(WrappedArray array, Range range) {
        for (int j, i = range.start + 1; i < range.end; i++) {
            int temp = array.get(i);
            for (j = i; j > range.start && (temp < array.get(j - 1)); j--)
                array.set(j, array.rAccess(j - 1));
            array.set(j, temp);
        }
    }

    void Reverse(WrappedArray array, Range range) {
        for (int index = range.length() / 2 - 1; index >= 0; index--) {
            array.swap(range.start + index, range.end - index - 1);
        }
    }

    // swap a series of values in the array
    void BlockSwap(WrappedArray array, int start1, int start2, int block_size) {
        for (int index = 0; index < block_size; index++) {
            array.swap(start1 + index, start2 + index);
        }
    }

    // rotate the values in an array ([0 1 2 3] becomes [1 2 3 0] if we rotate by 1)
    // this assumes that 0 <= amount <= range.length()
    void Rotate(WrappedArray array, int amount, Range range, boolean use_cache) {
        if (range.length() == 0) return;

        int split;
        if (amount >= 0)
            split = range.start + amount;
        else
            split = range.end + amount;

        Range range1 = new Range(range.start, split);
        Range range2 = new Range(split, range.end);

        if (use_cache) {
            // if the smaller of the two ranges fits into the cache, it's *slightly* faster copying it there and shifting the elements over
            if (range1.length() <= range2.length()) {
                if (range1.length() <= cache_size) {
                    if (cache != null) {
                        arrayCopy(array, range1.start, cache, 0, range1.length());
                        arrayCopy(array, range2.start, array, range1.start, range2.length());
                        arrayCopy(cache, 0, array, range1.start + range2.length(), range1.length());
                    }
                    return;
                }
            } else {
                if (range2.length() <= cache_size) {
                    if (cache != null) {
                        arrayCopy(array, range2.start, cache, 0, range2.length());
                        arrayCopy(array, range1.start, array, range2.end - range1.length(), range1.length());
                        arrayCopy(cache, 0, array, range1.start, range2.length());
                    }
                    return;
                }
            }
        }

        Reverse(array, range1);
        Reverse(array, range2);
        Reverse(array, range);
    }

    // merge two ranges from one array and save the results into a different array
    void MergeInto(WrappedArray from, Range A, Range B, int[] into, int at_index) {
        int A_index = A.start;
        int B_index = B.start;
        int insert_index = at_index;
        int A_last = A.end;
        int B_last = B.end;

        while (true) {
            if (from.get(B_index) >= from.get(A_index)) {
                into[insert_index] = from.get(A_index);
                A_index++;
                insert_index++;
                if (A_index == A_last) {
                    // copy the remainder of B into the final array
                    arrayCopy(from, B_index, into, insert_index, B_last - B_index);
                    break;
                }
            } else {
                into[insert_index] = from.get(B_index);
                B_index++;
                insert_index++;
                if (B_index == B_last) {
                    // copy the remainder of A into the final array
                    arrayCopy(from, A_index, into, insert_index, A_last - A_index);
                    break;
                }
            }
        }
    }

    void MergeInto(int[] from, Range A, Range B, WrappedArray into, int at_index) {
        int A_index = A.start;
        int B_index = B.start;
        int insert_index = at_index;
        int A_last = A.end;
        int B_last = B.end;

        while (true) {
            if (from[B_index] >= from[A_index]) {
                into.set(insert_index, from[A_index]);
                A_index++;
                insert_index++;
                if (A_index == A_last) {
                    // copy the remainder of B into the final array
                    arrayCopy(from, B_index, into, insert_index, B_last - B_index);
                    break;
                }
            } else {
                into.set(insert_index, from[B_index]);
                B_index++;
                insert_index++;
                if (B_index == B_last) {
                    // copy the remainder of A into the final array
                    arrayCopy(from, A_index, into, insert_index, A_last - A_index);
                    break;
                }
            }
        }
    }

    void MergeExternal(WrappedArray array, Range A, Range B) {
        // A fits into the cache, so use that instead of the internal buffer
        int A_index = 0;
        int B_index = B.start;
        int insert_index = A.start;
        int A_last = A.length();
        int B_last = B.end;

        if (B.length() > 0 && A.length() > 0) {
            while (true) {
                if ((array.get(B_index) >= cache[A_index])) {
                    array.set(insert_index, cache[A_index]);
                    A_index++;
                    insert_index++;
                    if (A_index == A_last) break;
                } else {
                    array.set(insert_index, array.get(B_index));
                    B_index++;
                    insert_index++;
                    if (B_index == B_last) break;
                }
            }
        }

        // copy the remainder of A into the final array
        if (cache != null) arrayCopy(cache, A_index, array, insert_index, A_last - A_index);
    }

    // merge operation using an internal buffer
    void MergeInternal(WrappedArray array, Range A, Range B, Range buffer) {
        // whenever we find a value to add to the final array, swap it with the value that's already in that spot
        // when this algorithm is finished, 'buffer' will contain its original contents, but in a different order
        int A_count = 0, B_count = 0, insert = 0;

        if (B.length() > 0 && A.length() > 0) {
            while (true) {
                if ((array.get(B.start + B_count) >= array.get(buffer.start + A_count))) {
                    array.swap(A.start + insert, buffer.start + A_count);
                    A_count++;
                    insert++;
                    if (A_count >= A.length()) break;
                } else {
                    array.swap(A.start + insert, B.start + B_count);
                    B_count++;
                    insert++;
                    if (B_count >= B.length()) break;
                }
            }
        }

        // swap the remainder of A into the final array
        BlockSwap(array, buffer.start + A_count, A.start + insert, A.length() - A_count);
    }

    // merge operation without a buffer
    void MergeInPlace(WrappedArray array, Range A, Range B) {
        if (A.length() == 0 || B.length() == 0) return;

		/*
		 this just repeatedly binary searches into B and rotates A into position.
		 the paper suggests using the 'rotation-based Hwang and Lin algorithm' here,
		 but I decided to stick with this because it had better situational performance

		 (Hwang and Lin is designed for merging subarrays of very different sizes,
		 but WikiSort almost always uses subarrays that are roughly the same size)

		 normally this is incredibly suboptimal, but this function is only called
		 when none of the A or B blocks in any subarray contained 2√A unique values,
		 which places a hard limit on the number of times this will ACTUALLY need
		 to binary search and rotate.

		 according to my analysis the worst case is √A rotations performed on √A items
		 once the constant factors are removed, which ends up being O(n)

		 again, this is NOT a general-purpose solution – it only works well in this case!
		 kind of like how the O(n^2) insertion sort is used in some places
		 */

        A = new Range(A.start, A.end);
        B = new Range(B.start, B.end);

        while (true) {
            // find the first place in B where the first item in A needs to be inserted
            int mid = BinaryFirst(array, array.get(A.start), B);

            // rotate A into place
            int amount = mid - A.end;
            Rotate(array, -amount, new Range(A.start, mid), true);
            if (B.end == mid) break;

            // calculate the new A and B ranges
            B.start = mid;
            A.set(A.start + amount, B.start);
            A.start = BinaryLast(array, array.get(A.start), A);
            if (A.length() == 0) break;
        }
    }

    void NetSwap(WrappedArray array, int[] order, Range range, int x, int y) {
        int compare = array.get(range.start + x) - array.get(range.start + y);
        if (compare > 0 || (order[x] > order[y] && compare == 0)) {
            array.swap(range.start+x, range.start+y);
            int swap2 = order[x];
            order[x] = order[y];
            order[y] = swap2;
        }
    }

    void arrayCopy(WrappedArray src, int srcpos, int[] dest, int destpos, int length) {
        for (int i=0; i<length; i++) {
            dest[i+destpos] = src.get(i+srcpos);
        }
    }

    void arrayCopy(int[] src, int srcpos, WrappedArray dest, int destpos, int length) {
        for (int i=0; i<length; i++) {
            dest.set(i+destpos, src[i+srcpos]);
        }
    }

    void arrayCopy(WrappedArray src, int srcpos, WrappedArray dest, int destpos, int length) {
        int[] temp = new int[length];
        for (int i=0; i<length; i++) {
            temp[i] = src.get(i+srcpos);
        }
        for (int i=0; i<length; i++) {
            dest.set(i+destpos, temp[i]);
        }
    }

    // bottom-up merge sort combined with an in-place merge algorithm for O(1) memory use
    void Sort(WrappedArray array) {
        int size = array.size();

        // if the array is of size 0, 1, 2, or 3, just sort them like so:
        if (size < 4) {
            if (size == 3) {
                // hard-coded insertion sort
                if (array.get(1) < array.get(0)) {
                    array.swap(0, 1);
                }
                if (array.get(2) < array.get(1)) {
                    array.swap(2,1);
                    if (array.get(1) < array.get(0)) {
                        array.swap(0, 1);
                    }
                }
            } else if (size == 2) {
                // swap the items if they're out of order
                if (array.get(1) < array.get(0)) {
                    array.swap(0, 1);
                }
            }

            return;
        }

        // sort groups of 4-8 items at a time using an unstable sorting network,
        // but keep track of the original item orders to force it to be stable
        // http://pages.ripco.net/~jgamble/nw.html
        Iterator iterator = new Iterator(size, 4);
        while (!iterator.finished()) {
            int[] order = {0, 1, 2, 3, 4, 5, 6, 7};
            Range range = iterator.nextRange();

            if (range.length() == 8) {
                NetSwap(array, order, range, 0, 1);
                NetSwap(array, order, range, 2, 3);
                NetSwap(array, order, range, 4, 5);
                NetSwap(array, order, range, 6, 7);
                NetSwap(array, order, range, 0, 2);
                NetSwap(array, order, range, 1, 3);
                NetSwap(array, order, range, 4, 6);
                NetSwap(array, order, range, 5, 7);
                NetSwap(array, order, range, 1, 2);
                NetSwap(array, order, range, 5, 6);
                NetSwap(array, order, range, 0, 4);
                NetSwap(array, order, range, 3, 7);
                NetSwap(array, order, range, 1, 5);
                NetSwap(array, order, range, 2, 6);
                NetSwap(array, order, range, 1, 4);
                NetSwap(array, order, range, 3, 6);
                NetSwap(array, order, range, 2, 4);
                NetSwap(array, order, range, 3, 5);
                NetSwap(array, order, range, 3, 4);

            } else if (range.length() == 7) {
                NetSwap(array, order, range, 1, 2);
                NetSwap(array, order, range, 3, 4);
                NetSwap(array, order, range, 5, 6);
                NetSwap(array, order, range, 0, 2);
                NetSwap(array, order, range, 3, 5);
                NetSwap(array, order, range, 4, 6);
                NetSwap(array, order, range, 0, 1);
                NetSwap(array, order, range, 4, 5);
                NetSwap(array, order, range, 2, 6);
                NetSwap(array, order, range, 0, 4);
                NetSwap(array, order, range, 1, 5);
                NetSwap(array, order, range, 0, 3);
                NetSwap(array, order, range, 2, 5);
                NetSwap(array, order, range, 1, 3);
                NetSwap(array, order, range, 2, 4);
                NetSwap(array, order, range, 2, 3);

            } else if (range.length() == 6) {
                NetSwap(array, order, range, 1, 2);
                NetSwap(array, order, range, 4, 5);
                NetSwap(array, order, range, 0, 2);
                NetSwap(array, order, range, 3, 5);
                NetSwap(array, order, range, 0, 1);
                NetSwap(array, order, range, 3, 4);
                NetSwap(array, order, range, 2, 5);
                NetSwap(array, order, range, 0, 3);
                NetSwap(array, order, range, 1, 4);
                NetSwap(array, order, range, 2, 4);
                NetSwap(array, order, range, 1, 3);
                NetSwap(array, order, range, 2, 3);

            } else if (range.length() == 5) {
                NetSwap(array, order, range, 0, 1);
                NetSwap(array, order, range, 3, 4);
                NetSwap(array, order, range, 2, 4);
                NetSwap(array, order, range, 2, 3);
                NetSwap(array, order, range, 1, 4);
                NetSwap(array, order, range, 0, 3);
                NetSwap(array, order, range, 0, 2);
                NetSwap(array, order, range, 1, 3);
                NetSwap(array, order, range, 1, 2);

            } else if (range.length() == 4) {
                NetSwap(array, order, range, 0, 1);
                NetSwap(array, order, range, 2, 3);
                NetSwap(array, order, range, 0, 2);
                NetSwap(array, order, range, 1, 3);
                NetSwap(array, order, range, 1, 2);
            }
        }
        if (size < 8) return;

        // we need to keep track of a lot of ranges during this sort!
        Range buffer1 = new Range(), buffer2 = new Range();
        Range blockA = new Range(), blockB = new Range();
        Range lastA = new Range(), lastB = new Range();
        Range firstA = new Range();
        Range A = new Range(), B = new Range();

        Pull[] pull = new Pull[2];
        pull[0] = new Pull();
        pull[1] = new Pull();

        // then merge sort the higher levels, which can be 8-15, 16-31, 32-63, 64-127, etc.
        while (true) {

            // if every A and B block will fit into the cache, use a special branch specifically for merging with the cache
            // (we use < rather than <= since the block size might be one more than iterator.length())
            if (iterator.length() < cache_size) {

                // if four subarrays fit into the cache, it's faster to merge both pairs of subarrays into the cache,
                // then merge the two merged subarrays from the cache back into the original array
                if ((iterator.length() + 1) * 4 <= cache_size && iterator.length() * 4 <= size) {
                    iterator.begin();
                    while (!iterator.finished()) {
                        // merge A1 and B1 into the cache
                        Range A1 = iterator.nextRange();
                        Range B1 = iterator.nextRange();
                        Range A2 = iterator.nextRange();
                        Range B2 = iterator.nextRange();

                        if (array.get(B1.end - 1) < array.get(A1.start)) {
                            // the two ranges are in reverse order, so copy them in reverse order into the cache
                            arrayCopy(array, A1.start, cache, B1.length(), A1.length());
                            arrayCopy(array, B1.start, cache, 0, B1.length());
                        } else if (array.get(B1.start) < array.get(A1.end - 1)) {
                            // these two ranges weren't already in order, so merge them into the cache
                            MergeInto(array, A1, B1, cache, 0);
                        } else {
                            // if A1, B1, A2, and B2 are all in order, skip doing anything else
                            if ((array.get(B2.start) >= array.get(A2.end - 1)) && (array.get(A2.start) >= array.get(B1.end - 1)))
                                continue;

                            // copy A1 and B1 into the cache in the same order
                            arrayCopy(array, A1.start, cache, 0, A1.length());
                            arrayCopy(array, B1.start, cache, A1.length(), B1.length());
                        }
                        A1.set(A1.start, B1.end);

                        // merge A2 and B2 into the cache
                        if ((array.get(B2.end - 1) < array.get(A2.start))) {
                            // the two ranges are in reverse order, so copy them in reverse order into the cache
                            arrayCopy(array, A2.start, cache, A1.length() + B2.length(), A2.length());
                            arrayCopy(array, B2.start, cache, A1.length(), B2.length());
                        } else if (array.get(B2.start) < array.get(A2.end - 1)) {
                            // these two ranges weren't already in order, so merge them into the cache
                            MergeInto(array, A2, B2, cache, A1.length());
                        } else {
                            // copy A2 and B2 into the cache in the same order
                            arrayCopy(array, A2.start, cache, A1.length(), A2.length());
                            arrayCopy(array, B2.start, cache, A1.length() + A2.length(), B2.length());
                        }
                        A2.set(A2.start, B2.end);

                        // merge A1 and A2 from the cache into the array
                        Range A3 = new Range(0, A1.length());
                        Range B3 = new Range(A1.length(), A1.length() + A2.length());

                        if ((cache[B3.end - 1] < cache[A3.start])) {
                            // the two ranges are in reverse order, so copy them in reverse order into the cache
                            arrayCopy(cache, A3.start, array, A1.start + A2.length(), A3.length());
                            arrayCopy(cache, B3.start, array, A1.start, B3.length());
                        } else if ((cache[B3.start] < cache[A3.end - 1])) {
                            // these two ranges weren't already in order, so merge them back into the array
                            MergeInto(cache, A3, B3, array, A1.start);
                        } else {
                            // copy A3 and B3 into the array in the same order
                            arrayCopy(cache, A3.start, array, A1.start, A3.length());
                            arrayCopy(cache, B3.start, array, A1.start + A1.length(), B3.length());
                        }
                    }

                    // we merged two levels at the same time, so we're done with this level already
                    // (iterator.nextLevel() is called again at the bottom of this outer merge loop)
                    iterator.nextLevel();

                } else {
                    iterator.begin();
                    while (!iterator.finished()) {
                        A = iterator.nextRange();
                        B = iterator.nextRange();

                        if (array.get(B.end - 1) < array.get(A.start)) {
                            // the two ranges are in reverse order, so a simple rotation should fix it
                            Rotate(array, A.length(), new Range(A.start, B.end), true);
                        } else if (array.get(B.start) < array.get(A.end - 1)) {
                            // these two ranges weren't already in order, so we'll need to merge them!
                            arrayCopy(array, A.start, cache, 0, A.length());
                            MergeExternal(array, A, B);
                        }
                    }
                }
            } else {
                // this is where the in-place merge logic starts!
                // 1. pull out two internal buffers each containing √A unique values
                //     1a. adjust block_size and buffer_size if we couldn't find enough unique values
                // 2. loop over the A and B subarrays within this level of the merge sort
                //     3. break A and B into blocks of size 'block_size'
                //     4. "tag" each of the A blocks with values from the first internal buffer
                //     5. roll the A blocks through the B blocks and drop/rotate them where they belong
                //     6. merge each A block with any B values that follow, using the cache or the second internal buffer
                // 7. sort the second internal buffer if it exists
                // 8. redistribute the two internal buffers back into the array

                int block_size = (int) Math.sqrt(iterator.length());
                int buffer_size = iterator.length() / block_size + 1;

                // as an optimization, we really only need to pull out the internal buffers once for each level of merges
                // after that we can reuse the same buffers over and over, then redistribute it when we're finished with this level
                int index, last, count, pull_index = 0;
                buffer1.set(0, 0);
                buffer2.set(0, 0);

                pull[0].reset();
                pull[1].reset();

                // find two internal buffers of size 'buffer_size' each
                int find = buffer_size + buffer_size;
                boolean find_separately = false;

                if (block_size <= cache_size) {
                    // if every A block fits into the cache then we won't need the second internal buffer,
                    // so we really only need to find 'buffer_size' unique values
                    find = buffer_size;
                } else if (find > iterator.length()) {
                    // we can't fit both buffers into the same A or B subarray, so find two buffers separately
                    find = buffer_size;
                    find_separately = true;
                }

                // we need to find either a single contiguous space containing 2√A unique values (which will be split up into two buffers of size √A each),
                // or we need to find one buffer of < 2√A unique values, and a second buffer of √A unique values,
                // OR if we couldn't find that many unique values, we need the largest possible buffer we can get

                // in the case where it couldn't find a single buffer of at least √A unique values,
                // all of the Merge steps must be replaced by a different merge algorithm (MergeInPlace)

                iterator.begin();
                while (!iterator.finished()) {
                    A = iterator.nextRange();
                    B = iterator.nextRange();

                    // check A for the number of unique values we need to fill an internal buffer
                    // these values will be pulled out to the start of A
                    for (last = A.start, count = 1; count < find; last = index, count++) {
                        index = FindLastForward(array, array.get(last), new Range(last + 1, A.end), find - count);
                        if (index == A.end) break;
                    }
                    index = last;

                    if (count >= buffer_size) {
                        // keep track of the range within the array where we'll need to "pull out" these values to create the internal buffer
                        pull[pull_index].range.set(A.start, B.end);
                        pull[pull_index].count = count;
                        pull[pull_index].from = index;
                        pull[pull_index].to = A.start;
                        pull_index = 1;

                        if (count == buffer_size + buffer_size) {
                            // we were able to find a single contiguous section containing 2√A unique values,
                            // so this section can be used to contain both of the internal buffers we'll need
                            buffer1.set(A.start, A.start + buffer_size);
                            buffer2.set(A.start + buffer_size, A.start + count);
                            break;
                        } else if (find == buffer_size + buffer_size) {
                            // we found a buffer that contains at least √A unique values, but did not contain the full 2√A unique values,
                            // so we still need to find a second separate buffer of at least √A unique values
                            buffer1.set(A.start, A.start + count);
                            find = buffer_size;
                        } else if (block_size <= cache_size) {
                            // we found the first and only internal buffer that we need, so we're done!
                            buffer1.set(A.start, A.start + count);
                            break;
                        } else if (find_separately) {
                            // found one buffer, but now find the other one
                            buffer1 = new Range(A.start, A.start + count);
                            find_separately = false;
                        } else {
                            // we found a second buffer in an 'A' subarray containing √A unique values, so we're done!
                            buffer2.set(A.start, A.start + count);
                            break;
                        }
                    } else if (pull_index == 0 && count > buffer1.length()) {
                        // keep track of the largest buffer we were able to find
                        buffer1.set(A.start, A.start + count);

                        pull[pull_index].range.set(A.start, B.end);
                        pull[pull_index].count = count;
                        pull[pull_index].from = index;
                        pull[pull_index].to = A.start;
                    }

                    // check B for the number of unique values we need to fill an internal buffer
                    // these values will be pulled out to the end of B
                    for (last = B.end - 1, count = 1; count < find; last = index - 1, count++) {
                        index = FindFirstBackward(array, array.get(last), new Range(B.start, last), find - count);
                        if (index == B.start) break;
                    }
                    index = last;

                    if (count >= buffer_size) {
                        // keep track of the range within the array where we'll need to "pull out" these values to create the internal buffer
                        pull[pull_index].range.set(A.start, B.end);
                        pull[pull_index].count = count;
                        pull[pull_index].from = index;
                        pull[pull_index].to = B.end;
                        pull_index = 1;

                        if (count == buffer_size + buffer_size) {
                            // we were able to find a single contiguous section containing 2√A unique values,
                            // so this section can be used to contain both of the internal buffers we'll need
                            buffer1.set(B.end - count, B.end - buffer_size);
                            buffer2.set(B.end - buffer_size, B.end);
                            break;
                        } else if (find == buffer_size + buffer_size) {
                            // we found a buffer that contains at least √A unique values, but did not contain the full 2√A unique values,
                            // so we still need to find a second separate buffer of at least √A unique values
                            buffer1.set(B.end - count, B.end);
                            find = buffer_size;
                        } else if (block_size <= cache_size) {
                            // we found the first and only internal buffer that we need, so we're done!
                            buffer1.set(B.end - count, B.end);
                            break;
                        } else if (find_separately) {
                            // found one buffer, but now find the other one
                            buffer1 = new Range(B.end - count, B.end);
                            find_separately = false;
                        } else {
                            // buffer2 will be pulled out from a 'B' subarray, so if the first buffer was pulled out from the corresponding 'A' subarray,
                            // we need to adjust the end point for that A subarray so it knows to stop redistributing its values before reaching buffer2
                            if (pull[0].range.start == A.start) pull[0].range.end -= pull[1].count;

                            // we found a second buffer in an 'B' subarray containing √A unique values, so we're done!
                            buffer2.set(B.end - count, B.end);
                            break;
                        }
                    } else if (pull_index == 0 && count > buffer1.length()) {
                        // keep track of the largest buffer we were able to find
                        buffer1.set(B.end - count, B.end);

                        pull[pull_index].range.set(A.start, B.end);
                        pull[pull_index].count = count;
                        pull[pull_index].from = index;
                        pull[pull_index].to = B.end;
                    }
                }

                // pull out the two ranges so we can use them as internal buffers
                for (pull_index = 0; pull_index < 2; pull_index++) {
                    int length = pull[pull_index].count;

                    if (pull[pull_index].to < pull[pull_index].from) {
                        // we're pulling the values out to the left, which means the start of an A subarray
                        index = pull[pull_index].from;
                        for (count = 1; count < length; count++) {
                            index = FindFirstBackward(array, array.get(index - 1), new Range(pull[pull_index].to, pull[pull_index].from - (count - 1)), length - count);
                            Range range = new Range(index + 1, pull[pull_index].from + 1);
                            Rotate(array, range.length() - count, range, true);
                            pull[pull_index].from = index + count;
                        }
                    } else if (pull[pull_index].to > pull[pull_index].from) {
                        // we're pulling values out to the right, which means the end of a B subarray
                        index = pull[pull_index].from + 1;
                        for (count = 1; count < length; count++) {
                            index = FindLastForward(array, array.get(index), new Range(index, pull[pull_index].to), length - count);
                            Range range = new Range(pull[pull_index].from, index - 1);
                            Rotate(array, count, range, true);
                            pull[pull_index].from = index - 1 - count;
                        }
                    }
                }

                // adjust block_size and buffer_size based on the values we were able to pull out
                buffer_size = buffer1.length();
                block_size = iterator.length() / buffer_size + 1;

                // the first buffer NEEDS to be large enough to tag each of the evenly sized A blocks,
                // so this was originally here to test the math for adjusting block_size above
                //if ((iterator.length() + 1)/block_size > buffer_size) throw new RuntimeException();

                // now that the two internal buffers have been created, it's time to merge each A+B combination at this level of the merge sort!
                iterator.begin();
                while (!iterator.finished()) {
                    A = iterator.nextRange();
                    B = iterator.nextRange();

                    // remove any parts of A or B that are being used by the internal buffers
                    int start = A.start;
                    if (start == pull[0].range.start) {
                        if (pull[0].from > pull[0].to) {
                            A.start += pull[0].count;

                            // if the internal buffer takes up the entire A or B subarray, then there's nothing to merge
                            // this only happens for very small subarrays, like √4 = 2, 2 * (2 internal buffers) = 4,
                            // which also only happens when cache_size is small or 0 since it'd otherwise use MergeExternal
                            if (A.length() == 0) continue;
                        } else if (pull[0].from < pull[0].to) {
                            B.end -= pull[0].count;
                            if (B.length() == 0) continue;
                        }
                    }
                    if (start == pull[1].range.start) {
                        if (pull[1].from > pull[1].to) {
                            A.start += pull[1].count;
                            if (A.length() == 0) continue;
                        } else if (pull[1].from < pull[1].to) {
                            B.end -= pull[1].count;
                            if (B.length() == 0) continue;
                        }
                    }

                    if ((array.get(B.end - 1) < array.get(A.start))) {
                        // the two ranges are in reverse order, so a simple rotation should fix it
                        Rotate(array, A.length(), new Range(A.start, B.end), true);
                    } else if ((array.get(A.end) < array.get(A.end - 1))) {
                        // these two ranges weren't already in order, so we'll need to merge them!

                        // break the remainder of A into blocks. firstA is the uneven-sized first A block
                        blockA.set(A.start, A.end);
                        firstA.set(A.start, A.start + blockA.length() % block_size);

                        // swap the first value of each A block with the value in buffer1
                        int indexA = buffer1.start;
                        for (index = firstA.end; index < blockA.end; index += block_size) {
                            array.swap(indexA, index);
                            indexA++;
                        }

                        // start rolling the A blocks through the B blocks!
                        // whenever we leave an A block behind, we'll need to merge the previous A block with any B blocks that follow it, so track that information as well
                        lastA.set(firstA.start, firstA.end);
                        lastB.set(0, 0);
                        blockB.set(B.start, B.start + Math.min(block_size, B.length()));
                        blockA.start += firstA.length();
                        indexA = buffer1.start;

                        // if the first unevenly sized A block fits into the cache, copy it there for when we go to Merge it
                        // otherwise, if the second buffer is available, block swap the contents into that
                        if (lastA.length() <= cache_size && cache != null)
                            arrayCopy(array, lastA.start, cache, 0, lastA.length());
                        else if (buffer2.length() > 0)
                            BlockSwap(array, lastA.start, buffer2.start, lastA.length());

                        if (blockA.length() > 0) {
                            while (true) {
                                // if there's a previous B block and the first value of the minimum A block is <= the last value of the previous B block,
                                // then drop that minimum A block behind. or if there are no B blocks left then keep dropping the remaining A blocks.
                                if ((lastB.length() > 0 && (array.get(lastB.end - 1) >= array.get(indexA))) || blockB.length() == 0) {
                                    // figure out where to split the previous B block, and rotate it at the split
                                    int B_split = BinaryFirst(array, array.get(indexA), lastB);
                                    int B_remaining = lastB.end - B_split;

                                    // swap the minimum A block to the beginning of the rolling A blocks
                                    int minA = blockA.start;
                                    for (int findA = minA + block_size; findA < blockA.end; findA += block_size)
                                        if ((array.get(findA) < array.get(minA)))
                                            minA = findA;
                                    BlockSwap(array, blockA.start, minA, block_size);

                                    // swap the first item of the previous A block back with its original value, which is stored in buffer1
                                    array.swap(blockA.start, indexA);
                                    indexA++;

                                    // locally merge the previous A block with the B values that follow it
                                    // if lastA fits into the external cache we'll use that (with MergeExternal),
                                    // or if the second internal buffer exists we'll use that (with MergeInternal),
                                    // or failing that we'll use a strictly in-place merge algorithm (MergeInPlace)
                                    if (lastA.length() <= cache_size)
                                        MergeExternal(array, lastA, new Range(lastA.end, B_split));
                                    else if (buffer2.length() > 0)
                                        MergeInternal(array, lastA, new Range(lastA.end, B_split), buffer2);
                                    else
                                        MergeInPlace(array, lastA, new Range(lastA.end, B_split));

                                    if (buffer2.length() > 0 || block_size <= cache_size) {
                                        // copy the previous A block into the cache or buffer2, since that's where we need it to be when we go to merge it anyway
                                        if (block_size <= cache_size)
                                            arrayCopy(array, blockA.start, cache, 0, block_size);
                                        else
                                            BlockSwap(array, blockA.start, buffer2.start, block_size);

                                        // this is equivalent to rotating, but faster
                                        // the area normally taken up by the A block is either the contents of buffer2, or data we don't need anymore since we memcopied it
                                        // either way, we don't need to retain the order of those items, so instead of rotating we can just block swap B to where it belongs
                                        BlockSwap(array, B_split, blockA.start + block_size - B_remaining, B_remaining);
                                    } else {
                                        // we are unable to use the 'buffer2' trick to speed up the rotation operation since buffer2 doesn't exist, so perform a normal rotation
                                        Rotate(array, blockA.start - B_split, new Range(B_split, blockA.start + block_size), true);
                                    }

                                    // update the range for the remaining A blocks, and the range remaining from the B block after it was split
                                    lastA.set(blockA.start - B_remaining, blockA.start - B_remaining + block_size);
                                    lastB.set(lastA.end, lastA.end + B_remaining);

                                    // if there are no more A blocks remaining, this step is finished!
                                    blockA.start += block_size;
                                    if (blockA.length() == 0)
                                        break;

                                } else if (blockB.length() < block_size) {
                                    // move the last B block, which is unevenly sized, to before the remaining A blocks, by using a rotation
                                    // the cache is disabled here since it might contain the contents of the previous A block
                                    Rotate(array, -blockB.length(), new Range(blockA.start, blockB.end), false);

                                    lastB.set(blockA.start, blockA.start + blockB.length());
                                    blockA.start += blockB.length();
                                    blockA.end += blockB.length();
                                    blockB.end = blockB.start;
                                } else {
                                    // roll the leftmost A block to the end by swapping it with the next B block
                                    BlockSwap(array, blockA.start, blockB.start, block_size);
                                    lastB.set(blockA.start, blockA.start + block_size);

                                    blockA.start += block_size;
                                    blockA.end += block_size;
                                    blockB.start += block_size;
                                    blockB.end += block_size;

                                    if (blockB.end > B.end)
                                        blockB.end = B.end;
                                }
                            }
                        }

                        // merge the last A block with the remaining B values
                        if (lastA.length() <= cache_size)
                            MergeExternal(array, lastA, new Range(lastA.end, B.end));
                        else if (buffer2.length() > 0)
                            MergeInternal(array, lastA, new Range(lastA.end, B.end), buffer2);
                        else
                            MergeInPlace(array, lastA, new Range(lastA.end, B.end));
                    }
                }

                // when we're finished with this merge step we should have the one or two internal buffers left over, where the second buffer is all jumbled up
                // insertion sort the second buffer, then redistribute the buffers back into the array using the opposite process used for creating the buffer

                // while an unstable sort like quick sort could be applied here, in benchmarks it was consistently slightly slower than a simple insertion sort,
                // even for tens of millions of items. this may be because insertion sort is quite fast when the data is already somewhat sorted, like it is here
                InsertionSort(array, buffer2);

                for (pull_index = 0; pull_index < 2; pull_index++) {
                    int unique = pull[pull_index].count * 2;
                    if (pull[pull_index].from > pull[pull_index].to) {
                        // the values were pulled out to the left, so redistribute them back to the right
                        Range buffer = new Range(pull[pull_index].range.start, pull[pull_index].range.start + pull[pull_index].count);
                        while (buffer.length() > 0) {
                            index = FindFirstForward(array, array.get(buffer.start), new Range(buffer.end, pull[pull_index].range.end), unique);
                            int amount = index - buffer.end;
                            Rotate(array, buffer.length(), new Range(buffer.start, index), true);
                            buffer.start += (amount + 1);
                            buffer.end += amount;
                            unique -= 2;
                        }
                    } else if (pull[pull_index].from < pull[pull_index].to) {
                        // the values were pulled out to the right, so redistribute them back to the left
                        Range buffer = new Range(pull[pull_index].range.end - pull[pull_index].count, pull[pull_index].range.end);
                        while (buffer.length() > 0) {
                            index = FindLastBackward(array, array.get(buffer.end - 1), new Range(pull[pull_index].range.start, buffer.start), unique);
                            int amount = buffer.start - index;
                            Rotate(array, amount, new Range(index, buffer.end), true);
                            buffer.start -= amount;
                            buffer.end -= (amount + 1);
                            unique -= 2;
                        }
                    }
                }
            }

            // double the size of each A and B subarray that will be merged in the next level
            if (!iterator.nextLevel()) break;
        }
    }
}