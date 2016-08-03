package it.webred.gitland.web.bean.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import it.webred.ct.data.access.basic.pgt.dto.CataloghiDataIn;
import it.webred.ct.data.model.pgt.PgtSqlLayer;
import it.webred.gitland.data.model.Azienda;
import it.webred.gitland.data.model.Comune;
import it.webred.gitland.data.model.GlAsi;
import it.webred.gitland.data.model.Lotto;
import it.webred.gitland.data.model.LottoCoordinate;
import it.webred.gitland.data.model.Parametro;
import it.webred.gitland.data.model.Tipologia;
import it.webred.gitland.ejb.client.GitLandSessionBeanRemote;
import it.webred.gitland.ejb.dto.FiltroDTO;
import it.webred.gitland.web.bean.GitLandBaseBean;
import it.webred.gitland.web.bean.items.AziendaView;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;



























import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.primefaces.event.SelectEvent;

@ManagedBean
@SessionScoped
public class SincronizzaLottiBean extends GitLandBaseBean {
	
	private String lottiDaAggiornare="0";
	private String lottiDaInserire="0";
	private String lottiDaEliminare="0";
	private String lottiAggiornati="0";
	private String lottiInseriti="0";
	private String lottiEliminati="0";
	private String lottiInErrore="0";
	private List<Lotto>lottiDaElim = new ArrayList<Lotto>();
	private List<Object[]>lottiDaInse = new ArrayList<Object[]>();
	private List<ElementoLotto>lottiDaModi = new ArrayList<ElementoLotto>();
	private Hashtable<String , List<LottoCoordinate>> coordinateDaInse =new Hashtable<String, List<LottoCoordinate>>();
	private List<String[]>listaEsiti = new ArrayList<String[]>();
	private Boolean sincronizzazioneAbilitata=false;
	
	private class ElementoLotto{
		private Lotto lottoGitLand;
		private Lotto lottoGis;
		
		public Lotto getLottoGitLand() {
			return lottoGitLand;
		}
		public ElementoLotto(Lotto lottoGitLand, Lotto lottoGis) {
			super();
			this.lottoGitLand = lottoGitLand;
			this.lottoGis = lottoGis;
		}
		public void setLottoGitLand(Lotto lottoGitLand) {
			this.lottoGitLand = lottoGitLand;
		}
		public Lotto getLottoGis() {
			return lottoGis;
		}
		public void setLottoGis(Lotto lottoGis) {
			this.lottoGis = lottoGis;
		}
		
	}
	public SincronizzaLottiBean() {
		logger.info(SincronizzaLottiBean.class + ".COSTRUTTORE");

	}//-------------------------------------------------------------------------
	
	public void init(){
		logger.info(SincronizzaLottiBean.class + ".init");

		lottiAggiornati="0";
		lottiDaAggiornare="0";
		lottiDaInserire="0";
		lottiDaEliminare="0";
		sincronizzazioneAbilitata=false;
		lottiAggiornati="0";
		lottiInErrore="0";
		lottiInseriti="0";
		lottiEliminati="0";
		
		verificaSituazione();
		
	}//-------------------------------------------------------------------------
	
	private void verificaSituazione() {
		String sql="select N_ADDETTI, ID_ASI, PARTICELLA, COMUNE, to_number(SHAPE_LENGTH) SHAPE_LENG, to_number(SHAPE_AREA) SHAPE_AREA, NOTE from CAT_CENS_AZIENDALE order by ID_ASI";
		CataloghiDataIn dto = new CataloghiDataIn();
	    dto.setTable("CAT_CENS_AZIENDALE");
	    dto.setEnteId(getEnte());
	    dto.setUserId(getCurrUserName());
	    dto.setQry(sql);
		List<Object[]> lstLotti= getPgtService().getListaSql(dto);
		
		//String sql="select N_ADDETTI, ID_ASI, PARTICELLE, COMUNE, SHAPE_LENG, SHAPE_AREA, NOTE from DIOGENE_F704_GIT.CAT_CENS_AZIENDALE order by ID_ASI";
//		String sql="select N_ADDETTI, ID_ASI, PARTICELLA, COMUNE, to_number(SHAPE_LENGTH) SHAPE_LENG, to_number(SHAPE_AREA) SHAPE_AREA, NOTE from DIOGENE_F704_GIT.CAT_CENS_AZIENDALE order by ID_ASI";
//		List<Object[]> lstLotti= getGitLandService().getList(sql,false);
//
		Hashtable htQry = new Hashtable();
		ArrayList<FiltroDTO> alFiltri = new ArrayList<FiltroDTO>();
		FiltroDTO f1= new FiltroDTO();
		f1.setAttributo("belfiore");
		f1.setParametro("belfiore");
		f1.setOperatore("=");
		f1.setValore( getEnte() );
		alFiltri.add(f1);
		
		htQry.put("where", alFiltri);
		//Carico i lotti presenti in gitland ed elimino quelli cessati
		List<Lotto> lstLottiCensiti= getGitLandService().getList(htQry, Lotto.class);
		lottiDaElim = new ArrayList<Lotto>();
		for (Lotto ll : lstLottiCensiti) {
			if(ll.getCessato()==null || !ll.getCessato())lottiDaElim.add(ll);
		}
		lstLottiCensiti= new ArrayList<Lotto>(lottiDaElim);
		
		lottiDaInse = new ArrayList<Object[]>();
		coordinateDaInse = new Hashtable<String, List<LottoCoordinate>>();
		lottiDaModi = new ArrayList<ElementoLotto>();
		listaEsiti = new ArrayList<String[]>();
		
		if (lstLotti != null && lstLotti.size()>0){
			for(int i=0; i<lstLotti.size(); i++){
				Object[] lotto = lstLotti.get(i);
				Long codiceAsi=Long.parseLong(lotto[1].toString());
				//Cerco il match del lotto corrente
				boolean trovato=false;
				Lotto lottoTrovato=null;
				System.out.println(lotto[1]);
				for (Lotto l : lstLottiCensiti) {
					if(l.getCodiceAsi()!=null && lotto[1]!=null && l.getCodiceAsi().toString().equals(lotto[1].toString())){
						lottoTrovato=l;
						lottiDaElim.remove(l);
						trovato=true;
					}
				}
				if(!trovato){
					lottiDaInse.add(lotto);
					String[] log= new String[]{""+lotto[1],"Lotto non trovato su GitLand"};
					listaEsiti.add(log);
				}
				if (lotto[2] != null){
					System.out.println("COORDINATE: " + lotto[2]);
					String seqLotto = lotto[1].toString();
					String lottoCoord = lotto[2].toString();
					String[] aryCoords = lottoCoord.split("]");
					
					List<LottoCoordinate> coordinateLottoGIS = new ArrayList<LottoCoordinate>();
					HashSet<String> coordinatePresenti= new HashSet<String>();
					/*
					 * so che da F a P c'è sempre un unico valore numerico che è 
					 * il foglio e che da P a ] ci possono essere piu valori numerici 
					 * divisi anche dallo spazio es. index 0 =[F. 54 - P. 223]; index 1 = [F: 50 P: 777 - 777 - 777]
					 */
					if (aryCoords != null && aryCoords.length>0){
						for (int j=0; j<aryCoords.length; j++){
							String gruppo = aryCoords[j];
							/*
							 * Estrapoliamo il foglio per questo gruppo
							 */
							String f = gruppo.substring(0, gruppo.indexOf("P"));
							String foglio = "";
							StringBuffer sbf = new StringBuffer(f);		
							for(int index=0; index<sbf.length(); index++){
								Character c = sbf.charAt(index);
								if (Character.isDigit(c)){
									foglio+=c.toString();
								}
							}
							/*
							 * Estrapoliamo le particelle per il gruppo corrente
							 */
							String p = gruppo.substring(gruppo.indexOf("P")+1, gruppo.length());
							StringBuffer sbp = new StringBuffer(p);
							String particella = "";
							for(int index=0; index<sbp.length(); index++){
								Character c = sbp.charAt(index);
								if (Character.isDigit(c)){
									particella+=c.toString();
								}else{
									if (particella != null && particella.length()>0){
										/*
										 * c'è il cambio di particella 
										 */
										LottoCoordinate lc= new LottoCoordinate();
										lc.setFoglio(foglio);;
										lc.setParticella(particella);
										if(!coordinatePresenti.contains(foglio+"§"+particella)){
											coordinateLottoGIS.add(lc);
											lc.setBelfiore(getEnte());
											lc.setCodiceAsi(codiceAsi);
											coordinatePresenti.add(foglio+"§"+particella);
										}
										particella = "";
									}else{
										/*
										 * il cambio di particella è già
										 * stato fatto per cui nulla da 
										 * aggiungere
										 */
										particella = "";
									}
								}
							}
							/*
							 * Se dopo l'ultimo numero della particella non 
							 * ci sono altri caratteri deve aggiungere le 
							 * coordinate qui sotto
							 */
							if (particella != null && particella.length()>0){
								/*
								 * ultima particella 
								 */
								LottoCoordinate lc= new LottoCoordinate();
								lc.setFoglio(foglio);;
								lc.setParticella(particella);
								lc.setBelfiore(getEnte());
								lc.setCodiceAsi(codiceAsi);
								if(!coordinatePresenti.contains(foglio+"§"+particella)){
									coordinateLottoGIS.add(lc);
									coordinatePresenti.add(foglio+"§"+particella);
								}
								
								particella = "";
							}
						}
					}
					System.out.println("#PARTICELLE TROVATE: " + coordinateLottoGIS.size());
					//Se il lotto esiste già lo comparo con quello esistente per verificare se è cambiato
					if(trovato){
						Lotto lottoGIS= valorizzaLotto(lotto, coordinateLottoGIS);
						if(!lottiUguali(lottoTrovato, lottoGIS)){
							lottiDaModi.add(new ElementoLotto(lottoTrovato,lottoGIS));
						}else{
							logger.info("Lotto non modificato "+lottoGIS.getCodiceAsi());
						}
					}else{
						coordinateDaInse.put(lotto[1].toString(), coordinateLottoGIS);
					}
				}else{
					//Se il lotto esiste già lo comparo con quello esistente per verificare se è cambiato
					if(trovato){
						Lotto lottoGIS= valorizzaLotto(lotto, new ArrayList<LottoCoordinate>());
						if(!lottiUguali(lottoTrovato, lottoGIS)){
							lottiDaModi.add(new ElementoLotto(lottoTrovato,lottoGIS));
						}else{
							logger.info("Lotto non modificato "+lottoGIS.getCodiceAsi());
						}
					}else{
						coordinateDaInse.put(lotto[1].toString(), new ArrayList<LottoCoordinate>());
					}
					
				}
			}
		}
		for (Lotto lde : lottiDaElim) {
			String[] log= new String[]{""+lde.getCodiceAsi(),"Lotto da Eliminare!","Lotto da Eliminare!"};
			listaEsiti.add(log);
			
		}
		//Una volta caricati i lotti da verificare li ciclo per compararli
		lottiDaAggiornare=lottiDaModi.size()+"";
		lottiDaInserire=lottiDaInse.size()+"";
		lottiDaEliminare=lottiDaElim.size()+"";
		

	}

	private Lotto valorizzaLotto(Object[] lotto,
			List<LottoCoordinate> coordinateLottoGIS) {
		// contenuto array lotto:
		// 0=N_ADDETTI, 1=ID_ASI, 2=PARTICELLE, 3=COMUNE, 4=SHAPE_LENG, 5=SHAPE_AREA, 6=NOTE
		Lotto esito= new Lotto();
		Long valLong=null; 
		Double valDouble=null;
		esito.setBelfiore(getEnte());
		//Num addetti
		if(lotto[0]!=null){
			valLong=Long.parseLong(lotto[0].toString());
			esito.setNumAddetti(valLong);
		}
		//Id Asi Lotto
		if(lotto[1]!=null){
			valLong=Long.parseLong(lotto[1].toString());
			esito.setCodiceAsi(valLong);
		}
		//Comune
		if(lotto[3]!=null){
			esito.setIstat(lotto[3].toString());
		}
		//il perimetro
		if(lotto[4]!=null){
			valDouble=Double.parseDouble(lotto[4].toString());
			esito.setPerimetro(valDouble);
		}
		//l'area
		if(lotto[5]!=null){
			valDouble=Double.parseDouble(lotto[5].toString());
			esito.setArea(valDouble);
		}
		// le note
		if(lotto[6]!=null){
			esito.setNote(lotto[6].toString());
		}else{
			esito.setNote(null);
		}
		esito.setCoordinate(new ArrayList<LottoCoordinate>());
		//le coordinate 
		for (LottoCoordinate lottoCoordinata : coordinateLottoGIS) {
			lottoCoordinata.setLotto(esito);
			esito.getCoordinate().add(lottoCoordinata);
		}
		return esito;
	}

	private boolean lottiUguali(Lotto lottoTrovato, Lotto lottoGIS) {
		boolean esito=true;
		String messaggioErrori="";
		// Controllo se i lotti sono uguali
		//lo stesso numero di addetti
		if(lottoTrovato.getNumAddetti()!=null && !lottoTrovato.getNumAddetti().equals(lottoGIS.getNumAddetti())){
			messaggioErrori+="Numero Addetti diverso GITLAND["+lottoTrovato.getNumAddetti()+"]-["+lottoGIS.getNumAddetti()+"]GIS\n";
			esito= false;
		}
		if(lottoTrovato.getNumAddetti()==null && lottoGIS.getNumAddetti()!=null){
			messaggioErrori+="Numero Addetti diverso GITLAND["+lottoTrovato.getNumAddetti()+"]-["+lottoGIS.getNumAddetti()+"]GIS\n";
			esito= false;
		}
		
		//lo stesso codice asi
		if(lottoTrovato.getCodiceAsi()!=null && !lottoTrovato.getCodiceAsi().equals(lottoGIS.getCodiceAsi())){
			messaggioErrori+="Codice Asi diverso GITLAND["+lottoTrovato.getCodiceAsi()+"]-["+lottoGIS.getCodiceAsi()+"]GIS\n";
			esito= false;
		}
		if(lottoTrovato.getCodiceAsi()==null && lottoGIS.getCodiceAsi()!=null){
			messaggioErrori+="Codice Asi diverso GITLAND["+lottoTrovato.getCodiceAsi()+"]-["+lottoGIS.getCodiceAsi()+"]GIS\n";
			esito= false;
		}
		
		//lo stesso nome comune
		if(lottoTrovato.getIstat()!=null && !lottoTrovato.getIstat().equals(lottoGIS.getIstat())){
			messaggioErrori+="Codice Istat diverso GITLAND["+lottoTrovato.getIstat()+"]-["+lottoGIS.getIstat()+"]GIS\n";
			esito= false;
		}
		if(lottoTrovato.getIstat()==null && lottoGIS.getIstat()!=null){
			messaggioErrori+="Codice Istat diverso GITLAND["+lottoTrovato.getIstat()+"]-["+lottoGIS.getIstat()+"]GIS\n";
			esito= false;
		}

		//lo stesso perimetro
		if(lottoTrovato.getPerimetro()!=null && lottoGIS.getPerimetro()!=null){
			double p1,p2;
			p1=round(lottoTrovato.getPerimetro().doubleValue(), 2);
			p2=round(lottoGIS.getPerimetro().doubleValue(), 2);
			if(p1!=p2){
				messaggioErrori+="Perimetro diverso GITLAND["+p1+"]-["+p2+"]GIS\n";
				esito= false;
			}
		}else{
			if(lottoTrovato.getPerimetro()!=null || lottoGIS.getPerimetro()!=null){
				messaggioErrori+="Perimetro diverso GITLAND["+lottoTrovato.getPerimetro()+"]-["+lottoGIS.getPerimetro()+"]GIS\n";
				esito= false;
			}
		}
		//la stessa area
		if(lottoTrovato.getArea()!=null && lottoGIS.getArea()!=null){
			double a1,a2;
			a1=round(lottoTrovato.getArea().doubleValue(), 2);
			a2=round(lottoGIS.getArea().doubleValue(), 2);
			if(a1!=a2){
				messaggioErrori+="Area diversa GITLAND["+a1+"]-["+a2+"]GIS\n";
				esito= false;
			}
		}else{
			if(lottoTrovato.getArea()!=null || lottoGIS.getArea()!=null){
				messaggioErrori+="Area diversa GITLAND["+lottoTrovato.getArea()+"]-["+lottoGIS.getArea()+"]GIS\n";
				esito= false;
			}
		}
		// le stesse note
		if(lottoTrovato.getNote()!=null && !lottoTrovato.getNote().equals(lottoGIS.getNote())){
			messaggioErrori+="Note diverse GITLAND["+lottoTrovato.getNote()+"]-["+lottoGIS.getNote()+"]GIS\n";
			esito= false;
		}
		if(lottoTrovato.getNote()==null && lottoGIS.getNote()!=null){
			messaggioErrori+="Note diverse GITLAND["+lottoTrovato.getNote()+"]-["+lottoGIS.getNote()+"]GIS\n";
			esito= false;
		}


		if(lottoGIS.getCoordinate().size()!= lottoTrovato.getCoordinate().size()){
			messaggioErrori+="Numero di Coordinate diverso GITLAND["+lottoTrovato.getCoordinate().size()+"]-["+lottoGIS.getCoordinate().size()+"]GIS\n";
			esito= false;
		}
		int totaleTrovate=0;
		for (LottoCoordinate lottoCoordinata : lottoGIS.getCoordinate()) {
			boolean trovata=false;
			for (LottoCoordinate c : lottoTrovato.getCoordinate()) {
				if(c.getFoglio()!=null && c.getFoglio().equals(lottoCoordinata.getFoglio()) 
						&& c.getParticella()!=null && c.getParticella().equals(lottoCoordinata.getParticella())){
					trovata=true;
					totaleTrovate++;
					break;
				}
			}
			//tutte le coordinate devono essere presenti
			if(!trovata){
				messaggioErrori+="Coordinate non trovata ["+lottoCoordinata.getFoglio()+"]-["+lottoCoordinata.getParticella()+"]\n";
				esito= false;
			}
		}
		//devo avere trovato tutte le coordinate rispettivamente
		if(lottoGIS.getCoordinate().size()!=totaleTrovate){
			messaggioErrori+="Coordinate diverse GITLAND["+lottoTrovato.getCoordinate().size()+"]-["+lottoGIS.getCoordinate().size()+"]GIS\n";
			esito= false;
		}
		if(!esito){
			String msgShort=messaggioErrori;
			if(msgShort.length()>50)msgShort=msgShort.substring(0, 48)+"...";
			String[] log= new String[]{""+lottoGIS.getCodiceAsi(),msgShort,messaggioErrori};
			listaEsiti.add(log);
		}
		return esito;
	}

	public String avviaSincronizzazione(){
		String esito = "sincronizzaLottiBean.avviaSincronizzazione";
		logger.info(SincronizzaLottiBean.class + ".avviaSincronizzazione");
		listaEsiti.clear();
		int inseriti=0;
		int aggiornati=0;
		int eliminati=0;
		int errore=0;
		for (Object[] lotto : lottiDaInse) {
			List<LottoCoordinate> coordinate= coordinateDaInse.get(lotto[1].toString());
			if(!inserisciLotto(lotto, coordinate)){
				String[] log= new String[]{""+lotto[1],"Inserimento Lotto Fallito!","Inserimento Lotto Fallito!"};
				listaEsiti.add(log);
				errore++;
			}else{
				String[] log= new String[]{""+lotto[1],"Inserito!","Inserito!"};
				listaEsiti.add(log);
				inseriti++;
			}
		}
		for (ElementoLotto ele : lottiDaModi) {
			if(!aggiornaLotto(ele.getLottoGitLand(), ele.lottoGis)){
				String[] log= new String[]{""+ele.getLottoGitLand().getCodiceAsi(),"Aggiornamento Lotto Fallito!","Aggiornamento Lotto Fallito!"};
				listaEsiti.add(log);
				errore++;
			}else{
				String[] log= new String[]{""+ele.getLottoGitLand().getCodiceAsi(),"Aggiornato!","Aggiornato!"};
				listaEsiti.add(log);
				aggiornati++;
			}
		}
		for (Lotto ele : lottiDaElim) {
			if(!eliminaLotto(ele)){
				String[] log= new String[]{""+ele.getCodiceAsi(),"Eliminazione Lotto Fallita!","Eliminazione Lotto Fallita!"};
				listaEsiti.add(log);
				errore++;
			}else{
				String[] log= new String[]{""+ele.getCodiceAsi(),"Eliminato!","Eliminato!"};
				listaEsiti.add(log);
				eliminati++;
			}
		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String dataSincro=sdf.format(new Date());
		setParametro(Parametro.DATA_ULTIMA_SINCRONIZZAZIONE,dataSincro);
		lottiAggiornati=""+aggiornati;
		lottiInErrore=""+errore;
		lottiInseriti=""+inseriti;
		lottiEliminati=""+eliminati;
		return esito;
	}//-------------------------------------------------------------------------

	

	private boolean inserisciLotto(Object[] lotto, List<LottoCoordinate> coordinate) {
		// Creo il lotto
		try {
			Lotto lottoGitLand= valorizzaLotto(lotto, coordinate);
			Boolean hasInsediamenti=checkInsediamenti(lottoGitLand.getCodiceAsi());
			lottoGitLand.setStatusUltimo(hasInsediamenti);
			lottoGitLand.setCessato(false);
			getGitLandService().addItem(lottoGitLand);
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
		return true;
	}

	private Boolean checkInsediamenti(Long codiceAsi) {
		String sql="select count(*) from aziende_lotti a  " +
				"where A.BELFIORE='"+getEnte()+"' and A.CODICE_ASI_LOTTO="+codiceAsi+" " +
				"and ((A.DATA_FINE_STATUS = '31/12/9999' and status=1)or(A.DATA_FINE_STATUS is null and status=1)) "; 
		List<Object[]> lst = gitLandService.getList(sql, false);
		if (lst != null && lst.size()>0){
			Object objs = (Object)lst.get(0);

			if("0".equals(objs.toString())) {
				return false;
			}else{
				return true;
			}
		}
		return true;
	}

	private boolean aggiornaLotto(Lotto lottoGitLand, Lotto lottoGis) {
		// Aggiorno il lotto
		try {
			lottoGitLand.setArea(lottoGis.getArea());
			lottoGitLand.setPerimetro(lottoGis.getPerimetro());
			lottoGitLand.setIstat(lottoGis.getIstat());
			lottoGitLand.setNote(lottoGis.getNote());
			lottoGitLand.setNumAddetti(lottoGis.getNumAddetti());
			for (LottoCoordinate coo : lottoGis.getCoordinate()) {
				//cerco la coordinata
				LottoCoordinate cooCercata=null;
				for (LottoCoordinate c : lottoGitLand.getCoordinate()) {
					if(coo.getFoglio().equals(c.getFoglio()) && coo.getParticella().equals(c.getParticella())){
						cooCercata=c;
						break;
					}
				}
				if(cooCercata==null){
					LottoCoordinate aggiunta= new LottoCoordinate();
					aggiunta.setLotto(lottoGitLand);
					aggiunta.setFoglio(coo.getFoglio());
					aggiunta.setParticella(coo.getParticella());
					lottoGitLand.getCoordinate().add(aggiunta);
				}
			}
			getGitLandService().updItem(lottoGitLand);
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
		return true;
	}

	private boolean eliminaLotto(Lotto lotto) {
		//eliminazione logica lotto
		try {
			lotto.setCessato(true);
			lotto.setDataCessazione(new Date());
			getGitLandService().updItem(lotto);
		} catch (Exception e) {
			logger.error(e);
			return true;
		}
		return true;
	}


	public String situazioneAttuale(){
		logger.info(SincronizzaLottiBean.class + ".situazioneAttuale");
		String esito = "sincronizzaLottiBean.situazioneAttuale";
		
		init();
		
		
		return esito;
	}//-------------------------------------------------------------------------

	public String getLottiDaAggiornare() {
		return lottiDaAggiornare;
	}

	public void setLottiDaAggiornare(String lottiDaAggiornare) {
		this.lottiDaAggiornare = lottiDaAggiornare;
	}

	public String getLottiDaInserire() {
		return lottiDaInserire;
	}

	public void setLottiDaInserire(String lottiDaInserire) {
		this.lottiDaInserire = lottiDaInserire;
	}

	public String getLottiDaEliminare() {
		return lottiDaEliminare;
	}

	public void setLottiDaEliminare(String lottiDaEliminare) {
		this.lottiDaEliminare = lottiDaEliminare;
	}

	public String getLottiAggiornati() {
		return lottiAggiornati;
	}

	public void setLottiAggiornati(String lottiAggiornati) {
		this.lottiAggiornati = lottiAggiornati;
	}

	public String getLottiInseriti() {
		return lottiInseriti;
	}

	public void setLottiInseriti(String lottiInseriti) {
		this.lottiInseriti = lottiInseriti;
	}

	public String getLottiEliminati() {
		return lottiEliminati;
	}

	public void setLottiEliminati(String lottiEliminati) {
		this.lottiEliminati = lottiEliminati;
	}

	public String getLottiInErrore() {
		return lottiInErrore;
	}

	public void setLottiInErrore(String lottiInErrore) {
		this.lottiInErrore = lottiInErrore;
	}

	public List<String[]> getListaEsiti() {
		return listaEsiti;
	}

	public void setListaEsiti(List<String[]> listaEsiti) {
		this.listaEsiti = listaEsiti;
	}

	public Boolean getSincronizzazioneAbilitata() {
		return sincronizzazioneAbilitata;
	}
	
}
