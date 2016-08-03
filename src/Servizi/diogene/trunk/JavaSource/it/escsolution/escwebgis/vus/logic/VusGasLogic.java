/*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.vus.logic;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.vus.bean.VusGas;
import it.escsolution.escwebgis.vus.bean.VusGasCatasto;
import it.escsolution.escwebgis.vus.bean.VusGasFinder;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

/**
 * @author Administrator To change the template for this generated type comment
 *         go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 *         Comments
 */
public class VusGasLogic extends EscLogic
{
	private String appoggioDataSource;

	public VusGasLogic(EnvUtente eu)
	{
		super(eu);
		appoggioDataSource = eu.getDataSource();
	}

	public static final String FINDER = "FINDER";
	public final static String LISTA_VUS_GAS = "LISTA_VUS_GAS@VusGasLogic";
	public final static String VUS_GAS = "VUS_GAS@VusGasLogic";
	public final static String VUS_GAS_CATA = "VUS_GAS_CATA@VusGasLogic";

	private final static String SQL_SELECT_LISTA =
								"SELECT * FROM ( " +
								"SELECT ROWNUM AS N, COD_SERVIZIO,RAGIONE_SOCIALE,COD_FISCALE,PART_IVA FROM( " +
								"SELECT DISTINCT COD_SERVIZIO,RAGIONE_SOCIALE,COD_FISCALE,PART_IVA " +
								"FROM vus_utenze_gas " +
								"WHERE 1= ? ";

	private final static String SQL_SELECT_COUNT_LISTA =
								"SELECT COUNT(*) conteggio FROM vus_utenze_gas WHERE 1=? ";

	public Hashtable mCaricareLista(Vector listaPar, VusGasFinder finder)
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
			java.sql.ResultSet rs;

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

				// il primo passaggio esegue la select count
				if (finder.getKeyStr().equals(""))
				{
					sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
				}
				else
				{
					// controllo provenienza chiamata
					String controllo = finder.getKeyStr();
					 if(i == 0 )
						sql = sql + " AND COD_SERVIZIO in (" + finder.getKeyStr() + ")";
					if (i == 1)
						sql = sql + " AND COD_SERVIZIO in (" + finder.getKeyStr() + ")))";
				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale() - 1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
				if (i == 1 && finder.getKeyStr().equals(""))
					sql = sql + " order by RAGIONE_SOCIALE )) where N > " + limInf + " and N <=" + limSup;

				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				if (i == 1)
				{
					while (rs.next())
					{
						// campi della lista
						VusGas vus = new VusGas();
						vus.setCodSer(rs.getString("COD_SERVIZIO"));
						vus.setRagiSoc(rs.getString("RAGIONE_SOCIALE"));
						vus.setCodiFisc(rs.getString("COD_FISCALE"));
						vus.setPartitaIva(rs.getString("PART_IVA"));
						vct.add(vus);
					}
				}
				else
				{
					if (rs.next())
						conteggio = rs.getString("conteggio");
				}
			}
			ht.put(LISTA_VUS_GAS, vct);
			finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			// pagine totali
			finder.setPagineTotali(1 + new Double(Math.ceil((new Long(conteggio).longValue() - 1) / RIGHE_PER_PAGINA)).longValue());
			finder.setTotaleRecord(conteggione);
			finder.setRighePerPagina(RIGHE_PER_PAGINA);

			ht.put(FINDER, finder);
			
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

	public Hashtable mCaricareDettaglio(String chiave) throws Exception
	{

		Hashtable ht = new Hashtable();
		// faccio la connessione al db
		Connection conn = null;
		try
		{
			conn = this.getConnection();
			this.initialize();
			String sql ="select  distinct ute.cod_servizio,ute.COD_FISCALE,ute.PART_IVA, " +
						"ute.rag_soc_ubicazione,ute.via_ubicazione,ute.civico_ubicazione,ute.cap_ubicazione,ute.comune_ubicazione," +
						"ute.consumo_medio,ute.codice_categoria ,cate.DESCRIZIONE," +
						"ute.RAGIONE_SOCIALE,ute.via_residenza,ute.civico_residenza,ute.CAP_RESIDENZA,ute.COMUNE_RESIDENZA,ute.PR_RESIDENZA," +
						"cata.foglio,cata.particella,cata.subalterno,cata.tipo_catasto " +
						"from vus_utenze_gas ute, vus_categorie_gas cate,vus_catasto_gas cata " +
						"where ute.cod_servizio= ? " +
						"and TO_CHAR(ute.codice_categoria) = cate.CODICE_CATEGORIA(+) " +
						"and  ute.cod_servizio = cata.COD_SERVIZIO (+)";

			int indice = 1;
			this.setString(indice, chiave);
			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			VusGas vus = new VusGas();
			if (rs.next())
			{
				vus.setCodSer(rs.getString("cod_servizio"));
				vus.setCodiFisc(rs.getString("COD_FISCALE"));
				vus.setPartitaIva(rs.getString("PART_IVA"));
				vus.setRagSocUbi(rs.getString("rag_soc_ubicazione"));
				vus.setViaUbi(rs.getString("via_ubicazione"));
				vus.setCivicoUbi(rs.getString("civico_ubicazione"));
				vus.setCapUbi(rs.getString("cap_ubicazione"));
				vus.setComuneUbi(rs.getString("comune_ubicazione"));
				vus.setConsumoMedio(rs.getString("consumo_medio"));
				vus.setCodCategoria(rs.getString("codice_categoria"));
				vus.setDescCategoria(rs.getString("DESCRIZIONE"));
				vus.setRagiSoc(rs.getString("RAGIONE_SOCIALE"));
				vus.setViaResi(rs.getString("via_residenza"));
				vus.setCivicoResi(rs.getString("civico_residenza"));
				vus.setCapResi(rs.getString("CAP_RESIDENZA"));
				vus.setComuneResi(rs.getString("COMUNE_RESIDENZA"));
				vus.setPrResi(rs.getString("PR_RESIDENZA"));
			}
			
			this.initialize();
			indice = 1;
			this.setString(indice, chiave);
			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			ArrayList appoCata = new ArrayList();
			
			while (rs.next())
			{
				VusGasCatasto gasCata = new VusGasCatasto();
				gasCata.setCodServizio(rs.getString("cod_servizio"));
				gasCata.setFoglio(rs.getString("foglio"));
				gasCata.setParticella(rs.getString("particella"));
				gasCata.setSubalterno(rs.getString("subalterno"));
				appoCata.add(gasCata);
				
			}
			vus.setDatiCatastali(appoCata);
			
			ht.put(VUS_GAS, vus);

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
			if (conn != null)
				if (!conn.isClosed())
					conn.close();
		}
	}

	public Hashtable mCaricareDettaglioCata(String chiave) throws Exception
	{

		Hashtable ht = new Hashtable();
		// faccio la connessione al db
		Connection conn = null;
		ArrayList listParam = new ArrayList();
		while (chiave.indexOf('|') > 0) {
			listParam.add(chiave.substring(0,chiave.indexOf('|')));
			chiave = chiave.substring(chiave.indexOf('|')+1);
		}
		listParam.add(chiave);
		
		try
		{
			conn = this.getConnection();
			this.initialize();
			String sql ="SELECT cod_servizio, cod_anagrafico, " +
						"ragione_sociale,cod_com_ammin,des_com_ammim, " +
						"ubic_fornit_des_loc,ubic_fornit_des_via,ubic_fornit_civico, " +
						"cod_com_catastale,comune_catastale,sottotipologia_stato, " +
						"dati_ines_incomp,tipo_catasto,sezione_urbana, " +
						"foglio, particella, subalterno, " +
						"particella_sistema_tavolare,tipo_particella " +
						"from vus_catasto_gas " +
						"where COD_SERVIZIO = ? " +
						"and FOGLIO = ? " +
						"and PARTICELLA = ? " +
						"and SUBALTERNO = ? ";

			int indice = 1;
			this.setString(indice,(String)listParam.get(indice-1));
			this.setString(++indice,(String)listParam.get(indice-1));
			this.setString(++indice,(String)listParam.get(indice-1));
			this.setString(++indice,(String)listParam.get(indice-1));
			
			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			VusGasCatasto vus = new VusGasCatasto();
			if (rs.next())
			{
				vus.setCodServizio(rs.getString("cod_servizio"));
				vus.setFoglio(rs.getString("foglio"));
				vus.setParticella(rs.getString("particella"));
				vus.setSubalterno(rs.getString("subalterno"));
				vus.setTipoCatasto(rs.getString("tipo_catasto"));
				vus.setCodAnagrafico(rs.getString("cod_anagrafico"));
				vus.setUbicFornitDesLoc(rs.getString("ubic_fornit_des_loc"));
				vus.setUbicFornitDesVia(rs.getString("ubic_fornit_des_via"));
				String civico = rs.getString("ubic_fornit_civico");
				if (civico.indexOf("/, //") != -1)
					civico = civico.substring(0,civico.indexOf("/"));
				else if (civico.indexOf(", //") != -1)
					civico = civico.substring(0,civico.indexOf(","));
				
				vus.setUbicFornitCivico(civico);
				vus.setDatiInesIncomp(rs.getString("dati_ines_incomp"));
				vus.setRagSociale(rs.getString("ragione_sociale"));
				vus.setCodComAmmin(rs.getString("cod_com_ammin"));
				vus.setDesComAmmin(rs.getString("des_com_ammim"));
				vus.setCodComuneCatastale(rs.getString("cod_com_catastale"));
				vus.setComuneCatastale(rs.getString("comune_catastale"));
				vus.setSottoTipoStato(rs.getString("sottotipologia_stato"));
				vus.setSezioneUrbana(rs.getString("sezione_urbana"));
				vus.setParticellaSistTavolare(rs.getString("particella_sistema_tavolare"));
				vus.setTipoParticella(rs.getString("tipo_particella"));
			
			}
			
			ht.put(VUS_GAS_CATA, vus);
			
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
			if (conn != null)
				if (!conn.isClosed())
					conn.close();
		}
	}

}
