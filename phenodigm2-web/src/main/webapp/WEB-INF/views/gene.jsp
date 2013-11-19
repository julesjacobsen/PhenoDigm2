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
            function getDiseaseAssociations(targetRowId) {
//                alert(targetRowId);
                var targetRow = $('#' + targetRowId);
                var geneId = $(targetRow).attr("geneid");
                var diseaseId = $(targetRow).attr("diseaseid");
                var requestPageType = $(targetRow).attr("requestpagetype");
                console.log(requestPageType + " page getDiseaseAssociations for: " + geneId + " " + diseaseId);

                var uri = "/phenodigm2/diseaseGeneAssociations";
                $.get(uri, {
                    geneId: geneId,
                    diseaseId: diseaseId,
                    requestPageType: requestPageType
                }, function(response) {
                    console.log(response);
                    //${disease.diseaseId}_${mouseGeneIdentifier.compoundIdentifier}
                    var id = "#" + geneId + "_" + diseaseId;
                    console.log("Searching for id:" + id);
                    $(targetRow).html(response);

                });
            }
            ;
        </script>

        <script type="text/javascript">
            $(document).ready(function() {
                $("#phenotypes tr:odd").addClass("odd");
                $("#phenotypes tr:not(.odd)").hide();
                $("#phenotypes tr:first-child").show();

                $("#phenotypes tr.odd").click(function() {
                    $(this).next("tr").toggle();
                    $(this).find(".arrow").toggleClass("up");
                    getDiseaseAssociations($(this).attr("targetRowId"));
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
                    getDiseaseAssociations($(this).attr("targetRowId"));
                });
            });
        </script>

    </head>
    <body>
        <p class="ikmcbreadcrumb">
            <a href=".">Home</a>&nbsp;&raquo;<a href="../gene"> Genes</a>&nbsp;&raquo; ${geneIdentifier.geneSymbol}
        </p>
        <div class='topic'>Disease Associations for <a href="https://www.mousephenotype.org/data/genes/${geneIdentifier.databaseCode}:${geneIdentifier.databaseAcc}">${geneIdentifier.geneSymbol}</a> (human ortholog <a href="${humanOrtholog.externalUri}">${humanOrtholog.geneSymbol}</a>)</div>
        <div class="container span12">
            <div class="row-fluid dataset">
                <h4 class="topic">Curated Disease Associations <a href='http://www.sanger.ac.uk/resources/databases/phenodigm/'></a></h4>
                    The following diseases are associated with ${geneIdentifier.geneSymbol} from external resources*
                <table id="phenotypes" class="table table-striped">
                    <thead>
                        <tr>
                            <th>Disease Name</th>
                            <th>Source</th>
                            <th>Associated in Human</th>
                            <th>Mouse Literature Evidence (MGI)</th>
                            <th>In Locus</th>
                            <th>MGI Mouse Phenotype Evidence (Phenodigm)</th>
                            <th>MGP Mouse Phenotype Evidence (Phenodigm)</th>
                            <th></th>
                        </tr>
                    </thead>                        
                    <tbody> 
                        <c:forEach var="association" items="${curatedAssociations}" varStatus="loop">
                            <c:set var="disease" value="${association.disease}"></c:set>
                            <c:set var="associationSummary" value="${association.associationSummary}"></c:set>
                            <tr id="${disease.diseaseIdentifier.databaseAcc}" targetRowId="${geneIdentifier.databaseAcc}_${disease.diseaseIdentifier.databaseAcc}">
                                <!--Disease Name-->
                                <td><a href="../disease/${disease.diseaseId}">${disease.term}</a></td>
                                <!--Source-->
                                <td>
                                    <a id="diseaseId" href="${disease.diseaseIdentifier.externalUri}">${disease.diseaseId}</a></td>
                                <!--Associated in Human --> 
                                <td>
                                    <c:if test="${associationSummary.associatedInHuman}">Yes</c:if>
                                    </td>  
                                    <td>
                                        <!--Mouse Literature Evidence (MGI) - Yes or empty-->
                                    <c:if test="${associationSummary.hasLiteratureEvidence}">Yes</c:if>
                                    </td>                                    
                                    <td>
                                        <c:if test="${associationSummary.inLocus}">
                                            <b style="color:#FF9000">Yes</b>
                                        </c:if>
                                    </td>
                                <!--Mouse Phenotype Evidence (Phenodigm)-->
                                <td>
                                    <b style="color:#FF9000">${associationSummary.bestMgiScore}</b>   
                                </td>
                                <td>
                                    <b style="color:#FF9000">${associationSummary.bestImpcScore}</b>   
                                </td>
                                <td>
                                    <div class="arrow">+</div>
                                </td>   
                            </tr>
                            <tr id="${geneIdentifier.databaseAcc}_${disease.diseaseIdentifier.databaseAcc}" requestpagetype= "gene" geneid="${geneIdentifier.compoundIdentifier}" diseaseid="${disease.diseaseIdentifier.compoundIdentifier}">
                            </tr>
                        </c:forEach>                            
                    </tbody>
                </table>
            </div>
        </div>


        <div class="container span12">
            <div class="row-fluid dataset">
                <h4 class="topic">Phenotypic Disease Associations <a href='http://www.sanger.ac.uk/resources/databases/phenodigm/'></a></h4>
                    The following diseases are associated with ${geneIdentifier.geneSymbol} by phenotypic similarity
                <table id="predictedPhenotypes" class="table table-striped">
                    <thead>
                        <tr>
                            <th>Disease Name</th>
                            <th>Source</th>
                            <th>Associated in Human</th>
                            <th>Mouse Literature Evidence (MGI)</th>
                            <th>In Locus</th>
                            <th>MGI Mouse Phenotype Evidence (Phenodigm)</th>
                            <th>MGP Mouse Phenotype Evidence (Phenodigm)</th>
                            <th></th>
                        </tr>
                    </thead>                        
                    <tbody>
                        <c:forEach var="association" items="${phenotypeAssociations}" varStatus="loop">
                            <c:set var="disease" value="${association.disease}"></c:set>
                            <c:set var="associationSummary" value="${association.associationSummary}"></c:set>
                            <tr id="${disease.diseaseIdentifier.databaseAcc}" targetRowId="P${geneIdentifier.databaseAcc}_${disease.diseaseIdentifier.databaseAcc}">
                                <!--Disease Name-->
                                <td><a href="../disease/${disease.diseaseId}">${disease.term}</a></td>
                                <!--Source-->
                                <td><a href="${disease.diseaseIdentifier.externalUri}">${disease.diseaseId}</a></td>
                                <!--Associated in Human --> 
                                <td>
                                    <c:if test="${associationSummary.associatedInHuman}">Yes</c:if>
                                    </td>  
                                    <td>
                                        <!--Mouse Literature Evidence (MGI) - Yes or empty-->
                                    <c:if test="${associationSummary.hasLiteratureEvidence}">Yes</c:if>
                                    </td>                                    
                                    <td>
                                    <c:if test="${associationSummary.inLocus}">
                                        <b style="color:#FF9000">Yes</b>
                                    </c:if>
                                </td>
                                <!--Mouse Phenotype Evidence (Phenodigm)-->
                                <td>
                                    <b style="color:#FF9000">${associationSummary.bestMgiScore}</b>   
                                </td>
                                <td>
                                    <b style="color:#FF9000">${associationSummary.bestImpcScore}</b>   
                                </td>
                                <td>
                                    <div class="arrow">+</div>
                                </td>   
                            </tr>
                            <tr id="P${geneIdentifier.databaseAcc}_${disease.diseaseIdentifier.databaseAcc}" requestpagetype= "gene" geneid="${geneIdentifier.compoundIdentifier}" diseaseid="${disease.diseaseIdentifier.compoundIdentifier}">
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </body>
</html>
