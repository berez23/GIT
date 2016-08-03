package it.webred.faces.tags;

import javax.faces.application.Application;
import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;
import javax.faces.webapp.UIComponentTag;

/**
 * Trasferisce i valori degli attributi del tag agli attributi del componente
 * @author Luigi Cecchini
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:33 $
 */
public class WinIcoTag extends UIComponentTag {
	
	/**
	 *
	 */
	private String imgSource;
	/**
	 *
	 */
	private String resName;
	/**
	 *
	 */
	private String actionListener;


	/* (non-Javadoc)
	 * @see javax.faces.webapp.UIComponentTag#getComponentType()
	 */
	@Override
	public String getComponentType() {
		// TODO Auto-generated method stub
		return "it.webred.faces.WinIco";
	}

	/* (non-Javadoc)
	 * @see javax.faces.webapp.UIComponentTag#getRendererType()
	 */
	@Override
	public String getRendererType() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param imgSource
	 */
	public void setImgSource(String imgSource) {
		this.imgSource = imgSource;
	}

	/* (non-Javadoc)
	 * @see javax.faces.webapp.UIComponentTag#setProperties(javax.faces.component.UIComponent)
	 */
	public void setProperties(UIComponent component)
	{
		super.setProperties(component);
		setString(component,"imgSource",imgSource);
		setString(component,"resName",resName);
		setActionListener(component, "actionListener", actionListener);

	}
	/**
	 * aggiunge un attributo Stringa alla mappa degli attributi del componente.
	 * @param component
	 * @param attributeName
	 * @param attributeValue
	 */
	public void setString(UIComponent component,String attributeName,String attributeValue)
	{
		if(attributeValue==null)return;
		if(isValueReference(attributeValue))
			setValueBinding(component,attributeName,attributeValue);
		else
			component.getAttributes().put(attributeName,attributeValue);
	}
	/**
	 * aggiunge un ValueBinding al componente
	 * @param component
	 * @param attributeName
	 * @param attributeValue
	 */
	public void setValueBinding(UIComponent component,String attributeName,String attributeValue)
	{
		FacesContext context=FacesContext.getCurrentInstance();
		Application app=context.getApplication();
		ValueBinding vb=app.createValueBinding(attributeValue);
		component.setValueBinding(attributeName,vb);
	}
	/**
	 * aggiunge un actionListener al componente
	 * @param component
	 * @param attributeName
	 * @param attributeValue
	 */
	private void setActionListener(UIComponent component, String attributeName, String attributeValue)
	{
		if (isValueReference(attributeValue))
		{
			setMethodBinding(component, attributeName, attributeValue, new Class[] {ActionEvent.class});
		}
	}
	/**
	 * costruisce un MethodBinding per aggiungere un ActionListener al componenete
	 * @param component
	 * @param attributeName
	 * @param attributeValue
	 * @param paramsTypes
	 */
	private void setMethodBinding(UIComponent component, String attributeName, String attributeValue, Class[] paramsTypes)
	{
		if (attributeValue == null)
			return;
		/*
		component.getAttributes().put(
				attributeName, 
				FacesContext.getCurrentInstance().getApplication().createMethodBinding(attributeValue, paramsTypes));
				*/
		MethodBinding mb = FacesContext.getCurrentInstance().getApplication().createMethodBinding(attributeValue, paramsTypes);
		((ActionSource) component).setActionListener(mb);
	}	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#release()
	 */
	public void release()
	{
		super.release();
		imgSource=null;
		resName=null;
	}

	/**
	 * @param resName
	 */
	public void setResName(String resName) {
		this.resName = resName;
	}

	/**
	 * @param actionListener
	 */
	public void setActionListener(String actionListener) {
		this.actionListener = actionListener;
	}
}
