/*
 * Created on 03-dic-2007
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.redditi2004.logic;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.redditi2004.bean.DecoQuadro;
import it.escsolution.escwebgis.redditi2004.bean.Redditi;
import it.escsolution.escwebgis.redditi2004.bean.RedditiFinder;
import it.webred.utils.DateFormat;
import it.webred.utils.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Vector;

import org.apache.commons.lang.time.DateUtils;



/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Redditi2004Logic extends EscLogic{
	private String appoggioDataSource;
	public Redditi2004Logic(EnvUtente eu) {
				super(eu);
				appoggioDataSource=eu.getDataSource();
			}

	public static final String FINDER = "FINDER";
	public static final String REDDITI = Redditi2004Logic.class.getName() + "@REDDITIKEY";
	public static final String ALTRIREDDITI = Redditi2004Logic.class.getName() + "@ALTRIREDDITI";
	public static final String LISTA = "LISTA_REDDITI2004";

	private final static String SQL_SELECT_LISTA = "SELECT * FROM( " +
	"select rownum n, R.*, " +
	  "c01.cod_riga cod_riga_01, c01.tipo_modello tipo_modello_01, c01.note note_01, c01.quadro quadro_01, c01.descrizione descrizione_01, "+ 
	  "c02.cod_riga cod_riga_02, c02.tipo_modello tipo_modello_02, c02.note note_02, c02.quadro quadro_02, c02.descrizione descrizione_02, "+ 
	  "c03.cod_riga cod_riga_03, c03.tipo_modello tipo_modello_03, c03.note note_03, c03.quadro quadro_03, c03.descrizione descrizione_03, "+ 
	  "c04.cod_riga cod_riga_04, c04.tipo_modello tipo_modello_04, c04.note note_04, c04.quadro quadro_04, c04.descrizione descrizione_04, "+ 
	  "c05.cod_riga cod_riga_05, c05.tipo_modello tipo_modello_05, c05.note note_05, c05.quadro quadro_05, c05.descrizione descrizione_05, "+ 
	  "c06.cod_riga cod_riga_06, c06.tipo_modello tipo_modello_06, c06.note note_06, c06.quadro quadro_06, c06.descrizione descrizione_06, "+ 
	  "c07.cod_riga cod_riga_07, c07.tipo_modello tipo_modello_07, c07.note note_07, c07.quadro quadro_07, c07.descrizione descrizione_07, "+ 
	  "c08.cod_riga cod_riga_08, c08.tipo_modello tipo_modello_08, c08.note note_08, c08.quadro quadro_08, c08.descrizione descrizione_08, "+ 
	  "c09.cod_riga cod_riga_09, c09.tipo_modello tipo_modello_09, c09.note note_09, c09.quadro quadro_09, c09.descrizione descrizione_09, "+ 
	  "c10.cod_riga cod_riga_10, c10.tipo_modello tipo_modello_10, c10.note note_10, c10.quadro quadro_10, c10.descrizione descrizione_10, "+ 
	  "c11.cod_riga cod_riga_11, c11.tipo_modello tipo_modello_11, c11.note note_11, c11.quadro quadro_11, c11.descrizione descrizione_11, "+ 
	  "c12.cod_riga cod_riga_12, c12.tipo_modello tipo_modello_12, c12.note note_12, c12.quadro quadro_12, c12.descrizione descrizione_12, "+ 
	  "c13.cod_riga cod_riga_13, c13.tipo_modello tipo_modello_13, c13.note note_13, c13.quadro quadro_13, c13.descrizione descrizione_13 "+
	"from RED_DICHSINT_A2004M3_P0021 R " +
	", RED_CODICI_RIGHE C01 " +
	", RED_CODICI_RIGHE C02 " +
	", RED_CODICI_RIGHE C03 " +
	", RED_CODICI_RIGHE C04 " +
	", RED_CODICI_RIGHE C05 " +
	", RED_CODICI_RIGHE C06 " +
	", RED_CODICI_RIGHE C07 " +
	", RED_CODICI_RIGHE C08 " +
	", RED_CODICI_RIGHE C09 " +
	", RED_CODICI_RIGHE C10 " +
	", RED_CODICI_RIGHE C11 " +
	", RED_CODICI_RIGHE C12 " +
	", RED_CODICI_RIGHE C13 " +
	"WHERE  1=? and " +
	"R.COD_QUADRO_01 = C01.COD_RIGA (+) and " +
	"R.COD_QUADRO_02 = C02.COD_RIGA (+) and " +
	"R.COD_QUADRO_03 = C03.COD_RIGA (+) and " +
	"R.COD_QUADRO_04 = C04.COD_RIGA (+)  and " +
	"R.COD_QUADRO_05 = C05.COD_RIGA (+) and " +
	"R.COD_QUADRO_06 = C06.COD_RIGA (+) and " +
	"R.COD_QUADRO_07 = C07.COD_RIGA (+) and " +
	"R.COD_QUADRO_08 = C08.COD_RIGA (+) and " +
	"R.COD_QUADRO_09 = C09.COD_RIGA (+) and " +
	"R.COD_QUADRO_10 = C10.COD_RIGA (+) and " +
	"R.COD_QUADRO_11 = C11.COD_RIGA (+) and " +
	"R.COD_QUADRO_12 = C12.COD_RIGA (+) and " + 
	"R.COD_QUADRO_13 = C13.COD_RIGA (+) " ;

	private final static String SQL_SELECT_COUNT_LISTA="select count(*) as conteggio " +
					"FROM RED_DICHSINT_A2004M3_P0021 where 1= ? " ;


	public Hashtable mCaricareDettaglio(String chiave) throws Exception{

		Hashtable ht = new Hashtable();
		// faccio la connessione al db
		Connection conn = null;
		try {

			conn = this.getConnection();
			this.initialize();
			String sql = SQL_SELECT_LISTA + " and dic_cf = ? )";

			int indice = 1;
			this.setInt(indice,1);
			indice++;
			this.setString(indice,(String)chiave);

			Redditi red = new Redditi();
			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			
			String sqlAltroDichiarante = "select ID_TELE_DIC, DIC_COGN, dic_nome, dic_cf from red_dichsint_a2004m3_p0021 red"+
										" where ID_TELE_DIC = ? and  dic_cf <> ?";
			
			if (rs.next()){

				
				
				// cerco eventuali altri soggetti con stesso ID_TELE_RIC
				Statement stmt = null;
				PreparedStatement pstm  = null;
				ResultSet altroRs =null;
				Vector vct = new Vector();
				try {
					
					pstm = conn.prepareStatement(sqlAltroDichiarante);
					pstm.setString(1,rs.getString("ID_TELE_DIC") );
					pstm.setString(2, rs.getString("DIC_CF"));
					altroRs = pstm.executeQuery();
					while (altroRs.next()){
						Redditi altroRed = new Redditi();
						altroRed.setIdTeleDic(altroRs.getString("ID_TELE_DIC"));
						altroRed.setDicCf(altroRs.getString("DIC_CF"));
						altroRed.setDicCogn(altroRs.getString("DIC_COGN"));
						altroRed.setDicNome(altroRs.getString("DIC_NOME"));
						vct.add(altroRed);
					}
					ht.put(Redditi2004Logic.ALTRIREDDITI, vct);

				} finally {
					if (altroRs!=null)
						altroRs.close();
					if (pstm!=null)
						pstm.close();
					if (stmt!=null)
						stmt.close();
				}

				
				red.setTipoRecord(rs.getString("TIPO_RECORD"));
				red.setTipoMod(rs.getString("TIPO_MOD"));
				red.setIdTeleDic(rs.getString("ID_TELE_DIC"));
				red.setFlagCorr(rs.getString("FLAG_CORR"));
				red.setFlagInte(rs.getString("FLAG_INTE"));
				red.setStatoDic(rs.getString("STATO_DIC"));
				red.setFlagValuta(rs.getString("FLAG_VALUTA"));

				red.setDicCf(rs.getString("DIC_CF"));
				red.setFlagDicConiu(rs.getString("FLAG_DIC_CONIU"));
				red.setDicCogn(rs.getString("DIC_COGN"));
				red.setDicNome(rs.getString("DIC_NOME"));
				red.setDicNaLuogo(rs.getString("DIC_NA_LUOGO"));
				Date dtNas = DateFormat.stringToDate(rs.getString("DIC_NA_DATA"), "yyyyMMdd");
				
				red.setDicNaData(DateFormat.dateToString(dtNas, "dd/MM/yyyy"));
				
				red.setDicSesso(rs.getString("DIC_SESSO"));
				red.setDicAttRe(rs.getString("DIC_ATT_RE"));
				red.setDicAttRf(rs.getString("DIC_ATT_RF"));
				red.setDicAttRg(rs.getString("DIC_ATT_RG"));
				red.setDicFiller(rs.getString("DIC_FILLER"));
				red.setDomFiscAnnoPrec(rs.getString("DOM_FISC_ANNO_PREC"));
				red.setDomFiscAnnoPrec(rs.getString("DOM_FISC_ANNO_ATT"));

				red.setIndirAttuale(rs.getString("INDIR_ATTUALE"));
				
				HashMap<String, DecoQuadro> quadro = new LinkedHashMap<String, DecoQuadro>();
				
				for (int i=1;i<=13;i++) {
					String prog = StringUtils.padding(new Integer(i).toString() , 2, '0',true);
					DecoQuadro d = new DecoQuadro(
					rs.getString("COD_RIGA_"+ prog),
					rs.getString("TIPO_MODELLO_"+ prog),
					rs.getString("NOTE_"+ prog),
					rs.getString("QUADRO_"+ prog),
					rs.getString("DESCRIZIONE_"+ prog),
					rs.getString("VALORE_" + prog));
					
					red.addQuadro(prog, d);

				}
			}

			ht.put(Redditi2004Logic.REDDITI,red);

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


	public Hashtable mCaricareLista(Vector listaPar, RedditiFinder finder) throws Exception
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
					sql = sql + " order by dic_cogn,dic_nome ";
					sql = sql + ") where N > " + limInf + " and N <=" + limSup;
				}

				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				if (i ==1) {
					while (rs.next()){
						//campi della lista
						Redditi red = new Redditi();
						red.setIdTeleDic(rs.getString("ID_TELE_DIC"));
						red.setDicCf(rs.getString("DIC_CF"));
						red.setDicCogn(rs.getString("DIC_COGN"));
						red.setDicNome(rs.getString("DIC_NOME"));

						vct.add(red);
					}
				}
				else{
					if (rs.next()){
						conteggio = rs.getString("conteggio");
					}
				}
			}

			ht.put(Redditi2004Logic.LISTA,vct);
			finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			// pagine totali
			finder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
			finder.setTotaleRecord(conteggione);
			finder.setRighePerPagina(RIGHE_PER_PAGINA);

			ht.put(Redditi2004Logic.FINDER,finder);
			
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


}
