package it.webred.amprofiler.ejb.perm;


import it.webred.amprofiler.model.perm.PermEnte;
import it.webred.amprofiler.model.perm.Permission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Session Bean implementation class LoginBean
 */
@Stateless
public class LoginBean implements LoginBeanService {

	@PersistenceContext(unitName = "AmProfilerDataModel")
	protected EntityManager manager;

	public HashMap getPermissions(String username) {
		HashMap<String, String> result = new HashMap<String, String>();
		
		try {
			//System.out.println("Get Permission");
			Query q = manager.createNamedQuery("Login.getPermission");
			//System.out.println("Query q["+q+"]");
			q.setParameter("username", username);
			
			List<Permission> tmp = q.getResultList();
			
			
			if (tmp != null) {
				for (int i=0; i < tmp.size(); i++) {
					Permission p = tmp.get(i);
					result.put(p.getPermission(), p.getPermission());
				}
			}
		}
		catch(Throwable t) {
			throw new LoginServiceException(t);
		}
		
		return result;
	}

	public List getEnte(String username) {
		List<String> result = new ArrayList<String>();
		
		try {
			Query q = manager.createNamedQuery("Login.getEnte");
			q.setParameter("username", username);
			List<PermEnte> tmp = q.getResultList();
			if (tmp != null) {
				for (int i=0; i < tmp.size(); i++) {
					PermEnte ep = tmp.get(i);
					result.add(ep.getEnte());
				}
			}
		}
		catch(Throwable t) {
			throw new LoginServiceException(t);
		}
		
		return result;
	}


}
