package it.webred.ct.service.tsSoggiorno.web.bean.dichiarazione;

import it.webred.ct.service.tsSoggiorno.data.access.dto.DichiarazioneDTO;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;
import org.ajax4jsf.model.SerializableDataModel;

public class DichiarazioneDataModel extends SerializableDataModel implements Serializable {

	private Map<String, Object> wrappedData = new HashMap<String, Object>();

	private List<String> wrappedKeys = null;
	private boolean detached = false;
	private String currentPk;

	private DichiarazioneDataProvider dataProvider;

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

		// logger.debug("Walk [ " + firstRow+ "- " + numberOfRows + " ]");
		if (detached) { // Is this serialized model
			for (String key : wrappedKeys) {
				setRowKey(key);
				visitor.process(context, key, argument);
			}
		} else { // if not serialized, than we request data from data provider
			wrappedKeys = new ArrayList<String>();
			for (DichiarazioneDTO item : dataProvider.getDichiarazioniByRange(firstRow,
					numberOfRows)) {

				wrappedKeys.add("" + item.getDichiarazione().getId());
				wrappedData.put("" + item.getDichiarazione().getId(), item);
				visitor.process(context, "" + item.getDichiarazione().getId(), argument);
			}
		}
	}

	private Integer rowCount; // better to buffer row count locally

	@Override
	public int getRowCount() {
		rowCount = new Integer(dataProvider.getDichiarazioniCount());
		return rowCount.intValue();
	}

	public Object getRowData() {
		if (currentPk == null) {
			return null;
		} else {
			Object ret = wrappedData.get(currentPk);
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

	@Override
	public SerializableDataModel getSerializableModel(Range range) {
		if (wrappedKeys != null) {
			detached = true;
			return this;
		}
		return null;
	}

	public DichiarazioneDataProvider getDataProvider() {
		return dataProvider;
	}

	public void setDataProvider(DichiarazioneDataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}

	public void setResetData(String val) {
		//dataProvider.resetData();
	}
}
