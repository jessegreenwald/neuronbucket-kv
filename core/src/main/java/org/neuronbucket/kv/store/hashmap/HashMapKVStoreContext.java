package org.neuronbucket.kv.store.hashmap;

import java.util.HashMap;
import java.util.concurrent.locks.ReadWriteLock;

import org.neuronbucket.kv.AbstractKVStoreContext;

class HashMapKVStoreContext<K, V> extends AbstractKVStoreContext<K, V> {

	private HashMap<K, V> mStore;

	public HashMapKVStoreContext(HashMapKVStore<K, V> parent, ReadWriteLock lock, HashMap<K, V> store) {
		super(parent, lock);
		mStore = store;
	}

	protected V doGet(K key) throws java.io.IOException {
		synchronized(mStore) {
			return mStore.get(key);
		}
	}

	protected void doPut(K key, V value) throws java.io.IOException {
		synchronized(mStore) {
			mStore.put(key, value);
		}
	}

	protected void doRemove(K key) throws java.io.IOException {
		synchronized(mStore) {
			mStore.remove(key);
		}
	}

}
