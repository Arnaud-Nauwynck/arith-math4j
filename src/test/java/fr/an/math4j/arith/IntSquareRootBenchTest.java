package fr.an.math4j.arith;

import org.junit.Ignore;
import org.junit.Test;

public class IntSquareRootBenchTest {

	static int NB_REPEAT = 2; // repeat several time to give a change to hotspot to optimize cod, and take averages..

	@Test
	public void benchIterate() {
		for (int nbRepeat = 0; nbRepeat < NB_REPEAT; nbRepeat++) {
			long startRef = System.currentTimeMillis();
			for (int i = 1; i < Integer.MAX_VALUE; i++) {
				dummyDoNothing(i);
			}
			long timeMillisRef = System.currentTimeMillis() - startRef;
			System.out.println("iterating for all int in 1..MAX_INTEGER, took " + timeMillisRef + "ms");
		}
	}
	
	/**
	 * cf log:
	 * <PRE>
	 * computing sqrtIfPerfectSquare_slow() for all int in 1..MAX_INTEGER, took 12541ms
	 * iterating for all int in 1..MAX_INTEGER, took 517ms
	 * </PRE>
	 */
	@Test @Ignore // slow: 25.2s
	public void benchALL_testSqrtIfPerfectSquare_slow() {
		for (int nbRepeat = 0; nbRepeat < NB_REPEAT; nbRepeat++) {
			long start = System.currentTimeMillis();
			int last = 0; // keep last value.. avoid hotspot inline intrinsic + remove code without side-effect!
			for (int i = 1; i < Integer.MAX_VALUE; i++) {
				last = IntSquareRoot.sqrtIfPerfectSquare_slow(i);
				if (i == 1000_000) {
					System.out.println("check 100000=>" + last);
				}
			}
			long timeMillis = System.currentTimeMillis() - start;
			System.out.println("computing sqrtIfPerfectSquare_slow() for all int in 1..MAX_INTEGER, took " + timeMillis + "ms");
			System.out.println("last: " + last);
		}
	}
	
	
	/**
	 * computing heuristicCandidatePerfectSquare256() for all int in 1..MAX_INTEGER, took 407ms, heuristiq=81.25%
	 * (computing actual sqrt ints takes 3.5s)
	 */
	@Test
	public void benchHeuristicCandidatePerfectSquare64_ALL() {
		for (int nbRepeat = 0; nbRepeat < NB_REPEAT; nbRepeat++) {
			long start = System.currentTimeMillis();
			boolean dummyNoInline = false;
			for (int i = 1; i < Integer.MAX_VALUE; i++) {
				boolean res = 
						(IntSquareRoot.MASK_SQR_CANDIDATES_MOD64 & (1L << (i & IntSquareRoot.MASK_MOD_64))) != 0;
						// IntSquareRoot.heuristicCandidatePerfectSquareMod64(i);
						// noInline_heuristicCandidatePerfectSquareMod64(i);
				if (i == 1000_000) {
					dummyNoInline = res;
				}
			}
			long timeMillis = System.currentTimeMillis() - start;
			System.out.println("computing heuristicCandidatePerfectSquare64() for all int in 1..MAX_INTEGER, took " + timeMillis + "ms");
			System.out.println("dummyNoInline: " + dummyNoInline);
		}
	}
	
	static boolean noInline_heuristicCandidatePerfectSquareMod64(int i) {
		dummy();
		return IntSquareRoot.heuristicCandidatePerfectSquareMod64(i);
	}
		
	/**
	 * computing heuristicCandidatePerfectSquare256() for all int in 1..MAX_INTEGER, took 402ms
	 * (computing actual sqrt ints takes 3.5s)
	 */
	@Test
	public void benchHeuristicCandidatePerfectSquare256_ALL() {
		for (int nbRepeat = 0; nbRepeat < NB_REPEAT; nbRepeat++) {
			long start = System.currentTimeMillis();
			boolean dummyNoInline = false; 
			for (int i = 1; i < Integer.MAX_VALUE; i++) {
				boolean res = IntSquareRoot.heuristicCandidatePerfectSquareMod256(i);
				if (i == 1000_000) {
					dummyNoInline = res;
				}
			}
			long timeMillis = System.currentTimeMillis() - start;
			System.out.println("computing heuristicCandidatePerfectSquare256() for all int in 1..MAX_INTEGER, took " + timeMillis + "ms");
			System.out.println("dummyNoInline: " + dummyNoInline);
		}
	}
	
	
	/**
	 * computing heuristicCandidatePerfectSquareMod_slow() for all int in 1..MAX_INTEGER, took 5000ms
	 * SLOWER than actually computing for ints !!!!
	 */
	@Test @Ignore // slow: 10s
	public void benchHeuristicCandidatePerfectSquareMod_slow_ALL() {
		for (int nbRepeat = 0; nbRepeat < NB_REPEAT; nbRepeat++) {
			long start = System.currentTimeMillis();
			boolean last = false; 
			for (int i = 1; i < Integer.MAX_VALUE; i++) {
				last = IntSquareRoot.heuristicCandidatePerfectSquareMod_slow(i);
				if (i == 1000_000) {
					System.out.println("check 100000=>" + last);
				}
			}
			long timeMillis = System.currentTimeMillis() - start;
			System.out.println("computing heuristicCandidatePerfectSquareMod_slow() for all int in 1..MAX_INTEGER, took " + timeMillis + "ms");
			System.out.println("last: " + last);
		}		
	}
	

	/**
	 * cf log:
	 * <PRE>
	 * computing sqrtIfPerfectSquare() for all int in 1..MAX_INTEGER, took 2884ms  (without heuristic: 3633ms, with heuristic256: 3381ms  ()
	 * iterating for all int in 1..MAX_INTEGER, took 518ms
	 * </PRE>
	 */
	@Test
	public void benchALL_testSqrtIfPerfectSquare() {
		for (int nbRepeat = 0; nbRepeat < NB_REPEAT; nbRepeat++) {
			long start = System.currentTimeMillis();
			for (int i = 1; i < Integer.MAX_VALUE; i++) {
				IntSquareRoot.sqrtIfPerfectSquare(i);
			}
			long timeMillis = System.currentTimeMillis() - start;
			System.out.println("computing sqrtIfPerfectSquare() for all int in 1..MAX_INTEGER, took " + timeMillis + "ms");
		}
	}
	
	public static void dummyDoNothing(int i) {
		// do nothing (but do not inline by jvm hotspot?)
		if(i == 1) { 
			IntSquareRoot.sqrtIfPerfectSquare(1);
		}
	}

	static void dummy() {
	}
	
}
