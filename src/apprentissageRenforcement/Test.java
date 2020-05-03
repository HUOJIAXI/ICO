package apprentissageRenforcement;

import java.util.ArrayList;

import city.City;
import data.RequeteVilleNombre;

public class Test {

    public static void main(String[] args){
        ArrayList<City> CityList = new ArrayList<City>();
        int NB_VILLE=20;

        RequeteVilleNombre requeteVilleNobre = new RequeteVilleNombre(NB_VILLE);
        ArrayList<String> carte = requeteVilleNobre.BDDconnexion();
        ArrayList<String> tableauVilles = requeteVilleNobre.getName(carte);
        ArrayList<Double> listeLatitudes = requeteVilleNobre.getLatitude(carte);
        ArrayList<Double> listeLongitudes = requeteVilleNobre.getLongitude(carte);
        for(int i=0;i<tableauVilles.size();i++) {
            City city=new City(tableauVilles.get(i),listeLatitudes.get(i),listeLongitudes.get(i));
            CityList.add(city);
        }
        City depart = CityList.get(0);
        LearningModeleGA test_GAslearning_1 = new LearningModeleGA(20, 0.7, 0.4, 10000, depart, CityList);
        test_GAslearning_1.Train();
        test_GAslearning_1.getFinalSolution();
        LearningModeleGA test_GAslearning_2 = new LearningModeleGA(20, 0.7, 0.3, 10000, depart, CityList);
        test_GAslearning_2.Train();
        test_GAslearning_2.getFinalSolution();
        LearningModeleGA test_GAslearning_3 = new LearningModeleGA(35, 0.7, 0.4, 10000, depart, CityList);
        test_GAslearning_3.Train();
        test_GAslearning_3.getFinalSolution();
    }
}
