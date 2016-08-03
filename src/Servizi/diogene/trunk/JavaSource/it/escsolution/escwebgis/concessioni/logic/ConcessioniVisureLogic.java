package it.escsolution.escwebgis.concessioni.logic;

import it.escsolution.escwebgis.catasto.bean.TipoAtto;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscLogic;
import it.escsolution.escwebgis.concessioni.bean.ConcessioneFinder;
import it.escsolution.escwebgis.concessioni.bean.ConcessioneVisura;
import it.escsolution.escwebgis.concessioni.bean.Costanti;
import it.webred.ct.data.access.basic.concedilizie.ConcVisuraDTO;
import it.webred.ct.data.access.basic.concedilizie.ConcessioniEdilizieService;
import it.webred.ct.data.access.basic.concedilizie.dto.RicercaConcEdilizieDTO;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.dbutils.DbUtils;

public class ConcessioniVisureLogic  extends EscLogic{
	
	private String appoggioDataSource;
	

	public ConcessioniVisureLogic(EnvUtente eu) {
		super(eu);
		appoggioDataSource = eu.getDataSource();
	}//-------------------------------------------------------------------------
	
	
	public static final String FINDER = "FINDER";
	public final static String LISTA = "LISTA_CONC";
	public final static String CONCESSIONE_VISURA = "CONCESSIONE_VISURA@ConcessioniVisureLogic";
	public static final String CONC = ConcessioniVisureLogic.class.getName() + "@CONC";

	private final static String SQL_SELECT_LISTA_PREFISSO = "SELECT * FROM ( select a.*, rownum r from ( ";
	private final static String SQL_SELECT_LISTA = "select inxdoc||'|'||tipo_atto as CHIAVE, v.* " +
												   " from mi_conc_edilizie_visure v where 1 = ? ";
	private final static String SQL_SELECT_LISTA_SUFFISSO = ") a where rownum <= ?) b where r >= ? " ;

	private final static String SQL_SELECT_COUNT_LISTA = "SELECT COUNT(*) conteggio FROM MI_CONC_EDILIZIE_VISURE WHERE (1=? ";
	
	public Hashtable mCaricareLista(Vector listaPar, ConcessioneFinder finder)
		throws Exception
	{
		// carico la lista e la metto in un hashtable
		Hashtable ht = new Hashtable();
		Vector vct = new Vector();
		sql = "";
		String conteggio = "";
		Connection conn = null;
		// faccio la connessione al db
		try
		{
			conn = this.getConnection();
	
			int indice = 1;
			java.sql.ResultSet rs;
	
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
					sql = sql + " AND ID in (" + finder.getKeyStr() + ")";
				}
	
				long limInf, limSup;
				limInf = (finder.getPaginaAttuale() - 1) * RIGHE_PER_PAGINA;
				limSup = finder.getPaginaAttuale() * RIGHE_PER_PAGINA;
	
				if (i == 1 )
				{
					sql = SQL_SELECT_LISTA_PREFISSO + sql ;
					String orderBy =elaboraOrderByMascheraRicerca(listaPar);
					if(orderBy==null || orderBy.equals(""))
						orderBy = " order by NOME_INTESTATARIO ";
					sql+=orderBy;
					sql = sql + ") a where rownum <= " + limSup + ") b where r >= " + limInf ;
				}
				else
					sql = sql + ")";
	
				log.debug("Concessioni Edilizie Visure (QUERY): " + sql);
				prepareStatement(sql);
				rs = executeQuery(conn, this.getClass().getName(), getEnvUtente());
				if (i == 1)
				{
					while (rs.next())
					{
						String id = rs.getString("CHIAVE");
						String inxdoc = rs.getString("INXDOC");
						String tipoAtto = rs.getString("TIPO_ATTO");
						String dataDoc = rs.getString("DATA_DOC");
						String numeroAtto = rs.getString("NUMERO_ATTO");
						String tpv = rs.getString("TPV");
						String nomeVia = rs.getString("NOME_VIA");
						String altravia = rs.getString("ALTRAVIA");
						String civico = rs.getString("CIVICO");
						String civicoSub = rs.getString("CIVICO_SUB");
						String altriciv = rs.getString("ALTRICIV");
						String privata = rs.getString("PRIVATA");
						String riparto = rs.getString("RIPARTO");
						String nomeIntestatario = rs.getString("NOME_INTESTATARIO");
						String tipologia = rs.getString("TIPOLOGIA");
						String destinazione = rs.getString("DESTINAZIONE");
						String vincoloAmbientale = rs.getString("VINCOLO_AMBIENTALE");
						String numProtGen = rs.getString("NUM_PROT_GEN");
						String numProtSett = rs.getString("NUM_PROT_SETT");
						String annoProtGen = rs.getString("ANNO_PROT_GEN");
						String inxvia = rs.getString("INXVIA");
						
						// campi della lista
						ConcessioneVisura con = new ConcessioneVisura();
						
						con.setChiave(id);
						
						if (inxdoc != null && !inxdoc.trim().equalsIgnoreCase("")){
							con.setInxdoc(Long.parseLong(inxdoc.trim()));
						}else
							con.setInxdoc(new Long(-1));
						
						if (tipoAtto != null)
							con.setTipoAtto(tipoAtto);
						else
							con.setTipoAtto("");
						
						
						if (dataDoc != null){
							/*String dataDocumento = "";
							int l = dataDoc.length();
							if (l == 10){
								String[] aryData = dataDoc.split("/");
								dataDocumento = aryData[2] + "/" + aryData[1] + "/" + aryData[0];
							}else if (l == 8){
								dataDocumento = dataDoc.substring(6, 8) + "/" + dataDoc.substring(4, 6) + "/" + dataDoc.substring(0, 4);
							}
							con.setDataDoc(dataDocumento);*/
						}
						con.setDataDoc(dataDoc);
						
						if (numeroAtto != null)
							con.setNumeroAtto(numeroAtto);
						else
							con.setNumeroAtto("");
						
						if (tpv != null)
							con.setTpv(tpv);
						else
							con.setTpv("");
						
						if (nomeVia != null)
							con.setNomeVia(nomeVia);
						else
							con.setNomeVia("");
						
						if (altravia != null)
							con.setAltraVia(altravia);
						else
							con.setAltraVia("");
						
						if (civico != null && !civico.trim().equalsIgnoreCase(""))
							con.setCivico(Long.parseLong(civico));
						else
							con.setCivico(new Long(0));
						
						if (civicoSub != null)
							con.setCivicoSub(civicoSub);
						else
							con.setCivicoSub("");
						
						if (altriciv != null)
							con.setAltriCiv(altriciv);
						else
							con.setAltriCiv("");
						
						if (privata != null && !privata.trim().equalsIgnoreCase(""))
							con.setPrivata(Long.parseLong(privata));
						else
							con.setPrivata(new Long(0));
						
						if (riparto != null)
							con.setRiparto(riparto);
						else
							con.setRiparto("");

						if (nomeIntestatario != null)
							con.setNomeIntestatario(nomeIntestatario);
						else
							con.setNomeIntestatario("");
						
						if (tipologia != null)
							con.setTipologia(tipologia);
						else
							con.setTipologia("");
						
						if (destinazione != null)
							con.setDestinazione(destinazione);
						else
							con.setDestinazione("");
						
						if (vincoloAmbientale != null)
							con.setVincoloAmbientale(vincoloAmbientale);
						else
							con.setVincoloAmbientale("");
						
						if (numProtGen != null)
							con.setNumProtGen(numProtGen);
						else
							con.setNumProtGen("");
						
						if (numProtSett != null)
							con.setNumProtSett(numProtSett);
						else
							con.setNumProtSett("");
						
						if (annoProtGen != null)
							con.setAnnoProtGen(annoProtGen);
						else
							con.setAnnoProtGen("");
						
						if (inxvia != null)
							con.setInxvia(inxvia);
						else
							con.setInxvia("");
						
						vct.add(con);
					}
				}
				else
				{
					if (rs.next())
						conteggio = rs.getString("conteggio");
				}
			}
			ht.put(LISTA, vct);
			finder.setTotaleRecordFiltrati(new Long(conteggio).longValue());
			// pagine totali
			finder.setPagineTotali(1 + new Double(Math.ceil((new Long(conteggio).longValue() - 1) / RIGHE_PER_PAGINA)).longValue());
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
			log.error(e.getMessage(),e);
			throw e;
		}
		finally
		{
			if (conn != null && !conn.isClosed())
				conn.close();
		}
	}//-------------------------------------------------------------------------
	
	public Hashtable mCaricareDettaglio(String chiave) throws Exception
	{
	
		Hashtable ht = new Hashtable();
		
		EnvUtente eu = this.getEnvUtente();
		String enteId = eu.getEnte();
		String userId = eu.getUtente().getUsername();
		String sessionId = eu.getUtente().getSessionId();
		
		try
		{
			ConcessioniEdilizieService servizioConc = (ConcessioniEdilizieService) getEjb("CT_Service", "CT_Service_Data_Access", "ConcessioniEdilizieServiceBean");
	
			RicercaConcEdilizieDTO rc = new RicercaConcEdilizieDTO();
			rc.setEnteId(enteId);
			rc.setUserId(userId);
			rc.setSessionId(sessionId);
			rc.setIdConc(chiave);
			
			ConcVisuraDTO dto = servizioConc.getVisuraById(rc);
			
			ht.put(this.CONCESSIONE_VISURA, dto);
			
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
			log.error("mCaricare Dettaglio " + e.getMessage(),e);
			throw e;
		}
	
	}

	public Vector<TipoAtto> mCaricareListaTipiAtti() throws Exception
	{
	
		Vector<TipoAtto> vct = new Vector<TipoAtto>();
		EnvUtente eu = this.getEnvUtente();
		String enteId = eu.getEnte();
		String userId = eu.getUtente().getUsername();
		String sessionId = eu.getUtente().getSessionId();
		
		try
		{	
			ConcessioniEdilizieService servizioConc = (ConcessioniEdilizieService) getEjb("CT_Service", "CT_Service_Data_Access", "ConcessioniEdilizieServiceBean");
			
			RicercaConcEdilizieDTO rc = new RicercaConcEdilizieDTO();
			rc.setEnteId(enteId);
			rc.setUserId(userId);
			rc.setSessionId(sessionId);
			
			List<String> rs = servizioConc.getVisureTipiAtto(rc);
	
			TipoAtto ta = null;
			
			ta = new TipoAtto();
			ta.setCode("");
			ta.setDescrizione("Tutti");
			vct.add(ta);
			
			for(String code : rs){
				ta = new TipoAtto();
				String desc = (String)Costanti.htTipiAtti.get(code);
				
				if (code != null && !code.trim().equalsIgnoreCase(""))
					ta.setCode(code.trim());
				if (desc != null && !desc.trim().equalsIgnoreCase(""))
					ta.setDescrizione(code + "-" + desc.trim());
				else
					ta.setDescrizione(code);
				vct.add(ta);
			}

			return vct;
		}
		catch (Exception e){
			log.error("mCaricareListaTipiAtti " + e.getMessage(),e);
			throw e;
		}
	}
	
}
