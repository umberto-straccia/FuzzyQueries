package gui;

import java.io.IOException;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class GUIApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GUIApplication.class.getResource("/GUI-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        // Cosa rara, revisar
        GUIController guiController = (GUIController) fxmlLoader.getController();
        guiController.setStage(stage);
        
        stage.setTitle("Q2S2");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setWidth(400);
        stage.setHeight(850);
        stage.show();
        
        guiController.init();
        
    }

    public static void main(String[] args) {
        launch();
    }
}