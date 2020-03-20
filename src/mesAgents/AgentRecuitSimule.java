package mesAgents;

import city.City;
import jade.core.Agent;
import mesComportements.InteractionAgentRS;
import mesMetaheuristiques.ModeleRecuitSimule;
import outil.GUI;
import tour.Tour;

public class AgentRecuitSimule extends Agent {

	private ModeleRecuitSimule maSimulation;
	private GUI monGUI;
	private float bestDistance;
	private Tour bestTour;
	
	protected void setup(){
        Tour tour=new Tour();
        tour.setDepart(new City("Bordeaux", 44.8378,-0.5792));
        tour.addCity(new City("Bordeaux", 44.8378,-0.5792));
        tour.addCity(new City("Lyon", 45.7640,4.8357));
        tour.addCity(new City("Nantes", 47.2184,-1.5536));
        tour.addCity(new City("Paris",48.8566,2.3522));
        tour.addCity(new City("Marseille", 43.2965,5.3698));
        tour.addCity(new City("Dijon", 47.3220,5.0415));
        
		ModeleRecuitSimule rs=new ModeleRecuitSimule();
	    rs.initTour(tour);
	    Tour bestTour = rs.recuitSimule();
	    this.bestDistance=bestTour.getDistance();
	    System.out.println("Tour: " + bestTour);
	    System.out.println("Final solution distance: " +bestTour.getDistance());
	    
	    addBehaviour(new InteractionAgentRS(this));
	    
	}
	
	public ModeleRecuitSimule getModeleRecuitSimule() {
		return this.maSimulation;
		
	}
	
	public float getBestDistance() {
		return this.bestDistance;
	}
	
	public Tour getBESTtOUR() {
		return this.bestTour;
	}
	
	public void setBestDistance(float currentDistance) {
		this.bestDistance=currentDistance;
		
	}
	
	public void setBestTour(Tour currentTour) {
		this.bestTour=currentTour;
	}
	
}
