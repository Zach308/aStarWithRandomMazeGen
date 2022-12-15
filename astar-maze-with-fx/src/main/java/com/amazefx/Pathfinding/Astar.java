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
 * ---The rough steps for an Astar algorithm---
 * --Add start node to list
 * --For all the neighboring nodes, find the least cost F node
 * --Switch to the closed list
 *      --For 8 nodes adjacent to the current node
 *      --If the node is not reachable, ignore it. Else
 *      --If the node is not on the open list, move it to the open list and calculate f, g, h.
 *      --If the node is on the open list, check if the path it offers is less than the current path and change to it if it does so.
 * --Stop running when You find the destination
 * 
 * 
 * https://en.wikipedia.org/wiki/A*_search_algorithm
 * https://en.wikipedia.org/wiki/Taxicab_geometry
 * https://brilliant.org/wiki/a-star-search/
 * https://stackabuse.com/graphs-in-java-a-star-algorithm/
 *
 * the a star algorithm uses the following function
 * f(n) = g(n)+ h(n)
 * to find the less expensive paths
 *  
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

    /**
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
                node.getFinalCost();
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
        unProcessedNodes.add(startNode);//add the node that we start on
        while(!unProcessedNodes.isEmpty()){//while priority queue is not empty
            Node currentNode = unProcessedNodes.poll();//get the first node that is in the priority queue
            if(isGoal(currentNode)){//is the current node is the goal 
                return remakePath(currentNode);//back track and remake the path
            }
            processedNodes.add(currentNode);//add the current node to processed node 
            updateNeighbors(currentNode);//set the neighbors to the nodes next to the current node
        }
        return new ArrayList<>();
    }


    /**
     * back tracks from the goal to the start to make the path
     */
    private List<Cell> remakePath(Node currentNode){
        LinkedList<Cell> path = new LinkedList<Cell>();//list of cells
        path.add(toCell(currentNode));//add the current node to the list
        //TODO see if u can use this class to assign a final costto a cell then display it in the gui
        while(currentNode.getParent() != currentNode){//while current node is not self
            Node parent = currentNode.getParent();
            path.addFirst(toCell(parent));//add parent node to the start of the list
            currentNode = parent;// set current node to new parent
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
        for(int[] cardinalD: cardinalDirections ){//we are only allowing for movement in certain directions
            int row = currentNode.getRow() + cardinalD[0];//-1, 0 aka north or up
            int col = currentNode.getCol() + cardinalD[1];//0, -1 aka west or left
            if(inBounds(row,col)){//check to see if the coordinates are in the correct bounds 
                Node node = mazeAsGrid[row][col];
                if(!node.isWall() && !processedNodes.contains(node)){{// if its not a wall and node has not been processed
                    if(unProcessedNodes.contains(node)){//if the node has not been processed
                        if(node.hasBetterPathThan(currentNode)){//if the node has a better f cost 
                            unProcessedNodes.remove(node);//remove the node from the unprocessed nodes 
                        }else{
                            //end the current iteration in a for loop and continues to the next iteration.
                            continue;
                        }
                    }
                    node.updatePath(currentNode);//changes the f cost 
                    unProcessedNodes.add(node);// add the node to the unprocessed
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
