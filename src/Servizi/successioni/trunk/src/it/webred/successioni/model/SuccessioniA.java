package it.webred.successioni.model;

import java.util.ArrayList;
import java.util.Collection;

public class SuccessioniA {

   //private String id;
   private String pk_id_succa;
   private String a_ufficio;
   private String a_anno;
   private String a_volume;
   private String a_numero;
   private String a_sottonumero;
   private String a_comune;
   private String a_progressivo;
   private String a_tipo_dichiarazione;
   private String a_data_apertura;
   private String a_cf_defunto;
   private String a_cognome_defunto;
   private String a_nome_defunto;
   private String a_sesso;
   private String a_citta_nascita;
   private String a_prov_nascita;
   private String a_data_nascita;
   private String a_citta_residenza;
   private String a_prov_residenza;
   private String a_indirizzo_residenza;
   private String a_tipo_record = "A";
   private String a_fornitura;

   private Collection listSuccessioniB = new ArrayList();
   private Collection listSuccessioniC = new ArrayList();
   private Collection listSuccessioniD = new ArrayList();
      
   /*public String getId() {
      return pk_id_succa + "-" + a_progressivo + "-" + a_fornitura;
    }
    public void setId(String id) {
      this.id = id;
    }*/

    public String getPk_id_succa() {
      return pk_id_succa;
    }
    public void setPk_id_succa(String pk_id_succa) {
      this.pk_id_succa = pk_id_succa;
    }
   
    public String getA_progressivo() {
       return a_progressivo;
     }
     public void setA_progressivo(String a_progressivo) {
       this.a_progressivo = a_progressivo;
     }
    
     public String getA_fornitura() {
        return a_fornitura;
      }
      public void setA_fornitura(String a_fornitura) {
        this.a_fornitura = a_fornitura;
      }
     
      public String getA_tipo_dichiarazione() {
         return a_tipo_dichiarazione;
      }
      public void setA_tipo_dichiarazione(String a_tipo_dichiarazione) {
         this.a_tipo_dichiarazione = a_tipo_dichiarazione;
      }
      public String getA_ufficio() {
         return a_ufficio;
      }
      public void setA_ufficio(String a_ufficio) {
         this.a_ufficio = a_ufficio;
      }
      public String getA_anno() {
         return a_anno;
      }
      public void setA_anno(String a_anno) {
         this.a_anno = a_anno;
      }
      public String getA_volume() {
         return a_volume;
      }
      public void setA_volume(String a_volume) {
         this.a_volume = a_volume;
      }
      public String getA_numero() {
         return a_numero;
      }
      public void setA_numero(String a_numero) {
         this.a_numero = a_numero;
      }
      public String getA_sottonumero() {
         return a_sottonumero;
      }
      public void setA_sottonumero(String a_sottonumero) {
         this.a_sottonumero = a_sottonumero;
      }

      public String getA_comune() {
         return a_comune;
      }
      public void setA_comune(String a_comune) {
         this.a_comune = a_comune;
      }

      public String getA_data_apertura() {
         return a_data_apertura;
      }
      public void setA_data_apertura(String a_data_apertura) {
         this.a_data_apertura = a_data_apertura;
      }
      public String getA_cognome_defunto() {
         return a_cognome_defunto;
      }
      public void setA_cognome_defunto(String a_cognome_defunto) {
         this.a_cognome_defunto = a_cognome_defunto;
      }

      public String getA_nome_defunto() {
         return a_nome_defunto;
      }
      public void setA_nome_defunto(String a_nome_defunto) {
         this.a_nome_defunto = a_nome_defunto;
      }

      public String getA_sesso() {
         return a_sesso;
      }
      public void setA_sesso(String a_sesso) {
         this.a_sesso = a_sesso;
      }

      public String getA_data_nascita() {
         return a_data_nascita;
      }
      public void setA_data_nascita(String a_data_nascita) {
         this.a_data_nascita = a_data_nascita;
      }

      public String getA_citta_nascita() {
         return a_citta_nascita;
      }
      public void setA_citta_nascita(String a_citta_nascita) {
         this.a_citta_nascita = a_citta_nascita;
      }

      public String getA_prov_nascita() {
         return a_prov_nascita;
      }
      public void setA_prov_nascita(String a_prov_nascita) {
         this.a_prov_nascita = a_prov_nascita;
      }

      public String getA_cf_defunto() {
         return a_cf_defunto;
      }
      public void setA_cf_defunto(String a_cf_defunto) {
         this.a_cf_defunto = a_cf_defunto;
      }

      public String getA_citta_residenza() {
         return a_citta_residenza;
      }
      public void setA_citta_residenza(String a_citta_residenza) {
         this.a_citta_residenza = a_citta_residenza;
      }

      public String getA_prov_residenza() {
         return a_prov_residenza;
      }
      public void setA_prov_residenza(String a_prov_residenza) {
         this.a_prov_residenza = a_prov_residenza;
      }

      public String getA_indirizzo_residenza() {
         return a_indirizzo_residenza;
      }
      public void setA_indirizzo_residenza(String a_indirizzo_residenza) {
         this.a_indirizzo_residenza = a_indirizzo_residenza;
      }

      public String getA_tipo_record() {
         return a_tipo_record;
      }
      public void setA_tipo_record(String a_tipo_record) {
         this.a_tipo_record = a_tipo_record;
      }
      
      
      
/************************************************************************************************
      // Elaborazione Liste varie
************************************************************************************************/
      public Collection getListSuccessioniB() { return listSuccessioniB;}
      public void setListSuccessioniB(Collection c) { this.listSuccessioniB = c;}
      public void addSuccessioniB( SuccessioniB successioniB )
      {
         listSuccessioniB.add( successioniB );
      }
  
      public Collection getListSuccessioniC() { return listSuccessioniC;}
      public void setListSuccessioniC(Collection c) { this.listSuccessioniC = c;}
      public void addSuccessioniC( SuccessioniC successioniC )
      {
         listSuccessioniC.add( successioniC );
      }
  
      public Collection getListSuccessioniD() { return listSuccessioniD;}
      public void setListSuccessioniD(Collection c) { this.listSuccessioniD = c;}
      public void addSuccessioniD( SuccessioniD successioniD )
      {
         listSuccessioniD.add( successioniD );
      }
}
