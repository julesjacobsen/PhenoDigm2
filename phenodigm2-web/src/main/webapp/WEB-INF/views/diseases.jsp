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
        <title>Diseases</title>
        <link rel="stylesheet" type="text/css" href="https://www.mousephenotype.org/data/css/bootstrap.css"/>
        <link rel="stylesheet" type="text/css" href="https://www.mousephenotype.org/data/css/custom.css"/>
        <link rel="stylesheet" href="/resources/demos/style.css" />

    </head>
    <body>
        <p class="ikmcbreadcrumb">
            <a href="../">Home</a>&nbsp;&raquo; Diseases
        </p>
        
        <div class='topic'>Ohh nasty diseases...</div>
        <div class="row-fluid dataset">
            <div class="container span12">
                <table id="diseases" class="table table-striped">
                        <thead>
                            <tr>
                                <th>Disease Name</th>
                                <th>Source</th>
                                <th>Curated Gene Association in Human</th>
                                <th>MGI Literature Evidence</th>
                                <th>Mouse Phenotype Evidence (MGI)</th>
                                <th>Mouse Phenotype Evidence (IMPC)</th>
                            </tr>
                        </thead>                        
                        <tbody>
                            <c:forEach var="disease" items="${allDiseases}">
                                <c:set var="status" value="${disease.curationStatus}"></c:set>
                                <tr>
                                    
                                    <td><a href="disease/${disease.diseaseId}">${disease.term}</a></td>
                                    <td>${disease.diseaseId}</td>
                                    <td colspan="4">
                                        ${status}
                                        <%--<c:if test="${status.isAssociatedInHuman}">Yes</c:if>--%>
                                    </td>
<!--                                    <td>
                                        <%--<c:if test="${status.hasMgiLiteratureEvidence}">Yes</c:if>--%>
                                    </td>
                                    <td>
                                        <%--<c:if test="${status.hasMgiPhenotypeEvidence}">Yes</c:if>--%>
                                    </td>
                                    <td>
                                        <%--<c:if test="${status.hasImpcPhenotypeEvidence}">Yes</c:if>--%>
                                    </td>-->
                                </tr>
                            </c:forEach>
                        </tbody>    
                
            </div>
        </div>
    </body>
</html>
