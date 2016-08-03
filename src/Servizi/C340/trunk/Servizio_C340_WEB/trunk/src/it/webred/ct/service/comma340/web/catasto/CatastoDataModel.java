package it.webred.ct.service.comma340.web.catasto;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;
import org.ajax4jsf.model.SerializableDataModel;

import it.webred.ct.data.access.basic.catasto.dto.*;

public class CatastoDataModel extends SerializableDataModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private Map<String, SintesiImmobileDTO> wrappedData = null;

	private List<String> wrappedKeys = null;
	private boolean detached = false;
	private String currentPk;
	
	private CatastoDataProvider dataProvider;

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
	public void walk(FacesContext context, DataVisitor visitor, Range range, Object argument) throws IOException {
		int firstRow = ((SequenceRange) range).getFirstRow();
		int numberOfRows = ((SequenceRange) range).getRows();
		
		System.out.println("Walk [ " + firstRow+ "- " + numberOfRows + " ]");
		if (detached) { // Is this serialized model
			for (String key : wrappedKeys) {
				setRowKey(key);
				visitor.process(context, key, argument);
			}
			System.out.println("Detached");
		} else { // if not serialized, than we request data from data provider
			wrappedKeys = new ArrayList<String>();
			wrappedData = new HashMap<String, SintesiImmobileDTO> ();
			for (SintesiImmobileDTO item : dataProvider.getDataByRange(firstRow, numberOfRows)) {
				wrappedKeys.add("" + item.getIdImmobile());
				wrappedData.put("" + item.getIdImmobile(),item);
				visitor.process(context, "" + item.getIdImmobile(), argument);
			}
		}
	}

	private Integer rowCount; // better to buffer row count locally

	@Override
	public int getRowCount() {
		rowCount = new Integer(dataProvider.getRecordCount().intValue());
		System.out.println("Row count ["+rowCount+"]");
		return rowCount.intValue();
	}

	public Object getRowData() {
		if (currentPk == null) {
			return null;
		} else {
			SintesiImmobileDTO ret = wrappedData.get(currentPk);
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

	/*@Override
	public SerializableDataModel getSerializableModel(Range range) {
		if (wrappedKeys != null) {
			detached = true;
			return this;
		}
		return null;
	}*/
	
	public CatastoDataProvider getDataProvider() {
		return dataProvider;
	}

	public void setDataProvider(CatastoDataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}

	public void setResetData(String val) {
		dataProvider.resetData();
	}
	
	public void pulisciFiltro(ActionEvent event) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("catastoDataModel");
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("catastoDataProviderImpl");
	}

}
