package it.webred.ct.service.tares.data.model;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name="SETAR_SEGNALA_3")
public class SetarSegnala3 extends BaseItem{

	private static final long serialVersionUID = 6802875967616295446L;
	
	private Long id = null;
	
	private String codiceFiscale = "";
	private String cognome = "";
	private String nome = "";
	private String telefono = "";
	private String email = "";
	private String ruolo = "";
	private Long segnala1Id = null;
	private SetarSegnala1 setarSegnala1 = null;
	private Long segnalazioneId = null;
	private Date dataInserimento = null;

	public SetarSegnala3() {
	
	}//-------------------------------------------------------------------------

	@Id
	@Column(name="ID")
	@SequenceGenerator(name="SeqSetar", sequenceName="SEQ_SETAR")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SeqSetar")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="CODICE_FISCALE")
	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	@Column(name="COGNOME")
	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	@Column(name="NOME")
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name="TELEFONO")
	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	@Column(name="EMAIL")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name="RUOLO")
	public String getRuolo() {
		return ruolo;
	}

	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Transient
	public SetarSegnala1 getSetarSegnala1() {
		return setarSegnala1;
	}

	public void setSetarSegnala1(SetarSegnala1 setarSegnala1) {
		this.setarSegnala1 = setarSegnala1;
	}

	@Column(name="DATA_INSERIMENTO")
	public Date getDataInserimento() {
		return dataInserimento;
	}

	public void setDataInserimento(Date dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	@Column(name="FK_SEGNALA_1")
	public Long getSegnala1Id() {
		return segnala1Id;
	}

	public void setSegnala1Id(Long segnala1Id) {
		this.segnala1Id = segnala1Id;
	}

	@Column(name="FK_SEGNALAZIONE")
	public Long getSegnalazioneId() {
		return segnalazioneId;
	}

	public void setSegnalazioneId(Long segnalazioneId) {
		this.segnalazioneId = segnalazioneId;
	}
	

}
