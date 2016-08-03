package it.webred.mui.http.codebehind;

import it.webred.mui.http.AbstractPage;
import it.webred.mui.http.MuiBaseServlet;
import it.webred.mui.http.WhereClause;
import it.webred.mui.http.codebehind.pagination.HibernateWhereQueryDisplayTagPaginator;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.skillbill.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.displaytag.pagination.PaginatedList;

public class CodeBehindnoteSearchPage extends AbstractPage {

	private static final Log log = LogFactory
			.getLog(CodeBehindnoteSearchPage.class);

	@Override
	protected boolean controlImpl(HttpServletRequest req,
			HttpServletResponse resp, MuiBaseServlet servlet)
			throws ServletException, IOException {
		if (req.getMethod().equalsIgnoreCase("post") ) {
			PaginatedList miDupNotaTrass = getPaginatedList(req);
			req.setAttribute("MiDupNotaTrass", miDupNotaTrass);
			req.setAttribute("requestedUri", "noteList.jsp");
			req.getRequestDispatcher("noteList.jsp").include(req, resp);
			return false;
		} else if (req.getQueryString() != null
				&& !req.getQueryString().trim().equals("")) {
			req.setAttribute("requestedUri", "noteList.jsp");
			req.getRequestDispatcher("noteList.jsp").include(req, resp);
			return false;
		}
		return true;
	}

	private PaginatedList getPaginatedList(HttpServletRequest req) {
		HibernateWhereQueryDisplayTagPaginator res  = new HibernateWhereQueryDisplayTagPaginator();
		res.setResTableAlias("nota");
//		if (checkQueryParameter(req, "iidFornitura")) {
//			String query = "select nota from it.webred.mui.model.MiDupNotaTras as nota left join nota.miDupFornitura as fornitura where fornitura.iid = "+req.getParameter("iidFornitura")+" ";
//			String countQuery = "select count(nota) from it.webred.mui.model.MiDupNotaTras as nota left join nota.miDupFornitura as fornitura where fornitura.iid = "+req.getParameter("iidFornitura")+" ";
//			res.setSQuery(query);
//			res.setCountSQuery(countQuery);
//		} else {
			String preQuery = " from MiDupNotaTras as nota ";
			boolean joinSoggetti = (checkQueryParameter(req, "codiceFiscaleG")
					|| checkQueryParameter(req, "codiceFiscale")
					|| checkQueryParameter(req, "denominazione")
					|| checkQueryParameter(req, "cognome")
					|| checkQueryParameter(req, "nome")
					|| checkQueryParameter(req, "codiceDup")
					|| checkQueryParameter(req, "quota")
					|| checkQueryParameter(req, "tipoSoggetto")
					|| checkQueryParameter(req, "scFlagTipoTitolNota")
					|| checkQueryParameter(req, "sfFlagTipoTitolNota") || checkQueryParameter(
					req, "quota"));
			boolean joinImmobili = (checkQueryParameter(req, "flagGraffato")
					|| checkQueryParameter(req, "tipoDenuncia")
					|| checkQueryParameter(req, "TIndirizzo")
					|| checkQueryParameter(req, "foglio")
					|| checkQueryParameter(req, "numero")
					|| checkQueryParameter(req, "subalterno")
					|| checkQueryParameter(req, "categoria") 
					|| checkQueryParameter(req, "renditaEuro")
					|| checkQueryParameter(req, "iid@fabbricato")
					);
			boolean joinDap = (checkQueryParameter(req, "flagSkipped")
					|| checkQueryParameter(req, "flagDapDiritto")
					);
			if (joinSoggetti) {
				preQuery += ",MiDupTitolarita as titolarita,MiDupSoggetti as soggetto ";
			}
			if (joinImmobili) {
				preQuery += ",MiDupFabbricatiIdentifica as identifica,MiDupFabbricatiInfo as fabbricato ";
			}
			if (joinDap) {
				preQuery += ",MiConsDap as dap ";
			}
			preQuery += " where 1=1 ";
			if (joinSoggetti) {
				preQuery += " and soggetto.miDupNotaTras = nota and titolarita.miDupNotaTras = nota  and titolarita.miDupSoggetti = soggetto  ";
			}
			if (joinImmobili) {
				preQuery += " and identifica.miDupNotaTras = nota and fabbricato.miDupNotaTras = nota  and identifica.idImmobile = fabbricato.idImmobile  ";
			}
			if (joinDap) {
				preQuery += " and dap.miDupNotaTras = nota   ";
			}
			Logger.log().info("prequery ", preQuery);
			String query = "select distinct nota "+preQuery;
			String countQuery = "select count (distinct nota) "+preQuery;
			res.setSQuery(query);
			res.setCountSQuery(countQuery);
			
			res.addWhereClause(new WhereClause("iid@nota", "nota", req));
			res.addWhereClause(new WhereClause("numeroNotaTrasLong", "nota", req));
			res.addWhereClause(new WhereClause("miDupFornitura.iid", "nota", req));
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
			res.addWhereClause(new WhereClause("iid@fabbricato", "fabbricato", req));

			res.addWhereClause(new WhereClause("flagSkipped", "dap", req));
			res.addWhereClause(new WhereClause("flagDapDiritto", "dap", req));
		
		
//		}
		return res;
	}

	protected static boolean checkQueryParameter(HttpServletRequest req,
			String param) {
		return (req.getParameter(param) != null
				&& !req.getParameter(param).trim().equals("") && !"IGNORE"
				.equals(req.getParameter("clause_" + param)));
	}
}
