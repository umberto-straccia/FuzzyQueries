package test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.IRI;

import DataProviders.OntologyDataProvider;
import FuzzyFunctions.TriangularConcreteConcept;
import core.FuzzyQuery;
import core.GDType2;
import core.Quantifier;
import core.QuantifierType;

public class GDType2Tests {
    
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
    static final IRI distanceHotel = IRI.create(hotelsIRIbase + "hasDistance");
    static final IRI highDistanceHotel = IRI.create(hotelsIRIbase + "HighDistance");

    //TO-DO
//    @Test
//    @DisplayName("Test GD Type II - Few(Left(0.25,0.5)) of HighAlcoholWines are LowPriceForWine")
//    public void testGDType2Wines() throws Exception {
//        OntologyDataProvider odp = new OntologyDataProvider();
//        odp.loadOntology(getClass().getResource(hotelsOntology).getPath());
//
//        Quantifier almost_half = new Quantifier("almost_half", new TriangularConcreteConcept(0.25,0.5,0.75), QuantifierType.RELATIVE);
//        Quantifier almost_a_quarter = new Quantifier("almost_a_quarter", new TriangularConcreteConcept(0.15,0.25,0.35), QuantifierType.RELATIVE);
//
//        FuzzyQuery fq = new GDType2(
//                odp, almost_a_quarter, hotel, lowPriceHotel, priceHotel, highDistanceHotel, distanceHotel);
//
//        System.out.println(fq.run());
//        assertEquals(1, fq.run());
//    }


    @Test
    @DisplayName("Test GD Type II - Almost-a-quarter(triangular(0.15,0.25,0.35) of lowPriceHotel are highDistanceHotel")
    public void testGDType2WinesHotels() throws Exception {
        OntologyDataProvider odp = new OntologyDataProvider();
        odp.loadOntology(getClass().getResource(hotelsOntology).getPath());

        Quantifier almost_a_quarter =
                new Quantifier("almost_a_quarter", new TriangularConcreteConcept(0.15,0.25,0.35), QuantifierType.RELATIVE);

        FuzzyQuery fq =
                new GDType2(odp, almost_a_quarter, hotel,  lowPriceHotel, priceHotel, highDistanceHotel, distanceHotel);

        System.out.println(fq.run());
        assertEquals(0.2222222222222222, fq.run());
    }

    }

