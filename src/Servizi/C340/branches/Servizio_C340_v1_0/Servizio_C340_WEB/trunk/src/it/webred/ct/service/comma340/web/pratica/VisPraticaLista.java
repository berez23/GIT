package it.webred.ct.service.comma340.web.pratica;

public class VisPraticaLista {

	String id;
	String idUiu;
	String tipo;
	String dichiarante;
	String cf;
	String indDich;
	String indImm;
	String dtProt;	
	
	public final static String TIPO_PLANIMETRIA = "P";
	public final static String TIPO_SUPERFICIE = "S";
	public final static String DESC_TIPO_PLANIMETRIA = "Pratica per deposito della planimetria catastale";
	public final static String DESC_TIPO_SUPERFICIE = "Pratica di istanza di rettifica della superficie";
	
	
	public String getIdUiu() {
		return idUiu;
	}

	public void setIdUiu(String idUiu) {
		this.idUiu = idUiu;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getDichiarante() {
		return dichiarante;
	}

	public void setDichiarante(String dichiarante) {
		this.dichiarante = dichiarante;
	}

	public String getCf() {
		return cf;
	}

	public void setCf(String cf) {
		this.cf = cf;
	}

	public String getIndDich() {
		return indDich;
	}

	public void setIndDich(String indDich) {
		this.indDich = indDich;
	}

	public String getIndImm() {
		return indImm;
	}

	public void setIndImm(String indImm) {
		this.indImm = indImm;
	}

	public String getDtProt() {
		return dtProt;
	}

	public void setDtProt(String dtProt) {
		this.dtProt = dtProt;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getDescTipo() {
		if (getTipo().equalsIgnoreCase(TIPO_PLANIMETRIA))
			return DESC_TIPO_PLANIMETRIA;
		if (getTipo().equalsIgnoreCase(TIPO_SUPERFICIE))
			return DESC_TIPO_SUPERFICIE;
		return "";
	}
	
}
