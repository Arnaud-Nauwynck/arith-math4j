package fr.an.math4j.arith;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class Permutation {

	private List<int[]> _result = new ArrayList<int[]>();

	// ------------------------------------------------------------------------
	
	public Permutation() {
	}

	// ------------------------------------------------------------------------
	
	/**
	 * @param array
	 *            working set
	 * @param i
	 *            index to swap from
	 * @param j
	 *            index to swap to
	 */
	private void swap(int[] array, int i, int j) {
		int tmp = array[i];
		array[i] = array[j];
		array[j] = tmp;

	}

	/**
	 * @param array
	 *            working set
	 * @param n
	 *            index (max length of array for 1st iteration)
	 */
	private void _perm(int[] array, int n) {
		// went through all iterations?
		if (n == 1) {
			// System.out.println("return " + array[0]+array[1]);
			_result.add(array.clone());
			return;
		}

		// using Dijkstra algorithm / recursion
		for (int i = 0; i < n; i++) {
			swap(array, i, n - 1);
			_perm(array, n - 1);
			swap(array, i, n - 1);
		}
	}

	/**
	 * @param input
	 *            integer to permutate
	 * @return list of all permutations
	 */
	public List<int[]> permutate(int input) {
		_result = new ArrayList<int[]>();
		String digitsStr = Integer.toString(input);
		int len = digitsStr.length();
		int[] digits = new int[len];
		for (int i = 0; i < len; i++) {
			digits[i] = digitsStr.charAt(i) - '0';
		}
		_perm(digits, digits.length);
		return _result;
	}

	/**
	 * @param input
	 *            input array to permutate
	 * @return list of all permutations
	 */
	public List<int[]> permutate(int[] input) {
		_result = new ArrayList<int[]>();
		_perm(input, input.length);
		return _result;
	}

	/**
	 * @param digits
	 *            byte array containing all 10 digits
	 * @param permutation
	 *            number of the permutation to be calculated
	 * @return the resulting permutated array of digits
	 * @note this function is adapted from a javascript function found on the
	 *       internet
	 */
	public static byte[] permutenth(byte[] digits, int permutation) {
		byte j = 0, k = 0;
		int factorials[] = new int[digits.length];
		byte idn[] = new byte[digits.length];

		// calculate factorials up to n
		factorials[digits.length - 1] = 1;
		for (j = (byte) (digits.length - 2); j >= 0; --j)
			factorials[j] = factorials[j + 1] * (digits.length - 1 - j);

		// ???
		for (j = 0; j < (byte) (digits.length - 1); ++j)
			idn[j] = j;

		// switch the digits
		for (j = 0; j < digits.length; ++j) {
			// the factorials define what numbers can stay the same and what has
			// to move
			// assume we have a set {1,2,3,4,5} and want the 5 permutation:
			// we can now safely say we don't need to move the 1 and 2 because
			// with the subset
			// {3,4,5} we already have 3! permutations to do (3! > 5)
			byte idx = (byte) (permutation / factorials[j]);
			byte tmp = idn[j];
			idn[j] = idn[j + idx];
			idn[j + idx] = tmp;
			tmp = digits[j + idx];

			for (k = idx; k > 0; --k)
				digits[j + k] = digits[j + k - 1];

			digits[j] = tmp;

			// remove the already done permutations
			permutation -= (idx * factorials[j]);
		}
		return digits;
	}
}
