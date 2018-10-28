package fr.an.math4j.arith;

import java.util.List;

import org.apache.commons.math3.fraction.BigFraction;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import fr.an.math4j.arith.BigFractionBernoulli;
import fr.an.math4j.arith.ProgressDisplayHelper;

public class BigFractionBernoulliTest {

	/**
	 * 0..1000 took 14mn (=848 s) !
	 */
	@Ignore @Test
	public void testBig() {
		int N = 1000;
		ProgressDisplayHelper progress = new ProgressDisplayHelper(100, 1_000);
		progress.start(N);
		long startTime = System.currentTimeMillis();
		for(int i = 0; i < N; i++) {
			BigFractionBernoulli.at(i);
			progress.incrPrint();
		}
		long timeMillis = System.currentTimeMillis() - startTime;
		System.out.println("B 0.." + N + " took " + timeMillis);
	}

	@Ignore
	@Test
	public void testBernoullis2nUpTo() {
		List<BigFraction> res = BigFractionBernoulli.bernoullis2nUpTo(50);
		System.out.println(res);
	}
	
}
