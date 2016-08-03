package it.webred.diogene.visualizzatore.runtime;

import it.webred.diogene.db.HibernateSession;
import it.webred.diogene.db.model.DcRel;
import it.webred.diogene.db.model.DvFormatClasse;
import it.webred.diogene.db.model.DvKey;
import it.webred.diogene.db.model.DvKeyPair;
import it.webred.diogene.db.model.DvLinkClasse;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;


/**
 * Bean che descrive i link di una opagina in termini di coppie di chiavi
 * @author marcoric
 * @version $Revision: 1.2 $ $Date: 2007/10/26 13:11:16 $
 */
public class PageLink
{


	
	/**
	 * Hashmap delle chiavi
	 * <idClasse,<  Nome link, Set di chiavi del link> 
	 */
	
	private LinkedHashMap<Long, LinkedHashMap<String, LinkedHashSet<String>>> keys = new LinkedHashMap<Long, LinkedHashMap<String, LinkedHashSet<String>>>();

	/**
	*	@param entityKey
	*	Viene passata la chiave (se composta viene passata come -con separatore-)
	*   Si va ad interogare DvKey per vedere con quali altre chiavi risulta correlata.
	 * @throws Exception 
	*/
	public PageLink(String entityKey, Long idClasse) throws Exception {
		
		Object owner = null;
		try {
			owner = HibernateSession.createSession();
			Session s = HibernateSession.getSession();




			// cerco tutte le classi coinvolte e correlate con la presente
			String sql = "from DvLinkClasse as dvlc"+
			" where dvlc.dvClasseByFkDvClasse1.id = :ida" +
			"";//" or dvlc.dvClasseByFkDvClasse2.id = :idb";
			Query qclassi = s.createQuery(sql);
			qclassi.setParameter("ida",idClasse);
			//qclassi.setParameter("idb",idClasse);
			List dvcs = qclassi.list();
			Iterator itc = dvcs.iterator();
			while (itc.hasNext()) {
				DvLinkClasse dvlc = (DvLinkClasse) itc.next();
				//Long idDcEntity;
				Long classecorrelata;
				if (dvlc.getDvClasseByFkDvClasse1().getId().equals(idClasse)) {
					classecorrelata = dvlc.getDvClasseByFkDvClasse2().getId();
				//	idDcEntity = ((DvFormatClasse)dvlc.getDvClasseByFkDvClasse1().getDvFormatClasses().iterator().next()).getDvUserEntity().getDcEntity().getId();
	        	} else {
					classecorrelata = dvlc.getDvClasseByFkDvClasse1().getId();
				//	idDcEntity = ((DvFormatClasse)dvlc.getDvClasseByFkDvClasse2().getDvFormatClasses().iterator().next()).getDvUserEntity().getDcEntity().getId();
	        	}
				
				

				if (keys.get(classecorrelata)==null || keys.get(classecorrelata).isEmpty()) {
					keys.put(classecorrelata,null);
				}
				
				long idLink = dvlc.getDvLink().getId();
				
				
				
				LinkedHashMap<String, LinkedHashSet<String>> hmlinksingolo = new LinkedHashMap<String, LinkedHashSet<String>>();
				LinkedHashSet<String> hschiavi = new LinkedHashSet<String>();
				
				/* String sqlkp = "from DvKeyPair as dvkp" +
				" where dvkp.dvLink = :link and " +
				" ((dvkp.dvKeyByFkDvKey1.entityKey = :eka and dvkp.dvKeyByFkDvKey1.dcEntity.id = :ide1 ) or" +
				" (dvkp.dvKeyByFkDvKey2.entityKey = :ekb and dvkp.dvKeyByFkDvKey2.dcEntity.id = :ide2 ))";
				Query q = s.createQuery(sqlkp);
				q.setParameter("link",dvlc.getDvLink());
				q.setParameter("eka",entityKey);
				q.setParameter("ekb",entityKey);
				q.setParameter("ide1",idDcEntity);
				q.setParameter("ide2",idDcEntity);
				List<DvKeyPair> dvkps = q.list(); 
				Iterator dvkpit = dvkps.iterator();
		        while (dvkpit.hasNext()){
		        	DvKeyPair dvkp = (DvKeyPair)dvkpit.next(); 
		        	String chiavecorrelata;
		        	if (dvkp.getDvKeyByFkDvKey1().getEntityKey().equals(entityKey)) {
		        		chiavecorrelata = dvkp.getDvKeyByFkDvKey2().getEntityKey();
		        	} else {
		        		chiavecorrelata = dvkp.getDvKeyByFkDvKey1().getEntityKey();
		        	}
		        	hschiavi.add(chiavecorrelata);
		        }
		        */
				hschiavi = KeyLinkFactory.run(idLink, entityKey);
		        hmlinksingolo.put(dvlc.getName(),hschiavi);
		        keys.put(classecorrelata,hmlinksingolo);

			}
			
			
			
			
		} catch (Exception e) {
			throw new Exception (e);
		} finally {
			if (owner!=null)
				HibernateSession.closeSession(owner);
		}
		

	}

	
	public LinkedHashMap<Long, LinkedHashMap<String, LinkedHashSet<String>>> getKeys()
	{
		return keys;
	}


	public void setKeys(LinkedHashMap<Long, LinkedHashMap<String, LinkedHashSet<String>>> keys)
	{
		this.keys = keys;
	}


	public static void main(java.lang.String[] args) {
		try
		{
			PageLink pl = new PageLink("30052",new Long(1));
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
