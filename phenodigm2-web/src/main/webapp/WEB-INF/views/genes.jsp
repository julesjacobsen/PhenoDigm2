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
                <table id="genes" class="table table-striped">
                        <thead>
                            <tr>
                                <th>Mouse Gene Symbol</th>
                                <th>Human Gene Symbol</th>
                                <th>Curated Diseases in Human</th>
                                <th>Curated Diseases in Mouse (MGI)</th>
                                <th>Candidate Diseases by Phenotype (MGI)</th>
                                <th>Candidate Diseases by Phenotype (IMPC)</th>
                                <th>MGI Mouse</th>
                                <th>IMPC Mouse</th>
                                <th>IMPC Phenotype Data</th>
                            </tr>
                        </thead>                        
                        <tbody>
                            <c:forEach var="gene" items="${genes}">
                                <c:set var="mouseGeneId" value="${gene.orthologGeneId}"></c:set>
                                <c:set var="humanGeneId" value="${gene.humanGeneId}"></c:set>
                                <c:set var="curationStatus" value="${gene.curationStatus}"></c:set>
                                <c:set var="projectStatus" value="${gene.projectStatus}"></c:set>
                                <tr> 
                                    <td>
                                        <a href="gene/${mouseGeneId.databaseCode}:${mouseGeneId.databaseAcc}">${mouseGeneId.geneSymbol}</a>
                                    </td>
                                    <td>                                
                                        <a href="http://www.genenames.org/data/hgnc_data.php?hgnc_id=${humanGeneId.databaseAcc}">${humanGeneId.geneSymbol}</a>
                                    </td>
                                    <td>
                                        <c:if test="${curationStatus.isAssociatedInHuman}">Yes</c:if>
                                    </td>
                                    <td>
                                        <c:if test="${curationStatus.hasMgiLiteratureEvidence}">Yes</c:if>
                                    </td>
                                    <td>
                                        <c:if test="${curationStatus.hasMgiPhenotypeEvidence}">Yes</c:if>
                                    </td>
                                    <td>
                                        <c:if test="${curationStatus.hasImpcPhenotypeEvidence}">Yes</c:if>
                                    </td>
                                    <td>
                                        <c:if test="${projectStatus.hasMgiMouse}">Yes</c:if>
                                    </td>
                                    <td>
                                        <c:if test="${projectStatus.hasImpcMouse}">Yes</c:if>                                        
                                    </td>
                                    <td>
                                        <c:if test="${projectStatus.hasImpcPhenotypeData}">Yes</c:if>                                                                                
                                    </td>
                               
                                </tr>
                            </c:forEach>
                        </tbody>    
                
            </div></div>
    </body>
</html>
