package it.webred.diogene.visualizzatore.runtime;

import it.webred.diogene.db.model.DcColumn;

import java.util.HashMap;

public class ColumnElement extends DcColumn {

	private HashMap operators;
	private HashMap listValues;

	public ColumnElement(DcColumn dcc) {
		
	}

	public HashMap getListValues() {
		return listValues;
	}

	public void setListValues(HashMap listValues) {
		this.listValues = listValues;
	}

	public HashMap getOperators() {
		return operators;
	}

	public void setOperators(HashMap operators) {
		this.operators = operators;
	}
}
