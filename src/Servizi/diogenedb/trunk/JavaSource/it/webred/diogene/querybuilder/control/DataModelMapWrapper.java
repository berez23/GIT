package it.webred.diogene.querybuilder.control;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.faces.model.DataModel;

/**
 * Questa classe &egrave; un <i>wrapper</i> da utilizzare
 * per passare una {@link java.util.Map} ad un <code>dataTable</code>
 * di JSF. La tabella scorrer&agrave; i valori della mappa
 * passata come se fossero in un {@link java.util.ArrayList},
 * tralasciando le chiavi.
 * @author Giulio Quaresima
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:33 $
 * @param <T>
 * Il tipo dei valori della mappa passata al costruttore
 */
public class DataModelMapWrapper<T> extends DataModel
{
	private Map<? extends Object,? extends T> map;
	private int position = -1;
	
	private DataModelMapWrapper() {}
	/**
	*	@param map
	* Una mappa che si vuole passare come valore a
	* un <code>dataTable</code> di JSF.
	*/
	public DataModelMapWrapper(Map<? extends Object,? extends T> map)
	{
		this.map = map;
	}
	
	/* (non-Javadoc)
	 * @see javax.faces.model.DataModel#getRowCount()
	 */
	@Override
	public int getRowCount()
	{
		return new ArrayList<Map.Entry<? extends Object,? extends T>>((Collection<? extends Map.Entry<? extends Object,? extends T>>) map.entrySet()).size();
	}

	/* (non-Javadoc)
	 * @see javax.faces.model.DataModel#getRowData()
	 */
	@Override
	public Map.Entry<? extends Object,? extends T> getRowData()
	{
		if (position > -1 && position < new ArrayList<Map.Entry<? extends Object,? extends T>>((Collection<? extends Map.Entry<? extends Object,? extends T>>) map.entrySet()).size())
			return new ArrayList<Map.Entry<? extends Object,? extends T>>((Collection<? extends Map.Entry<? extends Object,? extends T>>) map.entrySet()).get(position);
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.faces.model.DataModel#getRowIndex()
	 */
	@Override
	public int getRowIndex()
	{
		return position;
	}

	/* (non-Javadoc)
	 * @see javax.faces.model.DataModel#getWrappedData()
	 */
	@Override
	public Object getWrappedData()
	{
		return map;
	}

	/* (non-Javadoc)
	 * @see javax.faces.model.DataModel#isRowAvailable()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean isRowAvailable()
	{
		return position < new ArrayList((Collection<? extends Map.Entry>) map.entrySet()).size();
	}

	/* (non-Javadoc)
	 * @see javax.faces.model.DataModel#setRowIndex(int)
	 */
	@Override
	public void setRowIndex(int rowIndex)
	{
		this.position = rowIndex;
	}

	/* (non-Javadoc)
	 * @see javax.faces.model.DataModel#setWrappedData(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setWrappedData(Object data)
	{
		this.map = (Map<? extends Object,? extends T>) data;
		position = -1;
	}
}
