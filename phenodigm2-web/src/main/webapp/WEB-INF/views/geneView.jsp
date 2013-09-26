<%-- 
    Document   : gene
    Created on : 27-Aug-2013, 15:23:58
    Author     : jj8
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${mgiId}</title>
    </head>
    <body>
        <h1>${geneIdentifier.geneSymbol}</h1>
        <h2>Known Disease Associations:</h2>
        <c:forEach var="knownDisease" items="${knownDiseaseAssociations}" varStatus="loop">
            <!--Map<Disease, Set<DiseaseAssociation>>-->
            ${knownDisease.key.term}:<br>
                       
            <c:forEach var="diseaseAssociation" items="${knownDisease.value}" varStatus="loop">
                ${diseaseAssociation.mouseModel}<br>
            </c:forEach>
                <br>
        </c:forEach>
        <br><br>
        <h2>Predicted Disease Associations:</h2>
        <c:forEach var="predictedDisease" items="${predictedDiseaseAssociations}" varStatus="loop">
            <!--Map<Disease, Set<DiseaseAssociation>>-->
            ${predictedDisease.key.term}:<br>
            
            <c:forEach var="diseaseAssociation" items="${predictedDisease.value}" varStatus="loop">
                ${diseaseAssociation.mouseModel}<br>
            </c:forEach>
            <br><br>
        </c:forEach>
        
    </body>
</html>
