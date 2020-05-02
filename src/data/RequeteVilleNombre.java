package data;



import java.sql.*;
import java.util.ArrayList;

import city.City;
//  Classe pour la base de données 21/03/2020

public class RequeteVilleNombre {

	public static int num;
	
	public RequeteVilleNombre(int numrecherche){ 
		RequeteVilleNombre.num=numrecherche; 
	}
	
	public ArrayList<String> getName(ArrayList<String> carte) {
		ArrayList<String> listeVilles = new ArrayList<String>();
		
		for(int i = 0; i < num; i++) {
			listeVilles.add(carte.get(i));
		}
		
		return listeVilles;
	}
	
	public ArrayList<Double> getLatitude(ArrayList<String> carte) {
		
		ArrayList<Double> listeLatitudes = new ArrayList<Double>();
		
		for(int i = num; i < 2*num; i++) {
			listeLatitudes.add(Double.parseDouble(carte.get(i)));
		}
		
		return listeLatitudes;
	}
	
	public ArrayList<Double> getLongitude(ArrayList<String> carte) {
		
		ArrayList<Double> listeLongitude = new ArrayList<Double>();
		
		for(int i = 2*num; i < 3*num; i++) {
			listeLongitude.add(Double.parseDouble(carte.get(i)));
		}
		
		return listeLongitude;
	}
		
	public ArrayList<String> BDDconnexion(){
		
	String UserName="root";
	String Password="f19961104zj";
	String num_recherche=String.valueOf(num);
	
	Connection connect = null;
	
	try {
//		System.out.println("reussite à activer le driver");
		Class.forName("com.mysql.jdbc.Driver");
	}
	 catch (Exception e) 
	{
		 
		 e.printStackTrace();  
		 
	}
	
	try {
		connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/villes_france",UserName,Password); 
//		System.out.println("reussite à connecter la BDD");
	} 
	
	catch (Exception e) 
	{
		e.printStackTrace();  
	}
	
	String query = "SELECT ville_nom, ville_latitude_deg, ville_longitude_deg FROM villes_france_free ORDER BY RAND() LIMIT "+num_recherche+";";
	
	ResultSet results=null; // le résulat de la requête est stocké dans cette variable
	try {
		System.out.println("reussite a recuperer les donnees");
		Statement stmt = connect.createStatement();
		results = stmt.executeQuery(query);}
	catch(Exception e) 
	{ // Exception est la classe mère de toutes les exceptions, dans les blocs faits automatiquement faits par Eclipse, le type d'exception est précisé. Ces exceptions sont de sous classes de Exception
		System.out.println("exception due a la requete");
	}
	
	ArrayList<String> listeVilles     = new ArrayList<String>();
	ArrayList<String> listeLatitudes  = new ArrayList<String>();
	ArrayList<String> listeLongitudes = new ArrayList<String>();
	
	
	try {
		while(results.next()) {
			listeVilles.add(results.getString(1));
			listeLatitudes.add(results.getString(2));
			listeLongitudes.add(results.getString(3));
		}

	} catch (SQLException e) {
		e.printStackTrace();
	}

	ArrayList<String> carte = listeVilles;
	
	for(String string: listeLatitudes) {
		carte.add(string);
	}
	for(String string: listeLongitudes) {
		carte.add(string);
	}
	
	
	return carte;	
	
	}
	
	
	public static void main(String[] args) {
		RequeteVilleNombre bdd = new RequeteVilleNombre(20);
		ArrayList<String> carte = bdd.BDDconnexion();
		ArrayList<String> tableauVilles = bdd.getName(carte);
		ArrayList<Double> listeLatitudes = bdd.getLatitude(carte);
		ArrayList<Double> listeLongitudes = bdd.getLongitude(carte);
		System.out.println(tableauVilles);
		System.out.println(listeLatitudes);
		System.out.println(listeLongitudes);
		
	}

}