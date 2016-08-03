package it.webred.ct.service.carContrib.data.access.cc;

public class CarContribException extends RuntimeException {
	public CarContribException ()  {
		
	}
	public CarContribException (String msg) {
		super(msg);		
		
	}
	public CarContribException (Throwable t) {
		super(t);		
			
	}
}
