package it.webred.mui.http.codebehind;

import it.webred.docfa.model.DocfaDataLog;
import it.webred.mui.http.codebehind.pagination.HibernateQueryDisplayTagPaginator;

import javax.servlet.http.HttpServletRequest;

public class CodeBehinddocfaImportLogListPage extends AbstractListWithPaginatorPage {

	public CodeBehinddocfaImportLogListPage(){
		super();
		setDefaultSortByField("iid");
		setListName("DocfaDataLogs");
	}

	protected HibernateQueryDisplayTagPaginator getQueryPaginator(HttpServletRequest req) {
		HibernateQueryDisplayTagPaginator paginator = null;
		if (req.getParameter("idFornitura") != null && req.getParameter("protocollo") != null) {		
			paginator = new HibernateQueryDisplayTagPaginator(DocfaDataLog.class, "where  idFornitura = '"+req.getParameter("idFornitura")+"' and idProtocollo='"+req.getParameter("protocollo")+"'");
		} else	if (req.getParameter("idFornitura") != null) {
			String fornitura = req.getParameter("idFornitura");
			String data = fornitura.substring(8)+"/"+fornitura.substring(5, 7)+"/"+fornitura.substring(0, 4);
			paginator = new HibernateQueryDisplayTagPaginator(DocfaDataLog.class, "where proveninza = 'IMP' and idFornitura = '"+data+"'");
		} else {
			paginator = new HibernateQueryDisplayTagPaginator(DocfaDataLog.class, "where proveninza = 'IMP' ");
		}
		return paginator;
	}

}
