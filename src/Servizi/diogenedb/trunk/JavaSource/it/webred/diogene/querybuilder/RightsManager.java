package it.webred.diogene.querybuilder;

import it.webred.diogene.db.model.am.AmApplication;
import it.webred.diogene.db.model.am.AmApplicationItem;
import it.webred.diogene.db.model.am.AmItem;
import it.webred.diogene.db.model.am.AmItemRole;
import it.webred.diogene.db.model.am.AmPermission;
import it.webred.diogene.db.model.am.AmRole;
import it.webred.diogene.db.model.am.AmRolePermission;
import it.webred.diogene.db.model.am.AmRolePermissionId;
import it.webred.diogene.db.model.am.AmUser;
import it.webred.diogene.db.model.am.AmUserIr;
import it.webred.diogene.db.model.am.AmUserIrId;
import it.webred.permessi.GestionePermessi;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.swing.text.StyledEditorKit.ItalicAction;

import org.hibernate.Query;
import org.hibernate.Session;

public class RightsManager
{
	private static String SESSION_ENTITY_RIGHTS="session_entity_rights";
	/**
	 * Questo metodo si occupa di controllare se un utente è autorizzato <br>
	 * ad eseguire una determinata operazione sulla entità passata.<br>
	 * Il controllo viene effettuato sia tremite {@link it.webred.permessi.GestionePermessi#operazioneSuEntita(Object, String, String, String)} <br>
	 * che sulla variabile(HashMap) contenuta nell'attributo di sessione <br>
	 * {@link it.webred.diogene.querybuilder.RightsManager#SESSION_ENTITY_RIGHTS}.<br>
	 * @param user L'oggetto recuperato dalla request mediante il metodo getUserPrincipal()
	 * @param nome Il nome dell'entità della quale sto controllando i diritti
	 * @param application Il nome dell'applicazione 
	 * @param permesso Il permesso che descrive l'operazione che si vuole compiere(READ,WRITE,DELETE)
	 * @return true se è autorizzato, altrimenti false
	 */
	public static boolean isRightGranted(Object user,String nomeEntita,String application,String permesso)
	{
			/* 
		TODO: REINTEGRARE LA GESTIONE PERMESSI
		*/
	
		if (true)
			return true;
		
		HashMap map=(HashMap)((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true)).getAttribute(SESSION_ENTITY_RIGHTS);
		String ente=" ("+GestionePermessi.getEnteFromContext(user, application)+")";
		boolean isEntityPresent = (map!=null && map.containsKey(nomeEntita+ente));
		try
		{
			return isEntityPresent||GestionePermessi.operazioneSuEntita(user,nomeEntita,application,permesso);
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	
/*	
	/**Concede il diritto di ownership all'utente della sessione corrente tramite l'aggiunta<br>
	 * del nome entità nella variabile(HashMap) contenuta nell'attributo di sessione <br>
	 * {@link it.webred.diogene.querybuilder.RightsManager#SESSION_ENTITY_RIGHTS}.<br> 
	 * @param nomeEntita Nome dell'entità della quale Concedere il diritto sulla sessione corrente.
	 */
	public static void grantEntityForSession(String nomeEntita)
	{
	    //salvo i diritti dell'entita nella hashmap sulla sessione
		HttpSession session= ((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true));
		HashMap map=(HashMap)session.getAttribute(SESSION_ENTITY_RIGHTS);
		if(map==null)map=new HashMap();
		map.put(nomeEntita,nomeEntita);
		session.setAttribute(SESSION_ENTITY_RIGHTS,map);
	}
	/**Revoca il diritto di ownership all'utente della sessione corrente tramite la<br>
	 * rimozione del nome entità dalla variabile(HashMap) contenuta nell'attributo di sessione <br>
	 * {@link it.webred.diogene.querybuilder.RightsManager#SESSION_ENTITY_RIGHTS}.<br> 
	 * @param nomeEntita Nome dell'entità della quale revocare il diritto sulla sessione corrente
	 */
	public static void revokeEntityForSession(String nomeEntita)
	{
	    //salvo i diritti dell'entita nella hashmap sulla sessione 
		HashMap map=(HashMap)((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true)).getAttribute(SESSION_ENTITY_RIGHTS);
		if(map!=null){
			map.remove(nomeEntita);
		}
	}


	/** Metodo per la Serializzazione dell'oggetto AmApplication in un File XML
	 * 
	 * @param amApplication l'istanza dell'oggetto con tutte le relazioni valorizzate
	 * con istanze da serializzare insieme.
	 * @param fileName nome del file XML dove memorizzare l'oggetto e tutti gli oggetti a lui collegati.
	 * @throws FileNotFoundException
	 */
	public void encode(AmApplication amApplication, String fileName) throws FileNotFoundException
	{
		// Serializzo la lista nel file XML
		XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(fileName)));
		encoder.writeObject(amApplication);
		encoder.close();
	}
	/** Metodo per la De-Serializzazione dell'oggetto AmApplication da un File XML
	 * @param fis l'InputStream ritornato dal metodo Context.getResourceAsStream(PERCORSO_RELATIVO_FILE) che apre il file contenente 
	 *  l'oggetto AmApplication e tutti gli oggetti ad esso collegati.
	 * @param entityParms Parametri di personalizzazione dei dati il primo è il nome dell'entità
	 * il secondo è il nome dell'ente il terzo è il nome dell'utente
	 * @return
	 * @throws IOException 
	 */
	public AmApplication decode(InputStream fis, String...entityParms) throws IOException
	{
		InputStream is=null;
		//controllo se sostituire o meno il nome entità ed il nome Ente
		if(entityParms.length>0 ){
			
			StringBuffer buffer = new StringBuffer();
			InputStreamReader isr = new InputStreamReader(fis,"UTF8");
			Reader in = new BufferedReader(isr);
			int ch;
			while ((ch = in.read()) > -1) {
				buffer.append((char)ch);
			}
			in.close();
			String theStringa=MessageFormat.format(buffer.toString(),(Object[])entityParms);
			is= new ByteArrayInputStream(theStringa.getBytes("UTF-8"));
			
		}else{
			is=new BufferedInputStream(fis);
		}
		// Deserializzo la lista 
		
		XMLDecoder decoder = new XMLDecoder(is );
		AmApplication amApplication = (AmApplication) decoder.readObject();
		decoder.close();
		return amApplication;
	}
	
	/** Metodo per rendere persistente l'oggetto AmApplication e tutte le istanze ad esso collegate.<br>
	 * <B>N.B. oltre l'oggetto vengono resi persistenti anche i seguenti oggetti collegati:</br>
	 * 	AmApplicationItem<br>
	 * 	AmItem<br>
	 * 	AmItemRole<br>
	 *  AmUserIr<br>
	 *  AmPermission<br>
	 *  AmRolePermission<br>
	 * </B>
	 * @param amApplication l'oggetto da rendere persistente(completo di oggetti collegati)
	 * @param session la sessione Hibernate nella quale l'oggetto verrà resto persistente.
	 * @param user l'utente "owner" della entità
	 * @throws Exception 
	 */
	public void saveOrUpdateAmApplication(AmApplication amApplication, Session session, String user) throws Exception{
        //salvo l'oggetto AmApplication	
		session.saveOrUpdate(amApplication);
		session.flush();
		session.evict(amApplication);
		Set<AmApplicationItem> amApplicationItems=amApplication.getAmApplicationItems();
        for (AmApplicationItem amApplicationItem : amApplicationItems)
		{
        	
        	// Per ogni amApplicationItem salvo il relativo AmItem (relazione ApplicationItem --> Item)
			AmItem amItem= amApplicationItem.getAmItem();
			session.saveOrUpdate(amItem);
			grantEntityForSession(amItem.getName());
			// ricarico l'eventuale chiave primaria partendo dalla chiave alternativa
			Query qAi=session.createQuery("select ai.id from AmApplicationItem ai where ai.amApplication.name=:application and ai.amItem.name=:item");
			qAi.setParameter("application",amApplication.getName());
			qAi.setParameter("item",amItem.getName());
			Long idAmApplicationItem =  (Long)qAi.uniqueResult();
			amApplicationItem.setId(idAmApplicationItem);
        	// Poi salvo amApplicationItem stesso
			session.saveOrUpdate(amApplicationItem);

			//Per ogni amItem salvo tutte le AmPermission (relazione Item --> Permesso)
        	Set<AmPermission> amPermissions= amItem.getAmPermissions();
			for (AmPermission  amPermission : amPermissions)
			{
				session.saveOrUpdate(amPermission);
			}

			
			//Per ogni amApplicationItem salvo tutti gli AmItemRole (relazione ApplicationItem --> Ruolo)
        	Set<AmItemRole> amItemRoles= amApplicationItem.getAmItemRoles();
			for (AmItemRole amItemRole : amItemRoles)
			{
				
				//Salvo anche il Ruolo qualora non Esista.
				if(amItemRole.getAmRole()!=null) {
					session.saveOrUpdate(amItemRole.getAmRole());
					session.evict(amItemRole.getAmRole());
				}					
				
				Query qIr=session.createQuery("select ir.id from AmItemRole ir where ir.amApplicationItem.id=:item and ir.amRole.name=:role");
				qIr.setParameter("item",amItemRole.getAmApplicationItem().getId());
				qIr.setParameter("role",amItemRole.getAmRole().getName());
				Long idAmItem =  (Long)qIr.uniqueResult();
				amItemRole.setId(idAmItem);
				session.saveOrUpdate(amItemRole);
				//Per ogni AmItemRole salvo tutti gli AmUserIr (relazione Utente --> Item --> Ruolo)
				Set<AmUserIr> amUserIrs= amItemRole.getAmUserIrs();
				for (AmUserIr amUserIr : amUserIrs)
				{
					//Salvo anche l'utente qualora sia valorizzato e non Esista nel DB altrimenti lo carico.
					if(amUserIr.getAmUser()!=null){
						
						Query q=session.createQuery("from AmUser u where u.name=:user");
						q.setParameter("user",user);
						AmUser amUser =  (AmUser)q.uniqueResult();
						if(amUser==null){
							throw new Exception("Errore nella configurazione del Realm(Application Server). Contattare amministratore del sistema :["+user+"] Utente non Censito");
						}
						amUserIr.setAmUser(amUser);
						amUserIr.getId().setFkAmUser(amUser.getName());
						amUserIr.getId().setFkAmItemRole(amItemRole.getId());
					}
					session.saveOrUpdate(amUserIr);
				}
				//Per ogni AmPermission salvo tutte le AmRolePermission (relazione Ruolo --> Permesso)
				Set<AmRolePermission> amRolePermissions= amItemRole.getAmRolePermissions();
				for (AmRolePermission amRolePermission : amRolePermissions)
				{
					amRolePermission.getId().setFkAmItemRole(amItemRole.getId());
					session.saveOrUpdate(amRolePermission);
				}
				
			}			
		}
	}
	/** Metodo per eliminare l'oggetto AmItem e tutte le istanze ad esso collegate.
	 * @param amItem l'oggetto da rendere persistente(completo di oggetti collegati)
	 * @param session la sessione Hibernate nella quale l'oggetto verrà resto persistente.
	 * @throws Exception 
	 */
	public void deleteAmItem(AmItem amItem, Session session) throws Exception
	{
		if(amItem==null || session==null)return;
		//Elimino l'AmItem e tutti gli oggetto ad esso collegato con l'esclusione dell'AmApplication e degli AmRole
		
		//Per ogni AmItem elimino tutti gli AmApplicationItem (relazione Item --> Application Item)
		Set<AmApplicationItem> amApplicationeItems = amItem.getAmApplicationItems();
		for (AmApplicationItem amApplicationItem : amApplicationeItems)
		{
			//Per ogni AmApplicationItem elimino tutti gli AmItemRole (relazione Application Item --> Ruolo)
			Set<AmItemRole> amItemRoles = amApplicationItem.getAmItemRoles();
			for (AmItemRole amItemRole : amItemRoles)
			{

				//Per ogni AmPermission elimino tutte le AmRolePermission (relazione Ruolo --> Permesso)
				Set<AmRolePermission> amRolePermissions = amItemRole.getAmRolePermissions();
				for (AmRolePermission amRolePermission : amRolePermissions)
				{
					session.delete(amRolePermission);
				}
				//Per ogni AmItemRole elimino tutti gli AmUserIr (relazione Utente --> Item --> Ruolo)
				Set<AmUserIr> amUserIrs = amItemRole.getAmUserIrs();
				for (AmUserIr amUserIr : amUserIrs)
				{
					session.delete(amUserIr);
				}
				//elimino l'aAmItemRole (relazione Item --> Ruolo).
				session.delete(amItemRole);
			}
			session.delete(amApplicationItem);

		}
		//Per ogni amItem elimino tutte le AmPermission (relazione Item --> Permesso)
		Set<AmPermission> amPermissions = amItem.getAmPermissions();
		for (AmPermission amPermission : amPermissions)
		{
			session.delete(amPermission);
		}

		// elimino l'item
		session.delete(amItem);
	}
	
	
	/** Metodo che ritorna l' AmApplication template per generare il file XML Template.
	 * @return
	 */
	public AmApplication buildTemplate(){
		
		//AmItem 
		AmItem amItem = new AmItem(GestionePermessi.PLACEHOLDER_ENTITY_NAME, new HashSet(),new HashSet());
		
		//AmPermission
		AmPermission amPermission1 = new AmPermission(GestionePermessi.PLACEHOLDER_READ_ENTITY_RIGHT,amItem);
		AmPermission amPermission2 = new AmPermission(GestionePermessi.PLACEHOLDER_WRITE_ENTITY_RIGHT,amItem);
		AmPermission amPermission3 = new AmPermission(GestionePermessi.PLACEHOLDER_DELETE_ENTITY_RIGHT,amItem);
		
		//AmItem --> AmPermission
		amItem.getAmPermissions().add(amPermission1);
		amItem.getAmPermissions().add(amPermission2);
		amItem.getAmPermissions().add(amPermission3);
		//AmApplication
		AmApplication amApplication = new AmApplication(	GestionePermessi.PLACEHOLDER_APPLICATION_NAME,
														null,
														GestionePermessi.PLACEHOLDER_ENTE_NAME,
														GestionePermessi.PLACEHOLDER_APPLICATION_TYPE, GestionePermessi.CATEGORIA_AMAPPLICATION_INDEFINITA,
														new HashSet());
		
		
		//AmApplicationItem
		AmApplicationItem amApplicationItem = new AmApplicationItem(-1L,amApplication,amItem,new HashSet());
		//AmApplicationItem --> AmApplication
		amApplication.getAmApplicationItems().add(amApplicationItem);
		
		//AmRole
		AmRole amRole1 = new AmRole("Owner",new HashSet());
		AmRole amRole2 = new AmRole("Base",new HashSet());
		AmRole amRole3 = new AmRole("Avanzato",new HashSet());
		//AmItemRole
		AmItemRole amItemRole1 = new AmItemRole(-1L,amRole1,amApplicationItem,new HashSet(),new HashSet());
		AmItemRole amItemRole2 = new AmItemRole(-1L,amRole2,amApplicationItem,new HashSet(),new HashSet());
		AmItemRole amItemRole3 = new AmItemRole(-1L,amRole3,amApplicationItem,new HashSet(),new HashSet());
		//AmItemRole --> AmRole
		amRole1.getAmItemRoles().add(amItemRole1);
		amRole2.getAmItemRoles().add(amItemRole2);
		amRole3.getAmItemRoles().add(amItemRole3);
		//AmItemRole --> AmApplicationItem
		amApplicationItem.getAmItemRoles().addAll(amRole1.getAmItemRoles());
		amApplicationItem.getAmItemRoles().addAll(amRole2.getAmItemRoles());
		amApplicationItem.getAmItemRoles().addAll(amRole3.getAmItemRoles());
		
		//AmRolePermission
		AmRolePermission amRole1Permission1 = new AmRolePermission(new AmRolePermissionId( -1L,amPermission1.getPermission()),
																	amItemRole1,
																	amPermission1);

		AmRolePermission amRole1Permission2 = new AmRolePermission(new AmRolePermissionId( -1L,amPermission2.getPermission()),
																	amItemRole1,
																	amPermission2);
		AmRolePermission amRole1Permission3 = new AmRolePermission(new AmRolePermissionId( -1L,amPermission3.getPermission()),
																	amItemRole1,
																	amPermission3);
		AmRolePermission amRole2Permission1 = new AmRolePermission(new AmRolePermissionId( -1L,amPermission1.getPermission()),
																	amItemRole2,
																	amPermission1);
		AmRolePermission amRole3Permission1 = new AmRolePermission(new AmRolePermissionId( -1L,amPermission1.getPermission()),
																	amItemRole3,
																	amPermission1);
		AmRolePermission amRole3Permission2 = new AmRolePermission(new AmRolePermissionId( -1L,amPermission2.getPermission()),
																	amItemRole3,
																	amPermission2);
		AmRolePermission amRole3Permission3 = new AmRolePermission(new AmRolePermissionId( -1L,amPermission3.getPermission()),
																	amItemRole3,
																	amPermission3);
		//AmRolePermission --> AmItemRole
		amItemRole1.getAmRolePermissions().add(amRole1Permission1);
		amItemRole1.getAmRolePermissions().add(amRole1Permission2);
		amItemRole1.getAmRolePermissions().add(amRole1Permission3);
		amItemRole2.getAmRolePermissions().add(amRole2Permission1);
		amItemRole3.getAmRolePermissions().add(amRole3Permission1);
		amItemRole3.getAmRolePermissions().add(amRole3Permission2);
		amItemRole3.getAmRolePermissions().add(amRole3Permission3);
		
		

		//AmUserIr
		//AmUserIr userIr1 = new AmUserIr(new AmUserIrId(GestionePermessi.PLACEHOLDER_USER_NAME,1L),amItemRole1,new AmUser(GestionePermessi.PLACEHOLDER_USER_NAME,null,new HashSet()));
		//sostituito da (Filippo Mazzini 16.07.07):
		AmUserIr userIr1 = new AmUserIr(new AmUserIrId(GestionePermessi.PLACEHOLDER_USER_NAME,1L),amItemRole1,new AmUser(GestionePermessi.PLACEHOLDER_USER_NAME,null,null,null,null,null,new HashSet()));
		//AmUserIr --> AmItemRole
		amItemRole1.getAmUserIrs().add(userIr1);
		
		return amApplication;
	}

}
