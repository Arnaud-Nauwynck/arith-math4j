package fr.an.math4j.arith;

import java.util.Arrays;

public class IntUtil {

	public static long[] partialSum(int[] input) {
		final int len = input.length;
		long[] res = new long[len];
		int partialSum = 0; 
		for (int i = 0; i < len; i++) {
			partialSum += input[i]; 
			res[i] = partialSum;
		}
		return res;
	}

//	/** cf Long.numberOfLeadingZeros */
//    public static int numberOfLeadingZeros(long i) {
//        // HD, Figure 5-6
//         if (i == 0)
//            return 32;
//        int n = 1;
//        int x = (int)(i >>> 32);
//        if (x == 0) { n += 32; x = (int)i; }
//        if (x >>> 16 == 0) { n += 16; x <<= 16; }
//        if (x >>> 24 == 0) { n +=  8; x <<=  8; }
//        if (x >>> 28 == 0) { n +=  4; x <<=  4; }
//        if (x >>> 30 == 0) { n +=  2; x <<=  2; }
//        n -= x >>> 31;
//        return n;
//    }
//
//    
//	/**
//	 * @return the floor of the square root of n
//	 */
//	public static int sqrt(int n) {
//		int x = 1L << (32 - (Long.numberOfLeadingZeros(n) + 1) / 2);
//		for (;;) {
//			int y = n - x * x;
//			if (y == 0)
//				return x;
//			int z = y / (2 * x);
//			if (z == 0) {
//				if (y < 0)
//					--x;
//				return x;
//			}
//			x += z;
//		}
//	}

	/**
	 * Compute the greatest common divisor between a and b.
	 */
	private static int gcd(int a, int b) {
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
			int c = a;
			a = b;
			b = c % b;
		}
	}

	/**
	 * Compute the chinese remainer, i.e the value n such that: n mod m1 = r1 and n
	 * mod m2 = r2
	 */
	public static final int crt(int r1, int m1, int r2, int m2) {
		// Normalize modulo
		r1 %= m1;
		if (r1 < 0) {
			r1 += m1;
		}
		r2 %= m2;
		if (r2 < 0) {
			r2 += m2;
		}
		int gcd = gcd(m1, m2);
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
	public static final int crt0(int... x) {
		int n = x.length / 2;
		int mod = 1;
		int s = 0;
		for (int i = 0; i < n; ++i)
			mod *= x[2 * i + 1];
		for (int i = 0; i < n; ++i) {
			int r = x[2 * i];
			int m = x[2 * i + 1];
			int f = mod / m;
			s = (s + r * inverseMod(f, m) % m * f) % mod;
		}
		return s;
	}

	/**
	 * Compute the inverse of x modulo mod.
	 */
	public static final int inverseMod(int x, int mod) {
		int g = extendedGCD(x, mod)[0];
		int inv = g >= 0 ? g : g + mod;
		if (x * inv % mod != 1)
			throw new RuntimeException(); // x and mod not relatively prime
		return inv;
	}

	private static int[] extendedGCD(int a, int b) {
		int x0 = 1;
		int x1 = 0;
		int y0 = 0;
		int y1 = 1;
		while (b != 0) {
			int q = a / b;
			int t = a;
			a = b;
			b = t % b;
			t = x1;
			x1 = x0 - q * x1;
			x0 = t;
			t = y1;
			y1 = y0 - q * y1;
			y0 = t;
		}
		return new int[] { x0, y0 };
	}

	/**
	 * Compute the list (unordered) of all divisors of n.
	 */
	public static final int[] allDivisors(int n) {
		if (n < 2)
			return new int[] { 1 };
		int np = 0;
		int[] primes = new int[32];
		int[] divisors = new int[64];
		for (int i = 2; i * i <= n; ++i)
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

	private static int allDivisors(int[] primes, int index, int[] divisors, int nd, int product) {
		int p = primes[index++];
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
