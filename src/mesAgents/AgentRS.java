package mesAgents;

import java.sql.SQLException;

import city.City;
import jade.core.Agent;
import mesComportements.InteractionRS;
import mesMetaheuristiques.ModeleRS;
import outil.GUI;
import tour.Tour;

public class AgentRS extends Agent {

	private ModeleRS maSimulation;
	private Object[] argsTour;
	private double bestDistance;
	private Tour bestTour;

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
		ModeleRS rs=new ModeleRS();
		rs.initTour(tour);
		Tour bestTour = rs.recuitSimule();
		this.bestDistance=bestTour.getDistance();
		System.out.println("RS    |Tour: " + bestTour);
		System.out.println("RS    |Final solution distance: " +bestTour.getDistance());
		    
		addBehaviour(new InteractionRS(this));
	    
	}
	
	public ModeleRS getModele() {
		return this.maSimulation;
		
	}
	
	public double getBestDistance() {
		return this.bestDistance;
	}
	
	public Tour getBESTtOUR() {
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
