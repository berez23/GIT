package it.webred.fb.data;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;

public class DataModelCostanti {

	public static final String NOME_APPLICAZIONE = "FascicoloBene";
	public static final Date END_DATE = new GregorianCalendar(9999, 11, 31).getTime();
	public static final String END_DATE_STRING = "31/12/9999";
	
	public static class Segnalibri implements Serializable {

		private static final long serialVersionUID = 1L;
		
		public static class TipoAlert
		{
			public static final String WARNING = "warning";
			public static final String NOT_VALIDATE = "notValidated";
			public static final String VALIDATE = "validated";
			public static final String NOT_ENOUGH_DATA = "notEnoughData";
			
			public static final String TERR_NOT_VALIDATE =  "terrNotValidated";
			public static final String IMM_NOT_VALIDATE =  "immNotValidated";
			public static final String TERR_VALIDATE = "terrValidated";
			public static final String IMM_VALIDATE = "immValidated";
			public static final String INV_VALIDATE = "invValidated";
		}
	}	
}