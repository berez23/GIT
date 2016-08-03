package it.webred.mui.http.codebehind;

import it.webred.mui.http.codebehind.pagination.HibernateQueryDisplayTagPaginator;
import it.webred.mui.model.CodiceErroreImport;

import javax.servlet.http.HttpServletRequest;

public class CodeBehindregoleListPage extends AbstractListWithPaginatorPage {

	public CodeBehindregoleListPage() {
		super();
		setDefaultSortByField("codiceRegolaInfranta");
		setListName("codiceErrores");
		setDefaultSortByDirection(true);
	}

	protected HibernateQueryDisplayTagPaginator getQueryPaginator(
			HttpServletRequest req) {
		int nClasse = 1;
		try {
			nClasse = Integer.valueOf(req.getParameter("classe"));
		} catch (NumberFormatException e) {
		} catch (NullPointerException e) {
		}
		return new HibernateQueryDisplayTagPaginator(CodiceErroreImport.class,"where c.classe="+nClasse);
	}
}
