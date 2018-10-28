package fr.an.math4j.arith;

import java.util.ArrayList;
import java.util.List;

public class NumberPrimeDecompUtil {
	
	public static NumberPrimeDecomp comb(int n, int r) {
		NumberPrimeDecomp denom = factorial(n - r);
		NumberPrimeDecomp num = partialFactorial(r, n) ;
		NumberPrimeDecomp res = num.divide(denom);
		return res;
	}
	
	public static NumberPrimeDecomp partialFactorial(int r, int n) { 
		List<NumberAndExponent> res = new ArrayList<NumberAndExponent>();
		for(int i = r+1; i <= n; i++) {
			List<NumberAndExponent> decompI = NumberPrimeDecomp.decomposeInPrimeFactorList(i);
			res = NumberPrimeDecomp.mult(res, decompI);
		}
		return new NumberPrimeDecomp(res);
	}
	
	public static NumberPrimeDecomp factorial(int n) {
		List<NumberAndExponent> res = new ArrayList<NumberAndExponent>();
		for(int i = 2 ; i <= n; i++) {
			List<NumberAndExponent> decompI = NumberPrimeDecomp.decomposeInPrimeFactorList(i);
			res = NumberPrimeDecomp.mult(res, decompI);
		}
		return new NumberPrimeDecomp(res);
	}
	
}
