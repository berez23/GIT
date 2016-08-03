package it.webred.mui.http.codebehind;

import it.webred.docfa.model.Docfa;
import it.webred.docfa.model.DocfaComunOggetto;
import it.webred.docfa.model.DocfaComunicazione;
import it.webred.mui.hibernate.HibernateUtil;
import it.webred.mui.http.AbstractPage;
import it.webred.mui.http.MuiBaseServlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.skillbill.logging.Logger;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class CodeBehinddocfaComunicazioneDetailPage extends AbstractPage {

	private static SimpleDateFormat yearParser = new SimpleDateFormat("yyyy");
	
	@Override
	protected boolean controlImpl(HttpServletRequest req,
			HttpServletResponse resp, MuiBaseServlet servlet)
			throws ServletException, IOException {
		BigDecimal iidComunicazione = BigDecimal.valueOf( Long.parseLong( req.getParameter("iidComunicazione")));
		Session session = HibernateUtil.currentSession();
		DocfaComunicazione comunicazione = this.recuperaComunicazione(iidComunicazione);
	    req.setAttribute("comunicazione",comunicazione);
	    
	    if("post".equalsIgnoreCase(req.getMethod())){
	    	
	    	//è stato premuto il tasto "SALVA" del dettaglio
	    	Enumeration enumeration = req.getParameterNames();
	    	while (enumeration.hasMoreElements()) {
	    		String element = (String) enumeration.nextElement();
				String values[] = req.getParameterValues(element);
	    		if(! element.startsWith("oggetto.")){
	    			//parametri di Comunicazione
					Logger.log().info(this.getClass().getName(), "comunicazione per il parametro: "+element);
					setValue(comunicazione, element, values);
	    		}
	    		else{
	    			//parametri per oggetto della comunicazione
					Logger.log().info(this.getClass().getName(), "oggetto per il parametro: "+element);
	    			Iterator<DocfaComunOggetto> oggettiIterator = comunicazione.getDocfaComunOggettos().iterator();
	    			//devo recuperare iid oggetto e nome attributo
	    			String appoIdOgg = element.substring(8);
	    			BigDecimal iidOggetto = new BigDecimal(appoIdOgg.substring(0,appoIdOgg.indexOf(".")));
	    			String fieldName = appoIdOgg.substring(appoIdOgg.indexOf(".")+1);
	    			Logger.log().info(this.getClass().getName(), "oggetto iid: "+iidOggetto.toString());
	    			Logger.log().info(this.getClass().getName(), "oggetto nomeField: "+fieldName);
	    			
	    			while (oggettiIterator.hasNext()) {
	    				DocfaComunOggetto oggetto =  oggettiIterator.next();
						if(oggetto.getIid() == iidOggetto ){
							setValue(oggetto,fieldName,values);
						}
					}
	    		}
	    		
	    		/* */
			}
	    	Transaction tx =session.beginTransaction();
	    	try {
    			session.saveOrUpdate(comunicazione);
    			Iterator<DocfaComunOggetto> oggettiIterator = comunicazione.getDocfaComunOggettos().iterator();
    			while (oggettiIterator.hasNext()) {
	    			session.saveOrUpdate(oggettiIterator.next());
				}
    			tx.commit();
			} catch (Exception e) {
				try {
					tx.rollback();
				} catch (HibernateException e1) {
				}
			}
			finally {
			}
			
	    }
		return true;
	}

	private void setValue(DocfaComunicazione comunicazione, String element, String[] values) {
		String objName = "comunicazione";
		Object obj = comunicazione;
		setValue(element, values, objName, obj,0);
	}

	private void setValue(DocfaComunOggetto oggetto, String element, String[] values) {
		String objName = "oggetto";
		Object obj = oggetto;
		setValue(element, values, objName, obj,0);
	}

	private void setValue(String element, String[] values, String objName, Object obj, int index) {
		Logger.log().info(this.getClass().getName(),objName+" per il parametro: "+element+"="+values+";size="+values.length);
		if(values.length >= index +1  ){
			try {
				/*if(element.toLowerCase().indexOf("data") != -1 || element.toLowerCase().indexOf("decorrenza") != -1){
					Date dateVal = null;
					if(values[index] != null && values[index].trim().length() > 0){
						dateVal  = MuiFornituraParser.dateParser.parse(values[index].replace("/","").replace("-",""));
					}
					BeanUtils.setProperty(obj,element,dateVal);
				}
				else 
				*/
				if(element.toLowerCase().indexOf("anno") != -1 || element.toLowerCase().indexOf("annoVar") != -1){
					Date dateVal = null;
					if(values[index] != null && values[index].trim().length() > 0){
						dateVal  = yearParser.parse(values[index].replace("/","").replace("-",""));
					}
					BeanUtils.setProperty(obj,element,dateVal);
				}
				else{
					BeanUtils.setProperty(obj,element,values[index] != null ? values[index].toUpperCase() : null);
				}
				Logger.log().info(this.getClass().getName(), objName+" per il parametro: "+element+"="+values+";value set="+values[index]);
			} catch (IllegalAccessException e) {
				Logger.log().error(this.getClass().getName(), "errore durante il salvataggio di "+objName+" per il parametro: "+element+"="+values[index], e);
			} catch (InvocationTargetException e) {
				Logger.log().error(this.getClass().getName(), "errore durante il salvataggio di "+objName+" per il parametro: "+element+"="+values[index], e);
			}
			catch (Throwable e) {
				Logger.log().error(this.getClass().getName(), "errore durante il salvataggio di "+objName+" per il parametro: "+element+"="+values[index], e);
			}
		}
	}
	
	private DocfaComunicazione recuperaComunicazione(BigDecimal iidComunicazione) 
	{
		Query query = null;
		query = HibernateUtil.currentSession().createQuery("select comunicazione  from  it.webred.docfa.model.DocfaComunicazione as comunicazione where  iidComunicazione = :iidComunicazione");
		query.setBigDecimal("iidComunicazione", iidComunicazione);
		Iterator cIterator = query.iterate();
		DocfaComunicazione comunicazione =(cIterator.hasNext()?(DocfaComunicazione)cIterator.next():null);
		if (comunicazione == null) {
			Logger.log().info(this.getClass().getName(),"comunicazione non trovata!");
		}
		//recupero data_registrazione
		String sql ="SELECT DISTINCT CEN.DATA_REGISTRAZIONE "+
					"FROM  DOCFA_DATI_CENSUARI	CEN,DOCFA_DATI_METRICI	MET "+
					"WHERE CEN.PROTOCOLLO_REGISTRAZIONE = ? "+
					"AND CEN.FORNITURA =  TO_DATE(?,'DD/MM/YYYY') "+
					"AND MET.identificativo_immobile(+) = CEN.identificativo_immobile";
		
		try
		{
			Context cont = new InitialContext();
			Context datasourceContext = (Context) cont.lookup("java:comp/env");
			DataSource theDataSource = (DataSource) datasourceContext.lookup("jdbc/mui");
			Connection conn = theDataSource.getConnection();
			PreparedStatement pstm = conn.prepareStatement(sql);
			pstm.setString(1, comunicazione.getIidProtocolloReg());
			pstm.setString(2, comunicazione.getIidFornitura());
			ResultSet rs = pstm.executeQuery();
			if (rs.next())
			{ 
				String dtReg = rs.getString("DATA_REGISTRAZIONE");
				comunicazione.setDataRegistrazione(dtReg.substring(6, 8)+"/"+dtReg.substring(4, 6)+"/"+dtReg.substring(0, 4));
			}
			rs.close();
			pstm.close();
			conn.close();
			
		}catch(Exception e)
		{
			Logger.log().info(this.getClass().getName(),"ERRORE SQL:"+e.getMessage());
		}
		
		
		return comunicazione;
		
	}


}
