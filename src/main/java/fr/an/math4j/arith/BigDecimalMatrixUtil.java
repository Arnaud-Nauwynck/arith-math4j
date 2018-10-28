package fr.an.math4j.arith;

import java.math.BigDecimal;
import java.math.MathContext;


public class BigDecimalMatrixUtil {

	/**
	 *
	 */
	public static class BigDecimalMatrix22 {
		BigDecimal[][] a;
		
		public BigDecimalMatrix22(MathContext mc) {
			BigDecimal z = new BigDecimal(0.0, mc);
			this.a = new BigDecimal[][] {
				new BigDecimal[] { z, z }, 
				new BigDecimal[] { z, z }
			};
		}

		public BigDecimalMatrix22(BigDecimal a00, BigDecimal a01, BigDecimal a10, BigDecimal a11) {
			this.a = new BigDecimal[][] {
				new BigDecimal[] { a00, a01 }, 
				new BigDecimal[] { a10, a11 }
			};
//			a[0][0] = a00; 
//			a[0][1] = a01; 
//			a[1][0] = a10; 
//			a[1][1] = a11; 
		}
		
		public BigDecimalMatrix22 mult(BigDecimalMatrix22 p, MathContext mc) {
			BigDecimalMatrix22 res = new BigDecimalMatrix22(mc);
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < 2; j++) {
					// sum_k ...  res.a[i][k].multiply(other.a[k][j]);
					res.a[i][j]= a[i][0].multiply(p.a[0][j], mc)
							.add(a[i][1].multiply(p.a[1][j], mc), mc); 
				}
			}
			return res;
		}
		
		public BigDecimalVector2 mult(BigDecimalVector2 p, MathContext mc) {
			BigDecimalVector2 res = new BigDecimalVector2(mc);
			for (int i = 0; i < 2; i++) {
				res.a[i] = a[i][0].multiply(p.a[0], mc)
						.add(a[i][1].multiply(p.a[1], mc), mc); 
			}
			return res;
		}
		

		public BigDecimalMatrix22 pow(int exp, MathContext mc) {
			BigDecimalMatrix22 res = this;
			exp--;
			BigDecimalMatrix22 base = this;
	        while (exp != 0) {
	            if ((exp & 1) != 0) {
	                res = res.mult(base, mc);
	            }
	            exp >>= 1;
				base = base.mult(base, mc);
	        }
	        return res;
	    }
		
	}

	
	/**
	 *
	 */
	public static class BigDecimalSymMatrix22 extends BigDecimalMatrix22 {
		
		public BigDecimalSymMatrix22() {
			super(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
		}
		
		public BigDecimalSymMatrix22(BigDecimal a00, BigDecimal a01, BigDecimal a11) {
			super(a00, a01, a01, a11);
		}

		public BigDecimalSymMatrix22 multSym(BigDecimalSymMatrix22 p, MathContext mc) {
			BigDecimalSymMatrix22 res = new BigDecimalSymMatrix22();
			for (int i = 0; i < 2; i++) {
				for (int j = i; j < 2; j++) {
					// sum_k ...  res.a[i][k].multiply(other.a[k][j]);
					res.a[i][j] = a[i][0].multiply(p.a[0][j], mc)
							.add(a[i][1].multiply(p.a[1][j], mc), mc);
					res.a[j][i] = res.a[i][j]; 
				}
			}
			return res;
		}
		
		public BigDecimalSymMatrix22 pow(long exp, MathContext mc) {
			BigDecimalSymMatrix22 res = this;
			exp--;
			BigDecimalSymMatrix22 base = this;
	        while (exp != 0) {
	            if ((exp & 1) != 0) {
	                res = res.multSym(base, mc);
	            }
	            exp >>= 1;
				base = base.multSym(base, mc);
	        }

	        return res;
	    }
		
	}
	
	
	/**
	 *
	 */
	public static class BigDecimalVector2 {
		BigDecimal[] a; // = new BigDecimal[2];

		public BigDecimalVector2(MathContext mc) {
			BigDecimal z = new BigDecimal(0.0, mc);
			this.a = new BigDecimal[] { z, z };
		}

		public BigDecimalVector2(BigDecimal a0, BigDecimal a1) {
			this.a = new BigDecimal[] { a0, a1 };
		}
	}
}
