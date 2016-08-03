package it.escsolution.escwebgis.common;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author macche classe che recupera il file properties per mappare l'ente e il
 *         datasource
 */
public class EnvSource extends EnvBase {
	
	private String dataSource;
	private String dataSourceIntegrato;
	private String dataSourceId;
	private String dataSourceSiti;

	public static final String NOME_PROPERTIES = "datarouter.properties";
	public static final String NOME_PATH_JBOSS_CONF = "jboss.server.config.url";

	public EnvSource(String ente) {
		try {
			Properties props = new Properties();
			String path = System.getProperty("jboss.server.config.dir")
					+ "\\datarouter.properties";
			String newpath = "file:///" + path.replaceAll("\\\\", "/");
			URL url;
			url = new URL(newpath);
			props.load(url.openStream());
			String route = props.getProperty(ente);
			if (route == null)
				log.debug("!Data Source " + ente + " Not Found");
			dataSourceId = route;
			dataSource = "java:/jdbc/Diogene_DS_" + route;
			dataSourceIntegrato = "java:/jdbc/dbIntegrato_" + route;
			dataSourceSiti = "java:/jdbc/Siti_DS_" + route;

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getDataSourceIntegrato() {
		return dataSourceIntegrato;
	}

	public void setDataSourceIntegrato(String dataSourceIntegrato) {
		this.dataSourceIntegrato = dataSourceIntegrato;
	}

	public String getDataSourceId() {
		return dataSourceId;
	}

	public void setDataSourceId(String dataSourceId) {
		this.dataSourceId = dataSourceId;
	}

	public String getDataSourceSiti() {
		return dataSourceSiti;
	}

	public void setDataSourceSiti(String dataSourceSiti) {
		this.dataSourceSiti = dataSourceSiti;
	}

}
