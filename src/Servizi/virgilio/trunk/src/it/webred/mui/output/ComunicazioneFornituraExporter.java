package it.webred.mui.output;

import it.webred.mui.hibernate.HibernateUtil;
import it.webred.mui.http.MuiApplication;
import it.webred.mui.input.MuiFornituraParser;
import it.webred.mui.model.MiConsComunicazione;
import it.webred.mui.model.MiConsOggetto;
import it.webred.mui.model.MiDupFornitura;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ComunicazioneFornituraExporter {

	private static String getVal(String in) {
		return (in != null ? in : "");
	}
	
	private String codIstat = "";
	private String siglaProv = "";
	private long conta2=0;
	private long conta3=0;
	private long conta4=0;
	private long conta1=0;
	private long conta5=0;
	
	public void dumpFornitura(MiDupFornitura fornitura, Writer writer)
			throws IOException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,SQLException,NamingException {
		Iterator comIterator = null;
		// comIterator = fornitura.getMiDupNotaTrases().iterator();
		Session session = HibernateUtil.currentSession();
		Query query = session.createQuery("select c from it.webred.mui.model.MiConsComunicazione as c where c.miDupFornitura = :fornitura order by c.iid asc");
		
		/*
		Query query = session.createQuery("select distinct c " +
										 "from it.webred.mui.model.MiConsComunicazione as c, " +
										 "it.webred.mui.model.MiConsOggetto as o " +
										 "where c.miDupFornitura = :fornitura " +
										 "and o.miConsComunicazione= c " +
										 "and (o.flagPossessoProprieta = True " +
										 	  "	OR o.flagPossessoUso = True " +
										 	  " OR o.flagPossessoUsufrutto = True " +
										 	  " OR o.altroPossesso like '%2%') " +
										 "order by c.iid asc");
		*/
		query.setParameter("fornitura", fornitura);
		comIterator = query.iterate();
		//exportDap(writer, comIterator);
		exportTracciato(fornitura,writer, comIterator);
		
		
	}

	private void exportDap(Writer writer, Iterator comIterator)
			throws IOException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		while (comIterator.hasNext()) {
			MiConsComunicazione com = (MiConsComunicazione) comIterator.next();
			writer.write("C|");
			writeField(writer, ""+com.getIid());
			writeField(writer, com.getDataComunicazione());
			writeField(writer, com.getCodiceFiscale());
			writeField(writer, com.getNome());
			writeField(writer, com.getCognome());
			writeField(writer, com.getDataNascita());
			writeField(writer, com.getComuneNascita());
			writeField(writer, com.getProvinciaNascita());
			writeField(writer, com.getIndirizzo());
			writeField(writer, com.getComune());
			writeField(writer, com.getCap());
			writeField(writer, com.getProvincia());
			writeField(writer, com.getIndirizzoDomicilio());
			writeField(writer, com.getComuneDomicilio());
			writeField(writer, com.getCapDomicilio());
			writeField(writer, com.getProvinciaDomicilio());
			writeField(writer, com.getCodiceFiscaleRL());
			writeField(writer, com.getNomeRL());
			writeField(writer, com.getCognomeRL());
			writeField(writer, com.getDataNascitaRL());
			writeField(writer, com.getComuneNascitaRL());
			writeField(writer, com.getProvinciaNascitaRL());
			writeField(writer, com.getIndirizzoRL());
			writeField(writer, com.getComuneRL());
			writeField(writer, com.getCapRL());
			writeField(writer, com.getProvinciaRL());
			writeField(writer, com.getIndirizzoDomicilioRL());
			writeField(writer, com.getComuneDomicilioRL());
			writeField(writer, com.getCapDomicilioRL());
			writeField(writer, com.getProvinciaDomicilioRL());
			writeField(writer, com.getFlagCuratoreFallimentare());
			writeField(writer, com.getFlagErede());
			writeField(writer, com.getFlagRappresentanteLegale());
			writeField(writer, com.getFlagTutore(), true);
			int oCount = 0;
			for (Iterator iter = com.getMiConsOggettos().iterator(); iter.hasNext();) {
				oCount++;
				MiConsOggetto ogg = (MiConsOggetto) iter.next();
				writer.write("O|");
				writeField(writer, com.getIid());
				writeField(writer, oCount);
				writeField(writer, ogg.getIid());
				writeField(writer, ogg.getFoglio());
				writeField(writer, ogg.getParticella());
				writeField(writer, ogg.getSubalterno());
				writeField(writer, ogg.getIndirizzo());
//				writeField(writer, ogg.getNumeroCivico());
				writeField(writer, ogg.getCategoria());
				writeField(writer, ogg.getClasse());
				writeField(writer, ogg.getNumeroProtocollo());
				writeField(writer, ogg.getSezione());
				writeField(writer, ogg.getAnno());
				writeField(writer, ogg.getCodiceVariazione());
				writeField(writer, ogg.getDescrizioneVariazione());
				writeField(writer, ogg.getFoglioVar());
				writeField(writer, ogg.getParticellaVar());
				writeField(writer, ogg.getSubalternoVar());
				writeField(writer, ogg.getCategoriaVar());
				writeField(writer, ogg.getClasseVar());
				writeField(writer, ogg.getNumeroProtocolloVar());
				writeField(writer, ogg.getSezioneVar());
				writeField(writer, ogg.getAnnoVar());
				writeField(writer, ogg.getFlagAbitazionePrincipale());
				writeField(writer, ogg.getFlagAbitazionePrincipaleNoMore());
				writeField(writer, ogg.getFlagAgricolturaDiretta());
				writeField(writer, ogg.getFlagAreaFabbricabile());
				writeField(writer, ogg.getFlagCococo());
				writeField(writer, ogg.getFlagConiugePensionato());
				writeField(writer, ogg.getFlagCostiContabili());
				writeField(writer, ogg.getFlagDetrazioneDelibera());
				writeField(writer, ogg.getFlagDetrazioneDeliberaNM());
				writeField(writer, ogg.getFlagDisoccupato());
				writeField(writer, ogg.getFlagFabbricato());
				writeField(writer, ogg.getFlagFabbricatoD());
				writeField(writer, ogg.getFlagImmobileEscluso());
				writeField(writer, ogg.getFlagInagibile());
				writeField(writer, ogg.getFlagInterinale());
				writeField(writer, ogg.getFlagInvalido());
				writeField(writer, ogg.getFlagMobilita());
				writeField(writer, ogg.getFlagPensionato());
				writeField(writer, ogg.getFlagPossessoDirittoAbitazione());
				writeField(writer, ogg.getFlagPossessoLeasing());
				writeField(writer, ogg.getFlagPossessoProprieta());
				writeField(writer, ogg.getFlagPossessoSuperficie());
				writeField(writer, ogg.getFlagPossessoUso());
				writeField(writer, ogg.getFlagPossessoUsufrutto());
				writeField(writer, ogg.getFlagRedditoDomenicale());
				writeField(writer, ogg.getFlagRenditaDefinitiva());
				writeField(writer, ogg.getFlagRenditaPresunta());
				writeField(writer, ogg.getFlagRiduzioneLocazione());
				writeField(writer, ogg.getFlagStorico());
				writeField(writer, ogg.getFlagTerrenoAgricolo());
				writeField(writer, ogg.getFlagValoreVenale());
				writeField(writer, ogg.getContitolariAbitazionePrincipale());
				writeField(writer, ogg.getPercentualePossesso());
				writeField(writer, ogg.getRedditoEuro());
				writeField(writer, ogg.getDecorrenza());
				writeField(writer, ogg.getMembriNucleoFamiliare(),true);
			}
		}
	}
	
	private void exportTracciato(MiDupFornitura fornitura, Writer writer, Iterator comIterator)
					throws IOException, IllegalAccessException,
							InvocationTargetException, NoSuchMethodException,SQLException,NamingException {
		
		String sqlIstatComu = "SELECT ISTATP||ISTATC AS CODICEISTAT,SIGLA_PROV FROM SITICOMU C " +
							  "WHERE UPPER(C.NOME) = UPPER(?) ";


		
		Context cont = new InitialContext();
		Context datasourceContext = (Context) cont.lookup("java:comp/env");
		DataSource theDataSource = (DataSource) datasourceContext.lookup("jdbc/mui");
		Connection conn = theDataSource.getConnection();
		
		PreparedStatement pstm = conn.prepareStatement(sqlIstatComu);
		pstm.setString(1,MuiApplication.descComune);
		ResultSet rsIstatComu = pstm.executeQuery();
		if (rsIstatComu.next()){
			codIstat = rsIstatComu.getString("CODICEISTAT");
		 	siglaProv = rsIstatComu.getString("SIGLA_PROV");
		}
		rsIstatComu.close();
		pstm.close();
		
		
		//record TIPO 0
		fillerField(writer, "0", 1);//campo 1
		fillerField(writer, 0, 3);//campo 2
		fillerField(writer, "", 26);//campo 3
		fillerField(writer, "Variazioni dichiarazioni ICI", 28);//campo 4
		String anno = Integer.toString(fornitura.getAnno());
		fillerField(writer,anno.substring(2),2); //campo 5
		fillerField(writer,MuiApplication.descComune,25);//campo6
		fillerField(writer,0,3); //campo 7
		fillerField(writer,fornitura.getDataEstrCons(),6);//campo 8 - data estrazione conservatoria
		fillerField(writer, "", 404,true);//campo 9
		
		//record TIPO 1
		conta1++;
		fillerField(writer, "1", 1); //campo 1
		fillerField(writer, "", 26);//campo 2
		fillerField(writer,MuiApplication.descComune,25);//campo 3
		fillerField(writer,siglaProv,2);//campo 4
		fillerField(writer, "", 444,true);//campo 5
		
		while (comIterator.hasNext()) {
					
			MiConsComunicazione com = (MiConsComunicazione) comIterator.next();
			
			//verifico se è una comunicazione da trattare
				if(verificaComunicazione(com)){
					//FRONTESPIZIO
					scriviRecordTipo2(conn,writer,com);
					
					//CONTITOLARI:per ora non vengono scritti!!!!
					scriviRecordTipo3(conn,writer,com);
					 
					//IMMOBILI
					scriviRecordTipo4(conn,writer,com);
				} 

 		}
		
		//record Tipo 5 - coda Comune
		conta5++;
		fillerField(writer, "5", 1);//campo 1
		fillerField(writer, "", 26);//campo 2
		fillerField(writer,MuiApplication.descComune,25);//campo 3
		fillerField(writer,siglaProv,2);//campo 4
		fillerField(writer,conta1+conta2+conta3+conta4+conta5,13);//?????? totale conta1-4 + conta5?
		fillerField(writer,conta2,13);//campo 6
		fillerField(writer,conta3,13);//campo 7
		fillerField(writer,conta4,13);//campo 8
		fillerField(writer, "", 392,true);//campo 9
		
		//record Tipo 6 - coda supporto
		fillerField(writer, "6", 1);//campo 1
		fillerField(writer, "", 26);//campo 2
		fillerField(writer, "Variazioni dichiarazioni ICI", 28);//campo 3
		fillerField(writer,anno.substring(2),2); //campo 4
		fillerField(writer,MuiApplication.descComune,25);//campo 5
		fillerField(writer, "0", 3);//campo 6 
		fillerField(writer,conta2,13);//campo 7
		fillerField(writer,conta3,13);//campo 8
		fillerField(writer,conta4,13);//campo 9
		fillerField(writer,conta1,13);//campo 10
		fillerField(writer, "", 361,true);//campo 11
		
		conn.close();
		
	}	
	
	//controllo se gli oggetti sono di proprieta', nuda prop., usufrutto,uso
	private boolean verificaComunicazione(MiConsComunicazione com){
		
		boolean tipoGiusto = false;
		for (Iterator iterator = com.getMiConsOggettos().iterator(); iterator.hasNext();) {
			MiConsOggetto ogg = (MiConsOggetto) iterator.next();
			if ( (ogg.getFlagPossessoProprieta()!=null && ogg.getFlagPossessoProprieta().booleanValue()==true) 
				||(ogg.getFlagPossessoUsufrutto()!=null && ogg.getFlagPossessoUsufrutto().booleanValue()==true)
				||(ogg.getFlagPossessoUso()!=null && ogg.getFlagPossessoUso().booleanValue()==true)
				||(ogg.getAltroPossesso()!=null && ogg.getAltroPossesso().indexOf("2") > -1)
			){
				tipoGiusto = true;
				break;
			}
		}
		return tipoGiusto;
	}
	
	//controllo se gli oggetti sono di proprieta', nuda prop., usufrutto,uso
	private BigDecimal calcolaICI(Connection conn,MiConsComunicazione com,BigDecimal rendita,String categoria)
			throws IOException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,SQLException,NamingException {
		
		BigDecimal valICI= rendita;
		
		String sqlICI = "SELECT MOLTIPLICATORE " +
						"FROM DUP_RIVAL_ICI " +
						"WHERE CATEGORIA||LPAD(NUMERO,2,'0') = ? " +
						"AND ANNO = ?";
		
		String anno = (com.getMiDupNotaTras().getDataValiditaAtto()).substring(4);
		String molt = "0";
		if (categoria != null && rendita != null){
			PreparedStatement pstm =conn.prepareStatement(sqlICI);
			pstm.setString(1, categoria);
			pstm.setString(2, anno);
			ResultSet rs = pstm.executeQuery();
			if (rs.next()){
				molt = rs.getString("MOLTIPLICATORE");
			}
			
			valICI = rendita.multiply( new BigDecimal(molt));
			
			rs.close();
			pstm.close();
			
		}
			
		return valICI;
	}
	
	private void scriviRecordTipo2(Connection conn, Writer writer,MiConsComunicazione com) 
			throws IOException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,SQLException,NamingException {
		
		//record TIPO 2
		conta2++;
		fillerField(writer, "2", 1);//campo 1
		fillerField(writer,0,3);//campo 2 (fisso a 0)
		// marcoric 18-9-2008 
		// parseInt da errore con codIstat =""
		// fillerField(writer,Integer.parseInt(codIstat),6);//campo 3 - istatp+istatc
		// dunque lo cambio
		int cod = 0;
		try {
			cod =  Integer.parseInt(codIstat);
		} catch (Exception e) {
			cod = 0;
		}
		fillerField(writer,cod,6);//campo 3 - istatp+istatc
		fillerField(writer,Long.toString(com.getMiDupNotaTras().getIid()),8);//campo 4 - numero nota
		fillerField(writer,1,6);//NUMERO PACCO
		String idSoggNota = com.getMiDupSoggetti().getIdSoggettoNota();
		fillerField(writer,Integer.parseInt(idSoggNota==null?"0":idSoggNota),6);//campo 6 - prog. soggetto in nota
		//fillerField(writer,1,7);//  
		//contribuente
		fillerField(writer,com.getDataComunicazione(),6);//campo 7
		fillerField(writer,com.getCodiceFiscale(),16);//campo 8
		fillerField(writer,com.getPrefisso()==null?0:Integer.parseInt(com.getPrefisso()),4);//campo 9
		fillerField(writer,com.getTelefono()==null?0:Integer.parseInt(com.getTelefono()),8);//campo 10
		fillerField(writer,com.getCognome(),60);//campo 11
		fillerField(writer,com.getNome(),20);//campo 12
		fillerField(writer,com.getDataNascita(),6);//campo 13
		String sesso = "";
		if (com.getSesso() != null){
			if (com.getSesso().equals("1"))
				sesso = "M";
			else
				sesso = "F";
		}
		fillerField(writer,sesso,1);//campo 14
		fillerField(writer,com.getComuneNascita(),25);//campo 15
		fillerField(writer,com.getProvinciaNascita(),2);//campo 16
		fillerField(writer,com.getIndirizzo(),35);//campo 17
		fillerField(writer,com.getCap(),5);//campo 18
		fillerField(writer,com.getComune(),25);//campo 19
		fillerField(writer,com.getProvincia(),2);//campo 20
		//denunciante
		fillerField(writer,com.getCodiceFiscaleRL(),16);//campo 21
		//NATURA CARICA
		String carica= "";
		if (com.getFlagCuratoreFallimentare() != null && com.getFlagCuratoreFallimentare().booleanValue() == true)
			carica = "CURATORE FALLIMENTARE";
		if (com.getFlagErede() != null && com.getFlagErede().booleanValue() == true)
			carica = "EREDE";
		if (com.getFlagRappresentanteLegale() != null && com.getFlagRappresentanteLegale().booleanValue() == true)
			carica = "RAPPRESENTANTE LEGALE";
		if (com.getAltraNatura() != null)
			carica = com.getAltraNatura();
		fillerField(writer, carica, 25);//campo 22 
		fillerField(writer,com.getCognomeRL()==null?"":com.getCognomeRL()+" "+com.getNomeRL()==null?"":com.getNomeRL(),60);//campo 23
		fillerField(writer,com.getIndirizzoRL(),35);//campo 24
		fillerField(writer,com.getCapRL(),5);//campo 25
		fillerField(writer,com.getComuneRL(),25);//campo 26
		fillerField(writer,com.getProvinciaRL(),2);//campo 27
		fillerField(writer, "", 84,true);//campo 28
	}
	
	private void scriviRecordTipo3(Connection conn, Writer writer,MiConsComunicazione com) 
	throws IOException, IllegalAccessException,
	InvocationTargetException, NoSuchMethodException,SQLException,NamingException {
		//per ora non scrivo nulla
		//conta3++;
	}
	
	private void scriviRecordTipo4(Connection conn, Writer writer,MiConsComunicazione com) 
	throws IOException, IllegalAccessException,
	InvocationTargetException, NoSuchMethodException,SQLException,NamingException {
		
		String sqlTerreno = "SELECT TI.* FROM MI_CONS_OGGETTO O,MI_CONS_COMUNICAZIONE C,MI_DUP_TERRENI_INFO TI " +
				"WHERE O.IID= ? " +
				"AND O.IID_COMUNICAZIONE = C.IID " +
				"AND C.IID_NOTA = TI.IID_NOTA " +
				"AND TRIM(O.FOGLIO) = TRIM(TI.FOGLIO) " +
				"AND TRIM(O.PARTICELLA) =  TRIM(TI.NUMERO) " +
				"AND NVL(TRIM(O.SUBALTERNO),'-') = NVL(TRIM(TI.SUBALTERNO),'-')";
		
		String sqlCatasto = "SELECT CATEGORIA,CLASSE FROM SITIUIU " +
							"WHERE FOGLIO = ? " +
							"AND PARTICELLA = LPAD(?,'0',5) " +
							"AND UNIMM = NVL(?,0) " +
							"AND DATA_INIZIO_VAL <= ? " +
							"AND DATA_FINE_VAL > ? ";
		int oCount = 0;
		int testa = 1;
		int coda =0;
		scriviTestaTipo4(conn,writer,com);
		
		for (Iterator iter = com.getMiConsOggettos().iterator(); iter.hasNext();) {
			oCount++;
			
			//devo scrivere un record tipo4 ogni tre oggetti 
			if ((oCount-1+3)/3>testa){
				testa++;
				scriviTestaTipo4(conn,writer,com);
			}
			
			MiConsOggetto ogg = (MiConsOggetto) iter.next();

			fillerField(writer, oCount,5);//campo 7
			//caratteristiche dell'immobile
			int tipol= 0;
			if (ogg.getFlagTerrenoAgricolo()!= null && ogg.getFlagTerrenoAgricolo().booleanValue() == true)
				tipol = 1;
			if (ogg.getFlagAreaFabbricabile()!= null && ogg.getFlagAreaFabbricabile().booleanValue()==true)
				tipol = 2;
			if (ogg.getFlagFabbricato()!= null && ogg.getFlagFabbricato().booleanValue()==true)
				tipol = 3;
			if (ogg.getFlagFabbricatoD()!= null && ogg.getFlagFabbricatoD().booleanValue()==true)
				tipol = 4;
			fillerField(writer, tipol, 1);//campo 8
			//formattazione indirizzo
			String indirizzo = ogg.getIndirizzo()!=null?ogg.getIndirizzo().replace("null", ""):"";
			fillerField(writer, indirizzo, 35);//campo 9
			if (tipol == 1){
				//recupero la partita
				PreparedStatement pstm = conn.prepareStatement(sqlTerreno);
				pstm.setLong(1,ogg.getIid());
				ResultSet rsT = pstm.executeQuery();
				if (rsT.next())
					fillerField(writer, rsT.getString("PARTITA"), 8);//campo 10
				rsT.close();
				pstm.close();
			}else
				fillerField(writer, "", 8);//campo 10
			fillerField(writer, ogg.getSezione(), 3);//campo 11
			fillerField(writer, ogg.getFoglio(),5);//campo 12
			fillerField(writer, ogg.getParticella(),5);//campo 13
			fillerField(writer, ogg.getSubalterno()==null?0:Integer.parseInt(ogg.getSubalterno()),4);//campo 14
			fillerField(writer, ogg.getNumeroProtocollo(),6);//campo 15
			//recupero anno di denuncia
			String anno = com.getMiDupNotaTras().getAnnoNota();
			if (anno != null){
				anno = anno.substring(2);
			}
			fillerField(writer,anno==null?0:Integer.parseInt(anno),2);//campo 16
			//verifica presenza categoria
			String categoria = ogg.getCategoria();
			String classe = ogg.getClasse();
			if (categoria == null || categoria.trim().equals("")){
				//cerco a catasto se fabbricato
				if (ogg.getFlagTerrenoAgricolo()== null || ogg.getFlagTerrenoAgricolo().booleanValue()==false){
					PreparedStatement pstm = conn.prepareStatement(sqlCatasto);
					pstm.setString(1,ogg.getFoglio());
					pstm.setString(2,ogg.getParticella());
					pstm.setString(3,ogg.getSubalterno());
					pstm.setDate(4,new java.sql.Date( (ogg.getMiConsComunicazione().getMiDupNotaTras().getDataValiditaAttoDate()).getTime()));
					pstm.setDate(5,new java.sql.Date( (ogg.getMiConsComunicazione().getMiDupNotaTras().getDataValiditaAttoDate()).getTime()));
					
					ResultSet rsC = pstm.executeQuery();
					if (rsC.next()){
						categoria = rsC.getString("CATEGORIA");
						if (classe == null)
							classe = rsC.getString("CLASSE");
					}
					rsC.close();
					pstm.close();
				}
				categoria = (categoria=="-"?null:categoria);
			}
			fillerField(writer, categoria,3);//campo 17
			fillerField(writer, classe,2);//campo 18
			fillerField(writer, "0",1);//campo 19
			
			//calcolo valore ICI
			BigDecimal valIci = calcolaICI(conn,com,ogg.getRedditoEuro(),categoria);
			//fillerField(writer,ogg.getRedditoEuro(),13);//campo 20
			fillerField(writer,valIci,13);//campo 20
			//flag valore provvisionrio
			if (ogg.getFlagRenditaDefinitiva()!= null && ogg.getFlagRenditaDefinitiva().booleanValue()==true )
				fillerField(writer,"0",1);//campo 21
			else
				fillerField(writer,"1",1);//campo 21
			fillerField(writer, ogg.getPercentualePossesso(),5);//campo 22
			
			//da popolare
			Calendar dataVal = Calendar.getInstance(); 
			dataVal.setTime(com.getMiDupNotaTras().getDataValiditaAttoDate());
			//mi serve la data di acquisto dell'oggetto per i soggetti contro... dove la trovo?
		
			int giornoVal = dataVal.get(Calendar.DAY_OF_MONTH);
			int meseVal = 0;
			if (giornoVal <= 14){
				meseVal = dataVal.get(Calendar.MONTH);
			}	
			else
				meseVal = (dataVal.get(Calendar.MONTH))+1;
			//mesi possesso
			if (ogg.getCodiceVariazione().startsWith("A")){
				fillerField(writer, 12-meseVal,2);//campo 23
			}
			else{
				fillerField(writer, meseVal,2);//campo 23
			}
			//mesi esclusione
			if (ogg.getFlagImmobileEscluso()!= null && ogg.getFlagImmobileEscluso().booleanValue()==true){
				if (ogg.getCodiceVariazione().startsWith("A")){
					fillerField(writer, 12-meseVal,2);//campo 24
				}
				else{
					fillerField(writer, meseVal,2);//campo 24
				}
			}
			else{
				fillerField(writer, 0,2);//campo 24
			}
				
				
			//mesi di riduzione
			if( (ogg.getFlagAgricolturaDiretta()!=null && ogg.getFlagAgricolturaDiretta().booleanValue()==true) 
				|| (ogg.getFlagInagibile()!=null && ogg.getFlagInagibile().booleanValue()==true)
				){
				if (ogg.getCodiceVariazione().startsWith("A")){
					fillerField(writer, 12-meseVal,2);//campo 25
				}
				else{
					fillerField(writer, meseVal,2);//campo 25
				}
			}
			else{
				fillerField(writer, 0,2);//campo 25
			}
			//importo detrazione abitazione principale
			if (ogg.getFlagAbitazionePrincipale()!=null && ogg.getFlagAbitazionePrincipale().booleanValue()==true){
					MathContext ctx = new MathContext(2, RoundingMode.HALF_EVEN);
					BigDecimal totDetr = new BigDecimal(104.00);
					BigDecimal oper = new BigDecimal(0);
					if (ogg.getPercentualePossesso()!=null)
						oper = (totDetr.divide(ogg.getPercentualePossesso(), ctx));
					if (ogg.getCodiceVariazione().startsWith("A")){
						fillerField(writer, 
									meseVal > 11 ? new BigDecimal(0) : oper.divide(new BigDecimal( 12-meseVal), ctx),
									6);//campo 26
					}
					else{
						fillerField(writer, 
									meseVal == 0 ? new BigDecimal(0) : oper.divide(new BigDecimal( meseVal), ctx),
									6);//campo 26
					}
			}else
				fillerField(writer, 0,6);//campo 26
			//fine da popolare
			
			fillerField(writer,"",2);//campo 27
			if (ogg.getCodiceVariazione().startsWith("A"))
				fillerField(writer, 0,1);//campo 28
			else
				fillerField(writer, 1,1);//campo 28
			fillerField(writer, 2,1);//campo 29
			fillerField(writer, 2,1);//campo 30
			//flag abitazione principale
			if (ogg.getFlagAbitazionePrincipale()!=null && ogg.getFlagAbitazionePrincipale().booleanValue()==true)
				fillerField(writer, 0,1);//campo 31
			else if ((ogg.getFlagAbitazionePrincipale()==null || ogg.getFlagAbitazionePrincipale().booleanValue()==false) 
					&&( ogg.getFlagAbitazionePrincipaleNoMore()!=null && ogg.getFlagAbitazionePrincipaleNoMore().booleanValue()==true)
					){
				fillerField(writer, 1,1);//campo 31
			}
			else if ((ogg.getFlagAbitazionePrincipale()==null || ogg.getFlagAbitazionePrincipale().booleanValue()==false)
					&& ( ogg.getFlagAbitazionePrincipaleNoMore()==null || ogg.getFlagAbitazionePrincipaleNoMore().booleanValue()==false)
			){
				fillerField(writer, 2,1);//campo 31
			}
			else{
				fillerField(writer, 2,1);//campo 31
			}
			fillerField(writer,"",1);//campo 32
			fillerField(writer, 2,1);//campo 33
			fillerField(writer, 2,1);//campo 34
			fillerField(writer,"",25);// DESCRIZIONE UFFICIO DEL REGISTRO
			
			coda++;
			if (coda%3 == 0){
				//chiudo il record
				scriviCodaTipo4(conn,writer,com);
			}
		
		}
		
		//controllo se il record è da chiudere
		if (coda%3 != 0){
			riempiCampiTipo4(conn,writer,com,coda);
			scriviCodaTipo4(conn,writer,com);
		}

		
	}
	
	private void riempiCampiTipo4(Connection conn, Writer writer,MiConsComunicazione com,int oggScritti) 
	throws IOException, IllegalAccessException,
	InvocationTargetException, NoSuchMethodException,SQLException,NamingException {
		
		for (int i=oggScritti%3;i<3;i++){
			fillerField(writer, 0,5);//campo 7
			fillerField(writer, 0, 1);//campo 8
			fillerField(writer, "", 35);//campo 9
			fillerField(writer, "", 8);//campo 10
			fillerField(writer, "", 3);//campo 11
			fillerField(writer, "",5);//campo 12
			fillerField(writer, "",5);//campo 13
			fillerField(writer, 0,4);//campo 14
			fillerField(writer, "",6);//campo 15
			fillerField(writer,0,2);//campo 16
			fillerField(writer, "",3);//campo 17
			fillerField(writer, "",2);//campo 18
			fillerField(writer, "0",1);//campo 19
			fillerField(writer,0,13);//campo 20
			fillerField(writer,"",1);//campo 21
			fillerField(writer, 0,5);//campo 22
			fillerField(writer, 0,2);//campo 23
			fillerField(writer, 0,2);//campo 24
			fillerField(writer, 0,2);//campo 25
			fillerField(writer, 0,6);//campo 26
			fillerField(writer,"",2);//campo 27
			fillerField(writer, 0,1);//campo 28
			fillerField(writer, 2,1);//campo 29
			fillerField(writer, 2,1);//campo 30
			fillerField(writer, 0,1);//campo 31
			fillerField(writer,"",1);//campo 32
			fillerField(writer, 2,1);//campo 33
			fillerField(writer, 2,1);//campo 34
			fillerField(writer,"",25);//????  DESCRIZIONE UFFICIO DEL REGISTRO
		}
		
	}
	
	private void scriviCodaTipo4(Connection conn, Writer writer,MiConsComunicazione com) 
	throws IOException, IllegalAccessException,
	InvocationTargetException, NoSuchMethodException,SQLException,NamingException {
		
		fillerField(writer,"",2);//campo 94
		fillerField(writer,"",2);//campo 95
		fillerField(writer,1,1);//campo 96
		fillerField(writer,"",27,true);//campo 97
	}
	
	private void scriviTestaTipo4(Connection conn, Writer writer,MiConsComunicazione com) 
	throws IOException, IllegalAccessException,
	InvocationTargetException, NoSuchMethodException,SQLException,NamingException {
		
		//record TIPO 4
		conta4++;
		fillerField(writer,"4",1);//campo 1
		fillerField(writer,0,3);//campo 2 (fisso a 0)
		// marcoric 18-9-2008 
		// parseInt da errore con codIstat =""
		// fillerField(writer,Integer.parseInt(codIstat),6);//campo 3 - istatp+istatc
		// dunque lo cambio
		int cod = 0;
		try {
			cod =  Integer.parseInt(codIstat);
		} catch (Exception e) {
			cod = 0;
		}
		
		fillerField(writer,cod,6);//campo 3 - istatp+istatc
		fillerField(writer,Long.toString(com.getMiDupNotaTras().getIid()),8);//campo 4 - numero nota
		fillerField(writer,1,6);// NUMERO PACCO
		fillerField(writer,conta4,7);//??? PROGRESSIVO RECORD 
	}
	
	
	
	/*
	 * metodi per formattare il record secondo il tracciato fornito
	 */
	private void fillerField(Writer writer, String field,int lung) throws IOException {
		fillerField(writer,field,lung,false);
	}
	
	private void fillerField(Writer writer, String field,int lung,boolean last) throws IOException {
		if (field == null)
			field="";
		for (int i = field.length(); i < lung; i++) {
			field = field.concat(" ");
		}
		writeFieldTracciato(writer,field.substring(0, lung),last);
	}
	
	private void fillerField(Writer writer, Integer field,int lung) throws IOException {
		fillerField(writer,field,lung,false);
	}
	
	private void fillerField(Writer writer, Integer field,int lung,boolean last) throws IOException {
		String intfield ="";
		if (field == null){
			field = new Integer("0");	
		}	
		//else{
			intfield = field.toString();
			for (int i = intfield.length(); i < lung; i++) {
				intfield = "0".concat(intfield);
			}
		//}
		writeFieldTracciato(writer,intfield,last);
	}
	
	private void fillerField(Writer writer, Boolean field,int lung) throws IOException {
		fillerField(writer,field,lung,false);	
	}

	private void fillerField(Writer writer, Boolean field,int lung,boolean last) throws IOException {
		String boofield = "";
		if(field != null){
			boofield = field?"1":"0";
		}
		for (int i = boofield.length(); i < lung; i++) {
			boofield = boofield.concat(" ");
		}
		writeFieldTracciato(writer,boofield,last);
		
	}

	private void fillerField(Writer writer, Date field,int lung) throws IOException {
		fillerField(writer,field,lung,false);
	}
	
	private void fillerField(Writer writer, Date field,int lung,boolean last) throws IOException {
		String dtfield ="";
		if (field == null){
			for (int i = 0; i < lung; i++) {
				dtfield = dtfield.concat("0");	
			}
		}	
		else
			dtfield =MuiFornituraParser.dateParser6.format(field);
		
		writeFieldTracciato(writer,dtfield,last);
		
	}
	private void fillerField(Writer writer, Long field,int lung) throws IOException {
		fillerField(writer,field,lung,false);
	}
	
	private void fillerField(Writer writer, Long field,int lung,boolean last) throws IOException {
		String intfield ="";
		if (field == null){
			field = new Long("0");	
		}	
		//else{
			intfield = field.toString();
			for (int i = intfield.length(); i < lung; i++) {
				intfield = "0".concat(intfield);
			}
		//}
		writeFieldTracciato(writer,intfield,last);
	}
	
	private void fillerField(Writer writer, BigDecimal field,int lung) throws IOException {
		fillerField(writer,field,lung,false);
	}
	
	private void fillerField(Writer writer, BigDecimal field,int lung,boolean last) throws IOException {
		String intfield ="";
		if (field == null){
			field = new BigDecimal("0.00");	
		}	
		String formato = ".00";
		for (int j = 0; j < lung-2; j++) {
			formato = "#".concat(formato);
		}
		DecimalFormat df1 = new DecimalFormat(formato);
		intfield = df1.format(field.doubleValue());
		intfield = intfield.replace(",", "");
		
		//else{
			//intfield = field.toString();
			for (int i = intfield.length(); i < lung; i++) {
				intfield = "0".concat(intfield);
			}
		//}
		writeFieldTracciato(writer,intfield,last);
	}
	
	private void fillerEuroField(Writer writer, BigDecimal field,int lung) throws IOException {
		fillerEuroField(writer,field,lung,false);
	}
	
	private void fillerEuroField(Writer writer, BigDecimal field,int lung,boolean last) throws IOException {
		String intfield ="";
		if (field == null){
			field = new BigDecimal("0.00");	
		}	
		
		
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		nf.setCurrency(Currency.getInstance("EUR"));
		intfield = nf.format(field.doubleValue());
		//field.setScale(2,BigDecimal.ROUND_UP);
		//else{
			//intfield = field.toString();
			for (int i = intfield.length(); i < lung; i++) {
				intfield = "0".concat(intfield);
			}
		//}
		writeFieldTracciato(writer,intfield,last);
	}
	
	




	private void writeField(Writer writer, String field) throws IOException {
		writeField(writer, field, false);
	}
	private void writeField(Writer writer, Boolean field) throws IOException {
		writeField(writer, field, false);
	}
	private void writeField(Writer writer, Date field) throws IOException {
		writeField(writer, field, false);
	}
	private void writeField(Writer writer, Date field, boolean last) throws IOException {
		if(field == null){
			writeField(writer, (String)null, last);
		}
		else{
			writeField(writer,MuiFornituraParser.dateParser.format(field), last);
		}
	}
	private void writeField(Writer writer, Integer field) throws IOException {
		writeField(writer, field, false);
	}
	private void writeField(Writer writer, Integer field, boolean last) throws IOException {
		if(field == null){
			writeField(writer, (String)null, last);
		}
		else{
			writeField(writer,field.toString(), last);
		}
	}
	private void writeField(Writer writer, Long field) throws IOException {
		writeField(writer, field, false);
	}
	private void writeField(Writer writer, Long field, boolean last) throws IOException {
		if(field == null){
			writeField(writer, (String)null, last);
		}
		else{
			writeField(writer,field.toString(), last);
		}
	}
	private void writeField(Writer writer, BigDecimal field) throws IOException {
		writeField(writer, field, false);
	}
	private void writeField(Writer writer, BigDecimal field, boolean last) throws IOException {
		if(field == null){
			writeField(writer, (String)null, last);
		}
		else{
			writeField(writer,field.toString(), last);
		}
	}
	private void writeField(Writer writer, Boolean field, boolean last) throws IOException {
		if(field == null){
			writeField(writer, "0", last);
		}
		else{
			writeField(writer,field?"1":"0", last);
		}
	}

	private void writeField(Writer writer, String field, boolean last)
			throws IOException {
		if(field!=null)	writer.write(field);
			writer.write(last ? "\n" : "|");
	}
	
	private void writeFieldTracciato(Writer writer, String field, boolean last)
			throws IOException {
		if(field!=null)	writer.write(field);
		writer.write(last ? "\n" : "");
	}
}
