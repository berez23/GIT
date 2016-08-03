package it.webred.ct.diagnostics.service.web.beans;

import it.webred.ct.diagnostics.service.data.dto.DiaCommandDTO;
import it.webred.ct.diagnostics.service.web.user.UserBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EseguiDiaBean extends UserBean {
	
	private static final long serialVersionUID = 1L;		
	private String typeDiagnostiche;
	private String codCommand;
	private String diagType;
	private List<DiaCommandDTO> listaCmdForDia = new ArrayList<DiaCommandDTO>();
	private boolean visTdEsecuzioni;
	private String newDiaPage = "/jsp/protected/empty.xhtml";
	private String paginaDettaglio = "/jsp/protected/empty.xhtml";
	
	public List<DiaCommandDTO> getListaCmdForDia() {
		return listaCmdForDia;
	}


	public String getPaginaDettaglio() {
		return paginaDettaglio;
	}


	public void setPaginaDettaglio(String paginaDettaglio) {
		this.paginaDettaglio = paginaDettaglio;
	}


	public void setListaCmdForDia(List<DiaCommandDTO> listaCmdForDia) {
		this.listaCmdForDia = listaCmdForDia;
	}


	public String getCodCommand() {
		return codCommand;
	}


	public void setCodCommand(String codCommand) {
		this.codCommand = codCommand;
	}


	public String getDiagType() {
		return diagType;
	}


	public void setDiagType(String diagType) {
		this.diagType = diagType;
	}

	
	public String getNewDiaPage() {
		return newDiaPage;
	}


	public void setNewDiaPage(String newDiaPage) {
		this.newDiaPage = newDiaPage;
	}


	public boolean isVisTdEsecuzioni() {
		return visTdEsecuzioni;
	}


	public void setVisTdEsecuzioni(boolean visTdEsecuzioni) {
		this.visTdEsecuzioni = visTdEsecuzioni;
	}


	public String getTypeDiagnostiche() {
		return typeDiagnostiche;
	}


	public void setTypeDiagnostiche(String typeDiagnostiche) {
		this.typeDiagnostiche = typeDiagnostiche;
	}

	public EseguiDiaBean(){
		super();
	}
	
	private List<Long> getFontiSelezionate()  {
		if (getRequest().getSession().getAttribute("fonteBean") == null)
			return null;
		FonteBean fonti = (FonteBean)getRequest().getSession().getAttribute("fonteBean");
		return fonti.getFontiSelezionate();
	}
	
	public void doToro() {
		paginaDettaglio = "/jsp/protected/diagnostics/data/viewDiaCommand.xhtml";
//		ExecuteDataProviderImpl edp =  (ExecuteDataProviderImpl)super.getBeanReference("executeDataProviderImpl");
//		edp.setVisTdEsecuzioni(true);
	}
	
	public void doResetPage(){
		paginaDettaglio = "/jsp/protected/empty.xhtml";
	}
	
	public void doEsegui() {
		try {
			
			super.getLogger().debug("Attributi di request: "+this.diagType+ "#"+ this.codCommand);
					
			StringBuilder  pathController = new StringBuilder("/Controller/jsp/protected/diag/");
			
			if(this.diagType.equals("S")) {
				pathController.append("launcher.faces");
			}
			else if(this.diagType.equals("G")) {
				pathController.append("monitor.faces");
			}		
			
			pathController.append("?codiceComando=");
			pathController.append(this.codCommand);
			pathController.append("&diagType=");
			pathController.append(this.diagType);
			pathController.append("&belfiore=");
			pathController.append(getUser().getCurrentEnte());
			pathController.append("&pathApp=");
			pathController.append(getRequest().getRequestURL().toString());
			pathController.append("&diagHeader=true");
			
			super.getLogger().debug("URL: "+pathController.toString());
			
			getResponse().sendRedirect(pathController.toString());
			
			
		} catch (IOException e) {			
			getLogger().error(e.getMessage(),e);
		}		
	}
	
	
	public String goBackFromDetail(){
		return "menu.eseguiDia";
	}
}
