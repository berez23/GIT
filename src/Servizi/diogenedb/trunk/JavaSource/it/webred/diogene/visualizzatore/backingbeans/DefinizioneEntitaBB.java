package it.webred.diogene.visualizzatore.backingbeans;

import it.webred.diogene.visualizzatore.beans.*;
import it.webred.diogene.visualizzatore.model.*;

import java.util.*;

import javax.faces.application.*;
import javax.faces.context.*;
import javax.faces.event.*;
import javax.faces.model.*;

/**
 * Backing bean utilizzato dalla pagina che contiene il diagramma ad albero per la visualizzazione e la gestione 
 * della struttura progetti - classi.<p>
 * Contiene i metodi e i dati necessari per l'apertura delle popup di gestione progetti, classi contenitori e classi, 
 * nonché per la cancellazione di progetti e classi, per la modifica del loro ordinamento e per la navigazione 
 * nell'albero (apertura e chiusura di nodi, link).<p>
 * L'accesso ai dati non viene direttamente effettuato in questa classe ma avviene tramite l'istanza di un oggetto di 
 * classe DefinizioneEntitaModel.
 * @author Filippo Mazzini
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:33 $
 */
public class DefinizioneEntitaBB {
	
	/**
	 * DataModel per la costruzione della tabella utilizzata per la visualizzazione del diagramma ad albero.
	 */
	private DataModel projectsClassesDM = new ListDataModel(); 
	/**
	 * ArrayList di oggetti ProgettoClasse che costituiscono i dati necessari per valorizzare il DataModel 
	 * projectsClassesDM.
	 */
	private ArrayList<ProgettoClasse> projectsClasses;
	/**
	 * ArrayList di oggetti ProgettoClasse che costituiscono la struttura complessiva progetti - classi (sia gli 
	 * elementi visualizzati che gli elementi compresi in nodi non espansi).
	 */
	private ArrayList<ProgettoClasse> allProjectsClasses;
	/**
	 *	Oggetto di classe DefinizioneEntitaModel utilizzato per l'accesso ai dati del DB (query di selezione, inserimento, 
	 *	modifica, cancellazione), nonché per la restituzione di eventuali dati statici.
	 */
	private DefinizioneEntitaModel dem;
	/**
	 *	Flag che indica se è stata data la conferma alla cancellazione di un progetto o di una classe.
	 */
	private boolean deleteConfirm;

	/**
	* Costruttore che carica il diagramma ad albero tramite la chiamata al metodo della classe DefinizioneEntitaModel 
	* a ciò delegato.<p>
	* Se si verifica un'eccezione, visualizza il relativo messaggio nella pagina.
	*/
	public DefinizioneEntitaBB() {
		super();
		deleteConfirm = false;
		dem = new DefinizioneEntitaModel();
		try {
			setProjectsClasses(dem.getProjectsClasses(getProjectsClasses(), getAllProjectsClasses(), true));
		}catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Impossibile caricare la pagina"));
			e.printStackTrace();
		}
	}
	
	/**
	 * Elimina il bean corrente dal FacesContext tramite la chiamata a cancellaForm(). Quindi effettua il rientro alla 
	 * pagina di menu visualizzatore.
	 * @return Valore di tipo String che, secondo la configurazione della navigazione, permette l'apertura della pagina 
	 * di menu visualizzatore.
	 */
	public String esci() {
		cancellaForm();
		return "vaiAMenu";
	}
	
	/**
	 * Effettua il rientro alla pagina di menu visualizzatore.
	 * @return Valore di tipo String che, secondo la configurazione della navigazione, permette l'apertura della pagina 
	 * di menu visualizzatore.
	 */
	public String newProject() {
		return "vaiAMenuVisualizzatore";
	}
	
	/**
	 * Elimina i bean DefinizioneEntita, DefinizioneFolder e DefinizioneClasse dalla SessionMap perché venga chiamato 
	 * di nuovo il loro costruttore, ripristinando la situazione iniziale (come alla prima apertura della pagina).
	 */
	private void cancellaForm() {
		Map map = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		map.remove("DefinizioneEntita");
		if (map.containsKey("DefinizioneFolder"))
			map.remove("DefinizioneFolder");
		if (map.containsKey("DefinizioneClasse"))
			map.remove("DefinizioneClasse");
	}

	/**
	 * Espande o restringe un nodo dell'albero (tramite la chiamata ai metodi setIconPath() e getProjectsClasses() 
	 * di DefinizioneEntitaModel).
	 * @param event L'oggetto che rappresenta l'evento di click sull'icona associata alla descrizione di un elemento 
	 * del diagramma ad albero (che rappresenta un nodo).
	 */
	public void expandCollapse(ActionEvent event) {
		FacesContext context = FacesContext.getCurrentInstance(); 
		Map map = context.getExternalContext().getRequestParameterMap();
		long projectClassId = new Long((String) map.get("projectClassId")).longValue();
		short projectClassLevel = new Short((String) map.get("projectClassLevel")).shortValue();
		try {
			for (ProgettoClasse pc : getProjectsClasses()) {
				if (pc.getId() == projectClassId && pc.getLevel() == projectClassLevel) {
					pc.setExpanded(!pc.isExpanded());
					pc.setIconPath(pc.isExpanded() ? "/visualizzatore/img/collapse.gif" : "/visualizzatore/img/expand.gif");
					dem.setIconPath(pc, null);
					break;
				}
			}
			setProjectsClasses(dem.getProjectsClasses(getProjectsClasses(), getAllProjectsClasses(), false));
		}catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Impossibile ricaricare la pagina"));
			e.printStackTrace();
		}
	}
	
	/**
	 * Sposta di un posto verso l'alto o verso il basso (allo stesso livello) un elemento dell'albero 
	 * (tramite la chiamata ai metodi move() e getProjectsClasses() di DefinizioneEntitaModel).
	 * @param event L'oggetto che rappresenta l'evento di click sull'icona "Muovi su" o "Muovi già" associata ad un 
	 * elemento dell'albero.
	 */
	public void move(ActionEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();
		Map map = context.getExternalContext().getRequestParameterMap();
		long projectClassId = new Long((String) map.get("projectClassId")).longValue();
		short projectClassLevel = new Short((String) map.get("projectClassLevel")).shortValue();
		String mode = (String) map.get("mode");
		try {
			dem.move(projectClassId, projectClassLevel, mode);
			setProjectsClasses(dem.getProjectsClasses(getProjectsClasses(), getAllProjectsClasses(), true));
		}catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Impossibile effettuare l'operazione prescelta"));
			e.printStackTrace();
		}
	}
	
	/**
	 * Cancella un elemento dell'albero e tutti gli elementi eventualmente in esso compresi (tramite la chiamata ai 
	 * metodi delete() e setProjectsClasses() di DefinizioneEntitaModel).
	 * @param event L'oggetto che rappresenta l'evento di click sull'icona "Cancella" associata ad un elemento 
	 * dell'albero.
	 */
	public void delete(ActionEvent event) {
		if (!isDeleteConfirm())
			return;
		FacesContext context = FacesContext.getCurrentInstance();
		Map map = context.getExternalContext().getRequestParameterMap();
		long projectClassId = new Long((String) map.get("projectClassId")).longValue();
		short projectClassLevel = new Short((String) map.get("projectClassLevel")).shortValue();
		try {
			dem.delete(projectClassId, projectClassLevel);
			setProjectsClasses(dem.getProjectsClasses(getProjectsClasses(), getAllProjectsClasses(), true));
		}catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Impossibile cancellare l'elemento selezionato"));
			e.printStackTrace();
		}
		setDeleteConfirm(false);
	}

	public ArrayList<ProgettoClasse> getProjectsClasses() {
		return projectsClasses;
	}

	public void setProjectsClasses(ArrayList<ProgettoClasse> projectsClasses) {
		this.projectsClasses = projectsClasses;
		projectsClassesDM.setWrappedData(this.projectsClasses);
	}
	
	/**
	 * Overloading per impostare nello stesso tempo projectsClasses e allProjectsClasses.
	 * @param projectsClasses HashMap che contiene due ArrayList di ProgettoClasse, una imposta projectsClasses, 
	 * l'altra allProjectsClasses
	 */
	public void setProjectsClasses(HashMap<String,ArrayList<ProgettoClasse>> projectsClasses) {
		setProjectsClasses(projectsClasses.get("newProjectsClasses"));
		setAllProjectsClasses(projectsClasses.get("allProjectsClasses"));
	}

	public DataModel getProjectsClassesDM() {
		return projectsClassesDM;
	}

	public void setProjectsClassesDM(DataModel projectsClassesDM) {
		this.projectsClassesDM = projectsClassesDM;
	}

	public ArrayList<ProgettoClasse> getAllProjectsClasses() {
		return allProjectsClasses;
	}

	public void setAllProjectsClasses(ArrayList<ProgettoClasse> allProjectsClasses) {
		this.allProjectsClasses = allProjectsClasses;
	}

	public DefinizioneEntitaModel getDem() {
		return dem;
	}
	
	public void setDem(DefinizioneEntitaModel dem) {
		this.dem = dem;
	}
	
	public boolean isDeleteConfirm() {
		return deleteConfirm;
	}

	public void setDeleteConfirm(boolean deleteConfirm) {
		this.deleteConfirm = deleteConfirm;
	}
	
}
