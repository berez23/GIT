package it.webred.mui.consolidation;

import it.webred.mui.model.Residenza;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

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
	
	protected Residenza[] residenzeArray(ArrayList<Residenza> residenze) {
		int c= 0;
		Residenza[] residenzeArray = new Residenza[residenze.size()];
		for (Iterator<Residenza> iterRe = residenze.iterator(); iterRe.hasNext();) {
			residenzeArray[c] = (Residenza)iterRe.next();
			c++;
		}
		return residenzeArray;
	}
	
	protected String getProperty(String propName) {
        String fileName =  this.getClass().getName()+".properties";
        ClassLoader cl = this.getClass().getClassLoader();
        InputStream is = cl.getResourceAsStream(fileName);
        Properties props = new Properties();        
        try {
        	props.load(is);
        } catch (Exception e) {
			return null;
		}
        
        String p = props.getProperty(propName);
        return p;
	}


}
