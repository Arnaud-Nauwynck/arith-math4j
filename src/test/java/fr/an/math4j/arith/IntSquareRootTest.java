package fr.an.math4j.arith;

import static fr.an.math4j.arith.IntSquareRoot.sqrtIfPerfectSquare;
import static fr.an.math4j.arith.IntSquareRoot.sqrtIfPerfectSquare_heur;
import static org.junit.Assert.assertEquals;

import java.util.BitSet;
import java.util.function.Predicate;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class IntSquareRootTest {
	
	@Test @Ignore
	public void testSqrtIfPerfectSquare_slow() {
		ProgressDisplayHelper progress = new ProgressDisplayHelper(100_000_000, 1_000_000_000);
		progress.start(Integer.MAX_VALUE);
			
		Assert.assertEquals(1, IntSquareRoot.sqrtIfPerfectSquare_slow(1));
		int nextSqrt = 2;
		int nextI = nextSqrt * nextSqrt;
		int i = 2;
		for (;;) {
			for(; i < nextI; i++) {
				Assert.assertEquals(-1, IntSquareRoot.sqrtIfPerfectSquare_slow(i));
				progress.incrPrint();
			}
			Assert.assertEquals(nextSqrt, IntSquareRoot.sqrtIfPerfectSquare_slow(i));
			i++;
			progress.incrPrint();
			
			nextSqrt++;
			long nextSquareLong = (long)nextSqrt * nextSqrt;
			if (nextSquareLong >= Integer.MAX_VALUE) {
				break;
			}
			nextI = (int) nextSquareLong;
			if (nextSquareLong == 46341*46341) {
				System.out.println();
			}
		}
		for(; i < Integer.MAX_VALUE; i++) {
			Assert.assertEquals(-1, sqrtIfPerfectSquare_heur(i));
		}
	}
	
	/**
	 * results logs:
	 * <PRE>
	modulo 256 => 212 candidates = 0.828125
	modulo 2 => 0 candidates = 0.0
	modulo 3 => 1 candidates = 0.3333333333333333
	modulo 5 => 2 candidates = 0.4
	modulo 7 => 3 candidates = 0.42857142857142855
	modulo 11 => 5 candidates = 0.45454545454545453
	modulo 13 => 6 candidates = 0.46153846153846156
	modulo 17 => 8 candidates = 0.47058823529411764
	modulo 6 => 2 candidates = 0.3333333333333333
	modulo 30 => 18 candidates = 0.6
	modulo 210 => 162 candidates = 0.7714285714285715
	modulo 2310 => 2022 candidates = 0.8753246753246753
	modulo 30030 => 28014 candidates = 0.9328671328671329
	modulo 510510 => 492366 candidates = 0.9644590703414233
	modulo 4095 => 3759 candidates = 0.9179487179487179
	modulo 69615 => 66591 candidates = 0.9565610859728507
	modulo 64 => 52 candidates = 0.8125
    candidate mask 64: 0x202021202030213
    candidate ~mask 64: 0xfdfdfdedfdfcfdec
     * </PRE>
     * 
     * choosing 4095=9*5*7*13 => 91.7%   good compromise for mem usage?
	 */
	@Test
	public void heuristicCandidatePerfectSquare_generator() {
		boolean debug = false;
		int[] mods = new int[] { 256, 2, 3, 5, 7, 11, 13, 17, //
				2*3, 2*3*5, 2*3*5*7, 2*3*5*7*11, 2*3*5*7*11*13, 2*3*5*7*11*13*17, // 
				9*5*7*13, // best compromise 
				9*5*7*13*17, // best perf, big memory usage
				64 // best cpu compromise with no mem 
		};
		
		for (int mod : mods) {
			boolean[] cand = IntSquareRoot.generateSqrCandidateMod(mod);
			int countCand = countFalses(cand);
			if (debug) System.out.println("modulo " + mod + " => " + countCand + " candidates = " + ((double)countCand / mod));
		}
		
		boolean[] cand64 = IntSquareRoot.generateSqrCandidateMod(64);
		BitSet bsCand64 = new BitSet(64);
		for(int i = 0; i < 64; i++) {
			bsCand64.set(i, cand64[i]);
		}
		long bsCand64Bits = bsCand64.toLongArray()[0];
		if (debug) {
			System.out.println("candidate mask 64: 0x" + Long.toHexString(bsCand64Bits) + "  " + toBin64String(bsCand64Bits));
			System.out.println("         ~mask 64: 0x" + Long.toHexString(~bsCand64Bits) + " " + toBin64String(~bsCand64Bits));
		}
		long squareCan64 = IntSquareRoot.generateSqrCandidateModBitSet(64).toLongArray()[0];

		
		int mask64 = 64-1;
		if (debug) {
			System.out.println("mod64 : & (64-1)=" + Integer.toBinaryString(mask64) + " = 0x" + Integer.toHexString(mask64));
			for(int i = 64-1; i >= 0; i--) {
				System.out.print("[" + i + "]:" + (cand64[i]?"y":"n") + " ");
				if (i % 16 == 0) System.out.println(); 
			}
			System.out.println();
			System.out.println("SQR_CANDIDATES_MOD64: b" + toBin64String(IntSquareRoot.MASK_SQR_CANDIDATES_MOD64) + " 0x" + Long.toHexString(IntSquareRoot.MASK_SQR_CANDIDATES_MOD64));
		}
		for (int i = 0; i < 1000; i++) {
			int mod64  = i & IntSquareRoot.MASK_MOD_64; // 0x3f
			long mod64NthBit = 1L << mod64;
			boolean isCand = (IntSquareRoot.MASK_SQR_CANDIDATES_MOD64 & mod64NthBit) != 0;
			boolean expectedIsCand = cand64[mod64];
			Assert.assertEquals(isCand, expectedIsCand);
			
			boolean isCand2 = (IntSquareRoot.MASK_SQR_CANDIDATES_MOD64& (1L << (i & IntSquareRoot.MASK_MOD_64)) & squareCan64) != 0;
			Assert.assertEquals(isCand2, expectedIsCand);
		}
	}

	static String toBin64String(long val) {
		StringBuilder sb = new StringBuilder();
		sb.append('b');
		int z = Long.numberOfLeadingZeros(val);
		for (int i = 0; i < z; i++) sb.append('0');
		sb.append(Long.toBinaryString(val));
		return sb.toString();
	}
	
	static int countFalses(boolean[] values) {
		int res = 0;
		for(boolean b : values) {
			if (!b) res++;
		}
		return res;
	}
	
	
	/** heuristiq= % */
	@Test @Ignore
	public void testHeuristicCandidatePerfectSquare_slow_ALL() {
		doTestHeuristicCandidatePerfectSquare("heuristicCandidatePerfectSquareMod_slow", IntSquareRoot::heuristicCandidatePerfectSquareMod_slow);
	}
	
	/** heuristiq= % */
	@Test
	public void testHeuristicCandidatePerfectSquareMod64_ALL() {
		doTestHeuristicCandidatePerfectSquare("heuristicCandidatePerfectSquareMod64", IntSquareRoot::heuristicCandidatePerfectSquareMod64);
	}
	
	/** heuristiq= % */
	@Test
	public void testHeuristicCandidatePerfectSquareMod256_ALL() {
		doTestHeuristicCandidatePerfectSquare("heuristicCandidatePerfectSquareMod64", IntSquareRoot::heuristicCandidatePerfectSquareMod256);
	}
	
	protected void doTestHeuristicCandidatePerfectSquare(String display, Predicate<Integer> heuristicFunc) {
		long start = System.currentTimeMillis();
		
		int countHeuristicSuccess = 0;
		int countSquares = 0;
		int nextSqrt = 2;
		int nextI = nextSqrt * nextSqrt;
		int i = 2;
		for (;;) {
			for(; i < nextI; i++) {
				boolean hCandSq = heuristicFunc.test(i);
				if (!hCandSq) {
					countHeuristicSuccess++;
				}
			}
			boolean hCandSq = heuristicFunc.test(i);
			if (!hCandSq) {
				// redo for debug
				heuristicFunc.test(i);
			}
			Assert.assertTrue(hCandSq);
			i++;
			countSquares++;
			
			nextSqrt++;
			long nextSquareLong = (long)nextSqrt * nextSqrt;
			if (nextSquareLong >= Integer.MAX_VALUE) {
				break;
			}
			nextI = (int) nextSquareLong;
		}
		for(; i < Integer.MAX_VALUE; i++) {
			boolean hCandSq = heuristicFunc.test(i);
			if (!hCandSq) {
				countHeuristicSuccess++;
			}
		}
		
		long timeMillis = System.currentTimeMillis() - start;
		double heuristiqSuccProba = (double)countHeuristicSuccess / (Integer.MAX_VALUE - 2 - countSquares);
		System.out.println("computing " + display + "() for all int in 1..MAX_INTEGER, took " + timeMillis + "ms, heuristiq=" + (100.*heuristiqSuccProba) + "%");
	}

	
	
	
	
	@Test @Ignore // take 6s
	public void testSqrtIsPerfectSquare_heur_ALL() {
		ProgressDisplayHelper progress = new ProgressDisplayHelper(10_000_000, 100_000_000);
		progress.start(Integer.MAX_VALUE);
			
		Assert.assertEquals(1, sqrtIfPerfectSquare_heur(1));
		int nextSqrt = 2;
		int nextI = nextSqrt * nextSqrt;
		int i = 2;
		for (;;) {
			for(; i < nextI; i++) {
				Assert.assertEquals(-1, sqrtIfPerfectSquare_heur(i));
				progress.incrPrint();
			}
			Assert.assertEquals(nextSqrt, sqrtIfPerfectSquare_heur(i));
			i++;
			progress.incrPrint();
			
			nextSqrt++;
			long nextSquareLong = (long)nextSqrt * nextSqrt;
			if (nextSquareLong >= Integer.MAX_VALUE) {
				break;
			}
			nextI = (int) nextSquareLong;
			if (nextSquareLong == 46341*46341) {
				System.out.println();
			}
		}
		for(; i < Integer.MAX_VALUE; i++) {
			Assert.assertEquals(-1, sqrtIfPerfectSquare_heur(i));
		}
	}
	
	@Test
	public void testSqrtIfPerfectSquare_small() {
		int[] expected = new int[] {
			// 0, 1,  2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16
			0,   -1, -1, -1,2,-1,-1,-1,-1, 3, -1, -1, -1, -1, -1, -1, 4 
		};
		for (int i = 2; i <= 16; i++) {
			int actual = sqrtIfPerfectSquare(i);
			if (expected[i] != actual) {
				System.out.println("sqrt(" + i + ") .. ");
				actual = sqrtIfPerfectSquare(i);
			}
			assertEquals(expected[i], actual);
		}
	}
	
	@Test
	public void testSqrtIfPerfectSquare_overflow() {
		Assert.assertEquals(46340, IntSquareRoot.sqrtIfPerfectSquare_slow(2147395600));
		Assert.assertEquals(-1, IntSquareRoot.sqrtIfPerfectSquare_slow(2147395601));
		// Assert.assertEquals(46341, IntSquareRoot.sqrtIsPerfectSquare_slow(2147488281)); .. does not compile.. int overflow
				
		Assert.assertEquals(46340, IntSquareRoot.sqrtIfPerfectSquare(2147395600));
		Assert.assertEquals(-1, IntSquareRoot.sqrtIfPerfectSquare(2147395601));
		// Assert.assertEquals(46341, IntSquareRoot.sqrtIsPerfectSquare(2147488281)); .. does not compile.. int overflow
	}

	
	@Test @Ignore // take 10.6s
	public void testSqrtIfPerfectSquare() {
		// System.out.println("show progress '.' every 100M !"); .. measure perf without heuristic: 17.3M/s with checks... ~ 280.0M/s without check
		ProgressDisplayHelper progress = new ProgressDisplayHelper(100_000_000, 1000_000_000);
		progress.start(Integer.MAX_VALUE);
		
		Assert.assertEquals(1, IntSquareRoot.sqrtIfPerfectSquare(1));
		int nextSqrt = 2;
		int nextI = nextSqrt * nextSqrt;
		int i = 2;
		for (;;) {
			for(; i < nextI; i++) {
				int actualErr = IntSquareRoot.sqrtIfPerfectSquare(i);
				Assert.assertEquals(-1, actualErr);
				
				progress.incrPrint();
			}
			int actualNextSqrt = IntSquareRoot.sqrtIfPerfectSquare(i);
			Assert.assertEquals(nextSqrt, actualNextSqrt);
			i++;
			progress.incrPrint();

			nextSqrt++;
			long nextSquareLong = (long)nextSqrt * nextSqrt;
			if (nextSquareLong >= Integer.MAX_VALUE) {
				break;
			}
			nextI = (int) nextSquareLong;
		}
		for(; i < Integer.MAX_VALUE; i++) {
			Assert.assertEquals(-1, IntSquareRoot.sqrtIfPerfectSquare(i));
		}
	}
}
