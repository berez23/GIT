package it.webred.ct.service.muidocfa.web.bean.ici.pagination;

import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.diagnostiche.ici.dto.DocfaIciReportDTO;
import it.webred.ct.data.model.catasto.Sitideco;
import it.webred.ct.data.model.diagnostiche.DocfaIciReport;

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

public class DataModel extends SerializableDataModel implements Serializable {

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

		// logger.debug("Walk [ " + firstRow+ "- " + numberOfRows + " ]");
		if (detached) { // Is this serialized model
			for (String key : wrappedKeys) {
				setRowKey(key);
				visitor.process(context, key, argument);
			}
		} else { // if not serialized, than we request data from data provider
			wrappedKeys = new ArrayList<String>();
			for (DocfaIciReport item : dataProvider.getReportByRange(firstRow,
					numberOfRows)) {

				DocfaIciReportDTO dto = new DocfaIciReportDTO();
				if(item.getAnnotazioni() != null)
					item.setAnnotazioni(item.getAnnotazioni().replaceAll("@", ";"));
				dto.setDocfaIciReport(item);
				// gestione civici
				if (item.getCiviciDocfa() != null) {
					dto.getDocfaIciReport().setCiviciDocfa(item.getCiviciDocfa().replaceAll("@", ","));
				}

				dto.setCategoriaDocfaExt(dataProvider.getCategoria(item.getCategoriaDocfa()));
				dto.setCausaleDocfaExt(dataProvider.getCausale(item.getCausaleDocfa()));

				wrappedKeys.add("" + item.getIdRep());
				wrappedData.put("" + item.getIdRep(), dto);
				visitor.process(context, "" + item.getIdRep(), argument);
			}
		}
	}

	private Integer rowCount; // better to buffer row count locally

	@Override
	public int getRowCount() {
		rowCount = new Integer(dataProvider.getReportCount().intValue());
		// logger.debug("Row count ["+rowCount+"]");
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
	}

	public void setResetData(String val) {
		dataProvider.resetData();
	}
}
