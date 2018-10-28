package fr.an.math4j.arith;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import fr.an.math4j.arith.NumberAndExponent;
import fr.an.math4j.arith.NumberFormatUtils;
import fr.an.math4j.arith.NumberPrimeDecomp;
import fr.an.math4j.arith.PrimeNumberUtils;

public class PrimeNumberUtilsTest {

	@Test
	public void testSlowIsPrime() {
		for (int i = 2; i < 1500; i++) {
			if (PrimeNumberUtils.slowIsPrime(i)) {
				System.out.print(i + ", ");
			}
		}
	}
	
	@Test
	public void testGetNthPrimes() {
		int n;
		int[] primes;
		n = 100;
		primes = PrimeNumberUtils.getNthFirstPrimes(n);
		System.out.println(n + " first primes\n:" + NumberFormatUtils.arrayToString(primes, 250));
		n = 1000;
		primes = PrimeNumberUtils.getNthFirstPrimes(n);
		System.out.println(n + " first primes\n:" + NumberFormatUtils.arrayToString(primes, 250));
		n = 10000;
		primes = PrimeNumberUtils.getNthFirstPrimes(n);
		System.out.println(n + " first primes\n:" + NumberFormatUtils.arrayToString(primes, 250));
	}
	
	@Test
	public void testDecompose() {
		long n = 2*2*2*5*7;
		List<NumberAndExponent> decomp = NumberPrimeDecomp.decomposeInPrimeFactorList(n);
		Assert.assertEquals(2, decomp.get(0).getNumber());
		Assert.assertEquals(3, decomp.get(0).getExponent());
		Assert.assertEquals(5, decomp.get(1).getNumber());
		Assert.assertEquals(1, decomp.get(1).getExponent());
		Assert.assertEquals(7, decomp.get(2).getNumber());
		Assert.assertEquals(1, decomp.get(2).getExponent());
		Assert.assertEquals(3, decomp.size());
	}

	@Test
	public void testDecomposePrimes() {
		int[] primes = PrimeNumberUtils.getNthFirstPrimes(100);
		for (int prime : primes) {
			List<NumberAndExponent> decomp = NumberPrimeDecomp.decomposeInPrimeFactorList(prime);
			Assert.assertEquals(prime, decomp.get(0).getNumber());
			Assert.assertEquals(1, decomp.get(0).getExponent());
			Assert.assertEquals(1, decomp.size());
		}
	}
	
	@Test
	public void testRecompose_2_3() {
		List<NumberAndExponent> factor1 = NumberPrimeDecomp.decomposeInPrimeFactorList(2);
		List<NumberAndExponent> factor2 = NumberPrimeDecomp.decomposeInPrimeFactorList(3);
		List<NumberAndExponent> factor = NumberPrimeDecomp.mult(factor1, factor2);
		Assert.assertEquals(2, factor.get(0).getNumber());
		Assert.assertEquals(1, factor.get(0).getExponent());
		Assert.assertEquals(3, factor.get(1).getNumber());
		Assert.assertEquals(1, factor.get(1).getExponent());
		Assert.assertEquals(2, factor.size());
	}

	@Test
	public void testRecompose_3_2() {
		List<NumberAndExponent> factor1 = NumberPrimeDecomp.decomposeInPrimeFactorList(3);
		List<NumberAndExponent> factor2 = NumberPrimeDecomp.decomposeInPrimeFactorList(2);
		List<NumberAndExponent> factor = NumberPrimeDecomp.mult(factor1, factor2);
		Assert.assertEquals(2, factor.get(0).getNumber());
		Assert.assertEquals(1, factor.get(0).getExponent());
		Assert.assertEquals(3, factor.get(1).getNumber());
		Assert.assertEquals(1, factor.get(1).getExponent());
		Assert.assertEquals(2, factor.size());
	}

	@Test
	public void testEratosteneMaxPrimeDivisorsUpTo() {
		int[] res = PrimeNumberUtils.eratosteneMaxPrimeDivisorsUpTo(1000_000);
		
		int[] primes = PrimeNumberUtils.getNthFirstPrimes(1000);
		for(int prime : primes) {
			Assert.assertEquals(prime, res[prime]);
			int multiple = 2*prime;
			Assert.assertEquals(prime, res[multiple]);
		}
	}
	
	@Test
	public void testEratosteneMinPrimeDivisorsUpTo() {
		int[] res = PrimeNumberUtils.eratosteneMinPrimeDivisorsUpTo(1000_000);

		int[] expected = new int[] {
			//0 1, 2, 3, 4, 5, 6, 7  8, 9, 10, 11 12  13 14 15 16
			1,  1, 2, 3, 2, 5, 2, 7, 2, 3, 2,  11, 2, 13, 2, 3, 2 
		};
		for (int i = 0; i < expected.length; i++) Assert.assertEquals(expected[i], res[i]);
		
		for(int i = 2; i < res.length; i++) {
			Assert.assertEquals(0, i % res[i]);
			Assert.assertTrue(PrimeNumberUtils.slowIsPrime(res[i]));
		}
		
	}

	@Test
	public void testEratosteneSieveRange() {
		int[] sieve = new int[] { 2, 3, 5, 7, 11, 13 };
		// long maxRange = 13^2-1;
		int len = 100;
		int first = 14;
		boolean[] res = new boolean[len];
		PrimeNumberUtils.eratosteneSieveRange(first, res, sieve);
		boolean[] checkRes = PrimeNumberUtils.eratosteneCrible(first+len);
		for(int i = 0; i < len; i++) {
			if(checkRes[first+i] != res[i]) {
				Assert.assertEquals("i:" + i, checkRes[first+i], res[i]);
			}
		}
	}
	
	@Test
	public void testPrimesUpToN() {
		int[] res3 = PrimeNumberUtils.getPrimesUpTo(3);
		Assert.assertEquals(2, res3.length);
		Assert.assertEquals(2, res3[0]);
		Assert.assertEquals(3, res3[1]);
		
		int[] res4 = PrimeNumberUtils.getPrimesUpTo(4);
		Assert.assertEquals(2, res4.length);
		Assert.assertEquals(2, res4[0]);
		Assert.assertEquals(3, res4[1]);
		
		int[] res100 = PrimeNumberUtils.getPrimesUpTo(100);
		Assert.assertEquals("2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97", IntArrays.toString(res100));
		Assert.assertEquals(97, res100[res100.length-1]);
	}
}
