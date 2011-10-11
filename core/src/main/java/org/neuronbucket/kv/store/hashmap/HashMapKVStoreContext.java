package org.neuronbucket.kv.store.hashmap;

import java.util.HashMap;

import org.neuronbucket.kv.KVStoreContext;

class HashMapKVStoreContext<K, V> implements KVStoreContext<K, V> {

	private HashMap<K, V> mStore;

	public HashMapKVStoreContext(HashMap<K, V> store) {
		mStore = store;
	}

	public V get(K key) {
		synchronized(mStore) {
			return mStore.get(key);
		}
	}

	public void put(K key, V value) {
		synchronized(mStore) {
			mStore.put(key, value);
		}
	}

	public void remove(K key) {
		synchronized(mStore) {
			mStore.remove(key);
		}
	}
}
