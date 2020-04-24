package plateforme;


import java.sql.SQLException;

import city.City;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import outil.GUI;
import tour.Tour;

public class Plateforme {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		jade.core.Runtime rt=jade.core.Runtime.instance();
				
		AgentController AgentRS,AgentGA,AgentTabou;
		
		ProfileImpl mainPlateforme= new ProfileImpl(null,2020,"Plateforme_multiagnets_PVC");
		AgentContainer ac=rt.createMainContainer(mainPlateforme);
		
		try {
			Tour tourAInit=new Tour();
			tourAInit.setDepart(new City("Bordeaux", 44.8378,-0.5792));
			tourAInit.addCity(new City("Bordeaux", 44.8378,-0.5792));
			tourAInit.addCity(new City("Lyon", 45.7640,4.8357));
			tourAInit.addCity(new City("Nantes", 47.2184,-1.5536));
			tourAInit.addCity(new City("Paris",48.8566,2.3522));
			tourAInit.addCity(new City("Marseille", 43.2965,5.3698));
			tourAInit.addCity(new City("Dijon", 47.3220,5.0415));
			Tour[] argsTour=new Tour[1];
			argsTour[0]=tourAInit;
			
			AgentRS=ac.createNewAgent("AgentRS", "mesAgents.AgentRS",argsTour);
			AgentRS.start();
		
			AgentGA=ac.createNewAgent("AgentGA", "mesAgents.AgentGA",argsTour);
			AgentGA.start();
			
			AgentTabou=ac.createNewAgent("AgentTabou", "mesAgents.AgentTabou",argsTour);
			AgentTabou.start();
			
		}catch(StaleProxyException e) {
			e.printStackTrace();
		}
	}

}
