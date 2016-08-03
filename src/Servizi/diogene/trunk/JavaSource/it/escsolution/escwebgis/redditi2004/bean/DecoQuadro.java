package it.escsolution.escwebgis.redditi2004.bean;

public class DecoQuadro {
   private String codRiga;
   private String tipoModello;
   private String note;
   private String quadro;
   private String descrizione;
   private String valore;
   
   
   


	public DecoQuadro(String codRiga,String tipoModello,String note
			   ,String quadro,String descrizione, String valore) {
		   
		   this.setCodRiga(codRiga);
		   this.setDescrizione(descrizione);
		   this.setNote(note);
		   this.setQuadro(quadro);
		   this.setTipoModello(tipoModello);
		   this.setValore(valore);
	   }

		public String getCodRiga() {
			return codRiga;
		}
		public void setCodRiga(String codRiga) {
			this.codRiga = codRiga;
		}
		public String getTipoModello() {
			return tipoModello;
		}
		public void setTipoModello(String tipoModello) {
			this.tipoModello = tipoModello;
		}
		public String getNote() {
			return note;
		}
		public void setNote(String note) {
			this.note = note;
		}
		public String getQuadro() {
			return quadro;
		}
		public void setQuadro(String quadro) {
			this.quadro = quadro;
		}
		public String getDescrizione() {
			return descrizione;
		}
		public void setDescrizione(String descrizione) {
			this.descrizione = descrizione;
		}
		public String getValore() {
				return valore;
		}
			
		public void setValore(String valore) {
				this.valore = valore;
		}   
}
