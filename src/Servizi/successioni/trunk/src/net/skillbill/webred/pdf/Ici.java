package net.skillbill.webred.pdf;

import it.webred.mui.http.MuiApplication;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.pdfbox.exceptions.COSVisitorException;

public class Ici extends TmplBase {

	public Ici(int pagesBody) throws IOException,
			COSVisitorException {
		super(new File("pdf/"+MuiApplication.belfiore+"_ModelloComunicazione2006.tmpl.pdf"), pagesBody);
		
	}

	public Ici(File tmpl,int pagesBody) throws IOException,
			COSVisitorException {
		super(tmpl, pagesBody);
		
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws COSVisitorException
	 */
	public static void main(String[] args) throws IOException,
			COSVisitorException {
		//un report ICI con due pagine "body"
		Ici qcsaf = new Ici( 2);

		//specializzazione ad-hoc
		qcsaf.CFFrontespizio("CSTNTN76C13C523H");
		
		//specializzazione ad-hoc. Codice in sezione "Tipologia Della Variazione"... 1 = Prima pagina "body"
		qcsaf.TDVCodice(1,"toto");
		
		//###### senza specializzazioni su ICI
		
		//una var nella pagina 0 (il frontespizio)
		qcsaf.replaceVar(0, "_v31", "Dom fiscale toto");
		
		Map replaces = new HashMap();
		replaces.put("_v27","via turati, 2");
		replaces.put("_v28","20032");
		replaces.put("_v29","Cormano");
		replaces.put("_v30","MI");
		
		//n-var in una botta sola nella pagina 0 (frontespizio) 
		qcsaf.replaceVar(0, replaces);
		
		//una var nella pagina 1 (prima body)
		qcsaf.replaceVar(1, "_v103", "CIAO!!!");

		//		una var nella pagina 1 (prima body)
		qcsaf.replaceVar(1, "_v112", "123");
//		... stessa nell pagina 2 (seconda body)
		qcsaf.replaceVar(2, "_v112", "456");

		System.out.println(qcsaf.actual());
	}

	

	public void TDVCodice(int i, String string) throws COSVisitorException, IOException {
		replaceVar(i,"_v95", string);
		
	}

	/**
	 * il codice fiscale della prima pagina
	 * @param string
	 * @throws IOException 
	 * @throws COSVisitorException 
	 */
	public void CFFrontespizio(String cf) throws COSVisitorException, IOException {
		Map replaces = new HashMap();
		for (int i = 0; i < 16; i++) {
		replaces.put("_v" + i, String.valueOf( cf.charAt(i)));
		}
		replaceVar(0, replaces);
		
	}

}
