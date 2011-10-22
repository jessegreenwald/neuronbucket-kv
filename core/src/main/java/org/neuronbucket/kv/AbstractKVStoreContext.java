package org.neuronbucket.kv;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

public abstract class AbstractKVStoreContext<K, V> implements KVStoreContext<K, V> {

	protected ReadWriteLock mLock;
	protected KVStore<K, V> mParent;

	public AbstractKVStoreContext(KVStore<K, V> parent, ReadWriteLock lock) {
		mParent = parent;
		mLock = lock;
	}

	public V get(K key) throws IOException {
		mLock.readLock().lock();
		try {
			checkClosed();
			return doGet(key);
		} finally {
			mLock.readLock().unlock();
		}
	}

	protected abstract V doGet(K key) throws IOException;

	public void put(K key, V value) throws IOException {
		mLock.readLock().lock();
		try {
			checkClosed();
			doPut(key, value);
			mParent.flush();
		} finally {
			mLock.readLock().unlock();
		}
	}

	protected abstract void doPut(K key, V value) throws IOException;

	public void remove(K key) throws IOException {
		mLock.readLock().lock();
		try {
			checkClosed();
			doRemove(key);
			mParent.flush();
		} finally {
			mLock.readLock().unlock();
		}
	}

	protected abstract void doRemove(K key) throws IOException;

	public void putAll(Map<K, V> values) throws IOException {
		mLock.readLock().lock();
		try {
			checkClosed();
			doPutAll(values);
			mParent.flush();
		} finally {
			mLock.readLock().unlock();
		}
	}

	protected void doPutAll(Map<K, V> values) throws IOException {
		for (K k : values.keySet()) {
			doPut(k, values.get(k));
		}
	}

	protected void checkClosed() throws IOException {
		if (mParent.isClosed()) {
			throw new StoreClosedException();
		}
	}
}
