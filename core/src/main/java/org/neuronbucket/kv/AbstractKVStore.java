package org.neuronbucket.kv;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class AbstractKVStore<K, V> implements KVStore<K, V> {

	protected ReadWriteLock mLock;
	protected AtomicBoolean mClosed = new AtomicBoolean();

	public AbstractKVStore() {
		mLock = new ReentrantReadWriteLock();
	}

	public void close() throws IOException {
		mLock.writeLock().lock();
		try {
			checkClosed();
			doClose();
			mClosed.set(true);
		} finally {
			mLock.writeLock().unlock();
		}
	}

	protected void doClose() throws IOException {
	}

	public boolean isClosed() {
		return mClosed.get();
	}

	public KVStoreContext<K, V> newContext() throws IOException {
		mLock.readLock().lock();
		try {
			checkClosed();
			return doNewContext();
		} finally {
			mLock.readLock().unlock();
		}
	}

	protected abstract KVStoreContext<K, V> doNewContext();

	protected void checkClosed() throws IOException {
		if (isClosed()) {
			throw new StoreClosedException();
		}
	}

	public void flush() throws IOException {
		mLock.readLock().lock();
		try {
			checkClosed();
			doFlush();
		} finally {
			mLock.readLock().unlock();
		}
	}

	protected void doFlush() throws IOException {
	}

}
