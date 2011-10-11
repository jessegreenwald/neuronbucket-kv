package org.neuronbucket.kv.util;

import java.io.File;

public class StringToFileTransformer implements Transformer<String, File> {

	private File mBaseDir;

	public StringToFileTransformer(File baseDir) {
		mBaseDir = baseDir;
	}

	public File transformTo(String t) {
		return new File(mBaseDir, t);
	}

	public String transformFrom(File v) {
		return null;
	}

}