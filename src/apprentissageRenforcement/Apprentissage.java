package apprentissageRenforcement;

import java.util.LinkedList;
import java.util.Random;

public abstract class Apprentissage {
		public int fitnessMeilleureSolutionConnue;
		public int fitnessSolutionProposee;
		public int nbStep;
		public double learningRate;
		public double epsilon; // probabilité d'exploration
		public double[][] valueFunction; //plus valueFunction[i][j] est grande, mieux c'est
		public double parametre1; // pCroisement pour AG, nbNeighbor      pour TB, TEMPERATE_original    pour RS
		public double parametre2; // pMutation   pour AG, longeurOfListTB pour TB, Coefficient_descendre pour RS
		public LinkedList<double[]> historiqueEtats = new LinkedList<double[]>();


	// ATTENTION : LES ACTIONS NE MODIFIENT PAS DIRECTEMENT LES PARAMETRES
	abstract double action1(double p1); //augmentation du paramètre 1
	abstract double action2(double p2); //augmentation du paramètre 2
	abstract double action3(double p3); //augmentation du paramètre 3
	abstract double action4(double p4); //augmentation du paramètre 4
	
	abstract void updateValueFunction(); //utilise historiqueEtats pour mettre à jour le tableau valueFunction
	abstract int[] convertirParametres(double p1, double p2); //permet de convertir deux paramètres en coordonnées pour naviguer dans le tableau valueFunction
	
	public void action(int i) {
		/*
		 * Effectue l'action i
		 */
		if(i == 1) {
			parametre1 = action1(parametre1);
		}
		if(i == 2) {
			parametre2 = action2(parametre2);
		}
		if(i == 3) {
			parametre1 = action3(parametre1);
		}
		if(i == 4) {
			parametre2 = action4(parametre2);
		}
		
	}
	
	public int choseAction() {
		Random random = new Random();
		double exploration = random.nextDouble(); 
		if(exploration < epsilon) { //dans le cas où on doit explorer
			return randomChoice();
		}
		else {
			return greedyChoice();
		}
	}
	
	public int randomChoice() {
		Random random = new Random();
		int choix = 1 + random.nextInt(4);
		return choix;
		
	}
	
	public int greedyChoice() {

		/* 
		 * On va tester les différentes actions possibles et voir laquelle donne la meilleure valueFunction
		 */
		
		// Test n°1 : augmentation du paramètre 1
		double p1Augmente = action1(parametre1);
		int p1AugmenteCoo = convertirParametres(p1Augmente, parametre2)[0];
		int p2Coo = convertirParametres(p1Augmente, parametre2)[1];
		double test1 = valueFunction[p1AugmenteCoo][p2Coo];
		
		double meilleurTest = test1;
		int meilleureAction = 1;
		
		// Test n°2 : augmentation du paramètre 2
		double p2Augmente = action2(parametre2);
		int p2AugmenteCoo = convertirParametres(parametre1, p2Augmente)[1];
		int p1Coo = convertirParametres(parametre1, p2Augmente)[0];
		double test2 = valueFunction[p1Coo][p2AugmenteCoo];
		
		if(test2 > meilleurTest) {
			meilleurTest = test2;
			meilleureAction = 2;
		}
		
		// Test n°3 : diminution du paramètre 1
		double p1Diminue = action3(parametre1);
		int p1DiminueCoo = convertirParametres(p1Diminue, parametre2)[0];
		double test3 = valueFunction[p1DiminueCoo][p2Coo];
		
		if(test3 > meilleurTest) {
			meilleurTest = test3;
			meilleureAction = 3;
		}
		
		// Test n°4 : diminution du paramètre 2
		double p2Diminue = action4(parametre2);
		int p2DiminueCoo = convertirParametres(parametre1, p2Diminue)[1];
		double test4 = valueFunction[p1Coo][p2DiminueCoo];
				
		if(test4 > meilleurTest) {
			meilleurTest = test4;
			meilleureAction = 4;
		}
				
		return meilleureAction;
		
		
	}

	public void majEpsilon(int n) {
		epsilon = 1d/Math.sqrt(1d + (double) n);
		//epsilon = 1d/Math.sqrt(Math.sqrt(Math.sqrt(1d + (double) n)));
	}


}
