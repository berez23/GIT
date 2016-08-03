package it.webred.diogene.querybuilder.control;

import it.webred.diogene.querybuilder.beans.DcColumnWithLabelTypeBean;
import it.webred.diogene.querybuilder.db.LabelTypesDBManager;

import java.util.ArrayList;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import static it.webred.diogene.querybuilder.enums.Outcomes.SUCCESS;
import static it.webred.diogene.querybuilder.enums.Outcomes.FAILURE;

/**
 * Backing bean utilizzato per l'associazione delle colonne di una User Entity ad un tipo etichetta.
 * @author Filippo Mazzini
 * @version $Revision: 1.3 $ $Date: 2007/11/22 15:59:57 $
 */ 
public class LabelTypesController
{
	
	/**
	 * ArrayList di oggetti di classe DcColumnWithLabelTypeBean, utilizzato per il caricamento della lista delle colonne.
	 */
	private ArrayList<DcColumnWithLabelTypeBean> columns;
	/**
	 * ArrayList di oggetti di classe SelectItem, utilizzato per il caricamento delle combo box dei tipi etichetta.
	 */
	private ArrayList<SelectItem> labelTypes;
	/**
	 *	Oggetto di classe String che contiene il valore del campo SQL_STATEMENT per l'entità selezionata.
	 */
	private String sqlStatement;
	/**
	 *	Oggetto di classe String che contiene il valore del campo SQL_STATEMENT_BK per l'entità selezionata.
	 */
	private String sqlStatementBk;
	/**
	 *	Oggetto di classe LabelTypesDBManager utilizzato per l'accesso ai dati del DB.
	 */
	private LabelTypesDBManager ltdbm;	
	/**
	 *	Identificativo dell'entità selezionata.
	 */
	private Long userEntityId;
	/**
	 *	Stringa che specifica se deve essere mostrato il messaggio di sqlStatementBk modificato manualmente (corrisponde alla 
	 *	proprietà display del CSS).
	 */
	private String sqlStatementBkMsgDisplay;
	
	/**
	*	Costruttore che crea un LabelTypesController vuoto.
	*/
	public LabelTypesController() {
		super();
		ltdbm = new LabelTypesDBManager();
		String userEntityIdStr = ((EntitiesController)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("entitiesBb")).getUserEntityForLabelTypes();
		userEntityId = new Long(userEntityIdStr);
		caricaColonne();
		caricaTipiEtichetta();
		valorizzaSqlStatement();
		valorizzaSqlStatementBk();
		valorizzaSqlStatementBkMsgDisplay();
	}
	
	/**
	 * Metodo privato che carica la lista delle colonne.
	 */
	private void caricaColonne() {
		try {
			columns = ltdbm.getColumns(userEntityId);
		}catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Impossibile caricare la lista delle colonne"));
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo privato che carica la lista dei tipi etichetta.
	 */
	private void caricaTipiEtichetta() {
		try {
			labelTypes = ltdbm.getLabelTypes();
		}catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Impossibile caricare la lista dei tipi etichetta"));
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo privato che valorizza il campo sqlStatement.
	 */
	private void valorizzaSqlStatement() {
		try {
			sqlStatement = ltdbm.getSqlStatement(userEntityId);
		}catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Impossibile valorizzare il campo che contiene l'SQL originario"));
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo privato che valorizza il campo sqlStatementBk.
	 */
	private void valorizzaSqlStatementBk() {
		try {
			sqlStatementBk = ltdbm.getSqlStatementBk(userEntityId);
		}catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Impossibile valorizzare il campo che contiene l'SQL per le eventuali modifiche manuali"));
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo privato che valorizza il campo sqlStatementBkMsgDisplay.
	 */
	private void valorizzaSqlStatementBkMsgDisplay() {
		sqlStatementBkMsgDisplay = sqlStatement.equalsIgnoreCase(sqlStatementBk) ? "none" : "";
	}
	
	/**
	 * Elimina il bean userEntityLabelsBb dalla SessionMap perché venga chiamato di nuovo il suo costruttore 
	 * ripristinando la situazione iniziale (come alla prima apertura della pagina).
	 */
	private void cancellaForm() {
		/*elimino il bean userEntityLabelsBb dalla SessionMap perché venga chiamato di nuovo il suo costruttore
		 ripristinando la situazione iniziale (come alla prima apertura della pagina)*/
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("userEntityLabelsBb");
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
	 * Effettua un controllo sui dati inseriti (non possono essere presenti due campi con lo stesso tipo etichetta) e, se 
	 * tale controllo è superato, salva i dati contenuti nella pagina ed effettua il rientro alla pagina di lista delle User Entity.
	 * @return Valore di tipo String che, secondo la configurazione della navigazione, permette l'apertura della pagina 
	 * di lista delle User Entity (o la permanenza nella pagina corrente in caso di errore).
	 */
	public String salva() {
		try {
			for (DcColumnWithLabelTypeBean column : columns) {
				if (column.getLabelType() != null && column.getLabelType().longValue() == -1) {
					column.setLabelType(null);
				}
			}
			for (DcColumnWithLabelTypeBean column : columns) {
				for (DcColumnWithLabelTypeBean cfrColumn : columns) {
					if (column.getLabelType() != null && cfrColumn.getLabelType() != null) {
						if (column.getId().longValue() != cfrColumn.getId().longValue() &&
							column.getLabelType().longValue() == cfrColumn.getLabelType().longValue()) {
								String message = "Non è possibile associare a due o più campi lo stesso tipo etichetta";
								FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
								return FAILURE.outcome();
						}
					}				
				}
			}
			ltdbm.salva(userEntityId, sqlStatementBk, columns);
			cancellaForm();
			return SUCCESS.outcome();
		} catch (Exception e) {
			String message = "Errore nel salvataggio dei dati: ";
			message += e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
			e.printStackTrace();
		}
		return FAILURE.outcome();
	}

	public ArrayList<DcColumnWithLabelTypeBean> getColumns()
	{
		return columns;
	}

	public void setColumns(ArrayList<DcColumnWithLabelTypeBean> columns)
	{
		this.columns = columns;
	}

	public ArrayList<SelectItem> getLabelTypes()
	{
		return labelTypes;
	}

	public void setLabelTypes(ArrayList<SelectItem> labelTypes)
	{
		this.labelTypes = labelTypes;
	}

	public String getSqlStatement() {
		return sqlStatement;
	}

	public void setSqlStatement(String sqlStatement) {
		this.sqlStatement = sqlStatement;
	}

	public String getSqlStatementBk()
	{
		return sqlStatementBk;
	}

	public void setSqlStatementBk(String sqlStatementBk)
	{
		this.sqlStatementBk = sqlStatementBk;
	}

	public Long getUserEntityId()
	{
		return userEntityId;
	}

	public void setUserEntityId(Long userEntityId)
	{
		this.userEntityId = userEntityId;
	}

	public String getSqlStatementBkMsgDisplay() {
		return sqlStatementBkMsgDisplay;
	}

	public void setSqlStatementBkMsgDisplay(String sqlStatementBkMsgDisplay) {
		this.sqlStatementBkMsgDisplay = sqlStatementBkMsgDisplay;
	}
	
}
