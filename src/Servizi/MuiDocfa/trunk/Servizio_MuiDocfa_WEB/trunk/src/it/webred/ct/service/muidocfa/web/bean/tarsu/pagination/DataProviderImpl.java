package it.webred.ct.service.muidocfa.web.bean.tarsu.pagination;

import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.diagnostiche.DiagnosticheDataIn;
import it.webred.ct.data.access.basic.diagnostiche.dto.RicercaDocfaReportDTO;
import it.webred.ct.data.model.catasto.Sitideco;
import it.webred.ct.data.model.diagnostiche.DocfaAnomalie;
import it.webred.ct.data.model.diagnostiche.DocfaTarReport;
import it.webred.ct.data.model.diagnostiche.DocfaTarReportSogg;
import it.webred.ct.service.muidocfa.web.bean.MuiDocfaBaseBean;
import it.webred.ct.service.muidocfa.web.bean.util.CommonDataBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

public class DataProviderImpl extends MuiDocfaBaseBean implements DataProvider {

	private RicercaDocfaReportDTO criteria = new RicercaDocfaReportDTO();

	private boolean disableFlag;

	private List<SelectItem> listaAnomalie;
	
	private List<SelectItem> listaForniture;
	
	public List<DocfaTarReport> getReportByRange(int start, int rowNumber) {

		try {

			criteria.setStartm(start);
			criteria.setNumberRecord(rowNumber);
			DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
			fillEnte(dataIn);
			dataIn.setRicerca(criteria);
			return docfaTarReportService.searchReport(dataIn);

		} catch (Throwable t) {
			super.addErrorMessage("lista.error", t.getMessage());
			t.printStackTrace();
			return new ArrayList<DocfaTarReport>();
		}
	}

	public List<DocfaTarReportSogg> getReportSogg(String id) {

		try {

			DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
			fillEnte(dataIn);
			dataIn.setObj(id);
			return docfaTarReportService.getReportSogg(dataIn);

		} catch (Throwable t) {
			super.addErrorMessage("lista.error", t.getMessage());
			t.printStackTrace();
			return new ArrayList<DocfaTarReportSogg>();
		}
	}

	public Long getReportCount() {
		DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
		fillEnte(dataIn);
		dataIn.setRicerca(criteria);
		Long result = docfaTarReportService.searchReportCount(dataIn);
		return result;
	}

	public void doConfermaCategorie() {
		criteria.setCategoriaDocfa("");
		String nuovaCat = "";
		for (String cat : criteria.getCategorieDocfa()) {
			nuovaCat += "," + cat;
		}
		criteria.setCategoriaDocfa(nuovaCat.replaceFirst(",", ""));
	}

	public String getCategoria(String codCategoria) {

		if (codCategoria != null && !"".equals(codCategoria)) {
			try {

				RicercaOggettoCatDTO ro = new RicercaOggettoCatDTO();
				ro.setCodCategoria(codCategoria);
				fillEnte(ro);
				List<Sitideco> lista = catastoService.getListaCategorieImmobile(ro);
				if(lista.size()> 0)
					return lista.get(0).getDescription();
				else
					return "Categoria non codificata";

			} catch (Throwable t) {
				super.addErrorMessage("categorie.error", t.getMessage());
				t.printStackTrace();
			}
		}
		return "";
	}

	public void doConfermaCausali() {
		criteria.setCausaleDocfa("");
		String nuovaCau = "";
		for (String cau : criteria.getCausaliDocfa()) {
			nuovaCau += "," + cau;
		}
		criteria.setCausaleDocfa(nuovaCau.replaceFirst(",", ""));
	}

	public void doDisableFlag() {

		if (criteria.getTipoAnomalia() == null
				|| criteria.getTipoAnomalia().equals(""))
			disableFlag = false;
		else {
			disableFlag = true;
			criteria.setFlgElaborati("");
			criteria.setFlgAnomaliClasse("");
			criteria.setFlgPresPreDocfa("");
			criteria.setFlgPresPostDocfa("");
			criteria.setFlgPresCatDataDocfa("");
			criteria.setFlgDocfaSopprNoCostit("");
			criteria.setFlgVarCategoria("");
			criteria.setFlgVarClasse("");
			criteria.setFlgVarRendita("");
			criteria.setFlgAnomali("");
		}
	}

	public String getCausale(String codCausale) {

		if (codCausale != null && !"".equals(codCausale)) {
			try {

				return CommonDataBean.getCausali().get(codCausale);

			} catch (Throwable t) {
				super.addErrorMessage("categorie.error", t.getMessage());
				t.printStackTrace();
			}
		}
		return "";
	}

	public String goSearch() {
		criteria = new RicercaDocfaReportDTO();
		return "muidocfa.search.tarsu";
	}

	public void resetData() {
		criteria = new RicercaDocfaReportDTO();
	}

	public RicercaDocfaReportDTO getCriteria() {
		return criteria;
	}

	public void setCriteria(RicercaDocfaReportDTO criteria) {
		this.criteria = criteria;
	}

	public boolean isDisableFlag() {
		return disableFlag;
	}

	public void setDisableFlag(boolean disableFlag) {
		this.disableFlag = disableFlag;
	}

	public List<SelectItem> getListaAnomalie() {

		if (listaAnomalie == null) {
			listaAnomalie = new ArrayList<SelectItem>();
			try {

				DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
				fillEnte(dataIn);
				List<DocfaAnomalie> lista = docfaTarReportService.getListaAnomalie(dataIn);

				for (DocfaAnomalie anm : lista) {
					SelectItem item = new SelectItem(anm.getId(), anm.getDescrizione());
					listaAnomalie.add(item);
				}

			} catch (Throwable t) {
				super.addErrorMessage("anomalie.error", t.getMessage());
				t.printStackTrace();
			}
		}
		return listaAnomalie;
	}

	public void setListaAnomalie(List<SelectItem> listaAnomalie) {
		this.listaAnomalie = listaAnomalie;
	}
	
	public List<SelectItem> getListaForniture() {

		if (listaForniture == null) {
			listaForniture = new ArrayList<SelectItem>();
			listaForniture.add(new SelectItem(null,"-Seleziona-"));
			
			try {

				DiagnosticheDataIn ro = new DiagnosticheDataIn();
				fillEnte(ro);
				List<Date> lista = docfaTarReportService.getListaForniture(ro);
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				SimpleDateFormat text = new SimpleDateFormat("MMM yyyy");
				
				for (Date f : lista) {
					SelectItem item = new SelectItem(sdf.format(f), text.format(f));
					listaForniture.add(item);
				}

			} catch (Throwable t) {
				super.addErrorMessage("forniture.error", t.getMessage());
				t.printStackTrace();
			}
		}
		
		return listaForniture;
		
	}

	public void setListaForniture(List<SelectItem> listaForniture) {
		this.listaForniture = listaForniture;
	}

}
