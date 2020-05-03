package mesMetaheuristiques;

/**
 *@description: Module algorithme Récuit Simulé. Ce module est de construire la structure de l'algorithme récuit simulé, celui qui permet d'être appelé par le module externe
 *@author: FANG Zhengjie & HUO Jiaxi
 *@created: 02/05/2020
 */

import java.util.ArrayList;

import city.City;
import data.RequeteVilleNombre;
import tour.Tour;

public class ModeleRS {
	/*
	 * Les paramètres d'algorithme récuit simulé
	 * currentTemperature: Température initiale de refrodissement
	 * minTemperature: Température finale après le refrodissement
	 * interLoop: Nombre d'itération interne
	 * coolingRate: La vitesse de refroidissement
	 */
	private double currentTemperature=500;
	private double  minTemperature=1;
	private int interLoop=50;
	private double coolingRate=0.01;
	private Tour currentSolution; 
	
	public ModeleRS() {
		
	}
	public ModeleRS(double currentTemperature,double  minTemperature,int interLoop,double coolingRate) {
		this.currentTemperature=currentTemperature;
		this.minTemperature=minTemperature;
		this.interLoop=interLoop;
		this.coolingRate=coolingRate;
	}
	
	/*
	 * initTour
	 * Création d'un tour initial avec la méthode aléatoire
	 */
	public void initTour(Tour tourInit) {
		this.currentSolution=tourInit.generateIndividuel();
	}
	
	/*
	 * reInitTour
	 * Création d'un tour initial avec la solution récupérée de l'agent central
	 */
	public void reInitTour(Tour tourResultatRecu) {
		this.currentSolution=tourResultatRecu;
	}
	
	/*
	 * recuitSimule
	 * Méthode principale de l'algorithme recuit simulé.
	 * return: solution optimale de l'algorithme recuit simulé
	 * createNewNeighbourTour: Création d'un tour voisin avec le tour courant
	 */
	public Tour recuitSimule() {
		Tour bestSolution=currentSolution;
		Tour newSolution=null;
		while(currentTemperature>minTemperature) {
			for(int i=0;i<interLoop;i++) {
				newSolution=currentSolution.createNewNeighbourTour();
				double currentDistance=currentSolution.getDistance();
				double newDistance=newSolution.getDistance();
				/*
				 * La solution courante est remplacée par la solution voisine par une certaine probabilité:
				 * exp(-(newDistance-currentDistance)/currentTemperature)
				 * Solution courante deviendra une mauvaise solution avec une certaine probabilité 
				 * afin de changer le point d'atterrissage de la solution voisine
				 * Mais la solution optimale courante n'accepte pas la solution mauvaise 
				 * car elle ne garde que la solution optimale en cours d'itération
				 */
				if(Math.random()<Math.exp(-(newDistance-currentDistance)/currentTemperature)) {
					currentSolution=newSolution;
				}
				/*
				 * Lorsque la solution courante est meilleure que la solution optimale courante,
				 * on remplace la solution optimale courante avec la solution courante
				 */
				if(currentSolution.getDistance()<bestSolution.getDistance()) {
					bestSolution=currentSolution;
				}
			}
			currentTemperature*=1-coolingRate;
		}
		return bestSolution;
	}
	//注意退火的过程中有两个中间量，currentSolation会以一定的概率变为较差解，用来改变每次生成newSolution的落点
	//bestSolution是不会以一定概率接受较差解的，它只保存整个while循环也即整个退火过程中的最优解
	

	public double getCurrentTemperature() {
		return currentTemperature;
	}
	
	public void setCurrentTemperature(double currentTemperature) {
		this.currentTemperature = currentTemperature;
	}
	
	public double getMinTemperature() {
		return minTemperature;
	}
	
	public void setMinTemperature(double minTemperature) {
		this.minTemperature = minTemperature;
	}
	
	public int getInterLoop() {
		return interLoop;
	}
	
	public void setInterLoop(int interLoop) {
		this.interLoop = interLoop;
	}
	
	public double getCoolingRate() {
		return coolingRate;
	}
	
	public void setCoolingRate(double coolingRate) {
		this.coolingRate = coolingRate;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        Tour tour=new Tour();
        int NB_VILLE=60;
        
        RequeteVilleNombre requeteVilleNobre = new RequeteVilleNombre(NB_VILLE);
	      ArrayList<String> carte = requeteVilleNobre.BDDconnexion();
	      ArrayList<String> tableauVilles = requeteVilleNobre.getName(carte);
	      ArrayList<Double> listeLatitudes = requeteVilleNobre.getLatitude(carte);
	      ArrayList<Double> listeLongitudes = requeteVilleNobre.getLongitude(carte);
	      tour.setDepart(new City(tableauVilles.get(0), listeLatitudes.get(0),listeLongitudes.get(0)));
	      for(int i=0;i<tableauVilles.size();i++) {
	       City city=new City(tableauVilles.get(i),listeLatitudes.get(i),listeLongitudes.get(i));
	       tour.addCity(city);
	      }
	      
        for(int j=0;j<5;j++)
        {
        	System.out.println("Changer la température initiale，Numéro de test: "+(j+1));
		    for(int num=0;num<5;num++) {
				ModeleRS rs=new ModeleRS(); 
			    rs.setCurrentTemperature(500-50*j);
			    System.out.println("Eviter le hasard，Numéro de test: "+(num+1));
			    long timecurrent=System.currentTimeMillis();
			    rs.initTour(tour);
			    Tour besTour = rs.recuitSimule();
			    long timeOver=System.currentTimeMillis();
			    System.out.println("Runtime: " + (timeOver-timecurrent));
			    System.out.println("Tour: " + besTour);
			    System.out.println("Final solution distance: " +besTour.getDistance());
		    }
        }
        
        for(int j=0;j<5;j++)
        {
        	System.out.println("Changer le nombre d'itération interne，Numéro de test: "+(j+1));
        	for(int num=0;num<5;num++)
        	{
        		System.out.println("Eviter le hasard，Numéro de test: "+(num+1));
				ModeleRS rs=new ModeleRS();		    
			    rs.setInterLoop(50+50*j);
			    long timecurrent=System.currentTimeMillis();
			    rs.initTour(tour);
			    Tour besTour = rs.recuitSimule();
			    long timeOver=System.currentTimeMillis();
			    System.out.println("Runtime: " + (timeOver-timecurrent));
			    System.out.println("Tour: " + besTour);
			    System.out.println("Final solution distance: " +besTour.getDistance());
        	}
        }
        
        
	}

}
