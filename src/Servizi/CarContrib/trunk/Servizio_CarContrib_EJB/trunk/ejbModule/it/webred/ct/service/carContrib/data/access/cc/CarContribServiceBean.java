package it.webred.ct.service.carContrib.data.access.cc;
import it.webred.ct.service.carContrib.data.access.cc.dao.CarContribDAO;
import it.webred.ct.service.carContrib.data.access.cc.dto.CodiciTipoMezzoRispostaDTO;
import it.webred.ct.service.carContrib.data.access.cc.dto.FonteNoteDTO;
import it.webred.ct.service.carContrib.data.access.cc.dto.GesRicDTO;
import it.webred.ct.service.carContrib.data.access.cc.dto.RichiesteDTO;
import it.webred.ct.service.carContrib.data.access.cc.dto.RisposteDTO;
import it.webred.ct.service.carContrib.data.access.cc.dto.SoggettiCarContribDTO;
import it.webred.ct.service.carContrib.data.access.cc.dto.TipDocDTO;
import it.webred.ct.service.carContrib.data.access.common.CarContribServiceBaseBean;
import it.webred.ct.service.carContrib.data.model.CfgFonteNote;
import it.webred.ct.service.carContrib.data.model.CodiciTipoMezzoRisposta;
import it.webred.ct.service.carContrib.data.model.DecodTipDoc;
import it.webred.ct.service.carContrib.data.model.GesRic;
import it.webred.ct.service.carContrib.data.model.GesRicPK;
import it.webred.ct.service.carContrib.data.model.Richieste;
import it.webred.ct.service.carContrib.data.model.Risposte;
import it.webred.ct.service.carContrib.data.model.SoggettiCarContrib;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class CarContribServiceBean
 */
@Stateless
public class CarContribServiceBean extends CarContribServiceBaseBean implements
		CarContribService {
	/**
	 * 
	 */
	
	@Autowired
	private CarContribDAO carContribDAO;
	
	public DecodTipDoc getTipDoc(TipDocDTO td) {
		return carContribDAO.getTipDoc(td);
	}
	/*
	 * INPUT
	 * --> SoggettiCarContribDTO soggRicDTO: soggetto richiedente: obbligatorio
	 * --> SoggettiCarContribDTO soggCarDTO: soggetto di cui fare la cartella: può essere null, nel qual caso è il richiedente.
	 * --> RichiesteDTO richDTO: dati della richiesta
	 */
	/*@ 
	*/
	
			
		public Long insertRichiesta(SoggettiCarContribDTO soggRicDTO,SoggettiCarContribDTO soggCarDTO, RichiesteDTO richDTO) {
			String enteId= richDTO.getEnteId();
			String userId= richDTO.getUserId();
			Long idRich=null;
			Long idSoggR=null;
			Long idSoggC=null;
			try {
				//soggetto richiedente
				if (soggRicDTO !=null && soggRicDTO.getSogg() != null){
					SoggettiCarContrib soggR=null;
					if (soggRicDTO.getSogg().getCodTipSogg().equals("F") )
						soggR = carContribDAO.searchSoggettoPF(soggRicDTO);
					else
						soggR = carContribDAO.searchSoggettoPG(soggRicDTO);
					if (soggR != null)
						idSoggR=soggR.getIdSogg();
					else {
						carContribDAO.insertSoggetto(soggRicDTO);
						idSoggR=soggRicDTO.getSogg().getIdSogg();
					}
				}
				//soggetto di cui fare la cartella 
				if (soggCarDTO !=null && soggCarDTO.getSogg() != null){
					SoggettiCarContrib soggC=null;
					if (soggCarDTO.getSogg().getCodTipSogg().equals("F"))
						soggC = carContribDAO.searchSoggettoPF(soggCarDTO);
					else
						soggC = carContribDAO.searchSoggettoPG(soggCarDTO);
					if (soggC!=null)
						idSoggC = soggC.getIdSogg();
					else {
						carContribDAO.insertSoggetto(soggCarDTO);
						idSoggC= soggCarDTO.getSogg().getIdSogg();
					}
				}else {
					if (soggRicDTO !=null && soggRicDTO.getSogg() != null)
						idSoggC= new Long(idSoggR) ;
				}
				richDTO.getRich().setIdSoggRic(idSoggR);
				richDTO.getRich().setIdSoggCar(idSoggC);
				carContribDAO.insertRichiesta(richDTO);
				idRich = richDTO.getRich().getIdRic();
				//preparazione oggetto per inserimento tabella gestione richieste
				GesRicDTO gesRicDto = new GesRicDTO();
				gesRicDto.setEnteId(enteId);
				gesRicDto.setUserId(userId);
				GesRicPK gesRicPK = new GesRicPK();
				gesRicPK.setIdRic(idRich) ;
				gesRicPK.setUserName(userId);
				gesRicPK.setDtIniGes(new Date());
				GesRic gesRic = new GesRic();
				gesRic.setDtFinGes(null);
				gesRic.setId(gesRicPK);
				gesRicDto.setGesRic(gesRic);
				carContribDAO.insertGesRichiesta(gesRicDto);
			}
			catch (CarContribException e) {
				
			}catch (Exception e) {
				logger.error("SI E' VERIFICATO UN ERRORE", e);
				e.printStackTrace();
			}
			
		//restituisce l'id della Richiesta inserita 
		logger.debug("FINE INSERIMENTO RICHIESTA. ID RICH: "+ idRich)	;
		return idRich;
	}

		public Long insertOnlyRichiesta(RichiesteDTO richDTO,SoggettiCarContribDTO soggettoCartella)
		{
			String enteId= richDTO.getEnteId();
			String userId= richDTO.getUserId();
			
			Long idSoggC=null;
			Long idRich=null;
			try {
				
				// Soggetto di cui fare la cartella 
				SoggettiCarContrib soggC = carContribDAO.searchSoggettoPF(soggettoCartella);
				if (soggC!=null)
					idSoggC = soggC.getIdSogg();
				else {
					carContribDAO.insertSoggetto(soggettoCartella);
					idSoggC= soggettoCartella.getSogg().getIdSogg();
				}
				richDTO.getRich().setIdSoggCar(idSoggC);
				
				carContribDAO.insertRichiesta(richDTO);
				idRich = richDTO.getRich().getIdRic();
				
				//preparazione oggetto per inserimento tabella gestione richieste
				GesRicDTO gesRicDto = new GesRicDTO();
				gesRicDto.setEnteId(enteId);
				gesRicDto.setUserId(userId);
				GesRicPK gesRicPK = new GesRicPK();
				gesRicPK.setIdRic(idRich) ;
				gesRicPK.setUserName(userId);
				gesRicPK.setDtIniGes(new Date());
				GesRic gesRic = new GesRic();
				gesRic.setDtFinGes(null);
				gesRic.setId(gesRicPK);
				gesRicDto.setGesRic(gesRic);
				carContribDAO.insertGesRichiesta(gesRicDto);
			}
			catch (CarContribException e) {
				
			}catch (Exception e) {
				logger.error("SI E' VERIFICATO UN ERRORE", e);
				e.printStackTrace();
			}
			//restituisce l'id della Richiesta inserita 
			return idRich;
		}

		public List<DecodTipDoc> getListaTipDoc(CeTBaseObject obj) {
			return carContribDAO.getListaTipDoc(obj);
		}
		
		public List<CodiciTipoMezzoRisposta> getListaCodiciRisp(CeTBaseObject obj)
		{
			return carContribDAO.getListaCodiciRisp(obj);
		}

		public void updateFilePdfRichiesta(RichiesteDTO richDTO)		throws CarContribException {
			
			try
			{
				carContribDAO.updateFilePdfRichiesta(richDTO);
			}
			catch (CarContribException e) {
				
			}catch (Exception e) {
				logger.error("SI E' VERIFICATO UN ERRORE", e);
				e.printStackTrace();
			}
		}
		
		public CodiciTipoMezzoRisposta getDescCodiciRisp(CodiciTipoMezzoRispostaDTO codiceDTO)
		{
			try
			{
				return carContribDAO.getDescCodiciRisp(codiceDTO);
			}
			catch (CarContribException e) {
				return null;
			}catch (Exception e) {
				logger.error("SI E' VERIFICATO UN ERRORE", e);
				e.printStackTrace();
				return null;
			}
		}

		public void insertRisposta(RisposteDTO rispDTO)
		{
			try
			{
				carContribDAO.insertRisposta(rispDTO);
			}
			catch (CarContribException e) {
				
			}catch (Exception e) {
				logger.error("SI E' VERIFICATO UN ERRORE", e);
				e.printStackTrace();
			}
		}
		
		public Richieste getRichiesta(RichiesteDTO richDTO)
		{
			try
			{
				return carContribDAO.getRichiesta(richDTO);
			}
			catch (CarContribException e) {
				return null;
			}catch (Exception e) {
				logger.error("SI E' VERIFICATO UN ERRORE", e);
				e.printStackTrace();
				return null;
			}
		}

		public Risposte getRisposta(RichiesteDTO richDTO)
		{
			try
			{
				return carContribDAO.getRisposta(richDTO);
			}
			catch (CarContribException e) {
				return null;
			}catch (Exception e) {
				logger.error("SI E' VERIFICATO UN ERRORE", e);
				e.printStackTrace();
				return null;
			}
		}
		
		public  SoggettiCarContrib getSoggetto(SoggettiCarContribDTO soggCarDTO)
		{
			try
			{
				return carContribDAO.getSoggetto(soggCarDTO);
			}
			catch (CarContribException e) {
				return null;
			}catch (Exception e) {
				logger.error("SI E' VERIFICATO UN ERRORE", e);
				e.printStackTrace();
				return null;
			}
		}


		public List<String> getDistinctUserName(CeTBaseObject obj) {
			return carContribDAO.getDistinctUserName(obj);
		}


		public CfgFonteNote getFonteNote(FonteNoteDTO obj) throws CarContribException {
			return carContribDAO.getFonteNote(obj);
		}
}
