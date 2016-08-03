package it.escsolution.escwebgis.bolli.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Vector;

import it.escsolution.escwebgis.bolli.bean.BolliVeicoliFinder;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscLogic;
import it.webred.ct.data.model.bolliVeicoli.BolloVeicolo;

public class BolliVeicoliLogic extends EscLogic {
	
	public final static String LISTA_BOLLI_VEICOLI = "LISTA_BOLLI_VEICOLI@BolliVeicoliLogic";
	public final static String FINDER = "FINDER137";
	public final static String BOLLI_VEICOLI = "BOLLI_VEICOLI@BolliVeicoliLogic";	
	
	private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
	private static final SimpleDateFormat SDF_ANNO = new SimpleDateFormat("yyyy");
	
	private String appoggioDataSource = "";
	
	private static final DecimalFormat DF = new DecimalFormat();
	static {
		DF.setGroupingUsed(true);
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator(',');
		dfs.setGroupingSeparator('.');
		DF.setDecimalFormatSymbols(dfs);
	}
	
	private final static String SQL_SELECT_LISTA = "select * from (" +
	"select ROWNUM as N, Q.* from (" +
	"select * " +
	"from BOLLI_VEICOLI T where 1=?";
	
	private final static String SQL_SELECT_COUNT_LISTA = "select count(*) as conteggio FROM ( select * from BOLLI_VEICOLI T where 1=? ";
	
	private final static String SQL_SELECT_DETTAGLIO = "SELECT * FROM BOLLI_VEICOLI WHERE ID = ?";

	public BolliVeicoliLogic(EnvUtente eu) {
		super(eu);
		appoggioDataSource=eu.getDataSource();
	}//-------------------------------------------------------------------------
	
	private BolloVeicolo getOggettoBolloVeicolo(ResultSet rs, Connection conn) throws Exception{

		BolloVeicolo bv = new BolloVeicolo();
		
		bv.setAlimentazione( rs.getString("ALIMENTAZIONE")!=null?rs.getString("ALIMENTAZIONE"):"" );
		bv.setCap( rs.getString("CAP")!=null?rs.getString("CAP"):"" );
		bv.setCilindrata(  rs.getBigDecimal("CILINDRATA") );
		bv.setCodiceCarrozzeria( rs.getString("CODICE_CARROZZERIA")!=null?rs.getString("CODICE_CARROZZERIA"):"" );
		bv.setCodiceFiscalePiva( rs.getString("CODICEFISCALE_PIVA")!=null?rs.getString("CODICEFISCALE_PIVA"):"" );
		bv.setCodiceSesso( rs.getString("CODICESESSO")!=null?rs.getString("CODICESESSO"):"" );
		bv.setCodiceTelaio( rs.getString("CODICE_TELAIO")!=null?rs.getString("CODICE_TELAIO"):"" );
		bv.setCodSiglaEuro( rs.getString("COD_SIGLA_EURO")!=null?rs.getString("COD_SIGLA_EURO"):"" );
		bv.setCognome( rs.getString("COGNOME")!=null?rs.getString("COGNOME"):"" );
		bv.setComuneIstatC( rs.getString("COMUNE_ISTATC")!=null?rs.getString("COMUNE_ISTATC"):"" );
		bv.setComuneNascita( rs.getString("COMUNENASCITA")!=null?rs.getString("COMUNENASCITA"):"" );
		bv.setComuneResidenza( rs.getString("COMUNERESIDENZA")!=null?rs.getString("COMUNERESIDENZA"):"" );
		bv.setDataFineEsenzione( rs.getDate("DATAFINEESENZIONE")  );
		bv.setDataFineProprieta( rs.getDate("DATAFINEPROPRIETA")  );
		bv.setDataInizioEsenzione( rs.getDate("DATAINIZIOESENZIONE")  );
		bv.setDataInizioProprieta( rs.getDate("DATAINIZIOPROPRIETA")  );
		bv.setDataInserimento(rs.getDate("DATA_INSERIMENTO")  );
		bv.setDataNascita( rs.getDate("DATANASCITA")  );
		bv.setDestinazione( rs.getString("DESTINAZIONE")!=null?rs.getString("DESTINAZIONE"):"" );
		bv.setDtPrimaImmatricolazione( rs.getDate("DT_PRIMA_IMMATRICOLAZIONE")  );
		bv.setEmissioniCo2(  rs.getBigDecimal("EMISSIONI_CO2") );
		bv.setEnte( rs.getString("ENTE")!=null?rs.getString("ENTE"):"" );
		bv.setEsenzione( rs.getString("ESENZIONE")!=null?rs.getString("ESENZIONE"):"" );
		bv.setFlagAnnMassaRimorc( rs.getString("FLAG_ANN_MASSA_RIMORC")!=null?rs.getString("FLAG_ANN_MASSA_RIMORC"):"" );
		bv.setId(  rs.getBigDecimal("ID").longValue() );
		bv.setIndirizzo( rs.getString("INDIRIZZO")!=null?rs.getString("INDIRIZZO"):"" );
		bv.setKw(  rs.getBigDecimal("KW") );
		bv.setMassaRimorchiabile(  rs.getBigDecimal("MASSA_RIMORCHIABILE") );
		bv.setNome( rs.getString("NOME")!=null?rs.getString("NOME"):"" );
		bv.setNumeroAssi(  rs.getBigDecimal("NUMERO_ASSI") );
		bv.setNumeroCivico(  rs.getString("NUMEROCIVICO")!=null?rs.getString("NUMEROCIVICO"):"" );
		bv.setNumeroPosti(  rs.getBigDecimal("NUMERO_POSTI") );
		bv.setPesoComplessivo(  rs.getBigDecimal("PESO_COMPLESSIVO") );
		bv.setPortata(  rs.getBigDecimal("PORTATA") );
		bv.setPotenzaFiscale(  rs.getBigDecimal("POTENZA_FISCALE") );
		bv.setProvinciaIstatP( rs.getString("PROVINCIA_ISTATP")!=null?rs.getString("PROVINCIA_ISTATP"):"" );
		bv.setProvinciaNascita( rs.getString("PROVINCIANASCITA")!=null?rs.getString("PROVINCIANASCITA"):"" );
		bv.setProvinciaResidenza( rs.getString("PROVINCIARESIDENZA")!=null?rs.getString("PROVINCIARESIDENZA"):"" );
		bv.setRagioneSociale( rs.getString("RAGIONESOCIALE")!=null?rs.getString("RAGIONESOCIALE"):"" );
		bv.setRegioneResidenza( rs.getString("REGIONERESIDENZA")!=null?rs.getString("REGIONERESIDENZA"):"" );
		bv.setSospensionePneumatica( rs.getString("SOSPENSIONE_PNEUMATICA")!=null?rs.getString("SOSPENSIONE_PNEUMATICA"):"" );
		bv.setStatoEsenzione( rs.getString("STATOESENZIONE")!=null?rs.getString("STATOESENZIONE"):"" );
		bv.setTarga( rs.getString("TARGA")!=null?rs.getString("TARGA"):"" );
		bv.setTipoAlimentazioneImpianto( rs.getString("TIPO_ALIMENTAZIONE_IMPIANTO")!=null?rs.getString("TIPO_ALIMENTAZIONE_IMPIANTO"):"" );
		bv.setTipoHandicap( rs.getString("TIPOHANDICAP")!=null?rs.getString("TIPOHANDICAP"):"" );
		bv.setTipoSoggetto( rs.getString("TIPOSOGGETTO")!=null?rs.getString("TIPOSOGGETTO"):"" );
		bv.setUso( rs.getString("USO")!=null?rs.getString("USO"):"" );
		
		return bv;
	}//-------------------------------------------------------------------------
	
	public Hashtable mCaricareLista(Vector listaPar, BolliVeicoliFinder finder) throws Exception {

		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
	    sql = "";
		String conteggio = "";
		long conteggione = 0;
		Connection conn = null;
		
		try {
			conn = this.getConnection();
			int indice = 1;
			java.sql.ResultSet rs = null;
			
			for (int i=0;i<=1;i++){
				//il primo ciclo faccio la count
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
					sql = sql + " and BOLLI_VEICOLI.ID in (" +finder.getKeyStr() + ")" ;
				}

				long limInf, limSup;
				limInf = (finder.getPaginaAttuale()-1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;

				if (i == 1) sql = sql + " order by T.COGNOME, T.NOME, T.RAGIONESOCIALE) Q) where N > " + limInf + " and N <=" + limSup;
				else sql += ")";
				
				log.debug("SQL LISTA " + sql);
				
				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				if (i ==1) {
					while (rs.next()){
						BolloVeicolo bv = getOggettoBolloVeicolo(rs, conn);
						//tes.setChiave( "" + tes.getId() );
						vct.add(bv);					
					}
				}else{
					if (rs.next()){
						conteggio = rs.getString("conteggio");
					}
				}
			}
			
			ht.put(LISTA_BOLLI_VEICOLI, vct);
			finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			// pagine totali
			finder.setPagineTotali(1+new Double(Math.ceil((new Long(conteggio).longValue()-1) / RIGHE_PER_PAGINA)).longValue());
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
		catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}//-------------------------------------------------------------------------
	
	public Hashtable mCaricareDettaglio(String chiave) throws Exception {
		
		Hashtable ht = new Hashtable();

		Connection conn = null;
		BolloVeicolo bv = new BolloVeicolo();
		
		try {
			if(chiave != null && !chiave.equals("")) {
				
				conn = this.getConnection();
				
				this.initialize();
				sql = SQL_SELECT_DETTAGLIO;
				
				this.setString(1, chiave);

				log.debug(sql);
				
				prepareStatement(sql);
				java.sql.ResultSet rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());

				if (rs.next()) {
					bv = getOggettoBolloVeicolo(rs, conn);
				}
				
				ht.put(BOLLI_VEICOLI, bv);
			}
			
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
		finally {
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}//-------------------------------------------------------------------------
	
	protected String elaboraFiltroMascheraRicerca(int indice, Vector listaPar) throws NumberFormatException, Exception{
		String sql = "";
		Vector listaParTes = new Vector();
		Vector listaParDet = new Vector();
		
		for (int i = 0; i < listaPar.size(); i++) {
			EscElementoFiltro el = (EscElementoFiltro)listaPar.get(i);
			if (isParametroDettaglio(el)) {
				listaParDet.add(el);
			} else {
				listaParTes.add(el);
			}
		}
		
		sql = super.elaboraFiltroMascheraRicerca(indice, listaParTes);
		indice = getCurrentParameterIndex();
		
		boolean trovatoParDet = false;
		for (int i = 0; i < listaParDet.size(); i++) {
			EscElementoFiltro el = (EscElementoFiltro)listaParDet.get(i);
			if (!"".equals(el.getValore())) {
				trovatoParDet = true;
				break;
			}			
		}
		
		if (trovatoParDet) {			
			String sqlAdd = super.elaboraFiltroMascheraRicercaParziale(indice, listaParDet);
			sql += sqlAdd;
		}
		
		return sql;
	}//-------------------------------------------------------------------------
	
	private boolean isParametroDettaglio(EscElementoFiltro el) {
		String attName = el.getAttributeName();
		
		return "VIA".equalsIgnoreCase(attName) ||
			   "CIVICO".equalsIgnoreCase(attName) ||
			   "DESC_CLS".equalsIgnoreCase(attName) ||
			   "DT_INIZIO_D".equalsIgnoreCase(attName) ||
			   "DT_FINE_D".equalsIgnoreCase(attName);
	}//-------------------------------------------------------------------------

	

}
