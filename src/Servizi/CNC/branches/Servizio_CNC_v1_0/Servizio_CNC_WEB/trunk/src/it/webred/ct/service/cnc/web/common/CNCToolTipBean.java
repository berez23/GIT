package it.webred.ct.service.cnc.web.common;

import java.io.Serializable;

import it.webred.ct.service.cnc.web.CNCBaseBean;

public class CNCToolTipBean extends CNCBaseBean implements Serializable {
	
	private String descr;
	private String codTipoUfficio;
	private String codEnte;
	
	public void setAmbitoCode(String codeToResolve) {
		try {
			//System.out.println("Code To Resolve ["+codeToResolve+"]");
			descr = super.getCommonService().getAmbitoDescr(Long.parseLong(codeToResolve));
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
	public void setCodiceEntrataCode(String codeToResolve) {
		try {
			//System.out.println("Code To Resolve ["+codeToResolve+"]");
			descr = super.getCommonService().getCodiceEntrataDescr(codeToResolve);
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
	public void setCodiceTipoEntrataCode(String codeToResolve) {
		try {
			//System.out.println("Code To Resolve ["+codeToResolve+"]");
			descr = super.getCommonService().getCodiceTipoEntrataDescr(codeToResolve);
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
	public void setCodiceUfficio(String codUfficio) {
		try {
			//System.out.println("Code To Resolve ["+codUfficio+"] - CodEnte ["+codEnte+"] - ["+codTipoUfficio+"]");
			descr = super.getCommonService().getCodiceUfficioDescr(codUfficio, codEnte, codTipoUfficio);
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
	}
		
	public void setCodiceUfficioDaPartita(String partita) {
		try {
			//System.out.println("Partita ["+partita+"] - CodEnte ["+codEnte+"]");
			descr = super.getCommonService().getCodiceUfficioDescrDaPartita(codEnte, partita);
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
	public void setCodiceUfficioFull(String codUfficio) {
		try {
			//System.out.println("Partita ["+codUfficio+"] - CodEnte ["+codEnte+"] - CodTipoUfficio ["+codTipoUfficio+"]");
			descr = super.getCommonService().getCodiceUfficioDescrFull(codEnte, codTipoUfficio, codUfficio);
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
