package fr.an.math4j.arith;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

public class LongDivisors implements Iterable<Long> {

	private NumberAndExponent[] decomp;

	// ------------------------------------------------------------------------

	public LongDivisors(long value) {
		this(NumberPrimeDecomp.decompInPrimeFactors(value).getDecomp());
	}
	
	public LongDivisors(NumberAndExponent[] decomp) {
		this.decomp = decomp;
	}

	// ------------------------------------------------------------------------

	@Override
	public Iterator<Long> iterator() {
		return new DivisorsIterator(decomp);
	}
	
	public List<Long> toList() {
		List<Long> res = new ArrayList<>();
		for(Long div : this) {
			res.add(div);
		}
		return res;
	}

	public List<Long> toSortedList() {
		TreeSet<Long> sorted = new TreeSet<>();
		for(Long div : this) {
			sorted.add(div);
		}
		return new ArrayList<>(sorted);
	}

	
	public static class DivisorsIterator implements Iterator<Long> {
		
		private NumberAndExponent[] decomp;
		
		private int[] indices;
		
		private long curr;
		private long[] currValues;
		
		private boolean _hasNext = true;
		
		// ------------------------------------------------------------------------

		public DivisorsIterator(long value) {
			this(NumberPrimeDecomp.decompInPrimeFactors(value).getDecomp());
		}
		
		public DivisorsIterator(NumberAndExponent[] decomp) {
			this.decomp = decomp;
			this.indices = new int[decomp.length];
			this.currValues = new long[decomp.length];
		}

		// ------------------------------------------------------------------------

		@Override
		public boolean hasNext() {
			for (int i = 0; i < decomp.length; i++) {
		        if (indices[i] == decomp[i].exponent) {
		        	indices[i] = 0;
		        	currValues[i] = (i==0)? 1 : currValues[i-1];
		            if (i == decomp.length-1) {
		                _hasNext = false;
		            }
		        } else {
		            indices[i]++;
		            // currValues[i] = currValues[i].multiply(BigInteger.valueOf(decomp[i].number)); 
		            break;
		        }
		    }
		    
		    long curr = 1;
		    for (int i = 0; i < decomp.length; i++) {
		    	if (indices[i] != 0) {
		    		curr *= LongArithUtil.pow(decomp[i].number, indices[i]);
		    	}
		    }
		    this.curr = curr;
		    
			return _hasNext;
		}

		@Override
		public Long next() {
		    return curr;
		}
		
	}

	
}
