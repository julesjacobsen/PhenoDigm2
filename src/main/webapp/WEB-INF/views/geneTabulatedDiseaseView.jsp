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
        <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
        <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
        <link rel="stylesheet" href="/resources/demos/style.css" />
        <style>

            /* IE has layout issues when sorting (see #5413) */

            .group { zoom: 1 }

        </style>

        <script>

            $(function() {

                $( "#accordion" ).accordion({

        header: "> div > h2"

      })

      .sortable({

        axis: "y",

        handle: "h2",

        stop: function( event, ui ) {

          // IE doesn't register the blur when sorting

          // so trigger focusout handlers to remove .ui-state-focus

          ui.item.children( "h2" ).triggerHandler( "focusout" );

        }

      });

});

        </script>



    </head>
    <body>
        <h1>${geneIdentifier.geneSymbol} Disease Associations</h1>
        <div class="row-fluid dataset">
            <div class="row-fluid">
                <div class="container span12">
                    <h4 class="caption">Orthologous Gene Disease Associations <a href='http://www.sanger.ac.uk/resources/databases/phenodigm/'></a></h4>
                    <table class="table table-striped">
                        <caption>The following diseases are associated with ${geneIdentifier.geneSymbol} by gene orthology to ${humanOrtholog.geneSymbol}</caption>
                        <thead>
                            <tr>
                                <th>Disease Name</th>
                                <th>Source</th>
                                <th>Assoc. Human Genes</th>
                                <th>Assoc. Mouse Genes</th>
                                <th>Literature Evidence</th>
                                <th>Phenotype Evidence</th>
                            </tr>
                        </thead>                        
                        <tbody>    
                            <c:forEach var="diseaseAssociationsMap" items="${knownDiseaseAssociations}" varStatus="loop">
                                <c:set var="disease" value="${diseaseAssociationsMap.key}"></c:set>
                                <c:set var="literatureDiseaseAssociation" value="${diseaseAssociationsMap.value}"></c:set>
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

                                </tr>

                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

                        
                    <div id="accordion">
                        <c:forEach var="diseaseAssociationsMap" items="${predictedDiseaseAssociations}" varStatus="loop">
                            <c:set var="disease" value="${diseaseAssociationsMap.key}"></c:set>
                            <c:set var="literatureDiseaseAssociation" value="${knownDiseaseAssociations[disease]}"></c:set>
                            <c:set var="predictedDiseaseAssociation" value="${diseaseAssociationsMap.value}"></c:set>
                                <div class="group">
                                    <h2>
                                        <tr>
                                            <td>${disease.term}</td>
                                            <td><a href="http://omim.org/entry/${disease.fullOmimId}">${disease.omimId}</a></td>
                                            <td><c:forEach var="omimGene" items="${disease.associatedHumanGenes}" varStatus="loop"><a href="http://www.ensembl.org/Homo_sapiens/Search/Details?db=core;end=1;idx=Gene;q=${omimGene.geneSymbol};species=Homo_sapiens">${omimGene.geneSymbol}</a><c:if test="${!loop.last}">, </c:if></c:forEach></td>
                                            <td><c:forEach var="mgiGene" items="${disease.associatedMouseGenes}" varStatus="loop"><a href="http://www.informatics.jax.org/marker/${mgiGene.databaseCode}:${mgiGene.databaseAcc}">${mgiGene.geneSymbol}</a><c:if test="${!loop.last}">, </c:if></c:forEach></td>                                    
                                            <td><c:if test="${not empty literatureDiseaseAssociation}">Yes</c:if></td>
                                            <td><c:if test="${not empty predictedDiseaseAssociation}">
                                                    <c:forEach var="diseaseAssociation" items="${predictedDiseaseAssociation}" begin="0" end="0" varStatus="loop">
                                                        ${diseaseAssociation.diseaseToModelScore}
                                                    </c:forEach>
                                                </c:if>
                                            </td>
                                        </tr>
                                    </h2>
                                    <div>
                                    <c:forEach var="diseaseAssociation" items="${predictedDiseaseAssociation}" varStatus="loop">
                                        ${diseaseAssociation.diseaseToModelScore}: ${diseaseAssociation.mouseModel.allelicCompositionLink} ${diseaseAssociation.mouseModel.geneticBackground} </br>
                                    </c:forEach>  
                                    </div>
                                </div>
                            </c:forEach>
                    </div>
                        
        <div class="row-fluid dataset"><div class="row-fluid">
                <div class="container span12">
                    <h4 class="caption">Phenotypic Disease Associations <a href='http://www.sanger.ac.uk/resources/databases/phenodigm/'></a></h4>
                    <table id="diseases" class="table table-striped">
                        <caption>The following diseases are associated with ${geneIdentifier.geneSymbol} by phenotypic similarity</caption>
                        <tbody>
                        <th>Disease Name</th>
                        <th>Source</th>
                        <th>Assoc. Human Genes</th>
                        <th>Assoc. Mouse Genes</th>
                        <th>Literature Evidence</th>
                        <th>Phenotype Evidence</th>
                        </tbody>                        
                        <c:forEach var="diseaseAssociationsMap" items="${predictedDiseaseAssociations}" varStatus="loop">
                            <c:set var="disease" value="${diseaseAssociationsMap.key}"></c:set>
                            <c:set var="literatureDiseaseAssociation" value="${knownDiseaseAssociations[disease]}"></c:set>
                            <c:set var="predictedDiseaseAssociation" value="${diseaseAssociationsMap.value}"></c:set>
                            <tr> 
                                
                                <td>${disease.term}</td>
                                <td><a href="http://omim.org/entry/${disease.fullOmimId}">${disease.omimId}</a></td>
                                <td><c:forEach var="omimGene" items="${disease.associatedHumanGenes}" varStatus="loop"><a href="http://www.ensembl.org/Homo_sapiens/Search/Details?db=core;end=1;idx=Gene;q=${omimGene.geneSymbol};species=Homo_sapiens">${omimGene.geneSymbol}</a><c:if test="${!loop.last}">, </c:if></c:forEach></td>
                                <td><c:forEach var="mgiGene" items="${disease.associatedMouseGenes}" varStatus="loop"><a href="http://www.informatics.jax.org/marker/${mgiGene.databaseCode}:${mgiGene.databaseAcc}">${mgiGene.geneSymbol}</a><c:if test="${!loop.last}">, </c:if></c:forEach></td>                                    
                                <td><c:if test="${not empty literatureDiseaseAssociation}">Yes</c:if></td>
                                <td><c:if test="${not empty predictedDiseaseAssociation}">
                                        <c:forEach var="diseaseAssociation" items="${predictedDiseaseAssociation}" begin="0" end="0" varStatus="loop">
                                            ${diseaseAssociation.diseaseToModelScore}
                                        </c:forEach>
                                    </c:if>
                                </td>
                                
                            </tr>
                                <div>
                            <tr>
                                <td>
                                
                                    
                                <c:forEach var="diseaseAssociation" items="${predictedDiseaseAssociation}" varStatus="loop">
                                    <div>
                                        ${diseaseAssociation.diseaseToModelScore}: ${diseaseAssociation.mouseModel.allelicCompositionLink} ${diseaseAssociation.mouseModel.geneticBackground} </br>
                                    </div>
                                </c:forEach>                                         
                                </div>
                                </td> 
                            </tr>
                            </div>
                            </div>
                            
                        </c:forEach>
                        </div>
                    </table>
                    <!--                    <script>
                                            $(document).ready(function() {
                    
                                                // use jquery DataTable for table searching/sorting/pagination
                                                var aDataTblCols = [0, 1, 2, 3, 4, 5];
                                                var oDataTable = $.fn.initDataTable($('table#diseases'), {
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
                                            });
                                        </script>-->
                </div>
            </div>
        </div>
    </body>
</html>
