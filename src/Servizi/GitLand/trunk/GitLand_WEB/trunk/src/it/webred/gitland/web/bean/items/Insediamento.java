package it.webred.gitland.web.bean.items;

import it.webred.gitland.data.model.Azienda;
import it.webred.gitland.data.model.AziendaLotto;

public class Insediamento {
	private AziendaLotto aziendaLotto = new AziendaLotto();
	private Azienda azienda = new Azienda();
	
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
