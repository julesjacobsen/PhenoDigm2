<%-- 
    Document   : genes
    Created on : 28-Aug-2013, 10:15:02
    Author     : jj8
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Browse Genes</title>
    </head>
    <body>
        <h1>Genes</h1>
        <p>
        This will contain a list of all genes with links to the geneView page from each gene.
        </p>
        ${geneIdentifier.geneSymbol} (${geneIdentifier.databaseCode}:${geneIdentifier.databaseAcc}) <a href="gene/${geneIdentifier.databaseCode}:${geneIdentifier.databaseAcc}">link</a>
    </body>
</html>
