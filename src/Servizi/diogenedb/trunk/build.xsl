<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:param name="utentedb"></xsl:param>
	<xsl:param name="dbserver"></xsl:param>
	<xsl:param name="dbservice"></xsl:param>

	<xsl:output method="xml" indent="yes" omit-xml-declaration="no"
		doctype-public="-//Hibernate/Hibernate Configuration DTD 3.0//EN"
		doctype-system="http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd"/>


	<xsl:template match="/ | @* | node()">
		<xsl:choose>
			<xsl:when
				test="local-name(.)='property' and @name='hibernate.connection.password'">
				<xsl:call-template name="hibernate.connection.password" />
			</xsl:when>
			<xsl:when
				test="local-name(.)='property' and @name='hibernate.connection.username'">
				<xsl:call-template name="hibernate.connection.username" />
			</xsl:when>
			<xsl:when
				test="local-name(.)='property' and @name='hibernate.default_schema'">
				<xsl:call-template name="hibernate.default_schema" />
			</xsl:when>
			<xsl:when
				test="local-name(.)='property' and @name='hibernate.connection.url'">
				<xsl:call-template name="hibernate.connection.url" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:copy>
					<xsl:apply-templates select="@* | node()" />
				</xsl:copy>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>



	<xsl:template name="hibernate.connection.url">
		<property name="hibernate.connection.url">
		<xsl:text>jdbc:oracle:thin:@</xsl:text>
		<xsl:value-of select="$dbserver"></xsl:value-of>
		<xsl:text>:1521:</xsl:text>
		<xsl:value-of select="$dbservice"></xsl:value-of>
		</property>
	</xsl:template>
	<xsl:template name="hibernate.connection.password">
		<property name="hibernate.connection.password">
		<xsl:value-of select="$utentedb"></xsl:value-of>
		</property>
	</xsl:template>
	<xsl:template name="hibernate.connection.username">
		<property name="hibernate.connection.username">
		<xsl:value-of select="$utentedb"></xsl:value-of>
		</property>
	</xsl:template>
	<xsl:template name="hibernate.default_schema">
		<property name="hibernate.default_schema">
		<xsl:value-of select="$utentedb"></xsl:value-of>
		</property>
	</xsl:template>		
</xsl:stylesheet>

