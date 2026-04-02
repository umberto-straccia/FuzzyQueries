package test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.util.ArrayList;

import org.apache.jena.sparql.exec.RowSet.Exception;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import DataProviders.FuzzyDataProvider;
import DataProviders.KnowledgeGraphDataProvider;
import FuzzyFunctions.FuzzyConcreteConcept;
import util.Pair;

public class KnowledgeGraphDPTests {
    
    static final String kgEndpoint = "https://dbpedia.org/sparql";
    static final String importingFilename = "/importingProperties.owl";

    // DBPedia IRIs accessible through the dbpedia endpoint
    static final IRI seaIRI = IRI.create("http://dbpedia.org/ontology/Sea");
    static final IRI widthIRI = IRI.create("http://dbpedia.org/ontology/width");
    static final IRI mouthPosition = IRI.create("http://dbpedia.org/ontology/mouthPosition");
    static final IRI depthIRI = IRI.create("http://dbpedia.org/ontology/averageDepth");
    
    static final IRI fuzzyIRI = IRI.create("http://sid.cps.unizar.es/temp.owl#VeryLowPoblacion");

    static KnowledgeGraphDataProvider kgdp;
    
    @BeforeAll
    public static void setup() throws OWLOntologyCreationException {
        kgdp = new KnowledgeGraphDataProvider(kgEndpoint);
    }
    @Test
    @DisplayName("Test checkURL")
    public void testCheckURL() {
        KnowledgeGraphDataProvider kgdp = new KnowledgeGraphDataProvider(kgEndpoint);

        try {
            assertTrue(kgdp.checkURL());
        } catch (java.lang.Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    // TO-DO: Reemplazar los test por un Stream
    @Test
    @DisplayName("Test getClasses")
    public void testGetClasses() {
        FuzzyDataProvider fuzzyDataProvider = kgdp;
        ArrayList<IRI> classes = fuzzyDataProvider.getClasses();
        assertNotNull(classes);
    }

    @Test
    @DisplayName("Test getFuzzyDatatypes")
    public void testGetFuzzyProperties() {
        FuzzyDataProvider fuzzyDataProvider = kgdp;

        try {
            fuzzyDataProvider.importFuzzyDatatypes(new File(getClass().getResource(importingFilename).getPath()));
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }

        ArrayList<IRI> fuzzyProps = fuzzyDataProvider.getFuzzyDatatypes();
        assertNotNull(fuzzyProps);
    }

    @Test
    @DisplayName("Test getInstances")
    public void testGetInstances() throws Exception {
        FuzzyDataProvider fdp = kgdp;
        IRI classIRI = seaIRI;
        int expectedResult = 175;

        ArrayList<IRI> instances = null;
        instances = fdp.getInstances(classIRI);

        assertEquals(expectedResult, instances.size());
    }

    @Test
    @DisplayName("Test getProperties")
    public void testGetProperties() throws Exception {
        FuzzyDataProvider fdp = kgdp;
        IRI classIRI = seaIRI;
        int expectedResult = 599;

        ArrayList<IRI> instances = fdp.getProperties(classIRI);
        assertEquals(expectedResult, instances.size());
    }

    @Test
    @DisplayName("Test getPropertyValues")
    public void testGetPropertyValues() throws Exception {
        FuzzyDataProvider fdp = kgdp;
        IRI classIRI = seaIRI;
        IRI propIRI = widthIRI;
        int expectedResult = 28;

        ArrayList<Double> values = fdp.getPropertyValues(classIRI, propIRI);
        assertEquals(expectedResult, values.size());
    }

    @Test
    @DisplayName("Test getPropertyValues doesn't add non-numeric values")
    public void testGetPropertyValuesNonNumeric() throws Exception {
        FuzzyDataProvider fdp = kgdp;
        IRI classIRI = seaIRI;
        IRI propIRI = mouthPosition;
        int expectedResult = 0;

        ArrayList<Double> values = fdp.getPropertyValues(classIRI, propIRI);
        assertEquals(expectedResult, values.size());
    }

    @Test
    @DisplayName("Test getPropertyValuesByPairs equals to getPropertyValues by each property")
    public void testGetPropertyValuesByPairs() throws Exception {
        FuzzyDataProvider fdp = kgdp;
        IRI classIRI = seaIRI;
        IRI propIRIa = widthIRI;
        IRI propIRIb = depthIRI;

        ArrayList<Pair<Double,Double>> values = fdp.getPropertyValuesByPairs(classIRI,propIRIa,propIRIb);
        ArrayList<Double> listA = fdp.getPropertyValues(classIRI, propIRIa);
        ArrayList<Double> listB = fdp.getPropertyValues(classIRI, propIRIb);

        ArrayList<Double> valuesA = new ArrayList<Double>();
        ArrayList<Double> valuesB = new ArrayList<Double>();
        for (Pair<Double,Double> pair : values) {
            valuesA.add(pair.getD1());
            valuesB.add(pair.getD2());
        }
        assertTrue(valuesA.containsAll(listA) && valuesB.containsAll(listB));
    }

    @Test
    @DisplayName("Test getPropertyValuesByPairs can retrieve null properties from instances")
    public void testGetPropertyValuesWithNulls() throws Exception {
        FuzzyDataProvider fdp = kgdp;
        IRI classIRI = seaIRI;
        IRI propIRIa = widthIRI;
        IRI propIRIb = depthIRI;
        
        ArrayList<Pair<Double,Double>> values = fdp.getPropertyValuesByPairs(classIRI,propIRIa,propIRIb);

        for (Pair<Double,Double> pair : values) {
            if (pair.getD1() == null && pair.getD2() == null) {
                assertTrue(true);
                return;
            }
        }
        fail("The function doesn't retrieve null pairs");
    }


    @Test
    @DisplayName("Test getFuzzyFunction")
    public void testGetFuzzyFunction() throws Exception {
        FuzzyDataProvider fdp = kgdp;
   
        FuzzyConcreteConcept fcc = fdp.getFuzzyFunction(fuzzyIRI);
        assertTrue(fcc != null);
    }

    // TO-DO: do ontologies should merge??
    
    @Test
    @DisplayName("Test importFuzzyDatatypes")
    public void testimportFuzzyDatatypes() throws Exception {
        FuzzyDataProvider fdp = kgdp;

        File importingFile = new File(getClass().getResource(importingFilename).getPath());
        IRI fuzzyFuncIRI = IRI.create("http://sid.cps.unizar.es/temp.owl#VeryLowPoblacion");
        
        FuzzyConcreteConcept fcc1 = fdp.getFuzzyFunction(fuzzyFuncIRI);
        try {
            fdp.importFuzzyDatatypes(importingFile);
        } catch (java.lang.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail();
        }
        FuzzyConcreteConcept fcc2 = fdp.getFuzzyFunction(fuzzyFuncIRI);

        assertNotEquals(fcc1, fcc2);
    }

    // Esto no esta bien, hacer mejor
    @Test
    @DisplayName("Test exportPropertyValues")
    public void testExportPropertyValues() throws Exception {
        FuzzyDataProvider fdp = kgdp;
        IRI classIRI = seaIRI;
        IRI propIRI = depthIRI;
        String filename = "knowledgeGraphTest.csv";

        File file = new File(filename);
        
        try {
            fdp.exportPropertyValues(classIRI, propIRI, file);
        } catch (java.lang.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail();
        }

        long fileSize = file.length();
        System.out.println("File size: " + fileSize + " bytes");

        assertNotEquals(fileSize, 0);
    }
}

