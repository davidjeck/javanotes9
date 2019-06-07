<xsl:stylesheet version="1.0"
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
        xmlns:redirect="org.apache.xalan.xslt.extensions.Redirect"
        extension-element-prefixes="redirect">
<xsl:output method="html"/>
    
<xsl:template match="/">
   <redirect:write file="web/index.html">
        <html>
        <head>
        <title>Javanotes 8.1 -- Title Page</title>
        <link href="javanotes.css" rel="stylesheet" type="text/css"/>
        </head>
        <body><div class="page">
        <div class="content">
        <hr/>
        <h2 class="chapter_title">Introduction to Programming Using Java, Eighth Edition</h2>
        <h2 class="chapter_title">Version 8.1, July 2019</h2>
        <h4 align="center">Author:&#160; <a href="http://math.hws.edu/eck/">David J. Eck</a>&#160;
                 (<a href="mailto:eck@hws.edu">eck@hws.edu</a>)</h4>        
        <hr/>
        <table border="0">
        <tr valign="top"><td><p style="margin-right:20pt"><img height="235" width="180" src="javanotes8-cover-180x235.png"/></p></td>
        <td style="margin-left:1cm">
        <p><big>W</big>ELCOME TO
         the Eighth Edition of <i>Introduction to Programming Using Java</i>,
         a free, on-line textbook on introductory
         programming, which uses Java as the language of instruction.  This book is directed
         mainly towards beginning programmers, although it might also be useful for experienced
         programmers who want to learn something about Java.  It is certainly not meant to
         provide complete coverage of the Java language.
        </p>
        <p>The eighth edition requires Java 8 or higher, including JavaFX. 
         Version&#160;8.1 is a small update of Version&#160;8.0.  This version briefly covers some of the new
         features in Java&#160;11 and makes it clearer how to use this book with Java&#160;11 and later.
         Earlier editions of the book are still available. See the <a href="preface.html">preface</a> for links to
         older editions.</p>
         <p>You can the download this
         web site for use on your own computer. 
         PDF, <span style="white-space:pre">e-book</span>, and print
         versions of the textbook are also available.
         The PDF that includes links might be the best way to read it on your computer.
         <b>Links to the downloads can be found at the bottom of this page.</b>
         </p>
         <p><b>Readers are strongly encouraged to try out the <a href="source/index.html">sample&#160;programs</a> as they read
         the book!  You can download the source code separately or as part of the web site using the links below.
         See <a href="README-running-the-examples.txt">README&#160;file</a>
         for information about how to compile and run the examples.</b></p>
        <h3>Short Table of Contents:</h3>
        <ul class="contents">
        <li><b><a href="contents-with-subsections.html">Full Table of Contents</a></b></li>
        <xsl:if test="/javanotes/preface">
           <li><a href="preface.html">Preface</a></li>
        </xsl:if>
        <xsl:for-each select="/javanotes/chapter">
            <li>Chapter <xsl:value-of select="position()"/>: <b><a><xsl:attribute name="href"><xsl:value-of select="concat('c',position(),'/index.html')"/></xsl:attribute><xsl:value-of select="@title"/></a></b></li>
        </xsl:for-each>
        <xsl:if test="/javanotes/source">
           <li><a href="source/index.html">Source Code for All Examples in this Book</a></li>
        </xsl:if>
        <xsl:if test="/javanotes/glossary">
           <li><a href="glossary.html">Glossary</a></li>
        </xsl:if>
        <li><a href="news.html">News and Errata</a></li>
        </ul>
        <hr/>
        <blockquote>
         <i>&#169;1996--2019, David J. Eck.<br/>
            <small>This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/">Creative Commons Attribution-Noncommercial-ShareAlike 4.0 License</a>.
            (This license allows you to redistribute this book in unmodified form for non-commercial purposes.  It allows you
            to make and distribute modified versions for non-commercial purposes, as long as you include an attribution to the
            original author, clearly describe the modifications that you have made, and distribute
            the modified work under the same license as the original.  Permission might be given by the
            author for other uses.  See the
            <a href="http://creativecommons.org/licenses/by-nc-sa/4.0/">license</a> for full
            details.)<br/><br/>
            This book is available for downloading and for on-line use at the Web address:
            <a href="http://math.hws.edu/javanotes8">http://math.hws.edu/javanote8s/</a></small></i>
          </blockquote>
        <hr/>
        <h3>Downloading And Other Links</h3>
        <ul>
        <li>
          <b>Full Web Site Download:</b>
          <ul>
          <li><b><a href="http://math.hws.edu/eck/cs124/downloads/javanotes8.zip">http://math.hws.edu/eck/cs124/downloads/javanotes8.zip</a></b> &#8212;
          This "zip" archive contains a complete copy of the web site.  It should be usable on almost
          any computer. 
          Size:&#160;5.6&#160;Megabytes.
          </li>
          </ul>
        </li>
        <li>
          <b>Source Code Downloads:</b>
          <ul>
          <li><b><a href="http://math.hws.edu/eck/cs124/downloads/javanotes8-example-programs.zip">http://math.hws.edu/eck/cs124/downloads/javanotes8-example-programs.zip</a></b>  &#8212;
          A zip archive of the "source" directory from the web site, which includes source code for sample programs from the text.
          Note that if you download the complete web site, then you <b>already have</b> a copy of the same source directory.
          See the <a href="README-running-the-examples.txt">README&#160;file</a>. Size: 1.4&#160;Megabytes.
          </li>
          <li><b><a href="http://math.hws.edu/eck/cs124/downloads/javanotes8-exercise-solutions.zip">http://math.hws.edu/eck/cs124/downloads/javanotes8-exercise-solutions.zip</a></b>  &#8212;
          A zip archive containing source code for all the end-of-chapter exercises.  These have been extracted from the
          web pages that contain the solutions as a convenience.  They are <b>not</b> included in the web site download.
          See the <a href="README-exercise-solutions.txt">README&#160;file</a>. Size: 920&#160;Kilobytes.
          </li>
          </ul>
        </li>
        <li>
          <b>PDF Downloads:</b>
          <ul>
          <li>
          <b><a href="http://math.hws.edu/eck/cs124/downloads/javanotes8-linked.pdf">http://math.hws.edu/eck/cs124/downloads/javanotes8-linked.pdf</a></b> &#8212;
          a PDF version with internal links for navigation and external links to source code
          files, exercise solutions, and other resources that are not included in the PDF.
          Recommended for on-screen reading.
          Size:&#160;6.6&#160;Megabytes; 750 pages.
          </li>
          <li>
          <b><a href="http://math.hws.edu/eck/cs124/downloads/javanotes8.pdf">http://math.hws.edu/eck/cs124/downloads/javanotes8.pdf</a></b> &#8212;
          a PDF version without links, more suitable for printing.  <!-- This PDF is in the format that is used for the printed version of the text,
          except that it also includes an appendix listing example programs and a glossary (since they would have
          exceeded the lulu.com page limit). -->
          Size:&#160;6.1&#160;Megabytes; 759 pages.
          </li>
          </ul>
        </li>
        <li>
           <b>E-book Downloads.</b> 
           <ul>
           <li>
           <b><a href="http://math.hws.edu/eck/cs124/downloads/javanotes8.mobi">http://math.hws.edu/eck/cs124/downloads/javanotes8.mobi</a></b>, for Kindle.
           </li>
           <li>
           <b><a href="http://math.hws.edu/eck/cs124/downloads/javanotes8.epub">http://math.hws.edu/eck/cs124/downloads/javanotes8.epub</a></b>, for most other ebook readers.<br/>
           &#160;&#160;&#160;&#160;&#160;&#160;These should be considered experimental.  Depending on the particular ebook reader that you use,
           there can be problems with rendering of long lines in program code sample.  You might find that lines that are too long
           to fit across your screen are incorrectly split into multiple lines, or that the part that extends beyond the right
           margin is simply dropped.  On some readers, you might be able to scroll horizontally to see the hidden text.
           The ebooks include answers to quizzes
           and exercises but do not include source code for sample programs; the sample programs can be downloaded separately, above.
           </li>
           </ul>
        </li>
        <li>
          <b>Print Version Available from Lulu.com:</b>
          <ul>
          <li><b>Printed version</b> &#8212; I have made this book available for
          purchase in a printed version from the print-on-demand publisher
          <a href="http://www.lulu.com">lulu.com</a>.  This is for convenience only, for those who would
          like to have a bound printout in a nice form.
          The book is available in a rather thick printed version at
          <a href="http://www.lulu.com/content/23831534">http://www.lulu.com/content/23831534</a>.
          (Please <b>do not</b> feel obliged to buy the printed version; I do not make any money from it!)
<!--           It is also available in two parts as <a href="http://www.lulu.com/content/559884">http://www.lulu.com/content/559884</a>
          and <a href="http://www.lulu.com/content/822314">http://www.lulu.com/content/822314</a>.
-->
          </li>
          </ul>
        </li>
        <li>
          <b>Source Files for the Book</b>
          <ul>
          <li><b>Complete Sources</b> &#8212; The complete source files that are used to produce both the web site
          and PDF versions of this book are available for download, but will be useful only to a very limited
          audience.  See the end of the <a href="preface.html">preface</a> for more information and a link.</li>
          </ul>
        </li>
        </ul>
        </td></tr></table>
        <hr/>
        <div align="right"><small><i>
                 (8 July 2019, Version 8.1 Released)<br/>
                 (5 December 2018, Version 8 Released)
              </i></small></div>
        </div>
        </div></body>
        </html>
   </redirect:write>
   <xsl:call-template name="table-of-contents">
      <xsl:with-param name="subsections" select="true()"/>
   </xsl:call-template>
   <xsl:call-template name="table-of-contents">
      <xsl:with-param name="subsections" select="false()"/>
   </xsl:call-template>
   <xsl:apply-templates select="/javanotes/preface"/>
   <xsl:apply-templates select="/javanotes/chapter"/>
   <xsl:apply-templates select="/javanotes/source"/>
   <xsl:apply-templates select="/javanotes/glossary"/>
</xsl:template>
   
<xsl:template name="table-of-contents">
   <xsl:param name="subsections"/>
   <xsl:variable name="file">web/contents<xsl:if test="$subsections">-with-subsections</xsl:if>.html</xsl:variable>
   <redirect:write select="$file">
     <html>
     <head>
     <title>Javanotes 8.1 Table of Contents</title>
     <link href="javanotes.css" rel="stylesheet" type="text/css"/>
     </head>
     <body><div class="page">
     <div class="content">
        <h3 align="center">Introduction to Programming Using Java, Eighth Edition</h3>
        <h2 align="center">Table of Contents</h2>
        <hr/>
        <p align="center"><span class="start"><big>T</big>his is the Table of Contents</span> for the free on-line
           textbook <a href="index.html"><i>Introduction to Programming Using Java</i></a>.</p>
        <xsl:choose>
           <xsl:when test="$subsections">
              <p align="center"><b><a href="contents.html">(Click here to hide subsections.)</a></b></p>
           </xsl:when>
           <xsl:otherwise>
              <p align="center"><b><a href="contents-with-subsections.html">(Click here to show subsections.)</a></b></p>
           </xsl:otherwise>
        </xsl:choose>
        <hr/>
        <div style="margin-left: 30pt">
        <p><a href="preface.html"><b>Preface</b></a></p>
        <xsl:for-each select="/javanotes/chapter">
           <xsl:variable name="chapter"><xsl:value-of select="position()"/></xsl:variable>
           <p><b>Chapter <xsl:value-of select="$chapter"/>:&#160;
              <a><xsl:attribute name="href"><xsl:value-of 
                      select="concat('c',$chapter,'/index.html')"/></xsl:attribute><xsl:value-of select="@title"/></a></b></p>
           <ul>
           <xsl:for-each select="section">
              <xsl:variable name="section"><xsl:value-of select="position()"/></xsl:variable>
              <li>Section <xsl:value-of select="$chapter"/>.<xsl:value-of select="$section"/>&#160;
                 <a><xsl:attribute name="href"><xsl:value-of 
                         select="concat('c',$chapter,'/s',$section,'.html')"/></xsl:attribute><xsl:value-of select="@title"/></a>
               <xsl:if test="$subsections and subsection">
                  <ul>
                     <xsl:for-each select="subsection">
                        <li>
                           <xsl:value-of select="$chapter"/>.<xsl:value-of select="$section"/>.<xsl:value-of select="position()"/>&#160;
                                 <a><xsl:attribute name="href"><xsl:value-of 
                                        select="concat('c',$chapter,'/s',$section,'.html#',@id)"/></xsl:attribute><xsl:value-of select="@title"/></a>
                        </li>
                     </xsl:for-each>
                  </ul>
               </xsl:if>
              </li>
           </xsl:for-each>
           <xsl:if test="exercises">
              <li><a><xsl:attribute name="href"><xsl:value-of select="concat('c',$chapter,'/exercises.html')"/></xsl:attribute>Programming Exercises</a></li>
           </xsl:if>
           <xsl:if test="quiz">
              <li><a><xsl:attribute name="href"><xsl:value-of select="concat('c',$chapter,'/quiz.html')"/></xsl:attribute>Quiz</a></li>
           </xsl:if>
           </ul>
        </xsl:for-each>
        <p><b>Appendix:</b>&#160; <a href="source/index.html">Source Code for the Examples in this Book</a></p>
        <xsl:if test="/javanotes/glossary">
           <p><b>Appendix:</b>&#160; <a href="glossary.html">Glossary</a></p>
        </xsl:if>
     </div>
     <hr/>
     <div align="right"><small><a href="http://math.hws.edu/eck/index.html">David Eck</a>, July 2019</small></div>
     </div>
     </div></body>
     </html>
   </redirect:write>
</xsl:template>

<xsl:template match="chapter">
   <redirect:write select="concat('web/c',position(),'/index.html')">
        <html>
        <head>
        <title>Javanotes 8.1, Chapter <xsl:value-of select="position()"/> -- <xsl:value-of select="@title"/></title>
        <link href="../javanotes.css" rel="stylesheet" type="text/css"/>
        </head>
        <body><div class="page">
        <div align="right"><xsl:call-template name="chapter-navbar"/></div>
        <hr/>
        <div class="content">
        <h3 class="chapter_title">Chapter <xsl:value-of select="position()"/></h3>
        <h2 class="chapter_title"><xsl:value-of select="@title"/></h2>
        <hr class="break"/>
        <xsl:apply-templates select="intro"/>
        <hr class="break"/>
        <h3>Contents of Chapter <xsl:value-of select="position()"/>:</h3>
        <ul class="contents">
        <xsl:for-each select="section">
            <li>Section <xsl:value-of select="position()"/>: <a><xsl:attribute name="href"><xsl:value-of select="concat('s',position(),'.html')"/></xsl:attribute><xsl:value-of select="@title"/></a></li>
        </xsl:for-each>
        <xsl:if test="exercises">
           <li><a href="exercises.html">Programming Exercises</a></li>
        </xsl:if>
        <xsl:if test="quiz">
           <li><a href="quiz.html">Quiz on This Chapter</a></li>
        </xsl:if>
        </ul>
        </div>
        <hr/>
        <div align="right"><xsl:call-template name="chapter-navbar"/></div>
        </div></body>
        </html>
   </redirect:write>
   <xsl:apply-templates select="section">
       <xsl:with-param name="chapternum" select="position()"/>
   </xsl:apply-templates>
   <xsl:if test="exercises">
      <xsl:call-template name="do-exercises"></xsl:call-template>
      <xsl:call-template name="do-exercise-answers"></xsl:call-template>
   </xsl:if>
   <xsl:apply-templates select="quiz">
      <xsl:with-param name="answers" select="false()"/>
   </xsl:apply-templates>
   <xsl:apply-templates select="quiz">
      <xsl:with-param name="answers" select="true()"/>
   </xsl:apply-templates>
</xsl:template>
<xsl:template name="chapter-navbar">
    <small>
        [  <a href="s1.html">First Section</a> |
           <xsl:if test="position()>1">
               <a><xsl:attribute name="href"><xsl:value-of select="concat('../c',position()-1,'/index.html')"/></xsl:attribute>Previous Chapter</a> |
           </xsl:if>
           <xsl:if test="not(position()=last())">
               <a><xsl:attribute name="href"><xsl:value-of select="concat('../c',position()+1,'/index.html')"/></xsl:attribute>Next Chapter</a> |
           </xsl:if>
        <a href="../index.html">Main Index</a> ]
    </small>
</xsl:template>
   
<xsl:template match="preface">
   <redirect:write select="'web/preface.html'">
     <html>
     <head>
     <title>Javanotes 8.1 Preface</title>
     <link href="javanotes.css" rel="stylesheet" type="text/css"/>
     </head>
     <body><div class="page">
     <div class="content">
        <h3 align="center">Introduction to Programming Using Java<br/>
           Version 8.1, July 2019<br/></h3>
        <h2 align="center">Preface</h2>
        <hr class="break"/>
        <xsl:apply-templates/>
     </div>
     <hr/>
     <div align="right"><small><a href="http://math.hws.edu/eck/index.html">David Eck</a></small></div>
     </div></body>
     </html>
   </redirect:write>
</xsl:template>
    
<xsl:template match="source">
   <redirect:write select="'web/source/index.html'">
     <html>
     <head>
     <title>Javanotes Source Code</title>
     <link href="../javanotes.css" rel="stylesheet" type="text/css"/>
     </head>
     <body><div class="page">
     <div class="content">
        <h3 align="center">Introduction to Programming Using Java, Eighth Edition</h3>
        <h2 align="center">Source Code</h2>
        <hr class="break"/>
        <xsl:apply-templates/>
     </div>
     <hr/>
     <div align="right"><small><a href="http://math.hws.edu/eck/index.html">David Eck</a>, July 2019</small></div>
     </div></body>
     </html>
   </redirect:write>
</xsl:template>

<xsl:template match="glossary">
    <redirect:write select="'web/glossary.html'">
     <html>
     <head>
     <title>Javanotes Glossary</title>
     <link href="javanotes.css" rel="stylesheet" type="text/css"/>
     </head>
     <body><div class="page">
     <div class="content">
        <h3 align="center">Introduction to Programming Using Java, Eighth Edition</h3>
        <h2 align="center">Glossary</h2>
        <hr class="break"/>
        <xsl:apply-templates/>
     </div>
     <hr/>
     <div align="right"><small><a href="http://math.hws.edu/eck/index.html">David Eck</a>, July 2019</small></div>
     </div></body>
     </html>
    </redirect:write>
</xsl:template>
    

<xsl:template match="gitem">
    <p class="glossary_item"><span class="glossary_term"><xsl:value-of select="@term"/>.</span>&#160;
    <span class="glossary_definition"><xsl:apply-templates/></span></p>
</xsl:template>
    
<xsl:template match="section">
    <xsl:param name="chapternum"/>
    <redirect:write select="concat('web/c',$chapternum,'/s',position(),'.html')">
        <html>
        <head>
        <title>Javanotes 8.1, Section <xsl:value-of select="$chapternum"/>.<xsl:value-of select="position()"/> -- <xsl:value-of select="@title"/></title>
        <link href="../javanotes.css" rel="stylesheet" type="text/css"/>
        </head>
        <body><div class="page">
        <div align="right"><xsl:call-template name="section-navbar"/></div>
        <hr/>
        <xsl:if test="subsection">
           <table align="right" border="2" cellpadding="5" hspace="8" vspace="8" class="subsections"><tr><td>
              <div align="center">
                 <b>Subsections</b><hr/>
                 <small><xsl:for-each select="subsection">
                    <a><xsl:attribute name="href"><xsl:value-of select="concat('#',@id)"/></xsl:attribute><xsl:value-of select="@title"/></a><br/>
                 </xsl:for-each>
                 </small>
              </div>
           </td></tr></table>
        </xsl:if>
        <div class="content">
        <h3 class="section_title">Section <xsl:value-of select="$chapternum"/>.<xsl:value-of select="position()"/></h3>
        <h2 class="section_title"><xsl:value-of select="@title"/></h2>
        <hr class="break"/>
        <xsl:apply-templates/>
        </div>
        <hr/>
        <div align="right"><xsl:call-template name="section-navbar"/></div>
        </div></body>
        </html>
    </redirect:write>
</xsl:template>
<xsl:template name="section-navbar">
    <small>
        [  <xsl:if test="position()>1">
               <a><xsl:attribute name="href"><xsl:value-of select="concat('s',position()-1,'.html')"/></xsl:attribute>Previous Section</a> |
           </xsl:if>
           <xsl:if test="not(position()=last())">
               <a><xsl:attribute name="href"><xsl:value-of select="concat('s',position()+1,'.html')"/></xsl:attribute>Next Section</a> |
           </xsl:if>
        <a href="index.html">Chapter Index</a> | 
        <a href="../index.html">Main Index</a> ]
    </small>
</xsl:template>
    
<xsl:template match="subsection">
    <hr class="break"/>
    <h3 class="subsection_title"><a><xsl:attribute name="name"><xsl:value-of select="@id"/></xsl:attribute><xsl:if test="not(ancestor::source)"><xsl:number count="chapter"/>.<xsl:number count="section"/>.<xsl:number count="subsection"/>&#160;&#160;</xsl:if><xsl:value-of select="@title"/></a></h3>
    <xsl:apply-templates/>
</xsl:template>

<xsl:template name="do-exercises">
    <xsl:variable name="chapternum">
       <xsl:number count="chapter"/>
    </xsl:variable>
    <redirect:write select="concat('web/c',$chapternum,'/exercises.html')">
        <html>
        <head>
        <title>Javanotes 8.1, Exercises for Chapter <xsl:value-of select="$chapternum"/></title>
        <link href="../javanotes.css" rel="stylesheet" type="text/css"/>
        </head>
        <body><div class="page">
        <div align="right"><xsl:call-template name="exercises-navbar"></xsl:call-template></div>
        <hr/>
        <div class="content">
        <h2>Programming Exercises for Chapter <xsl:value-of select="$chapternum"/></h2>
        <hr class="break"/>
        <p><span class="start"><big>T</big>his page contains</span> several exercises for Chapter <xsl:value-of select="$chapternum"/>
        in <a href="../index.html">Introduction to Programming Using Java</a>.  For each exercise, a link to
        a possible solution is provided.   Each solution includes a discussion of how a programmer might approach the
        problem and interesting points raised by the problem or its solution, as well as complete source
        code of the solution.</p>
        <xsl:for-each select="exercises/exercise">
           <hr/>
           <h3 class="exercise">Exercise <xsl:value-of select="$chapternum"/>.<xsl:value-of select="position()"/>:</h3>
           <xsl:apply-templates select="exercise-question"/>
           <p align="right"><a><xsl:attribute name="href"><xsl:value-of select="concat('ex',position(),'-ans.html')"/></xsl:attribute>See the Solution</a></p>
        </xsl:for-each>
        </div>
        <hr/>
        <div align="right"><xsl:call-template name="exercises-navbar"></xsl:call-template></div>
        </div></body>
        </html>
    </redirect:write>
</xsl:template>
<xsl:template name="exercises-navbar">
    <small>
        [  <a href="index.html">Chapter Index</a> | 
        <a href="../index.html">Main Index</a> ]
    </small>
</xsl:template>
       
<xsl:template name="do-exercise-answers">
    <xsl:variable name="chapternum">
       <xsl:number count="chapter"/>
    </xsl:variable>
    <xsl:for-each select="exercises/exercise">
       <redirect:write select="concat('web/c',$chapternum,'/ex',position(),'-ans.html')">
           <html>
           <head>
           <title>Javanotes 8.1, Solution to Exercise <xsl:value-of select="position()"/>, Chapter <xsl:value-of select="$chapternum"/></title>
           <link href="../javanotes.css" rel="stylesheet" type="text/css"/>
           </head>
           <body><div class="page">
           <div align="right"><xsl:call-template name="exercise-answer-navbar"></xsl:call-template></div>
           <hr/>
           <div class="content">
           <h2>Solution for Programming Exercise <xsl:value-of select="$chapternum"/>.<xsl:value-of select="position()"/></h2>
           <hr class="break"/>
           <p><span class="start"><big>T</big>his page contains</span> a sample solution to
           one of the exercises from <a href="../index.html">Introduction to Programming Using Java</a>.</p>
           <hr/>
              <h3 class="exercise">Exercise <xsl:value-of select="$chapternum"/>.<xsl:value-of select="position()"></xsl:value-of>:</h3>
              <xsl:apply-templates select="exercise-question"/>
           <hr/>
           <div align="center" class="exercisesubtitle"><big><b>Discussion</b></big></div>
           <hr/>
           <xsl:apply-templates select="exercise-discuss"/>
           <hr/>
           <div align="center" class="exercisesubtitle"><big><b>The Solution</b></big></div>
           <hr/>
           <xsl:apply-templates select="exercise-code"/>
           </div>
           <hr/>
           <div align="right"><xsl:call-template name="exercise-answer-navbar"></xsl:call-template></div>
           </div></body>
           </html>
       </redirect:write>
    </xsl:for-each>
</xsl:template>
<xsl:template name="exercise-answer-navbar">
    <small>
        [ <a href="exercises.html">Exercises</a> |
        <a href="index.html">Chapter Index</a> | 
        <a href="../index.html">Main Index</a> ]
    </small>
</xsl:template>

<xsl:template match="quiz">
    <xsl:param name="answers"/>
    <xsl:variable name="chapternum">
       <xsl:number count="chapter"/>
    </xsl:variable>
    <xsl:variable name="filename">
       <xsl:choose>
          <xsl:when test="$answers"><xsl:text>quiz_answers</xsl:text></xsl:when>
          <xsl:otherwise><xsl:text>quiz</xsl:text></xsl:otherwise>
       </xsl:choose>
    </xsl:variable>
    <redirect:write select="concat('web/c',$chapternum,'/',$filename,'.html')">
        <html>
        <head>
        <title>Javanotes 8.1, <xsl:if test="$answers">Answers for </xsl:if>Quiz on Chapter <xsl:value-of select="$chapternum"/></title>
        <link href="../javanotes.css" rel="stylesheet" type="text/css"/>
        </head>
        <body><div class="page">
        <div align="right"><xsl:call-template name="quiz-navbar"><xsl:with-param name="answers" select="$answers"/></xsl:call-template></div>
        <hr/>
        <div class="content">
        <h2 class="quiz_title"><xsl:if test="$answers">Answers for </xsl:if>Quiz on Chapter <xsl:value-of select="$chapternum"/></h2>
           <xsl:choose>
           <xsl:when test="$answers">
           <p><span class="start"><big>T</big>his page contains</span> sample answers to the quiz on Chapter <xsl:value-of select="$chapternum"/> of
           <a href="../index.html"><i>Introduction to Programming Using Java</i></a>.
           Note that generally, there are lots of correct answers to a given question.</p>
           </xsl:when>
           <xsl:otherwise>
           <p><span class="start"><big>T</big>his page contains</span> questions on Chapter <xsl:value-of select="$chapternum"/> of
           <a href="../index.html"><i>Introduction to Programming Using Java</i></a>.
           You should be able to answer these questions after studying that chapter.
           Sample answers to these questions can be found <a href="quiz_answers.html">here</a>.</p>
           </xsl:otherwise>
        </xsl:choose>
        <xsl:for-each select="question">
           <div class="quiz-question"><p class="question">Question&#160;<xsl:number count="question"/>:</p>
           <xsl:apply-templates select="ques"/>
           </div>
           <xsl:if test="$answers">
              <div class="quiz-answer"><p class="answer">Answer:</p>
              <xsl:apply-templates select="ans"/>
              </div>
           </xsl:if>
        </xsl:for-each>
        </div>
        <hr/>
        <div align="right"><xsl:call-template name="quiz-navbar"><xsl:with-param name="answers" select="$answers"/></xsl:call-template></div>
        </div></body>
        </html>
    </redirect:write>
</xsl:template>
<xsl:template name="quiz-navbar">
    <xsl:param name="answers"/>
    <small>
        [  <xsl:if test="not($answers)"><a href="quiz_answers.html">Quiz Answers</a> | </xsl:if>
        <a href="index.html">Chapter Index</a> | 
        <a href="../index.html">Main Index</a> ]
    </small>
</xsl:template>

<xsl:template match="p">
    <p><xsl:if test="@align">
            <xsl:attribute name="align">
                <xsl:value-of select="@align"/>
            </xsl:attribute>
        </xsl:if>
        <xsl:apply-templates/></p>
</xsl:template>

<xsl:template match="np"><p><xsl:apply-templates/></p></xsl:template>

    
<xsl:template match="ol|ul|li|big|u|i|b|sup|sub">
   <xsl:copy><xsl:apply-templates/></xsl:copy>
</xsl:template>

<xsl:template match="pre">
   <xsl:choose>
      <xsl:when test="ancestor::exercise-code"><pre class="exercisecode">
         <xsl:apply-templates/>
         </pre>
      </xsl:when>
      <xsl:otherwise><pre>
         <xsl:apply-templates/>
         </pre>
      </xsl:otherwise>
   </xsl:choose>
</xsl:template>

<xsl:template match="code|ptype|newword|codedef|bnf|newcode|start|classname|atype">
    <span><xsl:attribute name="class"><xsl:value-of select="local-name()"/></xsl:attribute><xsl:apply-templates/></span>
</xsl:template>
    
<xsl:template match="a">
    <a><xsl:attribute name="href"><xsl:value-of select="@href"/></xsl:attribute><xsl:apply-templates/></a>
</xsl:template>
    
<xsl:template match="sourceref">
   <xsl:variable name="chapternum"><!-- chapter attribute must be a chapter number; used occasionally, only in chapters, for ref to example in another chapter -->
      <xsl:choose>
         <xsl:when test="@chapter"><xsl:value-of select="@chapter"/></xsl:when>
         <xsl:otherwise><xsl:number count="chapter"/></xsl:otherwise>
      </xsl:choose>
   </xsl:variable>
   <xsl:variable name="ref">
      <xsl:choose>
         <xsl:when test="ancestor::source"><xsl:value-of select="@href"/></xsl:when>
         <xsl:otherwise><xsl:text>../source/chapter</xsl:text><xsl:value-of select="$chapternum"/><xsl:text>/</xsl:text><xsl:value-of select="@href"/></xsl:otherwise>
      </xsl:choose>
   </xsl:variable>
   <span class="sourceref"><a><xsl:attribute name="href"><xsl:value-of select="$ref"/></xsl:attribute>
   <xsl:choose>
      <xsl:when test="text()"><xsl:apply-templates/></xsl:when>
      <xsl:otherwise><xsl:value-of select="@href"/></xsl:otherwise>
   </xsl:choose>
   </a></span>
</xsl:template>

<xsl:template match="jarref">
   <xsl:variable name="ref">
      <xsl:text>../jars/chapter</xsl:text><xsl:number count="chapter"/><xsl:text>/</xsl:text><xsl:value-of select="@href"/>   
   </xsl:variable>
   <span class="sourceref"><a><xsl:attribute name="href"><xsl:value-of select="$ref"/></xsl:attribute>
   <xsl:choose>
      <xsl:when test="text()"><xsl:apply-templates/></xsl:when>
      <xsl:otherwise><xsl:value-of select="@href"/></xsl:otherwise>
   </xsl:choose>
   </a></span>
</xsl:template>
    
<xsl:template match="localref">
    <xsl:if test="not(id(@href))"><xsl:message>Undefined reference <xsl:value-of select="@href"/></xsl:message></xsl:if>
    <xsl:variable name="href">
        <xsl:choose>
            <xsl:when test="not(id(@href))"><xsl:value-of select="@href"/></xsl:when>
            <xsl:when test="name(id(@href))='chapter'">
                <xsl:for-each select="id(@href)">
                <xsl:text>../c</xsl:text>
                <xsl:number count="chapter"/>
                <xsl:text>/index.html</xsl:text>
                </xsl:for-each>
            </xsl:when>
            <xsl:when test="name(id(@href))='section'">
                <xsl:for-each select="id(@href)">
                <xsl:text>../c</xsl:text>
                <xsl:number count="chapter"/>
                <xsl:text>/s</xsl:text>
                <xsl:number count="section" from="chapter"/>
                <xsl:text>.html</xsl:text>
                </xsl:for-each>
            </xsl:when>
            <xsl:when test="name(id(@href))='subsection'">
                <xsl:for-each select="id(@href)">
                <xsl:text>../c</xsl:text>
                <xsl:number count="chapter"/>
                <xsl:text>/s</xsl:text>
                <xsl:number count="section" from="chapter"/>
                <xsl:text>.html#</xsl:text>
                <xsl:value-of select="@id"/>
                </xsl:for-each>
            </xsl:when>
            <xsl:when test="name(id(@href))='exercise'">
               <xsl:for-each select="id(@href)">
                <xsl:text>../c</xsl:text>
                <xsl:number count="chapter"/>
                <xsl:text>/ex</xsl:text>
                <xsl:number count="exercise" from="chapter"/>
                <xsl:text>-ans.html</xsl:text>
               </xsl:for-each>
            </xsl:when>
            <xsl:when test="name(id(@href))='fudgeref'">futurelink</xsl:when>
        </xsl:choose>
    </xsl:variable>
    <xsl:variable name="reftext">
        <xsl:choose>
            <xsl:when test="text()"><xsl:apply-templates/></xsl:when>
            <xsl:when test="not(id(@href))"><xsl:text>(unknown&#160;reference)</xsl:text></xsl:when>
            <xsl:when test="name(id(@href))='chapter'">
                <xsl:for-each select="id(@href)">
                <xsl:text>Chapter&#160;</xsl:text>
                <xsl:number count="chapter"/>
                </xsl:for-each>
            </xsl:when>
            <xsl:when test="name(id(@href))='section'">
                <xsl:for-each select="id(@href)">
                <xsl:text>Section&#160;</xsl:text>
                <xsl:number count="chapter"/>
                <xsl:text>.</xsl:text>
                <xsl:number count="section" from="chapter"/>
                </xsl:for-each>
            </xsl:when>
            <xsl:when test="name(id(@href))='subsection'">
                <xsl:for-each select="id(@href)">
                <xsl:text>Subsection&#160;</xsl:text>
                <xsl:number count="chapter"/>
                <xsl:text>.</xsl:text>
                <xsl:number count="section" from="chapter"/>
                <xsl:text>.</xsl:text>
                <xsl:number count="subsection" from="section"/>
                </xsl:for-each>
            </xsl:when>
           <xsl:when test="name(id(@href))='exercise'">
                <xsl:for-each select="id(@href)">
                <xsl:text>Exercise&#160;</xsl:text>
                <xsl:number count="chapter"/>
                <xsl:text>.</xsl:text>
                <xsl:number count="exercise"/>
                </xsl:for-each>
           </xsl:when>
            <xsl:when test="name(id(@href))='fudgeref'">
                <xsl:for-each select="id(@href)"><xsl:value-of select="@text"/></xsl:for-each>
            </xsl:when>
        </xsl:choose>
    </xsl:variable>
    <a><xsl:attribute name="href"><xsl:value-of select="$href"/></xsl:attribute><xsl:value-of select="$reftext"/></a>
</xsl:template>

<xsl:template match="fudgeref"/>
    
<xsl:template match="break">
    <hr class="break"/>
</xsl:template>
   
<xsl:template match="br">
   <br/>
</xsl:template>
   
<xsl:template match="narrower">
   <xsl:variable name="margin"><xsl:choose>
      <xsl:when test="@margin"><xsl:value-of select="@margin"/></xsl:when>
      <xsl:otherwise>100</xsl:otherwise>
   </xsl:choose></xsl:variable>
<div>
<xsl:attribute name="style">margin-left: <xsl:value-of select="$margin"/>; margin-right: <xsl:value-of select="$margin"/>;</xsl:attribute>
<xsl:apply-templates/>
</div>
</xsl:template>
    
<xsl:template match="img">
    <xsl:choose>
    <xsl:when test="@align">
        <xsl:call-template name="copy-image"></xsl:call-template>
    </xsl:when>
    <xsl:otherwise>
        <p align="center"><xsl:call-template name="copy-image"></xsl:call-template></p>
    </xsl:otherwise>
    </xsl:choose>
</xsl:template>
<xsl:template name="copy-image">
    <img>
       <xsl:attribute name="src"><xsl:value-of select="@src"/></xsl:attribute>
       <xsl:if test="@width"><xsl:attribute name="width"><xsl:value-of select="@width"/></xsl:attribute></xsl:if>
       <xsl:if test="@height"><xsl:attribute name="height"><xsl:value-of select="@height"/></xsl:attribute></xsl:if>
       <xsl:if test="@align"><xsl:attribute name="align"><xsl:value-of select="@align"/></xsl:attribute></xsl:if>
       <xsl:attribute name="alt"><xsl:value-of select="@alt"/></xsl:attribute>
       <xsl:if test="@bordered"><xsl:attribute name="class"><xsl:text>bordered</xsl:text></xsl:attribute></xsl:if>
    </img>
</xsl:template>
   
<xsl:template match="imageclear">
   <br clear="all"/>
</xsl:template>
    
<xsl:template match="centered">
<div align="center">
<xsl:apply-templates/>
</div>
</xsl:template>
   
<xsl:template match="endchapter">
    <hr/>
    <div align="center"><b><small>End of Chapter <xsl:number count="chapter"/></small></b></div>
    <hr/>
</xsl:template>
    
<xsl:template match="prog">
    <xsl:apply-templates/>
</xsl:template>
    
<xsl:template match="web">
    <xsl:apply-templates/>
</xsl:template>
    
<xsl:template match="webdiv">
    <xsl:apply-templates/>
</xsl:template>
    
<xsl:template match="tex"></xsl:template>
    
<xsl:template match="texdiv"></xsl:template>
    
</xsl:stylesheet>
