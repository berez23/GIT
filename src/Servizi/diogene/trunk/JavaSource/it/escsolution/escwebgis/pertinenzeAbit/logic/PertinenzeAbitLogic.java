package it.escsolution.escwebgis.pertinenzeAbit.logic;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.naming.NamingException;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.pertinenzeAbit.bean.DatiCatastali;
import it.escsolution.escwebgis.pertinenzeAbit.bean.DatiTitolarita;
import it.escsolution.escwebgis.pertinenzeAbit.bean.PertinenzeAbit;
import it.escsolution.escwebgis.pertinenzeAbit.bean.PertinenzeAbitDatiCatastali;
import it.escsolution.escwebgis.pertinenzeAbit.bean.PertinenzeAbitTitolare;
import it.webred.utils.GenericTuples;
import it.webred.utils.StringUtils;

public class PertinenzeAbitLogic extends EscLogic{
	
	public static final String PERTINENZE_ABIT = PertinenzeAbitLogic.class.getName();
	
	public static final String LISTA_DETTAGLIO_DATI_CATASTALI = "LISTA_DETTAGLIO_DATI_CATASTALI@PertinenzeAbitLogic";
	public static final String LISTA_DETTAGLIO_TITOLARITA = "LISTA_DETTAGLIO_TITOLARITA@PertinenzeAbitLogic";
	public static final String LISTA_DETTAGLIO_ALTRE_TITOLARITA = "LISTA_DETTAGLIO_ALTRE_TITOLARITA@PertinenzeAbitLogic";
	public static final String LISTA_DETTAGLIO_TITOLARE = "LISTA_DETTAGLIO_TITOLARE@PertinenzeAbitLogic";
	public static final String CIVICO_IN_PERTINENZA_CARTOGRAFICA = "CIVICO_IN_PERTINENZA_CARTOGRAFICA@PertinenzeAbitLogic";
	public static final String UI_RESIDENZIALE_IN_PERTINENZA = "UI_RESIDENZIALE_IN_PERTINENZA@PertinenzeAbitLogic";
	public static final String UI_DI_CATEGORIA_IN_PERTINENZA = "UI_DI_CATEGORIA_IN_PERTINENZA@PertinenzeAbitLogic";
	
	public static final String UNIMM = PertinenzeAbitLogic.class.getName() +  "@UNIMM";
	
	private final static String SQL_SELECT_LISTA = "select * from ( " +
			"SELECT ROWNUM N, SELEZIONE.* FROM ( " +
			"select distinct ui.cod_nazionale, ui.foglio, ui.particella, ui.unimm as subalterno, ui.categoria as categ, sc.cuaa as cf, " +
			"sc.perc_poss, DECODE(sc.tipo_titolo,1,'PROPRIETA''',9,(decode(trim(sc.tipo_documento),'990',sc.tit_no_cod,'99',decode(sc.tit_no_cod,null,'TITOLO NON CODIFICATO',sc.tit_no_cod), 'ALTRO'))) AS tip_tit, " +
			"anag_soggetti.cognome, anag_soggetti.nome, anag_soggetti.d_nascita, t.descr ||' '|| uind.ind as indirizzo, uind.civ1 " +
			"FROM " +
			"sitiuiu ui, siticonduz_imm_all sc, anag_soggetti, load_cat_uiu_id i, load_cat_uiu_ind uind, cat_d_toponimi t, siticomu c " +
			"WHERE " +
			"c.codi_fisc_luna = (select uk_belfiore from ewg_tab_comuni) " +
			/*
			 * --and ui.foglio = 321
				--and ui.particella = lpad(327,5,'0')
				--and ui.unimm= 2 
			 */
			"and ui.data_fine_val=to_date('99991231','yyyymmdd') " +
			"and ui.categoria in ('A01', 'A02', 'A03', 'A04', 'A05', 'A06', 'A07', 'A08', 'A09', 'A11')" +
			/*
			 * --and sc.cuaa='GRNLGU39M08F205S'
			 */
			"and ui.cod_nazionale = sc.cod_nazionale (+) " +
			"and ui.data_fine_val=sc.data_fine (+) " +
			"and ui.foglio=sc.foglio (+) " +
			"and ui.particella=sc.particella (+) " +
			"and ui.sub = sc.sub (+) " +
			"and ui.unimm = sc.unimm (+) " +
			/*
			 * --and anag_soggetti.denominazione = 'GUARNIERI LUIGI'
				--and anag_soggetti.cognome = 'GUARNIERI' 		
				--and anag_soggetti.nome =  'LUIGI'
			 */
			"and sc.data_fine = anag_soggetti.data_fine_val (+) " +
			"and sc.pk_cuaa = anag_soggetti.cod_soggetto (+) " +
			"and ui.cod_nazionale = c.cod_nazionale (+) " +
			"and lpad(ui.foglio, 4, '0') = i.foglio (+) " +
			"and ui.particella = i.mappale (+) " +
			"and decode(ui.unimm, 0, ' ', lpad(ui.unimm, 4, '0')) = i.sub (+) " +
			"and i.codi_fisc_luna = uind.codi_fisc_luna (+) " +
			"and i.sezione = uind.sezione (+) " +
			"and i.progressivo = uind.progressivo (+) " +
			"and i.id_imm = uind.id_imm (+) " +
			/*
			 * --and uind.ind like '%GARIBALDI%'
			--and uind.civ1='12'
			 */
			"and uind.seq = 0 " +
			"and uind.toponimo = t.pk_id (+) " +
			"and 1 = ? ";

	private final static String SQL_SELECT_COUNT_LISTA =" SELECT count(*) as conteggio " +
			"FROM " +
			"(" +
			"select distinct ui.cod_nazionale, ui.foglio, ui.particella, ui.unimm as subalterno, ui.categoria as categ, sc.cuaa as cf, " +
			"sc.perc_poss, DECODE(sc.tipo_titolo,1,'PROPRIETA''',9,(decode(trim(sc.tipo_documento),'990',sc.tit_no_cod,'99',decode(sc.tit_no_cod,null,'TITOLO NON CODIFICATO',sc.tit_no_cod), 'ALTRO'))) AS tip_tit, " +
			"anag_soggetti.cognome, anag_soggetti.nome, anag_soggetti.d_nascita, t.descr ||' '|| uind.ind as indirizzo, uind.civ1 " +
			"FROM " +
			"sitiuiu ui, siticonduz_imm_all sc, anag_soggetti, load_cat_uiu_id i, load_cat_uiu_ind uind, cat_d_toponimi t, siticomu c " +
			"WHERE " +
			"c.codi_fisc_luna = (select uk_belfiore from ewg_tab_comuni) " +
			/*
			 * --and ui.foglio = 321
				--and ui.particella = lpad(327,5,'0')
				--and ui.unimm= 2 
			 */
			"and ui.data_fine_val=to_date('99991231','yyyymmdd') " +
			"and ui.categoria in ('A01', 'A02', 'A03', 'A04', 'A05', 'A06', 'A07', 'A08', 'A09', 'A11')" +
			/*
			 * --and sc.cuaa='GRNLGU39M08F205S'
			 */
			"and ui.cod_nazionale = sc.cod_nazionale (+) " +
			"and ui.data_fine_val=sc.data_fine (+) " +
			"and ui.foglio=sc.foglio (+) " +
			"and ui.particella=sc.particella (+) " +
			"and ui.sub = sc.sub (+) " +
			"and ui.unimm = sc.unimm (+) " +
			/*
			 * --and anag_soggetti.denominazione = 'GUARNIERI LUIGI'
				--and anag_soggetti.cognome = 'GUARNIERI' 		
				--and anag_soggetti.nome =  'LUIGI'
			 */
			"and sc.data_fine = anag_soggetti.data_fine_val (+) " +
			"and sc.pk_cuaa = anag_soggetti.cod_soggetto (+) " +
			"and ui.cod_nazionale = c.cod_nazionale (+) " +
			"and lpad(ui.foglio, 4, '0') = i.foglio (+) " +
			"and ui.particella = i.mappale (+) " +
			"and decode(ui.unimm, 0, ' ', lpad(ui.unimm, 4, '0')) = i.sub (+) " +
			"and i.codi_fisc_luna = uind.codi_fisc_luna (+) " +
			"and i.sezione = uind.sezione (+) " +
			"and i.progressivo = uind.progressivo (+) " +
			"and i.id_imm = uind.id_imm (+) " +
			/*
			 * --and uind.ind like '%GARIBALDI%'
			--and uind.civ1='12'
			 */
			"and uind.seq = 0 " +
			"and uind.toponimo = t.pk_id (+) " +
			"and 1 = ? ";
			// La query viene completata successivamente

	/*
	private final static String SQL_SELECT_COUNT_ALL = "select count(*) as conteggio" +
	" from sitiuiu " +
	//" where sitiuiu.COD_NAZIONALE = 'F205'" +
	" where sitiuiu.DATA_FINE_VAL = to_date('99991231', 'yyyymmdd')" +
	" and 1 = ? ";
	*/

	public PertinenzeAbitLogic(EnvUtente eu) {
		super(eu);
	}

	public Hashtable mCaricareDatiFormRicerca(String utente) throws NamingException, SQLException{
		// carico la lista delle categorie e le metto in un hashtable
		Hashtable ht = new Hashtable();

		this.setDatasource(JNDI_DIOGENE);

		Connection conn = this.getConnection();

		if (conn != null && !conn.isClosed())
			conn.close();

		return ht;
	}
	
	/*
	 * *************************************************************************
	 */

	public Hashtable mCaricareDettaglioPertinenze(String chiave) throws Exception{
		// carico la lista dei comuni e le metto in un hashtable
		Hashtable ht = new Hashtable();
		// costruisco la chiave per la query
		ArrayList listParam = new ArrayList();
		while (chiave.indexOf('|') > 0) {
			String p = chiave.substring(0,chiave.indexOf('|'));
			if (p != null && !p.equalsIgnoreCase("null")){
				listParam.add(p);
			}else{
				listParam.add(null);
			}
			chiave = chiave.substring(chiave.indexOf('|')+1);
			
		}
		if (chiave != null && !chiave.equalsIgnoreCase("null")){
			listParam.add(chiave);
		}else{
			listParam.add(null);
		}

		/*
		 * 0: Folgio
		 * 1: Particella
		 * 2: Subalterno
		 * 3: cf
		 * 4: cognome
		 * 5: nome
		 * 6: dataNascita
		 * 7: indirizzo
		 * 8: civico
		 */
		Connection conn = null;
		try {
			this.setDatasource(JNDI_DBTOTALE);

			conn = this.getConnection();
			this.initialize();

			//LISTA_DETTAGLIO_DATI_CATASTALI
			String sql = "select distinct " +
					"ui.foglio, ui.particella, ui.sub, ui.unimm, ui.data_inizio_val, " +
					"ui.categoria, ui.classe, ui.consistenza, ui.rendita, ui.sup_cat, ui.zona, ui.edificio, ui.scala, " +
					"ui.interno, ui.piano, " +
					"t.descr ||' '|| uind.ind as indirizzo, uind.civ1 "+
					"from " +
					"sitiuiu ui, load_cat_uiu_id i, load_cat_uiu_ind uind, cat_d_toponimi t, siticomu c " +
					"where " +
					"c.codi_fisc_luna = (select uk_belfiore from ewg_tab_comuni) " +
					"and ui.data_fine_val=to_date('99991231','yyyymmdd') " +
					"and ui.categoria in ('A01','A02','A03','A04','A05','A06','A07','A08','A09','A11') " +
					"and ui.foglio = ? " +
					"and ui.particella = lpad(?, 5, '0') " +
					"and ui.unimm= ? " +
					"and ui.cod_nazionale = c.cod_nazionale (+) " +
					"and lpad(ui.foglio, 4, '0') = i.foglio (+) " +
					"and ui.particella = i.mappale (+) " +
					"and lpad(ui.unimm, 4, '0') = i.sub (+) " +
					"and i.codi_fisc_luna = uind.codi_fisc_luna (+) " +
					"and i.sezione = uind.sezione (+) " +
					"and i.progressivo = uind.progressivo (+) " +
					"and i.id_imm = uind.id_imm (+) " +
					"and uind.seq = 0 " +
					"and uind.toponimo = t.pk_id (+) ";

			this.initialize();
			/*
			 * 0: Folgio
			 * 1: Particella
			 * 2: Subalterno
			 * 3: cf
			 * 4: cognome
			 * 5: nome
			 * 6: dataNascita
			 * 7: indirizzo
			 * 8: civico
			 */
			this.setString(1, (String)listParam.get(0));
			this.setString(2, (String)listParam.get(1));
			this.setString(3, (String)listParam.get(2));
			log.debug(sql);
			prepareStatement(sql);
			java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			ArrayList listaDatiCatastali = new ArrayList();
			DatiCatastali dc = null;
			while (rs.next())
			{
				String foglio = rs.getString("FOGLIO");
				String particella = rs.getString("PARTICELLA");
				String sub = rs.getString("SUB");
				String unimm = rs.getString("UNIMM");
				Date data_inizio_val = rs.getDate("DATA_INIZIO_VAL");
				String categoria = rs.getString("CATEGORIA");				
				String classe = rs.getString("CLASSE");
				String consistenza = rs.getString("CONSISTENZA");
				String rendita = rs.getString("RENDITA");
				String sup_cat = rs.getString("SUP_CAT");
				String zona = rs.getString("ZONA");
				String edificio = rs.getString("EDIFICIO");
				String scala = rs.getString("SCALA");
				String interno = rs.getString("INTERNO");
				String piano = rs.getString("PIANO");
				String indirizzo = rs.getString("INDIRIZZO");				
				String civ1 = rs.getString("CIV1");
				
				dc = new DatiCatastali();
				if (!StringUtils.isEmpty(foglio))
					dc.setFoglio(foglio);
				if (!StringUtils.isEmpty(particella))
					dc.setParticella(particella);
				if (!StringUtils.isEmpty(sub))
					dc.setSubalternoTerreno(sub.trim());
				if (!StringUtils.isEmpty(unimm))
					dc.setSubalterno(unimm.trim());
				if (data_inizio_val != null){
					SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
					String sDatIni = df.format(data_inizio_val);
					dc.setDataInizioVal(sDatIni);
				}
				if (!StringUtils.isEmpty(categoria))
					dc.setCategoria(categoria.trim());
				else
					dc.setCategoria("");
				if (!StringUtils.isEmpty(classe))
					dc.setClasse(classe.trim());
				else
					dc.setClasse("");
				if (!StringUtils.isEmpty(consistenza))
					dc.setConsistenza(consistenza.trim());
				else
					dc.setConsistenza("");
				if (!StringUtils.isEmpty(rendita))
					dc.setRendita(rendita.trim());
				else
					dc.setRendita("");
				if (!StringUtils.isEmpty(sup_cat))
					dc.setSuperficeCatastale(sup_cat.trim());
				else
					dc.setSuperficeCatastale("");
				if (!StringUtils.isEmpty(zona))
					dc.setZona(zona.trim());
				else
					dc.setZona("");
				if (!StringUtils.isEmpty(edificio))
					dc.setEdificio(edificio.trim());
				else
					dc.setEdificio("");
				if (!StringUtils.isEmpty(scala))
					dc.setScala(scala.trim());
				else
					dc.setScala("");
				if (!StringUtils.isEmpty(interno))
					dc.setInterno(interno.trim());
				else
					dc.setInterno("");
				if (!StringUtils.isEmpty(piano))
					dc.setPiano(piano.trim());
				else
					dc.setPiano("");
				if (!StringUtils.isEmpty(indirizzo))
					dc.setIndirizzo(indirizzo.trim());
				else
					dc.setIndirizzo("");
				if (!StringUtils.isEmpty(civ1))
					dc.setCivico(civ1.trim());
				else
					dc.setCivico("");
				dc.setChiave(chiave);
				listaDatiCatastali.add(dc);
			}
			ht.put(LISTA_DETTAGLIO_DATI_CATASTALI, listaDatiCatastali);

			
			//LISTA_DETTAGLIO_TITOLARITA
			sql = "select distinct " +
					"sc.cuaa as cf, ans.cognome, ans.nome, " +
					"ans.d_nascita as d_nas, sc.perc_poss, " +
					"DECODE(sc.tipo_titolo,1,'PROPRIETA''',9,(decode(trim(sc.tipo_documento),'990',sc.tit_no_cod,'99',decode(sc.tit_no_cod,null,'TITOLO NON CODIFICATO',sc.tit_no_cod), 'ALTRO'))) AS tip_tit " +
					"from " +
					"sitiuiu ui, siticonduz_imm_all sc, anag_soggetti ans, siticomu c " +
					"where " +
					"c.codi_fisc_luna = (select uk_belfiore from ewg_tab_comuni) " +
					"and ui.foglio = ? " +
					"and ui.particella = lpad(?, 5, '0') " +
					"and ui.unimm = ? " +
					"and ui.data_fine_val = to_date('99991231','yyyymmdd') " +
					"and ui.categoria in ('A01','A02','A03','A04','A05','A06','A07','A08','A09','A11') " +
					"and sc.cuaa = ? " +
					"and ui.cod_nazionale = sc.cod_nazionale (+) " +
					"and sc.cod_nazionale = c.cod_nazionale (+) " +
					"and ui.data_fine_val = sc.data_fine (+) " +
					"and ui.foglio = sc.foglio(+) " +
					"and ui.particella = sc.particella(+) " +
					"and ui.sub = sc.sub (+) " +
					"and ui.unimm = sc.unimm (+) " +
					"and sc.data_fine = ans.data_fine_val (+) " +
					"and sc.pk_cuaa = ans.cod_soggetto (+) ";

			this.initialize();
			/*
			 * 0: Folgio
			 * 1: Particella
			 * 2: Subalterno
			 * 3: cf
			 * 4: cognome
			 * 5: nome
			 * 6: dataNascita
			 * 7: indirizzo
			 * 8: civico
			 */
			this.setString(1, (String)listParam.get(0));
			this.setString(2, (String)listParam.get(1));
			this.setString(3, (String)listParam.get(2));
			this.setString(4, (String)listParam.get(3));
			log.debug(sql);
			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			ArrayList<DatiTitolarita> listaTitolarita = new ArrayList<DatiTitolarita>();
			DatiTitolarita tit = null;
			while (rs.next())
			{
				String cf = rs.getString("CF");
				String cognome = rs.getString("COGNOME");
				String nome = rs.getString("NOME");
				Date d_nas = rs.getDate("D_NAS");
				String perc_poss = rs.getString("PERC_POSS");				
				String tip_tit = rs.getString("TIP_TIT");
				
				tit = new DatiTitolarita();
				if (!StringUtils.isEmpty(cf))
					tit.setCf(cf);
				else
					tit.setCf("");
				if (!StringUtils.isEmpty(cognome))
					tit.setCognome(cognome);
				else
					tit.setCognome("");
				if (!StringUtils.isEmpty(nome))
					tit.setNome(nome);
				else
					tit.setNome("");
				if (d_nas != null){
					SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
					String sDatNas = df.format(d_nas);
					tit.setDataNascita(sDatNas);
				}
				if (!StringUtils.isEmpty(perc_poss)){
					NumberFormat nf = new DecimalFormat("#.##");
					tit.setPercentualePossesso(nf.format(Double.parseDouble(perc_poss)));	
				}else
					tit.setPercentualePossesso("");
				if (!StringUtils.isEmpty(tip_tit))
					tit.setTipoTitolo(tip_tit);
				else
					tit.setTipoTitolo("");
				tit.setChiave(chiave);
				
				listaTitolarita.add(tit);
			}
			ht.put(LISTA_DETTAGLIO_TITOLARITA, listaTitolarita);

			/*
			 * LISTA_DETTAGLIO_ALTRE_TITOLARITA
			 */
			sql = "select distinct " +
					"sc.cuaa as cf, ans.cognome, ans.nome, " +
					"ans.d_nascita as d_nas, sc.perc_poss, " +
					"DECODE(sc.tipo_titolo,1,'PROPRIETA''',9,(decode(trim(sc.tipo_documento),'990',sc.tit_no_cod,'99',decode(sc.tit_no_cod,null,'TITOLO NON CODIFICATO',sc.tit_no_cod), 'ALTRO'))) AS tip_tit " +
					"from " +
					"sitiuiu ui, siticonduz_imm_all sc, anag_soggetti ans, siticomu c " +
					"where " +
					"c.codi_fisc_luna = (select uk_belfiore from ewg_tab_comuni) " +
					"and ui.foglio = ? " +
					"and ui.particella = lpad(?, 5, '0') " +
					"and ui.unimm = ? " +
					"and ui.data_fine_val = to_date('99991231','yyyymmdd') " +
					"and ui.categoria in ('A01','A02','A03','A04','A05','A06','A07','A08','A09','A11') " +
					"and sc.cuaa <> ? " +
					"and ui.cod_nazionale = sc.cod_nazionale (+) " +
					"and sc.cod_nazionale = c.cod_nazionale (+) " +
					"and ui.data_fine_val = sc.data_fine (+) " +
					"and ui.foglio = sc.foglio(+) " +
					"and ui.particella = sc.particella(+) " +
					"and ui.sub = sc.sub (+) " +
					"and ui.unimm = sc.unimm (+) " +
					"and sc.data_fine = ans.data_fine_val (+) " +
					"and sc.pk_cuaa = ans.cod_soggetto (+) ";

			this.initialize();
			/*
			 * 0: Folgio
			 * 1: Particella
			 * 2: Subalterno
			 * 3: cf
			 * 4: cognome
			 * 5: nome
			 * 6: dataNascita
			 * 7: indirizzo
			 * 8: civico
			 */
			this.setString(1, (String)listParam.get(0));
			this.setString(2, (String)listParam.get(1));
			this.setString(3, (String)listParam.get(2));
			this.setString(4, (String)listParam.get(3));
			log.debug(sql);
			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			ArrayList<DatiTitolarita> listaAltreTitolarita = new ArrayList<DatiTitolarita>();
			DatiTitolarita alt = null;
			while (rs.next())
			{
				String cf = rs.getString("CF");
				String cognome = rs.getString("COGNOME");
				String nome = rs.getString("NOME");
				Date d_nas = rs.getDate("D_NAS");
				String perc_poss = rs.getString("PERC_POSS");				
				String tip_tit = rs.getString("TIP_TIT");
				
				alt = new DatiTitolarita();
				if (!StringUtils.isEmpty(cf))
					alt.setCf(cf);
				else
					alt.setCf("");
				if (!StringUtils.isEmpty(cognome))
					alt.setCognome(cognome);
				else
					alt.setCognome("");
				if (!StringUtils.isEmpty(nome))
					alt.setNome(nome);
				else
					alt.setNome("");
				if (d_nas != null){
					SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
					String sDatNas = df.format(d_nas);
					alt.setDataNascita(sDatNas);
				}
				if (!StringUtils.isEmpty(perc_poss)){
					NumberFormat nf = new DecimalFormat("#.##");
					alt.setPercentualePossesso(nf.format(Double.parseDouble(perc_poss)));	
				}else
					alt.setPercentualePossesso("");
				if (!StringUtils.isEmpty(tip_tit))
					alt.setTipoTitolo(tip_tit);
				else
					alt.setTipoTitolo("");
				
				alt.setChiave(chiave);
				
				listaAltreTitolarita.add(alt);
			}
			ht.put(LISTA_DETTAGLIO_ALTRE_TITOLARITA, listaAltreTitolarita);

			/*
			 * LISTA_DETTAGLIO_TITOLARE
			 */
			sql = "select distinct " +
					"codfisc, cognome, nome, sesso, data_nascita, posiz_ana, " +
					"viasedime || '  ' || descrizione_via as via, " +
					"civ_liv1 as civico, data_imm_persona, data_emi_persona, data_mor_persona " +
					"from " +
					"PERSONA_CIVICI_V " +
					"where " +
					"(CODFISC = ? OR (cognome = ? and nome = ? and data_nascita = nvl(to_date(?,'yyyy-mm-dd'), to_date('0001-01-01', 'yyyy-mm-dd'))  ))";

			this.initialize();
			/*
			 * 0: Folgio
			 * 1: Particella
			 * 2: Subalterno
			 * 3: cf
			 * 4: cognome
			 * 5: nome
			 * 6: dataNascita
			 * 7: indirizzo
			 * 8: civico
			 */
			this.setString(1, (String)listParam.get(3));
			this.setString(2, (String)listParam.get(4));
			this.setString(3, (String)listParam.get(5));
			//if (listParam.get(6) != null && !listParam.get(6).toString().equals("null")){
			this.setString(4, (String)listParam.get(6));
			//	sql += "and data_nascita = nvl(?, to_date('0001-01-01', 'yyyy-mm-dd'))  ))";
			//}else{
			//	sql += "))";
			//}
			log.debug(sql);
			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			ArrayList<PertinenzeAbitTitolare> listaTitolare = new ArrayList<PertinenzeAbitTitolare>();
			PertinenzeAbitTitolare pat = null;
			while (rs.next())
			{
				String codfisc = rs.getString("CODFISC");
				String cognome = rs.getString("COGNOME");
				String nome = rs.getString("NOME");
				String sesso = rs.getString("SESSO");
				Date data_nascita = rs.getDate("DATA_NASCITA");				
				String posiz_ana = rs.getString("POSIZ_ANA");
				String via = rs.getString("VIA");
				String civico = rs.getString("CIVICO");
				Date data_imm_persona = rs.getDate("DATA_IMM_PERSONA");
				Date data_emi_persona = rs.getDate("DATA_EMI_PERSONA");
				Date data_mor_persona = rs.getDate("DATA_MOR_PERSONA");
				
				pat = new PertinenzeAbitTitolare();
				if (!StringUtils.isEmpty(codfisc))
					pat.setCf(codfisc);
				else
					pat.setCf("");
				if (!StringUtils.isEmpty(cognome))
					pat.setCognome(cognome);
				else
					pat.setCognome("");
				if (!StringUtils.isEmpty(nome))
					pat.setNome(nome);
				else
					pat.setNome("");
				if (!StringUtils.isEmpty(sesso))
					pat.setSesso(sesso);
				else
					pat.setSesso("");
				if (data_nascita != null){
					SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
					String sDatNas = df.format(data_nascita);
					pat.setDataNascita(sDatNas);
				}
				if (!StringUtils.isEmpty(posiz_ana))
					pat.setPosizioneAnagrafica(posiz_ana);
				else
					pat.setPosizioneAnagrafica("");
				if (!StringUtils.isEmpty(via))
					pat.setIndirizzo(via);
				else
					pat.setIndirizzo("");
				if (!StringUtils.isEmpty(civico))
					pat.setCivico(civico);
				else
					pat.setCivico("");
				if (data_imm_persona != null){
					SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
					String sDatImm = df.format(data_imm_persona);
					pat.setDataImmigrazione(sDatImm);
				}
				if (data_emi_persona != null){
					SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
					String sDatEmi = df.format(data_emi_persona);
					pat.setDataEmigrazione(sDatEmi);
				}
				if (data_mor_persona != null){
					SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
					String sDatMor = df.format(data_mor_persona);
					pat.setDataMorte(sDatMor);
				}
				pat.setChiave(chiave);
				
				listaTitolare.add(pat);
			}
			ht.put(LISTA_DETTAGLIO_TITOLARE, listaTitolare);
			/*
			 * IL CIVICO DI RESIDENZA RICADE NELLA PERTINENZA CARTOGRAFICA DELLA U.I.
			 */
			sql = "select " +
			"decode(IS_RESIDENTE_IN_PROP(?, ?, ?, nvl(to_date(?,'yyyy-mm-dd'), to_date('0001-01-01', 'yyyy-mm-dd')), ?, ?), -1, 'Non determinabile', 0, 'No', 1, 'Si') as is_residente_in_prop " +
			"FROM " +
			"DUAL ";

			this.initialize();
			/*
			 * 0: Folgio
			 * 1: Particella
			 * 2: Subalterno
			 * 3: cf
			 * 4: cognome
			 * 5: nome
			 * 6: dataNascita
			 * 7: indirizzo
			 * 8: civico
			 */
			this.setString(1, (String)listParam.get(3));
			this.setString(2, (String)listParam.get(4));
			this.setString(3, (String)listParam.get(5));
			this.setString(4, (String)listParam.get(6));
			this.setString(5, (String)listParam.get(0));
			this.setString(6, (String)listParam.get(1));
			log.debug(sql);
			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			ArrayList<String> listaEsitoQuery = new ArrayList<String>();
			while (rs.next()){
				String outcome = rs.getString("IS_RESIDENTE_IN_PROP");
				
				if (!StringUtils.isEmpty(outcome))
					log.debug(outcome);
				else
					outcome = "";
				
				listaEsitoQuery.add(outcome);
			}
			ht.put(CIVICO_IN_PERTINENZA_CARTOGRAFICA, listaEsitoQuery);
			/*
			 * U.I. RESIDENZIALI NELLA PERTINENZA CARTOGRAFICA, DI CUI IL TITOLARE 
			 * SELEZIONATO è PROPRIETARIO
			 */
			sql = "select distinct " +
				  "ui.foglio, ui.particella, ui.sub, ui.unimm, ui.data_inizio_val, " +
				  "ui.categoria, ui.classe, ui.consistenza, ui.rendita, ui.sup_cat, ui.zona, ui.edificio, ui.scala, " +
				  "ui.interno, ui.piano, " +
				  "t.descr ||' '||uind.ind as indirizzo, uind.civ1, " +
				  "sc.perc_poss, DECODE(sc.tipo_titolo,1,'PROPRIETA''',9,(decode(trim(sc.tipo_documento),'990',sc.tit_no_cod,'99',decode(sc.tit_no_cod,null,'TITOLO NON CODIFICATO',sc.tit_no_cod), 'ALTRO'))) AS tip_tit " +
				  "FROM " +
				  "sitiuiu ui, siticonduz_imm_all sc, anag_soggetti ans, load_cat_uiu_id i, " +
				  "load_cat_uiu_ind uind, cat_d_toponimi t, siticomu c " +
				  "WHERE " +
				  "c.codi_fisc_luna=(select uk_belfiore from ewg_tab_comuni) " +
				  "and ui.foglio = ? " +
				  "and ui.particella = LPAD (?, 5, '0') " +
				  "and ui.data_fine_val=to_date('99991231','yyyymmdd') " +
				  "and ui.categoria in ('A01','A02','A03','A04','A05','A06','A07','A08','A09','A11') " +
				  "and sc.cuaa= ? " +
				  "and ui.cod_nazionale = sc.cod_nazionale (+) " +
				  "and ui.data_fine_val = sc.data_fine (+) " +
				  "and ui.foglio = sc.foglio (+) " +
				  "and ui.particella = sc.particella (+) " +
				  "and ui.sub=sc.sub(+) " +
				  "and ui.unimm = sc.unimm (+) " +
				  "and sc.data_fine = ans.data_fine_val (+) " +
				  "and sc.pk_cuaa=ans.cod_soggetto (+) " +
				  "and sc.cod_nazionale = c.cod_nazionale(+) " +
				  "and lpad(ui.foglio, 4, '0')=i.foglio (+) " +
				  "and ui.particella = i.mappale (+) " +
				  "and decode(ui.unimm, 0, ' ', lpad(ui.unimm, 4, '0')) = i.sub (+) " +
				  "and i.codi_fisc_luna = uind.codi_fisc_luna (+) " +
				  "and i.sezione = uind.sezione (+) " +
				  "and i.progressivo = uind.progressivo (+) " +
				  "and i.id_imm = uind.id_imm (+) " +
				  "and uind.seq = 0 " +
				  "and uind.toponimo = t.pk_id(+) " +
				  "UNION " +
				  "SELECT DISTINCT " +
				  "ui.foglio, ui.particella, ui.sub, ui.unimm, ui.data_inizio_val, " +
				  "ui.categoria, ui.classe, ui.consistenza, ui.rendita, ui.sup_cat, ui.zona, ui.edificio, ui.scala, " +
				  "ui.interno, ui.piano, " +
				  "t.descr ||' '||uind.ind as indirizzo, uind.civ1, " +
				  "sc.perc_poss, DECODE(sc.tipo_titolo,1,'PROPRIETA''',9,(decode(trim(sc.tipo_documento),'990',sc.tit_no_cod,'99',decode(sc.tit_no_cod,null,'TITOLO NON CODIFICATO',sc.tit_no_cod), 'ALTRO'))) AS tip_tit " +
				  "FROM " +
				  "sitiuiu ui, siticonduz_imm_all sc, anag_soggetti ans, load_cat_uiu_id i, " +
				  "load_cat_uiu_ind uind, cat_d_toponimi t, siticomu c " +
				  "WHERE " +
				  "c.codi_fisc_luna=(select uk_belfiore from ewg_tab_comuni) " +
				  "and ui.data_fine_val=to_date('99991231','yyyymmdd') " +
				  "and ui.categoria in ('A01','A02','A03','A04','A05','A06','A07','A08','A09','A11') " +
				  "and sc.cuaa= ? " +
				  "and ui.cod_nazionale=sc.cod_nazionale (+) " +
				  "and ui.data_fine_val=sc.data_fine (+) " +
				  "and ui.foglio=sc.foglio(+) " +
				  "and ui.particella=sc.particella(+) " +
				  "and ui.sub=sc.sub(+) " +
				  "and ui.unimm=sc.unimm(+) " +
				  "and sc.data_fine= ans.data_fine_val (+) " +
				  "and sc.pk_cuaa=ans.cod_soggetto (+) " +
				  "and sc.cod_nazionale=c.cod_nazionale(+) " +
				  "and lpad(ui.foglio,4,'0')=i.foglio(+) " +
				  "and ui.particella= i.mappale(+) " +
				  "and decode(ui.unimm, 0, ' ', lpad(ui.unimm, 4, '0')) = i.sub (+) " +
				  "and i.codi_fisc_luna=uind.codi_fisc_luna(+) " +
				  "and i.sezione=uind.sezione(+) " +
				  "and i.progressivo=uind.progressivo(+) " +
				  "and i.id_imm=uind.id_imm(+) " +
				  "and uind.seq=0 " +
				  "and uind.toponimo = t.pk_id (+) " +
				  "and t.descr ||' '||uind.IND = ? " +
				  "and uind.CIV1 = ? " +
				  "UNION " +
				  "SELECT DISTINCT " +
				  "ui.foglio, ui.particella, ui.sub, ui.unimm, ui.data_inizio_val, " +
				  "ui.categoria, ui.classe, ui.consistenza, ui.rendita, ui.sup_cat, ui.zona, ui.edificio, ui.scala, " +
				  "ui.interno, ui.piano, " +
				  "t.descr ||' '||uind.ind as indirizzo, uind.civ1, " +
				  "sc.perc_poss, DECODE(sc.tipo_titolo,1,'PROPRIETA''',9,(decode(trim(sc.tipo_documento),'990',sc.tit_no_cod,'99',decode(sc.tit_no_cod,null,'TITOLO NON CODIFICATO',sc.tit_no_cod), 'ALTRO'))) AS tip_tit " +
				  "FROM " +
				  "sitiuiu ui, siticonduz_imm_all sc, anag_soggetti ans, load_cat_uiu_id i, " +
				  "load_cat_uiu_ind uind, cat_d_toponimi t, siticomu c " +
				  "WHERE " +
				  "c.codi_fisc_luna=(select uk_belfiore from ewg_tab_comuni) " +
				  "and ui.foglio||' '||ui.particella in " +
				  "(select distinct pa.foglio||' '||pa.particella " +
				  "FROM " +
				  "sitipart pa " +
				  "WHERE " +
				  "sdo_relate ( " +
				  "pa.shape, " +
				  "(SELECT " +
				  "pe.shape " +
				  "FROM " +
				  "pertinenze pe, sitipart pa, siticomu cc " +
				  "WHERE " +
				  "sdo_relate (pe.shape, pa.shape,'MASK=ANYINTERACT') = 'TRUE' " +
				  "and sdo_geom.sdo_area(sdo_geom.sdo_intersection(pe.shape, pa.shape,0.005),0.005) > 0.5*sdo_geom.sdo_area(pa.shape,0.005) " +
				  "and cc.codi_fisc_luna=(select uk_belfiore from ewg_tab_comuni) " +
				  "and pa.cod_nazionale = cc.cod_nazionale " +
				  "and pa.foglio = ? " +
				  "AND pa.particella = LPAD (?, 5, '0') " +
				  "AND pa.data_fine_val = TO_DATE ('99991231', 'yyyymmdd') ), 'MASK=ANYINTERACT') = 'TRUE' " +
				  "and sdo_geom.sdo_area(sdo_geom.sdo_intersection(pa.shape, " +
				  "(SELECT " +
				  "pe.shape " +
				  "FROM " +
				  "pertinenze pe, sitipart pa, siticomu cc " +
				  "WHERE " +
				  "sdo_relate (pe.shape, pa.shape,'MASK=ANYINTERACT') = 'TRUE' " +
				  "and sdo_geom.sdo_area(sdo_geom.sdo_intersection(pe.shape, pa.shape,0.005),0.005) > 0.5*sdo_geom.sdo_area(pa.shape,0.005) " +
				  "and cc.codi_fisc_luna=(select uk_belfiore from ewg_tab_comuni) " +
				  "and pa.cod_nazionale = cc.cod_nazionale " +
				  "and pa.foglio = ? " +
				  "AND pa.particella = LPAD (?, 5, '0') " +
				  "AND pa.data_fine_val = TO_DATE ('99991231', 'yyyymmdd') ),0.005),0.005) > 0.5*sdo_geom.sdo_area(pa.shape,0.005) ) " +
				  "and ui.data_fine_val=to_date('99991231','yyyymmdd') " +
				  "and ui.categoria in ('A01','A02','A03','A04','A05','A06','A07','A08','A09','A11') " +
				  "and sc.cuaa= ? " +
				  "and ui.cod_nazionale=sc.cod_nazionale (+) " +
				  "and ui.data_fine_val=sc.data_fine (+) " +
				  "and ui.foglio=sc.foglio (+) " +
				  "and ui.particella=sc.particella(+) " +
				  "and ui.sub=sc.sub(+) " +
				  "and ui.unimm=sc.unimm(+) " +
				  "and sc.data_fine= ans.data_fine_val (+) " +
				  "and sc.pk_cuaa=ans.cod_soggetto (+) " +
				  "and sc.cod_nazionale=c.cod_nazionale(+) " +
				  "and lpad(ui.foglio,4,'0')=i.foglio(+) " +
				  "and ui.particella= i.mappale(+) " +
				  "and decode(ui.unimm, 0, ' ', lpad(ui.unimm, 4, '0')) = i.sub (+) " +
				  "and i.codi_fisc_luna=uind.codi_fisc_luna(+) " +
				  "and i.sezione=uind.sezione(+) " +
				  "and i.progressivo=uind.progressivo(+) " +
				  "and i.id_imm=uind.id_imm(+) " +
				  "and uind.seq=0 " +
				  "and uind.toponimo = t.pk_id(+) " +
				  "order by foglio, particella, unimm, categoria ";

/* 
			sql = "select distinct " +
			"ui.foglio, ui.particella, ui.sub, ui.unimm, ui.data_inizio_val, " +
			"ui.categoria, ui.classe, ui.consistenza, ui.rendita, ui.sup_cat, ui.zona, ui.edificio, ui.scala, " +
			"ui.interno, ui.piano, t.descr ||' '||uind.ind as indirizzo, uind.civ1, sc.perc_poss, decode(sc.tipo_titolo,'1','PROPRIETA''','9','ALTRO') as tip_tit " +
			"FROM " +
			"sitiuiu ui, siticonduz_imm_all sc, anag_soggetti ans, load_cat_uiu_id i, " +
			"load_cat_uiu_ind uind, cat_d_toponimi t " +
			"WHERE " +
			"ui.cod_nazionale=(select uk_belfiore from ewg_tab_comuni) " +
			"and ui.foglio||' '||ui.particella in " +
			"(select distinct pa.foglio||' '||pa.particella " +
			"from sitipart pa " +
			"where sdo_relate ( " +
			"pa.shape, " +
			"(SELECT pe.shape " +
			"FROM pertinenze pe, sitipart pa " +
			"WHERE sdo_relate (pe.shape, pa.shape, 'MASK=ANYINTERACT') = 'TRUE' " +
			"and sdo_geom.sdo_area(sdo_geom.sdo_intersection(pe.shape, pa.shape,0.005),0.005) > 0.5 * sdo_geom.sdo_area(pa.shape,0.005) " +
			"and pa.cod_nazionale = (select uk_belfiore from ewg_tab_comuni) " +
			"and pa.foglio = ? " +
			"AND pa.particella = LPAD (?, 5, '0') " +
			"AND pa.data_fine_val = TO_DATE ('99991231', 'yyyymmdd')), " +
			"'MASK=ANYINTERACT') = 'TRUE' " +
			"and sdo_geom.sdo_area(sdo_geom.sdo_intersection(pa.shape, " +
			"(SELECT pe.shape FROM pertinenze pe, sitipart pa " +
			"WHERE sdo_relate (pe.shape, pa.shape,'MASK=ANYINTERACT') = 'TRUE' " +
			"and sdo_geom.sdo_area(sdo_geom.sdo_intersection(pe.shape, pa.shape,0.005),0.005) > 0.5*sdo_geom.sdo_area(pa.shape,0.005) " +
			"and pa.cod_nazionale=(select uk_belfiore from ewg_tab_comuni) " +
			"and pa.foglio = ? " +
			"AND pa.particella = LPAD (?, 5, '0') " +
			"AND pa.data_fine_val = TO_DATE ('99991231', 'yyyymmdd')),0.005),0.005) > 0.5*sdo_geom.sdo_area(pa.shape,0.005)) " +
			"and ui.data_fine_val=to_date('99991231','yyyymmdd') " +
			"and ui.categoria in ('A01','A02','A03','A04','A05','A06','A07','A08','A09','A11') " +
			"and sc.cuaa = ? " +
			"and ui.cod_nazionale = sc.cod_nazionale (+) " +
			"and ui.data_fine_val = sc.data_fine (+) " +
			"and ui.foglio = sc.foglio (+) " +
			"and ui.particella = sc.particella (+) " +
			"and ui.sub = sc.sub (+) " +
			"and ui.unimm = sc.unimm (+) " +
			"and sc.data_fine = ans.data_fine_val (+) " +
			"and sc.pk_cuaa = ans.cod_soggetto (+) " +
			"and ui.cod_nazionale = i.codi_fisc_luna (+) " +
			"and lpad(ui.foglio, 4, '0') = i.foglio (+) " +
			"and ui.particella = i.mappale (+) " +
			"and lpad(ui.unimm, 4, '0') = i.sub (+) " +
			"and i.codi_fisc_luna = uind.codi_fisc_luna (+) " +
			"and i.sezione = uind.sezione (+) " +
			"and i.progressivo = uind.progressivo (+) " +
			"and i.id_imm = uind.id_imm (+) " +
			"and uind.seq = 0 " +
			"and uind.toponimo = t.pk_id (+) " +
			"order by ui.foglio, ui.particella, ui.unimm, ui.categoria ";
*/			
			this.initialize();
			/*
			 * 0: Folgio
			 * 1: Particella
			 * 2: Subalterno
			 * 3: cf
			 * 4: cognome
			 * 5: nome
			 * 6: dataNascita
			 * 7: indirizzo
			 * 8: civico
			 */
			this.setString(1, (String)listParam.get(0));
			this.setString(2, (String)listParam.get(1));
			this.setString(3, (String)listParam.get(3));
			this.setString(4, (String)listParam.get(3));
			this.setString(5, (String)listParam.get(7));
			this.setString(6, (String)listParam.get(8));
			this.setString(7, (String)listParam.get(0));
			this.setString(8, (String)listParam.get(1));
			this.setString(9, (String)listParam.get(0));
			this.setString(10, (String)listParam.get(1));
			this.setString(11, (String)listParam.get(3));
			
			log.debug(sql);
			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			ArrayList<PertinenzeAbitDatiCatastali> listaUiResidenzialeInPertinenza = new ArrayList<PertinenzeAbitDatiCatastali>();
			PertinenzeAbitDatiCatastali datiCatastali = null;
			while (rs.next())
			{
				String foglio = rs.getString("FOGLIO");
				String particella = rs.getString("PARTICELLA");
				String sub = rs.getString("SUB");
				String unimm = rs.getString("UNIMM");
				Date data_inizio_val = rs.getDate("DATA_INIZIO_VAL");
				String categoria = rs.getString("CATEGORIA");				
				String classe = rs.getString("CLASSE");
				String consistenza = rs.getString("CONSISTENZA");
				String rendita = rs.getString("RENDITA");
				String sup_cat = rs.getString("SUP_CAT");
				String zona = rs.getString("ZONA");
				String edificio = rs.getString("EDIFICIO");
				String scala = rs.getString("SCALA");
				String interno = rs.getString("INTERNO");
				String piano = rs.getString("PIANO");
				String indirizzo = rs.getString("INDIRIZZO");				
				String civ1 = rs.getString("CIV1");
				String pp = rs.getString("PERC_POSS");
				String tt = rs.getString("TIP_TIT");

				datiCatastali = new PertinenzeAbitDatiCatastali();
				if (!StringUtils.isEmpty(foglio))
					datiCatastali.setFoglio(foglio);
				if (!StringUtils.isEmpty(particella))
					datiCatastali.setParticella(particella);
				if (!StringUtils.isEmpty(sub))
					datiCatastali.setSubalternoTerreno(sub.trim());
				if (!StringUtils.isEmpty(unimm))
					datiCatastali.setSubalterno(unimm.trim());
				if (data_inizio_val != null){
					SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
					String sDatIni = df.format(data_inizio_val);
					datiCatastali.setDataInizioVal(sDatIni);
				}
				if (!StringUtils.isEmpty(categoria))
					datiCatastali.setCategoria(categoria.trim());
				else
					datiCatastali.setCategoria("");
				if (!StringUtils.isEmpty(classe))
					datiCatastali.setClasse(classe.trim());
				else
					datiCatastali.setClasse("");
				if (!StringUtils.isEmpty(consistenza))
					datiCatastali.setConsistenza(consistenza.trim());
				else
					datiCatastali.setConsistenza("");
				if (!StringUtils.isEmpty(rendita))
					datiCatastali.setRendita(rendita.trim());
				else
					datiCatastali.setRendita("");
				if (!StringUtils.isEmpty(sup_cat))
					datiCatastali.setSuperficeCatastale(sup_cat.trim());
				else
					datiCatastali.setSuperficeCatastale("");
				if (!StringUtils.isEmpty(zona))
					datiCatastali.setZona(zona.trim());
				else
					datiCatastali.setZona("");
				if (!StringUtils.isEmpty(edificio))
					datiCatastali.setEdificio(edificio.trim());
				else
					datiCatastali.setEdificio("");
				if (!StringUtils.isEmpty(scala))
					datiCatastali.setScala(scala.trim());
				else
					datiCatastali.setScala("");
				if (!StringUtils.isEmpty(interno))
					datiCatastali.setInterno(interno.trim());
				else
					datiCatastali.setInterno("");
				if (!StringUtils.isEmpty(piano))
					datiCatastali.setPiano(piano.trim());
				else
					datiCatastali.setPiano("");
				if (!StringUtils.isEmpty(indirizzo))
					datiCatastali.setIndirizzo(indirizzo.trim());
				else
					datiCatastali.setIndirizzo("");
				if (!StringUtils.isEmpty(civ1))
					datiCatastali.setCivico(civ1.trim());
				else
					datiCatastali.setCivico("");
				if (!StringUtils.isEmpty(pp))
					datiCatastali.setPercentualePossesso(pp.trim());
				else
					datiCatastali.setPercentualePossesso("");
				if (!StringUtils.isEmpty(tt))
					datiCatastali.setTipoTitolo(tt);
				else
					datiCatastali.setTipoTitolo("");
				datiCatastali.setChiave(chiave);
				listaUiResidenzialeInPertinenza.add(datiCatastali);
			}
			ht.put(UI_RESIDENZIALE_IN_PERTINENZA, listaUiResidenzialeInPertinenza);
			/*
			 * U.I. di categoria C2, C6, C7 nella pertinenza cartografica, di cui 
			 * il titolare selezionato è proprietario
			 */
			sql = "select distinct " +
			  "ui.foglio, ui.particella, ui.sub, ui.unimm, ui.data_inizio_val, " +
			  "ui.categoria, ui.classe, ui.consistenza, ui.rendita, ui.sup_cat, ui.zona, ui.edificio, ui.scala, " +
			  "ui.interno, ui.piano, " +
			  "t.descr ||' '||uind.ind as indirizzo, uind.civ1, " +
			  "sc.perc_poss, DECODE(sc.tipo_titolo,1,'PROPRIETA''',9,(decode(trim(sc.tipo_documento),'990',sc.tit_no_cod,'99',decode(sc.tit_no_cod,null,'TITOLO NON CODIFICATO',sc.tit_no_cod), 'ALTRO'))) AS tip_tit " +
			  "FROM " +
			  "sitiuiu ui, siticonduz_imm_all sc, anag_soggetti ans, load_cat_uiu_id i, " +
			  "load_cat_uiu_ind uind, cat_d_toponimi t, siticomu c " +
			  "WHERE " +
			  "c.codi_fisc_luna=(select uk_belfiore from ewg_tab_comuni) " +
			  "and ui.foglio = ? " +
			  "and ui.particella = LPAD (?, 5, '0') " +
			  "and ui.data_fine_val=to_date('99991231','yyyymmdd') " +
			  "and ui.categoria in ('C02','C06','C07') " +
			  "and sc.cuaa= ? " +
			  "and ui.cod_nazionale = sc.cod_nazionale (+) " +
			  "and ui.data_fine_val = sc.data_fine (+) " +
			  "and ui.foglio = sc.foglio (+) " +
			  "and ui.particella = sc.particella (+) " +
			  "and ui.sub=sc.sub(+) " +
			  "and ui.unimm = sc.unimm (+) " +
			  "and sc.data_fine = ans.data_fine_val (+) " +
			  "and sc.pk_cuaa=ans.cod_soggetto (+) " +
			  "and sc.cod_nazionale = c.cod_nazionale(+) " +
			  "and lpad(ui.foglio, 4, '0')=i.foglio (+) " +
			  "and ui.particella = i.mappale (+) " +
			  "and decode(ui.unimm, 0, ' ', lpad(ui.unimm, 4, '0')) = i.sub (+) " +
			  "and i.codi_fisc_luna = uind.codi_fisc_luna (+) " +
			  "and i.sezione = uind.sezione (+) " +
			  "and i.progressivo = uind.progressivo (+) " +
			  "and i.id_imm = uind.id_imm (+) " +
			  "and uind.seq = 0 " +
			  "and uind.toponimo = t.pk_id(+) " +
			  "UNION " +
			  "SELECT DISTINCT " +
			  "ui.foglio, ui.particella, ui.sub, ui.unimm, ui.data_inizio_val, " +
			  "ui.categoria, ui.classe, ui.consistenza, ui.rendita, ui.sup_cat, ui.zona, ui.edificio, ui.scala, " +
			  "ui.interno, ui.piano, " +
			  "t.descr ||' '||uind.ind as indirizzo, uind.civ1, " +
			  "sc.perc_poss, DECODE(sc.tipo_titolo,1,'PROPRIETA''',9,(decode(trim(sc.tipo_documento),'990',sc.tit_no_cod,'99',decode(sc.tit_no_cod,null,'TITOLO NON CODIFICATO',sc.tit_no_cod), 'ALTRO'))) AS tip_tit " +
			  "FROM " +
			  "sitiuiu ui, siticonduz_imm_all sc, anag_soggetti ans, load_cat_uiu_id i, " +
			  "load_cat_uiu_ind uind, cat_d_toponimi t, siticomu c " +
			  "WHERE " +
			  "c.codi_fisc_luna=(select uk_belfiore from ewg_tab_comuni) " +
			  "and ui.data_fine_val=to_date('99991231','yyyymmdd') " +
			  "and ui.categoria in ('C02','C06','C07') " +
			  "and sc.cuaa= ? " +
			  "and ui.cod_nazionale=sc.cod_nazionale (+) " +
			  "and ui.data_fine_val=sc.data_fine (+) " +
			  "and ui.foglio=sc.foglio(+) " +
			  "and ui.particella=sc.particella(+) " +
			  "and ui.sub=sc.sub(+) " +
			  "and ui.unimm=sc.unimm(+) " +
			  "and sc.data_fine= ans.data_fine_val (+) " +
			  "and sc.pk_cuaa=ans.cod_soggetto (+) " +
			  "and sc.cod_nazionale=c.cod_nazionale(+) " +
			  "and lpad(ui.foglio,4,'0')=i.foglio(+) " +
			  "and ui.particella= i.mappale(+) " +
			  "and decode(ui.unimm, 0, ' ', lpad(ui.unimm, 4, '0')) = i.sub (+) " +
			  "and i.codi_fisc_luna=uind.codi_fisc_luna(+) " +
			  "and i.sezione=uind.sezione(+) " +
			  "and i.progressivo=uind.progressivo(+) " +
			  "and i.id_imm=uind.id_imm(+) " +
			  "and uind.seq=0 " +
			  "and uind.toponimo = t.pk_id (+) " +
			  "and t.descr ||' '||uind.IND = ? " +
			  "and uind.CIV1 = ? " +
			  "UNION " +
			  "SELECT DISTINCT " +
			  "ui.foglio, ui.particella, ui.sub, ui.unimm, ui.data_inizio_val, " +
			  "ui.categoria, ui.classe, ui.consistenza, ui.rendita, ui.sup_cat, ui.zona, ui.edificio, ui.scala, " +
			  "ui.interno, ui.piano, " +
			  "t.descr ||' '||uind.ind as indirizzo, uind.civ1, " +
			  "sc.perc_poss, DECODE(sc.tipo_titolo,1,'PROPRIETA''',9,(decode(trim(sc.tipo_documento),'990',sc.tit_no_cod,'99',decode(sc.tit_no_cod,null,'TITOLO NON CODIFICATO',sc.tit_no_cod), 'ALTRO'))) AS tip_tit " +
			  "FROM " +
			  "sitiuiu ui, siticonduz_imm_all sc, anag_soggetti ans, load_cat_uiu_id i, " +
			  "load_cat_uiu_ind uind, cat_d_toponimi t, siticomu c " +
			  "WHERE " +
			  "c.codi_fisc_luna=(select uk_belfiore from ewg_tab_comuni) " +
			  "and ui.foglio||' '||ui.particella in " +
			  "(select distinct pa.foglio||' '||pa.particella " +
			  "FROM " +
			  "sitipart pa " +
			  "WHERE " +
			  "sdo_relate ( " +
			  "pa.shape, " +
			  "(SELECT " +
			  "pe.shape " +
			  "FROM " +
			  "pertinenze pe, sitipart pa, siticomu cc " +
			  "WHERE " +
			  "sdo_relate (pe.shape, pa.shape,'MASK=ANYINTERACT') = 'TRUE' " +
			  "and sdo_geom.sdo_area(sdo_geom.sdo_intersection(pe.shape, pa.shape,0.005),0.005) > 0.5*sdo_geom.sdo_area(pa.shape,0.005) " +
			  "and cc.codi_fisc_luna=(select uk_belfiore from ewg_tab_comuni) " +
			  "and pa.cod_nazionale = cc.cod_nazionale " +
			  "and pa.foglio = ? " +
			  "AND pa.particella = LPAD (?, 5, '0') " +
			  "AND pa.data_fine_val = TO_DATE ('99991231', 'yyyymmdd') ), 'MASK=ANYINTERACT') = 'TRUE' " +
			  "and sdo_geom.sdo_area(sdo_geom.sdo_intersection(pa.shape, " +
			  "(SELECT " +
			  "pe.shape " +
			  "FROM " +
			  "pertinenze pe, sitipart pa, siticomu cc " +
			  "WHERE " +
			  "sdo_relate (pe.shape, pa.shape,'MASK=ANYINTERACT') = 'TRUE' " +
			  "and sdo_geom.sdo_area(sdo_geom.sdo_intersection(pe.shape, pa.shape,0.005),0.005) > 0.5*sdo_geom.sdo_area(pa.shape,0.005) " +
			  "and cc.codi_fisc_luna=(select uk_belfiore from ewg_tab_comuni) " +
			  "and pa.cod_nazionale = cc.cod_nazionale " +
			  "and pa.foglio = ? " +
			  "AND pa.particella = LPAD (?, 5, '0') " +
			  "AND pa.data_fine_val = TO_DATE ('99991231', 'yyyymmdd') ),0.005),0.005) > 0.5*sdo_geom.sdo_area(pa.shape,0.005) ) " +
			  "and ui.data_fine_val=to_date('99991231','yyyymmdd') " +
			  "and ui.categoria in ('C02','C06','C07') " +
			  "and sc.cuaa= ? " +
			  "and ui.cod_nazionale=sc.cod_nazionale (+) " +
			  "and ui.data_fine_val=sc.data_fine (+) " +
			  "and ui.foglio=sc.foglio (+) " +
			  "and ui.particella=sc.particella(+) " +
			  "and ui.sub=sc.sub(+) " +
			  "and ui.unimm=sc.unimm(+) " +
			  "and sc.data_fine= ans.data_fine_val (+) " +
			  "and sc.pk_cuaa=ans.cod_soggetto (+) " +
			  "and sc.cod_nazionale=c.cod_nazionale(+) " +
			  "and lpad(ui.foglio,4,'0')=i.foglio(+) " +
			  "and ui.particella= i.mappale(+) " +
			  "and decode(ui.unimm, 0, ' ', lpad(ui.unimm, 4, '0')) = i.sub (+) " +
			  "and i.codi_fisc_luna=uind.codi_fisc_luna(+) " +
			  "and i.sezione=uind.sezione(+) " +
			  "and i.progressivo=uind.progressivo(+) " +
			  "and i.id_imm=uind.id_imm(+) " +
			  "and uind.seq=0 " +
			  "and uind.toponimo = t.pk_id(+) " +
			  "order by foglio, particella, unimm, categoria ";
			
/*			
			sql = "select distinct " +
			"ui.foglio, ui.particella, ui.sub, ui.unimm, ui.data_inizio_val, " +
			"ui.categoria, ui.classe, ui.consistenza, ui.rendita, ui.sup_cat, ui.zona, ui.edificio, ui.scala, " +
			"ui.interno, ui.piano, t.descr ||' '||uind.ind as indirizzo, uind.civ1, sc.perc_poss, decode(sc.tipo_titolo, '1', 'PROPRIETA''', '9', 'ALTRO') as tip_tit " +
			"FROM " +
			"sitiuiu ui, siticonduz_imm_all sc, anag_soggetti ans, load_cat_uiu_id i, " +
			"load_cat_uiu_ind uind, cat_d_toponimi t " +
			"WHERE " +
			"ui.cod_nazionale=(select uk_belfiore from ewg_tab_comuni) " +
			"and ui.foglio ||' '|| ui.particella in " +
			"(select distinct pa.foglio||' '||pa.particella " +
			"from sitipart pa " +
			"where sdo_relate ( " +
			"pa.shape, " +			
			"(SELECT pe.shape " +			
			"FROM pertinenze pe, sitipart pa " +
			"WHERE sdo_relate (pe.shape, pa.shape,'MASK=ANYINTERACT') = 'TRUE' " +
			"and sdo_geom.sdo_area(sdo_geom.sdo_intersection(pe.shape, pa.shape,0.005),0.005) > 0.5*sdo_geom.sdo_area(pa.shape,0.005) " +
			"and pa.cod_nazionale=(select uk_belfiore from ewg_tab_comuni) " +
			"and pa.foglio = ? " +
			"AND pa.particella = LPAD (?, 5, '0') " +
			"AND pa.data_fine_val = TO_DATE ('99991231', 'yyyymmdd')), " +
			"'MASK=ANYINTERACT') = 'TRUE' " +
			"and sdo_geom.sdo_area(sdo_geom.sdo_intersection(pa.shape, " +			
			"(SELECT pe.shape FROM pertinenze pe, sitipart pa " +
			"WHERE sdo_relate (pe.shape, pa.shape,'MASK=ANYINTERACT') = 'TRUE' " +			
			"and sdo_geom.sdo_area(sdo_geom.sdo_intersection(pe.shape, pa.shape,0.005),0.005) > 0.5*sdo_geom.sdo_area(pa.shape,0.005) " +			
			"and pa.cod_nazionale=(select uk_belfiore from ewg_tab_comuni) " +			
			"and pa.foglio = ? " +
			"AND pa.particella = LPAD (?, 5, '0') " +
			"AND pa.data_fine_val = TO_DATE ('99991231', 'yyyymmdd')),0.005),0.005) > 0.5*sdo_geom.sdo_area(pa.shape,0.005)) " +
			"and ui.data_fine_val=to_date('99991231','yyyymmdd') " +
			"and ui.categoria in ('C02','C06','C07') " +
			"and sc.cuaa = ? " +
			"and ui.cod_nazionale = sc.cod_nazionale (+) " +
			"and ui.data_fine_val = sc.data_fine (+) " +
			"and ui.foglio = sc.foglio (+) " +
			"and ui.particella = sc.particella (+) " +
			"and ui.sub = sc.sub (+) " +
			"and ui.unimm = sc.unimm (+) " +
			"and sc.data_fine = ans.data_fine_val (+) " +
			"and sc.pk_cuaa = ans.cod_soggetto (+) " +
			"and ui.cod_nazionale = i.codi_fisc_luna (+) " +
			"and lpad(ui.foglio, 4, '0') = i.foglio (+) " +
			"and ui.particella = i.mappale (+) " +
			"and lpad(ui.unimm, 4, '0') = i.sub (+) " +
			"and i.codi_fisc_luna = uind.codi_fisc_luna (+) " +
			"and i.sezione = uind.sezione (+) " +
			"and i.progressivo = uind.progressivo (+) " +
			"and i.id_imm = uind.id_imm (+) " +
			"and uind.seq = 0 " +
			"and uind.toponimo = t.pk_id (+) " +
			"order by ui.foglio, ui.particella, ui.unimm, ui.categoria ";
			*/
			
			this.initialize();
			/*
			 * 0: Folgio
			 * 1: Particella
			 * 2: Subalterno
			 * 3: cf
			 * 4: cognome
			 * 5: nome
			 * 6: dataNascita
			 * 7: indirizzo
			 * 8: civico
			 */
			this.setString(1, (String)listParam.get(0));
			this.setString(2, (String)listParam.get(1));
			this.setString(3, (String)listParam.get(3));
			this.setString(4, (String)listParam.get(3));
			this.setString(5, (String)listParam.get(7));
			this.setString(6, (String)listParam.get(8));
			this.setString(7, (String)listParam.get(0));
			this.setString(8, (String)listParam.get(1));
			this.setString(9, (String)listParam.get(0));
			this.setString(10, (String)listParam.get(1));
			this.setString(11, (String)listParam.get(3));
			
			log.debug(sql);
			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass ().getName(), getEnvUtente());
			ArrayList<PertinenzeAbitDatiCatastali> listaUiDiCatInPertinenza = new ArrayList<PertinenzeAbitDatiCatastali>();
			PertinenzeAbitDatiCatastali datCat = null;
			while (rs.next())
			{
				String foglio = rs.getString("FOGLIO");
				String particella = rs.getString("PARTICELLA");
				String sub = rs.getString("SUB");
				String unimm = rs.getString("UNIMM");
				Date data_inizio_val = rs.getDate("DATA_INIZIO_VAL");
				String categoria = rs.getString("CATEGORIA");				
				String classe = rs.getString("CLASSE");
				String consistenza = rs.getString("CONSISTENZA");
				String rendita = rs.getString("RENDITA");
				String sup_cat = rs.getString("SUP_CAT");
				String zona = rs.getString("ZONA");
				String edificio = rs.getString("EDIFICIO");
				String scala = rs.getString("SCALA");
				String interno = rs.getString("INTERNO");
				String piano = rs.getString("PIANO");
				String indirizzo = rs.getString("INDIRIZZO");				
				String civ1 = rs.getString("CIV1");
				String pp = rs.getString("PERC_POSS");
				String tt = rs.getString("TIP_TIT");

				datCat = new PertinenzeAbitDatiCatastali();
				if (!StringUtils.isEmpty(foglio))
					datCat.setFoglio(foglio);
				if (!StringUtils.isEmpty(particella))
					datCat.setParticella(particella);
				if (!StringUtils.isEmpty(sub))
					datCat.setSubalternoTerreno(sub.trim());
				if (!StringUtils.isEmpty(unimm))
					datCat.setSubalterno(unimm.trim());
				if (data_inizio_val != null){
					SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
					String sDatIni = df.format(data_inizio_val);
					datCat.setDataInizioVal(sDatIni);
				}
				if (!StringUtils.isEmpty(categoria))
					datCat.setCategoria(categoria.trim());
				else
					datCat.setCategoria("");
				if (!StringUtils.isEmpty(classe))
					datCat.setClasse(classe.trim());
				else
					datCat.setClasse("");
				if (!StringUtils.isEmpty(consistenza))
					datCat.setConsistenza(consistenza.trim());
				else
					datCat.setConsistenza("");
				if (!StringUtils.isEmpty(rendita))
					datCat.setRendita(rendita.trim());
				else
					datCat.setRendita("");
				if (!StringUtils.isEmpty(sup_cat))
					datCat.setSuperficeCatastale(sup_cat.trim());
				else
					datCat.setSuperficeCatastale("");
				if (!StringUtils.isEmpty(zona))
					datCat.setZona(zona.trim());
				else
					datCat.setZona("");
				if (!StringUtils.isEmpty(edificio))
					datCat.setEdificio(edificio.trim());
				else
					datCat.setEdificio("");
				if (!StringUtils.isEmpty(scala))
					datCat.setScala(scala.trim());
				else
					datCat.setScala("");
				if (!StringUtils.isEmpty(interno))
					datCat.setInterno(interno.trim());
				else
					datCat.setInterno("");
				if (!StringUtils.isEmpty(piano))
					datCat.setPiano(piano.trim());
				else
					datCat.setPiano("");
				if (!StringUtils.isEmpty(indirizzo))
					datCat.setIndirizzo(indirizzo.trim());
				else
					datCat.setIndirizzo("");
				if (!StringUtils.isEmpty(civ1))
					datCat.setCivico(civ1.trim());
				else
					datCat.setCivico("");
				if (!StringUtils.isEmpty(pp))
					datCat.setPercentualePossesso(pp.trim());
				else
					datCat.setPercentualePossesso("");
				if (!StringUtils.isEmpty(tt))
					datCat.setTipoTitolo(tt);
				else
					datCat.setTipoTitolo("");

				datCat.setChiave(chiave);
				listaUiDiCatInPertinenza.add(datCat);
			}
			ht.put(UI_DI_CATEGORIA_IN_PERTINENZA, listaUiDiCatInPertinenza);			
			
			/*INIZIO AUDIT*/
			try {
				Object[] arguments = new Object[1];
				arguments[0] = chiave;
				writeAudit(Thread.currentThread().getStackTrace()[1], arguments, ht);
			} catch (Throwable e) {				
				log.debug("ERRORE nella scrittura dell'audit", e);
			}
			/*FINE AUDIT*/
			
			return ht;
		}
	catch (Exception e) {
		log.error(e.getMessage(),e);
		throw e;
	}
	finally
	{
		if (conn != null)
		{
			if (!conn.isClosed())
				conn.close();
			conn = null;
		}
	}
	}//-------------------------------------------------------------------------

	public Hashtable mCaricareListaPertinenze(Vector listaPar,  EscFinder finder) throws Exception{
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		sql = "";
		String conteggio = "";
		long conteggione = 0;
		// faccio la connessione al db
		Connection conn = null;
		try {
			this.setDatasource(JNDI_DBTOTALE);

			conn = this.getConnection();
			int indice = 1;
			java.sql.ResultSet rs = null;

			for (int i=0;i<=1;i++){
				// il primo ciclo faccio la count
				if (i==0)
					 sql = SQL_SELECT_COUNT_LISTA;
				else
					sql = SQL_SELECT_LISTA;

				indice = 1;
				this.initialize();
				this.setInt(indice, 1);
				indice ++;

				// il primo passaggio esegue la select count
				if (finder.getKeyStr().equals("")){
					sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);

				}
				else{
					//if (!finder.getKeyStr().equals("")){
					sql = sql + " and sitiuiu.PKID_UIU in (" + finder.getKeyStr() + ")" ;
					//this.setString(indice,finder.getTitolarita());
					//indice ++;
				}

				//criterio di data fine validità = 31/12/9999
				//sql = sql + " AND DATA_FINE_VAL = to_date('99991231', 'yyyymmdd')";
				//criterio comune = milano
				//sql = sql + " AND COD_NAZIONALE = 'F205'";

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
				if (i ==1)
					//Qui completo la select
					sql = sql + " order by ui.foglio, ui.particella, ui.unimm, ui.categoria, sc.cuaa) selezione ) where N > " + limInf + " and N <=" + limSup;
				else if (i == 0)
					//Qui completo la select count
					sql = sql + ")";
					

				log.debug(sql);

				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				if (i ==1) {
					while (rs.next()){
						PertinenzeAbit pertinenzeAbit = new PertinenzeAbit();
						//imm.setChiave(rs.getString("PKID_UIU")); // la chiave
						//String key = rs.getString("FOGLIO") + "|" + rs.getString("PARTICELLA") + "|" + rs.getString("SUBALTERNO");
						//pertinenzeAbit.setChiave(key);
						String categoria = rs.getString("CATEG");
						String cf = rs.getString("CF");
						String civico = rs.getString("CIV1");
						String cognome = rs.getString("COGNOME");
						Date dataNascita = rs.getDate("D_NASCITA");
						String foglio = rs.getString("FOGLIO");
						String indirizzo = rs.getString("INDIRIZZO");
						String nome = rs.getString("NOME");
						String particella = rs.getString("PARTICELLA");
						String percPoss = rs.getString("PERC_POSS");
						String subalterno = rs.getString("SUBALTERNO");
						String tipoTitolo = rs.getString("TIP_TIT");
						String codNazionale = rs.getString("COD_NAZIONALE");
						String key = foglio + "|" + particella + "|" + subalterno + "|" + cf+ "|" + cognome + "|" + nome + "|" + dataNascita + "|" + indirizzo + "|" + civico;
						/*
						 * 0: Folgio
						 * 1: Particella
						 * 2: Subalterno
						 * 3: cf
						 * 4: cognome
						 * 5: nome
						 * 6: dataNascita
						 * 7: indirizzo
						 * 8: civico
						 */
						if (!StringUtils.isEmpty(categoria))
							pertinenzeAbit.setCategoria(categoria.trim());
						if (!StringUtils.isEmpty(cf))
							pertinenzeAbit.setCf(cf.trim());
						if (!StringUtils.isEmpty(civico))
							pertinenzeAbit.setCivico(civico.trim());
						if (!StringUtils.isEmpty(cognome))
							pertinenzeAbit.setCognome(cognome.trim());
						if (dataNascita != null){
							SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
							String sDatNas = df.format(dataNascita);
							pertinenzeAbit.setDataNascita(sDatNas);
						}
						if (!StringUtils.isEmpty(foglio))
							pertinenzeAbit.setFoglio(foglio);
						if (!StringUtils.isEmpty(indirizzo))
							pertinenzeAbit.setIndirizzo(indirizzo);
						if (!StringUtils.isEmpty(nome))
							pertinenzeAbit.setNome(nome);
						if (!StringUtils.isEmpty(particella))
							pertinenzeAbit.setParticella(particella);
						if (!StringUtils.isEmpty(percPoss)){
							NumberFormat nf = new DecimalFormat("#.##");
							String pp = nf.format(Double.parseDouble(percPoss));
							pertinenzeAbit.setPercPoss(pp);
						}
						if (!StringUtils.isEmpty(subalterno))
							pertinenzeAbit.setSubalterno(subalterno);
						if (!StringUtils.isEmpty(tipoTitolo))
							pertinenzeAbit.setTipoTitolo(tipoTitolo);
						if (!StringUtils.isEmpty(codNazionale))
							pertinenzeAbit.setCodNazionale(codNazionale);
						if (!StringUtils.isEmpty(key))
							pertinenzeAbit.setChiave(key);
						GenericTuples.T2<String,String> coord = null;
						try {
							coord = getLatitudeLongitude(pertinenzeAbit.getFoglio(), pertinenzeAbit.getParticella(), pertinenzeAbit.getCodNazionale());
						} catch (Exception e) {
						}
						if (coord!=null) {
							pertinenzeAbit.setLatitudine(coord.firstObj);
							pertinenzeAbit.setLongitudine(coord.secondObj);
						}
						
						vct.add(pertinenzeAbit);
					}
				}
				else{
					if (rs.next()){
						conteggio = rs.getString("conteggio");
					}
				}
			}
			ht.put("LISTA_PERTINENZE", vct);
			finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			// pagine totali
			finder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
			finder.setTotaleRecord(conteggione);
			finder.setRighePerPagina(RIGHE_PER_PAGINA);

			ht.put("FINDER",finder);
			
			/*INIZIO AUDIT*/
			try {
				Object[] arguments = new Object[2];
				arguments[0] = listaPar;
				arguments[1] = finder;
				writeAudit(Thread.currentThread().getStackTrace()[1], arguments, ht);
			} catch (Throwable e) {				
				log.debug("ERRORE nella scrittura dell'audit", e);
			}
			/*FINE AUDIT*/
			
			return ht;
		}
	catch (Exception e) {
		log.error(e.getMessage(),e);
		throw e;
	}
	finally
	{
		if (conn != null && !conn.isClosed())
			conn.close();
	}

	}


/*
 * *****************************************************************************
 */
	
	
}//-----------------------------------------------------------------------------
