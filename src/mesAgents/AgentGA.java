package mesAgents;

import jade.core.Agent;
import outil.GUI;
import tour.Tour;
import mesComportements.BehaviourGA;
import mesMetaheuristiques.ModeleGA;
import mesMetaheuristiques.ModeleRS;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import city.City;

public class AgentGA extends Agent { 

    private ModeleGA myGA;
	private Object[] argsTour;
    private double ShortestTotalDistance;
    private Tour shortestTour;
	private int NB_EXECUTION=5;

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
		for(int i=0;i<NB_EXECUTION;i++) {
			    long initTime = System.currentTimeMillis();
			    ModeleGA gas = new ModeleGA(50, 0.15, tour.getCityList(),tour.getDepart());
			    gas.Evolution();
		        Tour bestT = new Tour(gas.getShortestTour());
		        double bestDis = bestT.getDistance();
				long overTime = System.currentTimeMillis();
				long excutionTime=overTime-initTime;
				System.out.println("GA    |Best Tour: " + bestT);
				System.out.println("GA    |Final solution distance: " + bestDis);
				System.out.println("GA    |Le temps d'éxecution est :"+ excutionTime+"ms");
			}
		System.out.println("=====================================================");

		
		long initTimeSMA = System.currentTimeMillis();
        ModeleGA gas = new ModeleGA(50, 0.15, tour.getCityList(),tour.getDepart());
        gas.Evolution();
        this.shortestTour = new Tour(gas.getShortestTour());
        this.ShortestTotalDistance = this.shortestTour.getDistance();
		
        addBehaviour(new BehaviourGA(this,initTimeSMA));

    }

    public ModeleGA getModele(){
        return this.myGA;
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
