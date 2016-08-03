package it.webred.ct.service.comma336.data.access;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

public class C336ServiceBaseBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	protected static final Logger logger = Logger.getLogger("C336service.log");

	protected Date getCurrentDate(){

		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}

}
