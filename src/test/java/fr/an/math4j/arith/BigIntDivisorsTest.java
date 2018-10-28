package fr.an.math4j.arith;

import java.math.BigInteger;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import fr.an.math4j.arith.BigIntDivisors;

public class BigIntDivisorsTest {

	@Test
	public void test_6() {
		BigIntDivisors res = new BigIntDivisors(2*3);
		List<BigInteger> divisors = res.toList();
		Assert.assertEquals(3, divisors.size());
		Assert.assertEquals(BigInteger.valueOf(2), divisors.get(0));
		Assert.assertEquals(BigInteger.valueOf(3), divisors.get(1));
		Assert.assertEquals(BigInteger.valueOf(6), divisors.get(2));
	}

	@Test
	public void test_12() {
		BigIntDivisors res = new BigIntDivisors(2*2*3);
		List<BigInteger> divisors = res.toList();
		Assert.assertEquals(5, divisors.size());
		Assert.assertEquals(BigInteger.valueOf(2), divisors.get(0));
		Assert.assertEquals(BigInteger.valueOf(4), divisors.get(1));
		Assert.assertEquals(BigInteger.valueOf(3), divisors.get(2));
		Assert.assertEquals(BigInteger.valueOf(6), divisors.get(3));
		Assert.assertEquals(BigInteger.valueOf(12), divisors.get(4));
	}

}
