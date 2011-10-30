package org.neuronbucket.kv.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class ByteStreamTransformer implements StreamTransformer<byte[]> {

	public static final Factory<ByteStreamTransformer> FACTORY =
			new Factory<ByteStreamTransformer>() {
				public ByteStreamTransformer newInstance() {
					return new ByteStreamTransformer();
				}
	};
	byte[] buffer = new byte[1024];

	public void write(byte[] t, OutputStream out) throws IOException {
		out.write(t);
	}

	public byte[] read(InputStream in, int len) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(len);
		int read = 0;
		while ((read = in.read(buffer)) >= 0) {
			baos.write(buffer, 0, read);
		}
		return baos.toByteArray();
	}

}