package com.amazefx.MazeDisplay;

import com.amazefx.MazeGen.PassageTree;
import com.amazefx.Pathfinding.Astar;


import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.function.Consumer;

import static com.amazefx.MazeDisplay.Cell.Type.PATH;
import static com.amazefx.MazeDisplay.Cell.Type.WALL;
import static java.lang.Integer.parseInt;

/**
 * makes the imaginary maze that is based off the "real" maze
 * this class includes methods for creating, managing and extracting the information about the iMaze
 */
public class iMaze {

    private final int heightInCells;

    private final int widthInCells;

    private final Cell[][] mazeAsGrid;

    private final int cellSize = 50;

    private boolean isSolved = false;
    

    /*----constructors---- */
    /**
     *
     * make maze of desired height and width
     * must have a grater area than 10
     * uses the fillGrid method
     * contains custom exception
     *
     * @param height height of the iMaze
     * @param width width of the iMaze
     */
    private iMaze(int heightInCells, int widthInCells){
        if(heightInCells < 10 || widthInCells < 10){
            throw new IllegalArgumentException("height and width must be at least 3");
        }else if(heightInCells> 1000 || widthInCells > 1000){
            throw new IllegalArgumentException("height and width are permitted to be no grater than 80");
        }

        this.heightInCells = heightInCells;
        this.widthInCells = widthInCells;
        mazeAsGrid = new Cell[heightInCells][widthInCells];
        fillMazeGrid();
    }
    /**
     * creates a maze of set height and width
     * and takes in a grid of cells which are either walls or paths
     * this is not used by the user
     * @param heightInCells
     * @param widthInCells
     * @param mazeAsGrid a given maze in grid format
     */
    private iMaze(int heightInCells,int widthInCells,Cell[][] mazeAsGrid){
            this.heightInCells = heightInCells;
            this.widthInCells = widthInCells;
            this.mazeAsGrid = mazeAsGrid;
        }
    /**
     * Maze will have the same height and width
     *
     * @param size
     */
    public iMaze(int size){

        this(size,size);
    }

    /*---Getters---*/
    /**
     * gets the index of the passage in the last row
     * @return the index of the passage in the last row
     */
    private int getGoalCol(){
        return widthInCells -3 + widthInCells % 2;
    }

    /**
     * start and goal are always the same
     * however the path to getting to them is different
     *
     */
    private Cell getStartCell(){
        return mazeAsGrid[0][1];

    }

    /**
     * uses getGoalCol method
     * @return goal cell
     */
    private Cell getGoalCell(){
        return mazeAsGrid[heightInCells - 1][getGoalCol()];
    }



    /*---Placers---*/
    /**
     * makes a cell from given coordinates
     *
     * @param row
     * @param column
     * @param type
     */
    private void placeCell(int row, int column, Cell.Type cellType) {
        mazeAsGrid[row][column] = new Cell(row, column, cellType);
    }

     /**
     * places cell in corresponding coordinates
     *
     * @return lambda to put a cell
     */
    private Consumer<Cell> placeCell() {
        return cell -> mazeAsGrid[cell.getRow()][cell.getCol()] = cell;
    }

    /*---Fillers---*/
    /**
     * fills every other cell with a path type and others with wall type
     * uses putCell method
     */
    private void fillAlternately(){
        for(int r =0; r< heightInCells; r++){
            for(int c = 0; c< widthInCells; c++){
                if((r & 1)== 0 || (c & 1) == 0){
                    placeCell(r, c, WALL);
                }else{
                    placeCell(r, c, PATH);
                }
            }
        }
    }

    /**
     * if the maze is even it is required to fill the last row or col
     * with walls if this is not done the solution would always be to go to the "outside walls"
     * uses wallLastRow and wallLastColumn methods
     */
    private void fillGaps(){
        if (heightInCells % 2 == 0) wallLastRow();
        if (widthInCells % 2 == 0) wallLastCol();
    }

    /**
     * sets all cells in the last row to type wall
     */
    private void wallLastRow(){
        for(int c = 0; c < widthInCells; c++){
            placeCell(heightInCells -1, c, WALL);
        }
    }

    /**
     * sets all cells in the last column to type wall
     */
    private void wallLastCol() {
        for (int r = 0; r < heightInCells; r++)
            placeCell(r, widthInCells - 1, WALL);
    }


    /*--- Generation---*/
    /**
     * puts start and goal to the opposite corners of the maze
     * uses the getExitColumn() method
     */
    private void makeStartAndGoal(){
        placeCell(0,1,PATH);
        placeCell(heightInCells -1 , getGoalCol(), PATH);
        if(heightInCells % 2 == 0){
            placeCell(heightInCells - 2 , getGoalCol(), PATH);
        }
    }

    /**
     * places random cells of type Path between the static cells(start and goal)
     *
     */
    private void generatePaths(){
        new PassageTree(heightInCells, widthInCells).generate().forEach(placeCell());
    }

    /**
     * finds a path in the maze from its start to the goal
     * uses A star class
     * uses getEntrance and getExit methods
     */
    public void findGoalGUI(Group board){
        if(!isSolved){
            new Astar(mazeAsGrid, getStartCell(), getGoalCell()).findPath().forEach(placeCell());
            isSolved = true;
        }
        generateMaze(isSolved,board);
    }
    /**
         * fills cells of the newly created maze so that
         * the maze becomes connected
         * uses fillAlternately, fillGaps,makeEntranceAndExit,generatePassages methods
         *
         */
        private void fillMazeGrid(){
            fillAlternately();
            fillGaps();
            makeStartAndGoal();
            generatePaths();
        }

    

    
    public void generateMaze( boolean showSolved, Group board){
        for(int r= 0; r < mazeAsGrid.length; r++){
            for(int c =0 ; c< mazeAsGrid[r].length; c++){
                Rectangle cell = new Rectangle();
    
                if(mazeAsGrid[r][c].isWall()){
                    cell = new Rectangle(r * cellSize, c * cellSize, cellSize, cellSize);
                    cell.setFill(Color.BLACK);
                }else if(showSolved && mazeAsGrid[r][c].isGoal()){
                    
                    cell = new Rectangle(r * cellSize, c * cellSize, cellSize, cellSize);
                    cell.setFill(Color.GREEN);
                    
                }
                
                board.getChildren().add(cell);
            }
        }
    }
    

    /**
     * Parses a serialized maze representation and
     * constructs a new maze from it
     * 1 = wall
     * 0 = path
     * NOTE: The escape path is not serialized
     * @param str name of the maze file to copy
     * @return a new maze from a given string
     */

     public static iMaze loadSavedMaze(String str){
        try{
            String[] whole = str.split("\n");
            String[] size = whole[0].split(" ");
            int height = parseInt(size[0]);
            int width = parseInt(size[1]);
            Cell[][] grid = new Cell[height][width];

            for( int r =0; r< height; r++){
                String[] row = whole[r+ 1].split(" ");
                for(int c =0; c< width; c++){
                    grid[r][c] = new Cell(r,c,OneOrZeroToCellType(parseInt(row[c])));
                }
            }
            return new iMaze(height,width,grid);

        }catch(Exception e){
            throw new IllegalArgumentException("Cannot load maze");
        }
    }

/*---Conversion---*/

        /**
         * converts 1 to wall and 0 to path
         * since path is not serialized there are 2 options
         *
         * @param val val to convert
         * @return conversion
         */
        private static Cell.Type OneOrZeroToCellType(int val) {
            return val == 1 ? WALL : PATH;
        }
        /**
         * used to save a maze
         * @return string of ones and zeros
         */
        public String exportMaze(){
            StringBuilder sb = new StringBuilder();
            sb.append(heightInCells).append(" ").append(widthInCells).append("\n");
            for(Cell[] row: mazeAsGrid){
                for(Cell cell: row){
                    sb.append(cellTypeToOneOrZero(cell)).append(" ");
                }
                sb.append("\n");
            }
            return sb.toString();
        }
       

        /**
         * Converts wall to 1 and 0 to path
         */
        private int cellTypeToOneOrZero(Cell cell){
            return cell.isWall() ? 1 : 0;
        }




}
