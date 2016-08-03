package it.webred.ct.data.access.basic.pgt.dao;

import it.webred.ct.data.access.basic.common.CommonServiceException;
import it.webred.ct.data.access.basic.pgt.PGTService;
import it.webred.ct.data.access.basic.pgt.PgtServiceException;
import it.webred.ct.data.access.basic.pgt.dto.RicercaPgtDTO;
import it.webred.ct.data.access.basic.pgt.dto.DatoPgtDTO;
import it.webred.ct.data.model.pgt.PgtSqlLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;


public class PgtJPAImpl extends PgtBaseDAO implements PgtDAO {
	
	@Override
	public PgtSqlLayer getLayerByPK(RicercaPgtDTO rp) throws PgtServiceException {
		PgtSqlLayer ele=null;
		logger.debug("getLayerByPK - PARAMETRI[ID:"+ rp.getId()+"]; [STANDARD:"+ rp.getStandard()+"]");
		try {
			Query q =manager_diogene.createNamedQuery("Pgt.getLayerByPK");
			q.setParameter("id", rp.getId());
			q.setParameter("standard", rp.getStandard());
			ele =(PgtSqlLayer) q.getSingleResult();
			logger.warn("TROVATO PGT_SQl_LAYER");
		}catch (NoResultException nre) {
			logger.warn("No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new PgtServiceException (t);
		}
		return ele;
	}
	@Override
	public List<PgtSqlLayer> getListaLayersByTipo(RicercaPgtDTO rp) throws PgtServiceException {
		List<PgtSqlLayer> lista= new ArrayList<PgtSqlLayer>();
		logger.debug("getListPgtSqlLayer - PARAMETRI[COD_TIP_LAYER:"+ rp.getTipoLayer()+"]");
		try {
			Query q =manager_diogene.createNamedQuery("Pgt.getLayersByTipo");
			q.setParameter("tipoLayer", rp.getTipoLayer());
			lista = (List<PgtSqlLayer>)q.getResultList();
			if (lista.size() > 0)
				logger.warn("TROVATI DATI PGT_SQL_LAYER");
		} catch (Throwable t) {
			logger.error("", t);
			throw new PgtServiceException (t);
		}
		return lista;
	}

	@Override
	public Hashtable<String,Object> getHTDatiPgt(RicercaPgtDTO rp, PgtSqlLayer pgtSqlLayer) throws PgtServiceException {
		Hashtable<String,Object> ht = new Hashtable<String,Object>();
		List<DatoPgtDTO> listaDati = new ArrayList<DatoPgtDTO> ();
		List listaValori=new ArrayList();
		String nomeColShape = pgtSqlLayer.getNameColShape(); 
		String mask = "00000";		    
		// left padding by zeros...
	    String particella = mask.substring(0 , mask.length() - rp.getParticella().length()) + rp.getParticella();
		String sql = "SELECT C.ID_SEZC AS SEZIONE, L.* FROM (" + pgtSqlLayer.getSqlLayer() + ") L, SITIPART S, SITICOMU C ";
	    sql += " WHERE C.CODI_FISC_LUNA='"+ rp.getCodNazionale()+ "' AND C.COD_NAZIONALE=S.COD_NAZIONALE ";
	    sql += " AND SDO_RELATE (L." +nomeColShape + ", S.SHAPE,'MASK=ANYINTERACT') = 'TRUE' " ;
        if (rp.getSezione() != null && !rp.getSezione().equals(""))
        	sql+=" AND C.ID_SEZC= '" + rp.getSezione().trim() + "'";
        sql += " AND S.FOGLIO=" + rp.getFoglio() + "  AND S.PARTICELLA='" +  particella +"'";
        System.out.println("SQL DA ESEGUIRE: " + sql);
        PreparedStatement ps = null;
	    Connection con = null;
	    ResultSet rs = null;
	    try {
	    	con = dataSource.getConnection();
	        System.out.println("Ottenuta connessione JDBC");
	        ps = con.prepareStatement(sql);
	        rs = ps.executeQuery();
	        if (rs==null)
	    	   return ht;
	        ResultSetMetaData rsmd = rs.getMetaData();
	        int numTotCols = rsmd.getColumnCount();
	       	DatoPgtDTO ele=null;
	       	int indexShape=-1;
        	for (int i=1; i<=numTotCols ; i++ ) {
      		   ele = new DatoPgtDTO();
      		   if (rsmd.getColumnName(i).toLowerCase().equals(nomeColShape.toLowerCase())) {//la colonna shape non la considero (JBOSS NON RIESCE A RECUPERARNE I DATI DAI METADATI..)
      			   indexShape=i;
      			   continue;
      		   }
	    	   ele.setNomeDato(rsmd.getColumnName(i));
	    	   ele.setTipoOracleDato(rsmd.getColumnTypeName(i));
	    	   ele.setTipoJavaDato(rsmd.getColumnClassName(i));
	    	   listaDati.add(ele);
	    	}
        	Object valore = null;
        	List<Object> rigaValori=null;
 	        while (rs.next()) {
	           	rigaValori= new ArrayList<Object>();
	        	for (int i=1; i<=numTotCols ; i++ ) {
	        		valore = new Object();
	        		if (i==indexShape)
	        			continue;
	        		if (rs.getObject(i) != null)
	        			valore = rs.getObject(i) ;
	        		rigaValori.add(valore);
	        	}
	        	listaValori.add(rigaValori);
	        }
 	        ht.put(PGTService.KEY_HASH_COLONNE, listaDati);
 	        ht.put(PGTService.KEY_HASH_VALORI, listaValori);
	     
	    }
	    catch (SQLException sqle)
	    {
	       logger.error("ERRORE SQL: ", sqle);
	       throw new PgtServiceException();
	    }
	    finally
	    {
	    	try { if (rs != null) rs.close(); } catch (Exception e) {}
	    	try { if (ps != null) ps.close(); } catch (Exception e) {}
	    	try { if (con != null) con.close(); } catch (Exception e) {}
	    }    

		return ht;
	}
	

}
