package it.webred.diogene.querybuilder.control;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Observable;

public class SimpleRelationslist extends Observable implements List
{

	private List thisList;
	public SimpleRelationslist(List simplrelations)
	{
		thisList=simplrelations;
	}
	public int size()
	{
		return thisList.size();	
	}

	public boolean isEmpty()
	{
		return thisList.isEmpty();	
	}

	public boolean contains(Object o)
	{
		return thisList.contains(o);
	}

	public Iterator iterator()
	{
		return thisList.iterator();
	}

	public Object[] toArray()
	{
		
		return thisList.toArray();
	}

	public Object[] toArray(Object[] a)
	{
		
		return toArray(a);
	}

	public boolean add(Object o)
	{
		setChanged();
		notifyObservers();
		return thisList.add(o);
		
	}

	public boolean remove(Object o)
	{
		return thisList.remove(o);
	}

	public boolean containsAll(Collection c)
	{
		
		return thisList.containsAll(c);
	}

	public boolean addAll(Collection c)
	{
		
		return thisList.addAll(c);
	}

	public boolean addAll(int index, Collection c)
	{
		return thisList.addAll(index, c);
	}

	public boolean removeAll(Collection c)
	{
		return thisList.removeAll(c);
	}

	public boolean retainAll(Collection c)
	{
		return thisList.retainAll(c);
	}

	public void clear()
	{
		thisList.clear();
	}

	public Object get(int index)
	{
		return thisList.get(index);
	}

	public Object set(int index, Object element)
	{
		return thisList.set(index,element);
	}

	public void add(int index, Object element)
	{
		thisList.add(index, element);
	}

	public Object remove(int index)
	{
		return thisList.remove(index);
	}

	public int indexOf(Object o)
	{
		return thisList.indexOf(o);
	}

	public int lastIndexOf(Object o)
	{
		return thisList.lastIndexOf(o);
	}

	public ListIterator listIterator()
	{
		return thisList.listIterator();
	}

	public ListIterator listIterator(int index)
	{
		return thisList.listIterator(index);
	}

	public List subList(int fromIndex, int toIndex)
	{
		return thisList.subList(fromIndex, toIndex);
	}

}
