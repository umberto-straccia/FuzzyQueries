package DataProviders;

import fuzzyowl2.parser.Parser;
import util.GetDatatypes;
import util.Pair;
import FuzzyFunctions.FuzzyConcreteConcept;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.vocab.XSDVocabulary;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class OntologyDataProvider implements FuzzyDataProvider{

    OWLOntology ontology;
    OWLReasoner reasoner;
    OWLDataFactory factory;
    OWLOntologyManager manager;

    private HashMap<String,String> importedFuzzyDatatypes = new HashMap<>();

    public void loadOntology(String path) throws OWLOntologyCreationException {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        File file = new File(path);
        this.ontology = manager.loadOntologyFromOntologyDocument(file);
        factory = manager.getOWLDataFactory();
        reasoner = new Reasoner.ReasonerFactory().createReasoner(ontology);

        // Dont seem to do anything????? vvvvv
        // Maybe we have to select manually what inferences to precompute
        // Do some testing
        reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY,InferenceType.DATA_PROPERTY_ASSERTIONS);
        //reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
    }

    public ArrayList<IRI> getClasses(){
        Set<OWLClass> classes = ontology.getClassesInSignature();
        ArrayList<IRI> classes_IRIs = new ArrayList<>();
        for ( OWLClass owlClass : classes ) {
            classes_IRIs.add(owlClass.getIRI());
        }
        return classes_IRIs;
    }

    public ArrayList<IRI> getFuzzyDatatypes(){
        ArrayList<IRI> fuzzyDatatypes = new ArrayList<>();
        
        for (String propName : importedFuzzyDatatypes.keySet()) {
            fuzzyDatatypes.add(IRI.create(propName));
        }

        Set<OWLDatatype> ontologyFuzzyDatatypes = GetDatatypes.getFuzzyDataTypes(ontology);
        for ( OWLDatatype odtt : ontologyFuzzyDatatypes) {
            fuzzyDatatypes.add(odtt.getIRI());
        }
        return fuzzyDatatypes;
    }

    public ArrayList<IRI> getInstances(IRI class_name) {
        OWLClass owlClass = factory.getOWLClass(class_name);
        NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(owlClass, false);
        Set<OWLNamedIndividual> instances = individualsNodeSet.getFlattened();
        ArrayList<IRI> classes_IRIs = new ArrayList<>();
        for ( OWLNamedIndividual instance : instances ) {
            classes_IRIs.add(instance.getIRI());
        }
        return classes_IRIs;
    }

    public ArrayList<IRI> getDatatypes() {
        Set<OWLDatatype> datatypes = ontology.getDatatypesInSignature();
        ArrayList<IRI> datatypesIRIs = new ArrayList<>();
        for (OWLDatatype datatype : datatypes) {
            datatypesIRIs.add(datatype.getIRI());
        }
        return datatypesIRIs;
    }

    public ArrayList<IRI> getNumericProperties() {
        Set<OWLDatatype> supportedDts = new HashSet<>();
        supportedDts.add(factory.getOWLDatatype(XSDVocabulary.DOUBLE.getIRI()));
        supportedDts.add(factory.getOWLDatatype(XSDVocabulary.INTEGER.getIRI()));
        supportedDts.add(factory.getOWLDatatype(XSDVocabulary.DECIMAL.getIRI()));
        supportedDts.add(factory.getOWLDatatype(XSDVocabulary.FLOAT.getIRI()));
        supportedDts.add(factory.getOWLDatatype(XSDVocabulary.NON_NEGATIVE_INTEGER.getIRI()));
        supportedDts.add(factory.getOWLDatatype(XSDVocabulary.NON_POSITIVE_INTEGER.getIRI()));
      
        Set<OWLDataProperty> dataTypes = ontology.getDataPropertiesInSignature();
        ArrayList<IRI> numericProps = new ArrayList<>();
        for (OWLDataProperty data : dataTypes) {
            Set<OWLDataPropertyRangeAxiom> rangeAxioms = ontology.getDataPropertyRangeAxioms(data);
            for (OWLDataPropertyRangeAxiom rangeAxiom : rangeAxioms) {
                OWLDatatype range = rangeAxiom.getRange().asOWLDatatype();
                if (supportedDts.contains(range)) {
                    numericProps.add(data.getIRI());
                }
            }
        }

        System.out.println("Numeric properties: " + numericProps.size());
        return numericProps;
    }


    public ArrayList<IRI> getProperties(IRI class_name) {
        Set<OWLDatatype> datatypes =  GetDatatypes.getFuzzyDataTypes(ontology);
        ArrayList<IRI> datatypesIRIs = new ArrayList<>();
        for (OWLDatatype datatype : datatypes) {
            datatypesIRIs.add(datatype.getIRI());
        }
        return datatypesIRIs;
    }

    public ArrayList<Double> getPropertyValues(IRI class_name, IRI property) {
        OWLDataProperty propertyName = factory.getOWLDataProperty(property);
        OWLClass owlClass = factory.getOWLClass(class_name);
        NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(owlClass, false);
        Set<OWLNamedIndividual> instances = individualsNodeSet.getFlattened();

        ArrayList<Double> values = new ArrayList<>();
        for ( OWLNamedIndividual instance : instances) {
            try {
                values.add(getDataValue(instance, propertyName));
            } catch (NoSuchElementException e) {
                values.add(null);
//                System.err.println("xx-> instance " + instance.getIRI() + " has not valid " + propertyName.toString());
            }
        }
        return values;
    }
    public ArrayList<Pair<Double,Double>> getPropertyValuesByPairs(IRI class_name, IRI propertyA, IRI propertyB) {
        OWLDataProperty propertyNameA = factory.getOWLDataProperty(propertyA);
        OWLDataProperty propertyNameB = factory.getOWLDataProperty(propertyB);
        OWLClass owlClass = factory.getOWLClass(class_name);
        NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(owlClass, false);
        Set<OWLNamedIndividual> instances = individualsNodeSet.getFlattened();

        ArrayList<Pair<Double,Double>> values = new ArrayList<>();
        for ( OWLNamedIndividual instance : instances) {
            Double valueA = null, valueB = null;
            try {
                valueA = getDataValue(instance, propertyNameA);
            } catch (NoSuchElementException e) {
//                System.err.println(instance.toString() + " has  " + propertyNameA + " not set, or is not valid");
            }
            try {
                valueB = getDataValue(instance, propertyNameB);
            } catch (NoSuchElementException e) {
//                System.err.println(instance.toString() + " has " + propertyNameB + " not set, or is not valid");
            }
        values.add(new Pair(valueA, valueB));
    }
        return values;
    }
    public double getPropertyValue(IRI instance, IRI property) throws NoSuchElementException {
        return getDataValue(factory.getOWLNamedIndividual(instance), factory.getOWLDataProperty(property));
    };

    public FuzzyConcreteConcept getFuzzyFunction(IRI fuzzyProperty) {
       String fuzzyFunctionStr = importedFuzzyDatatypes.get(fuzzyProperty.toString());
       if (fuzzyFunctionStr != null) {
           Object ob1 = Parser.getDatatype(fuzzyFunctionStr);
           return GetDatatypes.parseFuzzyFunction(ob1);
       }

        for (OWLAxiom ax : ontology.getAxioms(AxiomType.DATATYPE_DEFINITION)) {
            for (OWLDatatype dt : ax.getDatatypesInSignature()) {
                OWLAnnotationAssertionAxiom s1 = GetDatatypes.getAnnotationAx(ontology,dt);
                if (s1 != null) {
                    String st = s1.getSubject().toString();
                    if (s1.toString().contains("#fuzzyLabel") && st.equals(fuzzyProperty.toString())) {
                        Object ob1 = Parser.getDatatype(s1.getValue().toString());
                        return GetDatatypes.parseFuzzyFunction(ob1);
                    }
                }
            }
        }
        return null;
    };

    public void exportPropertyValues(IRI owlClass, IRI property, File file) throws Exception {
        OWLClass objClass = factory.getOWLClass(owlClass);
        NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(objClass, false);
        Set<OWLNamedIndividual> individuals = individualsNodeSet.getFlattened();

        ArrayList<Double> values = new ArrayList<>();
        OWLDataProperty propertyName = factory.getOWLDataProperty(property);

        BufferedWriter output = new BufferedWriter(new FileWriter(file));

        String propertyFragment = property.getFragment();
        output.write(propertyFragment);
        output.newLine();

        Set<OWLNamedIndividual> finalIndividuals = new HashSet<>(individuals);
        for (OWLNamedIndividual ind : individuals) {
            try {
                Set<OWLLiteral> literalsSet = reasoner.getDataPropertyValues(ind, propertyName);
                if (literalsSet.size() == 1) {
                    String lit = null;
                    for (OWLLiteral lit2 : literalsSet) {
                        lit = lit2.getLiteral();
                    }
                    output.write(lit);
                    output.newLine();
                } else {
                    throw new Exception("Dataproperty not valid for "+ind.toString()+". Value: "+literalsSet.toString());
                }
            } catch (Exception e) {
                finalIndividuals.remove(ind);
            }
        }

        output.close();
    }

    public void importFuzzyDatatypes(File file) throws Exception {

        OWLOntologyManager import_manager = OWLManager.createOWLOntologyManager();
        OWLOntology import_ont = import_manager.loadOntologyFromOntologyDocument(file);
        Set<OWLDatatype> dataTypesG = GetDatatypes.getFuzzyDataTypes(import_ont);

        for (OWLDatatype owlDatatype : dataTypesG) {
            importedFuzzyDatatypes.put(owlDatatype.getIRI().toString(),
                GetDatatypes.getAnnotationAx(import_ont,owlDatatype).getValue().toString());
        }
    }

    private double getDataValue(OWLNamedIndividual ind, OWLDataProperty propertyName) throws NoSuchElementException {
        Set<OWLLiteral> literalsSet = reasoner.getDataPropertyValues(ind, propertyName);
        if (literalsSet.size() == 1) {
            String lit = null;
            for (OWLLiteral lit2 : literalsSet) {
                lit = lit2.getLiteral();
            }
            return Double.parseDouble(lit);
        } else {
            throw new NoSuchElementException( "Dataproperty not valid for "+ind.toString()+". Value: "+literalsSet.toString());
        }
    }


    public OWLOntology getOntology() {
        return ontology;
    }

    public void setOntology(OWLOntology ontology) {
        this.ontology = ontology;
    }

    public OWLReasoner getReasoner() {
        return reasoner;
    }

    public void setReasoner(OWLReasoner reasoner) {
        this.reasoner = reasoner;
    }

    public OWLDataFactory getFactory() {
        return factory;
    }

    public void setFactory(OWLDataFactory factory) {
        this.factory = factory;
    }

    public OWLOntologyManager getManager() {
        return manager;
    }

    public void setManager(OWLOntologyManager manager) {
        this.manager = manager;
    }

}
