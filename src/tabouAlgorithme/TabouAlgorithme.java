/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tabouAlgorithme;

import dataWindow.DynamicDataWindow;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Xingyu Shang
 */
public class TabouAlgorithme {

    /**
     * number of iteration
     */
    private int MAX_GEN;
    /**
     * number of neighbor in each search
     */
    private int neighbourhoodNum;
    /**
     * lenth of tabou
     */
    private int tabuTableLength;
    /**
     * number of nodes
     */
    private int nodeNum;

    /**
     * the route chosen
     */
    private Route route;
    /**
     * the best route
     */
    private Route bestRoute;
    /**
     * the distance of the best route
     */
    private double bestEvaluation;
    /**
     * tabou table
     */
    private ArrayList<Route> tabuTable;
    /**
     * the evaluation in the tabou table
     */
    private double[] tabuTableEvaluate;
    private DynamicDataWindow ddWindow;
    private long tp = 0;

    private ArrayList<City> cityList = new ArrayList<City>();

    public TabouAlgorithme() {

    }

    /**
     * constructor of GA
     *
     * @param n number of cities
     * @param g times of execution
     * @param c the number of neighbor in each search
     * @param m the length of tabou
     *
     *
     */
    public TabouAlgorithme(int n, int g, int c, int m) {
        nodeNum = n;
        MAX_GEN = g;
        neighbourhoodNum = c;
        tabuTableLength = m;
    }

    /**
     * initialisation of Tabu algo
     *
     * @param filename the file contains the coordinates of each city
     * @throws IOException
     */
    private void init(String filename) throws IOException {
        // read data
        String strbuff;
        FileReader fileReader = new FileReader(filename);
        BufferedReader data = new BufferedReader(fileReader);

        double x;
        double y;
        String[] strcol;
        for (int i = 0; i < nodeNum; i++) {
            // read the first line, the format of data: 1 Paris 10000 6734 1453
            strbuff = data.readLine();
            // split the string
            strcol = strbuff.split("\t"); //according to tab to divide the data

            x = Double.valueOf(strcol[4]);// x coordinate-longitude
            y = Double.valueOf(strcol[3]);// y coordinate-latitude
            cityList.add(new City(String.valueOf(strcol[1]), y, x));
        }
        data.close();

        route = new Route(cityList);
        bestRoute = new Route(cityList);
        bestEvaluation = Integer.MAX_VALUE;
        tabuTable = new ArrayList<Route>(tabuTableLength);
        tabuTableEvaluate = new double[tabuTableLength];
        for (int i = 0; i < tabuTableEvaluate.length; i++) {
            tabuTable.add(new Route(new ArrayList<City>()));
            tabuTableEvaluate[i] = Integer.MAX_VALUE;
        }
    }

    /**
     * generate the initial group
     */
    void generateInitGroup() {
        System.out.println("1. Generate the initial group");
        this.route.melangeRoute();
    }

    /*
    get a random list non-repetitive
     */
    public static List getRandomNumList(int nums, int start, int end) {
        List list = new ArrayList();
        Random r = new Random();
        while (list.size() != nums) {
            int num = r.nextInt(end - start) + start;
            if (!list.contains(num)) {
                list.add(num);
            }
        }

        return list;
    }

    /**
     * get some neighbor routes randomly
     *
     * @param route the current route
     * @param tempNeighbourhoodNum
     * @return
     *
     */
    public ArrayList<Route> getNeighbourhood(Route route, int tempNeighbourhoodNum) {
        if (route == null) {
            System.err.println("Route is null");
        }
        int[][] orderList = new int[nodeNum * (nodeNum - 1) / 2][2];
        for (int i = 0; i < nodeNum - 1; i++) {
            for (int j = i + 1; j < nodeNum; j++) {
                orderList[(2 * nodeNum - i - 1) * i / 2 + j - i - 1][0] = i;
                orderList[(2 * nodeNum - i - 1) * i / 2 + j - i - 1][1] = j;
            }
        }
        if ((tempNeighbourhoodNum + tabuTableLength) >= orderList.length) {
            tempNeighbourhoodNum = orderList.length - tabuTableLength;
        }
        List ranIndexList = getRandomNumList(tempNeighbourhoodNum + tabuTableLength, 0, orderList.length);
        ArrayList routeList = new ArrayList<Route>();
        int k = 0;
        while (k < ranIndexList.size() && routeList.size() < tempNeighbourhoodNum) {
            Route tempRoute = new Route(route);

            int index = (int) ranIndexList.get(k);

            //System.out.println("tempRoute size= " + tempRoute.getCities().size());
            Collections.swap(tempRoute.getCities(), orderList[index][0], orderList[index][1]);

            if (isInTabuTable(tempRoute)) {
                k++;
            } else {
                routeList.add(tempRoute);
                k++;
            }
        }

        return routeList;
    }

    public ArrayList<Route> getNeighbourhood2(Route route, int tempNeighbourhoodNum) {
        ArrayList routeList = new ArrayList<Route>();
        routeList = this.getNeighbourhood(route, tempNeighbourhoodNum);
        int[][] orderList = new int[nodeNum * (nodeNum - 1) / 2][2];
        for (int i = 0; i < nodeNum - 1; i++) {
            for (int j = i + 1; j < nodeNum; j++) {
                orderList[(2 * nodeNum - i - 1) * i / 2 + j - i - 1][0] = i;
                orderList[(2 * nodeNum - i - 1) * i / 2 + j - i - 1][1] = j;
            }
        }
        List ranIndexList2 = getRandomNumList((int) tempNeighbourhoodNum/2  + tabuTableLength, 0, orderList.length);
        int k = 0;
        while (k < ranIndexList2.size() && routeList.size() /2 < tempNeighbourhoodNum) {
            Route tempRoute = new Route((Route) routeList.get(k));

            int index = (int) ranIndexList2.get(k);

            //System.out.println("tempRoute size= " + tempRoute.getCities().size());
            Collections.swap(tempRoute.getCities(), orderList[index][0], orderList[index][1]);

            if (isInTabuTable(tempRoute)) {
                k++;
            } else {
                routeList.set(k, tempRoute);
                k++;
            }

        }
        return routeList;
    }

    /**
     * jugde if it's in the tabou table
     *
     * @param tempRoute
     */
    public boolean isInTabuTable(Route tempRoute) {
        for (int i = 0; i < tabuTable.size(); i++) {
            if (tempRoute.equals(tabuTable.get(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * aspiration and adding a new tabou
     *
     * @param tempRoute
     */
    public void flushTabuTable(Route tempRoute) {
        //System.out.println("Flush!!++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        double tempValue = tempRoute.getTotalDistance();
        // find the max route value in the tabou table: tempMax
        double tempMax = tabuTableEvaluate[0];
        int maxValueIndex = 0;
        //System.out.println("TabuTableSize= " + tabuTable.size());
        for (int i = 0; i < tabuTable.size(); i++) {
            if (tabuTableEvaluate[i] > tempMax) {
                tempMax = tabuTableEvaluate[i];
                maxValueIndex = i;
            }
        }
        // add a new route in the tabou table
        if (tempValue < tabuTableEvaluate[maxValueIndex]) {
            if (tabuTableEvaluate[maxValueIndex] < Integer.MAX_VALUE) {
                route = tabuTable.get(maxValueIndex);
            }
            System.out.println("Testing point: renew the tabou table, maxValueIndex= " + maxValueIndex);
            if (tabuTable == null) {
                tabuTable.add(tempRoute);
            } else {
                tabuTable.set(maxValueIndex, tempRoute);
            }
            tabuTableEvaluate[maxValueIndex] = tempValue;
        }
    }

    /**
     * starting the tabou research
     */
    public void startSearch() {

        double neighbourhoodEvaluation;
        double currentBestRouteEvaluation;
        /**
         * save the neighbor routes
         */
        Route neighborRoute = new Route(route);
        /**
         * the best current route
         */
        Route currentBestRoute = new Route(route);
        /**
         * the current indicator
         */
        int currentIterateNum = 0;
        /**
         * the best number of indicator
         */
        int bestIterateNum = 0;
        ArrayList neighborRoutesList = new ArrayList<Route>();

        //to control the number of iteration
        Route priviousRoute = new Route(route);

        // initialisation solution
        generateInitGroup();
        //System.out.println("Check point: currentRoute= " + route);

        // regard the current route as the best route
        bestRoute = new Route(route);
        currentBestRouteEvaluation = route.getTotalDistance();
        bestEvaluation = currentBestRouteEvaluation;
        ddWindow.setRangeY(bestEvaluation);
        System.out.println("2. Iteration....");
        while (currentIterateNum < MAX_GEN) {
            priviousRoute = route;
            //currentBestRoute = route;
            neighborRoutesList = getNeighbourhood2(route, neighbourhoodNum);

            System.out.println("CurrentIterateNum= " + currentIterateNum);

            /*find the best route in neighborhood
            
             */
            for (int i = 0; i < neighbourhoodNum; i++) {
                // get a neighbor called neighbourhoodOfRoute of current route

                neighborRoute = (Route) neighborRoutesList.get(i);
                neighbourhoodEvaluation = neighborRoute.getTotalDistance();
//              System.out.println("Testing: neighbourhoodOfRoute="+neighbourhoodEvaluation);
                if (neighbourhoodEvaluation < currentBestRouteEvaluation) {
                    currentBestRoute = neighborRoute;
                    currentBestRouteEvaluation = neighbourhoodEvaluation;
//                  System.out.println("Testing: neighbourhoodOfRoute="+neighbourhoodEvaluation);
                }
            }

            if (currentBestRouteEvaluation < bestEvaluation) {
                bestIterateNum = currentIterateNum;
                bestRoute = currentBestRoute;
                bestEvaluation = currentBestRouteEvaluation;
                System.out.println("CurrentBestRouteEvaluation=" + currentBestRouteEvaluation);
            }

            // aspiration, currentBestRoute added into the tabou list
            //System.out.println("Testing point: currentBestRoute= " + currentBestRoute);
            route = currentBestRoute;
            flushTabuTable(currentBestRoute);
            currentIterateNum++;

            if (!route.equals(priviousRoute)) {
                //System.out.println("Found a non equal neighbor");
                currentIterateNum = 0;
            }
            //System.out.println("The current best distance: " + bestEvaluation);
            System.out.println("currentBestRouteEvaluation: " + currentBestRouteEvaluation);
            //System.out.println("priviousRoute.getTotalDistance(): " + priviousRoute.getTotalDistance());

            printRunStatus();

        }

        //Showing the result
        System.out.println("The iterate number of the best length");
        System.out.println(bestIterateNum);
        System.out.println("The best length:");
        System.out.println(bestEvaluation);
        System.out.println("The best route");
        System.out.print(bestRoute);

    }

    /**
     * @Description: print run status
     */
    private void printRunStatus() {
        long millis = System.currentTimeMillis();
        if (millis - tp > 20) {
            tp = millis;
            ddWindow.addData(millis, bestEvaluation);
        }
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        System.out.println("Start....");
        TabouAlgorithme tabu = new TabouAlgorithme(50, 120, 500, 100);
        tabu.ddWindow = new DynamicDataWindow("Tabou algorithme");
        tabu.ddWindow.setVisible(true);
        tabu.init("C:/Users/Xingyu Shang/Desktop/ICO/Tabou/data.txt");
        tabu.startSearch();
    }

}
