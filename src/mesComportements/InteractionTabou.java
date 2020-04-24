package mesComportements;

import java.io.IOException;
import java.util.ArrayList;

import city.City;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.util.leap.Serializable;
import mesAgents.AgentRS; 
import mesAgents.AgentTabou;
import mesMetaheuristiques.ModeleRS;
import mesMetaheuristiques.ModeleTabou;
import tour.Tour;

public class InteractionTabou extends OneShotBehaviour {
	private AgentTabou monAgent;
	private double bestDistance;
	private Tour bestTour;
	private int NOMBRE_VERIFICATION=5;
	
	public InteractionTabou(AgentTabou monAgent) {
		this.monAgent=monAgent;
		this.bestDistance=monAgent.getBestDistance();
	}
	
	public void action() {
		int i=1;
		int j=1;
		ACLMessage msgEnvoyer = new ACLMessage(ACLMessage.REQUEST);
	    try {
	    	msgEnvoyer.setContentObject(this.bestTour);
	    }catch (IOException e) {
	    	e.printStackTrace();
	    }
	    msgEnvoyer.addReceiver(new AID("AgentRS@Plateforme_multiagnets_PVC",AID.ISGUID));
	    msgEnvoyer.addReceiver(new AID("AgentGA@Plateforme_multiagnets_PVC",AID.ISGUID));
		monAgent.send(msgEnvoyer);
		System.out.format("Tabou |send message %d\n",i++);
		
		int nombreEgale=0;
		ACLMessage msgRecu=this.monAgent.receive();
		System.out.format("Tabou |receive message %d\n",j++);

		while(true) {
			if(msgRecu!=null) {
				try{
					if(nombreEgale>this.NOMBRE_VERIFICATION) {
						System.out.println("AgentTabou was done!");
						break;
					}
					Tour tour= (Tour)msgRecu.getContentObject();
					if(tour==this.bestTour) {
						nombreEgale++;
					}else{
							nombreEgale=0;
							ModeleTabou tabou = new ModeleTabou(6, 30, 10, 4);
						    tabou.init(tour);
						    Tour currentTour = tabou.getBestTour(); 
						    double currentDistance=currentTour.getDistance();
						    if(this.bestDistance>currentDistance) {
						    	this.bestDistance=currentDistance;
						    	this.bestTour=currentTour;
						    	this.monAgent.setBestDistance(currentDistance);
						    	this.monAgent.setBestTour(currentTour);
						    }		
					}
					msgEnvoyer = new ACLMessage(ACLMessage.REQUEST);
					try {
					    msgEnvoyer.setContentObject(this.bestTour);
					}catch (IOException e) {
					    e.printStackTrace();
					}
					msgEnvoyer.addReceiver(new AID("AgentRS@Plateforme_multiagnets_PVC",AID.ISGUID));
					msgEnvoyer.addReceiver(new AID("AgentGA@Plateforme_multiagnets_PVC",AID.ISGUID));
					monAgent.send(msgEnvoyer);
					System.out.format("Tabou |send message %d\n",i++);
				}catch (UnreadableException e) {
					e.printStackTrace();
				}
			}
			msgRecu=this.getAgent().receive();
			System.out.format("Tabou |receive message %d\n",j++);
		}
	block();
	}	

}
