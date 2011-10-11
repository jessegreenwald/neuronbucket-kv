package org.neuronbucket.kv.store.hashmap;

import java.util.HashMap;

import org.neuronbucket.kv.KVStore;
import org.neuronbucket.kv.KVStoreContext;

public class HashMapKVStore<K, V> implements KVStore<K, V> {

	private HashMap<K, V> store;
	private HashMapKVStoreContext<K, V> mContext;

	public HashMapKVStore() {
		store = new HashMap<K, V>();
		mContext = new HashMapKVStoreContext<K, V>(store);
	}

	public V get(K key) {
		return store.get(key);
	}

	public void put(K key, V value) {
		store.put(key, value);
	}

	public KVStoreContext<K, V> newContext() {
		return mContext;
	}

}
