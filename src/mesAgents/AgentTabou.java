package mesAgents;

import java.sql.SQLException;

import jade.core.Agent;
import mesComportements.InteractionRS;
import mesComportements.InteractionTabou;
import mesMetaheuristiques.ModeleTabou;
import outil.GUI;
import tour.Tour;

public class AgentTabou extends Agent{
	

	private ModeleTabou maTabou;
	private Object [] argsTour;
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
		ModeleTabou tabu = new ModeleTabou(6, 30, 10, 4);
		tabu.init(tour);
		Tour bestTour = tabu.getBestTour();
		this.bestDistance=bestTour.getDistance();
		System.out.println("Tabou |Tour: " + bestTour);
		System.out.println("Tabou |Final solution distance: " +bestTour.getDistance());
		    
		addBehaviour(new InteractionTabou(this));
	    
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