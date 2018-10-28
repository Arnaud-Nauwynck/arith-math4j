package fr.an.math4j.arith;

import java.util.Arrays;
import java.util.BitSet;

/*
 * from http://atoms.alife.co.uk/sqrt/SquareRoot.java
 * 
 * cf also
 * https://en.wikipedia.org/wiki/Integer_square_root
 * http://www.codecodex.com/wiki/Calculate_an_integer_square_root
 * BigInteger floorSqrt
 * http://www.andrijar.com/algorithms/algorithms.htm#qusr
 * https://gmplib.org/manual/Square-Root-Algorithm.html
 * https://gmplib.org/manual/Perfect-Square-Algorithm.html
 * https://stackoverflow.com/questions/295579/fastest-way-to-determine-if-an-integers-square-root-is-an-integer
 * 
 * Integer Square Root function
 * Contributors include Arne Steinarson for the basic approximation idea, Dann 
 * Corbit and Mathew Hendry for the first cut at the algorithm, Lawrence Kirby 
 * for the rearrangement, improvments and range optimization, Paul Hsieh 
 * for the round-then-adjust idea, Tim Tyler, for the Java port
 * and Jeff Lawson for a bug-fix and some code to improve accuracy.
 * 
 * 
 * v0.02 - 2003/09/07
 */

/**
 * Faster replacements for (int)(java.lang.Math.sqrt(integer))
 */
public class IntSquareRoot {

	private final static int[] table = { 0, 16, 22, 27, 32, 35, 39, 42, 45, 48, 50, 53, 55, 57, 59, 61, 64, 65, 67, 69,
			71, 73, 75, 76, 78, 80, 81, 83, 84, 86, 87, 89, 90, 91, 93, 94, 96, 97, 98, 99, 101, 102, 103, 104, 106,
			107, 108, 109, 110, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 128, 128,
			129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 144, 145, 146, 147, 148,
			149, 150, 150, 151, 152, 153, 154, 155, 155, 156, 157, 158, 159, 160, 160, 161, 162, 163, 163, 164, 165,
			166, 167, 167, 168, 169, 170, 170, 171, 172, 173, 173, 174, 175, 176, 176, 177, 178, 178, 179, 180, 181,
			181, 182, 183, 183, 184, 185, 185, 186, 187, 187, 188, 189, 189, 190, 191, 192, 192, 193, 193, 194, 195,
			195, 196, 197, 197, 198, 199, 199, 200, 201, 201, 202, 203, 203, 204, 204, 205, 206, 206, 207, 208, 208,
			209, 209, 210, 211, 211, 212, 212, 213, 214, 214, 215, 215, 216, 217, 217, 218, 218, 219, 219, 220, 221,
			221, 222, 222, 223, 224, 224, 225, 225, 226, 226, 227, 227, 228, 229, 229, 230, 230, 231, 231, 232, 232,
			233, 234, 234, 235, 235, 236, 236, 237, 237, 238, 238, 239, 240, 240, 241, 241, 242, 242, 243, 243, 244,
			244, 245, 245, 246, 246, 247, 247, 248, 248, 249, 249, 250, 250, 251, 251, 252, 252, 253, 253, 254, 254,
			255 };

	/**
	 * A faster replacement for (int)(java.lang.Math.sqrt(x)). Completely accurate
	 * for x < 2147483648 (i.e. 2^31)...
	 */
	public static int sqrt(int x) {
		int xn;

		if (x >= 0x10000) {
			if (x >= 0x1000000) {
				if (x >= 0x10000000) {
					if (x >= 0x40000000) {
						xn = table[x >> 24] << 8;
					} else {
						xn = table[x >> 22] << 7;
					}
				} else {
					if (x >= 0x4000000) {
						xn = table[x >> 20] << 6;
					} else {
						xn = table[x >> 18] << 5;
					}
				}

				xn = (xn + 1 + (x / xn)) >> 1;
				xn = (xn + 1 + (x / xn)) >> 1;
				return ((xn * xn) > x) ? --xn : xn;
			} else {
				if (x >= 0x100000) {
					if (x >= 0x400000) {
						xn = table[x >> 16] << 4;
					} else {
						xn = table[x >> 14] << 3;
					}
				} else {
					if (x >= 0x40000) {
						xn = table[x >> 12] << 2;
					} else {
						xn = table[x >> 10] << 1;
					}
				}

				xn = (xn + 1 + (x / xn)) >> 1;

				return ((xn * xn) > x) ? --xn : xn;
			}
		} else {
			if (x >= 0x100) {
				if (x >= 0x1000) {
					if (x >= 0x4000) {
						xn = (table[x >> 8]) + 1;
					} else {
						xn = (table[x >> 6] >> 1) + 1;
					}
				} else {
					if (x >= 0x400) {
						xn = (table[x >> 4] >> 2) + 1;
					} else {
						xn = (table[x >> 2] >> 3) + 1;
					}
				}

				return ((xn * xn) > x) ? --xn : xn;
			} else {
				if (x >= 0) {
					return table[x] >> 4;
				}
			}
		}

		illegalArgument();
		return -1;
	}

	/**
	 * A faster replacement for (int)(java.lang.Math.sqrt(x)). Completely accurate
	 * for x < 2147483648 (i.e. 2^31)... Adjusted to more closely approximate
	 * "(int)(java.lang.Math.sqrt(x) + 0.5)" by Jeff Lawson.
	 */
	public static int accurateSqrtPlus05(int x) {
		int xn;

		if (x >= 0x10000) {
			if (x >= 0x1000000) {
				if (x >= 0x10000000) {
					if (x >= 0x40000000) {
						xn = table[x >> 24] << 8;
					} else {
						xn = table[x >> 22] << 7;
					}
				} else {
					if (x >= 0x4000000) {
						xn = table[x >> 20] << 6;
					} else {
						xn = table[x >> 18] << 5;
					}
				}

				xn = (xn + 1 + (x / xn)) >> 1;
				xn = (xn + 1 + (x / xn)) >> 1;
				return adjustment(x, xn);
			} else {
				if (x >= 0x100000) {
					if (x >= 0x400000) {
						xn = table[x >> 16] << 4;
					} else {
						xn = table[x >> 14] << 3;
					}
				} else {
					if (x >= 0x40000) {
						xn = table[x >> 12] << 2;
					} else {
						xn = table[x >> 10] << 1;
					}
				}

				xn = (xn + 1 + (x / xn)) >> 1;

				return adjustment(x, xn);
			}
		} else {
			if (x >= 0x100) {
				if (x >= 0x1000) {
					if (x >= 0x4000) {
						xn = (table[x >> 8]) + 1;
					} else {
						xn = (table[x >> 6] >> 1) + 1;
					}
				} else {
					if (x >= 0x400) {
						xn = (table[x >> 4] >> 2) + 1;
					} else {
						xn = (table[x >> 2] >> 3) + 1;
					}
				}

				return adjustment(x, xn);
			} else {
				if (x >= 0) {
					return adjustment(x, table[x] >> 4);
				}
			}
		}

		illegalArgument();
		return -1;
	}

	private static int adjustment(int x, int xn) {
		// Added by Jeff Lawson:
		// need to test:
		// if |xn * xn - x| > |x - (xn-1) * (xn-1)| then xn-1 is more accurate
		// if |xn * xn - x| > |(xn+1) * (xn+1) - x| then xn+1 is more accurate
		// or, for all cases except x == 0:
		// if |xn * xn - x| > x - xn * xn + 2 * xn - 1 then xn-1 is more accurate
		// if |xn * xn - x| > xn * xn + 2 * xn + 1 - x then xn+1 is more accurate
		int xn2 = xn * xn;

		// |xn * xn - x|
		int comparitor0 = xn2 - x;
		if (comparitor0 < 0) {
			comparitor0 = -comparitor0;
		}

		int twice_xn = xn << 1;

		// |x - (xn-1) * (xn-1)|
		int comparitor1 = x - xn2 + twice_xn - 1;
		if (comparitor1 < 0) { // need to correct for x == 0 case?
			comparitor1 = -comparitor1; // only gets here when x == 0
		}

		// |(xn+1) * (xn+1) - x|
		int comparitor2 = xn2 + twice_xn + 1 - x;

		if (comparitor0 > comparitor1) {
			return (comparitor1 > comparitor2) ? ++xn : --xn;
		}

		return (comparitor0 > comparitor2) ? ++xn : xn;
	}

	/**
	 * A *much* faster replacement for (int)(java.lang.Math.sqrt(x)). 
	 * Completely accurate for x < ...
	 */
	public static int fastSqrtSmall(int x) {
		if (x >= 0x10000) {
			if (x >= 0x1000000) {
				if (x >= 0x10000000) {
					if (x >= 0x40000000) {
						return (table[x >> 24] << 8);
					} else {
						return (table[x >> 22] << 7);
					}
				} else if (x >= 0x4000000) {
					return (table[x >> 20] << 6);
				} else {
					return (table[x >> 18] << 5);
				}
			} else if (x >= 0x100000) {
				if (x >= 0x400000) {
					return (table[x >> 16] << 4);
				} else {
					return (table[x >> 14] << 3);
				}
			} else if (x >= 0x40000) {
				return (table[x >> 12] << 2);
			} else {
				return (table[x >> 10] << 1);
			}
		} else if (x >= 0x100) {
			if (x >= 0x1000) {
				if (x >= 0x4000) {
					return (table[x >> 8]);
				} else {
					return (table[x >> 6] >> 1);
				}
			} else if (x >= 0x400) {
				return (table[x >> 4] >> 2);
			} else {
				return (table[x >> 2] >> 3);
			}
		} else if (x >= 0) {
			return table[x] >> 4;
		}
		illegalArgument();
		return -1;
	}

	public static int javaIntSqrt(int x) {
		return (int) Math.sqrt((double) x);
	}
	
	/*pp*/ static BitSet generateSqrCandidateModBitSet(int mod) {
		BitSet res = new BitSet(mod);
		for(int i = 0; i < mod; i++) {
			int sqrMod = (int) (((long)i*i) % mod);
			res.set(sqrMod, true);
		}
		return res;
	}
	
	/*pp*/ static boolean[] generateSqrCandidateMod(int mod) {
		boolean[] res = new boolean[mod];
		Arrays.fill(res, false);
		for(int i = 0; i < mod; i++) {
			int sqrMod = (int) (((long)i*i) % mod);
			res[sqrMod] = true;
		}
		return res;
	}
	
	private static final boolean[] SQR_CANDIDATES_MOD256 = generateSqrCandidateMod(256); // => 82.81%
	private static final int MOD_9_5_7_13 = 9*5*7*13; // 4095 => 95.01% !!
	private static final boolean[] SQR_CANDIDATES_MOD_9_5_7_13 = generateSqrCandidateMod(MOD_9_5_7_13);

	static final long MASK_SQR_CANDIDATES_MOD64 = generateSqrCandidateModBitSet(64).toLongArray()[0]; // = 0x202021202030213   => 81.25%
	static final int MASK_MOD_64 = 64-1; // = 0x3f

	/**
	 * computing for all int in 1..MAX_INTEGER, take 1050ms, heuristic=75.0%
	 * @param x
	 * @return
	 */
	static boolean heuristicCandidatePerfectSquareMod64(int x) {
		return (MASK_SQR_CANDIDATES_MOD64 & (1L << (x & MASK_MOD_64))) != 0;
	}
	
	/**
	 * computing for all int in 1..MAX_INTEGER, take 1050ms, heuristic=75.0%
	 * @param x
	 * @return
	 */
	static boolean heuristicCandidatePerfectSquareMod256(int x) {
		return SQR_CANDIDATES_MOD256[x & 0xF];
	}

	/**
	 * computing heuristicCandidatePerfectSquareMod() for all int in 1..MAX_INTEGER, took 6806ms
	 * SLOWER than actually computing for ints !!!!
	 * @param x
	 * @return
	 */
	static boolean heuristicCandidatePerfectSquareMod_slow(int x) {
		if (!SQR_CANDIDATES_MOD256[x & 0xF]) {
			return false;
		}
		if (!SQR_CANDIDATES_MOD_9_5_7_13[x % MOD_9_5_7_13]) {
			return false;
		}
		return true;
	}
		
	/**
	 * cf mpz/perfsqr.c:/* mpz_perfect_square_p(arg) -- Return non-zero if ARG is a perfect square
	 * 
	 * check modulo 256.. The first test excludes 212/256 (82.8%) of the perfect square candidates in O(1) time.
	 * TODO .. 
	 * check mod 9, 5, 7, 13 and 17, for a total 99.25% of inputs identified as non-squares. 
	 * check 97 for a total 99.62%. 
	 * 
	 * 
	 * cf also https://hackage.haskell.org/package/arithmoi-0.4.2.0/src/Math/NumberTheory/Powers/Squares.hs
	 * 
	 */
	public static int sqrtIfPerfectSquare_heur(int x) {
		// heuristic using modulo64
		if ((MASK_SQR_CANDIDATES_MOD64 & (1L << (x & MASK_MOD_64))) == 0) {
			return -1;
		}
				
		int s = javaIntSqrt(x); // + 0.5 // TOCHECK
		int res = (s * s != x)? -1 : s;
		return res;
	}
	
	public static int sqrtIfPerfectSquare_slow(int x) {
		int s = javaIntSqrt(x); // + 0.5 // TOCHECK
		long checkX = (long)s * s; 
		return (checkX != x)? -1 : s;
	}
	
	
	/**
	 * If x is square, return +sqrt(x).
	 * Otherwise, return -1.
	 * 
	 * benchmark: loop 1..Integer.MAX_VALUE in 3252ms !!! =600M/s = 1.4ns
	 * 
	 * adapted from https://math.stackexchange.com/questions/41337/efficient-way-to-determine-if-a-number-is-perfect-square
	 */
	public static int sqrtIfPerfectSquare(int x) {
	    // Precondition: make x odd if possible. ..divide by 2^(2*sh)
		// if (x <= 0) return -1;
		
		if ((MASK_SQR_CANDIDATES_MOD64 & (1L << (x & MASK_MOD_64))) == 0) {
			return -1;
		}
		
		int sh = 0;

//		// removing power of 2^64.. reduce perf!! 
//		int mod64 = x & MASK_MOD_64;
//		while(mod64 == 0) {
//			x >>= 6;
//			sh += 3;
//			mod64 = x & MASK_MOD_64;
//		}

		while ((x & 0b11) == 0) { 
	    	x >>= 2;
	    	sh++; 
	    }
	    if (x == 1) {
	    	return 1 << sh; // exact power of 2
	    }
//	    if ((x & 1) == 0) {
//	        return -1; // not even multiplicity of 2
//	    }	    
	    if ((x & 6) != 0) {
	    	return -1;
	    }

	    // 2-adic Newton
	    final int ITERATIONS = 5; // log log x - 1
	    long z = (3L-x) >> 1;
	    long y = x*z;
	    for (int i=0; i < ITERATIONS; i++) {
	    	long w = (3 - z*y) >> 1;
	        y *= w;
	        z *= w;
	    }
	    assert(x==0 || (y*z == 1 && x*z == y));

	    // Get the positive square root.  Also the top bit might be wrong.
	    if (0 != (y & (1L << 62))) {
	    	y = -y;
	    }
	    y &= ~(1L<<63);

	    // Is it the square root in Z?
	    if (y >= (1L << 32)) {
	    	return -1;
	    }
	    
	    int res = (int) (y << sh); // restore add power of 2
		return res;
	}

	private static void illegalArgument() {
		throw new IllegalArgumentException("Attemt to take the square root of negative number");
	}

}