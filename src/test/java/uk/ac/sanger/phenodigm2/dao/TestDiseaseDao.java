/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.sanger.phenodigm2.model.Disease;
import uk.ac.sanger.phenodigm2.model.DiseaseAssociation;
import uk.ac.sanger.phenodigm2.model.GeneIdentifier;
import uk.ac.sanger.phenodigm2.model.MouseModel;
import uk.ac.sanger.phenodigm2.model.PhenotypeMatch;
import uk.ac.sanger.phenodigm2.model.PhenotypeTerm;

/**
 * Provides Stub/test data from resource files for use with testing without a 
 * database. This is a read-only cache so it's a singleton.
 * 
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
public class TestDiseaseDao implements DiseaseDao {
    
    private static TestDiseaseDao instance;
    private static final Logger logger = Logger.getLogger(TestDiseaseDao.class.getName());
    
    private Map<String, String> mgiToOmimOrthologMap;
    private Map<String, Disease> omimDiseaseIdToDiseaseMap;
    //add caches for diseases, mouse models and orthologous genes
    
    private Map<String, Set<Disease>> omimGeneIdToDiseasesMap;
    private Map<String, Set<DiseaseAssociation>> diseaseAssociationMap;
    private Map<String, Set<DiseaseAssociation>> predictedDiseaseAssociationsMap;
    
    public static TestDiseaseDao getInstance() {
        if (instance == null) {
            logger.info("instance is null - making new TestDiseaseDAO");
            instance = new TestDiseaseDao();
        }
        return instance;
    }
    
    private TestDiseaseDao() {   
        populateDiseases();
        populatePhenotypeMatches();
    }
    
    @Override
    public Set<Disease> getDiseasesByOmimGeneId(String omimGeneId){
        Set<Disease> diseases = omimGeneIdToDiseasesMap.get(omimGeneId);
        if (diseases == null){ 
            logger.warning(omimGeneId + " not mapped to any diseases" );
            return new TreeSet<Disease>();
        }
        return diseases;
    }
    
    @Override
    public Disease getDiseaseByOmimDiseaseId(String omimDiseaseId) {
        for (Map.Entry<String, Disease> entry : omimDiseaseIdToDiseaseMap.entrySet()) {
            String string = entry.getKey();
            Disease diseaseId = entry.getValue();
            System.out.println(string + " : " + diseaseId);     
        }
        return omimDiseaseIdToDiseaseMap.get(omimDiseaseId);
    }

    @Override
    public Set<Disease> getDiseasesByMgiGeneId(String geneSymbol) {
        String humanOrtholog = mgiToOmimOrthologMap.get(geneSymbol);
        logger.info(String.format("%s maps to human ortholog %s", geneSymbol, humanOrtholog));
        return omimGeneIdToDiseasesMap.get(humanOrtholog);
    }

    @Override
    public Map<Disease, Set<DiseaseAssociation>> getKnownDiseaseAssociationsForMgiGeneId(String geneSymbol) {
        logger.info("getting known Disease Associations for " + geneSymbol);
               
        Map<Disease, Set<DiseaseAssociation>> knownDiseaseAssociations = new TreeMap<Disease, Set<DiseaseAssociation>>();
        for (Disease disease : getDiseasesByMgiGeneId(geneSymbol)){
            Set<DiseaseAssociation> diseaseAssociations = diseaseAssociationMap.get(disease.getOmimId());
            if (diseaseAssociations != null) {
                knownDiseaseAssociations.put(disease, diseaseAssociations);                
            } else {
                logger.warning(String.format("No known Disease Associations for disease %s - %s with gene %s", disease.getOmimId(), disease.getTerm(), geneSymbol));
                knownDiseaseAssociations.put(disease, new TreeSet<DiseaseAssociation>()); 
            }
        }
        return knownDiseaseAssociations;
    }

    @Override
    public Map<Disease, Set<DiseaseAssociation>> getPredictedDiseaseAssociationsForMgiGeneId(String mgiGeneId) {
        Map<Disease, Set<DiseaseAssociation>> diseaseAssociations = new TreeMap<Disease, Set<DiseaseAssociation>>();
        
        //the disease data should come from a cache
        for (String omimDiseaseId : predictedDiseaseAssociationsMap.keySet()) {
            Disease disease = omimDiseaseIdToDiseaseMap.get(omimDiseaseId);
            if (disease == null) {
                disease = new Disease();
                disease.setOmimId(omimDiseaseId);
                disease.setTerm("STUB PREDICTED DISEASE FOR " + mgiGeneId);
            }
            diseaseAssociations.put(disease, predictedDiseaseAssociationsMap.get(omimDiseaseId));
        }
        
        return diseaseAssociations;
    }

    
    public Map<Disease, Set<DiseaseAssociation>> getDiseaseAssociationsByOmimGeneId(String omimGeneId){
        Map<Disease, Set<DiseaseAssociation>> diseaseAssociations = new TreeMap<Disease, Set<DiseaseAssociation>>();
        
        for (Disease disease : omimGeneIdToDiseasesMap.get(omimGeneId)) {
            diseaseAssociations.put(disease, diseaseAssociationMap.get(omimGeneId));
        }
        
        return diseaseAssociations;
    }

    private void populateDiseases() {
        
        mgiToOmimOrthologMap = new HashMap<String, String>();
        omimDiseaseIdToDiseaseMap = new HashMap<String, Disease>();
        omimGeneIdToDiseasesMap = new HashMap<String, Set<Disease>>();
        
        //String diseaseStubData = "src/test/resources/data/fgfr2KnownDiseaseAssociationTestData.dsv";
        String diseaseStubData = "src/test/resources/data/diseaseGeneCache.dsv";
        BufferedReader diseaseData = getReaderFromFile(diseaseStubData);
        
        String line = null;
        int headerLine = 0;
        int currentLine = 0;
        try {
            while ((line = diseaseData.readLine()) != null) {
                //first line is a header 
                if (currentLine != headerLine) {
                    //there could be multiple genes associated with a single disease
                    //so there could be several lines for a single disease
                    makeDiseaseAndGeneOrthologs(line);
                }
                currentLine++; 
            }
        } catch (IOException ex) {
            Logger.getLogger(TestDiseaseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        logger.info(String.format("Made %d genes: %s mapping to %d diseases: %s ", omimGeneIdToDiseasesMap.keySet().size(), omimGeneIdToDiseasesMap.keySet(), omimDiseaseIdToDiseaseMap.keySet().size(), omimDiseaseIdToDiseaseMap.keySet()));
    }

    /**
     * Handles logic for linking diseases with genes and orthologs
     * @param line 
     */
    private void makeDiseaseAndGeneOrthologs(String line){
        String[] fields = line.split("\\t");
        String omimDiseaseId = fields[5];
        Disease disease = omimDiseaseIdToDiseaseMap.get(omimDiseaseId);
        
        if (disease == null) {
            String type = fields[6];
            String fullOmimId = fields[7];
            String term = fields[8];
            String altTerms = "";
            if (fields.length == 10) {
                altTerms = fields[9];
            } 
            disease = makeDisease(omimDiseaseId, type, fullOmimId, term, altTerms);
            omimDiseaseIdToDiseaseMap.put(disease.getOmimId(), disease);
        }
        
        if (!fields[0].isEmpty()) {
            GeneIdentifier humanGene = new GeneIdentifier(fields[2], fields[3]);
            GeneIdentifier mouseGene = new GeneIdentifier(fields[1], fields[0]);
            mgiToOmimOrthologMap.put(mouseGene.getCompoundIdentifier(), humanGene.getCompoundIdentifier());
            disease.getAssociatedHumanGenes().add(humanGene);
            disease.getAssociatedMouseGenes().add(mouseGene);
        }
        
        addDiseaseToOmimGeneIdToDiseaseMap(disease);    
    }
    
    private Disease makeDisease(String omimId, String type, String fullOmimId, String term, String altTerms){
        Disease disease = new Disease();
        
        disease.setOmimId(omimId);
        disease.setType(type);
        disease.setFullOmimId(fullOmimId);
        disease.setTerm(term);
        
        //add alternative disease terms - if there are any
        if (!altTerms.isEmpty()) {
            disease.setAlternativeTerms(makeAlternativeTerms(altTerms));
        }
        //add human gene identifiers
        disease.setAssociatedHumanGenes(new ArrayList<GeneIdentifier>());    
        //add mouse gene identifiers
        disease.setAssociatedMouseGenes(new ArrayList<GeneIdentifier>());
        
        return disease;
    }
    
    private List<String> makeAlternativeTerms(String otherTerms) {
        List<String> alternativeTerms = new ArrayList<String>();
            String[] altTerms = otherTerms.split("\\|");
            for (int i = 0; i < altTerms.length; i++) {
                alternativeTerms.add(altTerms[i]);
            }
            return alternativeTerms;
    }
    
    private void addDiseaseToOmimGeneIdToDiseaseMap(Disease disease) {
        
        for (GeneIdentifier humanGeneIdentifier : disease.getAssociatedHumanGenes()) {
        
            String omimGeneId = humanGeneIdentifier.getCompoundIdentifier();
            if (!omimGeneIdToDiseasesMap.containsKey(omimGeneId)) {
                Set<Disease> diseases = new TreeSet<Disease>();
                diseases.add(disease);
                omimGeneIdToDiseasesMap.put(omimGeneId, diseases);
            }
            else {
                omimGeneIdToDiseasesMap.get(omimGeneId).add(disease);
            }    
        }
    }
    
    private void populatePhenotypeMatches() {
        
        //this will contain all the known disease associations
        diseaseAssociationMap = new HashMap<String, Set<DiseaseAssociation>>();

        String knownDiseaseAssociationStubData = "src/test/resources/data/fgfr2MgiLiteratureMouseModels.dsv";
        makeDiseaseAssociations(diseaseAssociationMap, knownDiseaseAssociationStubData);
         
        //and this one the predicted associations
        predictedDiseaseAssociationsMap = new HashMap<String, Set<DiseaseAssociation>>();
        String predictedDiseaseAssociationStubData = "src/test/resources/data/fgfr2PredictedMouseModelToDiseaseAsociations.dsv"; 
        makeDiseaseAssociations(predictedDiseaseAssociationsMap, predictedDiseaseAssociationStubData);
        
        logger.info(String.format("Mapped %d diseases to mouse models: %s ", diseaseAssociationMap.keySet().size(), diseaseAssociationMap.values()));
    
    }
    
    private DiseaseAssociation makeDiseaseAssociation(String line) {
        DiseaseAssociation diseaseAssociation = new DiseaseAssociation();
        
        String[] fields = line.split("\\t");
        
        MouseModel mouseModel = makeMouseModel(fields[3], fields[4], fields[5], fields[6], fields[7], fields[8]);
        diseaseAssociation.setMouseModel(mouseModel);
        
        diseaseAssociation.setOmimDiseaseId(fields[0]);
        diseaseAssociation.setModelToDiseaseScore(Double.parseDouble(fields[1]));
        diseaseAssociation.setDiseaseToModelScore(Double.parseDouble(fields[2]));
        diseaseAssociation.setPubMedId("TODO");
        diseaseAssociation.setPhenotypeMatches(new ArrayList<PhenotypeMatch>());
        
        return diseaseAssociation;
    }

    private MouseModel makeMouseModel(String mgiGeneId, String mgiModelId, String allelicComp, String geneticBackgrnd, String link, String source) {
        MouseModel mouseModel = new MouseModel();
        
        mouseModel.setMgiGeneId(mgiGeneId);
        mouseModel.setMgiModelId(mgiModelId);
        mouseModel.setAllelicComposition(allelicComp);
        mouseModel.setGeneticBackground(geneticBackgrnd);
        mouseModel.setAllelicCompositionLink(link);
        mouseModel.setPhenotypeTerms(new ArrayList<PhenotypeTerm>());

        return mouseModel;
    }

    private void addDiseaseAssociationToMap(Map<String, Set<DiseaseAssociation>> diseaseAssociationsMap, DiseaseAssociation diseaseAssociation) {
        String omimDiseaseId = diseaseAssociation.getOmimDiseaseId();
        if (!diseaseAssociationsMap.containsKey(omimDiseaseId)) {
            Set<DiseaseAssociation> diseaseAssociations = new TreeSet<DiseaseAssociation>();
            diseaseAssociations.add(diseaseAssociation);
            diseaseAssociationsMap.put(omimDiseaseId, diseaseAssociations);
        } else {
            diseaseAssociationsMap.get(omimDiseaseId).add(diseaseAssociation);
        }
    }
    
    private BufferedReader getReaderFromFile(String fileName){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TestDiseaseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return reader;
    }    

    private void makeDiseaseAssociations(Map<String, Set<DiseaseAssociation>> diseaseAssociationsMap, String diseaseAssociationStubData) {
        
        BufferedReader diseaseAssociations = getReaderFromFile(diseaseAssociationStubData);
        
        String line = null;
        int headerLine = 0;
        int currentLine = 0;
        try {
            while ((line = diseaseAssociations.readLine()) != null) {
                //first line is a header 
                if (currentLine != headerLine) {
//                    System.out.println(line);
                    DiseaseAssociation diseaseAssociation = makeDiseaseAssociation(line);
                    addDiseaseAssociationToMap(diseaseAssociationsMap, diseaseAssociation);
                }
                currentLine++; 
            }
        } catch (IOException ex) {
            Logger.getLogger(TestDiseaseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //TODO!!
    @Override
    public GeneIdentifier getGeneIdentifierForMgiGeneId(String acc) {
        return null;
    }
    
    //TODO!!
    @Override
    public GeneIdentifier getHumanOrthologIdentifierForMgiGeneId(String acc) {
        return null;
    }


        
}
