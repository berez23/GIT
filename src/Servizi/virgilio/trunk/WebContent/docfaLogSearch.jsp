<%@include file="jspHead.jsp" %>
<div class="section">
<h2>DOCFA - FILTRO LOG DOCFA</h2>
<div class="section">
<p>
Selezionare criteri di ricerca per i log Docfa</p>
<form action="" method="post">
<table class="bodyTable">
	<tbody>
		<tr class="a">
			<td class="a_title" align="center" colspan="3">DATI LOG</td>
		</tr>
		<tr class="a">
			<td align="right">Campo</td>
			<td align="left">clausola</td>
			<td align="left">Valore</td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Numero Protocollo</td>
			<td align="left"><select name="clause_idProtocollo"><option value="EQUAL">uguale a</option><option value="CONTAINS">contiene</option><option value="STARTS_WITH">inizia per</option><option value="ENDS_WITH">finisce per</option></select></td>
			<td align="left"><input type="text" class="muiinput" value="" name="idProtocollo"/></td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Tipo</td>
			<td align="left">uguale a</td>
			<td align="left"><input type="hidden" name="clause_provenienza" value="EQUAL"/><select name="proveninza">
								<option value="">tutti</option>
								<option value="IMP">Importazione</option>
								<option value="INT">Integrzione</option>
			</td>
			
		</tr>
		<tr class="a">
			<td class="a_title" align="center" colspan="3">DICHIARANTE</td>
		</tr>
	


		<tr class="b">
			<td class="a" align="right">Foglio(es. 0001)</td>
			<td align="left">uguale a</td>
			<td align="left"><input type="hidden" name="clause_uiuFoglio" value="EQUAL"/><input type="text" class="muiinput" value="" name="uiuFoglio"/></td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Particella(es. 00001)</td>
			<td align="left">uguale a</td>
			<td align="left"><input type="hidden" name="clause_uiuNumero" value="EQUAL"/><input type="text" class="muiinput" value="" name="uiuNumero"/></td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Subalterno(es. 0001)</td>
			<td align="left">uguale a</td>
			<td align="left"><input type="hidden" name="clause_uiuSubalterno" value="EQUAL"/><input type="text" class="muiinput" value="" name="uiuSubalterno"/></td>
		</tr>
			<tr class="b">
			<td align="right"></td>
			<td align="left"><input type="submit" class="muiinput" value="Vai" name="Vai"/></td>
			<td align="left"><input type="reset" class="muiinput" value="Reset" name="Reset"/></td>
		</tr>
	</tbody>
</table>
</form>
</div>
</div>
