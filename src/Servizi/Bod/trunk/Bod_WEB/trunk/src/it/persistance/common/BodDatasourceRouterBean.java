package it.persistance.common;

import it.webred.cet.permission.CeTUser;
import it.webred.ct.support.datarouter.UserContext;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import static it.webred.ct.support.datarouter.context.DataContextHolder.*;
import static it.webred.ct.support.datarouter.ContextKey.*;

/**
 * Questa classe gestisce effettivamente il routing fra i diversi datasource
 * 
 * @author Francesco Azzola
 * 
 * */
public class BodDatasourceRouterBean extends AbstractRoutingDataSource {

	protected Logger logger = Logger.getLogger(BodDatasourceRouterBean.class.getName());

	@Override
	protected Object determineCurrentLookupKey() {
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			if(context != null){
				HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
				HttpSession sessione = request.getSession(false);
				
				CeTUser userCtx = (CeTUser) sessione.getAttribute("user");
				//logger.info("Routing datasource..." + (userCtx!=null ? userCtx.getCurrentEnte() : "NULL"));
				
				if(userCtx != null){
					String enteId = userCtx.getCurrentEnte();
					Properties props = new Properties();
					String path = System.getProperty("jboss.server.config.dir")
							+ "\\datarouter.properties";
					String newpath = "file:///" + path.replaceAll("\\\\", "/");
					logger.debug("\tEnte Id [" + enteId + "] path=" + path);
					URL url;
					url = new URL(newpath);
					props.load(url.openStream());
					String route = props.getProperty(enteId);
					if(route == null)
						logger.warn("!Data Source " + enteId + " Not Found, Default Source Will Be Used");
					return route;
				}
			}

		} catch (MalformedURLException e) {
			logger.error("Eccezione determineCurrentLookupKey: " + e.getMessage(), e);
		} catch (IOException e) {
			logger.error("Eccezione determineCurrentLookupKey: " + e.getMessage(), e);
		} catch (Throwable t) {
			logger.warn("determineCurrentLookupKey - Error userCtx null",t);
		}

		return null;
	}

}
