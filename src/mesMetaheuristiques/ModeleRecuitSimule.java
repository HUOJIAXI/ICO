package mesMetaheuristiques;

import java.util.ArrayList;

import city.City;
import tour.Tour;

public class ModeleRecuitSimule {
	private double currentTemperature=5000;
	private double  minTemperature=0.001;
	private int interLoop=1000;
	private double coolingRate=0.001;
	private Tour currentSolution;
	
	public void initTour(Tour tourInit) {
		this.currentSolution=tourInit.generateIndividuel();
		System.out.println("Tour initail: " + this.currentSolution);
		System.out.println("Initial solution distance: " + currentSolution.getDistance());
	}
	
	public Tour recuitSimule() {
		Tour bestSolution=currentSolution;
		Tour newSolution=null;
		while(currentTemperature>minTemperature) {
			for(int i=0;i<interLoop;i++) {
				newSolution=currentSolution.createNewNeighbourTour();
				float currentDistance=currentSolution.getDistance();
				float newDistance=newSolution.getDistance();
				if(newDistance<currentDistance) {
//					bestSolution= new Tour(newSolution.getCityList());
					bestSolution=newSolution;
				}else {
					if(Math.random()<Math.exp(-(newDistance-currentDistance)/currentTemperature)) {
						bestSolution=newSolution;
//						bestSolution= new Tour(newSolution.getCityList());
					}
				}
			}
			currentTemperature*=1-coolingRate;
		}
		return bestSolution;
	}
	
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
        
		ModeleRecuitSimule rs=new ModeleRecuitSimule();
	    rs.initTour(tour);
	    Tour besTour = rs.recuitSimule();
	    System.out.println("Tour: " + besTour);
	    System.out.println("Final solution distance: " +besTour.getDistance());
	}

}
