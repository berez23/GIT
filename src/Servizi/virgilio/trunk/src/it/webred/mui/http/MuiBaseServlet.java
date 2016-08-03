package it.webred.mui.http;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.skillbill.logging.Logger;

/**
 * <p>
 * Title: MUI -
 * </p>
 * <p>
 * Description: $Id: MuiBaseServlet.java,v 1.1 2007/05/16 07:45:40 dan Exp $ $Source: /cvs_repos/virgilio/src/it/webred/mui/http/MuiBaseServlet.java,v $
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: $Author: dan $
 * </p>
 * <p>
 * Date:
 * </p>
 * $Date: 2007/05/16 07:45:40 $
 * 
 * @author : Ing Francesco Ciacca
 * @version $Revision: 1.1 $
 * 
 */

public abstract class MuiBaseServlet extends javax.servlet.http.HttpServlet
		implements MuiHttpConstants {
	public MuiBaseServlet() {
	}

	protected final void service(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException,
			java.io.IOException {
		Logger.log().debug(MAIN_URI_REQ + " requestedURL:",
				req.getRequestURL());

		if (req.getSession().getAttribute("logged_out") != null) {
			resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		Controller controller = (Controller) req.getSession().getAttribute(
				CONTROLLER_SES);
		if (controller == null) {
			controller = new Controller();
			req.getSession().setAttribute(CONTROLLER_SES, controller);
		}
		try {
			controller.control(req, resp, this, getServletContext());
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	protected abstract void muiService(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException,
			java.io.IOException;
}
/**
 * <p>
 * History:
 * </p>
 * $Log: MuiBaseServlet.java,v $
 * Revision 1.1  2007/05/16 07:45:40  dan
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/24 15:26:09  ciacca
 * inserito supporto file XML
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
