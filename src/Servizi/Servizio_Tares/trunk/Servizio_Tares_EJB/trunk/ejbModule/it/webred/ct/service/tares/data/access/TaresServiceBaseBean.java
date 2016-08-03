package it.webred.ct.service.tares.data.access;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

public class TaresServiceBaseBean implements Serializable {
	
	protected Logger logger = Logger.getLogger("tares.log");
	
	protected Date getCurrentDate(){

		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}

}
