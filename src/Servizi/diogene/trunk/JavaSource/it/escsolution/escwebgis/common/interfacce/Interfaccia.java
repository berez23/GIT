/*
 * Created on 14-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.common.interfacce;


import it.escsolution.escwebgis.common.DiogeneException;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscObject;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.webred.utils.GenericTuples;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Interfaccia extends EscServlet {

	public void EseguiServizio(
		HttpServletRequest request,
		HttpServletResponse response)
		throws ServletException, IOException {

		super.EseguiServizio(request, response);
		/*
		 * ad ogni uc corrisponde una funzionalit diversa
		 *
		 */
		//super.EseguiServizio(request,response);
		try{
			// questa servlet è di raccordo tra i vari temi
			HttpSession sessione = request.getSession();

			/*
			 * leggo i parametri
			 */

			String query = URLDecoder.decode(request.getParameter("OGGETTO_SEL"),"US-ASCII");
			int inizioQuery = query.indexOf("QUERY");
			int rowNum = query.indexOf("NROW");
			String servletName = query.substring(8,inizioQuery-1);
			String queryDef = query.substring(inizioQuery+6,rowNum-1);
			int numeroRighe = new Integer(query.substring(rowNum+5)).intValue();

			/*
			 * prendo l'oggetto correntemente in sessione
			 */
			EscObject oggettoInSessione = (EscObject)sessione.getAttribute(Tema.getOggettoDettaglio(uc));
			
			String ds = Tema.getDataSource(EscServlet.URL_PATH + servletName);
			String myDs = this.getDataSource(request);
			if (ds != null && !ds.equals(myDs) && !ds.equals("jdbc/diogene"))
				myDs = ds;
				
				
			/*
			 * ricavo l'UC destinazione e pulisco la sessione
			 */
			EnvUtente eu = new EnvUtente(this.getEnvUtente(request).getUtente(),this.getDataSource(request),this.getEnvUtente(request).getNomeApplicazione());
			InterfacciaiLogic logic = new InterfacciaiLogic(eu);
			int NEW_UC = Tema.getServletUC(URL_PATH + servletName);
			sessione.removeAttribute(Tema.getNomeFinder(NEW_UC));
			sessione.removeAttribute("LISTA_INTERFACCE");
			sessione.removeAttribute(EscServlet.IDSTORICI);

			PulsantiNavigazione nav = new PulsantiNavigazione();

			GenericTuples.T2<String,String> rc = null;
			
			if (numeroRighe == 0){
				rc = logic.mCaricareDati(queryDef, oggettoInSessione.getChiave(),numeroRighe);
				if (rc.equals("-1"))
					throw new DiogeneException("NON CI SONO DATI DA VISUALIZZARE");
				nav.chiamataEsternaLista();
				String elencoChiavi = "&KEYSTR="+ rc.firstObj;
				String queryForKeyStr = "";
				if (rc.secondObj!=null)
					queryForKeyStr = "&queryForKeyStr="+ rc.secondObj;
				
				request.getSession().removeAttribute("DATASOURCE");
				this.chiamaPagina(request, response, servletName+"?EXT=1&UC=" +new Integer(NEW_UC).toString() + "&ST=2"+ elencoChiavi + queryForKeyStr, nav);
			}
			else{
				rc = logic.mCaricareDati(queryDef, oggettoInSessione.getChiave(),numeroRighe);
				if (rc.equals("-1"))
					throw new DiogeneException("NON CI SONO DATI DA VISUALIZZARE");
				nav.chiamataEsternaLista();
				String elencoChiavi = "&OGGETTO_SEL="+ rc.firstObj;
				String queryForKeyStr = "";
				if (rc.secondObj!=null)
					queryForKeyStr = "&queryForKeyStr="+ rc.secondObj;

				request.getSession().removeAttribute("DATASOURCE");
				this.chiamaPagina(request, response, servletName+"?EXT=1&UC=" +new Integer(NEW_UC).toString() + "&ST=3"+ elencoChiavi + queryForKeyStr, nav);
			}



		}
		 catch(it.escsolution.escwebgis.common.DiogeneException de)
		 {
		 	throw de;
		 }
		 catch(Exception ex){
			log.error(ex.getMessage(),ex);
		}


	}

}
