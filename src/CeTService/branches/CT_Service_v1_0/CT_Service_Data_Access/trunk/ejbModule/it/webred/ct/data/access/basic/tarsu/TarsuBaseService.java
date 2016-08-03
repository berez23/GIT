package it.webred.ct.data.access.basic.tarsu;

import it.webred.ct.data.access.basic.CTServiceBaseBean;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class TarsuBaseService extends CTServiceBaseBean implements Serializable {

	@PersistenceContext(unitName="CT_Diogene")
	protected EntityManager manager_diogene;
	
}
