package it.webred.mui.model;
// Generated 15-lug-2008 10.19.14 by Hibernate Tools 3.1.0.beta4



/**
 * CodiciDup generated by hbm2java
 */

public class CodiciDup  implements java.io.Serializable {


    // Fields    

     private String codice;
     private String descrizione;


    // Constructors

    /** default constructor */
    public CodiciDup() {
    }

	/** minimal constructor */
    public CodiciDup(String codice) {
        this.codice = codice;
    }
    
    /** full constructor */
    public CodiciDup(String codice, String descrizione) {
        this.codice = codice;
        this.descrizione = descrizione;
    }
    

   
    // Property accessors

    public String getCodice() {
        return this.codice;
    }
    
    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getDescrizione() {
        return this.descrizione;
    }
    
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
   








}