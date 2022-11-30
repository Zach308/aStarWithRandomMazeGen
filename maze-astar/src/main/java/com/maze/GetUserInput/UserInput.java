package com.maze.GetUserInput;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.maze.MazeDisplay.iMaze;
/**
 * reads an input and prints the output to the console.
 */
public class UserInput {

    private Scanner sc;

    private iMaze maze;

    private boolean isMazeAvailable = false;


    /**
     * options for game
     * is in an infinite loop
     */
    public void start() {
        sc = new Scanner(System.in);
        while (true) {
            help();
            try {
                int choice = sc.nextInt();
                sc.nextLine();
                switch (choice) {
                    case 0:
                        exit();
                        return;
                    case 1:
                        genMaze();
                        break;
                    case 2:
                        load();
                        break;
                    case 3:
                        save();
                        break;
                    case 4:
                        display();
                        break;
                    case 5:
                        findEscape();
                        break;
                    default:
                        System.out.println("Incorrect option. Please try again");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Incorrect option. Please try again");
            } catch (IllegalArgumentException e){
                System.out.println("Cannot generate a maze. Invalid size \nMaze size should be greater than 3 and no bigger than 80");
            }catch (Exception e) {
                System.out.println("Unknown error");
                e.printStackTrace();
            }
        }
    }

    private void help() {
        System.out.println("1. Generate a new maze");
        System.out.println("2. Load a maze");
        if (isMazeAvailable) {
            System.out.println("3. Save the maze");
            System.out.println("4. Display the maze");
            System.out.println("5. Find the escape");
        }
        System.out.println("0. Exit");
    }

    private void exit() {
        sc.close();
        System.out.println("GOODBYE!");
        System.exit(0);
    }

    private void genMaze(){
        System.out.println("Enter the size of the new maze(use one integer)");
        int size = sc.nextInt();
        maze = new iMaze(size);
        isMazeAvailable = true;
        display();
    }

    private void load() {
        System.out.println("Enter the filename");
        String filename = sc.nextLine();
        try {
            String content = Files.readString(Paths.get(filename));
            maze = iMaze.loadSavedMaze(content);
            isMazeAvailable = true;
            System.out.println("The maze is loaded");
        } catch (IOException e) {
            System.out.println("The file " + filename + " does not exist");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void save() {
        System.out.println("Enter the filename");
        String filename = sc.nextLine();

        try {

            String export = maze.exportMaze();
            Files.write(Paths.get(filename), export.getBytes());
            System.out.println("The maze is saved");

        } catch (IOException e) {
            System.out.println("Cannot write to file " + filename);
        }
    }


    private void display() {
        System.out.println(maze);//auto calls toString method

    }
    /**
     * uses A star to find goal
     * */
    private void findEscape() {
        System.out.println(maze.findGoal());

    }




}
