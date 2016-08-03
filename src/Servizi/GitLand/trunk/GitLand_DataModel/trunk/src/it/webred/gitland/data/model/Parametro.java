package it.webred.gitland.data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "PARAMETRI")
public class Parametro extends MasterItem {
	private static final long serialVersionUID = -2617316055709171129L;

	public static final String DATA_ULTIMA_SINCRONIZZAZIONE = "DATA_ULTIMA_SINCRONIZZAZIONE";
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="parametro_seq")
	@SequenceGenerator(	name="parametro_seq", sequenceName="SEQ_GITLAND")
	@Column(name = "ID", unique = true, nullable = false)
	private Long id = 0l;
	
	@Column(name="CHIAVE")
	private String chiave="";

	@Column(name="VALORE")
	private String valore="";

	@Column(name="BELFIORE")
	private String belfiore="";

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getChiave() {
		return chiave;
	}

	public void setChiave(String chiave) {
		this.chiave = chiave;
	}

	public String getValore() {
		return valore;
	}

	public void setValore(String valore) {
		this.valore = valore;
	}

	public String getBelfiore() {
		return belfiore;
	}

	public void setBelfiore(String belfiore) {
		this.belfiore = belfiore;
	}

}
