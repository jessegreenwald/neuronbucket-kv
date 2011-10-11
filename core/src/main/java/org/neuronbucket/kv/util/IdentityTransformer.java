package org.neuronbucket.kv.util;


public class IdentityTransformer<T> implements Transformer<T, T> {

	public T transformTo(T t) {
		return t;
	}

	public T transformFrom(T v) {
		return v;
	}
	
}