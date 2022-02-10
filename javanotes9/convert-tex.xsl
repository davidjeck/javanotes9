<xsl:stylesheet version="1.0"
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:str="http://xml.apache.org/xalan/java/java.lang.String"
        extension-element-prefixes="str">

<xsl:output method="text"/>
   
<xsl:template match="text()">
   <xsl:variable name="a"><xsl:value-of select="str:new(.)"/></xsl:variable>
   <xsl:variable name="b"><xsl:value-of select="str:replaceAll($a,'\\','\\1')"/></xsl:variable>
   <xsl:variable name="bb"><xsl:value-of select="str:replaceAll($b,'@dots@','\\dots')"/></xsl:variable>
   <xsl:variable name="k"><xsl:value-of select="str:replaceAll($bb,'@BigOh@','\\3')"/></xsl:variable>
   <xsl:variable name="kk"><xsl:value-of select="str:replaceAll($k,'@Theta@','\\4')"/></xsl:variable>
   <xsl:variable name="kkk"><xsl:value-of select="str:replaceAll($kk,'@Omega@','\\5')"/></xsl:variable>
   <xsl:variable name="bbb"><xsl:value-of select="str:replaceAll($kkk,'@pi@','\\2')"/></xsl:variable>
   <xsl:choose>
       <xsl:when test="ancestor::pre or ancestor::code or ancestor::codedef">
           <xsl:value-of select="str:replaceAll($bbb,'(#|&amp;|&lt;|&gt;|\$|%|\{|\}|_|\^)','\\$1')"/>
       </xsl:when>
       <xsl:when test="ancestor::atype">
           <xsl:variable name="d"><xsl:value-of select="str:replaceAll($bbb,'(#|&amp;|&lt;|&gt;|\$|%|\{|\}|_)','\\$1')"/></xsl:variable>
           <xsl:value-of select="str:replaceAll($d,'\[\]','\\hbox{[\\hskip2pt]}')"/>
       </xsl:when>
       <xsl:otherwise>
           <xsl:variable name="c"><xsl:value-of select="str:replaceAll($bbb,'( |\n|\()&quot;','$1``')"/></xsl:variable>
           <xsl:value-of select="str:replaceAll($c,'(#|&amp;|&lt;|&gt;|\$|%|\{|\}|_)','\\$1')"/>
       </xsl:otherwise>
   </xsl:choose>
</xsl:template>
    
<xsl:template match="/">
\documentclass[letterpaper,11pt]{book}
\usepackage[dvips]{graphicx}
\usepackage[mathscr]{eucal}
\setlength{\headsep}{0.4truein}
\setlength{\topmargin}{-0.3 true in}
\setlength{\topskip}{0 true in}
\setlength{\textwidth}{6.25 true in}
\setlength{\oddsidemargin}{0.25 true in}
\setlength{\evensidemargin}{0 true in}
\setlength{\textheight}{8.9 true in}
\pretolerance=1000
\tolerance=8000
\input texmacros
\title{Introduction to Programming Using Java}
\author{David J. Eck}
\begin{document}
\frontmatter
\pageone
\tableofcontents
<xsl:apply-templates select="/javanotes/preface"/>
\mainmatter
<xsl:apply-templates select="/javanotes/chapter"/>
\appendix
<xsl:apply-templates select="/javanotes/source"/>
<xsl:apply-templates select="/javanotes/glossary"/>
\end{document}
</xsl:template>

<xsl:template match="chapter">
\chapter<xsl:if test="@shorttitle">[<xsl:value-of select="@shorttitle"/>]</xsl:if>{<xsl:value-of select="@title"/>}\label{<xsl:value-of select="@id"/>}
<xsl:apply-templates/>
</xsl:template>
   
<xsl:template match="section">
\section<xsl:if test="@shorttitle">[<xsl:value-of select="@shorttitle"/>]</xsl:if>{<xsl:value-of select="@title"/>}\label{<xsl:value-of select="@id"/>}
<xsl:apply-templates/>
</xsl:template>
   
<xsl:template match="subsection"><xsl:if test="@scope != 'swing'">
<xsl:choose><xsl:when test="ancestor::source">\section*</xsl:when><xsl:otherwise>\subsection</xsl:otherwise></xsl:choose><xsl:if test="@shorttitle">[<xsl:value-of select="@shorttitle"/>]</xsl:if>{<xsl:value-of select="@title"/>}\label{<xsl:value-of select="@id"/>}
<xsl:apply-templates/>
</xsl:if></xsl:template>
   
<xsl:template match="preface">
\chapter*{Preface}\addcontentsline{toc}{chapter}{Preface}\markboth{\textsc{Preface}}{\textsc{Preface}}
<xsl:apply-templates/>
</xsl:template>
    
<xsl:template match="source">
\chapter*{Appendix: Source Files}\addcontentsline{toc}{chapter}{Appendix: Source Files}\markboth{\textsc{Source Code Listing}}{\textsc{Source Code Listing}}
<xsl:apply-templates/>
</xsl:template>
    
<xsl:template match="exercises">
\begin{exercises}
<xsl:for-each select="exercise">
\exercise <xsl:apply-templates select="exercise-question"/>
</xsl:for-each> 

\end{exercises}
</xsl:template>

<xsl:template match="quiz">
\begin{quiz}
<xsl:for-each select="question">
\quizquestion <xsl:apply-templates select="ques"/>
</xsl:for-each>

\end{quiz}
</xsl:template>

<xsl:template match="glossary">
\chapter*{Glossary}\addcontentsline{toc}{chapter}{Glossary}\markboth{\textsc{Glossary}}{\textsc{Glossary}}
<xsl:apply-templates/>
</xsl:template>
    

<xsl:template match="gitem">
    \glossaryitem{<xsl:value-of select="@term"/>}{<xsl:apply-templates/>}
</xsl:template>


<xsl:template match="p">
      <xsl:choose>
        <xsl:when test="@align='center'">
           \centerline{<xsl:apply-templates/>}
        </xsl:when>
        <xsl:when test="@align='left'">
           \leftline{<xsl:apply-templates/>}
        </xsl:when>
        <xsl:when test="@align='right'">
           \rightline{<xsl:apply-templates/>}
        </xsl:when>
        <xsl:otherwise>
           <xsl:text>&#10;</xsl:text><xsl:apply-templates/>
        </xsl:otherwise>
     </xsl:choose>
</xsl:template>

<xsl:template match="np">\noindent <xsl:apply-templates/>
</xsl:template>
    
<xsl:template match="ol">
<xsl:text>&#10;</xsl:text>
\mynumberedlist{
<xsl:for-each select="li">
\mynumbereditem <xsl:apply-templates/>
</xsl:for-each>
}
</xsl:template>

<xsl:template match="ul">
<xsl:text>&#10;</xsl:text>
\mylist{
<xsl:for-each select="li | fxSourceItems/li">
\myitem <xsl:apply-templates/>
</xsl:for-each>
}
</xsl:template>

<xsl:template match="pre">\displaycode{<xsl:apply-templates/>}\donedisplaycode
</xsl:template>

<xsl:template match="big">{\Large <xsl:apply-templates/>}</xsl:template>

<xsl:template match="u">\underline{<xsl:apply-templates/>}</xsl:template>

<xsl:template match="i">\textit{<xsl:apply-templates/>}</xsl:template>

<xsl:template match="b">\textbf{<xsl:apply-templates/>}</xsl:template>

<xsl:template match="sup">{{$^{<xsl:apply-templates/>}$}}</xsl:template>

<xsl:template match="sub">{{$_{<xsl:apply-templates/>}$}}</xsl:template>

<xsl:template match="code|ptype|newword|codedef|bnf|newcode|start|classname|atype">\<xsl:value-of select="name()"/>{<xsl:apply-templates/>}</xsl:template>
    
<xsl:template match="a">\weblink{<xsl:value-of select="@href"/>}{<xsl:apply-templates/>}</xsl:template>
   
<xsl:template match="sourceref"><xsl:choose>
   <xsl:when test="text()">\sourceref{<xsl:apply-templates/>}</xsl:when>
   <xsl:otherwise><xsl:variable name="a" select="str:new(@href)"/><xsl:variable name="b" select="str:replaceAll($a,'_','\\_')"/>\sourceref{<xsl:value-of select="$b"/>}</xsl:otherwise>
</xsl:choose></xsl:template>
    
<xsl:template match="jarref"><xsl:choose>
   <xsl:when test="text()">\jarref{<xsl:apply-templates/>}</xsl:when>
   <xsl:otherwise><xsl:variable name="a" select="str:new(@href)"/><xsl:variable name="b" select="str:replaceAll($a,'_','\\_')"/>\jarref{<xsl:value-of select="$b"/>}</xsl:otherwise>
</xsl:choose></xsl:template>
    
<xsl:template match="localref">
     <xsl:choose>
         <xsl:when test="text()"><xsl:apply-templates/></xsl:when>
         <xsl:when test="not(id(@href))">(\textit{Unknown reference}~\ref{<xsl:value-of select="@href"/>})</xsl:when>
         <xsl:when test="name(id(@href))='chapter'"><xsl:text>Chapter~\ref{</xsl:text><xsl:value-of select="@href"/><xsl:text>}</xsl:text></xsl:when>
         <xsl:when test="name(id(@href))='section'"><xsl:text>Section~\ref{</xsl:text><xsl:value-of select="@href"/><xsl:text>}</xsl:text></xsl:when>
         <xsl:when test="name(id(@href))='subsection'"><xsl:text>Subsection~\ref{</xsl:text><xsl:value-of select="@href"/><xsl:text>}</xsl:text></xsl:when>
         <xsl:when test="name(id(@href))='exercise'"><xsl:text>Exercise~</xsl:text><xsl:for-each select="id(@href)"><xsl:number count="chapter"/>.<xsl:number count="exercise"/></xsl:for-each></xsl:when>
         <xsl:when test="name(id(@href))='fudgeref'"><xsl:for-each select="id(@href)"><xsl:value-of select="@text"/></xsl:for-each></xsl:when>
     </xsl:choose>
</xsl:template>

<xsl:template match="fudgeref"/>
    
<xsl:template match="break">
\mybreak
</xsl:template>
   
<xsl:template match="br">\\</xsl:template>
    
<xsl:template match="narrower">
   <xsl:variable name="margin"><xsl:choose>
      <xsl:when test="@margin"><xsl:value-of select="@mrgin"/></xsl:when>
      <xsl:otherwise>70</xsl:otherwise>
   </xsl:choose></xsl:variable>
\medbreak
{\advance \leftskip by <xsl:value-of select="$margin"/> pt \advance \rightskip by <xsl:value-of select="$margin"/> pt

<xsl:apply-templates/>

}
\medbreak
</xsl:template>

<xsl:template match="img">
<xsl:variable name="name"><xsl:choose>
   <xsl:when test="@tex"><xsl:value-of select="@tex"/></xsl:when>
   <xsl:otherwise><xsl:value-of select="@src"/></xsl:otherwise>
</xsl:choose></xsl:variable>
\par\dumpfigure{
\includegraphics<xsl:if test="@texscale">[scale=<xsl:value-of select="@texscale"/>]</xsl:if>{images/<xsl:value-of select="$name"/>}
}
</xsl:template>
   
<xsl:template match="imageclear">
</xsl:template>
    
   
<xsl:template match="centered">\par
\begin{center}
<xsl:apply-templates/>
\end{center}
</xsl:template>
    
<xsl:template match="endchapter">
</xsl:template>
    
<xsl:template match="prog"><xsl:if test="@scope != 'swing'">
    <xsl:apply-templates/>
</xsl:if></xsl:template>
    
<xsl:template match="web">
</xsl:template>
    
<xsl:template match="webdiv">
</xsl:template>
    
<xsl:template match="tex">
   <xsl:apply-templates/>
</xsl:template>
    
<xsl:template match="texdiv">
      <xsl:apply-templates/>
</xsl:template>
    
<xsl:template match="fx">
    <xsl:apply-templates/>
</xsl:template>
    
<xsl:template match="fxdiv">
    <xsl:apply-templates/>
</xsl:template>
    
<xsl:template match="fxSourceItems">
    <xsl:apply-templates/>
</xsl:template>
    
<xsl:template match="swing"></xsl:template>
    
<xsl:template match="swingdiv"></xsl:template>

<xsl:template match="swingSourceItems"></xsl:template>
    
</xsl:stylesheet>
