package mesAgents;

import jade.core.Agent;
import mesComportements.BehaviourCentrale;
import mesComportements.BehaviourRS;
import mesMetaheuristiques.ModeleRS;
import tour.Tour;

public class AgentCentrale extends Agent  {
	

	private Object[] argsTour;
	private double bestDistance;
	private Tour bestTour;

	
	
	protected void setup(){
	
		argsTour = getArguments(); 
		Tour tour=(Tour)argsTour[0];
		this.bestTour = tour;
		this.bestDistance=tour.getDistance();
		
		try {
			Thread.sleep(6000);
			} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		
		
		addBehaviour(new BehaviourCentrale(this,1000));
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
	
}
