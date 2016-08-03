package it.webred.gitland.web.bean.items;

import it.webred.gitland.data.model.Azienda;
import it.webred.gitland.data.model.AziendaLotto;
import it.webred.gitland.data.model.Lotto;
import it.webred.gitland.data.model.MasterItem;

import javax.persistence.Entity;

@Entity
public class Ricerca extends MasterItem{
	
	private static final long serialVersionUID = 6749471767002999494L;
	
	private Lotto lotto = new Lotto();
	private AziendaLotto aziendaLotto = new AziendaLotto();
	private Azienda azienda = new Azienda();

	public Ricerca() {
	}//-------------------------------------------------------------------------

	public Lotto getLotto() {
		return lotto;
	}

	public void setLotto(Lotto lotto) {
		this.lotto = lotto;
	}

	

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public AziendaLotto getAziendaLotto() {
		return aziendaLotto;
	}

	public void setAziendaLotto(AziendaLotto aziendaLotto) {
		this.aziendaLotto = aziendaLotto;
	}

	public Azienda getAzienda() {
		return azienda;
	}

	public void setAzienda(Azienda azienda) {
		this.azienda = azienda;
	}
	
}
