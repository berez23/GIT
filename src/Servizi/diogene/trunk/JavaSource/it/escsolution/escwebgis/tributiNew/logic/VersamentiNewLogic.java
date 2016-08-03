package it.escsolution.escwebgis.tributiNew.logic;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.tributiNew.bean.VersamentiNew;
import it.escsolution.escwebgis.tributiNew.bean.VersamentiNewFinder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * @author Administrator To change the template for this generated type comment
 *         go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 *         Comments
 */
public class VersamentiNewLogic extends EscLogic
{

	public VersamentiNewLogic(EnvUtente eu)
	{
		super(eu);
	}
	
	public final static String VERSAMENTI = "VERSAMENTI";
	
	private final static String DATI_LISTA_VERSAMENTO = "ver.id, " +
														"ver.id_ext, ver.yea_rif, " +
														"ver.dat_pag, ver.imp_pag_eu, " +
														"ver.IMP_TER_AGR_EU,ver.IMP_ARE_FAB_EU,ver.IMP_ABI_PRI_EU,ver.IMP_ALT_FAB_EU,IMP_DTR_EU,TIP_PAG,ver.PROVENIENZA ";
	
	private final static String SQL_SELECT_LISTA = "select * from " +
								"(select ROWNUM n, c.* " +
								"from (select * from " +
								"(select ver.cod_fisc,  " +DATI_LISTA_VERSAMENTO+
								"from SIT_T_ICI_VERSAMENTI ver, SIT_T_ICI_SOGG sogg " +
								"where ver.ID_EXT_SOGG_ICI = sogg.ID_EXT(+) " +
								"@@@ver@@@ " +
								"union " +
								"select sogg.cod_fisc, " +DATI_LISTA_VERSAMENTO+
								"from SIT_T_ICI_VERSAMENTI ver, SIT_T_ICI_SOGG sogg " +
								"where ver.ID_EXT_SOGG_ICI = sogg.ID_EXT(+) " +
								"@@@sogg@@@";

	private final static String SQL_SELECT_LISTA_COUNT_SOGGETTO = "SELECT COUNT (*) AS conta from " +
								"(SELECT ver.cod_fisc,  " +DATI_LISTA_VERSAMENTO+
								"FROM SIT_T_ICI_VERSAMENTI ver, SIT_T_ICI_SOGG sogg " +
								"WHERE ver.ID_EXT_SOGG_ICI = sogg.ID_EXT(+) " +
								"AND ver.COD_FISC = ? " +
								"AND ver.YEA_RIF = ? " +
								"UNION " +
								"SELECT sogg.cod_fisc, " +DATI_LISTA_VERSAMENTO+
								"FROM SIT_T_ICI_VERSAMENTI ver, SIT_T_ICI_SOGG sogg " +
								"WHERE ver.ID_EXT_SOGG_ICI = sogg.ID_EXT(+) " +
								"AND sogg.COD_FISC = ? " +
								"AND ver.YEA_RIF = ?)";
	
	private final static String SQL_SELECT_COUNT_LISTA = "select count(*) as conteggio from " +
								"(select ROWNUM n, c.* " +
								"from " +
								"(select ver.* " +
								"from SIT_T_ICI_VERSAMENTI ver, SIT_T_ICI_SOGG sogg " +
								"where ver.ID_EXT_SOGG_ICI = sogg.ID_EXT(+) " +
								"@@@ver@@@ " +
								"union " +
								"select ver.* " +
								"from SIT_T_ICI_VERSAMENTI ver, SIT_T_ICI_SOGG sogg " +
								"where ver.ID_EXT_SOGG_ICI = sogg.ID_EXT(+) " +
								"@@@sogg@@@";
	
	private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
	
	public static final DecimalFormat DFEURO = new DecimalFormat();
	static {
		DFEURO.setGroupingUsed(true);
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator(',');
		dfs.setGroupingSeparator('.');
		DFEURO.setDecimalFormatSymbols(dfs);
		DFEURO.setMinimumFractionDigits(2);
	}

	
	public Hashtable mCaricareListaVersamenti(Vector listaPar, VersamentiNewFinder finder) throws Exception {
		
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
				
				//gestione particolare con un unico parametro (codice fiscale) e lettura "manuale" di listaPar
				String codFisc = null;
				if (finder.getKeyStr().equals("")) {
					if (listaPar != null && listaPar.size() > 0) {
						EscElementoFiltro el = (EscElementoFiltro)listaPar.get(0);
						if (el.getValore() != null && !el.getValore().trim().equals("")) {
							codFisc = el.getValore();
						}
					}
				}
				else {
					codFisc = finder.getKeyStr();
					//tolgo gli apici perché non faccio IN ma, anche qui, =
					if (codFisc.substring(0, 1).equals("\'") && codFisc.substring(codFisc.length() - 1, codFisc.length()).equals("\'")) {
						codFisc = codFisc.substring(1, codFisc.length() - 1);
					}					
				}
				if (codFisc != null) {
					sql = sql.replace("@@@ver@@@", " AND ver.COD_FISC = ? ");
					sql = sql.replace("@@@sogg@@@", " AND sogg.COD_FISC = ? ");
					this.setString(indice, codFisc);
					indice++;
					this.setString(indice, codFisc);
				} else {
					sql = sql.replace("@@@ver@@@", " AND 1 = ? ");
					sql = sql.replace("@@@sogg@@@", "");
					this.setInt(indice, 1); //parametro fittizio
				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale() - 1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;

				if (i == 1) {
					sql = sql + ") order by DAT_PAG";
					sql = sql + ") c) where n > " + limInf + " and n <=" + limSup;					
				} else {
					sql = sql + ") c)";
				}
				prepareStatement(sql);
				log.debug("Lista Versamenti SQL["+sql+"]");
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				if (i == 1)
				{
					while (rs.next())
					{
						// campi della lista
						VersamentiNew ver = new VersamentiNew();

						ver.setChiave(rs.getString("ID"));
						ver.setIdExt(rs.getString("ID_EXT"));
						ver.setCodFisc(rs.getObject("COD_FISC") == null ? "-" : rs.getString("COD_FISC"));
						ver.setYeaRif(rs.getObject("YEA_RIF") == null ? "-" : rs.getString("YEA_RIF"));
						ver.setDatPag(rs.getObject("DAT_PAG") == null ? "-" : SDF.format(rs.getTimestamp("DAT_PAG")));
						ver.setImpPagEu(rs.getObject("IMP_PAG_EU") == null ? "-" : DFEURO.format(rs.getDouble("IMP_PAG_EU")));	
						ver.setImpTerAgrEu(rs.getObject("IMP_TER_AGR_EU") == null ? "-" : DFEURO.format(rs.getDouble("IMP_TER_AGR_EU")));	
						ver.setImpAreFabEu(rs.getObject("IMP_ARE_FAB_EU") == null ? "-" : DFEURO.format(rs.getDouble("IMP_ARE_FAB_EU")));	
						ver.setImpAbiPriEu(rs.getObject("IMP_ABI_PRI_EU") == null ? "-" : DFEURO.format(rs.getDouble("IMP_ABI_PRI_EU")));
						ver.setImpAltFabEu(rs.getObject("IMP_ALT_FAB_EU") == null ? "-" : DFEURO.format(rs.getDouble("IMP_ALT_FAB_EU")));
						ver.setImpDtrEu(rs.getObject("IMP_DTR_EU") == null ? "-" : DFEURO.format(rs.getDouble("IMP_DTR_EU")));
						ver.setTipPag(rs.getObject("TIP_PAG") == null ? "-" : rs.getString("TIP_PAG"));
						ver.setProvenienza(rs.getObject("PROVENIENZA") == null ? "-" : rs.getString("PROVENIENZA"));
						
						vct.add(ver);
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
			ht.put("LISTA_VERSAMENTI", vct);
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
	
	public Vector<VersamentiNew> mCaricareListaVersamentiPopup(Vector listaPar)
	throws Exception {
		// carico la lista e la metto in un hashtable
		Vector vct = new Vector();
		sql = "";
		Connection conn = null;

		try
		{
			conn = this.getConnection();			
			java.sql.ResultSet rs = null;
		
			int indice = 1;
			this.initialize();
			
			sql = SQL_SELECT_LISTA;

			//gestione particolare con due parametri (codice fiscale e anno) e lettura "manuale" di listaPar
			String codFisc = null;
			String yeaRif = null;
			if (listaPar != null && listaPar.size() > 0) {
				EscElementoFiltro el = (EscElementoFiltro)listaPar.get(0);
				if (el.getValore() != null && !el.getValore().trim().equals("")) {
					codFisc = el.getValore();
				}
				if (listaPar.size() > 1) {
					el = (EscElementoFiltro)listaPar.get(1);
					if (el.getValore() != null && !el.getValore().trim().equals("")) {
						yeaRif = el.getValore();
					}
				}
			}
			if (codFisc != null) {
				if (yeaRif != null) {
					sql = sql.replace("@@@ver@@@", " AND ver.COD_FISC = ? AND ver.YEA_RIF = ? ");
					sql = sql.replace("@@@sogg@@@", " AND sogg.COD_FISC = ? AND ver.YEA_RIF = ? ");
				} else {
					sql = sql.replace("@@@ver@@@", " AND ver.COD_FISC = ? ");
					sql = sql.replace("@@@sogg@@@", " AND sogg.COD_FISC = ? ");
				}				
				this.setString(indice, codFisc);
				if (yeaRif != null) {
					indice++;
					this.setString(indice, yeaRif);
				}
				indice++;
				this.setString(indice, codFisc);
				if (yeaRif != null) {
					indice++;
					this.setString(indice, yeaRif);
				}
			} else {
				sql = sql.replace("@@@ver@@@", " AND 1 = ? ");
				sql = sql.replace("@@@sogg@@@", "");
				this.setInt(indice, 1); //parametro fittizio
			}
			
			sql = sql + ") order by DAT_PAG";
			sql = sql + ") c)";			
			
			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
		
			while (rs.next())
			{
				VersamentiNew ver = new VersamentiNew();

				ver.setChiave(rs.getString("ID"));
				ver.setIdExt(rs.getString("ID_EXT"));
				ver.setCodFisc(rs.getObject("COD_FISC") == null ? "-" : rs.getString("COD_FISC"));
				ver.setYeaRif(rs.getObject("YEA_RIF") == null ? "-" : rs.getString("YEA_RIF"));
				ver.setDatPag(rs.getObject("DAT_PAG") == null ? "-" : SDF.format(rs.getTimestamp("DAT_PAG")));
				ver.setImpPagEu(rs.getObject("IMP_PAG_EU") == null ? "-" : DFEURO.format(rs.getDouble("IMP_PAG_EU")));	
				ver.setImpTerAgrEu(rs.getObject("IMP_TER_AGR_EU") == null ? "-" : DFEURO.format(rs.getDouble("IMP_TER_AGR_EU")));	
				ver.setImpAreFabEu(rs.getObject("IMP_ARE_FAB_EU") == null ? "-" : DFEURO.format(rs.getDouble("IMP_ARE_FAB_EU")));	
				ver.setImpAbiPriEu(rs.getObject("IMP_ABI_PRI_EU") == null ? "-" : DFEURO.format(rs.getDouble("IMP_ABI_PRI_EU")));
				ver.setImpAltFabEu(rs.getObject("IMP_ALT_FAB_EU") == null ? "-" : DFEURO.format(rs.getDouble("IMP_ALT_FAB_EU")));
				ver.setImpDtrEu(rs.getObject("IMP_DTR_EU") == null ? "-" : DFEURO.format(rs.getDouble("IMP_DTR_EU")));
				ver.setTipPag(rs.getObject("TIP_PAG") == null ? "-" : rs.getString("TIP_PAG"));
				ver.setProvenienza(rs.getObject("PROVENIENZA") == null ? "-" : rs.getString("PROVENIENZA"));
				
				vct.add(ver);
			}
			
			return vct;
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
	
	public int getCountListaSoggetto(String codFiscaleDic, int annoImposta) throws Exception {
		
		Connection conn = null;
		int numVersamentiICI = 0;
		try
		{
			conn = this.getConnection();
			PreparedStatement pstmtVersamentiICI = conn.prepareStatement(SQL_SELECT_LISTA_COUNT_SOGGETTO);
			int indice = 1;
			pstmtVersamentiICI.setString(indice, codFiscaleDic);
			indice++;
			pstmtVersamentiICI.setString(indice, "" + annoImposta);
			indice++;
			pstmtVersamentiICI.setString(indice, codFiscaleDic);
			indice++;
			pstmtVersamentiICI.setString(indice, "" + annoImposta);
			ResultSet rsVersamentiICI = pstmtVersamentiICI.executeQuery();				
			while (rsVersamentiICI.next()) {
				numVersamentiICI = rsVersamentiICI.getInt("CONTA");
			}
			return numVersamentiICI;
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
