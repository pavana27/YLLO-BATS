package org.temple.cis.kantlab.edgecomputing.server;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.codec.binary.Base64;

public class Test {
	public static void main(String...strings) throws Exception {
		File file = new File("/Users/prenukaiah/Desktop/images/7.png");
		FileInputStream stream = new FileInputStream(file);
		int size = stream.available();
		byte[] buffer = new byte[size];
		stream.read(buffer, 0, size);
		String base64String = Base64.encodeBase64String(buffer);
		System.out.println(base64String);
		stream.close();
	}
}
