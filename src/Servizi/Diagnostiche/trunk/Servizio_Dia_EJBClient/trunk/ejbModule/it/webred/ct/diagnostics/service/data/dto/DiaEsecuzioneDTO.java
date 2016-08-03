package it.webred.ct.diagnostics.service.data.dto;


import it.webred.ct.diagnostics.service.data.model.controller.DiaCatalogo;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DiaEsecuzioneDTO extends CeTBaseObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	// Proprietà relative alla diagnostica
	private long idCatalogoDia;
	private String codCommandGrp;
	private String descrizione;
	private String codCommand;
	private String codCmdOrCmdGrp;
	
	//subtable
	private List<DiaTestataDTO> listaEsecuzioni = new ArrayList<DiaTestataDTO>();
	
	public DiaEsecuzioneDTO(DiaCatalogo objDia,String enteId, String userId){
		this.idCatalogoDia = objDia.getId();
		this.codCommandGrp = objDia.getCodCommandGrp();
		this.codCommand = objDia.getCodCommand();
		this.descrizione = objDia.getDescrizione();		
		
		super.setEnteId(enteId);
		super.setUserId(userId);
	}
	
	public String getCodCmdOrCmdGrp() {
		if (codCommandGrp != null &&  codCommandGrp.length() > 0)
			return codCommandGrp;
		else if (codCommand != null &&  codCommand.length() > 0)
			return codCommand;
		else
		return "";
	}

	public void setCodCmdOrCmdGrp(String codCmdOrCmdGrp) {
		this.codCmdOrCmdGrp = codCmdOrCmdGrp;
	}

	public long getIdCatalogoDia() {
		return idCatalogoDia;
	}
	public void setIdCatalogoDia(long idCatalogoDia) {
		this.idCatalogoDia = idCatalogoDia;
	}
	public String getCodCommandGrp() {
		return codCommandGrp;
	}
	public void setCodCommandGrp(String codCommandGrp) {
		this.codCommandGrp = codCommandGrp;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getCodCommand() {
		return codCommand;
	}
	public void setCodCommand(String codCommand) {
		this.codCommand = codCommand;
	}
	public List<DiaTestataDTO> getListaEsecuzioni() {
		return listaEsecuzioni;
	}
	public void setListaEsecuzioni(List<DiaTestataDTO> listaEsecuzioni) {
		this.listaEsecuzioni = listaEsecuzioni;
	}

	public void addEsecuzioni(DiaTestataDTO objDto) {
		if (objDto == null) return;
		if (listaEsecuzioni == null) listaEsecuzioni = new ArrayList<DiaTestataDTO>();
		listaEsecuzioni.add(objDto);		
	}
	
}
