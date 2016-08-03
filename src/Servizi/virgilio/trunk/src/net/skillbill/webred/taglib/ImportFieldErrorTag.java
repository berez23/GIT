package net.skillbill.webred.taglib;

import it.webred.mui.model.MiDupFabbricatiIdentifica;
import it.webred.mui.model.MiDupFabbricatiInfo;
import it.webred.mui.model.MiDupFornituraInfo;
import it.webred.mui.model.MiDupImportLog;
import it.webred.mui.model.MiDupIndirizziSog;
import it.webred.mui.model.MiDupNotaTras;
import it.webred.mui.model.MiDupSoggetti;
import it.webred.mui.model.MiDupTerreniInfo;
import it.webred.mui.model.MiDupTitolarita;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.skillbill.logging.Logger;

public class ImportFieldErrorTag extends TagSupport {

	private String propertyName = null;

	private MiDupNotaTras nota = null;

	MiDupImportLog log = null;
	
	private long miDupFabbricatiIdentifica = -1;

	private long miDupFabbricatiInfo = -1;

	private long miDupFornituraInfo = -1;

	private long miDupIndirizziSog = -1;

	private long miDupSoggetti = -1;

	private long miDupTerreniInfo = -1;

	private long miDupTitolarita = -1;

	public void setMiDupFabbricatiIdentifica(
			MiDupFabbricatiIdentifica miDupFabbricatiIdentifica) {
		this.miDupFabbricatiIdentifica = miDupFabbricatiIdentifica.getIid();
		Logger
		.log()
		.info(this.getClass().getName(),
				"getting log for type MiDupFabbricatiIdentifica iid="+
				this.miDupFabbricatiIdentifica);
	}

	public void setMiDupFabbricatiInfo(MiDupFabbricatiInfo miDupFabbricatiInfo) {
		this.miDupFabbricatiInfo = miDupFabbricatiInfo.getIid();
		Logger
		.log()
		.info(this.getClass().getName(),
				"getting log for type MiDupFabbricatiInfo iid="+
				this.miDupFabbricatiInfo);
	}

	public void setMiDupFornituraInfo(MiDupFornituraInfo miDupFornituraInfo) {
		this.miDupFornituraInfo = miDupFornituraInfo.getIid();
		Logger
		.log()
		.info(this.getClass().getName(),
				"getting log for type MiDupFornituraInfo iid="+
				this.miDupFornituraInfo);
	}

	public void setMiDupIndirizziSog(MiDupIndirizziSog miDupIndirizziSog) {
		this.miDupIndirizziSog = miDupIndirizziSog.getIid();
		Logger
		.log()
		.info(this.getClass().getName(),
				"getting log for type MiDupIndirizziSog iid="+
				this.miDupIndirizziSog);
	}

	public void setMiDupSoggetti(MiDupSoggetti miDupSoggetti) {
		this.miDupSoggetti = miDupSoggetti.getIid();
		Logger
		.log()
		.info(this.getClass().getName(),
				"getting log for type MiDupSoggetti iid="+
				this.miDupSoggetti);
	}

	public void setMiDupTerreniInfo(MiDupTerreniInfo miDupTerreniInfo) {
		this.miDupTerreniInfo = miDupTerreniInfo.getIid();
		Logger
		.log()
		.info(this.getClass().getName(),
				"getting log for type MiDupTerreniInfo iid="+
				this.miDupTerreniInfo);
	}

	public void setMiDupTitolarita(MiDupTitolarita miDupTitolarita) {
		this.miDupTitolarita = miDupTitolarita.getIid();
		Logger
		.log()
		.info(this.getClass().getName(),
				"getting log for type MiDupTitolarita iid="+
				this.miDupTitolarita);
	}

	public int doStartTag() throws JspException {
		// System.out.println("role="+this.role);
		Logger
		.log()
		.info(this.getClass().getName(),
				"doStartTag");
		try {
			if ((log = selectLog()) != null) {
				pageContext.getOut()
						.write("<a href=\"javascript:popUp('formPostNoTemplate/importLogListNoTemplate.jsp?iidLog="+log.getIid()+"')\">");
				if (log.getFlagBloccante()) {
					pageContext.getOut().write("<img src=\"img/bloccante.gif\"/>");
				}
				else{
					pageContext.getOut().write("<img src=\"img/nonbloccante.gif\"/>");
				}
			}
		} catch (IOException e) {
			throw new JspException(e);
		}
		return EVAL_BODY_INCLUDE;
	}

	public int doEndTag() throws JspException {
		try {
			if (log != null) {
				pageContext.getOut()
				.write("</a>");
			}
		} catch (IOException e) {
			throw new JspException(e);
		}
		miDupFabbricatiIdentifica = -1;
		miDupFabbricatiInfo = -1;
		miDupFornituraInfo = -1;
		miDupIndirizziSog = -1;
		miDupSoggetti = -1;
		miDupTerreniInfo = -1;
		miDupTitolarita = -1;
		
		propertyName = null;
		nota = null;
		log = null;
		
		return EVAL_PAGE;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public void setNota(MiDupNotaTras nota) {
		this.nota = nota;
	}

	private MiDupImportLog selectLog() {
		MiDupImportLog log = null;
		Logger
		.log()
		.info(this.getClass().getName(),
				"getting log for "+
				propertyName);
		List<MiDupImportLog> logs = nota.getLoggedProperty().get(propertyName);
		if (logs == null) {
			return null;
		}
		Iterator<MiDupImportLog> iter = logs.iterator();
		while (iter.hasNext()) {
			MiDupImportLog theLog = iter.next();
			Logger
			.log()
			.info(this.getClass().getName(),
					"checking for log "+
					theLog.getIid()+" "+theLog.getCodice()+" theLog.getMiDupIndirizziSog()="+theLog.getMiDupIndirizziSog()+" theLog.getMiDupSoggetti()="+theLog.getMiDupSoggetti());
			if (theLog.getMiDupFabbricatiIdentifica() != null) {
				if (theLog.getMiDupFabbricatiIdentifica().getIid() == miDupFabbricatiIdentifica) {
					log = theLog;
					break;
				}
				continue;
			}
			if (theLog.getMiDupFabbricatiInfo() != null) {
				if (theLog.getMiDupFabbricatiInfo().getIid() == miDupFabbricatiInfo) {
					log = theLog;
					break;
				}
				continue;
			}
			if (theLog.getMiDupFornituraInfo() != null) {
				if (theLog.getMiDupFornituraInfo().getIid() == miDupFornituraInfo) {
					log = theLog;
					break;
				}
				continue;
			}
			if (theLog.getMiDupIndirizziSog() != null) {
				if (theLog.getMiDupIndirizziSog().getIid() == miDupIndirizziSog) {
					log = theLog;
					break;
				}
				continue;
			}
			if (theLog.getMiDupSoggetti() != null) {
				if (theLog.getMiDupSoggetti().getIid() == miDupSoggetti) {
					log = theLog;
					break;
				}
				continue;
			}
			if (theLog.getMiDupTerreniInfo() != null) {
				if (theLog.getMiDupTerreniInfo().getIid() == miDupTerreniInfo) {
					log = theLog;
					break;
				}
				continue;
			}
			if (theLog.getMiDupTitolarita() != null) {
				if (theLog.getMiDupTitolarita().getIid() == miDupTitolarita) {
					log = theLog;
					break;
				}
				continue;
			}
			log = theLog;
			break;
		}
		return log;
	}
}
