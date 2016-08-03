package it.webred.cs.csa.web.manbean.fascicolo.schedaMultidimAnz.barthel.ver1;

import org.apache.commons.beanutils.BeanUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Alessandro Feriani
 *
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonBarthelBean {

	private static final long serialVersionUID = 1L;
	
	protected JsonBarthelMainDataBean mainData;
	protected JsonBarthelIADLDataBean iadlData;
	protected JsonBarthelPatologiePsichiatricheDataBean patologiePsichiatricheData;
	
	public JsonBarthelBean() {
		mainData = new JsonBarthelMainDataBean();
		iadlData = new JsonBarthelIADLDataBean();
		patologiePsichiatricheData = new JsonBarthelPatologiePsichiatricheDataBean();
	}

	public JsonBarthelBean(JsonBarthelBean jsonOriginal) {
	
		try {
			if( jsonOriginal != null )  {
				mainData = (JsonBarthelMainDataBean) BeanUtils.cloneBean( jsonOriginal.getMainData() );
				iadlData = (JsonBarthelIADLDataBean) BeanUtils.cloneBean( jsonOriginal.getIadlData() );
				patologiePsichiatricheData = (JsonBarthelPatologiePsichiatricheDataBean) BeanUtils.cloneBean( jsonOriginal.getPatologiePsichiatricheData() );
			}
			else
			{
				mainData = new JsonBarthelMainDataBean();
				iadlData = new JsonBarthelIADLDataBean();
				patologiePsichiatricheData = new JsonBarthelPatologiePsichiatricheDataBean();
			}

			mainData.calcolaPunteggioTotale();
			iadlData.calcolaPunteggioTotale();
		} 
		catch (Exception ex) {
			ex.printStackTrace();
			throw new Error( ex );
		}
	}

	public JsonBarthelMainDataBean getMainData() {
		return mainData;
	}

	public JsonBarthelIADLDataBean getIadlData() {
		return iadlData;
	}

	public JsonBarthelPatologiePsichiatricheDataBean getPatologiePsichiatricheData() {
		return patologiePsichiatricheData;
	}
	
}
