package it.webred.gitout;

import it.webred.gitout.dto.RespCompravenditeDTO;
import it.webred.gitout.dto.RespCoordDTO;
import it.webred.gitout.dto.RespDocfaDTO;
import it.webred.gitout.dto.RespElettricheFornitureDTO;
import it.webred.gitout.dto.RespGasFornitureDTO;
import it.webred.gitout.dto.RespLocazioniDTO;
import it.webred.gitout.dto.RespPregeoDTO;
import it.webred.gitout.dto.RespSoggettoCatDTO;
import it.webred.gitout.dto.RespSuccessioniDTO;
import it.webred.gitout.dto.RespUnitaImmoDTO;

public interface GitOutService {
	
	public RespCoordDTO getCoordUiByTopo(String belfiore, String utente, String ot_prik, String tipoArea, String nomeVia, String civico);
	public RespUnitaImmoDTO getUiuTitByCoord(String belfiore, String utente, String ot_prik, String sezione, String foglio, String particella, String subalterno, String dataValidita);
	public RespSoggettoCatDTO getPersFGByCFPI(String belfiore, String utente, String ot_prik, String identificativo, String tipologia);
	public RespCompravenditeDTO getCompravenditeByImmoSogg( String belfiore, String utente, String ot_prik, String sezione, String foglio, String particella, String subalterno, String identificativo, String tipologia);
	public RespDocfaDTO getDocfaByParams( String belfiore, String utente, String ot_prik, String sezione, String foglio, String particella, String subalterno, String protocollo, String dataFornitura);	
	public RespElettricheFornitureDTO getElettricByParams(String belfiore, String utente, String ot_prik, String sezione, String foglio, String particella, String subalterno, String identificativo, String tipologia, String tipoArea, String nomeVia, String civico);
	public RespGasFornitureDTO getGasByParams(String belfiore, String utente, String ot_prik, String identificativo, String tipologia, String tipoArea, String nomeVia, String civico);
	public RespLocazioniDTO getLocazioniByCoordAnag(String belfiore, String utente, String ot_prik, String sezione, String foglio, String particella, String subalterno, String identificativo, String tipologia, String tipoCoinvolgimento);	
	public RespSuccessioniDTO getSuccessioniByCoordAnag(String belfiore, String utente, String ot_prik, String sezione, String foglio, String particella, String subalterno, String identificativo, String tipoCoinvolgimento);
	public RespPregeoDTO getPregeoByCoord(String belfiore, String utente, String ot_prik, String foglio, String particella);
	
}
