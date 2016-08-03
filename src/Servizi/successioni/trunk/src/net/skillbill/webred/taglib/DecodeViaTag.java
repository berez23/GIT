package net.skillbill.webred.taglib;

import it.webred.mui.consolidation.ViarioDecoder;

import java.io.IOException;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class DecodeViaTag extends TagSupport {

	private static ViarioDecoder viario = new ViarioDecoder();

	private String codice = null;

	public void setCodice(String codicevia) {
		codice = codicevia;
	}

	public int doStartTag() throws JspException {
		String via;
		try {
			via = viario.decodeVia(codice);
			if (via != null) {
				pageContext.getOut().write(via);
			}
			codice = null;
			return EVAL_BODY_INCLUDE;
		} catch (SQLException e) {
			throw new JspException(e);
		} catch (NamingException e) {
			codice = null;
			throw new JspException(e);
		} catch (IOException e) {
			codice = null;
			throw new JspException(e);
		} finally {
			codice = null;
		}
	}

	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

}
