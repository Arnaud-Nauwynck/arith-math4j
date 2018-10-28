package fr.an.math4j.arith;

import java.math.BigInteger;

public class NumberDigitUtil {

	/**
	 * return number from digits
	 */
	public static long digitsToNumber(int[] digits, int start, int end) {
		long result = 0;
		for (int i = start; i < end; ++i) {
			result *= 10;
			result += digits[i];
		}
		return result;
	}
	
	/**
	 * return number from digits
	 */
	public static long digitsToNumber(int[] digits) {
		return digitsToNumber(digits, 0, digits.length);
	}
	
	/**
	 * return number from digits
	 */
	public static long digitsToNumber(byte[] digits, int start, int end) {
		long result = 0;
		for (int i = start; i < end; ++i) {
			result *= 10;
			result += digits[i];
		}
		return result;
	}
	
	/**
	 * return number from digits
	 */
	public static long digitsToNumber(byte[] digits) {
		return digitsToNumber(digits, 0, digits.length);
	}

	/**
	 * extract digits from number
	 */
	public static int[] numberToDigits(int number) {
		String str = Integer.toString(number);
		int len = str.length();
		int[] res = new int[len];
		for (int i = 0; i < len; i++) {
			res[i] = str.charAt(i) - '0';
		}
		return res;
	}
	
	/**
	 * extract digits from number
	 */
	public static int[] numberToDigits(long number) {
		String str = Long.toString(number);
		int len = str.length();
		int[] res = new int[len];
		for (int i = 0; i < len; i++) {
			res[i] = str.charAt(i) - '0';
		}
		return res;
	}
	

	/**
	 * @return true if number is palindromic
	 */
	public static boolean isPalindrome(BigInteger i) {
		// convert number to string
		String number = i.toString();
		// check each digit with the counter on the other end of the string
		int len2 = number.length() / 2;
		for (int n = 0, nopp=number.length()-1; n < len2; ++n,nopp--) {
			if (number.charAt(n) != number.charAt(nopp))
				return false;
		}
		return true;
	}

	/**
	 * @param b
	 *            number to reverse
	 * @return the reversed number (ie. 123 => 321)
	 */
	public static BigInteger reverseNumber(BigInteger b) {
		BigInteger res = BigInteger.ZERO;
		final String number = b.toString();
		final BigInteger v10 = BigInteger.valueOf(10);
		final int len = number.length();
		for (int n = len-1; n >= 0; n--) {
			int d = number.charAt(n) - '0';
			res = res.multiply(v10).add(BigInteger.valueOf(d));
		}
		return res;
	}
	
}
