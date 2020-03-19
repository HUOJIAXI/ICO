/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tabouAlgorithme;

import java.util.Objects;

/**
 *
 * @author Xingyu Shang
 */
public class City {
//constantes

    private static final double EARTH_EQUATORIAL_RADIUS = 6378.1370D;
    private static final double CONVERT_DEGREES_TO_RADIANS = Math.PI / 180D;
    private static final double CONVERT_KMS_TO_MILES = 0.621371;

//attributs
    private double longitude;
    private double latitude;
    private String name;

//Constructeurs
    public City(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude * CONVERT_DEGREES_TO_RADIANS;
        this.longitude = longitude * CONVERT_DEGREES_TO_RADIANS;
    }
//get methods


    public double getLongitude() {
        return this.longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public String getName() {
        return this.name;
    }
//

    public String toString() {
        return this.name;
    }
//Distance en Km entre 2 Villes

    public double measureDistance(City city) {
        double deltaLatitude = city.getLatitude() - this.getLatitude();
        double deltaLongitude = city.getLongitude() - this.getLongitude();
        double a = Math.pow(Math.sin(deltaLatitude / 2D), 2D)
                + Math.cos(this.getLatitude()) * Math.cos(city.getLatitude()) * Math.pow(Math.sin(deltaLongitude / 2D
        ), 2D);
        return EARTH_EQUATORIAL_RADIUS * 2D * Math.atan2(Math.sqrt(a), Math.sqrt(1D - a));
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + (int) (Double.doubleToLongBits(this.longitude) ^ (Double.doubleToLongBits(this.longitude) >>> 32));
        hash = 73 * hash + (int) (Double.doubleToLongBits(this.latitude) ^ (Double.doubleToLongBits(this.latitude) >>> 32));
        hash = 73 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final City other = (City) obj;
        if(other.name!=this.name){
            return false;
        }
        return true;
    }
    
    
}
