package it.webred.mui.http.codebehind;

import it.webred.mui.consolidation.DwhManager;
import it.webred.mui.http.AbstractPage;
import it.webred.mui.http.MuiBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CodeBehindintegrazioneResultPage extends AbstractPage {
	protected boolean controlImpl(HttpServletRequest req,
			HttpServletResponse resp, MuiBaseServlet servlet)
			throws ServletException, java.io.IOException {
		if (req.getMethod().equalsIgnoreCase("post")) {
			try {
				final DwhManager dwh = new DwhManager();
				final String iidFornitura = req.getParameter("iidFornitura");
				Thread t = new Thread() {
					public void run() {
						dwh.integrateFornitura(iidFornitura);
					}
				};
				t.start();
			} catch (Exception e) {
				throw new ServletException(e);
			}
		}
		return true;
	}

}
