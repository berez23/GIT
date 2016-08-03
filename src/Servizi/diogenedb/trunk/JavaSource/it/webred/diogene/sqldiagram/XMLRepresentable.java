package it.webred.diogene.sqldiagram;

import java.util.List;
import org.jdom.Element;

public interface XMLRepresentable
{
	public List<Element> getXml() throws UnsupportedOperationException;
	public String getStringXml() throws UnsupportedOperationException;
}
