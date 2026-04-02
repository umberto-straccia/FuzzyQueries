package test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.apache.jena.sparql.exec.RowSet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import DataProviders.FuzzyDataProvider;
import DataProviders.KnowledgeGraphDataProvider;
import util.Pair;

public class OpendataAragonTests {


    static final String kgEndpoint = "http://opendata.aragon.es/sparql";
    static final String importingFilename = "/importingProperties.owl";

    // DBPedia IRIs accessible through the dbpedia endpoint
    static final IRI municipioIRI = IRI.create("http://dbpedia.org/ontology/Municipality");
    static final IRI womenPopIRI = IRI.create("http://opendata.aragon.es/def/Aragopedia#womenPopulation");
    static final IRI menPopIRI = IRI.create("http://opendata.aragon.es/def/Aragopedia#menPopulation");

    static final IRI fuzzyIRI = IRI.create("http://sid.cps.unizar.es/temp.owl#VeryLowPoblacion");

    static KnowledgeGraphDataProvider kgdp;

    @BeforeAll
    public static void setup() throws OWLOntologyCreationException {
        kgdp = new KnowledgeGraphDataProvider(kgEndpoint);
    }

    // TO-DO: Reemplazar los test por un Stream
    @Test
    @DisplayName("Test getClasses")
    public void testGetClasses() {
        FuzzyDataProvider fuzzyDataProvider = kgdp;
        ArrayList<IRI> classes = fuzzyDataProvider.getClasses();
        System.out.println(classes);
        assertTrue(!classes.isEmpty());
    }


    @Test
    @DisplayName("Test getProperties")
    public void testGetProperties() throws RowSet.Exception {
        FuzzyDataProvider fdp = kgdp;
        IRI classIRI = municipioIRI;
        int expectedResult = 12;

        ArrayList<IRI> props = fdp.getProperties(classIRI);
        System.out.println(props);
        assertEquals(expectedResult, props.size());
    }

    @Test
    @DisplayName("Test getPropertyValues")
    public void testGetPropertyValues() throws RowSet.Exception {
        FuzzyDataProvider fdp = kgdp;
        IRI classIRI = municipioIRI;
        IRI propIRI = womenPopIRI;
        int expectedResult = 1574;

        ArrayList<Double> values = fdp.getPropertyValues(classIRI, propIRI);
        assertEquals(expectedResult, values.size());
    }

    @Test
    @DisplayName("Test getPropertyValuesByPairs equals to getPropertyValues by each property")
    public void testGetPropertyValuesByPairs() throws RowSet.Exception {
        FuzzyDataProvider fdp = kgdp;
        IRI classIRI = municipioIRI;
        IRI propIRIa = womenPopIRI;
        IRI propIRIb = menPopIRI;

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


}
