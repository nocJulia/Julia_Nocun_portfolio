<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:variable name="barColor" select="'#3498db'" />
  <xsl:variable name="barSpacing" select="10" />
  <xsl:variable name="chartHeight" select="550" />
  <xsl:variable name="chartWidth" select="1400" />
  <xsl:variable name="maxTitleLength" select="21" />

  <xsl:template match="/musicShop">
    <script type="text/javascript">
      <![CDATA[
        function changeColor(element) {
          var currentColor = element.getAttribute('fill');
          var newColor = (currentColor === '#3498db') ? '#e74c3c' : '#3498db';
          element.setAttribute('fill', newColor);
        }
      ]]>
    </script>

    <svg xmlns="http://www.w3.org/2000/svg" width="{$chartWidth}" height="{$chartHeight + 50}">
      <!-- opis osi X -->
      <text x="{$chartWidth div 2}" y="{$chartHeight + 80}" font-size="12" text-anchor="middle">Albums</text>
      <!-- opis osi Y -->
      <text x="10" y="{$chartHeight div 2}" font-size="12" text-anchor="middle" transform="rotate(-90 10,{$chartHeight div 2})">Price</text>
      
      <xsl:apply-templates select="albums/album"/>
    </svg>
  </xsl:template>

  <xsl:template match="album">
    <xsl:variable name="height" select="price" />
    <rect x="{position() * ($barSpacing + 50)}" y="{$chartHeight - $height}" width="40" height="{$height}" fill="#3498db" onclick="changeColor(this)" />
    <!-- Tytuł pod wykresem -->
    <text x="{position() * ($barSpacing + 50) + 20}" y="{$chartHeight + 20}" font-size="10" fill="#000" text-anchor="middle">
      <xsl:call-template name="wrapText">
        <xsl:with-param name="text" select="Title" />
      </xsl:call-template>
    </text>
    <!-- Cena nad wykresem -->
    <text x="{position() * ($barSpacing + 50) + 20}" y="{$chartHeight - $height - 5}" font-size="10" fill="#000" text-anchor="middle">
      <xsl:value-of select="price" />
    </text>
  </xsl:template>

<xsl:template name="wrapText">
  <xsl:param name="text" />
  <xsl:param name="maxLineLength" select="$maxTitleLength" />
  <xsl:param name="currentLine" select="1" />
  <xsl:variable name="words" select="tokenize($text, ' ')" />
  <xsl:variable name="currentWord" select="$words[$currentLine]" />
  
  <tspan x="{position() * ($barSpacing + 50) + 20}" dy="{($currentLine - 1) * 1.2}em">
    <xsl:value-of select="$currentWord" />
  </tspan>

  <xsl:if test="$currentLine &lt; count($words)">
    <xsl:call-template name="wrapText">
      <xsl:with-param name="text" select="$text" />
      <xsl:with-param name="maxLineLength" select="$maxLineLength" />
      <xsl:with-param name="currentLine" select="$currentLine + 1" />
    </xsl:call-template>
  </xsl:if>
</xsl:template>

</xsl:stylesheet>
