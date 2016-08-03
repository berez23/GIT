package it.webred.ct.diagnostics.service.data.dto;

import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;

public class DiaFindKeysDTO extends CeTBaseObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Object beanBridge;

	public DiaFindKeysDTO(String enteId, String userId) {
		super();		
		super.setEnteId(enteId);
		super.setUserId(userId);
	}
	
	public Object getBeanBridge() {
		return beanBridge;
	}

	public void setBeanBridge(Object beanBridge) {
		this.beanBridge = beanBridge;
	}
	
	
	
	private Long[] 	  idFonti;
	private String[]  tipoFonti;
	private Object[]  keysForFound;
	private String beanClassName;
	private String idFonte;
	private String tipoFonte;
	private Long id;

	public Long[] getIdFonti() {
		return idFonti;
	}

	public void setIdFonti(Long[] idFonti) {
		this.idFonti = idFonti;
	}

	public String[] getTipoFonti() {
		return tipoFonti;
	}

	public void setTipoFonti(String[] tipoFonti) {
		this.tipoFonti = tipoFonti;
	}

	public Object[] getKeysForFound() {
		return keysForFound;
	}

	public void setKeysForFound(Object[] keysForFound) {
		this.keysForFound = keysForFound;
	}

	public String getBeanClassName() {
		return beanClassName;
	}

	public void setBeanClassName(String beanClassName) {
		this.beanClassName = beanClassName;
	}

	public String getIdFonte() {
		return idFonte;
	}

	public void setIdFonte(String idFonte) {
		this.idFonte = idFonte;
	}

	public String getTipoFonte() {
		return tipoFonte;
	}

	public void setTipoFonte(String tipoFonte) {
		this.tipoFonte = tipoFonte;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
