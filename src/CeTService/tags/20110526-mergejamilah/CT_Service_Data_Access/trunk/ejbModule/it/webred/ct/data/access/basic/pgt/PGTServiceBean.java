package it.webred.ct.data.access.basic.pgt;

import it.webred.ct.data.access.basic.pgt.dao.PgtDAO;
import it.webred.ct.data.access.basic.pgt.dto.DatoPgtDTO;
import it.webred.ct.data.access.basic.pgt.dto.RicercaPgtDTO;
import it.webred.ct.data.model.pgt.PgtSqlLayer;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.sql.rowset.CachedRowSet;

import org.springframework.beans.factory.annotation.Autowired;
@Stateless
public class PGTServiceBean implements PGTService {
	@Autowired
	private PgtDAO pgtDAO;
	@Override
	public Hashtable<String,Object> getDatiPgtLayer(RicercaPgtDTO rp)  {
		Hashtable<String,Object> ht=new Hashtable<String,Object>();
		List<DatoPgtDTO> lista = new ArrayList<DatoPgtDTO>();
		PgtSqlLayer pgtSqlLayer = pgtDAO.getLayerByPK(rp);
		if (pgtSqlLayer==null)
			return ht;
		ht=pgtDAO.getHTDatiPgt(rp, pgtSqlLayer);
		return ht;
	}
	@Override
	public List<PgtSqlLayer> getListaLayersByTipo(RicercaPgtDTO rp) {
		List<PgtSqlLayer> lista= new ArrayList<PgtSqlLayer>();
		lista = pgtDAO.getListaLayersByTipo(rp);
		return lista;
	}

}
