<xsl:stylesheet version="1.0"
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
        xmlns:redirect="org.apache.xalan.xslt.extensions.Redirect"
        extension-element-prefixes="redirect"
        xmlns="http://www.w3.org/1999/xhtml">
<xsl:output method="xml"
   omit-xml-declaration="no"
   indent="yes"
   encoding="UTF-8"/>
    
<xsl:template match="/">
   <redirect:write file="epub/OEBPS/javanotes9.opf">
		<package version="2.0" xmlns="http://www.idpf.org/2007/opf" unique-identifier="javanotesID">
		    <metadata xmlns:dc="http://purl.org/dc/elements/1.1/"
		          xmlns:opf="http://www.idpf.org/2007/opf">
		   		<dc:title>Introduction to Programming Using Java, Version 9, JavaFX Edition</dc:title>
		   		<dc:creator opf:role="aut">David J. Eck</dc:creator>
		   		<dc:description>A free introductory programming textbook using Java as the programming language, version 9, JavaFX Edition.</dc:description>
		   		<dc:date>2022-5</dc:date>
		   		<dc:language>en-US</dc:language>
		   		<dc:rights>Copyright 1996-2022 by David J. Eck.  Released under the
		   		    Creative Commons Attribution-Noncommercial-ShareAlike 4.0 License.</dc:rights>
		   		<dc:identifier id="javanotesID">https://math.hws.edu/javanotes9</dc:identifier>
		    </metadata>
		    <manifest>
		        <item id="ncx" href="toc.ncx" media-type="application/x-dtbncx+xml"/>
		        <item id="css" href="javanotes-epub.css" media-type="text/css"/>
		        <item id="cover-image" href="javanotes9-cover-518x675.jpg" media-type="image/jpeg"/>
		    	<item id="titlepage" href="titlepage.html" media-type="application/xhtml+xml"/>
		    	<item id="cover" href="cover.html" media-type="application/xhtml+xml"/>
		    	<item id="quiz-answers-intro" href="quiz-answers-intro.html" media-type="application/xhtml+xml"/>
		    	<item id="exercise-answers-intro" href="exercise-answers-intro.html" media-type="application/xhtml+xml"/>
				<xsl:if test="/javanotes/preface"><item id="preface" href="preface.html" media-type="application/xhtml+xml"/></xsl:if>
		        <xsl:for-each select="/javanotes/chapter">
		           <xsl:variable name="chapter"><xsl:value-of select="position()"/></xsl:variable>
		           <item media-type="application/xhtml+xml"><xsl:attribute name="href"><xsl:value-of select="concat('c',$chapter,'/index.html')"/></xsl:attribute>
		           		<xsl:attribute name="id"><xsl:value-of select="concat('c',$chapter,'-intro')"/></xsl:attribute></item>
		           <xsl:for-each select="section">
		              <xsl:variable name="section"><xsl:value-of select="position()"/></xsl:variable>
		              <item media-type="application/xhtml+xml"><xsl:attribute name="href"><xsl:value-of select="concat('c',$chapter,'/s',$section,'.html')"/></xsl:attribute>
		              	<xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute></item>
		           </xsl:for-each>
		           <xsl:if test="exercises">
			           <item media-type="application/xhtml+xml"><xsl:attribute name="href"><xsl:value-of select="concat('c',$chapter,'/exercises.html')"/></xsl:attribute>
			           		<xsl:attribute name="id"><xsl:value-of select="concat('c',$chapter,'-exercises')"/></xsl:attribute></item>
		               <xsl:for-each select="exercises/exercise">
	  			           <item media-type="application/xhtml+xml"><xsl:attribute name="href"><xsl:value-of select="concat('c',$chapter,'/ex',position(),'-ans.html')"/></xsl:attribute>
				           		<xsl:attribute name="id"><xsl:value-of select="concat('c',$chapter,'-ex',position(),'-ans')"/></xsl:attribute></item>
		               </xsl:for-each>
		           </xsl:if>
		           <xsl:if test="quiz">
			           <item media-type="application/xhtml+xml"><xsl:attribute name="href"><xsl:value-of select="concat('c',$chapter,'/quiz.html')"/></xsl:attribute>
			           		<xsl:attribute name="id"><xsl:value-of select="concat('c',$chapter,'-quiz')"/></xsl:attribute></item>
			           <item media-type="application/xhtml+xml"><xsl:attribute name="href"><xsl:value-of select="concat('c',$chapter,'/quiz_answers.html')"/></xsl:attribute>
			           		<xsl:attribute name="id"><xsl:value-of select="concat('c',$chapter,'-quiz-answers')"/></xsl:attribute></item>
		           </xsl:if>
	               <xsl:for-each select=".//img">
	                  <xsl:variable name="imgname"><xsl:value-of select="@src"/></xsl:variable>
	                  <xsl:variable name="extension"><xsl:value-of select="substring-after($imgname,'.')"/></xsl:variable>
	                  <item><xsl:attribute name="media-type"><xsl:value-of select="concat('image/',$extension)"/></xsl:attribute>
	                  	 <xsl:attribute name="id"><xsl:value-of select="concat('c',$chapter,'-',$imgname)"/></xsl:attribute>
	                  	 <xsl:attribute name="href"><xsl:value-of select="concat('c',$chapter,'/',$imgname)"/></xsl:attribute></item>
 	               </xsl:for-each>
	               <xsl:for-each select=".//applet"><xsl:if test="@img">
	                  <xsl:variable name="imgname"><xsl:value-of select="@img"/></xsl:variable>
	                  <xsl:variable name="extension"><xsl:value-of select="substring-after($imgname,'.')"/></xsl:variable>
	                  <item><xsl:attribute name="media-type"><xsl:value-of select="concat('image/',$extension)"/></xsl:attribute>
	                  	 <xsl:attribute name="id"><xsl:value-of select="concat('c',$chapter,'-',$imgname)"/></xsl:attribute>
	                  	 <xsl:attribute name="href"><xsl:value-of select="concat('c',$chapter,'/',$imgname)"/></xsl:attribute></item>
 	               </xsl:if></xsl:for-each>
		        </xsl:for-each>
				<item id="glossary" href="glossary.html" media-type="application/xhtml+xml"/>
		    </manifest>
		    <spine toc="ncx">
		        <itemref idref="cover"/>
		    	<itemref idref="titlepage"/>
		    	<itemref idref="preface"/>
		        <xsl:for-each select="/javanotes/chapter">
		           <xsl:variable name="chapter"><xsl:value-of select="position()"/></xsl:variable>
		           <itemref><xsl:attribute name="idref"><xsl:value-of select="concat('c',$chapter,'-intro')"/></xsl:attribute></itemref>
		           <xsl:for-each select="section">
		              <xsl:variable name="section"><xsl:value-of select="position()"/></xsl:variable>
		              <itemref><xsl:attribute name="idref"><xsl:value-of select="@id"/></xsl:attribute></itemref>
		           </xsl:for-each>
		           <xsl:if test="exercises">
			           <itemref><xsl:attribute name="idref"><xsl:value-of select="concat('c',$chapter,'-exercises')"/></xsl:attribute></itemref>
		           </xsl:if>
		           <xsl:if test="quiz">
			           <itemref><xsl:attribute name="idref"><xsl:value-of select="concat('c',$chapter,'-quiz')"/></xsl:attribute></itemref>
		           </xsl:if>
		        </xsl:for-each>		    	
		    	<itemref idref="glossary"/>
		    	<itemref linear="no" idref="quiz-answers-intro"/>
		    	<xsl:for-each select="/javanotes/chapter">
		    	   <xsl:if test="quiz">
		              <xsl:variable name="chapter"><xsl:value-of select="position()"/></xsl:variable>
		    	      <itemref linear="no"><xsl:attribute name="idref"><xsl:value-of select="concat('c',$chapter,'-quiz-answers')"/></xsl:attribute></itemref>
		    	   </xsl:if>
		    	</xsl:for-each>
		    	<itemref linear="no" idref="exercise-answers-intro"/>
		    	<xsl:for-each select="/javanotes/chapter">
		    	   <xsl:if test="exercises">
		              <xsl:variable name="chapter"><xsl:value-of select="position()"/></xsl:variable>
		    	      <xsl:for-each select="exercises/exercise">
		                   <itemref linear="no"><xsl:attribute name="idref"><xsl:value-of select="concat('c',$chapter,'-ex',position(),'-ans')"/></xsl:attribute></itemref>
		               </xsl:for-each>
		           </xsl:if>
		    	</xsl:for-each>
		    </spine>
		    <guide>
		        <reference type="cover" title="Cover" href="cover.html"/>
		    	<reference type="title-page" title="Title Page" href="titlepage.html"/>
		    	<reference type="preface" title="Preface" href="preface.html"/>
		    	<reference type="glossary" title="Glossary" href="glossary.html"/>
		    	<reference type="text" title="Chapter 1" href="c1/index.html"/>
		    </guide>
		</package>
   </redirect:write>
   
   <redirect:write file="epub/OEBPS/toc.ncx">
	<ncx xmlns="http://www.daisy.org/z3986/2005/ncx/" version="2005-1" xml:lang="en-US">
	    <head>
	        <meta content="https://math.hws.edu/javanotes9" name="dtb:uid"/>
	    </head>
	    <docTitle>
	        <text>Introduction to Programming Using Java, Version 9.0</text>
	    </docTitle>
	    <docAuthor>
	        <text>David J. Eck</text>
	    </docAuthor>
	    <navMap>
	        <navPoint id="title-page">
	            <navLabel>
	                <text>Title Page</text>
	            </navLabel>
	            <content src="titlepage.html"/>
	        </navPoint>
	        <navPoint id="preface">
	            <navLabel>
	                <text>Preface</text>
	            </navLabel>
	            <content src="preface.html"/>
	        </navPoint>
	        <xsl:for-each select="/javanotes/chapter">
	           <xsl:variable name="chapter"><xsl:value-of select="position()"/></xsl:variable>
	           <navPoint><xsl:attribute name="id"><xsl:value-of select="concat('c',$chapter,'-intro')"/></xsl:attribute>
	              <navLabel>
	                 <text><xsl:value-of select="concat('Chapter ',$chapter, ': ',@title)"/></text>
	              </navLabel>
                  <content><xsl:attribute name="src"><xsl:value-of select="concat('c',$chapter,'/index.html')"/></xsl:attribute></content>
		          <xsl:for-each select="section">
	                <xsl:variable name="section"><xsl:value-of select="position()"/></xsl:variable>
		            <navPoint><xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
		               <navLabel>
		                  <text><xsl:value-of select="concat('Section ',$chapter,'.',$section,': ',@title)"/></text>
		               </navLabel>
		               <content><xsl:attribute name="src"><xsl:value-of select="concat('c',$chapter,'/s',$section,'.html')"/></xsl:attribute></content>
		               <xsl:for-each select="subsection"><xsl:if test="@scope != 'swing'">
			                <xsl:variable name="subsection"><xsl:value-of select="position()"/></xsl:variable>
				            <navPoint><xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
				               <navLabel>
				                  <text><xsl:value-of select="concat('Subsection ',$chapter,'.',$section,'.',$subsection,': ',@title)"/></text>
				               </navLabel>
				               <content><xsl:attribute name="src"><xsl:value-of select="concat('c',$chapter,'/s',$section,'.html#',@id)"/></xsl:attribute></content>
				            </navPoint>  
		               </xsl:if></xsl:for-each>
		            </navPoint>
		          </xsl:for-each>
		           <xsl:if test="exercises">
		               <navPoint><xsl:attribute name="id"><xsl:value-of select="concat('c',$chapter,'-exercises')"/></xsl:attribute>
		                  <navLabel>
		                     <text><xsl:value-of select="concat('Exercises for Chapter ',$chapter)"/></text>
		                  </navLabel>
		                  <content><xsl:attribute name="src"><xsl:value-of select="concat('c',$chapter,'/exercises.html')"/></xsl:attribute></content>
		               </navPoint>
		           </xsl:if>
		           <xsl:if test="quiz">
		               <navPoint><xsl:attribute name="id"><xsl:value-of select="concat('c',$chapter,'-quiz')"/></xsl:attribute>
		                  <navLabel>
		                     <text><xsl:value-of select="concat('Quiz on Chapter ',$chapter)"/></text>
		                  </navLabel>
		                  <content><xsl:attribute name="src"><xsl:value-of select="concat('c',$chapter,'/quiz.html')"/></xsl:attribute></content>
		               </navPoint>
		           </xsl:if>
	           </navPoint>
	        </xsl:for-each>
	        <navPoint id="glossary">
	            <navLabel>
	                <text>Glossary</text>
	            </navLabel>
	            <content src="glossary.html"/>
	        </navPoint>
	        <navPoint id="quiz-answers">
	           <navLabel>
	              <text>Answers To End-of-Chapter Quizzes</text>
	           </navLabel>
	           <content src="quiz-answers-intro.html"/>
	           <xsl:for-each select="/javanotes/chapter">
	              <xsl:variable name="chapter"><xsl:value-of select="position()"/></xsl:variable>
	              <xsl:if test="quiz">
		               <navPoint><xsl:attribute name="id"><xsl:value-of select="concat('c',$chapter,'-quiz-answers')"/></xsl:attribute>
		                  <navLabel>
		                     <text><xsl:value-of select="concat('Chapter ',$chapter, ' Quiz Answers')"/></text>
		                  </navLabel>
		                  <content><xsl:attribute name="src"><xsl:value-of select="concat('c',$chapter,'/quiz_answers.html')"/></xsl:attribute></content>
		               </navPoint>
	              </xsl:if>
	           </xsl:for-each>
	        </navPoint>
	        <navPoint id="exercise-answers">
	           <navLabel>
	              <text>Answers to End-of-Chapter Exercises</text>
	           </navLabel>
	           <content src="exercise-answers-intro.html"/>
	           <xsl:for-each select="/javanotes/chapter">
	              <xsl:variable name="chapter"><xsl:value-of select="position()"/></xsl:variable>
	              <xsl:if test="exercises">
					  <xsl:for-each select="exercises/exercise">
					     <navPoint><xsl:attribute name="id"><xsl:value-of select="concat('c',$chapter,'-ex',position(),'-ans')"/></xsl:attribute>
					        <navLabel>
					            <text><xsl:value-of select="concat('Exercise ', $chapter, '.', position(), ' Answer')"/></text>
					        </navLabel>
					        <content><xsl:attribute name="src"><xsl:value-of select="concat('c',$chapter,'/ex',position(),'-ans.html')"/></xsl:attribute></content>
					     </navPoint>
					  </xsl:for-each>
				  </xsl:if>	           
	           </xsl:for-each>
	        </navPoint>
	    </navMap>
	 </ncx>
   </redirect:write>   

   <redirect:write file="epub/OEBPS/titlepage.html">
        <xsl:text disable-output-escaping="yes">
&lt;!DOCTYPE html PUBLIC  "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd"&gt;
</xsl:text>
        <html xmlns="http://www.w3.org/1999/xhtml">
        <head>
        <title>Javanotes 9, JavaFX Edition -- Title Page</title>
        <link href="javanotes-epub.css" rel="stylesheet" type="text/css"/>
        </head>
        <body>
        <div class="content">
        <hr/>
        <h2 class="chapter_title">Introduction to Programming Using Java</h2>
        <h2 class="chapter_title">Version 9.0, JavaFX Edition</h2>
        <h2 class="chapter_title">May 2022</h2>
        <h4 class="centeralign">Author:&#160; <a href="https://math.hws.edu/eck/">David J. Eck</a>&#160;
                 (<a href="mailto:eck@hws.edu">eck@hws.edu</a>)</h4> 
        <div style="margin-left: 1cm; margin-right:1cm">
        <hr/>
        <p>This is an <b>experimental</b> ebook version of a free Java Programming textbook.  Please excuse any
        irregularities in formatting.  In particular, your reader might have problems with overly wide code segments.
        Note that PDF and web versions
        are available on-line at <a href="https://math.hws.edu/javanotes/">https://math.hws.edu/javanotes/</a>.  
        Source code for examples is <b>not</b> included in
        this ebook, but the ebook does contain links to online copies of the examples.
        You can email comments to the author.</p>
        <hr/>
        <p>&#169;1996--2022, David J. Eck.</p>
        <p>This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/">Creative Commons Attribution-Noncommercial-ShareAlike 4.0 License</a>.
            (This license allows you to redistribute this book in unmodified form for non-commercial purposes.  It allows you
            to make and distribute modified versions for non-commercial purposes, as long as you include an attribution to the
            original author, clearly describe the modifications that you have made, and distribute
            the modified work under the same license as the original.  Permission might be given by the
            author for other uses.  See the license for full
            details.)
            The most recent version of this book is always available, at no
            charge, for downloading and for on-line use at the Web address:
            <a href="https://math.hws.edu/javanotes/">https://math.hws.edu/javanotes/</a></p>
            <hr/>
        </div>
        </div>
        </body>
        </html>
   </redirect:write>

   <xsl:apply-templates select="/javanotes/preface"/>
   <xsl:apply-templates select="/javanotes/chapter"/>
 <!--   <xsl:apply-templates select="/javanotes/source"/> -->
   <xsl:apply-templates select="/javanotes/glossary"/>
</xsl:template>
   

<!-- 
<xsl:template name="table-of-contents">
   <redirect:write file="epub/OEBPS/contents.html">
        <xsl:text disable-output-escaping="yes">
&lt;!DOCTYPE html PUBLIC  "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd"&gt;
</xsl:text>
     <html xmlns="http://www.w3.org/1999/xhtml">
     <head>
     <title>Javanotes 7.0 Table of Contents</title>
     <link href="javanotes-epub.css" rel="stylesheet" type="text/css"/>
     </head>
     <body>
     <div class="content">
        <h3 class="centeralign">Introduction to Programming Using Java, Sixth Edition</h3>
        <h2 class="centeralign">Table of Contents</h2>
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
               <xsl:if test="subsection">
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
        <xsl:if test="/javanotes/glossary">
           <p><b>Appendix:</b>&#160; <a href="glossary.html">Glossary</a></p>
        </xsl:if>
     	</div>
     </div>
     </body>
     </html>
   </redirect:write>
</xsl:template>
-->
 
<xsl:template match="chapter">
   <redirect:write select="concat('epub/OEBPS/c',position(),'/index.html')">
        <xsl:text disable-output-escaping="yes">
&lt;!DOCTYPE html PUBLIC  "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd"&gt;
</xsl:text>
        <html xmlns="http://www.w3.org/1999/xhtml">
        <head>
        <title>Javanotes 9.0, Chapter <xsl:value-of select="position()"/> -- <xsl:value-of select="@title"/></title>
        <link href="../javanotes-epub.css" rel="stylesheet" type="text/css"/>
        </head>
        <body>
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
        </body>
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
   
<xsl:template match="preface">
   <redirect:write select="'epub/OEBPS/preface.html'">
        <xsl:text disable-output-escaping="yes">
&lt;!DOCTYPE html PUBLIC  "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd"&gt;
</xsl:text>
     <html xmlns="http://www.w3.org/1999/xhtml">     <head>
     <title>Javanotes 9 Preface</title>
     <link href="javanotes-epub.css" rel="stylesheet" type="text/css"/>
     </head>
     <body>
     <div class="content">
        <h3 class="centeralign">Introduction to Programming Using Java<br/>
           Version 9.0, JavaFX Edition, May 2022<br/></h3>
        <h2 class="centeralign">Preface</h2>
        <hr class="break"/>
        <xsl:apply-templates/>
     </div>
     <hr/>
     <div class="rightalign"><small><a href="https://math.hws.edu/eck/index.html">David Eck</a></small></div>
     </body>
     </html>
   </redirect:write>
</xsl:template>

<!--  
<xsl:template match="source">
   <redirect:write select="'epub/OEBPS/source/index.html'">
        <xsl:text disable-output-escaping="yes">
&lt;!DOCTYPE html PUBLIC  "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd"&gt;
</xsl:text>
     <html xmlns="http://www.w3.org/1999/xhtml">
     <head>
     <title>Javanotes Source Code</title>
     <link href="../javanotes-epub.css" rel="stylesheet" type="text/css"/>
     </head>
     <body>
     <div class="content">
        <h3 class="centeralign">Introduction to Programming Using Java, Sixth Edition</h3>
        <h2 class="centeralign">Source Code</h2>
        <hr class="break"/>
        <xsl:apply-templates/>
     </div>
     <hr/>
     <div class="rightalign"><small><a href="https://math.hws.edu/eck/index.html">David Eck</a>, June 2011</small></div>
     </body>
     </html>
   </redirect:write>
</xsl:template>
-->

<xsl:template match="glossary">
    <redirect:write select="'epub/OEBPS/glossary.html'">
        <xsl:text disable-output-escaping="yes">
&lt;!DOCTYPE html PUBLIC  "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd"&gt;
</xsl:text>
     <html xmlns="http://www.w3.org/1999/xhtml">
     <head>
     <title>Javanotes Glossary</title>
     <link href="javanotes-epub.css" rel="stylesheet" type="text/css"/>
     </head>
     <body>
     <div class="content">
        <h2 class="centeralign">Glossary</h2>
        <xsl:apply-templates/>
     </div>
     </body>
     </html>
    </redirect:write>
</xsl:template>
    

<xsl:template match="gitem">
    <p class="glossary_item"><span class="glossary_term"><xsl:value-of select="@term"/>.</span>&#160;
    <span class="glossary_definition"><xsl:apply-templates/></span></p>
</xsl:template>
    
<xsl:template match="section">
    <xsl:param name="chapternum"/>
    <redirect:write select="concat('epub/OEBPS/c',$chapternum,'/s',position(),'.html')">
        <xsl:text disable-output-escaping="yes">
&lt;!DOCTYPE html PUBLIC  "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd"&gt;
</xsl:text>
        <html xmlns="http://www.w3.org/1999/xhtml">
        <head>
        <title>Javanotes 9.0, Section <xsl:value-of select="$chapternum"/>.<xsl:value-of select="position()"/> -- <xsl:value-of select="@title"/></title>
        <link href="../javanotes-epub.css" rel="stylesheet" type="text/css"/>
        </head>
        <body>
        <div class="content">
        <h3 class="section_title">Section <xsl:value-of select="$chapternum"/>.<xsl:value-of select="position()"/></h3>
        <h2 class="section_title"><xsl:value-of select="@title"/></h2>
        <hr class="break"/>
        <xsl:apply-templates/>
        </div>
        </body>
        </html>
    </redirect:write>
</xsl:template>
    
<xsl:template match="subsection"><xsl:if test="@scope != 'swing'">
    <hr class="break"/>
    <h3 class="subsection_title"><a><xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute><xsl:if test="not(ancestor::source)"><xsl:number count="chapter"/>.<xsl:number count="section"/>.<xsl:number count="subsection"/>&#160;&#160;</xsl:if><xsl:value-of select="@title"/></a></h3>
    <xsl:apply-templates/>
</xsl:if></xsl:template>

<xsl:template name="do-exercises">
    <xsl:variable name="chapternum">
       <xsl:number count="chapter"/>
    </xsl:variable>
    <redirect:write select="concat('epub/OEBPS/c',$chapternum,'/exercises.html')">
        <xsl:text disable-output-escaping="yes">
&lt;!DOCTYPE html PUBLIC  "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd"&gt;
</xsl:text>
        <html xmlns="http://www.w3.org/1999/xhtml">
        <head>
        <title>Javanotes 9.0, Exercises for Chapter <xsl:value-of select="$chapternum"/></title>
        <link href="../javanotes-epub.css" rel="stylesheet" type="text/css"/>
        </head>
        <body>
        <div class="content">
        <h2>Programming Exercises for Chapter <xsl:value-of select="$chapternum"/></h2>
        <xsl:for-each select="exercises/exercise">
           <hr/>
           <h3 class="exercise">Exercise <xsl:value-of select="$chapternum"/>.<xsl:value-of select="position()"/>:</h3>
           <xsl:apply-templates select="exercise-question"/>
            <p class="rightalign"><a><xsl:attribute name="href"><xsl:value-of select="concat('ex',position(),'-ans.html')"/></xsl:attribute>See the Solution</a></p>
        </xsl:for-each>
        </div>
        </body>
        </html>
    </redirect:write>
</xsl:template>

<xsl:template name="do-exercise-answers">
    <xsl:variable name="chapternum">
       <xsl:number count="chapter"/>
    </xsl:variable>
    <xsl:for-each select="exercises/exercise">
       <redirect:write select="concat('epub/OEBPS/c',$chapternum,'/ex',position(),'-ans.html')">
        <xsl:text disable-output-escaping="yes">
&lt;!DOCTYPE html PUBLIC  "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd"&gt;
</xsl:text>
           <html xmlns="http://www.w3.org/1999/xhtml">
           <head>
           <title>Javanotes 9.0, Solution to Exercise <xsl:value-of select="position()"/>, Chapter <xsl:value-of select="$chapternum"/></title>
           <link href="../javanotes-epub.css" rel="stylesheet" type="text/css"/>
           </head>
           <body>
           <div class="content">
           <h2>Solution for Programming Exercise <xsl:value-of select="$chapternum"/>.<xsl:value-of select="position()"/></h2>
           <hr class="break"/>
              <h3 class="exercise">Exercise <xsl:value-of select="$chapternum"/>.<xsl:value-of select="position()"></xsl:value-of>:</h3>
              <xsl:apply-templates select="exercise-question"/>
           <hr/>
           <div class="centeralign exercisesubtitle"><big><b>Discussion</b></big></div>
           <hr/>
           <xsl:apply-templates select="exercise-discuss"/>
           <hr/>
           <div class="centeralign exercisesubtitle"><big><b>The Solution</b></big></div>
           <hr/>
           <xsl:apply-templates select="exercise-code"/>
           </div>
           </body>
           </html>
       </redirect:write>
    </xsl:for-each>
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
    <redirect:write select="concat('epub/OEBPS/c',$chapternum,'/',$filename,'.html')">
        <xsl:text disable-output-escaping="yes">
&lt;!DOCTYPE html PUBLIC  "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd"&gt;
</xsl:text>
        <html xmlns="http://www.w3.org/1999/xhtml">
        <head>
        <title>Javanotes 9.0, <xsl:if test="$answers">Answers for </xsl:if>Quiz on Chapter <xsl:value-of select="$chapternum"/></title>
        <link href="../javanotes-epub.css" rel="stylesheet" type="text/css"/>
        </head>
        <body>
        <div class="content">
        <h2 class="quiz_title"><xsl:if test="$answers">Answers for </xsl:if>Quiz on Chapter <xsl:value-of select="$chapternum"/></h2>
        <xsl:for-each select="question">
           <p><span class="question">Question&#160;<xsl:number count="question"/>:</span></p>
           <xsl:apply-templates select="ques"/>
           <xsl:if test="$answers">
              <hr class="break"/>
              <p><span class="answer">Answer:</span></p>
              <xsl:apply-templates select="ans"/>
              <hr/>
           </xsl:if>
        </xsl:for-each>
        <xsl:if test="not($answers)">
           <p class="rightalign"><a href="quiz_answers.html">See the Answers</a></p>
        </xsl:if>
        </div>
        </body>
        </html>
    </redirect:write>
</xsl:template>

<xsl:template match="p">
    <p><xsl:if test="@align">
            <xsl:attribute name="class">
                <xsl:value-of select="concat(@align,'align')"/>
            </xsl:attribute>
        </xsl:if>
        <xsl:apply-templates/></p>
</xsl:template>

<xsl:template match="np"><p><xsl:apply-templates/></p></xsl:template>

    
<xsl:template match="ol|ul|li|big|i|b|sup|sub">
   <xsl:copy><xsl:apply-templates/></xsl:copy>
</xsl:template>

<xsl:template match="u">
   <span class="underline"><xsl:apply-templates/></span>
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
         <xsl:otherwise><xsl:text>https://math.hws.edu/eck/cs124/javanotes9/source/chapter</xsl:text><xsl:value-of select="$chapternum"/><xsl:text>/</xsl:text><xsl:value-of select="@href"/></xsl:otherwise>
      </xsl:choose>
   </xsl:variable>
   <a><xsl:attribute name="href"><xsl:value-of select="$ref"/></xsl:attribute>
   <xsl:choose>
      <xsl:when test="text()"><xsl:apply-templates/></xsl:when>
      <xsl:otherwise><xsl:value-of select="@href"/></xsl:otherwise>
   </xsl:choose></a>
</xsl:template>

<!-- 
<xsl:template match="sourceref">
   <span class="sourceref"><xsl:choose>
      <xsl:when test="text()"><xsl:apply-templates/></xsl:when>
      <xsl:otherwise><xsl:value-of select="@href"/></xsl:otherwise>
   </xsl:choose></span>
</xsl:template>
-->
    
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
      <xsl:when test="@margin"><xsl:value-of select="@mrgin"/></xsl:when>
      <xsl:otherwise>100</xsl:otherwise>
   </xsl:choose></xsl:variable>
<div>
<xsl:attribute name="style">margin-left: <xsl:value-of select="$margin"/>; margin-right: <xsl:value-of select="$margin"/>;</xsl:attribute>
<xsl:apply-templates/>
</div>
</xsl:template>

<xsl:template match="img">
  <xsl:variable name="widthstyle">
	  <xsl:choose>
	    <xsl:when test="@width"><xsl:value-of select="concat('width: ',number(@width) div 100,'in; max-width: 95%;')"/></xsl:when>
	    <xsl:otherwise><xsl:text>max-width:95%</xsl:text></xsl:otherwise>
	  </xsl:choose>
  </xsl:variable>
  <div><xsl:attribute name="style"><xsl:value-of select="concat($widthstyle, ' margin-left:auto; margin-right:auto')"/></xsl:attribute>
     <img style="width:100%;height:auto">
        <xsl:attribute name="src"><xsl:value-of select="@src"/></xsl:attribute>
        <xsl:attribute name="alt"><xsl:value-of select="@alt"/></xsl:attribute>
        <xsl:if test="@bordered"><xsl:attribute name="class"><xsl:text>bordered</xsl:text></xsl:attribute></xsl:if>
     </img>
  </div>
</xsl:template>
    
<!-- 
<xsl:template match="img">
    <xsl:choose>
    <xsl:when test="@align">
        <xsl:call-template name="copy-image"></xsl:call-template>
    </xsl:when>
    <xsl:otherwise>
        <p class="centeralign"><xsl:call-template name="copy-image"></xsl:call-template></p>
    </xsl:otherwise>
    </xsl:choose>
</xsl:template>
<xsl:template name="copy-image">
    <img>
       <xsl:attribute name="src"><xsl:value-of select="@src"/></xsl:attribute>
       <xsl:if test="@width"><xsl:attribute name="width"><xsl:value-of select="@width"/></xsl:attribute></xsl:if>
       <xsl:if test="@height"><xsl:attribute name="height"><xsl:value-of select="@height"/></xsl:attribute></xsl:if>
       <xsl:attribute name="alt"><xsl:value-of select="@alt"/></xsl:attribute>
       <xsl:choose>
          <xsl:when test="@bordered and @align"><xsl:attribute name="class"><xsl:value-of select="concat('bordered ',@align,'align')"/></xsl:attribute></xsl:when>
          <xsl:when test="@bordered"><xsl:attribute name="class"><xsl:text>bordered</xsl:text></xsl:attribute></xsl:when>
          <xsl:when test="@align"><xsl:attribute name="class"><xsl:value-of select="concat(@align,'align')"/></xsl:attribute></xsl:when>
       </xsl:choose>
       <xsl:if test="@align"><xsl:attribute name="class"><xsl:value-of select="@align"/></xsl:attribute></xsl:if>
       <xsl:if test="@bordered"><xsl:attribute name="class"><xsl:text>bordered</xsl:text></xsl:attribute></xsl:if>
    </img>
</xsl:template>
-->
   
<xsl:template match="imageclear">
   <br class="clearall"/>
</xsl:template>
    
<xsl:template match="applet"><xsl:if test="@img">
  <img><xsl:attribute name="src"><xsl:value-of select="@img"/></xsl:attribute><xsl:attribute name="alt"><xsl:value-of select="@alt"/></xsl:attribute></img>
</xsl:if></xsl:template>
  
<xsl:template match="param"></xsl:template>
   
<xsl:template match="centered">
<div class="centeralign">
<xsl:apply-templates/>
</div>
</xsl:template>
   
<xsl:template match="endchapter"></xsl:template>
    
<xsl:template match="prog"><xsl:if test="@scope != 'swing'">
    <xsl:apply-templates/>
</xsl:if></xsl:template>
    
<xsl:template match="web"></xsl:template>
    
<xsl:template match="webdiv"></xsl:template>
    
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
