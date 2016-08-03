/*
 * Created on 2-lug-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.condomini.logic;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.condomini.bean.Condomini;
import it.escsolution.escwebgis.condomini.bean.CondominiFinder;

import java.sql.Connection;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * @author Administrator To change the template for this generated type comment
 *         go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 *         Comments
 */
public class CondominiCondominiLogic extends EscLogic
{

	public CondominiCondominiLogic(EnvUtente eu)
	{
		super(eu);
	}

	/*
	 * private final static String SQL_SELECT_LISTA = "select * from (" +
	 * "select ROWNUM as
	 * N,CODCTB,DENOMINAZIONE,INDIRIZZO,CODICEFISCALE,PARTITAIVA from ( " +
	 * "select ROWNUM as N,"+ "decode(MI_CONDOMINI.DENOMINAZIONE, null, '-',
	 * MI_CONDOMINI.DENOMINAZIONE) as DENOMINAZIONE, " +
	 * "decode(MI_CONDOMINI.INDIRIZZO, null, '-',MI_CONDOMINI.INDIRIZZO) as
	 * INDIRIZZO, " + "decode(MI_CONDOMINI.CODICEFISCALE, null, '-',
	 * MI_CONDOMINI.CODICEFISCALE) as CODICEFISCALE, " +
	 * "decode(MI_CONDOMINI.PARTITAIVA, null, '-', MI_CONDOMINI.PARTITAIVA) as
	 * PARTITAIVA, " + "MI_CONDOMINI.CODCTB as CODCTB " + " from MI_CONDOMINI
	 * where 1=?" ;
	 */

	private final static String SQL_SELECT_LISTA = "select * from (" + "select CODCTB,DENOMINAZIONE,CODVIA,INDIRIZZO,CIVICO, ROWNUM AS n from ( " + "select DISTINCT " + "decode(MI_CONDOMINI.DENOMINAZIONE, null, '-', MI_CONDOMINI.DENOMINAZIONE) as DENOMINAZIONE, " + "decode(MI_CONDOMINI.INDIRIZZO, null, '-',MI_CONDOMINI.INDIRIZZO) as INDIRIZZO, "
			+ "decode(MI_CONDOMINI.CODVIA, null, '-', MI_CONDOMINI.CODVIA) as CODVIA, " + "decode(MI_CONDOMINI.CIVICO, null, '-', MI_CONDOMINI.CIVICO) as CIVICO, " + "MI_CONDOMINI.CODCTB as CODCTB " + " from MI_CONDOMINI where 1=? ";

	private final static String SQL_SELECT_COUNT_LISTA = "select count(*) as conteggio  from MI_CONDOMINI WHERE 1=?";

	private final static String SQL_SELECT_COUNT_LISTA_DISTINCT = "select count(*) as conteggio  from (select distinct MI_CONDOMINI.DENOMINAZIONE,MI_CONDOMINI.CODVIA,MI_CONDOMINI.INDIRIZZO,MI_CONDOMINI.CIVICO from MI_CONDOMINI WHERE 1=? ";

	private final static String SQL_SELECT_COUNT_ALL = "select count(*) as conteggio  from MI_CONDOMINI WHERE 1=?";

	private final static String SQL_SELECT_COUNT_DISTINCT = "select count(*) as conteggio  from (select distinct MI_CONDOMINI.DENOMINAZIONE,MI_CONDOMINI.CODVIA,MI_CONDOMINI.INDIRIZZO,MI_CONDOMINI.CIVICO from MI_CONDOMINI WHERE 1=?)";

	public Hashtable mCaricareListaCondomini(Vector listaPar, CondominiFinder finder) throws Exception
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
			/*
			 * sql = SQL_SELECT_COUNT_DISTINCT; int indice = 1;
			 * this.initialize(); this.setInt(indice,1); indice ++;
			 * prepareStatement(sql); rs = executeQuery(conn); if (rs.next()){
			 * conteggione = rs.getLong("conteggio"); }
			 */

			for (int i = 0; i <= 1; i++)
			{
				// il primo ciclo faccio la count
				if (i == 0)
					sql = SQL_SELECT_COUNT_LISTA_DISTINCT;
				else
					sql = SQL_SELECT_LISTA;

				indice = 1;
				this.initialize();
				this.setInt(indice, 1);
				indice++;

				// il primo passaggio esegue la select count
				// campi della search

				// il primo passaggio esegue la select count
				if (finder.getKeyStr().equals(""))
				{
					sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
					if (i == 0)
						sql += ")";
				}
				else
				{
					sql = sql + " and MI_CONDOMINI.CODCTB in (" + finder.getKeyStr() + ")";

				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale() - 1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
				sql = sql + " order by DENOMINAZIONE ";
				if (i == 1)
					sql = sql + ")) where n > " + limInf + " and n <=" + limSup;

				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				if (i == 1)
				{
					while (rs.next())
					{
						// campi della lista
						Condomini cond = new Condomini();
						cond.setCodctb(rs.getString("CODCTB"));
						cond.setDenominazione(rs.getString("DENOMINAZIONE"));
						cond.setIndirizzo(rs.getString("INDIRIZZO"));
						cond.setCodvia(rs.getString("CODVIA"));
						cond.setCivico(rs.getString("CIVICO"));

						vct.add(cond);
					}
				}
				else
				{
					if (rs.next())
					{
						conteggio = rs.getString("conteggio");
					}
				}
			}
			ht.put("LISTA_CONDOMINI", vct);
			finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			// pagine totali
			finder.setPagineTotali(1 + new Double(Math.ceil((new Long(conteggio).longValue() - 1) / RIGHE_PER_PAGINA)).longValue());
			finder.setTotaleRecord(conteggione);
			finder.setRighePerPagina(RIGHE_PER_PAGINA);

			ht.put("FINDER", finder);
			
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
			// TODO Auto-generated catch block
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}

	}

	public Hashtable mCaricareDettaglioCondominio(String chiave) throws Exception
	{

		Hashtable ht = new Hashtable();
		// faccio la connessione al db
		Connection conn = null;
		try
		{

			conn = this.getConnection();
			this.initialize();

			String sql = "select * from MI_CONDOMINI where nvl(MI_CONDOMINI.CODCTB,'-1')  = ? " + "AND  nvl(MI_CONDOMINI.DENOMINAZIONE,'-') = ? " + "AND  nvl(MI_CONDOMINI.CODVIA, '-') = ? " + "AND  nvl(MI_CONDOMINI.INDIRIZZO,'-')  = ? " + "AND  nvl(MI_CONDOMINI.CIVICO,'-1')  = ? ";

			int indice = 1;
			// recupero le chiavi di ricerca
			if (chiave == null || chiave.trim().equals("") || chiave.trim().equals("-1"))
			{
				this.setString(1, "");
				this.setString(2, "");
				this.setString(3, "");
				this.setString(4, "");
				this.setString(5, "");
			}
			else
			{
				StringTokenizer st = new StringTokenizer(chiave, "@");
				String valore = null;
				while (st.hasMoreTokens())
				{
					String val = st.nextToken();
					if (val.equals("-") && (indice == 1 || indice == 5))
						this.setString(indice, "-1");
					else
						this.setString(indice, val);

					indice++;
				}
			}
			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			Vector vct = new Vector();
			Condomini cond = new Condomini();
			boolean primoGiro=true;
			while (rs.next())
			{

				Condomini cond1 = new Condomini();
				// campi della lista
				cond1.setCodctb(tornaValoreRS(rs, "CODCTB"));
				cond1.setDenominazione(tornaValoreRS(rs, "DENOMINAZIONE"));
				cond1.setCodvia(tornaValoreRS(rs, "CODVIA"));
				cond1.setIndirizzo(tornaValoreRS(rs, "INDIRIZZO"));
				cond1.setCivico(tornaValoreRS(rs, "CIVICO"));
				cond1.setEspciv(tornaValoreRS(rs, "ESPCIV"));
				cond1.setClassetar(tornaValoreRS(rs, "CLASSETAR"));
				cond1.setRiduzione(tornaValoreRS(rs, "RIDUZIONE"));
				cond1.setSuperf(tornaValoreRS(rs, "SUPERF"));
				cond1.setCaricoarr(tornaValoreRS(rs, "CARICOARR"));
				cond1.setProg(tornaValoreRS(rs, "PROG"));
				cond1.setCessato(tornaValoreRS(rs, "CESSATO"));
				cond1.setSospeso(tornaValoreRS(rs, "SOSPESO"));
				cond1.setCodiceFiscale(tornaValoreRS(rs, "CODICEFISCALE"));
				cond1.setPartitaIva(tornaValoreRS(rs, "PARTITAIVA"));
				cond1.setObjectid(tornaValoreRS(rs, "OBJECTID"));
				cond1.setPasso(tornaValoreRS(rs, "PASSO"));
				cond1.setCivico_cal(tornaValoreRS(rs, "CIVICO_CAL"));
				vct.add(cond1);

				if(primoGiro)
				{
					cond=cond1;
					primoGiro=false;
					}

			}
			ht.put("CONDOMINIO", cond);
			ht.put("LISTA_CONDOMINI_MULTIPLI", vct);
			
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
