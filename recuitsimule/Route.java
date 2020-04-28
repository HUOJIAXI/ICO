package recuitsimule;
import java.util.ArrayList;
import recuitsimule.City;
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
		//System.out.println(citydepart);
		//this.cities.addAll(cities);
		for (City city : route) {
            this.cities.add(city);
        }
		
		//citydepart=this.cities.get(0).getName();
		
		Collections.shuffle(this.cities);
		if(this.cities.get(0).getName()!=getInit()) {
			for(int i=0;i<this.cities.size();i++) {
				if(this.cities.get(i).getName()==getInit()) {
					Collections.swap(this.cities, i, 0);
					break;
				}
			}
		} 
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
		
//		Collections.shuffle(cities);
		if(this.cities.get(0).getName()!=getInit()) {
			for(int i=0;i<this.cities.size();i++) {
				if(this.cities.get(i).getName()==getInit()) {
					Collections.swap(this.cities, i, 0);
					break;
				}
			}
		} 
		
		return this;
		
	}
	
	public String getInit()
	{
		String initcity="Bordeaux";
		return initcity;
	}
	
	public Route GenerateTourVoisinage()
	{
		Route NouvelleSolution = new Route(this.cities);
		int routePos1 = 0;
		int routePos2 = 0;
		
		while(routePos1==routePos2||routePos1==0||routePos2==0)
		{
			routePos1 = (int) (NouvelleSolution.sizeOfCities() * Math.random());
			routePos2 = (int) (NouvelleSolution.sizeOfCities() * Math.random());
		}
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
//		if (distance==0)
//		{
//			int routedistance =0;
//			for (int Index=0;Index<sizeOfCities();Index++)
//			{
//				City citydepart=getCity(Index);
//				City citydestination;
//				
//				if (Index+1<sizeOfCities()) {
//					citydestination=getCity(Index+1);					
//				}
//				else {
//				citydestination=getCity(0);
//				}
//				routedistance+=citydepart.measureDistance(citydestination);
//			}
//			
//			distance=routedistance;		
//		}
//		return distance;
        int cities_nums = this.cities.size();
        double TotalDistance = 0;
        ArrayList<City> x = getAllCities();
        for(int i = 0;i < cities_nums - 1;i++){
            TotalDistance += x.get(i).measureDistance(x.get(i+1));
        }
        return TotalDistance;


	}
	
	public int sizeOfCities(){
		return cities.size();
	}
	
	
}
