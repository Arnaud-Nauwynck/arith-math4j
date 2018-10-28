package fr.an.math4j.arith;

import java.util.Arrays;

public class LongUtil {

	public static long[] partialSum(long[] input) {
		final int len = input.length;
		long[] res = new long[len];
		long partialSum = 0; 
		for (int i = 0; i < len; i++) {
			partialSum += input[i]; 
			res[i] = partialSum;
		}
		return res;
	}

	public static String toStringTruncate(long[] array, int displayBeginCount, int displayEndCount) {
		StringBuilder sb = new StringBuilder(1000);
		final int arrayLen = array.length;
		int maxBegin = Math.min(displayBeginCount, arrayLen);
		for(int i = 1; i < maxBegin; i++) {
			sb.append("[" + i + "]:" + array[i] + " ");
		}
		if (maxBegin < arrayLen) {
			sb.append(" ..... ");
			int minEnd = Math.max(maxBegin, arrayLen - displayEndCount); 
			for(int i = minEnd; i < arrayLen; i++) {
				sb.append("[" + i + "]:" + array[i] + " ");
			}
		}
		return sb.toString();
	}

	/**
	 * @return the floor of the square root of n
	 */
	public static long sqrt(long n) {
		long x = 1L << (32 - (Long.numberOfLeadingZeros(n) + 1) / 2);
		for (;;) {
			long y = n - x * x;
			if (y == 0)
				return x;
			long z = y / (2 * x);
			if (z == 0) {
				if (y < 0)
					--x;
				return x;
			}
			x += z;
		}
	}

	/**
	 * Compute the greatest common divisor between a and b.
	 */
	private static long gcd(long a, long b) {
		if (a < 0) {
			a = -a;
		}
		if (b < 0) {
			b = -b;
		}
		for (;;) {
			if (b == 0) {
				return a;
			}
			long c = a;
			a = b;
			b = c % b;
		}
	}

	/**
	 * Compute the chinese remainer, i.e the value n such that: n mod m1 = r1 and n
	 * mod m2 = r2
	 */
	public static final long crt(long r1, long m1, long r2, long m2) {
		// Normalize modulo
		r1 %= m1;
		if (r1 < 0) {
			r1 += m1;
		}
		r2 %= m2;
		if (r2 < 0) {
			r2 += m2;
		}
		long gcd = gcd(m1, m2);
		if (r1 % gcd != r2 % gcd) {
			throw new RuntimeException();
		}
		if (m1 == gcd) {
			return r2;
		}
		if (m2 == gcd) {
			return r1;
		}
		return crt0(r1 / gcd, m1 / gcd, r2 / gcd, m2 / gcd) * gcd + r1 % gcd;
	}

	/**
	 * @param x
	 *            residue1, modulo1, residu2, modulo2, etc.
	 * @return the value
	 */
	public static final long crt0(long... x) {
		int n = x.length / 2;
		long mod = 1;
		long s = 0;
		for (int i = 0; i < n; ++i)
			mod *= x[2 * i + 1];
		for (int i = 0; i < n; ++i) {
			long r = x[2 * i];
			long m = x[2 * i + 1];
			long f = mod / m;
			s = (s + r * inverseMod(f, m) % m * f) % mod;
		}
		return s;
	}

	/**
	 * Compute the inverse of x modulo mod.
	 */
	public static final long inverseMod(long x, long mod) {
		long g = extendedGCD(x, mod)[0];
		long inv = g >= 0 ? g : g + mod;
		if (x * inv % mod != 1)
			throw new RuntimeException(); // x and mod not relatively prime
		return inv;
	}

	private static long[] extendedGCD(long a, long b) {
		long x0 = 1;
		long x1 = 0;
		long y0 = 0;
		long y1 = 1;
		while (b != 0) {
			long q = a / b;
			long t = a;
			a = b;
			b = t % b;
			t = x1;
			x1 = x0 - q * x1;
			x0 = t;
			t = y1;
			y1 = y0 - q * y1;
			y0 = t;
		}
		return new long[] { x0, y0 };
	}

	/**
	 * Compute the list (unordered) of all divisors of n.
	 */
	public static final long[] allDivisors(long n) {
		if (n < 2)
			return new long[] { 1 };
		int np = 0;
		long[] primes = new long[32];
		long[] divisors = new long[64];
		for (long i = 2; i * i <= n; ++i)
			while (n % i == 0) {
				n /= i;
				primes[np++] = i;
			}
		if (n != 1) {
			primes[np++] = n;
		}
		primes = Arrays.copyOf(primes, np);
		int nd = allDivisors(primes, 0, divisors, 0, 1);
		return Arrays.copyOf(divisors, nd);
	}

	private static int allDivisors(long[] primes, int index, long[] divisors, int nd, long product) {
		long p = primes[index++];
		int n = 1;
		while (index < primes.length && primes[index] == p) {
			++n;
			++index;
		}
		for (int i = 0; i <= n; ++i, product *= p) {
			if (index < primes.length) {
				nd = allDivisors(primes, index, divisors, nd, product);
			} else {
				divisors[nd++] = product;
			}
		}
		return nd;
	}
}
