/*
 * Created on 14-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.common;

import org.apache.log4j.Logger;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Tema {
	
	protected static final Logger log = Logger.getLogger("diogene.log");
	
	public static String getNomeFinder(int uc) throws Exception{
		return "FINDER" + new Integer(uc).toString();
	}
	public static String getOggettoDettaglio(int uc) throws Exception{
		switch(uc){
			case 1:		
				return it.escsolution.escwebgis.catasto.logic.CatastoImmobiliLogic.UNIMM;
			case 2:
				return "TERRENO";
			case 3:
				return "INTESTATARIOF";
			case 4:
				return "INTESTATARIOG";
			case 5:		
				return "CONTRIBUENTE";
			case 6:
				return "ICI";			
			case 7:
				return "TARSU";
			case 8:
				return "ANAGRAFE";
			case 9:		
				return "FAMIGLIA";
			case 10:
				return it.escsolution.escwebgis.toponomastica.logic.ToponomasticaStradeLogic.STRADA;
			case 11:
				return it.escsolution.escwebgis.toponomastica.logic.ToponomasticaCiviciLogic.CIVICO;
			case 12:
				return "GAUSS";
			case 13:
				return "ISTAT";
			case 14:
				return "SOGGETTO";
			case 15:
				return "DEFUNTO";	
			case 16:
				return it.escsolution.escwebgis.aziende.logic.AziendeAziendeLogic.DETTAGLIO_AZIENDA;
			case 17:
				return "CONDOMINIO";				
			case 18:
				return "EREDE";	
			case 19:
				return "OGGETTO";	
			case 20:
				return it.escsolution.escwebgis.acquedotto.logic.AcquedottoAcquedottoLogic.DETTAGLIO_ACQUEDOTTO;
			case 21:
				return "CONTRIBUENTE";				
			case 22:
				return "CONTRIBUENTE";
			case 23:
				return "FORNITURE";					
			case 24:
				return "NOTE";		
			case 25:
				return "SOGGETTOFASCICOLO";	
			case 26:
				return "VERSAMENTI";
			case 27:
				return "IMPIANTITERMICI";
			case 28:
				return "SOGGETTIANOMALIA";
			case 29:
				return "LICENZA";
			case 30:
				return it.escsolution.escwebgis.locazioni.logic.LocazioniLogic.LOCAZIONI;
			case 31:
				return it.escsolution.escwebgis.concessioni.logic.ConcessioniLogic.CONCESSIONI;
			case 32:
				return it.escsolution.escwebgis.enel.logic.EnelLogic.ENEL;
			case 33:
				return it.escsolution.escwebgis.modellof24.logic.ModelloF24Logic.MODELLOF24;
			case 34:
				return "NOTETERR";
			case 35:
				return it.escsolution.escwebgis.anagrafe.logic.AnagrafeStoricoAnagrafeLogic.STORICO;
			case 36:
				return it.escsolution.escwebgis.anagrafe.logic.AnagrafeStorico2005Logic.STORICO;
			case 37:
				return it.escsolution.escwebgis.esatri.logic.EsatriRiversamentiLogic.ESATRIRIV;	
			case 38:
				return it.escsolution.escwebgis.esatri.logic.EsatriContribuentiLogic.ESATRICON;
			case 39:
				return it.escsolution.escwebgis.condono.logic.CondonoLogic.CONDONOKEY;
			case 40:
				return it.escsolution.escwebgis.concessioni.logic.DiaLogic.DIACONC;
			case 41:
				return it.escsolution.escwebgis.concessioni.logic.ConcessioneLogic.CONC;
			case 42:
				return it.escsolution.escwebgis.vus.logic.VusLogic.VUS;
			case 43:
				return it.escsolution.escwebgis.docfa.logic.DocfaLogic.DOCFA;		
			case 44:
				return it.escsolution.escwebgis.vus.logic.VusGasLogic.VUS_GAS_CATA;
			case 45:
				return it.escsolution.escwebgis.anagrafe.logic.AnagrafeDwhLogic.ANAGRAFEDWH;
			case 46:
				return it.escsolution.escwebgis.concessioni.logic.ConcessioniINFORMLogic.CONCESSIONI_INFORM;				
			case 47:
				return it.escsolution.escwebgis.concessioni.logic.ConcessioniVisureLogic.CONCESSIONE_VISURA;				
			case 48:
				return it.escsolution.escwebgis.redditi2004.logic.Redditi2004Logic.REDDITI;				
			case 49:
				return it.escsolution.escwebgis.cosap.logic.CosapLogic.DETT_COSAP;
			case 50:
				return it.escsolution.escwebgis.concessioni.logic.FornituraDiaLogic.FORNITURADIA;					
			case 51:
				return it.escsolution.escwebgis.cncici.logic.CncIciLogic.CNCICI;
			case 52:
				return it.escsolution.escwebgis.redditiAnnuali.logic.RedditiAnnualiLogic.REDDITI;
			case 53:
				return it.escsolution.escwebgis.concessioni.logic.StoricoConcessioniLogic.CONCESSIONI;
			case 54:
				return it.escsolution.escwebgis.gas.logic.FornitureGasLogic.DATI_GAS;
			case 55:
				return it.escsolution.escwebgis.licenzeCommercioNew.logic.LicenzeCommercioNewLogic.DATI_LICENZE_COMMERCIO_NEW;
			case 101:
				return it.escsolution.escwebgis.diagnostiche.logic.DiagnosticheLogic.DIAGNOSTICHE;
			case 102:
				return it.escsolution.escwebgis.diagnostiche.logic.DiagnosticheDocfaLogic.DIAGNOSTICHE;
			case 103:
				return it.escsolution.escwebgis.diagnostiche.logic.DiagnosticheDocfaNoResLogic.DIAGNOSTICHE;
			case 104:
				return it.escsolution.escwebgis.diagnostiche.logic.DiagnosticheTarsuLogic.DIAGNOSTICHE;
			case 105:
				return it.escsolution.escwebgis.enel.logic.EnelDwhLogic.ENELDWH;
			case 106:
				return it.escsolution.escwebgis.pertinenzeAbit.logic.PertinenzeAbitLogic.PERTINENZE_ABIT;
			case 107:
				return it.escsolution.escwebgis.urbanistica.logic.UrbanisticaLogic.URBANISTICA;
			case 108:
				return it.escsolution.escwebgis.tributiNew.logic.TributiOggettiICINewLogic.ICI;
			case 109:
				return it.escsolution.escwebgis.tributiNew.logic.TributiOggettiTARSUNewLogic.TARSU;
			case 110:
				return it.escsolution.escwebgis.tributiNew.logic.TributiContribuentiNewLogic.CONTRIBUENTI;
			case 111:
				return "it.escsolution.escwebgis.indagineCivico.logic.IndagineCivicoLogic.INDIRIZZO";
			case 112:
				return it.escsolution.escwebgis.tributiNew.logic.VersamentiNewLogic.VERSAMENTI;
			case 113:
				return it.escsolution.escwebgis.cosapNew.logic.CosapNewLogic.COSAP;
			case 114:
				return it.escsolution.escwebgis.pregeo.logic.PregeoLogic.PREGEO_DETT;
			case 115:
				return it.escsolution.escwebgis.pubblicita.logic.PubblicitaLogic.PUBBLICITA;
			case 116:
				return it.escsolution.escwebgis.ecog.logic.EcograficoStradeLogic.STRADA;
			case 117:
				return it.escsolution.escwebgis.ecog.logic.EcograficoCiviciLogic.CIVICO;
			case 118:
				return it.escsolution.escwebgis.pratichePortale.logic.PraticheLogic.PRATICA;
			case 119:
				return it.escsolution.escwebgis.rette.logic.RetteLogic.RETTA;
			case 120:
				return it.escsolution.escwebgis.multe.logic.MulteLogic.MULTE;
			case 121:
				return it.escsolution.escwebgis.acqua.logic.AcquaLogic.ACQUA;
			case 122:
				return it.escsolution.escwebgis.f24.logic.F24VersamentiLogic.VERSAMENTO;
			case 123:
				return it.escsolution.escwebgis.f24.logic.F24AnnullamentoLogic.ANNULLAMENTO;
			case 124:
				return it.escsolution.escwebgis.imu.logic.ConsulenzaImuLogic.IMU_CONS;
			case 132:
				return it.escsolution.escwebgis.demanio.logic.DemanioLogic.BENE;
			case 133:
				return it.escsolution.escwebgis.redditiFamiliari.logic.RedditiFamiliariLogic.REDDITI_FAMILIARI;
			case 134:
				return it.escsolution.escwebgis.contributi.logic.ContributiLogic.CONTRIBUTI;
			case 135:
				return it.escsolution.escwebgis.cened.logic.CertificazioniEnergeticheLogic.CENED;
			case 137:
				return it.escsolution.escwebgis.bolli.logic.BolliVeicoliLogic.BOLLI_VEICOLI;
			/*
			 case 136:
				throw new Exception("OGGETTO non definito"); //NON PREVISTO
			 */
				
			case 500:
				  return "Ricerca veloce soggetto";		
			case 501:
				  return "Ricerca veloce vie";		
			case 502:
				  return "Ricerca veloce civici";		
			case 503:
				  return "Ricerca veloce oggetti";	
			case 504:
				  return "Ricerca veloce fabbricati";	
			default:
				throw new Exception("OGGETTO non definito");
			}
		}
	public static String getServletName(int uc) throws Exception{
		switch(uc){
			case 1:		
				return "CatastoImmobili";
			case 2:
				return "CatastoTerreni";
			case 3:
				return "CatastoIntestatariF";
			case 4:
				return "CatastoIntestatariG";
			case 5:		
				return "TributiContribuenti";
			case 6:
				return "TributiOggettiICI";			
			case 7:
				return "TributiOggettiTARSU";
			case 8:
				return "AnagrafeAnagrafe";
			case 9:		
				return "AnagrafeFamiglia";
			case 10:
				return "ToponomasticaCivici";
			case 11:
				return "ToponomasticaStrade";
			case 12:
				return "CatastoGauss";
			case 13:
				return "IstatIstat";
			case 14:
				return "SoggettoSoggetto";
			case 15:
				return "SuccessioniDefunti";
			case 16:
				return "AziendeAziende";
			case 17:
				return "CondominiCondomini";						
			case 18:
				return "SuccessioniEredi";
			case 19:
				return "SuccessioniOggetti";				
			case 20:
				return "AcquedottoAcquedotto";
			case 21:
				return "AgenziaEntrateContribuenti";					
			case 22:
				return "AgenziaEntrateContribuentiGiu";	
			case 23:
				return "DupForniture";				
			case 24:
				return "DupNote";
			case 25:
				return "SoggettoFascicolo";
			case 26:
				return "Versamenti";
			case 27:
				return "ImpiantiTermici";					
			case 28:
				return "SoggettiAnomalie";
			case 29:
				return "LicenzeCommercio";
			case 30:
				return "Locazioni";
			case 31:
				return "Concessioni";
			case 32:
				return "Enel";
			case 33:
				return "ModelloF24";
			case 34:
				return "DupNoteTerr";	
			case 35:
				return "AnagrafeStorico";
			case 36:
				return "AnagrafeStorico2005";
			case 37:
				return "EsatriRiversamenti";
			case 38:
				return "EsatriContribuenti";
			case 39:
				return "Condono";
			case 40:
				return "Dia";
			case 41:
				return "Concessione";
			case 42:
				return "Vus";
			case 43:
				return "Docfa";	
			case 44:
				return "VusGas";
			case 45:
				return "AnagrafeDwh";
			case 46:
				return "ConcessioniINFORM";				
			case 47:
				return "ConcessioniVisure";				
			case 48:
				return "Redditi2004";				
			case 49:
				return "Cosap";
			case 50:
				return "CncIci";						
			case 51:
				return "FornituraDia";
			case 52:
				return "RedditiAnnuali";
			case 53:
				return "StoricoConcessioni";
			case 54:
				return "FornitureGas";
			case 55:
				return "LicenzeCommercioNew";
			case 101:
				return "Diagnostiche";	
			case 102:
				return "Diagnostiche Docfa";	
			case 103:
				return "Diagnostiche Docfa NoRes";	
			case 104:
				return "Diagnostiche Tarsu";
			case 105:
				return "EnelDwh";
			case 106:
				return "PertinenzeAbit";
			case 107:
				return "Urbanistica";
			case 108:
				return "TributiOggettiICINew";
			case 109:
				return "TributiOggettiTARSUNew";
			case 110:		
				return "TributiContribuentiNew";
			case 111:		
				return "IndagineCivico";
			case 112:		
				return "VersamentiNew";
			case 113:		
				return "CosapNew";
			case 114:
				return "Pregeo";
			case 115:
				return "Pubblicita";
			case 116:
				return "EcograficoStrade";
			case 117:
				return "EcograficoCivici";	
			case 118:
				return "PratichePortale";
			case 119:
				return "Rette";
			case 120:
				return "Multe";
			case 121:
				return "Acqua";
			case 122:
				return "F24Versamenti";
			case 123:
				return "F24Annullamento";
			case 124:
				return "ConsulenzaImu";
			case 125:
				return "ProspettoIncassi";
			case 126:
				return "VisuraNazionale";
			case 127:
				return "FascFabbApp";
			case 128:
				return "RuoloTarsu";
			case 129:
				return "RuoloTares";
			case 130:
				return "VersIciDm";
			case 131:
				return "VersamentiAll";
			case 132:
				return "Demanio";
			case 133:
				return "RedditiFamiliari";
			case 134:
				return "Contributi";
			case 135:
				return "CertificazioniEnergetiche";
			case 136:
				return "FascicoloDocumenti";
			case 137:
				return "BolliVeicoli";
			case 500:
				return "FastSearch";
			default:
				throw new Exception("SERVLET non definita");
			}
		
	}
	
	
	public static String getServletMapping(int uc) throws Exception{
		
		//log.debug("getServletMapping: UC=" + uc );
		
		switch(uc){
			case 1:		
				return EscServlet.URL_PATH + "/CatastoImmobili";
			case 2:
				return EscServlet.URL_PATH + "/CatastoTerreni";
			case 3:
				return EscServlet.URL_PATH + "/CatastoIntestatariF";
			case 4:
				return EscServlet.URL_PATH + "/CatastoIntestatariG";
			case 3344:
				return EscServlet.URL_PATH + "/CatastoIntestatari";
			case 5:		
				return EscServlet.URL_PATH + "/TributiContribuenti";
			case 6:
				return EscServlet.URL_PATH + "/TributiOggettiICI";			
			case 7:
				return EscServlet.URL_PATH + "/TributiOggettiTARSU";
			case 8:
				return EscServlet.URL_PATH + "/AnagrafeAnagrafe";
			case 9:		
				return EscServlet.URL_PATH + "/AnagrafeFamiglia";
			case 10:
				return EscServlet.URL_PATH + "/ToponomasticaStrade";	
			case 11:
				return EscServlet.URL_PATH + "/ToponomasticaCivici";
			case 12:
				return EscServlet.URL_PATH + "/CatastoGauss";
			case 13:
				return EscServlet.URL_PATH + "/IstatIstat";
			case 14:
				return EscServlet.URL_PATH + "/SoggettoSoggetto";
			case 15:
				return EscServlet.URL_PATH + "/SuccessioniDefunti";
			case 16:
				return EscServlet.URL_PATH + "/AziendeAziende";
			case 17:
				return EscServlet.URL_PATH + "/CondominiCondomini";				
			case 18:
				return EscServlet.URL_PATH + "/SuccessioniEredi";	
			case 19:
				return EscServlet.URL_PATH + "/SuccessioniOggetti";
			case 20:
				return EscServlet.URL_PATH + "/AcquedottoAcquedotto";
			case 21:
				return EscServlet.URL_PATH + "/AgenziaEntrateContribuenti";				
			case 22:
				return EscServlet.URL_PATH + "/AgenziaEntrateContribuentiGiu";				
			case 23:
				return EscServlet.URL_PATH + "/DupForniture";				
			case 24:
				return EscServlet.URL_PATH + "/DupNote";
			case 25:
				return EscServlet.URL_PATH + "/SoggettoFascicolo";
			case 26:
				return EscServlet.URL_PATH + "/Versamenti";	
			case 27:
				return EscServlet.URL_PATH + "/ImpiantiTermici";					
			case 28:
				return EscServlet.URL_PATH + "/SoggettoAnomalie";				
			case 29:
				return EscServlet.URL_PATH + "/LicenzeCommercio";				
			case 30:
				return EscServlet.URL_PATH + "/Locazioni";				
			case 31:
				return EscServlet.URL_PATH + "/Concessioni";				
			case 32:
				return EscServlet.URL_PATH + "/Enel";				
			case 33:
				return EscServlet.URL_PATH + "/ModelloF24";
			case 34:
				return EscServlet.URL_PATH + "/DupNoteTerr";
			case 35:
				return EscServlet.URL_PATH + "/AnagrafeStorico";
			case 36:
				return EscServlet.URL_PATH + "/AnagrafeStorico2005";
			case 37:
				return EscServlet.URL_PATH + "/EsatriRiversamenti";
			case 38:
				return EscServlet.URL_PATH + "/EsatriContribuenti";
			case 39:
				return EscServlet.URL_PATH + "/Condono";
			case 40:
				return EscServlet.URL_PATH + "/Dia";
			case 41:
				return EscServlet.URL_PATH + "/Concessione";
			case 42:
				return EscServlet.URL_PATH + "/Vus";
			case 43:
				return EscServlet.URL_PATH + "/Docfa";
			case 44:
				return EscServlet.URL_PATH + "/VusGas";
			case 45:
				return EscServlet.URL_PATH + "/AnagrafeDwh";
			case 46:
				return EscServlet.URL_PATH + "/ConcessioniINFORM";				
			case 47:
				return EscServlet.URL_PATH + "/ConcessioniVisure";				
			case 48:
				return EscServlet.URL_PATH + "/Redditi2004";				
			case 49:
				return EscServlet.URL_PATH + "/Cosap";
			case 50:
				return EscServlet.URL_PATH + "/CncIci";						
			case 51:
				return EscServlet.URL_PATH + "/FornituraDia";						
			case 52:
				return EscServlet.URL_PATH + "/RedditiAnnuali";
			case 53:
				return EscServlet.URL_PATH + "/StoricoConcessioni";
			case 54:
				return EscServlet.URL_PATH + "/FornitureGas";
			case 55:
				return EscServlet.URL_PATH + "/LicenzeCommercioNew";
			case 101:
				return EscServlet.URL_PATH + "/Diagnostiche";
			case 102:
				return EscServlet.URL_PATH + "/DiagnosticheDocfa";
			case 103:
				return EscServlet.URL_PATH + "/DiagnosticheDocfaNoRes";	
			case 104:
				return EscServlet.URL_PATH + "/DiagnosticheTarsu";	
			case 105:
				return EscServlet.URL_PATH + "/EnelDwh";	
			case 106:
				return EscServlet.URL_PATH + "/PertinenzeAbit";
			case 107:
				return EscServlet.URL_PATH + "/Urbanistica";
			case 108:
				return EscServlet.URL_PATH + "/TributiOggettiICINew";
			case 109:
				return EscServlet.URL_PATH + "/TributiOggettiTARSUNew";
			case 110:
				return EscServlet.URL_PATH + "/TributiContribuentiNew";
			case 111:
				return EscServlet.URL_PATH + "/IndagineCivico";
			case 112:
				return EscServlet.URL_PATH + "/VersamentiNew";
			case 113:
				return EscServlet.URL_PATH + "/CosapNew";
			case 114:
				return EscServlet.URL_PATH + "/Pregeo";
			case 115:
				return EscServlet.URL_PATH + "/Pubblicita";
			case 116:
				return EscServlet.URL_PATH + "/EcograficoStrade";
			case 117:
				return EscServlet.URL_PATH + "/EcograficoCivici";
			case 118:
				return EscServlet.URL_PATH + "/PratichePortale";
			case 119:
				return EscServlet.URL_PATH + "/Rette";
			case 120:
				return EscServlet.URL_PATH + "/Multe";
			case 121:
				return EscServlet.URL_PATH + "/Acqua";
			case 122:
				return EscServlet.URL_PATH + "/F24Versamenti";
			case 123:
				return EscServlet.URL_PATH + "/F24Annullamento";
			case 124:
				return EscServlet.URL_PATH + "/ConsulenzaImu";
			case 125:
				return EscServlet.URL_PATH + "/ProspettoIncassi";
			case 126:
				return EscServlet.URL_PATH + "/VisuraNazionale";
			case 127:
				return EscServlet.URL_PATH + "/FascFabbApp";
			case 128:
				return EscServlet.URL_PATH + "/RuoloTarsu";
			case 129:
				return EscServlet.URL_PATH + "/RuoloTares";
			case 130:
				return EscServlet.URL_PATH + "/VersIciDm";
			case 131:
				return EscServlet.URL_PATH + "/VersamentiAll";
			case 132:
				return EscServlet.URL_PATH + "/Demanio";
			case 133:
				return EscServlet.URL_PATH + "/RedditiFamiliari";
			case 134:
				return EscServlet.URL_PATH + "/Contributi";
			case 135:
				return EscServlet.URL_PATH + "/CertificazioniEnergetiche";
			case 136:
				return EscServlet.URL_PATH + "/FascicoloDocumenti";
			case 137:
				return EscServlet.URL_PATH + "/BolliVeicoli";
			case 500:
				return EscServlet.URL_PATH + "/FastSearch";		
			case 501:
				return EscServlet.URL_PATH + "/FastSearchVie";		
			case 502:
				return EscServlet.URL_PATH + "/FastSearchCivici";		
			case 503:
				return EscServlet.URL_PATH + "/FastSearchOggetti";	
			case 504:
				return EscServlet.URL_PATH + "/FastSearchFabbricati";	
			default:
				throw new Exception("URL non definito");
				}
		
	}
	public static String getNomeFiltro(int uc) throws Exception{
		
		switch(uc){
			case 1:		
				return "Filtro Fabbricati";
								
			case 2:
				return "Filtro Terreni";	
				
			case 3:
				return "Filtro Intestatari Fisici";
				
			case 4:
				return "Filtro Intestatari Giuridici";
							
			case 5:		
				return "Filtro Contribuenti";
				
			case 6:
				return "Filtro Oggetti ICI";			
				
			case 7:
				return "Filtro Oggetti TARSU";
				
			case 8:
				return "Filtro Anagrafe";
				
			case 9:		
				return "Filtro Famiglia";
				
			case 10:
				return "Filtro Strade";
				
			case 11:
				return "Filtro Civici";
				
			case 12:
				return "Filtro Catasto Grafico";
				
			case 13:
				return "Filtro Istat";
				
			case 14:
				return "Filtro Soggetto";
			
			case 15:
				return "Filtro Defunti";		
			
			case 16:
				return "Filtro Azienda";
			
			case 17:
				return "Filtro Condomini";				

			case 18:
				return "Filtro Eredi";	

			case 19:
				return "Filtro Oggetti";
				
			case 20:
				return "Filtro Acquedotto";
			
			case 21:
				return "Filtro Agenzia Entrate Persone Fisiche";				
			
			case 22:
				return "Filtro Agenzia Entrate Persone Giuridiche";				
			
			case 23:
				return "Filtro DUP Forniture";	
				
			case 24:
				return "Filtro DUP Note Immobili";
				
			case 25:
				return "Filtro Soggetto Fascicolo";
			case 26:
				return "Filtro Versamenti";	
			case 27:
				return "Filtro Impianti Termici";
			case 28:
				return "Filtro Soggetti in Anomalia";					
			case 29:
				return "Filtro Licenze di Commercio";					
			case 30:
				return "Filtro Locazioni";					
			case 31:
				return "Filtro Concessioni";					
			case 32:
				return "Filtro Enel";					
			case 33:
				return "Filtro Modello F24";
			case 34:
				return "Filtro DUP Note Terreni";	
			case 35:
				return "Filtro Anagrafe Storico 2006";
			case 36:
				return "Filtro Anagrafe Storico 2005";
			case 37:
				return "Filtro Esatri Riversamenti";
			case 38:
				return "Filtro Esatri Contribuenti";
			case 39:
				return "Filtro Condono";
			case 40:
				return "Filtro Dia 1996-2006";
			case 41:
				return "Filtro Concessioni 1988-1996";
			case 42:
				return "Filtro Forniture Idriche";
			case 43:
				return "Filtro Docfa";	
			case 44:
				return "Filtro Forniture Gas";
			case 45:
				return "Filtro Anagrafe";
			case 46:
				return "Filtro Concessioni";				
			case 47:
				return "Filtro Concessioni Visure";				
			case 48:
				return "Filtro Redditi 2004";				
			case 49:
				return "Filtro Dati Cosap";	
			case 50:
				return "Filtro Cnc Ici";					
			case 51:
				return "Filtro Fornitura Dia";
			case 52:
				return "Filtro Redditi Annuali";
			case 53:
				return "Filtro Concessioni";
			case 54:
				return "Filtro Forniture Gas";
			case 55:
				return "Filtro Licenze di Commercio";
			case 101:
				return "Filtro Diagnostiche";	
			case 102:
				return "Filtro Diagnostiche Docfa";
			case 103:
				return "Filtro Diagnostiche Docfa No Res";
			case 104:
				return "Filtro Diagnostiche Tarsu";
			case 105:
				return "Filtro Forniture Elettriche";
			case 106:
				return "Filtro Pertinenze Abitazioni";
			case 107:
				return "Filtro Urbanistica";
			case 108:
				return "Filtro Oggetti ICI";
			case 109:
				return "Filtro Oggetti TARSU";
			case 110:		
				return "Filtro Contribuenti";
			case 111:		
				return "Filtro Indagine Civico";
			case 112:
				return "Filtro Versamenti";
			case 113:
				return "Filtro Cosap";
			case 114:
				return "Filtro Pregeo";
			case 115:
				return "Filtro Pubblicita";
			case 116:
				return "Filtro Ecografico Strade";
			case 117:
				return "Filtro Ecografico Civici";
			case 118:
				return "Filtro Pratiche Portale Servizi";
			case 119:
				return "Filtro Rette Scolastiche";
			case 120:
				return "Filtro Multe";
			case 121:
				return "Filtro Forniture Idriche";
			case 122:
				return "Filtro F24 Versamenti";
			case 123:
				return "Filtro F24 Annullamento";
			case 124:
				return "Filtro Consulenze IMU";
			case 125:
				return "Filtro Prospetto Incassi Per Codice Tributo";
			case 126:
				return "Filtro Visura Nazionale";
			case 127:
				return "Filtro Fascicolo Fabbricato (App.Esterna)";
			case 128:
				return "Filtro Ruolo Tarsu";
			case 129:
				return "Filtro Ruolo Tares";
			case 130:
				return "Filtro Versamenti ICI (d.m.)";
			case 131:
				return "Filtro Versamenti";
			case 132:
				return "Filtro Demanio Comunale";
			case 133:
				return "Filtro Redditi Familiari";
			case 134:
				return "Filtro Contributi";
			case 135:
				return "Filtro Certificazioni Energetiche";
			case 136:
				return "Filtro Fascicolo Planimetrie";
			case 137:
				return "Filtro Bolli Veicoli";
			case 500:
				return "Filtro ricerca veloce soggetti";	
			case 501:
				return "Filtro ricerca veloce vie";		
			case 502:
				return "Filtro ricerca veloce civici";		
			case 503:
				return "Filtro ricerca veloce oggetti";	
			case 504:
				return "Filtro ricerca veloce fabbricati";	
			default:
				throw new Exception("UC non definito:" + uc);
		}
	}
	
public static String getNomeFunzionalita(int uc) {
		
		switch(uc){
			case 1:		
				return "Catasto -> Immobili";				
			case 2:
				return "Catasto -> Terreni";	
			case 3:
				return "Catasto -> Persone Fisiche";
			case 4:
				return "Catasto -> Persone Giuridiche";			
			case 5:		
				return "Tributi -> Contribuenti";	
			case 6:
				return "Tributi -> Oggetti ICI";				
			case 7:
				return "Tributi -> Oggetti TARSU";
			case 8:
				return "Filtro Anagrafe";
			case 9:		
				return "Filtro Famiglia";
			case 10:
				return "Toponomastica -> Strade";
			case 11:
				return "Toponomastica -> Numeri Civici";
			case 12:
				return "Catasto -> Catasto Grafico";
			case 13:
				return "Filtro Istat";
			case 14:
				return "Filtro Soggetto";
			case 15:
				return "Successioni -> Defunti";		
			case 16:
				return "Camera di Commercio -> Aziende";
			case 17:
				return "Condomini";				
			case 18:
				return "Successioni -> Eredi";	
			case 19:
				return "Successioni -> Oggetti";
			case 20:
				return "Acquedotto e Imp. Termici -> Acquedotto";
			case 21:
				return "SIATEL -> Contribuenti - Fis.";				
			case 22:
				return "SIATEL -> Contribuenti - Giur.";				
			case 23:
				return "Compravendite -> Compravendite Forniture";	
			case 24:
				return "Compravendite -> Compravendite Note Immobili";
			case 25:
				return "Fascicoli -> Soggetto";
			case 26:
				return "Tributi -> Versamenti";	
			case 27:
				return "Acquedotto e Imp. Termici -> Impianti Termici";
			case 28:
				return "Soggetti Anomalie -> Soggetti Anomalie";					
			case 29:
				return "Licenze di Commercio";					
			case 30:
				return "Locazioni";					
			case 31:
				return "Concessioni Edilizie -> Concessioni da ONLYOne";					
			case 32:
				return "Filtro Enel";					
			case 33:
				return "Filtro Modello F24";
			case 34:
				return "Compravendite -> Compravendite Note Terreni";	
			case 35:
				return "Popolazione -> Storico Anagrafe 2005";
			case 36:
				return "Popolazione -> Storico Anagrafe 2005";
			case 37:
				return "Esatri -> Riversamenti";
			case 38:
				return "Esatri -> Contribuenti";
			case 39:
				return "Condono";
			case 40:
				return "Concessioni Edilizie -> Concessioni da Prot. Gen. 1996-2006";
			case 41:
				return "Concessioni Edilizie -> Concessioni da Prot. Gen. 1988-1996";
			case 42:
				return "Forniture Idriche";
			case 43:
				return "Docfa";	
			case 44:
				return "Forniture Gas";
			case 45:
				return "Popolazione -> Anagrafe Storica";
			case 46:
				return "Concessioni Edilizie -> Concessioni delle zone";				
			case 47:
				return "Concessioni Edilizie -> Archivio storico Visure";				
			case 48:
				return "Redditi -> Redditi 2004";				
			case 49:
				return "Cosap -> Autorizzazioni e Diffide";	
			case 50:
				return "Filtro Cnc Ici";					
			case 51:
				return "Filtro Fornitura Dia";
			case 52:
				return "Redditi -> Redditi Annuali";
			case 53:
				return "Concessioni Edilizie -> Storico Concessioni";
			case 54:
				return "Forniture Gas";
			case 55:
				return "Licenze di Commercio";
			case 101:
				return "Filtro Diagnostiche";	
			case 102:
				return "Filtro Diagnostiche Docfa";
			case 103:
				return "Filtro Diagnostiche Docfa No Res";
			case 104:
				return "Filtro Diagnostiche Tarsu";
			case 105:
				return "Forniture Elettriche";
			case 106:
				return "Filtro Pertinenze Abitazioni";
			case 107:
				return "Urbanistica";
			case 108:
				return "Tributi -> Oggetti ICI";
			case 109:
				return "Tributi -> Oggetti TARSU";
			case 110:		
				return "Tributi -> Contribuenti";
			case 111:		
				return "Fascicoli -> Civico";
			case 112:
				return "Tributi -> Versamenti";
			case 113:
				return "Cosap";
			case 114:
				return "Pregeo";
			case 115:
				return "Pubblicita";
			case 116:
				return "Ecografico Strade";
			case 117:
				return "Ecografico Civici";
			case 118:
				return "Pratiche Portale";
			case 119:
				return "Rette";
			case 120:
				return "Multe";
			case 121:
				return "Acqua";
			case 122:
				return "F24Versamenti";
			case 123:
				return "F24Annullamento";
			case 124:
				return "ConsulenzaImu";
			case 125:
				return "ProspettoIncassi";
			case 126:
				return "Visura Nazionale";
			case 127:
				return "Fascicolo Fabbricato (App.Esterna)";
			case 128:
				return "Ruolo Tarsu";
			case 129:
				return "Ruolo Tares";
			case 130:
				return "Versamenti ICI";
			case 131:
				return "Versamenti";
			case 132:
				return "Demanio";
			case 133:
				return "Redditi -> Redditi Familiari";
			case 134:
				return "Contributi";
			case 135:
				return "Certificazioni Energetiche";
			case 136:		
				return "Fascicoli -> Planimetrie";
			case 137:
				return "Bolli Veicoli";
			case 500:
				return "Ricerca veloce soggetti";		
			case 501:
				return "Ricerca veloce vie";		
			case 502:
				return "Ricerca veloce civici";		
			case 503:
				return "Ricerca veloce oggetti";		
			case 504:
				return "Ricerca veloce fabbricati";		
			default:
				return "-";
		}
	}
	
	/**
	 * Utilizzata durante la costruzione dei crosslink, se una servlet opera su una diversa connessione 
	 * allora metterla qui
	 * @param servletPath
	 * @return
	 */
	public static String getDataSource(String servletPath) {
		int uc;
		try {
			uc = getServletUC(servletPath);
		} catch (Exception e) {
			return EscServlet.defaultDataSource;
		}
		switch(uc){
		case 45:		
			return "jdbc/diogene";
		default:
			return null;
		}
	}
	
	public static int getServletUC(String servletName) throws Exception{
				if (servletName.equals(EscServlet.URL_PATH + "/CatastoImmobili"))
					return 1;
				else if (servletName.equals(EscServlet.URL_PATH + "/CatastoTerreni"))
					return 2;
				else if (servletName.equals(EscServlet.URL_PATH + "/CatastoIntestatariF"))
					return 3;
				else if (servletName.equals(EscServlet.URL_PATH + "/CatastoIntestatariG"))
					return 4;
				else if (servletName.equals(EscServlet.URL_PATH + "/CatastoIntestatari"))
					return 3344;
				else if (servletName.equals(EscServlet.URL_PATH + "/TributiContribuenti"))
					return 5;
				else if (servletName.equals(EscServlet.URL_PATH + "/TributiOggettiICI"))
					return 6;
				else if (servletName.equals(EscServlet.URL_PATH + "/TributiOggettiTARSU"))
					return 7;
				else if (servletName.equals(EscServlet.URL_PATH + "/AnagrafeAnagrafe"))
					return 8;
				else if (servletName.equals(EscServlet.URL_PATH + "/AnagrafeFamiglia"))
					return 9;
				else if (servletName.equals(EscServlet.URL_PATH + "/ToponomasticaStrade"))
					return 10;
				else if (servletName.equals(EscServlet.URL_PATH + "/ToponomasticaCivici"))
					return 11;
				else if (servletName.equals(EscServlet.URL_PATH + "/CatastoGauss"))
					return 12;
				else if (servletName.equals(EscServlet.URL_PATH + "/IstatIstat"))
					return 13;
				else if (servletName.equals(EscServlet.URL_PATH + "/SoggettoSoggetto"))
					return 14;
				else if (servletName.equals(EscServlet.URL_PATH + "/SuccessioniDefunti"))
					return 15;
				else if (servletName.equals(getServletMapping(16)))
					return 16;
				else if (servletName.equals(EscServlet.URL_PATH + "/CondominiCondomini"))
					return 17;
				else if (servletName.equals(EscServlet.URL_PATH + "/SuccessioniEredi"))
					return 18;
				else if (servletName.equals(EscServlet.URL_PATH + "/SuccessioniOggetti"))
					return 19;				
				else if (servletName.equals(getServletMapping(20)))
					return 20;	
				else if (servletName.equals(EscServlet.URL_PATH + "/AgenziaEntrateContribuenti"))
					return 21;
				else if (servletName.equals(EscServlet.URL_PATH + "/AgenziaEntrateContribuentiGiu"))
					return 22;
				else if (servletName.equals(EscServlet.URL_PATH + "/DupForniture"))
					return 23;				
				else if (servletName.equals(EscServlet.URL_PATH + "/DupNote"))
					return 24;	
				else if (servletName.equals(EscServlet.URL_PATH + "/SoggettoFascicolo"))
					return 25;
				else if (servletName.equals(EscServlet.URL_PATH + "/Versamenti"))
					return 26;	
				else if (servletName.equals(EscServlet.URL_PATH + "/ImpiantiTermici"))
					return 27;					
				else if (servletName.equals(EscServlet.URL_PATH + "/SoggettoAnomalie"))
					return 28;				
				else if (servletName.equals(EscServlet.URL_PATH + "/LicenzeCommercio"))
					return 29;				
				else if (servletName.equals(EscServlet.URL_PATH + "/Locazioni"))
					return 30;				
				else if (servletName.equals(EscServlet.URL_PATH + "/Concessioni"))
					return 31;				
				else if (servletName.equals(EscServlet.URL_PATH + "/Enel"))
					return 32;				
				else if (servletName.equals(EscServlet.URL_PATH + "/ModelloF24"))
					return 33;
				else if (servletName.equals(EscServlet.URL_PATH + "/DupNoteTerr"))
					return 34;	
				else if (servletName.equals(EscServlet.URL_PATH + "/AnagrafeStorico"))
					return 35;	
				else if (servletName.equals(EscServlet.URL_PATH + "/AnagrafeStorico2005"))
					return 36;
				else if (servletName.equals(EscServlet.URL_PATH + "/EsatriRiversamenti"))
					return 37;
				else if (servletName.equals(EscServlet.URL_PATH + "/EsatriContribuenti"))
					return 38;
				else if (servletName.equals(EscServlet.URL_PATH + "/Condono"))
					return 39;
				else if (servletName.equals(EscServlet.URL_PATH + "/Dia"))
					return 40;
				else if (servletName.equals(EscServlet.URL_PATH + "/Concessione"))
					return 41;
				else if (servletName.equals(EscServlet.URL_PATH + "/Vus"))
					return 42;
				else if (servletName.equals(EscServlet.URL_PATH + "/Docfa"))
					return 43;	
				else if (servletName.equals(EscServlet.URL_PATH + "/VusGas"))
					return 44;
				else if (servletName.equals(EscServlet.URL_PATH + "/AnagrafeDwh"))
					return 45;
				else if (servletName.equals(EscServlet.URL_PATH + "/ConcessioniINFORM"))
					return 46;				
				else if (servletName.equals(EscServlet.URL_PATH + "/ConcessioniVisure"))
					return 47;	
				else if (servletName.equals(EscServlet.URL_PATH + "/Redditi2004"))
					return 48;	
				else if (servletName.equals(EscServlet.URL_PATH + "/Cosap"))
					return 49;
				else if (servletName.equals(EscServlet.URL_PATH + "/CncIci"))
					return 50;					
				else if (servletName.equals(EscServlet.URL_PATH + "/FornituraDia"))
					return 51;
				else if (servletName.equals(EscServlet.URL_PATH + "/RedditiAnnuali"))
					return 52;
				else if (servletName.equals(EscServlet.URL_PATH + "/StoricoConcessioni"))
					return 53;	
				else if (servletName.equals(EscServlet.URL_PATH + "/FornitureGas"))
					return 54;	
				else if (servletName.equals(EscServlet.URL_PATH + "/LicenzeCommercioNew"))
					return 55;
				else if (servletName.equals(EscServlet.URL_PATH + "/Diagnostiche"))
					return 101;
				else if (servletName.equals(EscServlet.URL_PATH + "/DiagnosticheDocfa"))
					return 102;
				else if (servletName.equals(EscServlet.URL_PATH + "/DiagnosticheDocfaNoRes"))
					return 103;
				else if (servletName.equals(EscServlet.URL_PATH + "/DiagnosticheTarsu"))
					return 104;
				else if (servletName.equals(EscServlet.URL_PATH + "/EnelDwh"))
					return 105;
				else if (servletName.equals(EscServlet.URL_PATH + "/PertinenzeAbit"))
					return 106;
				else if (servletName.equals(EscServlet.URL_PATH + "/Urbanistica"))
					return 107;
				else if (servletName.equals(EscServlet.URL_PATH + "/TributiOggettiICINew"))
					return 108;
				else if (servletName.equals(EscServlet.URL_PATH + "/TributiOggettiTARSUNew"))
					return 109;
				else if (servletName.equals(EscServlet.URL_PATH + "/TributiContribuentiNew"))
					return 110;
				else if (servletName.equals(EscServlet.URL_PATH + "/IndagineCivico"))
					return 111;
				else if (servletName.equals(EscServlet.URL_PATH + "/VersamentiNew"))
					return 112;
				else if (servletName.equals(EscServlet.URL_PATH + "/CosapNew"))
					return 113;
				else if (servletName.equals(EscServlet.URL_PATH + "/Pregeo"))
					return 114;
				else if (servletName.equals(EscServlet.URL_PATH + "/Pubblicita"))
					return 115;
				else if (servletName.equals(EscServlet.URL_PATH + "/EcograficoStrade"))
					return 116;
				else if (servletName.equals(EscServlet.URL_PATH + "/EcograficoCivici"))
					return 117;
				else if (servletName.equals(EscServlet.URL_PATH + "/PratichePortale"))
					return 118;
				else if (servletName.equals(EscServlet.URL_PATH + "/Rette"))
					return 119;
				else if (servletName.equals(EscServlet.URL_PATH + "/Multe"))
					return 120;
				else if (servletName.equals(EscServlet.URL_PATH + "/Acqua"))
					return 121;
				else if (servletName.equals(EscServlet.URL_PATH + "/F24Versamenti"))
					return 122;
				else if (servletName.equals(EscServlet.URL_PATH + "/F24Annullamento"))
					return 123;
				else if (servletName.equals(EscServlet.URL_PATH + "/ConsulenzaImu"))
					return 124;
				else if (servletName.equals(EscServlet.URL_PATH + "/ProspettoIncassi"))
					return 125;
				else if (servletName.equals(EscServlet.URL_PATH + "/VisuraNazionale"))
					return 126;
				else if (servletName.equals(EscServlet.URL_PATH + "/FascFabbApp"))
					return 127;
				else if (servletName.equals(EscServlet.URL_PATH + "/RuoloTarsu"))
					return 128;
				else if (servletName.equals(EscServlet.URL_PATH + "/RuoloTares"))
					return 129;
				else if (servletName.equals(EscServlet.URL_PATH + "/VersIciDm"))
					return 129;
				else if (servletName.equals(EscServlet.URL_PATH + "/Demanio"))
					return 132;
				else if (servletName.equals(EscServlet.URL_PATH + "/RedditiFamiliari"))
					return 133;
				else if (servletName.equals(EscServlet.URL_PATH + "/Contributi"))
					return 134;
				else if (servletName.equals(EscServlet.URL_PATH + "/CertificazioniEnergetiche"))
					return 135;
				else if (servletName.equals(EscServlet.URL_PATH + "/FascicoloDocumenti"))
					return 136;
				else if (servletName.equals(EscServlet.URL_PATH + "/BolliVeicoli"))
					return 137;
				else if (servletName.equals(EscServlet.URL_PATH + "/FastSearch"))
					return 500;
				else if (servletName.equals(EscServlet.URL_PATH + "/FastSearchVie"))
					return 501;
				else if (servletName.equals(EscServlet.URL_PATH + "/FastSearchCivici"))
					return 502;
				else if (servletName.equals(EscServlet.URL_PATH + "/FastSearchOggetti"))
					return 503;
				else if (servletName.equals(EscServlet.URL_PATH + "/FastSearchFabbricati"))
					return 504;
				else
					throw new Exception("Servlet Non definita");
		}
	
	/**
	 * ritorna divisi da virgola LA CHIAVE, CF E PI DA USARE NEGLI SCARTI DEL SOGGETTO
	 * @param uc
	 * @return
	 */
	public static String getFKCFPIScarti(int uc)
	{
		switch (uc)
		{
		case 3:
			return "PK_CUAA,CODI_FISC,CODI_PIVA";
			
		case 4:
			return "PK_CUAA,CODI_FISC,CODI_PIVA";
			
		case 8:
			return "PK_CODI_ANAGRAFE,CODICE_FISCALE,null";
			
		case 5:
			return "CODICE_CONTRIBUENTE,CODICE_FISCALE,PARTITA_IVA";
			
		case 24:
			return "ID_SOGGETTO_CATASTALE,CODICE_FISCALE,null";
		case 34:
			return "ID_SOGGETTO_CATASTALE,CODICE_FISCALE,null";			
		case 45:
			return "ID,CODFISC,null";
			
		default:
			return null;
		}
	}
	
	public static String getPkFieldName(int uc)
	{
		switch (uc)
		{
		case 3:
			return "PK_CUAA";
			
		case 4:
			return "PK_CUAA";
			
		case 5:
			return "UK_CODI_CONTRIBUENTE";
			
		case 8:
			return "PK_CODI_ANAGRAFE";
			
		case 24:
			return "IID_FORNITURA,ID_NOTA";
		case 34:
			return "IID_FORNITURA,ID_NOTA";			
		case 45:
			return "ID";			
		case 54:
			return "ID";			
		case 105:
			return "ID";			
			
		default: 
			return null;
		}
	}
	
	
	
	/*PER LE PAGINE lista.jsp  : parametro da aggiungere al progressivo del record per garantire l'apertura di finestre popup da funzionalità diverse relative allo stesso progressivo record  {
		if (servletName.equals(EscServlet.URL_PATH + "/CatastoImmobili"))
			return 1000;
		else if (servletName.equals(EscServlet.URL_PATH + "/CatastoTerreni"))
			return 2000;
		else if (servletName.equals(EscServlet.URL_PATH + "/CatastoIntestatariF"))
			return 3000;
		else if (servletName.equals(EscServlet.URL_PATH + "/CatastoIntestatariG"))
			return 4000;
		else if (servletName.equals(EscServlet.URL_PATH + "/CatastoIntestatari"))
			return 4500;
		else if (servletName.equals(EscServlet.URL_PATH + "/TributiContribuenti"))
			return 5000;
		else if (servletName.equals(EscServlet.URL_PATH + "/TributiOggettiICI"))
			return 6000;
		else if (servletName.equals(EscServlet.URL_PATH + "/TributiOggettiTARSU"))
			return 7000;
		else if (servletName.equals(EscServlet.URL_PATH + "/AnagrafeAnagrafe"))
			return 8000;
		else if (servletName.equals(EscServlet.URL_PATH + "/AnagrafeFamiglia"))
			return 9000;
		else if (servletName.equals(EscServlet.URL_PATH + "/ToponomasticaStrade"))
			return 10000;
		else if (servletName.equals(EscServlet.URL_PATH + "/ToponomasticaCivici"))
			return 11000;
		else if (servletName.equals(EscServlet.URL_PATH + "/CatastoGauss"))
			return 12000;
		else if (servletName.equals(EscServlet.URL_PATH + "/IstatIstat"))
			return 13000;
		else if (servletName.equals(EscServlet.URL_PATH + "/SoggettoSoggetto"))
			return 14000;
		else if (servletName.equals(EscServlet.URL_PATH + "/SuccessioniDefunti"))
			return 15000;
		else if (servletName.equals(getServletMapping(16)))
			return 16000;
		else if (servletName.equals(EscServlet.URL_PATH + "/CondominiCondomini"))
			return 17000;
		else if (servletName.equals(EscServlet.URL_PATH + "/SuccessioniEredi"))
			return 18000;
		else if (servletName.equals(EscServlet.URL_PATH + "/SuccessioniOggetti"))
			return 19000;				
		else if (servletName.equals(getServletMapping(20)))
			return 20000;	
		else if (servletName.equals(EscServlet.URL_PATH + "/AgenziaEntrateContribuenti"))
			return 21000;
		else if (servletName.equals(EscServlet.URL_PATH + "/AgenziaEntrateContribuentiGiu"))
			return 22000;
		else if (servletName.equals(EscServlet.URL_PATH + "/DupForniture"))
			return 23000;				
		else if (servletName.equals(EscServlet.URL_PATH + "/DupNote"))
			return 24000;	
		else if (servletName.equals(EscServlet.URL_PATH + "/SoggettoFascicolo"))
			return 25000;
		else if (servletName.equals(EscServlet.URL_PATH + "/Versamenti"))
			return 26000;	
		else if (servletName.equals(EscServlet.URL_PATH + "/ImpiantiTermici"))
			return 27000;					
		else if (servletName.equals(EscServlet.URL_PATH + "/SoggettoAnomalie"))
			return 28000;				
		else if (servletName.equals(EscServlet.URL_PATH + "/LicenzeCommercio"))
			return 29000;				
		else if (servletName.equals(EscServlet.URL_PATH + "/Locazioni"))
			return 30000;				
		else if (servletName.equals(EscServlet.URL_PATH + "/Concessioni"))
			return 31000;				
		else if (servletName.equals(EscServlet.URL_PATH + "/Enel"))
			return 32000;				
		else if (servletName.equals(EscServlet.URL_PATH + "/ModelloF24"))
			return 33000;
		else if (servletName.equals(EscServlet.URL_PATH + "/DupNoteTerr"))
			return 34000;	
		else if (servletName.equals(EscServlet.URL_PATH + "/AnagrafeStorico"))
			return 35000;	
		else if (servletName.equals(EscServlet.URL_PATH + "/AnagrafeStorico2005"))
			return 36000;
		else if (servletName.equals(EscServlet.URL_PATH + "/EsatriRiversamenti"))
			return 37000;
		else if (servletName.equals(EscServlet.URL_PATH + "/EsatriContribuenti"))
			return 38000;
		else if (servletName.equals(EscServlet.URL_PATH + "/Condono"))
			return 39000;
		else if (servletName.equals(EscServlet.URL_PATH + "/Dia"))
			return 40000;
		else if (servletName.equals(EscServlet.URL_PATH + "/Concessione"))
			return 41000;
		else if (servletName.equals(EscServlet.URL_PATH + "/Vus"))
			return 42000;
		else if (servletName.equals(EscServlet.URL_PATH + "/Docfa"))
			return 43000;	
		else if (servletName.equals(EscServlet.URL_PATH + "/VusGas"))
			return 44000;
		else if (servletName.equals(EscServlet.URL_PATH + "/AnagrafeDwh"))
			return 45000;
		else if (servletName.equals(EscServlet.URL_PATH + "/ConcessioniINFORM"))
			return 46000;				
		else if (servletName.equals(EscServlet.URL_PATH + "/ConcessioniVisure"))
			return 47000;	
		else if (servletName.equals(EscServlet.URL_PATH + "/Redditi2004"))
			return 48000;	
		else if (servletName.equals(EscServlet.URL_PATH + "/Cosap"))
			return 49000;
		else if (servletName.equals(EscServlet.URL_PATH + "/CncIci"))
			return 50000;					
		else if (servletName.equals(EscServlet.URL_PATH + "/FornituraDia"))
			return 51000;
		else if (servletName.equals(EscServlet.URL_PATH + "/RedditiAnnuali"))
			return 52000;
		else if (servletName.equals(EscServlet.URL_PATH + "/StoricoConcessioni"))
			return 53000;	
		else if (servletName.equals(EscServlet.URL_PATH + "/FornitureGas"))
			return 54000;	
		else if (servletName.equals(EscServlet.URL_PATH + "/LicenzeCommercioNew"))
			return 55000;
		else if (servletName.equals(EscServlet.URL_PATH + "/Diagnostiche"))
			return 101000;
		else if (servletName.equals(EscServlet.URL_PATH + "/DiagnosticheDocfa"))
			return 102000;
		else if (servletName.equals(EscServlet.URL_PATH + "/DiagnosticheDocfaNoRes"))
			return 103000;
		else if (servletName.equals(EscServlet.URL_PATH + "/DiagnosticheTarsu"))
			return 104000;
		else if (servletName.equals(EscServlet.URL_PATH + "/EnelDwh"))
			return 105000;
		else if (servletName.equals(EscServlet.URL_PATH + "/PertinenzeAbit"))
			return 106000;
		else if (servletName.equals(EscServlet.URL_PATH + "/Urbanistica"))
			return 107000;
		else if (servletName.equals(EscServlet.URL_PATH + "/TributiOggettiICINew"))
			return 108000;
		else if (servletName.equals(EscServlet.URL_PATH + "/TributiOggettiTARSUNew"))
			return 109000;
		else if (servletName.equals(EscServlet.URL_PATH + "/TributiContribuentiNew"))
			return 110000;
		else if (servletName.equals(EscServlet.URL_PATH + "/IndagineCivico"))
			return 111000;
		else if (servletName.equals(EscServlet.URL_PATH + "/VersamentiNew"))
			return 112000;
		else if (servletName.equals(EscServlet.URL_PATH + "/CosapNew"))
			return 113000;
			
			*/
}
