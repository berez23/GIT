package it.webred.ct.data.access.basic.catasto;

import it.webred.ct.data.access.basic.catasto.dto.DettaglioCatastaleImmobileDTO;
import it.webred.ct.data.access.basic.catasto.dto.IndirizzoDTO;
import it.webred.ct.data.access.basic.catasto.dto.ParticellaKeyDTO;
import it.webred.ct.data.access.basic.catasto.dto.PlanimetriaComma340DTO;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.catasto.dto.RicercaSoggettoCatDTO;
import it.webred.ct.data.access.basic.catasto.dto.SintesiImmobileDTO;
import it.webred.ct.data.access.basic.catasto.dto.SoggettoDTO;
import it.webred.ct.data.access.basic.catasto.dto.TerrenoPerSoggDTO;
import it.webred.ct.data.model.catasto.ConsSoggTab;
import it.webred.ct.data.model.catasto.Siticivi;
import it.webred.ct.data.model.catasto.Siticomu;
import it.webred.ct.data.model.catasto.SiticonduzImmAll;
import it.webred.ct.data.model.catasto.Sitideco;
import it.webred.ct.data.model.catasto.Sitidstr;
import it.webred.ct.data.model.catasto.SitiediVani;
import it.webred.ct.data.model.catasto.Sititrkc;
import it.webred.ct.data.model.catasto.Sitiuiu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

@Remote
public interface CatastoService {
	
	public Siticomu getSitiComu(RicercaOggettoCatDTO ro) ;
	
	public List<Siticomu> getListaSiticomu(String descr);
	
	public List<Siticomu> getSiticomuSezioni(RicercaOggettoCatDTO ro) ;
	
	//Suggerimenti 
	public List<Sitideco> getListaCategorieImmobile(RicercaOggettoCatDTO ro);
	
	public List<Sitidstr> getListaVie(RicercaOggettoCatDTO ro);
	
	public List<Siticivi> getListaIndirizzi(RicercaOggettoCatDTO ro);
	
	public List<ConsSoggTab> getListaSoggettiF(RicercaSoggettoCatDTO rs);
	
	public List<ConsSoggTab> getListaSoggettiG(RicercaSoggettoCatDTO rs);
	
	public List<ConsSoggTab> getSoggettoByCF(RicercaSoggettoCatDTO rs);
	
	public ConsSoggTab getSoggettoByPIVA(RicercaSoggettoCatDTO rs);
	
	//Query di ricerca sul catasto
	public List<SintesiImmobileDTO> getListaImmobili(RicercaOggettoCatDTO ro);
	
	public Long getCatastoRecordCount(RicercaOggettoCatDTO ro);
	
	public DettaglioCatastaleImmobileDTO getDettaglioImmobile(RicercaOggettoCatDTO ro);
	
	public ArrayList<IndirizzoDTO> getLocalizzazioneCatastale(RicercaOggettoCatDTO ro);
	
	public List<ParticellaKeyDTO> getListaParticelle(RicercaOggettoCatDTO ro);
	//decodifiche
	public Sitideco getSitideco(RicercaOggettoCatDTO ro);
	// ricerca soggetto
	public List<BigDecimal> getIdSoggByCF(RicercaSoggettoCatDTO rs);
	public BigDecimal getIdSoggByCFAllaData(RicercaSoggettoCatDTO rs);
	public List<BigDecimal> getIdSoggByDatiAnag(RicercaSoggettoCatDTO rs);//ricerca della persona fisica per dati anagrafici
	public List<BigDecimal> getIdSoggByPI(RicercaSoggettoCatDTO rs);
	public BigDecimal getIdSoggByPIAllaData(RicercaSoggettoCatDTO rs);
	public List<BigDecimal> getIdSoggPGByDatiAnag(RicercaSoggettoCatDTO rs);//ricerca della persona giuridica per dati anagrafici
	public ConsSoggTab getSoggettoByID(RicercaSoggettoCatDTO rs);
	public List<SoggettoDTO> getListaSoggettiAttualiTerreno(RicercaOggettoCatDTO ro);
	public List<SoggettoDTO> getListaSoggettiStoriciTerreno(RicercaOggettoCatDTO ro);
	public List<SoggettoDTO> getListaSoggettiImmobile(RicercaOggettoCatDTO ro);
	public List<SoggettoDTO> getListaSoggettiAttualiImmobile(RicercaOggettoCatDTO ro);
	public List<SoggettoDTO> getListaSoggettiStoriciImmobile(RicercaOggettoCatDTO ro);
	//dati PARTICELLA
	public List<IndirizzoDTO> getListaIndirizziPartAllaData(RicercaOggettoCatDTO ro) ;
	public List<Sitiuiu> getListaUiAllaData(RicercaOggettoCatDTO ro);
	//dati u.i e terreni
	public Sitiuiu getDatiUiAllaData(RicercaOggettoCatDTO ro);
	public SiticonduzImmAll getDatiBySoggUiAllaData(RicercaOggettoCatDTO ro, RicercaSoggettoCatDTO rs);
	public List<SiticonduzImmAll> getDatiBySoggFabbricatoAllaData(RicercaOggettoCatDTO ro, RicercaSoggettoCatDTO rs);
	public List<SiticonduzImmAll> getImmobiliByIdSogg(RicercaSoggettoCatDTO rs);
	public List<TerrenoPerSoggDTO> getTerreniByIdSogg(RicercaSoggettoCatDTO rs);
	public List<SiticonduzImmAll> getImmobiliByIdSoggCedutiInRangeDate(RicercaSoggettoCatDTO rs);
	public List<TerrenoPerSoggDTO> getTerreniByIdSoggCedutiInRangeDate(RicercaSoggettoCatDTO rs);
	public List<Sititrkc> getListaTerreniByFP(RicercaOggettoCatDTO ro);
	//SitiediVani
	public List<SitiediVani> getDettaglioVaniC340(RicercaOggettoCatDTO ro);
	public BigDecimal calcolaSupUtileTarsuC30(RicercaOggettoCatDTO ro);
	
	public List<IndirizzoDTO> getListaIndirizziImmobile(RicercaOggettoCatDTO ro);
	
	
	public List<PlanimetriaComma340DTO> getPlanimetrieComma340(RicercaOggettoCatDTO ro);

}
