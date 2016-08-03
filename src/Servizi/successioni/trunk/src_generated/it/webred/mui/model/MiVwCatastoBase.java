package it.webred.mui.model;
// Generated 1-apr-2009 13.23.58 by Hibernate Tools 3.1.0.beta4

import java.util.Date;


/**
 * MiVwCatastoBase generated by hbm2java
 */

public class MiVwCatastoBase  implements java.io.Serializable {


    // Fields    

     private String foglio;
     private String particella;
     private String subalterno;
     private Date dataInizio;
     private Date dataFine;
     private String codNazionale;


    // Constructors

    /** default constructor */
    public MiVwCatastoBase() {
    }

    
    /** full constructor */
    public MiVwCatastoBase(String foglio, String particella, String subalterno, Date dataInizio, Date dataFine, String codNazionale) {
        this.foglio = foglio;
        this.particella = particella;
        this.subalterno = subalterno;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.codNazionale = codNazionale;
    }
    

   
    // Property accessors

    public String getFoglio() {
        return this.foglio;
    }
    
    public void setFoglio(String foglio) {
        this.foglio = foglio;
    }

    public String getParticella() {
        return this.particella;
    }
    
    public void setParticella(String particella) {
        this.particella = particella;
    }

    public String getSubalterno() {
        return this.subalterno;
    }
    
    public void setSubalterno(String subalterno) {
        this.subalterno = subalterno;
    }

    public Date getDataInizio() {
        return this.dataInizio;
    }
    
    public void setDataInizio(Date dataInizio) {
        this.dataInizio = dataInizio;
    }

    public Date getDataFine() {
        return this.dataFine;
    }
    
    public void setDataFine(Date dataFine) {
        this.dataFine = dataFine;
    }

    public String getCodNazionale() {
        return this.codNazionale;
    }
    
    public void setCodNazionale(String codNazionale) {
        this.codNazionale = codNazionale;
    }
   








}