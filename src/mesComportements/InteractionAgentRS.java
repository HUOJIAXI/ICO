package mesComportements;

import java.io.IOException;
import java.util.ArrayList;

import city.City;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.util.leap.Serializable;
import mesAgents.AgentRecuitSimule;
import mesMetaheuristiques.ModeleRecuitSimule;
import tour.Tour;

public class InteractionAgentRS extends CyclicBehaviour {
	private AgentRecuitSimule monAgent;
	private float bestDistance;
	private Tour bestTour;
	private int NOMBRE_VERIFICATION=5;
	
	public InteractionAgentRS(AgentRecuitSimule monAgent) {
		this.monAgent=monAgent;
		this.bestDistance=monAgent.getBestDistance();
	}
	
	public void action() {
		ACLMessage msgRecu=this.monAgent.receive();
		if(msgRecu!=null) {
			try{
				Tour tour= (Tour)msgRecu.getContentObject();
				if(tour==this.bestTour) {
					int nombreEgale=1;
					while(nombreEgale!=0) {
						msgRecu=this.getAgent().receive();
						tour= (Tour)msgRecu.getContentObject();
						if(tour==this.bestTour){
							nombreEgale++;
						}else {
							if(nombreEgale>this.NOMBRE_VERIFICATION) {
								block();
							}
							nombreEgale=0;		
						}
					}
				}
				ModeleRecuitSimule rs=this.monAgent.getModeleRecuitSimule();
			    rs.reInitTour(tour);
			    Tour currentTour = rs.recuitSimule(); 
			    float currentDistance=currentTour.getDistance();
			    if(this.bestDistance>currentDistance) {
			    	this.bestDistance=currentDistance;
			    	this.bestTour=currentTour;
			    	this.monAgent.setBestDistance(currentDistance);
			    	this.monAgent.setBestTour(currentTour);
			    }
			    
			    ACLMessage msgEnvoyer = new ACLMessage(ACLMessage.REQUEST);
			    try {
			    	msgEnvoyer.setContentObject(this.bestTour);
			    }catch (IOException e) {
			    	e.printStackTrace();
			    }
			}catch (UnreadableException e) {
				e.printStackTrace();
			}
		      
	    
		}
	}
	

}
