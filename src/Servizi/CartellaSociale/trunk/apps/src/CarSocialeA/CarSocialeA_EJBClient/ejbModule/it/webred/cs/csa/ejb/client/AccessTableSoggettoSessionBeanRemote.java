package it.webred.cs.csa.ejb.client;

import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.csa.ejb.dto.PaginationDTO;
import it.webred.cs.data.model.CsAAnagrafica;
import it.webred.cs.data.model.CsASoggetto;
import it.webred.cs.data.model.CsASoggettoCategoriaSoc;
import it.webred.cs.data.model.CsASoggettoMedico;
import it.webred.cs.data.model.CsASoggettoStatoCivile;
import it.webred.cs.data.model.CsASoggettoStatus;
import it.webred.cs.data.model.CsCCategoriaSociale;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface AccessTableSoggettoSessionBeanRemote {
	
	public CsASoggetto getSoggettoById(BaseDTO dto);
	
	public CsASoggetto getSoggettiByCF(BaseDTO dto);
	
	public List<CsASoggetto> getSoggettiByDenominazione(BaseDTO dto);
	
	public List<CsASoggettoCategoriaSoc> getSoggettoCategorieBySoggetto(BaseDTO dto);
	
	public void saveSoggettoCategoria(BaseDTO dto);
	
	public void eliminaSoggettoCategorieBySoggetto(BaseDTO dto);
	
	public CsCCategoriaSociale getCatSocAttualeBySoggetto(BaseDTO dto);
	
	public List<CsASoggetto> getCasiSoggetto(PaginationDTO dto);
	
	public Integer getCasiSoggettoCount(PaginationDTO dto);

	public Integer getCasiPerCategoriaCount(PaginationDTO dto);
	
	public void updateSoggetto(BaseDTO dto);

	public CsASoggetto saveSoggetto(BaseDTO dto);

	public List<CsASoggettoMedico> getSoggettoMedicoBySoggetto(BaseDTO dto);

	public void saveSoggettoMedico(BaseDTO dto);
	
	public void updateSoggettoMedico(BaseDTO dto);
	
	public void eliminaSoggettoMedicoBySoggetto(BaseDTO dto);

	public List<CsASoggettoStatoCivile> getSoggettoStatoCivileBySoggetto(BaseDTO dto);

	public void saveSoggettoStatoCivile(BaseDTO dto);

	public void updateSoggettoStatoCivile(BaseDTO dto);
	
	public void eliminaSoggettoStatoCivileBySoggetto(BaseDTO dto);
	
	public List<CsASoggettoStatus> getSoggettoStatusBySoggetto(BaseDTO dto);

	public void saveSoggettoStatus(BaseDTO dto);

	public void updateSoggettoStatus(BaseDTO dto);
	
	public void eliminaSoggettoStatusBySoggetto(BaseDTO dto);

	public CsAAnagrafica saveAnagrafica(BaseDTO dto);
	
	public CsAAnagrafica updateAnagrafica(BaseDTO dto);

}
