package DataProviders;

import java.io.File;
import java.util.ArrayList;

import org.semanticweb.owlapi.model.IRI;

import FuzzyFunctions.FuzzyConcreteConcept;
import util.Pair;

/**
 * Interface for data providers that allow to query fuzzy data from an ontology
 */
public interface FuzzyDataProvider {

    // Get all owl classes in the ontology
    public ArrayList<IRI> getClasses();

    // Get all instances of class_name inside the ontology
    public ArrayList<IRI> getInstances(IRI class_name);

    // Get all properties of class_name inside the ontology
    public ArrayList<IRI> getProperties(IRI class_name);

    // Get all fuzzy datatypes in the ontology as recognized by FuzzyOWL2 parser
    public ArrayList<IRI> getFuzzyDatatypes();

    /** Get all properties in the ontology with range in:
     * xsd:integer, xsd:decimal, xsd:double, xsd:float,
     * xsd:nonNegativeInteger, xsd:positiveInteger
     */
    public ArrayList<IRI> getNumericProperties();

    // Get the value of a property for a specific instance. If the property is not numeric, it will throw an exception
    public double getPropertyValue(IRI instance, IRI property) throws Exception;

    /**
     *  Get ONLY NUMERIC property values for class_name inside the ontology. 
     *  Non-numeric values are add to the list as 'null' to reflect an invalid property value.
     */
    public ArrayList<Double> getPropertyValues(IRI class_name, IRI property);
    
    /** Get ONLY NUMERIC property values for class_name inside the ontology.
     * Every pair contains the value of propertyA and propertyB for the same instance.
     * One or both values of a Pair can be null if that property is not set or its value is invalid
     * */ 
    public ArrayList<Pair<Double,Double>> getPropertyValuesByPairs(IRI class_name, IRI propertyA, IRI propertyB);

    // Get the fuzzy function inside a fuzzy datatype annotation
    public FuzzyConcreteConcept getFuzzyFunction(IRI fuzzyDatatype);

    // Export the property values of class_name and property to a csv file
    public void exportPropertyValues(IRI class_name, IRI property, File file) throws Exception;

    // Import fuzzy datatypes from an ontology file
    public void importFuzzyDatatypes(File file) throws Exception;

}
