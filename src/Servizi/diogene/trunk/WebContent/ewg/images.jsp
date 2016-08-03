
<%@page import="it.webred.cet.permission.CeTUser"%><%@page trimDirectiveWhitespaces="true"%>
<%@page import="it.escsolution.escwebgis.common.EnvUtente"%>
<%@page import="it.escsolution.escwebgis.common.EnvSource"%>
<%	  
	  it.escsolution.escwebgis.common.Image image = null;
      synchronized (session) {
        image = (it.escsolution.escwebgis.common.Image) session.getAttribute("image");
        if (image == null){
          image = new it.escsolution.escwebgis.common.Image();
          session.setAttribute("image", image);
        }
      }
  String iImage ;
  //System.out.println("***************");	
  if ( request.getParameter("imgID") != null )
  {
	  iImage = request.getParameter("imgID") ;   
	  java.sql.Connection conn = null;  
    try
    {  
		Class.forName("oracle.jdbc.driver.OracleDriver");
  		javax.naming.Context cont= new javax.naming.InitialContext();
  		javax.sql.DataSource theDataSource = null;
  		EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
  		EnvSource es = new EnvSource(eu.getEnte());
        theDataSource = (javax.sql.DataSource)cont.lookup(es.getDataSourceIntegrato());
  		conn = theDataSource.getConnection();
        conn.setAutoCommit (false);  
       // get the image from the database
       byte[] imgData = image.getImage( conn, iImage,((CeTUser) request.getSession().getAttribute("user")).getCurrentEnte()  ) ;
       // display the image
       response.setContentType("image/gif");
       java.io.OutputStream o = response.getOutputStream();       
       o.write(imgData);       
       o.flush();
       o.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throw e;
    }
    finally
    {
    	if (conn!=null)	
	      conn.close();
    }  
  }
%>