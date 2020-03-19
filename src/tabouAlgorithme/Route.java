/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tabouAlgorithme;

/**
 *
 * @author Xingyu Shang
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class Route {

    private ArrayList<City> cities = new ArrayList<City>();

    public String toString() {
        return Arrays.toString(cities.toArray());
    }
//constructeurs
//-->à partir d'une autre route

    public Route(Route route) {
        /*
        * ajouter les villes de la route en param comme villes de cette route
         */
        //à compléter

        this.cities.addAll(route.getCities());
    }
//-->à partir d'une liste de villes qu'on mélange pour ne pas garder le même ordre

    public Route(ArrayList<City> cities) {
        this.cities.addAll(cities);
    }

    public void melangeRoute() {
        //mélanger aléatoirement les villes de la route
        Collections.shuffle(this.cities);
    }

//get methods
    public ArrayList<City> getCities() {
        return cities;
    }

    public double getTotalDistance() {
        int citiesSize = this.cities.size();
        double totalDistance = 0;
        for (int i = 1; i < citiesSize; i++) {
            totalDistance += this.cities.get(i).measureDistance(this.cities.get(i - 1));
        }
        return totalDistance;
    }

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
        final Route other = (Route) obj;
        if(this.cities.isEmpty()||other.cities.isEmpty()){
            return false;
        }
        for (int i = 0; i < this.cities.size(); i++) {
            if (!other.getCities().get(i).equals(this.cities.get(i)));
            return false;
        }
        return true;
    }

}
