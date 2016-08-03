package it.webred.mui.model;

import it.webred.mui.consolidation.ViarioDecoder;

public class CodiceViaCivico {
	private String codiceVia;
	private String civico;
	private static ViarioDecoder viario = new ViarioDecoder();
	public String getCivico() {
		return civico;
	}
	public void setCivico(String civico) {
		this.civico = civico;
	}
	public String getCodiceVia() {
		return codiceVia;
	}
	public String getVia() throws Exception {
		return viario.decodeVia(getCodiceVia());
	}
	public void setCodiceVia(String codiceVia) {
		this.codiceVia = codiceVia;
	}
}

