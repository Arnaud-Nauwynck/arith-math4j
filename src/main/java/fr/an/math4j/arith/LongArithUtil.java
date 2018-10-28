package fr.an.math4j.arith;

public final class LongArithUtil {

	public static long pow(long base, int exponent) {
		if (exponent <= 1) {
	        if (exponent < 0) {
	            throw new ArithmeticException("Negative exponent");
	        }
	        if (exponent == 0) {
	        	return 1;
	        }
	        if (exponent == 1) {
	        	return base;
	        }
		}
        long res = 1;
        long pow2 = base;
        while (exponent > 0) {
            if ((exponent & 1) == 1) {
                res *= pow2;        
            }
            exponent >>= 1;
            pow2 *= pow2; 
        }
        return res;
	}

	
	public static long pow_mod(long base, int exponent, long modulo) {
		if (exponent <= 3) {
	        if (exponent < 0) {
	            throw new ArithmeticException("Negative exponent");
	        }
	        if (exponent == 0) {
	        	return 1;
	        }
	        if (exponent == 1) {
	        	return base;
	        }
	        if (exponent == 2) {
	        	return (base * base) % modulo;
	        }
	        if (exponent == 3) {
	        	return (((base * base) % modulo) * base) % modulo;
	        }
		}
        long res = 1;
        long pow2 = base % modulo;
        while (exponent > 0) {
            if ((exponent & 1) == 1) {
                res = (res * pow2) % modulo;        
            }
            exponent >>= 1;
            pow2 = (pow2 * pow2) % modulo; 
        }
        return res;
	}

	public static long[] powsUpTo(long base, int pow) {
		long[] res = new long[pow+1];
		res[0] = 1;
		res[1] = base;
		long curr = base; 
		for(int i = 2; i <= pow; i++) {
			curr *= base;
			res[i] = curr;
		}
		return res;
	}

	public static long[] powsUpTo_mod(long base, int pow, long mod) {
		long[] res = new long[pow+1];
		res[0] = 1;
		res[1] = base;
		long curr = base; 
		for(int i = 2; i <= pow; i++) {
			curr = (curr * base) % mod;
			res[i] = curr;
		}
		return res;
	}


	
	
	/**
	 * geometric sum, with modulo 
	 * @param p0 initial term
	 * @param q multiply term
	 * @param exp number of exponent
	 * @param mod
	 * 
	 * @return (p0 + p0*q + p0*q^2 + ... + p0*q^exp) modulo mod
	 */
	public static long geometricSumMod(long p0, long q, int exp, long mod) {
		long numerator = (LongArithUtil.pow_mod(q, exp, mod) - 1) % mod; 
		long res = (p0 * numerator / (q-1)) % mod;
		return res;
	}

	public static long geometricSum(long p0, long q, int exp) {
		long numerator = (LongArithUtil.pow(q, exp) - 1); 
		long res = (p0 * numerator / (q-1));
		return res;
	}

		
	
//	public static int[][] allCombinations_pascalTriangle(int n) {
//		int[][] res = new int[n+1][];
//	    res[1] = new int[] { 1 };
//	    for (int i = 2; i <= n; i++) {
//	        res[i] = new int[i+1];
//	        for (int j = 0; j < i; j++) {
//	            res[i][j] = res[i-1][j-1] + res[i-1][j];
//	        }
//	    }
//	    return res;
//	}
	
	/** return  [ c(n,0), ... c(n,k).. c(n,n)] */
	public static long[] combinations(int n) {
		long res[] = new long[n+1];
		res[0] = 1;
		final int maxI = n/2+1;
		for(int i=1; i<maxI; i++) {
	        res[i] = res[i-1] * (n-i+1) / i;  // can overflow?
	    }
		completeCombinationsHalf(res, n);
	    return res;
	}

	/* Copy the inverse of the first part */
	public static void completeCombinationsHalf(long[] res, int n) {
		for(int i=n/2+1; i<=n; i++){ 
	        res[i] = res[n-i];
	    }
	}
	
	
	public static long partialFactorial(int r, int n) { 
		long res = 1;
		for(int i = r+1; i <= n; i++) {
			res *= i;
		}
		return res;
	}
	
	public static long factorial(int n) {
		long res = 1;
		for(int i = 2 ; i <= n; i++) {
			res *= i;
		}
		return res;
	}

	// unsafe with overflow ...
//	public static long comb(int n, int r) {
//		if(r > n / 2) {
//			r = n - r; // because C(n, r) == C(n, n - r)
//		}
//		long denom = factorial(n - r);
//		long num = partialFactorial(r, n);
//		long res = num / denom;
//		return res;
//	}
	
	public static long comb(long n, long k) {
		long res = 1;
		for (long i = 0; i < k; i++) {
			res *= n - i;
			res /= i + 1;
		}
		return res;
	}
	
	
	
	
	/** Greater Common Divisor */
	public static long gcd_signed(long a, long b) {
		long sign = 1;
		if (a < 0) {
			a = -a;
			if (b < 0) {
				sign = +1;
				b = -b;
			} else {
				sign = -1;
			}
		} else {
			if (b < 0) {
				sign = -1;
				b = -b;
			}
		}
		return sign * gcd(a, b);
	}
	
	public static long gcd(long a, long b) {
		if (b > a) {
			long swap = b;
			b = a;
			a = swap;
		}
		if (a < Integer.MAX_VALUE) {
			return IntArithUtil.gcd((int) a, (int) b);
		}
		for(;;) {
			if (b > a) {
				long swap = b;
				b = a;
				a = swap;
			}
			if (b == 0) return a;
			if (a < Integer.MAX_VALUE) {
				return IntArithUtil.gcd((int) a, (int) b);
			}
			long remain = a % b;
			// recurse (loop)
			a = b;
			b = remain;
		}
	}

	/** Least Common Multiple */
	public static long lcm(long a, long b) {
		long gcd = LongArithUtil.gcd(a,b);
		return a * b / gcd;
	}

	/** Greater Common Divisor */
	public static long gcd_rec(long a, long b) {
		if (b > a) {
			long swap = b;
			b = a;
			a = swap;
		}
		if (b == 0) return a;
		long remain = a % b;
		return gcd_rec(b, remain);
	}

	
	/**
	 * same as gcd , but return boolean with fast exist
	 * @param u
	 * @param v
	 * @return
	 */
	public static boolean isCoprime(long u, long v) {
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
	            long t = v;
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
