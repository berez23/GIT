package it.webred.faces.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

/**
 * <h3>NOTE DI IMPLEMENTAZIONE</h3>
 * <p>
 * Poich&eacute; gli oggetti che registrano i propri <i>listener</i> hanno un ciclo di
 * vita che pu&ograve; essere inferiore a quello di questo PhaseListener,
 * i riferimenti ad essi vengono mantenuti attraverso dei riferimenti deboli,
 * utilizzando una {@link java.util.WeakHashMap WeakHashMap}. In questo modo, 
 * scaduta la request o la sessione, questi oggetti possono essere 
 * tranquillamente eliminati dalla <i>Garbage Collection</i>
 * </p>
 * @author Giulio Quaresima
 */
public class Supervisor implements PhaseListener
{
	private static final long serialVersionUID = 3551994079449862381L;
	
	private final Logger log = Logger.getLogger(this.getClass());

	private boolean logOn = true;

	private Map<PhaseId,WeakHashMap<Object,Method>> registeredBeforeInstances;
	private Map<PhaseId,WeakHashMap<Object,Method>> registeredAfterInstances;
	{
		registeredBeforeInstances = new HashMap<PhaseId,WeakHashMap<Object,Method>>();			
		registeredAfterInstances = new HashMap<PhaseId,WeakHashMap<Object,Method>>();		
	}
	
	/**
	 * Registra un metodo per essere invocato durante la fase dichiarata.
	 * @param id
	 * L'id della fase prima o dopo della quale chiamare il metodo.
	 * @param invokeAfterPhase
	 * Se true, il metodo viene invocato dopo la fase specificata, 
	 * altrimenti prima.
	 * @param methodName
	 * Il nome del metodo. Il metodo passato deve avere visibilità
	 * da questa istanza di Supervisor, e non deve avere argomenti.
	 * @param instance
	 * Il riferimento all'istanza dell'oggetto. Questo riferimento verrà
	 * salvato come riferimento debole in una {@link java.util.WeakHashMap WeakHashMap},
	 * per cui l'istanza potrà andare liberamente in Garbage Collection
	 * una volra scaduta la request o la sessione cui appartiene.
	 * @throws Exception
	 */
	public void registerListener(PhaseId id, boolean invokeAfterPhase, String methodName, Object instance) throws Exception
	{
		Map<PhaseId,WeakHashMap<Object,Method>> map = null;
		if (invokeAfterPhase)
			map = registeredAfterInstances;
		else
			map = registeredBeforeInstances;
		if (!map.containsKey(id))
			map.put(id, new WeakHashMap<Object,Method>());
		
		try
		{
			Method method = instance.getClass().getMethod(methodName);
			map.get(id).put(instance, method);
		}
		catch (SecurityException e) {throw e;}
		catch (NoSuchMethodException e)	{throw e;}
	}

	public void beforePhase(PhaseEvent event)
	{
		if (isLogOn())
		{
			log.debug("\t> " + event.getPhaseId().toString());
			if (event.getFacesContext() != null && event.getFacesContext().getViewRoot() != null)
				log.debug("\t\tview-id: " + event.getFacesContext().getViewRoot().getViewId());
		}
		if (registeredBeforeInstances.containsKey(event.getPhaseId()))
		{
			for (Map.Entry<Object,Method> item : registeredBeforeInstances.get(event.getPhaseId()).entrySet())
			{
				synchronized (item.getKey())
				{
					try
					{
						item.getValue().invoke(item.getKey());
					}
					catch (IllegalArgumentException e) {}
					catch (IllegalAccessException e) {}
					catch (InvocationTargetException e) {}
				}
			}
		}
	}

	public void afterPhase(PhaseEvent event)
	{
		if (isLogOn())
		{
			if (event.getFacesContext() != null && event.getFacesContext().getViewRoot() != null)
				log.debug("\t\tview-id: " + event.getFacesContext().getViewRoot().getViewId());
			log.debug("\t< " + event.getPhaseId().toString());
		}
		if (registeredAfterInstances.containsKey(event.getPhaseId()))
		{
			for (Map.Entry<Object,Method> item : registeredAfterInstances.get(event.getPhaseId()).entrySet())
			{
				synchronized (item.getKey())
				{
					try
					{
						item.getValue().invoke(item.getKey());
					}
					catch (IllegalArgumentException e) {}
					catch (IllegalAccessException e) {}
					catch (InvocationTargetException e) {}
				}
			}
		}
		/* 
		// STAMPA TUTTI GLI HEADER HTTP DELLA REQUEST
		if (event.getPhaseId().equals(PhaseId.INVOKE_APPLICATION))
		{
			HttpServletRequest req = (HttpServletRequest) event.getFacesContext().getExternalContext().getRequest();
			Enumeration enumer = req.getHeaderNames();
			for (Object key : Collections.list(enumer))
			{
				log.debug(req.getHeader(key.toString()));
			}
		}
		*/
	}

	public PhaseId getPhaseId()
	{
		return PhaseId.ANY_PHASE;
	}

	public boolean isLogOn()
	{
		return logOn;
	}

	public void setLogOn(boolean logOn)
	{
		this.logOn = logOn;
	}
}
