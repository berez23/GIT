package it.webred.ct.service.cnc.web.flusso750;

import it.webred.ct.data.access.basic.cnc.flusso750.dto.Flusso750SearchCriteria;
import it.webred.ct.data.model.cnc.flusso750.VAnagrafica;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;
import org.ajax4jsf.model.SerializableDataModel;

public class Flusso750AnagDataModel extends SerializableDataModel implements
		Serializable {

	private Map<Long, VAnagrafica> wrappedData = new HashMap<Long, VAnagrafica>();

	private List<Long> wrappedKeys = null;
	private boolean detached = false;
	private Long currentPk;

	private String codIstat;
	private Flusso750AnagDataProvider dataProvider;
	private Flusso750SearchCriteria criteria = new Flusso750SearchCriteria();

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
			for (VAnagrafica item : dataProvider.getDataByRange(
					firstRow, numberOfRows, criteria)) {
				
				wrappedKeys.add(item.getId());
				wrappedData.put(item.getId(),
						item);
				visitor.process(context, item.getId(), argument);
			}
		}
	}

	private Integer rowCount; // better to buffer row count locally

	@Override
	public int getRowCount() {
		rowCount = new Integer((int) dataProvider.getRecordCount(criteria));
		return rowCount.intValue();
	}

	public Object getRowData() {
		if (currentPk == null) {
			return null;
		} else {
			VAnagrafica ret = wrappedData.get(currentPk);
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

	public Flusso750AnagDataProvider getDataProvider() {
		return dataProvider;
	}

	public void setDataProvider(Flusso750AnagDataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}



	public Flusso750SearchCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(Flusso750SearchCriteria criteria) {
		this.criteria = criteria;
	}

	public void setResetData(String val) {
		_resetData();
	}

	public void setResetData() {
		_resetData();
	}
	
	private void _resetData() {
		criteria = new Flusso750SearchCriteria();
		wrappedData = new HashMap<Long, VAnagrafica>();
		wrappedKeys = null;
	}

}
