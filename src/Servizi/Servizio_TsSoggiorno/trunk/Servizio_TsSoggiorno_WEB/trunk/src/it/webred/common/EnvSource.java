package it.webred.common;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author macche classe che recupera il file properties per mappare l'ente e il
 *         datasource
 */
public class EnvSource extends TsEnvBase {

	private String dataSourceVirgilioDS;
	private String dataSourceAmProfiler;

	public static final String NOME_PROPERTIES = "datarouter.properties";
	public static final String NOME_PATH_JBOSS_CONF = "jboss.server.config.url";

	public EnvSource(String ente) {
		try {
			Properties props = new Properties();
			String path = System.getProperty("jboss.server.config.dir") + "\\datarouter.properties";
			String newpath = "file:///" + path.replaceAll("\\\\", "/");
			URL url;
			url = new URL(newpath);
			props.load(url.openStream());
			String route = props.getProperty(ente);
			if (route == null)
				log.debug("!Data Source " + ente + " Not Found");
			// dataSourceId = route;
			this.dataSourceVirgilioDS = "java:/jdbc/Virgilio_DS_" + route;
			this.dataSourceAmProfiler = "java:/AMProfiler";

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String getDataSourceVirgilioDS() {
		return dataSourceVirgilioDS;
	}

	public void setDataSourceVirgilioDS(String dataSourceVirgilioDS) {
		this.dataSourceVirgilioDS = dataSourceVirgilioDS;
	}

	public String getDataSourceAmProfiler() {
		return dataSourceAmProfiler;
	}

	public void setDataSourceAmProfiler(String dataSourceAmProfiler) {
		this.dataSourceAmProfiler = dataSourceAmProfiler;
	}

}
