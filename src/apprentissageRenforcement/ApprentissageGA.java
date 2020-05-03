package apprentissageRenforcement;

/**
 *@program: PVC
 *@description: Le modèle de l'apprentissage renforcement pour l'algorithme génétique
 *@author: Zhenyu Brie ZHAO
 *@created: 02/05/2020
 */
public class ApprentissageGA extends Apprentissage {
	/*
	 * Les paramètres 1 et 2 de la classe mère Apprentissage correspondent ici respectivement à pCroisement et pMutation.
	 * Les deux sont compris entre 0 et 1 (inclus).
	 */

	public ApprentissageGA(int fitnessMeilleureSolutionConnue, int fitnessSolutionProposee, int nbStep,
			double learningRate, double epsilon, double pCroisement, double pMutation) { //initialiser les valeurs des champs
		super();
		this.fitnessMeilleureSolutionConnue = fitnessMeilleureSolutionConnue;
		this.fitnessSolutionProposee = fitnessSolutionProposee;
		this.nbStep = nbStep;
		this.learningRate = learningRate;
		this.epsilon = epsilon;
		this.parametre1 = pCroisement;
		this.parametre2 = pMutation;
		this.valueFunction = new double[11][11]; //les valeurs vont de 0 à 1 avec un incrément de 0.1
		for(int i=0;i<11;i++) {
			for(int j=0;j<11;j++) {
				valueFunction[i][j] = 0d;
			}
		}
		double[] parametresInitiaux = {pCroisement, pMutation};
		this.historiqueEtats.add(parametresInitiaux);
	}

	// ACTIONS
	/*
	 * Pour l'agent génétique, les actions sont :
	 * 1 : augmenter pCroisement de 0.1
	 * 2 : augmenter pMutation de 0.1
	 * 3 : diminuer pCroisement de 0.1
	 * 4 : diminuer pMutation de 0.1
	 * 
	 * Dans le cas où l'on excède l'une des bornes ( 0 < pX < 1), le paramètre n'est pas modifié.
	 *  
	 */
	
	double action1(double p1) {
		if(p1+0.1<=1) {
			return p1+0.1;
		}
		else {
			return p1;
		}
	}

	double action2(double p2) {
		if(p2+0.1<=1) {
			return p2+0.1;
		}
		else {
			return p2;
		}
	}

	double action3(double p3) {
		if(p3-0.1>=0) {
			return p3-0.1;
		}
		else {
			return p3;
		}
	}

	double action4(double p4) {
		if(p4-0.1>=0) {
			return p4-0.1;
		}
		else {
			return p4;
		}
	}

	public void updateValueFunction() {
		// On commence par mettre à jour la value function pour le dernier état, à partir de la "récompense"
		int cooPrec1 = convertirParametres(this.historiqueEtats.getLast()[0],this.historiqueEtats.getLast()[1])[0];
		int cooPrec2 = convertirParametres(this.historiqueEtats.getLast()[0],this.historiqueEtats.getLast()[1])[1];
		
		// On applique la formule : V(s) = V(s) + learningRate*(V(s')-V(s))
		// Pour le dernier état, V(s') prend la valeur de la "récompense", qui est ici la différence à la meilleure solution connue.
		this.valueFunction[cooPrec1][cooPrec2] = this.valueFunction[cooPrec1][cooPrec2] + this.learningRate*((fitnessMeilleureSolutionConnue-fitnessSolutionProposee)-this.valueFunction[cooPrec1][cooPrec2]);
		
		// Par "backtracking", on va mettre à jour tous les autres états.
		while(this.historiqueEtats.size() > 1) {
			int coo1 = convertirParametres(this.historiqueEtats.get(this.historiqueEtats.size()-2)[0],this.historiqueEtats.get(this.historiqueEtats.size()-2)[1])[0]; //on va utiliser le dernier élément de l'historique comme s', et donc s est l'élément qui précède
			int coo2 = convertirParametres(this.historiqueEtats.get(this.historiqueEtats.size()-2)[0],this.historiqueEtats.get(this.historiqueEtats.size()-2)[1])[1];
			this.valueFunction[coo1][coo2] = this.valueFunction[coo1][coo2] + this.learningRate*(this.valueFunction[cooPrec1][cooPrec2]-this.valueFunction[coo1][coo2]);
			cooPrec1 = coo1;
			cooPrec2 = coo2;
			this.historiqueEtats.removeLast();
		}
		this.historiqueEtats.removeLast();
		
	}

	int[] convertirParametres(double p1, double p2) {
		/*
		 * Avec l'incrément de 0.1 qu'on a choisi, pour obtenir des coordonnées à partir des probabilités de croisement/mutation,
		 * il suffit de les multiplier par 10.
		 */
		
		int paramConverti1 = (int) (p1*10d);
		int paramConverti2 = (int) (p2*10d);
		int[] parametresConvertis = {paramConverti1,paramConverti2};
		return parametresConvertis;
		
	}

}
