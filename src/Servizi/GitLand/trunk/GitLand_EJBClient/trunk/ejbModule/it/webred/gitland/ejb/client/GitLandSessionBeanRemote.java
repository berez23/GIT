package it.webred.gitland.ejb.client;

import java.util.Hashtable;
import java.util.List;

import it.webred.gitland.data.model.Azienda;
import it.webred.gitland.data.model.MasterItem;
import it.webred.gitland.ejb.dto.RicercaInfoDTO;

import javax.ejb.Remote;

@Remote
public interface GitLandSessionBeanRemote {

	public Azienda getAziendaById(Long id);
	public void addItem(MasterItem mi);
	public List getList(Hashtable htQry, Class cls);
	public List<Object[]> getList(String strQry, Boolean hql);
	public List getList(String strQry, Hashtable<String, Object> parametri, Boolean hql);
	public MasterItem getItemById(Long id, Class cls);
	public MasterItem updItem(MasterItem mi);
	public Boolean delItemById(Long id, Class cls, Boolean audit);
	public Boolean delItemById(Object id, Class cls);
	public Integer delList(Hashtable htQry, Class cls);

}
