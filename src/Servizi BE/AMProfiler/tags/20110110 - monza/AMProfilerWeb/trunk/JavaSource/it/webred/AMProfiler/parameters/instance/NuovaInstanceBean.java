package it.webred.AMProfiler.parameters.instance;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import it.webred.ct.config.model.AmComune;
import it.webred.ct.config.model.AmInstance;
import it.webred.ct.config.model.AmInstanceComune;
import it.webred.ct.config.model.AmInstanceComunePK;
import it.webred.ct.config.parameters.dto.SelectObjDTO;

public class NuovaInstanceBean extends InstanceBaseBean {

	private AmInstance nuovaInstance = new AmInstance();
	private String labelInstanceSelected;
	private String labelApplicationSelected;
	private List<SelectItem> listaInstance = new ArrayList<SelectItem>();
	private List<SelectObjDTO> listaComuni = new ArrayList<SelectObjDTO>();
	
	public void doInit(){
		
		doCaricaComuni(null);
		labelInstanceSelected = "";
		labelApplicationSelected = "";
		nuovaInstance = new AmInstance();
		
	}
	
	public void doCaricaComuni(String instance){
		
		String msg = "listainstance";

		listaComuni = new ArrayList<SelectObjDTO>();
		
		try {
			
			List<AmComune> lista = comuneService.getListaComune();
			
			for(AmComune comune: lista){
				SelectObjDTO dto =  new SelectObjDTO();
				dto.setOggetto(comune);
				
				if(comune != null && !comune.equals("")){
					AmInstanceComune ic = comuneService.getInstanceComuneByComuneInstance((String) comune.getBelfiore(), instance);
					dto.setCheck(ic != null? true: false);
					dto.setInDb(ic != null? ic: null);
				}
				
				listaComuni.add(dto);
			}

		} catch (Throwable t) {
			super.addErrorMessage(msg + ".error", t.getMessage());
			t.printStackTrace();
		}
		
	}
	
	public void fillItemsInstance() {

		listaInstance = new ArrayList<SelectItem>();
		String msg = "listainstance";

		try {

			listaInstance = applicationService
					.getListaInstanceByApplication(labelApplicationSelected);
			
			if(listaInstance.size() > 0){
				labelInstanceSelected = (String) listaInstance.get(0).getValue();
			}else labelInstanceSelected = "";
			
			doCaricaDatiInstance();

		} catch (Throwable t) {
			super.addErrorMessage(msg + ".error", t.getMessage());
			t.printStackTrace();
		}

	}
	
	public void doCaricaDatiInstance(){
		
		doCaricaComuni(labelInstanceSelected);
		
	}
	
	public void doSaveNewInstance(){
		
		String msg = "salva";
		
		try {
						
			String instance = labelInstanceSelected;
			if("".equals(labelApplicationSelected)){
				applicationService.saveInstance(nuovaInstance);
				instance = nuovaInstance.getName();
			}
			
			for(SelectObjDTO dto: listaComuni){
				if(dto.isCheck() && dto.getInDb() == null){
					AmInstanceComune ic = new AmInstanceComune();
					AmInstanceComunePK icPK = new AmInstanceComunePK();
					icPK.setFkAmComune(((AmComune)dto.getOggetto()).getBelfiore());
					icPK.setFkAmInstance(instance);
					ic.setId(icPK);
					comuneService.saveInstanceComune(ic);
				}else if(!dto.isCheck() && dto.getInDb() != null){
					comuneService.deleteInstanceComune((AmInstanceComune) dto.getInDb());
				}
			}
			
			//update
			InstanceBean bean = (InstanceBean) getBeanReference("instanceBean");
			bean.fillItemsApplication();
			
			super.addInfoMessage(msg);
		} catch (Throwable t) {
			super.addErrorMessage(msg + ".error", t.getMessage());
			t.printStackTrace();
		}
		
	}

	public AmInstance getNuovaInstance() {
		return nuovaInstance;
	}

	public void setNuovaInstance(AmInstance nuovaInstance) {
		this.nuovaInstance = nuovaInstance;
	}

	public String getLabelInstanceSelected() {
		return labelInstanceSelected;
	}

	public void setLabelInstanceSelected(String labelInstanceSelected) {
		this.labelInstanceSelected = labelInstanceSelected;
	}

	public List<SelectObjDTO> getListaComuni() {
		return listaComuni;
	}

	public void setListaComuni(List<SelectObjDTO> listaComuni) {
		this.listaComuni = listaComuni;
	}

	public String getLabelApplicationSelected() {
		return labelApplicationSelected;
	}

	public void setLabelApplicationSelected(String labelApplicationSelected) {
		this.labelApplicationSelected = labelApplicationSelected;
	}

	public List<SelectItem> getListaInstance() {
		return listaInstance;
	}

	public void setListaInstance(List<SelectItem> listaInstance) {
		this.listaInstance = listaInstance;
	}
}
