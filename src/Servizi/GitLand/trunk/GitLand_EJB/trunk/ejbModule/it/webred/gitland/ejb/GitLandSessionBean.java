package it.webred.gitland.ejb;

import java.util.Hashtable;
import java.util.List;

import it.webred.gitland.dao.GitLandDAO;
import it.webred.gitland.data.model.Azienda;
import it.webred.gitland.data.model.Comune;
import it.webred.gitland.data.model.MasterItem;
import it.webred.gitland.ejb.client.GitLandSessionBeanRemote;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;


@Stateless
public class GitLandSessionBean implements GitLandSessionBeanRemote  {
	
	@Autowired
	private GitLandDAO gitLandDao;
	
	@Override
	public Azienda getAziendaById(Long id) {

		System.out.println("_____ *_* SONO DENTRO COMPONENTE EJB *_* _____");
		//qui verranno preparate le chiamate per il/i DAO e gestiti i dati ottenuti 
		//GlAziende fAzienda = (GlAziende)dto.getObj();
		Azienda az = (Azienda)gitLandDao.getItemById(id, Azienda.class);

		return az;
	}//-------------------------------------------------------------------------
	
	public void addItem(MasterItem mi) {
		gitLandDao.addItem(mi);
	}//-------------------------------------------------------------------------
	
	public List<Object[]> getList(String strQry, Boolean hql){
		List<Object[]> lstObjs = gitLandDao.getList(strQry, hql);
		return lstObjs;
	}//-------------------------------------------------------------------------
	
	public List getList(String strQry, Hashtable<String, Object> parametri, Boolean hql){
		List lst = gitLandDao.getList(strQry, parametri, hql);
		return lst;
	}//-------------------------------------------------------------------------

	public List getList(Hashtable htQry, Class cls){
		List lst = gitLandDao.getList( htQry, cls );
		return lst;
	}//-------------------------------------------------------------------------
	
	public MasterItem getItemById(Long id, Class cls){
		MasterItem mi = gitLandDao.getItemById( id, cls);
		return mi;
	}//-------------------------------------------------------------------------
	
	public MasterItem updItem(MasterItem mi) {
		MasterItem iMaster = gitLandDao.updItem(mi);
		return iMaster;
	}//-------------------------------------------------------------------------
	
	public Boolean delItemById(Long id, Class cls, Boolean audit){
		Boolean ris=gitLandDao.delItemById(id, cls, audit);
		return ris;
	}

	public Boolean delItemById(Object id, Class cls) {
		Boolean ris=gitLandDao.delItemById(id, cls);
		return ris;
	}
	public Integer delList(Hashtable htQry, Class cls){
		Integer ris = gitLandDao.delList( htQry, cls );
		return ris;
	}//-------------------------------------------------------------------------


}
