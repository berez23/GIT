package it.webred.faces.components;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

/**
 * Componente che renderizza un icona stile Windows
 * @author Luigi Cecchini
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:33 $
 */
public class WinIco extends UICommand {
	private static final String FIRST_DIV_CLASS = "ext";
	private static final String INNER_DIV_CLASS = "int";
	private static final String IMAGE_CLASS = "obj";
	private static final String OPEN_ENTITY=".openEl";
	private static final String HIDDEN_FIELD=".hidd";
	private String imgSource;
	private String resName;
	private String resLabel;
	private boolean isClicked=false;
	/**
	*	
	*/
	public WinIco()
	{
		setRendererType(null);

	}
	/* (non-Javadoc)
	 * @see javax.faces.component.StateHolder#restoreState(javax.faces.context.FacesContext, java.lang.Object)
	 */
	public void restoreState(FacesContext context, Object state)
	{
		Object[] values = (Object[]) state;
		int i = values.length;
		super.restoreState(context, values[--i]);
		imgSource=(String) values[--i];
		resName=(String) values[--i];
		resLabel=(String) values[--i];


	}
	/* (non-Javadoc)
	 * @see javax.faces.component.StateHolder#saveState(javax.faces.context.FacesContext)
	 */
	public Object saveState(FacesContext context)
	{
		int i=4;
		Object[] values = new Object[i];
		values[--i] = super.saveState(context);
		values[--i] = imgSource;
		values[--i] = resName;
		values[--i] = resLabel;


		return values;
	}

	/**
	 * Realizza il decode del componente. Accoda un Actionevent al componente se si è fatto click su di esso
	 * @param context
	 */
	public void decode(FacesContext context)
	{
		Map requestMap=context.getExternalContext().getRequestParameterMap();
		if(requestMap.containsKey(getClientId(context)+OPEN_ENTITY+".x"))
		{
			setClicked(true);
			queueEvent(new ActionEvent(this));			
		}
	}	
	/* (non-Javadoc)
	 * @see javax.faces.component.UIComponent#encodeBegin(javax.faces.context.FacesContext)
	 */
	public void encodeBegin(FacesContext context)throws IOException
	{
		ResponseWriter writer=context.getResponseWriter();
		String clientId=getClientId(context);
		String formId;
		UIComponent parent=this;
	     while (!(parent instanceof UIForm)) parent = parent.getParent();      
	      formId= parent.getClientId(context); 
		writer.startElement("div",this);
		writer.writeAttribute("id",clientId,null);
		writer.writeAttribute("class",FIRST_DIV_CLASS,null);
		encodeImageButton(writer,formId,clientId);
		encodeText(writer);
		writer.endElement("div");
		
	}
	/**
	 * realizza l'encode dell'immagine legata all'icona
	 * @param writer
	 * @param formId
	 * @param clientId
	 * @throws IOException
	 */
	private void encodeImageButton(ResponseWriter writer,String formId,String clientId) throws IOException
	{
		writer.startElement("input",this);
		writer.writeAttribute("type","image",null);
		writer.writeAttribute("name",clientId+OPEN_ENTITY, null);		
		writer.writeAttribute("class",IMAGE_CLASS,null);
		writer.writeAttribute("onMouseUp","this.style.width='1.5cm';",null);
		String imageSrc=(String)this.getAttributes().get("imgSource");
		if(imageSrc!=null)writer.writeAttribute("src",imageSrc,null);
		writer.endElement("input");

		
	}
	/**
	 * realizza l'encode del testo legato all'icona
	 * @param writer
	 * @throws IOException
	 */
	private void encodeText(ResponseWriter writer)throws IOException
	{
		writer.startElement("div",this);
		writer.writeAttribute("class",INNER_DIV_CLASS,null);
		writer.startElement("span",this);
		writer.write(getResName());
		writer.endElement("span");
		writer.endElement("div");
	}
	/**
	 * @return
	 */
	public String getResName() {
		return resName;
	}
	/**
	 * @param resName
	 */
	public void setResName(String resName) {
		this.resName = resName;
	}
	/**
	 * @return
	 */
	public String getImgSource() {
		return imgSource;
	}
	/**
	 * @param imgSource
	 */
	public void setImgSource(String imgSource) {
		this.imgSource = imgSource;
	}
	/**
	 * @return
	 */
	public String getResLabel() {
		return resLabel;
	}
	/**
	 * @param resLabel
	 */
	public void setResLabel(String resLabel) {
		this.resLabel = resLabel;
	}
	/**
	 * @return
	 */
	public boolean isClicked() {
		return isClicked;
	}
	/**
	 * @param isClicked
	 */
	public void setClicked(boolean isClicked) {
		this.isClicked = isClicked;
	}

}
