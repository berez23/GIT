package it.webred.cet.service.gestprep.web.beans.anag;

import it.webred.cet.permission.CeTUser;
import it.webred.ct.service.gestprep.data.access.dto.SoggettoSearchCriteria;
import it.webred.ct.service.gestprep.data.model.GestPrepSoggetti;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;
import org.ajax4jsf.model.SerializableDataModel;

public class AnagListDataModel extends SerializableDataModel implements Serializable {

	private Map<Long, GestPrepSoggetti> wrappedData = new HashMap<Long, GestPrepSoggetti>();

	private List<Long> wrappedKeys = null;
	private boolean detached = false;
	private Long currentPk;

	private String codIstat;
	private AnagDataProvider dataProvider;
	private SoggettoSearchCriteria criteria = new SoggettoSearchCriteria();

	public Object getRowKey() {
		return currentPk;
	}

	/**
	 * This method normally called by Visitor before request Data Row.
	 */
	@Override
	public void setRowKey(Object key) {
		this.currentPk = (Long) key;

	}

	@Override	
	public void walk(FacesContext context, DataVisitor visitor, Range range,
			Object argument) throws IOException {

		int firstRow = ((SequenceRange) range).getFirstRow();
		int numberOfRows = ((SequenceRange) range).getRows();

		// System.out.println("Walk [ " + firstRow+ "- " + numberOfRows + " ]");
		if (detached) { // Is this serialized model
			for (Long key : wrappedKeys) {
				setRowKey(key);
				visitor.process(context, key, argument);
			}
		} else { // if not serialized, than we request data from data provider
			wrappedKeys = new ArrayList<Long>();
			criteria.setEnteId(this.getEnte());
			for (GestPrepSoggetti item : dataProvider.getDataByRange(
					firstRow, numberOfRows, criteria)) {
				// System.out.println("Item ["+item+"]");
				wrappedKeys.add(item.getIdSogg());
				wrappedData.put(item.getIdSogg(),
						item);
				visitor.process(context, item.getIdSogg(), argument);
			}
		}
	}

	private Integer rowCount; // better to buffer row count locally

	@Override
	public int getRowCount() {
		criteria.setEnteId(this.getEnte());
		rowCount = new Integer((int) dataProvider.getRecordCount(criteria));
		return rowCount.intValue();
	}

	public Object getRowData() {
		if (currentPk == null) {
			return null;
		} else {
			GestPrepSoggetti ret = wrappedData.get(currentPk);
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

	public AnagDataProvider getDataProvider() {
		return dataProvider;
	}

	public void setDataProvider(AnagDataProvider dataProvider) {
		this.dataProvider = dataProvider;
		System.out.println("Data Provider ["+this.dataProvider+"]");
	}

	public SoggettoSearchCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(SoggettoSearchCriteria criteria) {
		this.criteria = criteria;		
	}

	public void setResetData(String val) {
		_resetData();
	}

	public void setResetData() {
		_resetData();
	}
	
	private void _resetData() {
		criteria = new SoggettoSearchCriteria();
		wrappedData = new HashMap<Long, GestPrepSoggetti>();
		wrappedKeys = null;
	}

	
	public String getEnte() {
		HttpSession session =  (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);;
		CeTUser user = (CeTUser) session.getAttribute("user");
		if (user != null) {
			return user.getCurrentEnte();
		}
		
		return null;
	}


}