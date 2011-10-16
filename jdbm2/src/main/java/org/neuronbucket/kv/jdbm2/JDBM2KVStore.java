package org.neuronbucket.kv.jdbm2;

import java.io.IOException;

import jdbm.PrimaryTreeMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

import org.neuronbucket.kv.AbstractKVStore;
import org.neuronbucket.kv.KVStoreContext;
import org.neuronbucket.kv.util.Factory;
import org.neuronbucket.kv.util.Transformer;

public class JDBM2KVStore<K, V> extends AbstractKVStore<K, V> {

	private class Flusher implements Runnable {

		public void run() {
			while(true) {
				try {
					mLock.readLock().lock();
					try {
						if (!isClosed()) {
							flush();
						}
					} finally {
						mLock.readLock().unlock();
					}
					Thread.sleep(1000);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					return;
				}
			}
		}

	}

	PrimaryTreeMap<Comparable<?>, byte[]> mTreeMap;
	private Factory<Transformer<K, Comparable<?>>> mKeyFactory;
	private Factory<Transformer<V, byte[]>> mValueFactory;
	private RecordManager mRecMan;
	private Thread mFlusherThread;

	public JDBM2KVStore(
			Factory<Transformer<K, Comparable<?>>> keyFactory,
			Factory<Transformer<V, byte[]>> valueFactory,
			String recordManagerName,
			String treeMapName) throws IOException {
		mKeyFactory = keyFactory;
		mValueFactory = valueFactory;
		mRecMan = RecordManagerFactory.createRecordManager(recordManagerName);
		mTreeMap = mRecMan.treeMap(treeMapName);
		mFlusherThread = new Thread(new Flusher());
		mFlusherThread.start();
	}

	@Override
	protected KVStoreContext<K, V> doNewContext() {
		return new JDBM2KVStoreContext<K, V>(
				this, mLock, mTreeMap, mKeyFactory.newInstance(), mValueFactory.newInstance());
	}

	public void close() throws IOException {
		super.close();
		mFlusherThread.interrupt();
		try {
			mFlusherThread.join();
		} catch (InterruptedException e) {
			throw new IOException(e);
		}
		mRecMan.close();
	}

	public void flush() throws IOException {
		// TODO: call this every so often so we don't run out of memory.
		mLock.readLock().lock();
		try {
			checkClosed();
			mRecMan.commit();
		} finally {
			mLock.readLock().unlock();
		}
	}

}
