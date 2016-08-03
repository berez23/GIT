package it.webred.amprofiler.ejb.perm;
import java.util.HashMap;
import java.util.List;


import javax.ejb.Remote;

@Remote
public interface LoginBeanService {

	public HashMap<String, String> getPermissions(String username);
	
	public List<String> getEnte(String username);

}
