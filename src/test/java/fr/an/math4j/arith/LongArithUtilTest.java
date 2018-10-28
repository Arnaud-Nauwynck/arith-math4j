package fr.an.math4j.arith;

import org.junit.Assert;
import org.junit.Test;

import fr.an.math4j.arith.LongArithUtil;
import fr.an.math4j.arith.LongArrays;

public class LongArithUtilTest {

	@Test
	public void testPow() {
		for(int base = 1; base < 20; base++) {
			for(int exp = 1; exp < 10; exp++) {
				long expected = (long) Math.pow(base, exp);
				long actual = LongArithUtil.pow(base, exp);
				Assert.assertEquals(expected, actual);
			}
		}
	}
	
	@Test
	public void testPow_mod() {
		long MOD = 1000000007;
		for(int base = 1; base < 20; base++) {
			for(int exp = 1; exp < 10; exp++) {
				long expected = (long) Math.pow(base, exp) % MOD;
				long actual = LongArithUtil.pow_mod(base, exp, MOD);
				Assert.assertEquals(expected, actual);
			}
		}
	}

	@Test
	public void testPowsUpTo() {
		int base = 2;
		long[] pows = LongArithUtil.powsUpTo(base, 10);
		Assert.assertEquals(10+1, pows.length);
		for(int i = 0; i <= 10; i++) {
			long expected = (long) Math.pow(base, i);
			Assert.assertEquals(expected, pows[i]);
		}
	}

	@Test
	public void testPowsUpTo_mod() {
		int base = 2;
		long MOD = 1000000007;
		long[] pows_mod = LongArithUtil.powsUpTo_mod(2, 10, MOD);
		Assert.assertEquals(10+1, pows_mod.length);
		for(int i = 0; i <= 10; i++) {
			long expected = LongArithUtil.pow_mod(base, i, MOD);
			Assert.assertEquals(expected, pows_mod[i]);
		}
	}

	
	// TODO !!!
//	@Test
//	public void testAllCombinations_pascalTriangle() {
//		int[][] res = LongArithUtil.allCombinations_pascalTriangle(10);
//		IntArrays.assertEquals(new int[] { 1,  1 }, res[1]);
//		IntArrays.assertEquals(new int[] { 1,  2, 1 }, res[2]);
//		IntArrays.assertEquals(new int[] { 1,  3, 3, 1 }, res[3]);
//		IntArrays.assertEquals(new int[] { 1,  4, 6, 4, 1 }, res[4]);
//	}

	@Test
	public void testCombinations() {
		LongArrays.assertEquals(new long[] { 1,  2, 1 }, LongArithUtil.combinations(2));
		LongArrays.assertEquals(new long[] { 1,  3, 3, 1 }, LongArithUtil.combinations(3));
		LongArrays.assertEquals(new long[] { 1,  4, 6, 4, 1 }, LongArithUtil.combinations(4));
		LongArrays.assertEquals(new long[] { 1,  5, 10, 10, 5, 1 }, LongArithUtil.combinations(5));
	}


	@Test
	public void testGeometricSumMod() {
		long mod = 1000000007;
		long q = 3;
		// q + q^2 + q^3
		long res3 = LongArithUtil.geometricSumMod(q, q, 3, mod);
		Assert.assertEquals(q+q*q+q*q*q, res3);
		// q + q^2 + q^3 + q^4
		long res4 = LongArithUtil.geometricSumMod(q, q, 4, mod);
		Assert.assertEquals(q+q*q+q*q*q + q*q*q*q, res4);
	}
	
}
