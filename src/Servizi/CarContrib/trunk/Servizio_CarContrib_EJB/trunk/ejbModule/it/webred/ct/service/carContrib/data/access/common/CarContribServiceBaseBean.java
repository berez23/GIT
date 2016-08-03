package it.webred.ct.service.carContrib.data.access.common;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

public class CarContribServiceBaseBean implements Serializable {
	
	
	
	protected Logger logger = Logger.getLogger("carcontrib.log");
	
	protected Date getCurrentDate(){

		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}

}
