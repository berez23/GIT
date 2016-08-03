package it.webred.ct.data.access.basic.anagrafe;
 
import it.webred.ct.data.access.basic.CTServiceBaseBean;
import it.webred.ct.data.access.basic.anagrafe.dao.AnagrafeDAO;
import it.webred.ct.data.access.basic.anagrafe.dto.AnagraficaDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.ComponenteFamigliaDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.DatiPersonaDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.IndirizzoAnagrafeDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.RicercaIndirizzoAnagrafeDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.RicercaLuogoDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.RicercaSoggettoAnagrafeDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.SoggettoAnagrafeDTO;
import it.webred.ct.data.model.anagrafe.SitComune;
import it.webred.ct.data.model.anagrafe.SitDCivicoV;
import it.webred.ct.data.model.anagrafe.SitDPersFam;
import it.webred.ct.data.model.anagrafe.SitDPersona;
import it.webred.ct.data.model.anagrafe.SitDPersonaCivico;
import it.webred.ct.data.model.anagrafe.SitDVia;
import it.webred.ct.data.model.anagrafe.SitProvincia;
import it.webred.ct.data.access.basic.common.dto.RicercaSoggettoDTO;
import it.webred.ct.data.access.basic.common.utils.StringUtils; 

import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class AnagrafeServiceBean
 */ 
@Stateless 
public class AnagrafeServiceBean extends CTServiceBaseBean implements AnagrafeService {
	@Autowired
	private AnagrafeDAO anagrafeDAO;
	private static final long serialVersionUID = 1L;
	@Override
	public List<SitDPersona> getListaPersoneByCF(RicercaSoggettoAnagrafeDTO rs) {
		List<SitDPersona> lista =null;
		if (rs.getDtRif()== null) 
			lista = anagrafeDAO.getListaPersoneByCF(rs);
		else
			lista = anagrafeDAO.getListaPersoneByCFAllaData(rs);
		return lista;
	}
	
	@Override
	public List<SitDPersona> getListaPersoneByDatiAna(RicercaSoggettoAnagrafeDTO rs) {
		List<SitDPersona> lista =null;
		if (rs.getDtRif()== null) 
			lista = anagrafeDAO.getListaPersoneByDatiAna(rs);
		else
			lista = anagrafeDAO.getListaPersoneByDatiAnaAllaData(rs);
		return lista;
	}
	
	@Override
	public String getIdPersonaByCF(RicercaSoggettoAnagrafeDTO rs) {
		String idPersona=null;	
		logger.debug("AnagrafeServiceBean - getIdPersonaByCF(). " +	"[CF: "+rs.getCodFis()+"]");
		if (rs.getCodFis()==null || rs.getCodFis().equals("")) {
			return null;
		}
		List<SitDPersona> listaPersone = anagrafeDAO.getListaPersoneByCF(rs);
		//Controllo che siano la stessa persona
		if (listaPersone!=null) {
			String idExt= listaPersone.get(0).getIdExt();
			boolean stessaPersona=true;
			for(SitDPersona pers: listaPersone) {
				stessaPersona=idExt.equals(pers.getIdExt());
				logger.debug("... in for: stessaPersona: " + stessaPersona);
				if (!stessaPersona)
					break;
			}
			if (stessaPersona) {
				idPersona=idExt;
			}
		}
		return idPersona;
	}
	
	@Override
	public List<SitDPersona> getVariazioniPersonaByIdExt(RicercaSoggettoAnagrafeDTO rs) {
		List<SitDPersona> listaPersone=null; 
		logger.debug("AnagrafeServiceBean - getVariazioniPersonaByIdExt(). " +	"[ID-SOGG-ANA: "+rs.getIdSogg()+"]");
		if (rs.getIdSogg()!=null && !rs.getIdSogg().equals("")) {
			return null;
		}
		listaPersone = anagrafeDAO.getVariazioniPersonaByIdExt(rs);
		return listaPersone;
	}
	
	@Override
	public AnagraficaDTO getPersonaFamigliaByCF(RicercaSoggettoAnagrafeDTO rs) {
		return anagrafeDAO.getPersonaFamigliaByCF(rs);
	}
	
	@Override
	public List<SitDPersona> getFamigliaByCF(RicercaSoggettoAnagrafeDTO rs) {
		return anagrafeDAO.getFamigliaByCF(rs);
	}
	
	@Override
	public IndirizzoAnagrafeDTO getIndirizzoPersona(RicercaSoggettoAnagrafeDTO rs) {
		IndirizzoAnagrafeDTO  indirizzo=null;
		logger.debug("AnagrafeServiceBean - getIndirizzoPersona(). " +	"[ID-SOGG-ANA: "+rs.getIdSogg()+"]");
		if (rs.getIdSogg()==null || rs.getIdSogg().equals("")) {
			return null;
		}
		//persone-civici
		List<SitDPersonaCivico> listaPersCiv = anagrafeDAO.getListaPersCivByIdExt(rs);
		if (listaPersCiv==null || listaPersCiv.size() == 0){
			return null;
		}
		//ora si cerca la riga valida alla data, se non c'è si prende quella attuale,  se non c'è si prende la più recente 
		SitDPersonaCivico persCivValidoAllaDtRif =null;
		SitDPersonaCivico persCivUltimo=null;	
		for (SitDPersonaCivico ele: listaPersCiv) {
			if (rs.getDtRif()!=null) {
				if (ele.getDtInizioVal().compareTo(rs.getDtRif())<= 0 &&
					(ele.getDtFineVal() == null || rs.getDtRif().compareTo(ele.getDtFineVal()) <= 0))	 {
					persCivValidoAllaDtRif = ele;
				}
			}	
			if (ele.getDtFineVal() == null)
				persCivUltimo=ele;
		}
		SitDPersonaCivico persCiv =persCivValidoAllaDtRif != null?persCivValidoAllaDtRif:persCivUltimo;
		if (persCiv == null){
			persCiv=listaPersCiv.get(0);
		}
		//civico
		String idExtDCivico=persCiv.getIdExtDCivico();	
		RicercaIndirizzoAnagrafeDTO ri = new RicercaIndirizzoAnagrafeDTO(idExtDCivico);
		List<SitDCivicoV> listaCiv=anagrafeDAO.getListaCiviciByIdExt(ri);
		if (listaCiv==null || listaCiv.size() == 0){
			return null;
		}
		//ora si cerca la riga valida alla data, come sopra 
		SitDCivicoV civValidoAllaDtRif =null;
		SitDCivicoV civUltimo=null;
		for (SitDCivicoV ele: listaCiv) {
			if (rs.getDtRif()!=null) {
				if (ele.getDtInizioVal().compareTo(rs.getDtRif())<= 0 &&
			       (ele.getDtFineVal() == null || 	rs.getDtRif().compareTo(ele.getDtFineVal()) <= 0))	 {
					civValidoAllaDtRif = ele;
				}
			}	
			if (ele.getDtFineVal() == null)
				civUltimo=ele;
		}
		SitDCivicoV civ =civValidoAllaDtRif != null?civValidoAllaDtRif:civUltimo;
		if (civ == null ){
			civ=listaCiv.get(0);
		}
		//via
		ri.setSitDViaIdExt(civ.getIdExtDVia());
		List<SitDVia> listaVie = anagrafeDAO.getListaVieByIdExt(ri);
		if (listaVie==null || listaVie.size() ==0){
			return null;
		}
		logger.debug("LISTA VIE - n.ELE: " + listaVie.size() );
		//ora si cerca la riga valida alla data, come sopra 
		SitDVia viaValidoAllaDtRif =null;
		SitDVia viaUltimo=null;
		for (SitDVia ele: listaVie) {
			if (rs.getDtRif()!=null) {
				if (ele.getDtInizioVal().compareTo(rs.getDtRif())<= 0 &&
				   (ele.getDtFineVal()==null || rs.getDtRif().compareTo(ele.getDtFineVal()) <= 0))	 {
					viaValidoAllaDtRif = ele;
				}
			}	
			if (ele.getDtFineVal() == null)
				viaUltimo=ele;
		}
		SitDVia via =viaValidoAllaDtRif != null?viaValidoAllaDtRif:viaUltimo;
		if (via == null){
			via=listaVie.get(0);
		}
		//dati
		indirizzo= new IndirizzoAnagrafeDTO();
		indirizzo.valDatiIndirizzo(via, civ);
		indirizzo.setDtIniVal(persCiv.getDtInizioVal());
		indirizzo.setDtFinVal(persCiv.getDtFineVal());
		logger.debug("via, civ: "+ via + ", " +civ);
		return indirizzo;
	}

	@Override
	public List<SoggettoAnagrafeDTO> searchSoggetto(RicercaSoggettoAnagrafeDTO rs) {
		List<SoggettoAnagrafeDTO> lista= anagrafeDAO.searchSoggetto(rs);
		return lista;
	}

	@Override
	public List<ComponenteFamigliaDTO> getListaCompFamiglia(RicercaSoggettoAnagrafeDTO rs) {
		SitDPersFam persFam = anagrafeDAO.getPersFamByIdExtDPersona(rs);
		if (persFam==null)
			return null;
		String idExtDFamiglia = persFam.getIdExtDFamiglia();
		rs.setIdExtDFamiglia(idExtDFamiglia);
		List<ComponenteFamigliaDTO> listaComp =  anagrafeDAO.getListaCompFamiglia(rs);
		//aggiungo i dati relativi a luogo di nascita
		if (listaComp !=null) {
			RicercaLuogoDTO rl = new RicercaLuogoDTO();
			rl.setEnteId(rs.getEnteId());
			rl.setUserId(rs.getUserId());
			if (rs.getDtRif()!=null)
				rl.setDtRif(rs.getDtRif());
			SitComune comune=null;
			SitProvincia provincia =null;
			for (ComponenteFamigliaDTO compFam: listaComp) {
				compFam.setDatiAnagComponente(compFam.getRelazPar() + " - " + compFam.getPersona().getCognome() + " " + compFam.getPersona().getNome());
				if (compFam.getPersona().getIdExtComuneNascita() !=null) {
					rl.setIdExtComune(compFam.getPersona().getIdExtComuneNascita());
					comune =anagrafeDAO.getComune(rl);
					if (comune!= null) {
						String desComune=comune.getDescrizione()==null? "" : comune.getDescrizione().trim() ;
						compFam.setDesComNas(desComune);
					}
				}
				if (compFam.getPersona().getIdExtComuneNascita() != null)  {
					rl.setIdExtProvincia(compFam.getPersona().getIdExtProvinciaNascita());
					provincia=anagrafeDAO.getProvincia(rl);
					if(provincia!=null) {
						String desProvincia=provincia.getDescrizione()==null? "" : provincia.getDescrizione().trim() ;
						compFam.setDesProvNas(desProvincia);
						String sigla = provincia.getSigla()==null? "" : provincia.getSigla().trim() ;
						compFam.setSiglaProvNas(sigla);
					}
						
				}
			}
		}
		return listaComp; 
	}

	@Override
	public SitDPersona getPersonaById(RicercaSoggettoAnagrafeDTO rs) {
		return anagrafeDAO.getPersonaById(rs);
	}

	@Override
	public DatiPersonaDTO getDatiPersonaById(RicercaSoggettoAnagrafeDTO rs) {
		SitDPersona pers = anagrafeDAO.getPersonaById(rs);
		if (pers==null)
			return null;
		DatiPersonaDTO datiPers=new DatiPersonaDTO();
		RicercaLuogoDTO rl = new RicercaLuogoDTO();
		rl.setEnteId(rs.getEnteId());
		rl.setUserId(rs.getUserId());
		if (rs.getDtRif()!=null)
			rl.setDtRif(rs.getDtRif());
		SitComune comune=null;
		SitProvincia provincia =null;
		if (pers.getIdExtComuneNascita() != null)  {
			rl.setIdExtComune(pers.getIdExtComuneNascita());
			comune =anagrafeDAO.getComune(rl);
			if (comune== null)
				comune=new SitComune();
		}
		if (pers.getIdExtComuneNascita() != null)  {
			rl.setIdExtProvincia(pers.getIdExtProvinciaNascita());
			provincia=anagrafeDAO.getProvincia(rl);
			if(provincia==null)
				provincia=new SitProvincia();
		}
		datiPers.setPersona(pers);
		String desComune=comune.getDescrizione()==null? "" : comune.getDescrizione().trim() ;
		datiPers.setDesComNas(desComune);
		String desProvincia=provincia.getDescrizione()==null? "" : provincia.getDescrizione().trim() ;
		datiPers.setDesProvNas(desProvincia);
		String sigla = provincia.getSigla()==null? "" : provincia.getSigla().trim() ;
		datiPers.setSiglaProvNas(sigla);
		rs.setIdSogg(pers.getIdExt());
		IndirizzoAnagrafeDTO indir = getIndirizzoPersona(rs);
		String indStr = "";
		if (indir !=null) {
			indStr += (indir.getSedimeVia()== null )? "": indir.getSedimeVia().trim();
			indStr += (indir.getDesVia() == null )? "": " " + indir.getDesVia().trim();
			if (indir.getDesVia() != null && ! indir.getDesVia().equals("")){
				indStr += (indir.getCivico() == null )? "": ", " + StringUtils.removeLeadingZero(indir.getCivico().trim());
				indStr += (indir.getCivicoLiv2() == null )? "": " " + StringUtils.removeLeadingZero(indir.getCivicoLiv2().trim());
				indStr += (indir.getCivicoLiv3() == null )? "": " " + StringUtils.removeLeadingZero(indir.getCivicoLiv3().trim());	
			}
			
		}
		datiPers.setIndirizzoResidenza(indStr);
		return datiPers;
	}

	@Override
	public SitComune getComune(RicercaLuogoDTO rl) {
		return anagrafeDAO.getComune(rl);
	}

	@Override
	public SitProvincia getProvincia(RicercaLuogoDTO rl) {
		return anagrafeDAO.getProvincia(rl);
	}

	@Override
	public List<ComponenteFamigliaDTO> getResidentiAlCivico(RicercaIndirizzoAnagrafeDTO ri) {
		List<ComponenteFamigliaDTO> listaComp = anagrafeDAO.getResidentiAlCivico(ri);
		for (ComponenteFamigliaDTO comp: listaComp) {
			SitDPersona datiPers = comp.getPersona();
			comp.setDatiAnagComponente(comp.getIdExtDFamiglia() + " " + comp.getRelazPar() + " - " + comp.getPersona().getCognome() + " " + comp.getPersona().getNome() + " " + comp.getPersona().getCodfisc());
		}
		return listaComp;
	}

	@Override
	public IndirizzoAnagrafeDTO getIndirizzo(RicercaIndirizzoAnagrafeDTO ri) {
		return anagrafeDAO.getIndirizzo(ri);
	}

	@Override
	public List<AnagraficaDTO> getAnagrafeByCF(RicercaSoggettoDTO rs) throws AnagrafeException{
		return anagrafeDAO.getAnagrafeByCF(rs);
	}
	
}
