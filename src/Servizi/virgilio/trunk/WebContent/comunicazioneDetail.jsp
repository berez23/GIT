<%@include file="jspHead.jsp"%>
<div class="section">
<h2>MUI - COMUNICAZIONE ICI</h2>
<div class="section"><c:if test="${BROWSER == 'INTERNET EXPLORER' }">
	<style type="text/css">
table.bodyTable
{
position:absolute;
left:190px;
top:190px
}
</style>
	<c:set scope="request" var="HIDEFOOTER" value="Y"></c:set>
</c:if> <!-- broswer is ${BROWSER } -->
<form action="" method="post">
<input type="hidden" value="${comunicazione.iid }" name="iid" />
<table class="bodyTable">
	<tbody>
		<tr class="a">
			<td class="a_title" align="center" colspan="20"><img
				src="img/ICI_topF205.gif" name="ICI" /></td>
		</tr>
		<tr class="a">
			<td class="a_title" align="left" colspan="20">CONTRIBUENTE</td>
		</tr>
		<tr class="a">
			<td align="center" colspan="20">
			<table>
				<tr>
					<td class="a">Codice Fiscale</td>
					<td colspan="3"><input type="text" size="20"
						value="${comunicazione.codiceFiscale }" name="codiceFiscale" /></td>
					<td class="a">prefisso</td>
					<td><input type="text" size="4" value="${comunicazione.prefisso}"
						name="prefisso" /></td>
					<td class="a">telefono</td>
					<td><input type="text" size="14" value="${comunicazione.telefono}"
						name="telefono" /></td>
				</tr>
				<tr>
					<td class="a">Cognome</td>
					<td colspan="3"><input type="text" size="50"
						value="${comunicazione.cognome}" name="cognome" /></td>
					<td class="a">Nome</td>
					<td colspan="3"><input type="text" size="50"
						value="${comunicazione.nome}" name="nome" /></td>
				</tr>
				<tr>
					<td class="a">data di nascita</td>
					<td colspan="7">
					<table cellspacing="0">
						<tr align="left">
							
							<td colspan="1"><input type="text" size="12"
								value="<c:if test="${!empty comunicazione.dataNascita }"><dt:format pattern="dd/MM/yyyy" date="${comunicazione.dataNascita}" locale="true" /></c:if>" id="dataNascita"
								name="dataNascita" readonly="readonly" /> <img
								title='Inserisci Data' onclick="InserisciData('dataNascita')"
								src="img/calendario.gif" border="1" alt="Inserisci Data" />
							&nbsp; <img title="Rimuovi"
								onclick="document.getElementById('dataNascita').value='';"
								src="img/del.gif" border="1" alt="Rimuovi" /></td>
							<td class="a">comune di nascita</td>
							<td colspan="3"><input type="text" size="50"
								value="${comunicazione.comuneNascita}" name="comuneNascita" /></td>
							<td class="a">prov.</td>
							<td colspan="1"><input type="text" size="2"
								value="${comunicazione.provinciaNascita}"
								name="provinciaNascita" /></td>
							<td class="a">sesso</td>
							<td colspan="1"><input type="text" size="1"
								value="${comunicazione.sesso}" name="sesso" /></td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td class="a">Residenza</td>
					<td colspan="1"><input type="text" size="50"
						value="${comunicazione.indirizzo }" name="indirizzo" /></td>
					<td class="a">CAP</td>
					<td><input type="text" size="5" value="${comunicazione.cap}"
						name="cap" /></td>
					<td class="a">comune</td>
					<td><input type="text" size="25" value="${comunicazione.comune}"
						name="comune" /></td>
					<td class="a">prov.</td>
					<td colspan="1"><input type="text" size="2"
						value="${comunicazione.provincia}" name="provincia" /></td>
				</tr>
				<tr>
					<td class="a">Domicilio</td>
					<td colspan="1"><input type="text" size="50"
						value="${comunicazione.indirizzoDomicilio }"
						name="indirizzoDomicilio" /></td>
					<td class="a">CAP</td>
					<td><input type="text" size="5"
						value="${comunicazione.capDomicilio}" name="capDomicilio" /></td>
					<td class="a">comune</td>
					<td><input type="text" size="25"
						value="${comunicazione.comuneDomicilio}" name="comuneDomicilio" /></td>
					<td class="a">prov.</td>
					<td colspan="1"><input type="text" size="2"
						value="${comunicazione.provinciaDomicilio}"
						name="provinciaDomicilio" /></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr class="a">
			<td class="a_title" align="left" colspan="20">Riservato a chi
			presenta la Comunicazione per il Contribuente</td>
		</tr>
		<tr class="a">
			<td align="center" colspan="20">
			<table>
				<tr>
					<c:if test="${comunicazione.flagRappresentanteLegale}" ><c:set var="flagRappresentanteLegale" > checked="checked" </c:set></c:if>
					<td class="a" align="left" colspan="16"><input type="checkbox"
						name="flagRappresentanteLegale" value="true" ${flagRappresentanteLegale} />Rappresentante
					Legale
						<c:if test="${comunicazione.flagCuratoreFallimentare}" ><c:set var="flagCuratoreFallimentare" > checked="checked" </c:set></c:if>
						<input type="checkbox" name="flagCuratoreFallimentare"
						value="true" ${flagCuratoreFallimentare} />Curatore Fallimentare
						<c:if test="${comunicazione.flagTutore}" ><c:set var="flagTutore" > checked="checked" </c:set></c:if>
						<input type="checkbox" name="flagTutore" value="true" ${flagTutore} } />Tutore
						<c:if test="${comunicazione.flagErede}" ><c:set var="flagErede" > checked="checked" </c:set></c:if>
						<input type="checkbox" name="flagErede" value="true" ${flagErede } />Erede</td>
					<td class="a" colspan="4" align="right">Altro <input type="text"
						name="altraNatura" value="${comunicazione.altraNatura }" size="10" /></span></td>
			</table>
			</td>
		</tr>
		<tr class="a">
			<td align="center" colspan="20">
			<table>
				<tr>
					<td class="a">Codice Fiscale</td>
					<td colspan="3"><input type="text" size="20"
						value="${comunicazione.codiceFiscaleRL }" name="codiceFiscaleRL" /></td>
					<td class="a">prefisso</td>
					<td><input type="text" size="4" value="${comunicazione.prefissoRL}"
						name="prefissoRL" /></td>
					<td class="a">telefono</td>
					<td><input type="text" size="14"
						value="${comunicazione.telefonoRL}" name="telefonoRL" /></td>
				</tr>
				<tr>
					<td class="a">Cognome</td>
					<td colspan="3"><input type="text" size="50"
						value="${comunicazione.cognomeRL}" name="cognomeRL" /></td>
					<td class="a">Nome</td>
					<td colspan="3"><input type="text" size="50"
						value="${comunicazione.nomeRL}" name="nomeRL" /></td>
				</tr>
				<tr>
					<td class="a">data di nascita</td>
					<td colspan="7">
					<table cellspacing="0">
						<tr align="left">
							<td colspan="1"><input type="text" size="12"
								value="<c:if test="${!empty comunicazione.dataNascitaRL }"><dt:format pattern="dd/MM/yyyy" date="${comunicazione.dataNascitaRL}" locale="true" /></c:if>" id="dataNascitaRL"
								name="dataNascitaRL" readonly="readonly" /> <img
								title='Inserisci Data' onclick="InserisciData('dataNascitaRL')"
								src="img/calendario.gif" border="1" alt="Inserisci Data" />
							&nbsp; <img title="Rimuovi"
								onclick="document.getElementById('dataNascitaRL').value='';"
								src="img/del.gif" border="1" alt="Rimuovi" /></td>
							<td class="a">comune di nascita</td>
							<td colspan="3"><input type="text" size="50"
								value="${comunicazione.comuneNascitaRL}" name="comuneNascitaRL" /></td>
							<td class="a">prov.</td>
							<td colspan="1"><input type="text" size="2"
								value="${comunicazione.provinciaNascitaRL}"
								name="provinciaNascitaRL" /></td>
							<td class="a">sesso</td>
							<td colspan="1"><input type="text" size="1"
								value="${comunicazione.sessoRL}" name="sessoRL" /></td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td class="a">Residenza</td>
					<td colspan="1"><input type="text" size="50"
						value="${comunicazione.indirizzoRL }" name="indirizzoRL" /></td>
					<td class="a">CAP</td>
					<td><input type="text" size="5" value="${comunicazione.capRL}"
						name="capRL" /></td>
					<td class="a">comune</td>
					<td><input type="text" size="25" value="${comunicazione.comuneRL}"
						name="comuneRL" /></td>
					<td class="a">prov.</td>
					<td colspan="1"><input type="text" size="2"
						value="${comunicazione.provinciaRL}" name="provinciaRL" /></td>
				</tr>
				<tr>
					<td class="a">Domicilio</td>
					<td colspan="1"><input type="text" size="50"
						value="${comunicazione.indirizzoDomicilioRL }"
						name="indirizzoDomicilioRL" /></td>
					<td class="a">CAP</td>
					<td><input type="text" size="5"
						value="${comunicazione.capDomicilioRL}" name="capDomicilioRL" /></td>
					<td class="a">comune</td>
					<td><input type="text" size="25"
						value="${comunicazione.comuneDomicilioRL}"
						name="comuneDomicilioRL" /></td>
					<td class="a">prov.</td>
					<td colspan="1"><input type="text" size="2"
						value="${comunicazione.provinciaDomicilioRL}"
						name="provinciaDomicilioRL" /></td>
				</tr>
			</table>
			</td>
		</tr>
		<c:forEach var="oggetto" varStatus="status"
			items="${comunicazione.miConsOggettos}">
			<!-- 
		<input type="hidden" value="${oggetto.iid }" name="oggetto.iidOggetto" />
		 -->
		<tr class="a">
			<td class="a_title" align="left" colspan="20">Oggetto n.</td>
		</tr>
		<tr class="a">
			<td align="center" colspan="20">
			<table>
				<tr>
					<td colspan="4">
					<table cellspacing="0">
						<tr align="left">
							<td class="a">Codice Variazione</td>
							<td colspan="1"><input type="text" size="20"
								value="${oggetto.codiceVariazione }"  readonly="readonly" name="oggetto.${oggetto.iid }.codiceVariazione" /></td>
							<td class="a">Descrizione</td>
							<td colspan="3"><input type="text" size="30"
								value="${oggetto.descrizioneVariazione}"
								name="oggetto.${oggetto.iid }.descrizioneVariazione" readonly="readonly" /></td>
						</tr>
					</table>
					</td>
					<td class="a">decorrenza</td>
					<td colspan="1"><input type="text" size="12"
						value="<c:if test="${!empty oggetto.decorrenza }"><dt:format pattern="dd/MM/yyyy" date="${oggetto.decorrenza}" locale="true" /></c:if>" id="oggetto.${oggetto.iid }.decorrenza" name="oggetto.${oggetto.iid }.decorrenza"
						readonly="readonly" /> <img title='Inserisci Data'
						onclick="InserisciData('oggetto.${oggetto.iid }.decorrenza')" src="img/calendario.gif"
						border="1" alt="Inserisci Data" /> &nbsp; <img title="Rimuovi"
						onclick="document.getElementById('oggetto.${oggetto.iid }.decorrenza').value='';"
						src="img/del.gif" border="1" alt="Rimuovi" /></td>
				</tr>
				<tr>
					<td colspan="7">
					<table cellspacing="0">
						<tr align="left">
							<c:if test="${oggetto.flagFabbricato}" ><c:set var="flagFabbricato" > checked="checked" </c:set></c:if>
							<td class="a" align="right">Fabbricato</td>
							<td><input type="checkbox" name="oggetto.${oggetto.iid }.flagFabbricato" value="true"
							    ${flagFabbricato } /></td><c:set var="flagFabbricato" />
							<c:if test="${oggetto.flagAreaFabbricabile}" ><c:set var="flagAreaFabbricabile" > checked="checked" </c:set></c:if>
							<td class="a" align="right">Area Fabbricabile</td>
							<td><input type="checkbox" name="oggetto.${oggetto.iid }.flagAreaFabbricabile" value="true"
								${flagAreaFabbricabile}  /></td><c:set var="flagAreaFabbricabile" />
							<c:if test="${oggetto.flagFabbricatoD}" ><c:set var="flagFabbricatoD" > checked="checked" </c:set></c:if>
							<td class="a" align="right">Fabbricato D</td>
							<td><input type="checkbox" name="oggetto.${oggetto.iid }.flagFabbricatoD" value="true"
								${flagFabbricatoD}  /></td><c:set var="flagFabbricatoD" />
							<c:if test="${oggetto.flagTerrenoAgricolo}" ><c:set var="flagTerrenoAgricolo" > checked="checked" </c:set></c:if>
							<td class="a" align="right">Terreno Agricolo</td>
							<td><input type="checkbox" name="oggetto.${oggetto.iid }.flagTerrenoAgricolo" value="true"
								${flagTerrenoAgricolo} /></td><c:set var="flagTerrenoAgricolo" />
						</tr>
					</table>
					</td>
					<td class="a" align="left">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="8">
					<table cellspacing="0">
						<tr align="left">
							<td class="a">Indirizzo</td>
							<td colspan="7"><input type="text" size="100"
								value="${oggetto.indirizzo }" name="oggetto.${oggetto.iid }.indirizzo" /></td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td colspan="8">
					<table cellspacing="0">
						<tr align="left">
							<td class="a">sezione</td>
							<td colspan="1"><input type="text" size="5"
								value="${oggetto.sezione}" name="oggetto.${oggetto.iid }.sezione" /></td>
							<td class="a">foglio</td>
							<td colspan="1"><input type="text" size="5"
								value="${oggetto.foglio}" name="oggetto.${oggetto.iid }.foglio" /></td>
							<td class="a">particella</td>
							<td colspan="1"><input type="text" size="5"
								value="${oggetto.particella}" name="oggetto.${oggetto.iid }.particella" /></td>
							<td class="a">subalterno</td>
							<td colspan="1"><input type="text" size="5"
								value="${oggetto.subalterno}" name="oggetto.${oggetto.iid }.subalterno" /></td>
							<td class="a">numero protocollo</td>
							<td colspan="1"><input type="text" size="5"
								value="${oggetto.numeroProtocollo}" name="oggetto.${oggetto.iid }.numeroProtocollo" /></td>
							<td class="a">anno</td>
							<td colspan="1"><input type="text" size="5"
								value="<c:if test="${!empty oggetto.anno }"><dt:format pattern="yyyy" date="${oggetto.anno}" locale="true" /></c:if>" name="oggetto.${oggetto.iid }.anno" /></td>
							<td class="a">categoria</td>
							<td colspan="1"><input type="text" size="5"
								value="${oggetto.categoria}" name="oggetto.${oggetto.iid }.categoria" /></td>
							<td class="a">classe</td>
							<td colspan="1"><input type="text" size="5"
								value="${oggetto.classe}" name="oggetto.${oggetto.iid }.classe" /></td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td colspan="4">
					<table cellspacing="0">
						<tr align="right">
							<c:if test="${oggetto.flagRenditaPresunta}" ><c:set var="flagRenditaPresunta" > checked="checked" </c:set></c:if>
							<td class="a" align="right">Rendita Presunta</td>
							<td><input type="checkbox" name="oggetto.${oggetto.iid }.flagRenditaDefinitiva" value="true"
								${flagRenditaPresunta} /></td><c:set var="flagRenditaPresunta" />
							<c:if test="${oggetto.flagRenditaDefinitiva}" ><c:set var="flagRenditaDefinitiva" > checked="checked" </c:set></c:if>
							<td class="a" align="right">Rendita Definitiva</td>
							<td><input type="checkbox" name="oggetto.${oggetto.iid }.flagRenditaDefinitiva" value="true"
								${flagRenditaDefinitiva} /></td><c:set var="flagRenditaDefinitiva" />
						</tr>
						<tr align="right">
							<c:if test="${oggetto.flagValoreVenale}" ><c:set var="flagValoreVenale" > checked="checked" </c:set></c:if>
							<td class="a" align="right">Valore Venale</td>
							<td><input type="checkbox" name="oggetto.${oggetto.iid }.flagValoreVenale" value="true"
								${flagValoreVenale} /></td><c:set var="flagValoreVenale" />
							<c:if test="${oggetto.flagCostiContabili}" ><c:set var="flagCostiContabili" > checked="checked" </c:set></c:if>
							<td class="a" align="right">Costi Contabili</td>
							<td><input type="checkbox" name="oggetto.${oggetto.iid }.flagCostiContabili" value="true"
								${flagCostiContabili} /></td><c:set var="flagCostiContabili" />
						</tr>
					</table>
					</td>
					<c:if test="${oggetto.flagRedditoDomenicale}" ><c:set var="flagRedditoDomenicale" > checked="checked" </c:set></c:if>
					<td class="a" align="right">Reddito Domenicale</td>
					<td><input type="checkbox" name="oggetto.${oggetto.iid }.flagRedditoDomenicale" value="true"
						${flagRedditoDomenicale} /></td><c:set var="flagRedditoDomenicale" />
					<td class="a">Euro</td>
					<td colspan="3"><input type="text" size="5"
						value="${oggetto.redditoEuro}" name="oggetto.${oggetto.iid }.redditoEuro" /></td>
				</tr>
				<tr>
					<td colspan="8">
					<table cellspacing="0">
						<tr align="left">
							<td class="a" align="right">Variazione Catastale</td>
							<td class="a">sezione</td>
							<td colspan="1"><input type="text" size="5"
								value="${oggetto.sezioneVar}" name="oggetto.${oggetto.iid }.sezioneVar" /></td>
							<td class="a">foglio</td>
							<td colspan="1"><input type="text" size="5"
								value="${oggetto.foglioVar}" name="oggetto.${oggetto.iid }.foglioVar" /></td>
							<td class="a">particella</td>
							<td colspan="1"><input type="text" size="5"
								value="${oggetto.particellaVar}" name="oggetto.${oggetto.iid }.particellaVar" /></td>
							<td class="a">subalterno</td>
							<td colspan="1"><input type="text" size="5"
								value="${oggetto.subalternoVar}" name="oggetto.${oggetto.iid }.subalternoVar" /></td>
							<td class="a">numero protocollo</td>
							<td colspan="1"><input type="text" size="5"
								value="${oggetto.numeroProtocolloVar}" name="oggetto.${oggetto.iid }.numeroProtocolloVar" /></td>
							<td class="a">anno</td>
							<td colspan="1"><input type="text" size="5"
								value="<c:if test="${!empty oggetto.annoVar }"><dt:format pattern="yyyy" date="${oggetto.annoVar}" locale="true" /></c:if>" name="oggetto.${oggetto.iid }.annoVar" /></td>
							<td class="a">categoria</td>
							<td colspan="1"><input type="text" size="5"
								value="${oggetto.categoriaVar}" name="oggetto.${oggetto.iid }.categoriaVar" /></td>
							<td class="a">classe</td>
							<td colspan="1"><input type="text" size="5"
								value="${oggetto.classeVar}" name="oggetto.${oggetto.iid }.classeVar" /></td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td colspan="8">
					<table cellspacing="0">
						<tr align="right">
							<td class="a">Percentuale Possesso</td>
							<td colspan="3"><input type="text" size="5"
								value="${oggetto.percentualePossesso}" name="oggetto.${oggetto.iid }.percentualePossesso" /></td>
							<td class="a" align="right">Propriet&agrave;</td>
							<c:if test="${oggetto.flagPossessoProprieta}" ><c:set var="flagPossessoProprieta" > checked="checked" </c:set></c:if>
							<td><input type="checkbox" name="oggetto.${oggetto.iid }.flagPossessoProprieta" value="true"
								${flagPossessoProprieta} } /></td><c:set var="flagPossessoProprieta" />
							<c:if test="${oggetto.flagPossessoUsufrutto}" ><c:set var="flagPossessoUsufrutto" > checked="checked" </c:set></c:if>
							<td class="a" align="right">Usufrutto</td>
							<td><input type="checkbox" name="oggetto.${oggetto.iid }.flagPossessoUsufrutto" value="true"
								${flagPossessoUsufrutto} /></td><c:set var="flagPossessoUsufrutto" />
							<c:if test="${oggetto.flagPossessoDirittoAbitazione}" ><c:set var="flagPossessoDirittoAbitazione" > checked="checked" </c:set></c:if>
							<td class="a" align="right">Diritto Abitazione</td>
							<td><input type="checkbox" name="oggetto.${oggetto.iid }.flagPossessoDirittoAbitazione" value="true"
								${flagPossessoDirittoAbitazione} /></td><c:set var="flagPossessoDirittoAbitazione" />
							<c:if test="${oggetto.flagPossessoSuperficie}" ><c:set var="flagPossessoSuperficie" > checked="checked" </c:set></c:if>
							<td class="a" align="right">Superficie</td>
							<td><input type="checkbox" name="oggetto.${oggetto.iid }.flagPossessoSuperficie" value="true"
								${flagPossessoSuperficie} /></td><c:set var="flagPossessoSuperficie" />
							<c:if test="${flagPossessoLeasing}" ><c:set var="flagLeasing" > checked="checked" </c:set></c:if>
							<td class="a" align="right">Leasing</td>
							<td><input type="checkbox" name="oggetto.${oggetto.iid }.flagPossessoLeasing" value="true"
								${flagPossessoLeasing} /></td><c:set var="flagPossessoLeasing" />
							<td class="a">Altro</td>
							<td colspan="3"><input type="text" size="5"
								value="${oggetto.altroPossesso}" name="oggetto.${oggetto.iid }.altroPossesso" /></td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td colspan="8">
					<table cellspacing="0">
						<tr align="left">
							<td class="a_title" colspan="4">Detrazione Euro 104</td>
						</tr>
						<tr align="center">
							<c:if test="${oggetto.flagAbitazionePrincipale}" ><c:set var="flagAbitazionePrincipale" > checked="checked" </c:set></c:if>
							<td class="a" align="right">Abitazione Principale</td>
							<td><input type="checkbox" name="oggetto.${oggetto.iid }.flagAbitazionePrincipale" value="true"
								${flagAbitazionePrincipale} /></td><c:set var="flagAbitazionePrincipale" />
							<td class="a" align="right" rowspan="2">Contitolari Abitazione Principale che usufruiscono detrazione</td>
							<td  rowspan="2"  align="left"><input type="text" size="5"
								value="${oggetto.contitolariAbitazionePrincipale}" name="oggetto.${oggetto.iid }.contitolariAbitazionePrincipale" /></td>
						</tr>
						<tr align="center">
							<c:if test="${oggetto.flagAbitazionePrincipaleNoMore}" ><c:set var="flagAbitazionePrincipaleNoMore" > checked="checked" </c:set></c:if>
							<td class="a" align="right">Non pi&ugrave; Abitazione Principale</td>
							<td align="left"><input type="checkbox" name="oggetto.${oggetto.iid }.flagAbitazionePrincipaleNoMore" value="true"
								${flagAbitazionePrincipaleNoMore} /></td><c:set var="flagAbitazionePrincipaleNoMore" />
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td colspan="8">
					<table cellspacing="0">
						<tr align="left">
							<c:if test="${oggetto.flagInagibile}" ><c:set var="flagInagibile" > checked="checked" </c:set></c:if>
							<td><INPUT type="radio" name="oggetto.${oggetto.iid }.flagInagibile" ${flagInagibile } value="S">Si</td><td><INPUT type="radio" name="flagInagibile" value="N">No</td><td class="a_title"> Immobile inagibile/inabitabile/ si allega perizia del tecnico comunale o dichiarazione sostitutiva come previsto dall'art 8, comma 1, D.Lgs. 504/1992</td><c:set var="flagInagibile"/>
						</tr>
						<tr align="left">
							<c:if test="${oggetto.flagAgricolturaDiretta}" ><c:set var="flagAgricolturaDiretta" > checked="checked" </c:set></c:if>
							<td><INPUT type="radio" name="oggetto.${oggetto.iid }.flagAgricolturaDiretta" ${flagAgricolturaDiretta } value="S">Si</td><td><INPUT type="radio" name="flagAgricolturaDiretta" value="N">No</td><td class="a_title">Immobile a conduzione agricola diretta</td><c:set var="flagAgricolturaDiretta"/>
						</tr>
						<tr align="left">
							<c:if test="${oggetto.flagImmobileEscluso}" ><c:set var="flagImmobileEscluso" > checked="checked" </c:set></c:if>
							<td><INPUT type="radio" name="oggetto.${oggetto.iid }.flagImmobileEscluso" ${flagImmobileEscluso } value="S">Si</td><td><INPUT type="radio" name="flagImmobileEscluso" value="N">No</td><td class="a_title">Immobile esente/escluso</td><c:set var="flagImmobileEscluso"/>
						</tr>
						<tr align="left">
							<c:if test="${oggetto.flagRiduzioneLocazione}" ><c:set var="flagRiduzioneLocazione" > checked="checked" </c:set></c:if>
							<td><INPUT type="radio" name="oggetto.${oggetto.iid }.flagRiduzioneLocazione" ${flagRiduzioneLocazione } value="S">Si</td><td><INPUT type="radio" name="flagRiduzioneLocazione" value="N">No</td><td class="a_title">Immobile soggetto ad aliquota ridotta si allega copia contratto di locazione</td><c:set var="flagRiduzioneLocazione"/>
						</tr>
						<tr align="left">
							<c:if test="${oggetto.flagStorico}" ><c:set var="flagStorico" > checked="checked" </c:set></c:if>
							<td><INPUT type="radio" name="oggetto.${oggetto.iid }.flagStorico" ${flagStorico } value="S">Si</td><td><INPUT type="radio" name="flagStorico" value="N">No</td><td class="a_title">Immobile storico (desumile da decreto, rogito, ecc)</td><c:set var="flagStorico"/>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td colspan="8">
					<table cellspacing="0">
						<tr align="left">
							<td class="a_title"  colspan="2">Maggior detrazione (soggetti in situazione di disagio economico sociale)<br/>Comunico:</td>
						</tr>
							<c:if test="${oggetto.flagDetrazioneDelibera}" ><c:set var="flagDetrazioneDelibera" > checked="checked" </c:set></c:if>
							<td><INPUT type="checkbox" ${flagDetrazioneDelibera } name="oggetto.${oggetto.iid }.flagDetrazioneDelibera" value="S"></td><td>Di usufruire della maggior detrazione da Euro 104,00 a Euro 155,00 prevista dalla deliberazione di Giunta Comunale n. 2.997 del 16/12/2005 adottata ai sensi dell'art. D.Lgs n. 504 del 30.12.1992, e successive modificazioni, in quanto apprartenente/i alla/e sotto specificata/e categoria/e ammessa/e al beneficio.</td><c:set var="flagDetrazioneDelibera"/>
						</tr>
						<tr align="left">
							<c:if test="${oggetto.flagDetrazioneDeliberaNM}" ><c:set var="flagDetrazioneDeliberaNM" > checked="checked" </c:set></c:if>
							<td><INPUT type="checkbox" ${flagDetrazioneDeliberaNM } name="oggetto.${oggetto.iid }.flagDetrazioneDeliberaNM" value="S"></td><td>Di aver perso i requisiti previsti per usufruire della maggior detrazione</td><c:set var="flagDetrazioneDeliberaNM"/>
						</tr>
						<tr align="left">
							<td  class="a_title" colspan="2">Al fine di usufruire della maggior detrazione, autocertifico (ai sensi del DPR 28/12/2000 n. 445)<br/>allegando<br/>copia del documento di identit&agrave;<br/>Che il reddito annuo lordo dichiarato ai fini IRPEF relativo all'anno 2005 dell'intero nucleo familiare composto<br/>di n. persone non &egrave; superiore a Euro 10.850,00 oltre a Euro 827,00 per ogni persona a carico e di<br/>appartenere ad una o pi&ugrave; delle seguenti categorie:</td>
						</tr>
						<tr align="left">
							<c:if test="${oggetto.flagPensionato}" ><c:set var="flagPensionato" > checked="checked" </c:set></c:if>
							<td><INPUT type="checkbox" ${flagPensionato } name="oggetto.${oggetto.iid }.flagPensionato" value="S"></td><td>Pensionato</td><c:set var="flagPensionato"/>
						</tr>
						<tr align="left">
							<c:if test="${oggetto.flagConiugePensionato}" ><c:set var="flagConiugePensionato" > checked="checked" </c:set></c:if>
							<td><INPUT type="checkbox" ${flagConiugePensionato} name=flagConiugePensionato value="S"></td><td>Coniuge a carico di pensionato</td><c:set var="flagConiugePensionato"/>
						</tr>
						<tr align="left">
							<c:if test="${oggetto.flagInvalido}" ><c:set var="flagInvalido" > checked="checked" </c:set></c:if>
							<td><INPUT type="checkbox" ${flagInvalido } name="oggetto.${oggetto.iid }.flagInvalido" value="S"></td><td>Portatore di handicap con attestato di invalidit&agrave; civile</td><c:set var="flagInvalido"/>
						</tr>
						<tr align="left">
							<c:if test="${oggetto.flagDisoccupato}" ><c:set var="flagDisoccupato" > checked="checked" </c:set></c:if>
							<td><INPUT type="checkbox" ${flagDisoccupato } name="oggetto.${oggetto.iid }.flagDisoccupato" value="S"></td><td>Disoccupato nel 2005 per almeno sei mesi, regolarmente iscritto nelle liste di collocamento</td><c:set var="flagDisoccupato"/>
						</tr>
						<tr align="left">
							<c:if test="${oggetto.flagMobilita}" ><c:set var="flagMobilita" > checked="checked" </c:set></c:if>
							<td><INPUT type="checkbox" ${flagMobilita } name="oggetto.${oggetto.iid }.flagMobilita" value="S"></td><td>Lavoratore posto in cassa integrazione o in mobilit&agrave; nell'anno 2005 per almeno sei mesi</td><c:set var="flagMobilita"/>
						</tr>
						<tr align="left">
							<c:if test="${oggetto.flagInterinale}" ><c:set var="flagInterinale" > checked="checked" </c:set></c:if>
							<td><INPUT type="checkbox" name="oggetto.${oggetto.iid }.flagInterinale" ${flagInterinale } value="S"></td><td>Lavoratore interinale o part-time</td><c:set var="flagInterinale"/>
						</tr>
						<tr align="left">
							<c:if test="${oggetto.flagCococo}" ><c:set var="flagCococo" > checked="checked" </c:set></c:if>
							<td><INPUT type="checkbox" name="oggetto.${oggetto.iid }.flagCococo" ${flagCococo } value="S"></td><td>Lavoratori con contratti di collaborazione coordinata e continuativa</td><c:set var="flagCococo"/>
						</tr>
					</table>
					</td>
				</tr>
			</table>
			</td>
		</tr>
		</c:forEach>
		<tr class="b">
			<td align="right" colspan="3"></td>
			<td align="right"><security:lock role="mui-adm,mui-supusr"><input type="submit" class="muiinput" value="Salva" name="Salva"/></security:lock></td>
			<td align="left"><input type="reset" class="muiinput" value="Stampa" name="Stampa" onclick="popUp('iciPdfNoTemplate?iidSoggetto=${ comunicazione.miDupSoggetti.iid}');return false"/><input type="reset" class="muiinput" value="Controlla Detrazione" name="Controlla Detrazione" onclick="popUp('detrazioneCheckNoTemplate.jsp?iidSoggetto=${ comunicazione.miDupSoggetti.iid}');return false"/><!--  <input type="reset" class="muiinput" value="Storico Residenza" name="Storico Residenza" onclick="popUp('detrazioneSearch?iidSoggetto=${ comunicazione.miDupSoggetti.iid}');return false"/> --><input type="reset" class="muiinput" value="Reset" name="Reset"/></td>
			<td align="left" colspan="3"></td>
		</tr>
	</tbody>
</table>
</form>
</div>
</div>
