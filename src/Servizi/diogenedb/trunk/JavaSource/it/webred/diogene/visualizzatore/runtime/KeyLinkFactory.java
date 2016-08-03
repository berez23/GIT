package it.webred.diogene.visualizzatore.runtime;



import it.webred.diogene.db.DataJdbcConnection;
import it.webred.diogene.db.HibernateSession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * 
 * @author sisani
 * @version $Revision: 1.1 $ $Date: 2007/10/26 13:11:16 $
 */
public class KeyLinkFactory 
{
	private Connection connDWH;
	//private Connection conn;


	private static final Logger log = Logger.getLogger(KeyLinkFactory.class.getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.webred.rulengine.Command#run(it.webred.rulengine.Context)
	 */
	@SuppressWarnings("unchecked")
	public static LinkedHashSet<String> run(long id_link, String entityKey)
	{
		Connection connDWH = null;
		Connection connCatalog = null;
		LinkedHashSet<String> keys = new LinkedHashSet<String>();
		
		PreparedStatement psMetaData = null;
		ResultSet rsMetaData = null;
		PreparedStatement psChiavi = null;
		ResultSet rsChiavi = null;
		PreparedStatement psReale = null;
		ResultSet rsReale = null;
		Statement stSequence = null;
		ResultSet rsSequence = null;
		PreparedStatement psDvKey = null;
		PreparedStatement psDvKeyPair = null;
		PreparedStatement psRic = null;
		ResultSet rsRic = null;
		PreparedStatement psRicPair = null;
		PreparedStatement psED = null;
		ResultSet rsRicPair = null;
		PreparedStatement psDeleteDvKeyPair = null;

		// QUERY METADATA
		String sqlMetaData = "select UE1.FK_DC_ENTITY ID1 ," +
					 " 'E_' || UE1.FK_DC_ENTITY ALIAS1," +
					 " extractValue(ue1.sql_statement,'/sql_statement') SQL1," +
					 " UE2.FK_DC_ENTITY ID2 ," +
					 " 'E_' || UE2.FK_DC_ENTITY ALIAS2," +
					 " extractValue(ue2.sql_statement_bk, '/sql_statement')SQL2," +
					 " extractValue(r.CONDITION, '/condition/sql') RELAZIONE, " +
					 " l.id IDLINK"+
					 " from dv_link l , dc_entity_rel er," +
					 " dc_rel r, dv_user_entity ue1, dv_user_entity ue2" +
					 " where l.FK_DC_ENTITY_REL = er.ID" +
					 " and er.FK_DC_REL = r.ID" +
					 " and er.FK_DC_ENTITY1 = ue1.FK_DC_ENTITY" +
					 " and er.FK_DC_ENTITY2 = ue2.FK_DC_ENTITY";
		String sqlMetaDataLink = "select UE1.FK_DC_ENTITY ID1 ," +
					 " 'E_' || UE1.FK_DC_ENTITY ALIAS1," +
					 " extractValue(ue1.sql_statement_bk,'/sql_statement') SQL1," +
					 " UE2.FK_DC_ENTITY ID2 ," +
					 " 'E_' || UE2.FK_DC_ENTITY ALIAS2," +
					 " extractValue(ue2.sql_statement, '/sql_statement')SQL2," +
					 " extractValue(r.CONDITION, '/condition/sql') RELAZIONE, " +
					 " l.id IDLINK"+
					 " from dv_link l , dc_entity_rel er," +
					 " dc_rel r, dv_user_entity ue1, dv_user_entity ue2" +
					 " where l.FK_DC_ENTITY_REL = er.ID" +
					 " and er.FK_DC_REL = r.ID" +
					 " and er.FK_DC_ENTITY1 = ue1.FK_DC_ENTITY" +
					 " and er.FK_DC_ENTITY2 = ue2.FK_DC_ENTITY" +
					 " and l.id = ?";
		//QUERY CHIAVI
		String sqlChiavi = "SELECT ENTITA, ALIAS_CHIAVE, ALIAS_ENTITY," +
						" TO_NUMBER(POSITION) POSITION FROM (" +
						" SELECT '1' entita, " +
						" 'E_' || col1.fk_dc_entity || '.' || exp1.alias  ALIAS_CHIAVE," +
						" 'E_' || col1.fk_dc_entity ALIAS_ENTITY," +
						" att1.ATTRIBUTE_VAL POSITION" +
						" FROM dc_column col1, dc_attribute att1, dc_expression exp1" +
						" WHERE col1.FK_DC_ENTITY = ?" +
						" and att1.FK_DC_COLUMN = col1.ID" +
						" and exp1.ID = col1.FK_DC_EXPRESSION" +
						" AND att1.ATTRIBUTE_SPEC = 'KEY'" +
						" UNION" +
						" SELECT '2' entita, " +
						" 'E_' || col2.fk_dc_entity || '.' || exp2.alias ALIAS_CHIAVE, " +
						" 'E_' || col2.fk_dc_entity ALIAS_ENTITY," +
						" att2.ATTRIBUTE_VAL POSITION" +
						" FROM dc_column col2, dc_attribute att2, dc_expression exp2" +
						" WHERE col2.FK_DC_ENTITY = ?" +
						" and att2.FK_DC_COLUMN = col2.ID" +
						" and exp2.ID = col2.FK_DC_EXPRESSION" +
						" AND att2.ATTRIBUTE_SPEC = 'KEY'" +
						") ORDER BY POSITION";
		 
		int recordElaborati = 0;
		try
		{
			connDWH = DataJdbcConnection.getConnectionn("DWH");
			connCatalog = DataJdbcConnection.getConnectionn();

			log.debug("INIZIO VERIFICA RELAZIONI");
			long t1 = System.currentTimeMillis();
			long t2 = System.currentTimeMillis();


			psMetaData = connCatalog.prepareStatement(sqlMetaDataLink);
			psMetaData.setLong(1, id_link);
			rsMetaData = psMetaData.executeQuery();
			List arrayChiavi1 = new ArrayList();
			List arrayChiavi2 = new ArrayList();
			int giro = 0;
			int conteggio = 0;
			while (rsMetaData.next())
			{
				int id1 = rsMetaData.getInt("ID1");
				int id2 = rsMetaData.getInt("ID2");
				String sql1 = rsMetaData.getString("SQL1");
				String sql2 = rsMetaData.getString("SQL2");
				String alias1 = rsMetaData.getString("ALIAS1");
				String alias2 = rsMetaData.getString("ALIAS2");
				String relazione = rsMetaData.getString("RELAZIONE");
				int fk_dv_link = rsMetaData.getInt("IDLINK");
				psChiavi = connCatalog.prepareStatement(sqlChiavi);
				psChiavi.setInt(1, id1);
				psChiavi.setInt(2, id2);
				log.debug("Eseguo sqlChiavi"+ sqlChiavi);
				log.debug("Parametri sqlChiavi "+ id1 + "/" + id2);
				rsChiavi = psChiavi.executeQuery();
				int idchiave = 1;
				while (rsChiavi.next())
				{
					if (rsChiavi.getString("ENTITA").equals("1"))
					{
						arrayChiavi1.add(rsChiavi.getString("ALIAS_CHIAVE"));
						arrayChiavi1.add(", ");
					}
					else if (rsChiavi.getString("ENTITA").equals("2"))
					{
						arrayChiavi2.add(rsChiavi.getString("ALIAS_CHIAVE"));
						arrayChiavi2.add(", ");
					}
					idchiave++;
				}
				rsChiavi.close();
				String query_reale = " SELECT ";
				for (int i = 0; i < arrayChiavi1.size(); i++)
				{
					query_reale += arrayChiavi1.get(i);
				}
				for (int i = 0; i < arrayChiavi2.size(); i++)
				{
					query_reale += arrayChiavi2.get(i);
				}
				query_reale += id1 + ", " + id2 + " FROM ";
				query_reale += "( " + sql1 + " ) " + alias1 + ", ( " + sql2 + " ) " + alias2;
				query_reale += " WHERE " + relazione;
				String[] entityKeys = entityKey.split("#");
				int entityKeysId = 0;
				for (int i = 0; i < arrayChiavi1.size(); i++)
				{
					if (!arrayChiavi1.get(i).equals(", ")) {
						query_reale += " AND " + arrayChiavi1.get(i) + "= '" + entityKeys[entityKeysId] + "'";
						entityKeysId++;
					}
				}
				log.debug("Esecuzione query reale:" + query_reale);
				psReale = connDWH.prepareStatement(query_reale);
				rsReale = psReale.executeQuery();
				
				while (rsReale.next())
				{
					if (giro == 5000)
					{
						log.debug("TEMPO PARZIALE " + conteggio + " Record:" + new String(((System.currentTimeMillis() - t1) / 600) / 60 + ""));
						t1 = System.currentTimeMillis();
						giro = 0;
					}
					recordElaborati++;
					giro++;
					conteggio++;
					String entity1 = "";
					String entity2 = "";
					int giroK = 1;
					for (int y = 0; y < arrayChiavi1.size(); y++)
					{
						if (!arrayChiavi1.get(y).equals(", "))
						{
							entity1 += rsReale.getObject(giroK) + "#";
							giroK++;
						}
					}
					for (int y = 0; y < arrayChiavi2.size(); y++)
					{
						if (!arrayChiavi2.get(y).equals(", "))
						{
							entity2 += rsReale.getObject(giroK) + "#";
							giroK++;
						}
					}
					String entity_key = "";
					entity_key = entity2;
					entity_key = entity_key.substring(0, entity_key.length() - 1);
					
					keys.add(entity_key);
						
				}
			}
			log.debug("FINE RECUPERO RELAZIONI");
		}
		catch (Exception e)
		{
			log.error(e);
			log.error("Record elaborati= " +recordElaborati);
			log.error("Errore reperimento chiavi link! ID_LINK" + id_link, e);
		}
		finally
		{
			try
			{
			
				if (psDeleteDvKeyPair != null)
					psDeleteDvKeyPair.close();				
				if (rsRicPair != null)
					rsRicPair.close();				
				if (psRicPair != null)
					psRicPair.close();				
				if (psRic != null)
					psRic.close();				
				if (rsRic != null)
					rsRic.close();				
				if (psDvKey != null)
					psDvKey.close();				
				if (psDvKeyPair != null)
					psDvKeyPair.close();				
				if (psED != null)
					psED.close();				
				if (rsReale != null)
					rsReale.close();
				if (psReale != null)
					psReale.close();				
				if (rsChiavi != null)
					rsChiavi.close();
				if (psChiavi != null)
					psChiavi.close();				
				if (rsMetaData != null)
					rsMetaData.close();
				if (psMetaData != null)
					psMetaData.close();
				if (connDWH != null)
					connDWH.close();
				if (connCatalog != null)
					connCatalog.close();
			}
			catch (Exception ec)
			{
				log.error("Errore chiusura resultSet o Statement!", ec);
			}
		}
		return keys;

	}


}
