package fr.an.math4j.arith;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import fr.an.math4j.arith.LongDivisors;
import fr.an.math4j.arith.NumberAndExponent;

public class LongDivisorsTest {

	@Test
	public void test_6() {
		List<Long> divisors = new LongDivisors(2*3).toList(); 
		Assert.assertEquals(3, divisors.size());
		Assert.assertEquals(Long.valueOf(2), divisors.get(0));
		Assert.assertEquals(Long.valueOf(3), divisors.get(1));
		Assert.assertEquals(Long.valueOf(6), divisors.get(2));
	}

	@Test
	public void test_12() {
		List<Long> divisors = new LongDivisors(2*2*3).toList();
		Assert.assertEquals(5, divisors.size());
		Assert.assertEquals(Long.valueOf(2), divisors.get(0));
		Assert.assertEquals(Long.valueOf(4), divisors.get(1));
		Assert.assertEquals(Long.valueOf(3), divisors.get(2));
		Assert.assertEquals(Long.valueOf(6), divisors.get(3));
		Assert.assertEquals(Long.valueOf(12), divisors.get(4));
	}

	@Test
	public void testDivisors2pow60minus1() {
		NumberAndExponent[] mDecomp = new NumberAndExponent[] {
			new NumberAndExponent(3, 2),
			new NumberAndExponent(5, 2),
			new NumberAndExponent(7, 1),
			new NumberAndExponent(11, 1),
			new NumberAndExponent(13, 1),
			new NumberAndExponent(31, 1),
			new NumberAndExponent(41, 1),
			new NumberAndExponent(61, 1),
			new NumberAndExponent(151, 1),
			new NumberAndExponent(331, 1),
			new NumberAndExponent(1321, 1)
		};
		for (Long div : new LongDivisors(mDecomp)) {
			long n = div;
			System.out.println(n);
		}
	}
}
