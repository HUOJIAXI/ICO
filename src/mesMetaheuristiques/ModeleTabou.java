/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mesMetaheuristiques;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import city.City;
import data.RequeteVilleNombre;
import tour.Tour;

/**
 *
 * @author Xingyu Shang
 */
public class ModeleTabou {

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
    private Tour route;
    /**
     * the best route
     */
    private Tour bestRoute;
    /**
     * the distance of the best route
     */
    private double bestEvaluation;
    /**
     * tabou table
     */
    private ArrayList<Tour> tabuTable;
    /**
     * the evaluation in the tabou table
     */
    private double[] tabuTableEvaluate;
    private long tp = 0;

    private ArrayList<City> cityList = new ArrayList<City>();

    public ModeleTabou() {

    }

    /**
     * constructor of GA
     *
     * @param n number of cities
     * @param g times of execution
     * @param c the number of neighbor in each search
     * @param m the length of tabou
     *
     */
    public ModeleTabou(int n, int g, int c, int m) {
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
    public void init(Tour tourAInit) {

        route = new Tour(tourAInit);
        bestRoute = new Tour(tourAInit);
        bestEvaluation = Integer.MAX_VALUE;
        tabuTable = new ArrayList<Tour>(tabuTableLength);
        tabuTableEvaluate = new double[tabuTableLength];
        for (int i = 0; i < tabuTableEvaluate.length; i++) {
            tabuTable.add(new Tour(new ArrayList<City>()));
            tabuTableEvaluate[i] = Integer.MAX_VALUE;
        }
    }

    /**
     * generate the initial group
     */
    public void generateInitGroup() {
//        System.out.println("1. Generate the initial group");
        this.route.generateIndividuel();
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
    public ArrayList<Tour> getNeighbourhood(Tour route, int tempNeighbourhoodNum) {
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

        List ranIndexList = getRandomNumList(tempNeighbourhoodNum + tabuTableLength + nodeNum - 1, 0, orderList.length);
        ArrayList routeList = new ArrayList<Tour>();
        int k = 0;
        while (k < ranIndexList.size() && routeList.size() < tempNeighbourhoodNum) {
            Tour tempRoute = new Tour(route);

            int index = (int) ranIndexList.get(k);

            if (orderList[index][0] != 0) {
                Collections.swap(tempRoute.getCityList(), orderList[index][0], orderList[index][1]);
                if (isInTabuTable(tempRoute)) {
                    k++;
                } else {
                    routeList.add(tempRoute);
                    k++;
                }
            } else {
                k++;
            }

        }

        return routeList;
    }

    public ArrayList<Tour> getNeighbourhood2(Tour route, int tempNeighbourhoodNum) {
    	ArrayList routeList = new ArrayList<Tour>();
        routeList = this.getNeighbourhood(route, tempNeighbourhoodNum);
        int[][] orderList = new int[nodeNum * (nodeNum - 1) / 2][2];
        for (int i = 0; i < nodeNum - 1; i++) {
            for (int j = i + 1; j < nodeNum; j++) {
                orderList[(2 * nodeNum - i - 1) * i / 2 + j - i - 1][0] = i;
                orderList[(2 * nodeNum - i - 1) * i / 2 + j - i - 1][1] = j;
            }
        }
        List ranIndexList2 = getRandomNumList((int) tempNeighbourhoodNum / 2 + tabuTableLength, 0, orderList.length);
        int k = 0;
        while (k < ranIndexList2.size() && routeList.size() / 2 < tempNeighbourhoodNum) {
        	Tour tempRoute = new Tour((Tour) routeList.get(k));

            int index = (int) ranIndexList2.get(k);

            if (orderList[index][0] != 0) {
                Collections.swap(tempRoute.getCityList(), orderList[index][0], orderList[index][1]);
                if (isInTabuTable(tempRoute)) {
                    k++;
                } else {
                    routeList.add(tempRoute);
                    k++;
                }
            } else {
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
    public boolean isInTabuTable(Tour tempRoute) {
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
    public void flushTabuTable(Tour tempRoute) {
        //System.out.println("Flush!!++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        double tempValue = tempRoute.getDistance();
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
//            System.out.println("Testing point: renew the tabou table, maxValueIndex= " + maxValueIndex);
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
        Tour neighborRoute = new Tour(route);
        /**
         * the best current route
         */
        Tour currentBestRoute = new Tour(route);
        /**
         * the current indicator
         */
        int currentIterateNum = 0;
        /**
         * the best number of indicator
         */
        int bestIterateNum = 0;
        ArrayList neighborRoutesList = new ArrayList<Tour>();

        //to control the number of iteration
        Tour priviousRoute = new Tour(route);

        // initialisation solution
        generateInitGroup();
        //System.out.println("Check point: currentRoute= " + route);

        // regard the current route as the best route
        bestRoute = new Tour(route);
        currentBestRouteEvaluation = route.getDistance();
        bestEvaluation = currentBestRouteEvaluation;
        System.out.println("2. Iteration....");
        while (currentIterateNum < MAX_GEN) {
            priviousRoute = route;
            //currentBestRoute = route;
            neighborRoutesList = getNeighbourhood(route, neighbourhoodNum);

            System.out.println("CurrentIterateNum= " + currentIterateNum);

            /*find the best route in neighborhood
            
             */
            for (int i = 0; i < neighborRoutesList.size(); i++) {
                // get a neighbor called neighbourhoodOfRoute of current route
                neighborRoute = (Tour)neighborRoutesList.get(i);
                neighbourhoodEvaluation = neighborRoute.getDistance();
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

        }

        //Showing the result
        System.out.println("The iterate number of the best length");
        System.out.println(bestIterateNum);
        System.out.println("The best length:");
        System.out.println(bestEvaluation);
        System.out.println("The best route");
        System.out.print(bestRoute);

    }


    public Tour getBestTour() {
        double neighbourhoodEvaluation;
        double currentBestRouteEvaluation;
        /**
         * save the neighbor routes
         */
        Tour neighborRoute = new Tour(route);
        /**
         * the best current route
         */
        Tour currentBestRoute = new Tour(route);
        /**
         * the current indicator
         */
        int currentIterateNum = 0;
        /**
         * the best number of indicator
         */
        int bestIterateNum = 0;
        ArrayList<Tour> neighborRoutesList = new ArrayList<Tour>();

        //to control the number of iteration
        Tour priviousRoute = new Tour(route);

       
        // regard the current route as the best route
        bestRoute = new Tour(route);
        currentBestRouteEvaluation = route.getDistance();
        bestEvaluation = currentBestRouteEvaluation;
        while (currentIterateNum < MAX_GEN) {
            priviousRoute = route;
            //currentBestRoute = route;
            neighborRoutesList = getNeighbourhood(route, neighbourhoodNum);

            /*
             * find the best route in neighborhood
             */
            for (int i = 0; i < neighborRoutesList.size(); i++) {
                // get a neighbor called neighbourhoodOfRoute of current route
                neighborRoute = (Tour)neighborRoutesList.get(i);
                neighbourhoodEvaluation = neighborRoute.getDistance();
                if (neighbourhoodEvaluation < currentBestRouteEvaluation) {
                    currentBestRoute = neighborRoute;
                    currentBestRouteEvaluation = neighbourhoodEvaluation;
                }
            }

            if (currentBestRouteEvaluation < bestEvaluation) {
                bestIterateNum = currentIterateNum;
                bestRoute = currentBestRoute;
                bestEvaluation = currentBestRouteEvaluation;
            }

            route = currentBestRoute;
            flushTabuTable(currentBestRoute);
            currentIterateNum++;

            if (!route.equals(priviousRoute)) {
                currentIterateNum = 0;
            }

        }
        return bestRoute;

    }
    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        Tour tour=new Tour();
        RequeteVilleNombre requeteVilleNobre = new RequeteVilleNombre(20);
        ArrayList<String> carte = requeteVilleNobre.BDDconnexion();
        ArrayList<String> tableauVilles = requeteVilleNobre.getName(carte);
        ArrayList<Double> listeLatitudes = requeteVilleNobre.getLatitude(carte);
        ArrayList<Double> listeLongitudes = requeteVilleNobre.getLongitude(carte);
        tour.setDepart(new City(tableauVilles.get(0), listeLatitudes.get(0),listeLongitudes.get(0)));
        for(int i=0;i<tableauVilles.size();i++) {
         City city=new City(tableauVilles.get(i),listeLatitudes.get(i),listeLongitudes.get(i));
         tour.addCity(city);
        }

	    ModeleTabou tabou=new ModeleTabou(20, 50, 30, 20 );
		tabou.init(tour);
		tabou.generateInitGroup();
		Tour bestT = tabou.getBestTour();
		double bestDis=bestT.getDistance();

		System.out.println("Tabou |Best Tour: " + bestT);
		System.out.println("Tabou |Final solution distance: " + bestDis);
    }

}
