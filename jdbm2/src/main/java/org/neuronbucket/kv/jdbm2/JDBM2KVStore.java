package org.neuronbucket.kv.jdbm2;

import java.io.IOException;

import jdbm.PrimaryTreeMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

import org.neuronbucket.kv.AbstractKVStore;
import org.neuronbucket.kv.KVStoreContext;
import org.neuronbucket.kv.util.Factory;
import org.neuronbucket.kv.util.Transformer;

public class JDBM2KVStore<K, V> extends AbstractKVStore<K, V> {

	public static <K, V> JDBM2KVStore<K, V> newInstance(
			Factory<? extends Transformer<K, Comparable<?>>> keyFactory,
			Factory<? extends Transformer<V, byte[]>> valueTransformer,
			String recordManagerName, String treeMapName) throws IOException {
		return new JDBM2KVStore<K, V>(keyFactory, valueTransformer, recordManagerName, treeMapName);
	}

	PrimaryTreeMap<Comparable<?>, byte[]> mTreeMap;
	private Factory<? extends Transformer<K, Comparable<?>>> mKeyFactory;
	private Factory<? extends Transformer<V, byte[]>> mValueFactory;
	private RecordManager mRecMan;

	private JDBM2KVStore(
			Factory<? extends Transformer<K, Comparable<?>>> keyFactory,
			Factory<? extends Transformer<V, byte[]>> valueFactory,
			String recordManagerName,
			String treeMapName) throws IOException {
		mKeyFactory = keyFactory;
		mValueFactory = valueFactory;
		mRecMan = RecordManagerFactory.createRecordManager(recordManagerName);
		mTreeMap = mRecMan.treeMap(treeMapName);
	}

	@Override
	protected KVStoreContext<K, V> doNewContext() {
		return new JDBM2KVStoreContext<K, V>(
				this, mLock, mTreeMap, mKeyFactory.newInstance(), mValueFactory.newInstance());
	}

	@Override
	protected void doClose() throws IOException {
		mRecMan.close();
	}

	@Override
	protected void doFlush() throws IOException {
		mRecMan.commit();
	}

}
