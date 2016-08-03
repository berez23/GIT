/*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.modellof24.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ModelloF24 extends EscObject implements Serializable
{
	private String chiave;
	private String id;
	private String tipoRecord;
	private String fornituraData;
	private String fornituraProgressivo;
	private String ripartizioneData;
	private String ripartizioneProgressivo;
	private String bonificoData;
	private String progressivoDelega;
	private String progressivoRiga;
	private String cdEnteRendiconto;
	private String tpEnte;
	private String cab;
	private String cdFiscaleContribuente;
	private String flagErroreCdFiscale;
	private String dataRiscossione;
	private String cdEnteComunale;
	private String cdTributo;
	private String flagErroreCdTributo;
	private String rateizzazione;
	private String annoRiferimento;
	private String flagErroreAnno;
	private String cdValuta;
	private String importoDebito;
	private String importoCredito;
	private String iciRavvedimento;
	private String iciImmobiliVariati;
	private String iciAcconto;
	private String iciSaldo;
	private String iciNFabbricati;
	private String flagErroreDatiIci;
	private String importoDetrazioneAbPr;
	private String cognomeDenominazione;
	private String nome;
	private String nascitaSesso;
	private String nascitaData;
	private String nascitaLuogo;
	private String nascitaProvincia;
	private String impostaTipo;
	private String accreditoImporto;
	private String accreditoTipoImposta;
	private String accreditoCdEnteComunale;
	private String identificazioneImportoAccredito;
	private String identificazioneCro;
	private String identificazioneDataAccreditamento;
	private String identificazioneDataRipOrig;
	private String identificazioneProgRipOrig;
	private String identificazioneDataBonificoOriginario;
	private String identificazioneTipoImposta;
	private String identificazioneCdEnteComunale;

	
	public String getChiave()
	{
		return (chiave);
	}

	public void setChiave(String chiave)
	{
		this.chiave = chiave;
	}
	
	public String getAnnoRiferimento()
	{
		if(annoRiferimento==null)
			return("-");
		else
			return annoRiferimento;
	}

	public void setAnnoRiferimento(String annoRiferimento)
	{
		this.annoRiferimento = annoRiferimento;
	}

	public String getBonificoData()
	{
		if(bonificoData==null)
			return("-");
		else
		return bonificoData;
	}

	public void setBonificoData(String bonificoData)
	{
		this.bonificoData = bonificoData;
	}

	public String getCab()
	{
		if(cab==null)
			return("-");
		else
		return cab;
	}

	public void setCab(String cab)
	{
		this.cab = cab;
	}

	public String getCdEnteComunale()
	{
		if(cdEnteComunale==null)
			return("-");
		else
		return cdEnteComunale;
	}

	public void setCdEnteComunale(String cdEnteComunale)
	{
		this.cdEnteComunale = cdEnteComunale;
	}

	public String getCdEnteRendiconto()
	{
		if(cdEnteRendiconto==null)
			return("-");
		else
		return cdEnteRendiconto;
	}

	public void setCdEnteRendiconto(String cdEnteRendiconto)
	{
		this.cdEnteRendiconto = cdEnteRendiconto;
	}

	public String getCdFiscaleContribuente()
	{
		if(cdFiscaleContribuente==null)
			return("-");
		else
		return cdFiscaleContribuente;
	}

	public void setCdFiscaleContribuente(String cdFiscaleContribuente)
	{
		this.cdFiscaleContribuente = cdFiscaleContribuente;
	}

	public String getCdTributo()
	{
		if(cdTributo==null)
			return("-");
		else
		return cdTributo;
	}

	public void setCdTributo(String cdTributo)
	{
		this.cdTributo = cdTributo;
	}

	public String getCdValuta()
	{
		if(cdValuta==null)
			return("-");
		else
		return cdValuta;
	}

	public void setCdValuta(String cdValuta)
	{
		this.cdValuta = cdValuta;
	}

	public String getCognomeDenominazione()
	{
		if(cognomeDenominazione==null)
			return("-");
		else
		return cognomeDenominazione;
	}

	public void setCognomeDenominazione(String cognomeDenominazione)
	{
		this.cognomeDenominazione = cognomeDenominazione;
	}

	public String getDataRiscossione()
	{
		if(dataRiscossione==null)
			return("-");
		else
		return dataRiscossione;
	}

	public void setDataRiscossione(String dataRiscossione)
	{
		this.dataRiscossione = dataRiscossione;
	}

	public String getFlagErroreAnno()
	{
		if(flagErroreAnno==null)
			return("-");
		else
		return flagErroreAnno;
	}

	public void setFlagErroreAnno(String flagErroreAnno)
	{
		this.flagErroreAnno = flagErroreAnno;
	}

	public String getFlagErroreCdFiscale()
	{
		if(flagErroreCdFiscale==null)
			return("-");
		else
		return flagErroreCdFiscale;
	}

	public void setFlagErroreCdFiscale(String flagErroreCdFiscale)
	{
		this.flagErroreCdFiscale = flagErroreCdFiscale;
	}

	public String getFlagErroreCdTributo()
	{
		if(flagErroreCdTributo==null)
			return("-");
		else
		return flagErroreCdTributo;
	}

	public void setFlagErroreCdTributo(String flagErroreCdTributo)
	{
		this.flagErroreCdTributo = flagErroreCdTributo;
	}

	public String getFlagErroreDatiIci()
	{
		if(flagErroreDatiIci==null)
			return("-");
		else
		return flagErroreDatiIci;
	}

	public void setFlagErroreDatiIci(String flagErroreDatiIci)
	{
		this.flagErroreDatiIci = flagErroreDatiIci;
	}

	public String getFornituraData()
	{
		if(fornituraData==null)
			return("-");
		else
		return fornituraData;
	}

	public void setFornituraData(String fornituraData)
	{
		this.fornituraData = fornituraData;
	}

	public String getFornituraProgressivo()
	{
		if(fornituraProgressivo==null)
			return("-");
		else
		return fornituraProgressivo;
	}

	public void setFornituraProgressivo(String fornituraProgressivo)
	{
		this.fornituraProgressivo = fornituraProgressivo;
	}

	public String getIciAcconto()
	{
		if(iciAcconto==null)
			return("-");
		else if(iciAcconto.equals("0"))
			return("NO");
		else
			return "SI";
	}

	public void setIciAcconto(String iciAcconto)
	{
		this.iciAcconto = iciAcconto;
	}

	public String getIciImmobiliVariati()
	{
		if(iciImmobiliVariati==null)
			return("-");
		else if(iciImmobiliVariati.equals("0"))
			return("NO");
		else
			return "SI";
	}

	public void setIciImmobiliVariati(String iciImmobiliVariati)
	{
		this.iciImmobiliVariati = iciImmobiliVariati;
	}

	public String getIciNFabbricati()
	{
		if(iciNFabbricati==null)
			return("-");
		else
		return iciNFabbricati;
	}

	public void setIciNFabbricati(String iciNFabbricati)
	{
		this.iciNFabbricati = iciNFabbricati;
	}

	public String getIciRavvedimento()
	{
		if(iciRavvedimento==null)
			return("-");
		else if(iciRavvedimento.equals("0"))
			return("NO");
		else
			return "SI";
	}

	public void setIciRavvedimento(String iciRavvedimento)
	{
		this.iciRavvedimento = iciRavvedimento;
	}

	public String getIciSaldo()
	{
		if(iciSaldo==null)
			return("-");
		else if(iciSaldo.equals("0"))
			return("NO");
		else
			return "SI";
	}

	public void setIciSaldo(String iciSaldo)
	{
		this.iciSaldo = iciSaldo;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getImportoCredito()
	{
		if(importoCredito==null)
			return("-");
		else
		return importoCredito;
	}

	public void setImportoCredito(String importoCredito)
	{
		this.importoCredito = importoCredito;
	}

	public String getImportoDebito()
	{
		if(importoDebito==null)
			return("-");
		else
		return importoDebito;
	}

	public void setImportoDebito(String importoDebito)
	{
		this.importoDebito = importoDebito;
	}

	public String getImportoDetrazioneAbPr()
	{
		if(importoDetrazioneAbPr==null)
			return("-");
		else
		return importoDetrazioneAbPr;
	}

	public void setImportoDetrazioneAbPr(String importoDetrazioneAbPr)
	{
		this.importoDetrazioneAbPr = importoDetrazioneAbPr;
	}

	public String getImpostaTipo()
	{
		if(impostaTipo==null)
			return("-");
		else if(impostaTipo.equals("I"))
			return("ICI");
		else if(impostaTipo.equals("T"))
			return("TOSAP");
		else
			return "COSAP";
	}

	public void setImpostaTipo(String impostaTipo)
	{
		this.impostaTipo = impostaTipo;
	}

	public String getNascitaData()
	{
		if(nascitaData==null)
			return("-");
		else
		return nascitaData;
	}

	public void setNascitaData(String nascitaData)
	{
		this.nascitaData = nascitaData;
	}

	public String getNascitaLuogo()
	{
		if(nascitaLuogo==null)
			return("-");
		else
		return nascitaLuogo;
	}

	public void setNascitaLuogo(String nascitaLuogo)
	{
		this.nascitaLuogo = nascitaLuogo;
	}

	public String getNascitaProvincia()
	{
		if(nascitaProvincia==null)
			return("-");
		else
		return nascitaProvincia;
	}

	public void setNascitaProvincia(String nascitaProvincia)
	{
		this.nascitaProvincia = nascitaProvincia;
	}

	public String getNascitaSesso()
	{
		if(nascitaSesso==null)
			return("-");
		else
		return nascitaSesso;
	}

	public void setNascitaSesso(String nascitaSesso)
	{
		this.nascitaSesso = nascitaSesso;
	}

	public String getNome()
	{
		if(nome==null)
			return("-");
		else
		return nome;
	}

	public void setNome(String nome)
	{
		this.nome = nome;
	}

	public String getProgressivoDelega()
	{
		if(progressivoDelega==null)
			return("-");
		else
		return progressivoDelega;
	}

	public void setProgressivoDelega(String progressivoDelega)
	{
		this.progressivoDelega = progressivoDelega;
	}

	public String getProgressivoRiga()
	{
		if(progressivoRiga==null)
			return("-");
		else
		return progressivoRiga;
	}

	public void setProgressivoRiga(String progressivoRiga)
	{
		this.progressivoRiga = progressivoRiga;
	}

	public String getRateizzazione()
	{
		if(rateizzazione==null)
			return("-");
		else
		return rateizzazione;
	}

	public void setRateizzazione(String rateizzazione)
	{
		this.rateizzazione = rateizzazione;
	}

	public String getRipartizioneData()
	{
		if(ripartizioneData==null)
			return("-");
		else
		return ripartizioneData;
	}

	public void setRipartizioneData(String ripartizioneData)
	{
		this.ripartizioneData = ripartizioneData;
	}

	public String getRipartizioneProgressivo()
	{
		if(ripartizioneProgressivo==null)
			return("-");
		else
		return ripartizioneProgressivo;
	}

	public void setRipartizioneProgressivo(String ripartizioneProgressivo)
	{
		this.ripartizioneProgressivo = ripartizioneProgressivo;
	}

	public String getTipoRecord()
	{
		if(tipoRecord==null)
			return("-");
		else
		return tipoRecord;
	}

	public void setTipoRecord(String tipoRecord)
	{
		this.tipoRecord = tipoRecord;
	}

	public String getTpEnte()
	{
		if(tpEnte==null)
			return("-");
		else if(tpEnte.equals("B"))
			return("Banca");
		else if(tpEnte.equals("C"))
			return("Conc.");
		else if(tpEnte.equals("P"))
			return("Ag.Postale");
		else if(tpEnte.equals("I"))
			return("Internet");
		else
			return "";
	}

	public void setTpEnte(String tpEnte)
	{
		this.tpEnte = tpEnte;
	}

	public String getAccreditoImporto()
	{
		return accreditoImporto;
	}

	public void setAccreditoImporto(String accreditoImporto)
	{
		this.accreditoImporto = accreditoImporto;
	}

	public String getAccreditoTipoImposta()
	{
		if(accreditoTipoImposta==null)
			return("-");
		else if(accreditoTipoImposta.equals("I"))
			return("ICI");
		else if(accreditoTipoImposta.equals("T"))
			return("TOSAP");
		else if (accreditoTipoImposta.equals("C"))
			return "COSAP";
		else 
			return("");
	}

	public void setAccreditoTipoImposta(String accreditoTipoImposta)
	{
		this.accreditoTipoImposta = accreditoTipoImposta;
	}

	public String getIdentificazioneCro()
	{
		if(identificazioneCro==null)
			return("-");
		else
		return identificazioneCro;
	}

	public void setIdentificazioneCro(String identificazioneCro)
	{
		this.identificazioneCro = identificazioneCro;
	}

	public String getIdentificazioneDataAccreditamento()
	{
		if(identificazioneDataAccreditamento==null)
			return("-");
		else
		return identificazioneDataAccreditamento;
	}

	public void setIdentificazioneDataAccreditamento(String identificazioneDataAccreditamento)
	{
		this.identificazioneDataAccreditamento = identificazioneDataAccreditamento;
	}

	public String getIdentificazioneDataBonificoOriginario()
	{
		if(identificazioneDataBonificoOriginario==null)
			return("-");
		else
		return identificazioneDataBonificoOriginario;
	}

	public void setIdentificazioneDataBonificoOriginario(String identificazioneDataBonificoOriginario)
	{
		this.identificazioneDataBonificoOriginario = identificazioneDataBonificoOriginario;
	}

	public String getIdentificazioneDataRipOrig()
	{
		if(identificazioneDataRipOrig==null)
			return("-");
		else
		return identificazioneDataRipOrig;
	}

	public void setIdentificazioneDataRipOrig(String identificazioneDataRipOrig)
	{
		this.identificazioneDataRipOrig = identificazioneDataRipOrig;
	}

	public String getIdentificazioneImportoAccredito()
	{
		if(identificazioneImportoAccredito==null)
			return("-");
		else
		return identificazioneImportoAccredito;
	}

	public void setIdentificazioneImportoAccredito(String identificazioneImportoAccredito)
	{
		this.identificazioneImportoAccredito = identificazioneImportoAccredito;
	}

	public String getIdentificazioneProgRipOrig()
	{
		if(identificazioneProgRipOrig==null)
			return("-");
		else
		return identificazioneProgRipOrig;
	}

	public void setIdentificazioneProgRipOrig(String identificazioneProgRipOrig)
	{
		this.identificazioneProgRipOrig = identificazioneProgRipOrig;
	}

	public String getIdentificazioneTipoImposta()
	{
		if(identificazioneTipoImposta==null)
			return("-");
		else if(identificazioneTipoImposta.equals("I"))
			return("ICI");
		else if(identificazioneTipoImposta.equals("T"))
			return("TOSAP");
		else if(identificazioneTipoImposta.equals(" "))
			return(" ");
		else
			return "COSAP";
	}

	public void setIdentificazioneTipoImposta(String identificazioneTipoImposta)
	{
		this.identificazioneTipoImposta = identificazioneTipoImposta;
	}

	public String getIdentificazioneCdEnteComunale()
	{
		return identificazioneCdEnteComunale;
	}

	public void setIdentificazioneCdEnteComunale(String identificazioneCdEnteComunale)
	{
		this.identificazioneCdEnteComunale = identificazioneCdEnteComunale;
	}

	public String getAccreditoCdEnteComunale()
	{
		return accreditoCdEnteComunale;
	}

	public void setAccreditoCdEnteComunale(String accreditoCdEnteComunale)
	{
		this.accreditoCdEnteComunale = accreditoCdEnteComunale;
	}


}
