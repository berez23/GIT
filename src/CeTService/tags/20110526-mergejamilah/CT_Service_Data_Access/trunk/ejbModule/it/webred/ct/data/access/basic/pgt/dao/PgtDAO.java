package it.webred.ct.data.access.basic.pgt.dao;

import it.webred.ct.data.access.basic.pgt.PgtServiceException;
import it.webred.ct.data.access.basic.pgt.dto.DatoPgtDTO;
import it.webred.ct.data.access.basic.pgt.dto.RicercaPgtDTO;
import it.webred.ct.data.model.pgt.PgtSqlLayer;


import java.util.Hashtable;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

public interface PgtDAO {
	public PgtSqlLayer getLayerByPK(RicercaPgtDTO rp) throws PgtServiceException;
	public List<PgtSqlLayer> getListaLayersByTipo(RicercaPgtDTO rp) throws PgtServiceException;
    public Hashtable<String,Object> getHTDatiPgt(RicercaPgtDTO rp, PgtSqlLayer pgtSqlLayer)  throws PgtServiceException;
}
