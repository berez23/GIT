package it.webred.fb.helper.checks;

import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.model.catasto.Sititrkc;
import it.webred.ejb.utility.ClientUtility;
import it.webred.fb.data.DataModelCostanti.Segnalibri.TipoAlert;
import it.webred.fb.ejb.dto.Alert;
import it.webred.fb.ejb.dto.DatoSpec;
import it.webred.fb.ejb.dto.TabellaDatiCollegati;
import it.webred.fb.ejb.dto.locazione.DatiCatastali;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class DatiTerreniChecker extends  IChecker<DatiCatastali> {
	
	DatiCatastali dato;
	List<Sititrkc> terreni;
	
	public DatiTerreniChecker(String ente) {
		super(ente);
		terreni = new ArrayList<Sititrkc>();	}

	protected  List<Alert> checkImpl() throws CheckerException {
		List<Alert> resp = new ArrayList<Alert>();
		checkTerreni(dato.getCodComune().getDato(),dato.getFoglio().getDato(), dato.getMappale().getDato(),resp);
		return resp;
	}
	
    protected boolean isCheckable()  {
		
		if ( ((DatiCatastali)dato).getMappale() ==null ||  ((DatiCatastali)dato).getMappale().getDato()==null)
			return false;
		
		return true;
		
	}
	
	protected void checkTerreni(String enteMappale, String foglio, String mappale, List<Alert> alerts) throws CheckerException {
		CatastoService ejb=null;
	        
		try {
			ejb = (CatastoService) ClientUtility.getEjbInterface("CT_Service", "CT_Service_Data_Access", "CatastoServiceBean");
		

			RicercaOggettoCatDTO roc = new RicercaOggettoCatDTO();
			roc.setEnteId(ente);
			roc.setCodEnte(enteMappale);
			roc.setFoglio(foglio);
			roc.setParticella(mappale);
			roc.setDtVal(new Date());
			terreni = ejb.getListaTerreniByFP(roc);
			if (terreni.isEmpty()){
				roc.setDtVal(null);
				//Verifico se esiste il mappale tra i terreni cessati
				terreni = ejb.getListaTerreniByFP(roc); 
				if (terreni.size()>0) 
					alerts.add(new Alert(TipoAlert.TERR_NOT_VALIDATE, "Mappale cessato in catasto terreni"));
				else
					alerts.add(new Alert(TipoAlert.NOT_VALIDATE, "Mappale non presente in catasto terreni"));
			}else{
				alerts.add(new Alert(TipoAlert.TERR_VALIDATE, "Mappale presente in catasto terreni"));
				packTerreniInDatiAggiuntivi();
			}
		    
		} catch (Exception e) {
			logger.error("Impossibile verificare il Mappale a causa della chiamata fallita su EJB di CT_Service");
			throw new CheckerException();
		}
	}
	
	private TabellaDatiCollegati packTerreniInDatiAggiuntivi() {
		TabellaDatiCollegati tab = new TabellaDatiCollegati("Dati da Catasto Terreni");
		tab.addNomeColonna("Sub");
		tab.addNomeColonna("Data Inizio");
		tab.addNomeColonna("Data Fine");
		tab.addNomeColonna("Classe");
		tab.addNomeColonna("Area HA");
		tab.addNomeColonna("Qualita'");
		tab.addNomeColonna("Redd.Agr.");
		tab.addNomeColonna("Redd.Dom.");
		tab.addNomeColonna("Annotazioni");

		for (Sititrkc sititrkc : terreni) {
			tab.addRiga(sititrkc.getId().getTrkcId().toString(), 
					new DatoSpec(sititrkc.getId().getSub()!=null ? sititrkc.getId().getSub() : "-"),
					new DatoSpec(formatData(sititrkc.getDataAggi())),
					new DatoSpec(formatData(sititrkc.getId().getDataFine())),
					new DatoSpec(sititrkc.getClasseTerreno()),
					new DatoSpec(sititrkc.getAreaPart()),
					new DatoSpec(sititrkc.getDescQualita()),
					new DatoSpec(sititrkc.getRedditoAgrario()), 	
					new DatoSpec(sititrkc.getRedditoDominicale()),
					new DatoSpec(sititrkc.getAnnotazioni())
					);	
		}	
		return tab;
	}
	
	
    protected LinkedList<TabellaDatiCollegati> collegaDati() {
		
		LinkedList<TabellaDatiCollegati> tabelle = new LinkedList<TabellaDatiCollegati>();
		
		if(!terreni.isEmpty())
			tabelle.add(packTerreniInDatiAggiuntivi());
		
		return tabelle;
		
	}
	
	@Override
	protected DatiCatastali getDato() {
		return dato;
	}

	@Override
	public void setDato(DatiCatastali dato) {
		this.dato = dato;
		
	}
}
