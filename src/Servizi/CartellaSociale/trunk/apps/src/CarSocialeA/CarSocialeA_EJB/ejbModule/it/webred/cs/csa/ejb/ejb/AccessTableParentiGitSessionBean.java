package it.webred.cs.csa.ejb.ejb;

import it.webred.cs.csa.ejb.CarSocialeBaseSessionBean;
import it.webred.cs.csa.ejb.client.AccessTableParentiGitSessionBeanRemote;
import it.webred.cs.csa.ejb.dao.ParentiDAO;
import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.data.DataModelCostanti;
import it.webred.cs.data.model.CsAComponenteGit;
import it.webred.cs.data.model.CsAFamigliaGruppoGit;
import it.webred.cs.data.model.CsASoggetto;
import it.webred.cs.data.model.CsTbTipoRapportoCon;
import it.webred.ct.data.access.basic.anagrafe.AnagrafeService;
import it.webred.ct.data.access.basic.anagrafe.dto.ComponenteFamigliaDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.RicercaSoggettoAnagrafeDTO;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class AccessTableDataMediciSessionBean
 */
@Stateless
public class AccessTableParentiGitSessionBean extends CarSocialeBaseSessionBean implements AccessTableParentiGitSessionBeanRemote {

	@Autowired
	private ParentiDAO parentiDAO;
	
	@EJB(mappedName = "java:global/CT_Service/CT_Service_Data_Access/AnagrafeServiceBean")
	protected AnagrafeService anagrafeService;
	
	/**
     * Default constructor. 
     */
    public AccessTableParentiGitSessionBean() {
        // TODO Auto-generated constructor stub
    }
    
    public CsAFamigliaGruppoGit getFamigliaGruppoGit(BaseDTO dto) {
    	return parentiDAO.getFamigliaGruppoGit((Long) dto.getObj());
    }
    
    public List<CsAFamigliaGruppoGit> getFamigliaGruppoGitAggiornate(BaseDTO dto) {
    	return parentiDAO.getFamiglieGruppoGitAggiornate();
    }
    
    public void createFamigliaGruppoGit(BaseDTO dto) {
    	
    	CsASoggetto csASoggetto = (CsASoggetto) dto.getObj();
    	CsAFamigliaGruppoGit famigliaGit = new CsAFamigliaGruppoGit();
    	famigliaGit.setCsASoggetto(csASoggetto);
    	famigliaGit.setDataFineApp(DataModelCostanti.END_DATE);
    	famigliaGit.setDataInizioApp(new Date());
    	famigliaGit.setDataInizioSys(new Date());
    	famigliaGit.setUserIns(dto.getUserId());
    	famigliaGit.setDtIns(new Date());
    	    	
    	famigliaGit.setCsAComponenteGits(null);
    	famigliaGit = parentiDAO.saveFamigliaGruppoGit(famigliaGit);
    	
    	RicercaSoggettoAnagrafeDTO rsaDto = new RicercaSoggettoAnagrafeDTO();
		rsaDto.setEnteId(dto.getEnteId());
		rsaDto.setCodFis(csASoggetto.getCsAAnagrafica().getCf());
		List<ComponenteFamigliaDTO> lista = anagrafeService.getListaCompFamigliaInfoAggiuntiveByCf(rsaDto);
    	
    	for(ComponenteFamigliaDTO componenteDto: lista) {
    		
    		if(componenteDto.getPersona().getCodfisc() != null && componenteDto.getPersona().getCodfisc().equals(csASoggetto.getCsAAnagrafica().getCf()))
    			continue;
    		
    		CsAComponenteGit componenteGit = new CsAComponenteGit();
    		componenteGit.setCsAFamigliaGruppoGit(famigliaGit);
    		componenteGit.setUserIns(dto.getUserId());
    		componenteGit.setDtIns(new Date());
    		
    		componenteGit.setCognome(componenteDto.getPersona().getCognome());
    		componenteGit.setNome(componenteDto.getPersona().getNome());
    		componenteGit.setSesso(componenteDto.getPersona().getSesso());
    		componenteGit.setCf(componenteDto.getPersona().getCodfisc());
    		componenteGit.setDataNascita(componenteDto.getPersona().getDataNascita());
    		componenteGit.setDataDecesso(componenteDto.getPersona().getDataMor());
    		
    		componenteGit.setComuneNascitaCod(componenteDto.getCodComNas());
    		componenteGit.setComuneNascitaDes(componenteDto.getDesComNas());
    		componenteGit.setProvNascitaCod(componenteDto.getSiglaProvNas());
    		componenteGit.setStatoNascitaCod(componenteDto.getCodStatoNas());
    		componenteGit.setStatoNascitaDes(componenteDto.getDesStatoNas());
    		
    		componenteGit.setIndirizzoRes(componenteDto.getIndirizzoResidenza());
    		componenteGit.setNumCivRes(componenteDto.getCivicoResidenza());
    		componenteGit.setComResCod(componenteDto.getCodComRes());
    		componenteGit.setComResDes(componenteDto.getDesComRes());
    		componenteGit.setProvResCod(componenteDto.getSiglaProvRes());
    		
    		if(componenteDto.getRelazPar() != null) {
    			Integer idTipoRapporto = parentelaGitCsMap.get(componenteDto.getRelazPar());
    			if(idTipoRapporto != null) {
    				CsTbTipoRapportoCon tipoRapporto = new CsTbTipoRapportoCon();
    				tipoRapporto.setId(new Long(idTipoRapporto));
    				componenteGit.setCsTbTipoRapportoCon(tipoRapporto);
    			}
    		}
    		
    		parentiDAO.saveComponenteGit(componenteGit);
    		
    	}

    }
    
	public void updateComponenteGit(BaseDTO dto) {
		parentiDAO.updateComponenteGit((CsAComponenteGit) dto.getObj());
	}
	
	public void updateFamigliaGruppoGit(BaseDTO dto) {
		parentiDAO.updateFamigliaGruppoGit((CsAFamigliaGruppoGit) dto.getObj());
	}
	
	private static final Map<String, Integer> parentelaGitCsMap = new HashMap<String, Integer>();
    static {
    	parentelaGitCsMap.put("CGN", 3);
    	parentelaGitCsMap.put("CUG", 6);
    	parentelaGitCsMap.put("AFFILIATA", 6);
    	parentelaGitCsMap.put("AFFILIATO", 6);
    	parentelaGitCsMap.put("CV", 7);
    	parentelaGitCsMap.put("MC", 7);
    	parentelaGitCsMap.put("FG", 9);
    	parentelaGitCsMap.put("FIGLIASTRO", 11);
    	parentelaGitCsMap.put("FIGLIASTRA", 11);
    	parentelaGitCsMap.put("FR", 13);
    	parentelaGitCsMap.put("FRATELLASTRO", 14);
    	parentelaGitCsMap.put("GE", 15);
    	parentelaGitCsMap.put("MD", 16);
    	parentelaGitCsMap.put("MA", 19);
    	parentelaGitCsMap.put("MATRIGNA", 20);
    	parentelaGitCsMap.put("PATRIGNO", 20);
    	parentelaGitCsMap.put("MG", 21);
    	parentelaGitCsMap.put("NP", 22);
    	parentelaGitCsMap.put("PRONIPOTE", 22);
    	parentelaGitCsMap.put("NA", 23);
    	parentelaGitCsMap.put("NO", 24);
    	parentelaGitCsMap.put("BISNONNA", 23);
    	parentelaGitCsMap.put("BISNONNO", 24);
    	parentelaGitCsMap.put("NU", 25);
    	parentelaGitCsMap.put("PD", 26);
    	parentelaGitCsMap.put("SO", 29);
    	parentelaGitCsMap.put("SORELLASTRA", 30);
    	parentelaGitCsMap.put("SC", 31);
    	parentelaGitCsMap.put("ZA", 34);
    	parentelaGitCsMap.put("ZI", 34);
    }

}
