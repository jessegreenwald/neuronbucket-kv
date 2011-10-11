package org.neuronbucket.kv.util;

public interface Transformer<T, V> {

	public V transformTo(T t);

	public T transformFrom(V v);

}
