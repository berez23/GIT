package it.webred.ct.data.access.basic.ici;
import it.webred.ct.data.access.basic.CTServiceBaseBean;
import it.webred.ct.data.access.basic.common.dto.RicercaOggettoDTO;
import it.webred.ct.data.access.basic.ici.dao.IciDAO;
import it.webred.ct.data.access.basic.ici.dto.OggettoIciDTO;
import it.webred.ct.data.access.basic.ici.dto.RicercaOggettoIciDTO;
import it.webred.ct.data.access.basic.ici.dto.RicercaSoggettoIciDTO;
import it.webred.ct.data.access.basic.ici.dto.RicercaViaIciDTO;
import it.webred.ct.data.access.basic.ici.dto.VersamentoIciAnnoDTO;
import it.webred.ct.data.model.ici.SitTIciOggetto;
import it.webred.ct.data.model.ici.SitTIciSogg;
import it.webred.ct.data.model.ici.SitTIciVia;
import it.webred.ct.data.model.ici.VTIciSoggAll;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
@Stateless
public class IciServiceBean extends CTServiceBaseBean implements IciService {
 
	@Autowired 
	private IciDAO iciDAO;
	
	@Override
	public List<SitTIciSogg> getListaSogg(RicercaSoggettoIciDTO rs) {
		List<SitTIciSogg> lista=null;
		if (rs.getCodFis()!=null && !rs.getCodFis().equals(""))
			lista = iciDAO.getListaSoggByCF(rs);
		return lista;
	}

	@Override
	public List<OggettoIciDTO> getListaOggettiByIdSogg(RicercaSoggettoIciDTO rs) {
		List<OggettoIciDTO> lista=null;
		if (rs.getIdSoggIci()!=null && !rs.getIdSoggIci().equals(""))
			lista = iciDAO.getListaOggettiByIdSogg(rs);
		return lista;
		
	}

	@Override
	public List<VTIciSoggAll> getListaSoggettiByOgg(RicercaOggettoIciDTO ro) {
		List<VTIciSoggAll> lista=null;
		logger.debug("IciServiceBean - getListaSoggettiByOgg. IdExt:" + ro.getIdExt());
		if (ro.getIdExt()!=null && !ro.getIdExt().equals(""))
			lista = iciDAO.getListaSoggettiByOgg(ro);
		return lista;
				
	}

	@Override
	public List<SitTIciSogg> searchSoggetto(RicercaSoggettoIciDTO rs) {
		List<SitTIciSogg> lista=null;
		lista = iciDAO.searchSoggetto(rs);
		return lista;
	}
	public List<VersamentoIciAnnoDTO> getSommaVersamenti(RicercaSoggettoIciDTO rs) {
		return iciDAO.getSommaVersamenti(rs);
	}

	@Override
	public SitTIciSogg getSoggettoById(RicercaSoggettoIciDTO rs) {
		return iciDAO.getSoggettoById(rs);	
	}

	@Override
	public SitTIciVia getViaByIdExt(RicercaViaIciDTO rv) {
		return iciDAO.getViaByIdExt(rv);
	}	
	
	public List<SitTIciOggetto> getOggettiByFabbricato(RicercaOggettoDTO ro)	{
		return iciDAO.getOggettiByFabbricato(ro);
	}

	@Override
	public List<SitTIciOggetto> getOggettiByUI(RicercaOggettoDTO ro) {
		return iciDAO.getOggettiByUI(ro);
		
	}

	@Override
	public List<SitTIciOggetto> getListaOggettiAiCiviciIci(RicercaOggettoIciDTO ro) {
		return iciDAO.getListaOggettiAiCiviciIci(ro);
	}
}
