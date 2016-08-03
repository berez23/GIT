package it.webred.ct.service.carContrib.data.access.common.dto;

import java.io.Serializable;

import it.webred.ct.data.access.basic.anagrafe.dto.IndirizzoAnagrafeDTO;
import it.webred.ct.data.access.basic.catasto.dto.IndirizzoDTO;
import it.webred.ct.data.model.catasto.Siticivi;
import it.webred.ct.data.model.ici.SitTIciOggetto;
import it.webred.ct.data.model.tarsu.SitTTarOggetto;
import it.webred.ct.service.carContrib.data.access.common.utility.StringUtility;

public class IndirizzoIciTarsuDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	//dati principali
	private String desVia;
	private String civico;
	//dati a volte presenti in Ici
	private String espCivico;
	private String scala;
	private String piano;
	private String interno;
	
	private String desIndirizzo;
	
	public String getDesVia() {
		return desVia;
	}
	public void setDesVia(String desVia) {
		this.desVia = desVia;
	}
	public String getCivico() {
		return civico;
	}
	public void setCivico(String civico) {
		this.civico = civico;
	}
	public String getEspCivico() {
		return espCivico;
	}
	public void setEspCivico(String espCivico) {
		this.espCivico = espCivico;
	}
	public String getScala() {
		return scala;
	}
	public void setScala(String scala) {
		this.scala = scala;
	}
	public String getPiano() {
		return piano;
	}
	public void setPiano(String piano) {
		this.piano = piano;
	}
	public String getInterno() {
		return interno;
	}
	public void setInterno(String interno) {
		this.interno = interno;
	}
	
	public String getDesIndirizzo() {
		return desIndirizzo;
	}
	public void setDesIndirizzo(String desIndirizzo) {
		this.desIndirizzo = desIndirizzo;
	}
	public void valorizza(SitTIciOggetto ogg){
		String indCompleto="";
		if (ogg != null) {
			desVia=ogg.getDesInd();
			civico=ogg.getNumCiv();
			espCivico=ogg.getEspCiv();
			scala=ogg.getScala();
			piano=ogg.getPiano();
			interno=ogg.getInterno();
			if (desVia !=null)
				indCompleto += desVia;
			if (civico !=null && !civico.equals(""))
				indCompleto += ", " + StringUtility.removeLeadingZero(civico);
			if (espCivico !=null && !espCivico.equals(""))
				indCompleto += "/" + StringUtility.removeLeadingZero(espCivico);
			if (scala !=null && !scala.equals(""))
				indCompleto += " Sc. " + StringUtility.removeLeadingZero(scala);
			if (interno !=null && !interno.equals(""))
				indCompleto += " Int. " + StringUtility.removeLeadingZero(interno);
		}
		desIndirizzo= indCompleto;
	}
	public void valorizza(SitTTarOggetto ogg){
		String indCompleto="";
		if (ogg != null) {
			desVia=ogg.getDesInd();
			civico=ogg.getNumCiv();
			espCivico=ogg.getEspCiv();
			scala=ogg.getScala();
			piano=ogg.getPiano();
			interno=ogg.getInterno();
			if (desVia !=null)
				indCompleto += desVia;
			if (civico !=null && !civico.equals(""))
				indCompleto += ", " + StringUtility.removeLeadingZero(civico);
			if (espCivico !=null && !espCivico.equals(""))
				indCompleto += "/" + StringUtility.removeLeadingZero(espCivico);
			if (scala !=null && !scala.equals(""))
				indCompleto += " Sc. " + StringUtility.removeLeadingZero(scala);
			if (interno !=null && !interno.equals(""))
				indCompleto += " Int. " + StringUtility.removeLeadingZero(interno);
		}
		desIndirizzo= indCompleto;
	}
	
	public void valorizza(IndirizzoAnagrafeDTO indAna){
		String indCompleto="";
		if (indAna != null) {
			desVia="";
			if (indAna.getSedimeVia() !=null)
				desVia =indAna.getSedimeVia() + " " ;
			if (indAna.getDesVia() !=null)
				desVia += indAna.getDesVia();
			indCompleto=desVia;
			civico ="";
		    if (indAna.getCivico()!=null) {
		    	civico=indAna.getCivico();
		    	indCompleto +=", " + civico;
		    }
			String altriLivCiv = "";
			if (indAna.getCivicoLiv2() != null && !(indAna.getCivicoLiv2()).equals(""))
				altriLivCiv+= " " + indAna.getCivicoLiv2();
			if (indAna.getCivicoLiv3() != null && !(indAna.getCivicoLiv3()).equals(""))
				altriLivCiv+= " " + indAna.getCivicoLiv3();
			if (altriLivCiv !=null &&  !altriLivCiv.equals(""))  
				indCompleto += " " + altriLivCiv;
		}
		desIndirizzo= indCompleto;
	}
	
	public void valorizza(IndirizzoDTO indCat){
		String indCompleto="";
		if (indCat != null) {
			desVia =indCat.getStrada();
			civico =indCat.getNumCivico();
			indCompleto = indCat.getStrada();
			if (civico!=null &&  !civico.equals(""))  
				indCompleto += ", " + indCat.getNumCivico();
		}
		desIndirizzo= indCompleto;
	}
	//TODO.....
	public void valorizza(Siticivi ogg){
		String indCompleto="";
		
		desIndirizzo= indCompleto;
	}
}
