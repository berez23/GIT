<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="xml" indent="yes" omit-xml-declaration="no"
		doctype-public="-//Hibernate/Hibernate Mapping DTD 3.0//EN"
		doctype-system="http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd"/>




	<xsl:template match="/ | @* | node()">
		<xsl:choose>
			<xsl:when
				test="local-name(.)='generator' and @class='assigned' and not(contains(../../id/@type,'string'))">
				<xsl:call-template name="generator_increment" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:copy>
					<xsl:apply-templates select="@* | node()" />
				</xsl:copy>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>



	<xsl:template name="generator_increment">
		<generator class="increment" />
	</xsl:template>
</xsl:stylesheet>

