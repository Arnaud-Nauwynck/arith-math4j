package fr.an.math4j.arith;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

public class BigIntDivisors implements Iterable<BigInteger> {

	private NumberAndExponent[] decomp;

	// ------------------------------------------------------------------------

	public BigIntDivisors(long value) {
		this(NumberPrimeDecomp.decompInPrimeFactors(value).getDecomp());
	}
	
	public BigIntDivisors(NumberAndExponent[] decomp) {
		this.decomp = decomp;
	}

	// ------------------------------------------------------------------------

	@Override
	public Iterator<BigInteger> iterator() {
		return new DivisorsIterator(decomp);
	}
	
	public List<BigInteger> toList() {
		List<BigInteger> res = new ArrayList<>();
		for(BigInteger div : this) {
			res.add(div);
		}
		return res;
	}
	

	public List<BigInteger> toSortedList() {
		TreeSet<BigInteger> sorted = new TreeSet<>();
		for(BigInteger div : this) {
			sorted.add(div);
		}
		return new ArrayList<>(sorted);
	}

	
	
	public static class DivisorsIterator implements Iterator<BigInteger> {
		
		private NumberAndExponent[] decomp;
		
		private int[] indices;
		
		private BigInteger curr;
		private BigInteger[] currValues;
		
		private boolean _hasNext = true;
		
		// ------------------------------------------------------------------------

		public DivisorsIterator(long value) {
			this(NumberPrimeDecomp.decompInPrimeFactors(value).getDecomp());
		}
		
		public DivisorsIterator(NumberAndExponent[] decomp) {
			this.decomp = decomp;
			this.indices = new int[decomp.length];
			this.currValues = new BigInteger[decomp.length];
		}

		// ------------------------------------------------------------------------

		@Override
		public boolean hasNext() {
			for (int i = 0; i < decomp.length; i++) {
		        if (indices[i] == decomp[i].exponent) {
		        	indices[i] = 0;
		        	currValues[i] = (i==0)? BigInteger.ONE : currValues[i-1];
		            if (i == decomp.length-1) {
		                _hasNext = false;
		            }
		        } else {
		            indices[i]++;
		            // currValues[i] = currValues[i].multiply(BigInteger.valueOf(decomp[i].number)); 
		            break;
		        }
		    }
		    
		    BigInteger curr = BigInteger.ONE;
		    for (int i = 0; i < decomp.length; i++) {
		    	if (indices[i] != 0) {
		    		curr = curr.multiply(BigInteger.valueOf(decomp[i].number).pow(indices[i]));
		    	}
		    }
		    this.curr = curr;
		    
			return _hasNext;
		}

		@Override
		public BigInteger next() {
		    return curr;
		}
		
	}

	
}
