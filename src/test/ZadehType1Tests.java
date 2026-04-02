package test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.IRI;

import DataProviders.OntologyDataProvider;
import FuzzyFunctions.LeftConcreteConcept;
import core.FuzzyQuery;
import core.Quantifier;
import core.QuantifierType;
import core.ZadehType1;

public class ZadehType1Tests {
    
    static final String winesOntology = "/FuzzyWine.owl";
    static final String hotelsOntology = "/fuzzyHotels.owl";

    // Ontology IRIs defined in FuzzyWine.owl
    static final String wineIRIbase = "http://www.w3.org/TR/2003/PR-owl-guide-20031209/wine#";
    static final IRI wine = IRI.create(wineIRIbase + "Wine");
    static final IRI hasAlcohol = IRI.create(wineIRIbase + "hasAlcohol");
    static final IRI highAlcohol = IRI.create(wineIRIbase + "HighAlcoholForWine");
    static final IRI hasPriceWine = IRI.create(wineIRIbase + "hasPrice");
    static final IRI lowPriceWine = IRI.create(wineIRIbase + "LowPriceForWine");
    static final IRI winery = IRI.create(wineIRIbase + "Winery");
    
    // FuzzyHotels.owl
    static final String hotelsIRIbase = "http://www.semanticweb.org/fbobillo/ontologies/2024/5/untitled-ontology-32#";
    static final IRI hotel = IRI.create(hotelsIRIbase + "Hotel");
    static final IRI priceHotel = IRI.create(hotelsIRIbase + "hasPrice");
    static final IRI lowPriceHotel = IRI.create(hotelsIRIbase + "LowPrice");

    //TO-DO
    @Test
    @DisplayName("Test Zadeh Type I - Few(Left(0.25,0.5)) of wines are LowPriceForWine")
    public void testZadehType1() throws Exception {
        OntologyDataProvider odp = new OntologyDataProvider();
        odp.loadOntology(getClass().getResource(winesOntology).getPath());

        Quantifier few = new Quantifier("few", new LeftConcreteConcept(0.25,0.5), QuantifierType.ABSOLUTE);

        FuzzyQuery fq = new ZadehType1(
                odp, few, wine, lowPriceWine, hasPriceWine);
        assertEquals(0, fq.run());
    }

    @Test
    @DisplayName("Test Zadeh Type I - Few(Left(3,5)) of hotels are LowPrice")
    public void testZadehType1_2() throws Exception {
        OntologyDataProvider odp = new OntologyDataProvider();
        odp.loadOntology(getClass().getResource(hotelsOntology).getPath());

        Quantifier few = new Quantifier("few", new LeftConcreteConcept(3,5), QuantifierType.ABSOLUTE);

        FuzzyQuery fq = new ZadehType1(
                odp, few, hotel, lowPriceHotel, priceHotel);
        assertEquals(0.875, fq.run());
    }
}

