package it.webred.mui.consolidation;

import java.io.Serializable;

public class DapStat implements Serializable{

	private long iidFornitura;
	private long titolarita;
	private long evaluated = 0;
	private long dapNA = 0;
	private long dapY = 0;
	private long dapN = 0;
	private boolean evaluatedForced = false;
	
	public DapStat() {
		super();
		// TODO Auto-generated constructor stub
	}

	public long getEvaluated() {
		return evaluated;
	}

	public void setEvaluated(long evaluated) {
		this.evaluated = evaluated;
	}

	public long getIidFornitura() {
		return iidFornitura;
	}

	public void setIidFornitura(long iidFornitura) {
		this.iidFornitura = iidFornitura;
	}

	public long getTitolarita() {
		return titolarita;
	}

	public void setTitolarita(long titolarita) {
		this.titolarita = titolarita;
	}

	public long getToBeEvaluated() {
		return titolarita-evaluated;
	}

	public boolean isFullyEvaluated() {
		return evaluatedForced? true: getToBeEvaluated() == 0;
	}

	public void setEvaluatedForced(boolean evaluatedForced) {
		this.evaluatedForced = evaluatedForced;
	}

	public long getDapN() {
		return dapN;
	}

	public void setDapN(long dapN) {
		this.dapN = dapN;
	}

	public long getDapNA() {
		return dapNA;
	}

	public void setDapNA(long dapNA) {
		this.dapNA = dapNA;
	}

	public long getDapY() {
		return dapY;
	}

	public void setDapY(long dapY) {
		this.dapY = dapY;
	}
	

}
