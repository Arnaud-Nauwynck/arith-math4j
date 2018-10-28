package fr.an.math4j.arith;

public final class IntArithUtil {


	/** Greater Common Divisor */
	public static int gcd(int a, int b) {
		if (b > a) {
			int swap = b;
			b = a;
			a = swap;
		}
		for(;;) {
			if (b > a) {
				int swap = b;
				b = a;
				a = swap;
			}
			if (b == 0) return a;
			int remain = a % b;
			// recurse (loop)
			a = b;
			b = remain;
		}
	}

	/* Greater Common Divisor .. recursive algo */
	static int gcd_rec(int a, int b) {
		if (b > a) {
			int swap = b;
			b = a;
			a = swap;
		}
		if (b == 0) return a;
		int remain = a % b;
		return gcd_rec(b, remain);
	}

	/**
	 * same as gcd , but return boolean with fast exist
	 * @param u
	 * @param v
	 * @return
	 */
	public static boolean isCoprime(int u, int v) {
	    // If both numbers are even, then they are not coprime.
	    if (((u | v) & 1) == 0) return false;

	    // Now at least one number is odd. Eliminate all the factors of 2 from u.
	    while ((u & 1) == 0) u >>= 1;

	    // One is coprime with everything else by definition.
	    if (u == 1) return true;

	    do {
	        // Eliminate all the factors of 2 from v, because we know that u and v do not have any 2's in common.
	        while ((v & 1) == 0) v >>= 1;

	        // One is coprime with everything else by definition.
	        if (v == 1) return true;

	        // Swap if necessary to ensure that v >= u.
	        if (u > v) {
	            int t = v;
	            v = u;
	            u = t;
	        }

	        // We know that GCD(u, v) = GCD(u, v - u).
	        v -= u;
	    } while (v != 0);

	    // When we reach here, we have v = 0 and GCD(u, v) = current value of u, which is greater than 1.
	    return false;
	}


}
