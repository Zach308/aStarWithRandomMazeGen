package com.amazefx.MazeGen;
/*
 * A sequence of primitive int-valued elements supporting
 * sequential and parallel aggregate operations
 */
import static java.util.stream.IntStream.range;
/**
 * representatives for disjoint sets.
 * if set contains only one element
 * parent = self
 * else parent = nextItemInTree
 * sudo code for a idea/bases for this implementation can be found at:
 * https://en.wikipedia.org/wiki/Disjoint-set_data_structure
 */
public class DisjointSet {

    private int[] parent;

    /**
     * rank = hight in the tree according to subsets
     * if subset has one item then its rank is 0
     */
    private int[] rank;

    /**
     * number of subsets
     */
    private int size;

    /**
     * constructs a disjointed set subsets dictated by size
     * uses the makeSet() method
     * @param size
     */
    public DisjointSet(int size){
        this.size = size;
        parent = new int[size];
        rank = new int[size];
        range(0, size).forEach(this::makeSet);//int stream
    }

    /**
     * creates a set
     * @param i is the set to be initialized
     */
    private void makeSet(int i){
        parent[i] = i;
        rank[i] = 0;//height is 0
    }

    /**
     * gets the subset amount
     */
    public int getSize(){
        return size;
    }

    /**
     * finds representative for the set
     * if set contains only one element
     * parent = self
     *
     * @param i set
     * @return representative pointer for the selected set
     */
    public int find(int i){
        if(i != parent[i]){
            parent[i] = find(parent[i]);
        }
        return parent[i];
    }

    /**
     * connects two sets forming one set
     * only if they are the same set
     *
     * @param i first set
     * @param j second set
     * @return true if sets can be connected false if they are the same set
     */
    public boolean union(int iMerge, int jMerge){
        int iRoot = find(iMerge);
        int jRoot = find(jMerge);

        if(iRoot == jRoot){
            return false;
        }
        if(rank[iRoot] < rank[jRoot]){
            parent[iRoot] = jRoot;
        }else{
            parent[jRoot] = iRoot;
            if(rank[iRoot] == rank[iRoot]){
                rank[iRoot]++;
            }
        }
        size--;
        return true;
    }
}
