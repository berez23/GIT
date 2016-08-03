<%@include file="jspHead.jsp" %>
<div class="section">
<h2>MUI - FILTRO NOTE IMMOBILI</h2>
<div class="section">
<p>
Selezionare criteri di ricerca per Note di Trascrizione</p>
<form action="" method="post">
<table class="bodyTable">
	<tbody>
		<tr class="a">
			<td class="a_title" align="center" colspan="3">NOTA</td>
		</tr>
		<tr class="a">
			<td align="right">Campo</td>
			<td align="left">clausola</td>
			<td align="left">Valore</td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Numero Nota</td>
			<td align="left">uguale a</td>
			<td align="left"><input type="hidden" name="clause_numeroNotaTras" value="EQUAL"/><input type="text" class="muiinput" value="" name="numeroNotaTrasLong"/></td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Anno Nota</td>
			<td align="left"><select name="clause_annoNota"><option value="YEAR_EQUAL">uguale a</option><option value="YEAR_GREATER_OR_EQUAL_THAN">maggiore o uguale a</option><option value="YEAR_SMALLER_OR_EQUAL_THAN"> minore o uguale a</option> </select></td>
			<td align="left">
				<sql:query dataSource="jdbc/mui" var="anni_nota">
					select distinct anno_nota from mi_dup_nota_tras order by anno_nota
				</sql:query>
				<select name="annoNota" id="annoNota">
					<option value=""></option>
					<c:forEach var="row" items="${anni_nota.rows}">
						<option value="${row.anno_nota}">${row.anno_nota}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Data Rogito</td>
			<td align="left"><select name="clause_dataValiditaAtto"><option value="DATE_EQUAL">uguale a</option><option value="DATE_GREATER_OR_EQUAL_THAN">maggiore o uguale a</option><option value="DATE_SMALLER_OR_EQUAL_THAN"> minore o uguale a</option> </select></td>
			<td align="left">
				<input type="text" class="muiinput" value="" id="dataValiditaAtto" name="dataValiditaAtto" readonly="readonly"/>
				<img title='Inserisci Data' onclick="InserisciData('dataValiditaAtto')" src="img/calendario.gif" border="1" alt="Inserisci Data"/> &nbsp;
				<img title="Rimuovi" onclick="document.getElementById('dataValiditaAtto').value='';" src="img/del.gif" border="1" alt="Rimuovi"/>
			</td>
		</tr>
		<tr class="b">
			<td class="a" align="right">ID Nota</td>
			<td align="left">uguale a</td>
			<td align="left"><input type="hidden" name="clause_iid@nota" value="EQUAL"/><input type="text" class="muiinput" value="" name="iid@nota"/></td>
		</tr>
		<tr class="a">
			<td class="a_title" align="center" colspan="3">SOGGETTO PER NOTA</td>
		</tr>
		<tr class="a">
			<td align="right">Campo</td>
			<td align="left">clausola</td>
			<td align="left">Valore</td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Tipo Soggetto</td>
			<td align="left">&nbsp;<input type="hidden" name="tipoSoggetto" value="P"/></td>
			<td align="left"><select name="clause_tipoSoggetto"><option value="IGNORE"></option><option value="EQUAL">Persona Fisica</option><option value="NOT_EQUAL">Persona Giuridica</option></select></td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Tipo Soggetto per Nota</td>
			<td align="left">&nbsp;<input type="hidden" id="scFlagTipoTitolNota" name="scFlagTipoTitolNota" value=""/><input type="hidden" name="clause_scFlagTipoTitolNota" value="EQUAL"/><input type="hidden" name="sfFlagTipoTitolNota" id="sfFlagTipoTitolNota" value=""/><input type="hidden" name="clause_sfFlagTipoTitolNota" value="EQUAL"/></td>
			<td align="left"><select name="tipoSoggettoPerNota" id="tipoSoggettoPerNota" onchange="change_tipoSoggettoPerNota()"><option value=""></option><option value="FAVORE">A Favore</option><option value="CONTRO">Contro</option></select></td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Tipo Diritto Per Soggetto</td>
			<td align="left">&nbsp;<input type="hidden" name="clause_codiceDup" value="EQUAL"/></td>
			<td align="left">
				<sql:query dataSource="jdbc/mui" var="codiciDup">
					select codi_diritto as codice, descr_diritto as descrizione from codici_dup
				</sql:query>
				<select name="codiceDup" id="codiceDup">
					<option value=""></option>
					<c:forEach var="row" items="${codiciDup.rows}">
						<option value="${row.codice}">${row.descrizione}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Percentuale Possesso</td>
			<td align="left"><select name="clause_quota"><option value="EQUAL_NUM">uguale a</option><option value="MORE_THAN">maggiore di</option><option value="LESS_THAN">minore di</option><option value="MORE_OR_EQUAL_THAN">maggiore o uguale a</option><option value="LESS_OR_EQUAL_THAN">minore o uguale a</option></select></td>
			<td align="left"><input type="text" class="muiinput" value="" name="quota"/></td>
		</tr>
		<tr class="a">
			<td class="a_title" align="center" colspan="3">SOGGETTO GIURIDICO</td>
		</tr>
		<tr class="a">
			<td align="right">Campo</td>
			<td align="left">clausola</td>
			<td align="left">Valore</td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Partita IVA</td>
			<td align="left"><select name="clause_codiceFiscaleG"><option value="EQUAL">uguale a</option><option value="CONTAINS">contiene</option><option value="STARTS_WITH">inizia per</option><option value="ENDS_WITH">finisce per</option> </select></td>
			<td align="left"><input type="text" class="muiinput" value="" name="codiceFiscaleG"/></td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Denominazione</td>
			<td align="left"><select name="clause_denominazione"><option value="EQUAL">uguale a</option><option value="CONTAINS">contiene</option><option value="STARTS_WITH">inizia per</option><option value="ENDS_WITH">finisce per</option> </select></td>
			<td align="left"><input type="text" class="muiinput" value="" name="denominazione"/></td>
		</tr>
		<tr class="a">
			<td class="a_title" align="center" colspan="3">SOGGETTO FISICO</td>
		</tr>
		<tr class="a">
			<td class="a" align="right">Campo</td>
			<td align="left">clausola</td>
			<td align="left">Valore</td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Cognome</td>
			<td align="left"><select name="clause_cognome"><option value="EQUAL">uguale a</option><option value="CONTAINS">contiene</option><option value="STARTS_WITH">inizia per</option><option value="ENDS_WITH">finisce per</option></select></td>
			<td align="left"><input type="text" class="muiinput" value="" name="cognome"/></td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Nome</td>
			<td align="left"><select name="clause_nome"><option value="EQUAL">uguale a</option><option value="CONTAINS">contiene</option><option value="STARTS_WITH">inizia per</option><option value="ENDS_WITH">finisce per</option></select></td>
			<td align="left"><input type="text" class="muiinput" value="" name="nome"/></td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Codice Fiscale</td>
			<td align="left"><select name="clause_codiceFiscale"><option value="EQUAL">uguale a</option><option value="CONTAINS">contiene</option><option value="STARTS_WITH">inizia per</option><option value="ENDS_WITH">finisce per</option></select></td>
			<td align="left"><input type="text" class="muiinput" value="" name="codiceFiscale"/></td>
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
			<td class="a" align="right">Graffato</td>
			<td align="left">&nbsp;<input type="hidden" name="flagGraffato" value="1"/> </td>
			<td align="left"><select name="clause_flagGraffato"><option value="IGNORE"></option><option value="EQUAL">SI</option><option value="NOT_EQUAL">NO</option></select></td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Indirizzo</td>
			<td align="left"><select name="clause_TIndirizzo"><option value="EQUAL">uguale a</option><option value="CONTAINS">contiene</option><option value="STARTS_WITH">inizia per</option><option value="ENDS_WITH">finisce per</option></select></td>
			<td align="left"><input type="text" class="muiinput" value="" name="TIndirizzo"/></td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Foglio</td>
			<td align="left"><select name="clause_foglio"><option value="EQUAL">uguale a</option><option value="CONTAINS">contiene</option><option value="STARTS_WITH">inizia per</option><option value="ENDS_WITH">finisce per</option></select></td>
			<td align="left"><input type="text" class="muiinput" value="" name="foglio"/></td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Numero</td>
			<td align="left"><select name="clause_numero"><option value="EQUAL">uguale a</option><option value="CONTAINS">contiene</option><option value="STARTS_WITH">inizia per</option><option value="ENDS_WITH">finisce per</option></select></td>
			<td align="left"><input type="text" class="muiinput" value="" name="numero"/></td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Subalterno</td>
			<td align="left"><select name="clause_subalterno"><option value="EQUAL">uguale a</option><option value="CONTAINS">contiene</option><option value="STARTS_WITH">inizia per</option><option value="ENDS_WITH">finisce per</option></select></td>
			<td align="left"><input type="text" class="muiinput" value="" name="subalterno"/></td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Categoria</td>
			<td align="left">&nbsp;<input type="hidden" name="clause_categoria" value="EQUAL"/></td>
			<td align="left">
				<sql:query dataSource="jdbc/mui" var="categorie">
					select distinct categoria as codice from mi_dup_fabbricati_info order by categoria
				</sql:query>
				<select name="categoria" id="categoria">
					<option value=""></option>
					<c:forEach var="row" items="${categorie.rows}">
						<option value="${row.codice}">${row.codice}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Rendita</td>
			<td align="left"><input type="hidden" name="multiplyByFactor_renditaEuro" value="100"/><select name="clause_renditaEuro"><option value="EQUAL_NUM">uguale a</option><option value="MORE_THAN">maggiore di</option><option value="LESS_THAN">minore di</option><option value="MORE_OR_EQUAL_THAN">maggiore o uguale a</option><option value="LESS_OR_EQUAL_THAN">minore o uguale a</option></select></td>
			<td align="left"><input type="text" class="muiinput" value="" name="renditaEuro"/></td>
		</tr>
		<tr class="b">
			<td class="a" align="right">ID Immobile</td>
			<td align="left">uguale a</td>
			<td align="left"><input type="hidden" name="clause_iid@fabbricato" value="EQUAL"/><input type="text" class="muiinput" value="" name="iid@fabbricato"/></td>
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
