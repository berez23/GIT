package it.webred.mui.consolidation;

import java.io.IOException;
import java.io.InputStream;

public class SqlAdapter {

	public SqlAdapter() {
	}
	protected String loadResourceInString(String res) throws IOException {

		return loadFileInString(this.getClass().getClassLoader()
				.getResourceAsStream(res));
	}

	protected static String loadFileInString(InputStream is) throws IOException {
		StringBuffer sb = new StringBuffer();
		int chunkLength = 256;
		byte[] buf = new byte[chunkLength];
		int readen = -1;
		while ((readen = is.read(buf)) != -1) {
			sb.append(new String(buf, 0, readen));
		}
		return sb.toString();
	}


}
