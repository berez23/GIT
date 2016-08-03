package it.webred.mui.http.codebehind;

import it.webred.mui.MuiException;
import it.webred.mui.http.AbstractPage;
import it.webred.mui.http.MuiBaseServlet;
import it.webred.mui.input.MuiInvalidInputDataException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CodeBehinderrorPage extends AbstractPage {

	@Override
	protected boolean controlImpl(HttpServletRequest req,
			HttpServletResponse resp, MuiBaseServlet servlet)
			throws ServletException, IOException {
		Throwable t = (Throwable) req.getAttribute("Throwable");
		if (t == null) {
			return true;
		}
		StringWriter sw = new StringWriter();

		t.printStackTrace(new PrintWriter(sw));
		req.setAttribute("t_stack", sw.toString().replaceAll("\n\t",
				"<br/>&nbsp;&nbsp;&nbsp;"));
		try {
			throw t;
		} 
		catch (MuiInvalidInputDataException muie) {
			req
					.setAttribute(
							"t_code",
							"UNKNOWN".equalsIgnoreCase(muie.getCode()) ? "Errore generico durante la lettura del file di input, formato file errato"
									: muie.getCode());
		} 
		catch (MuiException muie) {
			req
					.setAttribute(
							"t_code",
							"UNKNOWN".equalsIgnoreCase(muie.getCode()) ? "Errore applicativo durante l'esecuzione della richiesta"
									: muie.getCode());
		} 
		catch (Throwable e) {
			req.setAttribute("t_msg",
					"Si e' verificato un errore durante l'esecuzione della pagina :"
							+ e.getMessage());
		}
		return true;
	}

}
