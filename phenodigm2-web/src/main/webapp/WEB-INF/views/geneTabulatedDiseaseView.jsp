<%-- 
    Document   : geneTabulatedDiseaseView
    Created on : 29-Aug-2013, 14:33:23
    Author     : jj8
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${mgiId} - ${geneIdentifier.geneSymbol}</title>

        <link rel="stylesheet" type="text/css" href="https://www.mousephenotype.org/data/css/bootstrap.css"/>
        <link rel="stylesheet" type="text/css" href="https://www.mousephenotype.org/data/css/custom.css"/>
        <script src="https://www.mousephenotype.org/js/general/toggle.js"></script>
        <script type="text/javascript" charset="utf-8" src="https://www.mousephenotype.org/media/js/jquery.js"></script>
        <script type="text/javascript" charset="utf-8" src="https://www.mousephenotype.org/media/js/jquery.dataTables.js"></script>
        <script type="text/javascript" charset="utf-8" src="http://code.jquery.com/jquery-1.10.1.min.js"></script>
        <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
        <!--        <script src="http://code.jquery.com/jquery-1.9.1.js"></script>-->
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js" type="text/javascript"></script>
        <link rel="stylesheet" href="/resources/demos/style.css" />

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
        <script>
            $(document).ready(function() {

// use jquery DataTable for table searching/sorting/pagination
                var aDataTblCols = [0, 1, 2, 3, 4, 5];
                var oDataTable = $.fn.initDataTable($('table#phenotypes'), {
                    //"aaSorting": [[0, "asc"], [1, "asc"]],   			     
                    "aoColumns": [
                        {"sType": "string"},
                        {"sType": "html", "mRender": function(data, type, full) {
                                return (type === "filter") ? $(data).text() : data;
                            }},
                        {"sType": "html", "mRender": function(data, type, full) {
                                return (type === "filter") ? $(data).text() : data;
                            }},
                        {"sType": "html", "mRender": function(data, type, full) {
                                return (type === "filter") ? $(data).text() : data;
                            }},
                        {"sType": "string"},
                        {"sType": "string"}

                    ],
                    "iDisplayLength": 10   // 10 rows as default 
                });

                $.fn.dataTableshowAllShowLess(oDataTable, aDataTblCols, null);

            });
        </script>
    
            <tr><td colspan="2"><div id="info" style="color: green;"></div></td></tr>

    </head>
    <body>
        <p class="ikmcbreadcrumb">
            <a href=".">Home</a>&nbsp;&raquo;<a href="../gene"> Genes</a>&nbsp;&raquo; ${geneIdentifier.geneSymbol}
        </p>

        <div class='topic'>Disease Associations for <a href="https://www.mousephenotype.org/data/genes/${geneIdentifier.databaseCode}:${geneIdentifier.databaseAcc}">${geneIdentifier.geneSymbol}</a> (human ortholog <a href="${humanOrtholog.externalUri}">${humanOrtholog.geneSymbol}</a>)</div>
        <div class="container span12">
            <div class="row-fluid dataset">
                    <h4 class="topic">Curated Disease Associations <a href='http://www.sanger.ac.uk/resources/databases/phenodigm/'></a></h4>
                    <table id="phenotypes" class="table table-striped">
                        The following diseases are associated with ${geneIdentifier.geneSymbol} from external resources*
                        <thead>
                            <tr>
                                <th>Disease Name</th>
                                <th>Source</th>
                                <th>Associated in Human</th>
                                <th>Mouse Literature Evidence (MGI)</th>
                                <th>Mouse Phenotype Evidence (Phenodigm)</th>
                                <th></th>
                            </tr>
                        </thead>                        
                        <tbody> 
                            <c:forEach var="association" items="${curatedAssociations}" varStatus="loop">
                                <c:set var="disease" value="${association.disease}"></c:set>
                                <tr>
                                    <!--Disease Name-->
                                    <td><a href="../disease/${disease.diseaseId}">${disease.term}</a></td>
                                    <!--Source-->
                                    <td><a href="http://omim.org/entry/${disease.diseaseIdentifier.databaseAcc}">${disease.diseaseId}</a></td>
                                    <!--Associated in Human --> 
                                    <td>
                                        <c:if test="${association.associatedInHuman}">Yes</c:if>
                                    </td>                                    
                                    <!--Mouse Literature Evidence (MGI)-->
                                    <td><c:if test="${association.hasLiteratureEvidence}">Yes</c:if></td>
                                    <!--Mouse Phenotype Evidence (Phenodigm)-->
                                    <td>
                                        <b style="color:#FF9000">${association.bestScore}</b>   
                                    </td>
                                    <td><div class="arrow">+</div></td>
                                </tr>
                                <tr>
                                    <td>
                                        <h5>${disease.diseaseId} Disease Phenotype Terms</h5>
                                        <c:forEach var="hpTerm" items="${disease.phenotypeTerms}">
                                            ${hpTerm}<br>
                                        </c:forEach>
                                    </td>
                                    <td></td>
                                    <td colspan="5">
                                        <c:if test="${not empty association.curatedAssociations}">
                                            <h5>${disease.diseaseId} Associated Mouse Models (MGI curated)</h5>
                                            <c:forEach var="diseaseAssociation" items="${association.curatedAssociations}" varStatus="loop">
                                                <c:set var="mouseModel" value="${diseaseAssociation.mouseModel}"></c:set>
                                                <b style="color:#FF9000">${diseaseAssociation.modelToDiseaseScore}</b>: ${mouseModel.allelicCompositionLink} ${mouseModel.geneticBackground} (Source: ${mouseModel.source})</br>
                                                <c:forEach var="phenotypeTerm" items="${mouseModel.phenotypeTerms}">
                                                    ${phenotypeTerm}<br/>
                                                </c:forEach>
                                                    <br/>
                                            </c:forEach>
                                        </c:if>
                                        <c:if test="${not empty association.phenotypicAssociations}">
                                            <h5>${disease.diseaseId} Associated Mouse Models (PhenoDigm predicted)</h5>
                                            <c:forEach var="diseaseAssociation" items="${association.phenotypicAssociations}" varStatus="loop">
                                                <c:set var="mouseModel" value="${diseaseAssociation.mouseModel}"></c:set>
                                                <b style="color:#FF9000">${diseaseAssociation.modelToDiseaseScore}</b>: ${mouseModel.allelicCompositionLink} ${mouseModel.geneticBackground} (Source: ${mouseModel.source})</br>
                                                <c:forEach var="phenotypeTerm" items="${mouseModel.phenotypeTerms}">
                                                    ${phenotypeTerm}<br/>
                                                </c:forEach>
                                                    <br/>                                        
                                            </c:forEach>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>                            
                        </tbody>
                    </table>
                </div>
            </div>
       

        <div class="container span12">
            <div class="row-fluid dataset">
                    <h4 class="topic">Phenotypic Disease Associations <a href='http://www.sanger.ac.uk/resources/databases/phenodigm/'></a></h4>
                    <table id="predictedPhenotypes" class="table table-striped">
                        The following diseases are associated with ${geneIdentifier.geneSymbol} by phenotypic similarity
                        <thead>
                            <tr>
                                <th>Disease Name</th>
                                <th>Source</th>
                                <th>Associated in Human</th>
                                <th>Mouse Literature Evidence (MGI)</th>
                                <th>Mouse Phenotype Evidence (Phenodigm)</th>
                                <th></th>
                            </tr>
                        </thead>                        
                        <tbody>
                            <c:forEach var="association" items="${phenotypeAssociations}" varStatus="loop">
                                <c:set var="disease" value="${association.disease}"></c:set>
                                <tr>
                                    <!--Disease Name-->
                                    <td><a href="../disease/${disease.diseaseId}">${disease.term}</a></td>
                                    <!--Source-->
                                    <td><a href="http://omim.org/entry/${disease.diseaseIdentifier.databaseAcc}">${disease.diseaseId}</a></td>
                                    <!--Associated in Human --> 
                                    <td>
                                        <c:if test="${association.associatedInHuman}">Yes</c:if>
                                    </td>                                    
                                    <!--Mouse Literature Evidence (MGI)-->
                                    <td><c:if test="${association.hasLiteratureEvidence}">Yes</c:if></td>
                                    <!--Mouse Phenotype Evidence (Phenodigm)-->
                                    <td>
                                        <b style="color:#FF9000">${association.bestScore}</b>   
                                    </td>
                                    <td><div class="arrow">+</div></td>
                                </tr>
                                <tr>
                                    <td>
                                        <h5>${disease.diseaseId} Disease Phenotype Terms</h5>
                                        <c:forEach var="hpTerm" items="${disease.phenotypeTerms}">
                                            ${hpTerm}<br>
                                        </c:forEach>
                                    </td>
                                    <td></td>
                                    <td colspan="5">
                                        <c:if test="${not empty association.curatedAssociations}">
                                            <h5>${disease.diseaseId} Associated Mouse Models (MGI curated)</h5>
                                            <c:forEach var="diseaseAssociation" items="${association.curatedAssociations}" varStatus="loop">
                                                <c:set var="mouseModel" value="${diseaseAssociation.mouseModel}"></c:set>
                                                <b style="color:#FF9000">${diseaseAssociation.modelToDiseaseScore}</b>: ${mouseModel.allelicCompositionLink} ${mouseModel.geneticBackground} (Source: ${mouseModel.source})</br>
                                                <c:forEach var="phenotypeTerm" items="${mouseModel.phenotypeTerms}">
                                                    ${phenotypeTerm}<br/>
                                                </c:forEach>
                                                    <br/>
                                            </c:forEach>
                                        </c:if>
                                        <c:if test="${not empty association.phenotypicAssociations}">
                                            <h5>${disease.diseaseId} Associated Mouse Models (PhenoDigm predicted)</h5>
                                            <c:forEach var="diseaseAssociation" items="${association.phenotypicAssociations}" varStatus="loop">
                                                <c:set var="mouseModel" value="${diseaseAssociation.mouseModel}"></c:set>
                                                <b style="color:#FF9000">${diseaseAssociation.modelToDiseaseScore}</b>: ${mouseModel.allelicCompositionLink} ${mouseModel.geneticBackground} (Source: ${mouseModel.source})</br>
                                                <c:forEach var="phenotypeTerm" items="${mouseModel.phenotypeTerms}">
                                                    ${phenotypeTerm}<br/>
                                                </c:forEach>
                                                    <br/>                                        
                                            </c:forEach>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
    </body>
</html>
