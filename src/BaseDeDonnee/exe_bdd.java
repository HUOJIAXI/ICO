package BaseDeDonnee;

import java.util.ArrayList;
import java.util.Arrays;

public class exe_bdd {

	public static void main(String[] args) {
		ArrayList carte = BDD.BDDconnexion();
		String[] tableauVilles = (String[]) carte.get(0);
		int[][] tableauDistances = (int[][]) carte.get(1);
		System.out.println(Arrays.toString(tableauVilles));
		System.out.println(Arrays.deepToString(tableauDistances));
	}

}