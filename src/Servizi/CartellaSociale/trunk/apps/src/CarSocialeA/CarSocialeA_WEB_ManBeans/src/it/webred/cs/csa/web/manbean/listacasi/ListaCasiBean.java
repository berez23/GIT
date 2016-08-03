package it.webred.cs.csa.web.manbean.listacasi;

import it.webred.cs.data.DataModelCostanti;
import it.webred.cs.jsf.bean.DatiCasoBean;
import it.webred.cs.jsf.interfaces.IListaCasi;
import it.webred.cs.jsf.manbean.IterDialogMan;
import it.webred.cs.jsf.manbean.superc.CsUiCompBaseBean;
import it.webred.utilities.CommonUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.naming.NamingException;

import org.primefaces.model.LazyDataModel;


@ManagedBean
@ViewScoped
public class ListaCasiBean extends CsUiCompBaseBean implements IListaCasi{

	private String widgetVar = "listaCasiVar";
	private LazyDataModel<DatiCasoBean> lazyListaCasiModel;
	private DatiCasoBean selectedCaso;
	
	private boolean renderListaCasi;
	
	@ManagedProperty( value="#{iterDialogMan}")
	private IterDialogMan iterDialogMan;

	public ListaCasiBean() {
		lazyListaCasiModel = new LazyListaCasiModel();
	}
	
	@Override
	public IterDialogMan getIterDialogMan() {
		return iterDialogMan;
	}

	public void setIterDialogMan(IterDialogMan iterDialogMan) {
		this.iterDialogMan = iterDialogMan;
	}

	public boolean isRenderListaCasi() {
		return checkPermesso(DataModelCostanti.PermessiCaso.VISUALIZZAZIONE_LISTA_CASI);
	}

	@Override
	public String getWidgetVar() {
		return widgetVar;
	}

	public void setWidgetVar(String widgetVar) {
		this.widgetVar = widgetVar;
	}
	
	public DatiCasoBean getSelectedCaso() {
		return selectedCaso;
	}

	public void setSelectedCaso(DatiCasoBean selectedCaso) {
		this.selectedCaso = selectedCaso;
	}

	public LazyDataModel<DatiCasoBean> getLazyListaCasiModel() {
		return lazyListaCasiModel;
	}

	public void setLazyListaCasiModel(LazyDataModel<DatiCasoBean> lazyListaCasiModel) {
		this.lazyListaCasiModel = lazyListaCasiModel;
	}

	@PostConstruct
	public void onPostConstruct() throws NumberFormatException, NamingException {
		String sIdCaso = getRequest().getParameter("IDCASO");
		if( CommonUtils.isNotEmptyString(sIdCaso) )
			iterDialogMan.openDialog(Long.parseLong(sIdCaso));
	}
	
	@Override
	public ActionListener getCloseDialog() {
	    return new ActionListener() {
	        @Override
	        public void processAction(ActionEvent event) throws AbortProcessingException {
	        	//loadListaCasi();
	        }
	    };
	}

	@Override
	public void rowDeselect() {
		lazyListaCasiModel = new LazyListaCasiModel();
		
	}
	
	
}
