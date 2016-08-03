/*
 * Created on 18-mar-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.eiv.titolo;
	

/**
 * @author silviat
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class JavaBeanTitolo {
	private String stampa="OFF"; 
	private String info="OFF";
	private String  layout="OFF";
	private String digi="OFF";
	private String sele="OFF";
	private String analisi="OFF";
	
	public String getStampa(){
		return this.stampa;
	}
	public String getInfo(){
			return this.info;
	}
	public String getLayout(){
			return this.layout;
	}
	public String getDigi(){
			return this.digi;
	}
	public String getSele(){
			return this.sele;
	}
	public String getAnalisi(){
			return this.analisi;
	}
	public void setStampa(String stampa ){
		this.stampa=stampa;
	}
	public void setInfo(String info ){
			this.info=info;
	}
	public void setLayout(String layout ){
			this.layout=layout;
	}
	public void setDigi(String digi ){
			this.digi=digi;
	}
	public void setSele(String sele ){
			this.sele= sele;
	}
	public void setAnalisi(String analisi){
			this.analisi=analisi;
	}	
}
