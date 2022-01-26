<xsl:stylesheet version="1.0"
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
        xmlns:redirect="org.apache.xalan.xslt.extensions.Redirect"
        extension-element-prefixes="redirect">

<xsl:output method="text"/>

<xsl:template match="/">
    <xsl:apply-templates select="//prog"/>
</xsl:template>

<xsl:template match="prog"><xsl:if test="@scope != 'swing'">
   <xsl:variable name="name">exercise-programs/chapter<xsl:number count="chapter"/>/<xsl:value-of select="@name"/>.java</xsl:variable>
<!-- <xsl:message>Extracting program <xsl:value-of select="$name"/></xsl:message>   -->
   <redirect:write select="$name"><xsl:apply-templates/></redirect:write>
</xsl:if></xsl:template>

<xsl:template match="fx">
    <xsl:apply-templates/>
</xsl:template>
    
<xsl:template match="swing"></xsl:template>


</xsl:stylesheet>
