package it.webred.gitland.web.bean.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import it.webred.gitland.data.model.AziendaLotto;
import it.webred.gitland.data.model.GlAsi;
import it.webred.gitland.data.model.Lotto;
import it.webred.gitland.ejb.dto.FiltroDTO;
import it.webred.gitland.web.bean.GitLandBaseBean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class DatiBean extends GitLandBaseBean {
	
	private List<GlAsi> lstLottiAsi = null;
		
	private final String CARATTERI = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private final String NUMERI = "1234567890";
	private final String BELFIORE = "F704";

	public DatiBean() {
		init();
	}//-------------------------------------------------------------------------
	
	public void init(){
		lstLottiAsi = new ArrayList<GlAsi>();
	}//-------------------------------------------------------------------------
	
	public String lottiImport(){
		String esito = "datiBean.lottiImport";
		System.out.println(DatiBean.class + ".lottiImport");

		Hashtable htQry = new Hashtable();
		ArrayList<FiltroDTO> alFiltri = new ArrayList<FiltroDTO>();
		
//		FiltroDTO f0 = new FiltroDTO();
//		f0.setAttributo("codiceAsi");
//		f0.setParametro("codiceAsi");
//		f0.setOperatore("=");
//		f0.setValore( lottoNew.getCodiceAsi() );
//		alFiltri.add(f0);
		htQry.put("orderBy", "idLotto");

		lstLottiAsi = gitLandService.getList(htQry, GlAsi.class);
		Hashtable<String, Object> htCoord = new Hashtable<String, Object>();
		if (lstLottiAsi != null && lstLottiAsi.size()>0){
			for(int i=0; i<lstLottiAsi.size(); i++){
				GlAsi glasi = lstLottiAsi.get(i);
				if (glasi != null){
					boolean coordinateAggiunte = false;
					System.out.println("COORDINATE: " + glasi.getParticelle());
					String seqLotto = glasi.getIdLotto().toString();
					String lottoCoord = glasi.getParticelle();
					String[] aryCoords = lottoCoord.split("]");
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
										htCoord.put(seqLotto +"§" + foglio + "§" + particella,  glasi);
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
								htCoord.put(seqLotto +"§" + foglio + "§" + particella,  glasi);
								particella = "";
							}
						}
					}
					System.out.println("#PARTICELLE AGGIUNTE: " + htCoord.size());
				}
			}
		}
		/*
		 * Faccio insert dei lotti estrapolati senza ripetizioni di foglio e particella 
		 */
	 if (htCoord != null && htCoord.size()>0){
		 Enumeration<String> enGlasi = htCoord.keys();
		 while(enGlasi.hasMoreElements()){
			 String key = enGlasi.nextElement();
			 String[] coord = key.split("§");
			 String seqLotto = coord[0];
			 String fog = coord[1];
			 String par = coord[2];
			 Lotto lotto = new Lotto();
			 GlAsi glasi = (GlAsi)htCoord.get(key);
			 lotto.setArea(glasi.getShapeArea().doubleValue());
			 lotto.setCodiceAsi(glasi.getIdAsi());
			 lotto.setIndirizzo(glasi.getIndirizzo());
			 lotto.setIstat(glasi.getComune());
			 lotto.setNote(glasi.getNote());
			 lotto.setPerimetro(glasi.getShapeLeng().doubleValue());
			 //questo lo aggiorniamo con il metodo lottiStatusUpdate in un momento successivo
			 //lotto.setStatusUltimo(statusUltimo);
			 
			 lotto.setNumAddetti(glasi.getnAddetti()!=null?glasi.getnAddetti():0l);
			 
			 //info che non esistevano in mdb
			 /*
			 lotto.setCessato(cessato);
			 lotto.setDataCessazione(dataCessazione);
			 lotto.setLottiAvi(lottiAvi);
			 lotto.setLottiEredi(lottiEredi);
			 lotto.setMotivo(motivo);
			  */

			 lotto.setBelfiore(BELFIORE);

			 gitLandService.addItem(lotto);
			 
		 }
	 }
		
	 /*
	  * IMPORT DELLE AZIENDE

INSERT INTO AZIENDE (
    PK_AZIENDA, CODICE_ASI, RAGIONE_SOCIALE, RAPPR_LEGALE, COD_ATTIVITA, ATTIVITA, VIA_CIVICO,
    ISTATC, ISTATP, NUM_ADDETTI, P_IVA, COD_FISCALE, TELEFONO, FAX, EMAIL, WEB, 
    DATA_ULTIMA_MODIFICA, BELFIORE, AREA_ASI )
SELECT
     SEQ_GITLAND.NEXTVAL, ID_ASI, RAGIONE_SOCIALE, RAPPR_LEGALE, '', ATTIVITA, VIA_CIVICO,
     substr(COMUNE, 4, 3) ISTATC, substr(COMUNE, 0, 3) ISTATP, NUM_ADDETTI, P_IVA, COD_FISCALE, TELEFONO, FAX, EMAIL, WEB,
     SYSDATE, 'C361', AREA_ASI
     FROM GL_AZIENDE
            
   COMMIT;
			 
	  */
		
	 
	 
	 /*
	  * IMPORT RELAZIONI AZIENDE_LOTTI
	  */
	 /*

	INSERT INTO AZIENDE_LOTTI (
         PK_AZIENDA_LOTTO, FK_LOTTO, FK_AZIENDA, DATA_FINE_STATUS, CODICE_ASI_AZIENDA, CODICE_ASI_LOTTO, STATUS, BELFIORE 
        )
        SELECT
        ID_RELAZ, ID_LOTTO, ID_AZIENDA, '', 0, 0, DISPONIBIL, '&BELFIORE'
        FROM GL_AZIENDE_LOTTO
               
        COMMIT;
		
		// VALORIZZIAMO I CODICI ASI IN AZIENDE_LOTTI 

		 UPDATE AZIENDE_LOTTI AL SET AL.CODICE_ASI_AZIENDA = 
		(
		select AZ.CODICE_ASI from AZIENDE AZ where AL.FK_AZIENDA = AZ.PK_AZIENDA AND AZ.BELFIORE = 'C361' 
		);
		
		COMMIT;

		UPDATE AZIENDE_LOTTI AL SET AL.CODICE_ASI_LOTTO = 
		(
		select DISTINCT L.CODICE_ASI from LOTTI L where AL.FK_LOTTO = L.ID_LOTTO AND L.belfiore = 'C361'
		);
		
		COMMIT;
		
	  */
	 
	 /*
	  * IMPORT PRATICHE
	  */
	 
	 /*
	  * 
	  * ACCEPT BELFIOREASI;

		INSERT INTO PRATICHE (
		 CONTATORE, E_GEO_PRAT, COD_PRATIC, E_GEO_AZIE, E_GEO_LOTT, FALDONE, COD_TIPO, TIPOLOGIA, NOASI_NUM, NOASI_DATA,
		 INIZIO_LAV, FINE_LAV, LAV_FINITO, CHIUDIP, CONCEDIL_N, CONCEDIL_C, PROTOCOLLO, PROTOCOLL1, CONVENZION, CONVENZION_D, 
		 VINCOLO, DELIBERA_N, DELIBERA_D, MODACQAREA, NOTE, PRCT, DISPONIBIL, RELAZIONE, SCADENZA, DATA_RI_PROG, INIZIO_LAVCOM,
		 FINE_LAVCOM, INIZIOATT, CONCEDIL_R, CODICE_ASI_AZIENDA, CODICE_ASI_LOTTO, BELFIORE
		)
		SELECT 
		GP.CONTATORE, GP.E_GEO_PRAT, GP.COD_PRATIC, GP.E_GEO_AZIE, GP.E_GEO_LOTT, GP.FALDONE, GP.COD_TIPO, GP.TIPOLOGIA, GP.NOASI_NUM, GP.NOASI_DATA,
		GP.INIZIO_LAV, GP.FINE_LAV, GP.LAV_FINITO, GP.CHIUDIP, GP.CONCEDIL_N, GP.CONCEDIL_D, GP.PROTOCOLLO, GP.PROTOCOLL1, GP.CONVENZION, GP.CONVENZION_D, 
		GP.VINCOLO, GP.DELIBERA_N, GP.DELIBERA_D, GP.MODACQAREA, GP.NOTE, GP.PRCT, GP.DISPONIBIL, GP.RELAZIONE, GP.SCADENZA, GP.DATA_RI_PROG, GP.INIZIO_LAVCOM,
		GP.FINE_LAVCOM, GP.INIZIOATT, GP.CONCEDIL_R,  
		(
		SELECT DISTINCT GA.ID_ASI 
		FROM GL_AZIENDE GA
		WHERE GP.E_GEO_AZIE = GA.E_GEO_AZIENDA
		), 
		(
		SELECT DISTINCT GASI.ID_ASI 
		FROM GL_ASI GASI
		WHERE GP.E_GEO_LOTT = GASI.ID_LOTTO
		), 
		'&BELFIOREASI'   
		FROM GL_PRATICHE GP;
		
		 
		COMMIT;
	  * 
	  */
	 
		return esito;
	}//-------------------------------------------------------------------------
	
	
	/*
	 * select codice_asi_lotto, codice_asi_azienda, data_fine_status, status, aziende.ragione_sociale, pezza.area, pezza.perimetro, pezza.indirizzo, pezza.istatc
from aziende_lotti al
left join aziende on al.codice_asi_azienda = aziende.codice_asi
left join (select distinct codice_asi, area, perimetro, indirizzo, istatc from lotti) pezza on al.codice_asi_lotto = pezza.codice_asi

	 */
	
	public String lottiStatusUpdate(){
		String esito = "datiBean.lottiStatusUpdate";
		System.out.println(DatiBean.class + ".lottiStatusUpdate");

		Hashtable htQry = new Hashtable();
		ArrayList<FiltroDTO> alFiltri = new ArrayList<FiltroDTO>();
		FiltroDTO f = new FiltroDTO();
		f.setAttributo("belfiore");
		f.setParametro("belfiore");
		f.setOperatore("=");
		f.setValore( BELFIORE );
		alFiltri.add(f);
		htQry.put("where", alFiltri);
		htQry.put("orderBy", "codiceAsi");

		List<Lotto> lstLotti = gitLandService.getList(htQry, Lotto.class);
		if (lstLotti != null && lstLotti.size()>0){
		
			for (int i=0; i<lstLotti.size(); i++){
				Lotto l = lstLotti.get(i);
				Long casi = l.getCodiceAsi();
				
				htQry = new Hashtable();
				alFiltri = new ArrayList<FiltroDTO>();
				
				FiltroDTO f0 = new FiltroDTO();
				f0.setAttributo("codiceAsiLotto");
				f0.setParametro("codiceAsiLotto");
				f0.setOperatore("=");
				f0.setValore( casi );
				alFiltri.add(f0);
				
				FiltroDTO f1 = new FiltroDTO();
				f1.setAttributo("belfiore");
				f1.setParametro("belfiore");
				f1.setOperatore("=");
				f1.setValore( BELFIORE );
				alFiltri.add(f1);
				
				htQry.put("where", alFiltri);
				htQry.put("ordeBy", "TO_DATE(dataFineStatus) DESC");
				htQry.put("limit", 1);
				
				List<AziendaLotto> lstAziendeLotti = gitLandService.getList(htQry, AziendaLotto.class);

				if (lstAziendeLotti != null && lstAziendeLotti.size()==1){
					AziendaLotto al = (AziendaLotto)lstAziendeLotti.get(0);
					l.setStatusUltimo(al.getStatus());
					
					gitLandService.updItem(l);
				}
				
			}
			
		}
		
		return esito;
	}//-------------------------------------------------------------------------
	

}
