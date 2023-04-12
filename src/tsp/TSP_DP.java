package tsp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TSP_DP {
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

    public static List<City> readCitiesFromFile(String fileName) {
        List<City> cities = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
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

    public static double tsp(List<City> cities, int currentCity, int visitedMask, double[][] dp) {
        if (visitedMask == (1 << cities.size()) - 1) {
            return distance(cities.get(currentCity), cities.get(0));
        }

        if (dp[currentCity][visitedMask] != 0) {
            return dp[currentCity][visitedMask];
        }

        double minCost = Double.MAX_VALUE;

        for (int nextCity = 0; nextCity < cities.size(); nextCity++) {
            if ((visitedMask & (1 << nextCity)) == 0) {
                int newVisitedMask = visitedMask | (1 << nextCity);
                double cost = distance(cities.get(currentCity), cities.get(nextCity)) +
                           tsp(cities, nextCity, newVisitedMask, dp);
                minCost = Math.min(minCost, cost);
            }
        }

        dp[currentCity][visitedMask] = minCost;
        return minCost;
    }

    public static List<Integer> findOptimalPath(List<City> cities, double[][] dp) {
        List<Integer> path = new ArrayList<>();
        int currentCity = 0;
        int visitedMask = 1;
        
        path.add(cities.get(0).getCityNum());
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
            visitedMask |= (1 << nextCity);
            currentCity = nextCity;
        }

        // Add the starting city to complete the path
        path.add(cities.get(0).getCityNum());
        return path;
    }

    public static double distance(City city1, City city2) {
        double dx = city1.getX() - city2.getX();
        double dy = city1.getY() - city2.getY();
        return (double) Math.sqrt(dx * dx + dy * dy);
    }
    
    public static void solve(String filepath) {
        List<City> cities = readCitiesFromFile(filepath); // Replace with your own file name
        double[][] dp = new double[cities.size()][1 << cities.size()];
    	startTimer();

        double tspCost = tsp(cities, 0, 1, dp);
        System.out.println("Optimum Cost: " + tspCost);

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
    }
    
    public static void main(String[] args) {
    	startTimer();
    	
    	solve("./data/test3-22.txt");
    	
    	stopTimer();
    	printTimer();
    }
}
            
