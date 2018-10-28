package fr.an.math4j.arith;

public class DoubleDouble
{
    final double hi;
    final double lo;

    static class DoubleDoubleMutable { 
        double hi;
        double lo;
        public DoubleDoubleMutable(double hi, double lo) {
    		this.hi = hi;
    		this.lo = lo;
        }
    }
    
//	public DoubleDouble(double x)  {
//        split( x, hi, lo );
//    }
    
	public DoubleDouble(double hi, double lo) {
		this.hi = hi;
		this.lo = lo;
    }

    
//    public DoubleDouble operator+( const DoubleDouble& rhs ) const
//    {
//        DoubleDouble ret;
//        TwoSum( hi, rhs.hi, ret.hi, ret.lo );
//        ret.lo = ret.lo + lo + rhs.lo;
//        FastTwoSum( ret.hi, ret.lo, ret.hi, ret.lo );
//        return ret;
//    }
//
//    DoubleDouble operator-( const DoubleDouble& rhs ) const
//    {
//        return *this + DoubleDouble(-rhs.hi,-rhs.lo);
//    }
//
//    DoubleDouble operator*( const DoubleDouble& rhs ) const
//    {
//        DoubleDouble ret;
//        TwoProduct( hi, rhs.hi, ret.hi, ret.lo );
//        ret.lo = ret.lo + hi*rhs.lo + lo*rhs.hi;
//        FastTwoSum( ret.hi, ret.lo, ret.hi, ret.lo );
//        return ret;
//    }
//
//    public DoubleDouble divide(DoubleDouble rhs )  {
//        DoubleDouble tmp, ret;
//        double xr = hi?lo/hi:0.0, yr = 1.0/rhs.hi;
//        ret.hi = hi * yr;
//        TwoProduct( ret.hi, rhs.hi, tmp.hi, tmp.lo );
//        ret.lo = ((hi-tmp.hi)-tmp.lo)*yr + ret.hi*(xr - rhs.lo*yr);
//        FastTwoSum( ret.hi, ret.lo, ret.hi, ret.lo );
//        return ret;
//    }
//
//    public DoubleDouble Pow( int exp )
//    {
//        DoubleDouble ret = 1.0, base = *this;
//        while ( exp ) {
//            if ( exp & 1 ) {
//                ret = ret * base;
//            }
//            base = base * base;
//            exp >>= 1;
//        }
//        return ret;
//    }
//
//
//private:
//
//    private static void FastTwoSum( double x, double y, double& s, double& e )
//    {
//        s = x + y;
//        e = y - (s-x);
//    }
//
//    private static void TwoSum( double x, double y, double& s, double& e )
//    {
//        s = x + y;
//        double v = s - x;
//        e = (x-(s-v)) + (y-v);
//    }
//
//    private static void Split( double x, double& h, double& l )
//    {
//        double t = 134217729 * x;
//        h = t - (t-x);
//        l = x - h;
//    }
//
//    private static void TwoProduct( double x, double y, double& p, double& e )
//    {
//        p = x * y;
//        double xh, xl, yh, yl;
//        Split( x, xh, xl );
//        Split( y, yh, yl );
//        e = ((xh*yh-p) + xh*yl + xl*yh) + xl*yl;
//    }
//

}

