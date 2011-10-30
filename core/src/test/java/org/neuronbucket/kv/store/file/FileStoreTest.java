package org.neuronbucket.kv.store.file;

import java.io.File;

import org.neuronbucket.kv.KVStoreTest;
import org.neuronbucket.kv.util.ByteStreamTransformer;
import org.neuronbucket.kv.util.StringToFileTransformer;

public class FileStoreTest extends KVStoreTest<FileKVStore<String, byte[]>> {

	private File mBaseDir;

	@Override
	public FileKVStore<String, byte[]> createStore() {
		mBaseDir = new File("tmp");
		return FileKVStore.newInstance(
				StringToFileTransformer.createFactory(mBaseDir),
				ByteStreamTransformer.FACTORY);
	}

	@Override
	public void destroyStore(FileKVStore<String, byte[]> store) {
		recursiveDelete(mBaseDir);
	}

	private void recursiveDelete(File file) {
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				recursiveDelete(child);
			}
		}
		file.delete();
	}
}
