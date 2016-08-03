package it.webred.ct.service.comma340.web.pratica;

import java.io.File;
import it.webred.ct.service.comma340.data.access.C340GestionePraticheService;
import it.webred.ct.service.comma340.web.Comma340BaseBean;

public class GestionePraticheBaseBean extends Comma340BaseBean{
	
	protected C340GestionePraticheService c340GestionePraticheService;
	

	public void setC340GestionePraticheService(
			C340GestionePraticheService c340GestionePraticheService) {
		this.c340GestionePraticheService = c340GestionePraticheService;
	}
	
	public C340GestionePraticheService getC340GestionePraticheService() {
		return this.c340GestionePraticheService;
	}
	
	
	public void createDirectoryPath(String path){
		//Se non esiste il percorso, lo crea
		File dir = new File(path);
		try{
			if(!dir.exists())
				dir.mkdirs();
		}catch(Throwable t){
			logger.error(t.getMessage(), t);
		}
	}
	
	

}
