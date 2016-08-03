package it.webred.successioni.model;

public class SuccessioniC {

   private String c_ufficio;
   private String c_anno;
   private String c_volume;
   private String c_numero;
   private String c_sottonumero;
   private String c_comune;
   private String c_progressivo;
   private String c_progressivo_immobile;
   private String c_fornitura;
   private String c_numeratore_quota_def;
   private String c_denominatore_quota_def;
   private String c_diritto;
   private String c_catasto;
   private String c_tipo_dati;
   private String c_foglio;
   private String c_particella1;
   private String c_particella2;
   private String c_subalterno1;
   private String c_subalterno2;
   private String c_denuncia1;
   private String c_anno_denuncia;
   private String c_natura;
   private String c_superficie_ettari;
   private String c_superficie_mq;
   private String c_vani;
   private String c_indirizzo_immobile;

   public String getId_succc() {
      return c_ufficio + "-" 
           + c_anno + "-" 
           + c_volume + "-" 
           + c_numero + "-" 
           + c_sottonumero + "-" 
           + c_comune + "-" 
           + c_progressivo + "-"
           + c_fornitura;
   }
   public String getC_ufficio() {
      return c_ufficio;
   }
   public void setC_ufficio(String c_ufficio) {
      this.c_ufficio = c_ufficio;
   }
   public String getC_anno() {
      return c_anno;
   }
   public void setC_anno(String c_anno) {
      this.c_anno = c_anno;
   }
   public String getC_volume() {
      return c_volume;
   }
   public void setC_volume(String c_volume) {
      this.c_volume = c_volume;
   }
   public String getC_numero() {
      return c_numero;
   }
   public void setC_numero(String c_numero) {
      this.c_numero = c_numero;
   }
   public String getC_sottonumero() {
      return c_sottonumero;
   }
   public void setC_sottonumero(String c_sottonumero) {
      this.c_sottonumero = c_sottonumero;
   }

   public String getC_comune() {
      return c_comune;
   }
   public void setC_comune(String c_comune) {
      this.c_comune = c_comune;
   }

   public String getC_progressivo() {
      return c_progressivo;
   }
   public void setC_progressivo(String c_progressivo) {
      this.c_progressivo = c_progressivo;
   }

   public String getC_progressivo_immobile() {
      return c_progressivo_immobile;
   }
   public void setC_progressivo_immobile(String c_progressivo_immobile) {
      this.c_progressivo_immobile = c_progressivo_immobile;
   }

   public String getC_fornitura() {
      return c_fornitura;
   }
   public void setC_fornitura(String c_fornitura) {
      this.c_fornitura = c_fornitura;
   }

   public String getC_numeratore_quota_def() {
      return c_numeratore_quota_def;
   }
   public void setC_numeratore_quota_def(String c_numeratore_quota_def) {
      this.c_numeratore_quota_def = c_numeratore_quota_def;
   }
   public String getC_denominatore_quota_def() {
      return c_denominatore_quota_def;
   }
   public void setC_denominatore_quota_def(String c_denominatore_quota_def) {
      this.c_denominatore_quota_def = c_denominatore_quota_def;
   }
   public String getC_diritto() {
      return c_diritto;
   }
   public void setC_diritto(String c_diritto) {
      this.c_diritto = c_diritto;
   }
   public String getC_catasto() {
      return c_catasto;
   }
   public void setC_catasto(String c_catasto) {
      this.c_catasto = c_catasto;
   }
   public String getC_tipo_dati() {
      return c_tipo_dati;
   }
   public void setC_tipo_dati(String c_tipo_dati) {
      this.c_tipo_dati = c_tipo_dati;
   }
   public String getC_foglio() {
      return c_foglio;
   }
   public void setC_foglio(String c_foglio) {
      this.c_foglio = c_foglio;
   }
   public String getC_particella1() {
      return c_particella1;
   }
   public void setC_particella1(String c_particella1) {
      this.c_particella1 = c_particella1;
   }
   public String getC_particella2() {
      return c_particella2;
   }
   public void setC_particella2(String c_particella2) {
      this.c_particella2 = c_particella2;
   }
   public String getC_subalterno1() {
      return c_subalterno1;
   }
   public void setC_subalterno1(String c_subalterno1) {
      this.c_subalterno1 = c_subalterno1;
   }
   public String getC_subalterno2() {
      return c_subalterno2;
   }
   public void setC_subalterno2(String c_subalterno2) {
      this.c_subalterno2 = c_subalterno2;
   }
   
   public String getC_denuncia1() {
      return c_denuncia1;
   }
   public void setC_denuncia1(String c_denuncia1) {
      this.c_denuncia1 = c_denuncia1;
   }

   public String getC_anno_denuncia() {
      return c_anno_denuncia;
   }
   public void setC_anno_denuncia(String c_anno_denuncia) {
      this.c_anno_denuncia = c_anno_denuncia;
   }
   public String getC_natura() {
      return c_natura;
   }
   public void setC_natura(String c_natura) {
      this.c_natura = c_natura;
   }
   public String getC_superficie_ettari() {
      return c_superficie_ettari;
   }
   public void setC_superficie_ettari(String c_superficie_ettari) {
      this.c_superficie_ettari = c_superficie_ettari;
   }
   public String getC_superficie_mq() {
      return c_superficie_mq;
   }
   public void setC_superficie_mq(String c_superficie_mq) {
      this.c_superficie_mq = c_superficie_mq;
   }
   public String getC_vani() {
      return c_vani;
   }
   public void setC_vani(String c_vani) {
      this.c_vani = c_vani;
   }
   public String getC_indirizzo_immobile() {
      return c_indirizzo_immobile;
   }
   public void setC_indirizzo_immobile(String c_indirizzo_immobile) {
      this.c_indirizzo_immobile = c_indirizzo_immobile;
   }
}
