package it.escsolution.escwebgis.f24.logic;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.f24.bean.F24Finder;
import it.escsolution.escwebgis.f24.bean.ProspettoIncassi;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.commons.dbutils.DbUtils;

/**
 * @author Administrator To change the template for this generated type comment
 *         go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 *         Comments
 */
public class ProspettoIncassiLogic extends EscLogic
{

	public ProspettoIncassiLogic(EnvUtente eu)
	{
		super(eu);
	}
	
	public final static String LISTA_INCASSI = "LISTA_INCASSI@ProspettoIncassiLogic";
	public final static String TOT_INCASSI = "TOTALE_INCASSI@ProspettoIncassiLogic";
	
	public final static String FINDER = "FINDER125";
	
	private final static String SQL_SELECT_LISTA = 
			"select * from " +
			"(select ROWNUM n, c.* " +
			"from ( "+
			"select c.descrizione, vv.* from (select tipo_imposta, anno_rif, cod_tributo, (sum(imp_debito) - sum(imp_credito)) sum_versamenti , sum(num_fabb_ici_imu) sum_fabbricati "+
			"from sit_t_f24_versamenti where 1=? @@@cond@@@ "+
			"group by tipo_imposta, anno_rif, cod_tributo) vv left join sit_t_cod_tributo c on c.codice = cod_tributo " +
			"order by tipo_imposta, anno_rif, cod_tributo) c )";
	
	private final static String SQL_SELECT_COUNT_LISTA = 
			"select count(*) as conteggio from " +
			"(select ROWNUM n, c.* " +
			"from ( "+
			"select c.descrizione, vv.* from (select anno_rif, cod_tributo, (sum(imp_debito) - sum(imp_credito)) sum_versamenti , sum(num_fabb_ici_imu) sum_fabbricati "+
			"from sit_t_f24_versamenti where 1=? @@@cond@@@ "+
			"group by anno_rif, cod_tributo) vv left join sit_t_cod_tributo c on c.codice = cod_tributo " +
			"order by anno_rif, cod_tributo) c )";
	
	private final static String SQL_SELECT_TOTALE=
			"select (sum(imp_debito) - sum(imp_credito)) tot_versamenti , sum(num_fabb_ici_imu) tot_fabbricati from sit_t_f24_versamenti where 1=? @@@cond@@@ ";
	
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

	
	public Hashtable mCaricareLista(Vector listaPar, F24Finder finder) throws Exception {
		
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		String totIncassi = "";
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
				this.setInt(indice, 1); //parametro fittizio
				indice ++;
				
				String cond = this.elaboraFiltroMascheraRicercaParziale(indice, listaPar);
				if(cond!=null && cond.length()>0){
					sql = sql.replace("@@@cond@@@", cond);
				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale() - 1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;

				if (i == 1) {
					//sql = sql + " where n > " + limInf + " and n <=" + limSup;					
				} 
				
				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				if (i == 1)
				{
					while (rs.next())
					{
						// campi della lista
						ProspettoIncassi inc = new ProspettoIncassi();
						inc.setTipoImposta(rs.getString("TIPO_IMPOSTA"));
						inc.setAnnoRif(rs.getString("ANNO_RIF"));
						inc.setVersato(DFEURO.format(rs.getDouble("SUM_VERSAMENTI")/100));
						inc.setCodTributo(rs.getString("COD_TRIBUTO"));
						inc.setDescTributo(rs.getString("DESCRIZIONE"));
						inc.setFabbricati(rs.getInt("SUM_FABBRICATI"));

						vct.add(inc);
					}
					
					if(vct.size()>0){
						
						sql = SQL_SELECT_TOTALE;
						
						indice = 1;
						this.initialize();
						this.setInt(indice, 1); //parametro fittizio
						indice ++;
						
						cond = this.elaboraFiltroMascheraRicercaParziale(indice, listaPar);
						if(cond!=null && cond.length()>0){
							sql = sql.replace("@@@cond@@@", cond);
						}else{
							sql = sql.replace("@@@cond@@@", "");
						}
						
						prepareStatement(sql);
						rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
						
						if(rs.next()){
							 totIncassi = DFEURO.format(rs.getDouble("TOT_VERSAMENTI")/100);
						}
						
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
			ht.put(this.LISTA_INCASSI, vct);
			ht.put(this.TOT_INCASSI, totIncassi);
			finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			// pagine totali
			//finder.setPagineTotali(1 + new Double(Math.ceil((new Long(conteggio).longValue() - 1) / RIGHE_PER_PAGINA)).longValue());
			finder.setPagineTotali(1);
			finder.setTotaleRecord(conteggione);
			//finder.setRighePerPagina(RIGHE_PER_PAGINA);
			finder.setRighePerPagina(conteggione);

			ht.put(ProspettoIncassiLogic.FINDER, finder);
			
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
			DbUtils.close(conn);
		}

	}
	
	
	
	
	
	

}
