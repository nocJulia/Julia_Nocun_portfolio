<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="text" encoding="UTF-8"/>

    <xsl:key name="maxLen" match="album/*" use="name()" />

    <xsl:template match="/">

        <xsl:variable name="id-width" select="string-length('Album ID')"/>
        <xsl:variable name="title-width">
            <xsl:value-of select="max(//album/Title/string-length(.))"/>
        </xsl:variable>
        <xsl:variable name="singer-width">
            <xsl:value-of select="max(//album/Singer/string-length(.))"/>
        </xsl:variable>
        <xsl:variable name="year-width" select="string-length('Year')"/>
        <xsl:variable name="price-width">
            <xsl:value-of select="max(//album/price/string-length(.))"/>
        </xsl:variable>
        <xsl:variable name="genre-width">
            <xsl:value-of select="max(//album/genre/string-length(.))"/>
        </xsl:variable>

        <xsl:value-of select="concat(substring('Album ID', 1, $id-width), ' | ')"/>
        <xsl:value-of select="concat(substring(concat('Title', '                        '), 1, $title-width), ' | ')"/>
        <xsl:value-of select="concat(substring(concat('Singer', '                 '), 1, $singer-width), ' | ')"/>
        <xsl:value-of select="concat(substring(concat('Year',' '), 1, $year-width), ' | ')"/>
        <xsl:value-of select="concat(substring(concat('Price',' '), 1, $price-width), ' | ')"/>
        <xsl:value-of select="concat(substring(concat('Genre', '                '), 1, $genre-width), '|')"/>
        <xsl:text>&#10;</xsl:text>

        <xsl:apply-templates select="//album">
            <xsl:with-param name="id-width" select="$id-width"/>
            <xsl:with-param name="title-width" select="$title-width"/>
            <xsl:with-param name="singer-width" select="$singer-width"/>
            <xsl:with-param name="year-width" select="$year-width"/>
            <xsl:with-param name="price-width" select="$price-width"/>
            <xsl:with-param name="genre-width" select="$genre-width"/>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="album">
        <xsl:param name="id-width"/>
        <xsl:param name="title-width"/>
        <xsl:param name="singer-width"/>
        <xsl:param name="year-width"/>
        <xsl:param name="price-width"/>
        <xsl:param name="genre-width"/>

        <!-- album id -->
        <xsl:value-of select="concat(substring(concat(@id, '                    '), 1, $id-width), ' | ')"/>
        <!-- Title -->
        <xsl:value-of select="concat(substring(concat(Title, '                       '), 1, $title-width), ' | ')"/>
        <!-- Singer -->
        <xsl:value-of select="concat(substring(concat(Singer, '                '), 1, $singer-width), ' | ')"/>
        <!-- releaseYear -->
        <xsl:value-of select="concat(substring(concat(releaseYear,' '), 1, $year-width), ' | ')"/>
        <!-- price -->
        <xsl:value-of select="concat(substring(concat(price,' '), 1, $price-width), ' | ')"/>
        <!-- genre -->
        <xsl:value-of select="concat(substring(concat(genre, '               '), 1, $genre-width), '|')"/>
        <xsl:text>&#10;</xsl:text>
    </xsl:template>

</xsl:stylesheet>
