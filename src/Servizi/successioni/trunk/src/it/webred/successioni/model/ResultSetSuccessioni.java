package it.webred.successioni.model;

public class ResultSetSuccessioni {

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

   private String pk_id_succb;
   private String b_ufficio;
   private String b_anno;
   private String b_volume;
   private String b_numero;
   private String b_sottonumero;
   private String b_comune;
   private String b_progressivo;
   private String b_progressivo_erede;
   private String b_cf_erede;
   private String b_denominazione;
   private String b_sesso;
   private String b_data_nascita;
   private String b_citta_nascita;
   private String b_prov_nascita;
   private String b_citta_residenza;
   private String b_prov_residenza;
   private String b_indirizzo_residenza;
   private String b_tipo_record = "B";
   private String b_fornitura;

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

   
   public String getIdA() {
      return pk_id_succa + "-" + a_progressivo + "-" + a_fornitura;
    }

   /*public String getIdB() {
      return a_pk_id_succa + "-" + a_progressivo + "-" + a_fornitura;
    }*/
   
   /*public void setId(String id) {
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

/**
 * Dati relativi alla tabella MI_SUCCESSIONI_B
 */      
      
      public String getPk_id_succb() {
         return pk_id_succb;
       }
       public void setPk_id_succb(String pk_id_succb) {
         this.pk_id_succb = pk_id_succb;
       }
      
       public String getB_ufficio() {
          return b_ufficio;
       }
       public void setB_ufficio(String b_ufficio) {
          this.b_ufficio = b_ufficio;
       }
       public String getB_anno() {
          return b_anno;
       }
       public void setB_anno(String b_anno) {
          this.b_anno = b_anno;
       }
       public String getB_volume() {
          return b_volume;
       }
       public void setB_volume(String b_volume) {
          this.b_volume = b_volume;
       }
       public String getB_numero() {
          return b_numero;
       }
       public void setB_numero(String b_numero) {
          this.b_numero = b_numero;
       }
       public String getB_sottonumero() {
          return b_sottonumero;
       }
       public void setB_sottonumero(String b_sottonumero) {
          this.b_sottonumero = b_sottonumero;
       }

       public String getB_comune() {
          return b_comune;
       }
       public void setB_comune(String b_comune) {
          this.b_comune = b_comune;
       }

       public String getB_progressivo() {
          return b_progressivo;
       }
       public void setB_progressivo(String b_progressivo) {
          this.b_progressivo = b_progressivo;
       }

       public String getB_progressivo_erede() {
          return b_progressivo_erede;
       }
       public void setB_progressivo_erede(String b_progressivo_erede) {
          this.b_progressivo_erede = b_progressivo_erede;
       }

       public String getB_fornitura() {
          return b_fornitura;
       }
       public void setB_fornitura(String b_fornitura) {
          this.b_fornitura = b_fornitura;
       }

       public String getB_denominazione() {
          return b_denominazione;
       }
       public void setB_denominazione(String b_denominazione) {
          this.b_denominazione = b_denominazione;
       }

       public String getB_sesso() {
          return b_sesso;
       }
       public void setB_sesso(String b_sesso) {
          this.b_sesso = b_sesso;
       }

       public String getB_data_nascita() {
          return b_data_nascita;
       }
       public void setB_data_nascita(String b_data_nascita) {
          this.b_data_nascita = b_data_nascita;
       }

       public String getB_citta_nascita() {
         return b_citta_nascita;
      }
      public void setB_citta_nascita(String b_citta_nascita) {
         this.b_citta_nascita = b_citta_nascita;
      }

      public String getB_prov_nascita() {
         return b_prov_nascita;
      }
      public void setB_prov_nascita(String b_prov_nascita) {
         this.b_prov_nascita = b_prov_nascita;
      }

      public String getB_cf_erede() {
         return b_cf_erede;
      }
      public void setB_cf_erede(String b_cf_erede) {
         this.b_cf_erede = b_cf_erede;
      }
  
      public String getB_citta_residenza() {
         return b_citta_residenza;
      }
      public void setB_citta_residenza(String b_citta_residenza) {
         this.b_citta_residenza = b_citta_residenza;
      }

      public String getB_prov_residenza() {
         return b_prov_residenza;
      }
      public void setB_prov_residenza(String b_prov_residenza) {
         this.b_prov_residenza = b_prov_residenza;
      }

      public String getB_indirizzo_residenza() {
         return b_indirizzo_residenza;
      }
      public void setB_indirizzo_residenza(String b_indirizzo_residenza) {
         this.b_indirizzo_residenza = b_indirizzo_residenza;
      }

      public String getB_tipo_record() {
         return b_tipo_record;
      }
      public void setB_tipo_record(String b_tipo_record) {
         this.b_tipo_record = b_tipo_record;
      }
      
      /**
       * Dati relativi alla tabella MI_SUCCESSIONI_C
       */      
            
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
      
      /**
       * Dati relativi alla tabella MI_SUCCESSIONI_D
       */      

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
