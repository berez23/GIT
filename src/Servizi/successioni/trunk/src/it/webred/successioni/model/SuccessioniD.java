package it.webred.successioni.model;

public class SuccessioniD {

   private String d_ufficio;
   private String d_anno;
   private String d_volume;
   private String d_numero;
   private String d_sottonumero;
   private String d_comune;
   private String d_progressivo;
   private String d_fornitura;
   private String d_progressivo_immobile;
   private String d_progressivo_erede;
   private String d_numeratore_quota;
   private String d_denominatore_quota;
   private String d_agevolazione_1_casa;
   private String d_tipo_record;

   public String getId_succd() {
      return d_ufficio + "-" 
      + d_anno + "-" 
      + d_volume + "-" 
      + d_numero + "-" 
      + d_sottonumero + "-" 
      + d_comune + "-" 
      + d_progressivo + "-"
      + d_progressivo_immobile + "-"
      + d_progressivo_erede + "-"
      + d_fornitura;
   }
   public String getD_ufficio() {
      return d_ufficio;
   }
   public void setD_ufficio(String d_ufficio) {
      this.d_ufficio = d_ufficio;
   }
   public String getD_anno() {
      return d_anno;
   }
   public void setD_anno(String d_anno) {
      this.d_anno = d_anno;
   }
   public String getD_volume() {
      return d_volume;
   }
   public void setD_volume(String d_volume) {
      this.d_volume = d_volume;
   }
   public String getD_numero() {
      return d_numero;
   }
   public void setD_numero(String d_numero) {
      this.d_numero = d_numero;
   }
   public String getD_sottonumero() {
      return d_sottonumero;
   }
   public void setD_sottonumero(String d_sottonumero) {
      this.d_sottonumero = d_sottonumero;
   }

   public String getD_comune() {
      return d_comune;
   }
   public void setD_comune(String d_comune) {
      this.d_comune = d_comune;
   }

   public String getD_progressivo() {
      return d_progressivo;
   }
   public void setD_progressivo(String d_progressivo) {
      this.d_progressivo = d_progressivo;
   }

   public String getD_progressivo_immobile() {
      return d_progressivo_immobile;
   }
   public void setD_progressivo_immobile(String d_progressivo_immobile) {
      this.d_progressivo_immobile = d_progressivo_immobile;
   }

   public String getD_progressivo_erede() {
      return d_progressivo_erede;
   }
   public void setD_progressivo_erede(String d_progressivo_erede) {
      this.d_progressivo_erede = d_progressivo_erede;
   }

   public String getD_fornitura() {
      return d_fornitura;
   }
   public void setD_fornitura(String d_fornitura) {
      this.d_fornitura = d_fornitura;
   }

   public String getD_numeratore_quota() {
      return d_numeratore_quota;
   }
   public void setD_numeratore_quota(String d_numeratore_quota) {
      this.d_numeratore_quota = d_numeratore_quota;
   }

   public String getD_denominatore_quota() {
      return d_denominatore_quota;
   }
   public void setD_denominatore_quota(String d_denominatore_quota) {
      this.d_denominatore_quota = d_denominatore_quota;
   }

   public String getD_agevolazione_1_casa() {
      return d_agevolazione_1_casa;
   }
   public void setD_agevolazione_1_casa(String d_agevolazione_1_casa) {
      this.d_agevolazione_1_casa = d_agevolazione_1_casa;
   }

   public String getD_tipo_record() {
      return d_tipo_record;
   }
   public void setD_tipo_record(String d_tipo_record) {
      this.d_tipo_record = d_tipo_record;
   }


}
