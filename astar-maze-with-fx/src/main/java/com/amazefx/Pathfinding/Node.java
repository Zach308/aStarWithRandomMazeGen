package com.amazefx.Pathfinding;

import java.util.Objects;
/**
 * a node contains a cost of the path,
 * from the start to the node itself,
 * and the estimated cost of the path from
 * itself to the end node and calculates
 * the final cost through it
 * see the variable declarations below for
 * A star algorithm function variable clarifications
 * https://en.wikipedia.org/wiki/Taxicab_geometry
 *
 *
 */
public class Node {

    //cost of moving
    private static final int edgeCost = 1;

    //current row the node is located in
    private final int row;

    //current column the node is located in
    private final int col;

    //wall
    private boolean isWall;


    /**
     * parent node is saved to remake a path
     * if A star goes through this node.
     */
    private Node parent;

    //cost of the path from the start node to this node
    private int g;

    // estimated cost of the path from this node to the end node
    private int h;

    //final cost of the path from the start node to the end node through this node.
    private int f;


    /**
     * creates a node
     * parent will be set to itself
     *
     * @param row;
     * @param col
     * @param isWall
     */
    public Node(int row, int col, boolean isWall){
        this.row = row;
        this.col = col;
        this.isWall = isWall;
        parent = this;//the current node
    }
    /*---Getters---*/
    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }

    public Node getParent(){
        return parent;
    }

    public int getFinalCost(){
        return f;
    }

    public boolean isWall(){
        return isWall;
    }
    /**
     * Calculates the estimated cost of
     * the path from itself node to the end node
     * @param node
     */
    public void calcHeuristic(Node node){
        this.h = Math.abs(node.row - this.row)+ Math.abs(node.col -this.col);
    }

    /**
     * checks if the path through current node is
     * more optimal than the current path
     *
     * @param node to compare
     * @return true if the path is more optimal than the current
     */
    public boolean hasBetterPathThan(Node node){
        return node.g + edgeCost < this.g;
    }

    /**
     * Updates the path
     * current node will become the parent
     * recalculates the final cost using the current node
     *
     * @param node
     */
    public void updatePath(Node node){
        this.parent = node;
        this.g = node.g + edgeCost;
        f = g + h;
    }

    /*---Overridden Methods---*/
    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return row == node.row && col == node.col && isWall == node.isWall;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col, isWall);
    }





}
