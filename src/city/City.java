package city;


import java.util.ArrayList;

import jade.util.leap.Serializable;
import tour.Tour;
/**
 * @Description:  class de ville  
 * @Date: 2020年5月3日 上午8:38:16
 * @verson:1.0.0
 */
public class City  implements Serializable{
	private static final double EARTH_EQUATORIAL_RADIUS=6378.1370D; 
	private static final double CONVERT_DEGREES_TO_RADIANS=Math.PI/180D; 
	private static final double CONVERT_KMS_TO_MILES=0.621371;
	
	private double longitude; 
	private double latitude; 
	private String name;
	
	public City(String name,double latitude, double longitude)
	{ 
		this.name=name; 
		this.latitude=latitude*CONVERT_DEGREES_TO_RADIANS; 
		this.longitude=longitude*CONVERT_DEGREES_TO_RADIANS;
	
	}
	
	public double getLongitude() {
		return this.longitude;
	}
	
	public double getLatitude() {
		return this.latitude;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String toString(){
		return this.name;
	}
	
	/**
	 * @Description: mesure la distance avec une autre ville donnée 
	 * @param:une ville
	 * @return:la distance
	 */
	public double measureDistance(City city){
		double deltaLatitude = city.getLatitude() - this.getLatitude(); 
		double deltaLongitude = city.getLongitude()-this.getLongitude(); 
		double a=Math.pow(Math.sin(deltaLatitude/2D), 2D)+Math.cos(this.getLatitude())*Math.cos(city.getLatitude())*Math.pow(Math.sin(deltaLongitude/2D), 2D);
		
		return EARTH_EQUATORIAL_RADIUS*2D*Math.atan2(Math.sqrt(a), Math.sqrt(1D-a));
	}
	
	/**
	 * @Description: justifier si ma ville est égale à une autre ville
	 * @param:une ville
	 * @return: True or False
	 */
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null) {
			return false;
		}

		final City other = (City) obj;
		if (this.getName().equals(other.getName())) {
			return false;
		}
		
		if(this.getLatitude()!=other.getLatitude()) {
			return false;
		}
		
		if(this.getLongitude()!=other.getLongitude()){
			return false;
		}
		
		return true;
	}
}


