package it.webred.faces.tags;

import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.event.ActionEvent;
import javax.faces.webapp.UIComponentBodyTag;

/**
 * 
 * @author Giulio Quaresima
 *
 */
public class QBTablePaneTag extends UIComponentBodyTag
{
	private String 
		action,
		actionListener,
		styleClass,
		frameId,
		frameName,
		frameDescription,
		closeButtonImageURL,
		closeButtonString,
		closeButtonStyleClass,
		closeButtonStyle,
		selectable,
		closeable,
		dhtml,
		clientPositionX,
		clientPositionY;
	
	public void setStyleClass(String styleClass) {this.styleClass = styleClass;}
	public void setFrameDescription(String frameDescription) {this.frameDescription = frameDescription;}
	public void setFrameId(String frameId) {this.frameId = frameId;}
	public void setFrameName(String frameName) {this.frameName = frameName;}
	public void setCloseButtonImageURL(String closeButtonImageURL) {this.closeButtonImageURL = closeButtonImageURL;}
	public void setCloseButtonString(String closeButtonString) {this.closeButtonString = closeButtonString;}
	public void setCloseButtonStyleClass(String closeButtonStyleClass) {this.closeButtonStyleClass = closeButtonStyleClass;}
	public void setCloseButtonStyle(String closeButtonStyle) {this.closeButtonStyle = closeButtonStyle;}
	public void setSelectable(String selectable) {this.selectable = selectable;}
	public void setCloseable(String closeable) {this.closeable = closeable;}
	public void setDhtml(String dhtml) {this.dhtml = dhtml;}
	public void setClientPositionX(String clientPositionX) {this.clientPositionX = clientPositionX;}
	public void setClientPositionY(String clientPositionY) {this.clientPositionY = clientPositionY;}
	public void setAction(String action) {this.action = action;}
	public void setActionListener(String actionListener) {this.actionListener = actionListener;}

	@Override
	public void release()
	{
		// ALWAYS CALL THE SUPERCLASS METHOD
		super.release();		
		action = 
			actionListener = 
				styleClass = 
					frameId = 
						frameName = 
							frameDescription = 
								closeButtonImageURL = 
									closeButtonString = 
										closeButtonStyleClass = 
											closeButtonStyle = 
												selectable = 
													closeable =
														dhtml =
															clientPositionX =
																clientPositionY =
																	null;
	}
	
	@Override
	protected void setProperties(UIComponent component)
	{
		// ALWAYS CALL THE SUPERCLASS METHOD
		super.setProperties(component);
		
		setString(component, "action", action);
		setActionListener(component, "actionListener", actionListener);
		setString(component, "styleClass", styleClass);
		setString(component, "frameId", frameId);
		setString(component, "frameName", frameName);
		setString(component, "frameDescription", frameDescription);
		setString(component, "closeButtonImageURL", closeButtonImageURL);
		setString(component, "closeButtonString", closeButtonString);
		setString(component, "closeButtonStyleClass", closeButtonStyleClass);
		setString(component, "closeButtonStyle", closeButtonStyle);
		setBoolean(component, "selectable", selectable);
		setBoolean(component, "closeable", closeable);
		setBoolean(component, "dhtml", dhtml);
		setInteger(component, "clientPositionX", clientPositionX);
		setInteger(component, "clientPositionY", clientPositionY);
	}
	@SuppressWarnings("unchecked")
	private void setInteger(UIComponent component, String attributeName, String attributeValue)
	{
		if (attributeValue == null) return;
		if (isValueReference(attributeValue)) 
			setValueBinding(component, attributeName, attributeValue);
		else
		{
			Integer value = null;			
			try
			{
				value = Integer.parseInt(attributeValue);
			}
			catch (NumberFormatException e) 
			{
				return;
			}
			component.getAttributes().put(attributeName, value);
		}
	}
	@SuppressWarnings("unchecked")
	private void setBoolean(UIComponent component, String attributeName, String attributeValue)
	{
		if (attributeValue == null) return;
		if (isValueReference(attributeValue)) 
			setValueBinding(component, attributeName, attributeValue);
		else
			component.getAttributes().put(attributeName, attributeValue != null ? "true".equalsIgnoreCase(attributeValue.trim()) : false);
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
	private void setActionListener(UIComponent component, String attributeName, String attributeValue)
	{
		if (isValueReference(attributeValue))
		{
			setMethodBinding(component, attributeName, attributeValue, new Class[] {ActionEvent.class});
		}
	}
	@SuppressWarnings("unchecked")
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
	
	@Override
	public String getComponentType() {return "it.webred.faces.QBTablePane";}

	@Override
	public String getRendererType()	{return null;}
}
