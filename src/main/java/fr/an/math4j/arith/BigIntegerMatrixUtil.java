package fr.an.math4j.arith;

import java.math.BigInteger;


public class BigIntegerMatrixUtil {

	/**
	 *
	 */
	public static class BigIntegerMatrix22 {
		BigInteger[][] a;
		
		public BigIntegerMatrix22() {
			this.a = new BigInteger[][] {
				new BigInteger[] { BigInteger.ZERO, BigInteger.ZERO}, 
				new BigInteger[] { BigInteger.ZERO, BigInteger.ZERO}
			};
		}

		public BigIntegerMatrix22(BigInteger a00, BigInteger a01, BigInteger a10, BigInteger a11) {
			this.a = new BigInteger[][] {
				new BigInteger[] { a00, a01 }, 
				new BigInteger[] { a10, a11 }
			};
//			a[0][0] = a00; 
//			a[0][1] = a01; 
//			a[1][0] = a10; 
//			a[1][1] = a11; 
		}
		
		public BigIntegerMatrix22 mult(BigIntegerMatrix22 p) {
			BigIntegerMatrix22 res = new BigIntegerMatrix22();
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < 2; j++) {
					// sum_k ...  res.a[i][k].multiply(other.a[k][j]);
					res.a[i][j]= a[i][0].multiply(p.a[0][j])
							.add(a[i][1].multiply(p.a[1][j])); 
				}
			}
			return res;
		}
		
		public BigIntegerVector2 mult(BigIntegerVector2 p) {
			BigIntegerVector2 res = new BigIntegerVector2();
			for (int i = 0; i < 2; i++) {
				res.a[i] = a[i][0].multiply(p.a[0])
						.add(a[i][1].multiply(p.a[1])); 
			}
			return res;
		}
		

		public BigIntegerMatrix22 pow(int exp) {
			BigIntegerMatrix22 res = new BigIntegerMatrix22();
			BigIntegerMatrix22 base = this;
	        while (exp != 0) {
	            if ((exp & 1) != 0) {
	                res = res.mult(base);
	            }
	            exp >>= 1;
				base = base.mult(base);
	        }
	        return res;
	    }
		
	}

	
	/**
	 *
	 */
	public static class BigIntegerSymMatrix22 extends BigIntegerMatrix22 {
		
		public BigIntegerSymMatrix22() {
			super(BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO);
		}
		
		public BigIntegerSymMatrix22(BigInteger a00, BigInteger a01, BigInteger a11) {
			super(a00, a01, a01, a11);
		}

		public BigIntegerSymMatrix22 multSym(BigIntegerSymMatrix22 p) {
			BigIntegerSymMatrix22 res = new BigIntegerSymMatrix22();
			for (int i = 0; i < 2; i++) {
				for (int j = i; j < 2; j++) {
					// sum_k ...  res.a[i][k].multiply(other.a[k][j]);
					res.a[i][j] = a[i][0].multiply(p.a[0][j])
							.add(a[i][1].multiply(p.a[1][j]));
					res.a[j][i] = res.a[i][j]; 
				}
			}
			return res;
		}
		
		public BigIntegerSymMatrix22 pow(int exp) {
			BigIntegerSymMatrix22 res = this;
			exp--;
			BigIntegerSymMatrix22 base = this;
	        while (exp != 0) {
	            if ((exp & 1) != 0) {
	                res = res.multSym(base);
	            }
	            exp >>= 1;
				base = base.multSym(base);
	        }

	        return res;
	    }
		
	}
	
	
	
	/**
	 *
	 */
	public static class BigIntegerSymModMatrix22 extends BigIntegerMatrix22 {
		
		private BigInteger modulo;
		
		public BigIntegerSymModMatrix22(BigInteger modulo) {
			super(BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO);
			this.modulo = modulo;
		}
		
		public BigIntegerSymModMatrix22(BigInteger modulo, BigInteger a00, BigInteger a01, BigInteger a11) {
			super(a00, a01, a01, a11);
			this.modulo = modulo;
		}

		public BigIntegerSymModMatrix22(BigInteger modulo, BigIntegerSymMatrix22 a) {
			this(modulo, a.a[0][0].mod(modulo), a.a[0][1].mod(modulo), a.a[1][1].mod(modulo));
		}

		public BigIntegerSymModMatrix22 multSym(BigIntegerSymModMatrix22 p, boolean immediateApplyModulo) {
			BigIntegerSymModMatrix22 res = new BigIntegerSymModMatrix22(modulo);
			for (int i = 0; i < 2; i++) {
				for (int j = i; j < 2; j++) {
					// sum_k ...  res.a[i][k].multiply(other.a[k][j]);
					res.a[i][j] = a[i][0].multiply(p.a[0][j])
							.add(a[i][1].multiply(p.a[1][j]));
					if (immediateApplyModulo) {
						res.a[i][j] = res.a[i][j].mod(modulo);
					}
					res.a[j][i] = res.a[i][j]; 
				}
			}
			return res;
		}
		
		public BigIntegerSymModMatrix22 pow(long exp) {
			BigIntegerSymModMatrix22 res = this;
			exp--;
			BigIntegerSymModMatrix22 base = this;
	        while (exp != 0) {
	            if ((exp & 1) != 0) {
	                res = res.multSym(base, true);
	            }
	            exp >>= 1;
				base = base.multSym(base, true);
	        }
	        return res;
	    }
		
		public BigIntegerSymModMatrix22 pow(long exp, BigIntegerSymModMatrix22[] precomputed2Pows) {
			BigIntegerSymModMatrix22 res = this;
			exp--;
			int expIndex = 1;
	        while (exp != 0) {
	            if ((exp & 1) != 0) {
	            	if (expIndex >= precomputed2Pows.length) {
	            		break; // not enough precomputed elts fallback to base squaring algo...
	            	}
	            	BigIntegerSymModMatrix22 ae = precomputed2Pows[expIndex];
	                res = res.multSym(ae, true);
	            }
	            exp >>= 1;
				expIndex++;
	        }
	        if (exp != 0) {
	        	BigIntegerSymModMatrix22 base = (expIndex > 0)? precomputed2Pows[precomputed2Pows.length-1] : this;
	        	base = base.multSym(base, true);
	        	while (exp != 0) {
		            if ((exp & 1) != 0) {
		                res = res.multSym(base, true);
		            }
		            exp >>= 1;
					expIndex++;
					base = base.multSym(base, true);
		        }
	        }
	        
	        return res;
	    }
		
		public BigIntegerVector2 mult(BigIntegerVector2 p) {
			BigIntegerVector2 res = new BigIntegerVector2();
			for (int i = 0; i < 2; i++) {
				res.a[i] = a[i][0].multiply(p.a[0])
						.add(a[i][1].multiply(p.a[1])).mod(modulo);
			}
			return res;
		}

		@Override
		public String toString() {
			return "[ [" + a[0][0] + " " + a[0][1] + "][" + a[1][0] + " " + a[1][1] + "] ]";
		}
		
		
	}
	
	
	/**
	 *
	 */
	public static class BigIntegerVector2 {
		BigInteger[] a = new BigInteger[2];
		
	}
}
