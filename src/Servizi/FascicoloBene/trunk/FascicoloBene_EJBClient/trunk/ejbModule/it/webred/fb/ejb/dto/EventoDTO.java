package it.webred.fb.ejb.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventoDTO implements Serializable {

	private final SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
	private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	private String codice;
	private String descrizione;
	private String data;
	
	
	
	public String getCodice() {
		return codice;
	}



	public void setCodice(String codice) {
		this.codice = codice;
	}



	public String getDescrizione() {
		return descrizione;
	}



	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}



	public String getData() {
		return data;
	}



	public void setData(String data) {
		this.data = data;
	}



	public String getDataEventoFormattata(){
		try{
			Date dt =  yyyyMMdd.parse(data);
			return sdf.format(dt);
		}catch(Exception e){
		}
		return data;
	}

}
