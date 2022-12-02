package com.amazefx.Pathfinding;

//import maze.model.Cell;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import com.amazefx.MazeDisplay.Cell;

import static com.amazefx.MazeDisplay.Cell.Type.GOAL;
import static java.util.Comparator.comparingInt;
/**
 * Astar class is used for finding the exit of the maze
 * This class uses HashSet, priority queue and sets for node organization
 * These sources are helpful for understanding A star and heuristic path finding:
 *
 * https://en.wikipedia.org/wiki/A*_search_algorithm
 * https://en.wikipedia.org/wiki/Taxicab_geometry
 * https://brilliant.org/wiki/a-star-search/
 * https://stackabuse.com/graphs-in-java-a-star-algorithm/
 *
 * the a star algorithm uses the following function
 * f(n) = g(n)+ h(n)
 * to find the less expensive paths
 * @see node for more information about the A star function variables
 *
 *
 **/

public class Astar{
    /*
    * moves up,left,right and down of the current cell
    * is a 2d array of ints because we cannot move half a step
    * and this allows us to move in all directions allowed by a 2 dimensional space
    */

    private static final int[][] cardinalDirections = {{-1, 0}, {0, -1}, {0, 1}, {1, 0}};

    /**
    * NOTE: all measurements values are of type node
    */

    private int gridHeight;

    private int gridWidth;

    /*
    * mazes are two dimensional so we need a 2d array to represent the maze
    * the maze is made up of nodes so this will be of data type Node
    */
    private Node[][] mazeAsGrid;

    /*
     * the start and goal must be of type node
     */

    //comes from
    private Node startNode;

    //ends at
    private Node goalNode;

    /*
    * These are similar to array list
    * These are all of data type Node
    * priority queue to perform the selection of minimum
    * this part is used to
    * estimate the cost node on every step of the algorithm
    * the first  of this queue is the least element with respect to the specified ordering.
    *
    */
    private PriorityQueue<Node> unProcessedNodes = new PriorityQueue<>(comparingInt(Node::getFinalCost));

    /*
     * Nodes that have already been processed
     *
     */
    private Set<Node> processedNodes = new HashSet<>();

    /**
    * makes a new object with a given grid of nodes
    * also creates start and end nodes
    * the rest of the nodes are based on the end and start nodes
    * @param grid
    * @param startNode
    * @param goalNode
    */
    public Astar(Cell[][] mazeAsGrid, Cell startNode, Cell goalNode){
        this.gridHeight = mazeAsGrid.length;//rows
        this.gridWidth = mazeAsGrid[0].length;//cols
        this.mazeAsGrid = new Node[gridHeight][gridWidth];
        this.startNode = new Node(startNode.getRow(),startNode.getCol(),false);
        this.goalNode = new Node(goalNode.getCol(),goalNode.getCol(),false);
        createNodes(mazeAsGrid);



    }
    /**
     * for loop goes through all the cells in a grid
     *
     * for each node in the maze it will create
     * a corresponding node in a grid of nodes
     * calculates an estimated cost from each of the
     * currents nodes to the end goal
     * grid will be a grid of cells
     * @param grid
    */

    private void createNodes(Cell[][] mazeAsGrid){
        for(int r = 0; r < gridHeight; r++){
            for(int c =0; c < gridWidth; c++){
                Node node = new Node(r,c,mazeAsGrid[r][c].isWall());
                node.calcHeuristic(goalNode);
                this.mazeAsGrid[r][c] = node;

            }
        }
    }

    /**
     * using A star algorithm to find the goal
     * return a cell list that starts from the startNode to
     * the endNode
     *@return a cell list that starts from the startNode and ends at the endNode
     */
    public List<Cell> findPath(){
        unProcessedNodes.add(startNode);
        while(!unProcessedNodes.isEmpty()){
            Node currentNode = unProcessedNodes.poll();
            if(isGoal(currentNode)){
                return remakePath(currentNode);
            }
            processedNodes.add(currentNode);
            updateNeighbors(currentNode);
        }
        return new ArrayList<>();
    }


    /**
     *
     */
    private List<Cell> remakePath(Node currentNode){
        LinkedList<Cell> path = new LinkedList<Cell>();
        path.add(toCell(currentNode));
        while(currentNode.getParent() != currentNode){
            Node parent = currentNode.getParent();
            path.addFirst(toCell(parent));
            currentNode = parent;
        }
        return path;

    }

    /**
     * check to see if the current node is the goalNode
     *
     *
     * @param currentNode
     * @return true if the node is
     *
     */
    private boolean isGoal(Node currentNode){
        return currentNode.equals(goalNode);
    }



    /**
     * converts node to cell
     * cell type is path
     *
     * @param node
     * @return converted node to cell
     */
    private Cell toCell(Node node){
        return new Cell(node.getRow(), node.getCol(),GOAL);
    }

    /*
     *
     * Updates the estimated and final cost of the cells
     * using the A star algorithm guidelines
     */
    private void updateNeighbors(Node currentNode){
        for(int[] cardinalD: cardinalDirections ){
            int row = currentNode.getRow() + cardinalD[0];
            int col = currentNode.getCol() + cardinalD[1];
            if(inBounds(row,col)){
                Node node = mazeAsGrid[row][col];
                if(!node.isWall() && !processedNodes.contains(node)){{
                    if(unProcessedNodes.contains(node)){
                        if(node.hasBetterPathThan(currentNode)){
                            unProcessedNodes.remove(node);
                        }else{
                            //end the current iteration in a for loop and continues to the next iteration.
                            continue;
                        }
                    }
                    node.updatePath(currentNode);
                    unProcessedNodes.add(node);
                }
            }
        }
    }
}
    /**
     * checks if the cell is in bounds of the 2d array
     *
     * @param row
     * @param col
     * @return true if the cell is in bounds of the 2d array
     */
    private boolean inBounds(int row, int col) {
        return row >= 0 && row < gridHeight && col >= 0 && col < gridWidth;
    }



}
