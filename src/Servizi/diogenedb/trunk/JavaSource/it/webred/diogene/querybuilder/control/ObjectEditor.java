package it.webred.diogene.querybuilder.control;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

/**
 * Classe di utilit&agrave;, permette l'<i>undo</i>
 * nell'editazione di un qualsiasi oggetto clonabile.
 * Se noi vogliamo editare un oggetto, questa classe ci permette 
 * di modificare una nuova istanza clonata di quell'oggetto e,
 * se non vogliamo salvare le modifiche, di ritornare all'ultima
 * istanza salvata. 
 * @author Giulio Quaresima
 * @version $Revision: 1.4 $ $Date: 2008/01/24 13:32:30 $
 * @param <T>
 */
public class ObjectEditor<T extends Cloneable> implements Serializable, Cloneable, Switches
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3822747501956057393L;
	public static final int EDITING_STATUS = 2;
	public static final int SAVED_STATUS = 4;
	
	private T savedValue;
	private T editingValue;
	
	private boolean editable = true;
	
	public ObjectEditor() {}
	/**
	 * Crea un nuovo editor, impostanto come valore in editazione
	 * quello passato come paramtero.
	*	@param value
	*/
	public ObjectEditor(T value)
	{
		super();
		if (value == null) throw new NullPointerException();
		this.editingValue = value;
	}
	
	/**
	 * Restituisce l'ultima istanza salvata dell'oggetto
	 * contenuto, o null se non &egrave; mai stato eseguito un
	 * salvataggio. 
	 * @return
	 */
	public T getValue()
	{
		return savedValue;
	}
	/**
	 * @return l'oggetto correntemente in editazione,
	 * o <code>null</code> se lo stato corrente
	 * non e di editazione.
	 */
	public T getEditingValue()
	{
		return editingValue;
	}
	
	/**
	 * Passa allo stato di editazione. 
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("unchecked")
	public ObjectEditor<T> edit()
	throws IllegalArgumentException
	{
		T pispolo = savedValue;
		if (savedValue != null)
		{
			try
			{
				pispolo = (T) savedValue.getClass().getMethod("clone").invoke(savedValue);
				editingValue = pispolo;
			}
			catch (SecurityException e)	{}
			catch (NoSuchMethodException e)	
			{
				throw new IllegalArgumentException("perché mai implementare l'interfaccia Cloneable e non implementare il metodo clone() ?!?!?", e);				
			}
			catch (IllegalArgumentException e) {}
			catch (IllegalAccessException e) {}
			catch (InvocationTargetException e) 
			{
				if (e.getCause() instanceof CloneNotSupportedException)
					throw new IllegalArgumentException("perché mai implementare l'interfaccia Cloneable e non implementare correttamente il metodo clone() ?!?!?", e.getCause());
			}				
		}
		else if (editingValue == null)
			throw new IllegalStateException("ObjectEditor si trova in uno stato incoerente"); // TODO 
		return this;
	}
	
	/**
	 * Passa allo stato di editazione, ma invece
	 * di editare l'istanza salvata, clonandola, 
	 * edita quella passata come argomento.
	 * @param value
	 */
	public ObjectEditor<T> edit(T value)
	{
		if (value == null) throw new NullPointerException();
		editingValue = value;
		return this;
	}
	
	/**
	 * Salva il valore correntemente in editazione,
	 * e lo stato non &egrave; pu&ugrave; di editazione.
	 */
	public ObjectEditor<T> save()
	{
		savedValue = editingValue;
		editingValue = null;
		return this;
	}
	/**
	 * Salva il valore passato come argomento, sovrascrivendo
	 * quello eventualmente gi&agrave; passato.
	 * @param value
	 */
	public ObjectEditor<T> save(T value)
	{
		if (value == null) throw new NullPointerException();
		savedValue = value;
		editingValue = null;
		return this;
	}
	/**
	 * Lo stato non &egrave; pi&ugrave; in editazione, ma
	 * e il valore salvato rimane lo stesso precedente.
	 */
	public void undo()
	{
		if (savedValue != null)
			editingValue = null;
	}
	
	
	
	
	/**
	 * Clona questa classe, clonando anche
	 * gli eventuali valori salvati e in editazione.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ObjectEditor<T> clone() throws CloneNotSupportedException
	{
		ObjectEditor<T> clone = (ObjectEditor<T>) super.clone();
		if (savedValue != null)
		{
			T pispolo = null;
			try
			{
				pispolo = (T) savedValue.getClass().getMethod("clone").invoke(savedValue);
				clone.savedValue = pispolo;
			}
			catch (SecurityException e)	{}
			catch (NoSuchMethodException e)	
			{					
				throw new RuntimeException("perché mai implementare l'interfaccia Cloneable e non implementare correttamente il metodo clone() ?!?!?", e);				
			}
			catch (IllegalArgumentException e) {}
			catch (IllegalAccessException e) {}
			catch (InvocationTargetException e) 
			{
				if (e.getCause() instanceof CloneNotSupportedException)
					throw new RuntimeException("perché mai implementare l'interfaccia Cloneable e non implementare correttamente il metodo clone() ?!?!?", e.getCause());
			}
		}
		if (editingValue != null)
		{
			T pispolo = null;
			try
			{
				pispolo = (T) editingValue.getClass().getMethod("clone").invoke(editingValue);
				clone.editingValue = pispolo;
			}
			catch (SecurityException e)	{}
			catch (NoSuchMethodException e)	
			{// TODO					
				throw new RuntimeException("perché mai implementare l'interfaccia Cloneable e non implementare correttamente il metodo clone() ?!?!?", e);				
			}
			catch (IllegalArgumentException e) {}
			catch (IllegalAccessException e) {}
			catch (InvocationTargetException e) 
			{// TODO
				if (e.getCause() instanceof CloneNotSupportedException)
					throw new RuntimeException("perché mai implementare l'interfaccia Cloneable e non implementare correttamente il metodo clone() ?!?!?", e.getCause());
			}
		}
		return clone;
	}
	
	/**
	 * @return
	 * <ul>
	 * <li>{@link ObjectEditor#SAVED_STATUS SAVED_STATUS} 
	 * se sono stati chiamati almeno una volta i metodi
	 * {@link ObjectEditor#save()} o {@link ObjectEditor#save(T)}
	 * </li>
	 * <li>{@link ObjectEditor#EDITING_STATUS EDITING_STATUS} 
	 * se sono stati chiamati almeno una volta i metodi
	 * {@link ObjectEditor#edit()} o {@link ObjectEditor#edit(T)}
	 * oppure subito dopo la chiamata del costruttore
	 * {@link ObjectEditor#ObjectEditor(T) ObjectEditor(T)}
	 * </li>
	 * </ul>
	 * I due stati possono sovrapporsi: testarli con gli
	 * operatori <i>bitwise</i> <code>&</code> e <code>|</code>.
	 */
	public int getStatus()
	{
		if (savedValue != null)
			if (editingValue != null)
				return SAVED_STATUS | EDITING_STATUS;
			else
				return SAVED_STATUS;
		else
			if (editingValue != null)
				return EDITING_STATUS;
			else
				return 0;
	}
	
	
	/* (non-Javadoc)
	 * @see it.webred.diogene.querybuilder.control.Switches#isEditable()
	 */
	public boolean isEditable()
	{
		return editable;
	}
	/* (non-Javadoc)
	 * @see it.webred.diogene.querybuilder.control.Switches#setEditable(boolean)
	 */
	public void setEditable(boolean editable)
	{
		this.editable = editable;
	}
}
