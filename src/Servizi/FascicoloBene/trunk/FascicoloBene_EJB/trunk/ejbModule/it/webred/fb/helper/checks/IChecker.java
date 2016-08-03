package it.webred.fb.helper.checks;

import it.webred.fb.data.DataModelCostanti.Segnalibri.TipoAlert;
import it.webred.fb.ejb.dto.Alert;
import it.webred.fb.ejb.dto.BeneInListaDTO;
import it.webred.fb.ejb.dto.Dato;
import it.webred.fb.ejb.dto.DatoSpec;
import it.webred.fb.ejb.dto.TabellaDatiCollegati;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

public abstract class IChecker<T extends Dato> {
	  SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
	  protected static Logger logger = Logger.getLogger("fascicolobene.log");
	  protected String ente;
	  
	  public  IChecker (String ente){
		  this.ente = ente;
	  }
	  
	  public  Dato check() throws Exception  {
		 
			  if (!this.isCheckable() )
				  getDato().getDatoArricchito().getAlerts().add(new Alert(TipoAlert.NOT_ENOUGH_DATA , "Dati insuff per i controlli di congruita'"));
			  else
				  getDato().getDatoArricchito().addAlert(checkImpl());
			  
			  LinkedList<TabellaDatiCollegati> tabelle = new LinkedList<TabellaDatiCollegati>();
			  LinkedList<TabellaDatiCollegati> tabParziali = collegaDati();
			  if(tabParziali!=null)
				  tabelle.addAll(tabParziali);
			  
			  for (TabellaDatiCollegati tabella : tabelle) {
				  getDato().getDatoArricchito().addTabellaDatiCollegati(tabella);
			  }

			  return getDato();
	  }
	  
		protected TabellaDatiCollegati packInventariInDatiAggiuntivi(List<BeneInListaDTO> inventari){
			TabellaDatiCollegati tab = new TabellaDatiCollegati("Altri Inventari in cui è presente");
			tab.addNomeColonna("Cod.Inventario");
			tab.addNomeColonna("Cat.Inventario");
			tab.addNomeColonna("Tipo Bene");
			tab.addNomeColonna("Diritti Reali");
			tab.addNomeColonna("Descrizione");

			for (BeneInListaDTO b : inventari) {
				
				String lstDiritti="";
				for(String s : b.getDirittiReali())
					lstDiritti+= s + "<br/>";
				
				String descrizione=b.getBene().getDescrizione();
				descrizione = descrizione!=null && descrizione.length()>300 ? descrizione.substring(0,30)+"[...]" : descrizione;
				
				tab.addRiga(Long.toString(b.getBene().getId()), 
						new DatoSpec(b.getBene().getDmBBeneInv().getCodInventario()),
						new DatoSpec(b.getBene().getDmBBeneInv().getDesCatInventariale()),
						new DatoSpec(b.getBene().getDesTipoBene()),
						new DatoSpec(lstDiritti),
						new DatoSpec(descrizione)
						);	
			}	
			return tab;
		}
		
		protected String formatData(Date data){
			String dtIni = "01/01/0001";
			String dtFine = "31/12/9999";
			
			String sData = data!=null ? SDF.format(data) : "-";
			sData = sData.equalsIgnoreCase(dtIni) ? "-" : sData; 
			sData = sData.equalsIgnoreCase(dtFine) ? "ATTUALE" : sData; 
			return sData;
		}

	  protected abstract T getDato();
	  public abstract void setDato(T dato);
	  protected  abstract List<Alert> checkImpl() throws CheckerException;
	  protected abstract LinkedList<TabellaDatiCollegati> collegaDati();
	  protected abstract boolean isCheckable() throws CheckerException  ;	



	  
}
