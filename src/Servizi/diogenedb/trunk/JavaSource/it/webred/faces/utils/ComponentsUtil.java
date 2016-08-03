package it.webred.faces.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 * 
 * @author Giulio Quaresima
 *
 */
public class ComponentsUtil
{
	/**
	 * Restituisce il Client Id della UIForm 
	 * cui la UIComponent passata come argomento appartiene
	 * @param component
	 * @param context
	 * @return
	 */
	public static String getFormClientId(UIComponent component, FacesContext context)
	{
		UIComponent form = component;
		while (!(form instanceof UIForm))
			form = component.getParent();
		return form.getClientId(context);
	}

	/**
	 * Passando un UIComponent, restituisce la lista di tutti
	 * i SelectItem figli che essa possiede, sia che essi siano
	 * rappresentati da una serie di tag selectItem, sia che
	 * siano rappresentati da un selectItems
	 * @param component
	 * @return
	 */
	public static List<SelectItem> getSelectItems(UIComponent component)
	{
		ArrayList<SelectItem> list = new ArrayList<SelectItem>();

		List<? extends UIComponent> children = (List<? extends UIComponent>) component.getChildren();
		for (UIComponent child : children)
		{
			if (child instanceof UISelectItem)
			{
				Object value = ((UISelectItem) child).getValue();
				if (value == null)
				{
					UISelectItem item = (UISelectItem) child;
					list.add(new SelectItem(item.getItemValue(), item.getItemLabel(), item.getItemDescription(), item.isItemDisabled()));
				}
				else if (value instanceof SelectItem)
				{
					list.add((SelectItem) value);
				}
			}
			else if (child instanceof UISelectItems)
			{
				Object value = ((UISelectItems) child).getValue();
				if (value instanceof SelectItem)
					list.add((SelectItem) value);
				else if (value instanceof SelectItem[])
					list.addAll(Arrays.asList((SelectItem[]) value));
				else if (value instanceof Collection)
					list.addAll((Collection<? extends SelectItem>) value);
				else if (value instanceof Map)
				{
					Iterator entries = ((Map) value).entrySet().iterator();
					while (entries.hasNext())
					{
						Map.Entry entry = (Map.Entry) entries.next();
						list.add(new SelectItem(entry.getKey(), "" + entry.getValue()));
					}
				}
			}
		}
		return list;
	}

	/**
	 * Ordina la lista secondo l'ordine alfabetico
	 * delle label.
	 * @param l
	 * La lista da ordinare
	 * @param locale
	 * Parametro necessario soltanto se <i>ignoreCase</i>
	 * viene passato a true
	 * @param ignoreCase
	 * Se true, ordina di ordinare ignorando
	 * la differenza tra maiuscole e minuscole
	 */
	synchronized public static void sortSelectItemsList(List<SelectItem> l, Locale locale, final boolean ignoreCase)
	{
		final Locale thisLocale; 
		if (locale == null)
			thisLocale = Locale.getDefault();
		else
			thisLocale = locale;
		
		if (l != null)
			Collections.sort(l, 
					new Comparator<SelectItem>() {
						public int compare(SelectItem item1, SelectItem item2)
						{
							if (item1 == null)
								if (item2 == null)
									return 0;
								else
									return -1;
							else if (item2 == null)
								return 1;

							String s1 = item1.getLabel();
							String s2 = item2.getLabel();
							if (s1 == null)
								if (s2 == null)
									return 0;
								else
									return -1;
							else if (s2 == null)
								return 1;
							
							s1 = s1.trim();
							s2 = s2.trim();
								
							if (ignoreCase)
								return (s1.toLowerCase(thisLocale).compareTo(s2.toLowerCase(thisLocale)));
							else
								return (s1.compareTo(s2));
						}
				
			});		
	}
}
