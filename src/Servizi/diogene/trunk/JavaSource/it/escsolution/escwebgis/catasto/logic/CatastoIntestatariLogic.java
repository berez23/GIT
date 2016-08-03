/*
 * Created on 6-dic-2005
 */
package it.escsolution.escwebgis.catasto.logic;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.soggetto.logic.SoggettoFascicoloLogic;

import java.sql.Connection;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Giulio
 */
public class CatastoIntestatariLogic extends EscLogic
{

	public CatastoIntestatariLogic(EnvUtente eu) {super(eu);}

	/**
	 * Restituisce l'UC relatico al tipo di persona, così come specificato in
	 * @link it.escsolution.escwebgis.common.Tema
	 *
	 * @param pkCuaa
	 * Il PK_CUAA del soggetto
	 *
	 * @return
	 * L'UC della del Catasto Intestatari Persone Fisiche se il soggetto  una persona fisica
	 * L'UC della del Catasto Intestatari Persone Giuridiche se il soggetto  una persona giuridica
	 *
	 * @throws Exception
	 * In caso di errori SQL
	 */
	public int restituisciUCperPkCuaa(String pkCuaa) throws Exception
	{
		Connection conn = null;
		Statement st = null;
		try {
			conn = this.getConnection();
			sql =
				"SELECT flag_pers_fisica " +
				"  FROM cons_sogg_tab " +
				" WHERE pk_cuaa in (@) ";

			/*sql = "SELECT cons_sogg_tab.flag_pers_fisica  ";
			String from = "FROM cons_sogg_tab ";
			String where = "WHERE  cons_sogg_tab.PK_CUAA in (@) ";
			*/

			// CERCA ALL'INIZIO DI pkCuaa LA STRINGA "SOGGFASC" E, SE LA
			// TROVA, ESTRAE IL CODENT E RIPULISCE pkCuaa
			Matcher lookForSoggFascicolo = Pattern.compile("(" + SoggettoFascicoloLogic.COST_PROCEDURA + ")(.*?)\\|(.*)").matcher(pkCuaa);
			if (lookForSoggFascicolo.find())
			{
				String ente = lookForSoggFascicolo.group(2);
				pkCuaa = lookForSoggFascicolo.group(3);
				/*from += ",siticomu ";
				where +="and siticomu.COD_NAZIONALE = '"+ente+"' "+
						"and cons_sogg_tab.DATA_FINE = to_date('99991231', 'yyyymmdd') " +
						"and SUBSTR(cons_sogg_tab.COMU_NASC,1,3) = siticomu.ISTATP (+) " +
						"and SUBSTR(cons_sogg_tab.COMU_NASC,4,3) = siticomu.ISTATC (+)";
				sql += from + where;
				*/
			}

			sql = sql.replaceFirst("@", pkCuaa);
			st = conn.createStatement();
			java.sql.ResultSet rs = st.executeQuery(sql);

			if (rs.next())
			{
				String tipoPersona = rs.getString(1);
				if ("G".equals(tipoPersona.trim().toUpperCase()))
				{
					return 4; // UC PERSONE GIURIDICHE
				}
				else if("P".equals(tipoPersona.trim().toUpperCase()))
				{
					return 3; // UC PERSONE FISICHE
				}
				else
					return -1;
			}
			else
				return -1;
		}
		catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (st != null)
				st.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		}

}

}
