package fr.an.math4j.arith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ExecutorServiceHelper {

	private ExecutorService delegate;

	public ExecutorServiceHelper(ExecutorService delegate) {
		this.delegate = delegate;
	}

	public void shutdown() {
		delegate.shutdown();
	}

	public void awaitTermination(long timeout, TimeUnit unit) {
		try {
			delegate.awaitTermination(timeout, unit);
		} catch (InterruptedException ex) {
			throw new RuntimeException(ex);
		}
	}

	public ExecutorService getDelegate() {
		return delegate; 
	}
	
	public <T> Future<T> submit(Callable<T> task) {
		return delegate.submit(task);
	}

	public Future<?> submit(Runnable task) {
		return delegate.submit(task);
	}
	
	public <T> List<T> submitWaitAll(List<Callable<T>> tasks, int showProgressFreq) {
		List<Future<T>> futures = submitAll(tasks);
		return waitAll(futures, showProgressFreq);
	}

	public <T> List<Future<T>> submitAll(List<Callable<T>> tasks) {
		List<Future<T>> futures = new ArrayList<>();
		for(Callable<T> task : tasks) {
			futures.add(delegate.submit(task));
		}
		return futures;
	}
	
	public void waitAllV(List<Future<?>> futures, int showProgressFreq) {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		List<Future<Void>> futureVoids = (List) futures;
		waitAll(futureVoids, showProgressFreq);
	}
	
	public <T> List<T> waitAll(List<Future<T>> futures, int showProgressFreq) {
		List<T> res = new ArrayList<>();
		int progressCount = showProgressFreq;
		final int futuresCount = futures.size();
		for(int i = 0; i < futuresCount; i++) {
			Future<T> f = futures.get(i);
			T resElt;
			try {
				resElt = f.get();
			} catch (InterruptedException ex) {
				throw new RuntimeException(ex);
			} catch (ExecutionException ex) {
				throw new RuntimeException(ex.getCause());
			}
			if (showProgressFreq >= 1) {
				progressCount--;
				if (progressCount <= 0) {
					progressCount = showProgressFreq;
					System.out.print(".");
				}
			}
			res.add(resElt);
		}
		return res;
	}
	
	@FunctionalInterface
	public static interface RangeCalc<T> {
		T calc(Object ctx, long from, long to);
	}
	@FunctionalInterface
	public static interface ReduceFunc<T> {
		T reduce(Object ctx, T sum, T elt);
	}
	
	public <T> T parallelRangeBatchReduce(Object ctx, long from, long to, RangeCalc<T> calcFunc,
			T initRes, ReduceFunc<T> reduceFunc,
			long batchSize) {
		// split batches and submit all
		int batchCount = (int) ((to-from) / batchSize);
		long toBatch = from + batchCount * batchSize; 
		List<Future<T>> futures = new ArrayList<>();
		for(long i = from; i < toBatch; i+=batchSize) {
			final long taskFrom = i, taskTo = i + batchSize; 
			futures.add(submit(() -> calcFunc.calc(ctx, taskFrom, taskTo)));
		}
		// collect and reduce results
		T res = initRes;
		ProgressDisplayHelper progress = new ProgressDisplayHelper(10, 100);
		progress.start(batchSize);
		for(Future<T> f : futures) {
			T resElt;
			try {
				resElt = f.get();
			} catch (InterruptedException | ExecutionException ex) {
				throw new RuntimeException("Failed", ex);
			}
			res = reduceFunc.reduce(ctx, res, resElt);
			progress.incrPrint();
		}
		return res;
	}


}
