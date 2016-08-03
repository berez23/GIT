package it.webred.mui.http.codebehind;

import it.webred.docfa.model.DocfaFornitura;
import it.webred.mui.hibernate.HibernateUtil;
import it.webred.mui.http.AbstractPage;
import it.webred.mui.http.MuiBaseServlet;
import it.webred.mui.http.WhereClause;
import it.webred.mui.http.codebehind.pagination.HibernateWhereQueryDisplayTagPaginator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.skillbill.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.displaytag.pagination.PaginatedList;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class  CodeBehinddocfaComunicazioneSearchPage extends AbstractPage {

	private static final Log log = LogFactory.getLog(CodeBehinddocfaComunicazioneSearchPage.class);

	@Override
	protected boolean controlImpl(HttpServletRequest req,
			HttpServletResponse resp, MuiBaseServlet servlet)
			throws ServletException, IOException {
		if (req.getMethod().equalsIgnoreCase("post") ) {
			PaginatedList docfaComunicaziones = getPaginatedList(req);
			req.setAttribute("docfaComunicaziones", docfaComunicaziones);
			req.setAttribute("requestedUri", "docfaComunicazioneList.jsp");
			req.getRequestDispatcher("docfaComunicazioneList.jsp").include(req, resp);
			return false;
		} else if (req.getQueryString() != null
				&& !req.getQueryString().trim().equals("")) {
			req.setAttribute("requestedUri", "docfaComunicazioneList.jsp");
			req.getRequestDispatcher("docfaComunicazioneList.jsp").include(req, resp);
			return false;
		}
		return true;
	}

	private PaginatedList getPaginatedList(HttpServletRequest req) {
		HibernateWhereQueryDisplayTagPaginator res  = new HibernateWhereQueryDisplayTagPaginator();
		res.setResTableAlias("c");
		res.setSortByField("iidComunicazione");
		String preQuery = "from it.webred.docfa.model.DocfaComunicazione as c ";
		
		boolean joinOggetti = (checkQueryParameter(req, "indirizzo")
				|| checkQueryParameter(req, "foglio")
				|| checkQueryParameter(req, "particella")
				|| checkQueryParameter(req, "subalterno")
				|| checkQueryParameter(req, "categoria"));
		
		if (joinOggetti) {
			preQuery += ",it.webred.docfa.model.DocfaComunOggetto as o ";
		}
		preQuery += "where 1=1  ";
		if (joinOggetti) {
			preQuery += "and o.docfaComunicazione = c  ";
		}
		
		if (req.getParameter("fornitura") != null)
		{
			String fornitura = req.getParameter("fornitura");
			String data = fornitura.substring(8)+"/"+fornitura.substring(5, 7)+"/"+fornitura.substring(0, 4);
			preQuery += "and c.iidFornitura = '"+data+"' ";
		}
		
		Logger.log().info("prequery ", preQuery);
		String query = "select distinct c "+preQuery;
		String countQuery = "select count (distinct c) "+preQuery;
		res.setSQuery(query);
		res.setCountSQuery(countQuery);
		res.addWhereClause(new WhereClause("iidProtocolloReg", "c", req));
		res.addWhereClause(new WhereClause("iidFornitura", "c", req));
		res.addWhereClause(new WhereClause("denominazione", "c", req));
		res.addWhereClause(new WhereClause("nome", "c", req));
		res.addWhereClause(new WhereClause("codfiscalePiva", "c", req));
		res.addWhereClause(new WhereClause("indirizzo", "o", req));
		res.addWhereClause(new WhereClause("foglio", "o", req));
		res.addWhereClause(new WhereClause("particella", "o", req));
		res.addWhereClause(new WhereClause("subalterno", "o", req));
		res.addWhereClause(new WhereClause("categoria", "o", req));
		
		return res;
	}

	private List<DocfaFornitura> searchComunicazioni(HttpServletRequest req) {
		List<DocfaFornitura> docfaComunicaziones = new ArrayList<DocfaFornitura>();
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		Query query = null;
		String sqlSelect = "select distinct c ";
		String sqlFrom = "from it.webred.docfa.model.DocfaComunicazione as c ";
		String sqlWhere = "where 1=1 ";
		
		boolean joinOggetti = (checkQueryParameter(req, "indirizzo")
				|| checkQueryParameter(req, "foglio")
				|| checkQueryParameter(req, "particella")
				|| checkQueryParameter(req, "subalterno")
				|| checkQueryParameter(req, "categoria"));
					
		if (joinOggetti) {
			sqlFrom += ",it.webred.docfa.model.DocfaComunOggetto as o ";
		}
		
		if (joinOggetti) {
			sqlWhere += " and o.docfaComunicazione = c  ";
		}
		
		List<WhereClause> whereClauses = new ArrayList<WhereClause>();
		whereClauses.add(new WhereClause("iidProtocolloReg", "c", req));
		whereClauses.add(new WhereClause("iidFornitura", "c", req));
		whereClauses.add(new WhereClause("denominazione", "c", req));
		whereClauses.add(new WhereClause("nome", "c", req));
		whereClauses.add(new WhereClause("codfiscalePiva", "c", req));
		whereClauses.add(new WhereClause("indirizzo", "o", req));
		whereClauses.add(new WhereClause("foglio", "o", req));
		whereClauses.add(new WhereClause("particella", "o", req));
		whereClauses.add(new WhereClause("subalterno", "o", req));
		whereClauses.add(new WhereClause("categoria","o", req));
		Iterator<WhereClause> theIterator = whereClauses.iterator();
		
		String sQuery = sqlSelect + sqlFrom + sqlWhere;
		while (theIterator.hasNext()) {
			WhereClause element = theIterator.next();
			sQuery = element.addClausetoQuery(sQuery);
		}
		
		Logger.log().info("query su comunicazioni ", sQuery);
		query = session.createQuery(sQuery);
		theIterator = whereClauses.iterator();
		while (theIterator.hasNext()) {
			WhereClause element = theIterator.next();
			element.setParameter(query);
		}
		
		query.setMaxResults(200+1); 
		
		for (Iterator it = query.iterate(); it.hasNext();) {
			DocfaFornitura comunicazione = (DocfaFornitura) it.next();
			docfaComunicaziones.add(comunicazione);
		}
		tx.commit();
		HibernateUtil.closeSession();
		
		return docfaComunicaziones;

	}

	protected static boolean checkQueryParameter(HttpServletRequest req,
			String param) {
		return (req.getParameter(param) != null
				&& !req.getParameter(param).trim().equals("") && !"IGNORE"
				.equals(req.getParameter("clause_" + param)));
	}
}

