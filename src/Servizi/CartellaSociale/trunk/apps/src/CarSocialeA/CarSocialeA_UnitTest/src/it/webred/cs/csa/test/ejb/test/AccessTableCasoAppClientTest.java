package it.webred.cs.csa.test.ejb.test;

import static org.junit.Assert.*;

import java.util.List;






import it.webred.amprofiler.ejb.user.UserService;
import it.webred.amprofiler.model.AmUser;
import it.webred.cs.csa.ejb.client.AccessTableCasoSessionBeanRemote;
import it.webred.cs.data.model.CsACaso;
import it.webred.ct.support.datarouter.CeTBaseObject;
import it.webred.ejb.utility.ClientUtility;

import javax.naming.NamingException;

import org.junit.Test;

public class AccessTableCasoAppClientTest {

	
	@Test
	public void testGetTableData() {
	    try {
	    	UserService bean = lookupRemoteEJB();
	    	
	    	List<AmUser> utenti = bean.getUsersByEnteInizialiGruppo("L872", "SSOCIALE");
	    	
				CeTBaseObject dataIn = new CeTBaseObject();
				dataIn.setEnteId("F704");
				
		//		CsACaso caso = bean.findCasoById(1);
				
				
				
		} catch (NamingException e) {
			fail(e.getMessage());
		}
	}
	
	
	
    private static UserService lookupRemoteEJB() throws NamingException {

    	
    	UserService ejb = ClientUtility.getEJBInterfaceForRemoteClient(UserService.class , 
    			"AmProfiler", "AmProfilerEjb", "UserServiceBean", "");
    	
    	return ejb;
    	 
    }
	    		


}
