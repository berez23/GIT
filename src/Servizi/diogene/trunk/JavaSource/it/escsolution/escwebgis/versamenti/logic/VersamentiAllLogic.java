package it.escsolution.escwebgis.versamenti.logic;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.versamenti.bean.VersComboObject;
import it.escsolution.escwebgis.versamenti.bean.VersFinder;
import it.escsolution.escwebgis.versamenti.iciDM.logic.VersIciDmLogic;
import it.webred.ct.data.access.basic.versamenti.iciDM.VersIciDmService;
import it.webred.ct.data.access.basic.versamenti.iciDM.dto.IciDmDTO;
import it.webred.ct.data.access.basic.versamenti.iciDM.dto.IciDmDataIn;
import it.webred.ct.data.access.basic.versamenti.iciDM.dto.VersamentoIciDmDTO;
import it.webred.ct.data.access.basic.versamenti.iciDM.dto.ViolazioneIciDmDTO;

import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

public class VersamentiAllLogic extends EscLogic {
	
	private static final Logger log = Logger.getLogger(VersamentiAllLogic.class.getName());
	
	public final static String FINDER = "FINDER131";

	public final static String LISTA_VERSAMENTI_ALL= "LISTA_VERSAMENTI@VersamentiAllLogic";
	
	private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
	private static final SimpleDateFormat SDF_ANNO = new SimpleDateFormat("yyyy");
	
	private String appoggioDataSource;
	
	private static final DecimalFormat DF = new DecimalFormat("0.00");
	
	private final static String SQL_INTERNA = 
			"select  F24.CF AS CF_VERSANTE from sit_t_f24_versamenti f24 "+
			"union all "+
			"select  F24.CF AS CF_VERSANTE from sit_t_f24_annullamento f24 "+
			"union all " +
			"select v.CF_VERSANTE from sit_t_ici_dm_vers v " +
			"union all " +
			"select v.CF_VERSANTE from sit_t_ici_dm_violazione v " +
			"union all " +
			"select  S.COD_FISC AS CF_VERSANTE from sit_t_ici_versamenti v, sit_t_ici_sogg s where S.ID_EXT=V.ID_EXT_SOGG_ICI "+
			"union all "+
			"select distinct R.CODFISC AS CF_VERSANTE from SIT_T_TAR_BP_VERS bp, sit_ruolo_tarsu_rata rata, sit_ruolo_tarsu r " +
					"where bp.num_bollettino=rata.v_campo and rata.id_ext_ruolo=r.id_ext";
	
	private final static String SQL_SELECT_LISTA = "select * from (" +
	"select ROWNUM as N, Q.* from (" +
	  "select distinct CF_VERSANTE AS ID, t.* from ( "+SQL_INTERNA+") T  "+
		"where 1=? ";
	
	private final static String SQL_SELECT_COUNT_LISTA = "select count(*) as conteggio FROM ( " +
	  "select distinct CF_VERSANTE AS ID, t.* from ( "+SQL_INTERNA+") T "+
		"where 1=? ";
	

	public VersamentiAllLogic(EnvUtente eu) {
		super(eu);
		appoggioDataSource=eu.getDataSource();
	}
	
	
	public Hashtable mCaricareLista(Vector listaPar, VersFinder finder) throws Exception {

		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
	    sql = "";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;
		
		try {
			conn = this.getConnection();
			int indice = 1;
			java.sql.ResultSet rs = null;
			
			for (int i=0;i<=1;i++){
				//il primo ciclo faccio la count
				if (i==0)
					sql = SQL_SELECT_COUNT_LISTA;
				else
					sql = SQL_SELECT_LISTA;

				indice = 1;
				this.initialize();
				this.setInt(indice,1);
				indice ++;
				
				if (finder.getKeyStr().equals("")){
					sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
				}
				else{
					sql = sql + " and T.ID in (" +finder.getKeyStr() + ")" ;
				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;

				if (i == 1) sql = sql + " order by T.CF_VERSANTE) Q) where N > " + limInf + " and N <=" + limSup;
				else sql += ")";
				
				log.debug("SQL LISTA " + sql);
				
				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				if (i ==1) {
					while (rs.next()){
						vct.add(rs.getString("ID"));					
					}
				}
				else{
					if (rs.next()){
						conteggio = rs.getString("conteggio");
					}
				}
			}
			
			ht.put(this.LISTA_VERSAMENTI_ALL, vct);
			finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			// pagine totali
			finder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
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
