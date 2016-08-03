package net.skillbill.webred.pdf.test;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import org.pdfbox.cos.COSArray;
import org.pdfbox.cos.COSDictionary;
import org.pdfbox.cos.COSInteger;
import org.pdfbox.cos.COSName;
import org.pdfbox.cos.COSObject;
import org.pdfbox.cos.COSString;
import org.pdfbox.exceptions.COSVisitorException;
import org.pdfbox.pdfparser.PDFStreamParser;
import org.pdfbox.pdfwriter.ContentStreamWriter;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.pdmodel.PDDocumentCatalog;
import org.pdfbox.pdmodel.PDPage;
import org.pdfbox.pdmodel.common.PDStream;
import org.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.pdfbox.pdmodel.interactive.form.PDField;
import org.pdfbox.pdmodel.interactive.form.PDTextbox;

public class Examples {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Examples fe = new Examples();
		//fe.replaceString("pdf/modelloIci.pdf","result.pdf");
		fe.introspect("/tmp/aaaa29395.pdf",0);
		//fe.setField("pdf/modelloIci.pdf", "FRANZ_FIELD", "totolo");
		//fe.printFields("/tmp/wrVAR9529.pdf");
		//fe.smanaccFields("/tmp/pdfWE2468.pdf");
		//PDFDebugger.main(new String[0]);
		
	}
	
	public void setField( String filename, String name, String value ) throws IOException, COSVisitorException
    {
		PDDocument pdfDocument = PDDocument.load( filename );
        PDDocumentCatalog docCatalog = pdfDocument.getDocumentCatalog();
        PDAcroForm acroForm = docCatalog.getAcroForm();
        PDField field = acroForm.getField( name );
        if( field != null )
        {
            field.setValue( value );
        }
        else
        {
            System.err.println( "No field found with name:" + name );
        }

        pdfDocument.save(filename );
    }

	
	public void introspect(String inputFile, int indexPage) throws IOException {
//		 the document
		PDDocument doc = null;
		try {
			doc = PDDocument.load(inputFile);
			
			//debugCos(doc.getDocumentCatalog().getStructureTreeRoot().getCOSDictionary());
			
			
			
			List pages = doc.getDocumentCatalog().getAllPages();
			
			//debugCos(doc.getDocumentCatalog().getCOSDictionary());
			
			 //debugDictionary("Contents", (COSDictionary) debugArray(1,(COSArray) debugDictionary("Kids", (COSDictionary) debugDictionary("Pages", doc.getDocumentCatalog().getCOSDictionary()))));
			
			//debugCos(doc.getDocumentCatalog().getStructureTreeRoot().getCOSDictionary());
		
			//for (int i = 0; i < pages.size(); i++) {
			//for (int i = 0; i < 1; i++) {
				PDPage page = (PDPage) pages.get(indexPage);
				PDStream contents = page.getContents();
			
				
				//debugCos(page.getCOSObject());
				
				//debugCos(page.getCOSDictionary());
				
				//debugCos(page.getContents().getCOSObject());
				
				List ann = page.getAnnotations();
				Iterator annIt = ann.iterator();
				while (annIt.hasNext()) {
					System.out.print(annIt.next());
				}
				
				
				
				PDFStreamParser parser = new PDFStreamParser(contents
						.getStream());
				parser.parse();
				List tokens = parser.getTokens();
			
				
				
				for (int j = 0; j < tokens.size(); j++) {
					Object next = tokens.get(j);
					
					debugCos(next);
					
				}
				
			//}
			
		} finally {
			if (doc != null) {
				doc.close();
			}
		}
	}

	private Object debugArray(int index, COSArray array) throws IOException {
		return debugCos( array.get(index));
		
	}

	private Object debugDictionary(String cosNameStre, COSDictionary dictionary) throws IOException {
		Object o = debugCos( dictionary.getDictionaryObject(cosNameStre));

		return o;
	}

	private Object debugCos(Object next) throws IOException {
		String logStr = logStr(next);
		
		System.out.println(logStr);
		
		if (next instanceof COSObject)
			next = ((COSObject) next).getObject();
		
		return next;
	}

	private String logStr(Object next) throws IOException {
		String result =next.getClass() + ":" + next; 
		
		if (next instanceof COSName) {
			COSName cos = (COSName) next;
			result = "cosname_" + cos.getName() ;// + ". " + cos.getCOSObject().getClass();
		}
		
		if (next instanceof COSInteger) {
			COSInteger cos = (COSInteger) next;
			result = "" + cos.getCOSObject();// + ". " + cos.getCOSObject().getClass();
		}
		
		if (next instanceof COSString) {
			COSString cos = (COSString) next;
			result = "COSSTRING : " + cos.getString();// + ". " + cos.getCOSObject().getClass();
		}
		
		
		
		if (next instanceof COSDictionary) {
			 
			COSDictionary cd = (COSDictionary)next;
			result = "COSDICTIONARY ["+ cd.getValues().size() +"] : {" ;
			 Iterator it = cd.getValues().iterator();
			 while (it.hasNext()) {
				 Object o = it.next();
				 result += "(" + logStr(cd.getKeyForValue(o)) + "=" + logStr(o) + ")";
			 }
			 result += "}";
		}
		
		if (next instanceof COSObject) {
			COSObject cos = (COSObject) next;
			//result = cos.getObject().getClass() + ":" + cos.getObject();
			result =  cos.getObject().toString();
			
			//result = logStr(cos.getObject());
		}
		
		if (next instanceof PDTextbox) {
			PDTextbox cos = (PDTextbox) next;
			result = "INPUT !!!!!!" + cos.getFullyQualifiedName() + " " + cos.getPartialName();
		}
		return result;
	}

	public void replaceString(String inputFile, String outputFile)
			throws IOException, COSVisitorException {
		// the document
		PDDocument doc = null;
		try {
			doc = PDDocument.load(inputFile);
			List pages = doc.getDocumentCatalog().getAllPages();
			for (int i = 0; i < pages.size(); i++) {
				PDPage page = (PDPage) pages.get(i);
				PDStream contents = page.getContents();
				PDFStreamParser parser = new PDFStreamParser(contents
						.getStream());
				parser.parse();
				List tokens = parser.getTokens();
				for (int j = 0; j < tokens.size(); j++) {
					Object next = tokens.get(j);
					
					if (next instanceof COSName) {
						System.out.println(((COSName)next).getName().equals("CF1"));
					} //else System.out.println(next);
					
					
					/*
					if (next instanceof PDFOperator) {
						PDFOperator op = (PDFOperator) next;
						// Tj and TJ are the two operators that display
						// strings in a PDF
						if (op.getOperation().equals("Tj")) {
							// Tj takes one operator and that is the string
							// to display so lets update that operator
							COSString previous = (COSString) tokens.get(j - 1);
							String string = previous.getString();
							
							string = string.replaceFirst(".", "/.x/");
							previous.reset();
							previous.append(string.getBytes());
						} else if (op.getOperation().equals("TJ")) {
							COSArray previous = (COSArray) tokens.get(j - 1);
							for (int k = 0; k < previous.size(); k++) {
								Object arrElement = previous.getObject(k);
								if (arrElement instanceof COSString) {
									COSString cosString = (COSString) arrElement;
									String string = cosString.getString();
									string = string.replaceFirst(".", "/.x/");
									cosString.reset();
									cosString.append(string.getBytes());
								}
							}
						}
					}*/
				}
				PDStream updatedStream = new PDStream(doc);
				OutputStream out = updatedStream.createOutputStream();
				ContentStreamWriter tokenWriter = new ContentStreamWriter(out);
				tokenWriter.writeTokens(tokens);
				page.setContents(updatedStream);
			}
			doc.save(outputFile);
		} finally {
			if (doc != null) {
				doc.close();
			}
		}
	}
	
	
	public void printFields( String filename ) throws IOException
    {
		PDDocument pdfDocument = PDDocument.load( filename );
        PDDocumentCatalog docCatalog = pdfDocument.getDocumentCatalog();
        PDAcroForm acroForm = docCatalog.getAcroForm();
        List fields = acroForm.getFields();
        Iterator fieldsIter = fields.iterator();

        System.out.println(new Integer(fields.size()).toString() + " top-level fields were found on the form");

        while( fieldsIter.hasNext())
        {
            PDField field = (PDField)fieldsIter.next();
               processField(field, "|--", field.getPartialName() + "(" + field.getFullyQualifiedName() +")");
        }
    }
	
	
	public void smanaccFields( String filename ) throws IOException, COSVisitorException
    {
		PDDocument pdfDocument = PDDocument.load( filename );
        PDDocumentCatalog docCatalog = pdfDocument.getDocumentCatalog();
        PDAcroForm acroForm = docCatalog.getAcroForm();
        //acroForm.setXFA( null ); 
        acroForm.setXFA(null);
        List fields = acroForm.getFields();
        Iterator fieldsIter = fields.iterator();

        System.out.println(new Integer(fields.size()).toString() + " top-level fields were found on the form");

        while( fieldsIter.hasNext())
        {
            PDField field = (PDField)fieldsIter.next();
               smanaccField(field);
        }
        
        pdfDocument.save(filename );
    }
	
	
	private void smanaccField(PDField field) throws IOException
    {
        List kids = field.getKids();
        if(kids != null)
        {
            Iterator kidsIter = kids.iterator();
        
            //System.out.println(sParent + " is of type " + field.getClass().getName());
            while(kidsIter.hasNext())
            {
               Object pdfObj = kidsIter.next();
               if(pdfObj instanceof PDField)
               {
                   PDField kid = (PDField)pdfObj;
                   smanaccField(kid);
               }
            }
         }
         else
         {
             if (field instanceof PDTextbox) {
            	 PDTextbox pd = (PDTextbox) field;
            	 //System.out.print(pd.getPartialName() + " = " + pd.getValue());
            	 pd.setValue("AAAA");
            	 
             }
         }
    }
	
	private void processField(PDField field, String sLevel, String sParent) throws IOException
    {
        List kids = field.getKids();
        if(kids != null)
        {
            Iterator kidsIter = kids.iterator();
            if(!sParent.equals(field.getPartialName()))
            {
               sParent = sParent + "." + field.getPartialName();
            }
            System.out.println(sLevel + sParent);
            //System.out.println(sParent + " is of type " + field.getClass().getName());
            while(kidsIter.hasNext())
            {
               Object pdfObj = kidsIter.next();
               if(pdfObj instanceof PDField)
               {
                   PDField kid = (PDField)pdfObj;
                   processField(kid, "|  " + sLevel, sParent);
               }
            }
         }
         else
         {
             String outputString = sLevel + sParent + "." + field.getPartialName() + " = " + field.getValue() +
                 ",  type=" + field.getClass().getName();

             System.out.println(outputString);
             
             debugCos(field.getCOSObject());
         }
    }



}
