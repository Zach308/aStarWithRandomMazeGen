package com.amazefx.GUI;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;



/**
 * JavaFX App
 */
public class MainGUI extends Application {

	@FXML TextField mazeSizTextField;
    @FXML Button genMazeButton;
    @FXML Button showMazeButton;
    
    @Override
	public void start(Stage stage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("mainPage.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}	

	

    

    public static void showGUI() {
        launch();
    }
	
}