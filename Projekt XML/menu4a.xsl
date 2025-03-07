<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml">
    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>

	<xsl:variable name="totalPrice" select="sum(/musicShop/albums/album/price)"/>
	<xsl:variable name="numAlbums" select="count(/musicShop/albums/album)"/>
	<xsl:variable name="numGenres" select="count(distinct-values(/musicShop/albums/album/genre))"/>

	<xsl:template match="/musicShop">
		<html>
			<head>
				<style type="text/css">
					table {
					font-family: Arial, sans-serif;
					border-collapse: collapse;
					width: 100%;
					}

					th, td {
					border: 1px solid #dddddd;
					text-align: left;
					padding: 8px;
					border-color: darkred;
					}

					th {
					background-color: darkred;
					color: whitesmoke;
					text-align:center;
					}

					.summary {
					margin-top: 20px;
					}

					h1, p, h2 {
					text-align: center;
					font-family: 'Segoe Script';
					}

					body {
					background-color: antiquewhite;
					}
				</style>
			</head>
			<body>
				<xsl:apply-templates select="albums"/>

				<div class="summary">
					<h2>Podsumowanie</h2>
					<p>
						Liczba albumów: <xsl:value-of select="$numAlbums"/>
					</p>
					<p>
						Liczba różnych gatunków muzycznych: <xsl:value-of select="$numGenres"/>
					</p>
					<p>
						Suma cen albumów: <xsl:value-of select="format-number($totalPrice, '#,##0.00')"/> PLN
					</p>
					<p>
						Data wygenerowania raportu: <xsl:value-of select="current-date()"/>
					</p>
				</div>
			</body>
		</html>
	</xsl:template>

	<xsl:template match="albums">
		<h1>Albumy</h1>
		<table>
			<tr>
				<th>Tytuł</th>
				<th>Wykonawca</th>
				<th>Rok wydania</th>
				<th>Cena (PLN)</th>
				<th>Gatunek</th>
			</tr>
			<xsl:apply-templates select="album">
				<xsl:sort select="releaseYear" data-type="number" order="descending"/>
			</xsl:apply-templates>
		</table>
	</xsl:template>

	<xsl:template match="album">
		<tr>
			<td>
				<xsl:value-of select="Title"/>
			</td>
			<td>
				<xsl:value-of select="Singer"/>
			</td>
			<td>
				<xsl:value-of select="releaseYear"/>
			</td>
			<td>
				<xsl:value-of select="format-number(price, '#,##0.00')"/>
			</td>
			<td>
				<xsl:value-of select="genre"/>
			</td>
		</tr>
	</xsl:template>
</xsl:stylesheet>
