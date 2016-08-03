package it.webred.ct.service.ff.data.access.common.dto;

import it.webred.ct.data.access.basic.anagrafe.dto.SoggettoAnagrafeDTO;
import it.webred.ct.data.model.ici.SitTIciSogg;
import it.webred.ct.data.model.tarsu.SitTTarSogg;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;
import java.util.Date;

//bean dei dati per la lista
public class SoggettoDTO extends CeTBaseObject  implements Serializable, Comparable<SoggettoDTO>{
	private String codEnte;
	private String tipoSogg;
	private String desTblProv;
	//dati pf
	private String codFis;
	private String cognome;
	private String nome;
	private Date dtNas;
	private String codComNas;
	private String desComNas;
	//dati pg
	private String parIva;
	private String denom;
	
	@Override
	public int compareTo(SoggettoDTO another) {
		int retVal = -10;
		if (tipoSogg.equals("F")) {
			retVal=cognome.compareTo(another.cognome); 
			if (retVal == 0)
				retVal=nome.compareTo(another.nome); 
			if (retVal==0) {
				if (dtNas!=null && another.dtNas !=null)	
					retVal=dtNas.compareTo(another.dtNas); 
			}
			if (retVal==0)
				retVal=desComNas.compareTo(another.desComNas); 
	
		}
		if (tipoSogg.equals("G")) {
			retVal=denom.compareTo(another.denom); 
			if (retVal == 0)
				retVal=parIva.compareTo(another.parIva); 
		}
		return retVal;
	}

	public String getTipoSogg() {
		return tipoSogg;
	}

	public void setTipoSogg(String tipoSogg) {
		this.tipoSogg = tipoSogg;
	}

	public String getDesTblProv() {
		return desTblProv;
	}

	public void setDesTblProv(String desTblProv) {
		this.desTblProv = desTblProv;
	}

	public String getCodFis() {
		return codFis;
	}

	public void setCodFis(String codFis) {
		this.codFis = codFis;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDtNas() {
		return dtNas;
	}

	public void setDtNas(Date dtNas) {
		this.dtNas = dtNas;
	}

	public String getCodComNas() {
		return codComNas;
	}

	public void setCodComNas(String codComNas) {
		this.codComNas = codComNas;
	}

	public String getDesComNas() {
		return desComNas;
	}

	public void setDesComNas(String desComNas) {
		this.desComNas = desComNas;
	}

	public String getParIva() {
		return parIva;
	}

	public void setParIva(String parIva) {
		this.parIva = parIva;
	}

	public String getDenom() {
		return denom;
	}

	public void setDenom(String denom) {
		this.denom = denom;
	} 
	
	public String getCodEnte() {
		return codEnte;
	}

	public void setCodEnte(String codEnte) {
		this.codEnte = codEnte;
	}
	
	public void valDaAnagrafe(SoggettoAnagrafeDTO sogg) {
		tipoSogg="F";
		desTblProv="SIT_D_PERSONA";
		cognome= sogg.getCognome() !=null ? sogg.getCognome(): "";
		nome= sogg.getNome() != null ? sogg.getNome(): "";
		dtNas= sogg.getDtNas();
		codComNas= sogg.getIdExtComNas() !=null ? sogg.getIdExtComNas(): "";
		desComNas=sogg.getDesComNas() != null ? sogg.getDesComNas(): "";
		codFis=sogg.getCodFis() != null ? sogg.getCodFis(): "";
		
	}
	public void valDaIci(SitTIciSogg sogg) {
		tipoSogg=sogg.getTipSogg();
		desTblProv="SIT_T_ICI_SOGG";		
		if (tipoSogg.equals("F")) {
			cognome= sogg.getCogDenom() !=null ? sogg.getCogDenom() : "";
			nome= sogg.getNome() != null ? sogg.getNome(): "";
			dtNas= sogg.getDtNsc();
			codComNas= sogg.getCodCmnNsc()!= null ? sogg.getCodCmnNsc(): "";
			desComNas=sogg.getDesComNsc() != null ? sogg.getDesComNsc(): "";
			codFis=sogg.getCodFisc() != null ? sogg.getCodFisc(): "";
		}else {
			denom= sogg.getCogDenom()!=null? sogg.getCogDenom(): "";
			parIva= sogg.getPartIva() != null ?sogg.getPartIva(): ""; 			
		}
	}
	public void valDaTarsu(SitTTarSogg sogg) {
		tipoSogg=sogg.getTipSogg();
		desTblProv="SIT_T_TAR_SOGG";		
		if (tipoSogg.equals("F")) {
			cognome= sogg.getCogDenom() !=null ? sogg.getCogDenom() : "";
			nome= sogg.getNome() != null ? sogg.getNome(): "";
			dtNas= sogg.getDtNsc();
			codComNas= sogg.getCodCmnNsc()!= null ? sogg.getCodCmnNsc(): "";
			desComNas=sogg.getDesComNsc() != null ? sogg.getDesComNsc(): "";
			codFis=sogg.getCodFisc() != null ? sogg.getCodFisc(): "";
		}else {
			denom= sogg.getCogDenom()!=null? sogg.getCogDenom(): "";
			parIva= sogg.getPartIva() != null ?sogg.getPartIva(): ""; 		
		}
	}
}
