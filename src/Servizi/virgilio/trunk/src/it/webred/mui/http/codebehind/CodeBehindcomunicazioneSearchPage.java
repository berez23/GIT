package it.webred.mui.http.codebehind;

import it.webred.mui.hibernate.HibernateUtil;
import it.webred.mui.http.AbstractPage;
import it.webred.mui.http.MuiBaseServlet;
import it.webred.mui.http.WhereClause;
import it.webred.mui.http.codebehind.pagination.HibernateWhereQueryDisplayTagPaginator;
import it.webred.mui.model.MiConsComunicazione;

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

public class CodeBehindcomunicazioneSearchPage extends AbstractPage {

	private static final Log log = LogFactory
			.getLog(CodeBehindcomunicazioneSearchPage.class);

	@Override
	protected boolean controlImpl(HttpServletRequest req,
			HttpServletResponse resp, MuiBaseServlet servlet)
			throws ServletException, IOException {
		if (req.getMethod().equalsIgnoreCase("post") ) {
//			List<MiConsComunicazione> miConsComunicaziones = searchComunicazioni(req);
			PaginatedList miConsComunicaziones = getPaginatedList(req);
			req.setAttribute("miConsComunicaziones", miConsComunicaziones);
			req.setAttribute("requestedUri", "comunicazioneList.jsp");
			req.getRequestDispatcher("comunicazioneList.jsp").include(req, resp);
			return false;
		} else if (req.getQueryString() != null
				&& !req.getQueryString().trim().equals("")) {
			req.setAttribute("requestedUri", "comunicazioneList.jsp");
			req.getRequestDispatcher("comunicazioneList.jsp").include(req, resp);
			return false;
		}
		return true;
	}

	private PaginatedList getPaginatedList(HttpServletRequest req) {
		HibernateWhereQueryDisplayTagPaginator res  = new HibernateWhereQueryDisplayTagPaginator();
		res.setResTableAlias("com");
		res.setSortByField("iid");
		if (checkQueryParameter(req, "numeroNotaTras")) {
			String query = "select distinct com from it.webred.mui.model.MiDupNotaTras as c,it.webred.mui.model.MiDupSoggetti as soggetto,MiDupTitolarita as titolarita,it.webred.mui.model.MiConsComunicazione as com where c.numeroNotaTras = "+req.getParameter("numeroNotaTras")+" and titolarita.miDupNotaTras=c and soggetto = titolarita.miDupSoggetti and com.miDupSoggetti=soggetto";
			String countQuery = "select count (distinct com) from it.webred.mui.model.MiDupNotaTras as c,it.webred.mui.model.MiDupSoggetti as soggetto,MiDupTitolarita as titolarita,it.webred.mui.model.MiConsComunicazione as com where c.numeroNotaTras = "+req.getParameter("numeroNotaTras")+" and titolarita.miDupNotaTras=c and soggetto = titolarita.miDupSoggetti and com.miDupSoggetti=soggetto";
			res.setSQuery(query);
			res.setCountSQuery(countQuery);
		} else if (checkQueryParameter(req, "iidFornitura")) {
			String query = "select distinct com from MiDupSoggetti as soggetto,MiDupTitolarita as titolarita, MiConsComunicazione as com where titolarita.miDupFornitura.iid = "+req.getParameter("iidFornitura")+" and soggetto = titolarita.miDupSoggetti and com.miDupSoggetti = soggetto";
			String countQuery = "select count(distinct com) from MiDupSoggetti as soggetto,MiDupTitolarita as titolarita, MiConsComunicazione as com where titolarita.miDupFornitura.iid = "+req.getParameter("iidFornitura")+" and soggetto = titolarita.miDupSoggetti and com.miDupSoggetti = soggetto";
			res.setSQuery(query);
			res.setCountSQuery(countQuery);
		} else {
			String preQuery = " from MiDupNotaTras as nota ";
			boolean joinImmobili = (checkQueryParameter(req, "flagGraffato")
					|| checkQueryParameter(req, "tipoDenuncia")
					|| checkQueryParameter(req, "TIndirizzo")
					|| checkQueryParameter(req, "foglio")
					|| checkQueryParameter(req, "numero")
					|| checkQueryParameter(req, "subalterno")
					|| checkQueryParameter(req, "categoria") || checkQueryParameter(
					req, "rendita"));
			preQuery += ",MiDupTitolarita as titolarita,MiDupSoggetti as soggetto, MiConsComunicazione as com ";
			if (joinImmobili) {
				preQuery += ",MiDupFabbricatiIdentifica as identifica,MiDupFabbricatiInfo as fabbricato ";
			}
			preQuery += " where com.miDupSoggetti= soggetto and soggetto = titolarita.miDupSoggetti and titolarita.miDupNotaTras = nota  ";
			if (joinImmobili) {
				preQuery += " and identifica.miDupNotaTras = nota and fabbricato.miDupNotaTras = nota  and identifica.idImmobile = fabbricato.idImmobile  ";
			}
			Logger.log().info("prequery ", preQuery);
			String query = "select distinct com "+preQuery;
			String countQuery = "select count (distinct com) "+preQuery;
			res.setSQuery(query);
			res.setCountSQuery(countQuery);
			res.addWhereClause(new WhereClause("annoNota", "nota", req));
			res.addWhereClause(new WhereClause("dataValiditaAtto", "nota", req));
			res.addWhereClause(new WhereClause("denominazione", "soggetto", req));
			res.addWhereClause(new WhereClause("cognome", "soggetto", req));
			res.addWhereClause(new WhereClause("nome", "soggetto", req));
			res.addWhereClause(new WhereClause("codiceFiscaleG", "soggetto", req));
			res.addWhereClause(new WhereClause("tipoSoggetto", "soggetto", req));
			res.addWhereClause(new WhereClause("codiceFiscale", "soggetto", req));
			res.addWhereClause(new WhereClause("codiceDup", "titolarita", req));
			res.addWhereClause(new WhereClause("quota", "titolarita", req));
			res.addWhereClause(new WhereClause("scFlagTipoTitolNota",
					"titolarita", req));
			res.addWhereClause(new WhereClause("sfFlagTipoTitolNota",
					"titolarita", req));
			res.addWhereClause(new WhereClause("foglio", "identifica", req));
			res.addWhereClause(new WhereClause("numero", "identifica", req));
			res.addWhereClause(new WhereClause("subalterno", "identifica", req));
			res.addWhereClause(new WhereClause("flagGraffato", "fabbricato", req));
			res.addWhereClause(new WhereClause("TIndirizzo", "fabbricato", req));
			res.addWhereClause(new WhereClause("renditaEuro", "fabbricato", req));
			res.addWhereClause(new WhereClause("categoria", "fabbricato", req));
		}
		return res;
	}

	private List<MiConsComunicazione> searchComunicazioni(HttpServletRequest req) {
		List<MiConsComunicazione> miConsComunicaziones = new ArrayList<MiConsComunicazione>();
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		/*
		 * Query query = session.createQuery( "select c from
		 * it.webred.mui.model.MiDupFornitura as c "); for (Iterator it =
		 * query.iterate(); it.hasNext(); ) { MiDupFornitura miDupFornitura =
		 * (MiDupFornitura) it.next(); miDupNotaTrass.addAll(
		 * miDupFornitura.getMiDupNotaTrases()); }
		 */
		Query query = null;
		if (checkQueryParameter(req, "numeroNotaTras")) {
			query = session
					.createQuery("select distinct com from it.webred.mui.model.MiDupNotaTras as c,it.webred.mui.model.MiDupSoggetti as soggetto,MiDupTitolarita as titolarita,it.webred.mui.model.MiConsComunicazione as com where c.numeroNotaTras = :numeroNotaTras and titolarita.miDupNotaTras=c and soggetto = titolarita.miDupSoggetti and com.miDupSoggetti=soggetto");
			query.setString("numeroNotaTras", req
					.getParameter("numeroNotaTras"));
		} else if (checkQueryParameter(req, "iidFornitura")) {
			//from Cat as cat
			query = session
					.createQuery("select distinct com from MiDupSoggetti as soggetto,MiDupTitolarita as titolarita, MiConsComunicazione as com where titolarita.miDupFornitura.iid =:iidFornitura and soggetto = titolarita.miDupSoggetti and com.miDupSoggetti = soggetto");
			query.setString("iidFornitura", req
					.getParameter("iidFornitura"));
		} else {
			String sQuery = "select distinct com from MiDupNotaTras as nota ";
			boolean joinImmobili = (checkQueryParameter(req, "flagGraffato")
					|| checkQueryParameter(req, "tipoDenuncia")
					|| checkQueryParameter(req, "TIndirizzo")
					|| checkQueryParameter(req, "foglio")
					|| checkQueryParameter(req, "numero")
					|| checkQueryParameter(req, "subalterno")
					|| checkQueryParameter(req, "categoria") || checkQueryParameter(
					req, "rendita"));
			sQuery += ",MiDupTitolarita as titolarita,MiDupSoggetti as soggetto, MiConsComunicazione as com ";
			if (joinImmobili) {
				sQuery += ",MiDupFabbricatiIdentifica as identifica,MiDupFabbricatiInfo as fabbricato ";
			}
			sQuery += " where com.miDupSoggetti= soggetto and soggetto = titolarita.miDupSoggetti and titolarita.miDupNotaTras = nota  ";
			if (joinImmobili) {
				sQuery += " and identifica.miDupNotaTras = nota and fabbricato.miDupNotaTras = nota  and identifica.idImmobile = fabbricato.idImmobile  ";
			}
			List<WhereClause> whereClauses = new ArrayList<WhereClause>();
			whereClauses.add(new WhereClause("annoNota", "nota", req));
			whereClauses.add(new WhereClause("dataValiditaAtto", "nota", req));
			whereClauses.add(new WhereClause("denominazione", "soggetto", req));
			whereClauses.add(new WhereClause("cognome", "soggetto", req));
			whereClauses.add(new WhereClause("nome", "soggetto", req));
			whereClauses
					.add(new WhereClause("codiceFiscaleG", "soggetto", req));
			whereClauses.add(new WhereClause("tipoSoggetto", "soggetto", req));
			whereClauses.add(new WhereClause("codiceFiscale", "soggetto", req));
			whereClauses.add(new WhereClause("codiceDup", "titolarita", req));
			whereClauses.add(new WhereClause("quota", "titolarita", req));
			whereClauses.add(new WhereClause("scFlagTipoTitolNota",
					"titolarita", req));
			whereClauses.add(new WhereClause("sfFlagTipoTitolNota",
					"titolarita", req));
			whereClauses.add(new WhereClause("foglio", "identifica", req));
			whereClauses.add(new WhereClause("numero", "identifica", req));
			whereClauses.add(new WhereClause("subalterno", "identifica", req));
			whereClauses
					.add(new WhereClause("flagGraffato", "fabbricato", req));
			whereClauses.add(new WhereClause("TIndirizzo", "fabbricato", req));
			whereClauses.add(new WhereClause("renditaEuro", "fabbricato", req));
			whereClauses.add(new WhereClause("categoria", "fabbricato", req));
			Iterator<WhereClause> theIterator = whereClauses.iterator();
			while (theIterator.hasNext()) {
				WhereClause element = theIterator.next();
				sQuery = element.addClausetoQuery(sQuery);
			}
			Logger.log().info("query su note ", sQuery);
			query = session.createQuery(sQuery);
			theIterator = whereClauses.iterator();
			while (theIterator.hasNext()) {
				WhereClause element = theIterator.next();
				element.setParameter(query);
			}
		}
		query.setMaxResults(MUI_MAX_RESULTS+1);
		for (Iterator it = query.iterate(); it.hasNext();) {
			MiConsComunicazione comunicazione = (MiConsComunicazione) it.next();
			miConsComunicaziones.add(comunicazione);
		}
		tx.commit();
		HibernateUtil.closeSession();
		return miConsComunicaziones;

	}

	protected static boolean checkQueryParameter(HttpServletRequest req,
			String param) {
		return (req.getParameter(param) != null
				&& !req.getParameter(param).trim().equals("") && !"IGNORE"
				.equals(req.getParameter("clause_" + param)));
	}
}

