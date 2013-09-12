<%-- 
    Document   : home
    Created on : 27-Aug-2013, 14:18:22
    Author     : jj8
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="https://www.mousephenotype.org/data/css/bootstrap.css"/>
        <link rel="stylesheet" type="text/css" href="https://www.mousephenotype.org/data/css/custom.css"/>

        <title>PhenoDigm2</title>
    </head>
    <body>

        <div class='topic'>Welcome to PhenoDigm (PHENOtype comparisons for DIsease and Gene Models)</div>

        <div class="row-fluid dataset">
            <div class="container span12">

                <p align="justify"> Model organisms represent a valuable resource
                    for the characterisation as well as identification of disease-gene
                    associations, especially where the molecular basis is unknown and
                    there is no clue to the candidate gene’s function, pathway involvement
                    or expression pattern. To systematically apply this methodology,
                    PhenoDigm uses a semantic approach to map between clinical features
                    observed in humans and mouse and zebrafish phenotype annotations. The
                    database allows browsing/searching of genetic diseases from the Online
                    Mendelian Inheritance in Man (<a href="http://omim.org/"
                                                     target="blank">OMIM</a>) database and display of the resulting animal
                    model matches ranked by their phenotypic similarity to the
                    disorder. To date, phenotyped mutants from the Mouse Genome
                    Informatics Database (<a href="http://informatics.jax.org/"
                                             target="blank">MGD</a>), the Sanger Mouse Genetics Project
                    (<a href="http://www.sanger.ac.uk/mouseportal" target="blank">MGP</a>)
                    and the Zebrafish Model Organism Database (<a href="http://zfin.org/"
                                                                  target="blank">ZFIN</a>) are incorporated. Future builds will
                    incorporate data from projects performing high throughput phenotyping
                    of every protein-coding gene: the International Mouse Phenotyping
                    Consortium (<a href="http://mousephenotype.org/"
                                   target="blank">IMPC</a>) and Zebrafish Mutation Project
                    (<a href="http://mousephenotype.org/" target="blank">ZMP</a>).
                </p>
                <ul>
                <li><a href="disease">Browse diseases and their prioritised models</a></li>
                <li><a href="gene">Search for diseases associated with a specific gene</a></li>
                <li><a href="home">Or just return back to this page...</a></li>
</ul>
            </div>
        </div>

        <div class="panel">
            <div class='topic'>Related information and publications</div>
            
            <div class="row-fluid dataset">
                <div class="container span12">
                    <ul>
                        <li><a href="http://www.sanger.ac.uk/htgt/biomart/martview" target="blank">BioMart of top 500 PhenoDigm matches for each disease</a></li>
                        <li><a href="http://mousemodels.org" target="blank">MouseFinder</a></li>
                        <li><a href="http://www.ncbi.nlm.nih.gov/pubmed/22331800" target="blank">MouseFinder's publication</a></li>
                        <li><a href="http://code.google.com/p/phenodigm/" target="blank">PhenoDigm on Google</a></li>
                        <li><a href="http://owlsim.org/" target="blank">OWLSim</a></li>
                    </ul>
                </div>
            </div>

            
    </div>                  
</body>
</html>
