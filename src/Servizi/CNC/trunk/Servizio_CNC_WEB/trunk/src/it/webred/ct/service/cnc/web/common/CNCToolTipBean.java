package it.webred.ct.service.cnc.web.common;

import java.io.Serializable;

import it.webred.ct.data.access.basic.cnc.CNCDTO;
import it.webred.ct.service.cnc.web.CNCBaseBean;

public class CNCToolTipBean extends CNCBaseBean implements Serializable {
	
	private String descr;
	private String codTipoUfficio;
	private String codEnte;
	
	public void setAmbitoCode(String codeToResolve) {
		try {
			//System.out.println("Code To Resolve ["+codeToResolve+"]");
			CNCDTO dto = new CNCDTO();
			fillEnte(dto);
			dto.setCodAmbito(Long.parseLong(codeToResolve));
			descr = super.getCommonService().getAmbitoDescr(dto);
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
	public void setCodiceEntrataCode(String codeToResolve) {
		try {
			CNCDTO dto = new CNCDTO();
			fillEnte(dto);
			dto.setCodEntrata(codeToResolve);
			//System.out.println("Code To Resolve ["+codeToResolve+"]");
			descr = super.getCommonService().getCodiceEntrataDescr(dto);
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
	public void setCodiceTipoEntrataCode(String codeToResolve) {
		try {
			CNCDTO dto = new CNCDTO();
			fillEnte(dto);
			dto.setCodTipoEntrata(codeToResolve);
			//System.out.println("Code To Resolve ["+codeToResolve+"]");
			descr = super.getCncCommonService().getCodiceTipoEntrataDescr(dto);
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
	public void setCodiceUfficio(String codUfficio) {
		try {
			CNCDTO dto = new CNCDTO();
			fillEnte(dto);
			dto.setCodUfficio(codUfficio);
			dto.setCodiceEnte(codEnte);
			dto.setCodiceTipoUfficio(codTipoUfficio);
			//System.out.println("Code To Resolve ["+codUfficio+"] - CodEnte ["+codEnte+"] - ["+codTipoUfficio+"]");
			descr = super.getCncCommonService().getCodiceUfficioDescr(dto);
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
	}
		
	public void setCodiceUfficioDaPartita(String partita) {
		try {
			CNCDTO dto = new CNCDTO();
			fillEnte(dto);
			dto.setCodiceEnte(codEnte);
			dto.setCodicePartita(partita);
			//System.out.println("Partita ["+partita+"] - CodEnte ["+codEnte+"]");
			descr = super.getCncCommonService().getCodiceUfficioDescrDaPartita(dto);
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
	public void setCodiceUfficioFull(String codUfficio) {
		try {
			CNCDTO dto = new CNCDTO();
			fillEnte(dto);
			dto.setCodiceEnte(codEnte);
			dto.setCodiceTipoUfficio(codTipoUfficio);
			dto.setCodUfficio(codUfficio);
			//System.out.println("Partita ["+codUfficio+"] - CodEnte ["+codEnte+"] - CodTipoUfficio ["+codTipoUfficio+"]");
			descr = super.getCncCommonService().getCodiceUfficioDescrFull(dto);
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
	}	
	

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getCodTipoUfficio() {
		return codTipoUfficio;
	}

	public void setCodTipoUfficio(String codTipoUfficio) {
		this.codTipoUfficio = codTipoUfficio;
	}

	public String getCodEnte() {
		return codEnte;
	}

	public void setCodEnte(String codEnte) {
		this.codEnte = codEnte;
	}
	
	

}
