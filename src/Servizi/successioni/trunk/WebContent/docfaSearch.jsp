<%@include file="jspHead.jsp" %>
<div class="section">
<h2>DOCFA - FILTRO DOCFA</h2>
<div class="section">
<p>
Selezionare criteri di ricerca per i Docfa</p>
<form action="" method="post">
<table class="bodyTable">
	<tbody>
		<tr class="a">
			<td class="a_title" align="center" colspan="3">DATI GENERALI</td>
		</tr>
		<tr class="a">
			<td align="right">Campo</td>
			<td align="left">clausola</td>
			<td align="left">Valore</td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Numero Protocollo</td>
			<td align="left">uguale a</td>
			<td align="left"><input type="hidden" name="clause_protocollo" value="EQUAL"/><input type="text" class="muiinput" value="" name="protocollo"/></td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Data Variazione</td>
			<td align="left">uguale a</td>
			<td align="left"><input type="hidden" name="clause_dtVariazione" value="EQUAL"/>
			<!-- <input type="text" class="muiinput" value="" name="dt_variazione"/>-->
			<input type="text" class="muiinput" value="" id="dt_variazione" name="dt_variazione" readonly="readonly"/>
				<img title='Inserisci Data' onclick="InserisciData('dt_variazione')" src="img/calendario.gif" border="1" alt="Inserisci Data"/> &nbsp;
				<img title="Rimuovi" onclick="document.getElementById('dt_variazione').value='';" src="img/del.gif" border="1" alt="Rimuovi"/>
			</td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Causale</td>
			<td align="left">uguale a</td>
			<td align="left"><input type="hidden" name="clause_causale" value="EQUAL"/><select name="causale">
								<option value="">tutti</option>
								<option value="NC">ACCATASTAMENTO</option>
								<option value="AFF">DENUNCIA UNITA AFFERENTE</option>
								<option value="DIV">DIVISIONE</option>
								<option value="FRZ">FRAZIONAMENTO</option>
								<option value="FUS">FUSIONE</option>
								<option value="AMP">AMPLIAMENTO</option>
								<option value="DET">DEMOLIZIONE TOTALE</option>
								<option value="DEP">DEMOLIZIONE PARZIALE</option>
								<option value="VSI">VARIAZIONE SPAZI INTERNI</option>
								<option value="RST">RISTRUTTURAZIONE</option>
								<option value=FRF>FRAZIONAMENTO E FUSIONE</option>
								<option value="VTO">VARIAZIONE TOPONOMASTICA</option>
								<option value="UFU">ULTIMAZIONE FABBRICATO URBANO</option>
								<option value="VDE">CARIAZIONE DELLA DESTINAZIONE</option>
								<option value="VAR">ALTRE VARIAZIONI</option>
								<option value="VRP">PRESENTAZIONE PLANIMETRIA MANCANTE</option>
								<option value="VMI">MODIFICA DI IDENTIFICATIVO</option></select>
			</td>
			
		</tr>
		<tr class="a">
			<td class="a_title" align="center" colspan="3">DICHIARANTE</td>
		</tr>
		<tr class="a">
			<td align="right">Campo</td>
			<td align="left">clausola</td>
			<td align="left">Valore</td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Cognome</td>
			<td align="left"><select name="clause_cognome"><option value="EQUAL">uguale a</option><option value="CONTAINS">contiene</option></select></td>
			<td align="left"><input type="text" class="muiinput" value="" name="cognome"/></td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Nome</td>
			<td align="left"><select name="clause_nome"><option value="EQUAL">uguale a</option><option value="CONTAINS">contiene</option></select></td>
			<td align="left"><input type="text" class="muiinput" value="" name="nome"/></td>
		</tr>
		<tr class="a">
			<td class="a_title" align="center" colspan="3">IMMOBILI</td>
		</tr>
		<tr class="a">
			<td align="right">Campo</td>
			<td align="left">clausola</td>
			<td align="left">Valore</td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Foglio(es. 0001)</td>
			<td align="left">uguale a</td>
			<td align="left"><input type="hidden" name="clause_foglio" value="EQUAL"/><input type="text" class="muiinput" value="" name="foglio"/></td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Particella(es. 00001)</td>
			<td align="left">uguale a</td>
			<td align="left"><input type="hidden" name="clause_particella" value="EQUAL"/><input type="text" class="muiinput" value="" name="particella"/></td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Subalterno(es. 0001)</td>
			<td align="left">uguale a</td>
			<td align="left"><input type="hidden" name="clause_subalterno" value="EQUAL"/><input type="text" class="muiinput" value="" name="subalterno"/></td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Indirizzo</td>
			<td align="left"><select name="clause_indirizzo"><option value="EQUAL">uguale a</option><option value="CONTAINS">contiene</option></select></td>
			<td align="left"><input type="text" class="muiinput" value="" name="indirizzo"/></td>
		</tr>
		<tr class="b">
			<td class="a" align="right">Tipo Operazione</td>
			<td align="left">uguale a</td>
			<td align="left"><input type="hidden" name="clause_operazione" value="EQUAL"/><select name="operazione"><option value="">Tutte</option><option value="C">Costituizione</option><option value="V">Variazione</option><option value="S">Soppressione</option></select>
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
