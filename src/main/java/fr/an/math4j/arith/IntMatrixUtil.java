package fr.an.math4j.arith;



public class IntMatrixUtil {

	/**
	 *
	 */
	public static class IntMatrix22 {
		int a00, a01, a10, a11;
		
		public IntMatrix22() {
		}

		public IntMatrix22(int a00, int a01, int a10, int a11) {
			this.a00 = a00; 
			this.a01 = a01; 
			this.a10 = a10; 
			this.a11 = a11; 
		}
		
		public IntMatrix22 mult(IntMatrix22 p) {
			IntMatrix22 res = new IntMatrix22();
			res.a00 = a00 * p.a00 + a01 * p.a10;
			res.a01 = a00 * p.a01 + a01 * p.a11;
			res.a10 = a10 * p.a00 + a11 * p.a10;
			res.a11 = a10 * p.a01 + a11 * p.a11;
			return res;
		}
		
		public IntVector2 mult(IntVector2 p) {
			IntVector2 res = new IntVector2();
			res.a0 = a00 * p.a0 + a01 * p.a1; 
			res.a1 = a10 * p.a0 + a11 * p.a1; 
			return res;
		}
		

		public IntMatrix22 pow(int exp) {
			IntMatrix22 res = this;
			exp--;
			IntMatrix22 base = this;
	        while (exp != 0) {
	            if ((exp & 1) != 0) {
	                res = res.mult(base);
	            }
	            exp >>= 1;
				base = base.mult(base);
	        }
	        return res;
	    }
		
		@Override
		public String toString() {
			return "[ [" + a00 + " " + a01 + "][" + a10 + " " + a11 + "] ]";
		}

	}

	
	/**
	 *
	 */
	public static class IntSymMatrix22 extends IntMatrix22 {
		
		public IntSymMatrix22() {
			super(0, 0, 0, 0);
		}
		
		public IntSymMatrix22(int a00, int a01, int a11) {
			super(a00, a01, a01, a11);
		}

		public IntSymMatrix22 multSym(IntSymMatrix22 p) {
			IntSymMatrix22 res = new IntSymMatrix22();
			res.a00 = a00 * p.a00 + a01 * p.a10;
			res.a01 = a00 * p.a01 + a01 * p.a11;
			res.a10 = res.a01; // override for sym
			res.a11 = a10 * p.a01 + a11 * p.a11;
			return res;
		}
		
		public IntSymMatrix22 pow(long exp) {
			IntSymMatrix22 res = this;
			exp--;
			IntSymMatrix22 base = this;
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
	public static class IntVector2 {
		int a0, a1;
		
	}
	
	

	/**
	 *
	 */
	public static class IntSymModMatrix22 extends IntSymMatrix22 {
		
		private int modulo;
		
		public IntSymModMatrix22(int modulo) {
			this.modulo = modulo;
		}
		
		public IntSymModMatrix22(int modulo, int a00, int a01, int a11) {
			super(a00, a01, a11);
			this.modulo = modulo;
		}

		public IntSymModMatrix22(int modulo, IntMatrix22 a) {
			this(modulo, a.a00 % modulo, a.a01 % modulo, a.a11 % modulo);
		}

		public IntSymModMatrix22 multSym(IntSymModMatrix22 p) {
			IntSymModMatrix22 res = new IntSymModMatrix22(modulo);

			long tmpa00 = (long)a00 * p.a00 + (long)a01 * p.a10;
			long tmpa01 = (long)a00 * p.a01 + (long)a01 * p.a11;
			long tmpa11 = (long)a10 * p.a01 + (long)a11 * p.a11;

			res.a00 = (int) (tmpa00 % modulo);
			res.a01 = (int) (tmpa01 % modulo);
			res.a10 = res.a01;
			res.a11 = (int) (tmpa11 % modulo);
			return res;
		}
		
		public IntSymModMatrix22 pow(long exp) {
			IntSymModMatrix22 res = this;
			exp--;
			IntSymModMatrix22 base = this;
	        while (exp != 0) {
	            if ((exp & 1) != 0) {
	                res = res.multSym(base);
	            }
	            exp >>= 1;
				base = base.multSym(base);
	        }
	        return res;
	    }
		
		public IntSymModMatrix22 pow(long exp, IntSymModMatrix22[] precomputed2Pows) {
			IntSymModMatrix22 res = this;
			exp--;
			int expIndex = 1;
	        while (exp != 0) {
	            if ((exp & 1) != 0) {
	            	if (expIndex >= precomputed2Pows.length) {
	            		break; // not enough precomputed elts fallback to base squaring algo...
	            	}
	            	IntSymModMatrix22 ae = precomputed2Pows[expIndex];
	                res = res.multSym(ae);
	            }
	            exp >>= 1;
				expIndex++;
	        }
	        if (exp != 0) {
	        	IntSymModMatrix22 base = (expIndex > 0)? precomputed2Pows[precomputed2Pows.length-1] : this;
	        	base = base.multSym(base);
	        	while (exp != 0) {
		            if ((exp & 1) != 0) {
		                res = res.multSym(base);
		            }
		            exp >>= 1;
					expIndex++;
					base = base.multSym(base);
		        }
	        }
	        
	        return res;
	    }
		
		public IntModVector2 mult(IntModVector2 p) {
			IntModVector2 res = new IntModVector2();
			long tmpa0 = (long)a00 * p.a0 + (long)a01 * p.a1; 
			long tmpa1 = (long)a10 * p.a0 + (long)a11 * p.a1; 
			res.a0 = (int)(tmpa0 % modulo);
			res.a1 = (int)(tmpa1 % modulo);
			return res;
		}
	}
	
	
	/**
	 *
	 */
	public static class IntModVector2 extends IntVector2 {
		
	}
	
}
