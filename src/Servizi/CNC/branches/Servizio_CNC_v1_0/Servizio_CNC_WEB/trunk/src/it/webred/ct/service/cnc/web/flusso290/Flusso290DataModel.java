package it.webred.ct.service.cnc.web.flusso290;

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

import it.webred.ct.data.access.basic.cnc.flusso290.dto.AnagraficaImpostaDTO;
import it.webred.ct.data.access.basic.cnc.flusso290.dto.Flusso290SearchCriteria;
import it.webred.ct.service.cnc.web.CNCBaseBean;

public class Flusso290DataModel extends SerializableDataModel implements
		Serializable {

	private Map<String, AnagraficaImpostaDTO> wrappedData = new HashMap<String, AnagraficaImpostaDTO>();

	private List<String> wrappedKeys = null;
	private boolean detached = false;
	private String currentPk;

	private String codIstat;
	private Flusso290DataProvider dataProvider;
	private Flusso290SearchCriteria criteria = new Flusso290SearchCriteria();

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
			for (AnagraficaImpostaDTO item : dataProvider.getDataByRange(
					firstRow, numberOfRows, criteria)) {
				// System.out.println("Item ["+item+"]");
				wrappedKeys.add(item.getAnagraficaIntestatario().getFileId());
				wrappedData.put(item.getAnagraficaIntestatario().getFileId(),
						item);
				visitor.process(context, item.getAnagraficaIntestatario()
						.getFileId(), argument);
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
			AnagraficaImpostaDTO ret = wrappedData.get(currentPk);
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

	public Flusso290DataProvider getDataProvider() {
		return dataProvider;
	}

	public void setDataProvider(Flusso290DataProvider dataProvider) {
		this.dataProvider = dataProvider;
		System.out.println("Data Provider ["+this.dataProvider+"]");
	}

	public Flusso290SearchCriteria getCriteria() {
		if (codIstat == null) {
			// Try to get it from the web.xml
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ServletContext ctx = (ServletContext) facesContext.getExternalContext().getContext();
			codIstat = ctx.getInitParameter("codice_istat");
			//System.out.println("Cod Istat ["+codIstat+"]");
			criteria.setCodiceComuneIscrizione(codIstat);
		}
		
		return criteria;
	}

	public void setCriteria(Flusso290SearchCriteria criteria) {
		this.criteria = criteria;		
	}

	public void setResetData(String val) {
		_resetData();
	}

	public void setResetData() {
		_resetData();
	}
	
	private void _resetData() {
		criteria = new Flusso290SearchCriteria();
		wrappedData = new HashMap<String, AnagraficaImpostaDTO>();
		wrappedKeys = null;
	}

}
