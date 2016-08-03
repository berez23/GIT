package it.webred.mui.output;

import it.webred.docfa.model.DocfaFornitura;
import it.webred.mui.http.MuiApplication;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;


public class DocfaFornituraExporter {

	private SimpleDateFormat dateParser = new SimpleDateFormat("yyyyMMdd");
	
	public void dumpFornitura(DocfaFornitura fornitura, Writer writer)
			throws IOException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException ,Exception
	{
		//Record Tipo 1
		Calendar cal = Calendar.getInstance();
		cal.setTime(fornitura.getDataFornitura());
		String inizioData = Integer.toString(cal.get(Calendar.YEAR))+ Integer.toString(cal.get(Calendar.MONTH)+1)+"01";
		Date data = dateParser.parse(inizioData);
		Calendar tempData = Calendar.getInstance();
		tempData.setTime(data);
		tempData.roll(Calendar.MONTH, 1);
		tempData.roll(Calendar.DAY_OF_MONTH, -1);
		String fineData = tempData.get(Calendar.YEAR)+ Integer.toString(tempData.get(Calendar.MONTH)+1)+Integer.toString(tempData.get(Calendar.DAY_OF_MONTH));
		String scrivi1 ="1|"+MuiApplication.belfiore+"|"+ inizioData +"|"+ fineData +"|"+"|"+dateParser.format(fornitura.getDataInizio())+"\n"; 
		writer.write(scrivi1);
		
		//Record Tipi 2-9
		exportNotes(writer, fornitura);
		
		
	}

	//private void exportNotes(Writer writer, Iterator noteIterator) throws IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
	private void exportNotes(Writer writer, DocfaFornitura fornitura) 
		throws IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, Exception 
	{	
		try{
		
			Context cont = new InitialContext();
			Context datasourceContext = (Context) cont.lookup("java:comp/env");
			DataSource theDataSource = (DataSource) datasourceContext.lookup("jdbc/mui");
			Connection conn = theDataSource.getConnection();
			
			String sqlProtocolliForn = "select distinct g.PROTOCOLLO_REG " +
									   "from docfa_dati_generali g " +
									   "where g.FORNITURA = ? ";
			
			PreparedStatement pstm = conn.prepareStatement(sqlProtocolliForn);
			pstm.setDate(1, new java.sql.Date(fornitura.getDataFornitura().getTime()) );
			ResultSet rsProtForn = pstm.executeQuery();
			
			//progressivo per numero protocollo nella fornitura
			int progProt = 0;
			
			int contaTipo3 = 0;
			int contaTipo4 = 0;
			int contaTipo5 = 0;
			int contaTipo6= 0;
			int contaTipo7= 0;
			
			while (rsProtForn.next()) {
				progProt++;
				//progressivo titolare nel protocollo
				int progTito = 0;
				String protocollo_reg = rsProtForn.getString("PROTOCOLLO_REG");
				
				//recupero data_registrazione da DOCFA_DATI_METRICI
				String sqlMetrici = "select distinct m.DATA_REGISTRAZIONE " +
									"from docfa_dati_metrici m " +
									"where m.FORNITURA = ? " +
									"and m.PROTOCOLLO_REGISTRAZIONE = ? ";
				PreparedStatement pstmM = conn.prepareStatement(sqlMetrici);
				pstmM.setDate(1, new java.sql.Date(fornitura.getDataFornitura().getTime()) );
				pstmM.setString(2, protocollo_reg);
				ResultSet rsM = pstmM.executeQuery();
				String dataReg = "";
				String annoReg = "";
				if (rsM.next())
				{
					dataReg = rsM.getString("DATA_REGISTRAZIONE"); //yyyymmdd
					annoReg = dataReg.substring(0, 4);
				}
				rsM.close();
				pstmM.close();
				//record tipo2
				String scrivi2 = "2|"+progProt+"||"+ protocollo_reg+"||"+annoReg+"|"+dataReg+"||||||||||||||"+"\n"; 
				writer.write(scrivi2);
				
				//recupero oggetti del docfa
				String sqlUIU = "SELECT DISTINCT TIPO_OPERAZIONE    AS TIPO,   " +
						"       FOGLIO             AS FOGLIO, " +
						"       NUMERO             AS NUMERO, " +
						"       TRIM(SUBALTERNO)         AS SUB, " +
						"		DENOMINATORE, " +
						"        NR_PROG              AS PROG_IMMO, " +
						"       TRIM(TRIM(INDIR_TOPONIMO) || ' ' || trim(INDIR_NCIV_UNO || ' ' || INDIR_NCIV_DUE || ' ' || INDIR_NCIV_TRE )|| ' ' ||trim( INDIR_NRPIANO_UNO || ' ' || INDIR_NRPIANO_DUE || ' ' || INDIR_NRPIANO_TRE))     AS INDIRIZZO,  " +
						"       PROP_CATEGORIA, " +
						"       PROP_CLASSE, " +
						"       PROP_RENDITA_EURO_CATA, " +
						"       PROP_SUPERFICIE_CATA, " +
						"       PROP_ZONA_CENSUARIA, prop_consistenza  " +
						"	   FROM DOCFA_UIU U " +
						"	   WHERE " +
						"	   U.FORNITURA = ? " +
						"	   AND U.PROTOCOLLO_REG =  ? ";

				
				pstmM = conn.prepareStatement(sqlUIU);
				pstmM.setDate(1, new java.sql.Date(fornitura.getDataFornitura().getTime()) );
				pstmM.setString(2, protocollo_reg);
				rsM = pstmM.executeQuery();
				
				while (rsM.next())
				{
					String tipoOpe = rsM.getString("TIPO");
					String progUIU = rsM.getString("PROG_IMMO");
					String foglio = rsM.getString("FOGLIO");
					String particella = rsM.getString("NUMERO");
					String sub = getValue(rsM.getString("SUB"));
					String denominatore = getValue(rsM.getString("DENOMINATORE"));
					String indirizzo = getValue(rsM.getString("INDIRIZZO"));
					String propCate = getValue(rsM.getString("PROP_CATEGORIA"));
					String propCla = getValue(rsM.getString("PROP_CLASSE"));
					String propZona = getValue(rsM.getString("PROP_ZONA_CENSUARIA"));
					String propSupe = getValue(rsM.getString("PROP_SUPERFICIE_CATA"));
					String propRend = getValue(rsM.getString("PROP_RENDITA_EURO_CATA"));
					String propCons = getValue(rsM.getString("prop_consistenza"));
					
					//record tipo7
					contaTipo7++;
					String scrivi7 = "7|"+progProt+"|"+progUIU+"||||"+Integer.parseInt(foglio)+"|"+Integer.parseInt(particella)+"|"+Integer.parseInt(denominatore)+"|";
					if (sub.equals(""))
						scrivi7 = scrivi7 + "|";
					else
						scrivi7 = scrivi7 +Integer.parseInt(sub) +"|";
					scrivi7 = scrivi7 + "|||\n";
					writer.write(scrivi7);
					
					//recupero info da catasto
					String sqlUIUCataS = "select * from( " +
											"SELECT DISTINCT sitidstr.numero as codice_via, " +
											"                sitidstr.prefisso as toponimo, " +
											"                sitidstr.nome as indirizzo,  " +
											"                siticivi.civico, " +
											"                sitiuiu.CATEGORIA,sitiuiu.CLASSE,sitiuiu.CONSISTENZA, " +
											"                sitiuiu.SUP_CAT,sitiuiu.RENDITA,  " +
											"				 TO_CHAR (sitiuiu.data_fine_val,'dd/mm/yyyy') AS data_fine_val " +
											"                FROM sitiuiu,  siticivi_uiu, siticivi, sitidstr, siticomu " +
											"                   WHERE siticomu.codi_fisc_luna = '"+MuiApplication.belfiore+"' " +
											"					  AND sitiuiu.cod_nazionale = siticomu.cod_nazionale " +
											"                     AND sitiuiu.foglio = ? " +
											"                     AND sitiuiu.particella = ? " +
											"                     AND DECODE (sitiuiu.sub, " +
											"                ' ', '-', " +
											"                NVL (sitiuiu.sub, '*'), '-', " +
											"                sitiuiu.sub " +
											"                ) = DECODE (NVL (' ', '*'), '*', '-', ' ', '-', ' ') " +
											"                AND sitiuiu.unimm = ? " +
											"                AND (    sitiuiu.data_inizio_val <= TO_DATE (?, 'dd/mm/yyyy') " +
											"                AND sitiuiu.data_fine_val >= TO_DATE (?, 'dd/mm/yyyy')) " +
											"                AND siticivi_uiu.pkid_uiu(+) = sitiuiu.pkid_uiu " +
											"                AND siticivi_uiu.pkid_civi = siticivi.pkid_civi(+) " +
											"                AND sitidstr.pkid_stra(+) = siticivi.pkid_stra " +
											"                AND siticivi_uiu.data_fine_val(+) = TO_DATE ('99991231', 'yyyymmdd') " +
											"                AND siticivi.data_fine_val(+) = TO_DATE ('99991231', 'yyyymmdd') " +
											"                AND sitidstr.data_fine_val(+) = TO_DATE ('99991231', 'yyyymmdd') " +
											"                ORDER BY data_fine_val, indirizzo, civico " +
										")where ROWNUM =1 ";
					
					String sqlUIUCataC = "select * from( " +
											"SELECT DISTINCT sitidstr.numero as codice_via, " +
											"                sitidstr.prefisso as toponimo, " +
											"                sitidstr.nome as indirizzo,  " +
											"                siticivi.civico, " +
											"                sitiuiu.CATEGORIA,sitiuiu.CLASSE,sitiuiu.CONSISTENZA, " +
											"                sitiuiu.SUP_CAT,sitiuiu.RENDITA,  " +
											"				 TO_CHAR (sitiuiu.data_fine_val,'dd/mm/yyyy') AS data_fine_val " +											
											"                FROM sitiuiu,  siticivi_uiu, siticivi, sitidstr, siticomu " +
											"                WHERE siticomu.codi_fisc_luna = '"+MuiApplication.belfiore+"'  " +
											"                AND sitiuiu.cod_nazionale = siticomu.cod_nazionale " +
											"                AND sitiuiu.foglio = ?  " +
											"                AND sitiuiu.particella = ?  " +
											"                AND DECODE (sitiuiu.sub,' ', '-',NVL (sitiuiu.sub, '*'), '-', sitiuiu.sub ) = DECODE (NVL (' ', '*'), '*', '-', ' ', '-', ' ')  " +
											"                AND sitiuiu.unimm = ?  " +
											"                AND sitiuiu.data_inizio_val >= TO_DATE (?, 'dd/mm/yyyy')  " +
											"                AND siticivi_uiu.pkid_uiu(+) = sitiuiu.pkid_uiu  " +
											"                AND siticivi_uiu.pkid_civi = siticivi.pkid_civi(+)  " +
											"                AND sitidstr.pkid_stra(+) = siticivi.pkid_stra  " +
											"                AND siticivi_uiu.data_fine_val(+) = TO_DATE ('99991231', 'yyyymmdd')  " +
											"                AND siticivi.data_fine_val(+) = TO_DATE ('99991231', 'yyyymmdd')  " +
											"                AND sitidstr.data_fine_val(+) = TO_DATE ('99991231', 'yyyymmdd')  " +
											"                ORDER BY data_fine_val, indirizzo, civico  " +
										")where ROWNUM =1 ";
					
					PreparedStatement ps = null;
					if (tipoOpe.equals("C"))
						ps = conn.prepareStatement(sqlUIUCataC);
					else
						ps = conn.prepareStatement(sqlUIUCataS);
					
					ps.setInt(1, Integer.parseInt(foglio) );
					ps.setString(2, particella);
					if(sub.equals(""))
						sub = "0";
					ps.setInt(3, Integer.parseInt(sub) );
					ps.setDate(4, new java.sql.Date(fornitura.getDataFornitura().getTime()));
					if (!tipoOpe.equals("C"))
						ps.setDate(5, new java.sql.Date(fornitura.getDataFornitura().getTime()));
					
					ResultSet rs = ps.executeQuery();
					
					String cateCata = "";
					String classeCata = "";
					String consCata = "";
					String supeCata = "";
					String renditaCata = "";
					String toponimoCata = "";
					String indirizzoCata = "";
					String civicoCata = "";
					if (rs.next())
					{
						cateCata = getValue(rs.getString("CATEGORIA"));
						classeCata = getValue(rs.getString("CLASSE"));
						consCata = getValue(rs.getString("CONSISTENZA"));
						supeCata = getValue(rs.getString("SUP_CAT"));
						renditaCata = getValue(rs.getString("RENDITA"));
						toponimoCata = getValue(rs.getString("toponimo"));
						indirizzoCata = getValue(rs.getString("indirizzo"));
						civicoCata = getValue(rs.getString("civico"));
					}
					rs.close();
					ps.close();
					
					//record tipo6
					contaTipo6++;
					String scrivi6 = "6|"+progProt+"|F||"+progUIU+"|||||";
					if (propCate.equals("") || propCate.equals("00"))
						scrivi6 = scrivi6+cateCata+"|"+classeCata+"|"+consCata+"||"+supeCata+"||"+renditaCata+"|";
					else
						scrivi6 = scrivi6+propCate+"|"+propCla+"|"+propCons+"||"+propSupe+"||"+propRend+"|";
					
					scrivi6 = scrivi6 + "||||||||||"+indirizzo+"|||||||||||||"+toponimoCata+" "+indirizzoCata+" "+" "+civicoCata+"|||\n";
					writer.write(scrivi6);
					
					//recupero i titolari della UIU
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
							"AND cons_csui_tab.cod_nazionale = siticomu.cod_nazionale " + 
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
					"AND cons_csui_tab.cod_nazionale = siticomu.cod_nazionale " + 
					"AND cons_csui_tab.foglio = ? " + "AND cons_csui_tab.particella = ? " + 
					"AND DECODE (cons_csui_tab.sub,' ', '-',NVL (cons_csui_tab.sub, '*'), '-',cons_csui_tab.sub) = DECODE (NVL (' ', '*'), '*', '-', ' ', '-', ' ') " + 
					"AND cons_csui_tab.unimm = ? " + "AND cons_csui_tab.data_inizio >= TO_DATE (?, 'dd/mm/yyyy') " +
							// "AND cons_sogg_tab.data_inizio >= TO_DATE (?, 'dd/mm/yyyy') " +
					"AND ( cons_sogg_tab.data_inizio <= TO_DATE (?, 'dd/mm/yyyy') " + 
					"    AND cons_sogg_tab.data_fine >= TO_DATE (?, 'dd/mm/yyyy'))	" + 
					"AND cons_csui_tab.pk_cuaa = cons_sogg_tab.pk_cuaa " + 
					"AND SUBSTR (cons_sogg_tab.comu_nasc, 1, 3) = siticomu.istatp(+) " + 
					"AND SUBSTR (cons_sogg_tab.comu_nasc, 4, 3) = siticomu.istatc(+) ";
					
					if (tipoOpe.equals("C"))
						ps = conn.prepareStatement(sqlUIUTitoC);
					else //S o V
						ps = conn.prepareStatement(sqlUIUTitoS);
					
					ps.setString(1, foglio );
					ps.setString(2, particella);
					ps.setString(3, sub );
					ps.setDate(4, new java.sql.Date(fornitura.getDataFornitura().getTime()));
					ps.setDate(5, new java.sql.Date(fornitura.getDataFornitura().getTime()));
					ps.setDate(6, new java.sql.Date(fornitura.getDataFornitura().getTime()));
					if (!tipoOpe.equals("C"))
						ps.setDate(7, new java.sql.Date(fornitura.getDataFornitura().getTime()));
					
					rs = ps.executeQuery();
					
					while (rs.next())
					{
						progTito++;
						String flagPers = getValue(rs.getString("flag_pers_fisica"));
						String quota = getValue(rs.getString("QUOTA"));
						Double rapporto = new Double("100")/new Double(quota);
						String denominazione = getValue(rs.getString("denominazione"));
						String nome = getValue(rs.getString("nome"));
						String sesso = getValue(rs.getString("sesso"));
						String codiFisc = getValue(rs.getString("codi_fisc"));
						String comuneNasc = getValue(rs.getString("sede"));
						String dataNascita = getValue(rs.getString("data_nasc"));
						
						//record tipo4
						contaTipo4++;
						String scrivi4 = "4|"+progProt+"|"+progTito+"||"+flagPers+"|"+progUIU+"|F|";
						if (tipoOpe.equals("C"))
							scrivi4 = scrivi4+"C|1|100|"+rapporto.toString()+"|||||||";
						else
							scrivi4 = scrivi4+"||||||F|1|100|"+rapporto.toString()+"|";
						
						scrivi4 = scrivi4 + "|||||||||||||||||\n"; 
						writer.write(scrivi4);
						
						//record tipo3
						contaTipo3++;
						String scrivi3 = "3|"+progProt+"|"+progTito+"|";
						if (tipoOpe.equals("C"))
							scrivi3 = scrivi3+"F|";
						else
							scrivi3 = scrivi3+"C|";
		
						if (flagPers.equals("P"))
							scrivi3 = scrivi3+denominazione+"|"+nome+"|"+sesso+"|"+dataNascita+"|"+comuneNasc+"|"+codiFisc+"|||";
						else
							scrivi3 = scrivi3+"|||"+dataNascita+"|"+comuneNasc+"||"+denominazione+"|"+comuneNasc+"|"+codiFisc;
						scrivi3 = scrivi3 + "\n"; 
						writer.write(scrivi3);
						
						//devo recuperare dati residenza soggetto
						String sqlTitoResPAna = "SELECT DISTINCT p.id_orig matricola, p.codfisc codice_fisc, p.cognome, p.nome," +
								" p.sesso," +
								" cxml.ID_ORIG_VIA cod_via,cxml.viasedime || ' ' || cxml.DESCRIZIONE  descr_via, " +
								" cxml.civ_liv1 AS numero_civ, cxml.civ_liv2 esp_civ, TO_CHAR (pc.dt_inizio_dato, 'DD/MM/YYYY') data_inizio_res," +
								" TO_CHAR (pc.dt_fine_dato, 'DD/MM/YYYY') data_fine_res " +
								" FROM " +
								" sit_d_persona_civico pc," +
								" sit_d_persona p," +
								" sit_d_pers_fam f,SIT_D_CIVICO_VIA_V cxml" +
								" WHERE pc.id_ext_d_civico = cxml.id_ext" +
								" AND p.id_ext = pc.id_ext_d_persona(+)" +
								" AND p.id_ext = f.id_ext_d_persona(+)" +
								" AND p.codfisc = ?";
						
						
						String sqlTitoResPTributi = "select descr_indirizzo,comune_residenza,pv.SIGLA  " +
								"from tri_contribuenti, " +
								"sit_comune c,  " +
								"sit_provincia pv  " +
								"where codice_fiscale = ? " +
								"and comune_residenza = c.DESCRIZIONE(+) " +
								"and c.ID_EXT_D_PROVINCIA = pv.ID_EXT (+) ";
						
						String sqlTitoGTributi = "select descr_indirizzo,comune_residenza,pv.SIGLA  " + 
								"from tri_contribuenti, " +
								"sit_comune c,  " +
								"sit_provincia pv  " +
								"where partita_iva = ? " +
								"and comune_residenza = c.DESCRIZIONE(+) " +
								"and c.ID_EXT_D_PROVINCIA = pv.ID_EXT (+) " +
								"order by provenienza desc";
						String sqlTitoGSiatel = "SELECT IND_CIV_SEDE_LEGALE as indirizzo, " +
								"COMUNE_SEDE_LEGALE as comune, " +
								"PROVIN_SEDE_LEGALE as provincia " +
								"FROM MI_SIATEL_P_GIU  " +
								"WHERE COD_FISC_INDIVIDUATO = ? ";
						
						String indirizzoRes = "";
						String civicoRes = "";
						String comuneRes = "";
						String provinciaRes = "";
						
						if (flagPers.equals("P"))
						{
							//cerco in anagrafe
							PreparedStatement psA = conn.prepareStatement(sqlTitoResPAna);
							psA.setString(1, codiFisc);
							ResultSet rsA = psA.executeQuery();
							if (rsA.next())
							{
								indirizzoRes = getValue(rsA.getString("descr_via"))+" "+getValue(rsA.getString("numero_civ"))+" "+getValue(rsA.getString("esp_civ"));
								comuneRes = getValue(rsA.getString("comune"));
								provinciaRes = getValue(rsA.getString("provincia"));
							}
							else
							{
								//cerco su tributi
								rsA.close();
								psA.close();
								psA = conn.prepareStatement(sqlTitoResPTributi);
								psA.setString(1, codiFisc);
								rsA = psA.executeQuery();
								if (rsA.next())
								{
									indirizzoRes = getValue(rsA.getString("descr_indirizzo"));
									comuneRes = getValue(rsA.getString("comune_residenza"));
									provinciaRes = getValue(rsA.getString("SIGLA"));
								}
								rsA.close();
								psA.close();
							}
						}
						else
						{ // soggetto giuridico
							//cerco in tributi
							PreparedStatement psT = conn.prepareStatement(sqlTitoGTributi);
							psT.setString(1, codiFisc);
							ResultSet rsT = psT.executeQuery();
							if (rsT.next())
							{
								indirizzoRes = getValue(rsT.getString("descr_indirizzo"));
								comuneRes = getValue(rsT.getString("comune_residenza"));
								provinciaRes = getValue(rsT.getString("SIGLA"));
							}
							else
							{
								//cerco in siatel
								rsT.close();
								psT.close();
								psT = conn.prepareStatement(sqlTitoGSiatel);
								psT.setString(1, codiFisc);
								rsT = psT.executeQuery();
								if (rsT.next())
								{
									indirizzoRes = getValue(rsT.getString("indirizzo"));
									comuneRes = getValue(rsT.getString("comune"));
									provinciaRes = getValue(rsT.getString("provincia"));
								}
								rsT.close();
								psT.close();
							}
							
						}
						
						//record tipo5
						contaTipo5++;
						String scrivi5 = "5|"+progProt+"|"+progTito+"|";
						if (flagPers.equals("P"))
							scrivi5 = scrivi5+"1|";
						else
							scrivi5 = scrivi5+"|";
						scrivi5 = scrivi5 +comuneRes+"|"+provinciaRes+"|"+indirizzoRes+"|\n"; 
						writer.write(scrivi5);
						
						
						
					}
					rs.close();
					ps.close();
					
					
					
					
				}//while su UIU
				rsM.close();
				pstmM.close();
				
			}//while su protocollo
			rsProtForn.close();
			pstm.close();
			
			//record tipo9
			int sumRecord = contaTipo3+contaTipo4+contaTipo5 + contaTipo6+contaTipo7+1; //1 record tipo2
			String scrivi9 = "9|"+sumRecord+"|"+fornitura.getNumeroDocfa()+"||||"+contaTipo6+"|\n"; 
			writer.write(scrivi9);
			
			
			conn.close();
		
		}catch(Exception e){
			throw e;
		}
	}

	private static String getValue(String val) {
		if (val == null) {
			return "";
		} else {
			return val;
		}
	}
}
