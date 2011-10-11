package org.neuronbucket.kv.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface StreamTransformer<T> {

	public void write(T t, OutputStream out) throws IOException;

	public T read(InputStream in, int len) throws IOException;

}
