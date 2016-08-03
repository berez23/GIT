package it.webred.ct.data.model.diagnostiche;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * The persistent class for the DOCFA_ICI_REPORT database table.
 * 
 */
@Entity
@Table(name="DOCFA_ICI_REPORT")
public class DocfaIciReport implements Serializable {
	private static final long serialVersionUID = 1L;

	@Transient
	private String annotazioni;

	@Column(name="CATEGORIA_CATASTO")
	private String categoriaCatasto;

	@Column(name="CATEGORIA_DOCFA")
	private String categoriaDocfa;

	@Column(name="CAUSALE_DOCFA")
	private String causaleDocfa;

	@Column(name="CIVICI_DOCFA")
	private String civiciDocfa;

	@Column(name="CIVICO_CATASTO")
	private String civicoCatasto;

	@Column(name="CLASSE_CATASTO")
	private String classeCatasto;

	@Column(name="CLASSE_DOCFA")
	private String classeDocfa;

	@Column(name="CLASSE_MIN")
	private String classeMin;

	@Column(name="COD_ANOMALIE")
	private String codAnomalie;

	@Column(name="COD_ENTE")
	private String codEnte;

	@Column(name="COD_VIA_CATASTO")
	private BigDecimal codViaCatasto;

	@Column(name="CONSISTENZA_DOCFA")
	private String consistenzaDocfa;

    @Temporal( TemporalType.DATE)
	@Column(name="DATA_AGGIORNAMENTO")
	private Date dataAggiornamento;

	@Column(name="DATA_DOCFA")
	private String dataDocfa;

	@Column(name="DATA_DOCFA_SUCCESSIVO")
	private String dataDocfaSuccessivo;

	@Column(name="DIC_COGNOME")
	private String dicCognome;

	@Column(name="DIC_NOME")
	private String dicNome;

	@Column(name="DIC_RES_CAP")
	private String dicResCap;

	@Column(name="DIC_RES_COM")
	private String dicResCom;

	@Column(name="DIC_RES_INDIR")
	private String dicResIndir;

	@Column(name="DIC_RES_NUMCIV")
	private String dicResNumciv;

	@Column(name="DIC_RES_PROV")
	private String dicResProv;

	@Column(name="DOCFA_CONTEMPORANEI")
	private String docfaContemporanei;

	@Column(name="DOCFA_IN_ANNO")
	private String docfaInAnno;

	@Column(name="FLG_ANOMALIA_CLASSE")
	private String flgAnomaliaClasse;

	@Column(name="FLG_ANOMALIE")
	private String flgAnomalie;

	@Column(name="FLG_DOCFA_S_NO_C")
	private String flgDocfaSNoC;

	@Column(name="FLG_ELABORATO")
	private String flgElaborato;

	@Column(name="FLG_ICI_ANTE_DOCFA")
	private String flgIciAnteDocfa;

	@Column(name="FLG_ICI_POST_DOCFA")
	private String flgIciPostDocfa;

	@Column(name="FLG_INDIRIZZO_IN_TOPONOMASTICA")
	private String flgIndirizzoInToponomastica;

	@Column(name="FLG_UIU_CATASTO")
	private String flgUiuCatasto;

	@Column(name="FLG_VAR_ANTE_CATEGORIA")
	private String flgVarAnteCategoria;

	@Column(name="FLG_VAR_ANTE_CLASSE")
	private String flgVarAnteClasse;

	@Column(name="FLG_VAR_ANTE_RENDITA")
	private String flgVarAnteRendita;

	@Column(name="FLG_VAR_POST_CATEGORIA")
	private String flgVarPostCategoria;

	@Column(name="FLG_VAR_POST_CLASSE")
	private String flgVarPostClasse;

	@Column(name="FLG_VAR_POST_RENDITA")
	private String flgVarPostRendita;

	private String foglio;

    @Temporal( TemporalType.DATE)
	private Date fornitura;

    @Id
	@Column(name="ID_REP")
	private String idRep;

	private String particella;

	@Column(name="PREFISSO_VIA_CATASTO")
	private String prefissoViaCatasto;

	@Column(name="PREFISSO_VIA_DOCFA")
	private String prefissoViaDocfa;

	@Column(name="PROTOCOLLO_DOCFA")
	private String protocolloDocfa;

	@Column(name="RENDITA_CATASTO")
	private String renditaCatasto;

	@Column(name="RENDITA_DOCFA")
	private String renditaDocfa;

	private String sezione;

	private String subalterno;

	@Column(name="TIPO_OPER_DOCFA")
	private String tipoOperDocfa;

	@Column(name="UIU_COSTITUITE")
	private BigDecimal uiuCostituite;

	@Column(name="UIU_SOPPRESSE")
	private BigDecimal uiuSoppresse;

	@Column(name="UIU_VARIATE")
	private BigDecimal uiuVariate;

	@Column(name="VIA_CATASTO")
	private String viaCatasto;

	@Column(name="VIA_DOCFA")
	private String viaDocfa;

    public DocfaIciReport() {
    }

	public String getAnnotazioni() {
		return this.annotazioni;
	}

	public void setAnnotazioni(String annotazioni) {
		this.annotazioni = annotazioni;
	}

	public String getCategoriaCatasto() {
		return this.categoriaCatasto;
	}

	public void setCategoriaCatasto(String categoriaCatasto) {
		this.categoriaCatasto = categoriaCatasto;
	}

	public String getCategoriaDocfa() {
		return this.categoriaDocfa;
	}

	public void setCategoriaDocfa(String categoriaDocfa) {
		this.categoriaDocfa = categoriaDocfa;
	}

	public String getCausaleDocfa() {
		return this.causaleDocfa;
	}

	public void setCausaleDocfa(String causaleDocfa) {
		this.causaleDocfa = causaleDocfa;
	}

	public String getCiviciDocfa() {
		return this.civiciDocfa;
	}

	public void setCiviciDocfa(String civiciDocfa) {
		this.civiciDocfa = civiciDocfa;
	}

	public String getCivicoCatasto() {
		return this.civicoCatasto;
	}

	public void setCivicoCatasto(String civicoCatasto) {
		this.civicoCatasto = civicoCatasto;
	}

	public String getClasseCatasto() {
		return this.classeCatasto;
	}

	public void setClasseCatasto(String classeCatasto) {
		this.classeCatasto = classeCatasto;
	}

	public String getClasseDocfa() {
		return this.classeDocfa;
	}

	public void setClasseDocfa(String classeDocfa) {
		this.classeDocfa = classeDocfa;
	}

	public String getClasseMin() {
		return this.classeMin;
	}

	public void setClasseMin(String classeMin) {
		this.classeMin = classeMin;
	}

	public String getCodAnomalie() {
		return this.codAnomalie;
	}

	public void setCodAnomalie(String codAnomalie) {
		this.codAnomalie = codAnomalie;
	}

	public String getCodEnte() {
		return this.codEnte;
	}

	public void setCodEnte(String codEnte) {
		this.codEnte = codEnte;
	}

	public BigDecimal getCodViaCatasto() {
		return this.codViaCatasto;
	}

	public void setCodViaCatasto(BigDecimal codViaCatasto) {
		this.codViaCatasto = codViaCatasto;
	}

	public String getConsistenzaDocfa() {
		return this.consistenzaDocfa;
	}

	public void setConsistenzaDocfa(String consistenzaDocfa) {
		this.consistenzaDocfa = consistenzaDocfa;
	}

	public Date getDataAggiornamento() {
		return this.dataAggiornamento;
	}

	public void setDataAggiornamento(Date dataAggiornamento) {
		this.dataAggiornamento = dataAggiornamento;
	}

	public String getDataDocfa() {
		return this.dataDocfa;
	}
	
	public Date getDataDocfaToDate() {
		if (this.dataDocfa != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			try {
				return sdf.parse(this.dataDocfa);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public void setDataDocfa(String dataDocfa) {
		this.dataDocfa = dataDocfa;
	}

	public String getDataDocfaSuccessivo() {
		return this.dataDocfaSuccessivo;
	}

	public void setDataDocfaSuccessivo(String dataDocfaSuccessivo) {
		this.dataDocfaSuccessivo = dataDocfaSuccessivo;
	}

	public String getDicCognome() {
		return this.dicCognome;
	}

	public void setDicCognome(String dicCognome) {
		this.dicCognome = dicCognome;
	}

	public String getDicNome() {
		return this.dicNome;
	}

	public void setDicNome(String dicNome) {
		this.dicNome = dicNome;
	}

	public String getDicResCap() {
		return this.dicResCap;
	}

	public void setDicResCap(String dicResCap) {
		this.dicResCap = dicResCap;
	}

	public String getDicResCom() {
		return this.dicResCom;
	}

	public void setDicResCom(String dicResCom) {
		this.dicResCom = dicResCom;
	}

	public String getDicResIndir() {
		return this.dicResIndir;
	}

	public void setDicResIndir(String dicResIndir) {
		this.dicResIndir = dicResIndir;
	}

	public String getDicResNumciv() {
		return this.dicResNumciv;
	}

	public void setDicResNumciv(String dicResNumciv) {
		this.dicResNumciv = dicResNumciv;
	}

	public String getDicResProv() {
		return this.dicResProv;
	}

	public void setDicResProv(String dicResProv) {
		this.dicResProv = dicResProv;
	}

	public String getDocfaContemporanei() {
		return this.docfaContemporanei;
	}

	public void setDocfaContemporanei(String docfaContemporanei) {
		this.docfaContemporanei = docfaContemporanei;
	}

	public String getDocfaInAnno() {
		return this.docfaInAnno;
	}

	public void setDocfaInAnno(String docfaInAnno) {
		this.docfaInAnno = docfaInAnno;
	}

	public String getFlgAnomaliaClasse() {
		return this.flgAnomaliaClasse;
	}

	public void setFlgAnomaliaClasse(String flgAnomaliaClasse) {
		this.flgAnomaliaClasse = flgAnomaliaClasse;
	}

	public String getFlgAnomalie() {
		return this.flgAnomalie;
	}

	public void setFlgAnomalie(String flgAnomalie) {
		this.flgAnomalie = flgAnomalie;
	}

	public String getFlgDocfaSNoC() {
		return this.flgDocfaSNoC;
	}

	public void setFlgDocfaSNoC(String flgDocfaSNoC) {
		this.flgDocfaSNoC = flgDocfaSNoC;
	}

	public String getFlgElaborato() {
		return this.flgElaborato;
	}

	public void setFlgElaborato(String flgElaborato) {
		this.flgElaborato = flgElaborato;
	}

	public String getFlgIciAnteDocfa() {
		return this.flgIciAnteDocfa;
	}

	public void setFlgIciAnteDocfa(String flgIciAnteDocfa) {
		this.flgIciAnteDocfa = flgIciAnteDocfa;
	}

	public String getFlgIciPostDocfa() {
		return this.flgIciPostDocfa;
	}

	public void setFlgIciPostDocfa(String flgIciPostDocfa) {
		this.flgIciPostDocfa = flgIciPostDocfa;
	}

	public String getFlgIndirizzoInToponomastica() {
		return this.flgIndirizzoInToponomastica;
	}

	public void setFlgIndirizzoInToponomastica(String flgIndirizzoInToponomastica) {
		this.flgIndirizzoInToponomastica = flgIndirizzoInToponomastica;
	}

	public String getFlgUiuCatasto() {
		return this.flgUiuCatasto;
	}

	public void setFlgUiuCatasto(String flgUiuCatasto) {
		this.flgUiuCatasto = flgUiuCatasto;
	}

	public String getFlgVarAnteCategoria() {
		return this.flgVarAnteCategoria;
	}

	public void setFlgVarAnteCategoria(String flgVarAnteCategoria) {
		this.flgVarAnteCategoria = flgVarAnteCategoria;
	}

	public String getFlgVarAnteClasse() {
		return this.flgVarAnteClasse;
	}

	public void setFlgVarAnteClasse(String flgVarAnteClasse) {
		this.flgVarAnteClasse = flgVarAnteClasse;
	}

	public String getFlgVarAnteRendita() {
		return this.flgVarAnteRendita;
	}

	public void setFlgVarAnteRendita(String flgVarAnteRendita) {
		this.flgVarAnteRendita = flgVarAnteRendita;
	}

	public String getFlgVarPostCategoria() {
		return this.flgVarPostCategoria;
	}

	public void setFlgVarPostCategoria(String flgVarPostCategoria) {
		this.flgVarPostCategoria = flgVarPostCategoria;
	}

	public String getFlgVarPostClasse() {
		return this.flgVarPostClasse;
	}

	public void setFlgVarPostClasse(String flgVarPostClasse) {
		this.flgVarPostClasse = flgVarPostClasse;
	}

	public String getFlgVarPostRendita() {
		return this.flgVarPostRendita;
	}

	public void setFlgVarPostRendita(String flgVarPostRendita) {
		this.flgVarPostRendita = flgVarPostRendita;
	}

	public String getFoglio() {
		return this.foglio;
	}

	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}

	public Date getFornitura() {
		return this.fornitura;
	}

	public void setFornitura(Date fornitura) {
		this.fornitura = fornitura;
	}

	public String getIdRep() {
		return this.idRep;
	}

	public void setIdRep(String idRep) {
		this.idRep = idRep;
	}

	public String getParticella() {
		return this.particella;
	}

	public void setParticella(String particella) {
		this.particella = particella;
	}

	public String getPrefissoViaCatasto() {
		return this.prefissoViaCatasto;
	}

	public void setPrefissoViaCatasto(String prefissoViaCatasto) {
		this.prefissoViaCatasto = prefissoViaCatasto;
	}

	public String getPrefissoViaDocfa() {
		return this.prefissoViaDocfa;
	}

	public void setPrefissoViaDocfa(String prefissoViaDocfa) {
		this.prefissoViaDocfa = prefissoViaDocfa;
	}

	public String getProtocolloDocfa() {
		return this.protocolloDocfa;
	}

	public void setProtocolloDocfa(String protocolloDocfa) {
		this.protocolloDocfa = protocolloDocfa;
	}

	public String getRenditaCatasto() {
		return this.renditaCatasto;
	}

	public void setRenditaCatasto(String renditaCatasto) {
		this.renditaCatasto = renditaCatasto;
	}

	public String getRenditaDocfa() {
		return this.renditaDocfa;
	}

	public void setRenditaDocfa(String renditaDocfa) {
		this.renditaDocfa = renditaDocfa;
	}

	public String getSezione() {
		return this.sezione;
	}

	public void setSezione(String sezione) {
		this.sezione = sezione;
	}

	public String getSubalterno() {
		return this.subalterno;
	}

	public void setSubalterno(String subalterno) {
		this.subalterno = subalterno;
	}

	public String getTipoOperDocfa() {
		return this.tipoOperDocfa;
	}
	
	public String getTipoOperDocfaEx() {
		String tipoOp = "";
		if (this.tipoOperDocfa != null) {
			if (this.tipoOperDocfa.equals("S"))
				tipoOp = "Soppressione";
			if (this.tipoOperDocfa.equals("C"))
				tipoOp = "Costituzione";
			if (this.tipoOperDocfa.equals("V"))
				tipoOp = "Variazione";
		}
		return tipoOp;
	}

	public void setTipoOperDocfa(String tipoOperDocfa) {
		this.tipoOperDocfa = tipoOperDocfa;
	}

	public BigDecimal getUiuCostituite() {
		return this.uiuCostituite;
	}

	public void setUiuCostituite(BigDecimal uiuCostituite) {
		this.uiuCostituite = uiuCostituite;
	}

	public BigDecimal getUiuSoppresse() {
		return this.uiuSoppresse;
	}

	public void setUiuSoppresse(BigDecimal uiuSoppresse) {
		this.uiuSoppresse = uiuSoppresse;
	}

	public BigDecimal getUiuVariate() {
		return this.uiuVariate;
	}

	public void setUiuVariate(BigDecimal uiuVariate) {
		this.uiuVariate = uiuVariate;
	}

	public String getViaCatasto() {
		return this.viaCatasto;
	}

	public void setViaCatasto(String viaCatasto) {
		this.viaCatasto = viaCatasto;
	}

	public String getViaDocfa() {
		return this.viaDocfa;
	}

	public void setViaDocfa(String viaDocfa) {
		this.viaDocfa = viaDocfa;
	}

}