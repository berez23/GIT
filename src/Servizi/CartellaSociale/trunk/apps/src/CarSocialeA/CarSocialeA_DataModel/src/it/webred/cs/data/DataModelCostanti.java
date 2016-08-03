package it.webred.cs.data;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;

public class DataModelCostanti {

	public static final String NOME_APPLICAZIONE = "CarSociale";
	public static final Date END_DATE = new GregorianCalendar(9999, 11, 31).getTime();
	public static final String END_DATE_STRING = "31/12/9999";
	
	public static class Segnalibri implements Serializable {

		private static final long serialVersionUID = 1L;
		
		public static final String CASO_ID = ":CASO_ID";
		public static final String CASO_NOME = ":CASO_NOME";
		public static final String CASO_DATA_CREAZIONE = ":CASO_DATA_CREAZIONE";
		public static final String CASO_DATA_MODIFICA = ":CASO_DATA_MODIFICA";
		public static final String CASO_USERNAME_UTENTE_CREAZIONE = ":CASO_USERNAME_UTENTE_CREAZIONE";
		public static final String CASO_USERNAME_UTENTE_MODIFICA = ":CASO_USERNAME_UTENTE_MODIFICA";

		public static final String ITERSTEP_USERNAME_SEGNALANTE = ":ITERSTEP_USERNAME_SEGNALANTE";
		public static final String ITERSTEP_NOME_SEGNALANTE = ":ITERSTEP_NOME_SEGNALANTE";
		public static final String ITERSTEP_COGNOME_SEGNALANTE = ":ITERSTEP_COGNOME_SEGNALANTE";
		public static final String ITERSTEP_STATO_SEZIONE_ATTRIBUTI = ":ITERSTEP_STATO_SEZIONE_ATTRIBUTI";

		public static final String ITERSTEP_USERNAME_SEGNALATO = ":ITERSTEP_USERNAME_SEGNALATO";
		public static final String ITERSTEP_NOME_SEGNALATO = ":ITERSTEP_NOME_SEGNALATO";
		public static final String ITERSTEP_COGNOME_SEGNALATO = ":ITERSTEP_COGNOME_SEGNALATO";

		public static final String ITERSTEP_DATA_CREAZIONE = ":ITERSTEP_DATA_CREAZIONE";
	}
	
	public static class IterStatoInfo
	{
		public static final Long APERTO = 1L;
		public static final Long CHIUSO = 2L;
		public static final Long PRESO_IN_CARICO = 3L;
		public static final Long SEGNALATO = 4L;
		public static final Long SEGNALATO_OP = 11L;
		public static final Long SEGNALATO_ENTE = 10L;

		public static final Long DEFAULT_ITER_STEP_ID = APERTO;
	}

	public static class TipoAttributo
	{
		public enum Enum 
		{
			LIST,
			BOOLEAN,
			STRING,
			DATE,
			INTEGER,
			
		}
		
		public static Enum ToEnum( String value ) {

			value = StringUtils.trimToEmpty( value );
			
			if( "LIST".compareToIgnoreCase(value) == 0 )
				return Enum.LIST;
			if( "BOOL".compareToIgnoreCase(value) == 0 )
				return Enum.BOOLEAN;
			if( "STRING".compareToIgnoreCase(value) == 0 )
				return Enum.STRING;
			if( "DATE".compareToIgnoreCase(value) == 0 )
				return Enum.DATE;
			if( "INTEGER".compareToIgnoreCase(value) == 0 )
				return Enum.INTEGER;
			
			throw new Error("TipoAttributo \"" +  value + "\" non gestito" );
		}
	}

	public static class StatoTransizione
	{
		public enum Enum 
		{
			IGNORA,
			NON_OBBLIGATORIO,
			OBBLIGATORIO,
			
		}

		public static Enum ToEnum( String value ) {
			
			value = StringUtils.trimToEmpty( value );
			
			if( "IGN".compareToIgnoreCase(value) == 0 )
				return Enum.IGNORA;
			if( "NOBB".compareToIgnoreCase(value) == 0 )
				return Enum.NON_OBBLIGATORIO;
			if( "OBB".compareToIgnoreCase(value) == 0 )
				return Enum.OBBLIGATORIO;

			throw new Error("StatoTransizione \"" +  value + "\" non gestito" );
		}
	}
	
	public static class PermessiGenerali {
		public static final String AMMINISTRAZIONE_ORG_SETT_OP = "Amministra Organizzazioni/Settori/Operatori";
		public static final String GESTIONE_CONFIG = "Gestisci Configurazione";
		public static final String EROGAZIONE_SERV_INTERV = "Eroga Servizi e Interventi";
	}
	
	public static class PermessiCaso {
		public static final String MODIFICA_CASI_SETTORE = "Modifica tutti i casi del mio Settore";
		public static final String VISUALIZZAZIONE_DATI_RISERV = "Accedi ai Dati Riservati";
		public static final String VISUALIZZAZIONE_LISTA_CASI = "Visualizza Elenco Casi";
		public static final String CREAZIONE_CASO = "Crea un caso";
		public static final String VISUALIZZAZIONE_CASI_SETTORE = "Visualizza Tutti i Casi del mio Settore";
		public static final String VISUALIZZAZIONE_CASI_ORG = "Visualizza Tutti i Casi della mia Organizzazione";
		public static final String VISUALIZZAZIONE_CARICO_LAVORO = "Visualizza Carico di Lavoro";
	}
	
	public static class PermessiFascicolo {
		public static final String VISUALIZZAZIONE_FASCICOLO = "Visualizza Fascicolo";
		public static final String GESTIONE_ELEM_FASCICOLO = "Inserisci / Modifica Elementi del Fascicolo";
	}
	
	public static class PermessiDocIndividuali {
		public static final String UPLOAD_DOC_INDIVIDUALI = "Upload Documenti Individuali";
	}
	
	public static class PermessiNotifiche {
		public static final String NOTIFICHE_ITEM = "Notifiche Cartella";
		
		public static final String VISUALIZZA_NOTIFICHE_ORGANIZZAZIONE = "VISUALIZZA-NOTIFICHE-ORGANIZZAZIONE";
		public static final String VISUALIZZA_NOTIFICHE_SETTORE = "VISUALIZZA-NOTIFICHE-SETTORE";
		public static final String VISUALIZZA_NOTIFICHE_TIPO = "VISUALIZZA-NOTIFICHE-TIPO-";
	}
	
	public static class PermessiIterDialog {
		public static final String ITER_DIALOG_PERMESSO_NOTIFICA_SETTORE_SEGNALANTE = "ITER_DIALOG_PERMESSO_NOTIFICA_SETTORE_SEGNALANTE";
	}
	
	public static class PermessiSchedeSegr {
		public static final String VISUALIZZA_SCHEDE_SEGR = "Visualizza Schede Segretariato";
	}
	
	public static class Operatori implements Serializable {
		private static final long serialVersionUID = 1L;
		
		public static final Long ASSISTENTI_SOCIALI_ID = new Long(1);
		public static final Long EDUCATORE_UOL_ID = new Long(2);
		public static final Long PSICOLOGA_ID = new Long(3);
		public static final Long EDUCATORE_ID = new Long(4);
		public static final Long EDUCATORE_SPR_ID = new Long(6);
		public static final Long COP_ID = new Long(5);
	
	}
	
	public static class TipiIndirizzi implements Serializable {
		private static final long serialVersionUID = 1L;
		
		public static final Long RESIDENZA_ID = new Long(1);
	
	}
	
	public static class TipiFamiglia implements Serializable {
		private static final long serialVersionUID = 1L;
		
		public static final String CONVIVENTI = "NUCLEO FAMILIARE CONVIVENTE";
		public static final String AMICI = "GRUPPO AMICALE";
		public static final String ALTRI = "ALTRI FAMILIARI";
	
	}
	
	public enum CategoriaSociale {
		MINORI, MINORI_DISABILI, ADULTI, ADULTI_DISABILI, ANZIANI, ORIENTAMENTO_LAVORO, ALL;
	}
	
	public static class TipiAlert implements Serializable {
		private static final long serialVersionUID = 1L;
		
		public static final String FAMIGLIA_GIT = "FAMGIT";
		public static final String FAMIGLIA_GIT_DESC = "Famiglia Anagrafe";
		public static final String ITER_STEP = "IT";
		public static final String ITER_STEP_DESC = "Iter Step";
		public static final String DOC_IND = "DOCIND";
		public static final String DOC_IND_DESC = "Doc Individuale";
	
	}
	
	public static class TipoDiario implements Serializable {
		private static final long serialVersionUID = 1L;
		
		public static final int CAMBIO_STATO_ID = 1;
		public static final int EROGAZIONE_INT_ID = 2;
		public static final int FOGLIO_AMM_ID = 3;
		public static final int NOTE_ID = 4;
		public static final int COLLOQUIO_ID = 5;
		public static final int RELAZIONE_ID = 6;
		public static final int VALUTAZIONE_MDS_ID = 7;
		public static final int PAI_ID = 8;
		public static final int BARTHEL_ID	= 9;
		public static final int DOC_INDIVIDUALE_ID = 10;
		public static final int DATI_SCUOLA_ID = 11;
		public static final int ISEE_ID = 12;
	}
	
	public static class SchedaSegr implements Serializable {
		private static final long serialVersionUID = 1L;
		
		public static final String STATO_INS = "I";
		public static final String STATO_MOD = "M";
		
		public static final String STATO_CREATA = "CREATA";
		public static final String STATO_RESPINTA = "RESPINTA";
		public static final String STATO_VISTA = "VISTA";
	}
	
	public static class ParamReport implements Serializable {
		private static final long serialVersionUID = 1L;
		
		public static final String SEZIONE = "report";
		
		public static final String PIEDIPAGINA = "piedipagina";
	
	}

}
