package fr.an.math4j.arith;

public class LongPolynomUtil {
	
	public static long[] multiply(long[] a, long[] b) {
		long[] ret = new long[a.length + b.length - 1];
		for (int i = 0; i < a.length; ++i) {
			for (int j = 0; j < b.length; ++j) {
				ret[i + j] += a[i] * b[j];
			}
		}
		return ret;
	}

	public static long[] power(long[] poly, int n) {
		long[] res = new long[] { 1 };
		long[] polyPower = poly;
		while (n > 0) {
			if ((n & 1) != 0) {
				res = multiply(res, polyPower);
			}
			polyPower = multiply(polyPower, polyPower);
			n >>= 1;
		}
		return res;
	}

	
	
	public static long[] multiplyModulo(long[] a, long[] b, long mod) {
		long[] ret = new long[a.length + b.length - 1];
		for (int i = 0; i < a.length; ++i) {
			for (int j = 0; j < b.length; ++j) {
				ret[i + j] = (ret[i + j] + (a[i] % mod) * (b[j] % mod)) % mod;
			}
		}
		return ret;
	}

	public static long[] powerModulo(long[] poly, int n, long mod) {
		long[] res = new long[] { 1 };
		long[] polyPower = poly;
		while (n > 0) {
			if ((n & 1) != 0) {
				res = multiplyModulo(res, polyPower, mod);
			}
			polyPower = multiplyModulo(polyPower, polyPower, mod);
			n >>= 1;
		}
		return res;
	}
	
}
