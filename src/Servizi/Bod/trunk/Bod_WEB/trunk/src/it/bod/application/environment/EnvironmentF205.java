package it.bod.application.environment;

import it.bod.application.beans.PrgBean;
import it.bod.application.beans.VincoloBean;
import it.doro.tools.Character;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


public class EnvironmentF205 extends BaseEnvironment implements IEnvironment {

 


	public EnvironmentF205() {
		super();
		// TODO Auto-generated constructor stub
	}

	
public List<PrgBean> getPRG(String foglio, String particella, String codEnte) {

	String sql = "SELECT " +
	"foglio, particella, codice as dest_funz, descrizione as legenda, " +
	"TO_CHAR (area_part, '999999d00') as area_part, " +
	"TO_CHAR (area_prg, '999999999d00') as area_prg, " +
	"TO_CHAR (sdo_geom.sdo_area (area_int, 1), '999999d00') as area_int " +
	"FROM " +
	"(SELECT p.foglio, p.particella, pr.codice, l.descrizione, " +
	"sdo_geom.sdo_area (p.shape, 0.005) area_part, " +
	"sdo_geom.sdo_area (pr.shape, 0.005) area_prg, " +
	"sdo_geom.sdo_intersection (p.shape, pr.shape, 0.005) area_int " +
	"FROM " +
	"sitipart p, prg pr, sitideco_catalog l, siticomu sc " +
	"WHERE 1=1 " +
	"and p.cod_nazionale = sc.cod_nazionale " +
	"and sc.codi_fisc_luna = '" + codEnte + "' " +
	"AND p.foglio = to_number('" + foglio + "') " +
	"AND p.particella = LPAD (TRIM ('" + particella + "'), 5, '0') " +
	"AND p.data_fine_val = TO_DATE ('31/12/9999', 'dd/mm/yyyy') " +
	"AND l.id_cat = 1 " +
	"AND l.codut = pr.codice " +
	"AND sdo_relate (pr.shape, (SELECT p1.shape FROM sitipart p1, siticomu sc1 WHERE p1.cod_nazionale = sc1.cod_nazionale and sc1.codi_fisc_luna = '" + codEnte + "' AND p1.foglio = to_number('" + foglio + "') AND p1.particella = LPAD (TRIM ('" + particella + "'), 5, '0') " +
	"AND data_fine_val = TO_DATE ('31/12/9999', 'dd/mm/yyyy')),'MASK=ANYINTERACT') = 'TRUE') ";

	Hashtable htQry  = new Hashtable();
	htQry.put("queryString", sql);
	List<Object> listaPrg = bodLogicService.getList(htQry);
	List<PrgBean> lstPrg = new ArrayList<PrgBean>();
	if (listaPrg != null && listaPrg.size()>0){
		//logger.debug("Num. PRG: " + listaPrg.size());
		PrgBean prg = null;
		for (int i=0; i<listaPrg.size(); i++){
			prg = new PrgBean();
			Object[] objs = (Object[])listaPrg.get(i);
			prg.setDestFunz(Character.checkNullString((String)objs[2]));
			prg.setLegenda(Character.checkNullString((String)objs[3]));
			prg.setAreaPart(Character.checkNullString((String)objs[4]));
			prg.setAreaPrg(Character.checkNullString((String)objs[5]));
			prg.setAreaInt(Character.checkNullString((String)objs[6]));
			
			lstPrg.add(prg);
		}
	}
	return lstPrg;
}
	
	public List<VincoloBean> getVincoli(String foglio, String particella, String codEnte) {
		String sql = "select distinct " +
		"sp.foglio, sp.particella, 'Aeroportuale Bresso' as vincolo, vin.descrizione,  " +
		"sdo_geom.sdo_area (sp.shape, 0.005) as area_part, " +
		"sdo_geom.sdo_area (vin.shape, 0.005) as area_vin, " +
		"sdo_geom.sdo_area(sdo_geom.sdo_intersection (sp.shape, vin.shape, 0.005),0.005) as area_int " +
		"from sitipart sp, cat_vin_aer_bresso vin, siticomu sc " +
		"where sc.codi_fisc_luna = (select uk_belfiore from ewg_tab_comuni) " +
		"AND sc.cod_nazionale = sp.cod_nazionale " +
		"AND sp.foglio = to_number('" + foglio + "') " +
		"AND sp.particella = LPAD (TRIM ('" + particella + "'), 5, '0') " +
		"AND sp.data_fine_val = TO_DATE ('31/12/9999', 'dd/mm/yyyy') " +
		"AND sdo_relate (vin.shape, " +
		"(SELECT shape FROM sitipart sp1, siticomu sc1 " +
		"WHERE sc1.codi_fisc_luna = (select uk_belfiore from ewg_tab_comuni) " +
		"AND sc1.cod_nazionale = sp1.cod_nazionale " +
		"AND sp1.foglio = to_number('" + foglio + "') " +
		"AND sp1.particella = LPAD (TRIM ('" + particella + "'), 5, '0') " +
		"AND sp1.data_fine_val = TO_DATE ('31/12/9999', 'dd/mm/yyyy')),'MASK=ANYINTERACT') = 'TRUE' " +
		"union " +
		"select distinct sp.foglio, sp.particella, 'Aeroportuale Linate' as vincolo, vin.descrizione,  " +
		"sdo_geom.sdo_area (sp.shape, 0.005) as area_part, " +
		"sdo_geom.sdo_area (vin.shape, 0.005) as area_vin, " +
		"sdo_geom.sdo_area(sdo_geom.sdo_intersection (sp.shape, vin.shape, 0.005),0.005) as area_int " +
		"from sitipart sp, cat_vin_aer_linate vin, siticomu sc " +
		"where sc.codi_fisc_luna = (select uk_belfiore from ewg_tab_comuni) " +
		"AND sc.cod_nazionale = sp.cod_nazionale " +
		"AND sp.foglio = to_number('" + foglio + "') " +
		"AND sp.particella = LPAD (TRIM ('" + particella + "'), 5, '0') " +
		"AND sp.data_fine_val = TO_DATE ('31/12/9999', 'dd/mm/yyyy') " +
		"AND sdo_relate (vin.shape, " +
		"(SELECT shape FROM sitipart sp1, siticomu sc1 " +
		"WHERE sc1.codi_fisc_luna = (select uk_belfiore from ewg_tab_comuni) " +
		"AND sc1.cod_nazionale = sp1.cod_nazionale " +
		"AND sp1.foglio = to_number('" + foglio + "') " +
		"AND sp1.particella = LPAD (TRIM ('" + particella + "'), 5, '0') " +
		"AND sp1.data_fine_val = TO_DATE ('31/12/9999', 'dd/mm/yyyy')),'MASK=ANYINTERACT') = 'TRUE' " +
		"union " +
		"select distinct sp.foglio, sp.particella, 'Architettonico' as vincolo, vin.descrizione,  " +
		"sdo_geom.sdo_area (sp.shape, 0.005) as area_part, " +
		"sdo_geom.sdo_area (vin.shape, 0.005) as area_vin, " +
		"sdo_geom.sdo_area(sdo_geom.sdo_intersection (sp.shape, vin.shape, 0.005),0.005) as area_int " +
		"from sitipart sp, cat_vin_arch vin, siticomu sc " +
		"where sc.codi_fisc_luna = (select uk_belfiore from ewg_tab_comuni) " +
		"AND sc.cod_nazionale = sp.cod_nazionale " +
		"AND sp.foglio = to_number('" + foglio + "') " +
		"AND sp.particella = LPAD (TRIM ('" + particella + "'), 5, '0') " +
		"AND sp.data_fine_val = TO_DATE ('31/12/9999', 'dd/mm/yyyy') " +
		"AND sdo_relate (vin.shape, " +
		"(SELECT shape FROM sitipart sp1, siticomu sc1  " +
		"WHERE sc1.codi_fisc_luna = (select uk_belfiore from ewg_tab_comuni) " +
		"AND sc1.cod_nazionale = sp1.cod_nazionale " +
		"AND sp1.foglio = to_number('" + foglio + "') " +
		"AND sp1.particella = LPAD (TRIM ('" + particella + "'), 5, '0') " +
		"AND sp1.data_fine_val = TO_DATE ('31/12/9999', 'dd/mm/yyyy')),'MASK=ANYINTERACT') = 'TRUE' " +
		"union " +
		"select distinct sp.foglio, sp.particella, 'Bellezze naturali' as vincolo, vin.descrizion as descrizione,  " +
		"sdo_geom.sdo_area (sp.shape, 0.005) as area_part, " +
		"sdo_geom.sdo_area (vin.shape, 0.005) as area_vin, " +
		"sdo_geom.sdo_area(sdo_geom.sdo_intersection (sp.shape, vin.shape, 0.005),0.005) as area_int " +
		"from sitipart sp, cat_vin_bel_natu vin, siticomu sc " +
		"where sc.codi_fisc_luna = (select uk_belfiore from ewg_tab_comuni) " +
		"AND sc.cod_nazionale = sp.cod_nazionale " +
		"AND sp.foglio = to_number('" + foglio + "') " +
		"AND sp.particella = LPAD (TRIM ('" + particella + "'), 5, '0') " +
		"AND sp.data_fine_val = TO_DATE ('31/12/9999', 'dd/mm/yyyy') " +
		"AND sdo_relate (vin.shape, " +		
		"(SELECT shape FROM sitipart sp1, siticomu sc1  " +
		"WHERE sc1.codi_fisc_luna = (select uk_belfiore from ewg_tab_comuni) " +
		"AND sc1.cod_nazionale = sp1.cod_nazionale " +		
		"AND sp1.foglio = to_number('" + foglio + "') " +
		"AND sp1.particella = LPAD (TRIM ('" + particella + "'), 5, '0') " +
		"AND sp1.data_fine_val = TO_DATE ('31/12/9999', 'dd/mm/yyyy')),'MASK=ANYINTERACT') = 'TRUE' " +
		"union " +
		"select distinct sp.foglio, sp.particella, 'Monumentale' as vincolo, vin.descrizion as descrizione,  " +
		"sdo_geom.sdo_area (sp.shape, 0.005) as area_part, " +
		"sdo_geom.sdo_area (vin.shape, 0.005) as area_vin, " +
		"sdo_geom.sdo_area(sdo_geom.sdo_intersection (sp.shape, vin.shape, 0.005),0.005) as area_int " +
		"from sitipart sp, cat_vin_monum vin, siticomu sc " +
		"where sc.codi_fisc_luna = (select uk_belfiore from ewg_tab_comuni) " +
		"AND sc.cod_nazionale = sp.cod_nazionale " +
		"AND sp.foglio = to_number('" + foglio + "') " +
		"AND sp.particella = LPAD (TRIM ('" + particella + "'), 5, '0') " +
		"AND sp.data_fine_val = TO_DATE ('31/12/9999', 'dd/mm/yyyy') " +
		"AND sdo_relate (vin.shape, " +		
		"(SELECT shape FROM sitipart sp1, siticomu sc1  " +
		"WHERE sc1.codi_fisc_luna = (select uk_belfiore from ewg_tab_comuni) " +
		"AND sc1.cod_nazionale = sp1.cod_nazionale " + 		
		"AND sp1.foglio = to_number('" + foglio + "') " +
		"AND sp1.particella = LPAD (TRIM ('" + particella + "'), 5, '0') " +
		"AND sp1.data_fine_val = TO_DATE ('31/12/9999', 'dd/mm/yyyy')),'MASK=ANYINTERACT') = 'TRUE' " +
		"union " +
		"select distinct sp.foglio, sp.particella, 'Monumentale sedimi' as vincolo, vin.descrizion as descrizione,  " +
		"sdo_geom.sdo_area (sp.shape, 0.005) as area_part, " +
		"sdo_geom.sdo_area (vin.shape, 0.005) as area_vin, " +
		"sdo_geom.sdo_area(sdo_geom.sdo_intersection (sp.shape, vin.shape, 0.005),0.005) as area_int " +
		"from sitipart sp, cat_vin_monum_sed vin, siticomu sc " +
		"where sc.codi_fisc_luna = (select uk_belfiore from ewg_tab_comuni) " +
		"AND sc.cod_nazionale = sp.cod_nazionale " +
		"AND sp.foglio = to_number('" + foglio + "') " +
		"AND sp.particella = LPAD (TRIM ('" + particella + "'), 5, '0') " +
		"AND sp.data_fine_val = TO_DATE ('31/12/9999', 'dd/mm/yyyy') " +
		"AND sdo_relate (vin.shape, " +		
		"(SELECT shape FROM sitipart sp1, siticomu sc1  " +
		"WHERE sc1.codi_fisc_luna = (select uk_belfiore from ewg_tab_comuni) " +
		"AND sc1.cod_nazionale = sp1.cod_nazionale " + 	
		"AND sp1.foglio = to_number('" + foglio + "') " +
		"AND sp1.particella = LPAD (TRIM ('" + particella + "'), 5, '0') " +
		"AND sp1.data_fine_val = TO_DATE ('31/12/9999', 'dd/mm/yyyy')),'MASK=ANYINTERACT') = 'TRUE' " +
		"union " +
		"select distinct sp.foglio, sp.particella, 'PAI classi rischio' as vincolo, vin.codice as descrizione,  " +
		"sdo_geom.sdo_area (sp.shape, 0.005) as area_part, " +
		"sdo_geom.sdo_area (vin.shape, 0.005) as area_vin, " +
		"sdo_geom.sdo_area(sdo_geom.sdo_intersection (sp.shape, vin.shape, 0.005),0.005) as area_int " +
		"from sitipart sp, cat_vin_pai_clas_risc vin, siticomu sc " +		
		"where sc.codi_fisc_luna = (select uk_belfiore from ewg_tab_comuni) " +
		"AND sc.cod_nazionale = sp.cod_nazionale " +		
		"AND sp.foglio = to_number('" + foglio + "') " +
		"AND sp.particella = LPAD (TRIM ('" + particella + "'), 5, '0') " +
		"AND sp.data_fine_val = TO_DATE ('31/12/9999', 'dd/mm/yyyy') " +
		"AND sdo_relate (vin.shape, " +		
		"(SELECT shape FROM sitipart sp1, siticomu sc1  " +
		"WHERE sc1.codi_fisc_luna = (select uk_belfiore from ewg_tab_comuni) " +
		"AND sc1.cod_nazionale = sp1.cod_nazionale " + 		
		"AND sp1.foglio = to_number('" + foglio + "') " +
		"AND sp1.particella = LPAD (TRIM ('" + particella + "'), 5, '0') " +
		"AND sp1.data_fine_val = TO_DATE ('31/12/9999', 'dd/mm/yyyy')),'MASK=ANYINTERACT') = 'TRUE' " +
		"union " +
		"select distinct sp.foglio, sp.particella, 'PTCT fasce radar' as vincolo, vin.codice as descrizione,  " +
		"sdo_geom.sdo_area (sp.shape, 0.005) as area_part, " +
		"sdo_geom.sdo_area (vin.shape, 0.005) as area_vin, " +
		"sdo_geom.sdo_area(sdo_geom.sdo_intersection (sp.shape, vin.shape, 0.005),0.005) as area_int " +
		"from sitipart sp, cat_vin_ptcp_fas_rad vin, siticomu sc " +		
		"where sc.codi_fisc_luna = (select uk_belfiore from ewg_tab_comuni) " +
		"AND sc.cod_nazionale = sp.cod_nazionale " +		
		"AND sp.foglio = to_number('" + foglio + "') " +
		"AND sp.particella = LPAD (TRIM ('" + particella + "'), 5, '0') " +
		"AND sp.data_fine_val = TO_DATE ('31/12/9999', 'dd/mm/yyyy') " +
		"AND sdo_relate (vin.shape, " +		
		"(SELECT shape FROM sitipart sp1, siticomu sc1  " +
		"WHERE sc1.codi_fisc_luna = (select uk_belfiore from ewg_tab_comuni) " +
		"AND sc1.cod_nazionale = sp1.cod_nazionale " +		
		"AND sp1.foglio = to_number('" + foglio + "') " +
		"AND sp1.particella = LPAD (TRIM ('" + particella + "'), 5, '0') " +
		"AND sp1.data_fine_val = TO_DATE ('31/12/9999', 'dd/mm/yyyy')),'MASK=ANYINTERACT') = 'TRUE' " +
		"order by vincolo ";
		Hashtable htQry = new Hashtable();
		htQry = new Hashtable();
		htQry.put("queryString", sql);
		List<Object> listaVincoli = bodLogicService.getList(htQry);
		List<VincoloBean> lstVincoli = new ArrayList<VincoloBean>();
		if (listaVincoli != null && listaVincoli.size()>0){
			//logger.debug("Num. Vincoli: " + listaVincoli.size());
			VincoloBean vincolo = null;
			for (int i=0; i<listaVincoli.size(); i++){
				vincolo = new VincoloBean();
				Object[] objs = (Object[])listaVincoli.get(i);
				vincolo.setFoglio(((BigDecimal)objs[0]).toString());
				vincolo.setParticella( (java.lang.String)objs[1] );
				vincolo.setVincolo(Character.checkNullString((String)objs[2]));
				vincolo.setDescrizione(Character.checkNullString((String)objs[3]));
				vincolo.setAreaPart(((BigDecimal)objs[4]).toString());
				vincolo.setAreaVin(((BigDecimal)objs[5]).toString());
				vincolo.setAreaInt(((BigDecimal)objs[6]).toString());
				
				lstVincoli.add(vincolo);
			}
		}
		return lstVincoli;
	}

	
}
