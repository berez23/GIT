<%@include file="jspHead.jsp" %>

<script language="javascript" type="text/javascript">
<!--

function validate(form) {
  if (form.anno.value == '') {
    alert("Selezionare un anno relativamente al quale effettuare l\'estrazione");
    return false;
  }
  if (form.mese.value == '') {
    alert("Selezionare un mese relativamente al quale effettuare l\'estrazione");
    return false;
  }
  return true;
}

// -->
</script>

<div class="section">
<h2>SUCCESSIONI - CARICAMENTO FORNITURE DA DB</h2>
<div class="section">
<p>Selezionare anno e mese di estrazione</p>
<form action="" method="post" onsubmit="return validate(this);">
<table class="bodyTable">
	<tbody>
		<tr class="a">
			<td align="left">Scegli anno</td>
			<td align="left">Scegli mese</td>
			<td align="left">Vai</td>
		</tr>
		<tr class="b">
			<td align="left">
				<select name="anno">
					<option value="">Seleziona</option>
					<% 
						java.util.Calendar now = java.util.Calendar.getInstance();
						int annoCurr = now.get(java.util.Calendar.YEAR);
						for (int i = annoCurr; i >= (annoCurr - 10); i--) {
					%>
						<option value="<%=i%>"><%=i%></option>
					<%
						}
					%>
				</select>				
			</td>
			<td align="left">
				<select name="mese">
					<option value="">Seleziona</option>
					<% 
						for (int ij = 1; ij <= 12; ij++) {
							String labelMese = (ij > 9) ? ("" + ij) : ("0" + ij);
					%>
						<option value="<%=ij%>"><%=labelMese%></option>
					<%
						}
					%>
				</select>				
			</td>
			<td align="left"><input type="submit" class="muiinput" value="Vai!" name="Vai!"/>
		</td>
		</tr>
	</tbody>
</table>
</form>
</div>
</div>
