package it.webred.diogene.sqldiagram;

import java.util.List;

import org.jdom.Element;

/**
 * Questa classe rappresenta un particolare tipo di valore che
 * &egrave; quello che ci si aspetta, in SQL, nel contesto
 * dell'operatore IN.
 * @see it.webred.diogene.sqldiagram.MembersList
 * @see it.webred.diogene.sqldiagram.Query
 * @author Giulio Quaresima
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:32 $
 */
public abstract class Membership extends ValueExpression
{
	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.ValueExpression#getDefinition()
	 */
	public String getDefinition() {
		return "Insieme di valori";
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Membership clone() throws CloneNotSupportedException
	{
		return (Membership) super.clone();
	}
	/* (non-Javadoc)
	 * @see it.webred.diogene.sqldiagram.XMLRepresentable#getXml()
	 */
	@Override
	public List<Element> getXml()
	{
		throw new UnsupportedOperationException();
	}
}
