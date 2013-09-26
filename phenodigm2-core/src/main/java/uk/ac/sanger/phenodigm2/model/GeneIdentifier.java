/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.model;

/**
 * 
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
public class GeneIdentifier extends ExternalIdentifier implements Comparable<GeneIdentifier>{
    
    private String geneSymbol;
    
    public GeneIdentifier(String geneSymbol, String databaseCode, String databaseAcc) {
        super(databaseCode, databaseAcc);
        this.geneSymbol = geneSymbol;
    }

    public GeneIdentifier(String geneSymbol, String compoundIdentifier) {
        super(compoundIdentifier);
        this.geneSymbol = geneSymbol;   

    }

    public String getGeneSymbol() {
        return geneSymbol;
    }

    public void setGeneSymbol(String geneSymbol) {
        this.geneSymbol = geneSymbol;
    }

    /**
     *
     * @param other
     * @return
     */

    public int compareTo(GeneIdentifier other) {
        return this.geneSymbol.compareTo(other.geneSymbol);
    }

    @Override
    public String toString() {
        return geneSymbol + "{" + super.getCompoundIdentifier() + '}';
    }
}
