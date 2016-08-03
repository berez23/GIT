package it.webred.cet.service.ff.web.util;

import it.webred.cet.service.ff.web.FFBaseBean;
import it.webred.cet.service.ff.web.UserBean;
import it.webred.cet.service.ff.web.beans.dettaglio.DettaglioFasFabBean;
import it.webred.cet.service.ff.web.beans.dettaglio.catasto.CatastoBean;
import it.webred.cet.service.ff.web.beans.dettaglio.compravendite.CompravenditeBean;
import it.webred.cet.service.ff.web.beans.dettaglio.docfa.DocfaBean;
import it.webred.cet.service.ff.web.beans.dettaglio.pregeo.PregeoBean;
import it.webred.cet.service.ff.web.beans.pdf.PDFBean;
import it.webred.cet.service.ff.web.beans.richieste.RichiesteBean;
import it.webred.ct.config.parameters.ParameterService;
import it.webred.ct.service.ff.data.access.richieste.GestRichiestaService;
import it.webred.ct.service.ff.data.access.richieste.dto.RichiestaDTO;
import it.webred.ct.service.ff.data.model.FFRichieste;
import it.webred.ct.service.ff.data.model.FFRisposte;
import it.webred.ct.service.ff.data.model.FFSoggetti;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

public class GestoreRichiesteEsterne extends FFBaseBean{
	
	
	public  String apriDettaglioRichAuto(RichiesteBean richBean) {
		
		DettaglioFasFabBean dettBean = (DettaglioFasFabBean)getBeanReference("dettaglioFasFabBean");
		PDFBean pdfBean = (PDFBean)getBeanReference("pdfBean");
		UserBean userBean = (UserBean)getBeanReference("userBean");
				
		String sezioneRic = richBean.getRichiesta().getRichiesta().getSezione();
		String foglioRic =  richBean.getRichiesta().getRichiesta().getFoglio();
		String particellaRic =  richBean.getRichiesta().getRichiesta().getParticella();
		
		dettBean.setIdRichiesta(richBean.getIdRich());
		pdfBean.setIdRich(richBean.getIdRich().toString());
		pdfBean.setDataRif(richBean.getRichiesta().getRichiesta().getStrDtRif());
		pdfBean.setDataRichiesta(richBean.getRichiesta().getRichiesta().getStrDtRic());
		pdfBean.setUsername(userBean.getUser().getUsername());
		pdfBean.setSoggRichiedente(richBean.getSoggRichiedente());
		pdfBean.setSezione(sezioneRic);
		pdfBean.setFoglio(foglioRic);
		pdfBean.setParticella(particellaRic);
		pdfBean.setEvadi("true");
		
		CatastoBean catastoBean = (CatastoBean)getBeanReference("catastoBean");
		catastoBean.setSezione(sezioneRic);
		catastoBean.setFoglio(foglioRic);
		catastoBean.setParticella(particellaRic);
		catastoBean.setDataRifStr(richBean.getStrDataRif());
		catastoBean.setDatiCatasto("");
		
		DocfaBean docfaBean = (DocfaBean)getBeanReference("docfaBean");
		docfaBean.setSezione(sezioneRic);
		docfaBean.setFoglio(foglioRic);
		docfaBean.setParticella(particellaRic);
		docfaBean.setDataRifStr(richBean.getStrDataRif());
		docfaBean.setDocfaParticella("");
		
		PregeoBean pregeoBean = (PregeoBean)getBeanReference("pregeoBean");
		pregeoBean.setSezione(sezioneRic);
		pregeoBean.setFoglio(foglioRic);
		pregeoBean.setParticella(particellaRic);
		pregeoBean.setDataRifStr(richBean.getStrDataRif());
		pregeoBean.setListaDatiPregeo("");
		
		CompravenditeBean compravenditeBean = (CompravenditeBean)getBeanReference("compravenditeBean");
		compravenditeBean.setSezione(sezioneRic);
		compravenditeBean.setFoglio(foglioRic);
		compravenditeBean.setParticella(particellaRic);
		compravenditeBean.setDataRifStr(richBean.getStrDataRif());
		compravenditeBean.setCompravenditeParticella("");
		
		return dettBean.doLoadDatiDettaglioDaRichiesta();
		
		
		
		
	
		
	}
	
	
}
