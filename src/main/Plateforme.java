package main;


import java.sql.SQLException;
import java.util.ArrayList;

import city.City;
import data.RequeteVilleNombre;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import tour.Tour;
/**
 * @Description: Plateforme dans lequel on va tester et comparer les performances de différents algorithmes pour N villes données 
 * @author: fangzhengjie
 * @Date: 2020年5月3日 上午9:12:58
 * @verson:1.0.0
 */
public class Plateforme {
	private static int NB_VILLE=70; // une nombre de ville N fixé par l'utilisateur

	public static void main(String[] args) {
		jade.core.Runtime rt=jade.core.Runtime.instance();
				
		AgentController AgentRS,AgentGA,AgentTabou,AgentCentrale;
		
		ProfileImpl mainPlateforme= new ProfileImpl(null,2020,"Plateforme_multiagnets_PVC");
		AgentContainer ac=rt.createMainContainer(mainPlateforme);
		
		try {
			/**
			 * Cherchez N villes cherchez aléatroire dans la base de donnée et générer une Tour initiale
			 */
			RequeteVilleNombre requeteVilleNobre = new RequeteVilleNombre(NB_VILLE);
			ArrayList<String> carte = requeteVilleNobre.BDDconnexion();
			ArrayList<String> tableauVilles = requeteVilleNobre.getName(carte);
			ArrayList<Double> listeLatitudes = requeteVilleNobre.getLatitude(carte);
			ArrayList<Double> listeLongitudes = requeteVilleNobre.getLongitude(carte);
			Tour tourAInit=new Tour();
			tourAInit.setDepart(new City(tableauVilles.get(0), listeLatitudes.get(0),listeLongitudes.get(0)));
			for(int i=0;i<tableauVilles.size();i++) {
				City city=new City(tableauVilles.get(i),listeLatitudes.get(i),listeLongitudes.get(i));
				tourAInit.addCity(city);
			}
			
			/**
			 * On transmet la tour initial comme une parametre de agent en le créant
			 */
			Tour[] argsTour=new Tour[1];
			argsTour[0]=tourAInit;
			
			AgentRS=ac.createNewAgent("AgentRS", "mesAgents.AgentRS",argsTour);
			AgentRS.start();
	
			AgentGA=ac.createNewAgent("AgentGA", "mesAgents.AgentGA",argsTour);
			AgentGA.start();
			
			AgentTabou=ac.createNewAgent("AgentTabou", "mesAgents.AgentTabou",argsTour);
			AgentTabou.start();
			
			AgentCentrale=ac.createNewAgent("AgentCentrale", "mesAgents.AgentCentrale",argsTour);
			AgentCentrale.start();
			
		}catch(StaleProxyException e) {
			e.printStackTrace();
		}
	}

}
