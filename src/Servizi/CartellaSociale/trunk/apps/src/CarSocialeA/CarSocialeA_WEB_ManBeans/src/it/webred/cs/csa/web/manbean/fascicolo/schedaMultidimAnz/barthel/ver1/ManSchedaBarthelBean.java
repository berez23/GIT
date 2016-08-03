package it.webred.cs.csa.web.manbean.fascicolo.schedaMultidimAnz.barthel.ver1;

import it.webred.cs.csa.web.manbean.SchedaValutazioneBaseBean;
import it.webred.cs.csa.web.manbean.fascicolo.schedaMultidimAnz.barthel.ISchedaBarthel;
import it.webred.cs.data.model.CsDValutazione;
import it.webred.cs.data.model.CsOOperatoreSettore;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;

/**
 * @author Alessandro Feriani
 *
 */
public class ManSchedaBarthelBean extends SchedaValutazioneBaseBean implements ISchedaBarthel, Serializable {

	private static final long serialVersionUID = 1L;

	private SchedaBarthelController controller;
	
	public ManSchedaBarthelBean( ) {
		controller = new SchedaBarthelController();
		controller.setUser(getUser());
		controller.setOperatoreSettore((CsOOperatoreSettore) getSession().getAttribute("operatoresettore"));
	}
	
	protected boolean validaData ( ) {

		boolean validazioneOk = true;
		
		List<String> messagges;
		
		messagges = controller.validaBarthelData(); 
		if( messagges.size() > 0 ) {
			addWarning(StringUtils.join(messagges, "<br/>"), "");
			validazioneOk &= false;
		}

		messagges = controller.validaIADLData();
		if( messagges.size() > 0 ) {
			addWarning(StringUtils.join(messagges, "<br/>"), "");
			validazioneOk &= false;
		}

		messagges = controller.validaPatologiePsichiatricheData();
		if( messagges.size() > 0 ) {
			addWarning(StringUtils.join(messagges, "<br/>"), "");
			validazioneOk &= false;
		}
		
		RequestContext.getCurrentInstance().addCallbackParam("canClose", validazioneOk);
		
		return validazioneOk;
	}
	
	@Override
	public void save() {
		
		try {
			if( validaData() ) {

				controller.save();
					
				//ora salva
				addInfoFromProperties( "salva.ok" );
			}
		} 
		catch (Exception e) {
			addErrorFromProperties("salva.error");
			logger.error(e.getMessage(),e);
		}
	}

	@Override
	public void restore() {
		controller.restore();
	}

	public void onChangeBarthelTab() {
		controller.calcolaPunteggioTotaleBarthel();
	}

	public void onChangeIADLTab() {
		controller.calcolaPunteggioTotaleIADL();
	}

	public JsonBarthelBean getJsonCurrent() {
		return controller.getJsonCurrent();
	}
	
	@Override
	public void init(CsDValutazione schedaMultidim, CsDValutazione schedaBarthel) {
		
		try {
			controller.loadData( schedaMultidim, schedaBarthel );
		} catch (Exception e) {
			addErrorFromProperties("caricamento.error");
			logger.error(e.getMessage(),e);
		}
	}
}
