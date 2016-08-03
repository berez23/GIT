package it.webred.cs.csa.ejb.client;

import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.data.model.CsCCategoriaSociale;
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

import javax.ejb.Remote;

@Remote
public interface AccessTableConfigurazioneSessionBeanRemote {

	public List<CsTbStatoCivile> getStatoCivile(CeTBaseObject cet);

    public CsTbStatoCivile getStatoCivileByIdOrigCet(BaseDTO dto);
    
    public CsTbStatoCivile getStatoCivileByDescrizione(BaseDTO dto);
	
	public List<CsTbStatus> getStatus(CeTBaseObject cet);

	public List<CsTbTipoIndirizzo> getTipoIndirizzo(CeTBaseObject cet);

    public List<CsTbTipoContributo> getTipoContributo(CeTBaseObject cet);
	
	public List<CsTbTipologiaFamiliare> getTipologieFamiliari(CeTBaseObject cet);

	public CsTbTipologiaFamiliare getTipologiaFamiliareById(BaseDTO dto);
	
	public List<CsTbResponsabilita> getResponsabilita(CeTBaseObject cet);

	public List<CsTbProblematica> getProblematiche(CeTBaseObject cet);

    public CsTbProblematica getProblematicaById(BaseDTO dto);
	
	public List<CsTbStesuraRelazioniPer> getStesuraRelazioniPer(CeTBaseObject cet);

	public List<CsTbTitoloStudio> getTitoliStudio(CeTBaseObject cet);

    public CsTbTitoloStudio getTitoloStudioById(BaseDTO dto);
	
	public List<CsTbProfessione> getProfessioni(CeTBaseObject cet);

	public List<CsTbTutela> getTutele(CeTBaseObject cet);

	public List<CsTbTutelante> getTutelanti(CeTBaseObject cet);

    public void salvaOrganizzazione(BaseDTO dto);
	
	public void updateOrganizzazione(BaseDTO dto);
	
	public void eliminaOrganizzazione(BaseDTO dto);
    
    public List<CsOOrganizzazione> getOrganizzazioniAll(CeTBaseObject cet);
    
    public List<CsOOrganizzazione> getOrganizzazioni(CeTBaseObject cet);
    
    public List<CsOOrganizzazione> getOrganizzazioniBelfiore(CeTBaseObject cet);
    
	public List<CsOSettore> findSettoreByOrganizzazione(BaseDTO dto);
	
	public void salvaSettore(BaseDTO dto);
	
	public void updateSettore(BaseDTO dto);
	
	public void eliminaSettore(BaseDTO dto);
    
    public CsTbIcd10 getIcd10ById(BaseDTO dto);
    
    public List<String> getIcd10CodIniziali(CeTBaseObject cet);

    public List<CsTbIcd10> getIcd10ByCodIniziali(BaseDTO dto);

    public CsTbIcd9 getIcd9ById(BaseDTO dto);
    
    public List<String> getIcd9CodIniziali(CeTBaseObject cet);

    public List<CsTbIcd9> getIcd9ByCodIniziali(BaseDTO dto);
    
	public List<CsTbTipoRapportoCon> getTipoRapportoConParenti(CeTBaseObject cet);
	
	public List<CsTbTipoRapportoCon> getTipoRapportoConConoscenti(CeTBaseObject cet);
	
	public List<CsTbPotesta> getPotesta(CeTBaseObject cet);

	public List<CsTbDisponibilita> getDisponibilita(CeTBaseObject cet);

	public List<CsTbContatto> getContatti(CeTBaseObject cet);
	
	public List<CsTbMacroSegnal> getMacroSegnalazioni(CeTBaseObject cet);
	
	public CsTbMacroSegnal getMacroSegnalazioneById(BaseDTO dto);
	
	public List<CsTbMicroSegnal> getMicroSegnalazioni(CeTBaseObject cet);
	
	public CsTbMicroSegnal getMicroSegnalazioneById(BaseDTO dto);
	
	public List<CsTbMotivoSegnal> getMotivoSegnalazioni(CeTBaseObject cet);

	public CsTbMotivoSegnal getMotivoSegnalazioneById(BaseDTO dto);
	
	public List<CsTbDisabEnte> getDisabEnte(CeTBaseObject cet);
	
	public List<CsTbDisabGravita> getDisabGravita(CeTBaseObject cet);
	
    public CsTbDisabGravita getDisabGravitaById(BaseDTO dto);
	
	public List<CsTbDisabTipologia> getDisabTipologia(CeTBaseObject cet);
	
    public CsTbDisabTipologia getDisabTipologiaById(BaseDTO dto);
	
	public List<CsTbServComunita> getServComunita(CeTBaseObject cet);
	
	public List<CsTbServLuogoStr> getServLuogoStr(CeTBaseObject cet);
	
	public List<CsTbServResRetta> getServResRetta(CeTBaseObject cet);
	
	public List<CsTbServSemiresRetta> getServSemiresRetta(CeTBaseObject cet);
		
	public List<CsTbBuono> getBuoni(CeTBaseObject cet);

	public List<CsTbEsenzioneRiduzione> getEsenzioniRiduzioni(CeTBaseObject cet);
	
	public List<CsTbMotivoChiusuraInt> getMotiviChiusuraIntervento(CeTBaseObject cet);
	
	public CsTbMotivoChiusuraInt getMotivoChiusuraIntervento(BaseDTO cet);
		
	public List<CsTbTipoDiario> getTipiDiario(CeTBaseObject cet);

	public List<CsTbTipoRetta> getTipiRetta(CeTBaseObject cet);

	public List<CsTbTipoRientriFami> getTipiRientriFami(CeTBaseObject cet);

	public List<CsTbTipoComunita> getTipiComunita(CeTBaseObject cet);

	public CsTbTipoComunita findTipoComunitaByDesc(BaseDTO cet);

	public CsTbTipoRetta getTipoRetta(BaseDTO cet);
	
	public CsTbTipoRientriFami getTipoRientriFami(BaseDTO cet);

	public List<CsTbTipoOperatore> getTipiOperatore(CeTBaseObject cet);

	public List<CsTbInterventiUOL> getinterventiUOL(CeTBaseObject cet);

	public List<CsTbTipoCirc4> getTipiCirc4(CeTBaseObject cet);

	public List<CsTbTipoProgetto> getTipiProgetto(CeTBaseObject cet);

	public CsTbInterventiUOL getInterventiUOLById(BaseDTO dto);

	public CsTbTipoCirc4 getTipoCirc4ById(BaseDTO dto);

	public CsTbTipoProgetto getTipoProgettoById(BaseDTO dto);

	public CsCfgParametri getParametro(BaseDTO dto);

	public CsTbTipoContributo getTipoContributo(BaseDTO cet);

	public List<CsTbScuola> getScuole(CeTBaseObject cet);

	public List<CsTbScuola> getScuoleByAnnoTipo(BaseDTO dto);

	public List<CsTbTipoScuola> getTipoScuole(CeTBaseObject cet);

	public List<CsTbSettoreImpiego> getSettoreImpiego(CeTBaseObject cet);

	public List<CsTbTipoContratto> getTipoContratto(CeTBaseObject cet);;
}
