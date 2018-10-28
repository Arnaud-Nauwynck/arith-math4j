package fr.an.math4j.arith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NumberFormatUtils {

	public static String arrayToString(int[] values, int numberPerRows) {
		StringBuilder sb = new StringBuilder();
		int moreNumberPerRow = numberPerRows;
		for (int elt : values) {
			sb.append(elt);
			if (moreNumberPerRow-- < 0) {
				moreNumberPerRow = numberPerRows;
				sb.append(" \n");
			} else {
				sb.append(", ");
			}
		}
		sb.delete(sb.length()-2, sb.length());
		return sb.toString();
	}
	
	public static String arrayToString(int[] values, int firstIndex, int len, int numberPerRows) {
		StringBuilder sb = new StringBuilder();
		int moreNumberPerRow = numberPerRows;
		int lastIndex = Math.min(firstIndex + len, values.length);
		if (lastIndex > firstIndex) {
			for (int i = firstIndex; i < lastIndex; i++) {
				int elt = values[i];
				sb.append(elt);
				if (moreNumberPerRow-- < 0) {
					moreNumberPerRow = numberPerRows;
					sb.append(" \n");
				} else {
					sb.append(", ");
				}
			}
			sb.delete(sb.length()-2, sb.length());
		}
		return sb.toString();
	}
	
	public static String lsIntegerToString(Collection<Integer> values, int numberPerRows) {
		StringBuilder sb = new StringBuilder();
		if (values != null && !values.isEmpty()) {
			int moreNumberPerRow = numberPerRows;
			for (int elt : values) {
				sb.append(elt);
				if (moreNumberPerRow-- < 0) {
					moreNumberPerRow = numberPerRows;
					sb.append(" \n");
				} else {
					sb.append(", ");
				}
			}
			sb.delete(sb.length()-2, sb.length());
		}
		return sb.toString();
	}
	

	public static String arrayToString(long[] values, int numberPerRows) {
		StringBuilder sb = new StringBuilder();
		int moreNumberPerRow = numberPerRows;
		for (long elt : values) {
			sb.append(elt);
			if (moreNumberPerRow-- < 0) {
				moreNumberPerRow = numberPerRows;
				sb.append(" \n");
			} else {
				sb.append(", ");
			}
		}
		sb.delete(sb.length()-2, sb.length());
		return sb.toString();
	}
	
	public static String arrayToString(long[] values, int firstIndex, int len, int numberPerRows) {
		StringBuilder sb = new StringBuilder();
		int moreNumberPerRow = numberPerRows;
		int lastIndex = Math.min(firstIndex + len, values.length);
		if (lastIndex > firstIndex) {
			for (int i = firstIndex; i < lastIndex; i++) {
				long elt = values[i];
				sb.append(elt);
				if (moreNumberPerRow-- < 0) {
					moreNumberPerRow = numberPerRows;
					sb.append(" \n");
				} else {
					sb.append(", ");
				}
			}
			sb.delete(sb.length()-2, sb.length());
		}
		return sb.toString();
	}
	
	public static String lsLongToString(Collection<Long> values, int numberPerRows) {
		StringBuilder sb = new StringBuilder();
		if (values != null && !values.isEmpty()) {
			int moreNumberPerRow = numberPerRows;
			for (long elt : values) {
				sb.append(elt);
				if (moreNumberPerRow-- < 0) {
					moreNumberPerRow = numberPerRows;
					sb.append(" \n");
				} else {
					sb.append(", ");
				}
			}
			sb.delete(sb.length()-2, sb.length());
		}
		return sb.toString();
	}
	
	public static int[] parseIntValues(String data, String sep) {
		String[] values = data.split(sep);
		int[] res = new int[values.length];
		int i = 0;
		for (String value : values) {
			res[i++] = Integer.parseInt(value);
		}
		return res;
	}
	
	public static int[][] parseIntInt(String dataStr, String lineSep, String colSep) {
		String[] lines = dataStr.split(lineSep);
		int[][] res = new int[lines.length][];
		int row = 0;
		for(String line : lines) {
			res[row++] = NumberFormatUtils.parseIntValues(line, colSep);
		}
		return res;
	}

	public static int[][] parseIntInt(InputStream in, String lineSep, String colSep) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		List<int[]> tmpres = new ArrayList<>();
		String line;
		try {
			while(null != (line = reader.readLine())) {
				tmpres.add(NumberFormatUtils.parseIntValues(line, colSep));
			}
		} catch (IOException ex) {
			throw new RuntimeException("Failed to read int[] line", ex);
		}
		return tmpres.toArray(new int[tmpres.size()][]);
	}

	public static int[][] parseIntInt(URL url, String lineSep, String colSep) {
		try (InputStream in = url.openStream()) {
			return parseIntInt(in, lineSep, colSep);
		} catch (IOException ex) {
			throw new RuntimeException("Failed to read int[] line", ex);
		}
	}
}
