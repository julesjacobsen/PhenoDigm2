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

        <title>${disease.diseaseId} - ${disease.term}</title>
            <script type="text/javascript" charset="utf-8" src="http://code.jquery.com/jquery-1.10.1.min.js"></script>

    <script type="text/javascript">
            $(document).ready(function() {
                $("#phenotypes tr:odd").addClass("odd");
                $("#phenotypes tr:not(.odd)").hide();
                $("#phenotypes tr:first-child").show();

                $("#phenotypes tr.odd").click(function() {
                    $(this).next("tr").toggle();
                    $(this).find(".arrow").toggleClass("up");
                });
                //$("#report").jExpand();
            });
        </script>
        <script type="text/javascript">
            $(document).ready(function() {
                $("#predictedPhenotypes tr:odd").addClass("odd");
                $("#predictedPhenotypes tr:not(.odd)").hide();
                $("#predictedPhenotypes tr:first-child").show();

                $("#predictedPhenotypes tr.odd").click(function() {
                    $(this).next("tr").toggle();
                    $(this).find(".arrow").toggleClass("up");
                });
                //$("#report").jExpand();
            });
        </script>
        
    </head>
    <body>
        <p class="ikmcbreadcrumb">
            <a href=".">Home</a>&nbsp;&raquo;<a href="../disease"> Diseases</a>&nbsp;&raquo; ${disease.diseaseId}
        </p>

        <div class='topic'>${disease.term}</div>

            <div class="container span12">
                <div class="row-fluid dataset">
                    <table id="omimData" class="table table-striped">
                        <thead>
                            <tr>
                                <th>Source</th>
                                <th>Assoc. Human Genes</th>
                                <th>Mouse Orthologs</th>
                            </tr>
                        </thead>                        
                        <tbody> 
                            <tr>
                                <td><a href="http://omim.org/entry/${disease.diseaseId}">${disease.diseaseId}</a></td>
                                <td><c:forEach var="omimGene" items="${disease.associatedHumanGenes}" varStatus="loop"><a href="http://www.ensembl.org/Homo_sapiens/Search/Details?db=core;end=1;idx=Gene;q=${omimGene.geneSymbol};species=Homo_sapiens">${omimGene.geneSymbol}</a><c:if test="${!loop.last}">, </c:if></c:forEach></td>
                                <td><c:forEach var="mgiGene" items="${disease.associatedMouseGenes}" varStatus="loop"><a href="https://www.mousephenotype.org/data/genes/${mgiGene.databaseCode}:${mgiGene.databaseAcc}">${mgiGene.geneSymbol}</a><c:if test="${!loop.last}">, </c:if></c:forEach></td>                                    
                            </tr>
                        </tbody> 
                        </table>
                </div>  
            </div>
            <div class="container span12">
                <div class="row-fluid dataset">
                    <h4 class="topic">Curated Gene - Disease Associations</h4>
                    <table id="phenotypes" class="table table-striped"> 
                        <thead>
                            <tr>
                                <th>Gene Symbol</th>
                                <th>Source</th>
                                <th>MGI</th>
                                <th>IMPC</th>
                                <th>Best Phenotype Score</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="geneAssociationMap" items="${orthologAssociations}">
                                <c:set var="geneIdentifier" value="${geneAssociationMap.key}"></c:set>
                                <c:set var="diseaseAssociations" value="${orthologAssociations[geneIdentifier]}"></c:set>
                                <tr>
                                    <td>
                                        <a href="https://www.mousephenotype.org/data/genes/${geneIdentifier.databaseCode}:${geneIdentifier.databaseAcc}"><b>${geneIdentifier.geneSymbol}</b></a> 
                                    </td>
                                    <td>
                                        (${geneIdentifier.databaseCode}:${geneIdentifier.databaseAcc}) 
                                    </td>
                                    <td>
                                        <a href="../gene/${geneIdentifier.databaseCode}:${geneIdentifier.databaseAcc}">Explore phenotype-associated rare diseases</a>
                                    </td>
                                    <td></td>
                                    <td></td>                                        
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <h5>${disease.diseaseId} Disease Phenotype Terms</h5>
                                        <c:forEach var="hpTerm" items="${disease.phenotypeTerms}">
                                            ${hpTerm}<br>
                                        </c:forEach>
                                    </td>
                                    <td></td>
                                    <td colspan="2">
                                        <h5>${disease.diseaseId} Associated Mouse Models (MGI curated)</h5>
                                        <c:forEach var="diseaseAssociation" items="${diseaseAssociations}">
                                            <c:set var="mouseModel" value="${diseaseAssociation.mouseModel}"></c:set>
                                            <b style="color:#FF9000">${diseaseAssociation.diseaseToModelScore}</b>: ${mouseModel.allelicCompositionLink} ${mouseModel.geneticBackground} (Source: MGI curation)</br>                                            
                                            <c:forEach var="phenotypeTerm" items="${mouseModel.phenotypeTerms}">
                                                ${phenotypeTerm}<br/>
                                            </c:forEach> 
                                                </br>
                                        </c:forEach>
                                    </td>                          
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table> 
                </div>
            </div>

            <div class="container span12">
                <div class="row-fluid dataset">
                    <h4 class="topic">Phenotypic Gene - Disease Associations <a href='http://www.sanger.ac.uk/resources/databases/phenodigm/'></a></h4>
                        <table id="predictedPhenotypes" class="table table-striped"> 
                        <thead>
                            <tr>
                                <th>Gene Symbol</th>
                                <th>Source</th>
                                <th>MGI</th>
                                <th>IMPC</th>
                                <th>Best Phenotype Score</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="geneAssociationMap" items="${predictedAssociations}">
                            <c:set var="geneIdentifier" value="${geneAssociationMap.key}"></c:set>
                            <c:set var="diseaseAssociations" value="${predictedAssociations[geneIdentifier]}"></c:set>
                            <tr>
                                    <td>
                                        <a href="https://www.mousephenotype.org/data/genes/${geneIdentifier.databaseCode}:${geneIdentifier.databaseAcc}"><b>${geneIdentifier.geneSymbol}</b></a> 
                                    </td>
                                    <td>
                                        <a href="../gene/${geneIdentifier.databaseCode}:${geneIdentifier.databaseAcc}">PhenoDigm</a> 
                                    </td>
                                    <td>
                                        ${geneIdentifier.databaseCode}:${geneIdentifier.databaseAcc}
                                    </td>
                                    <td></td>
                                    <td></td>                                        
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <h5>${disease.diseaseId} Disease Phenotype Terms</h5>
                                        <c:forEach var="hpTerm" items="${disease.phenotypeTerms}">
                                            ${hpTerm}<br>
                                        </c:forEach>
                                    </td>
                                    <td></td>
                                    <td colspan="2">
                                        <h5>${disease.diseaseId} Associated Mouse Models (PhenoDigm predicted)</h5>
                                        <c:forEach var="diseaseAssociation" items="${diseaseAssociations}">
                                            <c:set var="mouseModel" value="${diseaseAssociation.mouseModel}"></c:set>
                                            <b style="color:#FF9000">${diseaseAssociation.diseaseToModelScore}</b>: ${mouseModel.allelicCompositionLink} ${mouseModel.geneticBackground} (Source: MGI curation)</br>                                            
                                            <c:forEach var="phenotypeTerm" items="${mouseModel.phenotypeTerms}">
                                                ${phenotypeTerm}<br/>
                                            </c:forEach> 
                                                </br>
                                        </c:forEach>
                                    </td>                          
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>






    </body>
</html>
