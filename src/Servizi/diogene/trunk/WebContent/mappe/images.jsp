<%@page import="java.io.ByteArrayInputStream"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.io.File"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.BufferedInputStream"%>
<%@page import="com.lowagie.text.DocumentException"%>
<%@page import="com.lowagie.text.pdf.PdfWriter"%>
<%@page import="com.lowagie.text.Document"%>
<%@page import="com.lowagie.text.Rectangle"%>
<%@page import="com.lowagie.text.Image"%>

<%
	try {
		String path = request.getParameter("path");
		if (path.toLowerCase().endsWith(".pdf")) {
			File pdfFile = new File(path);
			InputStream isByte = new FileInputStream(pdfFile);				
			BufferedInputStream bis = new BufferedInputStream(isByte);
			response.setContentType("application/pdf");
	        PrintWriter pw = response.getWriter();
	        int count = 0;
	        while ((count = bis.read()) >= 0){
	            pw.write(count);                    
	        }
	        bis.close();
	        pw.flush();
	        pw.close();	
		} else {
			Image jpg = Image.getInstance(path);
		    float imgHeight = jpg.height() + 100;
		    float imgWidth = jpg.width() + 100;
		   	Rectangle pageSize = new Rectangle(imgWidth, imgHeight);
		    Document document = new Document(pageSize);
		    response.setContentType("application/pdf");
		  	//PdfWriter.getInstance(document, response.getOutputStream());
		    //document.open();
		    //document.add(jpg);
		    //document.close();
		  	//il codice commentato dava errore:
		  	//java.lang.IllegalStateException: getOutputStream() has already been called for this response
	        //sostituito da:
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();		     	    
		    PdfWriter pdfw = PdfWriter.getInstance(document, baos);
		    pdfw.setEncryption(PdfWriter.STRENGTH40BITS, null, null, 0);
		  	document.open();
		    document.add(jpg);
		    document.close();
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
	        PrintWriter pw = response.getWriter();
	        int count = 0;
	        while ((count = bais.read()) >= 0){
	            pw.write(count);                    
	        }
	        bais.close();
	        pw.flush();
	        pw.close();
	        //fine sostituito
		}
	} catch(DocumentException de) {
	  	de.printStackTrace();
	  	throw de;
	}
%>