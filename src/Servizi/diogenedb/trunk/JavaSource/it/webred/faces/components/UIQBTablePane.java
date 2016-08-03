package it.webred.faces.components;

import static it.webred.faces.utils.ComponentsUtil.getSelectItems;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.component.UICommand;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import static it.webred.utils.StringUtils.*;

/**
 * @author Giulio Quaresima
 * Component che visualizza una tabella del DB
 */
public class UIQBTablePane extends UICommand
{
	private static final String DEFAULT_CSS_CLASS = "dbTable";
	private static final String HEADER = ".header";
	private static final String CLOSE_BUTTON = ".close";
	private static final String CLIENT_POSITION_X = ".clientPositionX";
	private static final String CLIENT_POSITION_Y = ".clientPositionY";
	private static final String CLIENT_Z_INDEX = ".clientZIndex";
	private static final String DEFAULT_CLOSE_BUTTON_STRING = "X";
	private static final int NUM_OF_PROPERTIES_WHICH_STATE_SHOULD_BE_SAVED = 16;

	private static final String CLIENT_ID_KEY = "ID:";
	private static final String FRAME_KEY = "FRAME:";
	private static final String ITEM_KEY = "ITEM:";

	private String 
		styleClass,
		frameId,
		frameName,
		frameDescription,
		closeButtonImageURL,
		closeButtonString,
		closeButtonStyleClass,
		closeButtonStyle,
		onClick;
	
	private boolean selectable;
	private boolean closeable;
	private boolean dhtml = false;
	private boolean closed = false;
	
	private Integer 
		clientPositionX,
		clientPositionY;
	private Long
		clientZIndex;
	
	private Map<String,Boolean> selectedFrameItems;

	public UIQBTablePane()
	{
		setRendererType(null);
	}

	@Override
	public void restoreState(FacesContext context, Object state)
	{
		Object[] values = (Object[]) state;
		int i = values.length;
		super.restoreState(context, values[--i]);
		styleClass = (String) values[--i];
		frameId = (String) values[--i];
		frameName = (String) values[--i];
		frameDescription = (String) values[--i];
		closeButtonImageURL = (String) values[--i];
		closeButtonString = (String) values[--i];
		closeButtonStyleClass = (String) values[--i];
		closeButtonStyle = (String) values[--i];
		selectable = (Boolean) values[--i];
		closeable = (Boolean) values[--i];
		dhtml = (Boolean) values[--i];
		clientPositionX = (Integer) values[--i];
		clientPositionY = (Integer) values[--i];
		clientZIndex = (Long) values[--i];
		onClick = (String) values[--i];
	}

	@Override
	public Object saveState(FacesContext context)
	{
		int i = NUM_OF_PROPERTIES_WHICH_STATE_SHOULD_BE_SAVED;
		Object[] values = new Object[i];
		values[--i] = super.saveState(context);
		values[--i] = styleClass;
		values[--i] = frameId;
		values[--i] = frameName;
		values[--i] = frameDescription;
		values[--i] = closeButtonImageURL;
		values[--i] = closeButtonString;
		values[--i] = closeButtonStyleClass;
		values[--i] = closeButtonStyle;
		values[--i] = selectable;
		values[--i] = closeable;
		values[--i] = dhtml;
		values[--i] = clientPositionX;
		values[--i] = clientPositionY;
		values[--i] = clientZIndex;
		values[--i] = onClick;
		
		return values;
	}

	@Override
	public void decode(FacesContext context)
	{
		Map requestParams = context.getExternalContext().getRequestParameterMap();
		for (Object key : requestParams.keySet())
		{
			Matcher m = getSelectedItemRequestPattern(context).matcher(key.toString());
			if (m.find())
				getSelectedFrameItems().put(m.group(1), true);
			if (!isClosed() && key.toString().startsWith(getClientId(context) + CLOSE_BUTTON))
			{
				setClosed(true);
				queueEvent(new ActionEvent(this));
			}
			
			// MANTIENE LE COORDINATE DI VISUALIZZAZIONE
			if (key.toString().equals(getClientId(context) + CLIENT_POSITION_X))
				setClientPositionX(Integer.parseInt((String) requestParams.get(key)));
			if (key.toString().equals(getClientId(context) + CLIENT_POSITION_Y))
				setClientPositionY(Integer.parseInt((String) requestParams.get(key)));
			if (key.toString().equals(getClientId(context) + CLIENT_Z_INDEX))
				setClientZIndex(Long.parseLong((String) requestParams.get(key)));
		}
		
	}

	@Override
	public void encodeBegin(FacesContext context) throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();
		String clientId = getClientId(context);
		writer.write(CRLF);
		writer.startElement("div", this);
		writer.writeAttribute("id", clientId, null);
		if (getAttributes().get("styleClass") != null)
			writer.writeAttribute("class", getAttributes().get("styleClass"), null);
		else
			writer.writeAttribute("class", DEFAULT_CSS_CLASS, null);
		String style = "";
		if (getClientPositionX() != null)
			style += "left: " + getClientPositionX() + "px;"; 
		if (getClientPositionY() != null)
			style += "top: " + getClientPositionY() + "px;";
		if (getClientZIndex() != null)
			style += "z-index: " + getClientZIndex() + ";";
		
		if (!"".equals(style))
			writer.writeAttribute("style", style, null);
		
		encodeFrameHeader(writer, clientId);
	}

	@Override
	public void encodeChildren(FacesContext context) throws IOException
	{
		if (getChildCount() == 0)
			return;

		ResponseWriter writer = context.getResponseWriter();
		writer.write(CRLF);
		writer.write(getTab(3));
		writer.startElement("div", this);
		writer.writeAttribute("class", "content", null);
		writer.write(CRLF);
		writer.write(getTab(6));
		writer.startElement("table", this);

		List<SelectItem> items = getSelectItems(this);
		for (SelectItem item : items)
		{
			writer.write(CRLF);
			writer.write(getTab(9));
			writer.startElement("tr", this);
			writer.write(CRLF);
			writer.write(getTab(12));
			writer.startElement("td", this);
			writer.write(CRLF);
			writer.write(getTab(15));
			writer.startElement("span", this);
			writer.writeAttribute("class", item.getDescription(), null);
			writer.write(CRLF);
			writer.write(getTab(18));
			writer.write(item.getLabel());
			writer.write(CRLF);
			writer.write(getTab(15));
			writer.endElement("span");
			writer.write(CRLF);
			writer.write(getTab(12));
			writer.endElement("td");
			if (isSelectable())
			{
				writer.write(CRLF);
				writer.write(getTab(12));
				writer.startElement("td", this);
				writer.write(CRLF);
				writer.write(getTab(15));
				String name = CLIENT_ID_KEY + getClientId(context) + FRAME_KEY + getFrameId() + ITEM_KEY + item.getValue();
				writer.startElement("input", this);
				writer.writeAttribute("type", "checkbox", null);
				writer.writeAttribute("name", name, null);
				writer.writeAttribute("value", item.getValue(), null);
				if (checkSelected("" + item.getValue()))
					writer.writeAttribute("checked", "true", null);
				writer.write(CRLF);
				writer.write(getTab(12));
				writer.endElement("td");
			}
			writer.write(CRLF);
			writer.write(getTab(9));
			writer.endElement("tr");
		}
		writer.write(CRLF);
		writer.write(getTab(6));
		writer.endElement("table");
		writer.write(CRLF);
		writer.write(getTab(3));
		writer.endElement("div");
	}
	public boolean checkSelected(String key)
	{
		return getSelectedFrameItems().containsKey(key) && getSelectedFrameItems().get(key);
	}

	@Override
	public void encodeEnd(FacesContext context) throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();
		encodeHiddenFields(writer, getClientId(context));
		writer.endElement("div");
	}
	
	protected void encodeFrameHeader(ResponseWriter writer, String clientId) throws IOException
	{
		writer.write(CRLF);
		writer.write(getTab(3));
		writer.startElement("div", this);
		writer.writeAttribute("id", clientId + HEADER, null);
		writer.writeAttribute("class", "header", null);
		writer.write(CRLF);
		writer.write(getTab(6));
		writer.startElement("table", this);
		writer.write(CRLF);
		writer.write(getTab(9));
		writer.startElement("tr", this);
		writer.write(CRLF);
		writer.write(getTab(12));
		if (isCloseable())
			encodeCloseButton(writer, clientId);
		writer.startElement("td", this);
		if (isDhtml())
		{
			writer.writeAttribute("onmousedown", "beginMove(this.parentNode.parentNode.parentNode.parentNode.parentNode, event)", null);
			writer.writeAttribute("style", "cursor: move;", null);
		}
		String content = null;
		if ((content = getFrameName()) != null)
			writer.write(content);
		writer.write(CRLF);
		writer.write(getTab(12));
		writer.endElement("td");
		writer.write(CRLF);
		writer.write(getTab(9));
		writer.endElement("tr");
		writer.write(CRLF);
		writer.write(getTab(6));
		writer.endElement("table");
		writer.write(CRLF);
		writer.write(getTab(3));
		writer.endElement("div");
	}

	protected void encodeCloseButton(ResponseWriter writer, String clientId) throws IOException
	{
		writer.write(CRLF);
		writer.write(getTab(12));
		writer.startElement("td", this);
		writer.writeAttribute("width","18px",null);
		//writer.writeAttribute("id", clientId + CLOSE_BUTTON, null);
		writer.write(CRLF);
		writer.write(getTab(15));
		writer.startElement("input", this);
		if (getCloseButtonString() == null)
			setCloseButtonString(DEFAULT_CLOSE_BUTTON_STRING);
		if (getCloseButtonImageURL() != null)
		{
			writer.writeAttribute("type", "image", null);
			writer.writeAttribute("src", getCloseButtonImageURL(), null);
		}
		else
		{
			writer.writeAttribute("type", "submit", null);
			writer.writeAttribute("value", getCloseButtonString(), null);
		}
		if (getOnClick() != null && !"".equals(getOnClick().trim()))
			writer.writeAttribute("onclick", getOnClick(), null);
		
		writer.writeAttribute("name", clientId + CLOSE_BUTTON, null);
		writer.write(CRLF);
		writer.write(getTab(15));
		writer.endElement("input");
		writer.write(CRLF);
		writer.write(getTab(12));
		writer.endElement("td");
	}
	
	protected void encodeHiddenFields(ResponseWriter writer, String clientId) throws IOException
	{
		if (getClientPositionX() != null)
		{			
			writer.write(CRLF);
			writer.write(getTab(3));
			writer.startElement("input", this);
			writer.writeAttribute("type", "hidden", null);
			writer.writeAttribute("id", clientId + CLIENT_POSITION_X, null);
			writer.writeAttribute("name", clientId + CLIENT_POSITION_X, null);
			writer.writeAttribute("value", getClientPositionX(), null);
			writer.endElement("input");
		}
		if (getClientPositionY() != null)
		{			
			writer.write(CRLF);
			writer.write(getTab(3));
			writer.startElement("input", this);
			writer.writeAttribute("type", "hidden", null);
			writer.writeAttribute("id", clientId + CLIENT_POSITION_Y, null);
			writer.writeAttribute("name", clientId + CLIENT_POSITION_Y, null);
			writer.writeAttribute("value", getClientPositionY(), null);
			writer.endElement("input");
		}
		if (getClientZIndex() != null)
		{
			writer.write(CRLF);
			writer.write(getTab(3));
			writer.startElement("input", this);
			writer.writeAttribute("type", "hidden", null);
			writer.writeAttribute("id", clientId + CLIENT_Z_INDEX, null);
			writer.writeAttribute("name", clientId + CLIENT_Z_INDEX, null);
			writer.writeAttribute("value", getClientZIndex(), null);
			writer.endElement("input");			
		}
	}
	
	
	// GETTERS AND SETTERS //////////////////////////////////////////
	
	public String getFrameId()
	{
		return frameId;
	}

	public void setFrameId(String frameId)
	{
		this.frameId = frameId;
	}

	public String getFrameName()
	{
		return frameName;
	}

	public void setFrameName(String frameName)
	{
		this.frameName = frameName;
	}

	public String getStyleClass()
	{
		return styleClass;
	}

	public void setStyleClass(String styleClass)
	{
		this.styleClass = styleClass;
	}
	
	public String getFrameDescription()
	{
		return frameDescription;
	}

	public void setFrameDescription(String frameDescription)
	{
		this.frameDescription = frameDescription;
	}

	public boolean isSelectable()
	{
		return selectable;
	}

	public void setSelectable(boolean selectable)
	{
		this.selectable = selectable;
	}

	public String getCloseButtonImageURL()
	{
		return closeButtonImageURL;
	}

	public void setCloseButtonImageURL(String closeButtonImageURL)
	{
		this.closeButtonImageURL = closeButtonImageURL;
	}

	public String getCloseButtonStyle()
	{
		return closeButtonStyle;
	}

	public void setCloseButtonStyle(String closeButtonStyle)
	{
		this.closeButtonStyle = closeButtonStyle;
	}

	public String getCloseButtonStyleClass()
	{
		return closeButtonStyleClass;
	}

	public void setCloseButtonStyleClass(String closeButtonStyleClass)
	{
		this.closeButtonStyleClass = closeButtonStyleClass;
	}

	public String getCloseButtonString()
	{
		return closeButtonString;
	}

	public void setCloseButtonString(String closeButtonString)
	{
		this.closeButtonString = closeButtonString;
	}

	public boolean isClosed()
	{
		return closed;
	}

	public void setClosed(boolean closed)
	{
		this.closed = closed;
	}

	public Map<String,Boolean> getSelectedFrameItems()
	{
		if (selectedFrameItems == null)
			selectedFrameItems = new HashMap<String,Boolean>();
		return selectedFrameItems;
	}

	public void setSelectedFrameItems(Map<String,Boolean> selectedFrameItems)
	{
		this.selectedFrameItems = selectedFrameItems;
	}

	public boolean isDhtml()
	{
		return dhtml;
	}

	public void setDhtml(boolean dhtml)
	{
		this.dhtml = dhtml;
	}

	public Integer getClientPositionX()
	{
		return clientPositionX;
	}

	public void setClientPositionX(Integer clientPositionX)
	{
		this.clientPositionX = clientPositionX;
	}

	public Integer getClientPositionY()
	{
		return clientPositionY;
	}

	public void setClientPositionY(Integer clientPositionY)
	{
		this.clientPositionY = clientPositionY;
	}

	public String getOnClick()
	{
		return onClick;
	}

	public void setOnClick(String onClick)
	{
		this.onClick = onClick;
	}

	public Long getClientZIndex()
	{
		return clientZIndex;
	}

	public void setClientZIndex(Long clientZIndex)
	{
		this.clientZIndex = clientZIndex;
	}
	protected Pattern getSelectedItemRequestPattern(FacesContext context)
	{
		return Pattern.compile("\\A\\Q" + CLIENT_ID_KEY + getClientId(context) + FRAME_KEY + getFrameId() + ITEM_KEY + "\\E" + "(\\S+?)\\z");		
	}

	public boolean isCloseable()
	{
		return closeable;
	}

	public void setCloseable(boolean closeable)
	{
		this.closeable = closeable;
	}
}
