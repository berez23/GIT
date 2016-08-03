package it.webred.mui.http.codebehind;

import it.webred.mui.http.codebehind.pagination.HibernateQueryDisplayTagPaginator;
import it.webred.mui.model.MiDupImportLog;

import javax.servlet.http.HttpServletRequest;

public class CodeBehindimportLogListPage extends AbstractListWithPaginatorPage {

	public CodeBehindimportLogListPage(){
		super();
		setDefaultSortByField("idNota");
		setListName("MiDupImportLogs");
	}

	protected HibernateQueryDisplayTagPaginator getQueryPaginator(HttpServletRequest req) {
		HibernateQueryDisplayTagPaginator paginator = null;
		if (req.getParameter("iidFornitura") != null) {
			paginator = new HibernateQueryDisplayTagPaginator(MiDupImportLog.class, "where c.miDupFornitura.iid = :iidFornitura ","iidFornitura",Long.valueOf(req.getParameter("iidFornitura")) );
		} else if (req.getParameter("iidNota") != null) {
			paginator = new HibernateQueryDisplayTagPaginator(MiDupImportLog.class, "where c.miDupNotaTras.iid = :iidNota","iidNota",Long.valueOf( req.getParameter("iidNota")));
		} else if (req.getParameter("iidLog") != null) {
			paginator = new HibernateQueryDisplayTagPaginator(MiDupImportLog.class, "where c.iid = :iidLog","iidLog",Long.valueOf( req.getParameter("iidLog")));
		} else if (req.getParameter("codiceEsito") != null && req.getParameter("iidImmobile") != null) {
			paginator = new HibernateQueryDisplayTagPaginator(MiDupImportLog.class, "where c.miDupFabbricatiInfo.iid = :iidImmobile and c.codiceRegolaInfranta.codiceRegolaInfranta='R0602'","iidImmobile",Long.valueOf( req.getParameter("iidImmobile")));
		} else {
			paginator = new HibernateQueryDisplayTagPaginator(MiDupImportLog.class);
		}
		return paginator;
	}

}
