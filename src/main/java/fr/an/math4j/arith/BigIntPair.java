package fr.an.math4j.arith;

import java.math.BigInteger;

public class BigIntPair {

	public BigInteger a;
	public BigInteger b;

	public BigIntPair(int a, int b) {
		this.a = BigInteger.valueOf(a);
		this.b = BigInteger.valueOf(b);
	}

	public BigIntPair(BigInteger a, BigInteger b) {
		this.a = a;
		this.b = b;
	}

	@Override
	public String toString() {
		return "[" + a + ", " + b + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a == null) ? 0 : a.hashCode());
		result = prime * result + ((b == null) ? 0 : b.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BigIntPair other = (BigIntPair) obj;
		if (a == null) {
			if (other.a != null)
				return false;
		} else if (!a.equals(other.a))
			return false;
		if (b == null) {
			if (other.b != null)
				return false;
		} else if (!b.equals(other.b))
			return false;
		return true;
	}
	
	
}
