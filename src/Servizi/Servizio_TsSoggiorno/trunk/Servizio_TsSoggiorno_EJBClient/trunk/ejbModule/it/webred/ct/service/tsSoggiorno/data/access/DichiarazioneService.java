package it.webred.ct.service.tsSoggiorno.data.access;

import it.webred.ct.service.tsSoggiorno.data.access.dto.DataInDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.DichiarazioneDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.ModuloDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.RifiutoDTO;
import it.webred.ct.service.tsSoggiorno.data.model.IsConfig;
import it.webred.ct.service.tsSoggiorno.data.model.IsDichiarazione;
import it.webred.ct.service.tsSoggiorno.data.model.IsImpostaDovuta;
import it.webred.ct.service.tsSoggiorno.data.model.IsImpostaIncassata;
import it.webred.ct.service.tsSoggiorno.data.model.IsPeriodo;
import it.webred.ct.service.tsSoggiorno.data.model.IsRimborso;
import it.webred.ct.service.tsSoggiorno.data.model.IsSanzione;
import it.webred.ct.service.tsSoggiorno.data.model.IsStrutturaSnap;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Remote;

@Remote
public interface DichiarazioneService {

	public List<IsPeriodo> getPeriodi(DataInDTO dataIn);

	public List<IsPeriodo> getPeriodiNuovaDich(DataInDTO dataIn);

	public List<DichiarazioneDTO> getDichiarazioniByCodFisc(DataInDTO dataIn);

	public List<DichiarazioneDTO> getDichiarazioniByCriteria(DataInDTO dataIn);

	public List<DichiarazioneDTO> getDichiarazioniByPeriodoDalAl(
			DataInDTO dataIn);

	public List<IsDichiarazione> getDichiarazioniByPeriodoDalClasse(
			DataInDTO dataIn);
	
	public Long getDichiarazioniCountByCriteria(DataInDTO dataIn);

	public DichiarazioneDTO getDichiarazioneById(DataInDTO dataIn);

	public IsDichiarazione getDichiarazioneByPeriodoStrClasse(DataInDTO dataIn);

	public IsDichiarazione getDichiarazionePrecByPeriodoStrClasse(
			DataInDTO dataIn);

	public IsDichiarazione saveDichiarazione(DataInDTO dataIn);

	public void updateDichiarazione(DataInDTO dataIn);

	public void deleteDichiarazioneById(DataInDTO dataIn);

	public IsStrutturaSnap saveStrutturaSnap(DataInDTO dataIn);

	public void updateStrutturaSnap(DataInDTO dataIn);

	public List<ModuloDTO> getNewModuliVersato(DataInDTO dataIn);

	public List<ModuloDTO> getLoadModuliVersato(DataInDTO dataIn);

	public List<ModuloDTO> getNewModuliEsenzioni(DataInDTO dataIn);

	public List<ModuloDTO> getLoadModuliEsenzioni(DataInDTO dataIn);

	public List<ModuloDTO> getNewModuliRiduzioni(DataInDTO dataIn);

	public List<ModuloDTO> getLoadModuliRiduzioni(DataInDTO dataIn);

	public List<ModuloDTO> getNewModuliPagAnticipati(DataInDTO dataIn);

	public List<ModuloDTO> getLoadModuliPagAnticipati(DataInDTO dataIn);

	public void saveModuloOspiti(DataInDTO dataIn);

	public void updateModuloOspiti(DataInDTO dataIn);
	
	public void updateRifiuto(DataInDTO dataIn);
	
	public void saveRifiuto(DataInDTO dataIn);

	public void deleteModuloOspitiByDich(DataInDTO dataIn);

	public void saveModuloEventi(DataInDTO dataIn);

	public void updateModuloEventi(DataInDTO dataIn);

	public void deleteModuloEventiByDich(DataInDTO dataIn);

	public IsImpostaDovuta getImpostaDovutaByDichiarazione(DataInDTO dataIn);

	public void saveImpostaDovuta(DataInDTO dataIn);

	public void updateImpostaDovuta(DataInDTO dataIn);

	public IsImpostaIncassata getImpostaIncassataByDichiarazione(
			DataInDTO dataIn);

	public void saveImpostaIncassata(DataInDTO dataIn);

	public void updateImpostaIncassata(DataInDTO dataIn);
	
	public List<IsConfig> getConfig(DataInDTO dataIn);
	
	public IsConfig getConfigByChiave(DataInDTO dataIn);

	public List<IsRimborso> getRimborsiByPeriodoStrClasse(DataInDTO dataIn);

	public List<IsSanzione> getSanzioniByPeriodoStrClasse(DataInDTO dataIn);
	
	public BigDecimal getTotRimborsiByPeriodoStrClasse(DataInDTO dataIn);

	public BigDecimal getTotSanzioniByPeriodoStrClasse(DataInDTO dataIn);

	public BigDecimal getTotRimborsiDichByPeriodoStrClasse(DataInDTO dataIn);

	public BigDecimal getTotSanzioniDichByPeriodoStrClasse(DataInDTO dataIn);
	
	public BigDecimal getTariffaByPeriodoClasse(DataInDTO dataIn);

	public BigDecimal getRiduzioneByCodModulo(DataInDTO dataIn);
	
	public List<RifiutoDTO> getListaRifiutiByIdDich(DataInDTO dataIn);

	public void updateListaRifiuti(DataInDTO dataIn);

	public IsPeriodo getPeriodoById(DataInDTO dataIn);

	
}
