<jsp:useBean id="service" scope="request" class="DbMAP.management.beans.ServiceBean" />
<jsp:setProperty name="service" property="*" />
<jsp:forward page="/admin/DbMAPManagement" />