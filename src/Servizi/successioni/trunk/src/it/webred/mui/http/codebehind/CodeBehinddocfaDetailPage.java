package it.webred.mui.http.codebehind;

import it.webred.docfa.model.Docfa;
import it.webred.docfa.model.DocfaUIUTitolareBean;
import it.webred.mui.http.AbstractPage;
import it.webred.mui.http.MuiApplication;
import it.webred.mui.http.MuiBaseServlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;




public class CodeBehinddocfaDetailPage extends AbstractPage {

	public void init() {
		//MuiApplication.getMuiApplication().add(R0602_VARNAME,this);
	}

	@Override
	protected boolean controlImpl(HttpServletRequest req,
			HttpServletResponse resp, MuiBaseServlet servlet)
			throws ServletException, IOException {
		String chiaveDocfa = req.getParameter("docfaKey");
		storeDocfa(req, chiaveDocfa);
	    req.setAttribute("displayedIciLink",new HashMap());
		return true;
	}

	public static void storeDocfa(HttpServletRequest req, String chiaveDocfa) 
	{
		Hashtable ht = new Hashtable();
		// faccio la connessione al db
		Connection conn = null;
		try
		{
			String  fornituraStr = chiaveDocfa.substring(0,chiaveDocfa.indexOf("-"));
			String fornitura = fornituraStr.substring(6, 10)+ fornituraStr.substring(3, 5)+fornituraStr.substring(0, 2); 
			String protocollo = chiaveDocfa.substring(chiaveDocfa.indexOf("-")+1);
			
			Context cont = new InitialContext();
			Context datasourceContext = (Context) cont.lookup("java:comp/env");
			DataSource theDataSource = (DataSource) datasourceContext.lookup("jdbc/mui");
			conn = theDataSource.getConnection();
			

				
			//DOCFA_DATI_GENERALI
			String sql = "SELECT D_GEN.PROTOCOLLO_REG          AS PROTOCOLLO,"+
						"        TO_CHAR(D_GEN.DATA_VARIAZIONE,'YYYY-MM-DD')         AS DATA_VARIAZIONE,"+
						"        D_GEN.FORNITURA         	   AS FORNITURA,"+
						"	     CAU.CAU_DES                   AS CAUSALE"+
						"  FROM  DOCFA_CAUSALI CAU,     "+
						"        DOCFA_DATI_GENERALI  D_GEN"+
						"  WHERE D_GEN.CAUSALE_NOTA_VAX = CAU.CAU_COD(+)"+
						"	 AND D_GEN.PROTOCOLLO_REG = ?"+
						"	 AND D_GEN.FORNITURA =  TO_DATE(?,'YYYYMMDD')";
			PreparedStatement pstm = conn.prepareStatement(sql);
			pstm.setString(1, protocollo);
			pstm.setString(2, fornitura);
			ResultSet rs = pstm.executeQuery();
			Docfa docfa = new Docfa();
			if (rs.next())
			{ 
				docfa.setProtocollo(tornaValoreRS(rs,"PROTOCOLLO"));  
				docfa.setDataVariazione(tornaValoreRS(rs,"DATA_VARIAZIONE","YYYY-MM-DD"));
				docfa.setCausale(tornaValoreRS(rs,"CAUSALE"));
				docfa.setFornitura(tornaValoreRS(rs,"FORNITURA","YYYY-MM-DD"));
			}
			rs.close();
			pstm.close();
			pstm = conn.prepareStatement("select proveninza, COUNT(*) AS tot from docfa_data_log where  ID_PROTOCOLLO = ? "+
					" AND id_fornitura = TO_CHAR(TO_DATE(?,'YYYYMMDD'),'dd/mm/yyyy') GROUP BY proveninza");
			pstm.setString(1, protocollo);
			pstm.setString(2, fornitura);
			ResultSet rsLog = pstm.executeQuery();
			while(rsLog.next())
			{
				if(rsLog.getString("proveninza") != null && rsLog.getString("proveninza").trim().equalsIgnoreCase("imp"))
					docfa.setImportErrorCount(rsLog.getInt("tot"));
				else if(rsLog.getString("proveninza") != null && rsLog.getString("proveninza").trim().equalsIgnoreCase("int"))
					docfa.setIntegrazioneCount(rsLog.getInt("tot"));				
			}
			rsLog.close();
			pstm.close();
			sql = "SELECT   D_GEN.U_DERIV_DEST_ORDINARIA  AS DERIV_ORD,"+
				"       D_GEN.U_DERIV_DEST_SPECIALE   AS DERIV_SPE,"+
				"       UIU_IN_SOPPRESSIONE           AS SOPPRESSIONE,  "+ 
				"       UIU_IN_VARIAZIONE             AS VARIAZIONE,"+
				"       UIU_IN_COSTITUZIONE           AS COSTITUZIONE"+
				"  FROM  DOCFA_CAUSALI        CAU,     "+
				"        DOCFA_DATI_GENERALI  D_GEN"+
				"  WHERE D_GEN.CAUSALE_NOTA_VAX = CAU.CAU_COD(+)"+
				"	 AND D_GEN.PROTOCOLLO_REG = ? "+
				"	 AND D_GEN.FORNITURA =  TO_DATE(?,'YYYYMMDD')";			
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, protocollo);
			pstm.setString(2, fornitura);
			rs = pstm.executeQuery();
			if (rs.next())
			{
				docfa.setSoppressione(tornaValoreRS(rs,"SOPPRESSIONE"));
				docfa.setVariazione(tornaValoreRS(rs,"VARIAZIONE"));
				docfa.setCostituzione(tornaValoreRS(rs,"COSTITUZIONE"));
				docfa.setDerivSpe(tornaValoreRS(rs,"DERIV_SPE"));
			}
			rs.close();
			pstm.close();
//			ht.put(DOCFA, docfa);
			
			// DOCFA_INTESTATI
			sql ="SELECT DECODE(INTE.DENOMINAZIONE,NULL,INTE.COGNOME || ' ' || INTE.NOME,INTE.DENOMINAZIONE) AS INTESTATI, "+
				"       INTE.CODICE_FISCALE         AS CF,"+
				"       INTE.PARTITA_IVA            AS PI"+
				" 	   FROM DOCFA_INTESTATI      INTE"+
				"	   WHERE "+
				"	   INTE.PROTOCOLLO_REG = ? "+
				"	   AND INTE.FORNITURA =  TO_DATE(?,'YYYYMMDD') ";
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, protocollo);
			pstm.setString(2, fornitura);
			rs = pstm.executeQuery();
			ArrayList listaDocfaIntestatari = new ArrayList();
			while (rs.next())
			{
				Docfa locaList = new Docfa();
				locaList.setDenominazione(tornaValoreRS(rs,"INTESTATI"));
				locaList.setCodiceFiscale(tornaValoreRS(rs,"CF"));
				locaList.setPartitaIva(tornaValoreRS(rs,"PI"));
				listaDocfaIntestatari.add(locaList);
			}
			rs.close();
			pstm.close();
//			ht.put(LISTA_DETTAGLIO_DOCFA_INTESTATI, listaDocfaIntestatari);	
			
			//DOCFA_UIU
			sql ="SELECT DISTINCT TIPO_OPERAZIONE    AS TIPO,  "+
				"	   FOGLIO             AS FOGLIO,"+
				"	   NUMERO             AS NUMERO,"+
				"	   SUBALTERNO         AS SUB,"+
				"	   TRIM(INDIR_TOPONIMO) || ' ' || INDIR_NCIV_UNO     AS INDIRIZZO, "+
				"	   PROP_CATEGORIA,PROP_CLASSE,PROP_RENDITA_EURO_CATA,PROP_SUPERFICIE_CATA,PROP_ZONA_CENSUARIA " +
				"	   FROM DOCFA_UIU U"+
				"	   WHERE"+
				"	   U.PROTOCOLLO_REG = ?"+
				"	   AND U.FORNITURA =  TO_DATE(?,'YYYYMMDD') ";
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, protocollo);
			pstm.setString(2, fornitura);
			rs = pstm.executeQuery();
			ArrayList listaDocfaUiu = new ArrayList();
			while (rs.next())
			{
				Docfa locaList = new Docfa();
				locaList.setTipo(tornaValoreRS(rs,"TIPO"));
				locaList.setFoglio(tornaValoreRS(rs,"FOGLIO"));
				locaList.setParticella(tornaValoreRS(rs,"NUMERO"));
				locaList.setSubalterno(tornaValoreRS(rs,"SUB"));
				locaList.setIndirizzoDichiarante(tornaValoreRS(rs,"INDIRIZZO"));
				locaList.setCodCategoriaDocfaProp(tornaValoreRS(rs, "PROP_CATEGORIA"));
				locaList.setClasseDocfaProp(tornaValoreRS(rs, "PROP_CLASSE"));
				locaList.setRenditaDocfaProp(tornaValoreRS(rs, "PROP_RENDITA_EURO_CATA"));
				locaList.setSuperficieDocfaProp(tornaValoreRS(rs, "PROP_SUPERFICIE_CATA"));
				locaList.setZonaDocfaProp(tornaValoreRS(rs, "PROP_ZONA_CENSUARIA"));
				ArrayList listGraf = (ArrayList)mDatiGraffati(conn,tornaValoreRS(rs,"FOGLIO"), tornaValoreRS(rs,"NUMERO"), tornaValoreRS(rs,"SUB"),MuiApplication.belfiore);
				if (listGraf != null)
					locaList.setPresenzaGraffati("S");
				else
					locaList.setPresenzaGraffati("-");
				
				//cerco indirizzo a catasto per UIU cessate o variate
				//solo per docfa con uiuFoglio numerici sennò esplode!!!!
				String SQL_INDIRIZZO_PART_CAT = "SELECT DISTINCT s.prefisso || ' ' || s.nome AS nome, s.numero as codice_via, " +
												 " c.civico " +
												 "FROM sitiuiu p, siticivi_uiu cu, siticivi c, sitidstr s " +
												 "WHERE p.foglio = ? " +
												 "AND p.particella = ? " +
												 "AND p.unimm = ? " +
												 "AND cu.pkid_uiu = p.pkid_uiu " +
												 "AND c.pkid_civi = cu.pkid_civi " +
												 "AND s.pkid_stra = c.pkid_stra";
												
				ArrayList indCat = new ArrayList(); 
				try
				{
					Integer.parseInt(locaList.getFoglio());
				
					if (!locaList.getTipo().equals("C"))
					{	
						PreparedStatement pstcat= conn.prepareStatement(SQL_INDIRIZZO_PART_CAT);

						pstcat.setInt(1, Integer.parseInt(locaList.getFoglio()));
						pstcat.setString(2, locaList.getParticella());
						
						//controllo se il SUB è rappresentato da spazi vuoti (DOCFA_UIU) --> lo imposto a 0 (sitiuiu non a unimm a vuoto ma solo a 0!)
						//sennò esplode la query!!!
						if(locaList.getSubalterno().trim().equals(""))
							locaList.setSubalterno("0");
						
						pstcat.setInt(3, Integer.parseInt(locaList.getSubalterno()));
						
						ResultSet rscat = pstcat.executeQuery();
						
						while (rscat.next())
						{
							String appo = tornaValoreRS(rscat,"NOME")+" "+tornaValoreRS(rscat,"CIVICO");
							if (appo != null && !appo.trim().equals("")) 
								indCat.add(appo);
							
						}
						rscat.close();
						pstcat.close();
						
					}
					else
					{
						indCat.add(locaList.getIndirizzoDichiarante());
					}
				
				}catch (NumberFormatException e)
				{
					e.printStackTrace();
					
				}
				locaList.setIndPart(indCat);
				
				//info titolari FPS
				
				String sqlUIUTitoS = "SELECT distinct cons_sogg_tab.flag_pers_fisica," + 
				"DECODE (cons_sogg_tab.ragi_soci,NULL, '-',cons_sogg_tab.ragi_soci) AS denominazione," + 
				"DECODE (cons_sogg_tab.nome,NULL, '-',cons_sogg_tab.nome) AS nome," + 
				"DECODE(siticomu.codi_fisc_luna ,NULL, '-',siticomu.codi_fisc_luna)AS fk_comune," + 
				"DECODE (siticomu.nome, NULL, '-', siticomu.nome) AS sede,"
						+ "DECODE (cons_sogg_tab.codi_piva,NULL, '-',cons_sogg_tab.codi_piva) AS partita_iva," + 
						"DECODE (cons_sogg_tab.CODI_FISC ,NULL, '-',cons_sogg_tab.CODI_FISC) AS codi_fisc," + 
						"DECODE (cons_sogg_tab.pk_cuaa, NULL, '-', cons_sogg_tab.pk_cuaa) AS pk_cuaa," + 
						"NVL (TO_CHAR (cons_csui_tab.data_inizio, 'dd/mm/yyyy'),'-') AS data_inizio,"
						+ "NVL (TO_CHAR (cons_csui_tab.data_fine, 'dd/mm/yyyy'),'-') AS data_fine," + 
						"DECODE (cons_csui_tab.perc_poss,NULL, '-',cons_csui_tab.perc_poss) AS QUOTA," + 
						"DECODE (cons_sogg_tab.sesso,NULL, '-',cons_sogg_tab.sesso) AS sesso," + 
						"NVL (TO_CHAR (cons_sogg_tab.data_nasc, 'dd/mm/yyyy'),'-') AS data_nasc," + 
						"DECODE (cons_sogg_tab.comu_nasc,NULL, '-',cons_sogg_tab.comu_nasc) AS comune_nascita " + 
						"FROM cons_csui_tab, cons_sogg_tab, siticomu " + 
						"WHERE siticomu.codi_fisc_luna = '"+MuiApplication.belfiore+"' " + 
						"AND siticomu.cod_nazionale = cons_csui_tab.cod_nazionale " + 
						"AND cons_csui_tab.foglio = ? " + 
						"AND cons_csui_tab.particella = ? " + 
						"AND DECODE (cons_csui_tab.sub,' ', '-',NVL (cons_csui_tab.sub, '*'), '-',cons_csui_tab.sub) = DECODE (NVL (' ', '*'), '*', '-', ' ', '-', ' ') " + 
						"AND cons_csui_tab.unimm = ? " + "AND ( cons_csui_tab.data_inizio <= TO_DATE (?, 'dd/mm/yyyy') " + 
						"	 AND cons_csui_tab.data_fine >= TO_DATE (?, 'dd/mm/yyyy')) " + 
						"AND ( cons_sogg_tab.data_inizio <= TO_DATE (?, 'dd/mm/yyyy') " + 
						"    AND cons_sogg_tab.data_fine >= TO_DATE (?, 'dd/mm/yyyy'))	" + 
						"AND cons_csui_tab.pk_cuaa = cons_sogg_tab.pk_cuaa " + 
						"AND SUBSTR (cons_sogg_tab.comu_nasc, 1, 3) = siticomu.istatp(+) " + 
						"AND SUBSTR (cons_sogg_tab.comu_nasc, 4, 3) = siticomu.istatc(+) ";

				String sqlUIUTitoC = "SELECT distinct cons_sogg_tab.flag_pers_fisica," + 
				"DECODE (cons_sogg_tab.ragi_soci,NULL, '-',cons_sogg_tab.ragi_soci) AS denominazione," + 
				"DECODE (cons_sogg_tab.nome,NULL, '-',cons_sogg_tab.nome) AS nome," + 
				"DECODE(siticomu.codi_fisc_luna ,NULL, '-',siticomu.codi_fisc_luna)AS fk_comune," + 
				"DECODE (siticomu.nome, NULL, '-', siticomu.nome) AS sede," + 
				"DECODE (cons_sogg_tab.codi_piva,NULL, '-',cons_sogg_tab.codi_piva) AS partita_iva," + 
				"DECODE (cons_sogg_tab.CODI_FISC ,NULL, '-',cons_sogg_tab.CODI_FISC) AS codi_fisc," + 
				"DECODE (cons_sogg_tab.pk_cuaa, NULL, '-', cons_sogg_tab.pk_cuaa) AS pk_cuaa," + 
				"NVL (TO_CHAR (cons_csui_tab.data_inizio, 'dd/mm/yyyy'),'-') AS data_inizio," + 
				"NVL (TO_CHAR (cons_csui_tab.data_fine, 'dd/mm/yyyy'),'-') AS data_fine," + 
				"DECODE (cons_csui_tab.perc_poss,NULL, '-',cons_csui_tab.perc_poss) AS QUOTA," + 
				"DECODE (cons_sogg_tab.sesso,NULL, '-',cons_sogg_tab.sesso) AS sesso," + 
				"NVL (TO_CHAR (cons_sogg_tab.data_nasc, 'dd/mm/yyyy'),'-') AS data_nasc," + 
				"DECODE (cons_sogg_tab.comu_nasc,NULL, '-',cons_sogg_tab.comu_nasc) AS comune_nascita " + 
				"FROM cons_csui_tab, cons_sogg_tab, siticomu " + 
				"WHERE siticomu.codi_fisc_luna = '"+MuiApplication.belfiore+"' " + 
				"AND siticomu.cod_nazionale = cons_csui_tab.cod_nazionale " + 				
				"AND cons_csui_tab.foglio = ? " + "AND cons_csui_tab.particella = ? " + 
				"AND DECODE (cons_csui_tab.sub,' ', '-',NVL (cons_csui_tab.sub, '*'), '-',cons_csui_tab.sub) = DECODE (NVL (' ', '*'), '*', '-', ' ', '-', ' ') " + 
				"AND cons_csui_tab.unimm = ? " + "AND cons_csui_tab.data_inizio >= TO_DATE (?, 'dd/mm/yyyy') " +
						// "AND cons_sogg_tab.data_inizio >= TO_DATE (?, 'dd/mm/yyyy') " +
				"AND ( cons_sogg_tab.data_inizio <= TO_DATE (?, 'dd/mm/yyyy') " + 
				"    AND cons_sogg_tab.data_fine >= TO_DATE (?, 'dd/mm/yyyy'))	" + 
				"AND cons_csui_tab.pk_cuaa = cons_sogg_tab.pk_cuaa " + 
				"AND SUBSTR (cons_sogg_tab.comu_nasc, 1, 3) = siticomu.istatp(+) " + 
				"AND SUBSTR (cons_sogg_tab.comu_nasc, 4, 3) = siticomu.istatc(+) ";

				PreparedStatement pstsUIUTito = null;
				if (locaList.getTipo().equals("S") || locaList.getTipo().equals("V")){
					pstsUIUTito = conn.prepareStatement(sqlUIUTitoS);
				}
				if (locaList.getTipo().equals("C")){
					pstsUIUTito = conn.prepareStatement(sqlUIUTitoC);
				}
				
				pstsUIUTito.setString(1, locaList.getFoglio());
				pstsUIUTito.setString(2, locaList.getParticella());
				pstsUIUTito.setString(3, locaList.getSubalterno());
				pstsUIUTito.setString(4, fornituraStr);
				pstsUIUTito.setString(5, fornituraStr);
				pstsUIUTito.setString(6, fornituraStr);
				if (locaList.getTipo().equals("S") || locaList.getTipo().equals("V")){
					pstsUIUTito.setString(7, fornituraStr);
				}
				
				ResultSet rsUIUTito = pstsUIUTito.executeQuery();
				
				ArrayList elencoTitolari = new ArrayList();
				while (rsUIUTito.next())
				{
					//memorizzo le info dei Titolari
					DocfaUIUTitolareBean tito = new DocfaUIUTitolareBean();
					tito.setDenominazione(rsUIUTito.getString("denominazione"));
					tito.setNome(rsUIUTito.getString("nome"));
					tito.setDataNascita(rsUIUTito.getString("data_nasc"));
					tito.setComuneNascita(rsUIUTito.getString("comune_nascita"));
					tito.setFlagPersona(rsUIUTito.getString("flag_pers_fisica"));
					tito.setCodiceFiscale(rsUIUTito.getString("codi_fisc"));
					tito.setPartitaIva(rsUIUTito.getString("partita_iva"));
					tito.setSesso(rsUIUTito.getString("sesso"));
					tito.setDescrComuneNascita(rsUIUTito.getString("sede"));
					
					if (tito.getCodiceFiscale()!= null && tito.getCodiceFiscale().length() == 16)
					{
						//recupero residenza del titolare da Anagrafe
						String sqlUIUTitoResAna = "SELECT DISTINCT p.id_orig matricola, p.codfisc codice_fisc, p.cognome, p.nome," + 
						" p.sesso," + " cxml.ID_ORIG_VIA cod_via,cxml.viasedime || ' ' || cxml.DESCRIZIONE  descr_via, " + 
						" cxml.civ_liv1 AS numero_civ, cxml.civ_liv2 esp_civ, TO_CHAR (pc.dt_inizio_dato, 'DD/MM/YYYY') data_inizio_res," + 
						" TO_CHAR (pc.dt_fine_dato, 'DD/MM/YYYY') data_fine_res " + 
						" FROM " + " sit_d_persona_civico pc," + 
						" sit_d_persona p," + 
						" sit_d_pers_fam f,SIT_D_CIVICO_VIA_V cxml" + 
						" WHERE pc.id_ext_d_civico = cxml.id_ext" + 
						" AND p.id_ext = pc.id_ext_d_persona(+)" + 
						" AND p.id_ext = f.id_ext_d_persona(+)" + 
						" AND p.codfisc = ?";
						PreparedStatement pstsUIUTitoRes = conn.prepareStatement(sqlUIUTitoResAna);
						pstsUIUTitoRes.setString(1, tito.getCodiceFiscale());
						
						
						ResultSet rsUIUTitoRes = pstsUIUTitoRes.executeQuery();
						if (rsUIUTitoRes.next())
						{
							tito.setIndirizzoResidenza(rsUIUTitoRes.getString("descr_via"));
							tito.setCivicoResidenza(rsUIUTitoRes.getString("NUMERO_CIV")+" "+rsUIUTitoRes.getString("ESP_CIV"));

							tito.setComuneResidenza(MuiApplication.descComune);
							tito.setFlagProvenienzaInfo("A"); // dati trovati in Anagrafe
						}
						else
						{
							//cerco nei tributi
							String sqlUIUTitoResTributi = "select cognome,nome,sesso,data_nascita,descr_comune_nascita," +
											"descr_indirizzo,comune_residenza " +
											"from tri_contribuenti " +
											"where codice_fiscale = ?";
							PreparedStatement pstsUIUTitoResTri = conn.prepareStatement(sqlUIUTitoResTributi);
							pstsUIUTitoResTri.setString(1, tito.getCodiceFiscale());
							
							ResultSet rsUIUTitoResTri = pstsUIUTitoResTri.executeQuery();
							if (rsUIUTitoResTri.next())
							{
								tito.setIndirizzoResidenza(rsUIUTitoResTri.getString("descr_indirizzo"));
								tito.setComuneResidenza(rsUIUTitoResTri.getString("comune_residenza"));
								tito.setFlagProvenienzaInfo("T"); // dati trovati in Tributi
							}
							rsUIUTitoResTri.close();
							pstsUIUTitoResTri.close();
							
						}
						rsUIUTitoRes.close();
						pstsUIUTitoRes.close();							
					}
					else
					{
						//cerco dati giuridici in tributi
						String sqlUIUTitoGTributi = "select descr_indirizzo,comune_residenza " + 
											"from tri_contribuenti " +
											"where partita_iva = ? " +
											"order by provenienza desc";
						PreparedStatement pstsUIUTitoGTri = conn.prepareStatement(sqlUIUTitoGTributi);
						if (tito.getCodiceFiscale()!= null && tito.getCodiceFiscale().length() == 11)
							pstsUIUTitoGTri.setString(1, tito.getCodiceFiscale());
						else if (tito.getPartitaIva()!= null && tito.getPartitaIva().length() == 11)
							pstsUIUTitoGTri.setString(1, tito.getPartitaIva());
						else
							pstsUIUTitoGTri.setString(1, "");
						
						ResultSet rsUIUTitoGTri = pstsUIUTitoGTri.executeQuery();
						if (rsUIUTitoGTri.next())
						{
							tito.setIndirizzoResidenza(rsUIUTitoGTri.getString("descr_indirizzo"));
							tito.setComuneResidenza(rsUIUTitoGTri.getString("comune_residenza"));
							tito.setFlagProvenienzaInfo("T"); // dati trovati in Tributi
						}
						rsUIUTitoGTri.close();
						pstsUIUTitoGTri.close();
						
						//cerco dati del rappresentante legale
						String sqlUIURLTitoG = "SELECT DENOMINAZIONE," +
							"CAP_DOMICILIO_FISC," +
							"COD_DOMICILIO_FISC," +
							"IND_CIV_DOMICILIO_FISC," +
							"COMUNE_DOMILICIO_FISC," +
							"PROVIN_DOMICILIO_FISC," +
							"COD_FISC_RAP_LEG_PFIS," +
							"COGNOME_RAP_LEG_PFIS," +
							"NOME_RAP_LEG_PFIS," +
							"IND_CIV_RAP_LEG_PFIS," +
							"CAP_RAP_LEG_PFIS," +
							"COMUNE_RES_RAP_LEG_PFIS," +
							"PROVIN_RES_RAP_LEG " +
							"FROM MI_SIATEL_P_GIU " +
							"WHERE COD_FISC_INDIVIDUATO = ?";
						PreparedStatement pstsUIUTitoGRL = conn.prepareStatement(sqlUIURLTitoG);
						if (tito.getCodiceFiscale()!= null && tito.getCodiceFiscale().length() == 11)
							pstsUIUTitoGRL.setString(1, tito.getCodiceFiscale());
						else if (tito.getPartitaIva()!= null && tito.getPartitaIva().length() == 11)
							pstsUIUTitoGRL.setString(1, tito.getPartitaIva());
						else
							pstsUIUTitoGRL.setString(1, "");
						
						ResultSet rsUIUTitoGRL = pstsUIUTitoGRL.executeQuery();
						if (rsUIUTitoGRL.next())
						{
							tito.setCodiceFiscaleRL(rsUIUTitoGRL.getString("COD_FISC_RAP_LEG_PFIS"));
							tito.setDenominazioneRL(rsUIUTitoGRL.getString("COGNOME_RAP_LEG_PFIS"));
							tito.setNomeRL(rsUIUTitoGRL.getString("NOME_RAP_LEG_PFIS"));
							tito.setIndirizzoResidenzaRL(rsUIUTitoGRL.getString("IND_CIV_RAP_LEG_PFIS"));
							tito.setComuneResidenzaRL(rsUIUTitoGRL.getString("COMUNE_RES_RAP_LEG_PFIS"));
							tito.setCapResidenzaRL(rsUIUTitoGRL.getString("CAP_RAP_LEG_PFIS"));
							tito.setProvinciaResidenzaRL(rsUIUTitoGRL.getString("PROVIN_RES_RAP_LEG"));
						}
						rsUIUTitoGRL.close();
						pstsUIUTitoGRL.close();
						
					}
					
					tito.setPercentualePossesso(rsUIUTito.getString("QUOTA"));
					tito.setTipoPossesso("P");
					elencoTitolari.add(tito);
					
				}
				
				rsUIUTito.close();
				pstsUIUTito.close();
				locaList.setElencoTitolari(elencoTitolari);
				
				listaDocfaUiu.add(locaList);
			}
			rs.close();
			pstm.close();
//			ht.put(LISTA_DETTAGLIO_DOCFA_UIU, listaDocfaUiu);
				
			//DOCFA_BENI_NON_CENS  
			sql ="SELECT FOGLIO_01          AS FOG_01,"+
				"	   NUMERO_01          AS NUM_01,"+
				"	   SUBALTERNO_01      AS SUB_01,"+
				"	   FOGLIO_02          AS FOG_02,"+
				"	   NUMERO_02          AS NUM_02,"+
				"	   SUBALTERNO_02      AS SUB_02,"+
				"	   FOGLIO_03          AS FOG_03,"+
				"	   NUMERO_03          AS NUM_03,"+
				"	   SUBALTERNO_03      AS SUB_03"+
				"	   FROM DOCFA_BENI_NON_CENS  BNC  "+
				"	   WHERE 	BNC.PROTOCOLLO_REG = ?"+
				"	   AND BNC.FORNITURA =  TO_DATE(?,'YYYYMMDD')";
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, protocollo);
			pstm.setString(2, fornitura);
			rs = pstm.executeQuery();
			ArrayList listaDocfaBeniNonCens = new ArrayList();
			while (rs.next())
			{
				Docfa locaList = new Docfa();
				locaList.setFoglio(tornaValoreRS(rs,"FOG_01"));
				locaList.setParticella(tornaValoreRS(rs,"NUM_01"));
				locaList.setSubalterno(tornaValoreRS(rs,"SUB_01"));
				locaList.setFoglio2(tornaValoreRS(rs,"FOG_02"));
				locaList.setParticella2(tornaValoreRS(rs,"NUM_02"));
				locaList.setSubalterno2(tornaValoreRS(rs,"SUB_02"));
				locaList.setFoglio3(tornaValoreRS(rs,"FOG_03"));
				locaList.setParticella3(tornaValoreRS(rs,"NUM_03"));
				locaList.setSubalterno3(tornaValoreRS(rs,"SUB_03"));				
				listaDocfaBeniNonCens.add(locaList);
			}
			rs.close();
			pstm.close();
//			ht.put(LISTA_DETTAGLIO_DOCFA_BENI_NON_CENS, listaDocfaBeniNonCens);			
				
			// DOCFA_ANOTAZIONI
			sql ="SELECT ANNOTAZIONI  	 "+ 
				"	   FROM DOCFA_ANNOTAZIONI    ANN    "+
				"	   WHERE ANN.PROTOCOLLO_REG = ?"+
				"	   AND ANN.FORNITURA =  TO_DATE(?,'YYYYMMDD')"+
				"	   ORDER BY PROG_TRASC ";
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, protocollo);
			pstm.setString(2, fornitura);
			rs = pstm.executeQuery();
			ArrayList listaDocfaAnnotazioni = new ArrayList();
			while (rs.next())
			{
				Docfa locaList = new Docfa();
				locaList.setAnnotazioni(tornaValoreRS(rs,"ANNOTAZIONI"));
				listaDocfaAnnotazioni.add(locaList);
			}
			rs.close();
			pstm.close();
//			ht.put(LISTA_DETTAGLIO_DOCFA_ANNOTAZIONI, listaDocfaAnnotazioni);	
			
			//DOCFA_DATI_CENSUARI
			sql ="SELECT DISTINCT CEN.FOGLIO      FOG,"+
				"	   CEN.NUMERO      NUM,"+
				"	   CEN.SUBALTERNO  SUB,"+
				"	   CEN.ZONA        ZONA,"+
				"	   CEN.CLASSE      CLS,"+
				"	   CEN.CATEGORIA   CAT,"+
				"	   CEN.CONSISTENZA CONS,"+
				"	   CEN.SUPERFICIE  SUP,"+
				"	   CEN.RENDITA_EURO RENDITA_EU,"+
				"	   CEN.DATA_REGISTRAZIONE DATA_REGISTRAZIONE,"+
				"	   CEN.identificativo_immobile identificativo_immobile, " +
				"	   NVL(cen.presenza_graffati,'-') presenza_graffati "+
				"	   FROM  DOCFA_DATI_CENSUARI	CEN,DOCFA_DATI_METRICI	MET "+
				"	   WHERE CEN.PROTOCOLLO_REGISTRAZIONE = ? "+
				"		 AND CEN.FORNITURA =  TO_DATE(?,'YYYYMMDD')"+
				//nuova da millucci
				"  AND MET.identificativo_immobile(+) = CEN.identificativo_immobile";
			
			
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, protocollo);
			pstm.setString(2, fornitura);
			rs = pstm.executeQuery();
			ArrayList listaDocfaDatiCensuari = new ArrayList();
			while (rs.next())
			{
				if(docfa.getDataRegistrazione() == null)
					docfa.setDataRegistrazione(tornaValoreRS(rs,"DATA_REGISTRAZIONE","YYYYMMDD"));
				Docfa locaList = new Docfa();
				locaList.setFoglio(tornaValoreRS(rs,"FOG"));
				locaList.setParticella(tornaValoreRS(rs,"NUM"));
				locaList.setSubalterno(tornaValoreRS(rs,"SUB"));
				locaList.setZona(tornaValoreRS(rs,"ZONA"));
				locaList.setClasse(tornaValoreRS(rs,"CLS"));
				locaList.setCategoria(tornaValoreRS(rs,"CAT"));
				locaList.setConsistenza(tornaValoreRS(rs,"CONS"));
				locaList.setSuperfice(tornaValoreRS(rs,"SUP"));
				locaList.setRendita(tornaValoreRS(rs,"RENDITA_EU"));
				locaList.setIdentificativoImm(tornaValoreRS(rs,"identificativo_immobile"));
				locaList.setPresenzaGraffati(tornaValoreRS(rs,"presenza_graffati"));
				locaList.setProtocollo(protocollo);
				locaList.setFornitura(fornitura);
			
				
				listaDocfaDatiCensuari.add(locaList);
			}
			rs.close();
			pstm.close();
			
//			ht.put(LISTA_DETTAGLIO_DOCFA_DATI_CENSUARI, listaDocfaDatiCensuari);
			
			//DOCFA_DICHIARANTI
			sql ="SELECT dic_cognome cognome,  "+
				"	   dic_nome nome,  "+
				"	   dic_res_indir indirizzo, "+
				"	   dic_res_numciv civico, "+				
				"	   dic_res_com luogo "+
				"	   FROM DOCFA_dichiaranti dic "+
				"	   WHERE dic.PROTOCOLLO_REG = ? "+
				"	   AND dic.FORNITURA =  TO_DATE(?,'YYYYMMDD')";
			
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, protocollo);
			pstm.setString(2, fornitura);
			rs = pstm.executeQuery();
			ArrayList listaDocfaDichiaranti = new ArrayList();
			while (rs.next())
			{
				Docfa dicList = new Docfa();
				dicList.setCognome(tornaValoreRS(rs,"cognome"));
				dicList.setNome(tornaValoreRS(rs,"nome"));
				dicList.setIndirizzoDichiarante(tornaValoreRS(rs,"indirizzo")+" "+tornaValoreRS(rs,"civico"));
				dicList.setLuogo(tornaValoreRS(rs,"luogo"));				
				listaDocfaDichiaranti.add(dicList);
			}
			rs.close();
			pstm.close();
//			ht.put(LISTA_DETTAGLIO_DOCFA_DICHIARANTI, listaDocfaDichiaranti);
			
			//DOCFA_IN_PARTE_UNO
			sql ="SELECT CU_FOGLIO FOG, "+
				"	   CU_NUMERO NUM, "+
				"	   CU_DENOMINATORE_01 PAR, "+
				"	   ANNO_COSTRU ANNO_COST, "+
				"	   ANNO_RISTRU ANNO_RIST, "+
				"	   DECODE(POSIZIONE_FABB,1,'ISOLATO',2,'CONTIGUO',3,'A SCHIERA',POSIZIONE_FABB) POSIZ_FABB, "+
				"	   DECODE(TIPO_ACCES,1,'UNICO ESTERNO',2,'PLURIMO ESTERNO',3,'DAL CORTILE INT.',TIPO_ACCES) TIPO_ACCES "+
				"	   FROM DOCFA_IN_PARTE_UNO PAU" +
				"	   WHERE PAU.PROTOCOLLO_REG = ? " +
				"	   AND PAU.FORNITURA =  TO_DATE(?,'YYYYMMDD') "+
				"	   ORDER BY NR_PROG";
			
		
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, protocollo);
			pstm.setString(2, fornitura);
			rs = pstm.executeQuery();
			ArrayList listaDocfaParteUno = new ArrayList();
			while (rs.next())
			{
				Docfa parteUno = new Docfa();
				parteUno.setFoglio(tornaValoreRS(rs,"FOG"));	
				parteUno.setNumero(tornaValoreRS(rs,"NUM")); 
				parteUno.setParticella(tornaValoreRS(rs,"PAR"));				
				parteUno.setAnnoCostruzione(tornaValoreRS(rs,"ANNO_COST"));
				parteUno.setAnnoRistrutturazione(tornaValoreRS(rs,"ANNO_RIST"));
				parteUno.setPosizFabbr(tornaValoreRS(rs,"POSIZ_FABB"));
				parteUno.setTipoAccesso(tornaValoreRS(rs,"TIPO_ACCES"));
				listaDocfaParteUno.add(parteUno);
			}
			rs.close();
			pstm.close();
//			ht.put(LISTA_DETTAGLIO_DOCFA_PARTE_UNO, listaDocfaParteUno);								
				
			conn.close();
		
			req.setAttribute("docfa", docfa);
			req.setAttribute("listaDocfaIntestatari", listaDocfaIntestatari);	
			req.setAttribute("listaDocfaUiu", listaDocfaUiu);
			req.setAttribute("listaDocfaBeniNonCens", listaDocfaBeniNonCens);	
			req.setAttribute("listaDocfaAnnotazioni", listaDocfaAnnotazioni);
			req.setAttribute("listaDocfaDatiCensuari", listaDocfaDatiCensuari);
			req.setAttribute("listaDocfaDichiaranti", listaDocfaDichiaranti);
			req.setAttribute("listaDocfaParteUno", listaDocfaParteUno);
		
		}
		catch (Exception e)
		{
			e.printStackTrace();			
		}
		
		
		
	}
	
	private static String tornaValoreRS(ResultSet rs, String colonna) throws Exception
	{
		return tornaValoreRS(rs, colonna, null);
	}

	private static String tornaValoreRS(ResultSet rs, String colonna, String tipo) throws Exception
	{
		try
		{
			String s = null;
			s = rs.getString(colonna);

			if (s == null && tipo != null)
				if (tipo.equals("VUOTO"))
					s = "";
			if (s == null)
				return s = "-";

			if (tipo != null)
				if (tipo.equals("NUM"))
				{
					s = new Integer(s).toString();
				}
				else if (tipo.equals("DOUBLE"))
				{
					s = new Double(s).toString();
				}
				else if(tipo.equals("EURO"))
				{
					NumberFormat nf = NumberFormat.getNumberInstance(Locale.ITALY);
					nf.setMinimumFractionDigits(2);
					nf.setMaximumFractionDigits(2);
					s = nf.format(new Double(s));
				}
				else if (tipo.equalsIgnoreCase("YYMMDD"))
				{
					s = s.substring(4) + "/" + s.substring(2, 4) + "/" + s.substring(0, 2);
				}
				else if (tipo.equalsIgnoreCase("YYYY/MM/DD"))
				{
					s = s.substring(8) + "/" + s.substring(5, 7) + "/" + s.substring(0, 4);
				}
				else if (tipo.equalsIgnoreCase("YYYY-MM-DD"))
				{
					s = s.substring(8,10) + "/" + s.substring(5, 7) + "/" + s.substring(0, 4);
				}
				else if (tipo.equalsIgnoreCase("DDMMYYYY"))
				{
					s = s.substring(0, 2) + "/" + s.substring(2, 4) + "/" + s.substring(4, 8);
				}
				else if (tipo.equalsIgnoreCase("YYYYMMDD"))
				{
					s = s.substring(6, 8) + "/" + s.substring(4, 6) + "/" + s.substring(0, 4);
				}			
				else if (tipo.equalsIgnoreCase("FLAG"))
				{
					if (s.equals("0"))
						s = "NO";
					else
						s = "SI";
				}
			return s;
		}
		catch (Exception e)
		{
			return "-";
		}
	}
	
	private static Object mDatiGraffati(Connection conn,String foglio, String particella,String subalterno,String cod_ente)
	throws Exception
	{
		String sql1 = "SELECT id_imm " +
		 "FROM load_cat_uiu_id " +
		 /*"WHERE codi_fisc_luna = ? " +
		 "and foglio= ? " +*/
		 "where foglio= ? " +
		 "and mappale= ? " +
		 "and sub= ? ";
		
		String sql2 = "SELECT distinct foglio,mappale,sub " +
		 "FROM load_cat_uiu_id " +
		// "WHERE codi_fisc_luna = ? " +
		 "WHERE id_imm = ? ";
		
		ArrayList list = new ArrayList();
		try
		{
			PreparedStatement pstm = conn.prepareStatement(sql1);
			//pstm.setString(1, cod_ente);
			pstm.setString(1, foglio);
			pstm.setString(2, particella);
			pstm.setString(3, subalterno);
			ResultSet rs = pstm.executeQuery();
			
			Integer id_imm = null;
			if (rs.next())
			{
				id_imm = new Integer(tornaValoreRS(rs,"id_imm"));
			}
			rs.close();
			pstm.close();
			
			if (id_imm != null)
			{
				pstm = conn.prepareStatement(sql2);
				//pstm.setString(1, cod_ente);
				pstm.setInt(1, id_imm.intValue());
				rs = pstm.executeQuery();
				
				while(rs.next())
				{
					Docfa datiGraffati = new Docfa();
					datiGraffati.setFoglio(tornaValoreRS(rs,"FOGLIO"));
					datiGraffati.setParticella(tornaValoreRS(rs,"MAPPALE"));
					datiGraffati.setSubalterno(tornaValoreRS(rs,"SUB"));	
					list.add(datiGraffati);
				}
			}
			
			
			if (list.size() <= 1) // non ci sono graffati
				list= null;
			
			return list;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		
		
	}


}
