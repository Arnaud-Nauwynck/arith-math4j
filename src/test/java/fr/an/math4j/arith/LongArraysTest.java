package fr.an.math4j.arith;

import org.junit.Assert;
import org.junit.Test;

public class LongArraysTest {

	@Test
	public void testAllocMatrice() {
		long[][] a = new long[2][3];
		Assert.assertEquals(2, a.length);
		for(int i = 0; i < 2; i++) {
			Assert.assertEquals(3, a[i].length);
		}
	}
}
