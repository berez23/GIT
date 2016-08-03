<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ page import="java.io.File" %>
<%@ page import="java.io.OutputStream" %>
<%@ page import="java.io.FileInputStream" %>
<%
String fileToFind = request.getParameter("file");
if(fileToFind == null) return;

File fname = new File(fileToFind);

if(!fname.exists()){
	out.println("Save As: " + fname.getAbsolutePath() );
}
FileInputStream istr = null;

response.setContentType("application/msword;charset=ISO-8859-1");
response.setHeader("Content-Disposition", "attachment; filename=\"" + fname.getName() + "\";");
try {

	ServletOutputStream sos = response.getOutputStream();
	istr = new FileInputStream(fname);
	int curByte = -1;

	while( (curByte = istr.read()) !=-1){
		sos.write(curByte);
	}
	sos.flush();
} catch(Exception ex){
	ex.printStackTrace(System.out);
} finally{
try {
if(istr!=null) 
	istr.close();
}catch(Exception ex){
	System.out.println("Major Error Releasing Streams: "+ex.toString());
}
}
try {
	response.flushBuffer();
} catch(Exception ex){
	System.out.println("Error flushing the Response: "+ex.toString());
}
%>