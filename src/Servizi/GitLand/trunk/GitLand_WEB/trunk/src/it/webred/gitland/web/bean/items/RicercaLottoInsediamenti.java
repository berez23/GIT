package it.webred.gitland.web.bean.items;

import java.util.ArrayList;
import java.util.List;

import it.webred.gitland.data.model.Azienda;
import it.webred.gitland.data.model.AziendaLotto;
import it.webred.gitland.data.model.Lotto;
import it.webred.gitland.data.model.MasterItem;

import javax.persistence.Entity;

@Entity
public class RicercaLottoInsediamenti extends MasterItem{
	
	private static final long serialVersionUID = 6749471767002999494L;
	
	private Lotto lotto = new Lotto();
	private List<Insediamento> insediamenti=new  ArrayList<Insediamento>();  

	public RicercaLottoInsediamenti() {
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

	public List<Insediamento> getInsediamenti() {
		return insediamenti;
	}

	public void setInsediamenti(List<Insediamento> insediamenti) {
		this.insediamenti = insediamenti;
	}

}
