package it.webred.ct.diagnostics.service.web.beans.docfa.pagination;

import it.webred.ct.diagnostics.service.data.model.DocfaNonResidenziale;
import it.webred.ct.diagnostics.service.data.model.DocfaResidenziale;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;
import org.ajax4jsf.model.SerializableDataModel;

public class DocfaResDataModel extends SerializableDataModel implements Serializable{

	private static final long serialVersionUID = 1L;

	private Map<Date, DocfaResidenziale> wrappedData = new HashMap<Date, DocfaResidenziale>();

	private List<Date> wrappedKeys = null;
	private boolean detached = false;
	private Date currentPk;

	private DocfaResDataProvider docfaResDataProvider;

	public Object getRowKey() {
		return currentPk;
	}

	/**
	 * This method normally called by Visitor before request Data Row.
	 */
	@Override
	public void setRowKey(Object key) {
		this.currentPk = (Date) key;

	}

	@Override
	public void walk(FacesContext context, DataVisitor visitor, Range range,Object argument) throws IOException {
		int firstRow = ((SequenceRange) range).getFirstRow();
		int numberOfRows = ((SequenceRange) range).getRows();
		
		//System.out.println("Walk [ " + firstRow+ "- " + numberOfRows + " ]");
		if (detached) { // Is this serialized model
			for (Date key : wrappedKeys) {
				setRowKey(key);
				visitor.process(context, key, argument);
			}
		} else { // if not serialized, than we request data from data provider
			wrappedKeys = new ArrayList<Date>();
			for (DocfaResidenziale item : docfaResDataProvider.getVisualizzaByRange(firstRow, numberOfRows)) {

				//System.out.println("Item ["+item[0]+"]");
				wrappedKeys.add(item.getFornitura());
				wrappedData.put(item.getFornitura(),item);
				visitor.process(context,item.getFornitura(), argument);
			}
		}
	}

	private Integer rowCount; // better to buffer row count locally

	@Override
	public int getRowCount() {
		rowCount = new Integer((int) docfaResDataProvider.getVisualizzaCount());
		//System.out.println("Row count ["+rowCount+"]");
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
	public SerializableDataModel getSerializableModel(Range range) 
	{ 
	    if (wrappedKeys != null) 
	    { 
	        detached = true; 
	        return this; 
	    } 
	    return null; 
	}

	public DocfaResDataProvider getDocfaResDataProvider() {
		return docfaResDataProvider;
	}

	public void setDocfaResDataProvider(DocfaResDataProvider docfaResDataProvider) {
		this.docfaResDataProvider = docfaResDataProvider;
	}

	
}
