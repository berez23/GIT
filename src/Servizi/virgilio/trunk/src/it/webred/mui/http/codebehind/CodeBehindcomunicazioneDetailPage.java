package it.webred.mui.http.codebehind;

import it.webred.mui.consolidation.ComunicazioneConverter;
import it.webred.mui.hibernate.HibernateUtil;
import it.webred.mui.http.AbstractPage;
import it.webred.mui.http.MuiBaseServlet;
import it.webred.mui.input.MuiFornituraParser;
import it.webred.mui.input.RowField;
import it.webred.mui.model.MiConsComunicazione;
import it.webred.mui.model.MiConsOggetto;
import it.webred.mui.model.MiDupSoggetti;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.skillbill.logging.Logger;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class CodeBehindcomunicazioneDetailPage extends AbstractPage {

	@Override
	protected boolean controlImpl(HttpServletRequest req,
			HttpServletResponse resp, MuiBaseServlet servlet)
			throws ServletException, IOException {
		Long iidSoggetto = Long.valueOf( req.getParameter("iidSoggetto"));
		Session session = HibernateUtil.currentSession();
		MiDupSoggetti soggetto = (MiDupSoggetti) session.load(MiDupSoggetti.class, iidSoggetto);
		ComunicazioneConverter converter = new ComunicazioneConverter();
		MiConsComunicazione comunicazione = converter.evalComunicazione(soggetto);
	    req.setAttribute("comunicazione",comunicazione);
	    if("post".equalsIgnoreCase(req.getMethod())){
	    	Enumeration enumeration = req.getParameterNames();
	    	while (enumeration.hasMoreElements()) {
	    		String element = (String) enumeration.nextElement();
				String values[] = req.getParameterValues(element);
	    		if(! element.startsWith("oggetto.")){
					Logger.log().info(this.getClass().getName(), "comunicazione per il parametro: "+element);
					setValue(comunicazione, element, values);
	    		}
	    		else{
					Logger.log().info(this.getClass().getName(), "oggetto per il parametro: "+element);
	    			RowField rf = new RowField();
	    			rf = MuiFornituraParser.getNextField(element,rf,'.');
					Logger.log().info(this.getClass().getName(), "rf.remaining per il parametro: "+rf.remaining);
	    			rf = MuiFornituraParser.getNextField(rf.remaining,rf,'.');
	    			long iidOggetto = Long.valueOf(rf.field);
					Logger.log().info(this.getClass().getName(), "rf.remaining per il parametro: "+rf.remaining);
//	    			rf = MuiFornituraParser.getNextField(rf.remaining,rf,'.');
	    			String fieldName = rf.remaining;
	    			Iterator<MiConsOggetto> oggettiIterator = comunicazione.getMiConsOggettos().iterator();
	    			while (oggettiIterator.hasNext()) {
						MiConsOggetto oggetto =  oggettiIterator.next();
						if(oggetto.getIid() == iidOggetto ){
							setValue(oggetto,fieldName,values);
						}
					}
	    		}
	    		Transaction tx =session.beginTransaction();
	    		try {
	    			session.saveOrUpdate(comunicazione);
	    			Iterator<MiConsOggetto> oggettiIterator = comunicazione.getMiConsOggettos().iterator();
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
	    }
		return true;
	}

	private void setValue(MiConsComunicazione comunicazione, String element, String[] values) {
		String objName = "comunicazione";
		Object obj = comunicazione;
		setValue(element, values, objName, obj,0);
	}

	private void setValue(MiConsOggetto oggetto, String element, String[] values) {
		String objName = "oggetto";
		Object obj = oggetto;
		setValue(element, values, objName, obj,0);
	}

	private void setValue(String element, String[] values, String objName, Object obj, int index) {
		Logger.log().info(this.getClass().getName(),objName+" per il parametro: "+element+"="+values+";size="+values.length);
		if(values.length >= index +1  ){
			try {
				if(element.toLowerCase().indexOf("data") != -1 || element.toLowerCase().indexOf("decorrenza") != -1){
					Date dateVal = null;
					if(values[index] != null && values[index].trim().length() > 0){
						dateVal  = MuiFornituraParser.dateParser.parse(values[index].replace("/","").replace("-",""));
					}
					BeanUtils.setProperty(obj,element,dateVal);
				}
				else if(element.toLowerCase().indexOf("anno") != -1){
					Date dateVal = null;
					if(values[index] != null && values[index].trim().length() > 0){
						dateVal  = MuiFornituraParser.yearParser.parse(values[index].replace("/","").replace("-",""));
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


}
