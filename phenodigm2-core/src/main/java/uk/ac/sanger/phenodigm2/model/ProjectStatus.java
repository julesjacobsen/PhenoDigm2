
package uk.ac.sanger.phenodigm2.model;

/**
 * Bean for storing the status of a gene with regards to whether the gene has 
 * data from a particular project. 
 * 
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
public class ProjectStatus {
    private boolean hasMgiMouse = false;
    private boolean hasImpcMouse = false;
    private boolean hasImpcPhenotypeData = false;

    public ProjectStatus() {
    }

    public boolean hasMgiMouse() {
        return hasMgiMouse;
    }

    public void setHasMgiMouse(boolean hasMgiMouse) {
        this.hasMgiMouse = hasMgiMouse;
    }

    public boolean hasImpcMouse() {
        return hasImpcMouse;
    }

    public void setHasImpcMouse(boolean hasImpcMouse) {
        this.hasImpcMouse = hasImpcMouse;
    }

    public boolean hasImpcPhenotypeData() {
        return hasImpcPhenotypeData;
    }

    public void setHasImpcPhenotypeData(boolean hasImpcPhenotypeData) {
        this.hasImpcPhenotypeData = hasImpcPhenotypeData;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (this.hasMgiMouse ? 1 : 0);
        hash = 23 * hash + (this.hasImpcMouse ? 1 : 0);
        hash = 23 * hash + (this.hasImpcPhenotypeData ? 1 : 0);
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
        final ProjectStatus other = (ProjectStatus) obj;
        if (this.hasMgiMouse != other.hasMgiMouse) {
            return false;
        }
        if (this.hasImpcMouse != other.hasImpcMouse) {
            return false;
        }
        if (this.hasImpcPhenotypeData != other.hasImpcPhenotypeData) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ProjectStatus{" + "hasMgiMouse=" + hasMgiMouse + ", hasImpcMouse=" + hasImpcMouse + ", hasImpcPhenotypeData=" + hasImpcPhenotypeData + '}';
    }

}
