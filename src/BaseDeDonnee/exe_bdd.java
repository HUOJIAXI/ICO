package BaseDeDonnee;

import java.util.ArrayList;
//import java.util.Arrays;

public class exe_bdd {

	public static void main(String[] args) {
		new BDD(10);
		ArrayList<String> carte = BDD.BDDconnexion();
		ArrayList<String> tableauVilles = BDD.getName(carte);
		ArrayList<Double> listeLatitudes = BDD.getLatitude(carte);
		ArrayList<Double> listeLongitudes = BDD.getLongitude(carte);
		System.out.println(tableauVilles);
		System.out.println(listeLatitudes);
		System.out.println(listeLongitudes);
		
	}

}