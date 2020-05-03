package mesAgents;

import jade.core.Agent;
import mesComportements.BehaviourCentrale;
import tour.Tour;

/**
 * @Description: Class d'agent algorithme Centrale
 * @author: fangzhengjie
 * @Date: 2020年5月3日 上午9:51:59
 * @verson:1.0.0
 */
public class AgentCentrale extends Agent  {
	

	private Object[] argsTour;		// le paramètre on transmet quand à créer ce agent
	private double bestDistance;	// variable qui enregistre la disrtance optimale dans la pluisieurs fois d'exécution de Modèle heuristique
	private Tour bestTour;			// variable qui enregistre la solution(class Tour) optimale dans la pluisieurs fois d'exécution de Modèle heuristique
	
	
	protected void setup(){
		argsTour = getArguments(); 
		Tour tour=(Tour)argsTour[0];
		this.bestTour = tour;		// On initialise la solution optimale comme la Tour initiale donnée dans paramètre
		this.bestDistance=tour.getDistance();// On initialise la disrtance optimale comme la disrtance de la Tour initiale donnée dans paramètre

		/**
		 * Comme l'étape de test indépendant de 3 heuristiques va prendre un peu de temps,
		 * on met sleep pendant une certaine milliseconds pour agent centrale avant d'enter dans l'étape 2 (SMA)
		 */
		try {
			Thread.sleep(30000);
			} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		
		/**
		 * On ajoute une behaviour dans laquelle on se communique avec autres agents
		 */ 
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
