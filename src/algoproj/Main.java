package algoproj;
import java.util.*;
import java.io.*;

public class Main {
    private static Scanner sc = new Scanner(System.in);  // Create a Scanner object

    public static void degenPrompt(UndirectedGraph G){
        System.out.println("Would you like to find the degeneracy of the graph?(type y if so :)");
        String ans = sc.nextLine();
        if(ans.equals("y")){
            System.out.println("Finding degeneracy...");
            long startTime = System.currentTimeMillis();
            int degen = Algorithms.findDegeneracy(G);
            long endTime = System.currentTimeMillis();
            System.out.println("The degeneracy of the graph is : " +degen);
            System.out.println("Time taken: " + ((endTime - startTime)) + " milliseconds");
        }
    }

    public static void colorPrompt(UndirectedGraph G, String filename){
        System.out.println("Would you like to find a proper coloring using at most k+1 colors, where k is the degeneracy?(type y if so :)");
        String ans = sc.nextLine();
        if(ans.equals("y")) {
            System.out.println("Coloring graph...");
            long startTime = System.currentTimeMillis();
            HashMap<Integer, Integer> colorMap = Algorithms.colorGraph(G);
            long endTime = System.currentTimeMillis();
            System.out.println("Time taken: " + ((endTime - startTime)) + " milliseconds");
            System.out.println("Map will be saved at: " +filename +"_colorMap.csv");
            System.out.println("The map will be a .csv of two columns, where column 1 is the vertex, and column 2 is the color.");
            String eol = System.getProperty("line.separator");


            try (Writer writer = new FileWriter(filename.concat("_colormap.csv"))) {
                for (Integer vertex: colorMap.keySet()) {
                    writer.append(vertex.toString());
                    writer.append(',');
                    writer.append(colorMap.get(vertex).toString());
                    writer.append(eol);
                }
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        }
    }

    public static void coreNumberPrompt(UndirectedGraph G){
        System.out.println("If you would like to find the core number of a vertex, enter the vertexID. Else, enter a negative number");
        String target = sc.nextLine();
        try{
            Integer targetVertex = Integer.parseInt(target);
            System.out.println("Finding core number...");
            long startTime = System.currentTimeMillis();
            int answer = Algorithms.findCoreNumber(G,targetVertex);
            long endTime = System.currentTimeMillis();
            System.out.println("Time taken: " + ((endTime - startTime)) + " milliseconds");
            System.out.println("The core number of vertex " +target +" is : " +answer);
        }
        catch (NumberFormatException ex){
            System.out.println("Non integer number input. Ending program.");
        }

    }

    public static void parseFileToGraph(String edgeList, UndirectedGraph G, String delimiter) {
        //scans edgeList of delimiter. If a directed edgeList is provided, graph becomes undirected, and edges are bidirectional
        //
        System.out.println("Parsing file to graph...");
        try
        {
            //the file to be opened for reading
            FileInputStream fis=new FileInputStream(edgeList);
            //file to be scanned
            Scanner fsc=new Scanner(fis);
            //returns true if there is another line to read
            while(fsc.hasNextLine())
            {
                String[] currentLine = fsc.nextLine().split(delimiter);
                //if line does not have exactly two delimited entries
                if (currentLine.length!=2){
                    continue;
                }
                try {
                    int vertexID1 = Integer.parseInt(currentLine[0]);
                    int vertexID2 = Integer.parseInt(currentLine[1]);
                    //if edge is a loop, skip
                    if (vertexID1 == vertexID2) {
                        continue;
                    }
                    //if G doesnt contain VertexID1/2 , add it
                    if(!G.containsV(vertexID1)) {
                        G.addVertex(vertexID1);
                    }
                    if(!G.containsV(vertexID2)){
                        G.addVertex(vertexID2);
                    }
                    G.addEdge(vertexID1, vertexID2);
                }
                catch (NumberFormatException exception){
                    //if line contains non numerical characters, go to next iteration
                    continue;
                }

            }
            fsc.close();
        }
        catch(IOException exception)
        {
            exception.printStackTrace();
        }
        }

    public static void main(String[] args) {

        UndirectedGraph G = new UndirectedGraph();
        System.out.println("Enter path to file containing edge list");
        String filename = sc.nextLine();
        System.out.println("Enter delimiter (without quotes, can be space):");
        String delimiter = sc.nextLine();
        System.out.println(" ");
        parseFileToGraph(filename, G, delimiter);
        System.out.println("Parsed file");
        if (G.adjList.isEmpty()) {
            System.out.println("Edge list was not able to be processed. Its likely the delimiter input was wrong. \n Please relaunch the program");
        } else {
            System.out.println("Graph parsed");
            System.out.println(" ");
            degenPrompt(G);
            System.out.println(" ");
            colorPrompt(G, filename);
            System.out.println(" ");
            coreNumberPrompt(G);
        }
    }
}

