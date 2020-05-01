package mesAgents;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import jade.core.Agent;
import mesComportements.InteractionRS;
import mesComportements.InteractionTabou;
import mesMetaheuristiques.ModeleRS;
import mesMetaheuristiques.ModeleTabou;
import outil.GUI;
import tour.Tour;

public class AgentTabou extends Agent{
	

	private ModeleTabou maTabou;
	private Object [] argsTour;
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
//		System.out.println("Tabou is here");
		Tour tour=(Tour)argsTour[0];

		for(int i=0;i<NB_EXECUTION;i++) {
//			System.out.println("Tabou is in for boucle");
		    long initTime =  System.currentTimeMillis();
		    ModeleTabou tabou=new ModeleTabou(20, 50, 30, 20 );;
			tabou.init(tour);
			tabou.generateInitGroup();
			Tour bestT = tabou.getBestTour();
			double bestDis=bestT.getDistance();
			long overTime =  System.currentTimeMillis(); 
			long excutionTime=overTime-initTime;
			System.out.println("Tabou |Best Tour: " + bestT);
			System.out.println("Tabou |Final solution distance: " + bestDis);
			System.out.println("Tabou |Le temps d'éxecution est :"+ excutionTime+"ms");
		}
		
		long initTimeSMA =  System.currentTimeMillis();
		ModeleTabou tabu = new ModeleTabou(20, 50, 30, 20 );;
		tabu.init(tour);
		tabu.generateInitGroup();
		this.bestTour = tabu.getBestTour();
		this.bestDistance=bestTour.getDistance();
		    
		addBehaviour(new InteractionTabou(this,initTimeSMA));
	    
	} 
	
	public ModeleTabou getModele() {
		return this.maTabou;
		
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
		// TODO Auto-generated method stub

	}

}
