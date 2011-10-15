package org.neuronbucket.kv;

import java.io.IOException;
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
		} finally {
			mLock.readLock().unlock();
		}
	}

	protected abstract void doRemove(K key) throws IOException;

	protected void checkClosed() throws IOException {
		if (mParent.isClosed()) {
			throw new StoreClosedException();
		}
	}
}
