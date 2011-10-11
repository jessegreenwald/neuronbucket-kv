package org.neuronbucket.kv;

import java.io.IOException;

public interface KVStoreContext<K, V> {

	public V get(K key) throws IOException;

    public void put(K key, V value) throws IOException;

    public void remove(K key);

}
