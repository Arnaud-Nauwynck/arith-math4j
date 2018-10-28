package fr.an.math4j.arith;

import java.math.BigInteger;

public class BigIntegerArithUtil {

	public static BigInteger comb(long n, long r) {
		if(r > n / 2) {
			r = n - r; // because C(n, r) == C(n, n - r)
		}
		BigInteger denom = factorial(n - r);
		BigInteger num = partialFactorial(r, n);
		BigInteger res = num.divide(denom);
		return res;
	}
	
	public static BigInteger partialFactorial(long r, long n) { 
		BigInteger res = BigInteger.ONE;
		for(long i = r+1; i <= n; i++) {
			res = res.multiply(BigInteger.valueOf(i));
		}
		return res;
	}
	
	public static BigInteger factorial(long n) {
		BigInteger res = BigInteger.ONE;
		for(long  i = 2 ; i <= n; i++) {
			res = res.multiply(BigInteger.valueOf(i));
		}
		return res;
	}

	public static BigInteger gcd(BigInteger a, BigInteger b) {
		return a.gcd(b);
	}
	
	
	public static BigInteger pow(BigInteger base, int exponent) {
		if (exponent <= 1) {
	        if (exponent < 0) {
	            throw new ArithmeticException("Negative exponent");
	        }
	        if (exponent == 0) {
	        	return BigInteger.ONE;
	        }
	        if (exponent == 1) {
	        	return base;
	        }
		}
		BigInteger res = BigInteger.ONE;
        BigInteger pow2 = base;
        while (exponent > 0) {
            if ((exponent & 1) == 1) {
                res = res.multiply(pow2);        
            }
            exponent >>= 1;
            pow2 = pow2.multiply(pow2);
        }
        return res;
	}

	
	public static BigInteger powModulo(BigInteger base, BigInteger exponent, BigInteger modulo) {
		if (exponent.signum() < 0) {
            throw new ArithmeticException("Negative exponent");
		} else if (exponent.signum() == 0) {
			return BigInteger.ONE;
        } else if (exponent.equals(BigInteger.ONE)) {
        	return base;
		}
		BigInteger res = BigInteger.ONE;
        BigInteger pow2 = base;
        for(int i = 0, len = exponent.bitLength(); i < len; i++) {
            if (exponent.testBit(i)) {
                res = res.multiply(pow2).mod(modulo);        
            }
			pow2 = pow2.multiply(pow2).mod(modulo);
        }
        return res;
	}

	public static BigInteger[] powsUpTo(BigInteger base, int pow) {
		BigInteger[] res = new BigInteger[pow+1];
		res[0] = BigInteger.ONE;
		res[1] = base;
		BigInteger curr = base; 
		for(int i = 2; i <= pow; i++) {
			curr = curr.multiply(base);
			res[i] = curr;
		}
		return res;
	}
	
}
