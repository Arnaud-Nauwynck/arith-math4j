package fr.an.math4j.arith;

import java.util.ArrayList;
import java.util.List;

/**
 * Bernoulli numbers.
 * 
 * from https://github.com/deeplearning4j/nd4j/blob/master/nd4j-common/src/main/java/org/nd4j/linalg/util/Bernoulli.java
 */
public final class LongBernoulli {
	
    /*
     * The list of all Bernoulli numbers as a vector, n=0,2,4,....
     */
    private static List<LongFraction> a = new ArrayList<LongFraction>();

    static {
        a.add(LongFraction.ONE);
        a.add(new LongFraction(1, 6));
    }

    /**
     * Set a coefficient in the internal table.
     *
     * @param n     the zero-based index of the coefficient. n=0 for the constant term.
     * @param value the new value of the coefficient.
     */
    protected static void set(final int n, final LongFraction value) {
        final int nindx = n / 2;
        if (nindx < a.size()) {
            a.set(nindx, value);
        } else {
            while (a.size() < nindx) {
                a.add(LongFraction.ZERO);
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
    public static LongFraction at(int n) {
        if (n == 1) {
            return (new LongFraction(-1, 2));
        } else if (n % 2 != 0) {
            return LongFraction.ZERO;
        } else {
            final int nindx = n / 2;
            if (a.size() <= nindx) {
                for (int i = 2 * a.size(); i <= n; i += 2) {
                    set(i, doubleSum(i));
                }
            }
            return a.get(nindx);
        }
    }
    /* Generate a new B_n by a standard double sum.
     * @param n The index of the Bernoulli number.
     * @return The Bernoulli number at n.
     */
    private static LongFraction doubleSum(int n) {
    	throw new RuntimeException("NOT IMPL");
//        LongFraction resul = LongFraction.ZERO;
//        for (int k = 0; k <= n; k++) {
//        	LongFraction jsum = LongFraction.ZERO;
//            BigInteger bin = BigInteger.ONE;
//            for (int j = 0; j <= k; j++) {
//                BigInteger jpown = BigInteger.valueOf(j).pow(n);
//                if (j % 2 == 0) {
//                    jsum = jsum.add(bin.multiply(jpown));
//                } else {
//                    jsum = jsum.subtract(bin.multiply(jpown));
//                }
//                /* update binomial(k,j) recursively
//                 */
//                bin = bin.multiply(BigInteger.valueOf(k - j)).divide(BigInteger.valueOf(j + 1));
//            }
//            resul = resul.add(jsum.divide(BigInteger.valueOf(k + 1)));
//        }
//        return resul;
    }
    
}