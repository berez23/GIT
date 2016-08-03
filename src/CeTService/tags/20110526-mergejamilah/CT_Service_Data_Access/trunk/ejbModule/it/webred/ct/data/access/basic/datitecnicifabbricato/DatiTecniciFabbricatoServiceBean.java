package it.webred.ct.data.access.basic.datitecnicifabbricato;

import java.util.ArrayList;
import java.util.List;

import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.datitecnicifabbricato.dao.DatiTecniciFabbricatoDAO;
import it.webred.ct.data.access.basic.datitecnicifabbricato.dto.RicercaDatiTecniciDTO;
import it.webred.ct.data.model.datitecnicifabbricato.CertificazioneEnergetica;
import it.webred.ct.data.model.datitecnicifabbricato.CollaudoStatico;
import it.webred.ct.data.model.datitecnicifabbricato.DichiarazioneConformita;
import it.webred.ct.data.model.datitecnicifabbricato.DocumentiTecniciFabbricato;


import javax.ejb.Stateless;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;

@Stateless
public class DatiTecniciFabbricatoServiceBean implements DatiTecniciFabbricatoService {
	@Autowired
	private DatiTecniciFabbricatoDAO datiTecniciFabbricatoDAO;

	@Override
	public List<CertificazioneEnergetica> getListaCertificazioniEnergeticheByFP(RicercaOggettoCatDTO ro) throws DatiTecniciFabbricatoException {
		List<CertificazioneEnergetica> lista = datiTecniciFabbricatoDAO.getListaCertificazioniEnergeticheByFP(ro);
		return lista;
	}

	@Override
	public List<CollaudoStatico> getListaCollaudoStaticoByFP(RicercaOggettoCatDTO ro) throws DatiTecniciFabbricatoException {
		List<CollaudoStatico> lista = datiTecniciFabbricatoDAO.getListaCollaudoStaticoByFP(ro);
		return lista;

	}

	@Override
	public List<DichiarazioneConformita> getListaDichiarazioneConformitaByFP(RicercaOggettoCatDTO ro) throws DatiTecniciFabbricatoException {
		List<DichiarazioneConformita> lista = datiTecniciFabbricatoDAO.getListaDichiarazioneConformitaByFP(ro);
		return lista;

	}

	@Override
	public List<DocumentiTecniciFabbricato> getListaDocumentiByIdDati(RicercaDatiTecniciDTO rd) throws DatiTecniciFabbricatoException {
		List<DocumentiTecniciFabbricato> lista = datiTecniciFabbricatoDAO.getListaDocumentiByIdDati(rd); 
		return lista;
	}
	
	
}
