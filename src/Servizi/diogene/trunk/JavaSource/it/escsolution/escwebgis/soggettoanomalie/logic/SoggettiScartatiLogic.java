package it.escsolution.escwebgis.soggettoanomalie.logic;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.common.EscObject;
import it.escsolution.escwebgis.soggettoanomalie.bean.SoggettiScartati;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 *
 * @author Giulio
 *
 */
@Deprecated
public class SoggettiScartatiLogic extends EscLogic
{
	public SoggettiScartatiLogic(EnvUtente eu) {
		super(eu);
	}

	/**
	 * Restituisce una lista di riferimenti a SIT_SGT_SCARTI e SIT_SOGG_SCARTI
	 * nel caso in cui il presente soggetto sia stato scartato
	 *
	 * @param tableName
	 * Nome della tabella dalla quale verranno estratti i dati. Se <code>null</code>,
	 * viene preso in considerazione il valore di <code>ewgTableName</code>
	 * @param ewgTableName
	 * Nome della tabella relativa alla classe, ricavabile dal metodo
	 * <code>getTabellaPerCrossLink()</code> della servlet
	 * @param dbLookUpDesc
	 * Il nome della classe che corrisponde al campo DESCRIZIONE della
	 * tabella SIT_DATABASE_LOOKUP.
	 * @param fieldsToSelect
	 * Elenco dei campi da estrarre dalla tabella <code>tableName</code>, in
	 * pratica una stringa che deve contenere, separati da virgole:
	 * <ol>
	 * <li>il nome della chiave che deve essere coerente con la FK_CHIAVE specificata
	 * nelle procedure del package MILANO_DATA nel db DBTOTALEMI@RAPANUI
	 * </li><li>
	 * il nome del campo corrispondente al codice fiscale (o 'null' se non c'è tale campo)
	 * </li><li>
	 * il nome del campo corrispondente alla partita iva (o 'null' se non c'è tale campo)
	 * </li></ol>
	 * Questo elenco è ricavabile da
	 * <code>it.escsolution.escwebgis.common.Tema.getFKCFPIScarti(uc)</code>
	 * <code>finder.getKeyStr</code>
	 * @param pkChiaveTabellaOrigine
	 * Il nome, o l'elenco dei nomi separati da virgole, delle chiavi
	 * considerate primarie nella tabella specificata da <code>tableName</code>
	 * @param bean
	 * L'oggetto EscObject che si passa al dettaglio del soggetto
	 * @return
	 */
	public ArrayList getSoggettiScartati(String tableName, String ewgTableName, String dbLookUpDesc, String fieldsToSelect, String pkChiaveTabellaOrigine, EscObject bean, String nomeTema) throws Exception
	{
		if (true)
			return new ArrayList();
		
		if (fieldsToSelect == null || fieldsToSelect.split(",").length != 3)
			throw new Exception("Il campo fieldsToSelect deve contenere esattamente tre nomi di campi separati da virgole, tu hai immesso " + fieldsToSelect + ", consulta il JavaDoc!");
		if (bean == null || bean.getChiave() == null)
			throw new Exception("E' necessario passare un finder con una chiave getKeyStr() non nulla, consulta il JavaDoc!");
		if (ewgTableName == null && dbLookUpDesc == null)
		{
			throw new IllegalArgumentException("Uno e soltanto uno dei ewgTableName campi e nomeClasse pùò essere non nullo!");
		}
		else if (ewgTableName != null && dbLookUpDesc != null)
		{
			throw new IllegalArgumentException("Uno e soltanto uno dei ewgTableName campi e nomeClasse può essere non nullo!");
		}
		if (fieldsToSelect == null)
			throw new NullPointerException("Il campo fieldsToSelect è null!!!");
		if (pkChiaveTabellaOrigine == null)
			throw new NullPointerException("Il campo fkChiaveName è null!!!");

		ArrayList result = new ArrayList();

		if (ewgTableName != null)
			ewgTableName = ewgTableName.toUpperCase().trim();
		else if (tableName == null)
				throw new NullPointerException("Il campo tableName è null, il che è lecito solo in caso di ewgTableName != null");
		if (tableName == null)
			tableName = ewgTableName;

		fieldsToSelect = fieldsToSelect.toUpperCase().trim();
		pkChiaveTabellaOrigine = pkChiaveTabellaOrigine.toUpperCase().trim();

		String[] pkNomiChiaviTabellaOrigine = pkChiaveTabellaOrigine.split(",");
		String[] pkValoriChiaviTabellaOrigine = bean.getChiave().split("@");
		if (pkNomiChiaviTabellaOrigine.length != pkValoriChiaviTabellaOrigine.length)
			throw new Exception("Il numero di chiavi contenuti in pkChiaveTabellaOrigine e dei valori contenuti in bean.getChiave() non coincidono");

		String query = "";
		Connection conn = null;
		ResultSet rs = null;
		try
		{
			conn = getDefaultConnection();

			// RECUPERA LA DESCRIZIONE PER SIT_DATABASE_LOOKUP
			if (dbLookUpDesc == null)
			{
				super.initialize();
				query += "SELECT NOMECLASSE FROM EWG_CLASSE, EWG_TEMA WHERE FK_TEMA = PK_TEMA AND TABLENAME = ? AND NOME = ?";
				super.setString(super.getCurrentParameterIndex(), ewgTableName);
				super.setString(super.getCurrentParameterIndex(), nomeTema);
				super.prepareStatement(query);
				rs = super.executeQuery(conn, this.getClass().getName(), getEnvUtente());
				if (rs.next())
				{
					dbLookUpDesc = rs.getString(1);
					rs.close();
				}
				else
				{
					rs.close();
					return null;
				}
			}
		}	catch (Exception e)
		{
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}

		String
		fkChiave = "",
		codFiscale = "",
		pIva = "";
		try {
			conn = getConnection();
			// ESTRAE FK_CHIAVE, CODICE FISCALE E PARTITA IVA
			super.initialize();
			query = "SELECT " +
					fieldsToSelect +
					" FROM " +
					tableName +
					" WHERE 1 = ?" +
					"";
			super.setInt(super.getCurrentParameterIndex(), 1);
			for (int i = 0; i < (pkNomiChiaviTabellaOrigine.length); i++)
			{
				query += " AND ";
				query += pkNomiChiaviTabellaOrigine[i];
				query += " = ?";
				super.setString(super.getCurrentParameterIndex(), pkValoriChiaviTabellaOrigine[i]);
			}
			super.prepareStatement(query);
			rs = super.executeQuery(conn, this.getClass().getName(), getEnvUtente());
			if (rs.next())
			{
				fkChiave = rs.getString(1);
				codFiscale = rs.getString(2);
				pIva = rs.getString(3);
				rs.close();
			}
			else
			{
				rs.close();
				return null;
			}
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
		try {
			conn = this.getDefaultConnection();
			// POPOLA IL RISULTATO
			super.initialize();
			query = "SELECT 'SIT_SOGG_SCARTI' TIPO_SCARTO, FK_DB, FK_CHIAVE, NULL PK_IDINTERNOSGTSCARTI " +
					"  FROM SIT_SOGG_SCARTI, SIT_DATABASE_LOOKUP " +
					" WHERE FK_CHIAVE = ? " +
					"   AND FK_DB = SIT_DATABASE_LOOKUP.ID_DB " +
					"   AND SIT_DATABASE_LOOKUP.DESCRIZIONE = ? " +
					"UNION " +
					"SELECT 'SIT_SGT_SCARTI' TIPO_SCARTO, NULL FK_DB, NULL FK_CHIAVE, PK_IDINTERNOSGTSCARTI " +
					"  FROM SIT_SGT_SCARTI, SIT_DATABASE_LOOKUP " +
					" WHERE (COD_FISC = ? OR PART_IVA = ?) " +
					"   AND FK_DB = SIT_DATABASE_LOOKUP.ID_DB " +
					"   AND SIT_DATABASE_LOOKUP.DESCRIZIONE = ? " +
					"";
			super.setString(super.getCurrentParameterIndex(), fkChiave);
			super.setString(super.getCurrentParameterIndex(), dbLookUpDesc);
			super.setString(super.getCurrentParameterIndex(), codFiscale);
			super.setString(super.getCurrentParameterIndex(), pIva);
			super.setString(super.getCurrentParameterIndex(), dbLookUpDesc);
			super.prepareStatement(query);
			rs = super.executeQuery(conn, this.getClass().getName(), getEnvUtente());
			while (rs.next())
			{
				SoggettiScartati sogScart = new SoggettiScartati();
				sogScart.setTipoScarto(rs.getString(1));
				sogScart.setFkDb(rs.getString(2));
				sogScart.setDescrDb(dbLookUpDesc);
				sogScart.setFkChiave(rs.getString(3));
				sogScart.setPkIdInternoSgtScarti(rs.getString(4));
				String urlAnomalie = retriveUrlAnomalie(conn, sogScart.getTipoScarto());
				if ("SIT_SGT_SCARTI".equals(sogScart.getTipoScarto()))
				{
					if (!"\0".equals(urlAnomalie))
						urlAnomalie += "&KEYSTR=" + sogScart.getPkIdInternoSgtScarti();
					sogScart.setDetailedMessage("Chiavi di collegamento con FASCICOLO SOGGETTO non presenti. Clicca per maggiori informazioni");
				}
				else if ("SIT_SOGG_SCARTI".equals(sogScart.getTipoScarto()))
				{
					if (!"\0".equals(urlAnomalie))
						urlAnomalie += "&KEYSTR=" + sogScart.getFkChiave() + "@" + sogScart.getFkDb();
					sogScart.setDetailedMessage("Dato " + dbLookUpDesc + " non agganciato al fascicolo soggetto");
				}
				else {}
				if (!"\0".equals(urlAnomalie))
				{
					if ("SIT_SGT_SCARTI".equals(sogScart.getTipoScarto()))
						urlAnomalie += "&KEYSTR=" + sogScart.getPkIdInternoSgtScarti();
					else if ("SIT_SOGG_SCARTI".equals(sogScart.getTipoScarto()))
						urlAnomalie += "&KEYSTR=" + sogScart.getFkChiave() + "@" + sogScart.getFkDb();
				}
				sogScart.setUrlAnomalie(urlAnomalie);
				result.add(sogScart);
			}
			rs.close();
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

		return result;
	}

	/**
	 * Richiama semplicemente l'altro metodo ridefinito, con tableName a <code>null</code>
	 */
	public ArrayList getSoggettiScartati(String ewgTableName, String fieldsToSelect, String dbLookUpDesc, String pkChiaveTabellaOrigine, EscObject bean, String nomeTema) throws Exception
	{
		
		return getSoggettiScartati(null, ewgTableName, dbLookUpDesc, fieldsToSelect, pkChiaveTabellaOrigine, bean, nomeTema);
	}

	private String retriveUrlAnomalie(Connection conn, String tableName) throws SQLException, UnsupportedEncodingException
	{
		String result = "\0";
		ResultSet rs = conn.createStatement().executeQuery("SELECT URLLISTVIEW FROM EWG_CLASSE WHERE TABLENAME = '" + tableName + "'");
		if (rs.next())
			result = rs.getString(1);
		rs.close();
		return URLDecoder.decode(result,"US-ASCII");
	}
}
