package it.webred.cet.service.ff.web.beans.dettaglio.documenti;

import it.webred.ct.data.model.docfa.DocfaDatiCensuari;
import it.webred.ct.data.model.docfa.DocfaPlanimetrie;

import java.io.Serializable;
import java.util.List;

public class DocfaPlanimetrieDatiCensuari implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private DocfaPlanimetrie docfaPlanimetrie;
	private List<DocfaDatiCensuari> docfaDatiCensuari;
	
	public DocfaPlanimetrieDatiCensuari() {
		super();
	}
	
	public DocfaPlanimetrieDatiCensuari(DocfaPlanimetrie docfaPlanimetrie, List<DocfaDatiCensuari> docfaDatiCensuari) {
		super();
		this.docfaPlanimetrie = docfaPlanimetrie;
		this.docfaDatiCensuari = docfaDatiCensuari;
	}
	
	public DocfaPlanimetrie getDocfaPlanimetrie() {
		return docfaPlanimetrie;
	}
	
	public void setDocfaPlanimetrie(DocfaPlanimetrie docfaPlanimetrie) {
		this.docfaPlanimetrie = docfaPlanimetrie;
	}

	public List<DocfaDatiCensuari> getDocfaDatiCensuari() {
		return docfaDatiCensuari;
	}

	public void setDocfaDatiCensuari(List<DocfaDatiCensuari> docfaDatiCensuari) {
		this.docfaDatiCensuari = docfaDatiCensuari;
	}
	
	public String getSubalternoStr() {
		if (docfaDatiCensuari == null || docfaDatiCensuari.size() == 0) {
			return "-";
		}
		String subalternoStr = "";
		for (DocfaDatiCensuari ddc : docfaDatiCensuari) {
			if (!subalternoStr.equals("")) {
				subalternoStr += " - ";
			}
			subalternoStr += ddc.getSubalterno();
		}
		return subalternoStr;
	}

}
