package it.webred.mui.model;

public class MiVwCatasto extends MiVwCatastoBase {
    public boolean equals(Object other) {
        if (this == other) return true;
        if ( !(other instanceof MiVwCatasto) ) return false;

        final MiVwCatasto cat = (MiVwCatasto) other;

        if ( !cat.getFoglio().equals( getFoglio() ) ) return false;
        if ( !cat.getParticella().equals( getParticella() ) ) return false;
        if ( !cat.getSubalterno().equals( getSubalterno() ) ) return false;
        if ( !cat.getDataFine().equals( getDataFine() ) ) return false;
        if ( !cat.getDataInizio().equals( getDataInizio() ) ) return false;
        if ( !cat.getCodNazionale().equals( getCodNazionale() ) ) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = getFoglio().hashCode();
        result = 4 * result + getParticella().hashCode();
        result = 4 * result + getSubalterno().hashCode();
        result = 4 * result + getDataInizio().hashCode();
        result = 4 * result + getDataFine().hashCode();
        result = 4 * result + getCodNazionale().hashCode();
        return result;
    }
    
    public MiVwCatastoPk getIid() {
    	return new MiVwCatastoPk(getFoglio(),getParticella(),getSubalterno(),getDataInizio(),getDataFine(),getCodNazionale());
    }
    public void setIid(MiVwCatastoPk cpk) {
    	this.setFoglio(cpk.getFoglio());
    	this.setParticella(cpk.getParticella());
    	this.setSubalterno(cpk.getSubalterno());
    	this.setDataInizio(cpk.getDataInizio());
    	this.setDataFine(cpk.getDataFine());
    	this.setCodNazionale(cpk.getCodNazionale());
    }
}
