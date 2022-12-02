package com.amazefx.GUI;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

import com.amazefx.MazeDisplay.iMaze;

/**
 * JavaFX App
 */
public class MainGUI extends Application {

	@FXML TextField mazeSizTextField;
    @FXML Button genMazeButton;
    @FXML Button showMazeButton;
    private static Scene scene;
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