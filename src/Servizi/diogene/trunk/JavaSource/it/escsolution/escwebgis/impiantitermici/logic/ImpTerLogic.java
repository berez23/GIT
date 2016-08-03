package it.escsolution.escwebgis.impiantitermici.logic;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.impiantitermici.bean.ImpTer;
import it.escsolution.escwebgis.impiantitermici.bean.ImpTerFinder;

import java.sql.Connection;
import java.util.Hashtable;
import java.util.Vector;

/**
 * @author dan
 *
 */
public class ImpTerLogic extends EscLogic
{

	public ImpTerLogic(EnvUtente eu)
	{
		super(eu);
	}

	private final static String SQL_SELECT_LISTA = "select * from (select ROWNUM n,c.* from (select ter.* from  MI_IMPIANTI_TERMICI ter where 1=? ";

	private final static String SQL_SELECT_COUNT_LISTA = "select count(*) as conteggio  from MI_IMPIANTI_TERMICI WHERE 1=?";

	public Hashtable mCaricareListaImpTer(Vector listaPar, ImpTerFinder finder)
			throws Exception
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

			for (int i = 0; i <= 1; i++)
			{
				// il primo ciclo faccio la count
				if (i == 0)
					sql = SQL_SELECT_COUNT_LISTA;
				else
					sql = SQL_SELECT_LISTA;

				indice = 1;
				this.initialize();
				this.setInt(indice, 1);
				indice++;

				sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale() - 1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
				if (i == 1)
				{
					//sql = sql + " order by TO_DATE(data,'dd/mm/yyyy') ";
					sql = sql + ") c) where n > " + limInf + " and n <=" + limSup;

				}
				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				if (i == 1)
				{
					while (rs.next())
					{
						// campi della lista
						ImpTer impter = new ImpTer();
						impter.setOccupante(tornaValoreRS(rs, "occupante"));
						impter.setTipo_via(tornaValoreRS(rs, "tipo_via"));
						impter.setPref(tornaValoreRS(rs, "pref"));
						impter.setNome_via(tornaValoreRS(rs, "nome_via"));
						impter.setCivico(tornaValoreRS(rs, "civico"));
						impter.setBarrato(tornaValoreRS(rs, "barrato"));
						impter.setScala(tornaValoreRS(rs, "scala"));
						impter.setPiano(tornaValoreRS(rs, "piano"));
						impter.setInterno(tornaValoreRS(rs, "interno"));
						impter.setGruppo(tornaValoreRS(rs, "gruppo"));
						vct.add(impter);
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
			ht.put("LISTA_IMPTER", vct);
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
