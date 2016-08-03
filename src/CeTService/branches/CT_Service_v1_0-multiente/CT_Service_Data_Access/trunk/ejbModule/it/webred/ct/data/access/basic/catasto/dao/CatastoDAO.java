package it.webred.ct.data.access.basic.catasto.dao;

import it.webred.ct.data.access.basic.catasto.dto.DataVariazioneImmobileDTO;
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
import it.webred.ct.data.model.catasto.SitRepTarsu;
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
 
public interface CatastoDAO {
	public Siticomu getSitiComu(RicercaOggettoCatDTO ro) ;
	
	public List<Siticomu> getListaSiticomu(String descr) ;
	
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
	public Long getCatastoRecordCount(RicercaOggettoCatDTO ro);
	
	public List<SintesiImmobileDTO> getListaImmobili(RicercaOggettoCatDTO ro);
	
	public List<IndirizzoDTO> getListaIndirizziImmobile(RicercaOggettoCatDTO ro);

	public List<SoggettoDTO> getListaSoggettiImmobile(RicercaOggettoCatDTO ro);
	
	public DettaglioCatastaleImmobileDTO getDettaglioImmobile(RicercaOggettoCatDTO ro);
	
	public List<PlanimetriaComma340DTO> getPlanimetrieComma340(RicercaOggettoCatDTO ro);

	public ArrayList<IndirizzoDTO> getLocalizzazioneCatastale(RicercaOggettoCatDTO ro);
	
	public List<SitiediVani> getDettaglioVaniC340(RicercaOggettoCatDTO ro);
	
	public List<SitiediVani> getDettaglioVaniC340AllaData(RicercaOggettoCatDTO ro);
	
	public List<DataVariazioneImmobileDTO> getDateVariazioniImmobile(RicercaOggettoCatDTO ro);
	
	public List<SitRepTarsu> getReportTarsuList(RicercaOggettoCatDTO ro);
	
	public List<ParticellaKeyDTO> getListaParticelle(RicercaOggettoCatDTO ro);
	//decodifiche
	public Sitideco getSitideco(RicercaOggettoCatDTO ro);
	//ricerca del soggetto in anagrafe
	public List<BigDecimal> getIdSoggByCF(RicercaSoggettoCatDTO rs);
	public BigDecimal getIdSoggByCFAllaData(RicercaSoggettoCatDTO rs);
	public List<BigDecimal> getIdSoggByDatiAnag(RicercaSoggettoCatDTO rs);//ricerca della persona fisica per dati anagrafici
	public List<BigDecimal> getIdSoggByPI(RicercaSoggettoCatDTO rs);
	public BigDecimal getIdSoggByPIAllaData(RicercaSoggettoCatDTO rs);
	public List<BigDecimal> getIdSoggPGByDatiAnag(RicercaSoggettoCatDTO rs);//ricerca della persona giuridica per dati anagrafici
	public ConsSoggTab getSoggettoBYID(RicercaSoggettoCatDTO rs);
	public List<ConsSoggTab> getSoggettoByPkCuaa(RicercaSoggettoCatDTO rs);
	public List<SoggettoDTO> getListaSoggettiAttualiTerreno(RicercaOggettoCatDTO ro);
	public List<SoggettoDTO> getListaSoggettiStoriciTerreno(RicercaOggettoCatDTO ro);
	
	//dati particella
	public List<Sitiuiu> getListaUiAllaData(RicercaOggettoCatDTO ro) ;
	public List<IndirizzoDTO> getListaIndirizziPartAllaData(RicercaOggettoCatDTO ro);
	//ricerca u.i./terreni e dati relativi
	public Sitiuiu getDatiUiAllaData(RicercaOggettoCatDTO ro);
	public SiticonduzImmAll getDatiBySoggUiAllaData(RicercaOggettoCatDTO ro, RicercaSoggettoCatDTO rs);
	public List<SiticonduzImmAll> getDatiBySoggFabbricatoAllaData(RicercaOggettoCatDTO ro, RicercaSoggettoCatDTO rs); 
	public List<SiticonduzImmAll> getImmobiliByIdSogg(RicercaSoggettoCatDTO rs);
	public List<SiticonduzImmAll> getImmobiliByIdSoggAllaData(RicercaSoggettoCatDTO rs);
	public List<TerrenoPerSoggDTO> getTerreniByIdSogg(RicercaSoggettoCatDTO rs);
	public List<TerrenoPerSoggDTO> getTerreniByIdSoggAllaData(RicercaSoggettoCatDTO rs);
	public List<SiticonduzImmAll> getImmobiliByIdSoggCedutiInRangeDate(RicercaSoggettoCatDTO rs);
	public List<TerrenoPerSoggDTO> getTerreniByIdSoggCedutiInRangeDate(RicercaSoggettoCatDTO rs);
	public List<Sititrkc> getListaTerreniByFP(RicercaOggettoCatDTO ro);

}
