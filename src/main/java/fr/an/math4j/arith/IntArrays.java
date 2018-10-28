package fr.an.math4j.arith;

import java.util.Arrays;

public class IntArrays {

	public static int[] range(int n) {
		return range(0, n);
	}
	
	public static boolean isEqualsRange(int[] src) {
		final int len = src.length;
		// TODO test length ?
		return startsWithRange(src, len);
	}

	public static boolean startsWithRange(int[] src, int upToN) {
		for(int i = 0; i < upToN; i++) {
			if (src[i] != i) {
				return false;
			}
		}
		return true;
	}

	public static int[] range(int from, int to) {
		int[] res = new int[to-from];
		for(int i = from; i < to; i++) {
			res[i-from] = i;
		}
		return res;
	}
	
	public static String toString(int[] a) {
		StringBuilder res = new StringBuilder();
		final int len = a.length;
		for(int i = 0; i < len; i++) {
			res.append(Integer.toString(a[i]));
			if (i+1 < len) {
				res.append(", ");
			}
		}
		return res.toString();
	}

	public static String dumpArray(int[] array) {
		return dumpArray(array, 0, array.length);
	}

	public static String dumpArray(int[] array, int from, int to) {
		StringBuilder sb = new StringBuilder(1000);
		final int arrayLen = array.length;
		int maxBegin = Math.min(from, arrayLen);
		for(int i = 1; i < maxBegin; i++) {
			sb.append("[" + i + "]:" + array[i] + " ");
		}
		if (maxBegin < arrayLen) {
			sb.append(" ..... ");
			int minEnd = Math.max(maxBegin, arrayLen - to); 
			for(int i = minEnd; i < arrayLen; i++) {
				sb.append("[" + i + "]:" + array[i] + " ");
			}
		}
		return sb.toString();
	}
	
	public static boolean equals(int[] a1, int[] a2) {
		return Arrays.equals(a1, a2);
	}
	
	public static int findDiff(int[] a, int[] a2) {
        if (a==a2)
            return -1;
        if (a==null || a2==null)
            return 0;

        int length = a.length;
        if (a2.length != length) {
            return Math.max(a.length, a2.length); // not the first diff..
        }
        
        for (int i=0; i<length; i++) {
            if (a[i] != a2[i]) {
                return i;
            }
		}
        return -1;
    }
	
	public static void assertEquals(int[] a, int[] a2) {
		if (a==a2) return;
        if ((a==null) != (a2==null)) {
        	throw new IllegalArgumentException("not equally null");
        }

        int length = a.length;
        if (a2.length != length) {
        	throw new IllegalArgumentException("array length differs: got " + a2.length + " expected " + a.length);
        }
        
        for (int i=0; i<length; i++) {
            if (a[i] != a2[i]) {
            	throw new IllegalArgumentException("element[" + i + "] differs, got " + a2[i] + " expected " + a[i]);
            }
		}
	}
}
