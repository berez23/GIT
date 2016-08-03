/*
 * Created on 2-lug-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.soggettoanomalie.logic;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.soggettoanomalie.bean.SoggettoAnomalia;
import it.escsolution.escwebgis.soggettoanomalie.bean.SoggettoAnomaliaFinder;

import java.sql.Connection;
import java.util.Hashtable;
import java.util.Vector;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SoggettoAnomalieLogic extends EscLogic {

	public SoggettoAnomalieLogic(EnvUtente eu) {
		super(eu);
	}

	private final static String SQL_SELECT_LISTA = "select * from ("
			+ "select t.*,  ROWNUM AS n from ("
			+ "select "
			+ "TO_CHAR (nasdata,'DD/MM/YYYY') AS nasdata1 , TO_CHAR (data_caricamento,'DD/MM/YYYY') AS data_caricamento1, sit_sgt_scarti.*, SIT_DATABASE_LOOKUP.DESCRIZIONE AS DESCDB  from SIT_SGT_SCARTI, SIT_DATABASE_LOOKUP WHERE FK_DB= ID_DB(+) AND 1=?";

	private final static String SQL_SELECT_COUNT_LISTA = "select count(*) as conteggio  from SIT_SGT_SCARTI WHERE 1=?";

	private final static String SQL_SELECT_COUNT_ALL = "select count(*) as conteggio  from SIT_SGT_SCARTI WHERE 1=?";

	public Hashtable mCaricareListaSoggettiAnomalia(Vector listaPar,
			SoggettoAnomaliaFinder finder) throws Exception {
		// carico la lista e la metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		sql = "";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;

		// faccio la connessione al db
		try {
			conn = this.getConnection();
			int indice = 1;
			java.sql.ResultSet rs = null;
			/*
			sql = SQL_SELECT_COUNT_ALL;
			int indice = 1;
			this.initialize();
			this.setInt(indice, 1);
			indice++;
			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn);
			if (rs.next()) {
				conteggione = rs.getLong("conteggio");
			}
			*/

			for (int i = 0; i <= 1; i++) {
				// il primo ciclo faccio la count
				if (i == 0)
					sql = SQL_SELECT_COUNT_LISTA;
				else
					sql = SQL_SELECT_LISTA;

				indice = 1;
				this.initialize();
				this.setInt(indice, 1);
				indice++;

				//il primo passaggio esegue la select count
				//campi della search

				// il primo passaggio esegue la select count
				if (finder.getKeyStr().equals("")) {
					sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
				} else {
					sql = sql + " and SIT_SGT_SCARTI.PK_IDINTERNOSGTSCARTI in ("
							+ finder.getKeyStr() + ")";

				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale() - 1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
				sql = sql + " order by sit_sgt_scarti.COGNOME, sit_sgt_scarti.NOME ";
				if (i == 1)
					sql = sql + ")t) where N > " + limInf + " and N <=" + limSup;

				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				if (i == 1) {
					while (rs.next()) {
						//campi della lista
						SoggettoAnomalia anomalia = new SoggettoAnomalia();

						anomalia.setPk_idinternosgtscarti(tornaValoreRS(rs,"pk_idinternosgtscarti"));
						anomalia.setRegola(tornaValoreRS(rs,"regola"));
						anomalia.setData_caricamento(tornaValoreRS(rs,"data_caricamento1"));
						anomalia.setCodent(tornaValoreRS(rs,"codent"));
						anomalia.setCod_fisc(tornaValoreRS(rs,"cod_fisc"));
						anomalia.setPart_iva(tornaValoreRS(rs,"part_iva"));
						anomalia.setTipo_persona(tornaValoreRS(rs,"tipo_persona"));
						anomalia.setDenominazione(tornaValoreRS(rs,"denominazione"));
						anomalia.setCognome(tornaValoreRS(rs,"cognome"));
						anomalia.setNome(tornaValoreRS(rs,"nome"));
						anomalia.setSesso(tornaValoreRS(rs,"SESSO"));
						anomalia.setNasdata(tornaValoreRS(rs,"nasdata1"));
						anomalia.setNasluogo(tornaValoreRS(rs,"nasluogo"));
						anomalia.setNascodcom(tornaValoreRS(rs,"nascodcom"));
						anomalia.setFk_db(tornaValoreRS(rs,"fk_db"));
						anomalia.setFk_chiave(tornaValoreRS(rs,"fk_chiave"));
						anomalia.setDescrizione(tornaValoreRS(rs,"descrizione"));
						anomalia.setDescrDB(tornaValoreRS(rs,"DESCDB"));

						vct.add(anomalia);
					}
				} else {
					if (rs.next()) {
						conteggio = rs.getString("conteggio");
					}
				}
			}
			conn.close();
			ht.put("LISTA_SOGG_ANOM", vct);
			finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			// pagine totali
			finder.setPagineTotali(1 + new Double(Math.ceil((new Long(conteggio).longValue() - 1)
							/ RIGHE_PER_PAGINA)).longValue());
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
		} catch (Exception e) {
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

}
