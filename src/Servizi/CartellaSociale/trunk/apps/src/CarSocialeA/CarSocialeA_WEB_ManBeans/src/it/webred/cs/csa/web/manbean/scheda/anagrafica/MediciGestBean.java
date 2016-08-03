package it.webred.cs.csa.web.manbean.scheda.anagrafica;

import it.webred.cs.csa.ejb.client.AccessTableMediciSessionBeanRemote;
import it.webred.cs.data.model.CsCMedico;
import it.webred.cs.jsf.interfaces.IDatiValiditaGestione;
import it.webred.cs.jsf.manbean.DatiValGestioneMan;
import it.webred.ct.support.datarouter.CeTBaseObject;
import it.webred.dto.utility.KeyValuePairBean;
import it.webred.ejb.utility.ClientUtility;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.NoneScoped;
import javax.naming.NamingException;

@ManagedBean
@NoneScoped
public class MediciGestBean extends DatiValGestioneMan implements IDatiValiditaGestione {

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<KeyValuePairBean> getLstItems() {
		
		lstItems = new ArrayList<KeyValuePairBean>();
		try {
			AccessTableMediciSessionBeanRemote bean = (AccessTableMediciSessionBeanRemote) ClientUtility.getEjbInterface("CarSocialeA", "CarSocialeA_EJB", "AccessTableMediciSessionBean");
			CeTBaseObject bo = new CeTBaseObject();
			fillEnte(bo);
			List<CsCMedico> beanLstMedici = bean.getMedici(bo);
			if (beanLstMedici != null) {
				for (CsCMedico medico : beanLstMedici) {
					String denominazione = (medico.getCognome() == null ? "" : medico.getCognome()).trim();
					String nome = (medico.getNome() == null ? "" : medico.getNome()).trim();
					if (!nome.equals("")) {
						if (!denominazione.equals("")) {
							denominazione += " ";
						}
						denominazione += nome;
					}					
					lstItems.add(new KeyValuePairBean(medico.getId(), denominazione));
				}
			}
		} catch (NamingException e) {
			addErrorFromProperties("caricamento.error");
			logger.error(e.getMessage(),e);
		}
		
		return lstItems;
	}

}
