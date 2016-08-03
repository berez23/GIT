/*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.esatri.logic;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.esatri.bean.Riversamento;
import it.escsolution.escwebgis.esatri.bean.RiversamentoFinder;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class EsatriRiversamentiLogic extends EscLogic{
	private String appoggioDataSource;
	public EsatriRiversamentiLogic(EnvUtente eu) {
				super(eu);
				appoggioDataSource=eu.getDataSource();
			}

	public static final String FINDER = "FINDER";
	public static final String ESATRI_RIVERSAMENTO = "ESATRI_RIVERSAMENTO";
	public static final String LISTA = "LISTA_ESATRI_RIVERSAMENTO";
	public static final String ESATRIRIV = EsatriRiversamentiLogic.class.getName() + "@ESATRIRIV";

	private final static String SQL_SELECT_LISTA = "SELECT * FROM ( " +
						"SELECT ROWNUM N,ANNO_RIF_RISCOSSIONI,DATA_SCADENZA,PROGRESSIVO_INVIO,CODICE_CONCESSIONE,DATA_RIVERSAMENTO, " +
						"IMPORTO_VERSATO,PK_RIVERSAMENTO,CODICE_FORNITURA FROM( " +
						"SELECT ROWNUM N, " +
						"MILANO.MI_ESATRI_ICI0.ANNO_RIF_RISCOSSIONI, " +
						"SUBSTR(MILANO.MI_ESATRI_ICI0.DATA_SCADENZA,1,2)||'/'||SUBSTR(MILANO.MI_ESATRI_ICI0.DATA_SCADENZA,3,2)||'/'||SUBSTR(MILANO.MI_ESATRI_ICI0.DATA_SCADENZA,5,4) DATA_SCADENZA, " +
						"MILANO.MI_ESATRI_ICI0.PROGRESSIVO_INVIO, " +
						"MILANO.MI_ESATRI_RIV.CODICE_CONCESSIONE, " +
						//"SUBSTR(MILANO.MI_ESATRI_RIV.DATA_RIVERSAMENTO,7,2)||'/'||SUBSTR(MILANO.MI_ESATRI_RIV.DATA_RIVERSAMENTO,5,2)||'/'||SUBSTR(MILANO.MI_ESATRI_RIV.DATA_RIVERSAMENTO,1,4) DATA_RIVERSAMENTO, " +
						"DECODE(nvl(MILANO.MI_ESATRI_RIV.DATA_RIVERSAMENTO,'00000000'),'00000000','-',SUBSTR(MILANO.MI_ESATRI_RIV.DATA_RIVERSAMENTO,7,2)||'/'||SUBSTR(MILANO.MI_ESATRI_RIV.DATA_RIVERSAMENTO,5,2)||'/'||SUBSTR(MILANO.MI_ESATRI_RIV.DATA_RIVERSAMENTO,1,4)) DATA_RIVERSAMENTO, " +
						"MILANO.MI_ESATRI_RIV.IMPORTO_VERSATO, " +
						"MILANO.MI_ESATRI_RIV.PK_RIVERSAMENTO, " +
						"MILANO.MI_ESATRI_RIV.CODICE_FORNITURA " +
						"FROM MILANO.MI_ESATRI_ICI0,MILANO.MI_ESATRI_RIV " +
						"WHERE  1=? " +
						"AND MILANO.MI_ESATRI_ICI0.CODICE_FORNITURA = MILANO.MI_ESATRI_RIV.CODICE_FORNITURA " ;

	private final static String SQL_SELECT_COUNT_LISTA="select count(*) as conteggio  " +
						"from MILANO.MI_ESATRI_ICI0,MILANO.MI_ESATRI_RIV " +
						"WHERE 1=? " +
						"AND MILANO.MI_ESATRI_ICI0.CODICE_FORNITURA = MILANO.MI_ESATRI_RIV.CODICE_FORNITURA " ;






	public Hashtable mCaricareListaRiversamenti(Vector listaPar, RiversamentoFinder finder) throws Exception
	{
		// carico la lista e la metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		sql = "";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;

		// faccio la connessione al db
		try
		{
			conn = this.getConnection();
			int indice = 1;
			java.sql.ResultSet rs = null;

			for (int i=0;i<=1;i++)
			{
				// il primo ciclo faccio la count
				if (i==0)
					sql = SQL_SELECT_COUNT_LISTA;
				else
					sql = SQL_SELECT_LISTA;

				indice = 1;
				this.initialize();
				this.setInt(indice,1);
				indice ++;

				// il primo passaggio esegue la select count
				if (finder.getKeyStr().equals("")){
					sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
				}
				else{
					//controllo provenienza chiamata
					String chiavi = finder.getKeyStr();
				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;

				if (i ==1){
					//sql = sql + " order by COGNOME, NOME";
					sql = sql + ")) where N > " + limInf + " and N <=" + limSup;
				}

				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				if (i ==1) {
					while (rs.next()){
						//campi della lista
						Riversamento riv = new Riversamento();
						riv.setAnnoRiferimento(rs.getString("ANNO_RIF_RISCOSSIONI"));
						riv.setCodConcessione(rs.getString("CODICE_CONCESSIONE"));
						riv.setProgressivoInvio(rs.getString("PROGRESSIVO_INVIO"));
						riv.setDataRiv(rs.getString("DATA_RIVERSAMENTO"));
						riv.setDataScadenza(rs.getString("DATA_SCADENZA"));
						String impV = rs.getString("IMPORTO_VERSATO");
						if (impV.length()>2)
							impV = impV.substring(0,impV.length()-2)+","+impV.substring(impV.length()-2,impV.length());
						riv.setImportoVersato(impV);
						//chiave
						riv.setPkRiversamento(rs.getString("PK_RIVERSAMENTO"));
						riv.setCodFornitura(rs.getString("CODICE_FORNITURA"));

						vct.add(riv);
					}
				}
				else{
					if (rs.next()){
						conteggio = rs.getString("conteggio");
					}
				}
			}

			ht.put(EsatriRiversamentiLogic.LISTA,vct);
			finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			// pagine totali
			finder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
			finder.setTotaleRecord(conteggione);
			finder.setRighePerPagina(RIGHE_PER_PAGINA);

			ht.put(EsatriRiversamentiLogic.FINDER,finder);
			
			/*INIZIO AUDIT*/
			try {
				Object[] arguments = new Object[2];
				arguments[0] = listaPar;
				arguments[1] = finder;
				writeAudit(Thread.currentThread().getStackTrace()[1], arguments, ht);
			} catch (Throwable e) {				
				log.debug("ERRORE nella scrittura dell'audit", e);
			}
			/*FINE AUDIT*/
			
			return ht;
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}


	public Hashtable mCaricareDettaglio(String chiave) throws Exception{

		Hashtable ht = new Hashtable();
		// faccio la connessione al db
		Connection conn = null;
		try {
			//recupero le chiavi di ricerca
			ArrayList listParam = new ArrayList();
			while (chiave.indexOf('|') > 0) {
				listParam.add(chiave.substring(0,chiave.indexOf('|')));
				chiave = chiave.substring(chiave.indexOf('|')+1);
			}
			listParam.add(chiave);


			conn = this.getConnection();
			this.initialize();
			String sql = "SELECT MILANO.MI_ESATRI_ICI0.ANNO_RIF_RISCOSSIONI," +
						 "SUBSTR(MILANO.MI_ESATRI_ICI0.DATA_SCADENZA,1,2)||'/'||SUBSTR(MILANO.MI_ESATRI_ICI0.DATA_SCADENZA,3,2)||'/'||SUBSTR(MILANO.MI_ESATRI_ICI0.DATA_SCADENZA,5,4) DATA_SCADENZA, " +
						 "MILANO.MI_ESATRI_ICI0.PROGRESSIVO_INVIO, " +
						 "MILANO.MI_ESATRI_RIV.CODICE_CONCESSIONE, " +
						 "DECODE(nvl(MILANO.MI_ESATRI_RIV.DATA_RIVERSAMENTO,'00000000'),'00000000','-',SUBSTR(MILANO.MI_ESATRI_RIV.DATA_RIVERSAMENTO,7,2)||'/'||SUBSTR(MILANO.MI_ESATRI_RIV.DATA_RIVERSAMENTO,5,2)||'/'||SUBSTR(MILANO.MI_ESATRI_RIV.DATA_RIVERSAMENTO,1,4)) DATA_RIVERSAMENTO, " +
						 "MILANO.MI_ESATRI_RIV.IMPORTO_VERSATO, " +
						 "MILANO.MI_ESATRI_RIV.CODICE_TESORERIA, " +
						 "MILANO.MI_ESATRI_RIV.CODICE_ENTE, " +
						 "MILANO.MI_ESATRI_RIV.FLAG_TIPO_RIVERSAMENTO, " +
						 "MILANO.MI_ESATRI_RIV.IMPORTO_COMMISSIONE, " +
						 "MILANO.MI_ESATRI_RIV.NUMERO_QUIETANZA, " +
						 "MILANO.MI_ESATRI_RIV.NUMERO_RISCOSSIONI, " +
						 "MILANO.MI_ESATRI_RIV.PROGRESSIVO_RECORD, " +
						 "MILANO.MI_ESATRI_RIV.TIPOLOGIA_RISCOSSIONI_VERSATE, " +
						 "MILANO.MI_ESATRI_RIV.PK_RIVERSAMENTO, " +
						 "MILANO.MI_ESATRI_RIV.CODICE_FORNITURA " +
						 "FROM MILANO.MI_ESATRI_ICI0,MILANO.MI_ESATRI_RIV " +
						 "WHERE MILANO.MI_ESATRI_ICI0.CODICE_FORNITURA = MILANO.MI_ESATRI_RIV.CODICE_FORNITURA " +
						 "and MILANO.MI_ESATRI_RIV.PK_RIVERSAMENTO =  ? ";

			int indice = 1;
			this.setString(indice,chiave);

			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

			Riversamento riv = new Riversamento();

			if (rs.next()){
				riv.setAnnoRiferimento(rs.getString("ANNO_RIF_RISCOSSIONI"));
				riv.setCodConcessione(rs.getString("CODICE_CONCESSIONE"));
				riv.setCodEnte(rs.getString("CODICE_ENTE"));
				riv.setCodFornitura(rs.getString("CODICE_FORNITURA"));
				riv.setCodTesoreria(rs.getString("CODICE_TESORERIA"));
				riv.setDataRiv(rs.getString("DATA_RIVERSAMENTO"));
				riv.setDataScadenza(rs.getString("DATA_SCADENZA"));
				String impC = rs.getString("IMPORTO_COMMISSIONE");
				if (impC.length()>2)
					impC = impC.substring(0,impC.length()-2)+","+impC.substring(impC.length()-2,impC.length());
				riv.setImportoCommissione(impC);
				String impV = rs.getString("IMPORTO_VERSATO");
				if (impV.length()>2)
					impV = impV.substring(0,impV.length()-2)+","+impV.substring(impV.length()-2,impV.length());
				riv.setImportoVersato(impV);
				riv.setNumQuietanza(rs.getString("NUMERO_QUIETANZA"));
				riv.setNumRiscossioni(rs.getString("NUMERO_RISCOSSIONI"));
				riv.setPkRiversamento(rs.getString("PK_RIVERSAMENTO"));
				riv.setProgRecord(rs.getString("PROGRESSIVO_RECORD"));
				riv.setProgressivoInvio(rs.getString("PROGRESSIVO_INVIO"));
				riv.setTipoRec("1");
				riv.setTipoRisc(rs.getString("TIPOLOGIA_RISCOSSIONI_VERSATE"));
				riv.setTipoRiv(rs.getString("FLAG_TIPO_RIVERSAMENTO"));
			}
			ht.put(EsatriRiversamentiLogic.ESATRI_RIVERSAMENTO,riv);
			
			/*INIZIO AUDIT*/
			try {
				Object[] arguments = new Object[1];
				arguments[0] = chiave;
				writeAudit(Thread.currentThread().getStackTrace()[1], arguments, ht);
			} catch (Throwable e) {				
				log.debug("ERRORE nella scrittura dell'audit", e);
			}
			/*FINE AUDIT*/
			
			return ht;
		}
		catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}

	}

}
