package org.neuronbucket.kv.store.file;

import java.io.File;

import org.neuronbucket.kv.KVStore;
import org.neuronbucket.kv.KVStoreContext;
import org.neuronbucket.kv.util.Factory;
import org.neuronbucket.kv.util.StreamTransformer;
import org.neuronbucket.kv.util.Transformer;

public class FileKVStore<K, V> implements KVStore<K, V> {

	private Factory<Transformer<K, File>> mKeyTransformer;
	private Factory<StreamTransformer<V>> mValueTransformer;

	public FileKVStore(
			Factory<Transformer<K, File>> fileNameTansformer,
			Factory<StreamTransformer<V>> valueTransformer) {
		mKeyTransformer = fileNameTansformer;
		mValueTransformer = valueTransformer;
	}

	public KVStoreContext<K, V> newContext() {
		return new FileKVStoreContext<K, V>(
				mKeyTransformer.newInstance(),
				mValueTransformer.newInstance());
	}

}
