package DataProviders;

import fuzzyowl2.parser.Parser;
import util.GetDatatypes;
import util.Pair;
import FuzzyFunctions.FuzzyConcreteConcept;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.sparql.exec.http.QueryExecutionHTTP;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Set;

public class KnowledgeGraphDataProvider implements FuzzyDataProvider{

    private String sparqlEndpoint;

    private HashMap<String,String> importedProperties;

    public KnowledgeGraphDataProvider() {
        this.importedProperties = new HashMap<>();
    }
    
    public KnowledgeGraphDataProvider(String sparqlEndpoint) {
        this.importedProperties = new HashMap<>();
        this.sparqlEndpoint = sparqlEndpoint;
    }

    public boolean checkURL() throws Exception {
        String Query = "ASK {?a ?b ?c}";
        String Query_alt = "SELECT ?s ?p ?o WHERE { ?s ?p ?o } LIMIT 10";
        System.out.println(Query);
        System.out.println(sparqlEndpoint);
        org.apache.jena.query.Query query = QueryFactory.create(Query);
        QueryExecution exec = QueryExecutionHTTP.service(this.sparqlEndpoint,query);

        try {
            if (exec.execAsk() ) {
                return true;
        }
        }catch (Exception e){

        }finally {
            exec.close();
        }
        return false;
    }

    public ArrayList<IRI> getClasses() {
        String Query = "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                + "prefix owl: <http://www.w3.org/2002/07/owl#> "
                + "select distinct ?class "
                + "where { "
                + "?class rdf:type owl:Class .} ";
        System.out.println(Query);

        org.apache.jena.query.Query query = QueryFactory.create(Query);
        QueryExecution exec = QueryExecutionHTTP.service(this.sparqlEndpoint,query);

        ArrayList<IRI> resultados = new ArrayList<>();

        try {
            ResultSet results = exec.execSelect();
            if (!results.hasNext()) {
                System.out.println("Not found");
            }
            while ( results.hasNext() )
            {
                String variableName="?class";
                QuerySolution qs = results.nextSolution();
                RDFNode n = qs.get(variableName);
                resultados.add(IRI.create(n.toString()));
            }
        } finally {
            exec.close();
        }
        return resultados;
    }

    public ArrayList<IRI> getDataProperties() {
        String Query = "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                + "prefix owl: <http://www.w3.org/2002/07/owl#> "
                + "select distinct ?datatype "
                + "where { "
                + "?datatype rdf:type owl:DatatypeProperty .} ";
        System.out.println(Query);

        org.apache.jena.query.Query query = QueryFactory.create(Query);
        QueryExecution exec = QueryExecutionHTTP.service(this.sparqlEndpoint,query);

        ArrayList<IRI> resultados = new ArrayList<>();

        try {
            ResultSet results = exec.execSelect();
            if (!results.hasNext()) {
                System.out.println("Not found");
            }
            while ( results.hasNext() )
            {
                String variableName="?datatype";
                QuerySolution qs = results.nextSolution();
                RDFNode n = qs.get(variableName);
                resultados.add(IRI.create(n.toString()));
            }
        } finally {
            exec.close();
        }
        return resultados;
    }

    public ArrayList<IRI> getNumericProperties() {
        String Query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                   "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                   "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
                   "PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
                   "SELECT DISTINCT ?property ?range " +
                   "WHERE { " +
                   "?property a rdf:Property . " +
                   "?property rdfs:range ?range . " +
                   "FILTER(?range IN (xsd:integer, xsd:float, xsd:double, xsd:decimal, xsd:nonNegativeInteger, xsd:nonPositiveInteger)). " +
                   "}";

        System.out.println(Query);

        org.apache.jena.query.Query query = QueryFactory.create(Query);
        QueryExecution exec = QueryExecutionHTTP.service(this.sparqlEndpoint,query);

        ArrayList<IRI> resultados = new ArrayList<>();

        try {
            ResultSet results = exec.execSelect();
            if (!results.hasNext()) {
                System.out.println("Not found");
            }
            while ( results.hasNext() )
            {
                String variableName="?property";
                QuerySolution qs = results.nextSolution();
                RDFNode n = qs.get(variableName);
                resultados.add(IRI.create(n.toString()));
            }
        } finally {
            exec.close();
        }
        return resultados;
    }

    // Still no support from Fuzzy Labels on DBpedia
    public ArrayList<IRI> getFuzzyDatatypes() {
        ArrayList<IRI> list = new ArrayList<>();

        for (String key : importedProperties.keySet()) {
            list.add(IRI.create(key));
        }
        
        return list;
    }

    public ArrayList<IRI> getSubClassesKG(String parentClass) {
        String Query = "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                + "prefix owl: <http://www.w3.org/2002/07/owl#> "
                + "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
                + "select ?class "
                + "where { "
                + "?class rdfs:subClassOf* <" + parentClass + ">  .}";
        System.out.println(Query);

        org.apache.jena.query.Query query = QueryFactory.create(Query);
        QueryExecution exec = QueryExecutionHTTP.service(this.sparqlEndpoint,query);

        ArrayList<IRI> resultados = new ArrayList<IRI>();

        try {
            ResultSet results = exec.execSelect();
            if (!results.hasNext()) {
                System.out.println("Not found");
            }
            while ( results.hasNext() )
            {
                String variableName="?class";
                QuerySolution qs = results.nextSolution();
                RDFNode n = qs.get(variableName);
                resultados.add(IRI.create(n.toString()));
            }
        } finally {
            exec.close();
        }
        return resultados;
    }

    public ArrayList<IRI> getInstances(IRI class_name) {
        String Query = "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                + "prefix owl: <http://www.w3.org/2002/07/owl#> "
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
                + "select distinct ?instance "
                + "where { "
                + "?instance rdf:type/rdfs:subClassOf* <" + class_name + ">  . } ";
        System.out.println(Query);

        org.apache.jena.query.Query query = QueryFactory.create(Query);
        QueryExecution exec = QueryExecutionHTTP.service(this.sparqlEndpoint,query);

        ArrayList<IRI> resultados = new ArrayList<>();

        try {
            ResultSet results = exec.execSelect();
            if (!results.hasNext()) {
                System.out.println("Not found");
            }
            while ( results.hasNext() )
            {
                String variableName="?instance";
                QuerySolution qs = results.nextSolution();
                RDFNode n = qs.get(variableName);
                resultados.add(IRI.create(n.toString()));
            }
        } finally {
            exec.close();
        }
        return resultados;
    }
    public ArrayList<IRI> getProperties(IRI class_name) {
        String Query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "\n" +
                "SELECT DISTINCT ?property\n" +
                "WHERE {\n" +
                "  ?instance rdf:type/rdfs:subClassOf* <" + class_name + "> .   \n" +
                "  ?instance ?property ?value .\n" +
                "}\n" +
                "ORDER BY ?property ";
        System.out.println(Query);

        org.apache.jena.query.Query query = QueryFactory.create(Query);
        QueryExecution exec = QueryExecutionHTTP.service(this.sparqlEndpoint,query);

        ArrayList<IRI> resultados = new ArrayList<>();

        try {
            ResultSet results = exec.execSelect();
            if (!results.hasNext()) {
                System.out.println("Not found");
            }
            while ( results.hasNext() )
            {
                String variableName="?property";
                QuerySolution qs = results.nextSolution();
                RDFNode n = qs.get(variableName);
                resultados.add(IRI.create(n.toString()));
            }
        } finally {
            exec.close();
        }
        return resultados;
    }

    public ArrayList<Double> getPropertyValuesWithNulls(IRI class_name, IRI property) {

        // This query returns all instances of 'class_name' even if there is no 'property' defined for them
        String Query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "\n" +
                "SELECT DISTINCT ?instance MIN(?value)\n" +
                "WHERE {\n" +
                "  ?instance rdf:type/rdfs:subClassOf* <" + class_name + "> .\n" +
                "  OPTIONAL { ?instance <" + property + "> ?value }.\n" +
                "}";



        System.out.println(Query);
        org.apache.jena.query.Query query = QueryFactory.create(Query);
        QueryExecution exec = QueryExecutionHTTP.service(this.sparqlEndpoint,query);
        ArrayList<Double> resultados = new ArrayList<Double>();

        try {
            ResultSet results = exec.execSelect();
            if (!results.hasNext()) {
                System.out.println("Not found");
            }
            while ( results.hasNext() )
            {

                String valueName="?callret-1";
                QuerySolution qs = results.nextSolution();
                RDFNode n= qs.get(valueName);

                try {
                    String resultado = n.toString();

                    String numericPart = resultado.split("\\^\\^")[0];
                    if (isNumeric(numericPart)) {
                        resultados.add(Double.parseDouble(numericPart));
                    }
                } catch (Exception e) {
                    resultados.add(null);
                }
            }
        } finally {
            exec.close();
        }

        return resultados;
    }

    public ArrayList<Double> getPropertyValues(IRI class_name, IRI property) {

        // This query returns all instances of 'class_name' even if there is no 'property' defined for them
        String Query_optional = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "\n" +
                "SELECT DISTINCT ?instance MIN(?value)\n" +
                "WHERE {\n" +
                "  ?instance rdf:type/rdfs:subClassOf* <" + class_name + "> .\n" +
                "  OPTIONAL { ?instance <" + property + "> ?value }.\n" +
                "}";

        String min_Query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "\n" +
                "SELECT DISTINCT MIN(?value)\n" +
                "WHERE {\n" +
                "  ?instance rdf:type/rdfs:subClassOf* <" + class_name + "> .\n" +
                "  ?instance <" + property + "> ?value .\n" +
                "}";

        // Usando MIN(?value) solo devuelve un valor por instancia
        // El distintc para que lo queremos?
        String Query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "\n" +
                "SELECT ?value\n" +
                "WHERE {\n" +
                "  ?instance rdf:type/rdfs:subClassOf* <" + class_name + "> .\n" +
                "  ?instance <" + property + "> ?value .\n" +
                "}";

        System.out.println(Query);
        org.apache.jena.query.Query query = QueryFactory.create(Query);
        QueryExecution exec = QueryExecutionHTTP.service(this.sparqlEndpoint,query);
        ArrayList<Double> resultados = new ArrayList<Double>();

        try {
            ResultSet results = exec.execSelect();
            if (!results.hasNext()) {
                System.out.println("Not found");
            }
            while ( results.hasNext() )
            {

                String valueName="?value";
//                String valueName="?callret-0";
                QuerySolution qs = results.nextSolution();
                RDFNode n= qs.get(valueName);
                String resultado = n.toString();

                String numericPart = resultado.split("\\^\\^")[0];
                if (isNumeric(numericPart)) {
                    resultados.add(Double.parseDouble(numericPart));
                } else {
                    resultados.add(null);
                }

            }
        } finally {
            exec.close();
        }

        return resultados;
    }

    public ArrayList<Pair<Double,Double>> getPropertyValuesByPairs(IRI class_name, IRI propertyA, IRI propertyB) {

        String Query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "\n" +
                "SELECT DISTINCT MIN(?valueA) MIN(?valueB) \n" +
                "WHERE {\n" +
                "  ?instance rdf:type/rdfs:subClassOf* <" + class_name + "> .\n" +
                "  OPTIONAL { ?instance <" + propertyA + "> ?valueA . }\n" +
                "  OPTIONAL { ?instance <" + propertyB + "> ?valueB . }\n" +
                "}\n" +
                "GROUP BY ?instance\n";

        String new_Query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "\n" +
                "SELECT ?valueA ?valueB \n" +
                "WHERE {\n" +
                "  ?instance rdf:type/rdfs:subClassOf* <" + class_name + "> .\n" +
                "  OPTIONAL { ?instance <" + propertyA + "> ?valueA . }\n" +
                "  OPTIONAL { ?instance <" + propertyB + "> ?valueB . }\n" +
                "}\n" +
                "GROUP BY ?instance\n";


        System.out.println(Query);
        org.apache.jena.query.Query query = QueryFactory.create(Query);
        QueryExecution exec = QueryExecutionHTTP.service(this.sparqlEndpoint,query);
        ArrayList<Pair<Double,Double>> resultados = new ArrayList<>();

        try {
            ResultSet results = exec.execSelect();
            if (!results.hasNext()) {
                System.out.println("Not found");
            }
            while ( results.hasNext() )
            {

                String valueNameA = "?callret-0";
                QuerySolution qs = results.nextSolution();
                RDFNode n = qs.get(valueNameA);

                String valueNameB = "?callret-1";
                RDFNode n2 = qs.get(valueNameB);

                Double valueA = null, valueB = null;
                try {
                    String resultadoA = n.toString();
                    String numericPartA = resultadoA.split("\\^\\^")[0];
                    if (isNumeric(numericPartA)) {
                        valueA = Double.parseDouble(numericPartA);
                    }
                } catch (Exception e) {
//                    System.err.println("xx -> " + class_name.toString() + " has " + propertyA +  " not set, or is not valid");
                }

                try {
                    String resultadoB = n2.toString();
                    String numericPartB = resultadoB.split("\\^\\^")[0];
                    if (isNumeric(numericPartB)) {
                        valueB = Double.parseDouble(numericPartB);
                    }
                } catch (Exception e) {
//                    System.err.println("oo -> " + class_name.toString() + " has " + propertyB +  " not set, or is not valid");
                }
                resultados.add(new Pair<>(valueA,valueB));

            }
        } finally {
            exec.close();
        }

        return resultados;
    }
    
    public double getPropertyValue(IRI instance, IRI property) throws NoSuchElementException {
        String Query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "\n" +
                "SELECT DISTINCT MIN(?value)\n" +
                "WHERE {\n" +
                "<" + instance + "> <" + property + "> ?value . }";

        System.out.println(Query);
        org.apache.jena.query.Query query = QueryFactory.create(Query);
        QueryExecution exec = QueryExecutionHTTP.service(this.sparqlEndpoint,query);

        try {
            ResultSet results = exec.execSelect();
            if (!results.hasNext()) {
                System.out.println("Not found");
            }
            while ( results.hasNext() )
            {

                String valueName="?callret-1";
                QuerySolution qs = results.nextSolution();
                RDFNode n = qs.get(valueName);
                String resultado = n.toString();

                if (isNumeric(resultado.split("\\^\\^")[0])) {
                    return Double.parseDouble(n.toString());
                }

            }
        } finally {
            exec.close();
        }

        return 0;
    }

    public void exportPropertyValues(IRI className, IRI property, File file) throws Exception {

        BufferedWriter output = new BufferedWriter(new FileWriter(file));
        String propertyFragment = property.getFragment();
        output.write(propertyFragment);
        output.newLine();

        ArrayList<Double> values = getPropertyValues(className, property);
        for ( Double value: values) {
            output.write(value.toString());
            output.newLine();
        }

        output.close();
    }

    public void importFuzzyDatatypes(File file) throws Exception {

        OWLOntologyManager import_manager = OWLManager.createOWLOntologyManager();
        OWLOntology import_ont = import_manager.loadOntologyFromOntologyDocument(file);
        Set<OWLDatatype> dataTypesG = GetDatatypes.getFuzzyDataTypes(import_ont);

        for (OWLDatatype owlDatatype : dataTypesG) {
            importedProperties.put(owlDatatype.getIRI().toString(),
                GetDatatypes.getAnnotationAx(import_ont,owlDatatype).getValue().toString());
        }

    }

    // Currently there is no fuzzy Labels support on DBPedia
    public FuzzyConcreteConcept getFuzzyFunction(IRI fuzzyProperty) throws NoSuchElementException {
        if (importedProperties != null) {
            String fuzzyFunctionStr = importedProperties.get(fuzzyProperty.toString());
            if (fuzzyFunctionStr != null) {
                Object ob1 = Parser.getDatatype(fuzzyFunctionStr);
                return GetDatatypes.parseFuzzyFunction(ob1);
            }
        }

        return null;
    }

    public static boolean isNumeric(String string) {
        double intValue;

        if(string == null || string.equals("")) {

            return false;
        }

        try {
            intValue = Double.parseDouble(string);
            return true;
        } catch (NumberFormatException e) {

        }
        return false;
    }

    public String getSparqlEndpoint() {
        return this.sparqlEndpoint;
    }

    public void setSparqlEndpoint(String sparqlEndpoint) {
        this.sparqlEndpoint = sparqlEndpoint;
    }
}
