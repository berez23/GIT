package it.webred.cs.ejb.sample.test;

import static org.junit.Assert.*;
import it.webred.cs.ejb.sample.HelloWorldSessionBeanRemote;
import it.webred.ejb.utility.ClientUtility;

import org.junit.*;
import org.junit.experimental.theories.suppliers.TestedOn;

import javax.naming.NamingException;


public class HelloWorldSessionBeanApplicationClient {

	
    @Test
    public void ciaoMondo() {
	    try {
			HelloWorldSessionBeanRemote bean = lookupRemoteEJB();
			System.out.println(bean.ciaoMondo()); 
			assertEquals(bean.ciaoMondo(), "Hello World!");   
			
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
 
    private static HelloWorldSessionBeanRemote lookupRemoteEJB() throws NamingException {

    	
    	HelloWorldSessionBeanRemote ejb = ClientUtility.getEJBInterfaceForRemoteClient(HelloWorldSessionBeanRemote.class , "CartellaSocialeA", "CartellaSocialeA_EJB", "HelloWorldSessionBean", "");
    	
    	return ejb;
    	 
    }
    
 
 
}