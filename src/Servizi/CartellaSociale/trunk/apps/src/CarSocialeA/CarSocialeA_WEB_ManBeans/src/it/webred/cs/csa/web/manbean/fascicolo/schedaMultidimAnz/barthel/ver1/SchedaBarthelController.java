package it.webred.cs.csa.web.manbean.fascicolo.schedaMultidimAnz.barthel.ver1;

import it.webred.cs.csa.ejb.client.AccessTableDiarioSessionBeanRemote;
import it.webred.cs.csa.ejb.dto.SchedaBarthelDTO;
import it.webred.cs.csa.web.manbean.BaseController;
import it.webred.cs.data.model.CsDClob;
import it.webred.cs.data.model.CsDValutazione;
import it.webred.cs.data.model.CsOOperatoreSettore;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Alessandro Feriani
 *
 */
public class SchedaBarthelController extends BaseController {

	private static final long serialVersionUID = 1L;

	protected AccessTableDiarioSessionBeanRemote diarioService = (AccessTableDiarioSessionBeanRemote) getEjb("CarSocialeA", "CarSocialeA_EJB", "AccessTableDiarioSessionBean");
	
	private CsOOperatoreSettore operatoreSettore;
	private JsonBarthelBean jsonOriginal;
	private JsonBarthelBean jsonCurrent;
	private CsDValutazione valutazioneSchedaMultidim;
	private CsDValutazione valutazioneSchedaBarthel;

	public SchedaBarthelController() {
	}
	
	public List<String> validaBarthelData() {
		return jsonCurrent.getMainData().checkObbligatorieta();
	}

	public List<String> validaIADLData() {
		return jsonCurrent.getIadlData().checkObbligatorieta();
	}

	public List<String> validaPatologiePsichiatricheData() {
		return jsonCurrent.getPatologiePsichiatricheData().checkObbligatorieta();
	}

	public void loadData( CsDValutazione schedaMultidim, CsDValutazione schedaBarthel) throws Exception {
		valutazioneSchedaMultidim = schedaMultidim;
		valutazioneSchedaBarthel = schedaBarthel;
		
		try {
			if(valutazioneSchedaBarthel != null) {
				CsDClob clob = valutazioneSchedaBarthel.getCsDDiario().getCsDClob();
				if( StringUtils.isNotEmpty(clob.getDatiClob()) ) {
					ObjectMapper objectMapper = new ObjectMapper();
					jsonOriginal = objectMapper.readValue(clob.getDatiClob(), JsonBarthelBean.class);
				}
			}
		}
		catch (Exception e) {
			jsonOriginal = null;
			throw e;
		}

		restore();
	}

	public void restore() {
		jsonCurrent = new JsonBarthelBean( jsonOriginal );
	}

	public void save() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = objectMapper.writeValueAsString(jsonCurrent);

		SchedaBarthelDTO schedaBarthelDTO = new SchedaBarthelDTO(); 
		fillEnte( schedaBarthelDTO );

		//JSON 
		schedaBarthelDTO.setId( jsonOriginal != null ? valutazioneSchedaBarthel.getDiarioId() : null );
		schedaBarthelDTO.setSchedaMultidimDiarioId( valutazioneSchedaMultidim.getDiarioId() );
		schedaBarthelDTO.setJsonString( jsonString );
		schedaBarthelDTO.setOpSettore( operatoreSettore );
		schedaBarthelDTO.setDataDiValutazione( jsonCurrent.patologiePsichiatricheData.dataDiValutazione );
		schedaBarthelDTO.setVersione(this.getClass().getName());
		schedaBarthelDTO.setDescrizione("descrizioneSchedaBarthel");
		diarioService.saveSchedaBarthel( schedaBarthelDTO );
	}

	public void setOperatoreSettore(CsOOperatoreSettore operatoreSettore) {
		this.operatoreSettore = operatoreSettore;
	}

	public void calcolaPunteggioTotaleBarthel() {
		jsonCurrent.getMainData().calcolaPunteggioTotale();
	}

	public void calcolaPunteggioTotaleIADL() {
		jsonCurrent.getIadlData().calcolaPunteggioTotale();
	}

	public JsonBarthelBean getJsonCurrent() {
		return jsonCurrent;
	}
}
