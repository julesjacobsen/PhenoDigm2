<%-- 
    Document   : genes
    Created on : 28-Aug-2013, 10:15:02
    Author     : jj8
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="https://www.mousephenotype.org/data/css/bootstrap.css"/>
        <link rel="stylesheet" type="text/css" href="https://www.mousephenotype.org/data/css/custom.css"/>

        <title>Genes</title>
    </head>
    <body>
        <p class="ikmcbreadcrumb">
            <a href=".">Home</a>&nbsp;&raquo; Genes
        </p>
        <div class='topic'>Genes</div>
        
        <div class="row-fluid dataset">
            <div class="container span12">          
                <p>
                    This is a list of all genes with links to the geneView page from each gene.
                </p>
                <c:forEach var="geneIdentifier" items="${geneIdentifiers}">
                    <a href="https://www.mousephenotype.org/data/genes/${geneIdentifier.databaseCode}:${geneIdentifier.databaseAcc}">${geneIdentifier.geneSymbol}</a> (${geneIdentifier.databaseCode}:${geneIdentifier.databaseAcc}) <a href="gene/${geneIdentifier.databaseCode}:${geneIdentifier.databaseAcc}">Explore phenotype-associated rare diseases</a></br>
                </c:forEach>
            </div></div>
    </body>
</html>
