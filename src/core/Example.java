package core;
import DataProviders.KnowledgeGraphDataProvider;
import DataProviders.OntologyDataProvider;
import FuzzyFunctions.LeftConcreteConcept;
import FuzzyFunctions.TriangularConcreteConcept;
import org.semanticweb.owlapi.model.IRI;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;

public class Example {
    public static void main(String[] args) {
        try {
        	          
            // Ejemplo utilizando ontología en fichero .owl
        	
            // Ontología FuzzyHotels.owl definida en el proyecto
            String hotelsOntology = "/fuzzyHotels.owl";
            String hotelsOntologyPath = Objects.requireNonNull(Example.class.getResource(hotelsOntology)).getPath();

            // IRIs de la ontología FuzzyHotels.owl
            String hotelsIRIbase = "http://www.semanticweb.org/fbobillo/ontologies/2024/5/untitled-ontology-32#";
            IRI hotel = IRI.create(hotelsIRIbase + "Hotel");
            IRI hasPriceHotel = IRI.create(hotelsIRIbase + "hasPrice");
            IRI lowPriceHotel = IRI.create(hotelsIRIbase + "LowPrice");

            // Cargar los datos de la ontología
            OntologyDataProvider odp = new OntologyDataProvider();
            odp.loadOntology(hotelsOntologyPath);

            // Cuantificador absoluto "Pocos" con función "Left(3,5)"
            Quantifier few = new Quantifier("few", new LeftConcreteConcept(3,5), QuantifierType.ABSOLUTE);

            // Consulta: "Pocos hoteles son baratos" -> few(Left(3,5)) of Hotel are LowPrice
            FuzzyQuery fq = new GDType1(odp, few, hotel, lowPriceHotel, hasPriceHotel);
            double result = fq.run();

            System.out.println(" ======= " );
            System.out.println("QUERY 1: " + fq.toString());
            System.out.println("Evaluation Method: " + fq.getMethod());
            System.out.println("Result: " + result);
            System.out.println(" ======= " );
            
            //----------------------------------------------------------
            
            // Ejemplo utilizando un grafo de conocimiento (DBpedia)        
            
            String sparqlEndpoint = "https://dbpedia.org/sparql";

            // Cargar el punto de acceso SPARQL a la ontologia
            KnowledgeGraphDataProvider kgdp = new KnowledgeGraphDataProvider(sparqlEndpoint); 
                 
            IRI cityIRI = IRI.create("http://dbpedia.org/ontology/City");
            IRI lowElevationIRI = IRI.create("http://sid.cps.unizar.es/temp.owl#LowElevation"); 
            IRI elevationIRI = IRI.create("http://dbpedia.org/ontology/elevation");
            IRI highPopulationDensityIRI = IRI.create("http://sid.cps.unizar.es/temp.owl#HighPopulationDensity");
            IRI populationDensityIRI = IRI.create("http://dbpedia.org/ontology/populationDensity");
            
            Quantifier few2 = new Quantifier("few", new LeftConcreteConcept(0, 0.5), QuantifierType.RELATIVE);
            
            // Los tipos de dato difusos se importan desde un fichero previamente generado con datil con clusters para distintas etiquetas de elevacion y densidad de poblacion
            String elevationClusters = "/ElevationClusters.owl";
            String populationClusters = "/PopulationDensityClusters.owl";
          
            kgdp.importFuzzyDatatypes(new File(Objects.requireNonNull(Example.class.getResource(elevationClusters)).getPath()));
            kgdp.importFuzzyDatatypes(new File(Objects.requireNonNull(Example.class.getResource(populationClusters)).getPath()));
        
            FuzzyQuery fq2 = new GDType2(kgdp, few2, cityIRI, highPopulationDensityIRI, populationDensityIRI, lowElevationIRI, elevationIRI);

            double result2 = fq2.run();

            System.out.println(" ======= " );
            System.out.println("QUERY 2: " + fq2.toString());
            System.out.println("Evaluation Method: " + fq2.getMethod());
            System.out.println("Result: " + result2);
            System.out.println(" ======= " );

            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
