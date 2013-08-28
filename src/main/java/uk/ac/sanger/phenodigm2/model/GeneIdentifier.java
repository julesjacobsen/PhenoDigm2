/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.model;

/**
 * 
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
public class GeneIdentifier {
    
    private String geneSymbol;
    private String databaseCode;
    private String databaseAcc;

    
    public GeneIdentifier() {
    }
    
    public GeneIdentifier(String geneSymbol, String databaseCode, String databaseAcc) {
        this.geneSymbol = geneSymbol;
        this.databaseCode = databaseCode;
        this.databaseAcc = databaseAcc;
    }

    public GeneIdentifier(String geneSymbol, String dbAccesions) {
        
        if (geneSymbol.equals("")) {
            geneSymbol = "-";
        }
        this.geneSymbol = geneSymbol;   
        
        if (dbAccesions.equals("")) { 
            this.databaseAcc = "";
            this.databaseCode = "";
        } else {
            String[] database = dbAccesions.split(":");
            String dbCode = database[0];
            String dbAc = database[1];
            this.databaseCode = dbCode;
            this.databaseAcc = dbAc;
        }

    }

    public String getGeneSymbol() {
        return geneSymbol;
    }

    public void setGeneSymbol(String geneSymbol) {
        this.geneSymbol = geneSymbol;
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

    public void setDatabaseAcc(String databaseAc) {
        this.databaseAcc = databaseAc;
    }

    /**
     * Returns the 'full' identifier of a gene Id. In the case of an MGI gene Id
     * this will be 'MGI:95522'. 
     * OMIM ids will be in the same format, for example 'OMIM:101600'
     * @return String of the database identifier
     */
    public String getCompoundIdentifier() {
        return databaseCode + ":" + databaseAcc;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.geneSymbol != null ? this.geneSymbol.hashCode() : 0);
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
        final GeneIdentifier other = (GeneIdentifier) obj;
        if ((this.geneSymbol == null) ? (other.geneSymbol != null) : !this.geneSymbol.equals(other.geneSymbol)) {
            return false;
        }
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
        return geneSymbol + "{" + databaseCode + ":" + databaseAcc + '}';
    }
}
