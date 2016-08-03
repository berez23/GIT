package it.webred.ct.service.tsSoggiorno.data.access.dao;

import it.webred.ct.service.tsSoggiorno.data.access.TsSoggiornoServiceException;
import it.webred.ct.service.tsSoggiorno.data.access.dto.DichiarazioneDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.DichiarazioneSearchCriteria;
import it.webred.ct.service.tsSoggiorno.data.model.IsConfig;
import it.webred.ct.service.tsSoggiorno.data.model.IsDichiarazione;
import it.webred.ct.service.tsSoggiorno.data.model.IsImpostaDovuta;
import it.webred.ct.service.tsSoggiorno.data.model.IsImpostaIncassata;
import it.webred.ct.service.tsSoggiorno.data.model.IsModuloDati;
import it.webred.ct.service.tsSoggiorno.data.model.IsModuloEventi;
import it.webred.ct.service.tsSoggiorno.data.model.IsModuloOspiti;
import it.webred.ct.service.tsSoggiorno.data.model.IsPeriodo;
import it.webred.ct.service.tsSoggiorno.data.model.IsRiduzione;
import it.webred.ct.service.tsSoggiorno.data.model.IsRifiuto;
import it.webred.ct.service.tsSoggiorno.data.model.IsRifiutoGruppo;
import it.webred.ct.service.tsSoggiorno.data.model.IsRimborso;
import it.webred.ct.service.tsSoggiorno.data.model.IsSanzione;
import it.webred.ct.service.tsSoggiorno.data.model.IsStrutturaSnap;
import it.webred.ct.service.tsSoggiorno.data.model.IsTabModuloField;
import it.webred.ct.service.tsSoggiorno.data.model.IsTariffa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

public interface DichiarazioneDAO {

	public List<IsPeriodo> getPeriodi();

	public List<IsPeriodo> getPeriodiNuovaDich();

	public List<DichiarazioneDTO> getDichiarazioniByCodFisc(String codFiscale);

	public List<DichiarazioneDTO> getDichiarazioniByCriteria(
			DichiarazioneSearchCriteria criteria);

	public Long getDichiarazioniCountByCriteria(
			DichiarazioneSearchCriteria criteria);

	public DichiarazioneDTO getDichiarazioneById(Long id);

	/*public IsDichiarazione getDichiarazioneByPeriodoStrClasse(Long idPeriodo,
			Long idStr, String idClasse);*/
	
	public List<IsDichiarazione> getDichiarazioniByPeriodoStrClasse(Long idPeriodo,
			Long idStr, String idClasse);

	public IsDichiarazione getDichiarazionePrecByPeriodoStrClasse(
			Long idPeriodo, Long idStr, String idClasse);

	public List<DichiarazioneDTO> getDichiarazioniByPeriodoDalAl(Long dal, Long al);
	
	public List<IsDichiarazione> getDichiarazioniByPeriodoDalClasse(Long dal, String classe);
	
	public IsDichiarazione saveDichiarazione(IsDichiarazione dich);

	public void updateDichiarazione(IsDichiarazione dich);

	public void deleteDichiarazioneById(Long id);

	public IsStrutturaSnap saveStrutturaSnap(IsStrutturaSnap ss);

	public void updateStrutturaSnap(IsStrutturaSnap ss);

	public List<IsModuloDati> getVersatoByPeriodo(Long periodo);

	public List<IsModuloDati> getEsenzioniByPeriodo(Long periodo);

	public List<IsModuloDati> getRiduzioniByPeriodo(Long periodo);

	public List<IsModuloDati> getPagAnticipatiByPeriodo(Long periodo);

	public List<IsTabModuloField> getFieldByModulo(Long modulo);

	public IsModuloOspiti getOspitiByDichiarazioneModulo(Long dichiarazione,
			String modulo);

	public void saveModuloOspiti(IsModuloOspiti mod);

	public void updateModuloOspiti(IsModuloOspiti mod);

	public void deleteModuloOspitiByDich(Long id);

	public List<IsModuloEventi> getEventiByDichiarazioneModulo(
			Long dichiarazione, String modulo);

	public void saveModuloEventi(IsModuloEventi mod);

	public void updateModuloEventi(IsModuloEventi mod);

	public void deleteModuloEventiByDich(Long id);

	public IsImpostaDovuta getImpostaDovutaByDichiarazione(Long dichiarazione);

	public void saveImpostaDovuta(IsImpostaDovuta imp)
			throws TsSoggiornoServiceException;

	public void updateImpostaDovuta(IsImpostaDovuta imp)
			throws TsSoggiornoServiceException;

	public void deleteImpostaDovutaByDichiarazione(Long dichiarazione);

	public IsImpostaIncassata getImpostaIncassataByDichiarazione(
			Long dichiarazione);

	public void saveImpostaIncassata(IsImpostaIncassata imp)
			throws TsSoggiornoServiceException;

	public void updateImpostaIncassata(IsImpostaIncassata imp)
			throws TsSoggiornoServiceException;

	public void deleteImpostaIncassataByDichiarazione(Long dichiarazione);

	public List<IsConfig> getConfig();
	
	public IsConfig getConfigByChiave(String chiave);

	public List<IsSanzione> getSanzioniByPeriodoStrClasse(Long idPeriodo,
			Long idStr, String idClasse);

	public List<IsRimborso> getRimborsiByPeriodoStrClasse(Long idPeriodo,
			Long idStr, String idClasse);

	public IsTariffa getLastTariffaByPeriodoClasse(Long idPeriodo, String idClasse);
	
	public IsRiduzione getRiduzioneByCodModulo(String fkIdTabModulo);
	
	public List<IsRifiuto> getListaRifiutiByIdDich(Long idDichiarazione);

	public List<IsRifiutoGruppo> getListaGruppoByIdRifiuto(Long idRifiuto);

	public void updateIsRifiuto(IsRifiuto rifiuto);

	public void saveIsRifiutoGruppo(IsRifiutoGruppo rg);

	public IsRifiuto saveIsRifiuto(IsRifiuto rifiuto);
	
	public void updateIsRifiutoGruppo(IsRifiutoGruppo rg);

	public void deleteGruppoByRifiuto(Long idRifiuto);
	
	public void deleteIsRifiutoGruppo(Long id) ;

	public void deleteIsRifiuto(Long idOld);

	public IsPeriodo getPeriodoById(Long id);
}
