package it.webred.ct.service.comma340.data.access;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

public class C340ServiceBaseBean implements Serializable {
	
	protected Logger logger = Logger.getLogger("C340service_log");
	
	protected Date getCurrentDate(){

		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}

}
