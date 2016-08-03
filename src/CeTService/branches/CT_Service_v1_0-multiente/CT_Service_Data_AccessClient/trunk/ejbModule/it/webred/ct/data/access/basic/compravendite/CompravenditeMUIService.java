package it.webred.ct.data.access.basic.compravendite;
import java.util.List;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.compravendite.dto.RicercaCompravenditeDTO;
import it.webred.ct.data.access.basic.compravendite.dto.SoggettoCompravenditeDTO;
import it.webred.ct.data.model.compravendite.MuiFabbricatiIdentifica;
import it.webred.ct.data.model.compravendite.MuiNotaTras;

import javax.ejb.Remote;

@Remote
public interface CompravenditeMUIService {
	//public List<MuiFabbricatiIdentifica>  getMuiFabbrIdentificaTest(RicercaOggettoCatDTO rc);
	public List<MuiNotaTras>  getListaNoteByFPS(RicercaOggettoCatDTO rc) throws CompravenditeMUIException;
	public List<MuiNotaTras>  getListaNoteTerrenoByFP(RicercaOggettoCatDTO rc) throws CompravenditeMUIException;
	public List<SoggettoCompravenditeDTO>  getListaSoggettiNota(RicercaCompravenditeDTO rc) throws CompravenditeMUIException;
}
