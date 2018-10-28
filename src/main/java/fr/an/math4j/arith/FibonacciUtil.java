package fr.an.math4j.arith;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

import fr.an.math4j.arith.BigDecimalMatrixUtil.BigDecimalSymMatrix22;
import fr.an.math4j.arith.BigIntegerMatrixUtil.BigIntegerSymMatrix22;
import fr.an.math4j.arith.BigIntegerMatrixUtil.BigIntegerSymModMatrix22;
import fr.an.math4j.arith.DoubleMatrixUtil.DoubleSymMatrix22;
import fr.an.math4j.arith.IntMatrixUtil.IntSymModMatrix22;

public class FibonacciUtil {


	
	/** 
	 * Fast doubling method. ~Faster than the matrix method.
	 * F(2n) = F(n) * (2*F(n+1) - F(n)).
	 * F(2n+1) = F(n+1)^2 + F(n)^2.
	 */
	public static BigInteger fibonacci(int n) {
		BigInteger a = BigInteger.ZERO;
		BigInteger b = BigInteger.ONE;
		for (int bit = Integer.highestOneBit(n); bit != 0; bit >>>= 1) {
			BigInteger d = a.multiply(b.shiftLeft(1).subtract(a));
			BigInteger e = a.multiply(a).add(b.multiply(b));
			a = d;
			b = e;
			if ((n & bit) != 0) {
				BigInteger c = a.add(b);
				a = b;
				b = c;
			}
		}
		return a;
	}
	
	public static long fibonacciModuloLong(int n, long modulo) {
		long a = 0;
		long b = 1;
		for (int bit = Integer.highestOneBit(n); bit != 0; bit >>>= 1) {
			long d = (a * ((b << 1L) - a) % modulo) % modulo;
			while (d < 0) {
				d += modulo;
			}
			long e = (a * a % modulo + b * b % modulo) % modulo;
			a = d;
			b = e;
			if ((n & bit) != 0) {
				long c = (a + b) % modulo;
				a = b;
				b = c;
			}
		}
		return a;
	}
	
	public static BigInteger fibonacciModulo(BigInteger n, BigInteger modulo) {
		BigInteger a = BigInteger.ZERO;
		BigInteger b = BigInteger.ONE;
		for (int bitIndex = n.bitLength(); bitIndex >= 0; bitIndex--) {
			BigInteger d = a.multiply(b.shiftLeft(1).subtract(a)).mod(modulo);
			while (d.signum() < 0) {
				d = d.add(modulo);
			}
			BigInteger e = a.multiply(a).add(b.multiply(b)).mod(modulo);
			a = d;
			b = e;
			if (n.testBit(bitIndex)) {
				BigInteger c = a.add(b);
				a = b;
				b = c;
			}
		}
		return a;
	}
	
	/** another(?) fast doubling method...
	 L_{2n} &= 1/2 (5 F_n^2 + L_n^2)
	 F_{2n} &= L_n F_n

	 L_{2n+1} &= 1/2 (5 F_{2n} + L_{2n})
	 F_{2n+1} &= 1/2 (F_{2n} + L_{2n})
	*/
	
			   
	
	private static final BigIntegerSymMatrix22 A110 = 
			new BigIntegerSymMatrix22(BigInteger.ONE, BigInteger.ONE, BigInteger.ZERO);

//	private static final MathContext CST_mc100 = new MathContext(100);
//	@SuppressWarnings("unused")
//	private static final BigDecimalSymMatrix22 A110_dec100 = getA110_round(CST_mc100);

	private static BigDecimalSymMatrix22 getA110_round(MathContext mc) {
		BigDecimal one = new BigDecimal(1, mc);
		return new BigDecimalSymMatrix22(one, one, new BigDecimal(0, mc));
	}

	private static final DoubleSymMatrix22 A110_double = new DoubleSymMatrix22(1.0, 1.0, 0.0);

	private static BigIntegerSymModMatrix22 getA110_modulo(BigInteger modulo) {
		return new BigIntegerSymModMatrix22(modulo, BigInteger.ONE, BigInteger.ONE, BigInteger.ZERO);
	}

	private static BigIntegerSymMatrix22[] lazy_precomputedA2Pows = null; 
	/*pp*/ static BigIntegerSymMatrix22[] getPrecomputedA2Pows(int len) {
		if (lazy_precomputedA2Pows != null) {
			return lazy_precomputedA2Pows; // TODO check len
		}
		BigIntegerSymMatrix22[] res = new BigIntegerSymMatrix22[len];
		BigIntegerSymMatrix22 base = A110;
		res[0] = new BigIntegerSymMatrix22(BigInteger.ONE, BigInteger.ZERO, BigInteger.ONE);
		res[1] = base; 
		for (int i = 2; i < len; i++) {
			base = base.multSym(base);
			res[i] = base; 
		}
		lazy_precomputedA2Pows = res;
		return res;
	}
	
	public static BigIntegerSymModMatrix22[] getPrecomputeA2PowsModulo(
			int precomputeLen, BigInteger modulo) {
		BigIntegerSymMatrix22[] a2Pows = getPrecomputedA2Pows(precomputeLen);

		BigIntegerSymModMatrix22[] res = new BigIntegerSymModMatrix22[precomputeLen];
		for (int i = 0; i < precomputeLen; i++) {
			res[i] = new BigIntegerSymModMatrix22(modulo, a2Pows[i]);
		}
		return res;
	}

	private static IntSymModMatrix22 getIntA110_modulo(int modulo) {
		return new IntSymModMatrix22(modulo, 1, 1, 0);
	}
	
	public static IntSymModMatrix22[] getPrecomputeIntA2PowsModulo(
			int precomputeLen, int modulo) {
		BigIntegerSymMatrix22[] a2Pows = getPrecomputedA2Pows(precomputeLen);

		IntSymModMatrix22[] res = new IntSymModMatrix22[precomputeLen];
		BigInteger moduloI = BigInteger.valueOf(modulo);
		for (int i = 0; i < precomputeLen; i++) {
			BigIntegerSymMatrix22 a = a2Pows[i];
			int a00 = a.a[0][0].mod(moduloI).intValue(); 
			int a01 = a.a[0][1].mod(moduloI).intValue(); 
			int a11 = a.a[1][1].mod(moduloI).intValue(); 
			res[i] = new IntSymModMatrix22(modulo, a00, a01, a11);
		}
		return res;
	}

	
	public static long fib_basicLong(long k) {
		if (k == 0) return 0;
		if (k == 1 || k == 2) return 1;

		long k_minus_one = 1;
		long k_minus_two = 1;
		long answer = 0; 
		for (int i = 3; i <= k; i++) {
			answer = k_minus_one + k_minus_two;
			k_minus_two = k_minus_one;
			k_minus_one = answer;
		}
		return answer;
	}
	
	public static BigInteger fib_basicBigInt(long k) {
		BigInteger answer = null;
		if (k == 0)
			return BigInteger.ZERO;
		else if (k <= 2)
			return BigInteger.ONE;

		BigInteger k_minus_one = BigInteger.ONE;
		BigInteger k_minus_two = BigInteger.ONE;
		for (int i = 3; i <= k; i++) {
			answer = k_minus_one.add(k_minus_two);
			k_minus_two = k_minus_one;
			k_minus_one = answer;
		}
		return answer;
	}

	
	/**
    [ 1 1 ] n      [ F(n+1) F(n)   ]
    [ 1 0 ]    =   [ F(n)   F(n-1) ]
    
	 * in PariGP... using Matrix + modulo:
	 * <PRE>
	 * fibomod(n, MOD) = (Mod([1,1;1,0],MOD)^(n-1))[1,1];
	 * </PRE>
 	 */
	public static BigInteger[] fibNm1NNp1ByMatrix(int n) {
		BigIntegerSymMatrix22 an = A110.pow(n);
		return new BigInteger[] { an.a[1][1], an.a[1][0], an.a[0][0] };
	}

	public static BigInteger fib_Matrix(int n) {
		if (n == 0) return BigInteger.ZERO;
		if (n == 1 || n == 2) return BigInteger.ONE;
		BigIntegerSymMatrix22 an = A110.pow(n-1);
		return an.a[0][0];
	}

	public static BigDecimal fib_MatrixBigDecimal(long n, MathContext mc) {
		if (n == 0) return BigDecimal.ZERO;
		if (n == 1 || n == 2) return BigDecimal.ONE;
		BigDecimalSymMatrix22 a = getA110_round(mc);
		BigDecimalSymMatrix22 an = a.pow(n-1, mc);
		return an.a[0][0];
	}

	public static double fib_MatrixDouble(long n) {
		if (n == 0) return 0.0;
		if (n == 1 || n == 2) return 1.0;
		DoubleSymMatrix22 an = A110_double.pow(n-1);
		return an.a[0][0];
	}

	public static BigInteger fibModulo_Matrix(long n, BigInteger modulo) {
		if (n == 0) return BigInteger.ZERO;
		if (n == 1 || n == 2) return BigInteger.ONE;
		BigIntegerSymModMatrix22 aModulo = getA110_modulo(modulo);
		BigIntegerSymModMatrix22 an = aModulo.pow(n-1);
		return an.a[0][0];
	}
	
	public static BigInteger fibModulo_MatrixPrecomputedPows(long n, BigInteger modulo, BigIntegerSymModMatrix22[] precomputedA2PowsModulo) {
		if (n == 0) return BigInteger.ZERO;
		if (n == 1 || n == 2) return BigInteger.ONE;
		BigIntegerSymModMatrix22 aModulo = (precomputedA2PowsModulo != null)? precomputedA2PowsModulo[1] : getA110_modulo(modulo);
		BigIntegerSymModMatrix22 an = aModulo.pow(n-1, precomputedA2PowsModulo);
		return an.a[0][0];
	}

	public static int fibModulo_IntMatrixPrecomputedPows(long n, int modulo, IntSymModMatrix22[] precomputedA2PowsModulo) {
		if (n == 0) return 0;
		if (n == 1 || n == 2) return 1;
		IntSymModMatrix22 aModulo = (precomputedA2PowsModulo != null)? precomputedA2PowsModulo[1] : getIntA110_modulo(modulo);
		IntSymModMatrix22 an = aModulo.pow(n-1, precomputedA2PowsModulo);
		return an.a00;
	}

}
