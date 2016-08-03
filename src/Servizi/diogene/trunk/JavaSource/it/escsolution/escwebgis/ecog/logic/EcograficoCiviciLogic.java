/*
 * Created on 10-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.ecog.logic;

import it.escsolution.escwebgis.anagrafe.logic.AnagrafeDwhLogic;
import it.escsolution.escwebgis.common.ComuniLogic;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscComboObject;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.common.Utils;
import it.escsolution.escwebgis.ecog.bean.CivicoFinder;
import it.escsolution.escwebgis.ecografico.bean.CivicoEcografico;
import it.escsolution.escwebgis.ecografico.bean.FabbricatoEcografico;
import it.webred.utils.GenericTuples;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class EcograficoCiviciLogic extends EscLogic {
		
	public static final String FINDER = EcograficoCiviciLogic.class.getName() + "@FINDER";
	
	public static final String LISTA_SEDIME = EcograficoCiviciLogic.class.getName() + "@LISTA_SEDIME";
	public static final String CIVICO = EcograficoCiviciLogic.class.getName() + "@CIVICO";
	public static final String LISTA_CIVICI = EcograficoCiviciLogic.class.getName() + "@LISTA_CIVICI";
	public static final String LISTA_FABBRICATI = EcograficoCiviciLogic.class.getName() + "@LISTA_FABBRICATI";
	
	public static final String LISTA_CIVICI_STRADA = EcograficoCiviciLogic.class.getName() + "@LISTA_CIVICI_STRADA";
	
	public static final String CIVICIARIO = EcograficoCiviciLogic.class.getName() + "@CIVICIARIO";

	public EcograficoCiviciLogic(EnvUtente eu) {
				super(eu);
	}


	private final static String SQL_SELECT_LISTA = "select *  from ( "+ 
  "select  "+ 
  "  ROWNUM AS N,  "+ 
  "  ID,   "+ 
  "  nvl( UK_CIVICO, ' - ') as UK_CIVICO, "+ 
  "  CODICE_STRADA, "+ 
  "  nvl(SEDIME, ' - ') as SEDIME, "+ 
  "  nvl(NOME_STRADA, ' - ') as NOME_STRADA, "+ 
  "  nvl(DESCRIZIONE_CIVICO, ' - ') as DESCRIZIONE_CIVICO,  " +
  "  FOGLIO_CT, PARTICELLA_CT "+ 
  "from  "+ 
  "  (  "+ 
  "    select distinct  "+ 
  "            C.ID, "+ 
  "            C.UK_CIVICO, "+ 
  "            S.CODICE_STRADA,  "+ 
  "            S.SPECIE_STRADA AS SEDIME,  "+ 
  "            S.NOME_STRADA,  "+ 
  "            C.DESCRIZIONE_CIVICO,  "+ 
  "            C.pk_sequ_civico,  "+ 
  "            C.FK_COMUNI_BELF AS COD_NAZAIONALE,  "+ 
  "            COM.CODI_FISC_LUNA, "+ 
  "            F.FOGLIO_CT, F.PARTICELLA_CT  from  "+ 
  "      ec_top_strade S, ec_top_civici C, ec_r_fabbr_civici r, ec_fab_fabbricati F, SITICOMU COM  "+ 
  "      where S.UK_STRADA = C.FK_STRADE   "+ 
  "            and C.UK_CIVICO is not null  "+ 
  "            and C.FK_COMUNI_BELF = COM.COD_NAZIONALE  "+ 
  "            and C.UK_CIVICO = R.FK_CIVICI (+)  "+ 
  "            and F.UK_CODI_FABBRICATO = R.FK_FABBRICATI "+ 
  "            and 1=? "; 

	private final static String SQL_SELECT_DETAIL_FROM_CIVICO = "select distinct C.ID, C.ACCESSIBILITA, C.CAP, C.CODICE_CENSIMENTO, C.COMUNE, C.DATA_FINE_VAL, C.DATA_INIZIO_VAL, C.DESCR_DEST_USO, C.DESCR_STRADA, C.DESCRIZIONE_CIVICO, C.ESP1, C.ESP2, C.ESP3, C.ESP4, "+
			" C.FK_COMUNI_BELF, C.FK_STRADE, LTRIM(SUBSTR(C.FK_STRADE,5),'0') CODICE_STRADA, C.FOGLIO_CT, C.PARTICELLA_CT,  C.LATO_STRADA, C.NOTE, C.NUMERO, C.PK_SEQU_CIVICO, C.TIPO_ACCESSO, C.TIPO_CIVICO, C.UK_CIVICO, C.XCENTROID, C.YCENTROID, TO_CHAR(TO_DATE(c.data_record,'dd-MM-yy'),'dd/MM/yyyy') data_record_date " +
			" from ec_top_civici c where c.id = ? order by descr_strada, TO_NUMBER(numero) ";

	
	private final static String SQL_LISTA_FABBRICATI_CIVICO = "select f.* , TO_CHAR (TO_DATE (c.data_record, 'dd-MM-yy'), 'dd/MM/yyyy') data_rilievo_date " +
			" from ec_top_civici c, ec_r_fabbr_civici r, ec_fab_fabbricati f" +
			" where C.ID = ? and C.UK_CIVICO = r.fk_civici and R.FK_FABBRICATI = F.UK_CODI_FABBRICATO " +
			" ORDER BY F.FOGLIO_CT, F.PARTICELLA_CT";
	
	private final static String SQL_SELECT_DETAIL = SQL_SELECT_DETAIL_FROM_CIVICO;
	
	private final static String SQL_SELECT_COUNT_LISTA = "select count(*) as conteggio from (" + SQL_SELECT_LISTA;

    private final static String SQL_SELECT_COUNT_ALL = SQL_SELECT_COUNT_LISTA;

	
	public Map mCaricareDatiFormRicerca(String utente) throws Exception
	{
		Map result = new HashMap();

		Set<EscComboObject> listaSedime = new LinkedHashSet<EscComboObject>();
		result.put(LISTA_SEDIME, listaSedime);
		Set<String> listaComuni = new LinkedHashSet<String>();
		result.put("LISTA_COMUNI", listaComuni);
		listaSedime.add(
				new EscComboObject()
				{
					public String getCode(){
						return "";
					}
					public String getDescrizione(){
						return "Qualsiasi";
					}
				});

		Connection conn = null;
		try
		{
			conn = super.getConnection();
			super.initialize();
			super.prepareStatement("select distinct specie_strada from ec_top_strade, siticomu where ec_top_strade.fk_comuni_belf = siticomu.cod_nazionale and siticomu.codi_fisc_luna in (select distinct uk_belfiore from ewg_tab_comuni) and 1 = ? order by specie_strada");
			super.setInt(1, 1);
			ResultSet rs = super.executeQuery(conn, this.getClass().getName(), getEnvUtente());
			while (rs.next())
			{
				final String key = rs.getString(1);
				if (key != null)
					listaSedime.add(
							new EscComboObject()
							{
								public String getCode(){
									return key;
								}
								public String getDescrizione(){
									return key;
								}
							});
			}

			Vector vctComuni = new ComuniLogic(super.envUtente).getListaComuniUtente(utente);
			listaComuni.addAll(vctComuni);
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}


		return result;
	}

	public Hashtable mCaricareListaCivici(Vector listaPar, CivicoFinder finder) throws Exception{
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		sql = "";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;
		java.sql.ResultSet rs = null;
		try {
			conn = this.getConnection();
			//sql = SQL_SELECT_COUNT_ALL;
			int indice = 1;
			//this.initialize();
			//this.setInt(indice,1);
			//indice ++;
			//prepareStatement(sql);
			//if (rs.next()){
			//		conteggione = rs.getLong("conteggio");
			//}
			conteggione = 0;

			for (int i=0;i<=1;i++){
				if (i==0)
					sql = SQL_SELECT_COUNT_LISTA;
				else
					sql = SQL_SELECT_LISTA;

				indice = 1;
				this.initialize();
				this.setInt(indice,1);
				indice ++;

				if (finder.getKeyStr().equals("")){
					sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
				}
				else{
					sql = sql + "and C.UK_CIVICO in (" +finder.getKeyStr() + ") " ;
				}


				long limInf, limSup;
				limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
				// LISTA
				if (i == 1)
				{
					sql += "order by SEDIME, S.NOME_STRADA, C.DESCRIZIONE_CIVICO ";
					sql += ")) where N > " + limInf + " and N <=" + limSup;
				}
				// COUNT FILTRATI
				else
					sql += ")))";
				
				log.info("ListaCivici - SQL["+sql+"]");
				
				prepareStatement(sql) ;
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				if (i ==1) {
					while (rs.next()){
						CivicoEcografico civ = new CivicoEcografico();
						
						civ.setId(super.tornaValoreRS(rs,"ID"));
						civ.setUkCivico(super.tornaValoreRS(rs,"UK_CIVICO"));
						civ.setCodiceStrada(super.tornaValoreRS(rs, "CODICE_STRADA"));
						civ.setDescrStrada(super.tornaValoreRS(rs, "SEDIME") + " " + super.tornaValoreRS(rs, "NOME_STRADA"));
						civ.setDescrizioneCivico(super.tornaValoreRS(rs,"DESCRIZIONE_CIVICO"));
						civ.setFoglioCt(super.tornaValoreRS(rs, "FOGLIO_CT"));
						civ.setParticellaCt(super.tornaValoreRS(rs, "PARTICELLA_CT"));
					
						
						vct.add(civ);
					}
				}
				else{
					if (rs.next()){
						conteggio = rs.getString("conteggio");
					}
				}
			}
			ht.put(LISTA_CIVICI,vct);
			finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			finder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
			finder.setTotaleRecord(conteggione);
			finder.setRighePerPagina(RIGHE_PER_PAGINA);

			ht.put(FINDER,finder);
			
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
			if (rs != null)
			{
				rs.close();
				rs = null;
			}
			if (conn != null)
			{
				if (!conn.isClosed())
					conn.close();
				conn = null;
			}
		}
	}


	private CivicoEcografico getCivico(ResultSet rs, CivicoEcografico civ) throws Exception{

		civ.setId(super.tornaValoreRS(rs,"ID"));
		civ.setAccessibilita(super.tornaValoreRS(rs,"ACCESSIBILITA"));
		civ.setCap(super.tornaValoreRS(rs,"CAP"));
		civ.setCodiceCensimento(super.tornaValoreRS(rs,"CODICE_CENSIMENTO"));
		civ.setComune(super.tornaValoreRS(rs,"COMUNE"));
		civ.setDataFineVal(super.tornaValoreRS(rs,"DATA_FINE_VAL"));
		civ.setDataInizioVal(super.tornaValoreRS(rs,"DATA_INIZIO_VAL"));
		civ.setDescrDestUso(super.tornaValoreRS(rs,"DESCR_DEST_USO"));
		civ.setDescrizioneCivico(super.tornaValoreRS(rs,"DESCRIZIONE_CIVICO"));
		civ.setDescrStrada(super.tornaValoreRS(rs,"DESCR_STRADA"));
		civ.setEsp1(super.tornaValoreRS(rs,"ESP1"));
		civ.setEsp2(super.tornaValoreRS(rs,"ESP2"));
		civ.setEsp3(super.tornaValoreRS(rs,"ESP3"));
		civ.setEsp4(super.tornaValoreRS(rs,"ESP4"));
		civ.setFkComuniBelf(super.tornaValoreRS(rs,"FK_COMUNI_BELF"));
		civ.setFkStrade(super.tornaValoreRS(rs,"FK_STRADE"));
		civ.setCodiceStrada(super.tornaValoreRS(rs,"CODICE_STRADA"));
		civ.setFoglioCt(super.tornaValoreRS(rs,"FOGLIO_CT"));
		civ.setParticellaCt(super.tornaValoreRS(rs,"PARTICELLA_CT"));
		civ.setLatoStrada(super.tornaValoreRS(rs,"LATO_STRADA"));
		civ.setNote(super.tornaValoreRS(rs,"NOTE"));
		civ.setNumero(super.tornaValoreRS(rs,"NUMERO"));
		civ.setPkSequCivico(super.tornaValoreRS(rs, "PK_SEQU_CIVICO"));
		civ.setTipoAccesso(super.tornaValoreRS(rs,"TIPO_ACCESSO"));
		civ.setTipoCivico(super.tornaValoreRS(rs,"TIPO_CIVICO"));
		civ.setUkCivico(super.tornaValoreRS(rs,"UK_CIVICO"));
		civ.setXcentroid(super.tornaValoreRS(rs,"XCENTROID"));
		civ.setYcentroid(super.tornaValoreRS(rs,"YCENTROID"));
		civ.setDataRecord(super.tornaValoreRS(rs, "DATA_RECORD_DATE"));
			
		GenericTuples.T2<String,String> coord = null;
		try {
			String x = civ.getXcentroid();
			String y = civ.getYcentroid();
			
			x = x!=null ? x.replaceAll(",", "."): x;
			y = y!=null ? y.replaceAll(",", "."): y;
			
			coord = getLatitudeLongitudeFromXY(x,y);
		} catch (Exception e) {
		}
		if (coord!=null) {
			civ.setLatitudine(coord.firstObj);
			civ.setLongitudine(coord.secondObj);
		}
		
		return civ;
	}
	

	public Hashtable<String,Object> mCaricareDettaglioCivici(String chiave, int st) throws Exception
	{
		Hashtable<String,Object> ht = new Hashtable<String,Object>();
		String sql="";
		Connection conn = null;
		
		try
		{
			conn = this.getConnection();
			this.initialize();
			this.setString(1, chiave);
			
			if (st == 103)
				sql= SQL_SELECT_DETAIL_FROM_CIVICO;
			else
				sql=SQL_SELECT_DETAIL;
				
			prepareStatement(sql);
			
			log.info("mCaricareDettaglioCivici - SQL["+sql+"]");
			log.info("mCaricareDettaglioCivici - Chiave["+ chiave+"]");
			
			ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

			CivicoEcografico civ = new CivicoEcografico();
			
			if (rs.next()) 
				civ = this.getCivico(rs, civ);
				
			ht.put(CIVICO, civ);
			ht.put(LISTA_FABBRICATI, this.getListaFabbricatiCivico(conn, chiave));
			
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
		}catch (Exception e)
		{
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}
	
	
	public List<FabbricatoEcografico> getListaFabbricatiCivico(Connection conn, String chiaveCivico) throws Exception{
		ArrayList<FabbricatoEcografico> listaFabbricati = new ArrayList<FabbricatoEcografico>();
		this.initialize();
		this.setString(1, chiaveCivico);
		prepareStatement(SQL_LISTA_FABBRICATI_CIVICO);
		log.info("getListaFabbricatiCivico - SQL["+SQL_LISTA_FABBRICATI_CIVICO+"]");
		log.info("getListaFabbricatiCivico - Chiave["+ chiaveCivico+"]");
		
		ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
		
		while(rs.next()){
			
			FabbricatoEcografico fab = new FabbricatoEcografico();
			
			fab.setId(super.tornaValoreRS(rs,"ID"));  
			fab.setPkSequFabbricato(super.tornaValoreRS(rs,"PK_SEQU_FABBRICATO")); 
			fab.setUkCodiFabbricato(super.tornaValoreRS(rs,"UK_CODI_FABBRICATO")); 
			fab.setCodiceEcografico(super.tornaValoreRS(rs,"CODICE_ECOGRAFICO")); 
			fab.setFkComuniBelf(super.tornaValoreRS(rs,"FK_COMUNI_BELF")); 
			fab.setComune(super.tornaValoreRS(rs,"COMUNE")); 
			fab.setSezioneCt(super.tornaValoreRS(rs,"SEZIONE_CT")); 
			fab.setFoglioCt(super.tornaValoreRS(rs,"FOGLIO_CT")); 
			fab.setParticellaCt(super.tornaValoreRS(rs,"PARTICELLA_CT")); 
			fab.setId(super.tornaValoreRS(rs,"ID")); 
			fab.setStatoAccatastamento(super.tornaValoreRS(rs, "STATO_ACCATASTAMENTO"));
			fab.setSuperficie(super.tornaValoreRS(rs, "SUPERFICIE"));
			fab.setAltezza(super.tornaValoreRS(rs, "ALTEZZA"));
			fab.setVolume(super.tornaValoreRS(rs, "VOLUME"));
			fab.setnPianiFt(super.tornaValoreRS(rs, "N_PIANI_FT"));
			fab.setnPianiEt(super.tornaValoreRS(rs, "N_PIANI_ET"));
			fab.setDestinazioneUso(super.tornaValoreRS(rs, "DESTINAZIONE_USO"));
			fab.setStatoCostruito(super.tornaValoreRS(rs, "STATO_COSTRUITO"));
			fab.setNote(super.tornaValoreRS(rs, "NOTE"));
			fab.setStatoConservazione(super.tornaValoreRS(rs, "STATO_CONSERVAZIONE"));
			fab.setAnnoCostruzione(super.tornaValoreRS(rs, "ANNO_COSTRUZIONE"));
			fab.setOrigine(super.tornaValoreRS(rs, "ORIGINE"));
			fab.setXcentroid(super.tornaValoreRS(rs,"XCENTROID"));
			fab.setYcentroid(super.tornaValoreRS(rs,"YCENTROID"));
			fab.setCodiceCensimento(super.tornaValoreRS(rs, "CODICE_CENSIMENTO"));
			fab.setDataRilievo(super.tornaValoreRS(rs, "DATA_RILIEVO_DATE"));
			fab.setCausaNonRilevazione(super.tornaValoreRS(rs, "CAUSA_NON_RILEVAZIONE"));
			fab.setNoteRilievo(super.tornaValoreRS(rs, "NOTE_RILIEVO"));
			
			log.info("Xce:" + fab.getXcentroid());
			log.info("Yce:" + fab.getYcentroid());
			
			GenericTuples.T2<String,String> coord = null;
			try {
				
				String x = fab.getXcentroid();
				String y = fab.getYcentroid();
				
				x = x!=null ? x.replaceAll(",", "."): x;
				y = y!=null ? y.replaceAll(",", "."): y;
				
				
				coord = getLatitudeLongitudeFromXY(x, y);
			} catch (Exception e) {
				log.error(e.getMessage(),e);
			}
			if (coord!=null) {
				fab.setLatitudine(coord.firstObj);
				fab.setLongitudine(coord.secondObj);
			}
			
			listaFabbricati.add(fab);
		}
		
		log.info("getListaFabbricatiCivico - Num.Result["+ listaFabbricati.size()+"]");
		
		return listaFabbricati;
	}

	public Hashtable<String, Object> mCaricareListaCiviciStrada(String chiave) throws Exception
	{
		Hashtable<String, Object>  ht = new Hashtable<String, Object> ();
		String sql = 
				"select c.*,  TO_CHAR(TO_DATE(c.data_record,'dd-MM-yy'),'dd/MM/yyyy') data_record_date from ec_top_civici c, ec_top_strade s " +
				"where S.UK_STRADA = c.fk_strade and S.id = ? order by TO_NUMBER(numero) ";
		
		Connection conn = null;
		try
		{
			conn = this.getConnection();
			this.initialize();
			this.setString(1, chiave);
			
			log.info("mCaricareListaCiviciStrada - SQL["+sql+"]");
			log.info("mCaricareListaCiviciStrada - Id["+chiave+"]");
			
			prepareStatement(sql);
			ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());


			Vector<CivicoEcografico> listaCivStra = new Vector<CivicoEcografico>();
			while (rs.next()) {
				CivicoEcografico civ = new CivicoEcografico();
				civ = this.getCivico(rs, civ);
				listaCivStra.add(civ);
			}

			ht.put(LISTA_CIVICI_STRADA,listaCivStra);
			
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
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}

}



















