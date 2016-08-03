package it.webred.gitout;


import it.webred.cet.permission.CeTUser;
import it.webred.cet.permission.GestionePermessi;
import it.webred.ct.data.access.aggregator.elaborazioni.ElaborazioniService;
import it.webred.ct.data.access.aggregator.elaborazioni.dto.ControlloClassamentoConsistenzaDTO;
import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.CoordBaseDTO;
import it.webred.ct.data.access.basic.catasto.dto.ImmobiliAccatastatiOutDTO;
import it.webred.ct.data.access.basic.catasto.dto.IndirizzoDTO;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.catasto.dto.RicercaSoggettoCatDTO;
import it.webred.ct.data.access.basic.catasto.dto.SoggettoCatastoDTO;
import it.webred.ct.data.access.basic.catasto.dto.SoggettoDTO;
import it.webred.ct.data.access.basic.common.dto.RicercaCivicoDTO;
import it.webred.ct.data.access.basic.compravendite.CompravenditeMUIService;
import it.webred.ct.data.access.basic.compravendite.dto.RicercaCompravenditeDTO;
import it.webred.ct.data.access.basic.compravendite.dto.SoggettoCompravenditeDTO;
import it.webred.ct.data.access.basic.docfa.DocfaService;
import it.webred.ct.data.access.basic.docfa.dto.DettaglioDocfaDTO;
import it.webred.ct.data.access.basic.docfa.dto.DocfaDatiCensuariDTO;
import it.webred.ct.data.access.basic.docfa.dto.DocfaInParteUnoDTO;
import it.webred.ct.data.access.basic.docfa.dto.RicercaOggettoDocfaDTO;
import it.webred.ct.data.access.basic.fornitureelettriche.FornitureElettricheService;
import it.webred.ct.data.access.basic.fornitureelettriche.dto.FornituraElettricaDTO;
import it.webred.ct.data.access.basic.fornituregas.FornitureGasService;
import it.webred.ct.data.access.basic.fornituregas.dto.FornituraGasDTO;
import it.webred.ct.data.access.basic.locazioni.LocazioniService;
import it.webred.ct.data.access.basic.locazioni.dto.RicercaLocazioniDTO;
import it.webred.ct.data.access.basic.pregeo.PregeoService;
import it.webred.ct.data.access.basic.pregeo.dto.RicercaPregeoDTO;
import it.webred.ct.data.access.basic.successioni.SuccessioniService;
import it.webred.ct.data.access.basic.successioni.dto.RicercaSuccessioniDTO;
import it.webred.ct.data.model.catasto.ConsSoggTab;
import it.webred.ct.data.model.catasto.SitiediVani;
import it.webred.ct.data.model.catasto.Sititrkc;
import it.webred.ct.data.model.catasto.Sitiuiu;
import it.webred.ct.data.model.compravendite.MuiFabbricatiIdentifica;
import it.webred.ct.data.model.compravendite.MuiFabbricatiInfo;
import it.webred.ct.data.model.compravendite.MuiNotaTras;
import it.webred.ct.data.model.compravendite.MuiTerreniInfo;
import it.webred.ct.data.model.docfa.DocfaDatiCensuari;
import it.webred.ct.data.model.docfa.DocfaDatiGenerali;
import it.webred.ct.data.model.docfa.DocfaDichiaranti;
import it.webred.ct.data.model.docfa.DocfaUiu;
import it.webred.ct.data.model.fornituraelettrica.SitEnelUtente;
import it.webred.ct.data.model.fornituraelettrica.SitEnelUtenza;
import it.webred.ct.data.model.gas.SitUGas;
import it.webred.ct.data.model.locazioni.LocazioneBPK;
import it.webred.ct.data.model.locazioni.LocazioniA;
import it.webred.ct.data.model.locazioni.LocazioniB;
import it.webred.ct.data.model.locazioni.LocazioniI;
import it.webred.ct.data.model.pregeo.PregeoInfo;
import it.webred.ct.data.model.successioni.SuccessioniA;
import it.webred.ct.data.model.successioni.SuccessioniB;
import it.webred.ct.data.model.successioni.SuccessioniC;
import it.webred.gitout.dto.CompravenditaDTO;
import it.webred.gitout.dto.CoordDTO;
import it.webred.gitout.dto.DocfaCensuarioValoreDTO;
import it.webred.gitout.dto.DocfaDTO;
import it.webred.gitout.dto.DocfaDatoGeneraleDTO;
import it.webred.gitout.dto.DocfaDichiaranteDTO;
import it.webred.gitout.dto.DocfaUnitaImmobiliareDTO;
import it.webred.gitout.dto.ElettricaFornituraDTO;
import it.webred.gitout.dto.ElettricaUtenteDTO;
import it.webred.gitout.dto.ElettricaUtenzaDTO;
import it.webred.gitout.dto.GasFornituraDTO;
import it.webred.gitout.dto.GasUtenteDTO;
import it.webred.gitout.dto.GasUtenzaDTO;
import it.webred.gitout.dto.ImmobileMuiDTO;
import it.webred.gitout.dto.LocalizzazioneDTO;
import it.webred.gitout.dto.LocazioneAOggettoDTO;
import it.webred.gitout.dto.LocazioneBSoggettoDTO;
import it.webred.gitout.dto.LocazioneDTO;
import it.webred.gitout.dto.LocazioneIImmobileDTO;
import it.webred.gitout.dto.MetricaDTO;
import it.webred.gitout.dto.PersonaDTO;
import it.webred.gitout.dto.PregeoDTO;
import it.webred.gitout.dto.RespCompravenditeDTO;
import it.webred.gitout.dto.RespCoordDTO;
import it.webred.gitout.dto.RespDocfaDTO;
import it.webred.gitout.dto.RespElettricheFornitureDTO;
import it.webred.gitout.dto.RespGasFornitureDTO;
import it.webred.gitout.dto.RespLocazioniDTO;
import it.webred.gitout.dto.RespPregeoDTO;
import it.webred.gitout.dto.RespSoggettoCatDTO;
import it.webred.gitout.dto.RespSuccessioniDTO;
import it.webred.gitout.dto.RespUnitaImmoDTO;
import it.webred.gitout.dto.RespUnitaTerreDTO;
import it.webred.gitout.dto.RogitoDTO;
import it.webred.gitout.dto.SoggettoCatDTO;
import it.webred.gitout.dto.SoggettoMuiDTO;
import it.webred.gitout.dto.SuccessioneDTO;
import it.webred.gitout.dto.SuccessioneDefuntoDTO;
import it.webred.gitout.dto.SuccessioneEreditaDTO;
import it.webred.gitout.dto.SuccessionePraticaDTO;
import it.webred.gitout.dto.TerreniMuiDTO;
import it.webred.gitout.dto.TitolaritaDTO;
import it.webred.gitout.dto.UnitaImmoDTO;
import it.webred.gitout.dto.UnitaTerreDTO;

import java.math.BigDecimal;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.criteria.CriteriaBuilder.Trimspec;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;


@Stateless
@WebService
@SOAPBinding(style=Style.DOCUMENT)
public class GitOutWS extends GitOutBase implements GitOutService{
	
	public final static Integer QRY_LIMIT = 100;

	@Resource
	WebServiceContext wsContext; 
	
	public GitOutWS() {
	}//-------------------------------------------------------------------------
	
	 @WebMethod
	public RespCoordDTO getCoordUiByTopo(@WebParam(name = "belfiore") String belfiore, @WebParam(name = "utente") String utente, @WebParam(name = "ot_prik") String ot_prik,
			@WebParam(name = "tipoArea") String tipoArea, @WebParam(name = "nomeVia") String nomeVia, @WebParam(name = "civico") String civico){

		List<CoordBaseDTO> result = new ArrayList<CoordBaseDTO>();
		
		RespCoordDTO responseDto = new RespCoordDTO();
		ArrayList<CoordDTO> output = new ArrayList<CoordDTO>();
		/*
		 *  parametri URL credenziali:
			- ente (BELFIORE_ENTE);
			- utente
			- ot_prik (ONE TIME PRIVATE KEY); 
		 */
		CatastoService catastoService = (CatastoService) getEjb("CT_Service", "CT_Service_Data_Access", "CatastoServiceBean");
		
		belfiore = notNullAndTrim(belfiore);
		utente = notNullAndTrim(utente);
		//pwd = notNullAndTrim(pwd);
		ot_prik = notNullAndTrim(ot_prik);
		tipoArea = notNullAndTrim(tipoArea);
		nomeVia = notNullAndTrim(nomeVia);
		civico = notNullAndTrim(civico);
		
		if (!utente.trim().equalsIgnoreCase("") && !belfiore.trim().equalsIgnoreCase("")){
			
			try{
				String[] fonti = (String[])mappaTemiFonti.get("CATASTO");
				boolean autorizzato = false;
				
				Context cont = new InitialContext();
				DataSource theDataSource = (DataSource)cont.lookup("java:/AMProfiler");
				
//				String driver="oracle.jdbc.driver.OracleDriver";
//				String nomeUtente="AM_F704_GIT";
//				String pwdUtente="AM_F704_GIT";
				
//				String url="dbc:oracle:thin:@roma:1521:roma";
//			    Connection conn = DriverManager.getConnection(url, nomeUtente, pwdUtente);
				
			    Connection conn = theDataSource.getConnection();
			    /*
			     * Recupero del metodo di autenticazione scelto per le applicazioni 
			     * esterne
			     */
			    MessageContext msgx = wsContext.getMessageContext();
			    //HttpExchange exchange = (HttpExchange)msgx.get(JAXWSProperties.HTTP_EXCHANGE);
			    //HttpExchange exchange = (HttpExchange)msgx.get("com.sun.xml.ws.http.exchange");
			    HttpServletRequest exchange = (HttpServletRequest)msgx.get(MessageContext.SERVLET_REQUEST);
			    //InetSocketAddress remoteAddress = exchange.getRemoteAddress();
			    //String remoteHost = remoteAddress.getHostName();
			    String remoteHost = exchange.getRemoteAddr();
			    
			    String authType = this.getGitOutAuthenticationType(conn, belfiore);
			    Long idValid = 0l; 
			   if (authType != null && !authType.trim().equalsIgnoreCase("")){
				   if (authType.trim().equalsIgnoreCase("TOKEN")){
					   idValid = this.getOtpValidationByToken(conn, utente, belfiore, ot_prik);				   
				   }else if (authType.startsWith("IP")){
					   idValid = this.getOtpValidationByIp(conn, utente, belfiore, authType, remoteHost);
				   }
			   }
			    /*
			     * validazione OTP
			     */
			   if (idValid != null && idValid > 0){
				   /*
				    * Aggiorna stato OTP = USATA
				    */
				   this.setOtpValidation(conn, idValid);
				   
				   //Boolean autenticato = this.gitAuthentication(conn, utente, pwd);
				   Boolean autenticato = this.gitAuthentication(conn, utente);
				  
				   if (autenticato){
					
							   for (int z=0; z<fonti.length; z++){
									
									List<String> enteList = new LinkedList<String>();
									enteList.add(  belfiore  );
									
									HashMap<String,String> listaPermessi = this.getPermissionOfUser(conn, utente, belfiore);
									
				//					TEST				
				//					listaPermessi = new HashMap<String, String>();
				//					listaPermessi.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:CATASTO", "permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:CATASTO");
									
									CeTUser user = new CeTUser();
									user.setPermList(listaPermessi);
						            user.setUsername(  utente  );
						            user.setEnteList(enteList);
						            user.setCurrentEnte(  belfiore );
						            
									if (GestionePermessi.autorizzato(user, "diogene", ITEM_VISUALIZZA_FONTI_DATI, "Fonte:"+ fonti[z]))
										autorizzato = true;
								}
								if (autorizzato){
				
									RicercaCivicoDTO ric = new RicercaCivicoDTO();
				
									ric.setEnteId(  belfiore  );
									
									ric.setToponimoVia(  tipoArea  );
									ric.setDescrizioneVia(  nomeVia  );
									ric.setCivico(  civico  );
									
									result = catastoService.getCoordUiByTopo(ric);
									
									if (result != null && result.size()>0){
//										output = new ArrayList<CoordDTO>();
										for(int i=0; i<result.size(); i++){
											CoordBaseDTO cb = (CoordBaseDTO)result.get(i);
											CoordDTO c = new CoordDTO();
											c.setFoglio(cb.getFoglio());
											c.setNumero(cb.getNumero());
											
											output.add( c );
											
										}										
									}else{
//										output = new CoordDTO[1];
//										CoordDTO coordDTO = new CoordDTO();
//										coordDTO.setDescrizione("Nessun risultato trovato");
//										output[0] = coordDTO;
										
										String descrizione = "Nessun risultato trovato";
										responseDto.setSuccess(false);
										responseDto.setError(descrizione);
										System.out.println(descrizione);
										
									}
									
									responseDto.setLstObjs(output);
									responseDto.setSuccess(true);
									
								}else{
									String descrizione = "Utente NON autorizzato ad accedere alle informazioni richieste!!!";
//									output = new CoordDTO[1];
//									CoordDTO coordDTO = new CoordDTO();
//									coordDTO.setDescrizione(descrizione);
//									output[0] = coordDTO;
									
									responseDto.setSuccess(false);
									responseDto.setError(descrizione);
									System.out.println(descrizione);
								}
					}else{
						String descrizione = "Autenticazione fallita. Credenziali GIT errate o scadute!!!";
//						output = new CoordDTO[1];
//						CoordDTO coordDTO = new CoordDTO();
//						coordDTO.setDescrizione(descrizione);
//						output[0] = coordDTO;
						
						responseDto.setSuccess(false);
						responseDto.setError(descrizione);
						System.out.println(descrizione);
						
					}

			   }else{
//				   output = new CoordDTO[1];
//					CoordDTO coordDTO = new CoordDTO();
				   String descrizione = "";
					if (authType != null && !authType.trim().equalsIgnoreCase("") && authType.trim().equalsIgnoreCase("TOKEN"))
						descrizione = "OTP private key NON VALIDATA!!! ";
					if (authType != null && !authType.trim().equalsIgnoreCase("") && authType.startsWith("IP"))
						descrizione = "IP Remote Host NON VALIDO!!! " + remoteHost;
					//output[0] = coordDTO;
				   responseDto.setError(descrizione);					
				   responseDto.setSuccess(false);
				   System.out.println(descrizione);
				   
			   }
			   //conn.commit();
			   if (conn != null)
				   conn.close();
			}
			catch (Exception t) {
				t.printStackTrace();
				String descrizione = t.getMessage();
				responseDto.setSuccess(false);
				responseDto.setError(descrizione);
				System.out.println(descrizione);
			}
		}else{
//			   output = new CoordDTO[1];
//				CoordDTO coordDTO = new CoordDTO();
				String descrizione = "Utente e/o Codice Belfiore NON VALORIZZATI!!! ";
//				output[0] = coordDTO;
				responseDto.setSuccess(false);
				responseDto.setError(descrizione);
				System.out.println(descrizione);
		   }

		return responseDto;
	}//-------------------------------------------------------------------------
	 
	@WebMethod
	public RespUnitaImmoDTO getUiuTitByCoord(@WebParam(name = "belfiore") String belfiore, @WebParam(name = "utente") String utente, @WebParam(name = "ot_prik") String ot_prik, 
			@WebParam(name = "sezione") String sezione, @WebParam(name = "foglio") String foglio, @WebParam(name = "particella") String particella, @WebParam(name = "subalterno") String subalterno, @WebParam(name = "dataValidita") String dataValidita){
		
		RespUnitaImmoDTO responseDto = new RespUnitaImmoDTO();
		ArrayList<UnitaImmoDTO> output = new ArrayList<UnitaImmoDTO>();
		
//		UnitaImmoDTO[] output = null;
		/*
		 *  parametri URL credenziali:
			- ente (BELFIORE_ENTE);
			- utente
			- ot_prik (ONE TIME PRIVATE KEY); 
		 */
		CatastoService catastoService = (CatastoService) getEjb("CT_Service", "CT_Service_Data_Access", "CatastoServiceBean");
		
		belfiore = notNullAndTrim(belfiore);
		utente = notNullAndTrim(utente);
		//pwd = notNullAndTrim(pwd);
		ot_prik = notNullAndTrim(ot_prik);
		sezione = notNullAndTrim(sezione);
		foglio = notNullAndTrim(foglio);
		particella = notNullAndTrim(particella);
		subalterno = notNullAndTrim(subalterno);
		dataValidita = notNullAndTrim(dataValidita); 	// nel formato: GG/MM/AAAA
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		
		if (!utente.trim().equalsIgnoreCase("") && !belfiore.trim().equalsIgnoreCase("")){
			
			try{

				String[] fonti = (String[])mappaTemiFonti.get("CATASTO");
				boolean autorizzato = false;
				
				Context cont = new InitialContext();
				DataSource theDataSource = (DataSource)cont.lookup("java:/AMProfiler");
				
			    Connection conn = theDataSource.getConnection();
			    /*
			     * Recupero del metodo di autenticazione scelto per le applicazioni 
			     * esterne
			     */
			    MessageContext msgx = wsContext.getMessageContext();
			    HttpServletRequest exchange = (HttpServletRequest)msgx.get(MessageContext.SERVLET_REQUEST);
			    String remoteHost = exchange.getRemoteAddr();
			    
			    String authType = this.getGitOutAuthenticationType(conn, belfiore);
			    Long idValid = 0l; 
			   if (authType != null && !authType.trim().equalsIgnoreCase("")){
				   if (authType.trim().equalsIgnoreCase("TOKEN")){
					   idValid = this.getOtpValidationByToken(conn, utente, belfiore, ot_prik);				   
				   }else if (authType.startsWith("IP")){
					   idValid = this.getOtpValidationByIp(conn, utente, belfiore, authType, remoteHost);
				   }
			   }
			   /*
			     * validazione OTP
			     */
			   if (idValid != null && idValid > 0){
				   /*
				    * Aggiorna stato OTP = USATA
				    */
				   this.setOtpValidation(conn, idValid);
				   
				   Boolean autenticato = this.gitAuthentication(conn, utente);
				  
				   if (autenticato){
							   for (int z=0; z<fonti.length; z++){
									
									List<String> enteList = new LinkedList<String>();
									enteList.add(  belfiore  );

									HashMap<String,String> listaPermessi = this.getPermissionOfUser(conn, utente, belfiore);

				//					TEST				
				//					listaPermessi = new HashMap<String, String>();
				//					listaPermessi.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:CATASTO", "permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:CATASTO");

									CeTUser user = new CeTUser();
									user.setPermList(listaPermessi);
						            user.setUsername(  utente  );
						            user.setEnteList(enteList);
						            user.setCurrentEnte(  belfiore );

									if (GestionePermessi.autorizzato(user, "diogene", ITEM_VISUALIZZA_FONTI_DATI, "Fonte:"+ fonti[z]))
										autorizzato = true;
								}
								if (autorizzato){
									/*
									 * Valorizzo il bean di ricerca 
									 */
									RicercaOggettoCatDTO roc = new RicercaOggettoCatDTO();
									roc.setLimit(QRY_LIMIT);
									roc.setEnteId(  belfiore  );
									roc.setCodEnte(  belfiore  );
									roc.setSezione(sezione);
									roc.setFoglio(foglio);
									roc.setParticella(particella);
									roc.setUnimm(subalterno);
									boolean checkformat = false;
									if (dataValidita.matches("([0-9]{2})/([0-9]{2})/([0-9]{4})"))
									    checkformat=true;
									else
									   checkformat=false;
									if(checkformat){
										//String dateInString = "07/06/2013";
//										try {
										Date dateVal = formatter.parse(dataValidita);
//										System.out.println(dateVal);
//										System.out.println(formatter.format(dateVal));
//										} catch (ParseException e) {
//											e.printStackTrace();
//										}
										roc.setDtVal(dateVal);
									}else{
										/*
										 * TODO Formato data errato
										 */
									}
									/*
									 * Recupero lista immobili o il singolo sub 
									 * a seconda della valorizzazione del parametro 
									 * sub o meno
									 */
									Sitiuiu sitiuiu = new Sitiuiu();
									List<Object[]> alSitiuiu = catastoService.getListaImmobiliByParams(roc);
									/*
									 * Per ogni UIU recupero i titolari						
									 */
									if (alSitiuiu != null && alSitiuiu.size()>0){
										
//										output = new UnitaImmoDTO[alSitiuiu.size()];
										
										Sitiuiu uiu = null;
										for (int j=0; j<alSitiuiu.size(); j++){
											Object[] objs = alSitiuiu.get(j); 
											uiu = (Sitiuiu)objs[0];
											String sezId = (String)objs[1];
											if (uiu != null){
												UnitaImmoDTO unitaImmo = new UnitaImmoDTO();
												/*
												 * Valorizzo attributi oggetto unita immobiliare
												 */
												unitaImmo.setCategoria(uiu.getCategoria());
												unitaImmo.setClasse(uiu.getClasse());
												String consistenza = uiu.getConsistenza()!=null?uiu.getConsistenza().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString():"0";
												unitaImmo.setConsistenza( consistenza );
												Date dataFineVal = uiu.getId().getDataFineVal();
												unitaImmo.setDataFineVal( formatter.format(dataFineVal) );
												Date dataInizioVal = uiu.getDataInizioVal();
												unitaImmo.setDataInizioVal( formatter.format(dataInizioVal) );
												//unitaImmo.setDescrizione(descrizione);
												unitaImmo.setFoglio( new Long(uiu.getId().getFoglio()).toString() );
												unitaImmo.setNumero(uiu.getId().getParticella());
												String rendita = uiu.getRendita()!=null?uiu.getRendita().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString():"0";
												unitaImmo.setRendita( rendita );
												String supCat = uiu.getSupCat()!=null?uiu.getSupCat().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString():"0";
												unitaImmo.setSupCat( supCat );
												unitaImmo.setUnimm( new Long(uiu.getId().getUnimm()).toString() );
												
												RicercaOggettoCatDTO oggetto = new RicercaOggettoCatDTO();
												oggetto.setLimit(0); //i titolari della uiu devono essere tutti presenti 
												oggetto.setEnteId(  belfiore  );
												oggetto.setFoglio( new Long(uiu.getId().getFoglio()).toString() );
												oggetto.setParticella( uiu.getId().getParticella() );
												oggetto.setUnimm( new Long(uiu.getId().getUnimm()).toString() );
												oggetto.setSezione( notNullAndTrim(sezId) );
												/*
												 * Soggetti collegati ad ogni UIU
												 */
												List<SoggettoCatastoDTO> alPersone = null;
												if (!dataValidita.trim().equalsIgnoreCase("") && roc.getDtVal() != null){
													oggetto.setDtVal( roc.getDtVal() );
													alPersone = catastoService.getListaSoggettiUiuAllaDataByFPS(oggetto);									
												}else{
													alPersone = catastoService.getListaSoggettiUiuByFPS(oggetto);										
												}
												
												
												ArrayList<PersonaDTO> lstPersone = null;
												if (alPersone != null && alPersone.size()>0){
													lstPersone = new ArrayList<PersonaDTO>();
													for (int index=0; index<alPersone.size(); index++){
														SoggettoCatastoDTO sogCat = alPersone.get(index);
														if (sogCat != null){
															PersonaDTO persona = new PersonaDTO();
															persona.setCodiFisc( sogCat.getConsSoggTab().getCodiFisc() );
															persona.setCodiPiva( sogCat.getConsSoggTab().getCodiPiva() );
															String dataFine = "";
															if (sogCat.getConsSoggTab().getDataFine() != null){
																dataFine = formatter.format( sogCat.getConsSoggTab().getDataFine() );	
															}
															persona.setDataFine( dataFine );
															String dataInizio = "";
															if (sogCat.getConsSoggTab().getDataInizio() != null){
																dataInizio = formatter.format( sogCat.getConsSoggTab().getDataInizio() );
															}
															persona.setDataInizio( dataInizio );
															//persona.setDescrizione(descrizione);
															persona.setDescTipo( sogCat.getDescTipo() );
															persona.setFlagPersFisica( sogCat.getConsSoggTab().getFlagPersFisica() );
															persona.setNome( sogCat.getConsSoggTab().getNome() );
															String percPoss = sogCat.getPercPoss()!=null?sogCat.getPercPoss().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString():"";
															persona.setPercPoss( percPoss );
															persona.setRagiSoci( sogCat.getConsSoggTab().getRagiSoci() );
															persona.setSesso( sogCat.getConsSoggTab().getSesso() );
															persona.setTipoTitolo( sogCat.getTipoTitolo() );
															persona.setTitolo( sogCat.getTitolo() );
															
															lstPersone.add( persona );															
														}
													}
												}else{
													lstPersone = new ArrayList<PersonaDTO>();
												}
												unitaImmo.setAlPersone( lstPersone );
												
												/*
												 * recupero localizzazioni
												 */
												//catastoService.getListaIndirizzi(ro); //BELFIORE, PKID_STRA (se c'è anche like con CIVICO)
												//catastoService.getListaIndirizziImmobile(ro);  //BELFIORE, PKID_UIU
												//catastoService.getLocalizzazioneCatastaleDescr(ro);
												List<IndirizzoDTO> alLocalizzazioniSIT = null;
												if (!dataValidita.trim().equalsIgnoreCase("") && roc.getDtVal() != null){
													oggetto.setDtVal( roc.getDtVal() );
													alLocalizzazioniSIT = catastoService.getListaIndirizziPartAllaData(oggetto); //SITICIVI SITIDSTR - codNazionale, idSez, foglio, particella, dtVal,									
												}else{
													alLocalizzazioniSIT = catastoService.getListaIndirizziPart(oggetto); //SITICIVI SITIDSTR - codNazionale, idSez, foglio, particella										
												}
												
												List<IndirizzoDTO> alLocalizzazioniCAT = catastoService.getLocalizzazioneCatastale(oggetto);  // LOAC_CAT_* - codEnte, foglio, particella, subalterno
												ArrayList<LocalizzazioneDTO> lstLocalizzazioni = new ArrayList<LocalizzazioneDTO>();
												lstLocalizzazioni = new ArrayList<LocalizzazioneDTO>();
												if (alLocalizzazioniSIT != null && alLocalizzazioniSIT.size()>0){
													for (int index=0; index<alLocalizzazioniSIT.size(); index++){
														LocalizzazioneDTO localizza = new LocalizzazioneDTO();
														IndirizzoDTO indirizzo = alLocalizzazioniSIT.get(index);
														if (indirizzo != null){
															localizza.setIndirizzo( indirizzo.getStrada() );
															localizza.setCivico( indirizzo.getNumCivico() );
															localizza.setTipoLoca("SIT");
															lstLocalizzazioni.add(localizza);

														}
													}
												}
												if (alLocalizzazioniCAT != null && alLocalizzazioniCAT.size()>0){
													for (int index=0; index<alLocalizzazioniCAT.size(); index++){
														LocalizzazioneDTO localizza = new LocalizzazioneDTO();
														IndirizzoDTO indirizzo = alLocalizzazioniCAT.get(index);
														if (indirizzo != null){
															localizza.setIndirizzo( indirizzo.getStrada() );
															localizza.setCivico( indirizzo.getNumCivico() );
															localizza.setTipoLoca("CATASTO");
															lstLocalizzazioni.add(localizza);

														}
													}
												}
												unitaImmo.setAlLocalizzazioni(lstLocalizzazioni);
												/*
												 * recupero dati metrici
												 */
												List<SitiediVani> alMetrici = catastoService.getDettaglioVaniC340(oggetto); //Foglio, Particella, Subalterno, dtVal
												ArrayList<MetricaDTO> lstMetrici = new ArrayList<MetricaDTO>();
												if (alMetrici != null && alMetrici.size()>0){
													for (int index=0; index<alMetrici.size(); index++){
														MetricaDTO metrica = new MetricaDTO();
														SitiediVani sitiediVani = alMetrici.get(index);
														if (sitiediVani != null){
															String altezzaMax = sitiediVani.getAltezzamax()!=null?sitiediVani.getAltezzamax().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString():"";
															metrica.setAltezzaMax( altezzaMax );
															String altezzaMin = sitiediVani.getAltezzamin()!=null?sitiediVani.getAltezzamin().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString():"";
															metrica.setAltezzaMin( altezzaMin );
															metrica.setAmbiente( sitiediVani.getAmbiente() );
															String edificio = sitiediVani.getEdificio()!=null?sitiediVani.getEdificio().setScale(0, BigDecimal.ROUND_HALF_EVEN).toString():"";
															metrica.setEdificio( edificio );
															metrica.setPiano( sitiediVani.getPiano() );
															String superficie = sitiediVani.getSupeAmbiente()!=null?sitiediVani.getSupeAmbiente().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString():"";
															metrica.setSuperficie( superficie );
															String vano = sitiediVani.getVano()!=null?sitiediVani.getVano().setScale(0, BigDecimal.ROUND_HALF_EVEN).toString():"";
															metrica.setVano( vano );

															lstMetrici.add( metrica );														

														}
													}
												}
												unitaImmo.setAlMetrici(lstMetrici);
												output.add( unitaImmo );
											}
										}
										
										responseDto.setLstObjs( output );
										responseDto.setSuccess(true);

									}else{
										String descrizione = "Unita Immobiliare NON trovate!!!";
//										output = new UnitaImmoDTO[1];
//										UnitaImmoDTO unitaImmoDTO = new UnitaImmoDTO();
//										unitaImmoDTO.setDescrizione(descrizione);
//										output[0] = unitaImmoDTO;
										responseDto.setSuccess(false);
										responseDto.setError(descrizione);
										System.out.println(descrizione);
									}

									
								}else{
									String descrizione = "Utente NON autorizzato ad accedere alle informazioni richieste!!!";
//									output = new UnitaImmoDTO[1];
//									UnitaImmoDTO unitaImmoDTO = new UnitaImmoDTO();
//									unitaImmoDTO.setDescrizione(descrizione);
//									output[0] = unitaImmoDTO;
									responseDto.setSuccess(false);
									responseDto.setError(descrizione);
									System.out.println(descrizione);
								}
					}else{
						String descrizione = "Autenticazione fallita. Credenziali GIT errate o scadute!!!";
//						output = new UnitaImmoDTO[1];
//						UnitaImmoDTO unitaImmoDTO = new UnitaImmoDTO();
//						unitaImmoDTO.setDescrizione(descrizione);
//						output[0] = unitaImmoDTO;
						responseDto.setSuccess(false);
						responseDto.setError(descrizione);
						System.out.println(descrizione);
					}
			   }else{
//				   output = new UnitaImmoDTO[1];
//				   UnitaImmoDTO unitaImmoDTO = new UnitaImmoDTO();
					String descrizione = "";
					if (authType != null && !authType.trim().equalsIgnoreCase("") && authType.trim().equalsIgnoreCase("TOKEN"))
						descrizione = "OTP private key NON VALIDATA!!!";
					if (authType != null && !authType.trim().equalsIgnoreCase("") && authType.startsWith("IP"))
						descrizione = "IP Remote Host NON VALIDO!!! " + remoteHost;
					responseDto.setSuccess(false);
					responseDto.setError(descrizione);
				    System.out.println(descrizione);
				   
			   }
			   if (conn != null)
				   conn.close();
			}
			catch (Exception t) {
				t.printStackTrace();
				String descrizione = t.getMessage();
				responseDto.setSuccess(false);
				responseDto.setError(descrizione);
			    System.out.println(descrizione);
			}
		}else{
//			output = new UnitaImmoDTO[1];
//			UnitaImmoDTO unitaImmoDTO = new UnitaImmoDTO();
			String descrizione = "Utente e/o Codice Belfiore NON VALORIZZATI!!! ";
//			output[0] = unitaImmoDTO;
			responseDto.setSuccess(false);
			responseDto.setError(descrizione);
		    System.out.println(descrizione);
			   
		   }
		
		return responseDto;
	 }//------------------------------------------------------------------------
	
	@WebMethod
	public RespSoggettoCatDTO getPersFGByCFPI(@WebParam(name = "belfiore") String belfiore, @WebParam(name = "utente") String utente, @WebParam(name = "ot_prik") String ot_prik, 
			@WebParam(name = "identificativo") String identificativo, @WebParam(name = "tipologia") String tipologia){

		ArrayList<SoggettoCatDTO> output = new ArrayList<SoggettoCatDTO>();
		RespSoggettoCatDTO responseDto = new RespSoggettoCatDTO();
		
		/*
		 *  parametri URL credenziali:
			- ente (BELFIORE_ENTE);
			- utente
			- ot_prik (ONE TIME PRIVATE KEY); 
		 */
		CatastoService catastoService = (CatastoService) getEjb("CT_Service", "CT_Service_Data_Access", "CatastoServiceBean");
		
		belfiore = notNullAndTrim(belfiore);
		utente = notNullAndTrim(utente);
		//pwd = notNullAndTrim(pwd);
		ot_prik = notNullAndTrim(ot_prik);
		/*
		 * Questi parametri ci consentono anche di effettuare ricerche incrociate:
		 * codici fiscali tra le persone giuridiche e partite iva tra le persone fisiche
		 */
		identificativo = notNullAndTrim(identificativo);//codice fiscale o partita iva
		tipologia = notNullAndTrim(tipologia);			//P= fisica; G= giuridica

		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		
		if (!utente.trim().equalsIgnoreCase("") && !belfiore.trim().equalsIgnoreCase("")){
			
			try{

				String[] fonti = (String[])mappaTemiFonti.get("CATASTO");
				boolean autorizzato = false;
				
				Context cont = new InitialContext();
				DataSource theDataSource = (DataSource)cont.lookup("java:/AMProfiler");
				
			    Connection conn = theDataSource.getConnection();
			    /*
			     * Recupero del metodo di autenticazione scelto per le applicazioni 
			     * esterne
			     */
			    MessageContext msgx = wsContext.getMessageContext();
			    HttpServletRequest exchange = (HttpServletRequest)msgx.get(MessageContext.SERVLET_REQUEST);
			    String remoteHost = exchange.getRemoteAddr();
			    
			    String authType = this.getGitOutAuthenticationType(conn, belfiore);
			    Long idValid = 0l; 
			   if (authType != null && !authType.trim().equalsIgnoreCase("")){
				   if (authType.trim().equalsIgnoreCase("TOKEN")){
					   idValid = this.getOtpValidationByToken(conn, utente, belfiore, ot_prik);				   
				   }else if (authType.startsWith("IP")){
					   idValid = this.getOtpValidationByIp(conn, utente, belfiore, authType, remoteHost);
				   }
			   }
			   /*
			     * validazione OTP
			     */
			   if (idValid != null && idValid > 0){
				   /*
				    * Aggiorna stato OTP = USATA
				    */
				   this.setOtpValidation(conn, idValid);
				   
				   Boolean autenticato = this.gitAuthentication(conn, utente);
				  
				   if (autenticato){
							   for (int z=0; z<fonti.length; z++){
									
									List<String> enteList = new LinkedList<String>();
									enteList.add(  belfiore  );

									HashMap<String,String> listaPermessi = this.getPermissionOfUser(conn, utente, belfiore);

				//					TEST				
				//					listaPermessi = new HashMap<String, String>();
				//					listaPermessi.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:CATASTO", "permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:CATASTO");

									CeTUser user = new CeTUser();
									user.setPermList(listaPermessi);
						            user.setUsername(  utente  );
						            user.setEnteList(enteList);
						            user.setCurrentEnte(  belfiore );

									if (GestionePermessi.autorizzato(user, "diogene", ITEM_VISUALIZZA_FONTI_DATI, "Fonte:"+ fonti[z]))
										autorizzato = true;
								}
								if (autorizzato){
									/*
									 * Valorizzo il bean di ricerca 
									 */
									RicercaSoggettoCatDTO rsc = new RicercaSoggettoCatDTO();
									rsc.setLimit(QRY_LIMIT);
									rsc.setEnteId(  belfiore  );
									rsc.setCodEnte(  belfiore  );
									/*
									 * Recupero lista di persone richieste 
									 */
									List<ConsSoggTab> alConsSoggTab = new ArrayList<ConsSoggTab>();
									if ( tipologia != null && !tipologia.trim().equalsIgnoreCase("")){
										/*
										 * Cerco O Persone Fisiche o giuridiche
										 */
										if (tipologia.trim().equalsIgnoreCase("P")){
											rsc.setCodFis(identificativo);
											List<ConsSoggTab> alConsSoggTabPF = catastoService.getSoggettoByCF(rsc);
											alConsSoggTab.addAll(alConsSoggTabPF);
										}else{
											rsc.setPartIva(identificativo);
											List<ConsSoggTab> alconsSoggTabPG = catastoService.getSoggettiByPIVA(rsc);
											alConsSoggTab.addAll(alconsSoggTabPG);
										}
									}else{
										/*
										 * Cerco sia per le Persone Fisiche che Giuridiche 
										 */
										rsc.setCodFis(identificativo);
										rsc.setPartIva(identificativo);
										List<ConsSoggTab> alConsSoggTabPF = catastoService.getSoggettoByCF(rsc);
										if (alConsSoggTabPF != null && alConsSoggTabPF.size()>0)
											alConsSoggTab.addAll(alConsSoggTabPF);
										List<ConsSoggTab> alConsSoggTabPG = catastoService.getSoggettiByPIVA(rsc);
										if (alConsSoggTabPG != null && alConsSoggTabPG.size()>0)
											alConsSoggTab.addAll(alConsSoggTabPG);
									}
									/*
									 * Per ogni PERSONA recupero i fabbricati collegati						
									 */
									if (alConsSoggTab != null && alConsSoggTab.size()>0){
										
//										output = new ArrayList<SoggettoCatDTO>();
										
										ConsSoggTab consSogg = null;
										for (int j=0; j<alConsSoggTab.size(); j++){
											consSogg = alConsSoggTab.get(j); 
											if (consSogg != null){
												SoggettoCatDTO soggettoCat = new SoggettoCatDTO();
												/*
												 * Valorizzo attributi soggettoCat
												 */
												soggettoCat.setCodiFisc(consSogg.getCodiFisc());
												soggettoCat.setCodiPiva(consSogg.getCodiPiva());
												soggettoCat.setComuNasc(consSogg.getComuNasc());
												Date dataFine = consSogg.getDataFine();
												soggettoCat.setDataFine( formatter.format(dataFine) );
												Date dataInizio = consSogg.getDataInizio();
												soggettoCat.setDataInizio( formatter.format(dataInizio) );
												Date dataNasc = consSogg.getDataNasc();
												soggettoCat.setDataNasc( formatter.format(dataNasc) );
												//soggettoCat.setDescTipo(consSogg.get);
												soggettoCat.setFlagPersFisica(consSogg.getFlagPersFisica());
												soggettoCat.setNome(consSogg.getNome());
												//soggettoCat.setPercPoss(consSogg.get);
												soggettoCat.setRagiSoci(consSogg.getRagiSoci());
												//soggettoCat.setResiComu(consSogg.getResiComu());
												soggettoCat.setSesso(consSogg.getSesso());
												//soggettoCat.setTipoTitolo(consSogg.get);
												//soggettoCat.setTitolo(consSogg.get);
												
												RicercaSoggettoCatDTO soggetto = new RicercaSoggettoCatDTO();
												soggetto.setLimit(0);  
												soggetto.setEnteId(  belfiore  );
												soggetto.setCodFis(consSogg.getCodiFisc());
												//soggetto.setRicercaRegime(false); //regime di comunione dei beni, separazione dei beni, ...
												//soggetto.setRicercaSoggCollegato(true);
												/*
												 * Soggetti collegati ald ogni UIU
												 */
												List<ImmobiliAccatastatiOutDTO> alImmobiliAccatastati = catastoService.getImmobiliAccatastatiByDatiSoggetto(soggetto);	
												
												ArrayList<TitolaritaDTO> lstTitolarita = null;
												if (alImmobiliAccatastati != null && alImmobiliAccatastati.size()>0){
													lstTitolarita = new ArrayList<TitolaritaDTO>();
													for (int index=0; index<alImmobiliAccatastati.size(); index++){
														ImmobiliAccatastatiOutDTO iao = alImmobiliAccatastati.get(index);
														if (iao != null){
															TitolaritaDTO tito = new TitolaritaDTO();
															tito.setCategoria( iao.getCategoria() );
															tito.setDataFinePos( iao.getDataFineTit() );
															tito.setFoglio( iao.getFoglio() );
															tito.setNumero( iao.getNumero() );
															tito.setPercPos( iao.getPercentualePossesso() );
															tito.setSezione( iao.getSezione() );
															tito.setTitolo( iao.getDesTipoTitolo() );
															//iao.getTipoTitolo();
															tito.setUnimm( iao.getSubalterno() );
															//iao.getSub();
															/*
															 * altre info disponibili sono
															 * commentate qui sotto 
															 */
															//iao.getCivico();
															//iao.getClasse();
															//iao.getCodiceVia();
															//iao.getConsistenza();
															//iao.getDataFineUiu();
															//iao.getDataInizioTit();
															//iao.getDataInizioUiu();
															//iao.getDescRegime();
															//iao.getDescrizioneVia();
															//iao.getIndirizzoCatastale();
															//iao.getPartitaCatastale();
															//iao.getPkCuaa();
															//iao.getRegime();
															//iao.getRendita();
															//iao.getSoggCollegato();
															//iao.getSupCat();
															//iao.getTipoEvento();
															//iao.getTitNoCod();

															lstTitolarita.add(tito);
														}
													}
												}else{
													lstTitolarita = new ArrayList<TitolaritaDTO>();
												}
												soggettoCat.setAlTitolarita( lstTitolarita );
												output.add(soggettoCat);
											}
										}
										
										responseDto.setLstObjs( output );
										responseDto.setSuccess(true);
										
									}else{
										String descrizione = "Unita Immobiliare NON trovate!!!";
//										output = new ArrayList<SoggettoCatDTO>();
//										SoggettoCatDTO soggettoCatDTO = new SoggettoCatDTO();
//										soggettoCatDTO.setDescrizione(descrizione);
//										output.add(soggettoCatDTO);
										responseDto.setSuccess(false);
										responseDto.setError(descrizione);
										System.out.println(descrizione);
									}
								}else{
									String descrizione = "Utente NON autorizzato ad accedere alle informazioni richieste!!!";
									responseDto.setSuccess(false);
									responseDto.setError(descrizione);
								    System.out.println(descrizione);									
								}
					}else{
						String descrizione = "Autenticazione fallita. Credenziali GIT errate o scadute!!!";
						responseDto.setSuccess(false);
						responseDto.setError(descrizione);
						System.out.println(descrizione);
					}
			   }else{
				   String descrizione = "";
					if (authType != null && !authType.trim().equalsIgnoreCase("") && authType.trim().equalsIgnoreCase("TOKEN"))
						descrizione = "OTP private key NON VALIDATA!!!";
					if (authType != null && !authType.trim().equalsIgnoreCase("") && authType.startsWith("IP"))
						descrizione = "IP Remote Host NON VALIDO!!! " + remoteHost;
					responseDto.setSuccess(false);
					responseDto.setError(descrizione);
				    System.out.println(descrizione);
				   
			   }
			   if (conn != null)
				   conn.close();
			}
			catch (Exception t) {
				t.printStackTrace();
				String descrizione = t.getMessage();
				responseDto.setSuccess(false);
				responseDto.setError(descrizione);
				System.out.println(descrizione);
			}
		}else{
			String descrizione = "Utente e/o Codice Belfiore NON VALORIZZATI!!! ";
			responseDto.setSuccess(false);
			responseDto.setError(descrizione);
			System.out.println(descrizione);
			   
		   }
		
		return responseDto;
	 }//------------------------------------------------------------------------
	
	@WebMethod
	public RespCompravenditeDTO getCompravenditeByImmoSogg(@WebParam(name = "belfiore") String belfiore, @WebParam(name = "utente") String utente, @WebParam(name = "ot_prik") String ot_prik,
			@WebParam(name = "sezione") String sezione, @WebParam(name = "foglio") String foglio, @WebParam(name = "particella") String particella, @WebParam(name = "subalterno") String subalterno,
			@WebParam(name = "identificativo") String identificativo, @WebParam(name = "tipologia") String tipologia){
		
		ArrayList<CompravenditaDTO> output = new ArrayList<CompravenditaDTO>();
		RespCompravenditeDTO responseDto = new RespCompravenditeDTO();
		
//		List<CompravenditaDTO> lstCompravendite = new ArrayList<CompravenditaDTO>();

		CompravenditeMUIService compravenditeService = (CompravenditeMUIService) getEjb("CT_Service", "CT_Service_Data_Access", "CompravenditeMUIServiceBean");
		belfiore = notNullAndTrim(belfiore);
		utente = notNullAndTrim(utente);
		//pwd = notNullAndTrim(pwd);
		ot_prik = notNullAndTrim(ot_prik);
		sezione = notNullAndTrim(sezione);
		foglio = notNullAndTrim(foglio);
		particella = notNullAndTrim(particella);
		subalterno = notNullAndTrim(subalterno);
		/*
		 * Questi parametri ci consentono anche di effettuare ricerche incrociate:
		 * codici fiscali tra le persone giuridiche e partite iva tra le persone fisiche
		 */
		identificativo = notNullAndTrim(identificativo);//codice fiscale o partita iva
		tipologia = notNullAndTrim(tipologia);			//P= privato; G= giuridica

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		if (!utente.trim().equalsIgnoreCase("") && !belfiore.trim().equalsIgnoreCase("")){
			
			try{

				String[] fonti = (String[])mappaTemiFonti.get("CATASTO");
				boolean autorizzato = false;
				
				Context cont = new InitialContext();
				DataSource theDataSource = (DataSource)cont.lookup("java:/AMProfiler");
				
			    Connection conn = theDataSource.getConnection();
			    /*
			     * Recupero del metodo di autenticazione scelto per le applicazioni 
			     * esterne
			     */
			    MessageContext msgx = wsContext.getMessageContext();
			    HttpServletRequest exchange = (HttpServletRequest)msgx.get(MessageContext.SERVLET_REQUEST);
			    String remoteHost = exchange.getRemoteAddr();
			    
			    String authType = this.getGitOutAuthenticationType(conn, belfiore);
			    Long idValid = 0l; 
			   if (authType != null && !authType.trim().equalsIgnoreCase("")){
				   if (authType.trim().equalsIgnoreCase("TOKEN")){
					   idValid = this.getOtpValidationByToken(conn, utente, belfiore, ot_prik);				   
				   }else if (authType.startsWith("IP")){
					   idValid = this.getOtpValidationByIp(conn, utente, belfiore, authType, remoteHost);
				   }
			   }
			   /*
			     * validazione OTP
			     */
			   if (idValid != null && idValid > 0){
				   /*
				    * Aggiorna stato OTP = USATA
				    */
				   this.setOtpValidation(conn, idValid);
				   
				   Boolean autenticato = this.gitAuthentication(conn, utente);
				  
				   if (autenticato){
							   for (int z=0; z<fonti.length; z++){
									
									List<String> enteList = new LinkedList<String>();
									enteList.add(  belfiore  );

									HashMap<String,String> listaPermessi = this.getPermissionOfUser(conn, utente, belfiore);

				//					TEST				
				//					listaPermessi = new HashMap<String, String>();
				//					listaPermessi.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:CATASTO", "permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:CATASTO");

									CeTUser user = new CeTUser();
									user.setPermList(listaPermessi);
						            user.setUsername(  utente  );
						            user.setEnteList(enteList);
						            user.setCurrentEnte(  belfiore );

									if (GestionePermessi.autorizzato(user, "diogene", ITEM_VISUALIZZA_FONTI_DATI, "Fonte:"+ fonti[z]))
										autorizzato = true;
								}
							   if (autorizzato){
								   /*
								    * Tabelle Compravendite:
								      MUI_NOTA_TRAS
									  MUI_SOGGETTI 
									  MUI_INDIRIZZI_SOG
									  MUI_TITOLARITA
									  MUI_Fabbricati_Identifica
									  MUI_Fabbricati_Info
									  MUI_Terreni_Info
								    */
								   
								   boolean cercaTraSoggetti = false;
								   boolean cercaTraImmobili = false;
								   if ( identificativo != null && !identificativo.trim().equalsIgnoreCase("") ){
									   cercaTraSoggetti = true;
									  
								   }
								   
								   if ( (sezione != null && !sezione.trim().equalsIgnoreCase("")) ||
										   (foglio != null && !foglio.trim().equalsIgnoreCase("")) ||
										   (particella != null && !particella.trim().equalsIgnoreCase("")) ){
									   
									   cercaTraImmobili = true;
									   
								   }
								   /*
								    * 1. Recupero le nota in base ai parametri ricevuti
								    * 2. Recupero immobili e soggetti utilizzando l'iid delle note coinvolte
								    */
								   /*
								    * XXX 1^ Step recupero delle note in base ai parametri ricevuti
								    */
								   List<MuiNotaTras> lstMuiNotaTras = new ArrayList<MuiNotaTras>();
								   if (cercaTraImmobili && !cercaTraSoggetti){
									   System.out.println("Valorizzati i soli parametri per immobili");
									   
									   RicercaOggettoCatDTO roc = new RicercaOggettoCatDTO();
									   roc.setCodEnte(belfiore);
									   roc.setEnteId(belfiore);
									   roc.setFoglio(foglio);
									   roc.setLimit(QRY_LIMIT);
									   roc.setParticella(particella);
									   roc.setSezione(sezione);
									   //roc.setSub(sub);
									   roc.setUnimm(subalterno);
									   List<MuiNotaTras> lstMuiNotaTrasImmo = new ArrayList<MuiNotaTras>();
									   lstMuiNotaTrasImmo = compravenditeService.getListaNoteByCoord(roc);
//									   if (  subalterno != null && !subalterno.trim().equalsIgnoreCase("") ){
//										   lstMuiNotaTrasImmo = compravenditeService.getListaNoteByFPS(roc);			//Lista nota da fabbricati
//									   }else{
//										   lstMuiNotaTrasImmo = compravenditeService.getListaNoteByFP(roc);			//Lista nota da fabbricati
//									   }
									   List<MuiNotaTras> lstMuiNotaTrasTerr = new ArrayList<MuiNotaTras>();
									   lstMuiNotaTrasTerr = compravenditeService.getListaNoteTerrenoByFP(roc);	//Lista nota da terreni by fp
									   
									   if (lstMuiNotaTrasImmo != null && lstMuiNotaTrasImmo.size()>0){
										   lstMuiNotaTras.addAll(lstMuiNotaTrasImmo);
									   }
									   if (lstMuiNotaTrasTerr != null && lstMuiNotaTrasTerr.size()>0){
										   lstMuiNotaTras.addAll(lstMuiNotaTrasTerr);
									   }
									   
									   
								   }else if ( !cercaTraImmobili && cercaTraSoggetti){
									   System.out.println("Valorizzati i soli parametri per soggetti");
									   
									   RicercaCompravenditeDTO rcSog = new RicercaCompravenditeDTO();
									   rcSog.setEnteId(belfiore);
									   rcSog.setLimit(QRY_LIMIT);
									   rcSog.setTipoSoggetto(tipologia);
									   rcSog.setIdentificativoSoggetto(identificativo);
									   
									   List<SoggettoCompravenditeDTO> lstTitolari = compravenditeService.getListaSoggettiNota(rcSog);	//Lista soggetti da nota
									   /*
									    * Recupero le note dei soggetti in elenco
									    */
									   //List<MuiNotaTras> lstMuiNotaTrasSogg = new ArrayList<MuiNotaTras>();
									   List<MuiNotaTras> lstNote = null;
									   if (lstTitolari != null && lstTitolari.size()>0){
										   Iterator<SoggettoCompravenditeDTO> itTit = lstTitolari.iterator();
										   while(itTit.hasNext()){
											   SoggettoCompravenditeDTO sc = itTit.next();
											   if (sc != null){
												   String notaIid = sc.getIidNota();
												   RicercaCompravenditeDTO criteria = new RicercaCompravenditeDTO();
												   criteria.setIidNota(new BigDecimal(notaIid));
												   criteria.setEnteId(belfiore);
												   lstNote = compravenditeService.getListaNoteByCriteria(criteria);
											   }
											   if (lstNote != null && lstNote.size()>0){
												   lstMuiNotaTras.addAll(lstNote);
											   }
										   }
									   }
									   
									   
								   }else if (cercaTraImmobili && cercaTraSoggetti){
									   System.out.println("Valorizzati sia parametri per immobili che per soggetti");
									   /*
									    * Cerca tra i soggetti
									    */
									   RicercaCompravenditeDTO rcSog = new RicercaCompravenditeDTO();
									   rcSog.setEnteId(belfiore);
									   rcSog.setLimit(QRY_LIMIT);
									   rcSog.setTipoSoggetto(tipologia);
									   rcSog.setIdentificativoSoggetto(identificativo);
									   
									   List<SoggettoCompravenditeDTO> lstTitolari = compravenditeService.getListaSoggettiNota(rcSog);	//Lista soggetti da nota
									   /*
									    * Recupero le note dei soggetti in elenco
									    */
									   //List<MuiNotaTras> lstMuiNotaTrasSogg = new ArrayList<MuiNotaTras>();
									   List<MuiNotaTras> lstNote = null;
									   if (lstTitolari != null && lstTitolari.size()>0){
										   Iterator<SoggettoCompravenditeDTO> itTit = lstTitolari.iterator();
										   while(itTit.hasNext()){
											   SoggettoCompravenditeDTO sc = itTit.next();
											   if (sc != null){
												   String notaIid = sc.getIidNota();
												   RicercaCompravenditeDTO criteria = new RicercaCompravenditeDTO();
												   criteria.setIidNota(new BigDecimal(notaIid));
												   criteria.setEnteId(belfiore);
												   lstNote = compravenditeService.getListaNoteByCriteria(criteria);
											   }
											   if (lstNote != null && lstNote.size()>0){
												   lstMuiNotaTras.addAll(lstNote);
											   }
										   }
									   }
									   /*
									    * Cerca tra immobili
									    */
									   RicercaOggettoCatDTO roc = new RicercaOggettoCatDTO();
									   roc.setCodEnte(belfiore);
									   roc.setEnteId(belfiore);
									   roc.setFoglio(foglio);
									   roc.setLimit(QRY_LIMIT);
									   roc.setParticella(particella);
									   roc.setSezione(sezione);
									   //roc.setSub(sub);
									   roc.setUnimm(subalterno);
									   List<MuiNotaTras> lstMuiNotaTrasImmo = new ArrayList<MuiNotaTras>();
									   lstMuiNotaTrasImmo = compravenditeService.getListaNoteByCoord(roc);

									   List<MuiNotaTras> lstMuiNotaTrasTerr = new ArrayList<MuiNotaTras>();
									   lstMuiNotaTrasTerr = compravenditeService.getListaNoteTerrenoByFP(roc);	//Lista nota da terreni by fp
									   
									   if (lstMuiNotaTrasImmo != null && lstMuiNotaTrasImmo.size()>0){
										   lstMuiNotaTras.addAll(lstMuiNotaTrasImmo);
									   }
									   if (lstMuiNotaTrasTerr != null && lstMuiNotaTrasTerr.size()>0){
										   lstMuiNotaTras.addAll(lstMuiNotaTrasTerr);
									   }
									   
								   }else{
									   System.out.println("Nessun parametro valorizzato");
									   
								   }
								   /*
								    * XXX 2^ Step Avendo le note posso recuperare Immobili (Terreni e fabbricati) e Soggetti
								    */
								   
								   if ( lstMuiNotaTras != null && lstMuiNotaTras.size()>0 ){
									   RicercaCompravenditeDTO filtroCompravendite = new RicercaCompravenditeDTO();
									   filtroCompravendite.setFoglio(foglio);
									   filtroCompravendite.setIdentificativoSoggetto(identificativo);
									   filtroCompravendite.setEnteId(belfiore);
									   filtroCompravendite.setParticella(particella);
									   filtroCompravendite.setSezione(sezione);
									   filtroCompravendite.setTipoSoggetto(tipologia);
									   filtroCompravendite.setSub(subalterno);

									   for (int index=0; index<lstMuiNotaTras.size(); index++){

										   MuiNotaTras notaCur = lstMuiNotaTras.get(index);
										   filtroCompravendite.setIidNota(notaCur.getIid());
										   //filtroCompravendite.setIidFornitura(notaCur.getIidFornitura());
										   
										   CompravenditaDTO compravendita = new CompravenditaDTO();
										   RogitoDTO rogito = new RogitoDTO();
										   rogito.setAnno(notaCur.getAnnoNota());
										   rogito.setDataDiff(notaCur.getDataInDif());
										   rogito.setDataPresentazioneAtto( notaCur.getDataPresAtto());
										   rogito.setDataRegistrazioneAttivita(notaCur.getDataRegInAtti());
										   rogito.setDataValiditaAtto(notaCur.getDataValiditaAtto());
										   //rogito.setDescrizione(descrizione);
										   rogito.setEsito(notaCur.getEsitoNota());
										   rogito.setNumeroRepertorio(notaCur.getNumeroRepertorio());
										   rogito.setRegistrazioneDiff(notaCur.getRegistrazioneDif());
										   rogito.setRegistroParticolare(notaCur.getNumeroNotaTras());
										   rogito.setRettifica(notaCur.getFlagRettifica());
										   rogito.setRoganteCodiceFiscale(notaCur.getCodFiscRog());
										   rogito.setRoganteNominativo(notaCur.getCognomeNomeRog());
										   rogito.setSede(notaCur.getSedeRog());
										   rogito.setTipo(notaCur.getTipoNota());
										   
										   compravendita.setRogito(rogito);
										   
										   ArrayList<ImmobileMuiDTO> lstImmobiliMui = new ArrayList<ImmobileMuiDTO>();
										   ArrayList<TerreniMuiDTO> lstTerreniMui = new ArrayList<TerreniMuiDTO>();
										   ArrayList<SoggettoMuiDTO> lstSoggettiMui = new ArrayList<SoggettoMuiDTO>();
										   List<Object[]> lstFabbricati = compravenditeService.getFabbricatiByParams(filtroCompravendite);
										   if (lstFabbricati != null && lstFabbricati.size()>0){
											   Iterator<Object[]> itFab = lstFabbricati.iterator();
											   while(itFab.hasNext()){
												   ImmobileMuiDTO immobile = new ImmobileMuiDTO();
												   Object[] objs = itFab.next();
												   MuiFabbricatiIdentifica fabbIde = (MuiFabbricatiIdentifica)objs[0];
												   MuiFabbricatiInfo fabbInfo = (MuiFabbricatiInfo)objs[1];
												   if (fabbIde != null){
													
													   immobile.setSezione( notNullAndTrim( notNullAndTrim(fabbIde.getSezioneCensuaria()) + " " + notNullAndTrim( fabbIde.getSezioneUrbana()) ) );
													   immobile.setFoglio( notNullAndTrim(fabbIde.getFoglio()) );
													   immobile.setNumero( notNullAndTrim(fabbIde.getNumero()) );
													   immobile.setSubalterno( notNullAndTrim(fabbIde.getSubalterno()) );
													   
												   }
												   if (fabbInfo != null){
													   immobile.setCategoria( notNullAndTrim(fabbInfo.getCategoria()) );
													   immobile.setEdificio( notNullAndTrim(fabbInfo.getTEdificio()) );
													   immobile.setGraffato( notNullAndTrim(fabbInfo.getFlagGraffato()) );
													   immobile.setIdeCatastale( notNullAndTrim(fabbInfo.getIdCatastaleImmobile()) );
													   immobile.setMetriQuadri( notNullAndTrim(fabbInfo.getMq()) );
													   immobile.setVani( notNullAndTrim(fabbInfo.getVani()) );
													   immobile.setMetriCubi( notNullAndTrim(fabbInfo.getMc()) );
													   String indirizzoCatastale = notNullAndTrim(fabbInfo.getCToponimo()) + " " + notNullAndTrim(fabbInfo.getCIndirizzo()) + " " + notNullAndTrim(fabbInfo.getCCivico1()) + " " + notNullAndTrim(fabbInfo.getCCivico2()) + " " + notNullAndTrim(fabbInfo.getCCivico3()) ; 
													   immobile.setIndirizzoCata( notNullAndTrim(indirizzoCatastale) );
													   String indirizzoTras = notNullAndTrim(fabbInfo.getTToponimo()) + " " + notNullAndTrim(fabbInfo.getTIndirizzo()) + " " + notNullAndTrim(fabbInfo.getTCivico1()) + " " + notNullAndTrim(fabbInfo.getTCivico2()) + " " + notNullAndTrim(fabbInfo.getTCivico3());
													   immobile.setIndirizzoTras( notNullAndTrim(indirizzoTras) );
													   immobile.setInterno( notNullAndTrim( notNullAndTrim(fabbInfo.getTInterno1()) + " " + notNullAndTrim(fabbInfo.getTInterno2()) ) );													   
													   immobile.setLotto( notNullAndTrim(fabbInfo.getTLotto()) );
													   immobile.setPiano( notNullAndTrim( notNullAndTrim(fabbInfo.getTPiano1()) + " " + notNullAndTrim(fabbInfo.getTPiano2()) + " " + notNullAndTrim(fabbInfo.getTPiano3()) + " " + notNullAndTrim(fabbInfo.getTPiano4()) ) );
													   immobile.setRendita( notNullAndTrim(fabbInfo.getRenditaEuro()) );
													   immobile.setScala( notNullAndTrim(fabbInfo.getTScala()) );
													   immobile.setSuperficie( notNullAndTrim(fabbInfo.getSuperficie()) );

												   }
												   if (fabbIde != null || fabbInfo != null)
													   lstImmobiliMui.add(immobile);
											   }
											   compravendita.setLstImmobili(lstImmobiliMui);
										   }
										   List<Object[]> lstTerreni = compravenditeService.getTerreniByParams(filtroCompravendite);
										   if (lstTerreni != null && lstTerreni.size()>0){
											   Iterator<Object[]> itTer = lstTerreni.iterator();
											   while(itTer.hasNext()){
												   TerreniMuiDTO terreno = new TerreniMuiDTO();
												   Object objs = itTer.next();
												   MuiTerreniInfo terrInfo = (MuiTerreniInfo)objs;
												   if (terrInfo != null){

													   terreno.setAre( notNullAndTrim(terrInfo.getAre()) );
													   terreno.setCentiare( notNullAndTrim(terrInfo.getCentiare()) );
													   terreno.setClasse( notNullAndTrim(terrInfo.getClasse()) );
													   terreno.setCodEsito( notNullAndTrim(terrInfo.getCodiceEsito()) );
													   terreno.setEdificabilita( notNullAndTrim(terrInfo.getEdificabilita()) );
													   terreno.setEttari( notNullAndTrim(terrInfo.getEttari()) );
													   terreno.setFoglio( notNullAndTrim(terrInfo.getFoglio()) );
													   terreno.setIdCatastale( notNullAndTrim(terrInfo.getIdCatastaleImmobile()) );
													   terreno.setNatura( notNullAndTrim( terrInfo.getNatura()) );
													   terreno.setNumero( notNullAndTrim(terrInfo.getNumero()) );
													   terreno.setPartita( notNullAndTrim(terrInfo.getPartita()) );
													   terreno.setQualita( notNullAndTrim(terrInfo.getQualita()) );
													   terreno.setRedditoAgrario( notNullAndTrim(terrInfo.getRedditoAgrarioEuro()) );
													   terreno.setRedditoDominicale( notNullAndTrim(terrInfo.getRedditoDominicaleEuro()) );
													   terreno.setSezione( notNullAndTrim(terrInfo.getSezioneCensuaruia()) );
													   terreno.setSubalterno( notNullAndTrim(terrInfo.getSubalterno()) );

													   lstTerreniMui.add(terreno);
													   
												   }
											   }
											   compravendita.setLstTerreni(lstTerreniMui);
										   }
										   List<Object[]> lstSoggetti = compravenditeService.getSoggettiByParams(filtroCompravendite);
										   if (lstSoggetti != null && lstSoggetti.size()>0){
											   Iterator<Object[]> itSog = lstSoggetti.iterator();
											   while(itSog.hasNext()){
												   SoggettoMuiDTO soggetto = new SoggettoMuiDTO();
												   Object[] objs = itSog.next();
												   if (objs != null){
													   /*
			SOGG.IID, SOGG.IID_FORNITURA, SOGG.ID_NOTA, SOGG.IID_NOTA, SOGG.TIPO_SOGGETTO, 
			SOGG.COGNOME, SOGG.NOME, SOGG.SESSO, SOGG.DATA_NASCITA, SOGG.LUOGO_NASCITA, 
			SOGG.CODICE_FISCALE, SOGG.DENOMINAZIONE, SOGG.SEDE, SOGG.CODICE_FISCALE_G, 
			//INDSOGG.IID, INDSOGG.IID_FORNITURA, INDSOGG.ID_NOTA, INDSOGG.IID_NOTA, INDSOGG.IID_SOGGETTO, 
			INDSOGG.TIPO_INDIRIZZO, INDSOGG.COMUNE, INDSOGG.PROVINCIA, INDSOGG.INDIRIZZO, INDSOGG.CAP
													    */
													   
													   BigDecimal iid = (BigDecimal)objs[0];
													   BigDecimal iidFornitura = (BigDecimal)objs[1];
													   String idNota = (String)objs[2];
													   BigDecimal iidNota = (BigDecimal)objs[3];
													   String tipoSoggetto = (String)objs[4]; //P: privato; G: giuridico
													   
													   String cognome = (String)objs[5];
													   String nome = (String)objs[6];
													   String sesso = (String)objs[7];
													   String dataNascita = (String)objs[8];
													   String luogoNascita = (String)objs[9];
													   
													   String codiceFiscale = (String)objs[10];
													   String denominazione = (String)objs[11];
													   String sede = (String)objs[12];
													   String piva = (String)objs[13];
													   
													   //String siid = (String)objs[14];
													   //String siidFornitura = (String)objs[15];
													   //String sidNota = (String)objs[16];
													   //String siidNota = (String)objs[17];
													   //String siidSoggetto = (String)objs[18];
													   
													   String tipoIndirizzoRes = (String)objs[14];
													   String comuneRes = (String)objs[15];
													   String provinciaRes = (String)objs[16];
													   String indirizzoRes = (String)objs[17];
													   String capRes = (String)objs[18];
													   
													   soggetto.setCodiFisc( notNullAndTrim(codiceFiscale) );
													   soggetto.setCodiPiva( notNullAndTrim(piva) );
													   soggetto.setComuNasc( notNullAndTrim(luogoNascita) );
													   soggetto.setDataNasc( notNullAndTrim(dataNascita) );
													   //soggetto.setDescrizione(notNullAndTrim(codiceFiscale) );
													   soggetto.setNome( notNullAndTrim(nome) );
													   soggetto.setRagiSoci( notNullAndTrim(denominazione) );
													   soggetto.setSesso( notNullAndTrim(sesso) );
													   soggetto.setTipoIndirizzoRes( notNullAndTrim(tipoIndirizzoRes) );
													   soggetto.setComuneRes( notNullAndTrim(comuneRes) );
													   soggetto.setProvinciaRes( notNullAndTrim(provinciaRes) );
													   soggetto.setIndirizzoRes( notNullAndTrim(indirizzoRes) );
													   soggetto.setCapRes( notNullAndTrim(capRes) );
													   soggetto.setSede( notNullAndTrim(sede));
													   /*
													    * Recupero la titolarita 
													    */
													   RicercaCompravenditeDTO rcTit = new RicercaCompravenditeDTO();
													   rcTit.setEnteId(belfiore);
													   rcTit.setTipoSoggetto(tipoSoggetto); //P = Fisico; G = Giuridico
													   rcTit.setIdentificativoSoggetto(identificativo);
													   rcTit.setIidNota(iidNota);
													   List<SoggettoCompravenditeDTO> lstTitolari = compravenditeService.getListaSoggettiNota(rcTit);	//Lista Titolarita della nota corrente
													   if (lstTitolari != null && lstTitolari.size()>0){
														   Iterator<SoggettoCompravenditeDTO> itTit = lstTitolari.iterator();
														   while(itTit.hasNext()){
															   SoggettoCompravenditeDTO tit = itTit.next();
															   if (tit != null){

																   soggetto.setFlagPersFisica( notNullAndTrim( tit.getTipoSoggetto() ) );
																   soggetto.setFlagTipoTitolContro( notNullAndTrim( tit.getFlagTipoTitolContro() ) );
																   soggetto.setFlagTipoTitolFavore( notNullAndTrim( tit.getFlagTipoTitolFavore() ) );
																   soggetto.setDiritto( notNullAndTrim( tit.getSfCodiceDiritto() ) );
																   
																   BigDecimal scp = new BigDecimal(0);
																   if (tit.getScQuotaDenominatore() != null && !tit.getScQuotaDenominatore().trim().equalsIgnoreCase("") && tit.getFlagTipoTitolContro() != null && 
																		   tit.getFlagTipoTitolContro().trim().equalsIgnoreCase("C")){
																	   BigDecimal num = new BigDecimal(tit.getScQuotaNumeratore()!=null?tit.getScQuotaNumeratore():"0");
																	   BigDecimal denom = new BigDecimal(tit.getScQuotaDenominatore());
																	   scp = num.divide( (denom.multiply(new BigDecimal(10)) ), BigDecimal.ROUND_HALF_EVEN );
																	   soggetto.setPercPoss( scp.setScale(2, BigDecimal.ROUND_HALF_EVEN).toString() );
																   }
																   
																   BigDecimal sfp = new BigDecimal(0);
																   if (tit.getSfQuotaDenominatore() != null && !tit.getSfQuotaDenominatore().trim().equalsIgnoreCase("") && 
																		   tit.getFlagTipoTitolFavore() != null && tit.getFlagTipoTitolFavore().trim().equalsIgnoreCase("F")){
																	   BigDecimal num = new BigDecimal(tit.getSfQuotaNumeratore()!=null?tit.getSfQuotaNumeratore():"0");
																	   BigDecimal denom = new BigDecimal(tit.getSfQuotaDenominatore());
																	   sfp = num.divide( (denom.multiply(new BigDecimal(10)) ), BigDecimal.ROUND_HALF_EVEN );
																	   soggetto.setPercPoss( sfp.setScale(2, BigDecimal.ROUND_HALF_EVEN).toString() );
																   }
															   }
														   }
													   }
													   lstSoggettiMui.add(soggetto);
												   }
											   }
											   compravendita.setLstSoggetti(lstSoggettiMui);
										   }
										   output.add(compravendita);
									   }
									   
									   responseDto.setLstObjs( output );
									   responseDto.setSuccess(true);
										
								   }else{
										String descrizione = "Note Compravendite non trovate con questi parametri!!!";
										responseDto.setSuccess(false);
										responseDto.setError(descrizione);
										System.out.println(descrizione);
								   }
							   }else{
									String descrizione = "Utente NON autorizzato ad accedere alle informazioni richieste!!!";
									responseDto.setSuccess(false);
									responseDto.setError(descrizione);
									System.out.println(descrizione);
								}
				   }else{
						String descrizione = "Autenticazione fallita. Credenziali GIT errate o scadute!!!";
						responseDto.setSuccess(false);
						responseDto.setError(descrizione);
						System.out.println(descrizione);
					}
			   }else{
				   String descrizione = "";
					if (authType != null && !authType.trim().equalsIgnoreCase("") && authType.trim().equalsIgnoreCase("TOKEN"))
						descrizione = "OTP private key NON VALIDATA!!!";
					if (authType != null && !authType.trim().equalsIgnoreCase("") && authType.startsWith("IP"))
						descrizione = "IP Remote Host NON VALIDO!!! " + remoteHost;
					responseDto.setSuccess(false);
					responseDto.setError(descrizione);
					System.out.println(descrizione);
			   }
			   if (conn != null)
				   conn.close();
			}
			catch (Exception t) {
				t.printStackTrace();
				String descrizione = t.getMessage();
				responseDto.setSuccess(false);
				responseDto.setError(descrizione);
				System.out.println(descrizione);
			}
		}else{
			String descrizione = "Utente e/o Codice Belfiore NON VALORIZZATI!!! ";
			responseDto.setSuccess(false);
			responseDto.setError(descrizione);
			System.out.println(descrizione);
		   }
		
		return responseDto;
	 }//------------------------------------------------------------------------

	
	@WebMethod
	public RespDocfaDTO getDocfaByParams(@WebParam(name = "belfiore") String belfiore, @WebParam(name = "utente") String utente, @WebParam(name = "ot_prik") String ot_prik,
			@WebParam(name = "sezione") String sezione, @WebParam(name = "foglio") String foglio, @WebParam(name = "particella") String particella, @WebParam(name = "subalterno") String subalterno,
			@WebParam(name = "protocollo") String protocollo, @WebParam(name = "dataFornitura") String dataFornitura){
		
		ArrayList<DocfaDTO> outputDocfa = new ArrayList<DocfaDTO>();
		RespDocfaDTO responseDto = new RespDocfaDTO();

		ElaborazioniService elaborazioniService = (ElaborazioniService) getEjb("CT_Service", "CT_Service_Data_Access", "ElaborazioniServiceBean");
		
		DocfaService docfaService = (DocfaService) getEjb("CT_Service", "CT_Service_Data_Access", "DocfaServiceBean");
		belfiore = notNullAndTrim(belfiore);
		utente = notNullAndTrim(utente);
		//pwd = notNullAndTrim(pwd);
		ot_prik = notNullAndTrim(ot_prik);
		sezione = notNullAndTrim(sezione);
		foglio = notNullAndTrim(foglio);
		particella = notNullAndTrim(particella);
		subalterno = notNullAndTrim(subalterno);
		/*
		 * Identificativo DOCFA e Fornitura
		 */
		
		protocollo = notNullAndTrim(protocollo);
		dataFornitura = notNullAndTrim(dataFornitura); //formato GG/MM/AAAA (sempre il giorno 01 del Mese)

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		if (!utente.trim().equalsIgnoreCase("") && !belfiore.trim().equalsIgnoreCase("")){
			
			try{

				String[] fonti = (String[])mappaTemiFonti.get("CATASTO");
				boolean autorizzato = false;
				
				Context cont = new InitialContext();
				DataSource theDataSource = (DataSource)cont.lookup("java:/AMProfiler");
				
			    Connection conn = theDataSource.getConnection();
			    /*
			     * Recupero del metodo di autenticazione scelto per le applicazioni 
			     * esterne
			     */
			    MessageContext msgx = wsContext.getMessageContext();
			    HttpServletRequest exchange = (HttpServletRequest)msgx.get(MessageContext.SERVLET_REQUEST);
			    String remoteHost = exchange.getRemoteAddr();
			    
			    String authType = this.getGitOutAuthenticationType(conn, belfiore);
			    Long idValid = 0l; 
			   if (authType != null && !authType.trim().equalsIgnoreCase("")){
				   if (authType.trim().equalsIgnoreCase("TOKEN")){
					   idValid = this.getOtpValidationByToken(conn, utente, belfiore, ot_prik);				   
				   }else if (authType.startsWith("IP")){
					   idValid = this.getOtpValidationByIp(conn, utente, belfiore, authType, remoteHost);
				   }
			   }
			   /*
			     * validazione OTP
			     */
			   if (idValid != null && idValid > 0){
				   /*
				    * Aggiorna stato OTP = USATA
				    */
				   this.setOtpValidation(conn, idValid);
				   
				   Boolean autenticato = this.gitAuthentication(conn, utente);
				  
				   if (autenticato){
							   for (int z=0; z<fonti.length; z++){
									
									List<String> enteList = new LinkedList<String>();
									enteList.add(  belfiore  );

									HashMap<String,String> listaPermessi = this.getPermissionOfUser(conn, utente, belfiore);

				//					TEST				
				//					listaPermessi = new HashMap<String, String>();
				//					listaPermessi.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:CATASTO", "permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:CATASTO");

									CeTUser user = new CeTUser();
									user.setPermList(listaPermessi);
						            user.setUsername(  utente  );
						            user.setEnteList(enteList);
						            user.setCurrentEnte(  belfiore );

									if (GestionePermessi.autorizzato(user, "diogene", ITEM_VISUALIZZA_FONTI_DATI, "Fonte:"+ fonti[z]))
										autorizzato = true;
								}
							   if (autorizzato){
								   RicercaOggettoDocfaDTO rod = new RicercaOggettoDocfaDTO();
								   rod.setEnteId(belfiore);
								   rod.setUserId(utente);
								   
								   rod.setSezione(sezione);
								   rod.setFoglio(foglio);
								   rod.setParticella(particella);
								   rod.setUnimm(subalterno);
								   
								   rod.setProtocollo(protocollo);
								   boolean checkformat = false;
									if (dataFornitura.matches("([0-9]{2})/([0-9]{2})/([0-9]{4})"))
									    checkformat=true;
									else
									   checkformat=false;
									if(checkformat){
										Date dateFor = formatter.parse(dataFornitura);
										GregorianCalendar gc = new GregorianCalendar();
										gc.setTime(dateFor);
										gc.set(Calendar.DAY_OF_MONTH, 1);
										rod.setFornitura(gc.getTime());
									}else{
										/*
										 * TODO Formato data errato
										 */
									}
									rod.setLimit(QRY_LIMIT);
								   
								   /*
								    * XXX 
								    * Step 1: Recupro i dati censuari o partendo da qualsiasi parametro
								    * sia stato passato
								    * Step 2: facendo distinct su coppia protocollo e fornitura si accede
								    * ai dati generali, dichiaranti e poi al resto del mondo
								    */
									//recupero Dati Censuari By Coord e protocollo + data fornitura 01/MM/AAAA									
									//List<ControlloClassamentoConsistenzaDTO> lstDatiCensuariValori = elaborazioniService.getDocfaDatiCensuariValori(rod);
									
								   List<DocfaDatiCensuari> lstDatiCensuariParams = docfaService.getDocfaDatiCensuari(rod);
								   
								   Hashtable<String, String> htDatiGeneraliDistinct = new Hashtable<String, String>(); 
								   if (lstDatiCensuariParams != null && lstDatiCensuariParams.size()>0){
									   Iterator<DocfaDatiCensuari> itDocDatCen = lstDatiCensuariParams.iterator();
									   while(itDocDatCen.hasNext()){
										   DocfaDatiCensuari ccc = itDocDatCen.next();
										   if (ccc != null){

											   String key = formatter.format(ccc.getFornitura()) + "_" + ccc.getProtocolloRegistrazione();
											   
											   if (htDatiGeneraliDistinct.containsKey(key)){
												   /*
												    * Dati Generali e Dati Dichiaranti per Fornitura + Protocollo già recuperati 
												    */
											   }else{
												   
												   htDatiGeneraliDistinct.put(key, key);
												   DocfaDTO docfaDto = new DocfaDTO();
												   
												   RicercaOggettoDocfaDTO rod2 = new RicercaOggettoDocfaDTO();
												   rod2.setEnteId(belfiore);
												   rod2.setUserId(utente);
												   rod2.setFornitura(ccc.getFornitura());
												   rod2.setProtocollo(ccc.getProtocolloRegistrazione());

												   DettaglioDocfaDTO docfa = docfaService.getDettaglioDocfa(rod2);
												   //XXX 1: Recupero Dati Generali By protocollo e fornitura
												   //List<DocfaDatiGenerali> lstDocfaDatiGenerali = docfaService.getDocfaDatiGenerali(rod2);
												   //Recupero Dati Generali By Coord
												   //List<DatiGeneraliDocfaDTO> lstDatiGeneraliDocfa = docfaService.getListaDatiDocfaImmobile(rod);
												   DocfaDatiGenerali ddg = docfa.getDatiGenerali();
												   DocfaDatoGeneraleDTO datoGeneraleDto = new DocfaDatoGeneraleDTO();
												   datoGeneraleDto.setAnnotazioni( notNullAndTrim(ddg.getAnnotazioni()) );
												   datoGeneraleDto.setCausale( notNullAndTrim(ddg.getCausaleNotaVax()) );
												   datoGeneraleDto.setCostituizione( ddg.getUiuInCostituzione()!=null?ddg.getUiuInCostituzione().setScale(0, BigDecimal.ROUND_HALF_EVEN).toString():"" );
												   datoGeneraleDto.setDataVariazione( ddg.getDataVariazione()!=null?formatter.format(ddg.getDataVariazione()):"" );
												   datoGeneraleDto.setFornitura( ddg.getFornitura()!=null?formatter.format(ddg.getFornitura()):"" );
												   datoGeneraleDto.setProtocollo( notNullAndTrim(protocollo) );
												   datoGeneraleDto.setSoppressione( ddg.getUiuInSoppressione()!=null?ddg.getUiuInSoppressione().setScale(0, BigDecimal.ROUND_HALF_EVEN).toString():"" );
												   datoGeneraleDto.setVariazione( ddg.getUiuInVariazione()!=null?ddg.getUiuInVariazione().setScale(0, BigDecimal.ROUND_HALF_EVEN).toString():"" );
												   
												   List<DocfaInParteUnoDTO> lstParteUno = docfaService.getListaDocfaInParteUno(rod2);
												   if (lstParteUno != null && lstParteUno.size()>0){
													   DocfaInParteUnoDTO parteUno = lstParteUno.get(0);
													   datoGeneraleDto.setAnnoCostruzione(parteUno.getDocfaInParteUno().getAnnoCostru().toString());
													   datoGeneraleDto.setAnnoRistrutturazione(parteUno.getDocfaInParteUno().getAnnoRistru().toString());
												   }
												   docfaDto.setDatoGenerale(datoGeneraleDto);
												   /*
												    * Dati Dichiaranti per Fornitura + Protocollo da recuperare
												    */
												   //Recupero Dichiaranti By protocollo e fornitura
												   //List<DocfaDichiaranti> lstDichiaranti = docfaService.getDichiaranti(rod);
												   List<DocfaDichiaranti> lstDd = docfa.getDichiaranti();
												   List<DocfaDichiaranteDTO> dichiarantiLst = new ArrayList<DocfaDichiaranteDTO>();
												   if (lstDd != null && lstDd.size()>0){
													   Iterator<DocfaDichiaranti> itDd = lstDd.iterator();
													   while(itDd.hasNext()){
														   DocfaDichiaranti dd = itDd.next();

														   DocfaDichiaranteDTO dichiaranteDto = new DocfaDichiaranteDTO();
														   dichiaranteDto.setCognome( notNullAndTrim(dd.getDicCognome()) );
														   dichiaranteDto.setNome( notNullAndTrim(dd.getDicNome()) );
														   dichiaranteDto.setIndirizzoRes( notNullAndTrim(dd.getDicResIndir()) );
														   dichiaranteDto.setCapRes( notNullAndTrim(dd.getDicResCap()) );
														   dichiaranteDto.setCivicoRes( notNullAndTrim(dd.getDicResNumciv()) );
														   dichiaranteDto.setComuneRes( notNullAndTrim(dd.getDicResCom()) );
														   dichiaranteDto.setProvinciaRes( notNullAndTrim(dd.getDicResProv()) );
														   
														   dichiarantiLst.add(dichiaranteDto);
													   }
												   }
												   docfaDto.setDichiarantiLst(dichiarantiLst);
												   /*
												    * Recupero Uiu By protocollo e fornitura
												    */
												   List<DocfaUiu> lstUiu = docfa.getListaUiu();
												   List<DocfaUnitaImmobiliareDTO> unitaImmoLst = new ArrayList<DocfaUnitaImmobiliareDTO>();
												   if (lstUiu != null && lstUiu.size()>0){
													   Iterator<DocfaUiu> itUiu = lstUiu.iterator();
													   while(itUiu.hasNext()){
														   DocfaUiu du = itUiu.next();
														   
														   DocfaUnitaImmobiliareDTO unitaImmoDto = new DocfaUnitaImmobiliareDTO();
														   unitaImmoDto.setFoglio( notNullAndTrim(du.getFoglio()) );
														   unitaImmoDto.setIndirizzo( notNullAndTrim(notNullAndTrim(du.getIndirToponimo()) + " " + notNullAndTrim(du.getIndirNcivUno()) + " " + notNullAndTrim(du.getIndirNcivDue()) + " " + notNullAndTrim(du.getIndirNcivTre())) );
														   unitaImmoDto.setNumero( notNullAndTrim(du.getNumero()) );
														   unitaImmoDto.setSubalterno( notNullAndTrim(du.getSubalterno()) );
														   unitaImmoDto.setTipoOp( notNullAndTrim(du.getTipoOperazione()) );
														   
														   unitaImmoLst.add(unitaImmoDto);
													   }
												   }
												   docfaDto.setUnitaImmobilliariLst(unitaImmoLst);
												   /*
												    * TODO Valorizzo Dato censuario già recuperato in precedenza
												    */
												   List<DocfaDatiCensuariDTO> lstDatiCensuari = docfa.getDatiCensuari();
												   List<DocfaCensuarioValoreDTO> datiCensuariValoriLst = new ArrayList<DocfaCensuarioValoreDTO>();
												   if (lstDatiCensuari != null && lstDatiCensuari.size()>0){
													   Iterator<DocfaDatiCensuariDTO> itDc = lstDatiCensuari.iterator();
													   while(itDc.hasNext()){
														   DocfaDatiCensuariDTO ddc = itDc.next();

														   DocfaDatiCensuari dc = ddc.getDatiCensuari();

														   DocfaCensuarioValoreDTO censuarioValoreDto = new DocfaCensuarioValoreDTO(); 
														   censuarioValoreDto.setCategoria( notNullAndTrim(dc.getCategoria()) );
														   censuarioValoreDto.setClasse( notNullAndTrim(dc.getClasse()) );
														   censuarioValoreDto.setConsistenza( notNullAndTrim(dc.getConsistenza()) );
														   censuarioValoreDto.setFoglio( notNullAndTrim(dc.getFoglio()) );
														   censuarioValoreDto.setIdentificativoImmobile( notNullAndTrim(dc.getId().getIdentificativoImmobile()) );
														   censuarioValoreDto.setNumero( notNullAndTrim(dc.getNumero()) );
														   censuarioValoreDto.setRendita( notNullAndTrim(dc.getRenditaEuro()) );
														   censuarioValoreDto.setSubalterno( notNullAndTrim(dc.getSubalterno()) );
														   censuarioValoreDto.setSuperficie( notNullAndTrim(dc.getSuperficie()) );
														   censuarioValoreDto.setZona( notNullAndTrim(dc.getZona()) );
														   /*
														    * Calcolo del valore commerciale 
														    */
														   RicercaOggettoDocfaDTO rod3 = new RicercaOggettoDocfaDTO();
														   rod3.setEnteId(belfiore);
														   rod3.setUserId(utente);
														   rod3.setFornitura(ccc.getFornitura());
														   rod3.setProtocollo(ccc.getProtocolloRegistrazione());
														   rod3.setFoglio(ccc.getFoglio());
														   rod3.setParticella(ccc.getNumero());
														   rod3.setUnimm(ccc.getSubalterno());
														   /*
														    * Recupero modalita calcolo valore commerciale
														    * da variabile di AM_KEY_VALUE_EXT
														    */
														   String commValueCalcType = this.getCommValueCalcType(conn, belfiore);
														   rod3.setTipoOperDocfa( commValueCalcType );
														   ControlloClassamentoConsistenzaDTO controlloClassamentoConsistenza = elaborazioniService.getControlliClassConsistenzaByDocfaUiu( rod3 );
														   if (controlloClassamentoConsistenza != null)
															   censuarioValoreDto.setValoreCommerciale( controlloClassamentoConsistenza.getValoreCommerciale()!=null?controlloClassamentoConsistenza.getValoreCommerciale().toString():"" );
														   else
															   censuarioValoreDto.setValoreCommerciale("");
														   
														   datiCensuariValoriLst.add(censuarioValoreDto);
													   }
												   }
												   docfaDto.setDatiCensuariLst(datiCensuariValoriLst);

												   outputDocfa.add(docfaDto);
											   }
										   }
									   }
									   
									   responseDto.setLstObjs( outputDocfa );
									   responseDto.setSuccess(true);
									   
								   }
							   }else{
									String descrizione = "Utente NON autorizzato ad accedere alle informazioni richieste!!!";
									responseDto.setSuccess(false);
									responseDto.setError(descrizione);
									System.out.println(descrizione);
									
								}
				   }else{
						String descrizione = "Autenticazione fallita. Credenziali GIT errate o scadute!!!";
						responseDto.setSuccess(false);
						responseDto.setError(descrizione);
						System.out.println(descrizione);
					}
			   }else{
				   String descrizione = "Autenticazione fallita. Credenziali GIT errate o scadute!!!";
					if (authType != null && !authType.trim().equalsIgnoreCase("") && authType.trim().equalsIgnoreCase("TOKEN"))
						descrizione = "OTP private key NON VALIDATA!!!";
					if (authType != null && !authType.trim().equalsIgnoreCase("") && authType.startsWith("IP"))
						descrizione = "IP Remote Host NON VALIDO!!! " + remoteHost;
					responseDto.setSuccess(false);
					responseDto.setError(descrizione);
					System.out.println(descrizione);
			   }
			   if (conn != null)
				   conn.close();
			}
			catch (Exception t) {
				t.printStackTrace();
				String descrizione = t.getMessage();
				responseDto.setSuccess(false);
				responseDto.setError(descrizione);
				System.out.println(descrizione);
			}
		}else{
			String descrizione = "Utente e/o Codice Belfiore NON VALORIZZATI!!! ";
			responseDto.setSuccess(false);
			responseDto.setError(descrizione);
			System.out.println(descrizione);
		   }
		
		return responseDto;
	 }//------------------------------------------------------------------------

	@WebMethod
	public RespElettricheFornitureDTO getElettricByParams(@WebParam(name = "belfiore") String belfiore, @WebParam(name = "utente") String utente, @WebParam(name = "ot_prik") String ot_prik,
			@WebParam(name = "sezione") String sezione, @WebParam(name = "foglio") String foglio, @WebParam(name = "particella") String particella, @WebParam(name = "subalterno") String subalterno,
			@WebParam(name = "identificativo") String identificativo, @WebParam(name = "tipologia") String tipologia, 
			@WebParam(name = "tipoArea") String tipoArea, @WebParam(name = "nomeVia") String nomeVia, @WebParam(name = "civico") String civico){
		
		ArrayList<ElettricaFornituraDTO> outputEnel = new ArrayList<ElettricaFornituraDTO>();
		RespElettricheFornitureDTO responseDto = new RespElettricheFornitureDTO();

		FornitureElettricheService fornitureElettricheService = (FornitureElettricheService) getEjb("CT_Service", "CT_Service_Data_Access", "FornitureElettricheServiceBean");
		
		belfiore = notNullAndTrim(belfiore);
		utente = notNullAndTrim(utente);
		//pwd = notNullAndTrim(pwd);
		ot_prik = notNullAndTrim(ot_prik);
		
		sezione = notNullAndTrim(sezione);
		foglio = notNullAndTrim(foglio);
		particella = notNullAndTrim(particella);
		subalterno = notNullAndTrim(subalterno);

		identificativo = notNullAndTrim(identificativo);//codice fiscale o partita iva
		tipologia = notNullAndTrim(tipologia);			//P= fisica; G= giuridica

		tipoArea = notNullAndTrim(tipoArea);
		nomeVia = notNullAndTrim(nomeVia);
		civico = notNullAndTrim(civico);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		if (!utente.trim().equalsIgnoreCase("") && !belfiore.trim().equalsIgnoreCase("")){
			
			try{

				String[] fonti = (String[])mappaTemiFonti.get("CATASTO");
				boolean autorizzato = false;
				
				Context cont = new InitialContext();
				DataSource theDataSource = (DataSource)cont.lookup("java:/AMProfiler");
				
			    Connection conn = theDataSource.getConnection();
			    /*
			     * Recupero del metodo di autenticazione scelto per le applicazioni 
			     * esterne
			     */
			    MessageContext msgx = wsContext.getMessageContext();
			    HttpServletRequest exchange = (HttpServletRequest)msgx.get(MessageContext.SERVLET_REQUEST);
			    String remoteHost = exchange.getRemoteAddr();
			    
			    String authType = this.getGitOutAuthenticationType(conn, belfiore);
			    Long idValid = 0l; 
			   if (authType != null && !authType.trim().equalsIgnoreCase("")){
				   if (authType.trim().equalsIgnoreCase("TOKEN")){
					   idValid = this.getOtpValidationByToken(conn, utente, belfiore, ot_prik);				   
				   }else if (authType.startsWith("IP")){
					   idValid = this.getOtpValidationByIp(conn, utente, belfiore, authType, remoteHost);
				   }
			   }
			   /*
			     * validazione OTP
			     */
			   if (idValid != null && idValid > 0){
				   /*
				    * Aggiorna stato OTP = USATA
				    */
				   this.setOtpValidation(conn, idValid);
				   
				   Boolean autenticato = this.gitAuthentication(conn, utente);
				  
				   if (autenticato){
							   for (int z=0; z<fonti.length; z++){
									
									List<String> enteList = new LinkedList<String>();
									enteList.add(  belfiore  );

									HashMap<String,String> listaPermessi = this.getPermissionOfUser(conn, utente, belfiore);

				//					TEST				
				//					listaPermessi = new HashMap<String, String>();
				//					listaPermessi.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:CATASTO", "permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:CATASTO");

									CeTUser user = new CeTUser();
									user.setPermList(listaPermessi);
						            user.setUsername(  utente  );
						            user.setEnteList(enteList);
						            user.setCurrentEnte(  belfiore );

									if (GestionePermessi.autorizzato(user, "diogene", ITEM_VISUALIZZA_FONTI_DATI, "Fonte:"+ fonti[z]))
										autorizzato = true;
								}
							   if (autorizzato){
								   FornituraElettricaDTO enel = new FornituraElettricaDTO();
								   enel.setUserId(utente);
								   enel.setEnteId(belfiore);
								   
								   enel.setSezione(sezione);
								   enel.setFoglio(foglio);
								   enel.setParticella(particella);
								   enel.setSubalterno(subalterno);
								   
								   enel.setTipoArea(tipoArea);
								   enel.setTipologia(tipologia);
								   enel.setIdentificativo(identificativo);
								   enel.setVia(nomeVia);
								   enel.setCivico(civico);
								   //enel.setSessionId();
								   
								   enel.setLimit(QRY_LIMIT);

//								   List<SitEnelUtenza> lstUtenze = fornitureElettricheService.getUtenzeByParams(enel);
//								   if (lstUtenze != null && lstUtenze.size()>0){
//									   System.out.println("Utenze recuperate n. " + lstUtenze.size());
//								   }else
//									   System.out.println("Utenze recuperate n. 0 ");
//
//								   List<SitEnelUtente> lstUtenti = fornitureElettricheService.getUtentiByDatiAnag(enel);
//								   if (lstUtenti != null && lstUtenti.size()>0){
//									   System.out.println("Utenti recuperati n. " + lstUtenti.size());
//								   }else
//									   System.out.println("Utenti recuperate n. 0 ");

								   List<Object[]> lstForniture = fornitureElettricheService.getFornitureElettricheByParams(enel);
								   /*
								    * Questo elenco di forniture è ordinato per denominazione e identificativo soggetto
								    */
								   if (lstForniture != null && lstForniture.size()>0){
									   System.out.println("Forniture recuperate n. " + lstForniture.size());
									   SitEnelUtente utenteCor = null;
									   ArrayList<ElettricaUtenzaDTO> alFornitureDto = null;
									   for (int index=0; index<lstForniture.size(); index++){
										   
										   Object[] objs = lstForniture.get(index);
										   SitEnelUtenza iUtenza = (SitEnelUtenza)objs[0];
										   SitEnelUtente iUtente = (SitEnelUtente)objs[1];
										   if (iUtenza != null && iUtente != null){

											   if (index == 0){
												   utenteCor = new SitEnelUtente();
												   utenteCor.setDenominazione(iUtente.getDenominazione());
												   utenteCor.setCodiceFiscale(iUtente.getCodiceFiscale());
												   utenteCor.setSesso( iUtente.getSesso() );

												   alFornitureDto = new ArrayList<ElettricaUtenzaDTO>();
											   }
											   
											   if (utenteCor.getCodiceFiscale().trim().equalsIgnoreCase(iUtente.getCodiceFiscale().trim())){
												   /*
												    * Valorizzo ed aggiungo l'oggetto utenza alla lista delle 
												    * utenze dell'utente corrente
												    */
												   ElettricaUtenzaDTO utenzaDto = new ElettricaUtenzaDTO();
												   utenzaDto.setAnnoRiferimento(iUtenza.getAnnoRiferimentoDati());
												   utenzaDto.setCap(iUtenza.getCapUbicazione());
												   utenzaDto.setCodiceUtenza(iUtenza.getCodiceUtenza());
												   utenzaDto.setFoglio(iUtenza.getFoglio());
												   utenzaDto.setIndirizzo(iUtenza.getIndirizzoUbicazione());
												   utenzaDto.setKwh( iUtenza.getKwhFatturati()!=null?iUtenza.getKwhFatturati().setScale(0, BigDecimal.ROUND_HALF_EVEN).toString():"" );
												   utenzaDto.setMesi( iUtenza.getMesiFatturazione()!=null?iUtenza.getMesiFatturazione().setScale(0, BigDecimal.ROUND_HALF_EVEN).toString():"" );
												   utenzaDto.setNumero(iUtenza.getParticella());
												   utenzaDto.setSpesaAnnua( iUtenza.getSpesaAnnua()!=null?iUtenza.getSpesaAnnua().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString():"" );
												   utenzaDto.setTipoUtenza(iUtenza.getTipoUtenza());
												   alFornitureDto.add(utenzaDto);
											   }else{
												   /*
												    * 1. Creo e valorizzo oggetto per output
												    * 2. Aggiungo alla lista di output l'oggetto creato
												    * 3. Il nuovo utente diventa utente corrente
												    * 4. Instanzio la nuova lista delle forniture per il nuovo utente aggiungendo subito l'utenza corrente
												    */
												   ElettricaFornituraDTO fornituraDto = new ElettricaFornituraDTO();
												   ElettricaUtenteDTO utenteDto = new ElettricaUtenteDTO();
												   utenteDto.setDenominazione(utenteCor.getDenominazione());
												   utenteDto.setCodiceFiscale(utenteCor.getCodiceFiscale());
												   utenteDto.setSesso(utenteCor.getSesso());
												   fornituraDto.setUtente(utenteDto);
												   fornituraDto.setLstUtenze(alFornitureDto);
												   outputEnel.add(fornituraDto);
												   /*
												    * Break per passaggio a nuovo utente
												    */
												   utenteCor = new SitEnelUtente();
												   utenteCor.setDenominazione(iUtente.getDenominazione());
												   utenteCor.setCodiceFiscale(iUtente.getCodiceFiscale());
												   utenteCor.setSesso( iUtente.getSesso() );
												   alFornitureDto = new ArrayList<ElettricaUtenzaDTO>();
												   ElettricaUtenzaDTO utenzaDto = new ElettricaUtenzaDTO();
												   utenzaDto.setAnnoRiferimento(iUtenza.getAnnoRiferimentoDati());
												   utenzaDto.setCap(iUtenza.getCapUbicazione());
												   utenzaDto.setCodiceUtenza(iUtenza.getCodiceUtenza());
												   utenzaDto.setFoglio(iUtenza.getFoglio());
												   utenzaDto.setIndirizzo(iUtenza.getIndirizzoUbicazione());
												   utenzaDto.setKwh( iUtenza.getKwhFatturati()!=null?iUtenza.getKwhFatturati().setScale(0, BigDecimal.ROUND_HALF_EVEN).toString():"" );
												   utenzaDto.setMesi( iUtenza.getMesiFatturazione()!=null?iUtenza.getMesiFatturazione().setScale(0, BigDecimal.ROUND_HALF_EVEN).toString():"" );
												   utenzaDto.setNumero(iUtenza.getParticella());
												   utenzaDto.setSpesaAnnua( iUtenza.getSpesaAnnua()!=null?iUtenza.getSpesaAnnua().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString():"" );
												   utenzaDto.setTipoUtenza(iUtenza.getTipoUtenza());
												   alFornitureDto.add(utenzaDto);
											   }

										   }else{
											   System.out.println("Fornitura con indice " + index + " ha utente o utenza nulli!!!");
										   }
									   }
									   /*
									    * Qui devo aggiungere l'ultimo elemento che rimane escluso dal loop
									    * precedente
									    */
									   ElettricaFornituraDTO fornituraDto = new ElettricaFornituraDTO();
									   ElettricaUtenteDTO utenteDto = new ElettricaUtenteDTO();
									   utenteDto.setDenominazione(utenteCor.getDenominazione());
									   utenteDto.setCodiceFiscale(utenteCor.getCodiceFiscale());
									   utenteDto.setSesso(utenteCor.getSesso());
									   fornituraDto.setUtente(utenteDto);
									   fornituraDto.setLstUtenze(alFornitureDto);
									   outputEnel.add(fornituraDto);
									   
									   responseDto.setLstObjs( outputEnel );
									   responseDto.setSuccess(true);
									   
								   }else
									   System.out.println("Forniture recuperate n. 0 ");
							   }else{
									String descrizione = "Utente NON autorizzato ad accedere alle informazioni richieste!!!";
									responseDto.setSuccess(false);
									responseDto.setError(descrizione);
									System.out.println(descrizione);
								}
				   }else{
						String descrizione = "Autenticazione fallita. Credenziali GIT errate o scadute!!!";
						responseDto.setSuccess(false);
						responseDto.setError(descrizione);
						System.out.println(descrizione);
					}
			   }else{
				   String descrizione = "Utente e/o Codice Belfiore NON VALORIZZATI!!! ";
					
					if (authType != null && !authType.trim().equalsIgnoreCase("") && authType.trim().equalsIgnoreCase("TOKEN"))
						descrizione = "OTP private key NON VALIDATA!!!";
					if (authType != null && !authType.trim().equalsIgnoreCase("") && authType.startsWith("IP"))
						descrizione = "IP Remote Host NON VALIDO!!! " + remoteHost;
					responseDto.setSuccess(false);
					responseDto.setError(descrizione);
					System.out.println(descrizione);
				   
			   }
			   if (conn != null)
				   conn.close();
			}
			catch (Exception t) {
				t.printStackTrace();
				String descrizione = t.getMessage();
				responseDto.setSuccess(false);
				responseDto.setError(descrizione);
				System.out.println(descrizione);
			}
		}else{
			String descrizione = "Utente e/o Codice Belfiore NON VALORIZZATI!!! ";
			responseDto.setSuccess(false);
			responseDto.setError(descrizione);
			System.out.println(descrizione);
		   }
		
		
		return responseDto;
	 }//------------------------------------------------------------------------	
	
	@WebMethod
	public RespGasFornitureDTO getGasByParams(@WebParam(name = "belfiore") String belfiore, @WebParam(name = "utente") String utente, @WebParam(name = "ot_prik") String ot_prik,
			@WebParam(name = "identificativo") String identificativo, @WebParam(name = "tipologia") String tipologia, 
			@WebParam(name = "tipoArea") String tipoArea, @WebParam(name = "nomeVia") String nomeVia, @WebParam(name = "civico") String civico){
		
		ArrayList<GasFornituraDTO> outputGas = new ArrayList<GasFornituraDTO>();
		RespGasFornitureDTO responseDto = new RespGasFornitureDTO();
		
		FornitureGasService fornitureGasService = (FornitureGasService) getEjb("CT_Service", "CT_Service_Data_Access", "FornitureGasServiceBean");
		
		belfiore = notNullAndTrim(belfiore);
		utente = notNullAndTrim(utente);
		//pwd = notNullAndTrim(pwd);
		ot_prik = notNullAndTrim(ot_prik);
		
		identificativo = notNullAndTrim(identificativo);//codice fiscale o partita iva
		tipologia = notNullAndTrim(tipologia);			//P= fisica; G= giuridica

		tipoArea = notNullAndTrim(tipoArea);
		nomeVia = notNullAndTrim(nomeVia);
		civico = notNullAndTrim(civico);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		if (!utente.trim().equalsIgnoreCase("") && !belfiore.trim().equalsIgnoreCase("")){
			
			try{

				String[] fonti = (String[])mappaTemiFonti.get("CATASTO");
				boolean autorizzato = false;
				
				Context cont = new InitialContext();
				DataSource theDataSource = (DataSource)cont.lookup("java:/AMProfiler");
				
			    Connection conn = theDataSource.getConnection();
			    /*
			     * Recupero del metodo di autenticazione scelto per le applicazioni 
			     * esterne
			     */
			    MessageContext msgx = wsContext.getMessageContext();
			    HttpServletRequest exchange = (HttpServletRequest)msgx.get(MessageContext.SERVLET_REQUEST);
			    String remoteHost = exchange.getRemoteAddr();
			    
			    String authType = this.getGitOutAuthenticationType(conn, belfiore);
			    Long idValid = 0l; 
			   if (authType != null && !authType.trim().equalsIgnoreCase("")){
				   if (authType.trim().equalsIgnoreCase("TOKEN")){
					   idValid = this.getOtpValidationByToken(conn, utente, belfiore, ot_prik);				   
				   }else if (authType.startsWith("IP")){
					   idValid = this.getOtpValidationByIp(conn, utente, belfiore, authType, remoteHost);
				   }
			   }
			   /*
			     * validazione OTP
			     */
			   if (idValid != null && idValid > 0){
				   /*
				    * Aggiorna stato OTP = USATA
				    */
				   this.setOtpValidation(conn, idValid);
				   
				   Boolean autenticato = this.gitAuthentication(conn, utente);
				  
				   if (autenticato){
							   for (int z=0; z<fonti.length; z++){
									
									List<String> enteList = new LinkedList<String>();
									enteList.add(  belfiore  );

									HashMap<String,String> listaPermessi = this.getPermissionOfUser(conn, utente, belfiore);

				//					TEST				
				//					listaPermessi = new HashMap<String, String>();
				//					listaPermessi.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:CATASTO", "permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:CATASTO");

									CeTUser user = new CeTUser();
									user.setPermList(listaPermessi);
						            user.setUsername(  utente  );
						            user.setEnteList(enteList);
						            user.setCurrentEnte(  belfiore );

									if (GestionePermessi.autorizzato(user, "diogene", ITEM_VISUALIZZA_FONTI_DATI, "Fonte:"+ fonti[z]))
										autorizzato = true;
								}
							   if (autorizzato){
								   FornituraGasDTO gas = new FornituraGasDTO();
								   gas.setUserId(utente);
								   gas.setEnteId(belfiore);
								   
								   gas.setTipoArea(tipoArea);
								   gas.setTipologia(tipologia);
								   gas.setIdentificativo(identificativo);
								   gas.setVia(nomeVia);
								   gas.setCivico(civico);
								   //enel.setSessionId();
								   
								   gas.setLimit(QRY_LIMIT);

//								   List<SitUGas> lstUtenze = fornitureGasService.getUtenzeByParams(gas);
//								   if (lstUtenze != null && lstUtenze.size()>0){
//									   System.out.println("Utenze recuperate n. " + lstUtenze.size());
//								   }else
//									   System.out.println("Utenze recuperate n. 0 ");

//								   List<SitUGas> lstUtenti = fornitureGasService.getUtentiByDatiAnag(gas);
//								   if (lstUtenti != null && lstUtenti.size()>0){
//									   System.out.println("Utenti recuperati n. " + lstUtenti.size());
//									   /*
//									    * TODO effettuare la distinct dei soggetti visto che interessa solo quello
//									    */
//								   }else
//									   System.out.println("Utenti recuperate n. 0 ");

								   List<SitUGas> lstForniture = fornitureGasService.getFornitureGasByParams(gas);
								   /*
								    * Questo elenco di forniture è ordinato per UTENZE.cognomeUtente, UTENZE.ragioneSociale, UTENZE.cfTitolareUtenza, UTENZE.nomeUtente, UTENZE.annoRiferimento, UTENZE.identificativoUtenza
								    */
								   if (lstForniture != null && lstForniture.size()>0){
									   System.out.println("Forniture recuperate n. " + lstForniture.size());
									   SitUGas sitUGasCor = null;
									   ArrayList<GasUtenzaDTO> alUtenzeDto = null;
									   for (int index=0; index<lstForniture.size(); index++){
										   
										   SitUGas iSitUGas = lstForniture.get(index);

										   if (iSitUGas != null){

											   if (index == 0){
												   sitUGasCor = new SitUGas();
												   sitUGasCor.setTipoSoggetto(iSitUGas.getTipoSoggetto());
												   sitUGasCor.setRagioneSociale(iSitUGas.getRagioneSociale());
												   sitUGasCor.setCfTitolareUtenza(iSitUGas.getCfTitolareUtenza());
												   sitUGasCor.setCognomeUtente(iSitUGas.getCognomeUtente());
												   sitUGasCor.setNomeUtente(iSitUGas.getNomeUtente());
												   sitUGasCor.setDescComuneNascita(iSitUGas.getDescComuneNascita());
												   sitUGasCor.setSiglaProvNascita(iSitUGas.getSiglaProvNascita());
												   sitUGasCor.setDataNascita(iSitUGas.getDataNascita());
												   sitUGasCor.setSesso( iSitUGas.getSesso() );

												   alUtenzeDto = new ArrayList<GasUtenzaDTO>();
											   }
											   
											   if (sitUGasCor.getCfTitolareUtenza().trim().equalsIgnoreCase(iSitUGas.getCfTitolareUtenza().trim())){
												   /*
												    * Valorizzo ed aggiungo l'oggetto utenza alla lista delle 
												    * utenze dell'utente corrente
												    */
												   GasUtenzaDTO utenzaDto = new GasUtenzaDTO();
												   utenzaDto.setAnnoRiferimento(iSitUGas.getAnnoRiferimento());
												   utenzaDto.setCap(iSitUGas.getCapUtenza());
												   utenzaDto.setCodiceUtenza(iSitUGas.getIdentificativoUtenza());
												   utenzaDto.setIndirizzo(iSitUGas.getIndirizzoUtenza());
												   utenzaDto.setMesi( iSitUGas.getNMesiFatturazione() );
												   utenzaDto.setSpesaAnnua( iSitUGas.getSpesaConsumoNettoIva() );
												   utenzaDto.setTipoUtenza(iSitUGas.getTipoUtenza());
												   utenzaDto.setTipoFornitura(iSitUGas.getTipologiaFornitura());
												   alUtenzeDto.add(utenzaDto);
											   }else{
												   /*
												    * 1. Creo e valorizzo oggetto per output
												    * 2. Aggiungo alla lista di output l'oggetto creato
												    * 3. Il nuovo utente diventa utente corrente
												    * 4. Instanzio la nuova lista delle forniture per il nuovo utente aggiungendo subito l'utenza corrente
												    */
												   GasFornituraDTO fornituraDto = new GasFornituraDTO();
												   GasUtenteDTO utenteDto = new GasUtenteDTO();
												   utenteDto.setTipoSoggetto(sitUGasCor.getTipoSoggetto());
												   utenteDto.setCognome( iSitUGas.getCognomeUtente() );
												   utenteDto.setNome( iSitUGas.getNomeUtente() );
												   utenteDto.setDenominazione(sitUGasCor.getRagioneSociale());
												   utenteDto.setCodiceFiscale(sitUGasCor.getCfTitolareUtenza());
												   utenteDto.setSesso(sitUGasCor.getSesso());
												   utenteDto.setLuogoNascita(iSitUGas.getDescComuneNascita());
												   utenteDto.setProvNascita(iSitUGas.getSiglaProvNascita());
												   utenteDto.setDataNascita(iSitUGas.getDataNascita());
												   utenteDto.setSesso( iSitUGas.getSesso() );
												   fornituraDto.setUtente(utenteDto);
												   fornituraDto.setLstUtenze(alUtenzeDto);
												   outputGas.add(fornituraDto);
												   /*
												    * Break per passaggio a nuovo utente
												    */
												   sitUGasCor = new SitUGas();
												   sitUGasCor.setTipoSoggetto(iSitUGas.getTipoSoggetto());
												   sitUGasCor.setRagioneSociale(iSitUGas.getRagioneSociale());
												   sitUGasCor.setCfTitolareUtenza(iSitUGas.getCfTitolareUtenza());
												   sitUGasCor.setCognomeUtente(iSitUGas.getCognomeUtente());
												   sitUGasCor.setNomeUtente(iSitUGas.getNomeUtente());
												   sitUGasCor.setDescComuneNascita(iSitUGas.getDescComuneNascita());
												   sitUGasCor.setSiglaProvNascita(iSitUGas.getSiglaProvNascita());
												   sitUGasCor.setDataNascita(iSitUGas.getDataNascita());
												   sitUGasCor.setSesso( iSitUGas.getSesso() );
												   
												   alUtenzeDto = new ArrayList<GasUtenzaDTO>();
												   GasUtenzaDTO utenzaDto = new GasUtenzaDTO();
												   utenzaDto.setAnnoRiferimento(iSitUGas.getAnnoRiferimento());
												   utenzaDto.setCap(iSitUGas.getCapUtenza());
												   utenzaDto.setCodiceUtenza(iSitUGas.getIdentificativoUtenza());
												   utenzaDto.setIndirizzo(iSitUGas.getIndirizzoUtenza());
												   utenzaDto.setMesi( iSitUGas.getNMesiFatturazione() );
												   utenzaDto.setSpesaAnnua( iSitUGas.getSpesaConsumoNettoIva() );
												   utenzaDto.setTipoUtenza(iSitUGas.getTipoUtenza());
												   utenzaDto.setTipoFornitura(iSitUGas.getTipologiaFornitura());
												   alUtenzeDto.add(utenzaDto);
											   }

										   }else{
											   System.out.println("Fornitura con indice " + index + " nulla!!!");
										   }
									   }
									   /*
									    * Qui devo aggiungere l'ultimo elemento che rimane escluso dal loop
									    * precedente
									    */
									   GasFornituraDTO fornituraDto = new GasFornituraDTO();
									   GasUtenteDTO utenteDto = new GasUtenteDTO();
									   utenteDto.setTipoSoggetto(sitUGasCor.getTipoSoggetto());
									   utenteDto.setCognome( sitUGasCor.getCognomeUtente() );
									   utenteDto.setNome( sitUGasCor.getNomeUtente() );
									   utenteDto.setDenominazione(sitUGasCor.getRagioneSociale());
									   utenteDto.setCodiceFiscale(sitUGasCor.getCfTitolareUtenza());
									   utenteDto.setSesso(sitUGasCor.getSesso());
									   utenteDto.setLuogoNascita(sitUGasCor.getDescComuneNascita());
									   utenteDto.setProvNascita(sitUGasCor.getSiglaProvNascita());
									   utenteDto.setDataNascita(sitUGasCor.getDataNascita());
									   utenteDto.setSesso( sitUGasCor.getSesso() );
									   fornituraDto.setUtente(utenteDto);
									   fornituraDto.setLstUtenze(alUtenzeDto);
									   outputGas.add(fornituraDto);
									   
									   
									   responseDto.setLstObjs( outputGas );
									   responseDto.setSuccess(true);
									   
									   
								   }else
									   System.out.println("Forniture recuperate n. 0 ");
								   
							   }else{
									String descrizione = "Utente NON autorizzato ad accedere alle informazioni richieste!!!";
									responseDto.setSuccess(false);
									responseDto.setError(descrizione);
									System.out.println(descrizione);
								}
				   }else{
						String descrizione = "Autenticazione fallita. Credenziali GIT errate o scadute!!!";
						responseDto.setSuccess(false);
						responseDto.setError(descrizione);
						System.out.println(descrizione);
					}
			   }else{
				   String descrizione = "Utente e/o Codice Belfiore NON VALORIZZATI!!! ";
					if (authType != null && !authType.trim().equalsIgnoreCase("") && authType.trim().equalsIgnoreCase("TOKEN"))
						descrizione = "OTP private key NON VALIDATA!!!";
					if (authType != null && !authType.trim().equalsIgnoreCase("") && authType.startsWith("IP"))
						descrizione = "IP Remote Host NON VALIDO!!! " + remoteHost;
					responseDto.setSuccess(false);
					responseDto.setError(descrizione);
					System.out.println(descrizione);
				   
			   }
			   if (conn != null)
				   conn.close();
			}
			catch (Exception t) {
				t.printStackTrace();
				String descrizione = t.getMessage();
				responseDto.setSuccess(false);
				responseDto.setError(descrizione);
				System.out.println(descrizione);
			}
		}else{
			String descrizione = "Utente e/o Codice Belfiore NON VALORIZZATI!!! ";
			responseDto.setSuccess(false);
			responseDto.setError(descrizione);
			System.out.println(descrizione);
		   }
		
		return responseDto;
	 }//------------------------------------------------------------------------	
	
	@WebMethod(operationName = "getLocazioniByCoordAnag")
	public RespLocazioniDTO getLocazioniByCoordAnag(@WebParam(name = "belfiore") String belfiore, @WebParam(name = "utente") String utente, @WebParam(name = "ot_prik") String ot_prik,
			@WebParam(name = "sezione") String sezione, @WebParam(name = "foglio") String foglio, @WebParam(name = "particella") String particella, @WebParam(name = "subalterno") String subalterno,
			@WebParam(name = "identificativo") String identificativo, @WebParam(name = "tipologia") String tipologia, @WebParam(name = "tipoCoinvolgimento") String tipoCoinvolgimento){

		ArrayList<LocazioneDTO> outputLocazioni = new ArrayList<LocazioneDTO>();
		RespLocazioniDTO responseDto = new RespLocazioniDTO();

		LocazioniService locazioniService = (LocazioniService) getEjb("CT_Service", "CT_Service_Data_Access", "LocazioniServiceBean");
		
		belfiore = notNullAndTrim(belfiore);
		utente = notNullAndTrim(utente);
		//pwd = notNullAndTrim(pwd);
		ot_prik = notNullAndTrim(ot_prik);

		sezione = notNullAndTrim(sezione);
		foglio = notNullAndTrim(foglio);
		particella = notNullAndTrim(particella);
		subalterno = notNullAndTrim(subalterno);
		/*
		 * Questi parametri ci consentono anche di effettuare ricerche incrociate:
		 * codici fiscali tra le persone giuridiche e partite iva tra le persone fisiche
		 */
		identificativo = notNullAndTrim(identificativo);//codice fiscale o partita iva
		tipologia = notNullAndTrim(tipologia);			//P= fisica; G= giuridica
		tipoCoinvolgimento = notNullAndTrim(tipoCoinvolgimento);	//D= dante causa (locatore); A= avente causa (inquilino)

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		if (!utente.trim().equalsIgnoreCase("") && !belfiore.trim().equalsIgnoreCase("")){
			
			try{

				String[] fonti = (String[])mappaTemiFonti.get("CATASTO");
				boolean autorizzato = false;
				
				Context cont = new InitialContext();
				DataSource theDataSource = (DataSource)cont.lookup("java:/AMProfiler");
				
			    Connection conn = theDataSource.getConnection();
			    /*
			     * Recupero del metodo di autenticazione scelto per le applicazioni 
			     * esterne
			     */
			    MessageContext msgx = wsContext.getMessageContext();
			    HttpServletRequest exchange = (HttpServletRequest)msgx.get(MessageContext.SERVLET_REQUEST);
			    String remoteHost = exchange.getRemoteAddr();
			    
			    String authType = this.getGitOutAuthenticationType(conn, belfiore);
			    Long idValid = 0l; 
			   if (authType != null && !authType.trim().equalsIgnoreCase("")){
				   if (authType.trim().equalsIgnoreCase("TOKEN")){
					   idValid = this.getOtpValidationByToken(conn, utente, belfiore, ot_prik);				   
				   }else if (authType.startsWith("IP")){
					   idValid = this.getOtpValidationByIp(conn, utente, belfiore, authType, remoteHost);
				   }
			   }
			   /*
			     * validazione OTP
			     */
			   if (idValid != null && idValid > 0){
				   /*
				    * Aggiorna stato OTP = USATA
				    */
				   this.setOtpValidation(conn, idValid);
				   
				   Boolean autenticato = this.gitAuthentication(conn, utente);
				  
				   if (autenticato){
							   for (int z=0; z<fonti.length; z++){
									
									List<String> enteList = new LinkedList<String>();
									enteList.add(  belfiore  );

									HashMap<String,String> listaPermessi = this.getPermissionOfUser(conn, utente, belfiore);

				//					TEST				
				//					listaPermessi = new HashMap<String, String>();
				//					listaPermessi.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:CATASTO", "permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:CATASTO");

									CeTUser user = new CeTUser();
									user.setPermList(listaPermessi);
						            user.setUsername(  utente  );
						            user.setEnteList(enteList);
						            user.setCurrentEnte(  belfiore );

									if (GestionePermessi.autorizzato(user, "diogene", ITEM_VISUALIZZA_FONTI_DATI, "Fonte:"+ fonti[z]))
										autorizzato = true;
								}
							   if (autorizzato){
								   
								   RicercaLocazioniDTO rl = new RicercaLocazioniDTO();
								   rl.setEnteId(belfiore);
								   rl.setUserId(utente);
								   
								   rl.setCodFis(identificativo);
								   
								   rl.setSezione(sezione);
								   rl.setFoglio(foglio);
								   rl.setParticella(particella);
								   rl.setSubalterno(subalterno);
								   
								   rl.setTipoCoinvolgimento(tipoCoinvolgimento);
								   
								   rl.setLimit(QRY_LIMIT);
								   List<LocazioniA> lstLocazioniA = new ArrayList<LocazioniA>();
								   if (identificativo != null && !identificativo.trim().equalsIgnoreCase("")){
									   
									   List<Object[]> lstLocazioniAByCFAndCausa = locazioniService.getLocazioniByParams(rl);
									   if (lstLocazioniAByCFAndCausa != null ){
										   Iterator<Object[]> itObjs = lstLocazioniAByCFAndCausa.iterator();
										   while(itObjs.hasNext()){
											   Object[] objs = itObjs.next();
											   lstLocazioniA.add( (LocazioniA)objs[0]);   
										   }
									   }
								   }else{
									   System.out.println("Identificativo soggetto e/o Coinvolgimento non valorizzato!!!");
								   }

								   if ( (foglio != null && !foglio.trim().equalsIgnoreCase("")) || (particella != null && !particella.trim().equalsIgnoreCase("")) ){
									   
									   List<Object[]> lstLocazioniAByCoord = locazioniService.getLocazioniByCriteria(rl);
									   if (lstLocazioniAByCoord != null){
										   Iterator<Object[]> itObjs = lstLocazioniAByCoord.iterator();
										   while(itObjs.hasNext()){
											   Object[] objs = itObjs.next();
											   lstLocazioniA.add( (LocazioniA)objs[0]);   
										   }
									   }
								   }else{
									   System.out.println("Coordinate catastali non valorizzate!!!");
								   }
								   /*
								    * step 1: Recupero locA da CF e/o da Coord
								    * step 2: Recupero le "chiavi primarie"(ufficio+anno+serie+numero) 
								    * delle locA coinvolte
								    * step 3: per ogni chiave recupero locA e liste di locB e locI 
								    */
								   ArrayList<String> lstKeys = new ArrayList<String>();
								   Hashtable<String, String> htKeys = new Hashtable<String, String>();
								   if (lstLocazioniA != null && lstLocazioniA.size()>0){
									   /*
									    * Fare distinct della chiave primaria dell'oggetto
									    * locazione ordinate per chiave (ufficio+anno+serie+numero) 
									    */
									   Iterator<LocazioniA> itLocA = lstLocazioniA.iterator();
									   while(itLocA.hasNext()){
										   LocazioniA loca = (LocazioniA)itLocA.next();
										   String ufficio = notNullAndTrim(loca.getUfficio());
										   String anno = loca.getAnno()!=null?loca.getAnno().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString():"";
										   String serie = notNullAndTrim(loca.getSerie());
										   String numero = loca.getNumero()!=null?loca.getNumero().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString():"";
										   
										   String key = ufficio + "#" + anno + "#" + serie + "#" + numero;
										   if (!htKeys.containsKey(key)){
											   lstKeys.add(key);
											   htKeys.put(key, key);
										   }
									   }
								   }
								    
								   if (lstKeys != null && lstKeys.size()>0){
									   for (int z=0; z<lstKeys.size(); z++){
										   
										   LocazioneDTO locazioneDto = new LocazioneDTO();

										   String key = lstKeys.get(z);
										   String[] chiaveLocA = key.split("#");
										   RicercaLocazioniDTO ricLoc = new RicercaLocazioniDTO();
										   LocazioneBPK pk = new LocazioneBPK();
										   pk.setUfficio(chiaveLocA[0]);
										   pk.setAnno(new BigDecimal( chiaveLocA[1]) );
										   pk.setSerie(chiaveLocA[2]);
										   pk.setNumero( new BigDecimal(chiaveLocA[3]) );
										   ricLoc.setKey(pk);
										   ricLoc.setEnteId(belfiore);
										   ricLoc.setUserId(utente);
										   
										   List<LocazioniA> listaLocazioniA = locazioniService.getLocazioniOggByKey(ricLoc);
										   /*
										    * In teoria questa lista listaLocazioniA dovrebbe contenere una sola riga
										    */
										   Iterator<LocazioniA> itOggA = listaLocazioniA.iterator();
										   while(itOggA.hasNext()){
											   LocazioniA locazioneA = itOggA.next();
											   LocazioneAOggettoDTO oggettoDto = new LocazioneAOggettoDTO();
											   oggettoDto.setAnnoReg( locazioneA.getAnno().setScale(0, BigDecimal.ROUND_HALF_EVEN).toString() );
											   oggettoDto.setCodiceNegozio( locazioneA.getCodiceNegozio() );
											   oggettoDto.setDataFine( locazioneA.getDataFine()!=null?formatter.format(locazioneA.getDataFine()):"" );
											   oggettoDto.setDataInizio( locazioneA.getDataInizio()!=null?formatter.format(locazioneA.getDataInizio()):"" );
											   oggettoDto.setDataReg( locazioneA.getDataRegistrazione()!=null?formatter.format(locazioneA.getDataRegistrazione()):"" );
											   oggettoDto.setDataStipula( locazioneA.getDataStipula()!=null?formatter.format(locazioneA.getDataStipula()):"" );
											   oggettoDto.setImportoCanone( locazioneA.getImportoCanone() );
											   oggettoDto.setNumeroReg( locazioneA.getNumero().setScale(0, BigDecimal.ROUND_HALF_EVEN).toString() );
											   oggettoDto.setOggettoLocazione( locazioneA.getOggettoLocazione() );
											   oggettoDto.setProgressivoNegozio( locazioneA.getProgNegozio()!=null?locazioneA.getProgNegozio().setScale(0, BigDecimal.ROUND_HALF_EVEN).toString():"" );
											   oggettoDto.setSerieReg( locazioneA.getSerie() );
											   oggettoDto.setSottonumeroReg( locazioneA.getSottonumero()!=null?locazioneA.getSottonumero().setScale(0, BigDecimal.ROUND_HALF_EVEN).toString():"" );
											   oggettoDto.setTipoCanone( locazioneA.getTipoCanone() );
											   oggettoDto.setUfficioReg( locazioneA.getUfficio() );

											   locazioneDto.setOggetto(oggettoDto);
											   
										   }

										   List<LocazioniB> listaLocazioniB = locazioniService.getLocazioneSoggByChiave(ricLoc);
										   Iterator<LocazioniB> itSogB = listaLocazioniB.iterator();
										   ArrayList<LocazioneBSoggettoDTO> alSogB = new ArrayList<LocazioneBSoggettoDTO>();
										   while(itSogB.hasNext()){
											   LocazioniB locazioneB = itSogB.next();
											   LocazioneBSoggettoDTO soggettoDto = new LocazioneBSoggettoDTO();
											   soggettoDto.setTipoTitolo( locazioneB.getTipoSoggetto() );
											   soggettoDto.setCodiFisc( locazioneB.getCodicefiscale() );
											   soggettoDto.setSesso( locazioneB.getSesso() );
											   soggettoDto.setComuNasc( locazioneB.getCittaNascita() );
											   soggettoDto.setProvinciaNascita( locazioneB.getProvNascita() );
											   soggettoDto.setDataNasc( locazioneB.getDataNascita() );
											   soggettoDto.setComuneResidenza( locazioneB.getCittaResidenza() );
											   soggettoDto.setProvinciaResidenza( locazioneB.getProvResidenza() );
											   soggettoDto.setIndirizzoResidenza( locazioneB.getIndirizzoResidenza() );
											   soggettoDto.setCivicoResidenza( locazioneB.getCivicoResidenza() );
											   soggettoDto.setDataSubentro( locazioneB.getDataSubentro()!=null?formatter.format( locazioneB.getDataSubentro() ):"" );
											   soggettoDto.setDataCessione( locazioneB.getDataCessione()!=null?formatter.format( locazioneB.getDataCessione() ):"" );
											   
											   alSogB.add(soggettoDto);
										   }
										   locazioneDto.setLstSoggetti(alSogB);
										   
										   List<LocazioniI> listaLocazioniI = locazioniService.getImmobiliByKey(ricLoc);
										   Iterator<LocazioniI> itImmI = listaLocazioniI.iterator();
										   ArrayList<LocazioneIImmobileDTO> alImmI = new ArrayList<LocazioneIImmobileDTO>();
										   while(itImmI.hasNext()){
											   LocazioniI locazioneI = itImmI.next();
											   LocazioneIImmobileDTO immobileDto = new LocazioneIImmobileDTO();
											   immobileDto.setAccatastamento( locazioneI.getAccatastamento() );
											   immobileDto.setCodiceCatastale( locazioneI.getCodiceCat() );
											   immobileDto.setFoglio( locazioneI.getFoglio() );
											   immobileDto.setIndirizzo( locazioneI.getIndirizzo() );
											   immobileDto.setInteroPorzione( locazioneI.getFlgInteroPorz() );
											   immobileDto.setNumero( locazioneI.getParticella() );
											   immobileDto.setSezione( locazioneI.getSezUrbana() );
											   immobileDto.setSubalterno( locazioneI.getSubalterno() );
											   immobileDto.setTipoCatasto( locazioneI.getTipoCatasto() );
											   
											   alImmI.add(immobileDto);
										   }
										   locazioneDto.setLstImmobili(alImmI);
										   
										   outputLocazioni.add(locazioneDto);
									   }
									   
									   responseDto.setLstObjs( outputLocazioni );
									   responseDto.setSuccess(true);
									   
								   }else{
									   String descrizione = "Oggetti Locazioni A non trovati per i parametri richiesti!!!";
									   responseDto.setSuccess(false);
									   responseDto.setError(descrizione);
									   System.out.println(descrizione);
								   }
							   }else{

									   String descrizione = "Utente NON autorizzato ad accedere alle informazioni richieste!!!";
									   responseDto.setSuccess(false);
									   responseDto.setError(descrizione);
									   System.out.println(descrizione);
								}
				   }else{
						   String descrizione = "Autenticazione fallita. Credenziali GIT errate o scadute!!!";
						   responseDto.setSuccess(false);
						   responseDto.setError(descrizione);
						   System.out.println(descrizione);

					}
			   }else{
				   String descrizione = "";
					if (authType != null && !authType.trim().equalsIgnoreCase("") && authType.trim().equalsIgnoreCase("TOKEN"))
						descrizione =  "OTP private key NON VALIDATA!!!";
					if (authType != null && !authType.trim().equalsIgnoreCase("") && authType.startsWith("IP"))
						descrizione = "IP Remote Host NON VALIDO!!! " + remoteHost;

				   responseDto.setSuccess(false);
				   responseDto.setError(descrizione);
				   System.out.println(descrizione);
			   }
			   if (conn != null)
				   conn.close();
			}
			catch (Exception t) {
				t.printStackTrace();
				   String descrizione = t.getMessage();
				   responseDto.setSuccess(false);
				   responseDto.setError(descrizione);
				   System.out.println(descrizione);
			}
		}else{
			   String descrizione = "Utente e/o Codice Belfiore NON VALORIZZATI!!!";
			   responseDto.setSuccess(false);
			   responseDto.setError(descrizione);
			   System.out.println(descrizione);
			   
		   }

		return responseDto;
	 }//------------------------------------------------------------------------
	
	@WebMethod
	public RespSuccessioniDTO getSuccessioniByCoordAnag(@WebParam(name = "belfiore") String belfiore, @WebParam(name = "utente") String utente, @WebParam(name = "ot_prik") String ot_prik,
			@WebParam(name = "sezione") String sezione, @WebParam(name = "foglio") String foglio, @WebParam(name = "particella") String particella, @WebParam(name = "subalterno") String subalterno,
			@WebParam(name = "identificativo") String identificativo, @WebParam(name = "tipoCoinvolgimento") String tipoCoinvolgimento){

		ArrayList<SuccessioneDTO> outputSucc = new ArrayList<SuccessioneDTO>();
		RespSuccessioniDTO responseDto = new RespSuccessioniDTO();

		SuccessioniService successioniService = (SuccessioniService) getEjb("CT_Service", "CT_Service_Data_Access", "SuccessioniServiceBean");
		
		belfiore = notNullAndTrim(belfiore);
		utente = notNullAndTrim(utente);
		//pwd = notNullAndTrim(pwd);
		ot_prik = notNullAndTrim(ot_prik);

		sezione = notNullAndTrim(sezione);
		foglio = notNullAndTrim(foglio);
		particella = notNullAndTrim(particella);
		subalterno = notNullAndTrim(subalterno);
		/*
		 * Questi parametri ci consentono anche di effettuare ricerche incrociate:
		 * codici fiscali tra le persone giuridiche e partite iva tra le persone fisiche
		 */
		identificativo = notNullAndTrim(identificativo);//codice fiscale o partita iva
		tipoCoinvolgimento = notNullAndTrim(tipoCoinvolgimento);	//D= defunto; E= erede

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		if (!utente.trim().equalsIgnoreCase("") && !belfiore.trim().equalsIgnoreCase("")){
			
			try{

				String[] fonti = (String[])mappaTemiFonti.get("CATASTO");
				boolean autorizzato = false;
				
				Context cont = new InitialContext();
				DataSource theDataSource = (DataSource)cont.lookup("java:/AMProfiler");
				
			    Connection conn = theDataSource.getConnection();
			    /*
			     * Recupero del metodo di autenticazione scelto per le applicazioni 
			     * esterne
			     */
			    MessageContext msgx = wsContext.getMessageContext();
			    HttpServletRequest exchange = (HttpServletRequest)msgx.get(MessageContext.SERVLET_REQUEST);
			    String remoteHost = exchange.getRemoteAddr();

			    String authType = this.getGitOutAuthenticationType(conn, belfiore);
			    Long idValid = 0l;  
			   if (authType != null && !authType.trim().equalsIgnoreCase("")){
				   if (authType.trim().equalsIgnoreCase("TOKEN")){
					   idValid = this.getOtpValidationByToken(conn, utente, belfiore, ot_prik);				   
				   }else if (authType.startsWith("IP")){
					   idValid = this.getOtpValidationByIp(conn, utente, belfiore, authType, remoteHost);
				   }
			   }
			   /*
			     * validazione OTP
			     */
			   if (idValid != null && idValid > 0){
				   /*
				    * Aggiorna stato OTP = USATA
				    */
				   this.setOtpValidation(conn, idValid);
				   
				   Boolean autenticato = this.gitAuthentication(conn, utente);
				  
				   if (autenticato){
							   for (int z=0; z<fonti.length; z++){
									
									List<String> enteList = new LinkedList<String>();
									enteList.add(  belfiore  );

									HashMap<String,String> listaPermessi = this.getPermissionOfUser(conn, utente, belfiore);

				//					TEST				
				//					listaPermessi = new HashMap<String, String>();
				//					listaPermessi.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:CATASTO", "permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:CATASTO");

									CeTUser user = new CeTUser();
									user.setPermList(listaPermessi);
						            user.setUsername(  utente  );
						            user.setEnteList(enteList);
						            user.setCurrentEnte(  belfiore );

									if (GestionePermessi.autorizzato(user, "diogene", ITEM_VISUALIZZA_FONTI_DATI, "Fonte:"+ fonti[z]))
										autorizzato = true;
								}
							   if (autorizzato){
								   /*
								    * Step 1: ricerca chiave primaria pratiche: ufficio, anno, volume, numero, sottonumero, comune, progressivo, [fornitura]
								    * indipendentemente dal tipo di parametri valorizzati in ingresso
								    * Step 2: in base alla pratica, recuperare defunto ed eredità 
								    */
								  ArrayList<RicercaSuccessioniDTO> alRicSuc = new ArrayList<RicercaSuccessioniDTO>();
								  Hashtable<String, RicercaSuccessioniDTO> htKeys = new Hashtable<String, RicercaSuccessioniDTO>();
								   /*
								    * Interrogazione Defunto
								    */
								   if (identificativo != null && !identificativo.trim().equalsIgnoreCase("") 
										   && tipoCoinvolgimento != null && !tipoCoinvolgimento.trim().equalsIgnoreCase("") 
										   && tipoCoinvolgimento.trim().equalsIgnoreCase("D")){
									   
									   RicercaSuccessioniDTO rs = new RicercaSuccessioniDTO();
									   rs.setEnteId(belfiore);
									   rs.setLimit(QRY_LIMIT);
									   rs.setUserId(utente);
									   rs.setCodFis(identificativo);
									   rs.setTipoCoinvolgimento(tipoCoinvolgimento);
									   List<SuccessioniA> alSucA = successioniService.getSuccessioniAByPk(rs);
									   if (alSucA != null){
										   for (int index=0; index<alSucA.size(); index++){
											   SuccessioniA suca = alSucA.get(index);
											   if (suca != null){
												   RicercaSuccessioniDTO ricSuc = new RicercaSuccessioniDTO();
												   ricSuc.setEnteId(belfiore);
												   ricSuc.setLimit(QRY_LIMIT);
												   ricSuc.setUserId(utente);
												   String ufficio = suca.getId().getUfficio();
												   ricSuc.setUfficio(ufficio);
												   String anno = suca.getId().getAnno();
												   ricSuc.setAnno(anno);
												   String volume = suca.getId().getVolume().setScale(0, BigDecimal.ROUND_HALF_EVEN).toString();
												   ricSuc.setVolume(volume);
												   String numero = suca.getId().getNumero().setScale(0, BigDecimal.ROUND_HALF_EVEN).toString();
												   ricSuc.setNumero(numero);
												   String sottonumero = suca.getId().getSottonumero().setScale(0, BigDecimal.ROUND_HALF_EVEN).toString();
												   ricSuc.setSottonumero(sottonumero);
												   String comune = suca.getId().getComune();
												   ricSuc.setComune(comune);
												   String progressivo = suca.getId().getProgressivo().setScale(0, BigDecimal.ROUND_HALF_EVEN).toString();
												   ricSuc.setProgressivo(progressivo);
												   String fornitura = suca.getId().getFornitura().setScale(0, BigDecimal.ROUND_HALF_EVEN).toString();
												   ricSuc.setFornitura(fornitura);
												   
												   String currentKey = ufficio + "#" + anno + "#" + volume + "#" + numero + "#" + sottonumero + "#" + comune + "#" + progressivo;

												   alRicSuc.add(ricSuc);
												   htKeys.put(currentKey, ricSuc);
											   }
										   }
									   }else
										   System.out.println("Interrogazione DEFUNTO non ha prodotto risultati");

								   }else
									   System.out.println("Interrogazione DEFUNTO: Identificativo e tipo coinvolgimento NON valorizzati");
								   /*
								    * Interrogazione EREDITA Oggetti
								    */
								   if (foglio != null && !foglio.trim().equalsIgnoreCase("") 
										   && particella != null && !particella.trim().equalsIgnoreCase("")){
									   
									   RicercaSuccessioniDTO rs = new RicercaSuccessioniDTO();
									   rs.setEnteId(belfiore);
									   rs.setLimit(QRY_LIMIT);
									   rs.setUserId(utente);
									   rs.setSezione(sezione);
									   rs.setFoglio(foglio);
									   rs.setParticella(particella);
									   rs.setSubalterno(subalterno);
									   List<SuccessioniC> alSucC = successioniService.getSuccessioniCByPk(rs);
									   if (alSucC != null){
										   for (int index=0; index<alSucC.size(); index++){
											   SuccessioniC succ = alSucC.get(index);
											   if (succ != null){
												   RicercaSuccessioniDTO ricSuc = new RicercaSuccessioniDTO();
												   ricSuc.setEnteId(belfiore);
												   ricSuc.setLimit(QRY_LIMIT);
												   ricSuc.setUserId(utente);
												   String ufficio = succ.getId().getUfficio();
												   ricSuc.setUfficio(ufficio);
												   String anno = succ.getId().getAnno();
												   ricSuc.setAnno(anno);
												   String volume = succ.getId().getVolume().setScale(0, BigDecimal.ROUND_HALF_EVEN).toString();
												   ricSuc.setVolume(volume);
												   String numero = succ.getId().getNumero().setScale(0, BigDecimal.ROUND_HALF_EVEN).toString();
												   ricSuc.setNumero(numero);
												   String sottonumero = succ.getId().getSottonumero().setScale(0, BigDecimal.ROUND_HALF_EVEN).toString();
												   ricSuc.setSottonumero(sottonumero);
												   String comune = succ.getId().getComune();
												   ricSuc.setComune(comune);
												   String progressivo = succ.getId().getProgressivo().setScale(0, BigDecimal.ROUND_HALF_EVEN).toString();
												   ricSuc.setProgressivo(progressivo);
												   String fornitura = succ.getId().getFornitura().setScale(0, BigDecimal.ROUND_HALF_EVEN).toString();
												   ricSuc.setFornitura(fornitura);

												   String currentKey = ufficio + "#" + anno + "#" + volume + "#" + numero + "#" + sottonumero + "#" + comune + "#" + progressivo;

												   alRicSuc.add(ricSuc);
												   htKeys.put(currentKey, ricSuc);
											   }
										   }
									   }else
										   System.out.println("Interrogazione EREDITA non ha prodotto risultati");
									   
								   }else
									   System.out.println("Interrogazione EREDITA: Coordinate Immobile NON valorizzate");
								   /*
								    * Interrogazione EREDITA Soggetti (EREDI)
								    */
								   if (identificativo != null && !identificativo.trim().equalsIgnoreCase("") 
										   && tipoCoinvolgimento != null && !tipoCoinvolgimento.trim().equalsIgnoreCase("") 
										   && tipoCoinvolgimento.trim().equalsIgnoreCase("E")){
									   
									   RicercaSuccessioniDTO rs = new RicercaSuccessioniDTO();
									   rs.setEnteId(belfiore);
									   rs.setLimit(QRY_LIMIT);
									   rs.setUserId(utente);
									   rs.setCodFis(identificativo);
									   rs.setTipoCoinvolgimento(tipoCoinvolgimento);
									   List<SuccessioniB> alSucB = successioniService.getSuccessioniBByPk(rs);
									   if (alSucB != null){
										   for (int index=0; index<alSucB.size(); index++){
											   SuccessioniB sucb = alSucB.get(index);
											   if (sucb != null){
												   RicercaSuccessioniDTO ricSuc = new RicercaSuccessioniDTO();
												   ricSuc.setEnteId(belfiore);
												   ricSuc.setLimit(QRY_LIMIT);
												   ricSuc.setUserId(utente);
												   String ufficio = sucb.getId().getUfficio();
												   ricSuc.setUfficio(ufficio);
												   String anno = sucb.getId().getAnno();
												   ricSuc.setAnno(anno);
												   String volume = sucb.getId().getVolume().setScale(0, BigDecimal.ROUND_HALF_EVEN).toString();
												   ricSuc.setVolume(volume);
												   String numero = sucb.getId().getNumero().setScale(0, BigDecimal.ROUND_HALF_EVEN).toString();
												   ricSuc.setNumero(numero);
												   String sottonumero = sucb.getId().getSottonumero().setScale(0, BigDecimal.ROUND_HALF_EVEN).toString();
												   ricSuc.setSottonumero(sottonumero);
												   String comune = sucb.getId().getComune();
												   ricSuc.setComune(comune);
												   String progressivo = sucb.getId().getProgressivo().setScale(0, BigDecimal.ROUND_HALF_EVEN).toString();
												   ricSuc.setProgressivo(progressivo);
												   String fornitura = sucb.getId().getFornitura().setScale(0, BigDecimal.ROUND_HALF_EVEN).toString();
												   ricSuc.setFornitura(fornitura);

												   String currentKey = ufficio + "#" + anno + "#" + volume + "#" + numero + "#" + sottonumero + "#" + comune + "#" + progressivo;

												   alRicSuc.add(ricSuc);
												   htKeys.put(currentKey, ricSuc);
											   }
										   }
									   }else
										   System.out.println("Interrogazione EREDI non ha prodotto risultati");
									   
									   
								   }else
									   System.out.println("Interrogazione EREDI: Identificativo e tipo coinvolgimento NON valorizzati");
								   /*
								    * per ogni chiave di pratica si recuperano tutte le info: defunto ed eredità
								    */
								   if (htKeys != null){
									   Iterator<String> itKeys = htKeys.keySet().iterator();
									   String[] aryKeys = new String[htKeys.size()];
									   int i=0;
									   while(itKeys.hasNext()){
										   String key = itKeys.next();
										   aryKeys[i] = key;
										   i++;
									   }
									   /*
									    * Ordinamento per chiave pratica
									    */
									   Arrays.sort(aryKeys);
									   /*
									    * Inizio recupero informazioni
									    */
									   boolean trovato = false;
									   for (int z=0; z<aryKeys.length; z++){
										   String chiave = aryKeys[z];
										   String[] cc = chiave.split("#");
										   //ufficio + "#" + anno + "#" + volume + "#" + numero + "#" + sottonumero + "#" + comune + "#" + progressivo;
										   RicercaSuccessioniDTO ricSuc = new RicercaSuccessioniDTO();
										   ricSuc.setEnteId(belfiore);
										   ricSuc.setLimit(QRY_LIMIT);
										   ricSuc.setUserId(utente);
										   String ufficio = cc[0];
										   ricSuc.setUfficio(ufficio);
										   String anno = cc[1];
										   ricSuc.setAnno(anno);
										   String volume = cc[2];
										   ricSuc.setVolume(volume);
										   String numero = cc[3];
										   ricSuc.setNumero(numero);
										   String sottonumero = cc[4];
										   ricSuc.setSottonumero(sottonumero);
										   String comune = cc[5];
										   ricSuc.setComune(comune);
										   String progressivo = cc[6];
//										   ricSuc.setProgressivo(progressivo);
//										   String fornitura = cc[7];
//										   ricSuc.setFornitura(fornitura);

										   List<SuccessioniA> lstSucA = successioniService.getSuccessioniAByPk(ricSuc);
										   SuccessioneDTO successioneDto = new SuccessioneDTO();

										   if (lstSucA != null && lstSucA.size()>0){
											   trovato = true;
											   SuccessionePraticaDTO praticaDto = new SuccessionePraticaDTO();
											   SuccessioneDefuntoDTO defuntoDto = new SuccessioneDefuntoDTO();
											   
											   praticaDto.setAnno(anno);
											   praticaDto.setComune(comune);
											   //praticaDto.setFornitura();
											   praticaDto.setNumero(numero);
											   praticaDto.setProgressivo(progressivo);
											   praticaDto.setSottonumero(sottonumero);
											   praticaDto.setUfficio(ufficio);
											   praticaDto.setVolume(volume);
											   successioneDto.setPratica(praticaDto);
											   
											   SuccessioniA suca = (SuccessioniA)lstSucA.get(0);
											   defuntoDto.setCodiceFiscale(suca.getCfDefunto());
											   defuntoDto.setCognome(suca.getCognomeDefutno());
											   defuntoDto.setDataNascita(suca.getDataNascita());
											   defuntoDto.setLuogoNascita(suca.getCittaNascita());
											   defuntoDto.setNome(suca.getNomeDefunto());
											   defuntoDto.setProvinciaNascita(suca.getProvNascita());
											   defuntoDto.setSesso(suca.getSesso());
											   successioneDto.setDefunto(defuntoDto);
											   /*
											    * Eredita del defunto corrente
											    */
											   List<Object[]> lstObjsEredita = successioniService.getSuccessioniEreditaByParams(ricSuc);
											   ArrayList<SuccessioneEreditaDTO> lstEredita = new ArrayList<SuccessioneEreditaDTO>();
											   if (lstObjsEredita != null && lstObjsEredita.size()>0){
												   for (int j=0; j<lstObjsEredita.size(); j++){
													   SuccessioneEreditaDTO ereditaDto = new SuccessioneEreditaDTO();
													   Object[] objs = lstObjsEredita.get(j);
													   /*
													    *  successioni_c.UFFICIO, successioni_c.ANNO, successioni_c.VOLUME, successioni_c.NUMERO, successioni_c.SOTTONUMERO, successioni_c.COMUNE, successioni_c.PROGRESSIVO, 
														   successioni_c.FOGLIO, successioni_c.PARTICELLA1, successioni_c.SUBALTERNO1, successioni_c.PROGRESSIVO_IMMOBILE, successioni_c.PROGRESSIVO_PARTICELLA, successioni_d.PROGRESSIVO_EREDE, 
														   successioni_c.NUMERATORE_QUOTA_DEF, successioni_c.DENOMINATORE_QUOTA_DEF, successioni_d.NUMERATORE_QUOTA, successioni_d.DENOMINATORE_QUOTA, 
														   successioni_b.CF_EREDE, successioni_b.DENOMINAZIONE 
														   
														   [R2D, 07, 68, 53, 0, F704, 1, 0060, 00096, 1, 1, 1, 1, 0000001,000, 000001, 1, 1, VGNNGL30D56F704K, VIGANO' ANGELA]
														   
													    */

													   ereditaDto.setCodiceFiscale(  notNullAndTrim( (String)objs[17]) );
													   ereditaDto.setFoglio( notNullAndTrim( (String)objs[7]) );
													   ereditaDto.setNominativo( notNullAndTrim( (String)objs[18]) );
													   ereditaDto.setParticella( notNullAndTrim( (String)objs[8]) );
													   ereditaDto.setSubalterno( objs[9]!=null? ((BigDecimal)objs[9]).setScale(0, BigDecimal.ROUND_HALF_EVEN).toString() : "");
													   String numQuotaDef = notNullAndTrim( (String)objs[13]);
													   String denQuotaDef = notNullAndTrim( (String)objs[14]);
													   String numQuotaEre = notNullAndTrim( objs[15]!=null? ((BigDecimal)objs[15]).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString():"" );
													   String denQuotaEre = notNullAndTrim( objs[16]!=null? ((BigDecimal)objs[16]).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString():"" );
													   ereditaDto.setQuotaDefunto( numQuotaDef + "/" + denQuotaDef );
													   ereditaDto.setQuotaErede( numQuotaEre + "/" + denQuotaEre);
													   
													   lstEredita.add(ereditaDto);
													   
												   }
											   }
											   successioneDto.setLstEredita(lstEredita);
											   
											   outputSucc.add(successioneDto);
										   }
									   }
									   
									   if (!trovato){
										   
										   String descrizione = "Chiave di ricerca PRATICA/DEFUNTO esistente ma EREDITA assente!!!";
										   responseDto.setSuccess(false);
										   responseDto.setError(descrizione);
										   System.out.println(descrizione);
											
									   }else{
										   
										   responseDto.setLstObjs( outputSucc );
										   responseDto.setSuccess(true);

									   }
									   
								   }else{
										String descrizione = "Nessuna PRATICA di SUCCESSIONE recuperata!!!";
										   responseDto.setSuccess(false);
										   responseDto.setError(descrizione);
										   System.out.println(descrizione);
								   }
							   }else{
									String descrizione = "Utente NON autorizzato ad accedere alle informazioni richieste!!!";
									   responseDto.setSuccess(false);
									   responseDto.setError(descrizione);
									   System.out.println(descrizione);
									   
								}
				   }else{
						String descrizione = "Autenticazione fallita. Credenziali GIT errate o scadute!!!";
						   responseDto.setSuccess(false);
						   responseDto.setError(descrizione);
						   System.out.println(descrizione);
					}
			   }else{
				   String descrizione = "";
				   if (authType != null && !authType.trim().equalsIgnoreCase("") && authType.trim().equalsIgnoreCase("TOKEN"))
						descrizione = "OTP private key NON VALIDATA!!!";
					if (authType != null && !authType.trim().equalsIgnoreCase("") && authType.startsWith("IP"))
						descrizione = "IP Remote Host NON VALIDO!!! " + remoteHost;
				   responseDto.setSuccess(false);
				   responseDto.setError(descrizione);
				   System.out.println(descrizione);
				   
			   }
			   if (conn != null)
				   conn.close();
			}
			catch (Exception t) {
				t.printStackTrace();
				String descrizione = t.getMessage();
				   responseDto.setSuccess(false);
				   responseDto.setError(descrizione);
				   System.out.println(descrizione);
			}
		}else{
			String descrizione = "Utente e/o Codice Belfiore NON VALORIZZATI!!!";
			   responseDto.setSuccess(false);
			   responseDto.setError(descrizione);
			   System.out.println(descrizione);
			   
		   }

		return responseDto;
	 }//------------------------------------------------------------------------
	
	@WebMethod
	public RespPregeoDTO getPregeoByCoord(@WebParam(name = "belfiore") String belfiore, @WebParam(name = "utente") String utente, @WebParam(name = "ot_prik") String ot_prik,
				@WebParam(name = "foglio") String foglio, @WebParam(name = "particella") String particella){
			
		ArrayList<PregeoDTO> outputPregeo = new ArrayList<PregeoDTO>();
		RespPregeoDTO responseDto = new RespPregeoDTO();
	
		PregeoService pregeoService = (PregeoService) getEjb("CT_Service", "CT_Service_Data_Access", "PregeoServiceBean");
		
		belfiore = notNullAndTrim(belfiore);
		utente = notNullAndTrim(utente);
		//pwd = notNullAndTrim(pwd);
		ot_prik = notNullAndTrim(ot_prik);

		foglio = notNullAndTrim(foglio);
		particella = notNullAndTrim(particella);
	
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		if (!utente.trim().equalsIgnoreCase("") && !belfiore.trim().equalsIgnoreCase("")){
				
		try{

				String[] fonti = (String[])mappaTemiFonti.get("CATASTO");
				boolean autorizzato = false;
				
				Context cont = new InitialContext();
				DataSource theDataSource = (DataSource)cont.lookup("java:/AMProfiler");
				
			    Connection conn = theDataSource.getConnection();
			    /*
			     * Recupero del metodo di autenticazione scelto per le applicazioni 
			     * esterne
			     */
			    MessageContext msgx = wsContext.getMessageContext();
			    HttpServletRequest exchange = (HttpServletRequest)msgx.get(MessageContext.SERVLET_REQUEST);
			    String remoteHost = exchange.getRemoteAddr();
			    
			    String authType = this.getGitOutAuthenticationType(conn, belfiore);
			    Long idValid = 0l; 
			   if (authType != null && !authType.trim().equalsIgnoreCase("")){
				   if (authType.trim().equalsIgnoreCase("TOKEN")){
					   idValid = this.getOtpValidationByToken(conn, utente, belfiore, ot_prik);				   
				   }else if (authType.startsWith("IP")){
					   idValid = this.getOtpValidationByIp(conn, utente, belfiore, authType, remoteHost);
				   }
			   }
			   /*
			     * validazione OTP
			     */
			   if (idValid != null && idValid > 0){
				   /*
				    * Aggiorna stato OTP = USATA
				    */
				   this.setOtpValidation(conn, idValid);
				   
				   Boolean autenticato = this.gitAuthentication(conn, utente);
				  
				   if (autenticato){
							   for (int z=0; z<fonti.length; z++){
									
									List<String> enteList = new LinkedList<String>();
									enteList.add(  belfiore  );

									HashMap<String,String> listaPermessi = this.getPermissionOfUser(conn, utente, belfiore);

				//					TEST				
				//					listaPermessi = new HashMap<String, String>();
				//					listaPermessi.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:CATASTO", "permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:CATASTO");

									CeTUser user = new CeTUser();
									user.setPermList(listaPermessi);
						            user.setUsername(  utente  );
						            user.setEnteList(enteList);
						            user.setCurrentEnte(  belfiore );

									if (GestionePermessi.autorizzato(user, "diogene", ITEM_VISUALIZZA_FONTI_DATI, "Fonte:"+ fonti[z]))
										autorizzato = true;
								}
							   if (autorizzato){
								   RicercaPregeoDTO rp = new RicercaPregeoDTO();
								   rp.setEnteId(belfiore);
								   rp.setUserId(utente);
								   rp.setLimit(QRY_LIMIT);
								   rp.setFoglio(foglio);
								   rp.setParticella(particella);
								   
								   List<PregeoInfo> lstPregeo = pregeoService.getPregeoByCriteria(rp);
								   if (lstPregeo != null && lstPregeo.size()>0){
									   for(int index=0; index<lstPregeo.size(); index++){
										   PregeoDTO pregeoDto = new PregeoDTO();
										   PregeoInfo pi = (PregeoInfo)lstPregeo.get(index);
										   if (pi != null){
											   pregeoDto.setCodicePregeo(pi.getCodicePregeo());
											   Date dataPregeo = pi.getDataPregeo();
											   String dp = formatter.format(dataPregeo);
											   pregeoDto.setDataPregeo(dp);
											   pregeoDto.setDenominazione(pi.getDenominazione());
											   pregeoDto.setFoglio(pi.getFoglio());
											   pregeoDto.setParticella(pi.getParticella());

											   outputPregeo.add(pregeoDto);
										   }
									   }
									   
									   responseDto.setLstObjs( outputPregeo );
									   responseDto.setSuccess(true);

								   }else{
										String descrizione = "Nessun dato PREGEO presente con i parametri richiesti!!!";
										   responseDto.setSuccess(false);
										   responseDto.setError(descrizione);
										   System.out.println(descrizione);
										   
								   }
							   }else{
									String descrizione = "Utente NON autorizzato ad accedere alle informazioni richieste!!!";
									   responseDto.setSuccess(false);
									   responseDto.setError(descrizione);
									   System.out.println(descrizione);
								}
				   }else{
						String descrizione = "Autenticazione fallita. Credenziali GIT errate o scadute!!!";
						responseDto.setSuccess(false);
						responseDto.setError(descrizione);
						System.out.println(descrizione);
					}
			   }else{
				   String descrizione = "";
					if (authType != null && !authType.trim().equalsIgnoreCase("") && authType.trim().equalsIgnoreCase("TOKEN"))
						descrizione = "OTP private key NON VALIDATA!!!";
					if (authType != null && !authType.trim().equalsIgnoreCase("") && authType.startsWith("IP"))
						descrizione = "IP Remote Host NON VALIDO!!! " + remoteHost;
					responseDto.setSuccess(false);
					responseDto.setError(descrizione);
					System.out.println(descrizione);
			   }
			   if (conn != null)
				   conn.close();
			}
			catch (Exception t) {
				t.printStackTrace();
				String descrizione = t.getMessage();
				   responseDto.setSuccess(false);
				   responseDto.setError(descrizione);
				   System.out.println(descrizione);
			}
		}else{
			String descrizione = "Utente e/o Codice Belfiore NON VALORIZZATI!!! ";
			   responseDto.setSuccess(false);
			   responseDto.setError(descrizione);
			   System.out.println(descrizione);
		   }
			
			return responseDto;
		 }//--------------------------------------------------------------------	
	
	@WebMethod
	public RespUnitaTerreDTO getTerTitByCoord(@WebParam(name = "belfiore") String belfiore, @WebParam(name = "utente") String utente, @WebParam(name = "ot_prik") String ot_prik, 
			@WebParam(name = "sezione") String sezione, @WebParam(name = "foglio") String foglio, @WebParam(name = "particella") String particella, @WebParam(name = "subalterno") String subalterno, @WebParam(name = "dataValidita") String dataValidita){
		
		RespUnitaTerreDTO responseDto = new RespUnitaTerreDTO();
		ArrayList<UnitaTerreDTO> output = new ArrayList<UnitaTerreDTO>();
		
//		UnitaTerreDTO[] output = null;
		/*
		 *  parametri URL credenziali:
			- ente (BELFIORE_ENTE);
			- utente
			- ot_prik (ONE TIME PRIVATE KEY); 
		 */

		CatastoService catastoService = (CatastoService) getEjb("CT_Service", "CT_Service_Data_Access", "CatastoServiceBean");
		
		belfiore = notNullAndTrim(belfiore);
		utente = notNullAndTrim(utente);
		//pwd = notNullAndTrim(pwd);
		ot_prik = notNullAndTrim(ot_prik);
		sezione = notNullAndTrim(sezione);
		foglio = notNullAndTrim(foglio);
		particella = notNullAndTrim(particella);
		subalterno = notNullAndTrim(subalterno);		
		dataValidita = notNullAndTrim(dataValidita); 	// nel formato: GG/MM/AAAA
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		
		if (!utente.trim().equalsIgnoreCase("") && !belfiore.trim().equalsIgnoreCase("")){
			
			try{

				String[] fonti = (String[])mappaTemiFonti.get("CATASTO");
				boolean autorizzato = false;
				
				Context cont = new InitialContext();
				DataSource theDataSource = (DataSource)cont.lookup("java:/AMProfiler");
				
			    Connection conn = theDataSource.getConnection();
			    /*
			     * Recupero del metodo di autenticazione scelto per le applicazioni 
			     * esterne
			     */
			    MessageContext msgx = wsContext.getMessageContext();
			    HttpServletRequest exchange = (HttpServletRequest)msgx.get(MessageContext.SERVLET_REQUEST);
			    String remoteHost = exchange.getRemoteAddr();
			    
			    String authType = this.getGitOutAuthenticationType(conn, belfiore);
			    Long idValid = 0l; 
			   if (authType != null && !authType.trim().equalsIgnoreCase("")){
				   if (authType.trim().equalsIgnoreCase("TOKEN")){
					   idValid = this.getOtpValidationByToken(conn, utente, belfiore, ot_prik);				   
				   }else if (authType.startsWith("IP")){
					   idValid = this.getOtpValidationByIp(conn, utente, belfiore, authType, remoteHost);
				   }
			   }
			   /*
			     * validazione OTP
			     */
			   if (idValid != null && idValid > 0){
				   /*
				    * Aggiorna stato OTP = USATA
				    */
				   this.setOtpValidation(conn, idValid);
				   
				   Boolean autenticato = this.gitAuthentication(conn, utente);
				  
				   if (autenticato){
							   for (int z=0; z<fonti.length; z++){
									
									List<String> enteList = new LinkedList<String>();
									enteList.add(  belfiore  );

									HashMap<String,String> listaPermessi = this.getPermissionOfUser(conn, utente, belfiore);

				//					TEST				
				//					listaPermessi = new HashMap<String, String>();
				//					listaPermessi.put("permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:CATASTO", "permission@-@diogene@-@Visualizzazione Fonti Dati@-@Fonte:CATASTO");

									CeTUser user = new CeTUser();
									user.setPermList(listaPermessi);
						            user.setUsername(  utente  );
						            user.setEnteList(enteList);
						            user.setCurrentEnte(  belfiore );

									if (GestionePermessi.autorizzato(user, "diogene", ITEM_VISUALIZZA_FONTI_DATI, "Fonte:"+ fonti[z]))
										autorizzato = true;
								}
								if (autorizzato){
									/*
									 * Valorizzo il bean di ricerca 
									 */
									RicercaOggettoCatDTO roc = new RicercaOggettoCatDTO();
									roc.setLimit(QRY_LIMIT);
									roc.setEnteId(  belfiore  );
									roc.setCodEnte(  belfiore  );
									roc.setSezione(sezione);
									roc.setFoglio(foglio);
									roc.setParticella(particella);
									roc.setUnimm(subalterno);
									boolean checkformat = false;
									if (dataValidita.matches("([0-9]{2})/([0-9]{2})/([0-9]{4})"))
									    checkformat=true;
									else
									   checkformat=false;
									if(checkformat){
										//String dateInString = "07/06/2013";
//										try {
										Date dateVal = formatter.parse(dataValidita);
//										System.out.println(dateVal);
//										System.out.println(formatter.format(dateVal));
//										} catch (ParseException e) {
//											e.printStackTrace();
//										}
										roc.setDtVal(dateVal);
									}else{
										/*
										 * TODO Formato data errato
										 */
									}
									/*
									 * Recupero lista immobili o il singolo sub 
									 * a seconda della valorizzazione del parametro 
									 * sub o meno
									 */
									Sititrkc sititrkc = new Sititrkc();
									List<Sititrkc> alSititrkc = catastoService.getListaTerreniByParams(roc);
									/*
									 * Per ogni terreno recupero i titolari						
									 */
									if (alSititrkc != null && alSititrkc.size()>0){
										
										Sititrkc trkc = null;
										for (int j=0; j<alSititrkc.size(); j++){
											trkc = alSititrkc.get(j); 
											if (trkc != null){
												UnitaTerreDTO terre = new UnitaTerreDTO();
												terre.setClasse( trkc.getClasseTerreno() );
												terre.setDataFineVal( trkc.getId().getsDataFine() );
												terre.setDescrizione( trkc.getAnnotazioni() );
												terre.setFoglio( new Long(trkc.getId().getFoglio()).toString() );
												terre.setNumero( trkc.getId().getParticella() );
												terre.setQualita( trkc.getDescQualita() );
												terre.setRedditoAgrario( trkc.getRedditoAgrario()!=null? trkc.getRedditoAgrario().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString():"" );
												terre.setRedditoDominicale( trkc.getRedditoDominicale()!=null? trkc.getRedditoDominicale().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString():"" );
												terre.setRendita( trkc.getRendita()!=null? trkc.getRendita().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString():"" );
												terre.setSubalterno( trkc.getId().getSub() );
												/*
												 * Per ogni terreno valorizzo i titolari
												 */
												RicercaOggettoCatDTO rocDto = new RicercaOggettoCatDTO();
												rocDto.setSezione(roc.getSezione());
												rocDto.setAltriSoggetti(false);
												rocDto.setEnteId( roc.getEnteId() );
												rocDto.setFoglio( new Long(trkc.getId().getFoglio()).toString() );
												rocDto.setParticella( trkc.getId().getParticella() );
												/*
												 * La data di validita nella ricerca dei titolari del terreno corrente è riferita
												 * al terreno stesso (t.dataFine): se non si imposta nulla la procedura setta il 99991231
												 * altrimenti cio che si è impostato
												 */
												rocDto.setDtVal(roc.getDtVal());
												
												//altri metodi riferiti ai terreni 
												//catastoService.getListaProprietariTerrByFPSDataRange(roc);
												//catastoService.getListaSoggettiStoriciTerreno(roc);
												//catastoService.getListaSoggettiTerrByFPSDataRange(roc);
												ArrayList<PersonaDTO> alPax = new ArrayList<PersonaDTO>();
												List<SoggettoDTO> lstTit = catastoService.getListaSoggettiAttualiTerreno(rocDto);
												if (lstTit != null && lstTit.size()>0){
													Iterator<SoggettoDTO> itTit = lstTit.iterator();
													//catastoService.getSoggettoByPkCuaa(rs);
													while(itTit.hasNext()){
														SoggettoDTO soggettoDto = itTit.next();
														PersonaDTO titolare = new PersonaDTO();
														titolare.setCodiFisc(soggettoDto.getCfSoggetto());
														titolare.setCodiPiva(soggettoDto.getPivaSoggetto());
														//titolare.setComuNasc(comuNasc);
														titolare.setDataFine(soggettoDto.getDataFinePossStr());
														titolare.setDataInizio(soggettoDto.getDataInizioPossStr());
														//titolare.setDataNasc(dataNasc);
														//titolare.setDescrizione(descrizione);
														titolare.setDescTipo(soggettoDto.getDescTipo());
														//titolare.setFlagPersFisica(flagPersFisica);
														titolare.setNome(soggettoDto.getNomeSoggetto());
														titolare.setPercPoss(soggettoDto.getQuota()!=null?soggettoDto.getQuota().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString():"");
														titolare.setRagiSoci(soggettoDto.getDenominazioneSoggetto() + " " + soggettoDto.getCognomeSoggetto());
														//titolare.setSesso(sesso);
														titolare.setTipoTitolo( soggettoDto.getTipo());
														titolare.setTitolo( soggettoDto.getTitolo() );

														alPax.add(titolare);
													}

												}
												terre.setAlPersone(alPax);
												output.add( terre );
											}
										}
										
										responseDto.setLstObjs( output );
										responseDto.setSuccess(true);

									}else{
										String descrizione = "Unita Terreni NON trovate!!!";
										responseDto.setSuccess(false);
										responseDto.setError(descrizione);
										System.out.println(descrizione);
									}

								}else{
									String descrizione = "Utente NON autorizzato ad accedere alle informazioni richieste!!!";
									responseDto.setSuccess(false);
									responseDto.setError(descrizione);
									System.out.println(descrizione);
								}
					}else{
						String descrizione = "Autenticazione fallita. Credenziali GIT errate o scadute!!!";
						responseDto.setSuccess(false);
						responseDto.setError(descrizione);
						System.out.println(descrizione);
					}
			   }else{
					String descrizione = "";
					if (authType != null && !authType.trim().equalsIgnoreCase("") && authType.trim().equalsIgnoreCase("TOKEN"))
						descrizione = "OTP private key NON VALIDATA!!!";
					if (authType != null && !authType.trim().equalsIgnoreCase("") && authType.startsWith("IP"))
						descrizione = "IP Remote Host NON VALIDO!!! " + remoteHost;
					responseDto.setSuccess(false);
					responseDto.setError(descrizione);
				    System.out.println(descrizione);
				   
			   }
			   if (conn != null)
				   conn.close();
			}
			catch (Exception t) {
				t.printStackTrace();
				String descrizione = t.getMessage();
				responseDto.setSuccess(false);
				responseDto.setError(descrizione);
			    System.out.println(descrizione);
			}
		}else{
			String descrizione = "Utente e/o Codice Belfiore NON VALORIZZATI!!! ";
			responseDto.setSuccess(false);
			responseDto.setError(descrizione);
		    System.out.println(descrizione);
	   }
		
		return responseDto;
	 }//------------------------------------------------------------------------
	
	
	
	
	
	
	


	
}
