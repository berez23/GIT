package it.webred.cs.jsf.interfaces;

import it.webred.cs.data.model.CsAComponente;
import it.webred.cs.data.model.CsDValutazione;

import java.util.Date;
import java.util.List;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;


public interface ISchedaMultidimAnz {
	
	public void loadAnagConv();
	public void loadAnagNonConv();
	public void loadAnagAltri();
	public void removeAnaConv();
	public void removeAnaNonConv();
	public void removeAnaAltri();
	public void addAnaCorr();
	
	public void onRowSelectFamAnaConv(SelectEvent event);
    public void onRowUnselectFamAnaConv(UnselectEvent event);
    public void onRowSelectFamAnaNonConv(SelectEvent event);
    public void onRowUnselectFamAnaNonConv(UnselectEvent event);
    public void onRowSelectFamAnaCorr(SelectEvent event);
    public void onRowUnselectFamAnaCorr(UnselectEvent event);
    public void onRowSelectFamAnaNonCorr(SelectEvent event);
    public void onRowUnselectFamAnaNonCorr(UnselectEvent event);
    public void onRowSelectFamAnaAltri(SelectEvent event);
    public void onRowUnselectFamAnaAltri(UnselectEvent event);
    
	public void saveSchedaMultidimAnzDialog();
	
	public List<CsDValutazione> getListaSchedeMultidims();
	public CsDValutazione getSelectedSchedaMultidimAnz();
	public CsDValutazione getNewSchedaMultidimAnz();
	public List<String> getPrestazionis();
	public List<String> getDachis();
	public List<String> getPrestDachis();
	public String getInfoValReteSoc();
	public String getInfoSalFisic();
	public String getInfoAdAbit();
	public String getInfoUbAbit();
	public String getInfoValFam();
	public List<CsAComponente> getFamComponentes();
	public List<CsAComponente> getSelectedFamComponentes();
	public Date getDataValutazione();
	public String getDescrizioneScheda();
	public boolean isRemoveAnaConvButton();
	public boolean isAddAnaCorrButton();
	public boolean isRemoveAnaNonConvButton();
	public boolean isAddAnaNonCorrButton();
	public boolean isApriAnaConvButton();
	public boolean isApriAnaNonConvButton();
	public boolean isRemoveAnaAltriButton();
	public boolean isAddAnaAltriButton();
	public boolean isApriAltrifamButton();
	public List<CsAComponente> getFamAltriComponentes();
	public boolean isConvivente();
	public boolean isParente();
	public boolean isNewSchedaMultidimAnzRendered();
	public boolean isReadOnly();

	public void setOnViewBarthel(CsDValutazione scheda) throws Exception;	
}
