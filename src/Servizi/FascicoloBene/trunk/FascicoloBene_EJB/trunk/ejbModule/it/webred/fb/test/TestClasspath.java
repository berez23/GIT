package it.webred.fb.test;

import static org.junit.Assert.*;

import java.util.List;

import it.webred.ct.support.datarouter.CeTBaseObject;
import it.webred.ejb.utility.ClientUtility;
import it.webred.fb.data.model.DmBBene;
import it.webred.fb.ejb.client.DettaglioBeneSessionBeanRemote;
import it.webred.fb.ejb.dto.BaseDTO;

import javax.naming.NamingException;

import org.junit.Test;

public class TestClasspath {

	
	@Test
	public void testGetDettaglio() {
	    try {
	    	DettaglioBeneSessionBeanRemote bean = lookupRemoteEJB();
	    	BaseDTO b = new BaseDTO();
	    	b.setEnteId("F704");
	    	b.setObj(new Long(515739));
	    	try {
				DmBBene bene =  bean.getDettaglioBeneById(b);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
				
				
		} catch (NamingException e) {
			fail(e.getMessage());
		}
	}
	
	
	
    private static DettaglioBeneSessionBeanRemote lookupRemoteEJB() throws NamingException {

    	
    	DettaglioBeneSessionBeanRemote ejb = ClientUtility.getEJBInterfaceForRemoteClient(DettaglioBeneSessionBeanRemote.class , 
    			"FascicoloBene", "FascicoloBene_EJB", "DettaglioBeneSessionBean", "");
    	
    	return ejb;
    	 
    }
	    		


}
