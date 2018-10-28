package fr.an.math4j.arith;

import java.math.BigInteger;

import org.junit.Assert;
import org.junit.Test;

import fr.an.math4j.arith.BigIntegerArithUtil;

public class BigIntegerArithUtilTest {

	@Test
	public void testPowModulo() {
		BigInteger base = BigInteger.valueOf(123456);
		BigInteger modulo = BigInteger.valueOf(1_000_000_000);
		for(int i = 1; i < 30; i++) {
			BigInteger checkRes = BigIntegerArithUtil.pow(base, i).mod(modulo);
			BigInteger res = BigIntegerArithUtil.powModulo(base, BigInteger.valueOf(i), modulo);
			Assert.assertEquals(checkRes, res);
		}
	}
}
