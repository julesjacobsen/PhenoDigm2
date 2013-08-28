<%-- 
    Document   : test
    Created on : 28-Aug-2013, 10:32:17
    Author     : jj8
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${mgiId}</title>

        <link rel="stylesheet" type="text/css" href="${baseUrl}/css/ui.dropdownchecklist.themeroller.css"/>
        <link rel="stylesheet" type="text/css" href="${baseUrl}/css/custom.css"/>
    </head>
    <body>
        <h1>${geneIdentifier.geneSymbol} Disease Associations</h1>
        <div class="row-fluid dataset">
            <div class="row-fluid">
                <div class="container span12">
                    <h4 class="caption">Known Disease Associations <a href='http://www.sanger.ac.uk/resources/databases/phenodigm/'><small>Explore Genes via Disease-Phenotype Associations</small></a></h4>

                    <c:forEach var="diseaseAssociationsMap" items="${knownDiseaseAssociations}" varStatus="loop">

                        <h5 class="caption">${diseaseAssociationsMap.key.term} <small>(disease associated by gene orthology to ${humanOrtholog.geneSymbol})</small></h5>
                        <div id="disOrthologsAccordion" class="accordion-inner">
                            <table>
                                <tbody>
                                <th>OMIM ID</th>
                                <th>OMIM Term</th>
                                <th>OMIM Genes</th>
                                <th>MGI Genes</th>
                                <th>Alternative Terms</th>
                                <tr>
                                    <td><a href="http://omim.org/entry/${diseaseAssociationsMap.key.omimId}">${diseaseAssociationsMap.key.omimId}</a></td>
                                    <td>${knownDisease.key.term}</td>                                        
                                    <td><c:forEach var="omimGene" items="${diseaseAssociationsMap.key.associatedHumanGenes}" varStatus="loop"><a href="http://www.ensembl.org/Homo_sapiens/Search/Details?db=core;end=1;idx=Gene;q=${omimGene.geneSymbol};species=Homo_sapiens">${omimGene.geneSymbol}</a><c:if test="${!loop.last}">, </c:if></c:forEach></td>
                                    <td><c:forEach var="mgiGene" items="${diseaseAssociationsMap.key.associatedMouseGenes}" varStatus="loop"><a href="http://www.informatics.jax.org/marker/${mgiGene.databaseCode}:${mgiGene.databaseAcc}">${mgiGene.geneSymbol}</a><c:if test="${!loop.last}">, </c:if></c:forEach></td>                                    
                                    <td><c:forEach var="diseaseAlt" items ="${diseaseAssociationsMap.key.alternativeTerms}" varStatus="loop">${diseaseAlt}<c:if test="${!loop.last}">, </c:if></c:forEach></td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                                <h6 class="caption">Mouse Disease Models with Phenotypic Similarity to ${diseaseAssociationsMap.key.term} - via Literature <a href="http://www.informatics.jax.org/disease/models/${diseaseAssociationsMap.key.omimId}"><small>Source: MGI</small></a></h6>
                        <div class="accordion-inner" id="litDisPhenotypesAccordion">
                            <c:forEach var="diseaseAssociation" items="${diseaseAssociationsMap.value}" varStatus="loop">
                                ${diseaseAssociation.diseaseToModelScore} | ${diseaseAssociation.mouseModel.mgiModelId} | ${diseaseAssociation.mouseModel.allelicCompositionLink}
                                <c:if test="${!loop.last}"><br/></c:if>
                            </c:forEach>
                        </div>


                        <h6 class="caption">Phenotypic Matches to Mouse Models of ${knownDisease.key.term} <a href="http://www.sanger.ac.uk/resources/databases/phenodigm/"><small>Source: PhenoDigm</small></a></h6>
                        <!--                        These are a subset of the Predicted Disease Associations panel below, but only containing the models which have been linked to this disease OmimId
                                                show table of data using model data from MGI and link out to them from here.            -->
                        <div class="accordion-inner" id="litDisPhenotypesAccordion">
                            <c:forEach var="diseaseAssociation" items="${knownDisease.value}" varStatus="loop">
                                ${diseaseAssociation.diseaseToModelScore} | ${diseaseAssociation.mouseModel.mgiModelId} | ${diseaseAssociation.mouseModel.allelicCompositionLink}<br/>
                                Fgfr1tm1Led/Fgfr1tm1Led
                                Model | HP | MP | IC | SimJ

                                <c:if test="${!loop.last}"><br/></c:if></c:forEach>
                                </div>
                    </c:forEach>                
                </div>

            </div>  

        </div>

        <div class="row-fluid dataset">
            <div class="row-fluid">
                <div class="container span12">
                    <h4 class="caption">Predicted Disease Associations for ${gene.symbol} <a href='http://www.sanger.ac.uk/resources/databases/phenodigm/'><small>Explore Genes via Disease-Phenotype Associations here</small></a></h4>


                    (Eventually this will do something mind-blowing with graphics and stuff.)

                    <div class="accordion-inner" id="compDisPhenotypesAccordion">
                        <c:forEach var="diseaseAssociationsMap" items="${predictedDiseaseAssociations}" varStatus="loop">
                            ${diseaseAssociationsMap.key.omimId} | ${diseaseAssociationsMap.key.term}<br>
                            <c:forEach var="diseaseAssociation" items="${diseaseAssociationsMap.value}" varStatus="loop">
                                ${diseaseAssociation.diseaseToModelScore} | ${diseaseAssociation.mouseModel.mgiModelId} | ${diseaseAssociation.mouseModel.allelicCompositionLink}<br/>
                            </c:forEach>

                            <!--                                <pre>
                                                            OMIM ID | disease term | Match score 
                                                                           Model | HP | MP | IC | SimJ
                                                                           Fgfr1tm1Led/Fgfr1tm1Led | HP | MP | IC | SimJ
                                                                           Fgfr2tm2.3Dsn/Fgfr2+ | HP | MP | IC | SimJ
                                                            </pre>-->
                            <c:if test="${!loop.last}"><br></c:if></c:forEach>
                    </div>

                </div>
            </div>
        </div>
    </body>
</html>
