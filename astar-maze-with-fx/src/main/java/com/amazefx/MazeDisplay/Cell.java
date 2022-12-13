package com.amazefx.MazeDisplay;
import com.amazefx.Pathfinding.Node;
import java.util.Objects;
 
/**
 *  stores an information about the particular cell in a grid.
 *  uses enums for type identification
 */
public class Cell {


    /**
     * cell can be a path, wall, or goal 
     */
    public enum Type{
        PATH,
        WALL,
        GOAL;
    }

    private final int row;

    private final int col;
   
   
    /**
     * What is the type of the current cell
     */
    private final Type type;
    /*
     * Constructor for Cell
     * cells can only have one type
     * and one set of coordinates(Row and Col)
     */
    public Cell(int row, int col, Type type){
        this.row = row;
        this.col = col;
        this.type = type;
  
    }

    /*---Getters---*/
    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
    

    /**
     * type conformations
     * return true if the cell is of assigned type
     */
    public boolean isPath() {
        return type == Type.PATH;
    }

    public boolean isWall() {
        return type == Type.WALL;
    }

    public boolean isGoal() {
        return type == Type.GOAL;
    }

    /**
     * Overridden methods these are default methods of java util objects class
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return row == cell.row &&
            col == cell.col &&
            type == cell.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col, type);
    }

    @Override
    public String toString() {
        return "Cell{" +
            "row=" + row +
            ", column=" + col +
            ", type=" + type +
            '}';
    }

}
