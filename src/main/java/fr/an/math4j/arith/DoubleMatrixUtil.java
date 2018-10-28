package fr.an.math4j.arith;


public class DoubleMatrixUtil {

	/**
	 *
	 */
	public static class DoubleMatrix22 {
		double[][] a;
		
		public DoubleMatrix22() {
			this.a = new double[][] {
				new double[] { 0.0, 0.0 }, 
				new double[] { 0.0, 0.0 }
			};
		}

		public DoubleMatrix22(double a00, double a01, double a10, double a11) {
			this.a = new double[][] {
				new double[] { a00, a01 }, 
				new double[] { a10, a11 }
			};
//			a[0][0] = a00; 
//			a[0][1] = a01; 
//			a[1][0] = a10; 
//			a[1][1] = a11; 
		}
		
		public DoubleMatrix22 mult(DoubleMatrix22 p) {
			DoubleMatrix22 res = new DoubleMatrix22();
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < 2; j++) {
					// sum_k ...  res.a[i][k].multiply(other.a[k][j]);
					res.a[i][j]= (a[i][0] * p.a[0][j])
							+ (a[i][1] * p.a[1][j]); 
				}
			}
			return res;
		}
		
		public DoubleVector2 mult(DoubleVector2 p) {
			DoubleVector2 res = new DoubleVector2();
			for (int i = 0; i < 2; i++) {
				res.a[i] = (a[i][0] * p.a[0])
						+ (a[i][1] * p.a[1]); 
			}
			return res;
		}
		

		public DoubleMatrix22 pow(int exp) {
			DoubleMatrix22 res = this;
			exp--;
			DoubleMatrix22 base = this;
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
	public static class DoubleSymMatrix22 extends DoubleMatrix22 {
		
		public DoubleSymMatrix22() {
			super(0.0, 0.0, 0.0, 0.0);
		}
		
		public DoubleSymMatrix22(double a00, double a01, double a11) {
			super(a00, a01, a01, a11);
		}

		public DoubleSymMatrix22 multSym(DoubleSymMatrix22 p) {
			DoubleSymMatrix22 res = new DoubleSymMatrix22();
			for (int i = 0; i < 2; i++) {
				for (int j = i; j < 2; j++) {
					// sum_k ...  res.a[i][k].multiply(other.a[k][j]);
					res.a[i][j] = (a[i][0] * p.a[0][j])
							+ (a[i][1] * p.a[1][j]);
					res.a[j][i] = res.a[i][j]; 
				}
			}
			return res;
		}
		
		public DoubleSymMatrix22 pow(long exp) {
			DoubleSymMatrix22 res = this;
			exp--;
			DoubleSymMatrix22 base = this;
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
	public static class DoubleVector2 {
		double[] a = new double[2];
		
	}
}
