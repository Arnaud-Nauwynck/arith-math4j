package fr.an.math4j.arith;

import org.apache.commons.math3.fraction.BigFraction;
import org.junit.Assert;
import org.junit.Test;

import fr.an.math4j.arith.FaulhaberSumPowers;
import fr.an.math4j.arith.LongArithUtil;

public class FaulhaberSumPowersTest {


	@Test
	public void testSumPowers_mod() {
		long mod = 1000000007;

		// 1^3 + 2^3 + .. 5^3    , n=5
		// = 1 / 4 * n^2 + 1 / 2 * n^3 + 1 / 4 * n^4
		// = 1/4 ( n^2+ 2 n^3 + n^4 ) 
		// = 1/4 ( 25 + 2*125 + 625 )
		// = 1/4 ( 25 + 250 + 625 )
		// = 1/4 ( 25 + 250 + 625 )
		// = 1/4 ( 900 ) = 225
		doTestSumPowers_mod_byBernoulliNumber(3, 5, mod);
		
		// 1^2 + 2^2 + ... n^2
		// = 1/6 ( 2 n^3 + 3 n^2 + n)
		// = 1/3 ( n^3 + 3/2 n^2 + 1/2 n)
		// for n=5: 1/6 ( 2 * 125 + 3 * 25 + 5) = 1/6 ( 330) = 55
		//          1/6 ( 250 + 75 + 5)
		//          1/3 ( 125 + 3/2*25 + 5/2)
		//          1/3 ( 165 )
		doTestSumPowers_mod_byBernoulliNumber(2, 5, mod);
		
		// for n=10 :  1/6 ( 2 * 1000 + 3 * 100 + 10) = 1/6 ( 2310) = 385
		doTestSumPowers_mod_byBernoulliNumber(2, 10, mod);
		
		
		doTestSumPowers_mod_byBernoulliNumber(2, 100, mod);
		
		for (int pow = 2; pow < 5; pow++) {
			for (int n = 5; n < 10; n++) {
				doTestSumPowers_mod_byBernoulliNumber(pow, n, mod);
			}
		}
	}

	private void doTestSumPowers_mod_byBernoulliNumber(int pow, long N, long mod) {
		long res = FaulhaberSumPowers.sumPowers_mod_byBernoulliNumber(pow, N, mod);
		long expected = 0;
		for (int i = 1; i <= N; i++) {
			expected = (expected + LongArithUtil.pow_mod(i, pow, mod)) % mod; 
		}
		if (expected != res) {
			res = FaulhaberSumPowers.sumPowers_mod_byBernoulliNumber(pow, N, mod); // debug redo
			Assert.assertEquals(expected, res);
		}
	}
	
	@Test
	public void testToStringFaulhaberCoef() {
		int n = 50;
		BigFraction[][] coefs = FaulhaberSumPowers.faulhaberCoefs(n);
		Assert.assertEquals("sum_{i=1}^n i^1 = 1 / 2 * n^1 + 1 / 2 * n^2", FaulhaberSumPowers.toStringFaulhaberCoef(1, coefs));
		Assert.assertEquals("sum_{i=1}^n i^2 = 1 / 6 * n^1 + 1 / 2 * n^2 + 1 / 3 * n^3", FaulhaberSumPowers.toStringFaulhaberCoef(2, coefs));
		Assert.assertEquals("sum_{i=1}^n i^3 = 1 / 4 * n^2 + 1 / 2 * n^3 + 1 / 4 * n^4", FaulhaberSumPowers.toStringFaulhaberCoef(3, coefs));
		for(int pow = 2; pow < n; pow++) {
			String res = FaulhaberSumPowers.toStringFaulhaberCoef(pow, coefs);
			Assert.assertTrue(res.endsWith(" n^" + (pow+1)));
		}
	}
	
	@Test
	public void testPrintFaulhaberCoef() {
		int n = 50;
		BigFraction[][] coefs = FaulhaberSumPowers.faulhaberCoefs(n);
		for(int pow = 2; pow < n; pow++) {
			System.out.println(FaulhaberSumPowers.toStringFaulhaberCoef(pow, coefs));
		}
	}

	
	@Test
	public void testSumPowers_mod_faulhabert() {
		long mod = 1000000007;
		BigFraction[][] faulhabertCoefs = FaulhaberSumPowers.faulhaberCoefs(10);
		// 1^2 + 2^2 + ... n^2
		// = 1/6 ( 2 n^3 + 3 n^2 + n)
		// = 1/3 ( n^3 + 3/2 n^2 + 1/2 n)
		
		// for n=5: 1/6 ( 2 * 125 + 3 * 25 + 5) = 1/6 ( 330) = 55
		//          1/3 ( 125 + 3/2*25 + 5/2)
		//          1/3 ( 165 )
		doTestSumPowers_mod_faulhabert(2, 5, mod, faulhabertCoefs);
		
		// for n=10 :  1/6 ( 2 * 1000 + 3 * 100 + 10) = 1/6 ( 2310) = 385
		doTestSumPowers_mod_faulhabert(2, 10, mod, faulhabertCoefs);
		
		
		doTestSumPowers_mod_faulhabert(2, 100, mod, faulhabertCoefs);
		
		for (int pow = 2; pow < 5; pow++) {
			for (int n = 5; n < 10; n++) {
				doTestSumPowers_mod_faulhabert(pow, n, mod, faulhabertCoefs);
			}
		}
	}

	private void doTestSumPowers_mod_faulhabert(int pow, long N, long mod,
			BigFraction[][] cachedFaulhaberCoefs) {
		long res = FaulhaberSumPowers.sumPowers_mod_faulhabert(pow, N, mod, cachedFaulhaberCoefs[pow]);
		long expected = 0;
		for (int i = 1; i <= N; i++) {
			expected = (expected + LongArithUtil.pow_mod(i, pow, mod)) % mod; 
		}
		if (expected != res) {
			Assert.assertEquals(expected, res);
		}
	}
	

}
