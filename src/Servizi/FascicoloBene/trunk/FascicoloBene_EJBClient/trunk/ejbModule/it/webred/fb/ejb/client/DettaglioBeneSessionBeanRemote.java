package it.webred.fb.ejb.client;

import it.webred.fb.data.model.DmBBene;
import it.webred.fb.data.model.DmBFascicolo;
import it.webred.fb.data.model.DmBIndirizzo;
import it.webred.fb.data.model.DmBInfo;
import it.webred.fb.data.model.DmBMappale;
import it.webred.fb.data.model.DmBTerreno;
import it.webred.fb.data.model.DmBTipoUso;
import it.webred.fb.data.model.DmDDoc;
import it.webred.fb.data.model.DmBTitolo;
import it.webred.fb.data.model.DmEEvento;
import it.webred.fb.ejb.dto.BaseDTO;
import it.webred.fb.ejb.dto.BeneInListaDTO;
import it.webred.fb.ejb.dto.DettaglioBeneDTO;
import it.webred.fb.ejb.dto.EventoDTO;
import it.webred.fb.ejb.dto.IndirizzoDTO;
import it.webred.fb.ejb.dto.KeyValueDTO;
import it.webred.fb.ejb.dto.MappaleDTO;
import it.webred.fb.ejb.dto.RicercaBeneDTO;
import it.webred.fb.ejb.dto.TitoloDTO;
import it.webred.fb.ejb.dto.locazione.DatiCatastali;
import it.webred.fb.ejb.dto.locazione.DatiLocalizzazione;
import it.webred.fb.ejb.dto.locazione.DatiTerreni;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;


@Remote
public interface DettaglioBeneSessionBeanRemote {
	public List<String> getListaCivici(RicercaBeneDTO b);
	public List<KeyValueDTO> getListaVieByDesc(RicercaBeneDTO b);
	
	public List<BeneInListaDTO> searchBene(RicercaBeneDTO rb);
	
	public DmBBene getDettaglioBeneById(BaseDTO b) throws Exception;
	
	public List<DatiCatastali> buildDatiCatastali(BaseDTO b) throws Exception;
	public List<DatiTerreni> buildDatiTerreni(BaseDTO b) throws Exception;
	
	public List<DatiLocalizzazione> buildDatiLocalizzazione(BaseDTO b) throws Exception;

	public List<KeyValueDTO> getListaCatInventario(BaseDTO b);
	public List<KeyValueDTO> getListaTipo(BaseDTO b);
	public List<String> getListaTipoDirittoReale(BaseDTO b);
	public List<KeyValueDTO> getListaComuni(RicercaBeneDTO b);
		
	public List<MappaleDTO> getMappales(BaseDTO dtoMap);
	public List<MappaleDTO> getMappalesInventario(BaseDTO dtoMap);
	
	public List<DmBIndirizzo> getIndirizziBene(BaseDTO b) throws Exception;
	public List<DmBIndirizzo> getIndirizziInventario(BaseDTO dtoMap);
	public List<IndirizzoDTO> getIndirizziInventarioDesc(BaseDTO dtoMap);
	
	public DmBBene getBeneByChiave(BaseDTO dtoBene);
	
	public List<DmDDoc> getDocumentiBene(BaseDTO b) throws Exception;
	public List<DmDDoc> getDocumentiBeneTree(BaseDTO b) throws Exception;
	
	public List<KeyValueDTO> getListaMacro(BaseDTO b) throws Exception;
	public HashMap<String,List<KeyValueDTO>> getListaCategorie(BaseDTO b) throws Exception;
	
	public List<DmDDoc> getTitoliDocs(BaseDTO b) throws Exception;
	public List<DmDDoc> getTitoliDocsBeneTree(BaseDTO b) throws Exception;
	
	public List<TitoloDTO> getTitoliBeneTree(BaseDTO b) throws Exception;
	
	public List<EventoDTO> getEventiBene(BaseDTO br);
	public DmBInfo getInfoBene(BaseDTO br);
	public DmBTipoUso getTipoUso(BaseDTO br);
	public DmBFascicolo getFascicolo(BaseDTO br);
	public String getDescrizioneBene(BaseDTO dtoBene);
	public KeyValueDTO getIndirizzoByCodVia(RicercaBeneDTO b);
	public KeyValueDTO getComuneByCod(RicercaBeneDTO b);
	public List<DmBTerreno> getTerreniBene(BaseDTO dtoMap);
	public List<DmBTerreno> getTerreniInventario(BaseDTO dtoMap);
	public BeneInListaDTO searchRadiceBeneById(BaseDTO rb);
	
	public List<KeyValueDTO> getLstFascicoli(RicercaBeneDTO rb);
	
	
	
	
	
	
}