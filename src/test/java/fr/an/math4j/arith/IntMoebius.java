package fr.an.math4j.arith;

public class IntMoebius {

	public static int[] GetMu(int max) {
		int sqrt = (int) Math.floor(Math.sqrt(max));
		int[] mu = new int[max + 1];
		for (int i = 1; i <= max; i++) {
			mu[i] = 1;
		}
		for (int i = 2; i <= sqrt; i++) {
			if (mu[i] == 1) {
				for (int j = i; j <= max; j += i)
					mu[j] *= -i;
				for (int j = i * i; j <= max; j += i * i)
					mu[j] = 0;
			}
		}
		for (int i = 2; i <= max; i++) {
			if (mu[i] == i)
				mu[i] = 1;
			else if (mu[i] == -i)
				mu[i] = -1;
			else if (mu[i] < 0)
				mu[i] = 1;
			else if (mu[i] > 0)
				mu[i] = -1;
		}
		return mu;
	}

}
