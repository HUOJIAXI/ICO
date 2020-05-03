package mesAgents;

import jade.core.Agent;
import tour.Tour;
import mesComportements.BehaviourGA;
import mesMetaheuristiques.ModeleGA;

/**
 * @Description: Class d'agent algorithme génétique
 * @author: fangzhengjie
 * @Date: 2020年5月3日 上午9:21:10
 * @verson:1.0.0
 */
public class AgentGA extends Agent { 

    private ModeleGA myGA;					// Modèle heuristique algorithme génétique
    private Object[] argsTour; 				// le paramètre on transmet quand à créer ce agent
    private double ShortestTotalDistance;	// variable qui enregistre la disrtance optimale dans la pluisieurs fois d'exécution de Modèle heuristique
    private Tour shortestTour;				// variable qui enregistre la solution(class Tour) optimale dans la pluisieurs fois d'exécution de Modèle heuristique
	private int NB_EXECUTION=5;				// nombre de fois que l'on exécute le modèle heuristique indépendamment avent de se transmettre la solution avec autres agents

    protected void setup(){
        argsTour = getArguments();
		Tour tour=(Tour)argsTour[0];	
		/**
		 * on exécute le modèle heuristique indépendamment avent de se transmettre la solution avec autres agents
		 */
		for(int i=0;i<NB_EXECUTION;i++) {
			    long initTime = System.currentTimeMillis();
			    ModeleGA gas = new ModeleGA(50,0.15, tour.getCityList(),tour.getDepart());
			    gas.Evolution();
		        Tour bestT = new Tour(gas.getShortestTour());
		        double bestDis = bestT.getDistance();
				long overTime = System.currentTimeMillis();
				long excutionTime=overTime-initTime;		// variable qui enregistre le temps utilisé par mon modèle heuristique chaquefois
				System.out.println("GA    |Best Tour: " + bestT);
				System.out.println("GA    |Final solution distance: " + bestDis);
				System.out.println("GA    |Le temps d'éxecution est :"+ excutionTime+"ms");
			}
		System.out.println("============================================================================================================================");

		
		long initTimeSMA = System.currentTimeMillis();   	// on get a temps déclenche pour SMA
        ModeleGA gas = new ModeleGA(50,0.15, tour.getCityList(),tour.getDepart());
        gas.Evolution();
        this.shortestTour = new Tour(gas.getShortestTour());
        this.ShortestTotalDistance = this.shortestTour.getDistance();
		/**
		 * Après une première exécution de modèle heuristique, on ajoute une behaviour dans laquelle on se communique avec autres agents
		 */
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
