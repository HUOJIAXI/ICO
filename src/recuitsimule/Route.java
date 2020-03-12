package recuitsimule;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Route {
	
	private ArrayList<City> cities;
	public String toString(){ return Arrays.toString(cities.toArray());};
	private double distance = 0;
	
	public Route () {
		cities=new ArrayList<City>();
		
	}
	 // À partir d'autre route
	public Route(ArrayList<City> route) {
		cities=new ArrayList<City>();
		//this.cities.addAll(cities);
		for (City city : route) {
            this.cities.add(city);
        }
		Collections.shuffle(this.cities);
	}
	
	public ArrayList<City> getCities(){
		return cities;
	} 
	
	public Route GenerateTour()
	{
		for (int IndexCity=0;IndexCity<cities.size();IndexCity++)
		{
			setCity(IndexCity, this.getCity(IndexCity));
			
		}
		
		//setCity(cities.size()-1, this.getCity(0));
		
		Collections.shuffle(cities);
		return this;
		
	}
	
	public Route GenerateTourVoisinage()
	{
		Route NouvelleSolution = new Route(this.cities);
		int routePos1 = (int) (NouvelleSolution.sizeOfCities() * Math.random());
		int routePos2 = (int) (NouvelleSolution.sizeOfCities() * Math.random());
		City cityechange1 = NouvelleSolution.getCity(routePos1);
		City cityechange2 = NouvelleSolution.getCity(routePos2);
		
		NouvelleSolution.setCity(routePos1, cityechange1);
		NouvelleSolution.setCity(routePos2, cityechange2);
		return NouvelleSolution;	
	}
	
	public City getCity(int tourPosition)
	{
		return (City)cities.get(tourPosition);
	}
	
	public void setCity(int tourIndex, City city)
	{
		cities.set(tourIndex, city);
		distance=0;
		
	}
	
	public Route addCity(City city)
	{
		cities.add(city);
		return this;
	}
	
	public ArrayList<City> getAllCities() 
	{
	        return cities;
    }
	
	public double getTotalDistance(){
		if (distance==0)
		{
			int routedistance =0;
			for (int Index=0;Index<sizeOfCities();Index++)
			{
				City citydepart=getCity(Index);
				City citydestination;
				
				if (Index+1<sizeOfCities()) {
					citydestination=getCity(Index+1);					
				}
				else {
				citydestination=getCity(0);
				}
				routedistance+=citydepart.measureDistance(citydestination);
			}
			
			distance=routedistance;		
		}
		return distance;

	}
	
	public int sizeOfCities(){
		return cities.size();
	}
	
	
}
