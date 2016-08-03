package it.webred.cs.jsf.bean;

import it.webred.amprofiler.ejb.anagrafica.AnagraficaService;
import it.webred.amprofiler.model.AmAnagrafica;
import it.webred.cs.csa.ejb.client.AccessTableCasoSessionBeanRemote;
import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.data.model.CsACaso;
import it.webred.cs.data.model.CsACasoOpeTipoOpe;
import it.webred.cs.data.model.CsDColloquio;
import it.webred.cs.data.model.CsOOperatoreSettore;
import it.webred.cs.jsf.manbean.superc.CsUiCompBaseBean;

public class DatiColloquioBean extends CsUiCompBaseBean {

	protected AccessTableCasoSessionBeanRemote casoService = (AccessTableCasoSessionBeanRemote) getEjb("CarSocialeA", "CarSocialeA_EJB", "AccessTableCasoSessionBean");
	private AnagraficaService anagraficaService = (AnagraficaService) getEjb("AmProfiler", "AmProfilerEjb", "AnagraficaServiceBean");

	private CsDColloquio colloquio;
	private AmAnagrafica opAnagrafica;
	private boolean riservato;
	private boolean abilitato4riservato;
	private String campoTesto = "";
	
	public CsDColloquio getColloquio() {
		return colloquio;
	}
	public void setColloquio(CsDColloquio colloquio) {
		this.colloquio = colloquio;
	}
	public AmAnagrafica getOpAnagrafica() {
		return opAnagrafica;
	}
	public void setOpAnagrafica(AmAnagrafica opAnagrafica) {
		this.opAnagrafica = opAnagrafica;
	}
	public boolean isRiservato() {
		return riservato;
	}
	public void setRiservato(boolean riservato) {
		this.riservato = riservato;
	}
	public String getCampoTesto() {
		if( campoTesto != null && campoTesto.length() > 10 ) 
			return this.campoTesto.substring(0, 10) + "...";
		else return campoTesto;
	}
	public void setCampoTesto(String campoTesto) {
		this.campoTesto = campoTesto;
	}
	public boolean isAbilitato4riservato() {
		return abilitato4riservato;
	}
	public void setAbilitato4riservato(boolean abilitato4riservato) {
		this.abilitato4riservato = abilitato4riservato;
	}
	
	protected boolean isResponsabileCaso() throws Exception{
		CsOOperatoreSettore currentOpSettore = getCurrentOpSettore();

		BaseDTO dto = new BaseDTO();
		fillEnte(dto);

		boolean bIsResponsabileCaso = false;
		if( colloquio.getCsDDiario() != null ){
			CsACaso caso = colloquio.getCsDDiario().getCsACaso();
			dto.setObj(caso.getId());
			CsACasoOpeTipoOpe opResponsabile = casoService.findResponsabile(dto);
			
			if( opResponsabile != null ){
				bIsResponsabileCaso &= opResponsabile.getCsOOperatoreTipoOperatore().getCsOOperatoreSettore().getCsOOperatore().getId().equals(currentOpSettore.getCsOOperatore().getId());
				bIsResponsabileCaso &= opResponsabile.getCsOOperatoreTipoOperatore().getCsOOperatoreSettore().getCsOSettore().getId().equals(currentOpSettore.getCsOSettore().getId());
			}
			else
				bIsResponsabileCaso = false;
		}
		return bIsResponsabileCaso;
	
	}
	
	protected boolean isResponsabileUfficio(){
		String belfiore = getUser().getCurrentEnte();
		CsOOperatoreSettore currentOpSettore = getCurrentOpSettore();
		
		boolean is = currentOpSettore.getAmGroup().equals("CSOCIALE_RESPO_SETTORE_" + belfiore);
		return is;
		
	}
	
	public void Update( ) throws Exception
	{
		riservato = "1".equals( colloquio.getRiservato() ) ? true : false;
		campoTesto = colloquio.getTestoDiario();
				 
		if( colloquio.getDiarioId() != null ) 
		{ 
			if( riservato )
			{
				abilitato4riservato = isResponsabileCaso();
				if( !abilitato4riservato )
					abilitato4riservato = isResponsabileUfficio();
				if( !abilitato4riservato ){
					campoTesto = "Riservato";
				}
			}
			else
				abilitato4riservato = true;
		}
		else
			abilitato4riservato = true;
	}
	
	public void Initialize( CsDColloquio csColl, String usernameOp ) throws Exception
	{
		colloquio = csColl;
		opAnagrafica = anagraficaService.findAnagraficaByUserName(usernameOp);
	
		Update();
	}
	
}
