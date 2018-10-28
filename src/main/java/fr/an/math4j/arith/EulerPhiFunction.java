package fr.an.math4j.arith;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * http://en.wikipedia.org/wiki/Euler's_totient_function
 */
public class EulerPhiFunction {

	private static final int[] SMALL_VALUES_100 = new int[] {
		0, 
		1, 	1, 	2, 	2, 	4, 	2, 	6, 	4, 	6,
		4, 	10, 	4, 	12, 	6, 	8, 	8, 	16, 	6, 	18,
		8 , 	12, 	10, 	22, 	8 ,		20,		12, 	18, 	12, 	28,
		8 , 	30, 	16, 	20, 	16, 	24, 	12, 	36, 	18, 	24,
		16, 	40, 	12, 	42, 	20, 	24, 	22, 	46, 	16, 	42,
		20, 	32, 	24, 	52, 	18, 	40, 	24, 	36, 	28, 	58,
		16, 	60,		30, 	36, 	32, 	48, 	20, 	66, 	32, 	44,
		24, 	70,		24, 	72, 	36, 	40, 	36, 	60, 	24, 	78,
		32, 	54, 	40, 	82, 	24, 	64, 	42, 	56, 	40, 	88,
		24, 	72, 	44, 	60, 	46, 	72, 	32, 	96, 	42, 	60
	};
	
	public static long computePhi(long n) {
		if (n < SMALL_VALUES_100.length) {
			return SMALL_VALUES_100[(int) n];
		}
		List<NumberAndExponent> decompElts = NumberPrimeDecomp.decomposeInPrimeFactorList(n);
		return computePhi(decompElts);
	}

	public static long computePhi(List<NumberAndExponent> decompElts) {
		long res = 1;
		for(NumberAndExponent elt : decompElts) {
			long prime = elt.getNumber();
			long exp = elt.getExponent();
			if (exp == 1) {
				res *= (prime - 1);
			} else {
				long primePowerExponent = elt.getNumberPowerExponent();
				res *= (primePowerExponent - primePowerExponent/prime);
			}
		}
		return res;
	}

	public static BigInteger computePhiBigInt(List<NumberAndExponent> decompElts) {
		BigInteger res = BigInteger.ONE;
		for(NumberAndExponent elt : decompElts) {
			long prime = elt.getNumber();
			long exp = elt.getExponent();
			if (exp == 1) {
				res = res.multiply(BigInteger.valueOf(prime - 1));
			} else {
				long primePowerExponent = elt.getNumberPowerExponent();
				res = res.multiply(BigInteger.valueOf(primePowerExponent - primePowerExponent/prime));
			}
		}
		return res;
	}

	public static List<NumberAndExponent> computePhiDecomp(List<NumberAndExponent> decompElts) {
		List<NumberAndExponent> res = new ArrayList<NumberAndExponent>();
		for(NumberAndExponent elt : decompElts) {
			long prime = elt.getNumber();
			int exp = elt.getExponent();
			List<NumberAndExponent> decompPrimeMinus1 = NumberPrimeDecomp.decomposeInPrimeFactorList(prime - 1);
			if (exp == 1) {
				res = NumberPrimeDecomp.mult(res, decompPrimeMinus1);
			} else {
				NumberAndExponent primePowExpMinus1 = new NumberAndExponent(prime, (exp-1));
				List<NumberAndExponent> decompPrimePowExpMinus1 = new ArrayList<NumberAndExponent>();
				decompPrimePowExpMinus1.add(primePowExpMinus1);
				res = NumberPrimeDecomp.mult(res, decompPrimeMinus1);
				res = NumberPrimeDecomp.mult(res, decompPrimePowExpMinus1);
			}
		}
		return res;
	}


	/**
	 * cf OEIS 
	 * notice there are better sub-linear algorithm... cf file 351_overview.pdf
	 */
	public static long sumEulerphi_sieve(int N, int[] cachedSieveDivisors) {
		long res = 0;
		for(int i = 1; i < N; i++) {
			res += eulerphi_sieve(i, cachedSieveDivisors);
		}
		return res;
	}

	/**
	 * using sieve for precalculated divisors 
	 */
	public static int eulerphi_sieve(int N, int[] cachedSieveDivisors) {
		int res = 1;
		for(int remain = N; remain != 1; ) {
			int prime = cachedSieveDivisors[remain];
			res *= (prime - 1);
			remain = remain / prime;
			for(; remain % prime == 0; remain /= prime) {
				res *= prime;
			}
		}
		return res;
	}
	
}
