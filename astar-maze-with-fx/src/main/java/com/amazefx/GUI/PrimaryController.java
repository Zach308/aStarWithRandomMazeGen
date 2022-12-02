package com.amazefx.GUI;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;

import com.amazefx.MazeDisplay.iMaze;
public class PrimaryController {
    Scanner sc = new Scanner(System.in);
    Random r = new Random();
    int low = 10;
    int high = 70;
    int mazeSize = r.nextInt(high-low) + low;
    @FXML Button genMazeButton;
    @FXML Button showMazeButton;
    @FXML Button solveMazeButton;
    @FXML Button helpButton;
    @FXML Button saveCurrentMazeButton;
    @FXML Button loadMazePreset;
    @FXML ComboBox<Integer> mazeSizes;
    @FXML TextField fileName;
    @FXML TextField loadFileName;
    boolean isMazeAvailable;
    private iMaze maze;
    private final static int margin = 20;
    int size;
    Stage stage = new Stage();
    Group board = new Group();
    Popup popup = new Popup();
    @FXML
    public void initialize() {
    
        mazeSizes.getItems().addAll(10,15,20,25,30,35,40,45,50,55,60,65,70,75);
        mazeSizes.getSelectionModel();
    }

    @FXML 
    private void generateMaze(ActionEvent gen){ 
        
        maze = new iMaze(mazeSizes.getSelectionModel().getSelectedItem());
        saveCurrentMazeButton.setText("Save Current Maze");
    }

    @FXML
    private void genRdMazeButtonAction(ActionEvent gen){
        maze = new iMaze(mazeSize);
        saveCurrentMazeButton.setText("Save Current Maze");

    }

    @FXML
    private void saveCurrentMazeButton(ActionEvent e){
        String name = fileName.getText();
        try {

            String export = maze.exportMaze();
            Files.write(Paths.get(name), export.getBytes());
            File file = new File(name);
            saveCurrentMazeButton.setText("Save Current Maze");
            if(file.exists()){
                saveCurrentMazeButton.setText("The maze is saved");
            }
            
        } catch (IOException ex) {
            System.out.println("Cannot write to file " + name);
        }
    }

    @FXML
    private void loadMazePreset(ActionEvent e){
        
        String name = fileName.getText();
        try {
            String content = Files.readString(Paths.get(name));
            maze = iMaze.loadSavedMaze(content);
            isMazeAvailable = true;
            if(isMazeAvailable){
                loadMazePreset.setText("The maze is loaded");
            }
        } catch (IOException ie) {
            System.out.println("The file " + name + " does not exist");
        } catch (IllegalArgumentException erg) {
            System.out.println(erg.getMessage());
        }
    }
    @FXML
    private void ShowMazeAction(ActionEvent gen) throws IOException{ 
        
        maze.generateMaze(false, board);
        Bounds gameBounds = board.getLayoutBounds();
       
        StackPane root = new StackPane(board);
        
        // Listener to rescale and center the board
        ChangeListener<Number> resize = (ov, v, v1) -> {
            double scale = Math.min((root.getWidth() - margin) / gameBounds.getWidth(), 
                                    (root.getHeight() - margin) / gameBounds.getHeight());
            board.setScaleX(scale);
            board.setScaleY(scale);
            board.setLayoutX((root.getWidth() - gameBounds.getWidth()) / 2d);
            board.setLayoutY((root.getHeight() - gameBounds.getHeight()) / 2d);
        };
        root.widthProperty().addListener(resize);
        root.heightProperty().addListener(resize);

        Scene scene = new Scene(root);
       
        // Maximum size of window
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        double factor = Math.min(visualBounds.getWidth() / (gameBounds.getWidth() + margin),
                visualBounds.getHeight() / (gameBounds.getHeight() + margin));
        stage.setScene(scene);
        stage.setWidth((gameBounds.getWidth() + margin) * factor);
        stage.setHeight((gameBounds.getHeight() + margin) * factor);
        stage.show();
    }
    
    @FXML
    private void solveMaze(ActionEvent gen){
        maze.findGoalGUI(board);
        Bounds gameBounds = board.getLayoutBounds();

        StackPane root = new StackPane(board);

        // Listener to rescale and center the board
        ChangeListener<Number> resize = (ov, v, v1) -> {
            double scale = Math.min((root.getWidth() - margin) / gameBounds.getWidth(), 
                                    (root.getHeight() - margin) / gameBounds.getHeight());
            board.setScaleX(scale);
            board.setScaleY(scale);
            board.setLayoutX((root.getWidth() - gameBounds.getWidth()) / 2d);
            board.setLayoutY((root.getHeight() - gameBounds.getHeight()) / 2d);
        };
        root.widthProperty().addListener(resize);
        root.heightProperty().addListener(resize);

        Scene scene = new Scene(root);
        
        //MazeWindow = new Scene(root);
        // Maximum size of window
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        double factor = Math.min(visualBounds.getWidth() / (gameBounds.getWidth() + margin),
                visualBounds.getHeight() / (gameBounds.getHeight() + margin));
        stage.setScene(scene);
        stage.setWidth((gameBounds.getWidth() + margin) * factor);
        stage.setHeight((gameBounds.getHeight() + margin) * factor);
        stage.show();
    }
        
        
    
}
