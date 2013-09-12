<%-- 
    Document   : disease
    Created on : 27-Aug-2013, 17:09:07
    Author     : jj8
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Disease</title>
        <link rel="stylesheet" type="text/css" href="https://www.mousephenotype.org/data/css/bootstrap.css"/>
        <link rel="stylesheet" type="text/css" href="https://www.mousephenotype.org/data/css/custom.css"/>
        <link rel="stylesheet" href="/resources/demos/style.css" />

    </head>
    <body>
        <div class='topic'>Ohh nasty diseases...</div>
        <div class="row-fluid dataset">
            <div class="container span12">
                <table id="diseases" class="table table-striped">
                        <thead>
                            <tr>
                                <th>Disease Name</th>
                                <th>Source</th>
                                <th>Assoc. Human Genes</th>
                                <th>Assoc. Mouse Orthologs</th>
                                <th>MGI Literature Evidence</th>
                                <th>Mouse Phenotype Evidence</th>
                                <th></th>
                            </tr>
                        </thead>                        
                        <tbody>
                            <c:forEach var="disease" items="${allDiseases}">
                                <tr>
                                <td><a href="disease/${disease.omimId}">${disease.term}</a></td>
                                <td>${disease.omimId}</td>
                                <td>
                                    <c:forEach var="geneIdentifier" items="${disease.associatedHumanGenes}">
                                        ${geneIdentifier.geneSymbol}</br>
                                    </c:forEach>
                                </td>
                                <td>
                                    <c:forEach var="geneIdentifier" items="${disease.associatedMouseGenes}">
                                        <a href="gene/${geneIdentifier.databaseCode}:${geneIdentifier.databaseAcc}">${geneIdentifier.geneSymbol}</a></br>
                                    </c:forEach>
                                </td>
                                <td></td>
                                <td></td>
                                <td></td>
                                </tr>
                            </c:forEach>
                        </tbody>    
                
            </div>
        </div>
    </body>
</html>
