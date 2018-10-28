package fr.an.math4j.arith;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

import org.junit.Assert;
import org.junit.Test;

import fr.an.math4j.arith.FibonacciUtil;
import fr.an.math4j.arith.BigIntegerMatrixUtil.BigIntegerSymMatrix22;
import fr.an.math4j.arith.BigIntegerMatrixUtil.BigIntegerSymModMatrix22;
import fr.an.math4j.arith.IntMatrixUtil.IntSymModMatrix22;


public class FibonacciUtilTest {

	@Test
	public void testFibByMat() {
		BigInteger modulo = new BigInteger("1000000000"); 
		
		for (int i = 1; i < 100; i++) {
			BigInteger checkRes = FibonacciUtil.fib_basicBigInt(i);	
			BigInteger res = FibonacciUtil.fib_Matrix(i);
			Assert.assertEquals(checkRes, res);
			
			BigInteger checkResMod = res.mod(modulo);
			BigInteger resMod = FibonacciUtil.fibModulo_Matrix(i,  modulo);
			Assert.assertEquals(checkResMod, resMod);
		}
	}
	
	@Test
	public void testFibonacci() {
		for (int i = 1; i < 100; i++) {
			BigInteger checkRes = FibonacciUtil.fib_basicBigInt(i);	
			BigInteger res = FibonacciUtil.fibonacci(i);
			Assert.assertEquals(checkRes, res);
		}
	}
	
	@Test
	public void testFibonacciModulo() {
		long modulo = 1000_000_000L;
		BigInteger bigModulo = BigInteger.valueOf(modulo); 
		for (int i = 1; i < 100; i++) {
			long checkRes = FibonacciUtil.fib_basicBigInt(i).mod(bigModulo).longValue();	
			long res = FibonacciUtil.fibonacciModuloLong(i, modulo);
			Assert.assertEquals(checkRes, res);
		}
	}
	
	@Test
	public void testFibonacciModulo2() {
		BigInteger bigModulo = BigInteger.valueOf(1000_000_000L);
		for (int i = 1; i < 100; i++) {
			BigInteger checkRes = FibonacciUtil.fib_basicBigInt(i).mod(bigModulo);	
			BigInteger res = FibonacciUtil.fibonacciModulo(BigInteger.valueOf(i), bigModulo);
			Assert.assertEquals(checkRes, res);			
		}
	}
	
	@SuppressWarnings("unused")
	@Test
	public void benchBasicFib() {
		long n = 10;
		BigInteger modulo = new BigInteger("1000000000"); 
		for (int i = 0; i < 7; i++,n *= 10) {

			long nanosDoubleBefore = System.nanoTime();
			double resDouble = FibonacciUtil.fib_MatrixDouble(n);
			long nanosDouble = System.nanoTime() - nanosDoubleBefore;
			System.out.println("F(" + n + ") => with MatrixDouble=... took " + (nanosDouble/1000000) + "ms");

			long nanosBigDecBefore = System.nanoTime();
			MathContext mc = new MathContext(1000);
			BigDecimal resBigDec = FibonacciUtil.fib_MatrixBigDecimal(n, mc);
			long nanosBigDec = System.nanoTime() - nanosBigDecBefore;
			System.out.println("F(" + n + ") => with BigDecimal precision=... took " + (nanosBigDec/1000000) + "ms");

			BigInteger resBigModulo = FibonacciUtil.fibModulo_Matrix(n, modulo);

				
			if (n <= 100000) {
				long nanosBasicBefore = System.nanoTime();
				BigInteger res = FibonacciUtil.fib_Matrix((int) n);
				long nanosBasic = System.nanoTime() - nanosBasicBefore;
				System.out.println("compute Fibonacci(" + n + ") => with Matrix ... took " + (nanosBasic/1000000) + "ms");
				System.out.println("number of bits:" + res.bitLength() + " (of decimals:" + (int)(res.bitLength()/Math.log(10)) + ")");

				if (n <= 10000) {
					long nanosBefore = System.nanoTime();
					BigInteger res2 = FibonacciUtil.fib_basicBigInt(n);
					long nanos = System.nanoTime() - nanosBefore;
					System.out.println("F(" + n + ") => with basic BigInteger ... took " + (nanos/1000000) + "ms");
					Assert.assertEquals(res, res2);
				}

			}
			
			
			System.out.println();
		}
	}
	
	
	@Test
	public void testFibInverse() {
		BigInteger fn = new BigInteger("971183874599339129547649988289594072811608739584170445"); 
		int i = 200;
		int prevI = 0;
		for(;;) {
			BigInteger fi = FibonacciUtil.fib_basicBigInt(i);
			int comp = fi.compareTo(fn);
			if (comp == 0) {
				System.out.println("found fib inv:" + i);
				break;
			} else if (comp < 0) {
				i++;
				if (prevI == i) break;
			} else if (comp > 0) {
				i--;
				if (prevI == i) break;
			}
			prevI = i;
		}
	}

	@Test
	public void test_getPrecomputedA2Pows() {
		long nanosBefore = System.nanoTime();
		int n = 22; // 22 => 3 sec,  23 => 11.5 sec, 24 => ...
		BigIntegerSymMatrix22[] res = FibonacciUtil.getPrecomputedA2Pows(n);
		long nanos = System.nanoTime() - nanosBefore;
		System.out.println("precompute exact A^2i for i<" + n + "... ... took " + (nanos/1000000) + " ms");
		Assert.assertNotNull(res);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void test_finModulo_MatrixPrecomputeA2Pows() {
		int precomputeLen = 22;

		long nanosBefore = System.nanoTime();
		BigInteger p = BigInteger.valueOf(10000000); // BigInteger.probablePrime(bitLength, rnd) 
		BigInteger modulo = p.multiply(p); 

		// pre-compute A^2i(modulo p2)
		BigIntegerSymModMatrix22[] aMod2Pows = FibonacciUtil.getPrecomputeA2PowsModulo(precomputeLen, modulo);

		for (int i = 3; i < 1000; i++) {
			BigInteger expectedFibN = FibonacciUtil.fibModulo_Matrix(i, modulo);
			BigInteger check1FibN = FibonacciUtil.fibModulo_Matrix(i, modulo);
			BigInteger checkFibN = FibonacciUtil.fibModulo_MatrixPrecomputedPows(i, modulo, aMod2Pows);
			Assert.assertEquals(expectedFibN, check1FibN);
			Assert.assertEquals(expectedFibN, checkFibN);
		}
		
		long fibMin = 100000000;
		int count = 1000;
		long fibMax = fibMin + count;
		for (long i = fibMin; i < fibMax; i++) {
			// BigInteger fibI = FibonacciUtil.fibModulo_MatrixPrecomputedPows(i, modulo);
			BigInteger fibI = FibonacciUtil.fibModulo_MatrixPrecomputedPows(i, modulo, aMod2Pows);
		}
		long nanos = System.nanoTime() - nanosBefore;
		
		System.out.println("compute FibModulo(" + fibMin + " ,+1,+2..+" + count + " ... took " + (nanos/1000000) + " ms");
	}

	@Test
	public void test_Fib130775524_modulo1016() {
		int n = 130775524;
		BigInteger i10e8 = BigInteger.valueOf(100000000);
		BigInteger modulo = i10e8.multiply(i10e8);
		BigIntegerSymModMatrix22[] a2PowsModulo = FibonacciUtil.getPrecomputeA2PowsModulo(22, modulo);

		for (long dummy = 3; dummy < 10; dummy++) {
			BigInteger dummyFibNModulo = FibonacciUtil.fibModulo_MatrixPrecomputedPows(dummy, modulo, a2PowsModulo);
			long checkDummy = FibonacciUtil.fib_basicLong(dummy);
			Assert.assertEquals(checkDummy, dummyFibNModulo.longValue());
			// System.out.println("Fib(" + dummy + ") (modulo 10e16) = " + dummyFibNModulo);
		}
		
		BigInteger fibNModulo = FibonacciUtil.fibModulo_MatrixPrecomputedPows(n, modulo, a2PowsModulo);
		System.out.println("Fib(" + n + ") (modulo 10e16) = " + fibNModulo);
	}
	
	@Test
	public void test_Fib130775524_bigDecimal() {
		int n = 130775524;
		BigDecimal res = FibonacciUtil.fib_MatrixBigDecimal(n,  new MathContext(10));
		System.out.println("Fib(" + n + ") ~= " + res);
	}
	
	@Test
	public void test_fibModulo_IntMatrixPrecomputedPows() {
		int modulo = 10000000;
		BigInteger moduloI = BigInteger.valueOf(modulo);
		IntSymModMatrix22[] precomputedIntA2PowsModulo = FibonacciUtil.getPrecomputeIntA2PowsModulo(22, modulo);
		BigIntegerSymModMatrix22[] precomputedA2PowsModulo = FibonacciUtil.getPrecomputeA2PowsModulo(22, moduloI);
		
		for (int i = 0; i < precomputedIntA2PowsModulo.length; i++) {
			IntSymModMatrix22 intAPow = precomputedIntA2PowsModulo[i];
			BigIntegerSymModMatrix22 aPow = precomputedA2PowsModulo[i];
			Assert.assertEquals(aPow.a[0][0].intValue(), intAPow.a00); 
			Assert.assertEquals(aPow.a[0][1].intValue(), intAPow.a01); 
			Assert.assertEquals(aPow.a[1][1].intValue(), intAPow.a11); 
		}
		
		for (int i = 2; i < 100; i++) {
			BigInteger checkFib = FibonacciUtil.fib_Matrix(i);
			BigInteger checkFibMod = FibonacciUtil.fibModulo_MatrixPrecomputedPows(i, moduloI, precomputedA2PowsModulo);
			Assert.assertEquals(checkFib.mod(moduloI), checkFibMod);
			
			// <= was failing at i=49 in internal int*int overflow... now using intermediate long, then modulo + cast int
			int fibMod = FibonacciUtil.fibModulo_IntMatrixPrecomputedPows(i, modulo, precomputedIntA2PowsModulo);  
			Assert.assertEquals(checkFib.mod(moduloI).intValue(), fibMod);
		}
	}
	
	@Test
	public void test_bench_fibModulo_IntMatrixPrecomputedPows() {
		int modulo = 10000000;
		IntSymModMatrix22[] precomputedIntA2PowsModulo = FibonacciUtil.getPrecomputeIntA2PowsModulo(22, modulo);
		
		long nanosBefore = System.nanoTime();
		
		long fibMin = 100000000;
		int count = 1000;
		long fibMax = fibMin + count;
		for (long i = fibMin; i < fibMax; i++) {
			@SuppressWarnings("unused")
			int fibMod = FibonacciUtil.fibModulo_IntMatrixPrecomputedPows(i, modulo, precomputedIntA2PowsModulo);
		}
		long nanos = System.nanoTime() - nanosBefore;
		
		System.out.println("compute fibModulo_IntMatrixPrecomputedPows(" + fibMin + " ,+1,+2..+" + count + " ... took " + (nanos/1000000) + " ms");
	
		
	}
}
