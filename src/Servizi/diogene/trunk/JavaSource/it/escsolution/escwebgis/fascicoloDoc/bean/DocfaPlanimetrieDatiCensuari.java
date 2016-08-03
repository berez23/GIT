package it.escsolution.escwebgis.fascicoloDoc.bean;

import it.webred.ct.data.model.docfa.DocfaDatiCensuari;
import it.webred.ct.data.model.docfa.DocfaPlanimetrie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DocfaPlanimetrieDatiCensuari implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private DocfaPlanimetrie docfaPlanimetrie;
	private List<DocfaDatiCensuari> docfaDatiCensuari;
	private boolean lastSit;
	
	public DocfaPlanimetrieDatiCensuari() {
		super();
	}
	
	public DocfaPlanimetrieDatiCensuari(DocfaPlanimetrie docfaPlanimetrie, List<DocfaDatiCensuari> docfaDatiCensuari) {
		super();
		this.docfaPlanimetrie = docfaPlanimetrie;
		this.docfaDatiCensuari = docfaDatiCensuari;
	}
	
	public DocfaPlanimetrieDatiCensuari(DocfaPlanimetrie docfaPlanimetrie, List<DocfaDatiCensuari> docfaDatiCensuari, boolean lastSit) {
		super();
		this.docfaPlanimetrie = docfaPlanimetrie;
		this.docfaDatiCensuari = docfaDatiCensuari;
		this.lastSit = lastSit;
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
	
	public boolean isLastSit() {
		return lastSit;
	}

	public void setLastSit(boolean lastSit) {
		this.lastSit = lastSit;
	}

	public String getSubalternoStr() {
		if (this.getDocfaPlanimetrie() == null || this.getDocfaPlanimetrie().getFornitura() == null) {
			return "-";
		}
		if (docfaDatiCensuari == null || docfaDatiCensuari.size() == 0) {
			return "-";
		}
		String subalternoStr = "";
		ArrayList<String> subalterni = new ArrayList<String>();
		for (DocfaDatiCensuari ddc : docfaDatiCensuari) {
			if (ddc.getFornitura() != null && ddc.getFornitura().getTime() == this.getDocfaPlanimetrie().getFornitura().getTime()) {
				boolean add = true;				
				for (String subalterno : subalterni) {
					if (subalterno.equalsIgnoreCase(ddc.getSubalterno())) {
						add = false;
						break;
					}
				}
				if (add) {
					subalterni.add(ddc.getSubalterno());
				}
			}
		}
		for (String subalterno : subalterni) {
			if (!subalternoStr.equals("")) {
				subalternoStr += " - ";
			}
			subalternoStr += (subalterno == null ? "-" : subalterno);
		}
		return subalternoStr;
	}
	
	public String getCategoriaStr() {
		if (this.getDocfaPlanimetrie() == null || this.getDocfaPlanimetrie().getFornitura() == null) {
			return "-";
		}
		if (docfaDatiCensuari == null || docfaDatiCensuari.size() == 0) {
			return "-";
		}
		String categoriaStr = "";
		ArrayList<String> categorie = new ArrayList<String>();
		for (DocfaDatiCensuari ddc : docfaDatiCensuari) {
			if (ddc.getFornitura() != null && ddc.getFornitura().getTime() == this.getDocfaPlanimetrie().getFornitura().getTime()) {
				boolean add = true;				
				for (String categoria : categorie) {
					if (categoria.equalsIgnoreCase(ddc.getCategoria())) {
						add = false;
						break;
					}
				}
				if (add) {
					categorie.add(ddc.getCategoria());
				}
			}
		}
		for (String categoria : categorie) {
			if (!categoriaStr.equals("")) {
				categoriaStr += " - ";
			}
			categoriaStr += (categoria == null ? "-" : categoria);
		}
		return categoriaStr;
	}

}
