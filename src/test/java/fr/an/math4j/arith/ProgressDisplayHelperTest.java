package fr.an.math4j.arith;

import org.junit.Assert;
import org.junit.Test;

import fr.an.math4j.arith.ProgressDisplayHelper;

public class ProgressDisplayHelperTest {

	@Test
	public void testMillisToText() {
		Assert.assertEquals("10ms",ProgressDisplayHelper.millisToText(10));
		Assert.assertEquals("1.1s",ProgressDisplayHelper.millisToText(1100));
		Assert.assertEquals("45.0s",ProgressDisplayHelper.millisToText(45_000));
		Assert.assertEquals("1mn00s",ProgressDisplayHelper.millisToText(60_000));
		Assert.assertEquals("1mn01s",ProgressDisplayHelper.millisToText(61_000));
		Assert.assertEquals("1:00h",ProgressDisplayHelper.millisToText(3600_000));
		Assert.assertEquals("1:01h",ProgressDisplayHelper.millisToText(3660_000));
	}
}
