package algoproj;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Algorithms {


    //Pop element from set
    static int getElementFromSet(Set<Integer> vSet) {
        int v1 = -1;
        for (Integer v : vSet) {
            v1 = v;
            break;
        }
        return v1;
    }




    //Changes removedVertices such that subgraph H = G-removedVertices has a minimum degree of atleast k.
    public static boolean findKCore(UndirectedGraph G,
                                    LinkedHashSet<Integer> removedVertices,
                                    HashMap<Integer, Integer> vdMap,
                                    HashMap<Integer, HashSet<Integer>> dvMap,
                                    TreeSet<Integer> dvKeys,
                                    int k,
                                    int targetVertex){

        /*
        Returns true if stopAtVertex was provided and was removed
        Else, returns false.
         */
        //While (minDegOfGraph>=k or all vertices have been considered "removed")
        while(removedVertices.size()!=G.vertexCount()){
            int minDegOfGraph = dvKeys.first();
            if (minDegOfGraph>=k) break;
            //Get some vertex that has the min degree of graph
            int someV = getElementFromSet(dvMap.get(minDegOfGraph));
            //remove vertex and update maps
            removedVertices.add(someV);
            vdMap.put(someV,0);
            dvMap.get(minDegOfGraph).remove(someV);


            //delete entry if empty
            if (dvMap.get(minDegOfGraph).isEmpty()){
                dvMap.remove(minDegOfGraph);
                dvKeys.remove(minDegOfGraph);
            }

            //for each neighbor
            for (int neighbor: G.adjList.get(someV)) {
                if (removedVertices.contains(neighbor)) {
                    //if neighbor is removed already so we dont need to do anything further
                    continue;
                }
                //update maps for each neighbor, since they lost an edge with someV
                int neighborDegree = vdMap.get(neighbor);
                vdMap.put(neighbor, neighborDegree - 1);
                dvMap.get(neighborDegree).remove(neighbor);
                if (dvMap.get(neighborDegree).isEmpty()) {
                    dvMap.remove(neighborDegree);
                    dvKeys.remove(neighborDegree);

                }
                //if key doesnt exist, insert it.
                if(dvMap.get(neighborDegree-1)==null){
                    dvKeys.add(neighborDegree-1);
                    dvMap.put(neighborDegree-1,new HashSet<>());

                }
//                dvMap.computeIfAbsent(neighborDegree - 1, k1 -> new HashSet<>());

                dvMap.get(neighborDegree - 1).add(neighbor);
            }
            //If someV was targetVertex for findCoreNumber, return true
            if(someV==targetVertex){
                return true;
            }
        }
        return false;
    }

    //Overloaded function to use findKCore without target vertex
    public static boolean findKCore(UndirectedGraph G,
                                    LinkedHashSet<Integer> removedVertices,
                                    HashMap<Integer, Integer> vdMap,
                                    HashMap<Integer, HashSet<Integer>> dvMap,
                                    TreeSet<Integer> dvKeys,
                                    int k
    ) {
        return findKCore(G, removedVertices, vdMap, dvMap, dvKeys, k, -9999);
    }


    public static int degenerate(UndirectedGraph G, LinkedHashSet<Integer> removedVertices){

        TreeSet<Integer> dvKeys = new TreeSet<>();
        //map of vertex:degree
        HashMap<Integer, Integer> vdMap = new HashMap<Integer, Integer>();
        //map of degree:set of vertices with coressponding degree
        HashMap<Integer, HashSet<Integer>> dvMap = new  HashMap<Integer, HashSet<Integer>>();
        //Assign the vertices and their degrees to both maps
        for (int vertexID: G.getVertexSet()) {
            int vertexDeg = G.degree(vertexID);
            dvMap.computeIfAbsent(vertexDeg, k1->new HashSet<>());
            dvMap.get(vertexDeg).add(vertexID);
            vdMap.put(vertexID,vertexDeg);
        }
        dvKeys.addAll(dvMap.keySet());

        //Find kCores, one by one
        int k = 0;
        do {
            k += 1;
            findKCore(G,removedVertices,vdMap,dvMap,dvKeys,k);
        } while (removedVertices.size() != G.vertexCount());
        //Last k value produces a null graph H = G-removedVertices, therefore degeneracy is one less than k
        return k-1;
    }

    public static int findDegeneracy(UndirectedGraph G){
        //Wrapper function to return degeneracy of a graph
        return degenerate(G, new LinkedHashSet<Integer>());
    }


    public static LinkedHashSet<Integer> findDegenOrdering(UndirectedGraph G, AtomicInteger k){
        /*Same function as findDegeneracy but returns removedVertices, instead of K.
        Since java doesnt support multiple return values, and the k value is needed,
        k is passed by parameter and is an AtomicInteger because it is mutable, so it can be changed within the function.
        */
        LinkedHashSet<Integer> removedVertices = new LinkedHashSet<Integer>();
        k.set(degenerate(G, removedVertices));
        return removedVertices;
    }

    public static void greedyColoring(UndirectedGraph G, List<Integer> ordering, int k, HashMap<Integer, Integer> vertexToColorMap){
        //Colors graph using the greedy method in order given by ordering.
        int maxColor = 0;
        for (int vertex: ordering) {
            HashSet<Integer> colorsUsedByNeighbors = new HashSet<>();// to track colors of neighbors
            //Rule out colors used up
            for (int neighbor : G.adjList.get(vertex)) {
                Integer neighborColor = vertexToColorMap.get(neighbor);
                if (neighborColor == null) {
                    continue;
                }
                colorsUsedByNeighbors.add(neighborColor);
            }
            //cycle through each color possible. Mathematically, with a degeneracy ordering, it should not exceed k+1.
            for (int i = 0; i < k + 1; i++) {
                //If neighbors did not use color i, use i to color vertex and exit loop
                if (!colorsUsedByNeighbors.contains(i)) {
                    vertexToColorMap.put(vertex, i);
                    maxColor = Integer.max(maxColor,i);
                    break;
                }
            }
        }
        System.out.println("Colors used: "+maxColor);
    }



    public static HashMap<Integer,Integer> colorGraph(UndirectedGraph G){
        AtomicInteger k = new AtomicInteger(0);
        //retrieve degenOrdering
        LinkedHashSet<Integer> degenOrdering = findDegenOrdering(G, k);
        //A colorMap. VertexID as keys, and their respective color that is assigned to them as the values.
        HashMap<Integer, Integer> vertexToColorMap = new HashMap<>();
        //degenOrdering is ordered first to last removal, but we need last to first. So we reverse
        List<Integer> reverseDegenOrder = new ArrayList<>(degenOrdering);
        Collections.reverse(reverseDegenOrder);
        //Color each vertex, in order of degenOrdering
        greedyColoring(G,reverseDegenOrder, k.intValue(), vertexToColorMap);
        return vertexToColorMap;
    }

    public static int findCoreNumber(UndirectedGraph G, int targetVertex) {
        /*Almost the same as findDegeneracy
        Only difference is when targetVertex is removed, the max degree at the time of removal for a vertex is the k-core.
         */
        TreeSet<Integer> dvKeys = new TreeSet<>();
        //Set of vertices that will be considered removed
        LinkedHashSet<Integer> removedVertices = new LinkedHashSet<Integer>();
        //map of vertex:degree
        HashMap<Integer, Integer> vdMap = new HashMap<Integer, Integer>();
        //map of degree:set of vertices with coressponding degree
        HashMap<Integer, HashSet<Integer>> dvMap = new HashMap<Integer, HashSet<Integer>>();
        //Assign the vertices and their degrees to both maps
        for (int vertexID : G.getVertexSet()) {
            int vertexDeg = G.degree(vertexID);
            dvMap.computeIfAbsent(vertexDeg, k1 -> new HashSet<>());
            dvMap.get(vertexDeg).add(vertexID);
            vdMap.put(vertexID, vertexDeg);
        }
        dvKeys.addAll(dvMap.keySet());

        int k = 0;
       do {
            k+=1;
            //findKCore returns true if target gets removed
            boolean removedTarget = findKCore(G, removedVertices, vdMap, dvMap,dvKeys, k, targetVertex);
            //stop if removed target
            if(removedTarget){
                return k-1;
            }
        } while (true);
    }
}
