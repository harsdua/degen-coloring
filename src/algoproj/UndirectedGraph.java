package algoproj;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class UndirectedGraph {
//graph with adjList representation that does not allow duplicate edges. Edges are bidirectional.
    public HashMap<Integer,HashSet<Integer>> adjList = new HashMap<>();

    public Set<Integer> getVertexSet(){
        return adjList.keySet();
    }

    public int vertexCount(){
        return getVertexSet().size();
    }


    public boolean addVertex(int vertexID){
        if(adjList.containsKey(vertexID)){
            return false;}
        else{
            adjList.put(vertexID,new HashSet<Integer>());
            return true;
        }
    }


    public boolean addEdge(int v1,int v2){
        if(adjList.containsKey(v1) && adjList.containsKey(v2)){
            adjList.get(v1).add(v2);
            adjList.get(v2).add(v1);
            return true;
        }
        else{
            System.out.println("Error: Both vertices do not exist");
            return false;

        }
    }

    public boolean containsV(int vertex){
        if (adjList.containsKey(vertex))return true;
        else return false;
    }

    public int degree(int vertexID){
        return adjList.get(vertexID).size();
    }
}