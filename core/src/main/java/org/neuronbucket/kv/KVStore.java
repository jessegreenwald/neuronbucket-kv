package org.neuronbucket.kv;

import java.io.IOException;

public interface KVStore<K, V> {

	public KVStoreContext<K, V> newContext() throws IOException;

	public void close() throws IOException;

	public boolean isClosed();

}
