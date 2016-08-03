package it.escsolution.escwebgis.docfa.logic;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.imageio.stream.FileImageInputStream;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.common.Utils;
import it.escsolution.escwebgis.concessioni.logic.StoricoConcessioniLogic;
import it.escsolution.escwebgis.docfa.bean.ConcessioneDocfa;
import it.escsolution.escwebgis.docfa.bean.Docfa;
import it.escsolution.escwebgis.docfa.bean.DocfaFinder;
import it.escsolution.escwebgis.docfa.bean.DocfaPlanimetrie;
import it.escsolution.escwebgis.docfa.bean.PrgDocfa;
import it.escsolution.escwebgis.docfa.bean.RenditaDocfa;
import it.escsolution.escwebgis.tributi.bean.OggettiTARSU;
import it.webred.GlobalParameters;
import it.webred.ct.config.model.AmKeyValueExt;
import it.webred.ct.config.parameters.ParameterService;
import it.webred.ct.config.parameters.dto.ParameterSearchCriteria;
import it.webred.ct.data.access.aggregator.elaborazioni.ElaborazioniService;
import it.webred.ct.data.access.aggregator.elaborazioni.dto.ControlloClassamentoConsistenzaDTO;
import it.webred.ct.data.access.basic.common.utils.Info;
import it.webred.ct.data.access.basic.docfa.DocfaService;
import it.webred.ct.data.access.basic.docfa.dto.BeniNonCensDTO;
import it.webred.ct.data.access.basic.docfa.dto.DettaglioDocfaDTO;
import it.webred.ct.data.access.basic.docfa.dto.DocfaInParteUnoDTO;
import it.webred.ct.data.access.basic.docfa.dto.OperatoreDTO;
import it.webred.ct.data.access.basic.docfa.dto.RicercaOggettoDocfaDTO;
import it.webred.ct.data.model.docfa.DocfaAnnotazioni;
import it.webred.ct.data.model.docfa.DocfaDichiaranti;
import it.webred.utils.GenericTuples;



public class DocfaLogic extends EscLogic
{
	private String appoggioDataSource;
	public static String DOCFA_RAPPORTO = "DOCFA_RAPPORTO";
	
	private String modalitaCalcoloValoreCommerciale = null;
	
	private static final Logger log = Logger.getLogger(DocfaLogic.class.getName());
	
	public static final HashMap<Integer, String> CLASSI_MIN_STR = new HashMap<Integer, String>();
	static {
		CLASSI_MIN_STR.put(new Integer("9999"), "NULL");
		CLASSI_MIN_STR.put(new Integer("8999"), "ERROR");
		CLASSI_MIN_STR.put(new Integer("7999"), "U");
		CLASSI_MIN_STR.put(new Integer("9996"), "NULL");
		CLASSI_MIN_STR.put(new Integer("8996"), "ERROR");
		CLASSI_MIN_STR.put(new Integer("7996"), "U");
	}
	
	public static final String MARCA_NULL = "NULL c01d3f310c498266783effcd5fbb34ed NULL"; //valore che non sarà mai utilizzato effettivamente in campi di testo

	public DocfaLogic(EnvUtente eu)
	{
		super(eu);
		appoggioDataSource = eu.getDataSource();
		
	}

	public static final String FINDER = "FINDER";
	public final static String LISTA_DOCFA = "LISTA_DOCFA@DocfaLogic";
	public final static String LISTA_DETTAGLIO_DOCFA_INTESTATI  = "LISTA_DETTAGLIO_DOCFA_INTESTATI@DocfaLogic";
	public final static String LISTA_DETTAGLIO_DOCFA_UIU = "LISTA_DETTAGLIO_DOCFA_UIU@DocfaLogic";
	public final static String LISTA_DETTAGLIO_DOCFA_BENI_NON_CENS = "LISTA_DETTAGLIO_DOCFA_BENI_NON_CENS@DocfaLogic";
	public final static String LISTA_DETTAGLIO_DOCFA_ANNOTAZIONI = "LISTA_DETTAGLIO_DOCFA_ANNOTAZIONI@DocfaLogic";
	public final static String LISTA_DETTAGLIO_DOCFA_DATI_CENSUARI = "LISTA_DETTAGLIO_DOCFA_DATI_CENSUARI@DocfaLogic";
	public final static String LISTA_DETTAGLIO_DOCFA_DICHIARANTI = "LISTA_DETTAGLIO_DOCFA_DICHIARANTI@DocfaLogic";
	public final static String LISTA_DETTAGLIO_DOCFA_PARTE_UNO = "LISTA_DETTAGLIO_DOCFA_PARTE_UNO@DocfaLogic";
	public final static String LISTA_DETTAGLIO_DOCFA_SOLOREQUEST_DATI_CENSUARI = "LISTA_DETTAGLIO_DOCFA_SOLOREQUEST_DATI_CENSUARI@DocfaLogic";
	public final static String LISTA_DETTAGLIO_DOCFA_SOLOREQUEST_340 = "LISTA_DETTAGLIO_DOCFA_SOLOREQUEST_340@DocfaLogic";
	public final static String LISTA_DETTAGLIO_DOCFA_SOLOREQUEST_CLASSE_ATTESTA = "LISTA_DETTAGLIO_DOCFA_SOLOREQUEST_CLASSE_ATTESTA@DocfaLogic";	
	public final static String LISTA_DETTAGLIO_DOCFA_MEDIA_ATTESA = "LISTA_DETTAGLIO_DOCFA_MEDIA_ATTESA@DocfaLogic";
	public final static String LISTA_DETTAGLIO_DOCFA_CATEGORIA_CLASSE_PARTICELLA = "LISTA_DETTAGLIO_DOCFA_CATEGORIA_CLASSE_PARTICELLA@DocfaLogic";
	public final static String LISTA_DETTAGLIO_DOCFA_SOLOREQUEST_DATI_GRAFFATI = "LISTA_DETTAGLIO_DOCFA_SOLOREQUEST_DATI_GRAFFATI@DocfaLogic";
	public final static String LISTA_DETTAGLIO_DOCFA_PRG = "LISTA_DETTAGLIO_DOCFA_PRG@DocfaLogic";
	public final static String LISTA_DETTAGLIO_DOCFA_CONC = "LISTA_DETTAGLIO_DOCFA_CONC@DocfaLogic";
	public final static String LISTA_DETTAGLIO_DOCFA_OPER = "DETTAGLIO_DOCFA_OPER@DocfaLogic";
	public final static String NUMERO_DI_PLANIMETRIE = "NUMERO_DI_PLANIMETRIE@DocfaLogic";
	public final static String PLANIMETRIE_SENZA_IMM_SOLO_REQUEST = "PLANIMETRIE_SENZA_IMM_SOLO_REQUEST@DocfaLogic";
	//public final static String CATAST_DATI_SOLO_REQUEST = "CATAST_DATI_SOLO_REQUEST@DocfaLogic";
	public final static String SQL_COD_ENTE = "select uk_belfiore from ewg_tab_comuni";
	public final static String DOCFA = "DOCFA@DocfaLogic";
	
		
//	private final static String SQL_SELECT_LISTA = "SELECT tec_cognome, tec_nome, protocollo,data_variazione,causale, soppressione, variazione, costituzione, " +
//													"operazione, foglio,particella, subalterno, categoria, indirizzo_uiu, civico_uiu, dichiarante, indirizzo_dichiarante," +
//													"fornitura,data_protocollo,n " +
//													"FROM (" +
//													" SELECT tec_cognome, tec_nome, PROTOCOLLO, TO_CHAR(DATA_VARIAZIONE,'YYYY-MM-DD') DATA_VARIAZIONE,CAUSALE,SOPPRESSIONE,VARIAZIONE,COSTITUZIONE ," +
//												   	" OPERAZIONE," +
//													" FOGLIO,PARTICELLA,SUBALTERNO,CATEGORIA, indirizzo_uiu,civico_uiu, DICHIARANTE,INDIRIZZO_DICHIARANTE,TO_CHAR(FORNITURA,'YYYY-MM-DD') FORNITURA," +
//													" SUBSTR(data_registrazione,7,2)||'-'||SUBSTR(data_registrazione,5,2)||'-'||SUBSTR(data_registrazione,1,4) as DATA_PROTOCOLLO, ROWNUM AS n " +
//													" FROM ( "+
//													"   SELECT DISTINCT tec_cognome, tec_nome, "+
//													"   D_GEN.PROTOCOLLO_REG          AS PROTOCOLLO, "+
//													"   D_GEN.DATA_VARIAZIONE         AS DATA_VARIAZIONE, "+
//													"   CAU.CAU_DES                   AS CAUSALE, "+
//													"  UIU_IN_SOPPRESSIONE           AS SOPPRESSIONE,    "+
//													"  UIU_IN_VARIAZIONE             AS VARIAZIONE, "+
//													"  UIU_IN_COSTITUZIONE           AS COSTITUZIONE, "+
//													//"   DIC.COGNOME                    COGNOME, "+
//													//"   DIC.NOME                       NOME, "+
//													"   DECODE (U.TIPO_OPERAZIONE,'C','COSTITUITA', "+
//													"                             'V','VARIATA', "+
//													"							 'S','SOPPRESSA', "+
//													"                             U.TIPO_OPERAZIONE) AS OPERAZIONE,  "+
//													"   U.FOGLIO FOGLIO, "+
//													"   U.NUMERO PARTICELLA, "+
//													"   U.SUBALTERNO SUBALTERNO, "+
//													"   CEN.CATEGORIA CATEGORIA, "+
//										/*nuovo*/	"	TRIM(U.INDIR_TOPONIMO)     AS INDIRIZZO_UIU, "+	
//													"   U.INDIR_NCIV_UNO as CIVICO_UIU, "+
//													"   DIC.DIC_COGNOME || ' ' || DIC.DIC_NOME  AS DICHIARANTE,   "+
//													"   DIC.DIC_RES_COM || ' ' || DIC.DIC_RES_INDIR || ' ' || DIC.DIC_RES_NUMCIV AS INDIRIZZO_DICHIARANTE,  "+
//													"   d_gen.fornitura fornitura "+
//													//"   ,ROWNUM AS N "+
//													" ,cen.DATA_REGISTRAZIONE " +
//													"    FROM  "+
//													"     DOCFA_CAUSALI        CAU, "+     //      --  Causali Docfa
//													"     DOCFA_DATI_GENERALI  D_GEN, " +      //  --  Tipo "1" Testata ID. RECORD "A"          Dati generali
//													"     DOCFA_UIU            U,"+     //         --  Tipo "3"         ID. RECORD "C" Tiprec 3 Unità Immob.
//													"     DOCFA_DICHIARANTI    DIC"+     //        --  Tipo "A"         ID. RECORD "M"          Dichiaranti
//													"	  ,DOCFA_dati_metrici metr " +
//													"	  ,DOCFA_dati_censuari cen" +
//						/* nuovo per operatori*/	"	  ,DOCFA_OPERATORI oper" +
//													"     WHERE D_GEN.CAUSALE_NOTA_VAX = CAU.CAU_COD(+)"+
//													"     AND D_GEN.PROTOCOLLO_REG = U.PROTOCOLLO_REG(+) "+
//													"     AND D_GEN.FORNITURA = U.FORNITURA(+)"+
//													"     AND D_GEN.PROTOCOLLO_REG = DIC.PROTOCOLLO_REG(+) " +
//							                        "  	  AND d_gen.PROTOCOLLO_REG = cen.PROTOCOLLO_REGISTRAZIONE(+) " +
//							                        "  	  AND d_gen.FORNITURA = cen.FORNITURA(+) " + 
//							                        "  	  AND CEN.protocollo_registrazione = metr.PROTOCOLLO_REGISTRAZIONE(+) " +
//							                        "  	  AND cen.FORNITURA = metr.FORNITURA(+) " + 
//							                        "  	  AND cen.DATA_REGISTRAZIONE = metr.DATA_REGISTRAZIONE(+) "+ 
//													"     AND D_GEN.FORNITURA = DIC.FORNITURA(+)  " +
//						/* nuovo per operatori*/	"	  AND D_GEN.PROTOCOLLO_REG = oper.protocollo (+) " +
//						/* nuovo per operatori*/	"     AND SUBSTR(TO_CHAR(D_GEN.FORNITURA,'dd/mm/yyyy'),7,10) = oper.ANNO (+) " +
//													"	  AND 1 = ? ";

	private final static String SQL_SELECT_LISTA = " SELECT tec_cognome, tec_nome, protocollo,data_variazione,causale, soppressione, variazione, costituzione, " + 
													" operazione, foglio,particella, subalterno, categoria, indirizzo_uiu, civico_uiu, dichiarante, indirizzo_dichiarante, " +
													" fornitura,data_protocollo,n  " + 	
													" FROM ( " +	
													" SELECT tec_cognome, tec_nome, PROTOCOLLO, TO_CHAR(DATA_VARIAZIONE,'YYYY-MM-DD') DATA_VARIAZIONE,CAUSALE,SOPPRESSIONE,VARIAZIONE,COSTITUZIONE , " +
													" OPERAZIONE, " +
													" FOGLIO,PARTICELLA,SUBALTERNO,CATEGORIA, indirizzo_uiu,civico_uiu, DICHIARANTE,INDIRIZZO_DICHIARANTE,TO_CHAR(FORNITURA,'YYYY-MM-DD') FORNITURA, " +
													" SUBSTR(data_registrazione,7,2)||'-'||SUBSTR(data_registrazione,5,2)||'-'||SUBSTR(data_registrazione,1,4) as DATA_PROTOCOLLO, ROWNUM AS n " + 
													" FROM ( " + 
													" SELECT DISTINCT tec_cognome, tec_nome, " + 
													" D_GEN.PROTOCOLLO_REG          AS PROTOCOLLO, " + 
													" D_GEN.DATA_VARIAZIONE         AS DATA_VARIAZIONE, " + 
													" CAU.CAU_DES                   AS CAUSALE, " + 
													" UIU_IN_SOPPRESSIONE           AS SOPPRESSIONE, " +    
													" UIU_IN_VARIAZIONE             AS VARIAZIONE, " + 
													" UIU_IN_COSTITUZIONE           AS COSTITUZIONE, " + 
													" DECODE (U.TIPO_OPERAZIONE,'C','COSTITUITA', 'V','VARIATA', 'S','SOPPRESSA', U.TIPO_OPERAZIONE) AS OPERAZIONE, " +  
													" U.FOGLIO FOGLIO, " + 
													" U.NUMERO PARTICELLA, " + 
													" U.SUBALTERNO SUBALTERNO, " + 
													" CEN.CATEGORIA, " + 
													" TRIM(U.INDIR_TOPONIMO) AS INDIRIZZO_UIU, " + 
													" U.INDIR_NCIV_UNO as CIVICO_UIU, " + 
													" DIC.DIC_COGNOME || ' ' || DIC.DIC_NOME  AS DICHIARANTE, " +   
													" DIC.DIC_RES_COM || ' ' || DIC.DIC_RES_INDIR || ' ' || DIC.DIC_RES_NUMCIV AS INDIRIZZO_DICHIARANTE, " +  
													" D_GEN.fornitura fornitura,cen.DATA_REGISTRAZIONE " +
													//" --,CEN.FOGLIO, CEN.NUMERO, CEN.SUBALTERNO " +
													" FROM " +  
													" DOCFA_DATI_GENERALI  D_GEN " + 
													" LEFT JOIN DOCFA_CAUSALI CAU ON D_GEN.CAUSALE_NOTA_VAX = CAU.CAU_COD " +  
													" LEFT JOIN DOCFA_UIU U ON D_GEN.PROTOCOLLO_REG = U.PROTOCOLLO_REG AND D_GEN.FORNITURA = U.FORNITURA " + 
													" LEFT JOIN DOCFA_DICHIARANTI DIC ON D_GEN.PROTOCOLLO_REG = DIC.PROTOCOLLO_REG AND D_GEN.FORNITURA = DIC.FORNITURA " +
													" LEFT JOIN DOCFA_dati_censuari CEN ON d_gen.PROTOCOLLO_REG = cen.PROTOCOLLO_REGISTRAZIONE AND d_gen.FORNITURA = cen.FORNITURA AND CEN.FOGLIO = u.FOGLIO AND CEN.NUMERO = u.NUMERO AND CEN.SUBALTERNO = u.SUBALTERNO " +
													" LEFT JOIN DOCFA_dati_metrici metr ON D_GEN.protocollo_reg = metr.PROTOCOLLO_REGISTRAZIONE AND D_GEN.FORNITURA = metr.FORNITURA AND cen.DATA_REGISTRAZIONE = metr.DATA_REGISTRAZIONE " +
													" LEFT JOIN DOCFA_OPERATORI oper ON D_GEN.PROTOCOLLO_REG = oper.protocollo AND SUBSTR(TO_CHAR(D_GEN.FORNITURA,'dd/mm/yyyy'),7,10) = oper.ANNO " +  
													" WHERE 1= ? ";
													//" AND U.FOGLIO = '0017' and U.NUMERO = '00039' and U.SUBALTERNO = '0707'    --DATI TEST " +
													//" )) ";
	
	private final static String SQL_SELECT_COUNT_LISTA = "select count(*) as conteggio  from ("+SQL_SELECT_LISTA ;

	private final static String SQL_SELECT_COUNT_ALL = SQL_SELECT_COUNT_LISTA;
	
	private final static String SQL_INDIRIZZO_PART_CAT = "SELECT DISTINCT s.prefisso || ' ' || s.nome AS nome, s.numero as codice_via, " +
													 " c.civico " +
													 "FROM sitiuiu p, siticivi_uiu cu, siticivi c, sitidstr s, siticomu com " +
													 "WHERE com.codi_fisc_luna = ? " +
													 "AND p.cod_nazionale = com.cod_nazionale " +
													 "AND p.foglio = ? " +
													 "AND p.particella = ? " +
													 "AND p.unimm = ? " +
													 "AND cu.pkid_uiu = p.pkid_uiu " +
													 "AND c.pkid_civi = cu.pkid_civi " +
													 "AND s.pkid_stra = c.pkid_stra";

	private final static String SQL_PLANIMETRIE = 			"select distinct nome_plan, progressivo, formato from DOCFA_planimetrie" + 
											" where fornitura= TO_DATE(?,'YYYYMMDD')" +
											" and protocollo = ? and (IDENTIFICATIVO_IMMO = ? or IDENTIFICATIVO_IMMO = 0)";

	private final static String SQL_PLANIMETRIE_UNRECORD = 			"select distinct nome_plan, progressivo from DOCFA_planimetrie" + 
	" where fornitura= TO_DATE(?,'YYYYMMDD')" +
	" and protocollo = ? and IDENTIFICATIVO_IMMO = ?";

	
	private String SQL_DATI_CEN = 			"SELECT DISTINCT CEN.FOGLIO      FOG,"+
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
											"	   AND CEN.FORNITURA =  TO_DATE(?,'YYYYMMDD')"+
											"	   AND TO_CHAR(TO_DATE(cen.DATA_REGISTRAZIONE,'YYYYMMDD'),'YYYYMM') = TO_CHAR(CEN.FORNITURA,'YYYYMM')	" +		
											//nuova da millucci
											"  AND MET.identificativo_immobile(+) = CEN.identificativo_immobile";
	
	private String SQL_DATI_SUB_VICINI =  "select distinct u.foglio foglio, u.particella particella, u.unimm as sub, u.categoria categoria, u.classe classe  " +
									"from sitiuiu u, siticomu c, " +
									"("+SQL_DATI_CEN+") XX " +
									"where u.FOGLIO = XX.fog " +
									"and u.PARTICELLA = XX.NUM " +
									"and c.CODI_FISC_LUNA = ? " +
									"and u.COD_NAZIONALE = c.COD_NAZIONALE " +
									"and u.SUB <> XX.SUB   " +
									"and data_fine_val = to_date('99991231','yyyymmdd') " +
									"order by categoria, unimm   ";
	
	public Hashtable mCaricareListaDocfa(Vector listaPar, DocfaFinder finder) throws Exception
	{
		if (listaPar != null && listaPar.size() > 0 && listaPar.get(0).getClass() == String.class) {
			if (((String)listaPar.get(0)).equals("STORICO_CONCESSIONI")) {
				//CHIAMATA DA STORICO CONCESSIONI
				return mCaricareListaDocfaStoricoConcessioni(listaPar, finder);
			} else if (((String)listaPar.get(0)).equals("popupDaCondono")) {
				//CHIAMATA DA CONDONO
				return mCaricareListaDocfaDaCondono(listaPar, finder);
			}			
		}		
		// carico la lista e la metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		sql = "";
		String orderByLista = " ORDER BY  DATA_REGISTRAZIONE desc, PROTOCOLLO,operazione desc ";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;
		// faccio la connessione al db
		try
		{
			conn = this.getConnection();
			
			Statement st = conn.createStatement();
			ResultSet rsE = st.executeQuery(SQL_COD_ENTE);
			String cod_ente = null;
			if (rsE.next())
				cod_ente = rsE.getString("uk_belfiore");
			rsE.close();
			st.close();
			
			
			int indice = 1;
			/*
			 * per ora disabilito il conteggione sql = SQL_SELECT_COUNT_ALL;
			 * this.initialize(); this.setInt(indice,1); indice ++;
			 * prepareStatement(sql); rs = executeQuery(conn); if (rs.next()){
			 * conteggione = rs.getLong("conteggio"); }
			 */

			for (int i = 0; i <= 1; i++)
			{
				// il primo ciclo faccio la count
				if (i == 0)
					sql = SQL_SELECT_COUNT_LISTA;
				else
					sql = SQL_SELECT_LISTA;

				indice = 1;
				this.initialize();
				this.setInt(indice, 1);
				indice++;

				// il primo passaggio esegue la select count
				// campi della search

				// il primo passaggio esegue la select count
				if (finder.getKeyStr().equals(""))
				{
					sql = this.elaboraFiltroMascheraRicerca(indice, listaPar);
				}
				else
				{
					// controllo provenienza chiamata
					String controllo = finder.getKeyStr();
					if(i == 0 )
						sql = sql + " AND D_GEN.PROTOCOLLO_REG || '|' || TO_CHAR(D_GEN.FORNITURA,'YYYYMMDD')   in (" + finder.getKeyStr() + ")";
					if (i == 1)
						sql = sql + " AND D_GEN.PROTOCOLLO_REG || '|' || TO_CHAR(D_GEN.FORNITURA,'YYYYMMDD')  in (" + finder.getKeyStr() + ")";

				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale() - 1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
				if (i == 1)
				{
					sql = sql + orderByLista;
					sql = sql + ")) where N > " + limInf + " and N <=" + limSup;
				}
				if(i == 0 )
					sql = sql + ")))";


				prepareStatement(sql);
				java.sql.ResultSet rs = null;
				try {
					
					rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
					if (i == 1)
					{
						while (rs.next())
						{
							// campi della lista
							Docfa docfa = new Docfa();
							docfa.setTecCognome(tornaValoreRS(rs,"TEC_COGNOME"));
							docfa.setTecNome(tornaValoreRS(rs,"TEC_NOME"));
							docfa.setProtocollo(tornaValoreRS(rs,"PROTOCOLLO"));
							docfa.setDataVariazione(tornaValoreRS(rs,"DATA_VARIAZIONE","YYYY-MM-DD"));
							docfa.setCausale(tornaValoreRS(rs,"CAUSALE"));
							docfa.setSoppressione(tornaValoreRS(rs,"SOPPRESSIONE"));
							docfa.setVariazione(tornaValoreRS(rs,"VARIAZIONE"));
							docfa.setCostituzione(tornaValoreRS(rs,"COSTITUZIONE"));
							//docfa.setCognome(tornaValoreRS(rs,"COGNOME"));
							//docfa.setNome(tornaValoreRS(rs,"NOME"));
							//docfa.setDenominazione(tornaValoreRS(rs,"DENOMINAZIONE"));
							//docfa.setCodiceFiscale(tornaValoreRS(rs,"CODICE_FISCALE"));
							//docfa.setPartitaIva(tornaValoreRS(rs,"PARTITA_IVA"));
							docfa.setOperazione(tornaValoreRS(rs,"OPERAZIONE"));
							docfa.setFoglio(tornaValoreRS(rs,"FOGLIO"));
							docfa.setParticella(tornaValoreRS(rs,"PARTICELLA"));
							docfa.setSubalterno(tornaValoreRS(rs,"SUBALTERNO"));
							docfa.setCategoria(tornaValoreRS(rs,"CATEGORIA"));
							
							String indirizzoUiu= tornaValoreRS(rs,"INDIRIZZO_UIU");
							String indirizzoUiuNew= Utils.pulisciVia(indirizzoUiu);
							
							String civicoUiu= tornaValoreRS(rs,"CIVICO_UIU");
							
							String civicoUiuNew="";
							
							if (civicoUiu != null){
			            		try{
			            			Integer civicoInt= Integer.valueOf(civicoUiu);
			            			civicoUiuNew= String.valueOf(civicoInt);
			            		}catch(Exception e){
			            			civicoUiuNew= civicoUiu;
			            		}
							}
							docfa.setIndirizzoUiu(indirizzoUiuNew + " " + civicoUiuNew);
							docfa.setDichiarante(tornaValoreRS(rs,"DICHIARANTE"));
							docfa.setIndirizzoDichiarante(tornaValoreRS(rs,"INDIRIZZO_DICHIARANTE"));
							docfa.setFornitura(tornaValoreRS(rs,"FORNITURA","YYYY-MM-DD"));
							docfa.setDataProtocollo(tornaValoreRS(rs,"DATA_PROTOCOLLO"));
							
							//ricavo indirizzi uiu da catasto
							//solo per docfa con uiuFoglio numerici altrimenti esplode!!!!
							ArrayList indCat = new ArrayList(); 
							try
							{
								Integer.parseInt(docfa.getFoglio());
							
							if (!docfa.getOperazione().equals("COSTITUITA"))
							{	
								PreparedStatement pstcat = null;
								ResultSet rscat = null;
								try {
									pstcat= conn.prepareStatement(SQL_INDIRIZZO_PART_CAT);
									pstcat.setString(1, cod_ente);
									pstcat.setInt(2, Integer.parseInt(docfa.getFoglio()));
									pstcat.setString(3, docfa.getParticella());
									
									//controllo se il SUB è rappresentato da spazi vuoti (DOCFA_UIU) --> lo imposto a 0 (sitiuiu non a unimm a vuoto ma solo a 0!)
									//altrimenti esplode la query!!!
									if(docfa.getSubalterno().trim().equals(""))
										docfa.setSubalterno("0");
									
									pstcat.setInt(4, Integer.parseInt(docfa.getSubalterno()));
									rscat = pstcat.executeQuery();
									while (rscat.next())
									{
										String appo = tornaValoreRS(rscat,"NOME")+" "+tornaValoreRS(rscat,"CIVICO");
										if (appo != null && !appo.trim().equals("")) 
											indCat.add(appo);
										
									}
								} finally {
									DbUtils.close(rscat);
									DbUtils.close(pstcat);
								}
								//docfa.setIndPart(indCat);
							}
							else
							{
								indCat.add(docfa.getIndirizzoUiu());
							}
							
							}catch (NumberFormatException e)
							{
								log.debug("valore foglio UIU non numerico!!!!");
								
							}
							docfa.setIndPart(indCat);
							
							vct.add(docfa);
						}
					
					}
					else
					{
						if (rs.next())
							conteggio = rs.getString("conteggio");
					}
				} finally {
					DbUtils.close(rs);
				}
			}
			ht.put(LISTA_DOCFA, vct);
			finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			// pagine totali
			finder.setPagineTotali(1 + new Double(Math.ceil((new Long(conteggio).longValue() - 1) / RIGHE_PER_PAGINA)).longValue());
			finder.setTotaleRecord(conteggione);
			finder.setRighePerPagina(RIGHE_PER_PAGINA);

			ht.put(FINDER, finder);
			
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
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}
	
	private Hashtable mCaricareListaDocfaStoricoConcessioni(Vector listaPar, DocfaFinder finder) throws Exception
	{	
		// carico la lista e la metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			// faccio la connessione al db
			conn = this.getConnection();
			
			Statement st = conn.createStatement();
			ResultSet rsE = st.executeQuery(SQL_COD_ENTE);
			String cod_ente = null;
			if (rsE.next())
				cod_ente = rsE.getString("uk_belfiore");
			rsE.close();
			st.close();
			
			String foglio = (String)listaPar.get(1);
			String particella = (String)listaPar.get(2);
			String subalterno = (String)listaPar.get(3);
			
			//recupero il ResultSet da StoricoConcessioniLogic
			rs = new StoricoConcessioniLogic(this.getEnvUtente()).getDocfa(conn, pstmt, foglio, particella, subalterno);

			//caricamento lista e conteggio totale record
			long lngConteggio = 0;
			long limInf = (finder.getPaginaAttuale() - 1) * RIGHE_PER_PAGINA;
			long limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
			while (rs.next()) {
				lngConteggio++;
				if (lngConteggio > limInf && lngConteggio <= limSup) {
					// campi della lista
					Docfa docfa = new Docfa();
					docfa.setTecCognome(tornaValoreRS(rs,"TEC_COGNOME"));
					docfa.setTecNome(tornaValoreRS(rs,"TEC_NOME"));
					docfa.setProtocollo(tornaValoreRS(rs,"PROTOCOLLO"));
					docfa.setDataVariazione(tornaValoreRS(rs,"DATA_VARIAZIONE","YYYY-MM-DD"));
					docfa.setCausale(tornaValoreRS(rs,"CAUSALE"));
					docfa.setSoppressione(tornaValoreRS(rs,"SOPPRESSIONE"));
					docfa.setVariazione(tornaValoreRS(rs,"VARIAZIONE"));
					docfa.setCostituzione(tornaValoreRS(rs,"COSTITUZIONE"));
					//docfa.setCognome(tornaValoreRS(rs,"COGNOME"));
					//docfa.setNome(tornaValoreRS(rs,"NOME"));
					//docfa.setDenominazione(tornaValoreRS(rs,"DENOMINAZIONE"));
					//docfa.setCodiceFiscale(tornaValoreRS(rs,"CODICE_FISCALE"));
					//docfa.setPartitaIva(tornaValoreRS(rs,"PARTITA_IVA"));
					docfa.setOperazione(tornaValoreRS(rs,"OPERAZIONE"));
					docfa.setFoglio(tornaValoreRS(rs,"FOGLIO"));
					docfa.setParticella(tornaValoreRS(rs,"PARTICELLA"));
					docfa.setSubalterno(tornaValoreRS(rs,"SUBALTERNO"));
					docfa.setIndirizzoUiu(tornaValoreRS(rs,"INDIRIZZO_UIU"));
					docfa.setDichiarante(tornaValoreRS(rs,"DICHIARANTE"));
					docfa.setIndirizzoDichiarante(tornaValoreRS(rs,"INDIRIZZO_DICHIARANTE"));
					docfa.setFornitura(tornaValoreRS(rs,"FORNITURA","YYYY-MM-DD"));
					docfa.setDataProtocollo(tornaValoreRS(rs,"DATA_PROTOCOLLO"));
					
					//ricavo indirizzi uiu da catasto
					//solo per docfa con uiuFoglio numerici altrimenti esplode!!!!
					ArrayList indCat = new ArrayList(); 
					try
					{
						Integer.parseInt(docfa.getFoglio());
					
					if (!docfa.getOperazione().equals("COSTITUITA"))
					{	
						PreparedStatement pstcat= conn.prepareStatement(SQL_INDIRIZZO_PART_CAT);
						pstcat.setString(1, cod_ente);
						pstcat.setInt(2, Integer.parseInt(docfa.getFoglio()));
						pstcat.setString(3, docfa.getParticella());
						
						//controllo se il SUB è rappresentato da spazi vuoti (DOCFA_UIU) --> lo imposto a 0 (sitiuiu non a unimm a vuoto ma solo a 0!)
						//altrimenti esplode la query!!!
						if(docfa.getSubalterno().trim().equals(""))
							docfa.setSubalterno("0");
						
						pstcat.setInt(4, Integer.parseInt(docfa.getSubalterno()));
						
						ResultSet rscat = pstcat.executeQuery();
						
						while (rscat.next())
						{
							String appo = tornaValoreRS(rscat,"NOME")+" "+tornaValoreRS(rscat,"CIVICO");
							if (appo != null && !appo.trim().equals("")) 
								indCat.add(appo);
							
						}
						rscat.close();
						pstcat.close();
						//docfa.setIndPart(indCat);
					}
					else
					{
						indCat.add(docfa.getIndirizzoUiu());
					}
					
					}catch (NumberFormatException e)
					{
						log.debug("valore foglio UIU non numerico!!!!");
						
					}
					docfa.setIndPart(indCat);
					
					vct.add(docfa);
				}
			}
			conteggio = "" + lngConteggio;

			ht.put(LISTA_DOCFA, vct);
			finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			// pagine totali
			finder.setPagineTotali(1 + new Double(Math.ceil((new Long(conteggio).longValue() - 1) / RIGHE_PER_PAGINA)).longValue());
			finder.setTotaleRecord(conteggione);
			finder.setRighePerPagina(RIGHE_PER_PAGINA);

			ht.put(FINDER, finder);
			
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
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			DbUtils.close(rs);
			DbUtils.close(pstmt);
			DbUtils.close(conn);
			
		}
	}
	
	private Hashtable mCaricareListaDocfaDaCondono(Vector listaPar, DocfaFinder finder) throws Exception
	{
		// carico la lista e la metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		sql = "";
		String orderByLista = " ORDER BY DATA_REGISTRAZIONE desc, PROTOCOLLO,operazione desc ";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;
		
		// faccio la connessione al db
		try
		{
			conn = this.getConnection();
			
			Statement st = conn.createStatement();
			ResultSet rsE = st.executeQuery(SQL_COD_ENTE);
			String cod_ente = null;
			if (rsE.next())
				cod_ente = rsE.getString("uk_belfiore");
			rsE.close();
			st.close();			
			
			int indice = 1;

			for (int i = 0; i <= 1; i++)
			{
				// il primo ciclo faccio la count
				if (i == 0)
					sql = SQL_SELECT_COUNT_LISTA;
				else
					sql = SQL_SELECT_LISTA;

				indice = 1;
				this.initialize();
				this.setInt(indice, 1);
				indice++;
				
				sql += "AND U.FOGLIO = LPAD(?, 4, '0') AND U.NUMERO = LPAD(?, 5, '0') AND U.SUBALTERNO = LPAD(?, 4, '0') ";
				
				String foglio = (String)listaPar.get(1);
				String mappale = (String)listaPar.get(2);
				String sub = (String)listaPar.get(3);
				
				this.setString(indice, foglio);
				indice++;
				this.setString(indice, mappale);
				indice++;
				this.setString(indice, sub);
				indice++;

				if (i == 1)
				{
					sql = sql + orderByLista;
					sql = sql + "))"; //senza rownum (non c'è paginazione)
				}
				if(i == 0)
					sql = sql + ")))";

				prepareStatement(sql);
				java.sql.ResultSet rs = null;
				
				try {
					
					rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
					if (i == 1)
					{
						while (rs.next())
						{
							// campi della lista
							Docfa docfa = new Docfa();
							docfa.setTecCognome(tornaValoreRS(rs,"TEC_COGNOME"));
							docfa.setTecNome(tornaValoreRS(rs,"TEC_NOME"));
							docfa.setProtocollo(tornaValoreRS(rs,"PROTOCOLLO"));
							docfa.setDataVariazione(tornaValoreRS(rs,"DATA_VARIAZIONE","YYYY-MM-DD"));
							docfa.setCausale(tornaValoreRS(rs,"CAUSALE"));
							docfa.setSoppressione(tornaValoreRS(rs,"SOPPRESSIONE"));
							docfa.setVariazione(tornaValoreRS(rs,"VARIAZIONE"));
							docfa.setCostituzione(tornaValoreRS(rs,"COSTITUZIONE"));
							docfa.setOperazione(tornaValoreRS(rs,"OPERAZIONE"));
							docfa.setFoglio(tornaValoreRS(rs,"FOGLIO"));
							docfa.setParticella(tornaValoreRS(rs,"PARTICELLA"));
							docfa.setSubalterno(tornaValoreRS(rs,"SUBALTERNO"));
							docfa.setIndirizzoUiu(tornaValoreRS(rs,"INDIRIZZO_UIU"));
							docfa.setDichiarante(tornaValoreRS(rs,"DICHIARANTE"));
							docfa.setIndirizzoDichiarante(tornaValoreRS(rs,"INDIRIZZO_DICHIARANTE"));
							docfa.setFornitura(tornaValoreRS(rs,"FORNITURA","YYYY-MM-DD"));
							docfa.setDataProtocollo(tornaValoreRS(rs,"DATA_PROTOCOLLO"));
							
							//ricavo indirizzi uiu da catasto
							//solo per docfa con uiuFoglio numerici altrimenti esplode!!!!
							ArrayList indCat = new ArrayList(); 
							try
							{
								Integer.parseInt(docfa.getFoglio());
							
							if (!docfa.getOperazione().equals("COSTITUITA"))
							{	
								PreparedStatement pstcat = null; 
								ResultSet rscat = null;
								try {
									pstcat= conn.prepareStatement(SQL_INDIRIZZO_PART_CAT);
									pstcat.setString(1, cod_ente);
									pstcat.setInt(2, Integer.parseInt(docfa.getFoglio()));
									pstcat.setString(3, docfa.getParticella());
									
									//controllo se il SUB è rappresentato da spazi vuoti (DOCFA_UIU) --> lo imposto a 0 (sitiuiu non a unimm a vuoto ma solo a 0!)
									//altrimenti esplode la query!!!
									if(docfa.getSubalterno().trim().equals(""))
										docfa.setSubalterno("0");
									
									pstcat.setInt(4, Integer.parseInt(docfa.getSubalterno()));
									
									rscat = pstcat.executeQuery();
									
									while (rscat.next())
									{
										String appo = tornaValoreRS(rscat,"NOME")+" "+tornaValoreRS(rscat,"CIVICO");
										if (appo != null && !appo.trim().equals("")) 
											indCat.add(appo);
										
									}

								} finally {
									DbUtils.close(rscat);
									DbUtils.close(pstcat);
								}
							}
							else
							{
								indCat.add(docfa.getIndirizzoUiu());
							}
							
							}catch (NumberFormatException e)
							{
								log.debug("valore foglio UIU non numerico!!!!");
								
							}
							docfa.setIndPart(indCat);
							
							vct.add(docfa);
						}
					}
					else
					{
						if (rs.next())
							conteggio = rs.getString("conteggio");
					}
				} finally {
					DbUtils.close(rs);
				}
			}
			ht.put(LISTA_DOCFA, vct);
			finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			// pagine totali
			finder.setPagineTotali(1 + new Double(Math.ceil((new Long(conteggio).longValue() - 1) / RIGHE_PER_PAGINA)).longValue());
			finder.setTotaleRecord(conteggione);
			finder.setRighePerPagina(RIGHE_PER_PAGINA);

			ht.put(FINDER, finder);
			
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
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}

	public Hashtable mCaricareDettaglioDocfa(String chiave) throws Exception
	{
		Date inizio; Date fine; long durata;
		inizio = new Date();
		Hashtable ht = new Hashtable();
		// faccio la connessione al db
		Connection conn = null;
		java.sql.ResultSet rs = null;
		
		Context cont;
		try
		{
			
			String  protocollo = "";
			String fornitura = "";
			
			 protocollo=chiave.substring(0,chiave.indexOf("|"));
			 fornitura=chiave.substring(chiave.indexOf("|")+1);
			 
			 if (fornitura.indexOf("|")>0)
				 fornitura=fornitura.substring(0,fornitura.indexOf("|"));
			
			
			conn = this.getConnection();
			
			Statement st = conn.createStatement();
			ResultSet rsE = st.executeQuery(SQL_COD_ENTE);
			String cod_ente = null;
			if (rsE.next())
				cod_ente = rsE.getString("uk_belfiore");
			rsE.close();
			st.close();
		
			DocfaService docfaService= (DocfaService)getEjb("CT_Service", "CT_Service_Data_Access", "DocfaServiceBean");
			ElaborazioniService elabService= (ElaborazioniService)getEjb("CT_Service", "CT_Service_Data_Access", "ElaborazioniServiceBean");
			
			SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
			
			RicercaOggettoDocfaDTO rd = new RicercaOggettoDocfaDTO();
			rd.setEnteId(this.getEnvUtente().getEnte());
			rd.setUserId(this.getEnvUtente().getUtente().getUsername());
			rd.setProtocollo(protocollo);
			rd.setFornitura(yyyyMMdd.parse(fornitura));
			
			DettaglioDocfaDTO dto = docfaService.getDettaglioDocfa(rd);
			
			
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
			this.initialize();
			this.setString(1, protocollo);
			this.setString(2, fornitura);
			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			Docfa docfa = new Docfa();
			docfa.setCodEnte(cod_ente);
			if (rs.next())
			{ 
				docfa.setProtocollo(tornaValoreRS(rs,"PROTOCOLLO"));
				docfa.setDataVariazione(tornaValoreRS(rs,"DATA_VARIAZIONE","YYYY-MM-DD"));
				docfa.setCausale(tornaValoreRS(rs,"CAUSALE"));
				docfa.setFornitura(tornaValoreRS(rs,"FORNITURA","YYYY-MM-DD"));
			}
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
			this.initialize();
			this.setString(1, protocollo);
			this.setString(2, fornitura);
			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			if (rs.next())
			{
				docfa.setSoppressione(tornaValoreRS(rs,"SOPPRESSIONE"));
				docfa.setVariazione(tornaValoreRS(rs,"VARIAZIONE"));
				docfa.setCostituzione(tornaValoreRS(rs,"COSTITUZIONE"));
				docfa.setDerivSpe(tornaValoreRS(rs,"DERIV_SPE"));
			}
			ht.put(DOCFA, docfa);
			
			
			// DOCFA_INTESTATI
			ht.put(LISTA_DETTAGLIO_DOCFA_INTESTATI, dto.getIntestati());	
			
			fine = new Date();
			durata = fine.getTime() - inizio.getTime();
			log.debug("DURATA 1: " +durata);
			inizio = new Date();
			
			//DOCFA_UIU
			sql ="SELECT TIPO_OPERAZIONE    AS TIPO,  "+
				"	   FOGLIO             AS FOGLIO,"+
				"	   NUMERO             AS NUMERO,"+
				"	   SUBALTERNO         AS SUB,"+
				"	   TRIM(INDIR_TOPONIMO)     AS INDIRIZZO,"+
				"      INDIR_NCIV_UNO, "+
				"	   NR_PROG AS PROGR FROM DOCFA_UIU U"+
				"	   WHERE"+
				"	   U.PROTOCOLLO_REG = ?"+
				"	   AND U.FORNITURA =  TO_DATE(?,'YYYYMMDD') ";
			this.initialize();
			this.setString(1, protocollo);
			this.setString(2, fornitura);			
			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			
			fine = new Date();
			durata = fine.getTime() - inizio.getTime();
			log.debug("DURATA 1.1: " +durata);
			inizio = new Date();
			int i=0;
			ArrayList listaDocfaUiu = new ArrayList();
			HashMap<String, GenericTuples.T2<String,String>> hmCoord = new HashMap<String, GenericTuples.T2<String,String>>();
			while (rs.next())
			{
				inizio = new Date();
				log.debug("iteraz: " +i);
				Docfa locaList = new Docfa();
				locaList.setTipo(tornaValoreRS(rs,"TIPO"));
				locaList.setFoglio(tornaValoreRS(rs,"FOGLIO"));
				locaList.setParticella(tornaValoreRS(rs,"NUMERO"));
				locaList.setSubalterno(tornaValoreRS(rs,"SUB"));
				String indirizzo=tornaValoreRS(rs,"INDIRIZZO");
				String indirizzoNew= Utils.pulisciVia(indirizzo);
				String numeroCivico= tornaValoreRS(rs,"INDIR_NCIV_UNO");
				String numeroCivicoNew="";
				if (numeroCivico != null && !numeroCivico.equals("")){
            		try{
            			Integer civicoInt= Integer.valueOf(numeroCivico);
            			numeroCivicoNew= String.valueOf(civicoInt);
            		}catch(Exception e){
            			numeroCivicoNew= numeroCivico;
            		}
        		}
				locaList.setIndirizzoDichiarante(indirizzoNew + " "+ numeroCivicoNew);
				locaList.setNrProg(tornaValoreRS(rs,"PROGR"));
				fine = new Date();
				durata = fine.getTime() - inizio.getTime();
				log.debug("DURATA 1.2: " +durata);
				inizio = new Date();
				
				
				/*
				//indirizzo per ricerca in mappa prospettica
				String indRic = ritornaIndirizzoRicerca(locaList.getIndirizzoDichiarante().toUpperCase());
				locaList.setIndirizzoUiu(indRic);
				*/
				ArrayList listGraf = (ArrayList)mDatiGraffati(tornaValoreRS(rs,"FOGLIO"), tornaValoreRS(rs,"NUMERO"), tornaValoreRS(rs,"SUB"),cod_ente);
				if (listGraf != null)
					locaList.setPresenzaGraffati("S");
				else
					locaList.setPresenzaGraffati("-");
				fine = new Date();
				durata = fine.getTime() - inizio.getTime();
				log.debug("DURATA 1.3: " +durata);
				inizio = new Date();
				
				String keyCoord = locaList.getFoglio() + "|" + locaList.getParticella() + "|" + cod_ente;
				if (hmCoord.get(keyCoord) == null) {
					GenericTuples.T2<String,String> coord1 = null;
					try {
						coord1 = getLatitudeLongitude(locaList.getFoglio(), locaList.getParticella(), cod_ente);
					} catch (Exception e) {
						log.error("Errore reperimento coordinate latitudine longitudine " , e);
					}
					hmCoord.put(keyCoord, coord1);
				}
				GenericTuples.T2<String,String> coord = hmCoord.get(keyCoord);
				locaList.setCoord(coord);

				fine = new Date();
				durata = fine.getTime() - inizio.getTime();
				log.debug("DURATA 1.4: " +durata);
				
				
				listaDocfaUiu.add(locaList);
				i++;
			}
			ht.put(LISTA_DETTAGLIO_DOCFA_UIU, listaDocfaUiu);
			
			inizio = new Date();
			
			fine = new Date();
			durata = fine.getTime() - inizio.getTime();
			log.debug("DURATA 2: " +durata);
			inizio = new Date();
			
			List<BeniNonCensDTO> listaDocfaBeniNonCens = docfaService.getListaBeniNonCensibili(rd);
			ht.put(LISTA_DETTAGLIO_DOCFA_BENI_NON_CENS, listaDocfaBeniNonCens);			
			

			List<DocfaAnnotazioni> listaDocfaAnnotazioni = dto.getAnnotazioni();
			ht.put(LISTA_DETTAGLIO_DOCFA_ANNOTAZIONI, listaDocfaAnnotazioni);	
			
			fine = new Date();
			durata = fine.getTime() - inizio.getTime();
			log.debug("DURATA 3: " +durata);
			inizio = new Date();

			
			
			
			//DOCFA_DATI_CENSUARI
			sql =SQL_DATI_CEN;
			
			this.initialize();
			this.setString(1, protocollo);
			this.setString(2, fornitura);
			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			ArrayList listaDocfaDatiCensuari = new ArrayList();
			while (rs.next())
			{
				Docfa locaList = new Docfa();
				locaList.setDataRegistrazione(tornaValoreRS(rs,"DATA_REGISTRAZIONE","YYYYMMDD"));
				locaList.setFoglio(tornaValoreRS(rs,"FOG"));
				locaList.setParticella(tornaValoreRS(rs,"NUM"));
				locaList.setSubalterno(tornaValoreRS(rs,"SUB"));
				locaList.setZona(tornaValoreRS(rs,"ZONA"));
				
				if(tornaValoreRS(rs,"CAT").equals("A10") || tornaValoreRS(rs,"CAT").startsWith("C0"))
				{
					String min = getClasseMinima(tornaValoreRS(rs,"ZONA"),tornaValoreRS(rs,"FOG"),tornaValoreRS(rs,"CAT"));
					if(min == null)
					{
						locaList.setClasse(tornaValoreRS(rs,"CLS")+"<span style=\"color: red;\">(!)</span>");
					}
					else
					{
						String style="";
						try {
							if(new Integer(tornaValoreRS(rs,"CLS")).intValue() < new Integer(min).intValue())
							{
								style="color: red;";
							}
						} catch (Exception e) {}
						
						if(min.length() == 1)
							min = "0"+min;
						locaList.setClasse(tornaValoreRS(rs,"CLS")+"<span style=\""+style+"\">(min. "+min+")</span>");
					}
				}
				else
				{
					locaList.setClasse(tornaValoreRS(rs,"CLS"));
				}
				locaList.setCategoria(tornaValoreRS(rs,"CAT"));
				locaList.setConsistenza(tornaValoreRS(rs,"CONS"));
				locaList.setSuperfice(tornaValoreRS(rs,"SUP"));
				locaList.setRendita(tornaValoreRS(rs,"RENDITA_EU"));
				locaList.setIdentificativoImm(tornaValoreRS(rs,"identificativo_immobile"));
				locaList.setPresenzaGraffati(tornaValoreRS(rs,"presenza_graffati"));
				locaList.setProtocollo(protocollo);
				locaList.setFornitura(fornitura);
			
			
				//MB --> calcolo rendita
				 RenditaDocfa rdocfa = mCalcoloRendita(locaList,cod_ente);
				 locaList.setRendDocfa(rdocfa);
				//MB --> fine
				
				
				//CONTROLLO CLASSAMENTO RICHIAMATO DAL CT_SERVICE (COME SU BOD E C336) - Gestione Assenza Zone OMI per ZC/Foglio
				RicercaOggettoDocfaDTO rdu = new RicercaOggettoDocfaDTO();
				rdu.setEnteId(rd.getEnteId());
				rdu.setUserId(rd.getUserId());
				rdu.setProtocollo(rd.getProtocollo());
				rdu.setFornitura(rd.getFornitura());
				rdu.setFoglio(locaList.getFoglio());
				rdu.setParticella(locaList.getParticella());
				rdu.setUnimm(locaList.getSubalterno());
				/*
				 * usiamo il campo seguente per impostare il metodo di calcolo del valore
				 * commerciale deciso 
				 */
				rdu.setTipoOperDocfa(this.getModalitaCalcoloValoreCommerciale());
				
				ControlloClassamentoConsistenzaDTO conCls = elabService.getControlliClassConsistenzaByDocfaUiu(rdu);
				
				locaList.setConCls(conCls);
				
				listaDocfaDatiCensuari.add(locaList);
			}
			
			ht.put(LISTA_DETTAGLIO_DOCFA_DATI_CENSUARI, listaDocfaDatiCensuari);
			
			fine = new Date();
			durata = fine.getTime() - inizio.getTime();
			log.debug("DURATA 4: " +durata);
			inizio = new Date();
	
			//DOCFA_DICHIARANTI
			ArrayList listaDocfaDichiaranti = new ArrayList();
			for (DocfaDichiaranti d : dto.getDichiaranti())
			{
				Docfa dicList = new Docfa();
				dicList.setCognome(d.getDicCognome()!=null ? d.getDicCognome() : "-");
				dicList.setNome(d.getDicNome()!=null ? d.getDicNome() : "-");
				
				String indirizzo=d.getDicResIndir();
				String indirizzoNew= Utils.pulisciVia(indirizzo);
				String numeroCivico= d.getDicResNumciv();
				String numeroCivicoNew="";
				if (numeroCivico != null && !numeroCivico.equals("")){
            		try{
            			Integer civicoInt= Integer.valueOf(numeroCivico);
            			numeroCivicoNew= String.valueOf(civicoInt);
            		}catch(Exception e){
            			numeroCivicoNew= numeroCivico;
            		}
        		}
				
				dicList.setIndirizzoDichiarante(indirizzoNew+" "+numeroCivicoNew);
				
				dicList.setLuogo(d.getDicResCom()!=null ? d.getDicResCom() : "-");	
				dicList.setTecCognome(d.getTecCognome()!=null ? d.getTecCognome() : "-");
				dicList.setTecNome(d.getTecNome()!=null ? d.getTecNome() : "-");
				
				listaDocfaDichiaranti.add(dicList);
			}
			
			ht.put(LISTA_DETTAGLIO_DOCFA_DICHIARANTI, listaDocfaDichiaranti);
			
			fine = new Date();
			durata = fine.getTime() - inizio.getTime();
			log.debug("DURATA 5: " +durata);
			inizio = new Date();

			//DOCFA_IN_PARTE_UNO
			List<DocfaInParteUnoDTO> listaDocfaParteUno = docfaService.getListaDocfaInParteUno(rd);
			ht.put(LISTA_DETTAGLIO_DOCFA_PARTE_UNO, listaDocfaParteUno);	
			
			//DOCFA_OPERATORE
			List<OperatoreDTO>  listaDocfaOperatore = docfaService.getListaOperatori(rd);
			ht.put(LISTA_DETTAGLIO_DOCFA_OPER, listaDocfaOperatore);	
			
			fine = new Date();
			durata = fine.getTime() - inizio.getTime();
			log.debug("DURATA 6: " +durata);
			inizio = new Date();
			
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
			log.error("Error dettaglio",e);
			throw e;
		}
		finally
		{
			DbUtils.close(rs);
			DbUtils.close(conn);
		}
		
	}
	
/*	private String ritornaIndirizzoRicerca(String indCompleto){
		
		String indRic="";
		String sedime = "";
		if (indCompleto != null){
			String[] voci = indCompleto.split(" ");
			if (voci.length>1){
				sedime = sedimario.get(voci[0].toUpperCase());
				if (!sedime.equals("")){
					indRic = indCompleto.substring(indCompleto.indexOf(" ")+1); 
				}
			}
			else
				indRic=indCompleto;
		}
		
		indRic= indRic.concat(",Milano");
		return indRic;
			
	}
*/	
	
	private String getClasseMinima(String zona, String foglio, String categoria)
	{
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection conn = null;
		try
		{

			String sql = "select CLASSE_MIN from docfa_classi_min where nvl(TO_NUMBER(ZC),-1)  = nvl(?,-1) and TO_NUMBER(FOGLIO) = ? and CATEGORIA = ?";

			conn = getConnection();
			ps = conn.prepareStatement(sql);
			
			Integer zonaInt = null;
			try {
				zonaInt = new Integer(zona).intValue();
				ps.setInt(1, zonaInt);
			}catch (Exception e) {
				ps.setNull(1,Types.INTEGER);
			}
			Integer foglioInt = null;
			try {
				foglioInt = new Integer(foglio).intValue();
				ps.setInt(2,foglioInt );
			}catch (Exception e) {
				ps.setNull(2,Types.INTEGER);
			}

			
			ps.setString(3, categoria);
			rs = ps.executeQuery();

			if (rs.next())
			{
				return rs.getString("CLASSE_MIN");
			}

			return null;

		}
		catch (Exception e)
		{
			log.error("Errore !" , e);
			return null;
		}
		finally {
			
			try { 
				rs.close();
			} catch (Exception e) {}
			try {
				ps.close();
			} catch (Exception e) {}
			try {
				conn.close();
			} catch (Exception e) {}
		}
	}
	
	public Integer getNumPlanimetrie(String protocollo, String fornitura, String idimm) {
		ResultSet rs =null;
		PreparedStatement ps = null;
		Connection conn = null;
		try {
			
			String sql = SQL_PLANIMETRIE;
			
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1,fornitura);
			ps.setString(2, protocollo);
			ps.setString(3, idimm);
			rs = ps.executeQuery();
			int i = 0;
			while (rs.next()) {
				i+=1;
			}
			return new Integer(i);
			
		} catch (Exception e) {
			return null;
		} 
		finally {
			try { 
				rs.close();
			} catch (Exception e) {}
			try {
				ps.close();
			} catch (Exception e) {}
			try {
				conn.close();
			} catch (Exception e) {}
		}
	}
	public Integer presenzaPlanimetrieSenzaImm(String protocollo, String fornitura, String idimm) {
		ResultSet rs =null;
		PreparedStatement ps = null;
		Connection conn = null;
		try {
			
			String sql = SQL_PLANIMETRIE;
			
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1,fornitura);
			ps.setString(2, protocollo);
			ps.setInt(3, 0);
			rs = ps.executeQuery();
			int i = 0;
			while (rs.next()) {
				i+=1;
			}
			return new Integer(i);
			
		} catch (Exception e) {
			return null;
		} 
		finally {
			try { 
				rs.close();
			} catch (Exception e) {}
			try {
				ps.close();
			} catch (Exception e) {}
			try {
				conn.close();
			} catch (Exception e) {}
		}
	}	

	public File getImmagineConcessioneVisura(String pathDatiDiogene, String pathTiff) {
		try {
			File f = null;
			String pathPlanimetrie = pathDatiDiogene + File.separatorChar + this.getDirConcessioniVisureEnte();
			String pathFile = pathPlanimetrie + pathTiff;
			log.debug("Concessioni Visure:" + pathFile);
			f = new File(pathFile);
			
			return f;
		} catch (Exception e) {
			return null;
		} 
	}//-------------------------------------------------------------------------
	
	public File getPdfDocfa(String pathDatiDiogene, String protocollo, String fornitura) {
		FileImageInputStream input;
		
		try {
			
			File f = null;
			String pathPdfDocfa = pathDatiDiogene + File.separatorChar + this.getDirPdfDocfaEnte();
			String nomeFile = protocollo + ".PDF";
			String pathFile = pathPdfDocfa  + File.separatorChar + fornitura.substring(0,6) + File.separatorChar +nomeFile;
			log.debug("File pdf richiesto:"+pathFile);
			f = new File(pathFile);


			return f;
		} catch (Exception e) {
			return null;
		} finally {
		}
	}
	
	
	public List getImmaginePlanimetria(String pathDatiDiogene, String protocollo, String fornitura, String idImmo) {
		ResultSet rs =null;
		PreparedStatement ps = null;
		Connection conn = null;
		
		List l = new ArrayList();
		
		
		try {
			
			String sql = SQL_PLANIMETRIE;
			
			
			conn = getConnection();
			log.debug(SQL_PLANIMETRIE+"   -- params:"+fornitura+"@"+protocollo+"@"+idImmo);
			ps = conn.prepareStatement(sql);
			ps.setString(1,fornitura);
			ps.setString(2, protocollo);
			ps.setString(3, idImmo);
			rs = ps.executeQuery();
			
			File f = null;
			while (rs.next()) {
				String nomeFile = rs.getString(1);
				String progr =rs.getString(2); 
				int formato= rs.getInt(3);
				while(progr.length() < 3 )
					progr="0"+progr;
				nomeFile = nomeFile +"."+progr+".tif";
				String pathPlanimetrie = pathDatiDiogene +  File.separatorChar + this.getDirPlanimetrieEnte();
				String pathFile = pathPlanimetrie +  File.separatorChar + fornitura.substring(0, 6)+ File.separatorChar + nomeFile;
				log.debug("Planimetria:"+pathFile);
				f = new File(pathFile);
				DocfaPlanimetrie docfaPlanimetrie= new DocfaPlanimetrie();
				docfaPlanimetrie.setFile(f);
				docfaPlanimetrie.setFormato(formato);
				l.add(docfaPlanimetrie);
			}
			return l;
			//	return data;
		} catch (Exception e) {
			return null;
		} finally {
			try { 
				rs.close();
			} catch (Exception e) {}
			try {
				ps.close();
			} catch (Exception e) {}
			try {
				conn.close();
			} catch (Exception e) {}

		}
	}

	
	public ArrayList<OggettiTARSU> mDati340(String f, String m, String s)
	throws Exception
	{
		// riutulizzo OggettiTARSU che contiene bene idati del 340 perche fatto in precedenza
		Connection conn = null;
		java.sql.ResultSet rsVani = null;
		try
		{
			sql = "SELECT vano, piano, edificio, ambiente, supe_ambiente, altezzamin, altezzamax "+
			"		FROM sitiedi_vani "+
			"		WHERE foglio = ? AND particella = lpad(?,5,'0') AND unimm = ? and data_fine_val = to_date('31-12-9999','dd-mm-yyyy') order by ambiente";

			conn = this.getConnection();
			this.initialize();
			this.setString(1,f);
			this.setString(2,m);
			this.setString(3,s.equals("-")?"0":s);
			prepareStatement(sql);
			rsVani = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			ArrayList<OggettiTARSU> dettaglioVani = new ArrayList<OggettiTARSU>();
			while (rsVani.next())
			{
				OggettiTARSU vani = new OggettiTARSU();
				vani.setVano(tornaValoreRS(rsVani,"vano"));
				vani.setPiano(tornaValoreRS(rsVani,"piano"));
				vani.setEdificio(tornaValoreRS(rsVani,"edificio"));
				vani.setAmbiente(tornaValoreRS(rsVani,"ambiente"));
				vani.setSupVani(tornaValoreRS(rsVani,"supe_ambiente"));
				vani.setAltezzaMin(tornaValoreRS(rsVani,"altezzamin"));
				vani.setAltezzaMax(tornaValoreRS(rsVani,"altezzamax"));
				dettaglioVani.add(vani);
			}
			return dettaglioVani;
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			DbUtils.close(rsVani);
			DbUtils.close(conn);
		}
		
	}
	
	public ArrayList<PrgDocfa> mDatiPrg(String ente, String foglio, String particella)
	throws Exception
	{
		// riutulizzo OggettiTARSU che contiene bene idati del 340 perche fatto in precedenza
		Connection conn = null;
		java.sql.ResultSet rsPrg = null;
		try
		{
			sql = "SELECT foglio, particella, allegato, codice as dest_funz, descrizione as legenda, " +
				  "TO_CHAR (area_part, '999999d00') as area_part, " +
				  "TO_CHAR (area_prg, '999999d00') as area_prg " +
				  //",TO_CHAR (sdo_geom.sdo_area (area_int, 1), '999999d00') area_int " +
				  "FROM (SELECT p.foglio, p.particella, p.allegato, pr.codice, l.descrizione, " +
				  "sdo_geom.sdo_area (p.shape, 0.005) area_part, " +
				  "sdo_geom.sdo_area (pr.shape, 0.005) area_prg " +
				  //",sdo_geom.sdo_intersection (p.shape, pr.shape, 0.005) area_int " +
				  "FROM sitipart p, prg pr, sitideco_catalog l, siticomu c " +
				  "WHERE c.codi_fisc_luna = ? " +
				  "AND p.cod_nazionale = c.cod_nazionale " +
				  "AND p.foglio = ? " +
				  "AND p.particella = LPAD (TRIM (?), 5, '0') " +
				  "AND p.data_fine_val = TO_DATE ('31/12/9999', 'dd/mm/yyyy') " +
				  "AND l.id_cat = 1 " +
				  "AND l.codut = pr.codice " +
				  "AND sdo_relate (pr.shape,(SELECT shape FROM sitipart, siticomu " +
				  "WHERE siticomu.codi_fisc_luna = ? " +
				  "AND sitipart.cod_nazionale = siticomu.cod_nazionale " +
				  "AND sitipart.foglio = ? " +
				  "AND sitipart.particella = LPAD (TRIM (?), 5, '0') " +
				  "AND sitipart.data_fine_val = TO_DATE ('31/12/9999', 'dd/mm/yyyy')),'MASK=ANYINTERACT') = 'TRUE') ";

			conn = this.getConnection();
			this.initialize();
			this.setString(1,ente);
			this.setInt(2,Integer.parseInt(foglio));
			this.setString(3,particella);
			this.setString(4,ente);
			this.setInt(5,Integer.parseInt(foglio));
			this.setString(6,particella);
			prepareStatement(sql);
			rsPrg = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			ArrayList<PrgDocfa> dettaglioPRG = new ArrayList<PrgDocfa>();
			while (rsPrg.next())
			{
				PrgDocfa docfaPrg = new PrgDocfa();
				docfaPrg.setFoglio(tornaValoreRS(rsPrg,"foglio"));
				docfaPrg.setParticella(tornaValoreRS(rsPrg,"particella"));
				docfaPrg.setAllegato(tornaValoreRS(rsPrg,"allegato"));
				docfaPrg.setDestFunz(tornaValoreRS(rsPrg,"dest_funz"));
				docfaPrg.setLegenda(tornaValoreRS(rsPrg,"legenda"));
				//docfaPrg.setAreaInt(tornaValoreRS(rsPrg,"area_int"));
				docfaPrg.setAreaPart(tornaValoreRS(rsPrg,"area_part"));
				docfaPrg.setAreaPRG(tornaValoreRS(rsPrg,"area_prg"));
				dettaglioPRG.add(docfaPrg);
			}
			return dettaglioPRG;
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			DbUtils.close(rsPrg);
			DbUtils.close(conn);
		}
		
	}
	
	public ArrayList<ConcessioneDocfa> mDatiConcessioni(String ente, String foglio, String particella)
	throws Exception
	{
		// riutulizzo OggettiTARSU che contiene bene idati del 340 perche fatto in precedenza
		Connection conn = null;
		java.sql.ResultSet rsPrg = null;
		try
		{
			sql = "SELECT DISTINCT " +
					"o.FOGLIO, " +
					"o.PARTICELLA, " +
					"o.SUBALTERNO," +
					"c.pk_conc pk_conc, " +
					"c.rif_numero numero_protocollo, " +
					"c.rif_anno anno_protocollo, " +
					"to_char(c.data_protocollo,'dd/mm/yyyy') data_protocollof, " +
					"c.data_protocollo, " +
					"p.codice_fiscale, " +
					"p.cognome_ragsoc, p.nome," +
					"decode(cp.tipo_soggetto,   1,   'Proprietario',   'Richiedente') tipo_soggetto " +
					"FROM mi_concessioni c, " +
					"mi_concessioni_persona p, " +
					"mi_concessioni_conc_per cp, " +
					"mi_concessioni_oggetto o " +
					"WHERE p.pk_persona = cp.fk_persona " +
					"AND o.fk_conc = cp.fk_conc " +
					"AND cp.fk_conc = c.pk_conc " +
					"AND p.CODENTE =  c.CODENTE " +
					"AND c.CODENTE = o.CODENTE " +
					"AND c.CODENTE = ? " +
					"AND LPAD (TRIM (o.FOGLIO), 4, '0')  = LPAD (TRIM (?), 4, '0') " +
					"AND  LPAD (TRIM (o.PARTICELLA), 5, '0') =  LPAD (TRIM (?), 5, '0') " +
					"order by o.foglio, o.particella, o.SUBALTERNO, c.data_protocollo";

			conn = this.getConnection();
			this.initialize();
			this.setString(1,ente);
			this.setString(2,foglio);
			this.setString(3,particella);
			prepareStatement(sql);
			rsPrg = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			ArrayList<ConcessioneDocfa> dettaglioConc = new ArrayList<ConcessioneDocfa>();
			while (rsPrg.next())
			{
				ConcessioneDocfa docfaConc = new ConcessioneDocfa();
				docfaConc.setFoglio(tornaValoreRS(rsPrg,"foglio"));
				docfaConc.setParticella(tornaValoreRS(rsPrg,"particella"));
				docfaConc.setSubalterno(tornaValoreRS(rsPrg,"subalterno"));
				docfaConc.setIdConc(tornaValoreRS(rsPrg,"pk_conc"));
				docfaConc.setProtocollo(tornaValoreRS(rsPrg,"numero_protocollo"));
				docfaConc.setAnnoProtocollo(tornaValoreRS(rsPrg,"anno_protocollo"));
				docfaConc.setDataPotocollo(tornaValoreRS(rsPrg,"data_protocollof"));
				docfaConc.setCodFiscPI(tornaValoreRS(rsPrg,"codice_fiscale"));
				docfaConc.setRagSoc(tornaValoreRS(rsPrg,"cognome_ragsoc"));
				docfaConc.setNome(tornaValoreRS(rsPrg,"nome"));
				docfaConc.setTipoSoggetto(tornaValoreRS(rsPrg,"tipo_soggetto"));
				dettaglioConc.add(docfaConc);
			}
			return dettaglioConc;
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			DbUtils.close(rsPrg);
			DbUtils.close(conn);
		}
		
	}
	
	public Object mDatiCensuari(String identificativo_immobile, String codice)
	throws Exception
	{
		
		Connection conn = null;
		java.sql.ResultSet rs = null;
		ArrayList l = new ArrayList();
		try
		{
			String  protocollo = codice.substring(0,codice.indexOf("|"));
			String fornitura = codice.substring(codice.indexOf("|")+1);
			String sql = "SELECT DISTINCT cen.foglio fog, cen.numero num, cen.subalterno sub, "+
				"	   cen.superficie sup, cen.rendita_euro rendita_eu, "+
				"	   met.superficie supm, met.ambiente ambiente, met.altezza altezza, "+
				"	   cen.identificativo_immobile identificativo_immobile,"+
				"	   to_char(cen.fornitura,'yyyymmdd') fornitura,"+
				"	   cen.protocollo_registrazione protocollo_registrazione"+
				"	   FROM DOCFA_dati_metrici met, DOCFA_dati_censuari cen "+
				"	   WHERE cen.protocollo_registrazione = met.protocollo_registrazione "+
				"	   AND cen.data_registrazione = met.data_registrazione "+
				"	   AND cen.fornitura = met.fornitura "+
				"	   AND cen.identificativo_immobile = met.identificativo_immobile(+) "+
				"	   AND met.protocollo_registrazione  = ? "+
				"	   AND met.FORNITURA =  TO_DATE(?,'YYYYMMDD') "+
				"	   and cen.identificativo_immobile = ? order by ambiente ";
			conn = this.getConnection();
			this.initialize();
			this.setString(1, protocollo);
			this.setString(2, fornitura);
			this.setString(3, identificativo_immobile);
			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			while (rs.next())
			{
				Docfa datiCensuari = new Docfa();
				datiCensuari.setFoglio(tornaValoreRS(rs,"FOG"));
				datiCensuari.setParticella(tornaValoreRS(rs,"NUM"));
				datiCensuari.setSubalterno(tornaValoreRS(rs,"SUB"));	
				datiCensuari.setSuperfice(tornaValoreRS(rs,"sup"));
				datiCensuari.setRendita(tornaValoreRS(rs,"rendita_eu"));
				datiCensuari.setSuperficeMetrici(tornaValoreRS(rs,"supm"));
				datiCensuari.setAmbiente(tornaValoreRS(rs,"ambiente"));
				datiCensuari.setAltezza(tornaValoreRS(rs,"altezza"));
				datiCensuari.setIdentificativoImm(tornaValoreRS(rs,"identificativo_immobile"));
				datiCensuari.setProtocollo(tornaValoreRS(rs,"protocollo_registrazione"));
				datiCensuari.setFornitura(tornaValoreRS(rs,"fornitura"));
				l.add(datiCensuari);
			}
			return l;
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			DbUtils.close(rs);
			if (conn != null)
				if (!conn.isClosed())
					conn.close();
		}
		
	}
		
	public Object mDatiGraffati(String foglio, String particella,String subalterno,String cod_ente)
	throws Exception
	{
		String sql1 = "SELECT id_imm " +
		 "FROM load_cat_uiu_id " +
		 "WHERE codi_fisc_luna = ? " +
		 "and foglio= ? " +
		 "and mappale= ? " +
		 "and sub= ? ";
		
		String sql2 = "SELECT distinct foglio,mappale,sub " +
		 "FROM load_cat_uiu_id " +
		 "WHERE codi_fisc_luna = ? " +
		 "and id_imm = ? ";
		
		Connection conn = null;
		java.sql.ResultSet rs = null;
		ArrayList list = new ArrayList();
		try
		{
			conn = this.getConnection();
			
			if (cod_ente == null)
			{
				Statement st = conn.createStatement();
				ResultSet rsE = st.executeQuery(SQL_COD_ENTE);
				if (rsE.next())
					cod_ente = rsE.getString("uk_belfiore");
				rsE.close();
				st.close();
			}
			
			this.initialize();
			this.setString(1,cod_ente);
			this.setString(2, foglio);
			this.setString(3, particella);
			this.setString(4, subalterno);
			prepareStatement(sql1);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			Integer id_imm = null;
			if (rs.next())
			{
				id_imm = new Integer(tornaValoreRS(rs,"id_imm"));
			}
			rs.close();
			
			if (id_imm != null)
			{
				this.initialize();
				this.setString(1,cod_ente);
				this.setInt(2, id_imm.intValue());
				prepareStatement(sql2);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				
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
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			DbUtils.close(rs);
			DbUtils.close(conn);
		}
		
	}
	public Object mClasseAttesa(String valore,String vani,String zona, String rapporto,String categoria, String classe, HttpServletRequest request)
	throws Exception
	{
		String sql = "select ZONA,CATEGORIA, CLASSE, TARIFFA_EURO from DOCFA_CLASSE where "+
						" tariffa_euro >=	(?/?*0.9/105/?) and  "+
						" tariffa_euro <=  	(?/?*1.1/105/?) and  "+
						" (UPPER(categoria) like 'A%' and UPPER(categoria) <> 'A10' and UPPER(categoria) <> 'A05') and  "+
						" nvl(zona,'-1') = nvl(?,'-1') and (categoria <> ? or classe <> to_number(?))";
		Connection conn = null;
		java.sql.ResultSet rs  = null;
		ArrayList list = new ArrayList();
		try
		{
			conn = this.getConnection();
			this.initialize();
			int i=1;
			this.setDouble(i++, new Double(valore.replaceAll("[,]", ".")).doubleValue());
			this.setDouble(i++, new Double(vani.replaceAll("[,]", ".")).doubleValue());
			this.setDouble(i++, new Double(rapporto.replaceAll("[,]", ".")).doubleValue());
			this.setDouble(i++, new Double(valore.replaceAll("[,]", ".")).doubleValue());
			this.setDouble(i++, new Double(vani.replaceAll("[,]", ".")).doubleValue());			
			this.setDouble(i++, new Double(rapporto.replaceAll("[,]", ".")).doubleValue());
			int ii = i++;
			try {
				int zonaInt = new Integer(zona).intValue();
				this.setInt(ii, zonaInt);
			} catch (Exception e) {
				this.setNull(ii);
			}
			this.setString(i++, categoria);
			this.setString(i++, classe);
			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			while(rs.next())
			{
				Docfa datiClasseAttesa = new Docfa();
				datiClasseAttesa.setZona(tornaValoreRS(rs,"ZONA"));
				datiClasseAttesa.setCategoria(tornaValoreRS(rs,"CATEGORIA"));
				String cls = tornaValoreRS(rs,"CLASSE");
				if(cls != null && cls.trim().length() == 1)
					cls = "0"+cls;
				datiClasseAttesa.setClasse(cls);
				String appoRend = tornaValoreRS(rs,"TARIFFA_EURO");
				if(appoRend==null)
					appoRend="0";				
				datiClasseAttesa.setRendita(round(new Double(appoRend).doubleValue(), 2));	
				list.add(datiClasseAttesa);
			}			
			return list;
		}
		catch (SQLException e)
		{
			if(e.getErrorCode() == 942)
			{
				request.setAttribute("tabellaNonTrovata", "Rendita Categoria/Classe attesa non fornita");
				return list;
			}
			else
				throw e;
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			throw e;
		}		
		finally
		{
			DbUtils.close(rs);
			DbUtils.close(conn);
		}
		
	}	
	
	//public RenditaDocfa mCalcoloRendita(String foglio, String particella,String subalterno,String categoria,String flagNc, String superficie)
	public RenditaDocfa mCalcoloRendita(Docfa docfa,String cod_ente)
	throws Exception
	{
		
		// RICAVO A QUALE TIPOLOGIA IL DOCFA APPARTIENE IN MODO DA
		// POTER ACCEDE A DOCFA_VALORI CON IL GIUSTO FILTRO
		ArrayList<String> g1 = new ArrayList<String>();
		g1.add("A01");
		g1.add("A02");
		g1.add("A07");
		g1.add("A08");
		g1.add("A09");
		ArrayList<String> g2 = new ArrayList<String>();
		g2.add("A03");
		g2.add("A04");
		g2.add("A05");
		g2.add("A06");
		
		int gruppo =0;
		for (String item : g1){
			 
			   if (item.equals(docfa.getCategoria())){
			   
				   gruppo = 1;
			   }
		}
		for (String item : g2){
			 
			   if (item.equals(docfa.getCategoria())){
			   
				   gruppo = 2;
			   }
		}
		
		RenditaDocfa rendita = new RenditaDocfa();
		
		//query catasto 
		String sql1 = "SELECT microzona,  DATA_INIZIO_VAL, DATA_FINE_VAL FROM sitiuiu, siticomu " +
					  "WHERE siticomu.codi_fisc_luna = ? " +
					  "and sitiuiu.cod_nazionale = siticomu.cod_nazionale " +
		 			  "and sitiuiu.foglio = ? " +
		 			  "and sitiuiu.particella = ? " +
		 			  "and sitiuiu.sub = ' '" +
		 			  "and sitiuiu.unimm = ? order by  sitiuiu.DATA_FINE_VAL";

		//query recupero microzona da docfa 
		String sql1_1 = "select new_microzona from DOCFA_FOGLI_MICROZONA mic "+
                      "where lpad(mic.FOGLIO,10,'0') = lpad(?,10,'0') " +  
                      "AND NVL(to_number(mic.zc), -1) = NVL(to_number(?), -1)";

		
		//query tab. docfa_valori (petterini)
		String sql2 = "select val.VAL_MED, TIPOLOGIA_EDILIZIA from DOCFA_valori val " +
					  "where LPAD(val.MICROZONA,10,'0') = LPAD(?,10,'0') " +
					  "and val.STATO = ?";
		
		//query docfa_dati_generali
		String sql3 = "select flag_nc from DOCFA_dati_generali " +
					  "where PROTOCOLLO_REG = ? " +
					  "and  FORNITURA = TO_DATE (?, 'YYYYMMDD')";
		
		Connection conn = null;
		try
		{
			conn = this.getConnection();
			
			java.sql.ResultSet rs = null;
			try
			{
				this.initialize();
				this.setString(1, docfa.getFoglio());
				
				String zonaPar = !docfa.getZona().equalsIgnoreCase("-") ? docfa.getZona() : null;
				this.setString(2, zonaPar); 

				prepareStatement(sql1_1);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				if (rs.next()) {
					rendita.setMicrozona(tornaValoreRS(rs,"new_microzona"));
				}
			} catch (Exception e) {
				log.error(e.getMessage(),e);
			} finally {
				DbUtils.close(rs);
			}
			
			//verifico che foglio sia un valore numerico
			try
			{
				Integer.parseInt(docfa.getFoglio());
			
				//accedo al catasto e recupero microzona
				this.initialize();
				this.setString(1, cod_ente);
				this.setInt(2, Integer.parseInt(docfa.getFoglio()));
				this.setString(3, docfa.getParticella());
				if (docfa.getSubalterno()== null || (docfa.getSubalterno().trim()).equals("") || docfa.getSubalterno().equals("-"))
					this.setInt(4, Integer.parseInt("0"));
				else
					this.setInt(4, Integer.parseInt(docfa.getSubalterno()));
				
				prepareStatement(sql1);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				if (rs.next())
				{
					rendita.setAssenzaCatasto("");//stringa vuota == presente a catasto!
					rendita.setDataValiditaCatasto(tornaValoreRS(rs,"DATA_FINE_VAL","YYYY-MM-DD"));
					Date a  = new Date();
					do
					{
						if(rs.getDate("DATA_INIZIO_VAL").before(a) && rs.getDate("DATA_FINE_VAL").after(a))
							rendita.setDataValiditaCatasto(tornaValoreRS(rs,"DATA_FINE_VAL","YYYY-MM-DD"));
						
					}
				    while (rs.next());

				}
				else
				{
					//memorizzo la non presenza della microzona nello scarico del catasto
					rendita.setAssenzaCatasto("u.i.u. non presente nel DB Catasto!");
				}
			}
			catch (NumberFormatException e)
			{
				log.debug("valore foglio UIU non numerico!!!!");
				
			}	finally {
				DbUtils.close(rs);
			}
			//considero solo le categorie A01-->A09
			String categoria = docfa.getCategoria();
			if (categoria.startsWith("A" ) && !categoria.equals("A10"))
			{
				
				
				
				
					//calcolo rendita DOCFAx100
					Double rendita100 =new Double( new Double(docfa.getRendita().replace(',', '.')).doubleValue() * new Double ("100").doubleValue());
				
					rendita.setRenditaDocfaX100(round(rendita100,2));
					
					
					//calcolo rendita DOCFA aggiornata (+5%)
					Double rendita5 = new Double(rendita100.doubleValue() + ((rendita100.doubleValue())* (new Double(0.05).doubleValue())));
					rendita.setRenditaDocfa5(round(rendita5,2));
					
					//se trovo a catasto microzona
					if (rendita.getMicrozona()!=null && !rendita.getMicrozona().equals(""))
					{
						//accedo a docfa dati generali e recupero FLAG_NC
						this.initialize();
						this.setString(1, docfa.getProtocollo());
						this.setString(2, docfa.getFornitura());
						
						prepareStatement(sql3);
						try {
							
							rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
							if (rs.next())
							{
								rendita.setFlagNC(tornaValoreRS(rs,"flag_nc"));
							}
						}	finally {
							DbUtils.close(rs);
						}
						
						//accedo alla tabella valori (petterini)
						this.initialize();
						this.setString(1, rendita.getMicrozona());
						this.setString(2, Info.htFlagNuovaCostituzione.get(rendita.getFlagNC()));
						
						try {
							
							prepareStatement(sql2);
							rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
							double valMed=0;
							int contaValori=0;
							while (rs.next())
							{
								// per il gruppo 1 prendo solo tipologia edilizia mentre per il due faccio la media delle tipologie
								if (gruppo==1 &&  tornaValoreRS(rs,"TIPOLOGIA_EDILIZIA").equals("ABITAZIONI CIVILI")) {
									valMed=new Double (tornaValoreRS(rs,"val_med","DOUBLE")).doubleValue();
									break;
								}
								if (gruppo==2 &&  tornaValoreRS(rs,"TIPOLOGIA_EDILIZIA").equals("ABITAZIONI TIPO ECONOMICO")) {
									valMed=new Double (tornaValoreRS(rs,"val_med","DOUBLE")).doubleValue();
									break;
								}
								// la media e gia calcolata nel db VEDI tag XX1
								//XX1 valMed= valMed + (new Double(tornaValoreRS(rs,"val_med","DOUBLE")).doubleValue());
								//XX1 contaValori += 1;
							}
							/*XX1 if (contaValori!=0)
								valMed = valMed / contaValori;
							 if (valMed!=0)XX1*/
								rendita.setPetteriniValoreMedio((new String(new Double(valMed).toString())).replace(',', '.'));
						}	finally {
							DbUtils.close(rs);
						}
						
						//rendita media attesa (valore commerciale)
						Double valComm = null;
						try {
							valComm = new Double(new Double(rendita.getPetteriniValoreMedio()).doubleValue()* new Double(docfa.getSuperfice()).doubleValue());
						} catch (Exception e) {
							valComm = new Double("0");
						}
						rendita.setValoreCommerciale(round(valComm,3));
						
						//rapporto val.commerciale/docfaX100
						Double rapVal100 = new Double( valComm.doubleValue()/rendita100.doubleValue());
						String apporapVal100 =  round(rapVal100, 2);
						/*if (apporapVal100.substring(apporapVal100.indexOf(',')).length() > 2)
							apporapVal100 = apporapVal100.substring(0, apporapVal100.indexOf(',')+3);*/

						rendita.setRapportoValorex100(apporapVal100);

						String rapporto = getDocfaRapportoParameter();
						rendita.setRapporto(rapporto);
						
						if (Double.compare(rapVal100.doubleValue(), new Double(rapporto).doubleValue()) > 0 )
							// rapporto > DocfaLogic.RAPPORTO --> evidenziare anomalia
							rendita.setAnomaliaControllox100(true);
						else
							//rapporto <= DocfaLogic.RAPPORTO --> OK
							rendita.setAnomaliaControllox100(false);
						
						
						//rapporto val.commerciale/docfa100+5%
						Double rapVal = new Double( valComm.doubleValue()/rendita5.doubleValue());
						rendita.setRapportoValore(round(rapVal,2));
						
						if (Double.compare(rapVal.doubleValue(), new Double(rapporto).doubleValue()) > 0 )
							// rapporto > DocfaLogic.RAPPORTO --> evidenziare anomalia
							rendita.setAnomaliaControllo(true);
						else
							//rapporto <= DocfaLogic.RAPPORTO --> OK
							rendita.setAnomaliaControllo(false);
					}
				
				
				
			}
			
			return rendita;
		}
		catch (Exception e)
		{
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			DbUtils.close(conn);
		}
		
	}

	private  String getDocfaRapportoParameter() throws Exception {
		return GlobalParameters.getParameter(envUtente , DocfaLogic.DOCFA_RAPPORTO);
	}

	public List cercaIndirizzi(String s)
	{
		List ris = new ArrayList();
		if(s == null)
			return ris;
		s = s.toUpperCase().trim();
		Connection conn = null;
		ResultSet rs = null;
		try
		{
			conn = this.getConnection();
			String sql = "select distinct u.INDIR_TOPONIMO INDIRIZZO from docfa_uiu u where   UPPER (u.indir_toponimo) LIKE '%' || ? || '%'";
			this.initialize();
			this.setString(1, s);
			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			while (rs.next())
			{
				ris.add(rs.getString("INDIRIZZO"));
			}
		}
		catch(Exception e)
		{
			log.error(e.getMessage(),e);
		}
		finally
		{
			if (rs != null)
				try
				{
					rs.close();
				}
				catch (SQLException e)
				{
					log.error(e.getMessage(),e);
				}			
			if (conn != null)
				try
				{
					if (!conn.isClosed())
						conn.close();
				}
				catch (SQLException e)
				{
					log.error(e.getMessage(),e);
				}
			
		}
		return ris;
		
	}


	public Object mediaAttesa(String valore, String consistenza, String rapporto)	
	{
		return round( new Double(valore.replaceAll("[,]", ".")).doubleValue()/(105*new Double(consistenza.replaceAll("[,]", ".")).doubleValue()*new Double(rapporto.replaceAll("[,]", ".")).doubleValue()),2);
		
	}

	public Object getCategoriaClasseInParticella(String chiave)
	{
		String  protocollo = chiave.substring(0,chiave.indexOf("|"));
		String fornitura = chiave.substring(chiave.indexOf("|")+1);
		
		
		ArrayList lista = new ArrayList();
		Connection conn = null;
		ResultSet rs = null;
		try
		{
			conn = this.getConnection();

			Statement st = conn.createStatement();
			ResultSet rsE = st.executeQuery(SQL_COD_ENTE);
			String cod_ente = null;
			if (rsE.next())
				cod_ente = rsE.getString("uk_belfiore");
			rsE.close();
			st.close();
			
			sql =SQL_DATI_SUB_VICINI;
			this.initialize();
			this.setString(1, protocollo);
			this.setString(2, fornitura);
			this.setString(3, cod_ente);
			prepareStatement(sql);
			rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
			while (rs.next())
			{
				Docfa locaList = new Docfa();
				locaList.setFoglio(tornaValoreRS(rs,"foglio"));
				locaList.setParticella(tornaValoreRS(rs,"particella"));
				locaList.setSubalterno(tornaValoreRS(rs,"sub"));
				locaList.setCategoria(tornaValoreRS(rs,"categoria"));
				locaList.setClasse(tornaValoreRS(rs,"classe"));
				lista.add(locaList);
			}
			rs.close();
		}
		catch(Exception e)
		{
			log.error(e.getMessage(),e);
		}
		finally
		{
			if (rs != null)
				try
				{
					rs.close();
				}
				catch (SQLException e)
				{
					log.error(e.getMessage(),e);
				}			
			if (conn != null)
				try
				{
					if (!conn.isClosed())
						conn.close();
				}
				catch (SQLException e)
				{
					log.error(e.getMessage(),e);
				}
			
		}
		return lista;
	}//-------------------------------------------------------------------------
	
	protected String getModalitaCalcoloValoreCommerciale() {
		
		if(modalitaCalcoloValoreCommerciale == null || modalitaCalcoloValoreCommerciale.equals("")){
			ParameterSearchCriteria criteria= new ParameterSearchCriteria();
			criteria.setKey("docfa.modalita.calcolo.valore.commerciale");
			criteria.setComune(this.getEnvUtente().getEnte());
			
			try{
				Context cont = new InitialContext();
			
				ParameterService parameterService= (ParameterService) getEjb("CT_Service", "CT_Config_Manager", "ParameterBaseService");
				
				AmKeyValueExt amKey=parameterService.getAmKeyValueExt(criteria);
				modalitaCalcoloValoreCommerciale= amKey.getValueConf();
			
				log.info("MODALITA CALCOLO VALORE COMMERCIALE: " + modalitaCalcoloValoreCommerciale);
			
			}catch (NamingException e){
				modalitaCalcoloValoreCommerciale="";
			}
		}
		return modalitaCalcoloValoreCommerciale;
	}//-------------------------------------------------------------------------

	public static String getSQL_SELECT_LISTA() {
		return SQL_SELECT_LISTA;
	}
	
	protected String elaboraFiltroMascheraRicerca(int indice, Vector listaPar) throws NumberFormatException, Exception {
		String retVal = super.elaboraFiltroMascheraRicerca(indice, listaPar);
		for (Object obj : listaPar) {
			EscElementoFiltro filtro = (EscElementoFiltro)obj;
			if (filtro.getAttributeName().equals("D_GEN.CAUSALE_NOTA_VAX") && filtro.getValore() != null && filtro.getValore().equals(MARCA_NULL)) {
				retVal = retVal.replace("D_GEN.CAUSALE_NOTA_VAX = ?", "(D_GEN.CAUSALE_NOTA_VAX IS NULL OR D_GEN.CAUSALE_NOTA_VAX = ?)");
			}
		}
		return retVal;
	}
	
}
