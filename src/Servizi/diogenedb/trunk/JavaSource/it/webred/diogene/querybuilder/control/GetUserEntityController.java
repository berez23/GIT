package it.webred.diogene.querybuilder.control;

import static it.webred.diogene.querybuilder.Constants.UTF8_XML_ENCODING;
import static it.webred.diogene.querybuilder.enums.Outcomes.SUCCESS;
import static it.webred.diogene.querybuilder.enums.Outcomes.FAILURE;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import it.webred.diogene.db.model.DcEntity;
import it.webred.diogene.db.model.DvUeFromEntity;
import it.webred.diogene.db.model.DvUserEntity;
import it.webred.diogene.querybuilder.beans.EntityNamesToExport;
import it.webred.diogene.querybuilder.db.GetUserEntityDbManager;
import it.webred.diogene.querybuilder.db.UserEntityExporter;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * Backing bean utilizzato per il procedimento di acquisizione di una User Entity.
 * @author Filippo Mazzini
 * @version $Revision: 1.1 $ $Date: 2007/11/22 15:59:57 $
 */ 
public class GetUserEntityController {
	
	private String serverUrl;
	private Long selectedEntityId;
	private ArrayList<DvUserEntity> entities;
	private ArrayList<EntityNamesToExport> entitiesNames;
	private Integer entitiesNamesSize;
	private String sql;
	private GetUserEntityDbManager guedbm;
	private UserEntityExporter uee;
	
	public GetUserEntityController() {
		guedbm = new GetUserEntityDbManager();
		uee = new UserEntityExporter();
		entities = new ArrayList<DvUserEntity>();
		entitiesNames = new ArrayList<EntityNamesToExport>();
		entitiesNamesSize = new Integer(0);
		//aggiungo l'item vuoto
		DvUserEntity dvue = new DvUserEntity();
		dvue.setId(new Long(-1));
		DcEntity dce = new DcEntity();
		dce.setId(new Long(-1));
		dce.setName("");
		dvue.setDcEntity(dce);
		entities.add(dvue);		
	}

	/**
	 * Effettua il rientro alla pagina di lista delle User Entity.
	 * @return Valore di tipo String che, secondo la configurazione della navigazione, permette l'apertura della pagina 
	 * di lista delle User Entity.
	 */
	public String esci() {
		cancellaForm();
		return SUCCESS.outcome();
	}
	
	/**
	 * Elimina il bean getUserEntityBb dalla SessionMap perché venga chiamato di nuovo il suo costruttore 
	 * ripristinando la situazione iniziale (come alla prima apertura della pagina).
	 */
	private void cancellaForm() {
		/*elimino il bean getUserEntityBb dalla SessionMap perché venga chiamato di nuovo il suo costruttore
		 ripristinando la situazione iniziale (come alla prima apertura della pagina)*/
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("getUserEntityBb");
	}
	
	private void impostaEntitiesNames(Set dvUeFromEntities) {
		entitiesNames = new ArrayList<EntityNamesToExport>();
		Iterator it = dvUeFromEntities.iterator();
		while (it.hasNext()) {
			DvUeFromEntity dufe = (DvUeFromEntity)it.next();
			//per default i due nomi sono uguali
			entitiesNames.add(new EntityNamesToExport(dufe.getDcEntity().getName(), dufe.getDcEntity().getName()));
		}
		entitiesNamesSize = new Integer(entitiesNames.size());
	}
	
	private HashMap<String, String> getEntitiesNamesHashMap() {
		HashMap<String, String> retVal = new HashMap<String, String>();
		for (EntityNamesToExport ente : entitiesNames) {
			retVal.put(ente.getEntityFromName(), ente.getEntityToName());
		}
		return retVal;
	}
	
	public String connetti() {
		try {
			entities = guedbm.matchEntities(serverUrl);
			selectedEntityId = new Long(-1);
			sql = null;
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
			e.printStackTrace();
			return FAILURE.outcome();
		}
		return SUCCESS.outcome();
	}
	
	public void entityChanged(ValueChangeEvent event) {
		Long newSelectedEntityId = (Long)event.getNewValue();
		try {
			if (newSelectedEntityId == null || newSelectedEntityId.longValue() == -1) {
				sql = null;
				entitiesNames = new ArrayList<EntityNamesToExport>();
				entitiesNamesSize = new Integer(0);
				return;
			}
			for (DvUserEntity dvue : entities) {
				if (dvue.getDcEntity().getId().longValue() == newSelectedEntityId.longValue()) {
					impostaEntitiesNames(dvue.getDvUeFromEntities());
					entitiesNamesSize = new Integer(entitiesNames.size());
					String xmlSqlStatement = new String(dvue.getSqlStatement(), UTF8_XML_ENCODING);
					SAXBuilder builder = new SAXBuilder();
					List content = builder.build(new StringReader(xmlSqlStatement)).getContent();
					Iterator it1 = content.iterator();
					while (it1.hasNext()) {
						Element element = (Element) it1.next();
						sql = element.getText();
					}				
					break;
				}
			}
		}catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
			e.printStackTrace();
		}		
	}
	
	public String acquisisci() {
		try {
			uee.importUserEntity(serverUrl, selectedEntityId, getEntitiesNamesHashMap());
			cancellaForm();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
			e.printStackTrace();
			return FAILURE.outcome();
		}
		return SUCCESS.outcome();
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public Long getSelectedEntityId() {
		return selectedEntityId;
	}

	public void setSelectedEntityId(Long selectedEntityId) {
		this.selectedEntityId = selectedEntityId;
	}

	public ArrayList<SelectItem> getComboEntities() {
		ArrayList<SelectItem> comboEntities = new ArrayList<SelectItem>();
		for (DvUserEntity dvue : entities) {
			comboEntities.add(new SelectItem(dvue.getDcEntity().getId(), dvue.getDcEntity().getName()));
		}
		return comboEntities;
	}

	public ArrayList<EntityNamesToExport> getEntitiesNames() {
		return entitiesNames;
	}

	public void setEntitiesNames(ArrayList<EntityNamesToExport> entitiesNames) {
		this.entitiesNames = entitiesNames;
		entitiesNamesSize = new Integer(this.entitiesNames.size());
	}
	
	public Integer getEntitiesNamesSize() {
		return entitiesNamesSize;
	}

	public void setEntitiesNamesSize(Integer entitiesNamesSize) {
		//this.entitiesNamesSize = entitiesNamesSize;
		//non deve fare nulla, questo valore deve sempre essere recuperato da entitiesNames.size()
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
	
}
