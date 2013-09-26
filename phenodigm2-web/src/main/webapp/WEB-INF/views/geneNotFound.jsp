<%-- 
    Document   : geneNotFound
    Created on : 28-Aug-2013, 11:08:09
    Author     : jj8
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${mgiId} Not Found</title>
    </head>
    <body>
        <h1>${mgiId} Not Found</h1>
        <p>Sorry we cannot find the MGI gene ID ${mgiId}. Please Check it has the correct format e.g. <a href="gene/MGI:95523">MGI:95523</a> and that the gene id is currently valid.</p>
        <p>Perhaps you'd like to return <a href="home">home</a> or try searching <a href="gene">again</a>.</p>
    </body>
</html>
