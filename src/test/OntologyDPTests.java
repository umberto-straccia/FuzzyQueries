package test;

import FuzzyFunctions.FuzzyConcreteConcept;
import util.Pair;

import org.apache.jena.sparql.exec.RowSet.Exception;
import org.junit.jupiter.api.DisplayName;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import DataProviders.FuzzyDataProvider;
import DataProviders.OntologyDataProvider;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

public class OntologyDPTests {
    
    static final String ontologyFilename = "/FuzzyWine.owl";
    static final String importingFilename = "/importingProperties.owl";

    // Ontology IRIs defined in FuzzyWine.owl
    static final IRI wineIRI = IRI.create("http://www.w3.org/TR/2003/PR-owl-guide-20031209/wine#Wine");
    static final IRI hasAlcoholIRI = IRI.create("http://www.w3.org/TR/2003/PR-owl-guide-20031209/wine#hasAlcohol");
    static final IRI hasHighAlcIRI = IRI.create("http://www.w3.org/TR/2003/PR-owl-guide-20031209/wine#HighAlcoholForWine");
    static final IRI hasPrice = IRI.create("http://www.w3.org/TR/2003/PR-owl-guide-20031209/wine#hasPrice");
    static final IRI wineryIRI = IRI.create("http://www.w3.org/TR/2003/PR-owl-guide-20031209/wine#Winery");

    static final IRI fuzzyIRI = IRI.create("http://sid.cps.unizar.es/temp.owl#VeryLowPoblacion");

    static OntologyDataProvider ontologyDataProvider;

    @BeforeAll
    public static void setup() throws OWLOntologyCreationException {
        ontologyDataProvider = new OntologyDataProvider();
        String ontologyPath = OntologyDPTests.class.getResource(ontologyFilename).getPath();
        ontologyDataProvider.loadOntology(ontologyPath);
    }

    // TO-DO: Reemplazar los test por un Stream
    @Test
    @DisplayName("Test getClasses")
    public void testGetClasses() {
        FuzzyDataProvider fuzzyDataProvider = ontologyDataProvider;
        ArrayList<IRI> classes = fuzzyDataProvider.getClasses();
        assertNotNull(classes);
    }

    @Test
    @DisplayName("Test getFuzzyDatatypes")
    public void testGetFuzzyProperties() {
        FuzzyDataProvider fuzzyDataProvider = ontologyDataProvider;

            // if (provider.equals("OntologyDataProvider")) {
            //     fdp.importFuzzyDatatypes(new File(getClass().getResource(importingFilename).getPath()));
            // }

        ArrayList<IRI> fuzzyProps = fuzzyDataProvider.getFuzzyDatatypes();
        assertNotNull(fuzzyProps);
    }

    @Test
    @DisplayName("Test getInstances")
    public void testGetInstances() throws Exception {
        FuzzyDataProvider fdp = ontologyDataProvider;
        IRI classIRI = wineIRI;
        int expectedResult = 24;

        ArrayList<IRI> instances = null;
        instances = fdp.getInstances(classIRI);

        assertEquals(expectedResult, instances.size());
    }

    @Test
    @DisplayName("Test getProperties")
    public void testGetProperties() throws Exception {
        FuzzyDataProvider fdp = ontologyDataProvider;
        IRI classIRI = wineIRI;
        int expectedResult = 23;

        ArrayList<IRI> instances = fdp.getProperties(classIRI);
        assertEquals(expectedResult, instances.size());
    }


    @Test
    @DisplayName("Test getPropertyValues")
    public void testGetPropertyValues() throws Exception {
        FuzzyDataProvider fdp = ontologyDataProvider;
        IRI classIRI = wineIRI;
        IRI propIRI = hasAlcoholIRI;
        int expectedResult = 24;

        ArrayList<Double> values = fdp.getPropertyValues(classIRI, propIRI);
        assertEquals(expectedResult, values.size());
    }


    @Test
    @DisplayName("Test getPropertyValues doesn't add non-numeric values")
    public void testGetPropertyValuesNonNumeric() throws Exception {
        FuzzyDataProvider fdp = ontologyDataProvider;
        IRI classIRI = wineIRI;
        IRI propIRI = hasAlcoholIRI;
        int expectedResult = 24;

        ArrayList<Double> values = fdp.getPropertyValues(classIRI, propIRI);
        assertEquals(expectedResult, values.size());
    }

    @Test
    @DisplayName("Test getPropertyValuesByPairs equals to getPropertyValues by each property")
    public void testGetPropertyValuesByPairs() throws Exception {
        FuzzyDataProvider fdp = ontologyDataProvider;
        IRI classIRI = wineIRI;
        IRI propIRIa = hasAlcoholIRI;
        IRI propIRIb = hasPrice;


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
        FuzzyDataProvider fdp = ontologyDataProvider;
        IRI classIRI = wineIRI;
        IRI propIRIa = hasAlcoholIRI;
        IRI propIRIb = hasPrice;
        
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
        FuzzyDataProvider fdp = ontologyDataProvider;
   
        FuzzyConcreteConcept fcc = fdp.getFuzzyFunction(hasHighAlcIRI);
        assertNotNull(fcc);
    }

    // TO-DO: do ontologies should merge??
    
    @Test
    @DisplayName("Test importFuzzyDatatypes")
    public void testImportFuzzyProperties() {
        FuzzyDataProvider fdp = ontologyDataProvider;

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
        FuzzyDataProvider fdp = ontologyDataProvider;
        IRI classIRI = wineIRI;
        IRI propIRI = hasPrice;
        String filename = "ontologyTest.csv";

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

