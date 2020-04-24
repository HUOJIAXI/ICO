package mesAgents;

import jade.core.Agent;
import outil.GUI;
import tour.Tour;
import mesComportements.InteractionGA;
import mesMetaheuristiques.ModeleGA;
import mesMetaheuristiques.ModeleRS;

import java.util.ArrayList;

import city.City;

public class AgentGA extends Agent { 

    private ModeleGA myGAs;
	private Object[] argsTour;
    private double ShortestTotalDistance;
    private Tour shortestTour;

    protected void setup(){
//        ArrayList<City> cities = new ArrayList<City>();
//        cities.add(new City("Bordeaux", 44.8333, -0.5667));
//        cities.add(new City("Lyon", 45.75, 4.85));
//        cities.add(new City("Nantes", 47.2173, -1.5534));
//        cities.add(new City("Paris", 48.8534, 2.3488));
//        cities.add(new City("Marseille", 43.3, 5.4));
//        cities.add(new City("Dijon", 47.3167, 5.0167));
//        City depart =new City("Bordeaux", 44.8333, -0.5667);
        argsTour = getArguments();
		Tour tour=(Tour)argsTour[0];	
        ModeleGA gas = new ModeleGA(30, 0.15, tour.getCityList(),tour.getDepart());
        gas.Evolution();
        this.shortestTour = new Tour(gas.getShortestTour());
        this.ShortestTotalDistance = this.shortestTour.getDistance();
        System.out.println("GA    |Tour: " + shortestTour);
		System.out.println("GA    |Final solution distance: " +ShortestTotalDistance);
        addBehaviour(new InteractionGA(this));
    }

    public ModeleGA getModele() {
        return this.myGAs;
    }

    public double getBestDistance(){
        return this.ShortestTotalDistance;
    }

    public Tour getBestTour(){
        return this.shortestTour;
    }

    public void setBestDistance(double shortestTotalDistance) {
        this.ShortestTotalDistance = shortestTotalDistance;
    }

    public void setBestTour(Tour currentTour){
        this.shortestTour = currentTour;
    }
}
