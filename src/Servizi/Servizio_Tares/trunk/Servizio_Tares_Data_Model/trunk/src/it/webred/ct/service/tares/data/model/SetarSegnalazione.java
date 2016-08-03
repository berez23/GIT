package it.webred.ct.service.tares.data.model;

import java.sql.Date;
import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import it.webred.ct.service.tares.data.model.BaseItem;
import it.webred.ct.service.tares.data.model.SetarSegnala1;
import it.webred.ct.service.tares.data.model.SetarSegnala2;
import it.webred.ct.service.tares.data.model.SetarSegnala3;

@Entity
@Table(name="SETAR_SEGNALAZIONI")
public class SetarSegnalazione extends BaseItem{

	private static final long serialVersionUID = 4293215327020097195L;

	private Long id = null;
	private String nomeFile = "";
	private String nomeZip = "";
	private String pacchetti = "";
	private Boolean scaricato = false;
	private Long progressivo = new Long(0);
	private Long prog = new Long(0);
	private String codiceAmministrativo = "";
	private Date dataInserimento = null;
	
	private SetarSegnala1 segnala1 = null;
	private ArrayList<SetarSegnala2> lstSegnala2 = null;
	private SetarSegnala3 segnala3 = null;
	

	public SetarSegnalazione() {
	}//-------------------------------------------------------------------------

	@Transient
	public SetarSegnala1 getSegnala1() {
		return segnala1;
	}

	public void setSegnala1(SetarSegnala1 segnala1) {
		this.segnala1 = segnala1;
	}

	@Transient
	public ArrayList<SetarSegnala2> getLstSegnala2() {
		return lstSegnala2;
	}

	public void setLstSegnala2(ArrayList<SetarSegnala2> lstSegnala2) {
		this.lstSegnala2 = lstSegnala2;
	}

	@Transient
	public SetarSegnala3 getSegnala3() {
		return segnala3;
	}

	public void setSegnala3(SetarSegnala3 segnala3) {
		this.segnala3 = segnala3;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

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

	@Column(name="NOME_FILE")
	public String getNomeFile() {
		return nomeFile;
	}

	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}

	@Column(name="NOME_ZIP")
	public String getNomeZip() {
		return nomeZip;
	}

	public void setNomeZip(String nomeZip) {
		this.nomeZip = nomeZip;
	}

	@Column(name="PACCHETTI")
	public String getPacchetti() {
		return pacchetti;
	}

	public void setPacchetti(String pacchetti) {
		this.pacchetti = pacchetti;
	}

	@Column(name="SCARICATO")
	public Boolean getScaricato() {
		return scaricato;
	}

	public void setScaricato(Boolean scaricato) {
		this.scaricato = scaricato;
	}

	@Column(name="PROGRESSIVO")
	public Long getProgressivo() {
		return progressivo;
	}

	public void setProgressivo(Long progressivo) {
		this.progressivo = progressivo;
	}

	@Column(name="PROG")
	public Long getProg() {
		return prog;
	}

	public void setProg(Long prog) {
		this.prog = prog;
	}

	@Column(name="CODICE_AMMINISTRATIVO")
	public String getCodiceAmministrativo() {
		return codiceAmministrativo;
	}

	public void setCodiceAmministrativo(String codiceAmministrativo) {
		this.codiceAmministrativo = codiceAmministrativo;
	}

	@Column(name="DATA_INSERIMENTO")
	public Date getDataInserimento() {
		return dataInserimento;
	}

	public void setDataInserimento(Date dataInserimento) {
		this.dataInserimento = dataInserimento;
	}	
	

}
