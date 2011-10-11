package org.neuronbucket.kv;


public interface KVStore<K, V> {

	public KVStoreContext<K, V> newContext();

}
