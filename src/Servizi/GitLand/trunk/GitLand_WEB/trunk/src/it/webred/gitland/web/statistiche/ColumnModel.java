package it.webred.gitland.web.statistiche;

import java.io.Serializable;

public class ColumnModel implements Serializable {
	 
    /**
	 *  Modello delle colonne costruite dinamicamente per la visualizzazione dei risultati
	 */
	private static final long serialVersionUID = -1785148695991192888L;
	private String header;
    private String property;

    public ColumnModel(String header, String property) {
        this.header = header;
        this.property = property;
    }

    public String getHeader() {
        return header;
    }

    public String getProperty() {
        return property;
    }
}