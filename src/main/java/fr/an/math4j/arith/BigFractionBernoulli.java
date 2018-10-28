package fr.an.math4j.arith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.fraction.BigFraction;

/**
 * Bernoulli numbers.
 * 
 * from https://github.com/deeplearning4j/nd4j/blob/master/nd4j-common/src/main/java/org/nd4j/linalg/util/Bernoulli.java
 * 
 * cf precomputations...
 * 
 * generated from sage:
 * <PRE>
 * sage -c 'for i in range(1,2500): print(bernoulli(2*i))' > bernoulli.txt
 * </PRE> 
 * 
 */
public final class BigFractionBernoulli {
	
    /*
     * cached even Bernoulli numbers as n=0,2,4,....
     */
    private static List<BigFraction> a = new ArrayList<BigFraction>();

    static {
        a.add(BigFraction.ONE);
        a.add(new BigFraction(1, 6));
    }

    public static List<BigFraction> bernoullis2nUpTo(int n) {
    	List<BigFraction> res = new ArrayList<>(n);
    	URL resource = BigFractionBernoulli.class.getResource("bernoulli.txt");
    	BufferedReader reader = null;
    	try {
    		reader = new BufferedReader(new InputStreamReader(resource.openStream()));
    		for(int i = 1; i <= n; i++) {
    			String line = reader.readLine();
    			String[] numDen = line.split("/");
    			res.add(new BigFraction(new BigInteger(numDen[0]), new BigInteger(numDen[1])));
    		}
    	} catch(IOException ex) {
    		throw new RuntimeException("Failed to read", ex);
    	}
    	return res;
    }
    
    /**
     * Set a coefficient in the internal table.
     *
     * @param n     the zero-based index of the coefficient. n=0 for the constant term.
     * @param value the new value of the coefficient.
     */
    protected static void set(final int n, final BigFraction value) {
        final int nindx = n / 2;
        if (nindx < a.size()) {
            a.set(nindx, value);
        } else {
            while (a.size() < nindx) {
                a.add(BigFraction.ZERO);
            }
            a.add(value);
        }
    }

    /**
     * The Bernoulli number at the index provided.
     *
     * @param n the index, non-negative.
     * @return the B_0=1 for n=0, B_1=-1/2 for n=1, B_2=1/6 for n=2 etc
     */
    public static BigFraction at(int n) {
        if (n == 1) {
            return (new BigFraction(-1, 2));
        } else if (n % 2 != 0) {
            return BigFraction.ZERO;
        } else {
            final int nindx = n / 2;
            if (a.size() <= nindx) {
                for (int i = 2 * a.size(); i <= n; i += 2) {
                    set(i, bernoulliNum_ByDoubleSum(i));
                }
            }
            return a.get(nindx);
        }
    }
    
    /* Generate a new B_n by a standard double sum.
     * @param n The index of the Bernoulli number.
     * @return The Bernoulli number at n.
     */
    private static BigFraction bernoulliNum_ByDoubleSum(int n) {
        BigFraction resul = BigFraction.ZERO;
        for (int k = 0; k <= n; k++) {
            BigFraction jsum = BigFraction.ZERO;
            BigInteger bin = BigInteger.ONE;
            for (int j = 0; j <= k; j++) {
                BigInteger jpown = BigInteger.valueOf(j).pow(n);
                if (j % 2 == 0) {
                    jsum = jsum.add(bin.multiply(jpown));
                } else {
                    jsum = jsum.subtract(bin.multiply(jpown));
                }
                /* update binomial(k,j) recursively
                 */
                bin = bin.multiply(BigInteger.valueOf(k - j)).divide(BigInteger.valueOf(j + 1));
            }
            resul = resul.add(jsum.divide(BigInteger.valueOf(k + 1)));
        }
        return resul;
    }
    	
}