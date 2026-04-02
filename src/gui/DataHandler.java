package gui;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLXMLOntologyFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import DataProviders.FuzzyDataProvider;
import DataProviders.OntologyDataProvider;
import FuzzyFunctions.AroundConcreteConcept;
import FuzzyFunctions.LeftConcreteConcept;
import FuzzyFunctions.RightConcreteConcept;
import FuzzyFunctions.TrapezoidalConcreteConcept;
import FuzzyFunctions.TriangularConcreteConcept;
import core.Quantifier;
import core.QuantifierType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataHandler {
 
    // Quantifier syntax: funcName(arg1,arg2,arg3...))
     public static Quantifier parseQuantifier(String s, QuantifierType qType) throws ParseException {

        String funcName = s.split("\\(")[0];
        String funcArgs = s.split("\\(")[1].split("\\)")[0];
        String[] params = funcArgs.split(",");
        double[] numbers = new double[params.length];


        int i = 0;
        for (String param : params) {
            numbers[i] = Double.parseDouble(param);
            i++;
        }

        Quantifier q;

        funcName = funcName.trim().toLowerCase();

        switch (funcName){
            case "leftshoulder":
            case "left":
            case "l":
                if (numbers.length != 2) throw new ParseException("LeftShoulder must have 2 arguments",1);
                q = new Quantifier(s, new LeftConcreteConcept(numbers[0],numbers[1]),qType);
                break;
            case "rightshoulder":
            case "right":
            case "r":
                if (numbers.length != 2) throw new ParseException("RightShoulder must have 2 arguments",1);
                q = new Quantifier(s, new RightConcreteConcept(numbers[0],numbers[1]),qType);
                break;
            case "triangular":
            case "tri":
                if (numbers.length != 3) throw new ParseException("Triangular must have 3 arguments",1);
                q = new Quantifier(s, new TriangularConcreteConcept(numbers[0],numbers[1],numbers[2]),qType);
                break;
            case "trapezoidal":
            case "tra":
                if (numbers.length != 4) throw new ParseException("Trapezoidal must have 4 arguments",1);
                q = new Quantifier(s, new TrapezoidalConcreteConcept(numbers[0],numbers[1],numbers[2],numbers[3]),qType);
                break;
            default:
                throw new ParseException("Function " + funcName + " not supported", 1); /// Es la excepcion adecuada??
        }

        return q;
    }



    public static Quantifier parseQuantifier_v2(String s,  QuantifierType qType) throws ParseException {
        final Pattern syntaxPattern = Pattern.compile(
            "^(?<description>[^,]+)" +                          // Description before the comma or function name
            "(?<funcName>[a-zA-Z_][a-zA-Z0-9_]*)\\(" +          // Function name
            "(?<args>[^)]*)\\)$"                                // Function arguments
        );
    
        Matcher matcher = syntaxPattern.matcher(s);

        String description = matcher.group("description");
        String funcName = matcher.group("funcName");
        String args = matcher.group("args");

        String[] argsStrings = args.split(",");
        double[] numbers = new double[argsStrings.length];

        int i = 0;
        for (String arg : argsStrings) {
            numbers[i] = Double.parseDouble(arg);
            i++;
        }

        Quantifier q;

        funcName = funcName.trim().toLowerCase();

        switch (funcName){
            case "leftshoulder":
            case "left":
            case "l":
                if (numbers.length != 2) throw new ParseException("LeftShoulder must have 2 arguments",1);
                q = new Quantifier(s, new LeftConcreteConcept(numbers[0],numbers[1]),qType);
                break;
            case "rightshoulder":
            case "right":
            case "r":
                if (numbers.length != 2) throw new ParseException("RightShoulder must have 2 arguments",1);
                q = new Quantifier(s, new RightConcreteConcept(numbers[0],numbers[1]),qType);
                break;
            case "triangular":
            case "tri":
                if (numbers.length != 3) throw new ParseException("Triangular must have 3 arguments",1);
                q = new Quantifier(s, new TriangularConcreteConcept(numbers[0],numbers[1],numbers[2]),qType);
                break;
            case "trapezoidal":
            case "tra":
                if (numbers.length != 4) throw new ParseException("Trapezoidal must have 4 arguments",1);
                q = new Quantifier(s, new TrapezoidalConcreteConcept(numbers[0],numbers[1],numbers[2],numbers[3]),qType);
                break;
            case "around":
                if (numbers.length != 1) throw new ParseException("Around must have 1 argument",1);
                q = new Quantifier(s, new AroundConcreteConcept(numbers[0]),qType);
                break;
            default:
                throw new ParseException("Function " + funcName + " not supported", 1); /// Es la excepcion adecuada??
        }

        return q;
    }

    public static void mergeOntologies(OntologyDataProvider odp, File importingFile, File newFile) throws Exception {
        OWLOntologyManager import_manager = OWLManager.createOWLOntologyManager();
        OWLOntology import_ont = import_manager.loadOntologyFromOntologyDocument(importingFile);
        
        OWLOntologyManager new_manager = OWLManager.createOWLOntologyManager();
        OWLOntology new_ontology = new_manager.createOntology();
        
        new_manager.addAxioms(new_ontology, import_ont.getAxioms());
        new_manager.addAxioms(new_ontology, odp.getOntology().getAxioms());
        
        OWLXMLOntologyFormat owlxmlFormat = new OWLXMLOntologyFormat();
        new_manager.saveOntology(new_ontology,owlxmlFormat,IRI.create(newFile.toURI()));
    }

     static ObservableList<String> getNamesClasses(FuzzyDataProvider fdp) {
        ArrayList<IRI> classesX = fdp.getClasses();
        System.out.println("Clases X: "+ classesX.toString());
        ObservableList<String> namesX = FXCollections.observableArrayList();
        for (IRI classIRI : classesX) {
            namesX.add(parseFragment(classIRI));
        }
        FXCollections.sort(namesX);
        return namesX;
    }

     static ObservableList<String> getNamesFuzzyProps(FuzzyDataProvider fdp) {
        
        ArrayList<IRI> fuzzyProps = fdp.getFuzzyDatatypes();
        System.out.println("Total fuzzy datatypes: " + fuzzyProps.size());
        ObservableList<String> fuzzyPropsNames = FXCollections.observableArrayList();
        for (IRI classIRI : fuzzyProps) {
            fuzzyPropsNames.add(parseFragment(classIRI));
        }
        FXCollections.sort(fuzzyPropsNames);
        return fuzzyPropsNames;
    }

     static ObservableList<String> getNamesNumericProps(FuzzyDataProvider fdp) {

        ArrayList<IRI> numericProps = fdp.getNumericProperties();
        ObservableList<String> numericPropsNames = FXCollections.observableArrayList();
        for (IRI propIRI : numericProps) {
            numericPropsNames.add(parseFragment(propIRI));
        }
        FXCollections.sort(numericPropsNames);
        return numericPropsNames;
    }

    public static String getQuantifierTooltipText() {
        String txt = "Select a Quantifier from the list.\n" +
                 "To create a new one, write the fuzzy function and its parameters inside brackets.\n" +
                 "Available functions:\n" +
                 "Left(a,b)\n" +
                 "Right(a,b,c)\n" +
                 "Triangular(a,b,c)\n" +
                 "Trapezoidal(a,b,c,d1)\n";
        return txt;
    }

    private static String parseFragment(IRI iri) {
        String frag = iri.getFragment();
        if ( frag == null ) {
            return iri.toString();
        } else {
            return ( frag + " | " + iri.toString() );
        }
    }
}
