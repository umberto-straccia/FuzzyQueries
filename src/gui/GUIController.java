package gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Optional;

import org.apache.jena.atlas.web.HttpException;
import org.apache.jena.query.QueryException;
import org.controlsfx.control.SearchableComboBox;
import org.controlsfx.control.ToggleSwitch;
import org.semanticweb.owlapi.io.OWLOntologyInputSourceException;
import org.semanticweb.owlapi.model.IRI;

import DataProviders.FuzzyDataProvider;
import DataProviders.KnowledgeGraphDataProvider;
import DataProviders.OntologyDataProvider;
import core.FuzzyQuery;
import core.GDType1;
import core.GDType2;
import core.Quantifier;
import core.QuantifierType;
import core.ZadehType1;
import core.ZadehType2;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;



public class GUIController {
    private Stage stage;

    @FXML
    private VBox knowledgeGraphVBox;
    @FXML
    private VBox ontologyVBox;
    @FXML
    private TextField ontologyPathField;
    @FXML
    private TextField kgPathField;
    @FXML
    private Text validOntologyPathText;
    @FXML
    private Text validKGPathText;


    @FXML
    private Tooltip quantifierTooltip;

    @FXML
    private VBox type2QueryParams;

    @FXML
    private ToggleSwitch isAbsoluteToggleSwitch;

    @FXML
    private ComboBox<String> boxQ;
    @FXML
    private SearchableComboBox<String> boxX;
    @FXML
    private SearchableComboBox<String> boxG;
    @FXML
    private SearchableComboBox<String> boxF;

    @FXML
    private SearchableComboBox<String> boxd;
    @FXML
    private SearchableComboBox<String> boxh;
    @FXML
    private ComboBox<String> boxMethods;


    @FXML
    private VBox resVbox;
    @FXML
    private VBox errVbox;


    @FXML
    private Text resText;
    @FXML
    private Text methodText;
    @FXML
    private Text queryText;
    @FXML
    private Text errText;

    @FXML
    private Text typeQueryText;
    @FXML
    private Text typeQuerySubtext;


    private FuzzyDataProvider fuzzyDataProvider;
    private ObservableList<String> supportedMethodsType1;
    private ObservableList<String> supportedMethodsType2;

    public enum DataOrigin {ONTOLOGY, KNOWLEDGE_GRAPH};
    private DataOrigin activeDataOrigin;

    public enum QueryType {TYPE1, TYPE2};
    private QueryType activeQueryType;

    protected void init() {
        createQuantifierTooltip();
        addSupportedMethods();
        setOntologyOrigin();
        setTypeQuery1();
    }

    @FXML
    protected void createQuantifierTooltip(){
        quantifierTooltip.setText(DataHandler.getQuantifierTooltipText());
        quantifierTooltip.setAutoHide(false);
    }

    @FXML
    protected void onStartReasonerClick() {
    	validOntologyPathText.setOpacity(0);
        try {
            if ( activeDataOrigin != DataOrigin.ONTOLOGY) {
                throw new Exception(
                    "Tried to create OntologyDataProvider when ONTOLOGY DataOrigin is not active"
                );
            }
            fuzzyDataProvider = loadOntologyDataProvider(ontologyPathField.getText());
            populateFields();

        } catch (OWLOntologyInputSourceException e) {
            validOntologyPathText.setOpacity(1);
            validOntologyPathText.setText("Ontology file not found");
            e.printStackTrace();
        } catch (Exception e) {
            validOntologyPathText.setOpacity(1);
            validOntologyPathText.setText("Error loading ontology");
            e.printStackTrace();
        }
    }

    @FXML
    protected void onLoadKnowledgeGraph() {
        try {
            org.apache.jena.query.ARQ.init(); // Queries from GUI need to initialize ARQ
            if ( activeDataOrigin != DataOrigin.KNOWLEDGE_GRAPH) {
                throw new Exception(
                    "Tried to create knowledgeGraphDataProvider when KNOWLEDGE_GRAPH DataOrigin is not active"
                );
            }
            KnowledgeGraphDataProvider kgdp = new KnowledgeGraphDataProvider();
            kgdp.setSparqlEndpoint(kgPathField.getText());
//            if (!kgdp.checkURL()) {
//                throw new Exception("Couldn't check the URL or is not valid");
//            };
            fuzzyDataProvider = (FuzzyDataProvider) kgdp;
            populateFields();

        } catch (HttpException e) {
            validKGPathText.setText("URL is not valid or couldn't connect to it");
            validKGPathText.setOpacity(1);
            e.printStackTrace();
        } catch (QueryException e) {
            validKGPathText.setText("Endpoint is not a valid SPARQL endpoint");
            validKGPathText.setOpacity(1);
            e.printStackTrace();
        } catch (Exception e) {
            validKGPathText.setText("Error querying the endpoint");
            validKGPathText.setOpacity(1);
            e.printStackTrace();
        }
    }

    public FuzzyDataProvider loadOntologyDataProvider(String path) throws Exception {

        if ( activeDataOrigin != DataOrigin.ONTOLOGY) {
            throw new Exception(
                "Tried to create OntologyDataProvider when ONTOLOGY DataOrigin is not active"
            );
        }
        OntologyDataProvider odp = new OntologyDataProvider();
        odp.loadOntology(ontologyPathField.getText());

        return (FuzzyDataProvider) odp;
    }

    @FXML
    protected void onSearchOntologyPath(){
        try {

            if ( activeDataOrigin != DataOrigin.ONTOLOGY) {
                throw new Exception(
                    "Tried to create OntologyDataProvider when ONTOLOGY DataOrigin is not active"
                );
            }
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().addAll(
                    new ExtensionFilter("OWL Files", "*.owl"),
                    new ExtensionFilter("All Files", "*.*"));
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                ontologyPathField.setText(selectedFile.getPath());
            }
        } catch (Exception e) {
            validOntologyPathText.setOpacity(1);
            validOntologyPathText.setText("Error selecting file");
            e.printStackTrace();
        }
    }

    public void populateFields(){
        // Quantifier
        ObservableList<String> quantifiers = FXCollections.observableArrayList();
        // TO-DO: Replace standard quantifiers with those from ontologies
        quantifiers.add("FEW");
        quantifiers.add("MOST");
        quantifiers.add("ALMOST_HALF");
        boxQ.setItems(quantifiers);

        // Comoboboxes
        // X crisp Set
        boxX.setItems(DataHandler.getNamesClasses(fuzzyDataProvider)); // X

        ObservableList<String> fuzzyPropsNames = DataHandler.getNamesFuzzyProps(fuzzyDataProvider); 
        ObservableList<String> numericPropsNames = DataHandler.getNamesNumericProps(fuzzyDataProvider); 
        
        // G fuzzy Set
        boxG.setItems(fuzzyPropsNames); // G
        boxd.setItems(numericPropsNames); // d1

        // F fuzzy set
        boxF.setItems(fuzzyPropsNames); // F
        boxh.setItems(numericPropsNames); // d2

    }

    @FXML
    protected  void onImportGClick(){
        errVbox.setOpacity(0);
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Import Fuzzy Labels from file");
            fileChooser.getExtensionFilters().addAll(
                    new ExtensionFilter("OWL Files", "*.owl"),
                    new ExtensionFilter("All Files", "*.*"));
            File selectedFile = fileChooser.showOpenDialog(stage);

            fuzzyDataProvider.importFuzzyDatatypes(selectedFile);
            ObservableList<String> fuzzyPropsNames = DataHandler.getNamesFuzzyProps(fuzzyDataProvider); 
            boxG.setItems(fuzzyPropsNames);
            boxF.setItems(fuzzyPropsNames);

            // If target is .owl file, ask if want to merge ontologies into new file
            if (activeDataOrigin == DataOrigin.ONTOLOGY) {
                askToMergeOntologies((OntologyDataProvider) fuzzyDataProvider, selectedFile);
            }

        } catch (Exception e) {
            e.printStackTrace();
            errText.setText("Error importing fuzzy datatypes");
            errVbox.setOpacity(1);
        }
    }

    @FXML
    protected void onExportDClick(){
        errVbox.setOpacity(0);
        Alert alert_info = new Alert(Alert.AlertType.INFORMATION, "Exporting values...");

        try{

            String x = boxX.getValue();
            String d = boxd.getValue();

            if (x == null) {
                throw new Exception("X Class is not selected");
            }
            if (d == null) {
                throw new Exception("d1 DatatypeProperty is not selected");
            }

            IRI xIRI = createIRIfromInput(x);
            IRI dIRI = createIRIfromInput(d);
            String dFrag = dIRI.getFragment();

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save d1 values");
            fileChooser.setInitialFileName(dFrag + "_values.csv");
            fileChooser.getExtensionFilters().addAll(
                    new ExtensionFilter("CSV Files", "*.csv"),
                    new ExtensionFilter("All Files", "*.*"));
            File selectedFile = fileChooser.showSaveDialog(stage);

            if (selectedFile == null) {
                throw new FileNotFoundException("No es un archivo valido");
            }

            alert_info.show();

            fuzzyDataProvider.exportPropertyValues(xIRI, dIRI, selectedFile);
            Alert alert_completed = new Alert(Alert.AlertType.INFORMATION, "Values exported to " + selectedFile.getAbsolutePath());
            alert_completed.showAndWait();
        } catch (Exception e) {
            Alert alert_error = new Alert(Alert.AlertType.ERROR, "Error exporting d1 values:\n" + e.getMessage());
            alert_error.showAndWait();
            e.printStackTrace();
            errText.setText("Error exporting property values");
            errVbox.setOpacity(1);
        } finally {
            alert_info.close();
        }
    }

    @FXML
    protected void onRunQueryClick() {
        errVbox.setOpacity(0); 
        resVbox.setOpacity(0);

        try {
            String x_String = boxX.getValue();
            String g_String = boxG.getValue();
            String d_String = boxd.getValue();

            String method = boxMethods.getValue();
            String q_String = boxQ.getValue();

           if (x_String == null || g_String == null || d_String == null || method == null || q_String == null) {
                errText.setText("There are empty fields");
                errVbox.setOpacity(1);
                return;
            }

            IRI x = createIRIfromInput(x_String);
            IRI g = createIRIfromInput(g_String);
            IRI d = createIRIfromInput(d_String);
            IRI f = null;
            IRI h = null;
            if (activeQueryType == QueryType.TYPE2) {
                String f_String = boxF.getValue();
                String h_String = boxh.getValue();
                if (f_String == null || h_String == null) {
                    errText.setText("There are empty fields");
                    errVbox.setOpacity(1);
                    return;
                }
                f = createIRIfromInput(f_String);
                h = createIRIfromInput(h_String);
            }

            Quantifier q;
            //boolean absToggle =  isAbsoluteToggleSwitch.isSelected();
        	//System.out.println("ABS : " + absToggle);
            try {
                if (Arrays.stream(Quantifier.StdQuantifier.values()).anyMatch(stdq -> stdq.name().equals(q_String))) {
                	//System.out.println("IF brench : " + q_String);
                	q = new Quantifier(Quantifier.StdQuantifier.valueOf(q_String));
                } else {
                	//System.out.println("ELSE brench : " + isAbsoluteToggleSwitch.isSelected());
                    QuantifierType qType = isAbsoluteToggleSwitch.isSelected() ?  QuantifierType.ABSOLUTE : QuantifierType.RELATIVE;
                    q = DataHandler.parseQuantifier(q_String, qType);
                }
            } catch (Exception e){
                errText.setText("Quantifier is not valid: " + q_String);
                errVbox.setOpacity(1);
                return;
            }
            System.out.println("Quantifier: " + q.toString());
            FuzzyQuery query;

            switch (method) {
                case "Zadeh(Type I)":
                    query = new ZadehType1(fuzzyDataProvider,q,x,g,d);
                    break;
                case "GD(Type I)":
                    query = new GDType1(fuzzyDataProvider,q,x,g,d);
                    break;
                case "Zadeh(Type II)":
                    query = new ZadehType2(fuzzyDataProvider,q,x,g,d,f,h);
                    break;
                case "GD(Type II)":
                    query = new GDType2(fuzzyDataProvider,q,x,g,d,f,h);
                    break;
                default:
                    throw new IllegalArgumentException("No se encuentra el metodo " + method);
            }

            double result = query.run();
            System.out.println("Resultado ::  " + result);
            System.out.println("|-----------------------------|");
            resText.setText(Double.toString(result));
            queryText.setText(query.toString());
            methodText.setText(query.getMethod());
            resVbox.setOpacity(1);
        } catch (Exception e) {
            e.printStackTrace();
            //errText.setText("There was errors running the query:\n" + e.getMessage());
            errText.setText(e.getMessage());
            errVbox.setOpacity(1);
        }
    }

    @FXML
    protected void setTypeQuery1() {
        boxMethods.setItems(supportedMethodsType1);
        typeQueryText.setText("Type I Query");
        typeQuerySubtext.setText("Q of X are G");
        type2QueryParams.setManaged(false);
        type2QueryParams.setVisible(false);
        activeQueryType = QueryType.TYPE1;
    }

    @FXML
    protected void setTypeQuery2() {
        boxMethods.setItems(supportedMethodsType2);
        typeQueryText.setText("Type II Query");
        typeQuerySubtext.setText("Q of F are G");
        type2QueryParams.setManaged(true);
        type2QueryParams.setVisible(true);
        activeQueryType = QueryType.TYPE2;
    }

    @FXML
    protected void setOntologyOrigin() {
        ontologyVBox.setManaged(true);
        ontologyVBox.setVisible(true);
        knowledgeGraphVBox.setManaged(false);
        knowledgeGraphVBox.setVisible(false);
        activeDataOrigin = DataOrigin.ONTOLOGY;
    }

    @FXML
    protected void setKnowledgeGraphOrigin() {
        knowledgeGraphVBox.setManaged(true);
        knowledgeGraphVBox.setVisible(true);
        ontologyVBox.setManaged(false);
        ontologyVBox.setVisible(false);
        activeDataOrigin = DataOrigin.KNOWLEDGE_GRAPH;
    }

    private void addSupportedMethods() {
        supportedMethodsType1 = FXCollections.observableArrayList();
        supportedMethodsType1.add("Zadeh(Type I)");
        supportedMethodsType1.add("GD(Type I)");
        supportedMethodsType2 = FXCollections.observableArrayList();
        supportedMethodsType2.add("Zadeh(Type II)");
        supportedMethodsType2.add("GD(Type II)");
    }

    private void askToMergeOntologies(OntologyDataProvider odp, File importingFile) throws Exception{
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to create a new file with merged ontologies? "
        ,ButtonType.NO, ButtonType.YES);
        Optional<ButtonType> yesButton = alert.showAndWait();
        if (yesButton.isPresent()) {
            if ( yesButton.get() == ButtonType.YES ) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save merged ontology");
                fileChooser.getExtensionFilters().addAll(
                        new ExtensionFilter("OWL Files", "*.owl"),
                        new ExtensionFilter("All Files", "*.*"));
                File newOntFile = fileChooser.showSaveDialog(stage);

                Alert alert_info = new Alert(Alert.AlertType.INFORMATION, "Merging ontologies...");
                alert_info.show();
                Alert alert_success = new Alert(Alert.AlertType.INFORMATION, "Ontologies saved to "+ newOntFile.getAbsolutePath());
                try {
                    DataHandler.mergeOntologies(odp, importingFile, newOntFile);
                    alert_success.showAndWait();
                } catch (Exception e) {
                    Alert alert_error = new Alert(Alert.AlertType.ERROR, "Error merging ontologies:\n" + e.getMessage());
                    alert_error.showAndWait();
                    e.printStackTrace();
                    errText.setText("Error merging ontologies");
                    errVbox.setOpacity(1);
                } finally {
                    alert_info.close();
                }
            }
        }
    }

    private IRI createIRIfromInput(String str) {
        String[] strArray = str.split("\\|");
        if (strArray.length == 1) {
            return IRI.create(strArray[0].trim()); // Some IRIs have no fragment
        } else {
            return IRI.create(strArray[1].trim());
        }
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}