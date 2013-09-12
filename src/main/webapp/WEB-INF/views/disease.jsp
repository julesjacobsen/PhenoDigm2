<%-- 
    Document   : disease
    Created on : 10-Sep-2013, 12:13:01
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

        <title>${disease.omimId}</title>
    </head>
    <body>
        <div class='topic'>${disease.term}</div><a href="../disease"><tiny>up</tiny></a> <a href="../home"><tiny> home</tiny></a>
        <div class="row-fluid dataset">
            
            <div class="container span6">
            <div class="row-fluid dataset">
                <h5>${disease.omimId} Disease Phenotype Terms</h5>
                <c:forEach var="hpTerm" items="${disease.phenotypeTerms}">
                    ${hpTerm}<br>
                </c:forEach>
            </div>
            </div>
                
            <div class="container span6">
                <div class="row-fluid dataset">
                    <div class='topic'>Orthologous Gene Associations</div>
                    <c:forEach var="geneAssociationMap" items="${orthologAssociations}">
                        <c:set var="geneIdentifier" value="${geneAssociationMap.key}"></c:set>
                        <c:set var="diseaseAssociations" value="${orthologAssociations[geneIdentifier]}"></c:set>
                        <a href="https://www.mousephenotype.org/data/genes/${geneIdentifier.databaseCode}:${geneIdentifier.databaseAcc}"><b>${geneIdentifier.geneSymbol}</b></a> (${geneIdentifier.databaseCode}:${geneIdentifier.databaseAcc}) <a href="../gene/${geneIdentifier.databaseCode}:${geneIdentifier.databaseAcc}">Explore phenotype-associated rare diseases</a></br>
                        <c:forEach var="diseaseAssociation" items="${diseaseAssociations}">
                            <c:set var="mouseModel" value="${diseaseAssociation.mouseModel}"></c:set>
                            <b style="color:#FF9000">${diseaseAssociation.diseaseToModelScore}</b>: ${mouseModel.allelicCompositionLink} ${mouseModel.geneticBackground} (Source: MGI curation)</br>                                            
                            <c:forEach var="phenotypeTerm" items="${mouseModel.phenotypeTerms}">
                                ${phenotypeTerm}<br/>
                            </c:forEach>                               
                        </c:forEach>
                    </c:forEach>
                </div>
            </div>

            <div class="container span6">
                <div class="row-fluid dataset">
                    <div class='topic'>Phenotypic Gene Associations</div>
                    <c:forEach var="geneAssociationMap" items="${predictedAssociations}">
                        <c:set var="geneIdentifier" value="${geneAssociationMap.key}"></c:set>
                        <c:set var="diseaseAssociations" value="${predictedAssociations[geneIdentifier]}"></c:set>
                        <a href="https://www.mousephenotype.org/data/genes/${geneIdentifier.databaseCode}:${geneIdentifier.databaseAcc}"><b>${geneIdentifier.geneSymbol}</b></a> (${geneIdentifier.databaseCode}:${geneIdentifier.databaseAcc}) <a href="../gene/${geneIdentifier.databaseCode}:${geneIdentifier.databaseAcc}">Explore phenotype-associated rare diseases</a></br>
                        <c:forEach var="diseaseAssociation" items="${diseaseAssociations}">
                            <c:set var="mouseModel" value="${diseaseAssociation.mouseModel}"></c:set>
                            <b style="color:#FF9000">${diseaseAssociation.diseaseToModelScore}</b>: ${mouseModel.allelicCompositionLink} ${mouseModel.geneticBackground} (Source: PhenoDigm)</br>                                            
                            <c:forEach var="phenotypeTerm" items="${mouseModel.phenotypeTerms}">
                                ${phenotypeTerm}<br/>
                            </c:forEach>
                        </c:forEach>
                    </c:forEach>
                </div>
            </div>

        </div>




    </body>
</html>
