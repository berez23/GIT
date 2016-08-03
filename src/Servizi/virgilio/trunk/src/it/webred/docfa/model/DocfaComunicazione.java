package it.webred.docfa.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;


public class DocfaComunicazione extends UseDocfaComunicazione {
	private Set<DocfaComunOggetto> docfaComunOggettos = null;
	private Set<Residenza> residenzas = null;
	//data per controllo detrazione
	private String dataRegistrazione = "";
	
	public DocfaComunicazione() {
		super();
		init();
	}
    
	/** minimal constructor */
    public DocfaComunicazione(BigDecimal iidComunicazione) {
        super();
        init();
    }
    
    /** full constructor */
    public DocfaComunicazione(BigDecimal iidComunicazione, String iidProtocolloReg, String iidFornitura, String codfiscalePiva, String prefisso, String telefono, String denominazione, String nome, String dataNascita, String comuneNascita, String provinciaNascita, String sesso, String indirizzo, String cap, String comune, String provincia, String indirizzoDomicilio, String capDomicilio, String comuneDomicilio, String provinciaDomicilio, String codiceFiscalerl, String prefissorl, String telefonorl, String cognomerl, String nomerl, String dataNascitarl, String comuneNascitarl, String provinciaNascitarl, String sessorl, String indirizzorl, String caprl, String comunerl, String provinciarl, String indirizzoDomiciliorl, String capDomiciliorl, String comuneDomiciliorl, String provinciaDomiciliorl, Boolean flagRappresentanteLegale, Boolean flagCuratoreFallimentare, Boolean flagTutore, Boolean flagErede, String altraNatura, Character flagPersona, Date dataComunicazione, Long stato, String pagine, Long flagElaborato, Set<DocfaComunOggetto> docfaComunOggettos) {
    	super();
    	init();
    }
    
    private void init(){
    	this.docfaComunOggettos = this.getDocfaComunOggettos();
    }
	
	
	public void addResidenza(Residenza resid){
		if(residenzas == null){
			residenzas = new TreeSet<Residenza>();
		}
		residenzas.add(resid);
	}
	public Residenza[] getResidenzas(){
		Residenza[] res = null;
		if(residenzas == null){
			res = new Residenza[0];
		}
		else{
			res = new Residenza[residenzas.size()];
			Iterator<Residenza> theIterator = residenzas.iterator();
			int i = 0;
			while (theIterator.hasNext()) {
				res[i] = theIterator.next();
				i++;
			}
		}
		return res;
	}
	public boolean isGiuridico(){
		return ("G".equalsIgnoreCase(String.valueOf(getFlagPersona())) );
	}

	public Set<DocfaComunOggetto> getDocfaComunOggettos()
	{
		return docfaComunOggettos;
	}

	public void setDocfaComunOggettos(Set<DocfaComunOggetto> docfaComunOggettos)
	{
		this.docfaComunOggettos = docfaComunOggettos;
	}

	public String getDataRegistrazione()
	{
		return dataRegistrazione;
	}

	public void setDataRegistrazione(String dataRegistrazione)
	{
		this.dataRegistrazione = dataRegistrazione;
	}
}
