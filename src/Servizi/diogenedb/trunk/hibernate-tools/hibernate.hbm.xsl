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
			<xsl:when
				test="local-name(.)='property' and @name='condition' and ../../class[@table='DC_REL']">
				<xsl:call-template name="DC_REL" />
			</xsl:when>
			<xsl:when
				test="local-name(.)='property' and @name='expression' and ../../class[@table='DC_EXPRESSION']">
				<xsl:call-template name="DC_EXPRESSION" />
			</xsl:when>
			<xsl:when
				test="local-name(.)='property' and @name='sqlStatement' and ../../class[@table='DV_USER_ENTITY']">
				<xsl:call-template name="DV_USER_ENTITY" />
			</xsl:when>
			<xsl:when
				test="local-name(.)='property' and @name='filter' and ../../class[@table='DV_FORMAT_CLASSE']">
				<xsl:call-template name="DV_FORMAT_CLASSE1" />
			</xsl:when>
			<xsl:when
				test="local-name(.)='property' and @name='list' and ../../class[@table='DV_FORMAT_CLASSE']">
				<xsl:call-template name="DV_FORMAT_CLASSE2" />
			</xsl:when>
			<xsl:when
				test="local-name(.)='property' and @name='detail' and ../../class[@table='DV_FORMAT_CLASSE']">
				<xsl:call-template name="DV_FORMAT_CLASSE3" />
			</xsl:when>
			<xsl:when
				test="local-name(.)='property' and @name='hibernateMapping' and ../../class[@table='DC_METADATA_CONNECTION']">
				<xsl:call-template name="DC_METADATA_CONNECTION" />
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
	<xsl:template name="DC_REL">
		<property name="condition"
			type="it.webred.diogene.db.HibernateXMLType">
			<column name="CONDITION" />
		</property>
	</xsl:template>
	<xsl:template name="DC_EXPRESSION">
		<property name="expression"
			type="it.webred.diogene.db.HibernateXMLType">
			<column name="EXPRESSION" />
		</property>
	</xsl:template>
	<xsl:template name="DV_USER_ENTITY">
		<property name="sqlStatement"
			type="it.webred.diogene.db.HibernateXMLType">
			<column name="SQL_STATEMENT" />
		</property>
	</xsl:template>
	<xsl:template name="DV_FORMAT_CLASSE1">
		<property name="filter"
			type="it.webred.diogene.db.HibernateXMLType">
			<column name="FILTER" />
		</property>
	</xsl:template>
	<xsl:template name="DV_FORMAT_CLASSE2">
	   <property name="list"
			type="it.webred.diogene.db.HibernateXMLType">
			<column name="LIST" />
		</property>
	</xsl:template>
	<xsl:template name="DV_FORMAT_CLASSE3">
		<property name="detail"
			type="it.webred.diogene.db.HibernateXMLType">
			<column name="DETAIL" />
		</property>
	</xsl:template>
	<xsl:template name="DC_METADATA_CONNECTION">
		<property name="hibernateMapping"
			type="it.webred.diogene.db.HibernateXMLType">
			<column name="HIBERNATE_MAPPING" />
		</property>
	</xsl:template>
</xsl:stylesheet>

