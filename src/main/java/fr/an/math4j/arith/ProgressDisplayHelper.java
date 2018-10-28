package fr.an.math4j.arith;

public class ProgressDisplayHelper {

	// frequency to print "." progression
	private int progressFreq;
	private int currProgressModulo;
	
	// frequency to print intermediate values
	private int displayFreq;
	private int currDisplayModulo;
	
	private long globalStartTimeMillis;
	private long totalCount;
	
	private long prevDisplayTimeMillis;
	private long prevDisplayCount;
	
	private long currentCount;
	
	
	public ProgressDisplayHelper(int progressFreq, int displayFreq) {
		this.progressFreq = progressFreq;
		this.displayFreq = displayFreq;
		this.currProgressModulo = progressFreq;
		this.currDisplayModulo = displayFreq;
	}

	public void skipSetCount(long currentCount) {
		this.currentCount = currentCount;
		this.prevDisplayTimeMillis = System.currentTimeMillis();
	}
	
	public void start(long totalCount) {
		globalStartTimeMillis = System.currentTimeMillis();
		prevDisplayTimeMillis = System.currentTimeMillis();
		setTotalCount(totalCount);
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public String incr() {
		String res = null;
		this.currentCount++;
		
		if (--currProgressModulo <= 0) {
			currProgressModulo = progressFreq;
			System.out.print(".");
		}
		
		if (--currDisplayModulo <= 0) {
			currDisplayModulo = displayFreq;
			System.out.println();
			
			long timeMillis = System.currentTimeMillis();
			long globalMillis = timeMillis - globalStartTimeMillis;
			double globalCountPerSec = 1000*safeDiv(currentCount, globalMillis);
			long stepCount = currentCount - prevDisplayCount;
			long stepMillis = timeMillis - prevDisplayTimeMillis;
			double currCountPerSec = 1000*safeDiv(stepCount, stepMillis);
			long remainCount = totalCount - currentCount;
			long expectedRemainTimeMillis = (long) (1000 * safeDiv(remainCount, globalCountPerSec));
			
			res = " step " + millisToText(stepMillis) + " at " + speedToText(currCountPerSec)
					+ ", total " + millisToText(globalMillis) + " at " + speedToText(globalCountPerSec)
					+ ", expecting end: " + millisToText(expectedRemainTimeMillis)
					;

			prevDisplayTimeMillis = timeMillis;
			prevDisplayCount = currentCount;
		}

		return res;
	}

	public void incrPrint() {
		String msg = incr();
		if (msg != null) {
			System.out.println(msg);
		}
	}
	
	private static double safeDiv(long a, long b) {
		return (double) a / (b>0? b : 1);
	}
	
	private static double safeDiv(double a, double b) {
		return (double) a / (b>0? b : 1);
	}
	
	static String millisToText(long millis) {
		if (millis < 1000) {
			return millis + "ms";
		} else {
			int seconds = (int) (millis / 1000);
			if (seconds < 60) { // less than 1mn.. show "X s"
				return String.format("%1$.1fs", (double)millis/1000);
			} else {
				int minutes = seconds / 60;
				seconds = seconds - 60 * minutes;
				if (minutes < 60) { // less than 1 hour.. show X mn Y s 
					return String.format("%1dmn%2$02ds", minutes, seconds);
				} else { // show <H>h<mm>
					int hours = minutes / 60;
					minutes = minutes - hours * 60;
					return String.format("%1d:%2$02dh", hours, minutes);
				}
			}
		}
	}
	
	private static String speedToText(double speed) {
		String unit = "";
		if (speed > 1_000_000) {
			unit = "M";
			speed /= 1_000_000;
		} else if (speed > 1000) {
			unit = "k";
			speed /= 1000;
		} else {
			unit = "";
		}
		return String.format("%1$.1f%2$s/s", speed, unit);
	}
	
}
