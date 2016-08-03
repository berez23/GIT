package it.webred.ct.service.tsSoggiorno.data.access;

import java.util.List;

import it.webred.ct.service.tsSoggiorno.data.access.dto.DataInDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.SocietaDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.StrutturaDTO;
import it.webred.ct.service.tsSoggiorno.data.model.IsClasse;
import it.webred.ct.service.tsSoggiorno.data.model.IsClassiStruttura;
import it.webred.ct.service.tsSoggiorno.data.model.IsSocieta;
import it.webred.ct.service.tsSoggiorno.data.model.IsSocietaSogg;
import it.webred.ct.service.tsSoggiorno.data.model.IsSoggetto;
import it.webred.ct.service.tsSoggiorno.data.model.IsStruttura;
import it.webred.ct.service.tsSoggiorno.data.model.Sitidstr;

import javax.ejb.Remote;

@Remote
public interface AnagraficaService {

	public IsSoggetto getSoggettoByCodFis(DataInDTO dataIn);

	public void updateSoggetto(DataInDTO dataIn);

	public List<SocietaDTO> getSocieta(DataInDTO dataIn);
	
	public List<SocietaDTO> getSuggestionSocieta(DataInDTO dataIn);
	
	public List<SocietaDTO> getSocietaByCodFis(DataInDTO dataIn);

	public List<SocietaDTO> getSocietaByCfPi(DataInDTO dataIn);
	
	public IsSocieta getSocietaById(DataInDTO dataIn);

	public void updateSocieta(DataInDTO dataIn);

	public IsSocieta saveSocieta(DataInDTO dataIn);

	public IsSocietaSogg getSocietaSoggById(DataInDTO dataIn);

	public void updateSocietaSogg(DataInDTO dataIn);
	
	public void saveSocietaSogg(DataInDTO dataIn);

	public void deleteSocietaById(DataInDTO dataIn);

	public List<StrutturaDTO> getStrutture(DataInDTO dataIn);
	
	public List<StrutturaDTO> getStrutturaByCodFis(DataInDTO dataIn);

	public StrutturaDTO getStrutturaById(DataInDTO dataIn);

	public List<StrutturaDTO> getStrutturaByCriteria(DataInDTO dataIn);
	
	public void updateStruttura(DataInDTO dataIn);

	public IsStruttura saveStruttura(DataInDTO dataIn);

	public void deleteStrutturaById(DataInDTO dataIn);

	public List<IsClassiStruttura> getClassiByStruttura(DataInDTO dataIn);

	public void updateClasseStruttura(DataInDTO dataIn);
	
	public void saveClasseStruttura(DataInDTO dataIn);

	public void deleteClasseStrutturaByStr(DataInDTO dataIn);
	
	public void deleteClasseStrutturaByStrCls(DataInDTO dataIn);

	public List<IsClasse> getClassi(DataInDTO dataIn);

	public List<Sitidstr> getSitidstrByLikeIndirizzo(DataInDTO dataIn);
	
	public Sitidstr getSitidstrByIndirizzo(DataInDTO dataIn);

}
