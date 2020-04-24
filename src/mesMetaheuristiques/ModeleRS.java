package mesMetaheuristiques;

import java.util.ArrayList;

import city.City;
import tour.Tour;

public class ModeleRS {
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
	
	public void initTour(Tour tourInit) {
		this.currentSolution=tourInit.generateIndividuel();
//		System.out.println("Tour initail: " + this.currentSolution);
//		System.out.println("Initial solution distance: " + currentSolution.getDistance());
	}
	
	public void reInitTour(Tour tourResultatRecu) {
		this.currentSolution=tourResultatRecu;
//		System.out.println("Tour initail: " + this.currentSolution);
//		System.out.println("Initial solution distance: " + currentSolution.getDistance());
	}
	
	
	public Tour recuitSimule() {
		Tour bestSolution=currentSolution;
		Tour newSolution=null;
		while(currentTemperature>minTemperature) {
			for(int i=0;i<interLoop;i++) {
				newSolution=currentSolution.createNewNeighbourTour();
				double currentDistance=currentSolution.getDistance();
				double newDistance=newSolution.getDistance();
				if(Math.random()<Math.exp(-(newDistance-currentDistance)/currentTemperature)) {
					currentSolution=newSolution;
				}
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
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        Tour tour=new Tour();
        tour.setDepart(new City("Bordeaux", 44.8378,-0.5792));
        tour.addCity(new City("Bordeaux", 44.8378,-0.5792));
        tour.addCity(new City("Lyon", 45.7640,4.8357));
        tour.addCity(new City("Nantes", 47.2184,-1.5536));
        tour.addCity(new City("Paris",48.8566,2.3522));
        tour.addCity(new City("Marseille", 43.2965,5.3698));
        tour.addCity(new City("Dijon", 47.3220,5.0415));
        
		ModeleRS rs=new ModeleRS();
	    rs.initTour(tour);
	    Tour besTour = rs.recuitSimule();
	    System.out.println("Tour: " + besTour);
	    System.out.println("Final solution distance: " +besTour.getDistance());
	}

}
