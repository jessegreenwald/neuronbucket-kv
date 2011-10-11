package org.neuronbucket.kv;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public abstract class KVStoreTest<T extends KVStore<String, byte[]>> {

	protected T mStore;

	@Before
	public void initStore() {
		mStore = createStore();
	}

	public abstract T createStore();

	@After
	public void destroyStore() {
		destroyStore(mStore);
	}

	public abstract void destroyStore(T store);

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
}
