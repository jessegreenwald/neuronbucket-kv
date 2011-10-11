package org.neuronbucket.kv.util;

public interface Factory<T> {

	public T newInstance();

}