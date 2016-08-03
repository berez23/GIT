package it.webred.diogene.sqldiagram;

/**
 * Le implementazioni di questa interfaccia devono
 * essere entit&agrave; che possano essere coerentemente
 * inserite nella clausola FROM di una SELECT, in
 * pratica delle tabelle, viste o sinonimi, oppure
 * delle query.
 * @see it.webred.diogene.sqldiagram.Query#addFromTable(Table)
 * @see it.webred.diogene.sqldiagram.Column#setTable(Table) 
 * @author Giulio Quaresima
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:32 $
 */
public interface Table extends Cloneable {
	
	/**
	 * @return Il nome SQL di questa entit&agrave;,
	 * utilizzato da {@link Query#toString()} e da  
	 * {@link Column#toString()} per rappresentarla
	 * correttamente.
	 */
	public String getExpression();
	
	/**
	 * @return L'alias SQL di questa entit&agrave;,
	 * utilizzato da {@link Query#toString()} e da  
	 * {@link Column#toString()} per rappresentarla
	 * correttamente.
	 */
	public String getAlias();
	
	/**
	 * @param alias
	 * L'alias SQL di questa entit&agrave;,
	 * utilizzato da {@link Query#toString()} e da  
	 * {@link Column#toString()} per rappresentarla
	 * correttamente.
	 */
	public void setAlias(String alias);
	
	/**
	 * @return
	 * Una nuova istanza di Table, clonata su questa
	 * istanza. Se esistono nell'implementazione 
	 * riferimenti a classi clonabili, dovranno essere
	 * clonati anch'essi. Se nell'implementazione esistono 
	 * {@link java.util.Collection collections},
	 * si dovranno clonare tutte le eventuali classi clonabili 
	 * contenute all'interno di tali collections.
	 * @throws CloneNotSupportedException
	 */
	public Table clone() throws CloneNotSupportedException;
}
