/*
 * Created on 2-lug-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.crif.logic;

import it.escsolution.escwebgis.anagrafe.bean.Anagrafe;
import it.escsolution.escwebgis.common.EnvSource;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.crif.bean.RedditoTotDichiarato;
import it.escsolution.escwebgis.crif.bean.RedditoTotFamiglia;
import it.escsolution.escwebgis.crif.bean.RedditoTotSoggetto;
import it.escsolution.escwebgis.redditiAnnuali.bean.RedditiAnnuali;
import it.escsolution.escwebgis.toponomastica.bean.Civico;
import it.webred.cet.permission.CeTUser;
import it.webred.ct.data.access.basic.fonti.FontiService;
import it.webred.ct.data.access.basic.redditi.RedditiService;
import it.webred.ct.data.access.basic.redditi.dto.KeySoggDTO;
import it.webred.ct.data.access.basic.redditi.dto.RedditiDicDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class CrifLogic extends EscLogic {
	private String appoggioDataSource;
	private static HashMap tipoModello = new HashMap<String, String>();
			
	static {
		List<String> codiciQuadro = new ArrayList<String>();
		codiciQuadro.add("RA011");
		codiciQuadro.add("RB011");
		codiciQuadro.add("RC005");
		codiciQuadro.add("RC009");
		codiciQuadro.add("RL019");
		codiciQuadro.add("RL003");
		codiciQuadro.add("RL022");
		codiciQuadro.add("RL030");
		codiciQuadro.add("RH017");
		codiciQuadro.add("RH014");
		codiciQuadro.add("RM015");
		codiciQuadro.add("RE025");
		tipoModello.put("U", codiciQuadro);
		codiciQuadro = new ArrayList<String>();
		codiciQuadro.add("PL001");
		codiciQuadro.add("PL002");
		codiciQuadro.add("PL003");
		codiciQuadro.add("PL004");
		codiciQuadro.add("PL005");
		tipoModello.put("3", codiciQuadro);
		codiciQuadro = new ArrayList<String>();
		codiciQuadro.add("DB001001");
		codiciQuadro.add("DB001002");
		tipoModello.put("S", codiciQuadro);
		
	}
	
	public CrifLogic(EnvUtente eu) {
		super(eu);
		appoggioDataSource = eu.getDataSource();

	}

	public List<RedditoTotFamiglia> getRedditiFamigliaCivicoTopo(String idSezione,
			HttpServletRequest request) throws Exception {
		
		List<RedditoTotFamiglia> lista = new ArrayList<RedditoTotFamiglia>();

		String sql1 = " select s.prefisso, s.nome, c.* from crif_reddito_medio_famiglia r , siticivi c, sitidstr s "
				+ " where SDO_RELATE (c.SHAPE, r.shape, 'MASK=ANYINTERACT  QUERYTYPE=WINDOW') = 'TRUE' "
				+ " and c.data_fine_val = to_date('31-12-9999','dd-mm-yyyy') "
				+ " and c.pkid_stra = s.pkid_stra "
				+ " and se_row_id = ? "
				+ " order by prefisso, nome, civico ";
		
		// Per ogni civico del catasto recupero la lista dei civici
		// anagrafe correlati a quello in toponomastica
		String sql = "select distinct c_ana.id_dwh id_ana_civ , via.viasedime prefisso, via.descrizione nome, LTRIM(C_ANA.CIV_LIV1,'0') civico_1, LTRIM(C_ANA.CIV_LIV2,'0') civico_2 " +
		" from  sit_civico_totale c_cat, sit_civico_totale c_ana, sit_via_totale v_ana, sit_d_via via, " +
		"    (SELECT s.prefisso, s.nome, c.* " +
		"    FROM crif_reddito_medio_famiglia r, siticivi c, sitidstr s " +
		"   WHERE     SDO_RELATE (c.SHAPE, " +
		"                         r.shape, " +
		"                         'MASK=ANYINTERACT  QUERYTYPE=WINDOW') = 'TRUE' " +
		"         AND c.data_fine_val = TO_DATE ('31-12-9999', 'dd-mm-yyyy') " +
		"         AND c.pkid_stra = s.pkid_stra " +
		"         AND se_row_id = ? ) c_cat_shape " +
		" where c_cat.fk_civico = c_ana.FK_civico  " +
		" and c_cat.FK_ENTE_SORGENTE = '4' and c_cat.PROG_ES = '2' " +
		" and c_ana.FK_ENTE_SORGENTE = '1' and c_ana.PRog_es = '1' " +
		" and v_ana.FK_ENTE_SORGENTE = '1' and v_ana.PRog_es = '1' " +
		" and c_cat.id_dwh =  c_cat_shape.pkid_civi " +
		" and c_ana.fk_via = v_ana.FK_VIA " +
		" and v_ana.id_dwh = via.id " +
		" order by prefisso, nome, civico_1, civico_2 " ;

		Connection cnn = null;

		PreparedStatement pst = null;
		ResultSet rs = null;

		try {

			Context cont = new InitialContext();
			DataSource theDataSource = null;
			CeTUser user = (CeTUser)request.getSession().getAttribute("user");
			EnvUtente eu = new EnvUtente(user, null, null);
			String enteId = eu.getEnte();
			EnvSource es = new EnvSource(enteId);
			theDataSource = (DataSource) cont.lookup(es.getDataSource());
			cnn = theDataSource.getConnection();

			log.info("getListaRedditiCivicoTopo - SQL[" + sql + "]");
			log.info("getListaRedditiCivicoTopo - IdSezione[" + idSezione+"]");
			pst = cnn.prepareStatement(sql);
			pst.setString(1, idSezione);
			rs = pst.executeQuery();
			
			String idFamPrev = null;
			String idFamCurr = null;
			boolean colFam = false;
			
			while (rs.next()) {

				Civico civ = new Civico();
				civ.setSedime(rs.getString("PREFISSO")!=null ? rs.getString("PREFISSO") : "" );
				civ.setStrada(tornaValoreRS(rs, "NOME"));
				
				String civ1 = rs.getString("CIVICO_1");
				String civ2 = rs.getString("CIVICO_2");
				
				civ.setDescrCivico(civ2!=null ? civ1+"/"+civ2 : civ1);
				civ.setChiave(tornaValoreRS(rs, "ID_ANA_CIV"));

				//List<String> listaCiviciAna = this.getListaCiviciAnag(civ.getPk_sequ_civico(), cnn);
				
				List<Anagrafe> residenti = this.getListaResidentiCivico(civ.getChiave(), cnn);
				
				HashMap<String, RedditoTotFamiglia> listaReFam = new HashMap<String, RedditoTotFamiglia>();
				
				for (Anagrafe res : residenti) {
					
					idFamCurr = res.getFamiglie();
					String anno = "2009";
					
					RedditoTotSoggetto reSogg = new RedditoTotSoggetto();
					
					List<RedditoTotDichiarato> redditi = getRedditoSoggetto(res.getId(), anno , cnn, enteId, user.getUsername());
					
					reSogg.setCivico(civ);
					reSogg.setResidente(res);
					reSogg.setRedditi(redditi);
					
					RedditoTotFamiglia reFam = listaReFam.get(idFamCurr);
					if(reFam==null){
						reFam = new RedditoTotFamiglia();
						reFam.setAnno(anno);
						reFam.setCivico(civ);
						reFam.setCodiceFamiglia(idFamCurr);
						List<RedditoTotSoggetto> listSogg = new ArrayList();
						listSogg.add(reSogg);
						reFam.setListaSoggetti(listSogg);
					}else
						reFam.addSoggetto(reSogg);
					
					listaReFam.put(idFamCurr, reFam);
	
				}
				
				for(RedditoTotFamiglia f : listaReFam.values())
					lista.add(f);
				
			}	
			
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				rs.close();
			} catch (Exception ex) {
			}
			try {
				pst.close();
			} catch (Exception ex) {
			}
			try {
				cnn.close();
			} catch (Exception ex) {
			}
		}
		
		return lista;
	}

/*	public List<String> getListaCiviciAnag(String idCivCat, Connection cnn)
			throws Exception {
		ArrayList<String> listaCivici = new ArrayList<String>();

		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String sql = "select c_ana.id_dwh id_ana_civ "
					+ " from  sit_civico_totale c_cat, sit_civico_totale c_ana"
					+ " where c_cat.fk_civico = c_ana.FK_civico "
					+ " and c_cat.FK_ENTE_SORGENTE = '4' and c_cat.PROG_ES = '2'"
					+ " and c_ana.FK_ENTE_SORGENTE = '1' and c_ana.PRog_es = '1'"
					+ " and c_cat.id_dwh =  ? ";

			log.info("getListaCiviciAnag - SQL[" + sql + "]");
			log.info("getListaCiviciAnag - idCivico[" + idCivCat +"]");
			pst = cnn.prepareStatement(sql);
			pst.setString(1, idCivCat);
			rs = pst.executeQuery();

			while (rs.next())
				listaCivici.add(rs.getString("ID_ANA_CIV"));

		} catch (Exception e) {
			throw e;
		} finally {
			try {
				rs.close();
			} catch (Exception ex) {
			}
			try {
				pst.close();
			} catch (Exception ex) {
			}
		}

		return listaCivici;
	}*/

	public List<Anagrafe> getListaResidentiCivico(String idCivAna, Connection cnn) throws Exception {
		ArrayList<Anagrafe> residenti = new ArrayList<Anagrafe>();

		PreparedStatement pst = null;
		ResultSet rs = null;
		List<String> temp = new ArrayList<String>();

		String sql = "select distinct pf.relaz_Par, LTRIM(F.ID_ORIG,'0') codice_famiglia,  p.* , TO_CHAR(p.data_nascita,'dd/MM/yyyy') data_nascita_dt"
				+ " from Sit_D_Persona p, Sit_D_Persona_Civico pc, Sit_D_Civico c, Sit_D_Pers_Fam pf, Sit_d_famiglia f "
				+ " where c.id_Ext=pc.id_Ext_D_Civico  "
				+ " and p.id_Ext = pc.id_Ext_D_Persona  "
				+ " and pf.id_Ext_D_Persona=p.id_Ext  "
				+ " and f.id_ext = PF.ID_EXT_D_FAMIGLIA  "
				+ " and c.id = ? "
				+ " and p.posiz_ana IN ('A', 'ISCRITTO NELL''A.P.R.') "
				+ " and (p.dt_Fine_Val is null or p.dt_Fine_Val >= sysdate)  "
				+ " and (c.dt_Fine_Val is null or c.dt_Fine_Val >= sysdate)  "
				+ " and (pf.dt_Fine_Val is null or pf.dt_Fine_Val >= sysdate)  "
				+ " and (pc.dt_Fine_Val is null or pc.dt_Fine_Val >= sysdate)  "
				+ " AND sysdate BETWEEN  NVL(p.data_Nascita,TO_DATE('01/01/1000','DD/MM/YYYY'))  AND NVL(p.data_Mor,sysdate)  "
				+ " AND sysdate BETWEEN  NVL(p.dt_Inizio_Dato,TO_DATE('01/01/1000','DD/MM/YYYY')) AND NVL(p.dt_Fine_Dato,sysdate)  "
				+ " AND sysdate BETWEEN  NVL(p.data_Imm,TO_DATE('01/01/1000','DD/MM/YYYY'))      AND NVL(p.data_Emi,sysdate)  "
				+ " AND SYSDATE BETWEEN NVL (pf.dt_inizio_dato,TO_DATE ('01/01/1000', 'DD/MM/YYYY')) AND NVL (pf.dt_fine_dato, SYSDATE) "
				+ " order by TO_NUMBER(codice_famiglia), p.cognome, p.nome";

		log.info("getListaResidentiCivCT - SQL[" + sql + "]");
		
		try {
			pst = cnn.prepareStatement(sql);

			log.info("getListaResidentiCivCT - IdCivico["+ idCivAna +"]");
			pst.setString(1, idCivAna);
			rs = pst.executeQuery();
			while (rs.next()) {
				Anagrafe sogg = new Anagrafe();
				sogg.setId(rs.getString("ID"));
				sogg.setFamiglie(rs.getString("codice_famiglia"));
				sogg.setCodFiscale(rs.getString("CODFISC"));
				sogg.setCognome(rs.getString("COGNOME"));
				sogg.setNome(rs.getString("NOME"));
				sogg.setDataNascita(this.tornaValoreRS(rs, "DATA_NASCITA_DT"));

				// Lo conservo se il soggetto non è stato già inserito
				if (!temp.contains(sogg.getId())) {
					residenti.add(sogg);
					temp.add(sogg.getId());
				}

			}

		} catch (Exception e) {
			throw e;
		} finally {
			try {
				rs.close();
			} catch (Exception ex) {
			}
			try {
				pst.close();
			} catch (Exception ex) {
			}
		}

		return residenti;
	}

	public List<RedditoTotDichiarato> getRedditoSoggetto(String idAnaSogg, String anno, Connection cnn, String ente, String user) throws Exception {
		 
		List<RedditoTotDichiarato> redditi = new ArrayList<RedditoTotDichiarato>();
		RedditiService redditiService= (RedditiService)getEjb("CT_Service", "CT_Service_Data_Access", "RedditiServiceBean");
		
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			String sql = "select distinct S_REDD.id_dwh, s_ana.id_dwh,  S_ANA.COGNOME, S_ANA.NOME, S_ANA.CODFISC, s_redd.field1 anno " +
					"from  sit_soggetto_totale s_redd, sit_soggetto_totale s_ana " +
					"where S_REDD.FK_SOGGETTO = S_ANA.FK_SOGGETTO  " +
					"and S_REDD.FK_ENTE_SORGENTE = '11' and S_REDD.PROG_ES = '1' " +
					"and S_ANA.FK_ENTE_SORGENTE = '1' and S_ANA.PRog_es = '1' " +
					"and s_redd.field1 = ? " +
					"and S_ANA.ID_DWH = ? " +
					"order by anno desc";

			log.info("getListaRedditi - SQL[" + sql + "]");
			log.info("getListaRedditi - Anno[" + anno+"]");
			log.info("getListaRedditi - IdAnaSoggetto[" + idAnaSogg+"]");
			pst = cnn.prepareStatement(sql);
			pst.setString(1, anno);
			pst.setString(2, idAnaSogg);
			rs = pst.executeQuery();
			while (rs.next()) {
				RedditoTotDichiarato reDich = new RedditoTotDichiarato();
				
				String id_redd_tot = rs.getString("ID_DWH");
				String[] id_r = id_redd_tot.split("\\|");

				KeySoggDTO key = new KeySoggDTO();
				key.setIdeTelematico(id_r[0]);
				key.setCodFis(id_r[1]);
				key.setAnno(rs.getString("ANNO"));
				key.setEnteId(ente);
				key.setUserId(user);
				
				reDich.setIdeTelematico(key.getIdeTelematico());
				reDich.setCodFiscale(key.getCodFis());
				reDich.setAnno(key.getAnno());
				
				List<RedditiDicDTO> redditiDic =  redditiService.getRedditiByKeyAnno(key);
				
				if(redditiDic.size()>0)
					reDich.setCodModello(redditiDic.get(0).getCodModello());
				
				reDich.setImportoComplessivo(this.getRedditoComplessivo(redditiDic));
				
				redditi.add(reDich);
		
			}

		} catch (Exception e) {
			throw e;
		} finally {
			try {
				rs.close();
			} catch (Exception ex) {
			}
			try {
				pst.close();
			} catch (Exception ex) {
			}
		}

		return redditi;
	}
	
	public double getValoreContabileNum(String valore) {
		try {
			if (valore != null && !valore.trim().equals("")) {
				return Double.parseDouble(valore.trim());
			}			
		}catch (Exception e) {}
		return 0;
	}
	
	
	private Double getRedditoComplessivo(List<RedditiDicDTO> redditiDic){
		
		Double importo = 0d;
		//Calcola la somma dei redditi del soggetto in base al codice del modello e ai quadri di interesse
		for(RedditiDicDTO dto : redditiDic){
			String codiceQuadro = dto.getId().getCodiceQuadro().substring(0,5);
			String codiceModello = dto.getCodModello();
			
			List<String> quadri = (List<String>) tipoModello.get(codiceModello);
			
			log.info("getRedditoComplessivo " +codiceModello + " " + codiceQuadro);
			
			if(codiceModello.equalsIgnoreCase("S"))
				codiceQuadro = dto.getId().getCodiceQuadro();
			
			if(quadri!=null && quadri.contains(codiceQuadro)){
			String val = dto.getValoreContabile();
			log.info("valore["+val+"]");
			
			if(val.startsWith("-"))
				importo -= getValoreContabileNum(val);
			else
				importo += getValoreContabileNum(val);
			}
			
			
		}
		
		log.info("totale["+importo+"]");
		
		return importo;
		
	}
	

}
