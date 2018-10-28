package fr.an.math4j.arith;

import java.math.BigInteger;
import java.util.List;

import org.apache.commons.math3.fraction.BigFraction;

public class FaulhaberSumPowers {


    /**
     * Sum of powers $i^k$ can be done quickly using Bernoulli coefficients:
     * <PRE>$$ \sum_{i = 1}^n {i^pow}  = {1 \over {pow + 1}}\sum_{m = 0}^pow {pow+1\choose m} B_m n^{pow + 1 - m} $$</PRE>
     */
//    public static BigInteger sumPowers(int pow, long n) {
//    	List<BigFraction> bernoullis = bernoullis2nUpTo(k);
//    	BigInteger res = BigInteger.ZERO;
//    	
//    	return res;
//    }

//    public static BigInteger sumPowers(int pow, long n) {
//    	final List<BigFraction> bernoullis2n = bernoullis2nUpTo((pow + 1 + 1)/2);
//    	final long[] choose_pow = LongArithUtil.combinations(pow);
//    	final BigInteger bigN = BigInteger.valueOf(n);
//    	final BigInteger[] n_pows = BigIntegerArithUtil.powsUpTo(bigN, pow + 1);
//    	final BigInteger bigPowp1 = BigInteger.valueOf(pow + 1);
//    	// case m=0
//    	BigInteger c0 = n_pows[pow+1];
//    	// special case for odd m=1 ( b1 = 1/2)
//    	BigInteger c1 = bigPowp1.multiply(n_pows[pow]); // .. div next by 1/2
//    	// res = (res + c1) % mod;
//    	BigFraction sum2 = BigFraction.ZERO;
//    	for(int m = 2; m <= pow; m+=2) {
//    		BigFraction b = bernoullis2n.get(m/2);
//    		BigInteger c = n_pows[pow+1-m].multiply(BigInteger.valueOf(choose_pow[m]));
//    		c = (c * b_num) % mod;
//    		c = (2 * c / b_den) % mod; // pb div truncate!!  => mult by 2... divide at end??
//    		sum2 = (sum2 + c) % mod;
//    	}
//    	long res = (2*c0 + c1 + sum2) / 2;
//    	res = (res / (pow + 1));
//    	return res;
//    }

    
    
    public static long sumPowers_mod_byBernoulliNumber(int pow, long n, long mod) {
    	List<BigFraction> bernoullis2n = BigFractionBernoulli.bernoullis2nUpTo(1+(pow + 1)/2);
    	long[] choose_pow = LongArithUtil.combinations(pow+1);
    	long[] n_pows = LongArithUtil.powsUpTo_mod(n, pow + 1, mod);
    	long resNum = 0;
    	long resDenom = 1;
    	long c0 = n_pows[pow+1];
    	long c1 = ((pow+1) * n_pows[pow]) % mod;
    	resNum = (2 * c0 + c1) % mod;
    	resDenom = 2 * (pow + 1);
    	for(int m = 2; m <= pow; m+=2) {
    		BigFraction b = bernoullis2n.get(m/2);
    		long bmNum = b.getNumeratorAsLong();
    		long bmDenom = b.getDenominatorAsLong();
    		long coefNum = (bmNum * choose_pow[m]) % mod;
    		long coefDenom = (bmDenom * (pow + 1)) % mod; // TODO.. do once at end
    		long gcdCoef = LongArithUtil.gcd(Math.abs(coefNum), coefDenom);
    		if (gcdCoef != 1) {
    			coefNum = coefNum / gcdCoef;
    			coefDenom = coefDenom / gcdCoef;
    		}
    		long sumEltNum = (coefNum * n_pows[pow+1-m]) % mod;
    		long gcd = LongArithUtil.gcd(coefDenom, resDenom);
    		long coefDenom_gcd = coefDenom / gcd;
    		long resDenom_gcd = resDenom / gcd;
    		resNum = (resNum * coefDenom_gcd + sumEltNum * resDenom_gcd) % mod;
    		resDenom = resDenom * coefDenom_gcd;
    	}
    	long res = resNum / resDenom;
		return res % mod;
    }

    
    
    public static BigFraction[][] faulhaberCoefs(int n) {
	    BigFraction[][] f = new BigFraction[n][];
		f[0] = new BigFraction[1];
		f[0][0] = new BigFraction(1,1);
		for (int r = 1; r < n; r++) {
			f[r] = new BigFraction[r + 1];
			BigFraction sum = new BigFraction(0, 1);
			for (int c = 1; c <= r; c++) {
				f[r][c] = (new BigFraction(r, c + 1)).multiply(f[r - 1][c - 1]);
				sum = sum.add(f[r][c]);
			}
			f[r][0] = (new BigFraction(1,1)).subtract(sum);
		}
		return f;
    }

    public static String toStringFaulhaberCoef(int pow, BigFraction[][] coefs) {
		StringBuilder res = new StringBuilder();
		res.append("sum_{i=1}^n i^"+ pow + " = ");
		int i = pow;
		for(int j = 0; j <= i; j++) {
			if (! coefs[i][j].getNumerator().equals(BigInteger.ZERO)) {
				res.append(coefs[i][j] + " * n^" + (j+1));
				if (j < i) {
					res.append(" + ");
				}
			}
		}
		return res.toString();
	}
    
    public static long sumPowers_mod_faulhabert(int pow, long n, long mod,
    		BigFraction[][] cachedFaulhaberCoefs) {
    	return sumPowers_mod_faulhabert(pow, n, mod, cachedFaulhaberCoefs[pow]);
    }

    public static long sumPowers_mod_faulhabert(int pow, long n, long mod,
    		BigFraction[] cachedFaulhaberCoefs) {
    	long resNum = 0;
    	long resDenom = 1;
    	long curr_n_pow = n; // = n^(i+1) % mod
    	for(int i = 0; i <= pow; i++, curr_n_pow = (curr_n_pow * n) % mod) {
    		long coefNum = cachedFaulhaberCoefs[i].getNumeratorAsLong() % mod;
    		long coefDenom = cachedFaulhaberCoefs[i].getDenominatorAsLong();
    		long sumEltNum = (curr_n_pow * coefNum) % mod;
    		long gcd = LongArithUtil.gcd(coefDenom, resDenom);
    		long coefDenom_gcd = coefDenom / gcd;
    		long resDenom_gcd = resDenom / gcd;
    		resNum = (resNum * coefDenom_gcd + sumEltNum * resDenom_gcd) % mod;
    		resDenom = resDenom * coefDenom_gcd;
    	}
    	long res = resNum / resDenom;
		return res % mod;
    }
 
}
