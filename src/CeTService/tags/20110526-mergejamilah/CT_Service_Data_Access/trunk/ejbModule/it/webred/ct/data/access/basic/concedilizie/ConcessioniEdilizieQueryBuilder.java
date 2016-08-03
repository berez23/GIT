package it.webred.ct.data.access.basic.concedilizie;

public class ConcessioniEdilizieQueryBuilder {
	private final String SQL_SOGGETTI_CONCESSIONE = 
		"SELECT CP.TITOLO, TIPO_PERSONA, COGNOME, NOME, DENOMINAZIONE " +
	      "FROM SIT_C_CONC_PERSONA CP, SIT_C_PERSONA P  " +
	     "WHERE CP.ID_EXT_C_PERSONA = P.ID_EXT (+) " +
	       "AND CP.ID_EXT_C_CONCESSIONI = ? " +// -->ID_EXT DI SIT_ C_CONCESSIONI
	       "AND (CP.DT_FINE_VAL IS NULL OR CP.DT_FINE_VAL >= ?) " +
	       "AND (P.DT_FINE_VAL IS NULL OR P.DT_FINE_VAL >= ?)";

	private final String SQL_OGGETTI_CONCESSIONE = 
		"SELECT SEZIONE, FOGLIO, PARTICELLA,SUBALTERNO " +
	      "FROM SIT_C_CONCESSIONI C , SIT_C_CONCESSIONI_CATASTO CC " +
	     "WHERE CC.ID_EXT_C_CONCESSIONI = C.ID_EXT " +
	       "AND C.ID_EXT=?" +
	       "AND (CC.DT_FINE_VAL IS NULL OR CC.DT_FINE_VAL >= ?) " +
	       "AND (C.DT_FINE_VAL IS NULL OR C.DT_FINE_VAL >= ?)";
	public String getSQL_OGGETTI_CONCESSIONE() {
		return SQL_OGGETTI_CONCESSIONE;
	}

	public String getSQL_SOGGETTI_CONCESSIONE() {
		return SQL_SOGGETTI_CONCESSIONE;
	}
	
}
