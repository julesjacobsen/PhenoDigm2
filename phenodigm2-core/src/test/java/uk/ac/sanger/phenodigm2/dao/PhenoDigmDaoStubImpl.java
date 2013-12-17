/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.dao;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
import uk.ac.sanger.phenodigm2.model.DiseaseModelAssociation;
import uk.ac.sanger.phenodigm2.model.DiseaseIdentifier;
import uk.ac.sanger.phenodigm2.model.Gene;
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
public class PhenoDigmDaoStubImpl implements PhenoDigmDao {
    
    private static PhenoDigmDaoStubImpl instance;
    private static final Logger logger = Logger.getLogger(PhenoDigmDaoStubImpl.class.getName());
    
    //caches these are constantly cross-referenced when building objects
    private static OrthologCache orthologCache;
    private static DiseaseCache diseaseCache;
    private static MouseModelCache mouseModelCache;
       
    private Map<String, Set<DiseaseModelAssociation>> diseaseAssociationMap;
    private Map<String, Set<DiseaseModelAssociation>> predictedDiseaseAssociationsMap;
    
    public static PhenoDigmDaoStubImpl getInstance() {
        if (instance == null) {
            logger.info("instance is null - making new TestDiseaseDAO");
            instance = new PhenoDigmDaoStubImpl();
        }
        return instance;
    }
    
    public PhenoDigmDaoStubImpl() {   
        populateDiseases();
        populatePhenotypeMatches();
    }
    
    @Override
    public Set<Disease> getDiseasesByHgncGeneId(String omimGeneId){
        Set<Disease> diseases = diseaseCache.getDiseasesByHgncGeneId(omimGeneId);
        if (diseases == null){ 
            logger.warning(omimGeneId + " not mapped to any diseases" );
            return new TreeSet<Disease>();
        }
        return diseases;
    }
    
    @Override
    public Disease getDiseaseByDiseaseId(String omimDiseaseId) {
   
        return diseaseCache.getDiseaseForDiseaseId(omimDiseaseId);
    }

    @Override
    public Set<Disease> getDiseasesByMgiGeneId(String geneSymbol) {
        GeneIdentifier humanOrtholog = orthologCache.getHumanOrthologOfMouseGene(geneSymbol);
        logger.info(String.format("%s maps to human ortholog %s", geneSymbol, humanOrtholog));
        return diseaseCache.getDiseasesByHgncGeneId(humanOrtholog.getCompoundIdentifier());
    }

    @Override
    public Map<Disease, Set<DiseaseModelAssociation>> getKnownDiseaseAssociationsForMgiGeneId(String geneSymbol) {
        logger.info("getting known Disease Associations for " + geneSymbol);
               
        Map<Disease, Set<DiseaseModelAssociation>> knownDiseaseAssociations = new TreeMap<Disease, Set<DiseaseModelAssociation>>();
        for (Disease disease : getDiseasesByMgiGeneId(geneSymbol)){
            Set<DiseaseModelAssociation> diseaseAssociations = diseaseAssociationMap.get(disease.getDiseaseId());
            if (diseaseAssociations != null) {
                knownDiseaseAssociations.put(disease, diseaseAssociations);                
            } else {
                logger.warning(String.format("No known Disease Associations for disease %s - %s with gene %s", disease.getDiseaseId(), disease.getTerm(), geneSymbol));
                knownDiseaseAssociations.put(disease, new TreeSet<DiseaseModelAssociation>()); 
            }
        }
        return knownDiseaseAssociations;
    }

    @Override
    public Map<Disease, Set<DiseaseModelAssociation>> getPredictedDiseaseAssociationsForMgiGeneId(String mgiGeneId) {
        Map<Disease, Set<DiseaseModelAssociation>> diseaseAssociations = new TreeMap<Disease, Set<DiseaseModelAssociation>>();
        
        //the disease data should come from a cache
        for (String omimDiseaseId : predictedDiseaseAssociationsMap.keySet()) {
            Disease disease = diseaseCache.getDiseaseForDiseaseId(omimDiseaseId);
            if (disease == null) {
                disease = new Disease(omimDiseaseId);
                disease.setTerm("STUB PREDICTED DISEASE FOR " + mgiGeneId);
            }
            diseaseAssociations.put(disease, predictedDiseaseAssociationsMap.get(omimDiseaseId));
        }
        
        return diseaseAssociations;
    }

    private void populateDiseases() {

        Map<GeneIdentifier, Gene> mgiToOmimOrthologMap = new HashMap<GeneIdentifier, Gene>();
        Map<String, Disease> omimDiseaseIdToDiseaseMap = new HashMap<String, Disease>();
        
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
                    makeDiseaseAndGeneOrthologs(line, mgiToOmimOrthologMap, omimDiseaseIdToDiseaseMap);
                }
                currentLine++; 
            }
        } catch (IOException ex) {
            Logger.getLogger(PhenoDigmDaoStubImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        orthologCache = new OrthologCache(mgiToOmimOrthologMap);
        diseaseCache = new DiseaseCache(omimDiseaseIdToDiseaseMap);
        
        logger.info(String.format("Mapped %d gene orthologs to %d diseases", mgiToOmimOrthologMap.keySet().size(), omimDiseaseIdToDiseaseMap.keySet().size()));
    }

    /**
     * Handles logic for linking diseases with genes and orthologs
     * @param line 
     */
    private void makeDiseaseAndGeneOrthologs(String line, Map<GeneIdentifier, Gene> orthologMap, Map<String, Disease> diseaseMap){
        String[] fields = line.split("\\t");
        String diseaseId = fields[5];
        Disease disease = diseaseMap.get(diseaseId);
        
        if (disease == null) {
            String type = fields[6];
            String fullOmimId = fields[7];
            String term = fields[8];
            String altTerms = "";
            if (fields.length == 10) {
                altTerms = fields[9];
            } 
            disease = makeDisease(diseaseId, term, altTerms);
            diseaseMap.put(disease.getDiseaseId(), disease);
        }
        
        if (!fields[0].isEmpty()) {
            GeneIdentifier humanGene = new GeneIdentifier(fields[2], fields[3]);
            GeneIdentifier mouseGene = new GeneIdentifier(fields[1], fields[0]);
            Gene gene = new Gene(mouseGene, humanGene);
            orthologMap.put(mouseGene, gene);
            disease.getAssociatedHumanGenes().add(humanGene);
            disease.getAssociatedMouseGenes().add(mouseGene);
        }
        
    }
    
    private Disease makeDisease(String omimId, String term, String altTerms){
        
        Disease disease = new Disease(omimId);
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
        
    private void populatePhenotypeMatches() {
        
        //this will contain all the known disease associations
        diseaseAssociationMap = new HashMap<String, Set<DiseaseModelAssociation>>();

        String knownDiseaseAssociationStubData = "src/test/resources/data/fgfr2MgiLiteratureMouseModels.dsv";
        makeDiseaseAssociations(diseaseAssociationMap, knownDiseaseAssociationStubData);
         
        //and this one the predicted associations
        predictedDiseaseAssociationsMap = new HashMap<String, Set<DiseaseModelAssociation>>();
        String predictedDiseaseAssociationStubData = "src/test/resources/data/fgfr2PredictedMouseModelToDiseaseAsociations.dsv"; 
        makeDiseaseAssociations(predictedDiseaseAssociationsMap, predictedDiseaseAssociationStubData);
        
        logger.info(String.format("Mapped %d diseases to mouse models: %s ", diseaseAssociationMap.keySet().size(), diseaseAssociationMap.values()));
    
    }
    
    private DiseaseModelAssociation makeDiseaseAssociation(String line) {
        DiseaseModelAssociation diseaseAssociation = new DiseaseModelAssociation();
        
        String[] fields = line.split("\\t");
        
        MouseModel mouseModel = makeMouseModel(fields[3], fields[4], fields[5], fields[6], fields[7], fields[8]);
        DiseaseIdentifier diseaseIdentifier = new DiseaseIdentifier(fields[0]);

        diseaseAssociation.setMouseModel(mouseModel);
        diseaseAssociation.setDiseaseIdentifier(diseaseIdentifier);
        diseaseAssociation.setModelToDiseaseScore(Double.parseDouble(fields[1]));
        diseaseAssociation.setDiseaseToModelScore(Double.parseDouble(fields[2]));
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

    private void addDiseaseAssociationToMap(Map<String, Set<DiseaseModelAssociation>> diseaseAssociationsMap, DiseaseModelAssociation diseaseAssociation) {
        String diseaseId = diseaseAssociation.getDiseaseIdentifier().getCompoundIdentifier();
        if (!diseaseAssociationsMap.containsKey(diseaseId)) {
            Set<DiseaseModelAssociation> diseaseAssociations = new TreeSet<DiseaseModelAssociation>();
            diseaseAssociations.add(diseaseAssociation);
            diseaseAssociationsMap.put(diseaseId, diseaseAssociations);
        } else {
            diseaseAssociationsMap.get(diseaseId).add(diseaseAssociation);
        }
    }
    
    private BufferedReader getReaderFromFile(String fileName){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PhenoDigmDaoStubImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return reader;
    }    

    private void makeDiseaseAssociations(Map<String, Set<DiseaseModelAssociation>> diseaseAssociationsMap, String diseaseAssociationStubData) {
        
        BufferedReader diseaseAssociations = getReaderFromFile(diseaseAssociationStubData);
        
        String line = null;
        int headerLine = 0;
        int currentLine = 0;
        try {
            while ((line = diseaseAssociations.readLine()) != null) {
                //first line is a header 
                if (currentLine != headerLine) {
//                    System.out.println(line);
                    DiseaseModelAssociation diseaseAssociation = makeDiseaseAssociation(line);
                    addDiseaseAssociationToMap(diseaseAssociationsMap, diseaseAssociation);
                }
                currentLine++; 
            }
        } catch (IOException ex) {
            Logger.getLogger(PhenoDigmDaoStubImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public GeneIdentifier getGeneIdentifierForMgiGeneId(String acc) {
        return orthologCache.getMouseGeneIdentifier(acc);
    }
    
    @Override
    public GeneIdentifier getHumanOrthologIdentifierForMgiGeneId(String acc) {
        return orthologCache.getHumanOrthologOfMouseGene(acc);
    }

    @Override
    public List<PhenotypeTerm> getDiseasePhenotypeTerms(String diseaseId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<PhenotypeTerm> getMouseModelPhenotypeTerms(String mouseModelId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<PhenotypeMatch> getPhenotypeMatches(String diseaseId, String mouseModelId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<Disease> getAllDiseses() {
        return diseaseCache.getAllDiseses();
    }

    @Override
    public Set<GeneIdentifier> getAllMouseGeneIdentifiers() {
        return orthologCache.getAllMouseGeneIdentifiers();
    }

    @Override
    public Map<GeneIdentifier, Set<DiseaseModelAssociation>> getKnownDiseaseAssociationsForDiseaseId(String diseaseId) {
        Map<GeneIdentifier, Set<DiseaseModelAssociation>> results = new TreeMap<GeneIdentifier, Set<DiseaseModelAssociation>>();
//        results.put(diseaseId, diseaseAssociationMap.get(diseaseId));
        return results;
    }

    @Override
    public Map<GeneIdentifier, Set<DiseaseModelAssociation>> getPredictedDiseaseAssociationsForDiseaseId(String diseaseId) {
        Map<GeneIdentifier, Set<DiseaseModelAssociation>> results = new TreeMap<GeneIdentifier, Set<DiseaseModelAssociation>>();
//        results.put(diseaseId, diseaseAssociationMap.get(diseaseId));
        return results;    }

    @Override
    public Set<MouseModel> getAllMouseModels() {
        return mouseModelCache.getAllMouseModels();
    }    

    @Override
    public Gene getGene(GeneIdentifier geneIdentifier) {
        return orthologCache.getGene(geneIdentifier);
    }

    @Override
    public Set<Gene> getAllGenes() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
