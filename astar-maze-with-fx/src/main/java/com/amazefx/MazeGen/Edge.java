package com.amazefx.MazeGen;

/**
 * Stores info on an edge in a passage tree
 * stores two coordinates corresponding to the cells locations in a grid.
 * cell locations are as they would be in a 1d array
 * calculated using:  row * width + column
 */
public class Edge {

    private final int firstCell;

    private final int secondCell;

    /**
     * constructs a new edge with the given coordinates
     *
     * @param firstCell the first cell
     * @param secondCell the second cell
     */
    public Edge(int firstCell, int secondCell) {
        this.firstCell = firstCell;
        this.secondCell = secondCell;
    }


    /*
     * getters and setters
     */
    int getFirstCell() {
        return firstCell;
    }

    int getSecondCell() {
        return secondCell;
    }
}
