package it.webred.ct.service.muidocfa.web.bean.tarsu;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;

import it.webred.ct.data.access.basic.diagnostiche.DiagnosticheDataIn;
import it.webred.ct.data.model.diagnostiche.DocfaTarReportSogg;
import it.webred.ct.service.muidocfa.web.bean.MuiDocfaBaseBean;

public class SubjectBean extends MuiDocfaBaseBean {

	private String idRep;
	private List<DocfaTarReportSogg> listaSoggetti;

	@PostConstruct
	public void initService() {

		listaSoggetti = new ArrayList<DocfaTarReportSogg>();

	}

	public void doCaricaSoggetti() {
		try {

			if (idRep != null){
				DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
				fillEnte(dataIn);
				dataIn.setObj(idRep);
				listaSoggetti = docfaTarReportService.getReportSogg(dataIn);
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

	public List<DocfaTarReportSogg> getListaSoggetti() {
		return listaSoggetti;
	}

	public void setListaSoggetti(List<DocfaTarReportSogg> listaSoggetti) {
		this.listaSoggetti = listaSoggetti;
	}

}
