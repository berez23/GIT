package it.webred.mui.http.codebehind;

import it.webred.mui.consolidation.DetrazioneManager;
import it.webred.mui.consolidation.ViarioDecoder;
import it.webred.mui.hibernate.HibernateUtil;
import it.webred.mui.http.AbstractPage;
import it.webred.mui.http.MuiBaseServlet;
import it.webred.mui.model.CodiceViaCivico;
import it.webred.mui.model.MiConsOggetto;
import it.webred.mui.model.MiDupSoggetti;
import it.webred.mui.model.Residenza;

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

public class CodeBehinddetrazioneCheckPage extends AbstractPage {
	private static ViarioDecoder viario = new ViarioDecoder();

	protected static SimpleDateFormat dateFormatter = new SimpleDateFormat(
			"dd/MM/yyyy");

	@Override
	protected boolean controlImpl(HttpServletRequest req,
			HttpServletResponse resp, MuiBaseServlet servlet)
			throws ServletException, IOException {
		try {
			Long iidSoggetto = Long.valueOf(req.getParameter("iidSoggetto"));
			Session session = HibernateUtil.currentSession();
			MiDupSoggetti soggetto = (MiDupSoggetti) session.load(
					MiDupSoggetti.class, iidSoggetto);
			DetrazioneManager dm = new DetrazioneManager(soggetto);
			boolean foundDetrazione = dm.evalDetrazione();
			Iterator<MiConsOggetto> ogs = dm.getMiConsOggettos().iterator();
			req
					.setAttribute("MSG", "La data presa in considerazione e' "
							+ dateFormatter.format(dm.getDataRiferimento())
							+ "<br/>\n");
			Residenza[] resds = soggetto.getResidenzas();
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
				MiConsOggetto oggetto = ogs.next();
				if (oggetto == null) {
					Logger.log().error(this.getClass().getName(),
							"error oggetto == null");
					throw new ServletException("error oggetto == null");
				}
				boolean isAbitPri = (oggetto.getFlagAbitazionePrincipale() != null ? oggetto
						.getFlagAbitazionePrincipale()
						: false);
				String msg = req.getAttribute("MSG") + "il Soggetto indicato "
						+ (isAbitPri ? "HA" : "NON HA")
						+ " diritto alla detrazione per l'immobile sito in (";
				msg += "f/p/s = " + oggetto.getFoglio() + "/"
						+ oggetto.getParticella() + "/ "
						+ oggetto.getSubalterno() + "; ";
				int i = 0;
				for (Iterator<CodiceViaCivico> iter = oggetto
						.getCodiceViaCivicos(); iter.hasNext(); i++) {
					if (i > 0)
						msg += ", ";
					CodiceViaCivico cvc = iter.next();
					msg += getVia(cvc) + "(codice via ="
							+ cvc.getCodiceVia() + ") " + cvc.getCivico();
				}
				msg += ")";
				req.setAttribute("MSG", msg);
			}
			req.setAttribute("SOGGETTO", soggetto);
			req.setAttribute("SOGGETTO_CF_IN_ANAGRAFE", DetrazioneManager.checkSoggettoInAnagrafe(soggetto) );
			List<MiConsOggetto> ogsAbit = new ArrayList<MiConsOggetto>();
			ogs = dm.getMiConsOggettos().iterator();
			while (ogs.hasNext()) {
				MiConsOggetto element = ogs.next();
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
			req.setAttribute("RESIDENZE", soggetto.getResidenzas());
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
