package it.webred.mui.http.codebehind;

import it.webred.mui.MuiException;
import it.webred.mui.hibernate.HibernateUtil;
import it.webred.mui.http.AbstractPage;
import it.webred.mui.http.MuiApplication;
import it.webred.mui.http.MuiBaseServlet;
import it.webred.mui.http.cache.CacheRefresher;
import it.webred.mui.model.CodiceErroreImport;
import it.webred.mui.model.MiDupFabbricatiInfo;
import it.webred.mui.model.MiDupNotaTras;
import it.webred.mui.model.MiDupSoggetti;
import it.webred.mui.model.MiDupTerreniInfo;
import it.webred.mui.model.MiDupTitolarita;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.skillbill.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class CodeBehindnoteDetailPage extends AbstractPage implements CacheRefresher {

	public void init() {
		MuiApplication.getMuiApplication().add(R0602_VARNAME,this);
	}

	public Object doRefresh() {
		Session session;
		Transaction tx;
		session = HibernateUtil.currentSession();
	    tx = session.beginTransaction();
	    try{
	    	CodiceErroreImport loaded = (CodiceErroreImport)session.load(CodiceErroreImport.class,"R0602");
		    tx.commit();
	    	CodiceErroreImport res = new CodiceErroreImport();
	    	res.setCodiceRegolaInfranta(loaded.getCodiceRegolaInfranta());
	    	res.setDescrizione(loaded.getDescrizione());
	    	res.setEffetto(loaded.getEffetto());
	    	res.setClasse(loaded.getClasse());
	    	res.setFlagBloccante(loaded.getFlagBloccante());
		    return res;
	    }
	    catch (Throwable e) {
			Logger.log().error(this.getClass().getName(),
					"error while updating R0602 data", e);
		    try {
				tx.rollback();
			} catch (HibernateException e1) {
				e1.printStackTrace();
			}
			throw new MuiException("error while updating R0602 data",e);
		}
	    finally{
		    HibernateUtil.closeSession();
	    }
	}

	@Override
	protected boolean controlImpl(HttpServletRequest req,
			HttpServletResponse resp, MuiBaseServlet servlet)
			throws ServletException, IOException {
		Long iidNota = Long.valueOf( req.getParameter("iidNota"));
		storeNota(req, iidNota);
	    req.setAttribute("displayedIciLink",new HashMap());
		return true;
	}

	public static void storeNota(HttpServletRequest req, Long iidNota) {
		Session session = HibernateUtil.currentSession();
		MiDupNotaTras nota = (MiDupNotaTras) session.load(MiDupNotaTras.class, iidNota);
	    req.setAttribute("nota",nota);
	    
	    Set<MiDupTitolarita> titolaritaNota= nota.getMiDupTitolaritas();
	    List<MiDupTitolarita> titolaritaFavoreNota = new ArrayList<MiDupTitolarita>();
	    Map<Long,MiDupTitolarita> titolaritaFavoreNotaId = new HashMap<Long,MiDupTitolarita>();
	    List<MiDupTitolarita> titolaritaControNota = new ArrayList<MiDupTitolarita>();
	    Map<Long,MiDupTitolarita> titolaritaControNotaId = new HashMap<Long,MiDupTitolarita>();
	    List<MiDupTerreniInfo> terreniNota = new ArrayList<MiDupTerreniInfo>();
	    Map<Long,MiDupTerreniInfo> terreniNotaId = new HashMap<Long,MiDupTerreniInfo>();
	    List<MiDupFabbricatiInfo> fabbricatiNota = new ArrayList<MiDupFabbricatiInfo>();
	    Map<Long,MiDupFabbricatiInfo> fabbricatiNotaId = new HashMap<Long,MiDupFabbricatiInfo>();
	    HashSet<Long> soggettiCoinvoltiId = new HashSet<Long>();
	    List<MiDupSoggetti> soggettiNonCoinvolti = new ArrayList<MiDupSoggetti>();

	    Iterator<MiDupTitolarita> iter = titolaritaNota.iterator();
	    while (iter.hasNext()) {
	    	MiDupTitolarita element = iter.next();
	    	if(element.getMiDupSoggetti() != null){
				Logger.log().info(CodeBehindnoteDetailPage.class.getName(), "soggetto nota "+element.getMiDupSoggetti().getIid()+" coinvolto!");
	    		soggettiCoinvoltiId.add(element.getMiDupSoggetti().getIid());
	    	}
	    	if("C".equals( element.getScFlagTipoTitolNota())){
	    		if(!titolaritaControNotaId.containsKey(element.getIid())){
	    			titolaritaControNotaId.put(element.getIid(),element);
	    			titolaritaControNota.add(element);
	    		}
	    	}
	    	else if("F".equals( element.getSfFlagTipoTitolNota())){
	    		if(!titolaritaFavoreNotaId.containsKey(element.getIid())){
	    			titolaritaFavoreNotaId.put(element.getIid(),element);
	    			titolaritaFavoreNota.add(element);
	    		}
	    	}
	    	MiDupTerreniInfo terreno = null;
	    	if((terreno = element.getMiDupTerreniInfo()) != null){
	    		if(!terreniNotaId.containsKey(terreno.getIid())){
	    			terreniNotaId.put(terreno.getIid(),terreno);
	    			terreniNota.add(terreno);
	    		}
	    	}
	    	MiDupFabbricatiInfo fabbricato = null;
	    	if((fabbricato = element.getMiDupFabbricatiInfo()) != null){
	    		if(!fabbricatiNotaId.containsKey(fabbricato.getIid())){
	    			fabbricatiNotaId.put(fabbricato.getIid(),fabbricato);
		    		fabbricatiNota.add(fabbricato);
	    		}
	    	}
		}
	    Iterator<MiDupSoggetti> soggettiNotaIter = nota.getMiDupSoggettis().iterator(); 
	    while (soggettiNotaIter.hasNext()) {
			MiDupSoggetti soggetto = soggettiNotaIter.next();
			Logger.log().info(CodeBehindnoteDetailPage.class.getName(), "soggetto nota "+soggetto.getIid());
			if(!soggettiCoinvoltiId.contains(soggetto.getIid())){
				Logger.log().info(CodeBehindnoteDetailPage.class.getName(), "soggetto nota "+soggetto.getIid()+" non coinvolto!");
				soggettiNonCoinvolti.add(soggetto);
			}
		}
	    req.setAttribute("titolaritaFavoreNota",titolaritaFavoreNota);
	    req.setAttribute("titolaritaControNota",titolaritaControNota);
	    req.setAttribute("soggettiNonCoinvoltiNota",soggettiNonCoinvolti);
	    req.setAttribute("terreniNota",terreniNota);
	    req.setAttribute("fabbricatiNota",fabbricatiNota);
	}


}
