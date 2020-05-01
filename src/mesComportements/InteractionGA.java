package mesComportements;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.util.leap.Serializable;
import mesAgents.AgentGA;
import mesMetaheuristiques.ModeleGA;
import mesMetaheuristiques.ModeleRS;
import tour.Tour;
import city.City;

public class InteractionGA extends OneShotBehaviour{
    private AgentGA monAgent;
    private double bestDistance;
    private Tour bestTour;
    private int NOMBRE_VERIFICATION=3;
    private long initTimeSMA;
    
    public InteractionGA(AgentGA monAgent, long initTimeSMA){
    	super(monAgent);
        this.monAgent = monAgent;
		this.bestTour=monAgent.getBestTour();
        this.bestDistance = monAgent.getBestDistance();
        this.initTimeSMA=initTimeSMA;
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
	    msgEnvoyer.addReceiver(new AID("AgentTabou@Plateforme_multiagnets_PVC",AID.ISGUID));
		monAgent.send(msgEnvoyer);
//		System.out.format("GA    |send message %d\n",i++);
		
		int nombreEgale=0;
		ACLMessage msgRecu=this.monAgent.receive();
//		if(msgRecu!=null)
//			System.out.format("GA    |receive message %d\n",j++);

		while(true) {
			if(msgRecu!=null) {
				try{
					if(nombreEgale>this.NOMBRE_VERIFICATION) {
						long overTime = System.currentTimeMillis();
						long excutionTime=overTime-initTimeSMA;
						System.out.println("GASMA |AgentGA was done!");
						System.out.println("GASMA |Best Tour: " + this.bestTour);
						System.out.println("GASMA |Final solution distance: " + this.bestDistance);
						System.out.println("GASMA |Le temps d'éxecution SMA est :"+ excutionTime+"ms");
						break;
					}
					Tour tour= (Tour)msgRecu.getContentObject();
//					System.out.println("GA    |I  get  a  tour: "+tour.toString()+"; Distance "+tour.getDistance());
//					System.out.println("GA    |bestTourCurrent: "+this.bestTour.toString()+"; Distance "+this.bestTour.getDistance());
					
					if(tour.toString().equals(this.bestTour.toString())) {
						nombreEgale++;
//						System.out.println("GA    |nombreEgale:"+nombreEgale);
					}else{
						nombreEgale=0;
						ModeleGA GA=new ModeleGA(30,0.15,tour);
						GA.Evolution();
						Tour currentTour = new Tour(GA.getShortestTour());
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
					msgEnvoyer.addReceiver(new AID("AgentTabou@Plateforme_multiagnets_PVC",AID.ISGUID));
					monAgent.send(msgEnvoyer);
//					System.out.format("GA    |send message %d\n",i++);
				}catch (UnreadableException e) {
					e.printStackTrace();
				}	
			}
			msgRecu=this.monAgent.receive();
//			if(msgRecu!=null)
//				System.out.format("GA    |receive message %d\n",j++);
		}
    block();    
    }
}

		  

