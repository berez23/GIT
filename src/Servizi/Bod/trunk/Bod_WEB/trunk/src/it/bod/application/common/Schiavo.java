package it.bod.application.common;

import it.bod.application.beans.BodBean;
import it.bod.application.beans.BodFabbricatoBean;
import it.bod.application.beans.BodFornituraDocfaBean;
import it.bod.application.beans.BodIstruttoriaBean;
import it.bod.application.beans.BodSegnalazioneBean;
import it.bod.application.beans.BodUiuBean;
import it.doro.tools.Character;
import it.doro.tools.FilesHandler;
import it.doro.tools.TimeControl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

public class Schiavo {

	protected static Logger logger = Logger.getLogger(Schiavo.class.getName());
	
	public Schiavo() {
		// TODO Auto-generated constructor stub
	}

	public static ArrayList<BodBean> setLstBodFromLstObject(List lstObj) {
		ArrayList<BodBean> lstBod = new ArrayList<BodBean>();
		SimpleDateFormat sdf = new SimpleDateFormat ("dd/MM/yyyy");
		if (lstObj != null && lstObj.size()>0){
			BodBean bb = null;
			for (int i=0; i<lstObj.size(); i++){
				bb = new BodBean();
				Object[] objs = (Object[])lstObj.get(i);
				bb.setProtocollo((String)objs[0]);
				bb.setFornitura((String)objs[1]);
				bb.setDataVariazione(objs[2] != null?sdf.format((Date)objs[2]):"");
				bb.setDataRegistrazione(objs[17] != null?sdf.format((Date)objs[17]):"");
				bb.setSoppressione((BigDecimal)objs[3]);
				bb.setVariazione((BigDecimal)objs[4]);
				bb.setCostituzione((BigDecimal)objs[5]);
				bb.setDichiarante((String)objs[6]);
				bb.setTecnico((String)objs[7]);
				bb.setCausale((String)objs[8]);
				/*
				 * Stato: Non esaminata, presa in carico, chiusa
				 */
				/*
				 * Esito: L. 662, L. 80, L. 311, NA
				 */
				String stato = "NON ESAMINATA";
				String esito = "";
				BigDecimal presaInCarico = (BigDecimal)objs[9];
				BigDecimal chiusa = (BigDecimal)objs[14];
				/*
				 * presa in carico
				 * chiusa 
				 */
				if (presaInCarico != null && presaInCarico.byteValue() == 1){
					stato = "PRESA IN CARICO";
					if (chiusa != null && chiusa.byteValue() == 1){
						stato = "CHIUSA";	
					}
				}
				bb.setStato(stato);
				BigDecimal l662 = (BigDecimal)objs[10];
				BigDecimal l80 = (BigDecimal)objs[11];
				BigDecimal l311 = (BigDecimal)objs[12];
				BigDecimal na = (BigDecimal)objs[13];
				if (l662 != null && l662.byteValue() == 1){
					esito += "L. 662 ";
				}
				if (l80 != null && l80.byteValue() == 1){
					esito += "L. 80 ";
				}
				if (l311 != null && l311.byteValue() == 1){
					esito += "L. 311 ";
				}
				if (na != null && na.byteValue() == 1){
					//esito += "N.A. ";
					esito += "OK ";
				}
				bb.setEsito(esito);
				bb.setOperatore((String)objs[15]);
				bb.setCollaudo( (String)objs[18] );
				
				lstBod.add(bb);
			}
		}
		return lstBod;
	}//-------------------------------------------------------------------------

	public static ArrayList<String> setLstStringFromLstObject(List lstObj, int nCampiInSelect) {
		ArrayList<String> lstDg = new ArrayList<String>();
		if (lstObj != null && lstObj.size()>0){
			String dg = "";
			for (int i=0; i<lstObj.size(); i++){
				Object[] objs = (Object[])lstObj.get(i);
				for (int index=0; index<nCampiInSelect; index++){
					if (objs[index] == null){
						dg += "__ ";
					}else if (objs[index] instanceof String){
						dg += "__" + (String)objs[index];						
					}else if(objs[index] instanceof BigDecimal){
						dg += "__" + (BigDecimal)objs[index];
					}else if(objs[index] instanceof Date){
						SimpleDateFormat sdf = new SimpleDateFormat ("dd/MM/yyyy");
						dg += "__" + objs[index] != null?sdf.format((Date)objs[index]):"";
					}
				}
				lstDg.add(dg);
			}
		}
		return lstDg;
	}//-------------------------------------------------------------------------

	public static String fromDescrToCode(String desc, Hashtable<String, String> ht){
		String cod = "";
		if (ht != null && ht.size()>0){
			Enumeration<String> en = ht.keys();
			while(en.hasMoreElements()){
				String key = (String)en.nextElement();
				String descrizione = Character.checkNullString(ht.get(key));
				if (descrizione.trim().equalsIgnoreCase(desc.trim())){
					cod = key;
					break;
				}
				
			}
		}
		return cod;
	}//-------------------------------------------------------------------------
	
	public static String fromCodeToDescr(String code, Hashtable<String, String> ht){
		String descr = "";
		if (ht != null && ht.size()>0){
			Enumeration<String> en = ht.keys();
			while(en.hasMoreElements()){
				String key = (String)en.nextElement();
				if (code.trim().equalsIgnoreCase(key)){
					descr = Character.checkNullString(ht.get(key));
					break;
				}
				
			}
		}
		return descr;
	}//-------------------------------------------------------------------------
	
	public static String[] unzipper(String targetFolder, String sourcePathFile) throws Exception{
		String[] decompresso = new String[]{"false", ""};

		byte[] buffer = new byte[1024];
		 
    	//create output directory is not exists
    	File folder = new File(targetFolder);
    	if(!folder.exists()){
    		folder.mkdir();
    	}
 
    	//get the zip file content
    	ZipInputStream zis = new ZipInputStream(new FileInputStream(sourcePathFile));
    	
    	//get the zipped file list entry
    	ZipEntry ze = zis.getNextEntry();
 
    	while(ze!=null){
 
    	   decompresso[1] = ze.getName();
           File newFile = new File(targetFolder + File.separator + decompresso[1]);
 
            //create all non exists folders
            //else you will hit FileNotFoundException for compressed folder
            new File(newFile.getParent()).mkdirs();
 
            FileOutputStream fos = new FileOutputStream(newFile);             
 
            int len;
            while ((len = zis.read(buffer)) > 0) {
            	fos.write(buffer, 0, len);
            }
            decompresso[0] = "true";
            
            fos.close();   
            ze = zis.getNextEntry();
            
    	}
    	
    	System.out.println("Unzip del file "  + sourcePathFile + " eseguito con successo" );
 
        zis.closeEntry();
    	zis.close();
 
		return decompresso;
	}//-------------------------------------------------------------------------
	
	public static boolean zipper(String targetFolder, String sourcePathFile, String fileName){
		boolean compresso = false;
		try{
			
			logger.info("Schiavo zipper target:"+targetFolder);
			logger.info("Schiavo zipper sourcePathFile:"+sourcePathFile);
			logger.info("Schiavo zipper fileName:"+fileName);
		
				
				// definiamo l'output previsto che sarà un file in formato zip
				File zipFolder = new File(targetFolder);
				boolean creato = false;
				if (zipFolder.exists())
					creato = true;
				else
					creato = zipFolder.mkdir();
				
				FileOutputStream fos = new FileOutputStream(targetFolder + "/outFile.zip");
				//CheckedOutputStream cos = new CheckedOutputStream(fos, new Adler32() );
				CheckedOutputStream cos = new CheckedOutputStream(fos, new CRC32() );
				
				//Dove scrivere il file zip
				ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(cos));
	
				// definiamo il buffer per lo stream di bytes 
				byte[] data = new byte[1024];               
				
				// indichiamo il PATH del file che subirà la compressione
				BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourcePathFile));
	
				// processo di compressione nel file CHIAMATO come di seguito
				int count;
				zos.putNextEntry(new ZipEntry(fileName));
				while((count = bis.read(data, 0, 1024)) != -1){      
					zos.write(data, 0, count);
				}
				zos.closeEntry();

				bis.close();
			    
			    zos.flush();
			    zos.close();
				
			    cos.flush();
			    cos.close();
			    
			    fos.flush();
			    fos.close();

				compresso = true;
				logger.info("File is zipped successfully - Checksum: " + cos.getChecksum().getValue() );
			    
		}catch(FileNotFoundException fnfe){
			logger.error(fnfe.getMessage(),fnfe);
		}catch(IOException ioe){
			logger.error(ioe.getMessage(),ioe);
		}catch(Exception ex){
			logger.error(ex.getMessage(),ex);
		}
		return compresso;
	}//-------------------------------------------------------------------------
	
	public static boolean ziplus(String targetFolder, List listaFiles, String outFileNoExtension){
		boolean compresso = false;
		compresso = true;
				
		logger.info("Schiavo target:"+targetFolder);
		logger.info("Schiavo outFileNoExtension:"+outFileNoExtension);
		
		try{
			// definiamo l'output previsto che sarà un file in formato zip
			File zipFolder = new File(targetFolder);
			boolean creato = false;
			if (zipFolder.exists())
				creato = true;
			else
				creato = zipFolder.mkdir();
			
			FileOutputStream fos = new FileOutputStream(targetFolder + "/" + outFileNoExtension + ".zip", true);
			//CheckedOutputStream cos = new CheckedOutputStream(fos, new Adler32() );
			CheckedOutputStream cos = new CheckedOutputStream(fos, new CRC32() );
			//Dove scrivere il file zip
			ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(cos));

			// definiamo il buffer per lo stream di bytes 
			byte[] data = new byte[1024];               
			for (int i=0; i<listaFiles.size(); i++){
				// indichiamo il PATH del file che subirà la compressione
				
				String pathFileName = (String)listaFiles.get(i);
				logger.info("Schiavo ziplus lista:"+pathFileName);
				BufferedInputStream bis = new BufferedInputStream(new FileInputStream( pathFileName ));
				int start = pathFileName.lastIndexOf("\\");
				if (start == -1)
					start = pathFileName.lastIndexOf('/');
				String fileName = pathFileName.substring(start+1, pathFileName.length());
				// processo di compressione nel file CHIAMATO come di seguito
				int count;
				zos.putNextEntry(new ZipEntry(fileName));
				while((count = bis.read(data, 0, 1024)) != -1){      
					zos.write(data, 0, count);
				}
				zos.closeEntry();
				
			    bis.close();
			}

		    zos.flush();
		    zos.close();
		    
		    cos.flush();
		    cos.close();
		    
		    fos.flush();
		    fos.close();
		    
			compresso = true;
			logger.info("File is zipped successfully - Checksum: " + cos.getChecksum().getValue() );
		    
		}catch(FileNotFoundException fnfe){
			logger.error(fnfe.getMessage(),fnfe);
		}catch(IOException ioe){
			logger.error(ioe.getMessage(),ioe);
		}catch(Exception ex){
			logger.error(ex.getMessage(),ex);
		}
		return compresso;
	}//-------------------------------------------------------------------------
	
	public static Hashtable createXMLfornitura(String xmlPath, Hashtable htRpt) {

		String codiceEnte = (String)htRpt.get("codiceEnte");
		String progressivo = (String)htRpt.get("progressivo");
		String xmlFile = xmlPath + codiceEnte + "_" + Character.fillUpZeroInFront(progressivo, 4) + ".xml";
		Hashtable htFornitura = new Hashtable();
		try{
			FileOutputStream out = new FileOutputStream(xmlFile.trim());
			StringBuffer sb = new StringBuffer(250);

			List lista = (List)htRpt.get("listaIstruttorie");
			Integer totAllegati = 0;
            sb.append("<?xml version=\"1.0\" encoding=\"iso-8859-1\" ?>");
            sb.append((char)13);
            sb.append((char)10);
			//<SegnalazioniL80 xmlns="http://www.agenziaterritorio.it/SegnalazioniL80.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.agenziaterritorio.it/SegnalazioniL80.xsd SegnalazioniL80.xsd">

            sb.append("<SegnalazioniL80 ");
            sb.append("xmlns=\"http://www.agenziaterritorio.it/SegnalazioniL80.xsd\" ");
            sb.append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ");
            sb.append("xsi:schemaLocation=\"http://www.agenziaterritorio.it/SegnalazioniL80.xsd SegnalazioniL80.xsd\"> ");
            sb.append((char)13);
            sb.append((char)10);

            sb.append("<Comune>" + checkNull(codiceEnte) + "</Comune>");
            sb.append((char)13);
            sb.append((char)10);
            
			if (lista != null && lista.size()>0){
				Set<BodFornituraDocfaBean> setFornitureDocfa = new HashSet<BodFornituraDocfaBean>();
				BodFornituraDocfaBean fornituraDocfa = null;
				for (int i=0; i<lista.size(); i++){
					BodIstruttoriaBean istruttoria = (BodIstruttoriaBean)lista.get(i);
					if (istruttoria != null){
						Set<BodSegnalazioneBean> segnalazioni = istruttoria.getSegnalazioni();
						Date dataFornitura = istruttoria.getFornitura();
						String aaaa = TimeControl.getYear(new Timestamp(dataFornitura.getTime()));
						if (segnalazioni != null){
							Iterator<BodSegnalazioneBean> itSeg = segnalazioni.iterator();
							while (itSeg.hasNext()){
								BodSegnalazioneBean segnalazione = itSeg.next();
								if (segnalazione != null){
					            	if (segnalazione.getNomeFile() != null && !segnalazione.getNomeFile().trim().equalsIgnoreCase("")){
					            		totAllegati++;
					            		fornituraDocfa = new BodFornituraDocfaBean();
					            		fornituraDocfa.setPathFileName(segnalazione.getNomeFile());
					            		fornituraDocfa.setIstFk(istruttoria.getIdIst());
					            		fornituraDocfa.setDescrizione(FilesHandler.fromPathToName(segnalazione.getNomeFile()));
					            		setFornitureDocfa.add(fornituraDocfa);
					            	}

									sb.append("<Segnalazione>");
						            sb.append((char)13);
						            sb.append((char)10);

							            sb.append("<ProtocolloRegistrazione>" + segnalazione.getProtocollo() + "_" + aaaa + "</ProtocolloRegistrazione>");
						            	sb.append((char)13);
						            	sb.append((char)10);
						            	sb.append("<EsitoControllo>");
						            	sb.append((char)13);
						            	sb.append((char)10);
										            			            
						            	if (segnalazione.getEsitoControllo().trim().equalsIgnoreCase("INCOERENTE")){
							            	sb.append("<DichiarazioneIncoerente>");
						            	}else if (segnalazione.getEsitoControllo().trim().equalsIgnoreCase("COMPLETA") || segnalazione.getEsitoControllo().trim().equalsIgnoreCase("DOCUMENTALE")){
						            		sb.append("<DichiarazioneCoerente>");
						            	}else if (segnalazione.getEsitoControllo().trim().equalsIgnoreCase("ASSENZA")){
						            		sb.append("<AssenzaInformazioni>");
						            	}
						            	sb.append((char)13);
						            	sb.append((char)10);			            
	
							            	if (segnalazione.getEsitoControllo().trim().equalsIgnoreCase("INCOERENTE")){
							            		/*
							            		 * Recupero Fabbricati e li itero
							            		 */
							           
/*							            		Set<BodFabbricatoBean> setFabbricati = segnalazione.getFabbricati();
							
							            		if (setFabbricati != null && setFabbricati.size()>0){
							            			Iterator<BodFabbricatoBean> itFab = setFabbricati.iterator();
							            			while(itFab.hasNext()){
							            				BodFabbricatoBean fab = itFab.next();
							            				if (fab != null){
									            			sb.append("<Fabbricato>");
											            	sb.append((char)13);
											            	sb.append((char)10);
										            			sb.append("<IdentificativiFabbricato>");
												            	sb.append((char)13);
												            	sb.append((char)10);

										            			sb.append("<SezioneAmministrativa>" + checkNull(fab.getAmministrativa()) + "</SezioneAmministrativa>");
												            	sb.append((char)13);
												            	sb.append((char)10);
												            	sb.append("<SezioneUrbana>" + checkNull(fab.getSezione()) + "</SezioneUrbana>");
												            	sb.append((char)13);
												            	sb.append((char)10);
										            			sb.append("<Foglio>" + checkNull(fab.getFoglio()) + "</Foglio>");
												            	sb.append((char)13);
												            	sb.append((char)10);
										            			sb.append("<Numero>" + checkNull(fab.getParticella()) + "</Numero>");
												            	sb.append((char)13);
												            	sb.append((char)10);
										            			sb.append("<Denominatore>" + checkNull(fab.getDenominatore()) + "</Denominatore>");
												            	sb.append((char)13);
												            	sb.append((char)10);
										            			sb.append("<Edificialita>" + checkNull(fab.getEdificialita()) + "</Edificialita>");
												            	sb.append((char)13);
												            	sb.append((char)10);
												            	
												            	sb.append("</IdentificativiFabbricato>");
												            	sb.append((char)13);
												            	sb.append((char)10);
												            	sb.append("<Sopralluogo>" + checkNull(segnalazione.getSopralluogo()) + "</Sopralluogo>");
												            	sb.append((char)13);
												            	sb.append((char)10);
												            	String data = TimeControl.formatDate(segnalazione.getDataFineLavori());
												            	sb.append("<UltimazioneLavori>" + checkNull(data) + "</UltimazioneLavori>");
												            	sb.append((char)13);
												            	sb.append((char)10);
												            	sb.append("<NomeAllegato>" + checkNull(segnalazione.getNomeFile().substring(segnalazione.getNomeFile().lastIndexOf("/") + 1)) + "</NomeAllegato>");
												            	sb.append((char)13);
												            	sb.append((char)10);
												            	
									            			sb.append("</Fabbricato>");
											            	sb.append((char)13);
											            	sb.append((char)10);
							            				}
							            			}
							            		}*/
							            		/*
								            	 * Recupero uiu e li itero
								            	 */
							            		Set<BodUiuBean> setUius = segnalazione.getUius();
							            		if (setUius != null && setUius.size()>0){
							            			Iterator<BodUiuBean> itUiu = setUius.iterator();
							            			Hashtable<String, String> htAnnex = new Hashtable<String, String>();
							            			while(itUiu.hasNext()){
							            				BodUiuBean uiu = itUiu.next();
							            				/*
							            				 * Oltre a non essere nulla, la UIU deve essere incoerente altrimenti
							            				 * non deve essere presente nella fornitura XML
							            				 */
							            				if (uiu != null && (uiu.getIncConsistenza() || uiu.getIncDestinazione() || uiu.getIncClassamento() || uiu.getIncPlanimetria()) ){
									            		
									            			sb.append("<UIU>");
											            	sb.append((char)13);
											            	sb.append((char)10);
										            			sb.append("<IdentificativiUIU>");
												            	sb.append((char)13);
												            	sb.append((char)10);
	
											            		if (uiu.getSezione() != null){
													            	sb.append("<SezioneUrbana>" + Character.checkNullString( uiu.getSezione() ) + "</SezioneUrbana>");
													            	sb.append((char)13);
													            	sb.append((char)10);												            			
											            		}
											            		if (uiu.getFoglio() != null){
											            			sb.append("<Foglio>" + Character.checkNullString( uiu.getFoglio() ) + "</Foglio>");
													            	sb.append((char)13);
													            	sb.append((char)10);												            			
											            		}
											            		if (uiu.getParticella() != null){
											            			sb.append("<Numero>" + Character.checkNullString( uiu.getParticella() ) + "</Numero>");
													            	sb.append((char)13);
													            	sb.append((char)10);												            			
											            		}
											            		if (uiu.getDenominatore() != null){
											            			sb.append("<Denominatore>" + Character.checkNullString( uiu.getDenominatore() ) + "</Denominatore>");
													            	sb.append((char)13);
													            	sb.append((char)10);												            			
											            		}
											            		if (uiu.getEdificialita() != null){
											            			sb.append("<Edificialita>" + Character.checkNullString( uiu.getEdificialita() ) + "</Edificialita>");
													            	sb.append((char)13);
													            	sb.append((char)10);												            			
											            		}
											            		if (uiu.getSubalterno() != null){
											            			sb.append("<Subalterno>" + Character.checkNullString( uiu.getSubalterno() ) + "</Subalterno>");
													            	sb.append((char)13);
													            	sb.append((char)10);												            			
											            		}
												            	
										            			sb.append("</IdentificativiUIU>");
												            	sb.append((char)13);
												            	sb.append((char)10);
												            	sb.append("<Incoerenze>");
												            	sb.append((char)13);
												            	sb.append((char)10);
												            	
													            	sb.append("<Consistenza>" + checkNull(uiu.getIncConsistenza()) + "</Consistenza>");
													            	sb.append((char)13);
													            	sb.append((char)10);
											            			sb.append("<Destinazione>" + checkNull(uiu.getIncDestinazione()) + "</Destinazione>");
													            	sb.append((char)13);
													            	sb.append((char)10);
											            			sb.append("<Classamento>" + checkNull(uiu.getIncClassamento()) + "</Classamento>");
													            	sb.append((char)13);
													            	sb.append((char)10);
											            			sb.append("<Planimetria>" + checkNull(uiu.getIncPlanimetria()) + "</Planimetria>");
													            	sb.append((char)13);
													            	sb.append((char)10);
													            	
													            	/*
													            	 * UltimazioneLavori: la data deve essere indicata se precede la data di
																	 * registrazione del documento, presente nei file forniti dall'Agenzia, di oltre 30
																	 * giorni. 
													            	 */
													            	
													            	/*
													    			 * 22-08-2012: In seguito a segnalazione di Chiare, commentata la valorizzazione automatica di DatafineLavori, 
													    			 *          poiché il file xml non viene validato dal portale dell'agenzia se presente il tag Ultimazione Lavori
													    			*/
													            	
													            	/*if (segnalazione.getDataFineLavori() != null){
												            			sb.append("<UltimazioneLavori>" + TimeControl.parsItalyDate( segnalazione.getDataFineLavori() ) + "</UltimazioneLavori>");
														            	sb.append((char)13);
														            	sb.append((char)10);
													            	}*/
												            	
													            	sb.append("</Incoerenze>");
													            	sb.append((char)13);
													            	sb.append((char)10);
													            	sb.append("<SopralluogoUiu>" + checkNull(segnalazione.getSopralluogo()) + "</SopralluogoUiu>");
													            	sb.append((char)13);
													            	sb.append((char)10);
												            	
												            	if (htAnnex != null && segnalazione.getNomeFile() != null && !htAnnex.containsKey(segnalazione.getNomeFile().trim())){
													            	sb.append("<NomeAllegato>" + segnalazione.getNomeFile().substring(segnalazione.getNomeFile().lastIndexOf("/") + 1) + "</NomeAllegato>");
													            	sb.append((char)13);
													            	sb.append((char)10);												            		
													            	htAnnex.put(segnalazione.getNomeFile().trim(), segnalazione.getNomeFile().trim());
												            	}
												            	
	
									            			sb.append("</UIU>");
											            	sb.append((char)13);
											            	sb.append((char)10);
							            					}
							            			}
							            		}
							            		
							            	}else if (segnalazione.getEsitoControllo().trim().equalsIgnoreCase("COMPLETA") || segnalazione.getEsitoControllo().trim().equalsIgnoreCase("DOCUMENTALE")){
							            		if (segnalazione.getEsitoControllo().trim().equalsIgnoreCase("COMPLETA"))
							            			sb.append(" <CoerenzaCompleta>true</CoerenzaCompleta>");
							            		if (segnalazione.getEsitoControllo().trim().equalsIgnoreCase("DOCUMENTALE"))
							            			sb.append(" <CoerenzaDocumentale>true</CoerenzaDocumentale>");
							            	}else if (segnalazione.getEsitoControllo().trim().equalsIgnoreCase("ASSENZA")){
							            		sb.append("true");
							            	}
							            	sb.append((char)13);
							            	sb.append((char)10);

						            	
						            	if (segnalazione.getEsitoControllo().trim().equalsIgnoreCase("INCOERENTE")){
							            	sb.append("</DichiarazioneIncoerente>");
						            	}else if (segnalazione.getEsitoControllo().trim().equalsIgnoreCase("COMPLETA") || segnalazione.getEsitoControllo().trim().equalsIgnoreCase("DOCUMENTALE")){
						            		sb.append("</DichiarazioneCoerente>");
						            	}else if (segnalazione.getEsitoControllo().trim().equalsIgnoreCase("ASSENZA")){
						            		sb.append("</AssenzaInformazioni>");
						            	}
						            	sb.append((char)13);
						            	sb.append((char)10);

						            	sb.append("</EsitoControllo>");
						            	sb.append((char)13);
						            	sb.append((char)10);

							        sb.append("</Segnalazione>");
						            sb.append((char)13);
						            sb.append((char)10);

								}
							}
							if (setFornitureDocfa != null && setFornitureDocfa.size()>0){
								fornituraDocfa = new BodFornituraDocfaBean();
			            		fornituraDocfa.setPathFileName(xmlFile);
			            		fornituraDocfa.setIstFk(istruttoria.getIdIst());
			            		setFornitureDocfa.add(fornituraDocfa);
			            		fornituraDocfa.setDescrizione(FilesHandler.fromPathToName(xmlFile));
								htFornitura.put("setFornitureDocfa", setFornitureDocfa);								
							}
						}
					}
				}
			}
            sb.append("<TotaleAllegati>" + totAllegati + "</TotaleAllegati>");
            sb.append((char)13);
            sb.append((char)10);
			sb.append("</SegnalazioniL80> ");
            
            out.write(sb.toString().getBytes());
            out.flush();
            out.close();
            
            htFornitura.put("xmlFile", xmlFile);

		}catch(FileNotFoundException fnfe){
			logger.error(fnfe.getMessage(),fnfe);
		}catch(IOException ioe){
			logger.error(ioe.getMessage(),ioe);
		}catch(Exception ex){
			logger.error(ex.getMessage(),ex);
		}
		return htFornitura;
	}//-------------------------------------------------------------------------
	
	public static boolean usersComparator(String utenteJosso, String utenteApp){
		boolean match = false;
		logger.info("XXX CONFRONTO UTENTE: JOSSO= " + utenteJosso + " APPS=" + utenteApp);
		if ( utenteJosso.trim().equalsIgnoreCase(utenteApp.trim()) ){
			match = true;
		}
		return match;
	}//-------------------------------------------------------------------------

	private static String checkNull(Object obj){
		String s = obj!=null ? obj.toString() : "";
		return s;
	}//-------------------------------------------------------------------------
	


}
