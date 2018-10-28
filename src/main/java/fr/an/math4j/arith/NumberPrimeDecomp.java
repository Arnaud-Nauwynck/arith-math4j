package fr.an.math4j.arith;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * efficient number factorisation: cf https://www.alpertron.com.ar/ECM.HTM
 * 
 *
 */
public class NumberPrimeDecomp {

	private NumberAndExponent[] decomp;
	
	// ------------------------------------------------------------------------
	
//	private NumberPrimeDecomp(NumberAndExponent[] decomp) {
//		this.decomp = decomp;
//	}

	public NumberPrimeDecomp(List<NumberAndExponent> src) {
		this.decomp = src.toArray(new NumberAndExponent[src.size()]);
	}

	public static NumberPrimeDecomp decompInPrimeFactors(long number) {
		return new NumberPrimeDecomp(decomposeInPrimeFactorList(number));
	}
	
	public static NumberPrimeDecomp snew1() {
		return new NumberPrimeDecomp(new ArrayList<NumberAndExponent>());
	}
	
	// ------------------------------------------------------------------------

	public NumberAndExponent[] getDecomp() {
		return decomp;
	}

	public long computeValue() {
		long res = 1;
		for(NumberAndExponent elt : decomp) {
			res *= elt.getNumberPowerExponent();
		}
		return res;
	}
	
	public NumberPrimeDecomp multiply(NumberPrimeDecomp number) {
		List<NumberAndExponent> n1 = new ArrayList<NumberAndExponent>(Arrays.asList(decomp));
		List<NumberAndExponent> n2 = new ArrayList<NumberAndExponent>(Arrays.asList(number.decomp));
		List<NumberAndExponent> res = mult(n1, n2);
		return new NumberPrimeDecomp(res);
	}

	public NumberPrimeDecomp divide(NumberPrimeDecomp number) {
		List<NumberAndExponent> n1 = new ArrayList<NumberAndExponent>(Arrays.asList(decomp));
		List<NumberAndExponent> n2 = new ArrayList<NumberAndExponent>(Arrays.asList(number.decomp));
		List<NumberAndExponent> res = divide(n1, n2);
		return new NumberPrimeDecomp(res);
	}

	@Override
	public String toString() {
		return NumberAndExponent.toString(decomp);
	}
	
	// ------------------------------------------------------------------------
	

	public static List<NumberAndExponent> decomposeInPrimeFactorList(long number) {
		List<NumberAndExponent> res = new ArrayList<NumberAndExponent>();
		long remaining = number;
		int maxSqrt = (int) Math.sqrt(number) + 1;
		int[] primes = PrimeNumberUtils.getPrimesUpTo(maxSqrt);
		
		final int primesLen = primes.length;
		for (int primeIndex = 0, prime=primes[primeIndex]; prime < maxSqrt && primeIndex < primesLen; primeIndex++) {
			prime = primes[primeIndex];
			while ((remaining % prime) == 0) {
				int exponent = 1;
				remaining /= prime;
				while((remaining % prime) == 0) {
					exponent++;
					remaining /= prime;
				}
				res.add(new NumberAndExponent(prime, exponent));
				if (remaining == 1) {
					break;
				}
				maxSqrt = (int) Math.sqrt(number) + 1;
//				System.out.println("found factor: (prime) " + prime + "^" + exponent + " => remain " + remaining);
			}
		}
		if (remaining != 1) {
			res.add(new NumberAndExponent(remaining, 1));
		}
		return res;		
	}

//	public static List<NumberAndExponent> decomposeInPrimeFactorList(BigInteger number) {
//		List<NumberAndExponent> res = new ArrayList<NumberAndExponent>();
//		BigInteger remaining = number;
//		BigInteger maxSqrt = number; // TODO BigIntegerArithUtil.sqrtCeil(number);
//		
//		int[] primes = PrimeNumberUtils.getPrimesUpTo(maxSqrt.intValue());
//		final int primesLen = primes.length;
//		for (int primeIndex = 0, prime=primes[primeIndex]; prime < maxSqrt && primeIndex < primesLen; primeIndex++) {
//			prime = primes[primeIndex];
//			while ((remaining % prime) == 0) {
//				int exponent = 1;
//				remaining /= prime;
//				while((remaining % prime) == 0) {
//					exponent++;
//					remaining /= prime;
//				}
//				res.add(new NumberAndExponent(prime, exponent));
//				if (remaining == 1) {
//					break;
//				}
//				maxSqrt = (int) Math.sqrt(number) + 1;
////				System.out.println("found factor: (prime) " + prime + "^" + exponent + " => remain " + remaining);
//			}
//		}
//		if (remaining != 1) {
//			res.add(new NumberAndExponent(remaining, 1));
//		}
//		return res;		
//	}

	public static List<NumberAndExponent> decomposePossibleFactors(long number, List<NumberAndExponent> possibleFactors, long[] outRemainder) {
		List<NumberAndExponent> res = new ArrayList<NumberAndExponent>();
		long remainder = number;
		for(NumberAndExponent factor : possibleFactors) {
			long f = factor.getNumber();
			if ((remainder % f) == 0) {
				NumberAndExponent resElt = findOrInsertListElt(res, f);
				while ((remainder % f) == 0) {
					remainder = remainder / f;
					resElt.incrExponent();
				}
			}
		}
		outRemainder[0] = remainder;
		return res;
	}

	public static List<NumberAndExponent> decomposePossibleFactors(BigInteger number, List<NumberAndExponent> possibleFactors, BigInteger[] outRemainder) {
		List<NumberAndExponent> res = new ArrayList<NumberAndExponent>();
		BigInteger remainder = number;
		for(NumberAndExponent factor : possibleFactors) {
			long f = factor.getNumber();
			BigInteger[] qr = remainder.divideAndRemainder(BigInteger.valueOf(f));
			if (qr[1].equals(BigInteger.ZERO)) {
				NumberAndExponent resElt = findOrInsertListElt(res, f);
				while (qr[1].equals(BigInteger.ZERO)) {
					qr = remainder.divideAndRemainder(BigInteger.valueOf(f));
					remainder = qr[0];
					resElt.incrExponent();
				}
			}
		}
		outRemainder[0] = remainder;
		return res;
	}

	public static List<NumberAndExponent> decompose(long number, List<NumberAndExponent> possibleFactors) {
		long[] remainder = new long[1]; 
		// extract primes from candidate factors...
		// TODO List<NumberAndExponent> possiblePrimeFactors ...
		List<NumberAndExponent> decompose1 = decomposePossibleFactors(number, possibleFactors, remainder);
		List<NumberAndExponent> decomposeRemainder = decomposeInPrimeFactorList(remainder[0]);
		List<NumberAndExponent> res = mult(decompose1, decomposeRemainder);
		return res;
	}
	
	public static List<NumberAndExponent> decompose(BigInteger number, List<NumberAndExponent> possibleFactors) {
		BigInteger[] remainder = new BigInteger[1];
		// extract primes from candidate factors...
		// TODO List<NumberAndExponent> possiblePrimeFactors ...
		List<NumberAndExponent> decompose1 = decomposePossibleFactors(number, possibleFactors, remainder);
		List<NumberAndExponent> decomposeRemainder = decomposeInPrimeFactorList(remainder[0].longValue());
		List<NumberAndExponent> res = mult(decompose1, decomposeRemainder);
		return res;
	}
		
	public static void checkIncrFactors(List<NumberAndExponent> ls) {
		if (ls.size() <= 2) return;
		Iterator<NumberAndExponent> iter = ls.iterator();
		long prevFactor = iter.next().getNumber();
		for(; iter.hasNext(); ) {
			long factor = iter.next().getNumber();
			if (factor <= prevFactor) { 
				throw new IllegalStateException();
			}
			prevFactor = factor;
		}
	}
	
	public static NumberAndExponent findOrInsertListElt(List<NumberAndExponent> ls, long prime) { 
		NumberAndExponent res = null;
		int len = ls.size();
		for (int i = 0; i < len; i++) {
			NumberAndExponent elt = ls.get(i);
			if (elt.getNumber() >= prime) {
				if (elt.getNumber() == prime) {
					res = elt;
					break;
				} else {
					res = new NumberAndExponent(prime, 0);
					ls.add(i, res);
					break;
				}
			}
		}
		if (res == null) {
			res = new NumberAndExponent(prime, 0);
			ls.add(res);
		}
		return res;
	}

	// assume n1 and n2 decomposition elts are sorted
	public static List<NumberAndExponent> mult(List<NumberAndExponent> n1, List<NumberAndExponent> n2) {
		List<NumberAndExponent> res = new ArrayList<NumberAndExponent>();
		Iterator<NumberAndExponent> n1Iter = n1.iterator();
		Iterator<NumberAndExponent> n2Iter = n2.iterator();
		NumberAndExponent n1Next = (n1Iter.hasNext())? n1Iter.next() : null;
		NumberAndExponent n2Next = (n2Iter.hasNext())? n2Iter.next() : null;
		while (n1Next != null && n2Next != null) {
			long n1NextNumber = n1Next.getNumber();
			long n2NextNumber = n2Next.getNumber();
			if (n1NextNumber == n2NextNumber) {
				res.add(new NumberAndExponent(n1NextNumber, n1Next.getExponent() + n2Next.getExponent()));
				n1Next = (n1Iter.hasNext())? n1Iter.next() : null;
				n2Next = (n2Iter.hasNext())? n2Iter.next() : null;				
			} else if (n1NextNumber < n2NextNumber) {
				res.add(new NumberAndExponent(n1NextNumber, n1Next.getExponent()));
				n1Next = (n1Iter.hasNext())? n1Iter.next() : null;
			} else {
				res.add(new NumberAndExponent(n2NextNumber, n2Next.getExponent()));
				n2Next = (n2Iter.hasNext())? n2Iter.next() : null;				
			}
		}
		while(n1Next != null) {
			res.add(new NumberAndExponent(n1Next.getNumber(), n1Next.getExponent()));
			n1Next = (n1Iter.hasNext())? n1Iter.next() : null;
		}
		while(n2Next != null) {
			res.add(new NumberAndExponent(n2Next.getNumber(), n2Next.getExponent()));
			n2Next = (n2Iter.hasNext())? n2Iter.next() : null;				
		}
		return res;
	}	
	
	// assume n1 and n2 decomposition elts are sorted
	public static List<NumberAndExponent> divide(List<NumberAndExponent> n1, List<NumberAndExponent> n2) {
		List<NumberAndExponent> res = new ArrayList<NumberAndExponent>();
		Iterator<NumberAndExponent> n1Iter = n1.iterator();
		Iterator<NumberAndExponent> n2Iter = n2.iterator();
		NumberAndExponent n1Next = (n1Iter.hasNext())? n1Iter.next() : null;
		NumberAndExponent n2Next = (n2Iter.hasNext())? n2Iter.next() : null;
		while (n1Next != null && n2Next != null) {
			long n1NextNumber = n1Next.getNumber();
			long n2NextNumber = n2Next.getNumber();
			if (n1NextNumber == n2NextNumber) {
				res.add(new NumberAndExponent(n1NextNumber, n1Next.getExponent() - n2Next.getExponent()));
				n1Next = (n1Iter.hasNext())? n1Iter.next() : null;
				n2Next = (n2Iter.hasNext())? n2Iter.next() : null;				
			} else if (n1NextNumber < n2NextNumber) {
				res.add(new NumberAndExponent(n1NextNumber, n1Next.getExponent()));
				n1Next = (n1Iter.hasNext())? n1Iter.next() : null;
			} else {
				res.add(new NumberAndExponent(n2NextNumber, - n2Next.getExponent()));
				n2Next = (n2Iter.hasNext())? n2Iter.next() : null;				
			}
		}
		while(n1Next != null) {
			res.add(new NumberAndExponent(n1Next.getNumber(), n1Next.getExponent()));
			n1Next = (n1Iter.hasNext())? n1Iter.next() : null;
		}
		while(n2Next != null) {
			res.add(new NumberAndExponent(n2Next.getNumber(), - n2Next.getExponent()));
			n2Next = (n2Iter.hasNext())? n2Iter.next() : null;				
		}
		return res;
	}	

	public static long computeValue(List<NumberAndExponent> decomp) {
		long res = 1;
		for(NumberAndExponent elt : decomp) {
			res *= elt.getNumberPowerExponent();
		}
		return res;
	}
	
}
