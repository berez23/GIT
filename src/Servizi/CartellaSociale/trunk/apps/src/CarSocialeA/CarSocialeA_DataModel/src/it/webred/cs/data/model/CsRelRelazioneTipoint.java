package it.webred.cs.data.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the CS_REL_RELAZIONE_TIPOINT database table.
 * 
 */
@Entity
@Table(name="CS_REL_RELAZIONE_TIPOINT")
@NamedQuery(name="CsRelRelazioneTipoint.findAll", query="SELECT c FROM CsRelRelazioneTipoint c")
public class CsRelRelazioneTipoint implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private CsRelRelazioneTipointPK id;
	
	//uni-directional many-to-one association to CsCTipoIntervento
	@ManyToOne
	@JoinColumn(name="TIPO_INTERVENTO_ID", insertable=false, updatable=false)
	private CsCTipoIntervento csCTipoIntervento;

	//uni-directional many-to-one association to CsDRelazione
	@ManyToOne
	@JoinColumn(name="RELAZIONE_DIARIO_ID", insertable=false, updatable=false)
	private CsDRelazione csDRelazione;

	public CsRelRelazioneTipoint() {
	}

	public CsCTipoIntervento getCsCTipoIntervento() {
		return this.csCTipoIntervento;
	}

	public void setCsCTipoIntervento(CsCTipoIntervento csCTipoIntervento) {
		this.csCTipoIntervento = csCTipoIntervento;
	}

	public CsDRelazione getCsDRelazione() {
		return this.csDRelazione;
	}

	public void setCsDRelazione(CsDRelazione csDRelazione) {
		this.csDRelazione = csDRelazione;
	}

	public CsRelRelazioneTipointPK getId() {
		return id;
	}

	public void setId(CsRelRelazioneTipointPK id) {
		this.id = id;
	}

}