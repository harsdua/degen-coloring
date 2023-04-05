# degen-coloring
An algorithm to color a graph with k-colors, where k is it's degeneracy, in linear time. 

Read the report for more detailed information

How to compile, execute and test my code, on linux: 

You need java-jdk17 installed before hand

Let wd be the current directory of this file

open terminal and cd to wd/src/algoproj 

type in the following command : "javac Algorithms.java Main.java UndirectedGraph.java"

cd to wd/src

type in the following command : "java algoproj.Main"

You will be prompted to type the path of the file containing the edgelist
Type in the delimiter with no quotes. eg: \t for tab delimited
type y if you want to find the degeneration
type y if you want to find the proper coloring of the graph, which will be saved as a csv file
type vertexID that is not negative to find the kcore.
