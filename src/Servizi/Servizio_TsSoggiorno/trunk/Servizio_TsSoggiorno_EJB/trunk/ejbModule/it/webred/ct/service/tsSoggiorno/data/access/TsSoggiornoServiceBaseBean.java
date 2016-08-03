package it.webred.ct.service.tsSoggiorno.data.access;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

public class TsSoggiornoServiceBaseBean implements Serializable {
	
	protected Logger logger = Logger.getLogger("tssoggiorno_log");
	
	protected Date getCurrentDate(){

		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}

}
