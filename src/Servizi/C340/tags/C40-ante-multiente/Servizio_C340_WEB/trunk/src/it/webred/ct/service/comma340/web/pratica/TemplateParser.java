package it.webred.ct.service.comma340.web.pratica;

import it.webred.ct.service.comma340.data.access.C340CommonServiceException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.StyleSheet;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;


public class TemplateParser {
	
	private String templateName;
	private OutputStream os;
	private HashMap<String, String> keys = new HashMap<String, String>();
	
	public TemplateParser(String templateName, OutputStream os) {
		this.templateName = templateName;
		this.os = os;
	}
	
	/**
	 * Effettua la scansione del file template della pratica corrente 
	 * per riempire i campi con le informazioni inserite dall'operatore
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void parse() throws FileNotFoundException, IOException {
		File f = new File(templateName);
		FileInputStream fis = new FileInputStream(f);
		
		HWPFDocument doc = new HWPFDocument(fis);
		Range range = doc.getRange();
		
		String plainText = range.text(); 
		StyleSheet styleSheet = doc.getStyleSheet();

	    for (int i=0; i<range.numParagraphs(); i++){
	        Paragraph p = range.getParagraph(i);
	        CharacterRun cr = p.getCharacterRun(0);
	        try
	        {
		        String oldText = p.text();
		        p.replaceText(oldText, replaceTags(oldText), 0);
	        }
	        catch(Throwable t) {}
	    }
	    
	    doc.write(os);	    
	}
	
	/**
	 * Effettua la scansione della stringa corrente sostituendo 
	 * a ciascuna chiave della HashMap il valore corrispondente.
	 * 
	 * @param currentLine
	 * @return
	 */
	private String replaceTags(String currentLine) {
		String result = currentLine;
		Set<String> keySet = keys.keySet();
		Iterator<String> i = keySet.iterator();
		
		while (i.hasNext()) {
			String key = i.next();
			String val = keys.get(key);
			String code = "#" + key + "#";
			result = result.replaceAll(code, val);
		}
		
		return result;
	}

	public HashMap<String, String> getKeys() {
		return keys;
	}

	public void setKeys(HashMap<String, String> keys) {
		this.keys = keys;
	}
	
	

}
