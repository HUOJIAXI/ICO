package mesAgents;

import jade.core.Agent;
import mesComportements.BehaviourTabou;
import mesMetaheuristiques.ModeleTabou;
import tour.Tour;

/**
 * @Description: Class d'agent algorithme Tabou
 * @author: fangzhengjie
 * @Date: 2020年5月3日 上午9:47:47
 * @verson:1.0.0
 */
public class AgentTabou extends Agent{
	private ModeleTabou maTabou;		// Modèle heuristique algorithme Tabou
	private Object [] argsTour;			// le paramètre on transmet quand à créer ce agent
	private double bestDistance;		// variable qui enregistre la disrtance optimale dans la pluisieurs fois d'exécution de Modèle heuristique
	private Tour bestTour;				// variable qui enregistre la solution(class Tour) optimale dans la pluisieurs fois d'exécution de Modèle heuristique
	private int NB_EXECUTION=5;			// nombre de fois que l'on exécute le modèle heuristique indépendamment avent de se transmettre la solution avec autres agents

	protected void setup(){	
		argsTour = getArguments();
		Tour tour=(Tour)argsTour[0];
		/**
		 * on exécute le modèle heuristique indépendamment avent de se transmettre la solution avec autres agents
		 */
		for(int i=0;i<NB_EXECUTION;i++) {
		    long initTime =  System.currentTimeMillis();
		    ModeleTabou tabou=new ModeleTabou(70, 90, 245,98);
			tabou.init(tour);
			tabou.generateInitGroup();
			Tour bestT = tabou.getBestTour();
			double bestDis=bestT.getDistance();
			long overTime =  System.currentTimeMillis(); 
			long excutionTime=overTime-initTime;		// variable qui enregistre le temps utilisé par mon modèle heuristique chaquefois
			System.out.println("Tabou |Best Tour: " + bestT);
			System.out.println("Tabou |Final solution distance: " + bestDis);
			System.out.println("Tabou |Le temps d'éxecution est :"+ excutionTime+"ms");
		}
				
		/**
		 * Comme une fois exécution de heuristique GA va prendre beaucoup plus de temps que RS et Tabou, 
		 * on met sleep pendant une certaine milliseconds pour RS et Tabou
		 */
		try {
			Thread.sleep(30000);
			} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		
		long initTimeSMA =  System.currentTimeMillis();		// on get a temps déclenche pour SMA
		ModeleTabou tabu = new ModeleTabou(70, 90, 245,98);
		tabu.init(tour);
		tabu.generateInitGroup();
		this.bestTour = tabu.getBestTour();
		this.bestDistance=bestTour.getDistance();
		/**
		 * Après une première exécution de modèle heuristique, on ajoute une behaviour dans laquelle on se communique avec autres agents
		 */    
		addBehaviour(new BehaviourTabou(this,initTimeSMA));
	    
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
