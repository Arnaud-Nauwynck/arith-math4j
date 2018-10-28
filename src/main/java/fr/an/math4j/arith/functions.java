package fr.an.math4j.arith;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * from https://gist.githubusercontent.com/cab1729/1318030/raw/8680c8dfdad7055232e108c4ee2258e38291b459/functions.java
 *
 */
public class functions {
	
	private static double rootpi = StrictMath.sqrt(StrictMath.PI);
	//private static double root2pi = StrictMath.sqrt(2*StrictMath.PI);
	private static double oneonrootpi = 1/rootpi;
	private static double twoonrootpi = 2/rootpi;
	private static double logtwo = StrictMath.log(2);
	private static double pi2on12 = StrictMath.pow(StrictMath.PI, 2)/12.;
	private static int INFINITY = 100000;
	private static double PRECISION = 1E-12;
	
	private static final int[] PRIMES =
			{2,3,5,7,11,13,17,19,23,29,31,37,41,43,47,53,59,
		 	61,67,71,73,79,83,89,97,101,103,107,109,113,127,
		 	131,137,139,149,151,157,163,167,173,179,181,191,
		 	193,197,199,211,223,227,229,233,239,241,251,257,
		 	263,269,271,277,281,283,293,307,311,313,317,331,
		 	337,347,349,353,359,367,373,379,383,389,397,401,
		 	409,419,421,431,433,439,443,449,457,461,463,467,
		 	479,487,491,499,503,509,521,523,541,547,557,563,
		 	569,571,577,587,593,599,601,607,613,617,619,631,
		 	641,643,647,653,659,661,673,677,683,691,701,709,
		 	719,727,733,739,743,751,757,761,769,773,787,797,
		 	809,811,821,823,827,829,839,853,857,859,863,877,
		 	881,883,887,907,911,919,929,937,941,947,953,967,
		 	971,977,983,991,997};
	
	private static int[] primes = PRIMES;	// default values in case of file error
	
	static {
		// load primes array with PARI/GP generated file (5000 primes)
		try {
			BufferedReader pf = 
				new BufferedReader(new FileReader("primes.txt"));
			String ps = pf.readLine();
			String[] pe = ps.split(", ");
			int pel = pe.length;
			primes = new int[pel];
			for (int i=0; i<pel; i++) {
				primes[i] = Integer.parseInt(pe[i]);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//	 Taylor coefficients for 1/gamma(1+x)-x...
	private static final double c[]= {
	    +0.5772156649015328606065120900824024310421593359,
	    -0.6558780715202538810770195151453904812797663805,
	    -0.0420026350340952355290039348754298187113945004,
	    +0.1665386113822914895017007951021052357177815022,
	    -0.0421977345555443367482083012891873913016526841,
	    -0.0096219715278769735621149216723481989753629422,
	    +0.0072189432466630995423950103404465727099048009,
	    -0.0011651675918590651121139710840183886668093337,
	    -0.0002152416741149509728157299630536478064782419,
	    +0.0001280502823881161861531986263281643233948920,
	    -0.0000201348547807882386556893914210218183822948,
	    -0.0000012504934821426706573453594738330922423226,
	    +0.0000011330272319816958823741296203307449433240,
	    -0.0000002056338416977607103450154130020572836512,
	    +0.0000000061160951044814158178624986828553428672,
	    +0.0000000050020076444692229300556650480599913030,
	    -0.0000000011812745704870201445881265654365055777,
	    +1.0434267116911005104915403323122501914007098231E-10,
	    +7.7822634399050712540499373113607772260680861813E-12,
	    -3.6968056186422057081878158780857662365709634513E-12,
	    +5.1003702874544759790154813228632318027268860697E-13,
	    -2.0583260535665067832224295448552374197460910808E-14,
	    -5.3481225394230179823700173187279399489897154781E-15,
	    +1.2267786282382607901588938466224224281654557504E-15,
	    -1.1812593016974587695137645868422978312115572918E-16,
	    +1.1866922547516003325797772429286740710884940796E-18,
	    +1.4123806553180317815558039475667090370863507503E-18,
	    -2.2987456844353702065924785806336992602845059314E-19,
	    +1.7144063219273374333839633702672570668126560625E-20,
	    +1.3373517304936931148647813951222680228750594717E-22,
	    -2.0542335517666727893250253513557337966820379352E-22,
	    +2.7360300486079998448315099043309820148653116958E-23,
	    -1.7323564459105166390574284515647797990697491087E-24,
	    -2.3606190244992872873434507354275310079264135521E-26,
	    +1.8649829417172944307184131618786668989458684290E-26,
	    +2.2180956242071972043997169136268603797317795006E-27,
	    +1.2977819749479936688244144863305941656194998646E-28,
	    +1.1806974749665284062227454155099715185596846378E-30,
	    -1.1245843492770880902936546742614395121194117955E-30,
	    +1.2770851751408662039902066777511246477487720656E-31,
	    -7.3914511696151408234612893301085528237105689924E-33,
	    +1.1347502575542157609541652594693063930086121959E-35,
	    +4.6391346410587220299448049079522284630579686797E-35
	  };
	
	private functions () {
		// prevent instantiation
	}

	
	/**
	 * error function
	 * ref: Java implementation of C function from zetagrid code (math.cpp)
	 * @param x
	 * @return
	 */
	public static double erf (double x) {
		
		double cut = 1.5;
		double y, r = 0.0;
		
		y = functions.fabs(x);
		if (y < cut) {
			// power series expansion
			double ap = 0.5;
			double s, t, x2;
			s = t = 2.0;
			x2 = StrictMath.sqrt(x);
			for (int i = 0; i<INFINITY; i++) {
				ap += 1;
				t *= x2/ap;
				s += t;
				if (functions.fabs(t) < 1e-35 * functions.fabs(s)) {
					r = x * functions.oneonrootpi * s/StrictMath.exp(x2);
					break;
				}
			}
		} else {
			// continued fractions
			double an, small = 1e-300;
			double b,c,d,h,del,x2;
			
			x2 = StrictMath.sqrt(x);
			b = x2+0.5;
			c = 1.0e300;
			d = functions.recip(b);
			h = d;
			for (int i=0; i<INFINITY; i++) {
				an = i*(0.5 - i);
				b += 2.0;
				d = an*d+b;
				if (functions.fabs(d) < small)
					d = small;
				c = b+an/c;
				if (functions.fabs(c) < small)
					c = small;
				d = functions.recip(d);
				del = c*d;
				h *= del;
				if ((del - frac(del)) == 1.0 && del < 1.0e-300)
					break;
			}
			r = 1.0 - functions.oneonrootpi * StrictMath.sqrt(x2) / 
				StrictMath.exp(x2) * h;
		}
		if (x > 0.0)
			return r;
		else
			return -r;
	}
		
	/**
	 * complementary error function
	 * ref: Java implementation of C function from zetagrid code (math.cpp)
	 * @param x
	 * @return
	 */
	public static double erfc (double x) {
		
		double cut = 1.5;
		double y, r = 0.0;
		
		y = functions.fabs(x);
		if (y < cut) {
			// power series expansion
			double ap = 0.5;
			double s, t, x2;
			s = t = 2.0;
			x2 = StrictMath.sqrt(x);
			for (int i = 0; i<INFINITY; i++) {
				ap += 1;
				t *= x2/ap;
				s += t;
				if (functions.fabs(t) < 1e-35 * functions.fabs(s)) {
					r = 1.0 - x * functions.oneonrootpi * s/StrictMath.exp(x2);
					break;
				}
			}
		} else {
			// continued fractions
			double an, small = 1e-300;
			double b,c,d,h,del,x2;
			
			x2 = StrictMath.sqrt(x);
			b = x2+0.5;
			c = 1.0e300;
			d = functions.recip(b);
			h = d;
			for (int i=0; i<INFINITY; i++) {
				an = i*(0.5 - i);
				b += 2.0;
				d = an*d+b;
				if (functions.fabs(d) < small)
					d = small;
				c = b+an/c;
				if (functions.fabs(c) < small)
					c = small;
				d = functions.recip(d);
				del = c*d;
				h *= del;
				if ((del - frac(del)) == 1.0 && del < 1.0e-300)
					break;
			}
			r = functions.oneonrootpi * StrictMath.sqrt(x2) / 
				StrictMath.exp(x2) * h;
		}
		if (x > 0.0)
			return r;
		else
			return -r;
		
	}
	
	// test erf,c for x > 0
	public static double erfg(double x)
	{
		return oneonrootpi *
			incGamma(.5, StrictMath.pow(x,2),0);
	}
	
	public static double erfgc(double x)
	{
		return incGamma(.5, StrictMath.pow(x,2),0)/
			rootpi;
	}
	
	public static double erf2(double x)
	{
		double sum = 0.0;
		for (int k=0; k<INFINITY; k++)
		{
			sum += StrictMath.pow(-1, k)*StrictMath.pow(x, 2*k+1)/
				factorial(k)*(2*k+1);
		}
		
		return twoonrootpi * sum;
	}
	
	public static double erf2c(double x)
	{
		return q_gamma(.5, StrictMath.pow(x,2),0)/
			rootpi;
	}
	
	/**
	 * reciprocal function
	 * @param x
	 * @return
	 */
	public static double recip(double x) {
		if (x == 0.0)
			return 0.0;
		return 1.0/x;
	}
	
	/**
	 * combine StrictMath.floor and StrictMath.abs to implement c++ fabs
	 * @param x
	 * @return
	 */
	public static double fabs(double x) {
		
		return StrictMath.floor(StrictMath.abs(x));
	}
	
	/**
	 * fractional function {x} = x - [x]
	 * @param x
	 * @return fractional part of x
	 * 
	 * caveat: this function does not seem to play well with negative input values
	 * 
	 */
	public static double frac(double x)
	{
		return x - StrictMath.floor(x);
	}
	
	
	/**
	 * check if argument is an even integer
	 * @param n
	 * @return 1 if n is even, else -1
	 */
	public static int even (int n) {
		if (n%2 == 0)
			return 1;
		else
			return -1;
	}
	
	
	/**
	 * Riemann-Siegel theta function using Stirling series
	 * @param t
	 * @return
	 */
	public static double theta (double t) {
		return (t/2.0 * StrictMath.log(t/2.0/StrictMath.PI) - t/2.0
				- StrictMath.PI/8.0 + 1.0/48.0/t + 7.0/5760.0/t/t/t);
	}
	
	/**
	 * moebius function
	 * implemented as series representation 
	 * ref: http://functions.wolfram.com/13.07.06.0002.01
	 * @param n
	 * @return 
	 * 		0, if n has >= repeated prime factors
	 * 		1, if n = 1
	 * 		(-1)^k, if n is a product of k distinct primes
	 */
	public static long moebiusmu (int n) {
		
		// test for zero value
		if (n == 0)
			return 0;
		
		double m = 0.0;
		
		for (int k=1; k <= n; k++) {
			m += (functions.kdelta(functions.gcd(n, k), 1)
				* (StrictMath.cos((2*StrictMath.PI*k)/n)));	
		}
		
		return (long) StrictMath.round(m);
	}
	
	/**
	 * Mangoldt function
	 * @param n
	 * @return 0 if n is not a power of a prime p,
	 * 	ln(p) otherwise
	 */
	public static double mangoldtLambda (int n) {
		
		int plen = primes.length;
		int klim = 200;
		double powerof = 0.0;
		
		for (int p = 0; p < plen; p++) {
			for (int k = 1; k < klim; k++) {
				if (n == StrictMath.pow(primes[p], k)) {
					powerof = primes[p];
					break;
				}
			}
		}
		
		if (powerof > 0) {
			return StrictMath.log(powerof);
		} else {
			return 0.0;
		}
	}
	
	
	/**
	 * Summatory mangoldt function
	 * @param x
	 * @return double s
	 */
	public static double psi(int x) {
		
		double s = 0.0;
		for (int n = 1; n < x; n++) {
			s += functions.mangoldtLambda(n);
		}
		
		return s;
	}
	
	/**
	 * repetitive prime factor counting function
	 * @param x
	 * @return double s
	 */
	public static int bigOmega(long n)
	{
		int factors = 0;
		
		// for each potential factor i
        for (long i = 2; i <= n / i; i++) {

            // if i is a factor of N, repeatedly divide it out
            while (n % i == 0) {
                factors++; 
                n = n / i;
            }
        }
        
        // if biggest factor occurs only once, n > 1
        if (n > 1) factors++;
		
		return factors;
	}
	
	/**
	 * Liouville Lambda
	 * @param long n
	 * @return int
	 */
	public static int LiouvilleLambda(long n)
	{
		return (int)StrictMath.pow(-1, bigOmega(n));
	}
	
	/**
	 * Summatory Liouville function
	 * @param long n
	 * @return int
	 */
	public static int L(int n)
	{
		int sum = 0;
		for (int k=1; k<=n; k++)
		{
			sum += LiouvilleLambda(k);
		}
		
		return sum;
	}
	
	/**
	 * another Summatory Liouville function?
	 * @param long n
	 * @return int
	 */
	public static int T(int n)
	{
		int sum = 0;
		for (int k=1; k<=n; k++)
		{
			sum += (LiouvilleLambda(k)/k);
		}
		
		return sum;
	}
	
	/**
	 * Kronecker delta
	 * @param n1
	 * @param n2
	 * @return 1 if n1 == n2, 0 otherwise
	 */
	public static int kdelta (int n1, int n2) {
		if (n1 == n2)
			return 1;
		else
			return 0;
	}
	
	/**
	 * gcd
	 * @param int m
	 * @param int n
	 * @return gcd of m,n
	 */
	public static int gcd(int m, int n) {

        if (m < n) {
            int t = m;
            m = n;
            n = t;
        }

        int r = m % n;

        if (r == 0) {
            return n;
        } else {
            return gcd(n, r);
        }

    }
	
	
	/**
	 * Mertens summary function M(n)
	 * @param n
	 * @return
	 */
	public static int M (int n) {
		int m = 0;
		for (int k = 1; k <= n; k++) {
			m += functions.moebiusmu(k);
		}
		return m;
	}
	
	/**
	 * Binomial function
	 * ref: http://functions.wolfram.com/06.03.02.0001.01
	 * @param n
	 * @param k
	 * @return
	 */
	public static double Binomial(int n, int k)
	{
		return factorial(n)/(factorial(k)*factorial(n-k));
	}
	
	
	/**
	 * factorial function
	 * @param n
	 * @return
	 */
	public static double factorial(int N)
	{
		int n = 1;
		double f = 1.0;
		do
		{
			f *= n;
			n++;
		} while (n <= N);
		
		return f;
	}
	
	/**
 	 * return a Stirling number of the first kind
 	 * @param n
 	 * @param k - partitions
 	 * @return
 	 */
	public static double Stirling1K(int n, int k)
	{
		// special value 1 if n, k = 0
		if (n == 0.0 && k == 0.0)
			return 1.0;
		
		return factorial(n) * Binomial(k, n);
	}
	
	/**
 	 * logarithmic integral function
 	 * @param z
 	 * @return
 	 * 
 	 * ref: http://functions.wolfram.com/06.36.06.0015.01
 	 */
	public static double Li(double z)
	{
		double z1 = z - 1;
		double l2 = (StrictMath.log(z - 1) - StrictMath.log(1/z1))/2;
		double s1 = 0.0;
		double s2 = 0.0;
		for (int k=0; k < INFINITY; k++)
		{
			s1 += (StrictMath.pow(-1, k))/factorial(k+1);
			s2 = 0.0;
			for (int j=1; j <= k+1; j++)
			{
				s2 += (BernoulliB(j)*Stirling1K(k, j-1))/j;
			}
			s1 *= s2;
			s1 *= StrictMath.pow(1-z, k+1);
		}
				
		return l2 + constants.EM + s1;
	}
	
 	/**
 	 * extended Gamma function
 	 * @param s
 	 * @param N
 	 * @return
 	 */
	public static double PI(int s, int N)
 	{
 		double fn = functions.factorial(N);
 		double fs = functions.factorial(s+N);
 		
 		return (fn/fs)*(StrictMath.pow(N+1, s));
 	}
	
	/**
 	 * extended Gamma function for double arguments
 	 * @param s
 	 * @return
 	 */
	public static double PI(double s)
	{
		int n=1;
		double result = 
			(StrictMath.pow(n, 1-s)*StrictMath.pow(n+1, s))/(s+n);
		for (n=2;n<INFINITY;n++)
		{
			result *= (StrictMath.pow(n, 1-s)*StrictMath.pow(n+1, s))/(s+n);
		}
		
		return result;
	}
	
	/**
	 * Gamma function
	 * ref: java implementation of zetagrid c++ code (math.cpp)
	 * @param z
	 * @return
	 */
	public static double Gamma(double z) {
		
		double ss = z;
		double f = 1.0;
		double sum = 0.0;
		double one = 1.0;
		int n = 43;
		
		while (ss > one) {
			ss -= 1;
			f *= ss;
		}
		while (ss < one) {
			f/= ss;
			ss += 1;
		}
		
		if (ss == one) {
			return f;
		}
		
		ss -= 1.0;
		
		for (int i = n-1; i >= 0; i--) {
			sum = c[i] + ss * sum;		
		}
		
		return f/ (ss * sum+1);	
	}
	
	/**
	 * Beta function
	 * ref: Weisstein, Eric W. "Beta Function." 
	 * 		From MathWorld--A Wolfram Web Resource. 
	 * 		http://mathworld.wolfram.com/BetaFunction.html 
	 * @param p
	 * @param q
	 * @return
	 */
	public static double beta(double p, double q)
	{
		return (Gamma(p)*Gamma(q))/Gamma(p+q);
	}
	
	/**
	 * Generalized Incomplete Gamma (3 arg)
	 * ref: http://functions.wolfram.com/06.07.06.0002.01
	 * @param a
	 * @param z1
	 * @param z2
	 * @return
	 */
	public static double incGamma(double a, double z1, double z2)
	{
		double z1a = StrictMath.pow(z1,a);
		double z2a = StrictMath.pow(z2,a);
		
		double sum2 = 0.0;
		// check zero values for z2,z1 to avoid NaN, +/- Infinity
		if (z2 != 0.0)
		{
			for (int k=0;k<INFINITY;k++)
			{
				sum2 += (StrictMath.pow(-z2, k))/
				((a+k)*factorial(k));
			}
		}
		double sum1 = 0.0;
		if (z1 != 0.0)
		{
			for (int k=0;k<INFINITY;k++)
			{
				sum1 += (StrictMath.pow(-z1, k))/
				((a+k)*factorial(k));
			}
		}
		
		double result = (z2a*sum2);
		if (result != 0.0)
			result -= (z1a*sum1);
		else
			result = (z1a*sum1);
		return result;
		//return (z2a*sum2) - (z1a*sum1);
	}
		
	/**
	 * Generalized Incomplete Gamma (2 args)
	 * 
	 * @param a	
	 * @param z1
	 * @return
	 */
	public static double incGamma(double a, double z1)
	{
		if (z1 == 0.0)
			return Gamma(a);
		
		double result = StrictMath.pow(z1,a);
		double sum = 0.0;
		for (int k=0; k<100; k++)
		{
			sum +=
				((StrictMath.pow(-z1,k)/(a+k)*factorial(k)));
		}
		
		return Gamma(a) - (result*sum);
	}
	
	/**
	 * Generalized Incomplete Gamma (2 args)
	 * alternate experimental version
	 * ref: Spanier & Oldham
	 * @param v
	 * @param x
	 * @return
	 */
	public static double incGamma2(double v, double x)
	{
		double g,p,j,f = 0.0;
		
		g = p = 1.0;
		double vl = v+1;
		
		while(vl <= 2.)
		{ 
			g *= x;
			p *= vl;
			p += g;
			vl++;
		}
		
		double j2 = (5*(3+StrictMath.abs(x))/2);
		j = j2 - frac(j2);
		f = 1/(j+vl-x);
		
		do {
			j -= 1;
			f = (f*x+1)/(j+vl);
		} while (j > 0.0);
		
		p += (f*g*x);
		double vl2 = StrictMath.pow(vl, 2);
		g = ((1-2/7*vl2)*(1-2/3*vl2))/(30*vl2);
		g = ((g-1)/12*vl) - vl*(StrictMath.log(vl)-1);
		
		f = p*StrictMath.exp(g-x)*StrictMath.sqrt(vl/(2*StrictMath.PI));
		
		return f;
	}
	
	/**
	 * Regularized Incomplete Gamma
	 * ref: http://functions.wolfram.com/06.09.02.0001.01
	 * @param a
	 * @param z1
	 * @param z2
	 * @return
	 */
	public static double regIncGamma(double a, double z1, double z2)
	{
		
		return incGamma(a, z1, z2)/Gamma(a);
		
	}

	/**
	 * Incomplete gamma function
	 * 1 / Gamma(a) * Int_0^x exp(-t) t^(a-1) dt
	 * ref:
	 * erf.c  - public domain implementation of error function erf(3m)
	 * reference - Haruhiko Okumura: C-gengo niyoru saishin algorithm jiten
	 * New Algorithm handbook in C language) (Gijyutsu hyouronsha, Tokyo, 1991) p.227 [in Japanese]                 
	 * 
	 * @param a
	 * @param x
	 * @param loggamma_a
	 * @return
	 */
	public static double p_gamma(double a, double x, double loggamma_a)
	{
	    int k;
	    double result, term, previous;

	    //if (x >= 1 + a) 
	    //	return 1 - q_gamma(a, x, loggamma_a);
	    if (x == 0)     
	    	return 0;
	    result = term = StrictMath.exp(a * StrictMath.log(x) - x - loggamma_a) / a;
	    for (k = 1; k < 1000; k++) {
	        term *= x / (a + k);
	        previous = result;  
	        result += term;
	        if (result == previous) 
	        	return result;
	    }
	    System.out.println("functions.java:%d:p_gamma() could not converge.");
	    return result;
	}

	/**
	 * Incomplete gamma function
	 * 1 / Gamma(a) * Int_x^inf exp(-t) t^(a-1) dt
	 * ref:
	 * erf.c  - public domain implementation of error function erf(3m)
	 * reference - Haruhiko Okumura: C-gengo niyoru saishin algorithm jiten
	 * New Algorithm handbook in C language) (Gijyutsu hyouronsha, Tokyo, 1991) p.227 [in Japanese]                 
	 * 
	 * @param a
	 * @param x
	 * @param loggamma_a
	 * @return
	 */
	public static double q_gamma(double a, double x, double loggamma_a)
	{
	    int k;
	    double result, w, temp, previous;
	    double la = 1, lb = 1 + x - a;  /* Laguerre polynomial */

	    //if (x < 1 + a) 
	    //	return 1 - p_gamma(a, x, loggamma_a);
	    w = StrictMath.exp(a * StrictMath.log(x) - x - loggamma_a);
	    result = w / lb;
	    for (k = 2; k < 1000; k++) {
	        temp = ((k - 1 - a) * (lb - la) + (k + x) * lb) / k;
	        la = lb;  lb = temp;
	        w *= (k - 1 - a) / k;
	        temp = w / (la * lb);
	        previous = result;  
	        result += temp;
	        if (result == previous) 
	        	return result;
	    }
	    System.out.println("functions.java:%d:q_gamma() could not converge.");
	    return result;
	}

	
	/**
	 * return nth Bernoulli number in rational form
	 * @param n
	 * @return
	 */
	public static double BernoulliB(int n)
	{
		
		double Bn = 0.0; // return 0 if n is not even, >= 2
		if ((n % 2) != 0)
			return Bn;
		if (n == 0)
			return 1;
		if (n == 1)
			return -0.5;
		
		double K = (2*factorial(n))/StrictMath.pow((2*StrictMath.PI), n);
		
		double d = 0.0;
		int pl = primes.length;
		int prod = 1;
		for (int i = 0; i < pl; i++)
		{
			if ((n % (primes[i]-1)) == 0)
				prod *= primes[i];
		}
		d = prod;
		
		// intermediate work field for debugging
		//double nwork = StrictMath.pow((K*d), 1.0/(n-1));
		double N = StrictMath.ceil(StrictMath.pow((K*d), 1.0/(n-1)));
		
		double z = 1.0;
		for (int i = 0; i < pl; i++)
		{
			if (primes[i] > N)
				break;
			if (i == 0)
			{
				z = StrictMath.pow(1-(StrictMath.pow(primes[i], -n)), -1);
			}else {
				z *= StrictMath.pow(1-(StrictMath.pow(primes[i], -n)), -1);
			}
		}
						
		double a = StrictMath.pow((-1), (n/2)+1)*StrictMath.ceil(d*K*z);
		
		Bn = a/d;
		
		return Bn;
	}
	
	/**
	 * return nth Bernoulli polynomial of x using Fourier expansion
	 * ref: Abramowitz & Stegun 23.1.16 pp 805
	 * @param n
	 * @param x
	 * @return
	 */
	public static double BernoulliP (int n, double x)
	{
		// for n>1, 1>=x>=0
		// for n=1, 1>x>0
		
		// for x=0 return the nth Bernoulli number
		if (x==0.0) {
			return BernoulliB(n);
		}
				
		double K = -2*(factorial(n))/StrictMath.pow((2*StrictMath.PI), n);
		
		double S = 0.0;
		for (int k=1; k<100001; k++)
		{
			S += 
				StrictMath.cos((2*StrictMath.PI*k*x)-(.5*StrictMath.PI*n))/
					StrictMath.pow(k, n);
		}
		
		return K*S;
	}
	
	/**
	 * Periodic Bernoulli function - Bn({x})
	 * @param n
	 * @param x
	 * @return
	 */
	public static double BBn(int n, double x) {
		
		return BernoulliP(n, frac(x));
	}
	
	/**
	 * return nth Euler number in rational form
	 * ref: http://functions.wolfram.com/04.12.06.0001.01
	 * @param n
	 * @return
	 */
	public static double EulerE(int n)
	{
		// return fixed point values
		if (n == 0)
			return 1;
		else if (even(n) != 1)
			return 0;
		
		double K = (StrictMath.pow(2,n+2)*factorial(n)) /
						StrictMath.pow(StrictMath.PI, n+1);
		double S = 0.0;
		
		for (int k=0; k<100001; k++)
		{
			S += (1./StrictMath.pow(2*k+1,n+1)) *
					StrictMath.cos((k*StrictMath.PI)-((n*StrictMath.PI)/2));
		}
		
		return K*S;
	}
	
	
	/**
	 * return nth Euler polynomial of x using Fourier expansion
	 * ref: 
	 * - Abramowitz & Stegun 23.1.16 pp 805, table 23.1
	 * - http://functions.wolfram.com/05.13.06.0001.01
	 * @param n
	 * @param x
	 * @return
	 */
	public static double EulerP(int n, double x)
	{
		// for n>0, 1>=x>=0
		// for n=0, 1>x>0
		
		// for small n (<=15) calculate using coefficients - A&S table 23.1 pp 809
		if (n == 0)
			return 1.0;
		else if (n == 1)
			return x - .5;
		else if (n == 2)
			return x*x - x;
		else if (n == 3)
			return StrictMath.pow(x,3) - 
					(StrictMath.pow(x,2)*1.5) + .25;
		else if (n == 4)
			return StrictMath.pow(x,4) - 
					(2*StrictMath.pow(x,3)) + x;
		else if (n == 5)
			return StrictMath.pow(x,5) -
					(StrictMath.pow(x,4)*2.5) + (StrictMath.pow(x,2)*2.5) - .5;
		else if (n == 6)
			return StrictMath.pow(x, 6) - (3*StrictMath.pow(x, 5)) +
						(5*StrictMath.pow(x, 3)) - 3*x;
		else if (n == 7)
			return StrictMath.pow(x, 7) - (StrictMath.pow(x, 6)*3.5) +
						(StrictMath.pow(x, 4)*8.75) - 
						(StrictMath.pow(x, 2)*10.5) + 2.125;
		else if (n == 8)
			return StrictMath.pow(x, 8) - (4*StrictMath.pow(x, 7)) +
						(14*StrictMath.pow(x, 5)) - 
						(28*StrictMath.pow(x, 3)) + 17*x;
		else if (n == 9)
			return StrictMath.pow(x, 9) - (StrictMath.pow(x, 8)*4.5) +
						(21*StrictMath.pow(x, 6)) - 
						(63*StrictMath.pow(x, 4)) +
						(StrictMath.pow(x, 2)*76.5) - 15.5;
		else if (n == 10)
			return StrictMath.pow(x, 10) - (5*StrictMath.pow(x, 9)) +
						(30*StrictMath.pow(x, 7)) -
						(126*StrictMath.pow(x, 5)) +
						(255*StrictMath.pow(x, 3)) -
						(155*x);
		else if (n == 11)
			return StrictMath.pow(x, 11) - (StrictMath.pow(x, 10)*5.5) +
						(StrictMath.pow(x, 8)*41.25) -
						(231*StrictMath.pow(x, 6)) +
						(StrictMath.pow(x, 4)*701.25) -
						(StrictMath.pow(x, 2)*852.5) + 172.75;
		else if (n == 12)
			return StrictMath.pow(x, 12) - (6*StrictMath.pow(x, 11)) +
						(55*StrictMath.pow(x, 9)) -
						(396*StrictMath.pow(x, 7)) +
						(1683*StrictMath.pow(x, 5)) -
						(3410*StrictMath.pow(x, 3)) + 2073*x;
		else if (n == 13)
			return StrictMath.pow(x, 13) - (StrictMath.pow(x, 12)*6.5) +
						(StrictMath.pow(x, 10)*71.5) -
						(StrictMath.pow(x, 8)*643.5) +
						(StrictMath.pow(x, 6)*3646.5) -
						(StrictMath.pow(x, 4)*11082.5) +
						(StrictMath.pow(x, 2)*13474.5) - 2730.5;
		else if (n == 14)
			return StrictMath.pow(x, 14) - (7*StrictMath.pow(x, 13)) +
						(91*StrictMath.pow(x, 11)) -
						(1001*StrictMath.pow(x, 9)) +
						(7293*StrictMath.pow(x, 7)) -
						(31031*StrictMath.pow(x, 5)) +
						(62881*StrictMath.pow(x, 3)) - 38227*x;
		else if (n == 15)
			return StrictMath.pow(x, 15) - (StrictMath.pow(x, 14)*7.5) +
						(StrictMath.pow(x, 12)*113.75) -
						(StrictMath.pow(x, 10)*1501.5) +
						(StrictMath.pow(x, 8)*13674.375) -
						(StrictMath.pow(x, 6)*77577.5) +
						(StrictMath.pow(x, 4)*235803.75) -
						(StrictMath.pow(x, 2)*286702.5) + 58098.0625;
		
		double K = 4*(factorial(n))/StrictMath.pow(StrictMath.PI, n+1);
		double S = 0.0;
		
		for (int k=0; k<100001; k++)
		{
			S += 
				StrictMath.sin((2*k+1)*((StrictMath.PI*x)-.5*(StrictMath.PI*n)))/
					StrictMath.pow(2*k+1, n+1);
		}
		
		return K*S;
	}
	
	/**
	 * ref: Spanier & Oldham (?)
	 * @param n
	 * @param z
	 * @return
	 */
	public static double Ep(int n, double z)
	{
		double s,f,g,j,x;
		x = z;
		s = f = 1;
		f = g = 0;
		
		while(x>1) {
			x--;
			g += 2*s*StrictMath.pow(x,n);
			s *= -1;
		}
		
		do {
			g += 2*s*StrictMath.pow(x,n);
			s *= -1;
			x++;
		} while (x<0);
		
		j = 1.5 + StrictMath.floor(30/n);
		do {
			f += StrictMath.sin((StrictMath.PI/2)*(4*j*x - n))/StrictMath.pow(j,n+1);
			j--;
		} while (j>0);
		
		f *= (4*s*factorial(n))/StrictMath.pow(2*StrictMath.PI, n+1);
		f += g;
		
		return f;
	}
	
	/**
	 * Euler totient function
	 * ref:
	 * http://functions.wolfram.com/13.06.02.0001.01
	 * @param n
	 * @return number of integers not exceeding n and (m,n)=1
	 */
	public static int EulerPhi(int n)
	{
		int s=0;
		for (int k=1; k<=n; k++)
		{
			s += kdelta(gcd(n,k), 1);
		}
				
		return s;
	}
	
	/**
	 * Lambert W(z) funtion - implemented using Lagrange Inversion Theorem
	 * for |z| < 1/e
	 * @param z
	 * @return
	 */
	public static double LambertW1e(double z)
	{
		double S = 0.0;
		for (int k=1; k <= INFINITY; k++)
		{
			S += 
				(StrictMath.pow(-k, k-1) * StrictMath.pow(z,k))
						/ factorial(k);
		}
		return S;
	}
	
	/**
	 * Lambert W(z) function - Series approximation
	 * ref: implemented from Python code found somewhere in the web
	 * @param z
	 * @return
	 */
	public static double LambertW(double z)
	{
		double S = 0.0;
		for (int n=1; n <= 100; n++)
		{
			double Se = S * StrictMath.pow(StrictMath.E, S);
			double S1e = (S+1) * 
				StrictMath.pow(StrictMath.E, S);
			if (PRECISION > StrictMath.abs((z-Se)/S1e))
			{
				return S;
			}
			S -= 
				(Se-z) / (S1e - (S+2) * (Se-z) / (2*S+2));
		}
		return S;
	}
	
	/**
	 * return the generalized nth Laguerre polynomial
	 * ref: Spanier & Oldham
	 * @param n
	 * @param m 	// zero will return regular Laguerre polynomial
	 * @param x
	 * @return
	 */
	public static double Lp(int n, double m, double x)
	{
		double h, f, j;
		h = f = j = 1;
		if (n == 0)
			return f;
		f = 1 + m - x;
		if (n == 1)
			return f;
		do {
			j++;
			double g = f;
			f = ((2*j+m - 1 - x)*g - (j+m - 1)*h)/j;
			h = g;
		} while (j<n);
		
		return f;
	}
	
	/**
	 * return the nth Laguerre polynomial
	 * 
	 * @param n
	 * @param x
	 * @return
	 */
	public static double Lp(int n, double x)
	{
		return Lp(n, 0, x);
	}
	
	
	
	/**
	 * return the nth Pochhammer symbol (rising factorial)
	 * @param n
	 * @param x
	 * @return
	 */
	public static double P(int n, double x)
	{
		return Gamma(n+x)/Gamma(x);
	}
	
	/**
	 * falling factorial 
	 * @param n
	 * @param x
	 * @return
	 */
	public static double Pf(int n, int x)
	{
		return factorial(x)/factorial(x-n);
	}
	
	
	/**
	 * Dirichlet eta fuction (aka Zeta alternating series)
	 * ref: Weisstein, Eric W. "Dirichlet Eta Function." 
	 * From MathWorld--A Wolfram Web Resource. 
	 * http://mathworld.wolfram.com/DirichletEtaFunction.html 
	 * @param s
	 * @return
	 */
	public static double eta(double s)
	{
		// return precomputed values
		if (s == 0)
			return .5;
		else if (s == 1)
			return logtwo;
		else if (s == 2)
			return pi2on12;
		else if (s > 125)
			// any value > 125 converges to 1
			return 1.0;
		
		double result = 0.0;
		for (int k=1; k<INFINITY; k++)
		{
			result += StrictMath.pow(-1, k-1)/StrictMath.pow(k, s);
		}
		
		return result;
		
	}
	
	/**
	 * Chebyshev polynomial of the 1st kind
	 * ref: http://functions.wolfram.com/05.04.02.0001.01
	 * @param n
	 * @param z
	 * @return
	 */
	public static double Tn(int n, double z)
	{
		if (n == 0)
			return 1;
		else if (n == 1)
			return z;
		
		if (z == 0)
			if (even(n) == 1)
				return StrictMath.pow(-1, n);
			else
				return 0.0;
		
		double K = kdelta(n, 0)/2. + n/2.;
		double S = 0.0;
		
		double n2 = StrictMath.floor(n/2.);
		for (int k=1; k<=n2; k++)
		{
			S += ((StrictMath.pow(-1, k)*factorial(n-k-1)*StrictMath.pow(2*z, n-2*k))/
						factorial(k)*factorial(n-2*k)) + 
							StrictMath.pow(2, n-1)*StrictMath.pow(z, n);
		}
		
		return K*S;
	}
	
	/**
	 * Chebyshev polynomial of the 2nd kind
	 * ref: http://functions.wolfram.com/05.05.02.0001.01
	 * @param n
	 * @param z
	 * @return
	 */
	public static double Un(int n, double z)
	{
		if (n == 0)
			return 1;
		else if (n == 1)
			return z*2;
		
		if (z == 0)
			if (even(n) == 1)
				return StrictMath.pow(-1, n);
			else
				return 0.0;
		
		double S = 0.0;
		
		double n2 = StrictMath.floor(n/2.);
		for (int k=1; k<=n2; k++)
		{
			S += ((StrictMath.pow(-1, k)*factorial(n-k-1)*StrictMath.pow(2*z, n-2*k))/
						factorial(k)*factorial(n-2*k));
		}
		
		return S;
	}
	
	/**
	 * Chebyshev polynomial of the 1st kind
	 * ref: Spanier & Oldham (??)
	 * @param n
	 * @param x
	 * @return
	 */
	public static double ChebyshevT(int n, double x)
	{
		int j = 0;
		double f,g,h;
		
		f = g = x;	/* 2x fo Un(x) */
		if (n == 1)
			return f;
		
		f = h = j = 1;
		if (n == 0)
			return f;
		
		do {
			j++;
			f = 2*x*g - h;
			if (j == n)
				break;
			h = g;
			g = f;
		
		} while (1==1);
		
		return f;
	}
	
	/**
	 * Chebyshev polynomial of the 2nd kind
	 * ref: Spanier & Oldham (??)
	 * @param n
	 * @param x
	 * @return
	 */
	public static double ChebyshevU(int n, double x)
	{
		int j = 0;
		double f,g,h;
		
		f = g = 2*x;	/* 2x fo Un(x) */
		if (n == 1)
			return f;
		
		f = h = j = 1;
		if (n == 0)
			return f;
		
		do {
			j++;
			f = 2*x*g - h;
			if (j == n)
				break;
			h = g;
			g = f;
		
		} while (1==1);
		
		return f;
	}
	
	/**
	 * @param n
	 * @return
	 */
	public static double StieltjesGp(int n)
	{
		// lim m->infinity
		int m = 101;
		
		double sum = 0.0;
		for (int k=1; k<=m; k++)
		{
			sum += (StrictMath.pow(-1, k)/k) * 
						functions.BernoulliP(
								n+1,(StrictMath.log(k)/StrictMath.log(2)));
		}
		
		return (StrictMath.pow(StrictMath.log(2),n)/n+1) * sum;
	}
	
	/**
	 * @param n
	 * @return
	 */
	public static double StieltjesG(int n)
	{
		// lim m->infinity
		int m = 101;
		
		double sum = 0.0;
		double t2 = (StrictMath.pow(StrictMath.log(m),n+1)/n+1);
		for (int k=1; k<=m; k++)
		{
			sum += (StrictMath.pow(StrictMath.log(k), n)/k);
		}
		
		return  (StrictMath.pow(-1,n)/factorial(n)) * (sum - t2);
	}

	/**
	 * diGamma Psi asymptotic approximation using Bernoulli numbers
	 * @param x
	 * @return
	 */
	public static double PolyGamma(double x)
	{
		double sum = 0.0;
		
		// special values - for x=1, return -EM constant
		if (x == 1.0)
			return -1*constants.EM;
		
		for (int n=1; n<10; n++)
		{
			sum += functions.BernoulliB(2*n)/(2*n*(StrictMath.pow(x, 2*n)));
		}
		
		return (StrictMath.log(x)) - (1.0/(2.0*x)) - sum;
	}
	
	/**
	 * diGamma Psi
	 * ref: Spanier & Oldham
	 * @param x
	 * @return psi(x)
	 */
	public static double PolyGammaP(double x)
	{
		double f = StrictMath.pow(10, 99);
		double g = 0.0;
		
		if (x == 0.0)
			return f;
		
		do 
		{
			g += 1/x;
			x++;
		} while(x<5);
		
		f = 1.0 + ((((0.46/StrictMath.pow(x, 2))-1)))/(10*StrictMath.pow(x, 2));
		f = -((f/(6*x)+1)/(2*x)) + StrictMath.log(x) - g;
		
		return f;
	}
	
	/**
	 * PolyGamma Psi
	 * ref: Spanier & Oldham
	 * @param n
	 * @param x
	 * @return
	 */
	public static double PolyGamma(int n, double x)
	{
		double f = StrictMath.pow(10, 99);
		double g = 0.0;
		double h = 0.0;
		int j = 0;
		
		if (x == 0.0)
			return f;
		
		do {
			h = -1/x;
			j = n;
			
			do {
				h = -(h*j)/x;
				j--;
			} while (j != 0);
			
			g += h;
			x += 1;
			
		} while (x<5);
		
		f = (1 - ((n+4)*(n+5))/(45.0*StrictMath.pow(x, 2)))/(60.0*StrictMath.pow(x, 2));
		f = ((n+1)*(1-(f*(n+2)*(n+3))))/(6*x);
		f = -((1/n)+((f+1)/(2*x)));
		
		j = n;
		
		do {
			f = (-f*j)/x;
			j--;
		} while(j != 0);
		
		f = f+g;
		
		return f;
	}
	
	/**
	 * PolyGamma Psi
	 * ref: http://functions.wolfram.com/06.15.02.0002.02
	 * @param n (N+)
	 * @param z
	 * @return
	 */
	public static double PolyGamma2(int n, double z)
	{
		double t1 = StrictMath.pow(-1, n+1);
		double t2 = factorial(n);
		double sum = 0.0;
		
		//TODO
		for (int k=0; k<INFINITY; k++)
		{
			sum += 1/StrictMath.pow(k+z,n+1);
		}
		
		return t1*t2*sum;
	}
	
	/**
	 * simple algorithm for generating an arbitrary polynomial of degree n
	 * that does not vanish at -1
	 * ref: "An Efficient Algorithm for the Riemann Zeta Function", P. Borwein
	 * @param n
	 * @param x
	 * @return
	 */
	public static double Pn(int n, double x) {
		
		return StrictMath.pow(x, n) * StrictMath.pow(1-x, n);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//double x = Double.parseDouble(args[0]);
		int n = 1;
		try {
			n = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			
		}
		for (int i=1; i<=n; i++) {
			
			System.out.println(
					"moebiusmu for " + i + "=" + functions.moebiusmu(i));
		}
//		for (int i=0; i<100; i++) {
//			int n;
//			try {
//				n = i;
//			} catch (NumberFormatException e) {
//				break;
//			}
//			System.out.println(
//					"exp(lambda(n)) for " + i + "=" 
//						+ StrictMath.round(StrictMath.exp(functions.mangoldtLambda(n))));
//		}
//		for (int i=0; i<100; i++) {
//			int n = i;
//			
//			System.out.println(
//					"psi for " + i + "=" + functions.psi(n));
//		}
		
		//System.out.println("e^pi: " + StrictMath.pow(StrictMath.E, StrictMath.PI));
		
		// test lambda for the first 95 values
//		for (int i=0; i<96; i++) {
//			System.out.println("lambda(" + i + "): " + mangoldtLambda(i));
//		}
		
		// test psi for first 10 values
		//for (int i=0; i<10; i++) {
		//	System.out.println("psi(" + i + "): " + psi(i));
		//}
		
		// test mobius mu for the first 20 values
//		for (int i=0; i<40; i++) {
//			System.out.println("mobiusmu(" + i + "): " + moebiusmu(i));
//		}
		
		// test mertens M for some large n
		//int n = Integer.parseInt(args[0]);
//		for (int n=0; n<82; n++) {
//			System.out.println("M(" + n + "):" + M(n));
//		}
//		int n = c
//		System.out.println("factorial(" + n + "): " + factorial(n));
//		
//		int N = Integer.parseInt(args[0]);
//		double s = Double.parseDouble(args[1]);
//		System.out.println("PI(" + s + "," + N + "): " + PI(s,N));
		
		/*
		int n = 50;
		for (int i=0; i<=n; i++)
		{
			if (even(i) == 1)
			{	
				System.out.print("BernoulliB(" + i + "):" + BernoulliB(i));
				System.out.print("\n");
			}
		}
		*/
		//double x = Double.parseDouble(args[1]);
		//System.out.print("BernoulliP(" + n + ", " + x + "):" + BernoulliP(n, x));
		
		// test Lambert W function
//		double z = Double.parseDouble(args[0]);
//		System.out.println("LambertW(" + z + "): " + LambertW(z));
		
		// try a gram point approximation using the Lambert W function
//		double gn = 2*StrictMath.PI*StrictMath.exp(1+LambertW((8*z+1)/8*StrictMath.E));
//		System.out.println("gram point ~~ " + gn);
		
		// test beta function
		//System.out.println("beta(2,4): " + beta(2.,4.));
		
		// test erf,erfc
		//System.out.println("erf(.15): " + erf(.15));
		//System.out.println("erfc(.15): " + erfc(.15));
		//System.out.println("erf+erfc(.15): " + (erf(.15)+erfc(.15)));
		
		// test gamma
		/*
		System.out.println("Gamma(.5): " + Gamma(.5));
		System.out.println("Gamma(0.6): " + Gamma(0.6));
		System.out.println("Gamma(7.4): " + Gamma(7.4));
		System.out.println("Gamma(-4.2): " + Gamma(-4.2));
		*/
		// test inc gamma
		/*
		System.out.println("incGamma(.5,4.84): " + incGamma(.5,4.84));
		System.out.println("incGamma(.5,4.84,0): " + incGamma(.5,4.84,0));
		System.out.println("p_gamma(.5,4.84): " + p_gamma(.5,4.84,0));
		System.out.println("q_gamma(.5,4.84): " + q_gamma(.5,4.84,0));
		
		System.out.println("incGamma2(.5,-0.49): " + incGamma2(.5,-0.49));
		System.out.println("incGamma2(-3.9,0): " + incGamma2(-3.9,0));
		System.out.println("incGamma2(1,Pi): " + incGamma2(1,StrictMath.PI));
		*/
		// test inc gamma
		//System.out.println("incGamma(.5,4.84,Gamma(.5)): " + incGamma(.5,4.84,Gamma(.5)));
		
		// test reg inc gamma
		//System.out.println("regIncGamma(.5,0,1): " + regIncGamma(.5,0,1));
		
		// test first n laguerre polynomials
		//double x = Double.parseDouble(args[0]);
		//for (int i=0; i<11; i++) {
		//	int n = i;
			/*
			System.out.println(
					"L" + 0 + "(" + 2 + ")=" + functions.Lp(0,2));
			System.out.println(
					"L" + 1 + "(" + 9 + ")=" + functions.Lp(1,9));
			System.out.println(
					"L" + 6 + "(" + 8.5 + ")=" + functions.Lp(6,8.5));
			System.out.println(
					"L" + 3 + "(2)(" + 1 + ")=" + functions.Lp(3,2,1));
					*/
		//}
			
		// test pochhammer
		//System.out.println("P" + 1 + "(" + .5 + ")=" + functions.P(1,.5));
		
		// test eta function 0-42
		/*
		for (double s = 0; s<43; s++)
		{
			System.out.println("eta("+ s + "): " + eta(s));
		}
		*/
		// test Euler number & polynomials
		//double x = 0.25;
		//for (n = 1; n<16; n++)
		//{
			//System.out.print("\nEulerE(" + n + "):" + EulerE(n));
		//	System.out.print("\nEulerP(" + n + ", " + x + "):" + EulerP(n, x));
		//	System.out.print("\n(-1)^" + n + " * EulerP(" + n + "," + (1-x) + "):" + 
		//			StrictMath.pow(-1,n)*EulerP(n, 1-x));
		//}
		
		// more Euler polynomial tests - Spanier & Oldham
		/*
		System.out.print("\nEulerP(" + 6 + ", " + 0.4 + "):" + EulerP(6, 0.4));
		System.out.print("\nEulerP(" + 13 + ", " + 1 + "):" + EulerP(13, 1.));
		System.out.print("\nEulerP(" + 8 + ", " + -1.5 + "):" + EulerP(8, -1.5));
		System.out.print("\nEulerP(" + 9 + ", " + 3.14159 + "...):" + EulerP(9, StrictMath.PI));
		*/
		
		// test Chebyshev polynomials
		/*
		double z = .2;
		for (int n = 0; n<13; n++)
		{
			//System.out.print("\nTn(" + n + ", " + z + "):" + Tn(n, z));
			System.out.print("\nChebyshevT(" + n + ", " + z + "):" + ChebyshevT(n, z));
			//System.out.print("\nUn(" + n + ", " + z + "):" + Un(n, z));
			System.out.print("\nChebyshevU(" + n + ", " + z + "):" + ChebyshevU(n, z));
		}
		
		System.out.print("\nChebyshevU(" + 5 + ", " + 0.5 + "):" + ChebyshevU(5, 0.5));
		System.out.print("\nChebyshevU(" + 11 + ", " + -0.6 + "):" + ChebyshevU(11, -0.6));
		*/
		// test Stieltjes constants 0-43
		/*
		for (int n = 0; n<10; n++)
		{
			System.out.print("\nStieltjesG("+ n + "): " + StieltjesG(n));
		}
		*/
		// test binomial
		//System.out.println("\n(n k): 5,3: " + Binomial(5,3));
		
		// test PI
		//System.out.println("5!: " + factorial(5));
		//System.out.println("PI(5):" + PI(5,100));
		//System.out.println("PI(5.):" + PI(5.));
		
		//System.out.println("0!=" + factorial(0));
		// test bigOmega
		//System.out.println("factors of 2008:" + bigOmega(2008));
		
		// test Liouville function 1-40
		/*
		for (long l = 1; l<41; l++)
		{
			System.out.println("LLambda(" + l + "): " + LiouvilleLambda(l));
		}
		// test L(n) function 1-80
		for (int l = 1; l<81; l++)
		{
			System.out.println("L(" + l + "): " + L(l));
		}
		*/
		// test polygamma
		/*
		for (double x = 1; x<10; x++)
		{
			System.out.println("PolyGamma(" + x + "): " + PolyGamma(x));
		}
		System.out.println("PolyGamma(" + .5 + "): " + PolyGamma(.5));
		
		for (int x = 1; x<10; x++)
		{
			System.out.println("PolyGammaP(" + x + "): " + PolyGammaP(x));
		}
		System.out.println("PolyGammaP(" + .5 + "): " + PolyGammaP(.5));
		System.out.println("PolyGammaP(" + -1.05 + "): " + PolyGammaP(-1.05));
		System.out.println("PolyGammaP(" + 1.461632145 + "): " + PolyGammaP(1.461632145));
		
		// test PolyGamma with derivatives
		System.out.println("PolyGamma(1,1): " + PolyGamma(1,1));
		System.out.println("PolyGamma(1,5.5): " + PolyGamma(1,5.5));
		System.out.println("PolyGamma(2,-0.5): " + PolyGamma(2,-0.5));
		System.out.println("PolyGamma(2,5): " + PolyGamma(2,5));
		System.out.println("PolyGamma(3,9): " + PolyGamma(3,9));
		
		System.out.println("PolyGamma2(1,1): " + PolyGamma2(1,1));
		System.out.println("PolyGamma2(1,5.5): " + PolyGamma2(1,5.5));
		System.out.println("PolyGamma2(2,-0.5): " + PolyGamma2(2,-0.5));
		System.out.println("PolyGamma2(2,5): " + PolyGamma2(2,5));
		System.out.println("PolyGamma2(3,9): " + PolyGamma2(3,9));
		*/
		
		// test stirling
//		for (int n=0; n <= 9; n++)
//		{
//			for (int k=0; k <= 9; k++)
//			{
//				System.out.println("Stirling(" + n + "," + k +") : " + Stirling1K(n,k));
//			}
//		}
	}

}
