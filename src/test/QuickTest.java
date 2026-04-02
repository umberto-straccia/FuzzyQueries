package test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.IRI;

import DataProviders.OntologyDataProvider;
import FuzzyFunctions.LeftConcreteConcept;
import FuzzyFunctions.RightConcreteConcept;
import util.Pair;

public class QuickTest {
    
    static final String hotelsOntology = "/fuzzyHotels.owl";

    // FuzzyHotels.owl
    static final String hotelsIRIbase = "http://www.semanticweb.org/fbobillo/ontologies/2024/5/untitled-ontology-32#";
    static final IRI hotel = IRI.create(hotelsIRIbase + "Hotel");
    static final IRI priceHotel = IRI.create(hotelsIRIbase + "hasPrice");
    static final IRI distanceHotel = IRI.create(hotelsIRIbase + "hasDistance");

    static final IRI lowPriceHotel = IRI.create(hotelsIRIbase + "LowPrice");
    static final IRI highDistanceHotel = IRI.create(hotelsIRIbase + "HighDistance");

    //TO-DO
    @Test
    @DisplayName("Test GD Type I - Few(Left(0.25,0.5)) of wines are LowPriceForWine")
    public void testGDType1() throws Exception {
        OntologyDataProvider odp = new OntologyDataProvider();
        odp.loadOntology(getClass().getResource(hotelsOntology).getPath());
        ArrayList<Double> precios = odp.getPropertyValues(hotel, priceHotel);
        ArrayList<Double> distancias = odp.getPropertyValues(hotel, distanceHotel);

        ArrayList<Pair<Double,Double>> parejas = odp.getPropertyValuesByPairs(hotel, priceHotel, distanceHotel);


        LeftConcreteConcept left = new LeftConcreteConcept(60,100);
        RightConcreteConcept right = new RightConcreteConcept(75,150);

        parejas.forEach(pair -> {
            System.out.println("Precio: "+pair.getD1()+";  PocoPrecio: "+left.getMembershipDegree(pair.getD1()) +
                    " Distancia: "+pair.getD2()+"; MuchaDistancia: "+right.getMembershipDegree(pair.getD2()));
        });

        assertEquals(9, parejas.size());
    }

}

