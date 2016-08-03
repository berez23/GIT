package it.webred.cs.csa.ejb.client;

import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.csa.ejb.dto.PaiDTO;
import it.webred.cs.csa.ejb.dto.RelSettCatsocEsclusivaDTO;
import it.webred.cs.csa.ejb.dto.RelazioneDTO;
import it.webred.cs.csa.ejb.dto.SchedaBarthelDTO;
import it.webred.cs.data.model.CsAFamigliaGruppo;
import it.webred.cs.csa.ejb.dto.InterventoDTO;
import it.webred.cs.data.model.CsCDiarioConchi;
import it.webred.cs.data.model.CsCDiarioDove;
import it.webred.cs.data.model.CsCTipoColloquio;
import it.webred.cs.data.model.CsDClob;
import it.webred.cs.data.model.CsDColloquio;
import it.webred.cs.data.model.CsDDiario;
import it.webred.cs.data.model.CsDDiarioDoc;
import it.webred.cs.data.model.CsDDocIndividuale;
import it.webred.cs.data.model.CsDIsee;
import it.webred.cs.data.model.CsDPai;
import it.webred.cs.data.model.CsDRelazione;
import it.webred.cs.data.model.CsDScuola;
import it.webred.cs.data.model.CsDValutazione;
import it.webred.cs.data.model.CsRelSettCatsocEsclusiva;
import it.webred.cs.data.model.CsTbTipoDiario;

import java.util.List;

import javax.ejb.Remote;

/**
 * @author Andrea
 *
 */
@Remote
public interface AccessTableDiarioSessionBeanRemote {
	// TODO: HiWeb
	public CsDDiario createDiario(BaseDTO dto) throws Exception;
	
	public CsDDiario updateDiario(BaseDTO dto) throws Exception;
		
	public void saveDiarioChild(BaseDTO dto);
	
	public List<CsDColloquio> getColloquios(BaseDTO dto) throws Exception;

	public List<CsCTipoColloquio> getTipoColloquios(BaseDTO dto) throws Exception;

	public List<CsCDiarioDove> getDiarioDoves(BaseDTO dto);

	public List<CsCDiarioConchi> getDiarioConchis(BaseDTO dto);

	public void updateColloquio(BaseDTO dto) throws Exception;
	
	public CsTbTipoDiario findTipoDiarioById(BaseDTO tipoDiarioIdDTO) throws Exception;

	public CsAFamigliaGruppo getFamigliaGruppoCorr(BaseDTO anaFamCurrDto);
	
	public CsDDiario findDiarioById(BaseDTO dto) throws Exception;
	
	public List<RelazioneDTO> findRelazioniByCaso(InterventoDTO i);
	
	public List<CsDRelazione> findRelazioniByCasoTipoIntervento(InterventoDTO i);
	
	public CsDRelazione findRelazioneById(BaseDTO dto);

	public void deleteDiario(BaseDTO b);
	
	public void saveRelazione(BaseDTO dto) throws Exception;
	
	public void updateRelazione(BaseDTO dto) throws Exception;

	public List<CsDDiarioDoc> findDiarioDocById(BaseDTO b);

	public void saveDiarioDoc(BaseDTO b);

	public void deleteDiarioDoc(BaseDTO b);

	public List<CsDValutazione> getSchedeMultidimAnzs(BaseDTO dto);

	public CsDClob createClob(BaseDTO clobDTO);

	public void updateClob(BaseDTO clobDTO);

	public void updateSchedaMultidimAnz(BaseDTO schedaMultidimAnzDto);

	public CsDPai salvaSchedaPai(PaiDTO dto) throws Exception;

	public void eliminaSchedaPai(PaiDTO dto);

	public void saveSchedaBarthel(SchedaBarthelDTO schedaBarthelDTO) throws Exception;

	public CsDValutazione findBarthelByMutlidimId(SchedaBarthelDTO barthelDto);

	public List<CsDDocIndividuale> findDocIndividualiByCaso(BaseDTO dto);

	public void updateDocIndividuale(BaseDTO dto) throws Exception;

	public CsDDiario saveDocIndividuale(BaseDTO dto) throws Exception;

	public void deleteDocIndividuale(BaseDTO dto) throws Exception;

	public List<CsRelSettCatsocEsclusiva> findRelSettCatsocEsclusivaByTipoDiarioId(
			RelSettCatsocEsclusivaDTO dto);

	public CsRelSettCatsocEsclusiva findRelSettCatsocEsclusivaByIds(
			RelSettCatsocEsclusivaDTO dto);

	public void saveCsRelSettCatsocEsclusiva(BaseDTO dto);

	public void updateCsRelSettCatsocEsclusiva(BaseDTO dto);

	public void deleteCsRelSettCatsocEsclusiva(RelSettCatsocEsclusivaDTO dto);

	public List<CsDScuola> findScuoleByCaso(BaseDTO dto);

	public void updateScuola(BaseDTO dto) throws Exception;

	public CsDDiario saveScuola(BaseDTO dto) throws Exception;

	public void deleteScuola(BaseDTO dto) throws Exception;

	public List<CsDIsee> findIseeByCaso(BaseDTO dto);

	public void updateIsee(BaseDTO dto) throws Exception;

	public CsDDiario saveIsee(BaseDTO dto) throws Exception;

	public void deleteIsee(BaseDTO dto) throws Exception;
	
}
