package it.webred.diogene.visualizzatore.backingbeans;

import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;

import it.webred.diogene.visualizzatore.beans.*;
import it.webred.diogene.visualizzatore.model.DefinizioneEntitaModel;
import it.webred.diogene.visualizzatore.runtime.tag.MenuRenderTag;

/**
 * Backing bean utilizzato dalla popup di gestione progetti e classi contenitori.<p>
 * Contiene i metodi e i dati necessari per l'inserimento o la modifica di un progetto o di una classe contenitore.<p>
 * L'accesso ai dati non viene direttamente effettuato in questa classe ma avviene tramite l'istanza di un oggetto di 
 * classe DefinizioneEntitaModel.
 * @author Filippo Mazzini
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:33 $
 */
public class DefinizioneFolderBB {

	/**
	 * L'oggetto Progetto da inserire o modificare (diverso da null se inserimento o modifica progetto).
	 */
	Progetto project;
	/**
	 * L'oggetto Classe da inserire o modificare (diverso da null se inserimento o modifica classe contenitore).
	 */
	Classe clazz;
	/**
	 * Il titolo visualizzato nella testata della popup.
	 */
	String title;
	/**
	 * Il nome del progetto o della classe contenitore.
	 */
	String name;
	/**
	 * Il nome del progetto o della classe contenitore che contiene la classe contenitore corrente.
	 */
	String container;
	/**
	 * Flag che indica se il valore di container deve essere o meno visualizzato nella popup (non è visualizzato se 
	 * si inserisce o modifica un progetto).
	 */
	boolean renderedContainer;
	/**
	 * Riferimento alla classe java che gestisce la visualizzazione dell'albero progetti - classi, per operazioni 
	 * susseguenti al salvataggio dei dati della popup (reimpostazione dell'albero).
	 */
	DefinizioneEntitaBB debb;
	/**
	 *	Oggetto di classe DefinizioneEntitaModel utilizzato per l'accesso ai dati del DB (query di selezione, inserimento, 
	 *	modifica, cancellazione), nonché per la restituzione di eventuali dati statici.
	 */
	DefinizioneEntitaModel dem;
	/**
	 *	Flag che indica se la popup presenta errori e quindi non deve essere chiusa dalla chiamante una volta effettuato il 
	 *	submit.
	 */
	protected boolean popupError;
	/**
	 *	Costante String che identifica la modalità di inserimento.
	 */
	public static String INSERT = "insert";
	/**
	 *	Costante String che identifica la modalità di modifica.
	 */
	public static String UPDATE = "update";
	
	/**
	* Costruttore che imposta la popup per la prima apertura della pagina (chiamata a setPopup()).
	*/
	public DefinizioneFolderBB() {
		super();
		setPopup();
	}
	
	/**
	 * Metodo che inizializza la popup valorizzando l'oggetto Progetto (o Classe) che contiene i dati da inserire o 
	 * modificare, inoltre valorizza la testata e tutte le descrizioni, infine valorizza l'oggetto 
	 * DefinizioneEntitaModel usato per l'accesso ai dati e chiama il metodo getProjectOrClass().<p>
	 * Se si verificano eccezioni, visualizza nella pagina un messaggio di errore.
	 */
	protected void setPopup() {
		FacesContext context = FacesContext.getCurrentInstance(); 
		Map reqParMap = context.getExternalContext().getRequestParameterMap();
		if (!reqParMap.containsKey("mode")) return;
		Map sessMap = context.getExternalContext().getSessionMap();
		try {
			String mode = (String) reqParMap.get("mode");
			Short level = new Short((String) reqParMap.get("level"));
			boolean isProject = level.shortValue() == -2 || (mode.equals(UPDATE) && level.shortValue() == -1);
			boolean isClassInProject = mode.equals(INSERT) && level.shortValue() == -1;
			project = isProject ? new Progetto() : null;
			clazz = isProject ? null : new Classe();
			Long id = reqParMap.get("id") == null || ((String)reqParMap.get("id")).equals("") ?
						null : 
						new Long((String)reqParMap.get("id"));
			title = mode.equalsIgnoreCase(INSERT) ? "Inserimento" : "Modifica";
			title += isProject ? " progetto" : " classe";
			name = null;
			container = null;
			renderedContainer = !isProject;
			debb = ((DefinizioneEntitaBB)sessMap.get("DefinizioneEntita"));
			dem = debb.getDem();
			getProjectOrClass(id, mode, isClassInProject);
		}catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Impossibile caricare correttamente la finestra"));
			e.printStackTrace();
			setPopupError(true);
		}
	}
	
	/**
	 * Recupera dal DB i dati relativi al progetto o alla classe selezionata.
	 * @param id Identificativo del progetto o della classe contenitore
	 * @param mode Modalità (inserimento o modifica)
	 * @param isClassInProject Flag che indica (se true) che si tratta di una classe e che questa si trova al livello 
	 * più esterno (direttamente inclusa in un progetto).
	 * @throws Exception Se si verifica una qualsiasi eccezione durante l'esecuzione del metodo.
	 */
	private void getProjectOrClass(Long id, String mode, boolean isClassInProject) throws Exception {
		if (project != null) {
			if (mode.equalsIgnoreCase(UPDATE)) {
				project = dem.getProject(id);
				name = project.getName();
			}
		}else if (clazz != null) {
			if (mode.equalsIgnoreCase(UPDATE)) {
				clazz = dem.getClazz(id);
				name = clazz.getName();				
			}else{
				if (isClassInProject) {
					Progetto fkProgetto = dem.getProject(id);
					clazz.setDvProgetto(fkProgetto.getId());
					clazz.setDvProgettoName(fkProgetto.getName());
				} else {
					Classe fkClass = dem.getClazz(id);
					clazz.setDvClasse(fkClass.getId());
					clazz.setDvClasseName(fkClass.getName());
					clazz.setDvProgetto(fkClass.getDvProgetto());
					clazz.setDvProgettoName(fkClass.getDvProgettoName());
				}
			}
			if (clazz.getDvClasseName() != null && !clazz.getDvClasseName().equals("")) {
				container = "Classe contenitore: " + clazz.getDvClasseName();
			}else{
				container = "Progetto: " + clazz.getDvProgettoName();
			}
		}
	}
	
	/**
	 * Effettua i controlli preliminari e chiama il metodo di DefinizioneEntitaModel che effettua il salvataggio dei 
	 * dati nel DB. Quindi aggiorna la struttura ad albero della pagina chiamante.
	 * @param event L'oggetto che rappresenta l'evento di pressione del pulsante "Salva" della popup.
	 * @throws AbortProcessingException Se si verifica una qualsiasi eccezione durante l'esecuzione del metodo.
	 */
	public void salva (ActionEvent event) throws AbortProcessingException {
		if (name == null || name.length() == 0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Inserire un valore per il campo Nome"));
			setPopupError(true);
			return;
		}
		if (name != null && name.length() > 50) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Impossibile inserire un nome lungo più di 50 caratteri"));
			setPopupError(true);
			return;
		}
		try {
			boolean isProject = project != null;
			if (isProject) {
				project.setName(name);
				if (dem.isNameFree(project)) {
					dem.saveProject(project);
				}else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Un progetto con lo stesso nome è già stato inserito"));
					setPopupError(true);
					return;
				}
			}else{
				clazz.setName(name);
				if (dem.isNameFree(clazz)) {
					dem.saveClazz(clazz);
				}else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Una classe dello stesso livello con lo stesso nome è già stata inserita"));
					setPopupError(true);
					return;
				}
			}
			debb.setProjectsClasses(dem.getProjectsClasses(debb.getProjectsClasses(), debb.getAllProjectsClasses(), true));
			MenuRenderTag.svuotaCache();
		} catch (Exception e) {
			String message = "Errore nel salvataggio dei dati: ";
			message += e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
			e.printStackTrace();
			setPopupError(true);
			return;
		}
	}
	
	public Classe getClazz() {
		return clazz;
	}

	public void setClazz(Classe clazz) {
		this.clazz = clazz;
	}

	public Progetto getProject() {
		return project;
	}

	public void setProject(Progetto project) {
		this.project = project;
	}

	/**
	 * Trattandosi del get del primo campo visibile, verifica se deve inizializzare la popup (chiamata a setPopup()).
	 * @return L'oggetto title.
	 */
	public String getTitle() {
		FacesContext context = FacesContext.getCurrentInstance(); 
		Map reqParMap = context.getExternalContext().getRequestParameterMap();
		boolean doSetPopup = reqParMap.containsKey("doSetPopup") && ((String)reqParMap.get("doSetPopup")).equals("true");
		if (doSetPopup) {
			//al get del primo campo visibile reimposto tutta la popup
			setPopup();
		}
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContainer() {
		return container;
	}

	public void setContainer(String container) {
		this.container = container;
	}

	public boolean isRenderedContainer() {
		return renderedContainer;
	}

	public void setRenderedContainer(boolean renderedContainer) {
		this.renderedContainer = renderedContainer;
	}

	public boolean isPopupError() {
		return popupError;
	}

	public void setPopupError(boolean popupError) {
		this.popupError = popupError;
	}
	
}
