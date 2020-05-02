package mesAgents;


import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import city.City;
import jade.core.Agent;
import mesComportements.BehaviourRS;
import mesMetaheuristiques.ModeleRS;
import outil.GUI;
import tour.Tour;

public class AgentRS extends Agent {

	private ModeleRS maSimulation;
	private Object[] argsTour;
	private double bestDistance;
	private Tour bestTour;
	private int NB_EXECUTION=5;

	protected void setup(){
//        Tour tour=new Tour();
//        tour.setDepart(new City("Bordeaux", 44.8378,-0.5792));
//        tour.addCity(new City("Bordeaux", 44.8378,-0.5792));
//        tour.addCity(new City("Lyon", 45.7640,4.8357));
//        tour.addCity(new City("Nantes", 47.2184,-1.5536));
//        tour.addCity(new City("Paris",48.8566,2.3522));
//        tour.addCity(new City("Marseille", 43.2965,5.3698));
//        tour.addCity(new City("Dijon", 47.3220,5.0415));
		argsTour = getArguments();
		Tour tour=(Tour)argsTour[0];

		for(int i=0;i<NB_EXECUTION;i++) {
		    long initTime = System.currentTimeMillis();
			ModeleRS rs=new ModeleRS();
			rs.initTour(tour);
			Tour bestT = rs.recuitSimule();
			double bestDis=bestT.getDistance();
			long overTime=System.currentTimeMillis();
			long excutionTime=overTime-initTime;
			System.out.println("RS    |Best Tour: " + bestT);
			System.out.println("RS    |Final solution distance: " + bestDis);
			System.out.println("Rs    |Le temps d'éxecution est :"+ excutionTime+"ms");
		}
		
		System.out.println("=====================================================");

		
		try {
			Thread.sleep(6000);
			} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		
		long initTimeSMA =  System.currentTimeMillis();
		ModeleRS rs=new ModeleRS();
		rs.initTour(tour);
		this.bestTour = rs.recuitSimule();
		this.bestDistance=bestTour.getDistance();
		
		addBehaviour(new BehaviourRS(this,initTimeSMA));
	}
	
	public ModeleRS getModele() {
		return this.maSimulation;
		
	}
	
	public double getBestDistance() {
		return this.bestDistance;
	}
	
	public Tour getBestTour() {
		return this.bestTour;
	}
	
	public void setBestDistance(double currentDistance) {
		this.bestDistance=currentDistance;
		
	}
	
	public void setBestTour(Tour currentTour) {
		this.bestTour=currentTour;
	}
	
	
	public static void main(String[] args) {
		
		
		
	}
}
