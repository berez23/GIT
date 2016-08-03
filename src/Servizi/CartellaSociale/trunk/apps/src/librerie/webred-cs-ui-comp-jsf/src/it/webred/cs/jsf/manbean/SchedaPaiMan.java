package it.webred.cs.jsf.manbean;

import it.webred.cs.csa.ejb.client.AccessTableDiarioSessionBeanRemote;
import it.webred.cs.csa.ejb.dto.PaiDTO;
import it.webred.cs.data.model.CsDPai;
import it.webred.cs.data.model.CsDRelazione;
import it.webred.cs.jsf.interfaces.ISchedaPAI;
import it.webred.cs.jsf.manbean.superc.CsUiCompBaseBean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

@ManagedBean
@ViewScoped
public class SchedaPaiMan extends CsUiCompBaseBean implements ISchedaPAI {

	private CsDPai pai;
    private CsDRelazione relazione;
    private Long idCaso;
    private String widgetVar;
    
	protected AccessTableDiarioSessionBeanRemote diarioService = (AccessTableDiarioSessionBeanRemote) getEjb("CarSocialeA", "CarSocialeA_EJB", "AccessTableDiarioSessionBean");
	
	@Override
	public void inizializzaDialog(CsDRelazione rel, CsDPai pai) {
		relazione = rel;
		this.pai = pai;
	}

	@Override
	public void salva() {
		PaiDTO dto = new PaiDTO();
		fillEnte(dto);
		dto.setPai(this.getPai());
		dto.setIdRelazione(this.relazione.getDiarioId());
		dto.setCasoId(idCaso);
		try{
			
			diarioService.salvaSchedaPai(dto);
			
			RequestContext.getCurrentInstance().addCallbackParam("saved", true);		
			addInfoFromProperties("salva.ok");
		
		} catch(Exception e) {
			addErrorFromProperties("salva.error");
			logger.error(e.getMessage(),e);
		}
	}
	
	@Override
	public void reset(){
		this.pai = new CsDPai();
		relazione = null;
	}
	
	@Override
	public void elimina(CsDPai pai){
	
		PaiDTO dto = new PaiDTO();
		fillEnte(dto);
		dto.setPai(pai);
		diarioService.eliminaSchedaPai(dto);
	}

	@Override
	public void carica() {
		// TODO Auto-generated method stub
		
	}

	public CsDPai getPai() {
		if(pai==null) 
			pai = new CsDPai();
		return pai;
	}

	public void setPai(CsDPai pai) {
		this.pai = pai;
	}

	public CsDRelazione getRelazione() {
		return relazione;
	}

	public void setRelazione(CsDRelazione relazione) {
		this.relazione = relazione;
	}

	public Long getIdCaso() {
		return idCaso;
	}

	public void setIdCaso(Long idCaso) {
		this.idCaso = idCaso;
	}

	public String getWidgetVar() {
		widgetVar = "schedaPaiDialog";
		return widgetVar;
	}

	public void setWidgetVar(String widgetVar) {
		this.widgetVar = widgetVar;
	}

}
