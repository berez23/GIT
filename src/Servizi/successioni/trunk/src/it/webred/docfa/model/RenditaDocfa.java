package it.webred.docfa.model;


public class RenditaDocfa  implements java.io.Serializable {


	String assenzaCatasto="";// per memorizzare l'assenza della microzona nello scarico del catasto 
	String microzonaCatasto; 
	String foglio ; 
	String particella ; 
	String subalterno ; 
	String renditaDocfaX100 ; 
	String renditaDocfa5 ; 
	String petteriniValoreMedio = ""; 
	String flagNC ; 
	String valoreCommerciale = ""; 
	String rapportoValore = "";
	String rapportoValorex100 = "" ;
	String valoreCatastale;
	String flagControllo = "1";  // OK
	String flagControllox100 = "1";  // OK

    // Constructors

    /** default constructor */
    public RenditaDocfa() {
    }
    
    public String getAssenzaCatasto() {
		return assenzaCatasto;
	}
	public void setAssenzaCatasto(String assenzaCatasto) {
		this.assenzaCatasto = assenzaCatasto;
	}
	public String getFlagNC() {
		return flagNC;
	}
	public void setFlagNC(String flagNC) {
		this.flagNC = flagNC;
	}
	public String getFoglio() {
		return foglio;
	}
	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}
	public String getMicrozonaCatasto() {
		return microzonaCatasto;
	}
	public void setMicrozonaCatasto(String microzonaCatasto) {
		this.microzonaCatasto = microzonaCatasto;
	}
	public String getParticella() {
		return particella;
	}
	public void setParticella(String particella) {
		this.particella = particella;
	}
	public String getPetteriniValoreMedio() {
		return petteriniValoreMedio;
	}
	public void setPetteriniValoreMedio(String petteriniValoreMedio) {
		this.petteriniValoreMedio = petteriniValoreMedio;
	}
	public String getRapportoValore() {
		return rapportoValore;
	}
	public void setRapportoValore(String rapportoValore) {
		this.rapportoValore = rapportoValore;
	}
	public String getRenditaDocfa5() {
		return renditaDocfa5;
	}
	public void setRenditaDocfa5(String renditaDocfa5) {
		this.renditaDocfa5 = renditaDocfa5;
	}
	public String getRenditaDocfaX100() {
		return renditaDocfaX100;
	}
	public void setRenditaDocfaX100(String renditaDocfaX100) {
		this.renditaDocfaX100 = renditaDocfaX100;
	}
	public String getSubalterno() {
		return subalterno;
	}
	public void setSubalterno(String subalterno) {
		this.subalterno = subalterno;
	}
	public String getValoreCommerciale() {
		return valoreCommerciale;
	}
	public void setValoreCommerciale(String valoreCommerciale) {
		this.valoreCommerciale = valoreCommerciale;
	}
	public String getValoreCatastale() {
		return valoreCatastale;
	}
	public void setValoreCatastale(String valoreCatastale) {
		this.valoreCatastale = valoreCatastale;
	}
	public String getFlagControllo() {
		return flagControllo;
	}
	public void setFlagControllo(String flagControllo) {
		this.flagControllo = flagControllo;
	}
	public String getRapportoValorex100() {
		return rapportoValorex100;
	}
	public void setRapportoValorex100(String rapportoValorex100) {
		this.rapportoValorex100 = rapportoValorex100;
	}
	public String getFlagControllox100() {
		return flagControllox100;
	}
	public void setFlagControllox100(String flagControllox100) {
		this.flagControllox100 = flagControllox100;
	} 
    
}
