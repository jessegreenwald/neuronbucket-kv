package org.neuronbucket.kv.jdbm2;

import java.io.File;
import java.io.IOException;

import org.neuronbucket.kv.KVStoreTest;
import org.neuronbucket.kv.util.Factory;
import org.neuronbucket.kv.util.IdentityTransformer;
import org.neuronbucket.kv.util.SingletonFactory;
import org.neuronbucket.kv.util.Transformer;

public class JDBM2StoreTest extends KVStoreTest<JDBM2KVStore<String, byte[]>> {

	private static final String RECORD_MANAGER_NAME = "test";
	@Override
	public JDBM2KVStore<String, byte[]> createStore() throws IOException {
		Transformer<String, Comparable<?>> keyTransformer = new Transformer<String, Comparable<?>>() {
			public Comparable<?> transformTo(String t) {
				return t;
			}
			public String transformFrom(Comparable<?> v) {
				return v.toString();
			}
		};
		Factory<Transformer<String, Comparable<?>>> keyTransformerFactory =
				new SingletonFactory<Transformer<String, Comparable<?>>>(keyTransformer);
		Factory<Transformer<byte[], byte[]>> valueTransformerFactory = 
				new SingletonFactory<Transformer<byte[], byte[]>>(new IdentityTransformer<byte[]>());
		return new JDBM2KVStore<String, byte[]>(
				keyTransformerFactory,
				valueTransformerFactory,
				RECORD_MANAGER_NAME, "test");
	}

	@Override
	public void destroyStore(JDBM2KVStore<String, byte[]> store) throws IOException {
		if (!store.isClosed()) {
			store.close();
		}
		for (File child : new File(".").listFiles()) {
			if (child.getName().startsWith(RECORD_MANAGER_NAME + ".")) {
				child.delete();
			}
		}
	}

}
