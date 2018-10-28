package fr.an.math4j.arith;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import fr.an.math4j.arith.NumberAndExponent;
import fr.an.math4j.arith.NumberPrimeDecomp;

public class NumberPrimeDecompTest extends Assert {

	@Test
	public void testMult() {
		List<NumberAndExponent> n1 = NumberPrimeDecomp.decomposeInPrimeFactorList(2*3);
		List<NumberAndExponent> n2 = NumberPrimeDecomp.decomposeInPrimeFactorList(2*5);
		List<NumberAndExponent> res = NumberPrimeDecomp.mult(n1, n2);
		assertEquals(3, res.size());
		assertEquals(2, res.get(0).getNumber());
		assertEquals(2, res.get(0).getExponent());
		assertEquals(3, res.get(1).getNumber());
		assertEquals(1, res.get(1).getExponent());
		assertEquals(5, res.get(2).getNumber());
		assertEquals(1, res.get(2).getExponent());		
	}
	
	@Test
	public void testMult2() {
		for (int i = 2; i < 30; i++) {
			List<NumberAndExponent> n1 = NumberPrimeDecomp.decomposeInPrimeFactorList(i);
			for (int j = 2; j < 30; j++) {
				List<NumberAndExponent> n2 = NumberPrimeDecomp.decomposeInPrimeFactorList(j);
				List<NumberAndExponent> res = NumberPrimeDecomp.mult(n1, n2);
				assertEquals(i*j, NumberPrimeDecomp.computeValue(res));
				NumberPrimeDecomp.checkIncrFactors(res);
			}
		}
	}
	
}
