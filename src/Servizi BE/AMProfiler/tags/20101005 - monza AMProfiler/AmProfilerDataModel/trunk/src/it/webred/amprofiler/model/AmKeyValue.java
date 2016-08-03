package it.webred.amprofiler.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the AM_KEY_VALUE database table.
 * 
 */
@Entity
@Table(name="AM_KEY_VALUE")
public class AmKeyValue implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

    @Temporal( TemporalType.DATE)
	private Date deleted;

	@Column(name="KEY_CONF")
	private String keyConf;

	@Column(name="KEY_INDEX")
	private int keyIndex;

	@Column(name="VALUE_CONF")
	private String valueConf;

	//bi-directional many-to-one association to AmSection
    @ManyToOne
	@JoinColumn(name="FK_AM_SECTION")
	private AmSection amSection;

    public AmKeyValue() {
    }

	public AmKeyValue(AmSection section, String key, String value) {
		amSection=section;
		keyConf=key;
		valueConf=value;
		
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDeleted() {
		return this.deleted;
	}

	public void setDeleted(Date deleted) {
		this.deleted = deleted;
	}

	public String getKeyConf() {
		return this.keyConf;
	}

	public void setKeyConf(String keyConf) {
		this.keyConf = keyConf;
	}

	public int getKeyIndex() {
		return this.keyIndex;
	}

	public void setKeyIndex(int keyIndex) {
		this.keyIndex = keyIndex;
	}

	public String getValueConf() {
		return this.valueConf;
	}

	public void setValueConf(String valueConf) {
		this.valueConf = valueConf;
	}

	public AmSection getAmSection() {
		return this.amSection;
	}

	public void setAmSection(AmSection amSection) {
		this.amSection = amSection;
	}
	
}