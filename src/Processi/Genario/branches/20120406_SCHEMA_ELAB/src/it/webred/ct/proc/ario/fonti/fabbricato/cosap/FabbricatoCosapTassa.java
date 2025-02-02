package it.webred.ct.proc.ario.fonti.fabbricato.cosap;

import it.webred.ct.data.model.indice.IndicePK;
import it.webred.ct.data.model.indice.SitFabbricatoTotale;
import it.webred.ct.data.model.indice.SitOggettoTotale;
import it.webred.ct.proc.ario.bean.HashParametriConfBean;
import it.webred.ct.proc.ario.fonti.DatoDwh;
import it.webred.ct.proc.ario.fonti.fabbricato.Fabbricato;
import it.webred.ct.proc.ario.normalizzazione.NormalizzaTotali;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Properties;

import org.apache.log4j.Logger;

public class FabbricatoCosapTassa extends DatoDwh implements Fabbricato{

	private Properties props = null;
	protected static final Logger log = Logger.getLogger(FabbricatoCosapTassa.class.getName());
	
	
	public FabbricatoCosapTassa(){
		
		//Caricamento del file di properties dei caricatori		
		this.props = new Properties();
		try {
			InputStream is = this.getClass().getResourceAsStream("/sql/caricatori.sql");
		    this.props.load(is);  
		    		   
		}catch(Exception e) {
		    log.error("Eccezione: "+e.getMessage());			   
		}																
	}
	
	/**
	 * Metodo che controlla se la tabella del DWH � stata droppata
	 */
	@Override
	public boolean dwhIsDrop(Connection conn) throws Exception {
		
		String sql = this.getProperty("SQL_DWH_IS_DROP_FABB");
		
		sql = sql.replace("$TAB", this.getTable());
		
		int fkEnteSorgente = this.getFkEnteSorgente();
		int progEs = this.getProgEs();
		
		return super.executeIsDropDWh(sql, conn, fkEnteSorgente, progEs);
	}
	
	@Override
	//Metodo che indica se la tabella � gestita o no tramite PROCESSID
	public boolean existProcessId(){
		return true;
	}
	

	@Override
	//Metodo che restituisce la query di cancellazione tabella Totale
	public String getDeleteSQL(){
		
		String sqlDeleteTot = this.getProperty("SQL_DEL_FABB_TOTALE");		
		return sqlDeleteTot;
	}

	@Override
	public int getFkEnteSorgente() {
		
		return 14;
	}

	@Override
	//Metodo che restituisce la query di inserimento in tabella Totale
	public String getInsertSQL(){
		
		String sqlInsertTot = this.getProperty("SQL_INS_FABB_TOTALE");		
		return sqlInsertTot;
	}

	@Override
	public int getProgEs() {
		
		return 2;
	}

	//Metodo che restituisce la query che aggiorna e cancella un processId dalla tabella di gestione PID
	@Override
	public String getQuerySQLDeleteProcessId() throws Exception {
		
		String query = this.getProperty("SQL_DELETE_PID_FABB");
		
		return query;
	}

	//Metodo che restituisce la query per trovare i PID della tabella DWH
	@Override
	public String getQuerySQLgetProcessId() throws Exception {
		
		String query = this.getProperty("SQL_GET_PID_DWH_FABB");
		
		return query;
	}
	
	@Override
	//Metodo che restituisce la query dei nuovi processId da caricare
	public String getQuerySQLNewProcessId() throws Exception {

		String query = this.getProperty("SQL_NEW_PID_DWH_FABB");
		
		return query;
	}

	@Override
	//Metodo che restituisce la query per l'inserimento del PID elaborato per gestione con PID
	public String getQuerySQLSaveProcessId() throws Exception {
		
		String query = this.getProperty("SQL_INSERT_PID_FABB");
			
		return query;
	}

	@Override
	//Metodo che restituisce la query per l'update del PID elaborato per gestione con PID
	public String getQuerySQLUpdateProcessId() throws Exception {
			
		String query = this.getProperty("SQL_UPDATE_PID_FABB");
		
		return query;
	}

	@Override
	//Metodo che restituisce la query di ricerca in tabella Totale
	public String getSearchSQL(){
		
		String sqlSearchTot = this.getProperty("SQL_CERCA_FABB_TOTALE");		
		return sqlSearchTot;
	}

	@Override
	//Metodo che restituisce le query per il caricamento da DHW
	public String getSql(String processID) {

		String sqlFabbricato = this.getProperty("SQL_FABB_COSAP_TASSA");
				
		if (processID != null && !processID.equals("")){
			sqlFabbricato = sqlFabbricato + " WHERE PROCESSID =?";
		}
			
		return sqlFabbricato;
				
	}

	@Override
	//Metodo che restituisce la tabella del DWH
	public String getTable() {
		
		//Tabella del DHW da cui leggere i dati
		String tabella = "SIT_T_COSAP_TASSA";		
		return tabella;
	}

	@Override
	//Metodo che restituisce la query di update in tabella Totale
	public String getUpdateSQL(){
		
		String sqlUpdateTot = this.getProperty("SQL_UPDATE_FABB_TOTALE");		
		return sqlUpdateTot;
	}

	@Override
	//Metodo che mappa normalizza e salva i dati
	public void prepareSaveDato(DatoDwh classeFonte, Connection connForInsert, String insFabbricatoTotale, ResultSet rs, String codEnte, HashParametriConfBean paramConfBean) throws Exception {

		SitFabbricatoTotale sft = new SitFabbricatoTotale();
		IndicePK iPk = new IndicePK();
		
		
		try{			
			
			sft.setIdStorico(rs.getString("ID_STORICO"));
			sft.setSezione(rs.getString("SEZIONE"));
			sft.setFoglio(rs.getString("FOGLIO"));
			sft.setParticella(rs.getString("PARTICELLA"));			
			sft.setDtInizioVal(rs.getDate("DT_INIZIO_VAL"));
			sft.setDtFineVal(rs.getDate("DT_FINE_VAL"));							
			sft.setFkFabbricato(null);
			sft.setRelDescr(null);
			sft.setRating(null);
			sft.setNote(rs.getString("NOTE"));
			sft.setAnomalia(null);					
			
			
			sft.setProcessId(rs.getString("PROCESSID"));
			sft.setDtInizioDato(rs.getDate("DT_INIZIO_DATO"));
			sft.setDtFineDato(rs.getDate("DT_FINE_DATO"));
			sft.setDtExpDato(rs.getDate("DT_EXP_DATO"));
			sft.setProvenienza(rs.getString("PROVENIENZA"));
			sft.setDataRegistrazione(rs.getDate("DATA_REGISTRAZIONE"));
						
			sft.setCategoria(rs.getString("CATEGORIA"));
			sft.setClasse(rs.getString("CLASSE"));
			sft.setRendita(rs.getString("RENDITA"));
			sft.setZona(rs.getString("ZONA"));
			sft.setFoglioUrbano(rs.getString("FOGLIO_URBANO"));
			sft.setSuperficie(rs.getString("SUPERFICIE"));
			sft.setScala(rs.getString("SCALA"));
			sft.setInterno(rs.getString("INTERNO"));
			sft.setPiano(rs.getString("PIANO"));
			
			sft.setField1(rs.getString("FIELD1"));
			sft.setField2(rs.getString("FIELD2"));
			sft.setField3(rs.getString("FIELD3"));
			sft.setField4(rs.getString("FIELD4"));
			sft.setField5(rs.getString("FIELD5"));
			sft.setField6(rs.getString("FIELD6"));
			sft.setField7(rs.getString("FIELD7"));
			sft.setField8(rs.getString("FIELD8"));
			sft.setField9(rs.getString("FIELD9"));
			sft.setField10(rs.getString("FIELD10"));
			sft.setField11(rs.getString("FIELD11"));
			sft.setField12(rs.getString("FIELD12"));
			sft.setField13(rs.getString("FIELD13"));
			sft.setField14(rs.getString("FIELD14"));
			sft.setField15(rs.getString("FIELD15"));
			sft.setField16(rs.getString("FIELD16"));
			sft.setField17(rs.getString("FIELD17"));
			sft.setField18(rs.getString("FIELD18"));
			sft.setField19(rs.getString("FIELD19"));
			sft.setField20(rs.getString("FIELD20"));
			sft.setField21(rs.getString("FIELD21"));
			sft.setField22(rs.getString("FIELD22"));
			sft.setField23(rs.getString("FIELD23"));
			sft.setField24(rs.getString("FIELD24"));
			sft.setField25(rs.getString("FIELD25"));
			sft.setField26(rs.getString("FIELD26"));
			sft.setField27(rs.getString("FIELD27"));
			sft.setField28(rs.getString("FIELD28"));
			sft.setField29(rs.getString("FIELD29"));
					
			//Normalizzazione Dati
			NormalizzaTotali.normalizzaFabbricato(sft);
			
			//Setto indice Fabbricato
			iPk.setIdDwh(rs.getString("ID_DWH"));
			iPk.setFkEnteSorgente(this.getFkEnteSorgente());
			iPk.setProgEs(this.getProgEs());							
			//Calcolo Hash di chiave
			iPk.setCtrHash(setCtrHashSitFabbricatoTotale(sft));		
			
			sft.setId(iPk);
		
			
			//Salvataggio
			super.saveSitFabbricatoTotale(classeFonte, connForInsert, insFabbricatoTotale, sft);

			
		}catch (Exception e) {
			log.warn("Errore:Save Fabbricato Cosap Tassa="+e.getMessage());
			Exception ea = new Exception("Errore:Save Cosap Tassa:"+e.getMessage());
			throw ea;
		}
		
		
	}

	@Override 
	//Metodo che mappa normalizza e aggiorna i dati
	public void prepareUpdateDato(DatoDwh classeFonte, Connection connForInsert, String insFabbricatoTotale, String updateFabbricatoTotale, String searchFabbricatoTotale, ResultSet rs, String codEnte, HashParametriConfBean paramConfBean) throws Exception {

		SitFabbricatoTotale sft = new SitFabbricatoTotale();
		NormalizzaTotali nt = new NormalizzaTotali();
		IndicePK iPk = new IndicePK();
		
		try{			
						
			sft.setIdStorico(rs.getString("ID_STORICO"));
			sft.setSezione(rs.getString("SEZIONE"));
			sft.setFoglio(rs.getString("FOGLIO"));
			sft.setParticella(rs.getString("PARTICELLA"));			
			sft.setDtInizioVal(rs.getDate("DT_INIZIO_VAL"));
			sft.setDtFineVal(rs.getDate("DT_FINE_VAL"));							
			sft.setFkFabbricato(null);
			sft.setRelDescr(null);
			sft.setRating(null);
			sft.setNote(rs.getString("NOTE"));
			sft.setAnomalia(null);					
			
			
			sft.setProcessId(rs.getString("PROCESSID"));
			sft.setDtInizioDato(rs.getDate("DT_INIZIO_DATO"));
			sft.setDtFineDato(rs.getDate("DT_FINE_DATO"));
			sft.setDtExpDato(rs.getDate("DT_EXP_DATO"));
			sft.setProvenienza(rs.getString("PROVENIENZA"));
			sft.setDataRegistrazione(rs.getDate("DATA_REGISTRAZIONE"));
						
			sft.setCategoria(rs.getString("CATEGORIA"));
			sft.setClasse(rs.getString("CLASSE"));
			sft.setRendita(rs.getString("RENDITA"));
			sft.setZona(rs.getString("ZONA"));
			sft.setFoglioUrbano(rs.getString("FOGLIO_URBANO"));
			sft.setSuperficie(rs.getString("SUPERFICIE"));
			sft.setScala(rs.getString("SCALA"));
			sft.setInterno(rs.getString("INTERNO"));
			sft.setPiano(rs.getString("PIANO"));
			
			sft.setField1(rs.getString("FIELD1"));
			sft.setField2(rs.getString("FIELD2"));
			sft.setField3(rs.getString("FIELD3"));
			sft.setField4(rs.getString("FIELD4"));
			sft.setField5(rs.getString("FIELD5"));
			sft.setField6(rs.getString("FIELD6"));
			sft.setField7(rs.getString("FIELD7"));
			sft.setField8(rs.getString("FIELD8"));
			sft.setField9(rs.getString("FIELD9"));
			sft.setField10(rs.getString("FIELD10"));
			sft.setField11(rs.getString("FIELD11"));
			sft.setField12(rs.getString("FIELD12"));
			sft.setField13(rs.getString("FIELD13"));
			sft.setField14(rs.getString("FIELD14"));
			sft.setField15(rs.getString("FIELD15"));
			sft.setField16(rs.getString("FIELD16"));
			sft.setField17(rs.getString("FIELD17"));
			sft.setField18(rs.getString("FIELD18"));
			sft.setField19(rs.getString("FIELD19"));
			sft.setField20(rs.getString("FIELD20"));
			sft.setField21(rs.getString("FIELD21"));
			sft.setField22(rs.getString("FIELD22"));
			sft.setField23(rs.getString("FIELD23"));
			sft.setField24(rs.getString("FIELD24"));
			sft.setField25(rs.getString("FIELD25"));
			sft.setField26(rs.getString("FIELD26"));
			sft.setField27(rs.getString("FIELD27"));
			sft.setField28(rs.getString("FIELD28"));
			sft.setField29(rs.getString("FIELD29"));
			
					
			//Normalizzazione Dati
			nt.normalizzaFabbricato(sft);
			
			//Setto indice Fabbricato
			iPk.setIdDwh(rs.getString("ID_DWH"));
			iPk.setFkEnteSorgente(this.getFkEnteSorgente());
			iPk.setProgEs(this.getProgEs());							
			//Calcolo Hash di chiave
			iPk.setCtrHash(setCtrHashSitFabbricatoTotale(sft));		
			
			sft.setId(iPk);
								
			
			String azione = super.trovaDatoTotaleCtrHash(classeFonte, connForInsert,searchFabbricatoTotale,iPk);								
														
			
			if(azione.equals("AGGIORNA")){
				//Aggiorna elemento gi� presente
				super.updateSitFabbricatoTotale(classeFonte,connForInsert,updateFabbricatoTotale,sft);										
			}else if(azione.equals("INSERISCI")){
				//Salva il nuovo elemento										
				super.saveSitFabbricatoTotale(classeFonte, connForInsert, insFabbricatoTotale, sft);										
			}

			
		}catch (Exception e) {
			log.warn("Errore:Update Fabbricato Cosap Tassa= "+e.getMessage());
			Exception ea = new Exception("Errore:Update Fabbricato Cosap Tassa:"+e.getMessage());
			throw ea;
		}
		
	}

	@Override
	//Metodo che indica se la query di lettura da DWH ha come parametro il CodiceEnte
	public boolean queryWithParamCodEnte(){
		return false;
	}
	
	//Metodo che restituisce la query di caricamento letta da file di property
	private String getProperty(String propName) {

		String p = this.props.getProperty(propName+ "." + this.getFkEnteSorgente());
		
		if (p==null)
			p = this.props.getProperty(propName);
			
		return p;
	}

}
