package org.neuronbucket.kv;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public abstract class KVStoreTest<T extends KVStore<String, byte[]>> {

	protected T mStore;

	@Before
	public void initStore() throws IOException {
		mStore = createStore();
	}

	public abstract T createStore() throws IOException;

	@After
	public void destroyStore() throws IOException {
		destroyStore(mStore);
	}

	public abstract void destroyStore(T store) throws IOException;

	@Test
	public void testRemove() throws IOException {
		byte[] val = new byte[] {
				'a',
				'b',
				'c',
				'd',
		};
		mStore.newContext().put("test", val);
		byte[] val2 = mStore.newContext().get("test");
		assertTrue(Arrays.equals(val, val2));

		mStore.newContext().remove("test");
		assertTrue(mStore.newContext().get("test") == null);
	}

	@Test
	public void testPutThenGet() throws IOException {
		byte[] val = new byte[] {
				'a',
				'b',
				'c',
				'd',
		};
		mStore.newContext().put("test", val);
		byte[] val2 = mStore.newContext().get("test");
		assertTrue(Arrays.equals(val, val2));

		val = new byte[] {
				'b',
				'c',
				'd',
				'e',
		};
		mStore.newContext().put("test", val);
		val2 = mStore.newContext().get("test");
		assertTrue(Arrays.equals(val, val2));
	}

	private class MultiThread extends Thread {
		private int mStart;
		private int mFinish;
		private AtomicBoolean mError;
		private MultiThread(AtomicBoolean error, int start, int finish) {
			mError = error;
			mStart = start;
			mFinish = finish;
		}
		@Override
		public void run() {
			try {
				KVStoreContext<String, byte[]> context = mStore.newContext();
				for (int i = mStart; i < mFinish; i++) {
					byte[] val = Integer.toString(i).getBytes();
					doWork(i, val, context);
				}
			} catch (IOException e) {
				mError.set(true);
				e.printStackTrace();
			}
		}
		protected void doWork(int i, byte[] val, KVStoreContext<String, byte[]> context) throws IOException {
		}
	}

	@Test
	public void testMultiThreadedPut() throws IOException, InterruptedException {
		ArrayList<Thread> threads = new ArrayList<Thread>();
		final AtomicBoolean error = new AtomicBoolean();
		for (int i = 0; i < 10; i++) {
			MultiThread t = new MultiThread(error, i * 10, (i + 1) * 10) {
				@Override
				protected void doWork(int i, byte[] val, KVStoreContext<String, byte[]> context) throws IOException {
					context.put("" + i, val);
				}
			};
			t.start();
			threads.add(t);
		}
		for (Thread t : threads) {
			t.join();
		}
		assertTrue(!error.get());

		for (int i = 0; i < 10; i++) {
			Thread t = new MultiThread(error, i * 10, (i + 1) * 10) {
				protected void doWork(int i, byte[] val, KVStoreContext<String, byte[]> context) throws IOException {
					byte[] fetchVal = context.get("" + i);
					if (!Arrays.equals(fetchVal, val)) {
						error.set(true);
					}
				}
			};
			t.start();
			threads.add(t);
		}
		for (Thread t : threads) {
			t.join();
		}

	}

	@Test
	public void testIsClosed() throws IOException {
		assertFalse(mStore.isClosed());
		mStore.close();
		assertTrue(mStore.isClosed());
	}

	@Test
	public void testClosedClose() throws IOException {
		assertFalse(mStore.isClosed());
		mStore.close();
		assertTrue(mStore.isClosed());
		try {
			mStore.close();
			assertTrue(false);
		} catch (StoreClosedException e) {
		}
		assertTrue(mStore.isClosed());
	}

	@Test
	public void testClosedNewContext() throws IOException {
		mStore.close();
		try {
			mStore.newContext();
			assertTrue(false);
		} catch (StoreClosedException e) {
		}
	}

	@Test
	public void testClosedGet() throws IOException {
		KVStoreContext<String, byte[]> context = mStore.newContext();
		context.put("a", "a".getBytes());
		mStore.close();
		try {
			context.get("a");
			assertTrue(false);
		} catch (StoreClosedException e) {
		}
	}

	@Test
	public void testClosedPut() throws IOException {
		KVStoreContext<String, byte[]> context = mStore.newContext();
		mStore.close();
		try {
			context.put("test", new byte[0]);
			assertTrue(false);
		} catch (StoreClosedException e) {
		}
	}

	@Test
	public void testClosedRemove() throws IOException {
		KVStoreContext<String, byte[]> context = mStore.newContext();
		context.put("a", "a".getBytes());
		mStore.close();
		try {
			context.remove("a");
			assertTrue(false);
		} catch (StoreClosedException e) {
		}
	}

}
