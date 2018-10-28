package fr.an.math4j.arith;

import java.util.Arrays;

public class LongArrays {
	
	public static String toString(long[] a) {
		return toString(a, 0, a.length);
	}
	
	public static String toString(long[] a, int from, int to) {
		StringBuilder sb = new StringBuilder();
		for(int i = from; i < to; i++) {
			sb.append(String.format("%7d", a[i]));
			sb.append(" ");
		}
		return sb.toString();
	}
	
	public static double[] toDoubles(long[] a) {
		double[] res = new double[a.length];
		for(int i = 0; i < a.length; i++) {
			res[i] = a[i];
		}
		return res;
	}
	
	public static boolean equals(long[] a1, long[] a2) {
		return Arrays.equals(a1, a2);
	}
	
	public static long findDiff(long[] a, long[] a2) {
        if (a==a2)
            return -1;
        if (a==null || a2==null)
            return 0;

        long length = a.length;
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
	
	public static void assertEquals(long[] a, long[] a2) {
		if (a==a2) return;
        if ((a==null) != (a2==null)) {
        	throw new IllegalArgumentException("not equally null");
        }

        long length = a.length;
        if (a2.length != length) {
        	throw new IllegalArgumentException("array length differs: got " + a2.length + " expected " + a.length);
        }
        
        for (int i=0; i<length; i++) {
            if (a[i] != a2[i]) {
            	throw new IllegalArgumentException("element[" + i + "] differs, got " + a2[i] + " expected " + a[i]);
            }
		}
	}

	public static long sum(long[] a) {
		long res = 0;
        final long length = a.length;
        for (int i=0; i < length; i++) {
        	res += a[i];
        }
		return res;
	}
	
}
