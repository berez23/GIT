package it.webred.cs.csa.ejb.ejb;

import it.webred.cs.csa.ejb.CarSocialeBaseSessionBean;
import it.webred.cs.csa.ejb.client.AccessTableConfigurazioneSessionBeanRemote;
import it.webred.cs.csa.ejb.dao.ConfigurazioneDAO;
import it.webred.cs.csa.ejb.dao.IndirizzoDAO;
import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.data.model.CsAAnaIndirizzo;
import it.webred.cs.data.model.CsCfgParametri;
import it.webred.cs.data.model.CsOOrganizzazione;
import it.webred.cs.data.model.CsOSettore;
import it.webred.cs.data.model.CsTbBuono;
import it.webred.cs.data.model.CsTbContatto;
import it.webred.cs.data.model.CsTbDisabEnte;
import it.webred.cs.data.model.CsTbDisabGravita;
import it.webred.cs.data.model.CsTbDisabTipologia;
import it.webred.cs.data.model.CsTbDisponibilita;
import it.webred.cs.data.model.CsTbEsenzioneRiduzione;
import it.webred.cs.data.model.CsTbIcd10;
import it.webred.cs.data.model.CsTbIcd9;
import it.webred.cs.data.model.CsTbInterventiUOL;
import it.webred.cs.data.model.CsTbMacroSegnal;
import it.webred.cs.data.model.CsTbMicroSegnal;
import it.webred.cs.data.model.CsTbMotivoChiusuraInt;
import it.webred.cs.data.model.CsTbMotivoSegnal;
import it.webred.cs.data.model.CsTbPotesta;
import it.webred.cs.data.model.CsTbProblematica;
import it.webred.cs.data.model.CsTbProfessione;
import it.webred.cs.data.model.CsTbResponsabilita;
import it.webred.cs.data.model.CsTbScuola;
import it.webred.cs.data.model.CsTbServComunita;
import it.webred.cs.data.model.CsTbServLuogoStr;
import it.webred.cs.data.model.CsTbServResRetta;
import it.webred.cs.data.model.CsTbServSemiresRetta;
import it.webred.cs.data.model.CsTbSettoreImpiego;
import it.webred.cs.data.model.CsTbStatoCivile;
import it.webred.cs.data.model.CsTbStatus;
import it.webred.cs.data.model.CsTbStesuraRelazioniPer;
import it.webred.cs.data.model.CsTbTipoCirc4;
import it.webred.cs.data.model.CsTbTipoComunita;
import it.webred.cs.data.model.CsTbTipoContratto;
import it.webred.cs.data.model.CsTbTipoContributo;
import it.webred.cs.data.model.CsTbTipoDiario;
import it.webred.cs.data.model.CsTbTipoIndirizzo;
import it.webred.cs.data.model.CsTbTipoOperatore;
import it.webred.cs.data.model.CsTbTipoProgetto;
import it.webred.cs.data.model.CsTbTipoRapportoCon;
import it.webred.cs.data.model.CsTbTipoRetta;
import it.webred.cs.data.model.CsTbTipoRientriFami;
import it.webred.cs.data.model.CsTbTipoScuola;
import it.webred.cs.data.model.CsTbTipologiaFamiliare;
import it.webred.cs.data.model.CsTbTitoloStudio;
import it.webred.cs.data.model.CsTbTutela;
import it.webred.cs.data.model.CsTbTutelante;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.util.List;

import javax.ejb.Stateless;

import org.apache.commons.pool.BaseObjectPool;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class AccessTableDataStatoCivileSessionBean
 */
@Stateless
public class AccessTableConfigurazioneSessionBean extends CarSocialeBaseSessionBean implements AccessTableConfigurazioneSessionBeanRemote {

	@Autowired
	private ConfigurazioneDAO configurazioneDAO;
	@Autowired
	private IndirizzoDAO indirizzoDAO;

    public AccessTableConfigurazioneSessionBean() {
        // TODO Auto-generated constructor stub
    }
    
    public List<CsTbStatoCivile> getStatoCivile(CeTBaseObject cet) {
    	return configurazioneDAO.getStatoCivile();
    }
    
    public CsTbStatoCivile getStatoCivileByIdOrigCet(BaseDTO dto) {
    	return configurazioneDAO.getStatoCivileByIdOrigCet(dto.getEnteId(), (String) dto.getObj());
    }
    
    public CsTbStatoCivile getStatoCivileByDescrizione(BaseDTO dto) {
    	return configurazioneDAO.getStatoCivileByDescrizione((String) dto.getObj());
    }
    
    public List<CsTbStatus> getStatus(CeTBaseObject cet) {
    	return configurazioneDAO.getStatus();
    }
    
    public List<CsTbTipoIndirizzo> getTipoIndirizzo(CeTBaseObject cet) {
    	return configurazioneDAO.getTipoIndirizzo();
    }
    
    public List<CsTbTipoContributo> getTipoContributo(CeTBaseObject cet) {
    	return configurazioneDAO.getTipoContributo();
    }
    
    public List<CsTbTipologiaFamiliare> getTipologieFamiliari(CeTBaseObject cet) {
    	return configurazioneDAO.getTipologieFamiliari();
    }
    
	public CsTbTipologiaFamiliare getTipologiaFamiliareById(BaseDTO dto) {
		return configurazioneDAO.getTipologiaFamiliareById((Long) dto.getObj());
	}
    
    public List<CsTbResponsabilita> getResponsabilita(CeTBaseObject cet) {
    	return configurazioneDAO.getResponsabilita();
    }
    
    public List<CsTbProblematica> getProblematiche(CeTBaseObject cet) {
    	return configurazioneDAO.getProblematiche();
    }
    
    public CsTbProblematica getProblematicaById(BaseDTO dto) {
    	return configurazioneDAO.getProblematicaById((Long) dto.getObj());
    }
    
    public List<CsTbStesuraRelazioniPer> getStesuraRelazioniPer(CeTBaseObject cet) {
    	return configurazioneDAO.getStesuraRelazioniPer();
    }
    
    public List<CsTbTitoloStudio> getTitoliStudio(CeTBaseObject cet) {
    	return configurazioneDAO.getTitoliStudio();
    }
    
    public CsTbTitoloStudio getTitoloStudioById(BaseDTO dto) {
    	return configurazioneDAO.getTitoloStudioById((Long) dto.getObj());
    }
    
    public List<CsTbProfessione> getProfessioni(CeTBaseObject cet) {
    	return configurazioneDAO.getProfessioni();
    }
    
    @Override
    public List<CsTbSettoreImpiego> getSettoreImpiego(CeTBaseObject cet) {
    	return configurazioneDAO.getSettoreImpiego();
    }
    
    @Override
    public List<CsTbTipoContratto> getTipoContratto(CeTBaseObject cet) {
    	return configurazioneDAO.getTipoContratto();
    }
    
    public List<CsTbTutela> getTutele(CeTBaseObject cet) {
    	return configurazioneDAO.getTutele();
    }
    
    public List<CsTbTutelante> getTutelanti(CeTBaseObject cet) {
    	return configurazioneDAO.getTutelanti();
    }
    
    public List<CsOOrganizzazione> getOrganizzazioni(CeTBaseObject cet) {
    	return configurazioneDAO.getOrganizzazioni();
    }
    
    public List<CsOOrganizzazione> getOrganizzazioniBelfiore(CeTBaseObject cet) {
    	return configurazioneDAO.getOrganizzazioniBelfiore();
    }
    
    public List<CsOOrganizzazione> getOrganizzazioniAll(CeTBaseObject cet) {
    	return configurazioneDAO.getOrganizzazioniAll();
    }
    
	public void salvaOrganizzazione(BaseDTO dto) {
		configurazioneDAO.salvaOrganizzazione((CsOOrganizzazione) dto.getObj());
	}
	
	public void updateOrganizzazione(BaseDTO dto) {
		configurazioneDAO.updateOrganizzazione((CsOOrganizzazione) dto.getObj());
	}
	
	public void eliminaOrganizzazione(BaseDTO dto) {
		configurazioneDAO.eliminaOrganizzazione((Long) dto.getObj());
	}
    
	public List<CsOSettore> findSettoreByOrganizzazione(BaseDTO dto) {
		return configurazioneDAO.findSettoreByOrganizzazione((Long) dto.getObj());
	}
	
	public void salvaSettore(BaseDTO dto) {
		CsOSettore sett = (CsOSettore) dto.getObj();
		if(sett.getCsAAnaIndirizzo() != null && sett.getCsAAnaIndirizzo().getId() == null) {
			CsAAnaIndirizzo anaInd = indirizzoDAO.saveAnaIndirizzo(sett.getCsAAnaIndirizzo());
			sett.setCsAAnaIndirizzo(anaInd);
		}
		configurazioneDAO.salvaSettore( sett );
	}
	
	public void updateSettore(BaseDTO dto) {
		configurazioneDAO.updateSettore((CsOSettore) dto.getObj());
	}
	
	public void eliminaSettore(BaseDTO dto) {
		CsOSettore cs = (CsOSettore) dto.getObj();
		configurazioneDAO.eliminaSettore(cs.getId());
	}
    
    public CsTbIcd10 getIcd10ById(BaseDTO dto) {
    	return configurazioneDAO.getIcd10ById((Long) dto.getObj());
    }
    
    public List<String> getIcd10CodIniziali(CeTBaseObject cet) {
    	return configurazioneDAO.getIcd10CodIniziali();
    }
    
    public List<CsTbIcd10> getIcd10ByCodIniziali(BaseDTO dto) {
    	return configurazioneDAO.getIcd10ByCodIniziali((String) dto.getObj());
    }
    
    public CsTbIcd9 getIcd9ById(BaseDTO dto) {
    	return configurazioneDAO.getIcd9ById((Long) dto.getObj());
    }
    
    public List<String> getIcd9CodIniziali(CeTBaseObject cet) {
    	return configurazioneDAO.getIcd9CodIniziali();
    }
    
    public List<CsTbIcd9> getIcd9ByCodIniziali(BaseDTO dto) {
    	return configurazioneDAO.getIcd9ByCodIniziali((String) dto.getObj());
    }
    
	public List<CsTbTipoRapportoCon> getTipoRapportoConParenti(CeTBaseObject cet) {
    	return configurazioneDAO.getTipoRapportoConParenti();
    }
	
	public List<CsTbTipoRapportoCon> getTipoRapportoConConoscenti(CeTBaseObject cet) {
    	return configurazioneDAO.getTipoRapportoConConoscenti();
    }
    
    public List<CsTbPotesta> getPotesta(CeTBaseObject cet) {
    	return configurazioneDAO.getPotesta();
    }
    
	public List<CsTbDisponibilita> getDisponibilita(CeTBaseObject cet) {
    	return configurazioneDAO.getDisponibilita();
    }
    
	public List<CsTbContatto> getContatti(CeTBaseObject cet) {
    	return configurazioneDAO.getContatti();
    }
	
	public List<CsTbMacroSegnal> getMacroSegnalazioni(CeTBaseObject cet) {
    	return configurazioneDAO.getMacroSegnalazioni();
    }
	
	public CsTbMacroSegnal getMacroSegnalazioneById(BaseDTO dto) {
		return configurazioneDAO.getMacroSegnalazioneById((Long) dto.getObj());
	}
	
	public List<CsTbMicroSegnal> getMicroSegnalazioni(CeTBaseObject cet) {
    	return configurazioneDAO.getMicroSegnalazioni();
    }
	
	public CsTbMicroSegnal getMicroSegnalazioneById(BaseDTO dto) {
		return configurazioneDAO.getMicroSegnalazioneById((Long) dto.getObj());
	}
	
	public List<CsTbMotivoSegnal> getMotivoSegnalazioni(CeTBaseObject cet) {
    	return configurazioneDAO.getMotivoSegnalazioni();
    }
	
	public CsTbMotivoSegnal getMotivoSegnalazioneById(BaseDTO dto) {
		return configurazioneDAO.getMotivoSegnalazioneById((Long) dto.getObj());
	}
	
	public List<CsTbDisabEnte> getDisabEnte(CeTBaseObject cet) {
    	return configurazioneDAO.getDisabEnte();
    }
	
	public List<CsTbDisabGravita> getDisabGravita(CeTBaseObject cet) {
    	return configurazioneDAO.getDisabGravita();
    }
	
    public CsTbDisabGravita getDisabGravitaById(BaseDTO dto) {
    	return configurazioneDAO.getDisabGravitaById((Long) dto.getObj());
    }
	
	public List<CsTbDisabTipologia> getDisabTipologia(CeTBaseObject cet) {
    	return configurazioneDAO.getDisabTipologia();
    }
	
    public CsTbDisabTipologia getDisabTipologiaById(BaseDTO dto) {
    	return configurazioneDAO.getDisabTipologiaById((Long) dto.getObj());
    }
	
	public List<CsTbServComunita> getServComunita(CeTBaseObject cet) {
    	return configurazioneDAO.getServComunita();
    }
	
	public List<CsTbServLuogoStr> getServLuogoStr(CeTBaseObject cet) {
    	return configurazioneDAO.getServLuogoStr();
    }
	
	public List<CsTbServResRetta> getServResRetta(CeTBaseObject cet) {
    	return configurazioneDAO.getServResRetta();
    }
	
	public List<CsTbServSemiresRetta> getServSemiresRetta(CeTBaseObject cet) {
    	return configurazioneDAO.getServSemiresRetta();
    }
	
	public List<CsTbBuono> getBuoni(CeTBaseObject cet) {
    	return configurazioneDAO.getBuoni();
    }
	
	public List<CsTbEsenzioneRiduzione> getEsenzioniRiduzioni(CeTBaseObject cet) {
    	return configurazioneDAO.getEsenzioniRiduzioni();
    }
	
	
	public List<CsTbMotivoChiusuraInt> getMotiviChiusuraIntervento(CeTBaseObject cet) {
		return configurazioneDAO.getMotiviChiusuraIntervento();
	}
	
	public CsTbMotivoChiusuraInt getMotivoChiusuraIntervento(BaseDTO cet) {
		String id = (String)cet.getObj();
		if(id!=null && id.length()>0)
			return configurazioneDAO.getMotivoChiusuraIntervento(new Long((String)cet.getObj()));
		else
			return null;
	}

	@Override
	public List<CsTbTipoDiario> getTipiDiario(CeTBaseObject cet) {
		return configurazioneDAO.getTipiDiario();
	}

	@Override
	public List<CsTbTipoRetta> getTipiRetta(CeTBaseObject cet) {
		return configurazioneDAO.getTipiRetta();
	}

	@Override
	public List<CsTbTipoRientriFami> getTipiRientriFami(CeTBaseObject cet) {
		return configurazioneDAO.getTipiRientriFami();
	}

	@Override
	public List<CsTbTipoComunita> getTipiComunita(CeTBaseObject cet) {
		return configurazioneDAO.getTipiComunita();
	}
	
	@Override
	public CsTbTipoComunita findTipoComunitaByDesc(BaseDTO cet) {
		return configurazioneDAO.getTipoComunitaByDesc((String)cet.getObj());
	}

	@Override
	public CsTbTipoRetta getTipoRetta(BaseDTO cet) {
		return configurazioneDAO.getTipoRetta((Long)cet.getObj());
	}
	
	@Override
	public CsTbTipoContributo getTipoContributo(BaseDTO cet) {
		return configurazioneDAO.getTipoContributo((Long)cet.getObj());
	}
	
	@Override
	public CsTbTipoRientriFami getTipoRientriFami(BaseDTO cet) {
		return configurazioneDAO.getTipoRientriFami((Long)cet.getObj());
	}

	@Override
	public List<CsTbTipoOperatore> getTipiOperatore(CeTBaseObject cet) {
		return configurazioneDAO.getTipiOperatore();
	}
	
	@Override
	public List<CsTbInterventiUOL> getinterventiUOL(CeTBaseObject cet) {
		return configurazioneDAO.getInterventiUOL();
	}
	
	@Override
    public CsTbInterventiUOL getInterventiUOLById(BaseDTO dto) {
    	return configurazioneDAO.getInterventiUOLById((Long) dto.getObj());
    }
	
	@Override
	public List<CsTbTipoCirc4> getTipiCirc4(CeTBaseObject cet) {
		return configurazioneDAO.getTipiCirc4();
	}
	
	@Override
    public CsTbTipoCirc4 getTipoCirc4ById(BaseDTO dto) {
    	return configurazioneDAO.getTipoCirc4ById((Long) dto.getObj());
    }
	
	@Override
	public List<CsTbTipoProgetto> getTipiProgetto(CeTBaseObject cet) {
		return configurazioneDAO.getTipiProgetto();
	}
	
	@Override
    public CsTbTipoProgetto getTipoProgettoById(BaseDTO dto) {
    	return configurazioneDAO.getTipoProgettoById((Long) dto.getObj());
    }
	
	@Override
    public CsCfgParametri getParametro(BaseDTO dto) {
    	return configurazioneDAO.getParametro((String) dto.getObj(), (String) dto.getObj2());
    }
	
	@Override
	public List<CsTbScuola> getScuole(CeTBaseObject cet) {
		return configurazioneDAO.getScuole();
	}
	
	@Override
	public List<CsTbScuola> getScuoleByAnnoTipo(BaseDTO dto) {
		return configurazioneDAO.getScuoleByAnnoTipo((String)dto.getObj(), (Long)dto.getObj2());
	}
	
	@Override
	public List<CsTbTipoScuola> getTipoScuole(CeTBaseObject cet) {
		return configurazioneDAO.getTipoScuole();
	}
}
