package org.neuronbucket.kv;

import java.io.IOException;
import java.util.Map;

public interface KVStoreContext<K, V> {

	public V get(K key) throws IOException;

    public void put(K key, V value) throws IOException;

    public void putAll(Map<K, V> values) throws IOException;

    public void remove(K key) throws IOException;

}
