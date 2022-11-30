package com.maze.MazeGen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.maze.MazeDisplay.Cell;

import static com.maze.MazeDisplay.Cell.Type.PATH;
import static java.util.stream.Collectors.toList;

/**
 * used for creating the solution path
 * This class uses Trees
 * in the class its self it creates a tree of edges in a square
 * for this random maze generation class we will be using,
 * randomized Kruskal's algorithm. see the steps and description of the algorithm can be found in the link below
 *
 * Trees are a collection of nodes (vertices),
 * and they are linked with edges (pointers),
 * representing the hierarchical connections between the nodes
 * A node contains data of any type,
 * but all the nodes must be of the same data type.
 * Trees are similar to graphs,
 * a cycle cannot exist in a tree.
 *
 * --Research Sources--
 * https://en.wikipedia.org/wiki/Component_(graph_theory)
 * https://en.wikipedia.org/wiki/Maze_generation_algorithm#Randomized_Kruskal's_algorithm
 *
 */

public class PassageTree {

    /**
     * the height of the maze
     */
    private int height;

    /**
     * the width of the maze
     */
    private int width;

    /**
     * creates an imaginary edge form of the maze
     *
     * @param height of the original un processed maze
     * @param width of the original un processed maze
     */
    public PassageTree(int height, int width) {
        this.height = (height-1) / 2;
        this.width = (width-1) / 2;
    }

    /**
     * Generates a random list of cells that connect passages
     *
     */
    public List<Cell> generate(){
        List<Edge> edges = createEdges();
        Collections.shuffle(edges);
        List<Edge> tree = createRandSpanningTree(edges);
        return createPaths(tree);
    }

    /**
     * generates a list of all possible edges in a simulated form
     *
     * @return a list of possible edges
     * @see edge for type declaration
     */
    private List<Edge> createEdges(){
        ArrayList<Edge> edges = new ArrayList<>();
        for(int c = 1; c< width; c++){
            edges.add(new Edge(toOneDIndex(0, c),toOneDIndex(0, c-1)));

        }

        for(int r = 1; r< height; r++){
            edges.add(new Edge(toOneDIndex(r,0 ),toOneDIndex(r-1,0)));
        }

        for(int r = 1; r< height; r++){
            for(int col = 1; col< width; col++){
                edges.add(new Edge(toOneDIndex(r,col),toOneDIndex(r, col-1)));
                edges.add(new Edge(toOneDIndex(r,col),toOneDIndex(r-1,col)));
            }
        }

        return edges;

    }

    /**
     * Turns the coordinates in the 2d array to array
     * using the r * w + c formula
     *
     * @param row coordinate in a 2-dimensional array
     * @param col coordinate in a 2-dimensional array
     * @return converted coordinates in 1 dimensional format
     */
    private int toOneDIndex(int row,int col){
        return row * width + col;
    }

    /**
     * makes a list of edges that connect paths
     * https://en.wikipedia.org/wiki/Maze_generation_algorithm#Randomized_Kruskal's_algorithm
     *
     * On each step of the algorithm an edge is added to the list
     * only connects if edge is connected to paths
     *
     * @param edges random list
     * @return random list of edges that connect paths
     */
    private List<Edge> createRandSpanningTree(List<Edge> edges){
        DisjointSet disjointedSet = new DisjointSet(width * height);
        return edges.stream().filter(edge -> connects(edge, disjointedSet)).collect(toList());
    }

    /**
     * check to see if edge connects two unconnected subsets
     *
     * @param edge
     * @param disjointedSet
     * @return true if the edges connect false otherwise
     */
    private boolean connects(Edge edge, DisjointSet disjointedSet){
        return disjointedSet.union(edge.getFirstCell(),edge.getSecondCell());

    }
    /**
     * converts and scales edges to the original form
     * that connects includes the connected path
     *
     * @param spanningTree random list of edges
     * @param list of cells
     */
    private List<Cell> createPaths(List<Edge> spanningTree){
        return spanningTree.stream().map(edge->{
            Cell first = fromIndex(edge.getFirstCell());
            Cell second = fromIndex(edge.getSecondCell());
            return getPath(first, second);
        }).collect(toList());
    }

    /**
     * turns 1d array back to 2d array
     *
     * uses these equations for conversions
     * row = index / width & col = index % width
     */
    private Cell fromIndex(int index){
        int row = index / width;
        int column = index % width;
        return new Cell(row, column, PATH);
    }

    /**
     * scales and the coordinates of the cells of the paths to
     * original scale
     *
     * @param first
     * @param second
     * @return converted path cell
     */

     private Cell getPath(Cell first, Cell second){
        int row = first.getRow() + second.getRow() + 1;
        int col = first.getCol() + second.getCol() + 1;
        return new Cell(row, col, PATH);
     }
}
