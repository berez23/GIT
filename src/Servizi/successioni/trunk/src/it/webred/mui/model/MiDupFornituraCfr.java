package it.webred.mui.model;

import it.webred.mui.input.MuiFornituraParser;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MiDupFornituraCfr extends MiDupFornitura implements Comparable<MiDupFornituraCfr> {
	
	private String dataInizialeOrig;
    private Date dataInizialeDateOrig;
    private String dataFinaleOrig;
    private Date dataFinaleDateOrig;
    private int numeroNoteSummaryOrig = -1;
    private boolean fornitura;
    private boolean fornituraCaricata;
    
    private static final String EMPTY_VAL = "-";
	
	/** default constructor */
    public MiDupFornituraCfr() {
    	super();
    }

	/** minimal constructor */
    public MiDupFornituraCfr(MiDupFornitura midupFornitura) {
        super();
        this.setNumeroAnno(midupFornitura.getNumeroAnno());
        this.setDataCaricamento(midupFornitura.getDataCaricamento());
        this.setDataIniziale(midupFornitura.getDataIniziale());
        this.setDataInizialeDate(midupFornitura.getDataInizialeDate());
        this.setDataFinale(midupFornitura.getDataFinale());
        this.setDataFinaleDate(midupFornitura.getDataFinaleDate());
        this.setMiVwNoteSummary(midupFornitura.getMiVwNoteSummary());
        setFornituraCaricata(true);
    }
    
    public int compareTo(MiDupFornituraCfr compare) {
    	//n.b. ordinamento desc
    	if (compare.getDataOrd() == null)
    		return -1;
    	if (this.getDataOrd() == null)
    		return 1;
    	if (compare.getDataOrd().getTime() < this.getDataOrd().getTime())
    		return -1;
    	if (compare.getDataOrd().getTime() > this.getDataOrd().getTime())
    		return 1;
    	return 0;
    }
	
	public int getAnno() {
		return super.getAnno();
	}

	public String getDataInizialeOrig() {
		return dataInizialeOrig;
	}

	public void setDataInizialeOrig(String dataInizialeOrig) {
		this.dataInizialeOrig = dataInizialeOrig;
	}

	public Date getDataInizialeDateOrig() {
		return dataInizialeDateOrig;
	}

	public void setDataInizialeDateOrig(Date dataInizialeDateOrig) {
		this.dataInizialeDateOrig = dataInizialeDateOrig;
	}

	public String getDataFinaleOrig() {
		return dataFinaleOrig;
	}

	public void setDataFinaleOrig(String dataFinaleOrig) {
		this.dataFinaleOrig = dataFinaleOrig;
	}

	public Date getDataFinaleDateOrig() {
		return dataFinaleDateOrig;
	}

	public void setDataFinaleDateOrig(Date dataFinaleDateOrig) {
		this.dataFinaleDateOrig = dataFinaleDateOrig;
	}

	public int getNumeroNoteSummaryOrig() {
		return numeroNoteSummaryOrig;
	}

	public void setNumeroNoteSummaryOrig(int numeroNoteSummaryOrig) {
		this.numeroNoteSummaryOrig = numeroNoteSummaryOrig;
	}
	
	//per ordinamento
	public Date getDataOrd() {
		if (getDataInizialeDate() != null)
			getDataInizialeDate();
		return getDataInizialeDateOrig();
	}	
	
	//metodi get per sola visualizzazione (suffisso Dspl)
		
	public String getDataInizialeOrigDspl() {
		if (getDataInizialeDateOrig() != null) {
			return new SimpleDateFormat("dd/MM/yyyy").format(getDataInizialeDateOrig()); 
		}
		return EMPTY_VAL;
	}
	
	public String getDataFinaleOrigDspl() {
		if (getDataFinaleDateOrig() != null) {
			return new SimpleDateFormat("dd/MM/yyyy").format(getDataFinaleDateOrig()); 
		}
		return EMPTY_VAL;
	}
	
	public String getNoteOrigDspl() {
		if (numeroNoteSummaryOrig > -1) {
			return "" + numeroNoteSummaryOrig;
		}
		return EMPTY_VAL;
	}
	
	public String getNumeroAnnoDspl() {
		if (getNumeroAnno() != -1 && getAnno() != -1) {
			return getNumeroAnno() + "/" + getAnno(); 
		}
		return EMPTY_VAL;
	}
	
	public String getDataCaricamentoDspl() {
		if (getDataCaricamento() != null) {
			return new SimpleDateFormat("HH:mm dd/MM/yyyy").format(getDataCaricamento()); 
		}
		return EMPTY_VAL;
	}
	
	public String getDataInizialeDspl() {
		if (getDataInizialeDate() != null) {
			return new SimpleDateFormat("dd/MM/yyyy").format(getDataInizialeDate()); 
		}
		return EMPTY_VAL;
	}
	
	public String getDataFinaleDspl() {
		if (getDataFinaleDate() != null) {
			return new SimpleDateFormat("dd/MM/yyyy").format(getDataFinaleDate()); 
		}
		return EMPTY_VAL;
	}
	
	public String getNoteDspl() {
		if (getMiVwNoteSummary() != null) {
			return "" + getMiVwNoteSummary().getTotale(); 
		}
		return EMPTY_VAL;
	}
	
	public boolean isFornitura() {
		return fornitura;
	}

	public void setFornitura(boolean fornitura) {
		this.fornitura = fornitura;
	}

	public boolean isFornituraCaricata() {
		return fornituraCaricata;
	}

	public void setFornituraCaricata(boolean fornituraCaricata) {
		this.fornituraCaricata = fornituraCaricata;
	}
    
}
