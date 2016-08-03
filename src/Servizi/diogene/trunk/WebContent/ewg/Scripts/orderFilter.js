function order(orderField, orderType, st) {
	wait();
	document.mainform.orderField.value = orderField;
	document.mainform.orderType.value = orderType;
	document.mainform.ST.value = st;
	document.mainform.submit();
}
document.write("<input type=\"hidden\" name=\"orderField\" value=\"\">");
document.write("<input type=\"hidden\" name=\"orderType\" value=\"\">");