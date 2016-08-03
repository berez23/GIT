package it.webred.ct.data.access.basic.catasto;

import it.webred.ct.data.access.basic.CTServiceBaseBean;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class CatastoBaseService extends CTServiceBaseBean implements Serializable {

	@PersistenceContext(unitName="CT_Siti")
	protected EntityManager manager_siti;
	
	@PersistenceContext(unitName="CT_Diogene")
	protected EntityManager manager_diogene;
	
}
