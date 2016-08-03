package it.webred.faces.components;

import java.io.IOException;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

/**
 * 
 * @author Giulio Quaresima
 *
 */
public class UIHtmlBlock extends UIPanel
{
	protected static final String DEFAULT_HTML_ELEMENT = "div";
	
	private String 
		style,
		styleClass,
		htmlElementName;
	
	public UIHtmlBlock()
	{
		setRendererType(null);
	}

	@Override
	public boolean getRendersChildren()
	{
		return true;
	}
	
	@Override
	public void decode(FacesContext context)
	{
		super.decode(context);
	}

	@Override
	public void encodeBegin(FacesContext context) throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();
		if (getHtmlElementName() == null)
			setHtmlElementName(DEFAULT_HTML_ELEMENT);
		writer.startElement(getHtmlElementName(), this);
		writer.writeAttribute("id", getClientId(context), "clientId");
		if (getAttributes().get("styleClass") != null)
			writer.writeAttribute("class", getAttributes().get("styleClass"), null);
		if (getAttributes().get("style") != null)
			writer.writeAttribute("style", getAttributes().get("style"), null);
	}

	@Override
	public void encodeChildren(FacesContext context) throws IOException
	{
		if (getChildCount() == 0)
			return;
		List<? extends UIComponent> children = (List<? extends UIComponent>) getChildren();
		for (UIComponent item : children)
		{
			item.encodeBegin(context);
			item.encodeChildren(context);
			item.encodeEnd(context);
		}
	}

	@Override
	public void encodeEnd(FacesContext context) throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement(getHtmlElementName());
	}

	@Override
	public void restoreState(FacesContext context, Object state)
	{
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		setHtmlElementName(values[1] != null ? (String) values[1] : null);
		setStyle((String) values[2]);
		setStyleClass((String) values[3]);
		setHtmlElementName((String) values[4]);
	}

	@Override
	public Object saveState(FacesContext context)
	{
		Object[] values = new Object[5];
		values[0] = super.saveState(context); 
		values[1] = getHtmlElementName();
		values[2] = getStyle();
		values[3] = getStyleClass();
		values[4] = getHtmlElementName();
		return values;
	}

	public String getHtmlElementName()
	{
		return htmlElementName;
	}

	public void setHtmlElementName(String htmlElementName)
	{
		this.htmlElementName = htmlElementName;
	}

	public String getStyle()
	{
		return style;
	}

	public void setStyle(String style)
	{
		this.style = style;
	}

	public String getStyleClass()
	{
		return styleClass;
	}

	public void setStyleClass(String styleClass)
	{
		this.styleClass = styleClass;
	}
}
