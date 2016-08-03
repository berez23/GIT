package it.webred.faces.tags;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.webapp.UIComponentTag;

public class HtmlBlockTag extends UIComponentTag
{
	private String 
		style,
		styleClass,
		htmlElementName;
	
	public void setStyle(String style) {this.style = style;}
	public void setStyleClass(String styleClass) {this.styleClass = styleClass;}
	public void setHtmlElementName(String htmlElementName) {this.htmlElementName = htmlElementName;}
	
	@Override
	public String getComponentType()
	{
		return "it.webred.faces.HtmlBlock";
	}
	
	@Override
	public String getRendererType() {return null;}

	@Override
	public void release()
	{
		// ALWAYS CALL THE SUPERCLASS METHOD
		super.release();
		htmlElementName = null;
	}

	@Override
	protected void setProperties(UIComponent component)
	{
		// ALWAYS CALL THE SUPERCLASS METHOD
		super.setProperties(component);
		
		setString(component, "style", style);
		setString(component, "styleClass", styleClass);
		setString(component, "htmlElementName", htmlElementName);
	}
	@SuppressWarnings("unchecked")
	private void setString(UIComponent component, String attributeName, String attributeValue)
	{
		if (attributeValue == null) return;
		if (isValueReference(attributeValue)) 
			setValueBinding(component, attributeName, attributeValue);
		else
			component.getAttributes().put(attributeName, attributeValue);		
	}
	private void setValueBinding(UIComponent component, String attributeName, String attributeValue)
	{
		component.setValueBinding(attributeName, FacesContext.getCurrentInstance().getApplication().createValueBinding(attributeValue));
	}
}
