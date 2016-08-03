package it.webred.ct.service.muidocfa.web.bean.ici;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;

import it.webred.ct.data.access.basic.diagnostiche.DiagnosticheDataIn;
import it.webred.ct.data.model.diagnostiche.DocfaIciReportSogg;
import it.webred.ct.service.muidocfa.web.bean.MuiDocfaBaseBean;

public class SubjectBean extends MuiDocfaBaseBean {

	private String idRep;
	private List<DocfaIciReportSogg> listaSoggetti;

	@PostConstruct
	public void initService() {

		listaSoggetti = new ArrayList<DocfaIciReportSogg>();

	}

	public void doCaricaSoggetti() {
		try {

			if (idRep != null){
				DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
				dataIn.setObj(idRep);
				fillEnte(dataIn);
				listaSoggetti = docfaIciReportService.getReportSogg(dataIn);
			}

		} catch (Throwable t) {
			super.addErrorMessage("lista.error", t.getMessage());
			t.printStackTrace();
		}
	}

	public String getIdRep() {
		return idRep;
	}

	public void setIdRep(String idRep) {
		this.idRep = idRep;
	}

	public List<DocfaIciReportSogg> getListaSoggetti() {
		return listaSoggetti;
	}

	public void setListaSoggetti(List<DocfaIciReportSogg> listaSoggetti) {
		this.listaSoggetti = listaSoggetti;
	}

}
