package it.webred.ct.service.carContrib.web.pages.filtro;

import it.webred.cet.permission.CeTUser;
import it.webred.ct.service.carContrib.data.access.cc.dto.FiltroRichiesteSearchCriteria;
import it.webred.ct.service.carContrib.web.UserBean;
import it.webred.ct.service.carContrib.web.beans.FiltroRichieste;
import it.webred.ct.service.carContrib.web.utils.Permessi;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;
import org.ajax4jsf.model.SerializableDataModel;
import org.apache.log4j.Logger;

public class FiltroDataModel extends SerializableDataModel implements
		Serializable {

	protected Logger logger = Logger.getLogger("carcontrib.log");
	
	private static final long serialVersionUID = 1L;

	private Map<String, FiltroRichieste> wrappedData = new HashMap<String, FiltroRichieste>();

	private List<String> wrappedKeys = null;
	private boolean detached = false;
	private String currentPk;

	private FiltroDataProvider dataProvider;
	private FiltroRichiesteSearchCriteria criteria = new FiltroRichiesteSearchCriteria();

	private boolean visualizzaSelectOperatori;
	private boolean showTableRusult;
	
	public void setVisualizzaSelectOperatori(boolean visualizzaSelectOperatori) {
		this.visualizzaSelectOperatori = visualizzaSelectOperatori;
	}

	public void setShowTableRusult(boolean showTableRusult) {
		this.showTableRusult = showTableRusult;
	}

	public boolean isShowTableRusult() {
		return showTableRusult;
	}

	public boolean isVisualizzaSelectOperatori() {
		return visualizzaSelectOperatori;
	}
	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}

	public Object getRowKey() {
		return currentPk;
	}

	/**
	 * This method normally called by Visitor before request Data Row.
	 */
	@Override
	public void setRowKey(Object key) {
		this.currentPk = (String) key;

	}

	@Override	
	public void walk(FacesContext context, DataVisitor visitor, Range range,
			Object argument) throws IOException {

		int firstRow = ((SequenceRange) range).getFirstRow();
		int numberOfRows = ((SequenceRange) range).getRows();

		if (detached) { // Is this serialized model
			logger.info(" walk detached");
			for (String key : wrappedKeys) {
				setRowKey(key);
				visitor.process(context, key, argument);
			}
		} else { // if not serialized, than we request data from data provider
			wrappedKeys = new ArrayList<String>();
			
			UserBean u = new UserBean();
			CeTUser user =u.getUser();
			criteria.setEnteId(user.getCurrentEnte());
			criteria.setUserId(user.getUsername());
			
			for (FiltroRichieste item : dataProvider.getDataByRange(criteria,firstRow, numberOfRows)) {
				wrappedKeys.add(item.getRichiesta().getIdRichiesta());
				wrappedData.put(item.getRichiesta().getIdRichiesta(),
						item);
				
				visitor.process(context, item.getRichiesta().getIdRichiesta(), argument);
			}
		}
	}

	private Integer rowCount; // better to buffer row count locally

	private FiltroDataProviderImpl dataService;

	@Override
	public int getRowCount() {
		
		UserBean u = new UserBean();
		CeTUser user =u.getUser();
		criteria.setEnteId(user.getCurrentEnte());
		criteria.setUserId(user.getUsername());
		
		Long res = dataProvider.getRecordCount(criteria);
		if (res!=null)
			rowCount = res.intValue();
		else
			rowCount = 0;
		
		return rowCount.intValue();
	}

	public Object getRowData() {
		if (currentPk == null) {
			return null;
		} else {
			FiltroRichieste ret = wrappedData.get(currentPk);
			if (ret == null) {
				// ret = dataProvider.getStanza(currentPk);
				// wrappedData.put(currentPk, ret);
				// return ret;
				return null;
			} else {
				return ret;
			}
		}
	}

	@Override
	public int getRowIndex() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getWrappedData() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isRowAvailable() {
		/*
		 * if (currentPk==null) { return false; } else { if
		 * (dataProvider.getStanza(currentPk) != null) return true; else return
		 * false; }
		 */

		return true;
	}

	@Override
	public void setRowIndex(int rowIndex) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setWrappedData(Object data) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void update() {
	}

	public FiltroDataProvider getDataProvider() {
		return dataProvider;
	}

	public void setDataProvider(FiltroDataProvider dataProvider) {
		this.dataProvider = dataProvider;
		logger.info("Data Provider ["+this.dataProvider+"]");
	}

	public FiltroRichiesteSearchCriteria getCriteria() {
		
		return criteria;
	}
	
    public void controllaPermessi() {
    	logger.info( "in FiltroRichiesteSearchCriteria.getCriteria()");	
		UserBean u = new UserBean();
		CeTUser user =u.getUser();
		//SE L'UTENTE NON è SUPERVISORE PUO' VEDERE SOLO LE RICHIESTE DA LUI GESTITE
		if (!Permessi.controlla(user,Permessi.PERMESSO_SUPERVISIONE_RICHIESTE_CARTELLA)) {
			criteria.setUserGesRic(user.getUsername());
			logger.info( "L'UTENTE NON HA IL PERMESSO DI SUPERVISORE");	
		}else {
			visualizzaSelectOperatori=true;
			logger.info( "L'UTENTE HA IL PERMESSO DI SUPERVISORE");	
		}
    	
    }
    
	public void setCriteria(FiltroRichiesteSearchCriteria criteria) {
		this.criteria = criteria;		
	}

	public void setResetData(String val) {
		_resetData();
	}

	public void setResetData() {
		_resetData();
	}
	
	private void _resetData() {
		criteria = new FiltroRichiesteSearchCriteria();
		wrappedData = new HashMap<String, FiltroRichieste>();
		wrappedKeys = null;
		showTableRusult=false;
		
		UserBean u = new UserBean();
		CeTUser user =u.getUser();
		if (!Permessi.controlla(user,Permessi.PERMESSO_SUPERVISIONE_RICHIESTE_CARTELLA)) {
			visualizzaSelectOperatori=true;
			logger.info( "L'UTENTE HA IL PERMESSO DI SUPERVISORE");	
		}
		else {
			visualizzaSelectOperatori=false;
			logger.info( "L'UTENTE NON HA IL PERMESSO DI SUPERVISORE");	
		}
	}
	
	public TimeZone getTimeZone() {
		return TimeZone.getDefault();
	}
	
	public String getUsername() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		CeTUser user = (CeTUser) session.getAttribute("user");
		if (user != null) {
			return user.getUsername();
		}
		
		return null;
	}

	//Aggiunto Alessandra
    public List<SelectItem> getUsersNames() {
		UserBean u = new UserBean();
		CeTUser user =u.getUser();

    	CeTBaseObject cetObj = new CeTBaseObject();
		cetObj.setEnteId(user.getCurrentEnte());
		cetObj.setUserId(user.getUsername());
		
		return  dataProvider.getDistinctUserName(cetObj);
	}

	public FiltroDataProviderImpl getDataService() {
		return dataService;
	}
	public void setDataService(FiltroDataProviderImpl dataService) {
		this.dataService = dataService;
	}
}
