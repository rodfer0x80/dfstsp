package dfstsp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class TSP_DFS {
	private static long t = 0;
    private static int numCities;
    private static int[][] distance;
    private static boolean[] visited;
    private static int minDistance;
    private static int[] minPath;
    
    /// bootstrap
    public static void solve(String fileName) throws IOException {  	
    	init(fileName);
        
    	readData(fileName);
        
    	startTimer();
        dfs(0, 0, new int[numCities], 1);
        stopTimer();
        
        String path = "";
        System.out.println("Solution for: " + fileName);
        System.out.println("Minimum Distance: " + minDistance);
        for (int city:minPath) {
        	if (path != "") {
        		path += "->" + city;
        	} else {
        		path += city;
        	}
            	
        }
        System.out.println("Minimum Path: " + path);
        printTimer();
    }

    // Init datastructures, read data, parse and perform calculations
    // data from file structure [cityNum, xPos, yPos]\n
    private static void init(String fileName) throws IOException {        
        numCities = countCases(fileName);
        distance = new int[numCities][numCities];
        visited = new boolean[numCities];
        minDistance = Integer.MAX_VALUE;
        minPath = new int[numCities];
    }
    
    // read form cities file and count lines for number of cities
    @SuppressWarnings("null")
	private static int countCases(String fileName) throws IOException {
    	int n = 0;
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line = br.readLine();
        // catch empty line misscount
        while (line != null) {
        	if (line.compareTo("") != 0)
        		n++;
            line = br.readLine();
        }
        br.close();
        return n;
    }
    
    private static void startTimer() {
    	t = System.nanoTime();
    }
    
    private static void stopTimer() {
    	t = System.nanoTime();
    }
    
    private static void printTimer() {
    	long ts = TimeUnit.SECONDS.convert(t, TimeUnit.NANOSECONDS);
    	System.out.println(String.format("Timer: %s ns",ts));
    }

    // read from cities file and parse data [cityNum, xPos, yPos]
    private static void readData(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line = br.readLine();
        int i = 0;
        while (line != null) {
            String[] parts = line.trim().split("\\s+"); // catch 1 or more whitespaces as data separator
            for (int j = 1; j < parts.length; j++) {
                distance[i][j - 1] = Integer.parseInt(parts[j]);
            }
            i++;
            line = br.readLine();
        }
        br.close();
    }

    // depth first search algorithm, caches min distance and min path taken
    private static void dfs(int currCity, int currDist, int[] currPath, int visitedCount) {
        if (visitedCount == numCities) {
            currDist += distance[currCity][0];
            if (currDist < minDistance) {
                minDistance = currDist;
                minPath = Arrays.copyOfRange(currPath, 0, numCities+1);
            }
            return;
        }

        visited[currCity] = true;
        for (int i = 0; i < numCities; i++) {
            if (!visited[i]) {
            	
            	currPath[i] = currCity+1;
                dfs(i, currDist + calculateDistance(currCity, i), currPath, visitedCount + 1);
            }
        }
        visited[currCity] = false;
    }

    // euclidean algorithm for 2D cartesian plane distance calculation
    private static int calculateDistance(int city1, int city2) {
        int x1 = distance[city1][0];
        int y1 = distance[city1][1];
        int x2 = distance[city2][0];
        int y2 = distance[city2][1];
        return (int) Math.round(Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)));
    }

    // run cases from filepath
    public static void main(String[] args) throws IOException {
    	//TSP_DFS.solve("./data/test1tsp.txt");
    	//TSP_DFS.solve("./data/test2atsp.txt");
    	//TSP_DFS.solve("./data/test3atsp.txt");
    	
    	//TSP_DFS.solve("./data/test1-22.txt");
    	//TSP_DFS.solve("./data/test2-22.txt");
    	//TSP_DFS.solve("./data/test3-22.txt");
    	//TSP_DFS.solve("./data/test4-22.txt");
    }
}