package it.webred.ct.diagnostics.service.data.dto;


import it.webred.ct.diagnostics.service.data.model.controller.DiaCatalogo;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;
import java.util.List;

public class DiaCatalogoDTO extends CeTBaseObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long idDiagnostica;
	private String codCommand;
	private Long tipoComando;
	private List<Long> fonti;
	
	
	private int start;
	private int maxrows;
	
	private DiaCatalogo objCatalogo;
	

	public DiaCatalogoDTO(DiaCatalogo objCatalogo,String enteId, String userId) {
		super();
		this.objCatalogo = objCatalogo;
		super.setEnteId(enteId);
		super.setUserId(userId);
	}


	public DiaCatalogo getObjCatalogo() {
		return objCatalogo;
	}


	public void setObjCatalogo(DiaCatalogo objCatalogo) {
		this.objCatalogo = objCatalogo;
	}


	public Long getIdDiagnostica() {
		return idDiagnostica;
	}


	public void setIdDiagnostica(Long idDiagnostica) {
		this.idDiagnostica = idDiagnostica;
	}


	public String getCodCommand() {
		return codCommand;
	}


	public void setCodCommand(String codCommand) {
		this.codCommand = codCommand;
	}


	public int getStart() {
		return start;
	}


	public void setStart(int start) {
		this.start = start;
	}


	public int getMaxrows() {
		return maxrows;
	}


	public void setMaxrows(int maxrows) {
		this.maxrows = maxrows;
	}



	public List<Long> getFonti() {
		return fonti;
	}


	public void setFonti(List<Long> fonti) {
		this.fonti = fonti;
	}


	public Long getTipoComando() {
		return tipoComando;
	}


	public void setTipoComando(Long tipoComando) {
		this.tipoComando = tipoComando;
	}
	
	

}
