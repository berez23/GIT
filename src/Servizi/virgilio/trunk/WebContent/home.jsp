<%@include file="jspHead.jsp" %>
<div class="section">
<h2>MUI - COMUNICAZIONI ICI</h2>
<div class="section">
<p>
<c:set var="x" scope="session" value="${x + 1}"/>
Before: ${x}<br>
Ciao Mondo
Patapim Patapam patapim patapam patapim patapam patapim patapam patapim patapam</p>
<table class="bodyTable">
	<tbody>
		<tr class="a">
			<td align="left">tld</td>
			<td align="left">URI</td>
			<td align="left">description</td>
		</tr>
		<tr class="b">
			<td align="left">displaytag.tld</td>
			<td align="left">http://displaytag.sf.net</td>
			<td align="left">Jsp 1.2 version of the tld: requires j2ee 1.3
			(Tomcat 4,<br />
			WebSphere 5, WebLogic 7...). Use this version if you are not<br />
			looking for j2ee 1.2 compatibility and don't need EL support.</td>
		</tr>
		<tr class="a">
			<td align="left">displaytag-el.tld</td>
			<td align="left">http://displaytag.sf.net/el</td>
			<td align="left">EL version of the tag library. It offers the same
			features as the<br />
			standard version, plus Expression Language Support. It will<br />
			require a couple of addictional libraries, see the dependencies<br />
			page.<br />
			<tt>Don't use this one if you are looking for EL support on jsp<br />
			2.0 containers (Tomcat 5).</tt><br />
			In Jsp 2.0 compatible servers expressions are evaluated directly<br />
			by the container, so you can use the standard tld and still have<br />
			EL support (the EL tld will not work, since expressions will be<br />
			evaluated twice).</td>
		</tr>
	</tbody>
</table>
<div class="section">
<h3>Jsp 1.1 / j2ee 1.2</h3>
<p>Note that as of displaytag 1.1 the jsp 1.1 version of the taglibrary
is no more supported.</p>
<p>If you need to use displaytag with container that only supporting
j2ee 1.2 (Tomcat 3, Websphere 4, WebLogic 6...) you will have to stay
with displaytag 1.0.</p>
</div>
</div>
