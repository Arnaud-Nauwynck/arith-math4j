package fr.an.math4j.arith;

import org.junit.Assert;
import org.junit.Test;

import fr.an.math4j.arith.EulerPhiFunction;
import fr.an.math4j.arith.PrimeNumberUtils;

public class EulerPhiFunctionTest {

	@Test
	public void testEulerphi_sieve() {
		int[] sieveDivisors = PrimeNumberUtils.eratosteneMaxPrimeDivisorsUpTo(1_000_000);
		for(int i = 1; i < sieveDivisors.length; i++) {
			int check = (int) EulerPhiFunction.computePhi(i);
			int res = EulerPhiFunction.eulerphi_sieve(i, sieveDivisors);
			Assert.assertEquals(check, res);
		}
	}
}
