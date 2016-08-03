package it.webred.cs.csa.web.manbean.fascicolo.schedaMultidimAnz.barthel;

import it.webred.cs.csa.ejb.client.AccessTableDiarioSessionBeanRemote;
import it.webred.cs.csa.web.manbean.SchedaValutazioneBaseBean;
import it.webred.cs.data.model.CsDValutazione;

import java.io.Serializable;

/**
 * @author Alessandro Feriani
 *
 */
public class ManSchedaBarthelBean extends SchedaValutazioneBaseBean implements ISchedaBarthel, Serializable {

	private static final long serialVersionUID = 1L;

	protected AccessTableDiarioSessionBeanRemote diarioService = (AccessTableDiarioSessionBeanRemote) getEjb("CarSocialeA", "CarSocialeA_EJB", "AccessTableDiarioSessionBean");
	
	JsonBarthelBean jsonOriginal;
	JsonBarthelBean jsonCurrent;
	
	private CsDValutazione valutazioneSchedaMultidim;
	private CsDValutazione valutazioneSchedaBarthel;
	
	@Override
	public void save() {
		
		try {
				//ora salva
				addInfoFromProperties( "salva.ok" );
		} 
		catch (Exception e) {
			addErrorFromProperties("salva.error");
			logger.error(e.getMessage(),e);
		}
	}

	@Override
	public void restore() {

	}

	@Override
	public void init(CsDValutazione schedaMultidim, CsDValutazione schedaBarthel) {
		// TODO Auto-generated method stub
		jsonOriginal = null;
		jsonCurrent = new JsonBarthelBean(jsonOriginal);
		
	}
	
	public JsonBarthelBean getJsonCurrent() {
		return jsonCurrent;
	}

}
