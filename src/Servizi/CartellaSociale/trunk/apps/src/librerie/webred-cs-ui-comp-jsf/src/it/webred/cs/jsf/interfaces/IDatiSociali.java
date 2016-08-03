package it.webred.cs.jsf.interfaces;

import it.webred.cs.data.model.CsTbTipologiaFamiliare;

import java.math.BigDecimal;
import java.util.List;

import javax.faces.model.SelectItem;

public interface IDatiSociali {
	
	public List<SelectItem> getLstTipologiaFam();
	
	public List<SelectItem> getLstInviante();
	
	public List<SelectItem> getLstProblematiche();
	
	public List<SelectItem> getLstStesuraRelPer();
	
	public List<SelectItem> getLstTitoliStudio();
	
	public List<SelectItem> getLstProfessioni();
	
	public List<SelectItem> getLstTutele();
	
	public List<SelectItem> getLstTutelanti();
	
	public List<CsTbTipologiaFamiliare> getLstCsTbTipologiaFam();
	
	public BigDecimal getIdTipologiaFam();
	
	public Integer getnMinori();
	
	public BigDecimal getIdInviante();
	
	public BigDecimal getIdInviatoA();
	
	public BigDecimal getIdProblematica();
	
	public BigDecimal getIdStesuraRelPer();
	
	public boolean isInterventiNucleo();
	
	public String getInterventiTipo();
	
	public BigDecimal getIdTitoloStudio();
	
	public BigDecimal getIdProfessione();
	
	public BigDecimal getIdTutela();
	
	public BigDecimal getIdTutelante();
	
	public boolean isInCaricoCPS();

	public List<SelectItem> getLstTipoContratto();

	public List<SelectItem> getLstSettoreImpiego();

	public List<SelectItem> getLstAutosufficienza();

	public BigDecimal getIdTipoContratto();

	public BigDecimal getIdSettoreImpiego();

	public String getAutosufficienza();
	
	public String getPatologia();
	
	public String getPatologiaAltro();

	public boolean isInCaricoSERT();

	public boolean isInCaricoNOA();

}
