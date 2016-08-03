<%@include file="jspHead.jsp" %>
<div class="section">
<h2>DOCFA - FILTRO COMUNICAZIONI ICI</h2>
<div class="section">
<p>
Selezionare criteri di ricerca per Comunicazioni ICI</p>
<form action="" method="post">
<table class="bodyTable">
	<tbody>
		<tr class="a">
			<td class="a_title" align="center" colspan="3">DOCFA</td>
		</tr>
		<tr class="a">
			<td align="right">Campo</td>
			<td align="left">clausola</td>
			<td align="left">Valore</td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Numero Protocollo</td>
			<td align="left">uguale a</td>
			<td align="left"><input type="hidden" name="clause_iidProtocolloReg" value="EQUAL"/><input type="text" class="muiinput" value="" name="iidProtocolloReg"/></td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Data Fornitura</td>
			<td align="left"><select name="clause_iidFornitura"><option value="EQUAL">uguale a</option></select></td>
			<td align="left">
				<sql:query dataSource="jdbc/mui" var="data_fornitura">
					select distinct iid_fornitura from docfa_comunicazione order by iid_fornitura
				</sql:query>
				<select name="iidFornitura" id="iidFornitura">
					<option value=""></option>
					<c:forEach var="row" items="${data_fornitura.rows}">
						<option value="${row.iid_fornitura}">${row.iid_fornitura}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr class="a">
			<td class="a_title" align="center" colspan="3">TITOLARE IMMOBILI</td>
		</tr>
		<tr class="a">
			<td align="right">Campo</td>
			<td align="left">clausola</td>
			<td align="left">Valore</td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Codice Fiscale/Partita IVA</td>
			<td align="left">uguale a</td>
			<td align="left"><input type="hidden" name="clause_codfiscalePiva" value="EQUAL" /><input type="text" class="muiinput" value="" name="codfiscalePiva"/></td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Denominazione/Cognome</td>
			<td align="left"><select name="clause_denominazione"><option value="EQUAL">uguale a</option><option value="CONTAINS">contiene</option><option value="STARTS_WITH">inizia per</option><option value="ENDS_WITH">finisce per</option> </select></td>
			<td align="left"><input type="text" class="muiinput" value="" name="denominazione"/></td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Nome</td>
			<td align="left"><select name="clause_nome"><option value="EQUAL">uguale a</option><option value="CONTAINS">contiene</option><option value="STARTS_WITH">inizia per</option><option value="ENDS_WITH">finisce per</option></select></td>
			<td align="left"><input type="text" class="muiinput" value="" name="nome"/></td>
		</tr>
		<tr class="a">
			<td class="a_title" align="center" colspan="3">IMMOBILE</td>
		</tr>
		<tr class="a">
			<td align="right">Campo</td>
			<td align="left">clausola</td>
			<td align="left">Valore</td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Indirizzo</td>
			<td align="left"><select name="clause_indirizzo"><option value="EQUAL">uguale a</option><option value="CONTAINS">contiene</option><option value="STARTS_WITH">inizia per</option><option value="ENDS_WITH">finisce per</option></select></td>
			<td align="left"><input type="text" class="muiinput" value="" name="indirizzo"/></td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Foglio (Es.0001)</td>
			<td align="left">uguale a</td>
			<td align="left"><input type="hidden" name="clause_foglio" value="EQUAL" /><input type="text" class="muiinput" value="" name="foglio"/></td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Numero (Es.00001)</td>
			<td align="left">uguale a</td>
			<td align="left"><input type="hidden" name="clause_particella" value="EQUAL" /><input type="text" class="muiinput" value="" name="particella"/></td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Subalterno (Es.0001)</td>
			<td align="left">uguale a</td>
			<td align="left"><input type="hidden" name="clause_subalterno" value="EQUAL" /><input type="text" class="muiinput" value="" name="subalterno"/></td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Categoria</td>
			<td align="left">uguale a</td>
			<td align="left"><input type="hidden" name="clause_categoria" value="EQUAL" />
				<sql:query dataSource="jdbc/mui" var="categorie">
					select distinct categoria from docfa_comun_oggetto order by categoria
				</sql:query>
				<select name="categoria" id="categoria">
					<option value=""></option>
					<c:forEach var="row" items="${categorie.rows}">
						<option value="${row.categoria}">${row.categoria}</option>
					</c:forEach>
				</select>
			</td>
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
