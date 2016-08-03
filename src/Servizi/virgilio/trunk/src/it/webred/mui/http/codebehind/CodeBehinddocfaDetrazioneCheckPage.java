package it.webred.mui.http.codebehind;

import it.webred.docfa.model.Residenza;
import it.webred.docfa.model.DocfaComunOggetto;
import it.webred.mui.consolidation.DocfaDetrazioneManager;
import it.webred.mui.consolidation.ViarioDecoder;
import it.webred.mui.hibernate.HibernateUtil;
import it.webred.mui.http.AbstractPage;
import it.webred.mui.http.MuiBaseServlet;
import it.webred.mui.model.CodiceViaCivico;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.skillbill.logging.Logger;

import org.hibernate.Session;

public class CodeBehinddocfaDetrazioneCheckPage extends AbstractPage {
	private static ViarioDecoder viario = new ViarioDecoder();

	protected static SimpleDateFormat dateFormatter = new SimpleDateFormat(
			"dd/MM/yyyy");

	@Override
	protected boolean controlImpl(HttpServletRequest req,
			HttpServletResponse resp, MuiBaseServlet servlet)
			throws ServletException, IOException {
		try { 
			String codiceSoggetto = req.getParameter("CF");
			String dataRif = req.getParameter("DR");
			String protocollo = req.getParameter("PRT");
			String fornitura = req.getParameter("FORN");
			DocfaDetrazioneManager dm = new DocfaDetrazioneManager(codiceSoggetto,dataRif,protocollo,fornitura);

			boolean foundDetrazione = dm.evalDetrazione();
			Iterator<DocfaComunOggetto> ogs = dm.getConsOggettos().iterator();
			req.setAttribute("MSG", "La data presa in considerazione e' "
							//+ dateFormatter.format(dm.getDataRiferimento())
							+ dm.getDataRiferimento()
							+ "<br/>\n");
			Residenza[] resds = dm.getComunicazione().getResidenzas();
			for (int i = 0; i < resds.length; i++) {
				String till = resds[i].getDataADate() != null ? " al "
						+ resds[i].getDataA() : "";
				req.setAttribute("MSG", req.getAttribute("MSG")
						+ "il Soggetto indicato ha preso residenza in "
						+ resds[i].getCodiceVia() + ": "
						+ getVia(resds[i]) + " "
						+ resds[i].getNumeroCivico() + " dal "
						+ resds[i].getDataDa() + till + "<br/>\n");
			}
			while (ogs.hasNext()) {
				DocfaComunOggetto oggetto = ogs.next();
				if (oggetto == null) {
					Logger.log().error(this.getClass().getName(),
							"error oggetto == null");
					throw new ServletException("error oggetto == null");
				}
				boolean isAbitPri = (oggetto.getFlagAbitPrincipale() != null ? oggetto.getFlagAbitPrincipale(): false);
				String msg = req.getAttribute("MSG") + "il Soggetto indicato "
						+ (isAbitPri ? "HA" : "NON HA")
						+ " diritto alla detrazione per l'immobile sito in (";
				msg += "f/p/s = " + oggetto.getFoglio() + "/"
						+ oggetto.getParticella() + "/ "
						+ oggetto.getSubalterno() + "; ";
				int i = 0;
				for (Iterator<CodiceViaCivico> iter = oggetto.getCodiceViaCivicos(); iter.hasNext(); i++) {
					if (i > 0)
						msg += ", ";
					CodiceViaCivico cvc = iter.next();
					msg += getVia(cvc) + "(codice via ="
							+ cvc.getCodiceVia() + ") " + cvc.getCivico();
				}
				msg += ")";
				req.setAttribute("MSG", msg);
			}
			req.setAttribute("SOGGETTO", dm.getComunicazione());
			req.setAttribute("SOGGETTO_CF_IN_ANAGRAFE", DocfaDetrazioneManager.checkSoggettoInAnagrafe(dm.getComunicazione().getCodfiscalePiva()) );
			List<DocfaComunOggetto> ogsAbit = new ArrayList<DocfaComunOggetto>();
			ogs = dm.getConsOggettos().iterator();
			while (ogs.hasNext()) {
				DocfaComunOggetto element = ogs.next();
				if(element.isAbitativo()){
					ogsAbit.add(element);
					Logger.log().info(
							this.getClass().getName(),
							"oggetto di categoria ="
									+ element.getCategoria()+"; e' abitativo");

				}
				else{
					Logger.log().info(
							this.getClass().getName(),
							"oggetto di categoria ="
							+ element.getCategoria()+"; non e' abitativo");
				}
				
			}
			req.setAttribute("OGGETTI", ogsAbit);
			req.setAttribute("RESIDENZE", dm.getComunicazione().getResidenzas());
			if(dm.getOggettoConDetrazione()!=null){
				req.setAttribute("OGGETTO_CON_DETRAZIONE", dm.getOggettoConDetrazione());
				req.setAttribute("RESIDENZA_CON_DETRAZIONE", dm.getResidenzaConDetrazione());
				req.setAttribute("RESIDENZA_OLTRE_90GG", dm.isResidenzaOltre90gg());
			}
			if (!foundDetrazione) {
				req
						.setAttribute(
								"MSG",
								req.getAttribute("MSG")
										+ "il Soggetto indicato non ha diritto alla detrazione per nessun degli immobili oggetto della comunicazione<br/>\n ");
			}
			return true;
		} catch (Throwable t) {
			Logger.log().error(this.getClass().getName(),
					"error while evaluating detrazione Prima Casa", t);
			throw new ServletException(t);
		}
	}

	private String getVia(Residenza res) throws Exception {
		String via = viario.decodeViaAna(res);
		if (via == null) {
			return "";
		} else {
			return via;
		}
	}
	private String getVia(CodiceViaCivico cvc) throws Exception {
		String via = viario.decodeViaSIT(cvc);
		if (via == null) {
			return "";
		} else {
			return via;
		}
	}
}
