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
        <title>${mgiId}</title>

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
        <h1>${geneIdentifier.geneSymbol} Disease Associations</h1>
        <div class="row-fluid dataset">
            <div class="row-fluid">
                <div class="container span12">
                    <h4 class="caption">Curated Gene Disease Associations <a href='http://www.sanger.ac.uk/resources/databases/phenodigm/'></a></h4>
                    <table id="phenotypes" class="table table-striped">
                        <caption>The following diseases are associated with ${geneIdentifier.geneSymbol} from external resources*</caption>
                        <thead>
                            <tr>
                                <th>Disease Name</th>
                                <th>Source</th>
                                <th>Assoc. Human Genes</th>
                                <th>Mouse Orthologs</th>
                                <th>Mouse Literature Evidence</th>
                                <th>Mouse Phenotype Evidence</th>
                                <th></th>
                            </tr>
                        </thead>                        
                        <tbody>    
                            <c:forEach var="diseaseAssociationsMap" items="${knownDiseaseAssociations}" varStatus="loop">
                                <c:set var="disease" value="${diseaseAssociationsMap.key}"></c:set>
                                <c:set var="literatureDiseaseAssociation" value="${knownDiseaseAssociations[disease]}"></c:set>
                                <c:set var="predictedDiseaseAssociation" value="${predictedDiseaseAssociations[disease]}"></c:set>

                                    <tr>
                                        <td>${disease.term}</td>
                                    <td><a href="http://omim.org/entry/${disease.omimId}">${disease.omimId}</a></td>
                                    <td><c:forEach var="omimGene" items="${disease.associatedHumanGenes}" varStatus="loop"><a href="http://www.ensembl.org/Homo_sapiens/Search/Details?db=core;end=1;idx=Gene;q=${omimGene.geneSymbol};species=Homo_sapiens">${omimGene.geneSymbol}</a><c:if test="${!loop.last}">, </c:if></c:forEach></td>
                                    <td><c:forEach var="mgiGene" items="${disease.associatedMouseGenes}" varStatus="loop"><a href="http://www.informatics.jax.org/marker/${mgiGene.databaseCode}:${mgiGene.databaseAcc}">${mgiGene.geneSymbol}</a><c:if test="${!loop.last}">, </c:if></c:forEach></td>                                    
                                    <td><c:if test="${not empty literatureDiseaseAssociation}">Yes</c:if></td>
                                    <td><c:if test="${not empty predictedDiseaseAssociation}">
                                            <c:forEach var="diseaseAssociation" items="${predictedDiseaseAssociation}" begin="0" end="0" varStatus="loop">
                                                ${diseaseAssociation.diseaseToModelScore}
                                            </c:forEach>
                                        </c:if>
                                    </td>                        
                                    <td><div class="arrow">+</div></td>
                                </tr>
                                <tr>
                                    <td>
                                        <h5>${disease.omimId} Disease Phenotype Terms</h5>
                                        <c:forEach var="hpTerm" items="${disease.phenotypeTerms}">
                                            ${hpTerm}<br>
                                        </c:forEach>
                                    </td>
                                    <td></td>
                                    <td colspan="5">
                                        <h5>${disease.omimId} Associated Mouse Models (MGI curated)</h5>
                                        <c:forEach var="diseaseAssociation" items="${literatureDiseaseAssociation}" varStatus="loop">
                                            <c:set var="mouseModel" value="${diseaseAssociation.mouseModel}"></c:set>
                                            ${diseaseAssociation.diseaseToModelScore}: ${mouseModel.allelicCompositionLink} ${mouseModel.geneticBackground} (Source: MGI curation)</br>
                                            <c:forEach var="phenotypeTerm" items="${mouseModel.phenotypeTerms}">
                                                ${phenotypeTerm}<br/>
                                            </c:forEach>
                                                <br/>
                                        </c:forEach>
                                        <h5>${disease.omimId} Associated Mouse Models (PhenoDigm predicted)</h5>
                                        <c:forEach var="diseaseAssociation" items="${predictedDiseaseAssociation}" varStatus="loop">
                                            <c:set var="mouseModel" value="${diseaseAssociation.mouseModel}"></c:set>
                                            ${diseaseAssociation.diseaseToModelScore}: ${mouseModel.allelicCompositionLink} ${mouseModel.geneticBackground} (Source: PhenoDigm)</br>
                                            <c:forEach var="phenotypeTerm" items="${mouseModel.phenotypeTerms}">
                                                ${phenotypeTerm}<br/>
                                            </c:forEach>
                                                <br/>                                        
                                        </c:forEach>  
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
       

        <div class="row-fluid dataset">
            <div class="row-fluid">
                <div class="container span12">
                    <h4 class="caption">Phenotypic Disease Associations <a href='http://www.sanger.ac.uk/resources/databases/phenodigm/'></a></h4>
                    <table id="predictedPhenotypes" class="table table-striped">
                        <caption>The following diseases are associated with ${geneIdentifier.geneSymbol} by phenotypic similarity</caption>
                        <thead>
                            <tr>
                                <th>Disease Name</th>
                                <th>Source</th>
                                <th>Assoc. Human Genes</th>
                                <th>Mouse Orthologs</th>
                                <th>Mouse Literature Evidence</th>
                                <th>Mouse Phenotype Evidence</th>
                                <th></th>
                            </tr>
                        </thead>                        
                        <tbody>    
                            <c:forEach var="diseaseAssociationsMap" items="${predictedDiseaseAssociations}" varStatus="loop">
                                <c:set var="disease" value="${diseaseAssociationsMap.key}"></c:set>
                                <!--careful here... in this section the literatureDiseaseAssociation" value="${knownDiseaseAssociations[disease]}" as we're referring to the other data set-->
                                <c:set var="literatureDiseaseAssociation" value="${knownDiseaseAssociations[disease]}"></c:set>
                                <c:set var="predictedDiseaseAssociation" value="${predictedDiseaseAssociations[disease]}"></c:set>

                                    <tr>
                                        <td>${disease.term}</td>
                                    <td><a href="http://omim.org/entry/${disease.omimId}">${disease.omimId}</a></td>
                                    <td><c:forEach var="omimGene" items="${disease.associatedHumanGenes}" varStatus="loop"><a href="http://www.ensembl.org/Homo_sapiens/Search/Details?db=core;end=1;idx=Gene;q=${omimGene.geneSymbol};species=Homo_sapiens">${omimGene.geneSymbol}</a><c:if test="${!loop.last}">, </c:if></c:forEach></td>
                                    <td><c:forEach var="mgiGene" items="${disease.associatedMouseGenes}" varStatus="loop"><a href="http://www.informatics.jax.org/marker/${mgiGene.databaseCode}:${mgiGene.databaseAcc}">${mgiGene.geneSymbol}</a><c:if test="${!loop.last}">, </c:if></c:forEach></td>                                    
                                    <td><c:if test="${not empty literatureDiseaseAssociation}">Yes</c:if></td>
                                    <td><c:if test="${not empty predictedDiseaseAssociation}">
                                            <c:forEach var="diseaseAssociation" items="${predictedDiseaseAssociation}" begin="0" end="0" varStatus="loop">
                                                ${diseaseAssociation.diseaseToModelScore}
                                            </c:forEach>
                                        </c:if>
                                    </td>                        
                                    <td><div class="arrow">+</div></td>
                                </tr>
                                <tr>
                                    <td>
                                        <h5>${disease.omimId} Disease Phenotype Terms</h5>
                                        <c:forEach var="hpTerm" items="${disease.phenotypeTerms}">
                                            ${hpTerm}<br>
                                        </c:forEach>
                                    </td>
                                    <td></td>
                                    <td colspan="5">
                                        <h5>${disease.omimId} Associated Mouse Models (MGI curated)</h5>
                                        <c:forEach var="diseaseAssociation" items="${literatureDiseaseAssociation}" varStatus="loop">
                                            <c:set var="mouseModel" value="${diseaseAssociation.mouseModel}"></c:set>
                                            ${diseaseAssociation.diseaseToModelScore}: ${mouseModel.allelicCompositionLink} ${mouseModel.geneticBackground} (Source: MGI curation)</br>
                                            <c:forEach var="phenotypeTerm" items="${mouseModel.phenotypeTerms}">
                                                ${phenotypeTerm}<br/>
                                            </c:forEach>
                                                <br/>
                                        </c:forEach>
                                        <h5>${disease.omimId} Associated Mouse Models (PhenoDigm predicted)</h5>
                                        <c:forEach var="diseaseAssociation" items="${predictedDiseaseAssociation}" varStatus="loop">
                                            <c:set var="mouseModel" value="${diseaseAssociation.mouseModel}"></c:set>
                                            ${diseaseAssociation.diseaseToModelScore}: ${mouseModel.allelicCompositionLink} ${mouseModel.geneticBackground} (Source: PhenoDigm)</br>
                                            <c:forEach var="phenotypeTerm" items="${mouseModel.phenotypeTerms}">
                                                ${phenotypeTerm}<br/>
                                            </c:forEach>
                                                <br/>                                        
                                        </c:forEach> 
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </body>
</html>
