package it.webred.ct.service.intTerritorio.web.bean;

import java.util.ArrayList;
import java.util.List;

import it.webred.ct.aggregator.ejb.dto.DatiCivicoDTO;
import it.webred.ct.aggregator.ejb.dto.RichiestaDatiCivicoDTO;
import it.webred.ct.aggregator.ejb.dto.ViaDTO;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
/**
 * Session Bean implementation class C340CommonServiceBean
 */
public class InterrogazioneTerritorioBean extends InterrogazioneTerritorioBaseBean{
	
	private RichiestaDatiCivicoDTO richiesta = new RichiestaDatiCivicoDTO();
	private DatiCivicoDTO dto;
	private List<SelectItem> vie;
	
	@PostConstruct
	public void initService() {
		vie = new ArrayList<SelectItem>();
		dto = new DatiCivicoDTO();
	}
	
	public String getDatiCivico() {
		
		richiesta.setUserId(this.getUser().getUsername());
		richiesta.setEnteId(this.getUser().getCurrentEnte());
		setDto(getDatiCivico(richiesta));
		
		if(dto.getListaVieRicerca()== null)
			return "intTerr.detail";
		else{
			richiesta = new RichiestaDatiCivicoDTO();
			vie = new ArrayList<SelectItem>();
			for(ViaDTO via : dto.getListaVieRicerca()){
				SelectItem item = new SelectItem();
				item.setValue(via.getIdVia());
				item.setDescription(via.getDescrizione());
				System.out.println(item.getValue()+" - " + item.getDescription());
				vie.add(item);
			}
			return "intTerr.via.select";
		}
	}
	
	public List<SelectItem> getVie() {
		return vie;
	}

	public void setVie(List<SelectItem> vie) {
		this.vie = vie;
	}

	public DatiCivicoDTO getDatiCivico(RichiestaDatiCivicoDTO input){
		return datiCivicoService.getDatiCivico(input);
	}
	
/*	public DatiCivicoDTO getDatiCivico(RichiestaDatiCivicoDTO input) {
			
		DatiCivicoDTO info = new DatiCivicoDTO();
		RicercaCivicoDTO rc = new RicercaCivicoDTO();
		rc.setToponimoVia(input.getToponimo());
		rc.setDescrizioneVia(input.getVia());
		rc.setCivico(input.getCivico());
		if(input.getCodEnte()!= null)
			rc.setEnteId(input.getCodEnte());
		else
			rc.setEnteId(this.getUser().getCurrentEnte());
		
		try {
		
			//Estrazioni info catasto
			DatiCivicoCatastoDTO datiCatasto = catastoService.getDatiCivicoCatasto(rc);
			
			List<ParticellaInfoDTO> listap = datiCatasto.getInfoParticelleCivico();
			info.setInfoParticelleCivico(listap.toArray(new ParticellaInfoDTO[listap.size()]));
			
			//Estrazione numero famiglie residenti proprietarie
			List<ConsSoggTab> titolariCivico = datiCatasto.getTitolariSuCivico();
			List<String> idCatSogg = new ArrayList<String>();
			for(ConsSoggTab s : titolariCivico){
				idCatSogg.add(s.getPkid().toString());
			}
			
			//Estrazione info anagrafe
			rc.setListaId(idCatSogg.toArray(new String[idCatSogg.size()]));
			DatiCivicoAnagrafeDTO datiAnagrafe = anagrafeService.getDatiCivicoAnagrafe(rc);
			
			info.setCountUnder18(datiAnagrafe.getCountUnder18());
			info.setCount18_65(datiAnagrafe.getCount18_65());
			info.setCountOver65(datiAnagrafe.getCountOver65());
			info.setCountFamResidenti(datiAnagrafe.getCountFamResidenti());
			info.setCountFamResidentiProprietarie(datiAnagrafe.getCountFamResidentiProprietarie());
		
			
			//Numero di contratti di locazione al CIVICO
			int countLocazioni = locazioniService.countLocazioniCivicoAllaData(rc);
			info.setCountLocazioni(countLocazioni);
			
			//Licenze commerciali al CIVICO
			List<SitLicenzeCommercio> listaLic =  licenzeCommercioService.getLicCommercioCivicoAllaData(rc);
			info.setListaLicenzeCommercio(listaLic.toArray(new SitLicenzeCommercio[listaLic.size()]));
			
			int countLicenzeCommercio = licenzeCommercioService.countLicCommercioCivicoAllaData(rc);
			info.setCountLicenzeCommercio(countLicenzeCommercio);
			
			
		} catch (Throwable t) {
			getLogger().error("",t);
		}

		return info;
	}*/


	public void setRichiesta(RichiestaDatiCivicoDTO richiesta) {
		this.richiesta = richiesta;
	}


	public RichiestaDatiCivicoDTO getRichiesta() {
		return richiesta;
	}

	public void setDto(DatiCivicoDTO dto) {
		this.dto = dto;
	}

	public DatiCivicoDTO getDto() {
		return dto;
	}

}
