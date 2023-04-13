package tsp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TSP_DP {
	
	// Class for algorithmic timing in ns
	static class Timer {
		private static long t = 0;
		
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
	}
	
	
	// Class representing cities from cities file
	// where cityNum = lines != empty in file
	// city -> [x, y] coordinates in cartesian 2D plane
    static class City {
        private final int cityNum;
        private final int x;
        private final int y;

        public City(int cityNum, int x, int y) {
            this.cityNum = cityNum;
            this.x = x;
            this.y = y;
        }

        public int getCityNum() {
            return cityNum;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    // read and parse cities from file
    // where city -. [x,y] coordinates
    public static List<City> parseData(String fileName) {
        List<City> cities = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split("\\s+"); // catch >= 1 whitespaces in separator
                int cityNum = Integer.parseInt(parts[0]);
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                cities.add(new City(cityNum, x, y));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cities;
    }

    // find optimal distance to travel through all cities and go back to initial city
    /* 
     *     cities: A list of City objects representing the cities to be visited.
	    currentCity: An integer representing the current city being visited.
	    visitedMask: An integer representing a bitmask that keeps track of the visited cities. The binary representation of visitedMask is used to determine which cities have been visited. For example, if the third bit from the right is set to 1, it means that the third city in the cities list has been visited.
	    dp: A 2D double array used for memoization to store the calculated optimal distances for subproblems.
	    start from all cities visited
	    cache the value to travel between cities to avoid duplicate calculations
	    at the end find the min cost to complete the full path and back and keep track of the shortest path only
	    at the end find the minimum value and return as final value
     * */
    public static double findOptimalDistance(List<City> cities, int currentCity, int visitedMask, double[][] dp) {
    	// back to initial city
        if (visitedMask == (1 << cities.size()) - 1) {
            return distance(cities.get(currentCity), cities.get(0));
        }

        // if already calculated cost use cached value
        if (dp[currentCity][visitedMask] != 0) {
            return dp[currentCity][visitedMask];
        }

        double minCost = Double.MAX_VALUE;

        for (int nextCity = 0; nextCity < cities.size(); nextCity++) {
            if ((visitedMask & (1 << nextCity)) == 0) {
            	// mask for next city
                int newVisitedMask = visitedMask | (1 << nextCity);
                // recursively calculate cost for path
                double cost = distance(cities.get(currentCity), cities.get(nextCity)) +
                		findOptimalDistance(cities, nextCity, newVisitedMask, dp);
                minCost = Math.min(minCost, cost);
            }
        }

        dp[currentCity][visitedMask] = minCost;
        return minCost;
    }

    // find optimal city path from cached 2d matrix of cities and shortest nearby city
    // because all paths have been calculated and cached
    // this function performs much faster than the first iteration as it only need to lookup
    // and find shortest path to add the corresponding city value
    public static List<Integer> findOptimalPath(List<City> cities, double[][] dp) {
        List<Integer> path = new ArrayList<>();
        int currentCity = 0;
        int visitedMask = 1;
        
        // add initial city
        path.add(cities.get(0).getCityNum());
        // get next optimal distance city
        while (visitedMask != (1 << cities.size()) - 1) {
            double minCost = Integer.MAX_VALUE;
            int nextCity = -1;

            for (int i = 0; i < cities.size(); i++) {
                if ((visitedMask & (1 << i)) == 0) {
                    double cost = distance(cities.get(currentCity), cities.get(i)) + dp[i][visitedMask | (1 << i)];
                    if (cost < minCost) {
                        minCost = cost;
                        nextCity = i;
                    }
                }
           }
            path.add(cities.get(nextCity).getCityNum());
            // change mask for next city
            visitedMask |= (1 << nextCity);
            currentCity = nextCity;
        }

        // add the starting city to complete the path
        path.add(cities.get(0).getCityNum());
        
        return path;
    }
    
    // build matrix of city distances using euclidean algorithm for 2D plane
    public static double distance(City city1, City city2) {
        double dx = city1.getX() - city2.getX();
        double dy = city1.getY() - city2.getY();
        return (double) Math.sqrt(dx * dx + dy * dy);
    }
    
    public static void solve(String filepath) {
    	Timer.startTimer();

        List<City> cities = parseData(filepath);
        
        // create 2d matrix [[city, 
        double[][] dp = new double[cities.size()][1 << cities.size()];
        
    	
        double optimalCost = findOptimalDistance(cities, 0, 1, dp);
        System.out.println("Optimum Cost: " + optimalCost);

        List<Integer> optimalPath = findOptimalPath(cities, dp);
        String path = "";
        for (int city:optimalPath) {
        	if (path != "") {
        		path += "->" + city;
        	} else {
        		path += city;
        	}
            	
        }
        System.out.println("Optimal Path: " + path);
        
        Timer.stopTimer();
        Timer.printTimer();
    }
    
    public static void main(String[] args) {    	
    	solve("./data/test3-22.txt");
    }
}
            
