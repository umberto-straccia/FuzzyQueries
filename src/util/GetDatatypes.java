package util;

import FuzzyFunctions.FuzzyConcreteConcept;
import FuzzyFunctions.LeftConcreteConcept;
import FuzzyFunctions.RightConcreteConcept;
import FuzzyFunctions.TrapezoidalConcreteConcept;
import FuzzyFunctions.TriangularConcreteConcept;
import fuzzyowl2.LeftShoulderFunction;
import fuzzyowl2.RightShoulderFunction;
import fuzzyowl2.TrapezoidalFunction;
import fuzzyowl2.TriangularFunction;
import fuzzyowl2.parser.Parser;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

public class GetDatatypes {

    public static OWLAnnotationAssertionAxiom getAnnotationAx(OWLOntology ontology, OWLDatatype dataType) {
        //Set<OWLAnnotationAssertionAxiom> set2 = ontology.getAnnotationAssertionAxioms(IRI.create(dataType.getIRI().toString()));
        Set<OWLAnnotationAssertionAxiom> set2 = ontology.getAnnotationAssertionAxioms(dataType.getIRI());
        OWLAnnotationAssertionAxiom annotationAxiom = null;
        if (set2.size() == 1) {
            for (OWLAnnotationAssertionAxiom annAxiom : set2)
                annotationAxiom = annAxiom;
        }
        return annotationAxiom;
    }

    public static Set<OWLDatatype> getFuzzyDataTypes(OWLOntology ontology) {
        Set<OWLDatatype> fuzzyDataTypes = new HashSet<>();

        for (OWLAxiom ax : ontology.getAxioms(AxiomType.DATATYPE_DEFINITION)) {
            for (OWLDatatype dt : ax.getDatatypesInSignature()) {
                OWLAnnotationAssertionAxiom s1 = getAnnotationAx(ontology, dt);

                if (s1 != null && s1.toString().contains("#fuzzyLabel")) {
                    fuzzyDataTypes.add(dt);
                    Object ob1 = Parser.getDatatype(s1.getValue().toString());
                    if ((ob1 != null) && (ob1 instanceof TriangularFunction)) {
                        TriangularFunction t1 = (TriangularFunction) ob1;
                        double a = t1.getA();
                        double b = t1.getB();
                        double c = t1.getC();
                        //System.out.println(st + " |" + ob1.toString() + " |" + a + "," + b + "," + c);
                    } else if ((ob1 != null) && (ob1 instanceof LeftShoulderFunction)) {
                        LeftShoulderFunction t1 = (LeftShoulderFunction) ob1;
                        double a = t1.getA();
                        double b = t1.getB();
                        //System.out.println(st + " |" + ob1.toString() + " |" + a + "," + b);
                    } else if ((ob1 != null) && (ob1 instanceof RightShoulderFunction)) {
                        RightShoulderFunction t1 = (RightShoulderFunction) ob1;
                        double a = t1.getA();
                        double b = t1.getB();
                        //System.out.println(st + " |" + ob1.toString() + " |" + a + "," + b);
                    } else if ((ob1 != null) && (ob1 instanceof TrapezoidalFunction)) {
                        TrapezoidalFunction t1 = (TrapezoidalFunction) ob1;
                        double a = t1.getA();
                        double b = t1.getB();
                        double c = t1.getC();
                        double d = t1.getD();
                    }
                }
            }
        }
        return fuzzyDataTypes;
    }

    /**
     * Finds the suited function to represent the fuzzy set
     *
     * @param ontology The ontology applied
     * @param cl       Class which all members of the set belong
     * @param dataType The name of the fuzzy property to search
     * @return An object with the extended class with the right function to calculate membership degree
     */
    public static FuzzyConcreteConcept getFuzzyFunc(OWLOntology ontology, OWLClass cl, IRI dataType){
        for (OWLAxiom ax : ontology.getAxioms(AxiomType.DATATYPE_DEFINITION)) {
            for (OWLDatatype dt : ax.getDatatypesInSignature()) {
                OWLAnnotationAssertionAxiom s1 = getAnnotationAx(ontology, dt);
                if (s1 != null) {
                    String st = s1.getSubject().toString();
                    if (s1.toString().contains("#fuzzyLabel") && st.equals(dataType.toString())) {
                        Object ob1 = Parser.getDatatype(s1.getValue().toString());
                        return parseFuzzyFunction(ob1);
                    }
                }
            }
        }
        return null;
    }
    public static boolean isValidFuzzyFunction(OWLAnnotationAssertionAxiom ax) {
        if (ax == null || !ax.toString().contains("#fuzzyLabel")) {
            return false;
        }

        Object ob1 = Parser.getDatatype(ax.getValue().toString());

        if (ob1 instanceof TriangularFunction) {
            return true;
        } else if (ob1 instanceof LeftShoulderFunction) {
            return true;
        } else if (ob1 instanceof RightShoulderFunction) {
            return true;
        } else if (ob1 instanceof TrapezoidalFunction) {
            return true;
        } else {
            return false;
        }
    }
    public static double getDataValue(OWLNamedIndividual ind, OWLDataProperty propertyName, OWLReasoner reasoner) throws NoSuchElementException {
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
    public static void getDataTypeFromFuzzyDataType(OWLOntology ontology, IRI dataType) {
        for (OWLAxiom ax : ontology.getAxioms(AxiomType.DATATYPE_DEFINITION)) {
            for (OWLDatatype dt : ax.getDatatypesInSignature()) {
                OWLAnnotationAssertionAxiom s1 = getAnnotationAx(ontology, dt);
                if (s1 != null) {
                    String st = s1.getSubject().toString();
                    System.out.println(st);
                    if (s1.toString().contains("#fuzzyLabel") && st.equals(dataType.toString())) {

                    }
                }
            }
        }
    }

    public static FuzzyConcreteConcept parseFuzzyFunction(Object ob1) {
        if ((ob1 instanceof TriangularFunction)) {
            TriangularFunction t1 = (TriangularFunction) ob1;
            double a = t1.getA();
            double b = t1.getB();
            double c = t1.getC();
            return new TriangularConcreteConcept(a, b, c);
        } else if ((ob1 instanceof LeftShoulderFunction)) {
            LeftShoulderFunction t1 = (LeftShoulderFunction) ob1;
            double a = t1.getA();
            double b = t1.getB();
            return new LeftConcreteConcept(a, b);
        } else if ((ob1 instanceof RightShoulderFunction)) {
            RightShoulderFunction t1 = (RightShoulderFunction) ob1;
            double a = t1.getA();
            double b = t1.getB();
            return new RightConcreteConcept(a, b);
        } else if ((ob1 instanceof TrapezoidalFunction)) {
            TrapezoidalFunction t1 = (TrapezoidalFunction) ob1;
            double a = t1.getA();
            double b = t1.getB();
            double c = t1.getC();
            double d = t1.getD();
            return new TrapezoidalConcreteConcept(a, b, c, d);
        } else {
            return null;
        }
    }
}


