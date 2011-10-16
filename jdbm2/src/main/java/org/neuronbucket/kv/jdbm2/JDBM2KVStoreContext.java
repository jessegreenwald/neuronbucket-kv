package org.neuronbucket.kv.jdbm2;

import java.io.IOException;
import java.util.concurrent.locks.ReadWriteLock;

import jdbm.PrimaryTreeMap;

import org.neuronbucket.kv.AbstractKVStoreContext;
import org.neuronbucket.kv.util.Transformer;

public class JDBM2KVStoreContext<K, V> extends AbstractKVStoreContext<K, V> {

	private PrimaryTreeMap<Comparable<?>, byte[]> mTreeMap;
	private Transformer<K, Comparable<?>> mKeyTransformer;
	private Transformer<V, byte[]> mValueTransformer;

	public JDBM2KVStoreContext(JDBM2KVStore<K, V> parent, ReadWriteLock lock, PrimaryTreeMap<Comparable<?>, byte[]> treeMap,
			Transformer<K, Comparable<?>> keyTransformer, Transformer<V, byte[]> valueTransformer) {
		super(parent, lock);
		mTreeMap = treeMap;
		mKeyTransformer = keyTransformer;
		mValueTransformer = valueTransformer;
	}

	@Override
	protected V doGet(K key) throws IOException {
		byte[] rawVal = (byte[]) mTreeMap.get(key);
		return mValueTransformer.transformFrom(rawVal);
	}

	@Override
	protected void doPut(K key, V value) throws IOException {
		Comparable<?> transformedKey = mKeyTransformer.transformTo(key);
		byte[] rawVal = mValueTransformer.transformTo(value);
		mTreeMap.put(transformedKey, rawVal);
	}

	@Override
	protected void doRemove(K key) throws IOException {
		mTreeMap.remove(key);
	}

}
