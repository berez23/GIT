package it.webred.ct.diagnostics.service.data.dao;

import javax.persistence.*;

import org.apache.log4j.Logger;

public class DIABaseDAO {
	@PersistenceContext(unitName="Servizio_Dia_Data_Model")
	protected EntityManager manager;

	protected Logger logger = Logger.getLogger("dia.log");
	
	
	protected String[] toStringArray(Object[] oo) {
		String[] ss = new String[oo.length];
		int index = 0;
		
		for(Object o: oo) {
			ss[index] = o.toString();
			logger.debug("[Colonna] "+ss[index]);
			index++;
		}
		
		return ss;
	}
}
