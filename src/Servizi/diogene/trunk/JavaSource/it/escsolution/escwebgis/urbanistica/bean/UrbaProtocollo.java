package it.escsolution.escwebgis.urbanistica.bean;

public class UrbaProtocollo extends Urbanistica{

	private static final long serialVersionUID = 7025768229083909561L;
	
	private String pos_ed = "";
	private String resp_proc = "";
	private String prot_dom = "";
	private String data_dom = "";
	private String intervento = "";
	private String invio_asl = "";
	private String rientro_asl = "";
	private String parere_asl = "";
	private String cartella_utp_n = "";

	public UrbaProtocollo() {
		// TODO Auto-generated constructor stub
	}

	public String getPos_ed() {
		return pos_ed;
	}

	public void setPos_ed(String pos_ed) {
		this.pos_ed = pos_ed;
	}

	public String getResp_proc() {
		return resp_proc;
	}

	public void setResp_proc(String resp_proc) {
		this.resp_proc = resp_proc;
	}

	public String getProt_dom() {
		return prot_dom;
	}

	public void setProt_dom(String prot_dom) {
		this.prot_dom = prot_dom;
	}

	public String getData_dom() {
		return data_dom;
	}

	public void setData_dom(String data_dom) {
		this.data_dom = data_dom;
	}

	public String getIntervento() {
		return intervento;
	}

	public void setIntervento(String intervento) {
		this.intervento = intervento;
	}

	public String getInvio_asl() {
		return invio_asl;
	}

	public void setInvio_asl(String invio_asl) {
		this.invio_asl = invio_asl;
	}

	public String getRientro_asl() {
		return rientro_asl;
	}

	public void setRientro_asl(String rientro_asl) {
		this.rientro_asl = rientro_asl;
	}

	public String getParere_asl() {
		return parere_asl;
	}

	public void setParere_asl(String parere_asl) {
		this.parere_asl = parere_asl;
	}

	public String getCartella_utp_n() {
		return cartella_utp_n;
	}

	public void setCartella_utp_n(String cartella_utp_n) {
		this.cartella_utp_n = cartella_utp_n;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
	

}
