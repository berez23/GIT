package it.webred.diogene.querybuilder.dataviewer;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ExportResultServlet extends HttpServlet
{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		// TODO Auto-generated method stub
		doExport(req, res);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		// TODO Auto-generated method stub
		doExport(req, res);
	}
	private void doExport(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		HttpSession session =req.getSession();
		Object objSesName= session.getAttribute("fileName");
		Object objSesSql= session.getAttribute("sqlToExport");
		Object objSesDvClaId= session.getAttribute("idDvClasseToExport");
		Object objSesDcEntId= session.getAttribute("idDcEntityToExport");
		Object objReqName= req.getAttribute("fileName");
		Object objReqSql= req.getAttribute("sqlToExport");
		Object objReqDvClaId= req.getAttribute("idDvClasseToExport");
		Object objReqDcEntId= req.getAttribute("idDcEntityToExport");
		String name ="export.csv";
		String sql ="select 'Nessun Dato Selezionato' Risultato from dual";
		Long dvClaId = null;
		Long dcEntId = null;
		if(objSesName!=null)name = objSesName.toString();
		if(objReqName!=null)name = objReqName.toString();
		if(objSesSql!=null)sql= objSesSql.toString();
		if(objReqSql!=null)sql= objReqSql.toString();
		try
		{
			if (objSesDvClaId != null)
				dvClaId = Long.parseLong(objSesDvClaId.toString());
			if (objReqDvClaId != null)
				dvClaId = Long.parseLong(objReqDvClaId.toString());
			if (objSesDcEntId != null)
				dcEntId = Long.parseLong(objSesDcEntId.toString());
			if (objReqDcEntId != null)
				dcEntId = Long.parseLong(objReqDcEntId.toString());
		}
		catch (NumberFormatException e)
		{
			// TODO: Non faccio nulla
		}
		res.setContentType( "application/octet-stream" );
		res.setHeader( "Content-Disposition","Attachment;filename=\"" + name + "\"" );
		it.webred.diogene.querybuilder.db.ResultsExporter re=new it.webred.diogene.querybuilder.db.ResultsExporter();
		if (dvClaId!=null)
			re.exportAsCSVByDvClasseId(res,sql,dvClaId);
		else if(dcEntId!=null)
			re.exportAsCSVByDcEntityId(res,sql,dcEntId);
		else
			re.exportAsCSV(res,sql);
	}

}
