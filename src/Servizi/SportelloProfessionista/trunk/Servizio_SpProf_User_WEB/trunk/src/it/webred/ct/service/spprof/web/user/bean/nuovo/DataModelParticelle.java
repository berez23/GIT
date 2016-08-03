package it.webred.ct.service.spprof.web.user.bean.nuovo;

import it.webred.ct.service.geospatial.data.access.dto.ParticellaDTO;

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

public class DataModelParticelle extends SerializableDataModel implements Serializable {

	private Map<String, Object> wrappedData = new HashMap<String, Object>();

	private List<String> wrappedKeys = null;
	private boolean detached = false;
	private String currentPk;

	private DataProvider dataProvider;

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

		// System.out.println("Walk [ " + firstRow+ "- " + numberOfRows + " ]");
		if (detached) { // Is this serialized model
			for (String key : wrappedKeys) {
				setRowKey(key);
				visitor.process(context, key, argument);
			}
		} else { // if not serialized, than we request data from data provider
			wrappedKeys = new ArrayList<String>();
			String fogli = "";
			String particelle = "";
			for (ParticellaDTO item : dataProvider.getParticelleByRange(firstRow,
					numberOfRows)) {

				System.out.println("Item [" + item.getParticella() + "]");
				wrappedKeys.add("" + item.getParticella());
				wrappedData.put("" + item.getParticella(), item);
				visitor.process(context, "" + item.getParticella(), argument);
				
				//creazione lista in string di fogli particella
				if(!fogli.contains(item.getFoglio()))
					fogli += "|" + item.getFoglio();
				particelle += "|" + item.getParticella();
			}
			if(!fogli.equals("")){ 
				dataProvider.setListaFogliRicerca(fogli.substring(1));
				dataProvider.setListaParticelleRicerca(particelle.substring(1));
			}
		}
	}

	private Integer rowCount; // better to buffer row count locally

	@Override
	public int getRowCount() {
		rowCount = new Integer(dataProvider.getParticelleCount().intValue());
		// System.out.println("Row count ["+rowCount+"]");
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

	public DataProvider getDataProvider() {
		return dataProvider;
	}

	public void setDataProvider(DataProvider dataProvider) {
		this.dataProvider = dataProvider;
		System.out.println("Data Provider [" + this.dataProvider + "]");
	}

	public void setResetData(String val) {
		dataProvider.resetData();
	}
}
