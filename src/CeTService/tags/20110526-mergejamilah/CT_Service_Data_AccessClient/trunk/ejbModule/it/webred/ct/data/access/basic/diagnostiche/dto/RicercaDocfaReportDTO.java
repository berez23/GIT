package it.webred.ct.data.access.basic.diagnostiche.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.webred.ct.data.access.basic.docfa.dto.RicercaOggettoDocfaDTO;
import it.webred.ct.data.model.catasto.Sitideco;

public class RicercaDocfaReportDTO extends RicercaOggettoDocfaDTO {
	
	private String idRepDaEscludere;
	
	//parametri Docfa
	private Date fornitura;
	
	private String protocolloDocfa;
	
	private Date dataRegistrazioneDocfa;
	
	private String tipoOperDocfa;
	
	private String categoriaDocfa;
	
	private List<String> categorieDocfa = new ArrayList<String>();
	
	private String categoriaCatasto;
	
	private String causaleDocfa;
	
	private List<String> causaliDocfa = new ArrayList<String>();
	
	private String viaDocfa;
	
	private String civicoDocfa;
	
	private String ente;
	
	//parametri Flag
	private String flgElaborati;
	
	private String flgAnomali;
	
	private String tipoAnomalia;
	
	private String flgAnomaliClasse;

	private String flgPresPreDocfa;
	
	private String flgPresPostDocfa;
	
	private String flgPresCatDataDocfa;
	
	private String flgDocfaSopprNoCostit;
	
	private String flgVarCategoria;
	
	private String flgVarClasse;
	
	private String flgVarRendita;
	
	//parametri soggetto
	private RicercaDocfaReportSoggDTO titolare = new RicercaDocfaReportSoggDTO();

	//proprietà per visualizzazione tabella
	private int startm;
	
	private int numberRecord;

	public Date getFornitura() {
		return fornitura;
	}

	public void setFornitura(Date fornitura) {
		this.fornitura = fornitura;
	}

	public String getProtocolloDocfa() {
		return protocolloDocfa;
	}

	public void setProtocolloDocfa(String protocolloDocfa) {
		this.protocolloDocfa = protocolloDocfa;
	}

	public Date getDataRegistrazioneDocfa() {
		return dataRegistrazioneDocfa;
	}

	public void setDataRegistrazioneDocfa(Date dataRegistrazioneDocfa) {
		this.dataRegistrazioneDocfa = dataRegistrazioneDocfa;
	}

	public String getTipoOperDocfa() {
		return tipoOperDocfa;
	}

	public void setTipoOperDocfa(String tipoOperDocfa) {
		this.tipoOperDocfa = tipoOperDocfa;
	}

	public String getCategoriaDocfa() {
		return categoriaDocfa;
	}

	public void setCategoriaDocfa(String categoriaDocfa) {
		this.categoriaDocfa = categoriaDocfa;
	}

	public String getCausaleDocfa() {
		return causaleDocfa;
	}

	public void setCausaleDocfa(String causaleDocfa) {
		this.causaleDocfa = causaleDocfa;
	}

	public String getViaDocfa() {
		return viaDocfa;
	}

	public void setViaDocfa(String viaDocfa) {
		this.viaDocfa = viaDocfa;
	}

	public String getCivicoDocfa() {
		return civicoDocfa;
	}

	public void setCivicoDocfa(String civicoDocfa) {
		this.civicoDocfa = civicoDocfa;
	}

	public RicercaDocfaReportSoggDTO getTitolare() {
		return titolare;
	}

	public void setTitolare(RicercaDocfaReportSoggDTO titolare) {
		this.titolare = titolare;
	}

	public int getStartm() {
		return startm;
	}

	public void setStartm(int startm) {
		this.startm = startm;
	}

	public int getNumberRecord() {
		return numberRecord;
	}

	public void setNumberRecord(int numberRecord) {
		this.numberRecord = numberRecord;
	}

	public String getCategoriaCatasto() {
		return categoriaCatasto;
	}

	public void setCategoriaCatasto(String categoriaCatasto) {
		this.categoriaCatasto = categoriaCatasto;
	}

	public String getEnte() {
		return ente;
	}

	public void setEnte(String ente) {
		this.ente = ente;
	}

	public String getIdRepDaEscludere() {
		return idRepDaEscludere;
	}

	public void setIdRepDaEscludere(String idRepDaEscludere) {
		this.idRepDaEscludere = idRepDaEscludere;
	}

	public List<String> getCategorieDocfa() {
		return categorieDocfa;
	}

	public void setCategorieDocfa(List<String> categorieDocfa) {
		this.categorieDocfa = categorieDocfa;
	}

	public List<String> getCausaliDocfa() {
		return causaliDocfa;
	}

	public void setCausaliDocfa(List<String> causaliDocfa) {
		this.causaliDocfa = causaliDocfa;
	}

	public String getTipoAnomalia() {
		return tipoAnomalia;
	}

	public void setTipoAnomalia(String tipoAnomalia) {
		this.tipoAnomalia = tipoAnomalia;
	}

	public String getFlgElaborati() {
		return flgElaborati;
	}

	public void setFlgElaborati(String flgElaborati) {
		this.flgElaborati = flgElaborati;
	}

	public String getFlgAnomali() {
		return flgAnomali;
	}

	public void setFlgAnomali(String flgAnomali) {
		this.flgAnomali = flgAnomali;
	}

	public String getFlgAnomaliClasse() {
		return flgAnomaliClasse;
	}

	public void setFlgAnomaliClasse(String flgAnomaliClasse) {
		this.flgAnomaliClasse = flgAnomaliClasse;
	}

	public String getFlgPresPreDocfa() {
		return flgPresPreDocfa;
	}

	public void setFlgPresPreDocfa(String flgPresPreDocfa) {
		this.flgPresPreDocfa = flgPresPreDocfa;
	}

	public String getFlgPresPostDocfa() {
		return flgPresPostDocfa;
	}

	public void setFlgPresPostDocfa(String flgPresPostDocfa) {
		this.flgPresPostDocfa = flgPresPostDocfa;
	}

	public String getFlgPresCatDataDocfa() {
		return flgPresCatDataDocfa;
	}

	public void setFlgPresCatDataDocfa(String flgPresCatDataDocfa) {
		this.flgPresCatDataDocfa = flgPresCatDataDocfa;
	}

	public String getFlgDocfaSopprNoCostit() {
		return flgDocfaSopprNoCostit;
	}

	public void setFlgDocfaSopprNoCostit(String flgDocfaSopprNoCostit) {
		this.flgDocfaSopprNoCostit = flgDocfaSopprNoCostit;
	}

	public String getFlgVarCategoria() {
		return flgVarCategoria;
	}

	public void setFlgVarCategoria(String flgVarCategoria) {
		this.flgVarCategoria = flgVarCategoria;
	}

	public String getFlgVarClasse() {
		return flgVarClasse;
	}

	public void setFlgVarClasse(String flgVarClasse) {
		this.flgVarClasse = flgVarClasse;
	}

	public String getFlgVarRendita() {
		return flgVarRendita;
	}

	public void setFlgVarRendita(String flgVarRendita) {
		this.flgVarRendita = flgVarRendita;
	}

}
