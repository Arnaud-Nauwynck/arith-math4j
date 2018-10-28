package fr.an.math4j.arith;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * immutable BigInteger fraction
 */
public class BigIntegerFraction {
	
	public static final BigIntegerFraction ZERO = new BigIntegerFraction(0, 1); 
	public static final BigIntegerFraction ONE = new BigIntegerFraction(1, 1); 
	public static final BigIntegerFraction TWO = new BigIntegerFraction(2, 1); 
	
	private final BigInteger num;
	
	private final BigInteger den;
	
	// ------------------------------------------------------------------------
	
	public BigIntegerFraction(BigInteger num, BigInteger den) {
		super();
		this.num = num;
		this.den = den;
	}

	public BigIntegerFraction(long num, long den) {
		super();
		this.num = BigInteger.valueOf(num);
		this.den = BigInteger.valueOf(den);
	}

	public BigIntegerFraction(BigIntegerFractionMutable src) {
		this.num = src.num;
		this.den = src.den;
	}

	public static BigIntegerFraction snewSimplify(BigInteger num, BigInteger den) {
		BigInteger gcd = num.gcd(den);
		if (!gcd.equals(BigInteger.ONE)) {
			num = num.divide(gcd);
			den = den.divide(gcd);
		}
		return new BigIntegerFraction(num, den);
	}
	
	// ------------------------------------------------------------------------

	public BigInteger getNum() {
		return num;
	}

	public BigInteger getDen() {
		return den;
	}
	
	public BigIntegerFraction simplifyByGcd() {
		BigIntegerFraction res;
		BigInteger gcd = num.gcd(den);
		if (!gcd.equals(BigInteger.ONE)) {
			res = new BigIntegerFraction(num.divide(gcd), den.divide(gcd));
		} else {
			res = this;
		}
		return res;
	}

	public BigIntegerFraction negate() {
		return new BigIntegerFraction(num.negate(), den);
	}

	public BigIntegerFraction add(BigIntegerFraction other) {
		return addFrac(other.getNum(), other.getDen());
	}

	public BigIntegerFraction subtract(BigIntegerFraction other) {
		return subtractFrac(other.getNum(), other.getDen());
	}

	public BigIntegerFraction mult(BigIntegerFraction other) {
		return multFrac(other.getNum(), other.getDen());
	}

	public BigIntegerFraction mult(long other) {
		return mult(BigInteger.valueOf(other));
	}

	public BigIntegerFraction mult(BigInteger other) {
		BigInteger newNum = num.multiply(other);
		return snewSimplify(newNum, den);
	}

	public BigIntegerFraction pow(int n) {
		BigInteger newNum = num.pow(n);
		BigInteger newDen = den.pow(n);
		return new BigIntegerFraction(newNum, newDen);
	}
	
	
	public BigIntegerFraction multFrac(long otherNum, long otherDen) {
		return multFrac(BigInteger.valueOf(otherNum), BigInteger.valueOf(otherDen));
	}

	public BigIntegerFraction multFrac(BigInteger otherNum, BigInteger otherDen) {
		BigInteger newNum = num.multiply(otherNum);
		BigInteger newDen = den.multiply(otherDen);
		return snewSimplify(newNum, newDen);	
	}

	public BigIntegerFraction addFrac(long otherNum, long otherDen) {
		return addFrac(BigInteger.valueOf(otherNum), BigInteger.valueOf(otherDen));
	}

	public BigIntegerFraction addFrac(BigInteger otherNum, BigInteger otherDen) {
		BigInteger newNum = num.multiply(otherDen).add(den.multiply(otherNum));
		BigInteger newDen = otherDen.multiply(den);
		return snewSimplify(newNum, newDen);	
	}

	public BigIntegerFraction subtractFrac(long otherNum, long otherDen) {
		return subtractFrac(BigInteger.valueOf(otherNum), BigInteger.valueOf(otherDen));
	}
	
	public BigIntegerFraction subtractFrac(BigInteger otherNum, BigInteger otherDen) {
		BigInteger newNum = num.multiply(otherDen).subtract(den.multiply(otherNum));
		BigInteger newDen = otherDen.multiply(den);
		return snewSimplify(newNum, newDen);	
	}

	public BigFractionIntPart toIntFractPart() {
		BigInteger[] divrem = num.divideAndRemainder(den);
		return new BigFractionIntPart(divrem[0], new BigIntegerFraction(divrem[1], den));
	}
	
	public String toStringIntRoundFractPart(int precision) {
		BigFractionIntPart intFractPart = toIntFractPart();
		return intFractPart.toStringRound(precision);
	}
	
	public String toString() {
		return num + " / " + den;
	}
	
	// ------------------------------------------------------------------------
	

	public static class BigFractionIntPart { 
		public BigInteger intPart;
		public BigIntegerFraction fractPart;
		
		public BigFractionIntPart(BigInteger intPart, BigIntegerFraction fractPart) {
			this.intPart = intPart;
			this.fractPart = fractPart;
		}
		
		public String toString() {
			return intPart + " + " + fractPart;
		}
		
		public String toStringRound(int precision) {
			String res = "";
	    	BigInteger fracNum = fractPart.getNum();
			BigInteger fracDen = fractPart.getDen();
			if (!intPart.equals(BigInteger.ZERO)) {
				res += intPart;
			}
			if (!fracNum.equals(BigInteger.ZERO)) {
				if (!res.equals("")) {
					res += "+";
				}
				// MathContext precisionMC = new MathContext(precision);
				BigDecimal fracPartRound = new BigDecimal(fracNum)
		    		.divide(new BigDecimal(fracDen),
		    				precision, RoundingMode.FLOOR);
		    	res += fracPartRound;
			} else if (res.equals("")) {
				res += "0";
			}
	    	return res;
		}
		
	}
	
	// ------------------------------------------------------------------------
	
	/**
	 * Mutable class..
	 */
	public static class BigIntegerFractionMutable {
		private BigInteger num;
		private BigInteger den;

		public BigIntegerFractionMutable() {
			this(BigInteger.ZERO, BigInteger.ONE);
		}

		public BigIntegerFractionMutable(BigInteger num, BigInteger den) {
			this.num = num;
			this.den = den;
		}

		public BigIntegerFractionMutable(BigIntegerFraction src) {
			this.num = src.num;
			this.den = src.den;
		}
		
		public BigIntegerFraction toBigIntegerFraction() {
			return new BigIntegerFraction(num, den);
		}
		
		public BigInteger getNum() {
			return num;
		}

		public void setNum(BigInteger num) {
			this.num = num;
		}

		public BigInteger getDen() {
			return den;
		}

		public void setDen(BigInteger den) {
			this.den = den;
		}

		public void set(BigInteger num, BigInteger den) {
			this.num = num;
			this.den = den;
		}
		
		public void setSimplify(BigInteger num, BigInteger den) {
			this.num = num;
			this.den = den;
			simplifyByGcd();
		}
		
		public BigInteger simplifyByGcd() {
			BigInteger gcd = num.gcd(den);
			if (!gcd.equals(BigInteger.ONE)) {
				num = num.divide(gcd);
				den= den.divide(gcd);
			}
			return gcd;
		}

		public void setAdd(BigIntegerFraction p) {
			setAddFrac(p.getNum(), p.getDen());
		}

		public void setAdd(BigIntegerFractionMutable p) {
			setAddFrac(p.getNum(), p.getDen());
		}

		public void setAddFrac(BigInteger otherNum, BigInteger otherDen) {
			BigInteger tmpnum = num.multiply(otherDen).add(den.multiply(otherNum));
			BigInteger tmpden = otherDen.multiply(den);
			setSimplify(tmpnum, tmpden);
		}

		public void setMult(BigIntegerFraction p) {
			setAddFrac(p.getNum(), p.getDen());
		}

		public void setMult(BigIntegerFractionMutable p) {
			setAddFrac(p.getNum(), p.getDen());
		}
		
		public void setMultFrac(BigInteger otherNum, BigInteger otherDen) {
			BigInteger tmpnum = num.multiply(otherNum);
			BigInteger tmpden = den.multiply(otherDen);
			setSimplify(tmpnum, tmpden);
		}

		public String toString() {
			return num + " / " + den;
		}

	}
}
