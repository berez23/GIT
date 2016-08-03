package it.webred.mui.http;

import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.HttpJspPage;
/**
 * <p>Title: UMA - </p>
 * <p>Description: $Id: MuiBaseJsp.java,v 1.2 2012/09/11 13:11:54 filippo Exp $ $Source: /cvs_repos/virgilio/src/it/webred/mui/http/MuiBaseJsp.java,v $</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: $Author: filippo $</p>
 * <p> Date:</p>$Date: 2012/09/11 13:11:54 $
 * @author: Ing Francesco Ciacca
 * @version $Revision: 1.2 $
 */

public class MuiBaseJsp extends MuiBaseServlet implements HttpJspPage{
  public MuiBaseJsp() {
  }
  /*protected void muiService(HttpServletRequest req, HttpServletResponse resp)throws ServletException, java.io.IOException{
	  _jspService(req,resp);
  }*/
  protected void muiService(HttpServletRequest req, HttpServletResponse resp)throws ServletException, java.io.IOException{
	  try {
		  _jspService(req,resp);
	  } catch (Throwable thr) {
		  jspInit();
		  _jspService(req,resp);
	  }
  }
  public void _jspService(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, javax.servlet.ServletException {
  }
  public void jspDestroy(){
    super.destroy();
  }
  /*public void jspInit(){
    try{
      super.init();
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }*/
  public void jspInit() {
      try {
          Method _jspInit = this.getClass().getMethod("_jspInit", (Class[])null);
          try {
              _jspInit.invoke(this, (Object[])null);
          } catch (Exception e) {
        	  e.printStackTrace();
          }
      } catch (NoSuchMethodException ignored) {
    	  try{
    	      super.init();
    	    }
    	    catch(Exception e){
    	      e.printStackTrace();
    	    }
      }
  }
}
/**
 * <p>History:</p>
 * $Log: MuiBaseJsp.java,v $
 * Revision 1.2  2012/09/11 13:11:54  filippo
 * Modifiche per deploy su JBoss
 *
 * Revision 1.1  2007/05/16 07:45:40  dan
 * *** empty log message ***
 *
 * Revision 1.1.1.1  2007/02/15 08:44:08  dan
 * no message
 *
 * Revision 1.2  2007/01/16 09:49:49  franci
 * dap rilascio seconda offerta webred
 *
 * Revision 1.1.1.1  2006/05/04 09:45:45  franci
 * Importing from Scratch
 *
 *
 */
