package org.neuronbucket.kv.util;

import java.io.File;

public class StringToFileTransformer implements Transformer<String, File> {

	public static Factory<StringToFileTransformer> createFactory(final File baseDir) {
		return new Factory<StringToFileTransformer>() {
			public StringToFileTransformer newInstance() {
				return new StringToFileTransformer(baseDir);
			}
		};
	};

	private File mBaseDir;

	private StringToFileTransformer(File baseDir) {
		mBaseDir = baseDir;
	}

	public File transformTo(String t) {
		return new File(mBaseDir, t);
	}

	public String transformFrom(File v) {
		return null;
	}

}