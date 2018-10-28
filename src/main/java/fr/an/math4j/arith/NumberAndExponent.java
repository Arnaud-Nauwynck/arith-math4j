package fr.an.math4j.arith;

import java.util.Comparator;
import java.util.List;

public class NumberAndExponent {

	public final long number;
	public int exponent;

	public long numberPowerExponent;

	// ------------------------------------------------------------------------
	
	public NumberAndExponent(long number, int exponent) {
		super();
		this.number = number;
		this.exponent = exponent;
		this.numberPowerExponent = 0; // computed lazily.... powerLong(number, exponent);
	}

	public long getNumber() {
		return number;
	}

	public int getExponent() {
		return exponent;
	}

	public void incrExponent() {
		this.exponent++;
		if (numberPowerExponent != 0) this.numberPowerExponent *= number;
	}

	public void incrExponent(int pow, long numberPow) {
		this.exponent += pow;
		if (numberPowerExponent != 0) this.numberPowerExponent *= numberPow;
	}

	public long getNumberPowerExponent() {
		if (numberPowerExponent == 0) {
			numberPowerExponent = powerLong(number, exponent);
		}
		return numberPowerExponent;
	}

	
	@Override
	public String toString() {
		return "" + number 
			+ ((exponent != 1)? "^" + exponent : ""); 
	}

	public static String toString(List<NumberAndExponent> numberDecomposition) {
		StringBuilder sb = new StringBuilder();
		for(NumberAndExponent ne : numberDecomposition) {
			sb.append(ne);
			sb.append(" * ");
		}
		if (numberDecomposition.size() >= 1) {
			sb.delete(sb.length() - 3, sb.length());
		}
		return sb.toString();
	}

	public static String toString(NumberAndExponent[] numberDecomposition) {
		StringBuilder sb = new StringBuilder();
		for(NumberAndExponent ne : numberDecomposition) {
			sb.append(ne);
			sb.append(" * ");
		}
		if (numberDecomposition.length >= 1) {
			sb.delete(sb.length() - 3, sb.length());
		}
		return sb.toString();
	}

	public static long powerLong(long number, int exponent) {
		long res = 1;
		for (int i = 0; i < exponent; i++) {
			res *= number;
		}
		return res;
	}

	/** return a^b mod n */
    public static int modexp(int a, int b, int n) {
        if (b == 0) return 1;
        long t = modexp(a, b/2, n);
        long c = (t * t) % n;
        if (b % 2 == 1)
           c = (c * a) % n;
        return (int) c;
    }
	
    public static long ipow(int base, int exp)
    {
    	long result = 1;
        while (exp != 0)
        {
            if ((exp & 1) != 0) {
                result *= base;
            }
            exp >>= 1;
            base *= base;
        }

        return result;
    }

    
	
	public static NumberAndExponent findDecompElt(List<NumberAndExponent> decomp, int prime) {
		NumberAndExponent res = null;
		for(NumberAndExponent elt :  decomp) {
			if (elt.getNumber() == prime) {
				res = elt;
				break;
			}
		}
		return res;
	}
	
	/**
	 * comparator by <code>NumberAndExponent.getNumber()</code>
	 */
	public static class NumberPartComparator implements Comparator<NumberAndExponent> {

		public static final NumberPartComparator INSTANCE = new NumberPartComparator();
		private NumberPartComparator() {
		}
		
		public int compare(NumberAndExponent o1, NumberAndExponent o2) {
			long number1 = o1.getNumber();
			long number2 = o2.getNumber();
			if (number1 == number2) return 0;
			else return (number1 < number2)? -1 : +1;
		}
		
	}
}
