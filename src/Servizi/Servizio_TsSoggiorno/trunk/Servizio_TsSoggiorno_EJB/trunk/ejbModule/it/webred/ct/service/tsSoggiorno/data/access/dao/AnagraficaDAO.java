package it.webred.ct.service.tsSoggiorno.data.access.dao;

import java.math.BigDecimal;
import java.util.List;

import it.webred.ct.service.tsSoggiorno.data.access.TsSoggiornoServiceException;
import it.webred.ct.service.tsSoggiorno.data.access.dto.DichiarazioneSearchCriteria;
import it.webred.ct.service.tsSoggiorno.data.access.dto.SocietaDTO;
import it.webred.ct.service.tsSoggiorno.data.model.IsClasse;
import it.webred.ct.service.tsSoggiorno.data.model.IsClassiStruttura;
import it.webred.ct.service.tsSoggiorno.data.model.IsSocieta;
import it.webred.ct.service.tsSoggiorno.data.model.IsSocietaSogg;
import it.webred.ct.service.tsSoggiorno.data.model.IsSoggetto;
import it.webred.ct.service.tsSoggiorno.data.model.IsStruttura;
import it.webred.ct.service.tsSoggiorno.data.model.Sitidstr;

public interface AnagraficaDAO {

	public IsSoggetto getSoggettoByCodFis(String codFiscale)
			throws TsSoggiornoServiceException;

	public void updateSoggetto(IsSoggetto sogg)
			throws TsSoggiornoServiceException;

	public List<IsSocieta> getSocieta() throws TsSoggiornoServiceException;

	public List<IsSocieta> getSocietaByCodFis(String codFiscale)
			throws TsSoggiornoServiceException;

	public IsSocieta getSocietaById(Long id) throws TsSoggiornoServiceException;

	public void updateSocieta(IsSocieta soc) throws TsSoggiornoServiceException;

	public IsSocieta saveSocieta(IsSocieta soc)
			throws TsSoggiornoServiceException;

	public IsSocietaSogg getSocietaSoggById(Long id)
			throws TsSoggiornoServiceException;
	
	public List<IsSocieta> getSocietaByCfPi(String codFiscale)
			throws TsSoggiornoServiceException;

	public void updateSocietaSogg(IsSocietaSogg soc)
			throws TsSoggiornoServiceException;

	public void saveSocietaSogg(IsSocietaSogg soc)
			throws TsSoggiornoServiceException;

	public void deleteSocietaById(Long id) throws TsSoggiornoServiceException;

	public List<IsStruttura> getStrutture() throws TsSoggiornoServiceException;
	
	public List<IsStruttura> getStrutturaByCodFis(String codFiscale)
			throws TsSoggiornoServiceException;

	public List<IsStruttura> getStrutturaBySoc(BigDecimal id)
			throws TsSoggiornoServiceException;

	public IsStruttura getStrutturaById(Long id)
			throws TsSoggiornoServiceException;

	public List<IsStruttura> getStrutturaByCriteria(
			DichiarazioneSearchCriteria criteria);

	public void updateStruttura(IsStruttura str)
			throws TsSoggiornoServiceException;

	public IsStruttura saveStruttura(IsStruttura str)
			throws TsSoggiornoServiceException;

	public void deleteStrutturaById(Long id) throws TsSoggiornoServiceException;

	public List<IsClassiStruttura> getClassiByStruttura(Long id)
			throws TsSoggiornoServiceException;

	public void updateClasseStruttura(IsClassiStruttura str)
			throws TsSoggiornoServiceException;

	public void saveClasseStruttura(IsClassiStruttura str)
			throws TsSoggiornoServiceException;

	public void deleteClasseStrutturaByStr(Long str)
			throws TsSoggiornoServiceException;

	public void deleteClasseStrutturaByStrCls(Long str, String cls)
			throws TsSoggiornoServiceException;

	public List<IsClasse> getClassi() throws TsSoggiornoServiceException;

	public List<Sitidstr> getSitidstrByLikeIndirizzo(String indirizzo,
			boolean suggestion, Integer maxNumber)
			throws TsSoggiornoServiceException;

	public Sitidstr getSitidstrByIndirizzo(String indirizzo)
			throws TsSoggiornoServiceException;

	public List<IsSocieta> getSuggestionSocieta(String descrizione);
}
