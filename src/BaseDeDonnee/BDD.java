package BaseDeDonnee;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class BDD {
	
//	private static final double EARTH_EQUATORIAL_RADIUS=6378000.1370D;
//	private static final double CONVERT_DEGREES_TO_RADIANS=Math.PI/180D;
//	private static final double CONVERT_KMS_TO_MILES=0.621371;
	//------------------------------------------------------------------------------------ //
	
	//Méthode qui permet le calcul de distances---------------------------------------------
	
	public static double distance(double lat1, double lat2, double lon1,
	        double lon2, double el1, double el2) {

	    final int R = 6371; // Radius of the earth

	    double latDistance = Math.toRadians(lat2 - lat1);
	    double lonDistance = Math.toRadians(lon2 - lon1);
	    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
	            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
	            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double distance = R * c * 1000; // convert to meters

	    double height = el1 - el2;

	    distance = Math.pow(distance, 2) + Math.pow(height, 2);

	    return Math.sqrt(distance)/1000D;
	}
	
	public static ArrayList BDDconnexion(){
		
	String UserName="root";
	String Password="zjuhjx1998";
	
	Connection connect = null;
	
	try {
		System.out.println("reussite à activer le driver");
		Class.forName("com.mysql.jdbc.Driver");
	}
	 catch (Exception e) 
	{
		 
		 e.printStackTrace();  
		 
	}
	
	try {
		connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/city",UserName,Password); 
		System.out.println("reussite à connecter la BDD");
	} 
	
	catch (Exception e) 
	{
		e.printStackTrace();  
	}
	
	String query = "SELECT ville_nom, ville_latitude_deg, ville_longitude_deg FROM villes_france_free;";
	
	ResultSet results=null; // le résulat de la requête est stocké dans cette variable
	try {
		System.out.println("reussite a recuperer les donnees");
		Statement stmt = connect.createStatement();
		results = stmt.executeQuery(query);}
	catch(Exception e) 
	{ // Exception est la classe mère de toutes les exceptions, dans les blocs faits automatiquement faits par Eclipse, le type d'exception est précisé. Ces exceptions sont de sous classes de Exception
		System.out.println("exception due a la requete");
	}
	
	LinkedList<String> listeVilles     = new LinkedList<String>();
	LinkedList<Double> listeLatitudes  = new LinkedList<Double>();
	LinkedList<Double> listeLongitudes = new LinkedList<Double>();
	
	
	try {
		while(results.next()) {
			listeVilles.add(results.getString(1));
			listeLatitudes.add(results.getDouble(2));
			listeLongitudes.add(results.getDouble(3));
		}

	} catch (SQLException e) {
		e.printStackTrace();
	}
	
	ArrayList carte = new ArrayList();
	int nbVilles = listeVilles.size();
	int[][] tableauDistances = new int[nbVilles][nbVilles];
	String[] tableauVilles = new String[nbVilles];
	
	//création du tableau des villes
	for(int i = 0; i<nbVilles; i++) {
		tableauVilles[i] = listeVilles.get(i);
	}

	//----- CALCUL DES DISTANCES -----
	for(int i = 0;i<nbVilles;i++) {
		for(int j = 0; j<nbVilles;j++) {
			if(i==j) {
				tableauDistances[i][i] = 0;
			}
			if(i<j) {

				tableauDistances[i][j] = (int) distance(listeLatitudes.get(i), listeLatitudes.get(j), listeLongitudes.get(i), listeLongitudes.get(j), 0, 0);
				tableauDistances[j][i] = tableauDistances[i][j];
			}

			}
		}
	//Les tableaux sont maintenant remplis
	carte.add(tableauVilles);
	carte.add(tableauDistances);
	carte.add(listeLatitudes);
	carte.add(listeLongitudes);
	return carte;
	
	
	}

}
