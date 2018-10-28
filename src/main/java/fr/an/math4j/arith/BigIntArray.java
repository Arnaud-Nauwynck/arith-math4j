package fr.an.math4j.arith;

/**
 * Large Int array (index > 2^32 ..)
 */
public final class BigIntArray {
	public static final long CHUNK_SIZE = 1024*1024*1024L;
	public static final int CHUNK_BITS = 30;
	public static final long CHUNK_MASK = (1L << CHUNK_BITS) - 1;
	
	private final long size;
	private final int chunkCount;
	private int[][] chunks;
	
	public BigIntArray(long size) {
		this.size = size;
	
		this.chunkCount = 1 + (int) (size / CHUNK_SIZE);
		this.chunks = new int[chunkCount][];  
		for (int i = 0; i < chunkCount-1; i++) {
			chunks[i] = new int[(int)CHUNK_SIZE];
		}
		chunks[chunkCount-1] = new int[(int)(size - (chunkCount-1)*CHUNK_SIZE)];
	}
	
	public long getSize() {
		return size;
	}
	
	public int get(long index) {
		int chunk = (int) (index >>> CHUNK_BITS);
		int offset = (int) (index & CHUNK_MASK);
		return chunks[chunk][offset];
	}
	
	public void set(long index, int value) {
		int chunk = (int) (index >>> CHUNK_BITS);
		int offset = (int) (index & CHUNK_MASK);
		chunks[chunk][offset] = value;
	}
	
	public void incr(long index, int incrValue) {
		int chunk = (int) (index >>> CHUNK_BITS);
		int offset = (int) (index & CHUNK_MASK);
		chunks[chunk][offset] += incrValue;
	}
}