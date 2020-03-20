package BaseDeDonnee;

import java.util.ArrayList;
//import java.util.Arrays;

public class exe_bdd {

	public static void main(String[] args) {
		BDD bdd = new BDD(20);
		ArrayList<String> carte = bdd.BDDconnexion();
		ArrayList<String> tableauVilles = bdd.getName(carte);
		ArrayList<Double> listeLatitudes = bdd.getLatitude(carte);
		ArrayList<Double> listeLongitudes = bdd.getLongitude(carte);
		System.out.println(tableauVilles);
		System.out.println(listeLatitudes);
		System.out.println(listeLongitudes);
		
	}

}