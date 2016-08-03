package it.webred.mui.consolidation;

import it.webred.docfa.model.Docfa;
import it.webred.docfa.model.DocfaDap;
import it.webred.docfa.model.DocfaDapToBeExported;
import it.webred.docfa.model.DocfaUIUTitolareBean;
import it.webred.mui.consolidation.DocfaDapEvaluator.MultiPossessoType;
import it.webred.mui.hibernate.HibernateUtil;
import it.webred.mui.http.MuiApplication;
import it.webred.mui.model.Familiare;
import it.webred.mui.model.Possesore;
import it.webred.docfa.model.Residenza;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import net.skillbill.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DocfaDapManager
{

	public static HashSet<String>				tipologieAbitazione	= null;
	static
	{
		tipologieAbitazione = new HashSet<String>();
		tipologieAbitazione.add("A01");
		tipologieAbitazione.add("A02");
		tipologieAbitazione.add("A03");
		tipologieAbitazione.add("A04");
		tipologieAbitazione.add("A05");
		tipologieAbitazione.add("A06");
		tipologieAbitazione.add("A07");
		tipologieAbitazione.add("A08");
		tipologieAbitazione.add("A09");
	}

	private static Map<Long, DocfaDapManager>	_dapManagers		= new HashMap<Long, DocfaDapManager>();

	private static boolean						_stop				= false;

	private static int							_running			= 0;

	private int									_rowCount			= 0;

	public int getRowCount()
	{
		return _rowCount;
	}

	public static Map<Long, DocfaDapManager> getRunningDapManagers()
	{
		return _dapManagers;
	}

	public DocfaDapManager()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public static DocfaDapManager getRunningDapManagers(String iidFornitura)
	{
		return getRunningDapManagers(Long.valueOf(iidFornitura));
	}

	public static DocfaDapManager getRunningDapManagers(Long iidFornitura)
	{
		return getRunningDapManagers().get(iidFornitura);
	}

	public static void stop()
	{
		_stop = true;
		while (_running > 0)
		{
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		_stop = false;
	}

	public void manageDap(String iidFornitura)
	{
		_running++;
		getRunningDapManagers().put(Long.valueOf(iidFornitura), this);
		MuiApplication.getMuiApplication().getServletContext().setAttribute("docfadapManagers", getRunningDapManagers());
		Session session = HibernateUtil.currentSession();

		try
		{
			Query query = null;

			query = HibernateUtil.currentSession().createQuery("select todap  from  DocfaDapToBeExported as todap where todap.iidFornitura = :iidFornitura");
			query.setString("iidFornitura", iidFornitura);
			Iterator todapIterator = query.iterate();
			DocfaDapToBeExported todap = null;
			while (todapIterator.hasNext())
			{
				todap = (DocfaDapToBeExported) todapIterator.next();
				_rowCount++;
				try
				{

					String fornitura = todap.getIidFornitura(); // yyyymmdd
					String protocollo = todap.getIidProtocolloReg();
					String formFornitura = fornitura.substring(6, 8) + "/" + fornitura.substring(4, 6) + "/" + fornitura.substring(0, 4);

					// recupero tutti i dati che mi servono dal docfa corrente
					Hashtable ht = this.elaboraDatiDocfa(protocollo, formFornitura);

					Docfa docfa = (Docfa) ht.get("docfa");
					ArrayList listaDocfaUiu = (ArrayList) ht.get("listaDocfaUiu");
					// ArrayList listaDocfaDatiCensuari = (ArrayList)ht.get("listaDocfaDatiCensuari");
					// //////////////////////////////////////////////////////////////
					Iterator uiuIter = listaDocfaUiu.iterator();

					while (uiuIter.hasNext())
					{
						Docfa uiu = (Docfa) uiuIter.next();
						Iterator titoIter = uiu.getElencoTitolari().iterator();
						while (titoIter.hasNext())
						{
							DocfaUIUTitolareBean tito = (DocfaUIUTitolareBean) titoIter.next();

							DocfaDap dap = new DocfaDap();
							dap.setIidFornitura(fornitura);
							dap.setIidProtocolloReg(protocollo);
							dap.setIdSoggetto(tito.getCodiceFiscale() != null ? tito.getCodiceFiscale() : tito.getPartitaIva());
							dap.setTipoSoggetto(tito.getFlagPersona());
							dap.setFoglio(uiu.getFoglio());
							dap.setParticella(uiu.getParticella());
							dap.setSubalterno(uiu.getSubalterno());



							
							if (dap.getIdSoggetto().equals("") || dap.getIdSoggetto().equals("-"))
							{
								dap.setFlagSkipped(true);
							}
							else
							{
								if(!"P".equalsIgnoreCase(dap.getTipoSoggetto()) || uiu.getTipo() == null)
								{
									dap.setFlagSkipped(true);
								}
								else if ((uiu.getTipo().equalsIgnoreCase("V") || uiu.getTipo().equalsIgnoreCase("C")) && !DocfaDapManager.isTipologiaImmobileAbitativo(uiu.getCodCategoriaDocfaProp()))
								{
									dap.setFlagSkipped(true);
								}
								else if ((uiu.getTipo().equalsIgnoreCase("S") ) && !DocfaDapManager.isTipologiaImmobileAbitativo(uiu.getCategoria()))
								{
									dap.setFlagSkipped(true);
								}																	
								else
								{
									dap.setFlagSkipped(false);
									DocfaDetrazioneManager dm = new DocfaDetrazioneManager(dap.getIdSoggetto(), docfa.getDataRegistrazione(), dap.getIidProtocolloReg(), formFornitura);
									boolean foundDetrazione = dm.evalDetrazione();
									dap.setFlagDapDiritto(foundDetrazione);
									Residenza res = dm.getResidenzaConDetrazione();
									if (res != null)
									{
										String dataRForm = docfa.getDataRegistrazione().substring(6, 10) + docfa.getDataRegistrazione().substring(3, 5) + docfa.getDataRegistrazione().substring(0, 2);
										Date dataRif = new Date(Long.parseLong(dataRForm));
										dap.setDapData(res.getDataDa() != null && res.getDataDaDate().after(dataRif) ? res.getDataDa() : docfa.getDataRegistrazione());
									}
									else
									{
										dap.setDapData(docfa.getDataRegistrazione());
									}

									DocfaDapEvaluator dapE = DocfaDapEvaluatorFactory.getInstance().getDapEvaluator();
									List<Familiare> familiari = dapE.getFamiliares(dap);
									Logger.log().info(this.getClass().getName(), "trovati " + familiari.size() + " familiari per " + dap.getIdSoggetto());

									List<Possesore> possesori = dapE.getPossesores(dap);
									Logger.log().info(this.getClass().getName(), "trovati " + possesori.size() + " comproprietari per " + dap.getIdSoggetto());

									int famContitolari = 0;
									BigDecimal totalePossesso = new BigDecimal(0);
									Familiare self = new Familiare();
									self.setCodiceFiscale(dap.getIdSoggetto());

									for (Iterator<Possesore> iter = possesori.iterator(); iter.hasNext();)
									{
										Possesore possesore = iter.next();
										totalePossesso = totalePossesso.add(possesore.getPercentualePossesso());
										Logger.log().info(this.getClass().getName(), "controllo per possesore " + possesore + " perc=" + possesore.getPercentualePossesso());
										if (!possesore.equals(self))
										{
											for (Iterator<Familiare> iterFam = familiari.iterator(); iterFam.hasNext();)
											{
												Familiare familiare = iterFam.next();
												Logger.log().info(this.getClass().getName(), "controllo per familiare " + familiare + " se uguale a " + possesore);
												if (possesore.equals(familiare))
												{
													famContitolari++;
													Logger.log().info(this.getClass().getName(), "trovato un familiare contitolare, conto=" + famContitolari);
												}
											}
										}
									}
									dap.setDapNumeroSoggetti(famContitolari);
									MultiPossessoType mps = dapE.getPossedutoImmobiles(dap);
									if (totalePossesso.compareTo(new BigDecimal(100)) == 0)
									{
										// dap.setFlagRegoleDapPrecentualePossessoTotaleErrata(true);
										dap.setEsitoDap("005");
									}
									else if (mps == MultiPossessoType.SEVERAL)
									{
										// dap.setFlagRegoleDapSoggettoPossessorePiuImmobili(true);
										dap.setEsitoDap("007");
									}
									else if (mps == MultiPossessoType.REPEATED)
									{
										// dap.setFlagRegoleDapSoggettoPossessorePiuImmobiliStessoIndirizzo(true);
										dap.setEsitoDap("011");
									}
									else
										dap.setEsitoDap("000");
								}
							}

							session.saveOrUpdate(dap);
						}
					}

					// //////////////////////////////////////////////////////////////
					// session.saveOrUpdate(dap);

					if (_stop)
					{
						break;
					}
				}
				catch (Throwable e)
				{
					Logger.log().error(this.getClass().getName(), "errore durante la valutazione DAP per la fornitura=" + iidFornitura, e);

				}
			}

		}
		finally
		{
			_running--;
			try
			{
				session.flush();
			}
			catch (HibernateException e)
			{
				Logger.log().error(this.getClass().getName(), "errore durante la generazione della comunicazione per la fornitura=" + iidFornitura, e);
			}
			getRunningDapManagers().remove(Long.valueOf(iidFornitura));
		}
	}

	public static boolean isTipologiaImmobileAbitativo(String tipologiaImmobile)
	{
		return (tipologiaImmobile != null ? tipologieAbitazione.contains(tipologiaImmobile.toUpperCase()) : false);

	}

	private Hashtable elaboraDatiDocfa(String protocollo, String dataFornitura)
		throws Exception
	{

		Hashtable ht = new Hashtable();
		// faccio la connessione al db
		Connection conn = null;
		PreparedStatement pstm = null;
		PreparedStatement pstcat = null;
		PreparedStatement pstsUIUTito = null;
		PreparedStatement pstsUIUTitoRes = null;
		PreparedStatement pstsUIUTitoResTri = null;
		PreparedStatement pstsUIUTitoGTri = null;
		PreparedStatement pstsCatComOgg = null;
		try
		{
			String fornituraStr = dataFornitura;
			String fornitura = fornituraStr.substring(6, 10) + fornituraStr.substring(3, 5) + fornituraStr.substring(0, 2);

			Docfa docfa = new Docfa();
			Context cont = new InitialContext();
			Context datasourceContext = (Context) cont.lookup("java:comp/env");
			DataSource theDataSource = (DataSource) datasourceContext.lookup("jdbc/mui");
			conn = theDataSource.getConnection();

			// DOCFA_UIU
			String sql = "SELECT DISTINCT TIPO_OPERAZIONE    AS TIPO,  " +
			"	   FOGLIO             AS FOGLIO," + 
			"	   NUMERO             AS NUMERO," +
			"	   SUBALTERNO         AS SUB," + 
			"	   TRIM(INDIR_TOPONIMO) || ' ' || INDIR_NCIV_UNO     AS INDIRIZZO, " +
			"	   PROP_CATEGORIA,PROP_CLASSE,PROP_RENDITA_EURO_CATA,PROP_SUPERFICIE_CATA,PROP_ZONA_CENSUARIA " +
			"	   FROM DOCFA_UIU U" + 
			"	   WHERE" +
			"	   U.PROTOCOLLO_REG = ?" + 
			"	   AND U.FORNITURA =  TO_DATE(?,'YYYYMMDD') ";
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, protocollo);
			pstm.setString(2, fornitura);
			ResultSet rs = pstm.executeQuery();
			ArrayList listaDocfaUiu = new ArrayList();
			while (rs.next())
			{
				Docfa locaList = new Docfa();
				locaList.setTipo(tornaValoreRS(rs, "TIPO"));
				locaList.setFoglio(tornaValoreRS(rs, "FOGLIO"));
				locaList.setParticella(tornaValoreRS(rs, "NUMERO"));
				locaList.setSubalterno(tornaValoreRS(rs, "SUB"));
				locaList.setIndirizzoDichiarante(tornaValoreRS(rs, "INDIRIZZO"));
				locaList.setCodCategoriaDocfaProp(tornaValoreRS(rs, "PROP_CATEGORIA"));
				locaList.setClasseDocfaProp(tornaValoreRS(rs, "PROP_CLASSE"));
				locaList.setRenditaDocfaProp(tornaValoreRS(rs, "PROP_RENDITA_EURO_CATA"));
				locaList.setSuperficieDocfaProp(tornaValoreRS(rs, "PROP_SUPERFICIE_CATA"));
				locaList.setZonaDocfaProp(tornaValoreRS(rs, "PROP_ZONA_CENSUARIA"));

				//DAN: cerco la categoria per il tipo S
				try
				{
					if(locaList.getTipo().equals("S"))
					{
						String sqlCatComOgg = "select distinct categoria from DOCFA_COMUN_OGGETTO d where  " +
							" d.FOGLIO = ? " +
							" and d.PARTICELLA = ? " +
							" and d.SUBALTERNO = ? " +
							" and d.IID_PROTOCOLLO_REG = ? " +
							" and d.IID_FORNITURA = ?"; 
						pstsCatComOgg = conn.prepareStatement(sqlCatComOgg);
						pstsCatComOgg.setInt(1, Integer.parseInt(locaList.getFoglio()));
						pstsCatComOgg.setString(2, locaList.getParticella());
						// controllo se il SUB è rappresentato da spazi vuoti (DOCFA_UIU) --> lo imposto a 0 (sitiuiu non a unimm a vuoto ma solo a 0!)
						// sennò esplode la query!!!
						if (locaList.getSubalterno().trim().equals(""))
							locaList.setSubalterno("0");
						pstsCatComOgg.setInt(3, Integer.parseInt(locaList.getSubalterno()));
						pstsCatComOgg.setString(4, protocollo);
						pstsCatComOgg.setString(5, fornituraStr);				
						ResultSet rscate = pstsCatComOgg.executeQuery();
						if(rscate.next())
							locaList.setCategoria(tornaValoreRS(rscate, "CATEGORIA"));
						rscate.close();
						pstsCatComOgg.close();
					}
				}				
				catch (Exception e)
				{
					Logger.log().error(this.getClass().getName(),"errore nella ricerca categoria ", e);

				}
				// cerco indirizzo a catasto per UIU cessate o variate
				// solo per docfa con uiuFoglio numerici sennò esplode!!!!
				String SQL_INDIRIZZO_PART_CAT = "SELECT DISTINCT s.prefisso || ' ' || s.nome AS nome, s.numero as codice_via, " +
				" c.civico " + 
				"FROM sitiuiu p, siticivi_uiu cu, siticivi c, sitidstr s " + 
				"WHERE p.foglio = ? " + "AND p.particella = ? " + 
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
						pstcat = conn.prepareStatement(SQL_INDIRIZZO_PART_CAT);
						pstcat.setInt(1, Integer.parseInt(locaList.getFoglio()));
						pstcat.setString(2, locaList.getParticella());

						// controllo se il SUB è rappresentato da spazi vuoti (DOCFA_UIU) --> lo imposto a 0 (sitiuiu non a unimm a vuoto ma solo a 0!)
						// sennò esplode la query!!!
						if (locaList.getSubalterno().trim().equals(""))
							locaList.setSubalterno("0");

						pstcat.setInt(3, Integer.parseInt(locaList.getSubalterno()));

						ResultSet rscat = pstcat.executeQuery();

						while (rscat.next())
						{
							String appo = tornaValoreRS(rscat, "NOME") + " " + tornaValoreRS(rscat, "CIVICO");
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

				}
				catch (Exception e)
				{
					Logger.log().error(this.getClass().getName(),"errore nella ricerca indirizzo  ", e);

				}
				locaList.setIndPart(indCat);

				// info titolari FPS
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

				if (locaList.getTipo().equals("S") || locaList.getTipo().equals("V"))
				{
					pstsUIUTito = conn.prepareStatement(sqlUIUTitoS);
				}
				if (locaList.getTipo().equals("C"))
				{
					pstsUIUTito = conn.prepareStatement(sqlUIUTitoC);
				}

				pstsUIUTito.setString(1, locaList.getFoglio());
				pstsUIUTito.setString(2, locaList.getParticella());
				pstsUIUTito.setString(3, locaList.getSubalterno());
				pstsUIUTito.setString(4, fornituraStr);
				pstsUIUTito.setString(5, fornituraStr);
				pstsUIUTito.setString(6, fornituraStr);
				if (locaList.getTipo().equals("S") || locaList.getTipo().equals("V"))
				{
					pstsUIUTito.setString(7, fornituraStr);
				}

				ResultSet rsUIUTito = pstsUIUTito.executeQuery();

				ArrayList elencoTitolari = new ArrayList();
				while (rsUIUTito.next())
				{
					// memorizzo le info dei Titolari
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

					if (tito.getCodiceFiscale() != null && tito.getCodiceFiscale().length() == 16)
					{
						// recupero residenza del titolare da Anagrafe
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
						pstsUIUTitoRes = conn.prepareStatement(sqlUIUTitoResAna);
						pstsUIUTitoRes.setString(1, tito.getCodiceFiscale());

						ResultSet rsUIUTitoRes = pstsUIUTitoRes.executeQuery();
						if (rsUIUTitoRes.next())
						{
							tito.setIndirizzoResidenza(rsUIUTitoRes.getString("descr_via"));
							tito.setCivicoResidenza(rsUIUTitoRes.getString("NUMERO_CIV") + " " + rsUIUTitoRes.getString("ESP_CIV"));

							tito.setComuneResidenza(MuiApplication.descComune);
							tito.setFlagProvenienzaInfo("A"); // dati trovati in Anagrafe
						}
						else
						{
							// cerco nei tributi
							String sqlUIUTitoResTributi = "select cognome,nome,sesso,data_nascita,descr_comune_nascita," + 
							"descr_indirizzo,comune_residenza " + 
							"from tri_contribuenti " + 
							"where codice_fiscale = ?";
							pstsUIUTitoResTri = conn.prepareStatement(sqlUIUTitoResTributi);
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
						// cerco dati giuridici in tributi
						String sqlUIUTitoGTributi = "select descr_indirizzo,comune_residenza " + 
						"from tri_contribuenti " + 
						"where partita_iva = ? " + 
						"order by provenienza desc";
						pstsUIUTitoGTri = conn.prepareStatement(sqlUIUTitoGTributi);
						if (tito.getCodiceFiscale() != null && tito.getCodiceFiscale().length() == 11)
							pstsUIUTitoGTri.setString(1, tito.getCodiceFiscale());
						else if (tito.getPartitaIva() != null && tito.getPartitaIva().length() == 11)
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

			// DOCFA_DATI_CENSUARI
			sql = "SELECT DISTINCT CEN.FOGLIO      FOG," + 
			"	   CEN.NUMERO      NUM," + 
			"	   CEN.SUBALTERNO  SUB," + 
			"	   CEN.ZONA        ZONA," + 
			"	   CEN.CLASSE      CLS," + 
			"	   CEN.CATEGORIA   CAT," + 
			"	   CEN.CONSISTENZA CONS," + 
			"	   CEN.SUPERFICIE  SUP," + 
			"	   CEN.RENDITA_EURO RENDITA_EU," + 
			"	   CEN.DATA_REGISTRAZIONE DATA_REGISTRAZIONE," + 
			"	   CEN.identificativo_immobile identificativo_immobile, " + 
			"	   NVL(cen.presenza_graffati,'-') presenza_graffati " + 
			"	   FROM  DOCFA_DATI_CENSUARI	CEN,DOCFA_DATI_METRICI	MET " + 
			"	   WHERE CEN.PROTOCOLLO_REGISTRAZIONE = ? " + 
			"		 AND CEN.FORNITURA =  TO_DATE(?,'YYYYMMDD')" +
					// nuova da millucci
			"  AND MET.identificativo_immobile(+) = CEN.identificativo_immobile";

			pstm = conn.prepareStatement(sql);
			pstm.setString(1, protocollo);
			pstm.setString(2, fornitura);
			rs = pstm.executeQuery();
			ArrayList listaDocfaDatiCensuari = new ArrayList();
			while (rs.next())
			{
				if (docfa.getDataRegistrazione() == null)
					docfa.setDataRegistrazione(tornaValoreRS(rs, "DATA_REGISTRAZIONE", "YYYYMMDD"));
				Docfa locaList = new Docfa();
				locaList.setFoglio(tornaValoreRS(rs, "FOG"));
				locaList.setParticella(tornaValoreRS(rs, "NUM"));
				locaList.setSubalterno(tornaValoreRS(rs, "SUB"));
				locaList.setZona(tornaValoreRS(rs, "ZONA"));
				locaList.setClasse(tornaValoreRS(rs, "CLS"));
				locaList.setCategoria(tornaValoreRS(rs, "CAT"));
				locaList.setConsistenza(tornaValoreRS(rs, "CONS"));
				locaList.setSuperfice(tornaValoreRS(rs, "SUP"));
				locaList.setRendita(tornaValoreRS(rs, "RENDITA_EU"));
				locaList.setIdentificativoImm(tornaValoreRS(rs, "identificativo_immobile"));
				locaList.setPresenzaGraffati(tornaValoreRS(rs, "presenza_graffati"));
				locaList.setProtocollo(protocollo);
				locaList.setFornitura(fornitura);

				listaDocfaDatiCensuari.add(locaList);
			}
			rs.close();
			pstm.close();

			conn.close();

			ht.put("docfa", docfa);
			ht.put("listaDocfaUiu", listaDocfaUiu);
			ht.put("listaDocfaDatiCensuari", listaDocfaDatiCensuari);

		}
		catch (Exception e)
		{
			try
			{
				if (pstm != null)
					pstm.close();
			}
			catch (Exception www)
			{
			}
			try
			{
				if (pstcat != null)
					pstcat.close();
			}
			catch (Exception www)
			{
			}
			try
			{
				if (pstsUIUTito != null)
					pstsUIUTito.close();
			}
			catch (Exception www)
			{
			}
			try
			{
				if (pstsUIUTitoRes != null)
					pstsUIUTitoRes.close();
			}
			catch (Exception www)
			{
			}
			try
			{
				if (pstsUIUTitoResTri != null)
					pstsUIUTitoResTri.close();
			}
			catch (Exception www)
			{
			}
			try
			{
				if (pstsUIUTitoGTri != null)
					pstsUIUTitoGTri.close();
			}
			catch (Exception www)
			{
			}
			try
			{
				if (pstsCatComOgg != null)
					pstsCatComOgg.close();
			}
			catch (Exception www)
			{
			}			
			throw e;
		}
		finally
		{

			try
			{
				if (conn != null && !conn.isClosed())
					conn.close();
			}
			catch (Exception www)
			{
			}

		}

		return ht;
	}

	private static String tornaValoreRS(ResultSet rs, String colonna)
		throws Exception
	{
		return tornaValoreRS(rs, colonna, null);
	}

	private static String tornaValoreRS(ResultSet rs, String colonna, String tipo)
		throws Exception
	{
		try
		{
			String s = null;
			s = rs.getString(colonna);

			if (s == null && tipo != null)
				if (tipo.equals("VUOTO"))
					s = "";
			if (s == null)
				return s = "";

			if (tipo != null)
				if (tipo.equals("NUM"))
				{
					s = new Integer(s).toString();
				}
				else if (tipo.equals("DOUBLE"))
				{
					s = new Double(s).toString();
				}
				else if (tipo.equals("EURO"))
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
					s = s.substring(8, 10) + "/" + s.substring(5, 7) + "/" + s.substring(0, 4);
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
			return "";
		}
	}

}
