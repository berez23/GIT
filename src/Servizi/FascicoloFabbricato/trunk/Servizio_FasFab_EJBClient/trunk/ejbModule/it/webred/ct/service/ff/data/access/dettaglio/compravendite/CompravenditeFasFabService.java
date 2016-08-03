package it.webred.ct.service.ff.data.access.dettaglio.compravendite;

import java.util.List;

import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.service.ff.data.access.dettaglio.compravendite.dto.DatiCompravenditeDTO;

import javax.ejb.Remote;

@Remote
public interface CompravenditeFasFabService {
	
	public List<DatiCompravenditeDTO> getListaCompravenditeUiu(RicercaOggettoCatDTO ro);
	public List<DatiCompravenditeDTO> getListaCompravenditeTerreno(RicercaOggettoCatDTO ro);
	public List<DatiCompravenditeDTO> getListaCompravenditeParticella(RicercaOggettoCatDTO ro) ;

}
