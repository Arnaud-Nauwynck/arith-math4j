package fr.an.math4j.arith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrimeNumberUtils {

	public static boolean slowIsPrime(long n) {
		if (n < 1) return false;
		if (n == 2) return true;
		if ((n % 2) == 0) return false;
		long maxK = (long) Math.sqrt(n) + 1;
		for (int k = 3; k < maxK; k+=2) {
			if ((n % k) == 0) {
				return false;
			}
		}
		return true;
	}

	public static int[] cachedFirstPrimes = new int[] { 
			2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, //
			101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, //
			211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, //
			307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397, //
			401, 409, 419, 421, 431, 433, 439, 443, 449, 457, 461, 463, 467, 479, 487, 491, 499, //
			503, 509, 521, 523, 541, 547, 557, 563, 569, 571, 577, 587, 593, 599, //
			601, 607, 613, 617, 619, 631, 641, 643, 647, 653, 659, 661, 673, 677, 683, 691, //
			701, 709, 719, 727, 733, 739, 743, 751, 757, 761, 769, 773, 787, 797, //
			809, 811, 821, 823, 827, 829, 839, 853, 857, 859, 863, 877, 881, 883, 887, //
			907, 911, 919, 929, 937, 941, 947, 953, 967, 971, 977, 983, 991, 997, 
			1009, 1013, 1019, 1021, 1031, 1033, 1039, 1049, 1051, 1061, 1063, 1069, 1087, 1091, 1093, 1097, //
			1103, 1109, 1117, 1123, 1129, 1151, 1153, 1163, 1171, 1181, 1187, 1193, //
			1201, 1213, 1217, 1223, 1229, 1231, 1237, 1249, 1259, 1277, 1279, 1283, 1289, 1291, 1297, //
			1301, 1303, 1307, 1319, 1321, 1327, 1361, 1367, 1373, 1381, 1399, //
			1409, 1423, 1427, 1429, 1433, 1439, 1447, 1451, 1453, 1459, 1471, 1481, 1483, 1487, 1489, 1493, 1499,
	};

	public static int[] getNthFirstPrimes(int n) {
		int[] res = new int[n];
		int len = (cachedFirstPrimes != null)? cachedFirstPrimes.length : 0;
		int lastCachedPrime = 0;
		if (cachedFirstPrimes != null) {
			System.arraycopy(cachedFirstPrimes, 0, res, 0, Math.min(n, len));
			lastCachedPrime = cachedFirstPrimes[len-1]; 
		}
		if (len < n) {
			int index = len;
			for (int i = lastCachedPrime+2; index<n; i+=2) {
				if (slowIsPrime(i)) {
					res[index++] = i;
				}
			}
			cachedFirstPrimes = res;
		}
		return res;
	}
	
	public static int[] getPrimesUpTo(int maxPrime) {
		int lastCachedPrime = 0;
		if (cachedFirstPrimes != null) {
			int len = (cachedFirstPrimes != null)? cachedFirstPrimes.length : 0;
			lastCachedPrime = cachedFirstPrimes[len-1];
			if (lastCachedPrime > maxPrime) {
				// enough cached...
				// dichotomy search for sub-array, or return enclosing array
				int search = Arrays.binarySearch(cachedFirstPrimes, maxPrime);
				if (search < 0) {
					search = -(search + 1); // for Arrays.binarySearch not found marker 
					search--;
				}
				int[] res = new int[search+1];
				System.arraycopy(cachedFirstPrimes, 0, res, 0, search+1);
				return res;
			}
		}
		
		boolean[] isPrime = eratosteneCrible(maxPrime);
		int len = isPrime.length;
		int countPrimes = 0;
		for (int i = 0; i < len; i++) {
			if (isPrime[i]) countPrimes++; 
		}
		int[] res = new int[countPrimes];
		int primeIndex = 0;
		
		for (int i = 0; i < len; i++) {
			if (isPrime[i]) {
				res[primeIndex++] = i; 
			}
		}
		
//		System.arraycopy(cachedFirstPrimes, 0, res, 0, cachedFirstPrimes.length);
//		primeIndex = cachedFirstPrimes[cachedFirstPrimes.length - 1];
//		for (int i = cachedFirstPrimes.length; i < len; i++) {
//			if (isPrime[i]) res[primeIndex++] = i;  
//		}
		
		cachedFirstPrimes = res;
		
		return res;
	}
	
	
	public static int[] getPrimesPi(int[] primes) {
		int maxI = primes.length;
		int[] res = new int[maxI];
		int j= 0;
		for (int i= 0; i < maxI; i++) {
			if (i == primes[j]) {
				j++;
			}
			res[i]= j;
		}
		return res;
	}
	  

	public static List<Integer> naiveListDivisors(long number) {
		List<Integer> res = new ArrayList<Integer>();
		res.add(1);
		for (int d = 2; d < number; d++) {
			if ((number % d) == 0) {
				res.add(d);
			}
		}
		res.add((int) number);
		return res;
	}
	
	public static boolean[] eratosteneCrible(int n) {
		boolean[] res = new boolean[n];
		Arrays.fill(res, true);
		res[0] = false;
		res[1] = false;
		for (int i = 2; i < n; i++) {
			if (res[i]) {
				for (int j = i+i; j < n; j+=i) res[j] = false;
			}
		}
		return res;
	}
	
	public static boolean[] eratosteneSieveRange(long from, boolean[] res, int[] sievePrimes) {
		Arrays.fill(res, true);
		final int len = res.length;
		for (int prime : sievePrimes) {
			// find smallest multiple: prime*k >= from
			long firstMultiple = prime * (long) ((from+prime-1)/prime); 
			for (int j = (int) (firstMultiple-from); j < len; j+=prime) res[j] = false;
		}
		return res;
	}
	
	public static boolean isPrime(int n, boolean[] cached) {
		if (n <= 1) return false;
		if (n < cached.length) return cached[n];
		else return slowIsPrime(n);
	}
	
	
	public static int[] eratosteneMaxPrimeDivisorsUpTo(int n) {
		final int maxI = n + 1;
		int[] res = new int[maxI];
		Arrays.fill(res, 1);
		for (int i = 2; i < maxI; i++) {
			if (res[i] == 1) {
				res[i] = i;
				for (int j = i+i; j < maxI; j+=i) res[j] = i;
			}
		}
		return res;
	}

	public static int[] eratosteneMinPrimeDivisorsUpTo(int n) {
		final int maxI = n + 1;
		int[] res = new int[maxI];
		Arrays.fill(res, 1);
		for (int i = 2; i < maxI; i++) {
			if (res[i] == 1) {
				res[i] = i;
				for (int j = i+i; j < maxI; j+=i) {
					if (res[j] == 1) res[j] = i;
				}
			}
		}
		return res;
	}

}
