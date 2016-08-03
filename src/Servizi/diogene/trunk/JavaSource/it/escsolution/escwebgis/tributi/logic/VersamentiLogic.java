/*
 * Created on 2-lug-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.tributi.logic;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.tributi.bean.Versamenti;
import it.escsolution.escwebgis.tributi.bean.VersamentiFinder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * @author Administrator To change the template for this generated type comment
 *         go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 *         Comments
 */
public class VersamentiLogic extends EscLogic
{

	public VersamentiLogic(EnvUtente eu)
	{
		super(eu);
	}
	
	private final static String SQL_SELECT_LISTA = "select * from (select ROWNUM n,c.* from (select ver.* from  SIT_T_VERSAMENTI ver where 1=? ";

	private final static String SQL_SELECT_LISTA_COUNT_SOGGETTO = "SELECT COUNT (*) AS conta " +
								"FROM sit_t_versamenti ver " +
								"WHERE 1 = ? AND ver.codent = ? AND ver.codfisc = ? AND ver.anno = ?";
	
	private final static String SQL_SELECT_COUNT_LISTA = "select count(*) as conteggio  from SIT_T_VERSAMENTI ver WHERE 1=?";

	public Hashtable mCaricareListaVersamenti(Vector listaPar, VersamentiFinder finder)
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
					sql = SQL_SELECT_COUNT_LISTA;
				else
					sql = SQL_SELECT_LISTA;

				indice = 1;
				this.initialize();
				this.setInt(indice, 1);
				indice++;
				
				//per il filtro sul codice fiscale, se si tratta di partita iva devo estendere il valore a 16 caratteri
				if (listaPar != null && listaPar.size()>0){
				for (int j=0; i< listaPar.size(); j++){
					if (listaPar.get(j) != null){
					EscElementoFiltro elementoFiltro= (EscElementoFiltro)listaPar.get(j);
					if (elementoFiltro.getAttributeName().equals("CODICEFISCALE")){
						String valore= elementoFiltro.getValore();
						if (valore.length()<16){
							valore= this.fillUpSpazioBehind(valore,16);
							elementoFiltro.setValore(valore);
						}
						break;
					}
					}
				}
				}

				if (finder.getKeyStr().equals(""))
					sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
				else{
					String keyStr= finder.getKeyStr();
					if (keyStr.length()<16){
						keyStr= this.fillUpSpazioBehind(keyStr,16);
					}
					
					sql = sql + " and CODFISC = " +keyStr;
				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale() - 1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
				// sql = sql + " order by DATA_VERS ";
				if (i == 1)
				{
					sql = sql + " order by TO_DATE(data_vers,'dd/mm/yyyy') ";
					sql = sql + ") c) where n > " + limInf + " and n <=" + limSup;

				}
				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				if (i == 1)
				{
					while (rs.next())
					{
						// campi della lista
						Versamenti ver = new Versamenti();
						ver.setAnno(tornaValoreRS(rs, "anno"));
						ver.setData_vers(tornaValoreRS(rs, "data_vers"));
						ver.setImp_terreni_agri(tornaValoreRS(rs, "imp_terreni_agri","EURO"));
						ver.setImp_aree_fabbr(tornaValoreRS(rs, "imp_aree_fabbr","EURO"));
						ver.setImp_prima_casa(tornaValoreRS(rs, "imp_prima_casa","EURO"));
						ver.setImp_fabbr(tornaValoreRS(rs, "imp_fabbr","EURO"));
						ver.setImp_detraz(tornaValoreRS(rs, "imp_detraz","EURO"));
						ver.setImp_totale(tornaValoreRS(rs, "imp_totale","EURO"));
						ver.setTipo_paga(tornaValoreRS(rs, "tipo_paga"));
						ver.setCodfisc(tornaValoreRS(rs, "codfisc"));
						ver.setProvenienza(tornaValoreRS(rs, "provenienza"));
						ver.setPk_id_versamenti(tornaValoreRS(rs, "Pk_id_versamenti"));
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
	
	public Vector mCaricareListaVersamentiPopup(Vector listaPar, VersamentiFinder finder)
		throws Exception
	{
	// carico la lista e la metto in un hashtable
	Vector vct = new Vector();
	sql = "";
	Connection conn = null;
	
	// faccio la connessione al db
	try
	{
		conn = this.getConnection();
		int indice = 1;
		java.sql.ResultSet rs = null;
	
		sql = SQL_SELECT_LISTA;
		sql = sql + " and ver.CODENT = ?";

		indice = 1;
		this.initialize();
		
		this.setInt(indice, 1);
		indice++;
		String codEnte = "";
		ResultSet rsEnte = conn.prepareStatement("select codent from sit_ente").executeQuery();
		while (rsEnte.next()) {
			codEnte = rsEnte.getString("codent");
		}
		this.setString(indice, codEnte);
		indice++;	
		
		//per il filtro sul codice fiscale, se si tratta di partita iva devo estendere il valore a 16 caratteri
		for (int i=0; i< listaPar.size(); i++){
			if (listaPar.get(i) != null){
			EscElementoFiltro elementoFiltro= (EscElementoFiltro)listaPar.get(i);
			if (elementoFiltro.getAttributeName().equals("CODICEFISCALE")){
				String valore= elementoFiltro.getValore();
				if (valore.length()<16){
					valore= this.fillUpSpazioBehind(valore,16);
					elementoFiltro.setValore(valore);
				}
				break;
			}
			}
		
		}
		sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
		sql = sql + " order by TO_DATE(data_vers,'dd/mm/yyyy') ";
		sql = sql + ") c)";

		prepareStatement(sql);
		rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

		while (rs.next())
		{
			// campi della lista
			Versamenti ver = new Versamenti();
			ver.setAnno(tornaValoreRS(rs, "anno"));
			ver.setData_vers(tornaValoreRS(rs, "data_vers"));
			ver.setImp_terreni_agri(tornaValoreRS(rs, "imp_terreni_agri","EURO"));
			ver.setImp_aree_fabbr(tornaValoreRS(rs, "imp_aree_fabbr","EURO"));
			ver.setImp_prima_casa(tornaValoreRS(rs, "imp_prima_casa","EURO"));
			ver.setImp_fabbr(tornaValoreRS(rs, "imp_fabbr","EURO"));
			ver.setImp_detraz(tornaValoreRS(rs, "imp_detraz","EURO"));
			ver.setImp_totale(tornaValoreRS(rs, "imp_totale","EURO"));
			ver.setTipo_paga(tornaValoreRS(rs, "tipo_paga"));
			ver.setCodfisc(tornaValoreRS(rs, "codfisc"));
			ver.setProvenienza(tornaValoreRS(rs, "provenienza"));
			ver.setPk_id_versamenti(tornaValoreRS(rs, "Pk_id_versamenti"));
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
			/*PreparedStatement pstmtVersamentiICI = conn.prepareStatement("SELECT COUNT (*) AS CONTA FROM (" + 
																	SQL_SELECT_LISTA +
																	" and ver.CODENT = ?" + 
																	" and ver.CODFISC = ?" + 
																	" and ver.ANNO = ?" + 
																	" ) c)" +
																	")");*/
			PreparedStatement pstmtVersamentiICI = conn.prepareStatement(SQL_SELECT_LISTA_COUNT_SOGGETTO);
			int indice = 1;			
			pstmtVersamentiICI.setInt(indice, 1);
			indice++;
			String codEnte = "";
			ResultSet rsEnte = conn.prepareStatement("select codent from sit_ente").executeQuery();
			while (rsEnte.next()) {
				codEnte = rsEnte.getString("codent");
			}
			pstmtVersamentiICI.setString(indice, codEnte);
			indice++;
			
			//se si tratta di una partita iva devo mettere gli spazi in fondo per arrivare a 16
			if (codFiscaleDic!= null && codFiscaleDic.length()<16)
				codFiscaleDic= this.fillUpSpazioBehind(codFiscaleDic, 16);
			
			pstmtVersamentiICI.setString(indice, codFiscaleDic);
			indice++;
			pstmtVersamentiICI.setInt(indice, annoImposta);
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
	
	private  String fillUpSpazioBehind(String s, int l){
		String str = "";
		if (s != null && s.length() < l){
			int dif = l - s.length();
			for (int i=0; i<dif; i++){
				str = str + " ";
			}
			s = s + str;
		}
		return s;
	}//-------------------------------------------------------------------------

}
