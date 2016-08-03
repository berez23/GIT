package it.webred.ct.data.access.basic.pgt;

import it.webred.ct.data.access.basic.pgt.dto.DatoPgtDTO;
import it.webred.ct.data.access.basic.pgt.dto.RicercaPgtDTO;
import it.webred.ct.data.model.pgt.PgtSqlLayer;

import java.util.Hashtable;
import java.util.List;

import javax.ejb.Remote;

@Remote
public interface PGTService {
	public final static String KEY_HASH_COLONNE="KEY_HASH_COLONNE";
	public final static String KEY_HASH_VALORI="KEY_HASH_VALORI";

	public Hashtable<String,Object> getDatiPgtLayer(RicercaPgtDTO rp) ;
	public List<PgtSqlLayer> getListaLayersByTipo(RicercaPgtDTO rp) ;
}
