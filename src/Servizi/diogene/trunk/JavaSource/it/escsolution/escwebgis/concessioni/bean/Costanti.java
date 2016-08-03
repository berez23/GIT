package it.escsolution.escwebgis.concessioni.bean;

import java.util.Hashtable;

public class Costanti {

	public final static Hashtable<String, String> htTipiAtti = new Hashtable<String, String>();
	public final static Hashtable<String, String> htDestinazioni = new Hashtable<String, String>();
	public final static Hashtable<String, String> htTipologie = new Hashtable<String, String>();

	static {
		htTipiAtti.put("CE", "Concessioni Edilizie");
		htTipiAtti.put("CEO", "Concessioni a scomputo Oneri");
		htTipiAtti.put("CES", "Concessioni a Sanatoria");
		htTipiAtti.put("CESP", "Concessioni a parziale Sanatoria");
		htTipiAtti.put("CEV", "Concessione a Variante");
		htTipiAtti.put("CEX", "Voltura");
		htTipiAtti.put("AE", "Autorizzazioni Edilizie");
		htTipiAtti.put("LA", "Licenze di Abitabilità");
		htTipiAtti.put("DM", "Demolizioni");
		htTipiAtti.put("DE", "Atti Densita Edilizia");
		htTipiAtti.put("AP", "Autorizzazioni Paesistiche");
		htTipiAtti.put("DA", "Assenza Danno Ambientale");
		htTipiAtti.put("DIA", "DIA ex LR 22/99");
		htTipiAtti.put("SDIA", "DIA ex L 662/99");
		htTipiAtti.put("CO", "Condono Ordinario");
		htTipiAtti.put("CS", "Condono Straordinario");
		
		htDestinazioni.put("RES", "Residenziale");
		htDestinazioni.put("IND", "Industriale");
		htDestinazioni.put("COM", "Commerciale-Industriale");
		htDestinazioni.put("MIX", "Mista");
		htDestinazioni.put("AGR", "Agricola");
		htDestinazioni.put("REL", "Attrezzature Religiose");
		htDestinazioni.put("HCC", "Ospedali-Case di Cura");
		htDestinazioni.put("SCO", "Edilizia Scolastica");
		htDestinazioni.put("ESA", "Edilizia Socio Assistenziale");

		htTipologie.put("BOX", "Box");
		htTipologie.put("ANT", "Antenna");
		htTipologie.put("STT", "Sottotetto");
		htTipologie.put("RIS", "Ristrutturazione");
		htTipologie.put("NEO", "Nuova Edificazione");
		htTipologie.put("MST", "Manutenzione Straordinaria");
		htTipologie.put("RCS", "Risanamento Conservativo");
		htTipologie.put("RES", "Restauro");

	}//------------------------------------------------------------------------

}
