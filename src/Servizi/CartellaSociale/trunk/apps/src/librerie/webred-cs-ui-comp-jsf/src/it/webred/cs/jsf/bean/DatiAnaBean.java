package it.webred.cs.jsf.bean;

import it.webred.cs.csa.utils.StringUtils;

import java.io.Serializable;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.NoneScoped;

@ManagedBean
@NoneScoped
public class DatiAnaBean implements Serializable {	

	private static final long serialVersionUID = 1L;
	
	private String cognome;
	private String nome;
	private Date dataNascita;
	private String sesso;
	private String codiceFiscale;
	private String cittadinanza;
	private String telefono;
	private String cellulare;
	private String eMail;
	

	public String getCognome() {
		return StringUtils.capitalize(cognome);
	}
	
	public void setCognome(String cognome) {
		this.cognome = StringUtils.capitalize(cognome);
	}

	public String getNome() {
		return StringUtils.capitalize(nome);
	}

	public void setNome(String nome) {
		this.nome = StringUtils.capitalize(nome);
	}

	public Date getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(Date dataNascita) {
		this.dataNascita = dataNascita;
	}	

	public String getSesso() {
		return sesso;
	}

	public void setSesso(String sesso) {
		this.sesso = sesso;
	}

	public String getCodiceFiscale() {
		return StringUtils.uppercase(codiceFiscale);
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = StringUtils.uppercase(codiceFiscale);
	}

	public String getCittadinanza() {
		return cittadinanza;
	}

	public void setCittadinanza(String cittadinanza) {
		this.cittadinanza = cittadinanza;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCellulare() {
		return cellulare;
	}

	public void setCellulare(String cellulare) {
		this.cellulare = cellulare;
	}

	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

}
