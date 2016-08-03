package it.webred.cs.data.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

/**
 * The persistent class for the CS_D_DIARIO database table.
 * 
 */
@Entity
@Table(name="CS_D_DIARIO")
@NamedQuery(name="CsDDiario.findAll", query="SELECT c FROM CsDDiario c")
public class CsDDiario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CS_D_DIARIO_ID_GENERATOR", sequenceName="SQ_ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CS_D_DIARIO_ID_GENERATOR")
	private long id;

	//bi-directional many-to-one association to CsACaso
	@ManyToOne
	@JoinColumn(name="CASO_ID")
	private CsACaso csACaso;
	
	//bi-directional one-to-one association to CsDColloquio
	@OneToOne(mappedBy="csDDiario")
	private CsDColloquio csDColloquio;
	
	//bi-directional one-to-one association to CsDColloquio
	@OneToOne(mappedBy="csDDiario", cascade={CascadeType.ALL})
	private CsDPai csDPai;

	//bi-directional one-to-one association to CsDNote
	@OneToOne(mappedBy="csDDiario")
	private CsDNote csDNote;

	//bi-directional one-to-one association to CsDRelazionePeriodica
	@OneToOne(mappedBy="csDDiario")
	private CsDRelazione csDRelazione;
	
	//bi-directional one-to-one association to CsDDocIndividuale
	@OneToOne(mappedBy="csDDiario")
	private CsDDocIndividuale csDDocIndividuale;
	
	//bi-directional one-to-one association to CsDScuola
	@OneToOne(mappedBy="csDDiario")
	private CsDScuola csDScuola;
	
	//bi-directional one-to-one association to CsFlgIntervento
	@OneToOne(mappedBy="csDDiario")
	private CsFlgIntervento csFlgIntervento;

	@ManyToOne
	@JoinColumn(name="TIPO_DIARIO_ID")
	private CsTbTipoDiario csTbTipoDiario;

	@ManyToOne
	@JoinColumn(name="OPERATORE_SETTORE_ID")
	private CsOOperatoreSettore csOOperatoreSettore;
	
	//bi-directional many-to-one association to CsDClob
	@ManyToOne
	@JoinColumn(name="CLOB_ID")
	private CsDClob csDClob;

	//bi-directional one-to-one association to CsDValutazione
	@OneToOne(mappedBy="csDDiario")
	private CsDValutazione csDValutazione;
	
	//bi-directional many-to-many association to CsDDiario
	@ManyToMany
	@JoinTable(
		name="CS_REL_DIARIO_DIARIORIF"
		, joinColumns={
			@JoinColumn(name="DIARIO_ID") 
			}
		, inverseJoinColumns={
			@JoinColumn(name="DIARIO_ID_RIF")
			}
		)
	private List<CsDDiario> csDDiariFiglio;
	
	//bi-directional many-to-many association to CsDDiario
	@ManyToMany(mappedBy="csDDiariFiglio")
	private List<CsDDiario> csDDiariPadre;

	//bi-directional one-to-many association to CsDDiarioDoc
	@OneToMany(mappedBy="csDDIario", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<CsDDiarioDoc> csDDiarioDocs;
	
	public CsDDiario() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public CsDColloquio getCsDColloquio() {
		return this.csDColloquio;
	}

	public void setCsDColloquio(CsDColloquio csDColloquio) {
		this.csDColloquio = csDColloquio;
	}

	public CsDNote getCsDNote() {
		return this.csDNote;
	}

	public void setCsDNote(CsDNote csDNote) {
		this.csDNote = csDNote;
	}

	public CsDRelazione getCsDRelazione() {
		return this.csDRelazione;
	}

	public void setCsDRelazione(CsDRelazione csDRelazione) {
		this.csDRelazione = csDRelazione;
	}

	public CsTbTipoDiario getCsTbTipoDiario() {
		return this.csTbTipoDiario;
	}

	public void setCsTbTipoDiario(CsTbTipoDiario csTbTipoDiario) {
		this.csTbTipoDiario = csTbTipoDiario;
	}
	
	public CsACaso getCsACaso() {
		return this.csACaso;
	}

	public void setCsACaso(CsACaso csACaso) {
		this.csACaso = csACaso;
	}

	public CsOOperatoreSettore getCsOOperatoreSettore() {
		return csOOperatoreSettore;
	}

	public void setCsOOperatoreSettore(CsOOperatoreSettore csOOperatoreSettore) {
		this.csOOperatoreSettore = csOOperatoreSettore;
	}
		
	public List<CsDDiario> getCsDDiariPadre() {
		return csDDiariPadre;
	}

	public void setCsDDiariPadre(List<CsDDiario> csDDiariPadre) {
		this.csDDiariPadre = csDDiariPadre;
	}

	public List<CsDDiario> getCsDDiariFiglio() {
		return csDDiariFiglio;
	}

	public void setCsDDiariFiglio(List<CsDDiario> csDDiariFiglio) {
		this.csDDiariFiglio = csDDiariFiglio;
	}

	public void addCsDDiariFiglio(CsDDiario csDDiario){
		if(this.csDDiariFiglio==null)
			this.csDDiariFiglio= new ArrayList<CsDDiario>();
		
		this.csDDiariFiglio.add(csDDiario);
	}
	
	public CsDClob getCsDClob() {
		return this.csDClob;
	}

	public void setCsDClob(CsDClob csDClob) {
		this.csDClob = csDClob;
	}

	public CsDValutazione getCsDValutazione() {
		return this.csDValutazione;
	}

	public void setCsDValutazione(CsDValutazione csDValutazione) {
		this.csDValutazione = csDValutazione;
	}

	public Set<CsDDiarioDoc> getCsDDiarioDocs() {
		return csDDiarioDocs;
	}

	public void setCsDDiarioDocs(Set<CsDDiarioDoc> csDDiarioDocs) {
		this.csDDiarioDocs = csDDiarioDocs;
	}

	public CsDPai getCsDPai() {
		return csDPai;
	}

	public void setCsDPai(CsDPai csDPai) {
		this.csDPai = csDPai;
	}

	public CsFlgIntervento getCsFlgIntervento() {
		return csFlgIntervento;
	}

	public void setCsFlgIntervento(CsFlgIntervento csFlgIntervento) {
		this.csFlgIntervento = csFlgIntervento;
	}

	public CsDDocIndividuale getCsDDocIndividuale() {
		return csDDocIndividuale;
	}

	public void setCsDDocIndividuale(CsDDocIndividuale csDDocIndividuale) {
		this.csDDocIndividuale = csDDocIndividuale;
	}

	public CsDScuola getCsDScuola() {
		return csDScuola;
	}

	public void setCsDScuola(CsDScuola csDScuola) {
		this.csDScuola = csDScuola;
	}

}