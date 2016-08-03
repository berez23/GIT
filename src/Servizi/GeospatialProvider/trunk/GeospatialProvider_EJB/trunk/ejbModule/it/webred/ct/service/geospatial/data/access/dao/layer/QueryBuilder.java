package it.webred.ct.service.geospatial.data.access.dao.layer;

import it.webred.ct.data.model.pgt.PgtSqlLayer;

public class QueryBuilder {
	
	
	
	public String createQuery(Long foglio, String particella, String ente, PgtSqlLayer psl) {
		
		StringBuilder sql = new StringBuilder("select ");
		sql.append(" tl."+psl.getNameColId()+" ID_GEOMETRIA_LAYER, ");
		sql.append(" '"+psl.getDesLayer()+"' DES_LAYER, ");
		sql.append(" '"+psl.getTipoLayer()+"' TIPO_LAYER, ");
		sql.append(" tl."+psl.getNameColTema()+" CODICE_TEMA, ");
		sql.append(" tl."+psl.getNameColTemaDescr()+" DES_TEMA, ");
		sql.append(" '"+psl.getNameTable()+"' NAME_TABLE, ");
		sql.append(" tl."+psl.getNameColShape()+" SHAPE ");
		sql.append(" from ");
		sql.append(" ("+psl.getSqlLayer()+") tl, SITIPART sp, SITICOMU sc ");
		sql.append(" where ");
		sql.append(" nvl(sp.data_fine_val, to_date('31/12/9999','DD/MM/YYYY')) = ");
		sql.append(" to_date('31/12/9999','DD/MM/YYYY') ");
		sql.append(" and ");
		sql.append(" sdo_relate(tl."+psl.getNameColShape()+", sdo_geom.sdo_buffer(sp.shape,mdsys.sdo_dim_array(mdsys.sdo_dim_element('X',1313328,2820083,0.0050), mdsys.sdo_dim_element('Y',3930191,5220322.5,0.0050)),'50'), 'MASK=ANYINTERACT QUERYTYPE=WINDOW') = 'TRUE' ");
		sql.append(" and ");
		sql.append(" sp.cod_nazionale = sc.cod_nazionale ");
		sql.append(" and ");
		sql.append(" sc.codi_fisc_luna = '"+ente+"' ");
		sql.append(" and ");
		sql.append(" sp.foglio = "+foglio+" ");
		sql.append(" and ");
		sql.append(" sp.particella = '"+particella+"'");
		
		//System.out.println(sql.toString());
		
		return sql.toString();
	}
	
	public String createQueryIntersezioneUpl(Long idIntervento) {
		
		StringBuilder sql = new StringBuilder("select * from dual where ");
		sql.append(" (select count(part.FOGLIO || part.PARTICELLA) from s_sp_area_part part where part.FK_SP_INTERVENTO = "+idIntervento+") = ");
		sql.append(" (Select count(a.FOGLIO || a.PARTICELLA) from s_sp_area_part a, s_sp_progetto p ");
		sql.append(" where nvl(a.data_fine_val, to_date('31/12/9999','DD/MM/YYYY')) = to_date('31/12/9999','DD/MM/YYYY') ");
		sql.append(" and p.FK_INTERVENTO = a.FK_SP_INTERVENTO ");
		sql.append(" and a.FK_SP_INTERVENTO = "+idIntervento+" ");
		sql.append(" and sdo_relate(a.SHAPE,p.GEOMETRY,'MASK=ANYINTERACT QUERYTYPE=WINDOW') = 'TRUE')");
		
		//System.out.println(sql.toString());
		
		return sql.toString();
	}
	
}
