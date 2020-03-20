package tour;

import java.util.ArrayList;
import java.util.Collections;

import city.City;
import jade.util.leap.Serializable;


public class Tour implements Serializable{
	private ArrayList<City> cityList;
	private float distance=0;
	private City depart;
	
	public Tour() {
		this.cityList=new ArrayList<City>();	
	}
	
	public Tour(City depart, ArrayList<City> citylist) {
		this.cityList=new ArrayList<City>();
		this.depart=depart;
		for(City city:citylist) {
			this.cityList.add(city);
		}
	}
	
	public Tour generateIndividuel(){
		Collections.shuffle(this.cityList);
		for(int i=0;i<this.cityList.size();i++) {
			if(this.cityList.get(i).toString()==this.depart.toString()) { //为什么写了toString就可以了
				System.out.println("Ville Départ: "+this.cityList.get(i));
				City city0=this.cityList.get(0);
				this.cityList.set(0, this.depart);
				this.cityList.set(i, city0);
			}
		}
		return this;
	}
	
	public Tour createNewNeighbourTour() {
		Tour newSolution=new Tour(this.depart,this.cityList);
		int position1=(int)((this.cityList.size()-1)*Math.random())+1;
		int position2=(int)((this.cityList.size()-1)*Math.random())+1;
		
		City city1=this.cityList.get(position1);
		City city2=this.cityList.get(position2);
		
		newSolution.cityList.set(position1, city2);
		newSolution.cityList.set(position2, city1);
		
		return newSolution;
	}
	
	public City getCity(int index) {
		return this.cityList.get(index);
	}
	
	public void setCity(int index, City city) {
		this.cityList.set(index, city);
		this.distance=this.getDistance();
	}
	
	public void setDepart(City depart) {
		this.depart=depart;
	}
	
	public Tour addCity(City city) {
		this.cityList.add(city);
		return this;
	}
	
	public ArrayList getCityList() {
		return this.cityList;
	}
	
	public City getDepart() {
		return this.depart;
	}
	
	public float getDistance() {
		int tourDistance=0;
		for(int i=0;i<this.cityList.size();i++) {
			City cityDepart=this.cityList.get(i);
			City cityDestine;
			if(i+1<this.cityList.size()) {
				cityDestine=this.cityList.get(i+1);
			}else {
				cityDestine=this.cityList.get(0);
			}
			tourDistance+=cityDepart.measureDistance(cityDestine);
		}
		this.distance=tourDistance;
	return distance;
	}
	
	public String toString() {
		String geneString = "";
		int i=0;
	    	for (i=0; i < this.cityList.size()-1; i++) {
	    		geneString += getCity(i)+" -> ";
	        }
	    	geneString += getCity(i);
	    return geneString;
	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
