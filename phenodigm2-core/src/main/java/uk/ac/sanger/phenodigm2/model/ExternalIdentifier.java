/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.model;

import org.apache.log4j.Logger;

/**
 * Class describing the attributes of an external reference. In PhenoDigm
 * this is anouther resource, e.g. MGI, HGNC, OMIM, ORPHANET, DECIPHER.
 * 
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
public class ExternalIdentifier {
    
    private static final Logger logger = Logger.getLogger(ExternalIdentifier.class);
    
    private String databaseCode;
    private String databaseAcc;
    private String externalUri;

    /**
     * Creates a new ExternalIdentifier from a compound identifier.
     * Compound identifiers must follow the format:
     * databaseCode:databaseAcc
     * 
     * Examples: OMIM:101600, ORPHANET:1040, MGI:88452
     * @param compoundIdentifier 
     */
    protected ExternalIdentifier(String compoundIdentifier) {
        
        if (!compoundIdentifier.contains(":")) {
            logger.warn(compoundIdentifier + " is not of the format GENE:ACCESSION - creating an empty external identifier");
            this.databaseAcc = "";
            this.databaseCode = "";
        } else {
            String[] database = compoundIdentifier.split(":");
            String dbCode = database[0];
            String dbAc = database[1];
            this.databaseCode = dbCode;
            this.databaseAcc = dbAc;
        }
    }
    
    /**
     * Creates a new ExternalIdentifier from a databaseCode and a databaseAcc.
     * 
     * Examples: OMIM:101600, ORPHANET:1040, MGI:88452
     * @param databaseCode
     * @param databaseAcc 
     */
    protected ExternalIdentifier(String databaseCode, String databaseAcc) {
        this.databaseCode = databaseCode;
        this.databaseAcc = databaseAcc;
    }
    
    public String getDatabaseCode() {
        return databaseCode;
    }

    public void setDatabaseCode(String databaseCode) {
        this.databaseCode = databaseCode;
    }

    public String getDatabaseAcc() {
        return databaseAcc;
    }

    public void setDatabaseAcc(String databaseAcc) {
        this.databaseAcc = databaseAcc;
    }

    public String getExternalUri() {
        return externalUri;
    }

    public void setExternalUri(String externalUri) {
        this.externalUri = externalUri;
    }

    /**
     * Returns the 'full' identifier of a gene Id. In the case of an MGI gene Id
     * this will be 'MGI:95522'. 
     * OMIM ids will be in the same format, for example 'OMIM:101600'
     * @return String of the database identifier
     */
    public String getCompoundIdentifier() {
        return String.format("%s:%s", databaseCode, databaseAcc);
    }
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + (this.databaseCode != null ? this.databaseCode.hashCode() : 0);
        hash = 79 * hash + (this.databaseAcc != null ? this.databaseAcc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ExternalIdentifier other = (ExternalIdentifier) obj;
        if ((this.databaseCode == null) ? (other.databaseCode != null) : !this.databaseCode.equals(other.databaseCode)) {
            return false;
        }
        if ((this.databaseAcc == null) ? (other.databaseAcc != null) : !this.databaseAcc.equals(other.databaseAcc)) {
            return false;
        }
        return true;
    }
    

    @Override
    public String toString() {
        return getCompoundIdentifier();
    }
        
}
