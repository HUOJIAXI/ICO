package mesComportements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

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
	private long initTimeSMA;
	private int NOMBRE_VERIFICATION=3;
	
	public InteractionTabou(AgentTabou monAgent, long initTimeSMA) {
		super(monAgent);
		this.monAgent=monAgent;
		this.bestTour=monAgent.getBestTour();
		this.bestDistance=monAgent.getBestDistance();
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
	    msgEnvoyer.addReceiver(new AID("AgentGA@Plateforme_multiagnets_PVC",AID.ISGUID));
		monAgent.send(msgEnvoyer);
//		System.out.format("Tabou |send message %d\n",i++);
		
		int nombreEgale=0;
		ACLMessage msgRecu=this.monAgent.receive();
//		if(msgRecu!=null)
//			System.out.format("Tabou |receive message %d\n",j++);

		while(true) {
			if(msgRecu!=null) {
				try{
					if(nombreEgale>this.NOMBRE_VERIFICATION) {
						long overTime = System.currentTimeMillis();
						long excutionTime=overTime-initTimeSMA;
						System.out.println("TbSMA |AgentTabou was done!");
						System.out.println("TbSMA |Best Tour: " + this.bestTour);
						System.out.println("TbSMA |Final solution distance: " + this.bestDistance);
						System.out.println("TbSMA |Le temps d'éxecution SMA est :"+ excutionTime +"ms");
						break;
					}
					Tour tour= (Tour)msgRecu.getContentObject();
//					System.out.println("Tabou |I  get  a  tour: "+tour.toString()+"; Distance "+tour.getDistance());
//					System.out.println("Tabou |bestTourCurrent: "+this.bestTour.toString()+"; Distance "+this.bestTour.getDistance());

					if(tour.toString().equals(this.bestTour.toString())){
						nombreEgale++;
//						System.out.println("Tabou |nombreEgale:"+nombreEgale);
					}else{
							nombreEgale=0;
							ModeleTabou tabou = new ModeleTabou(20, 50, 30, 20 );
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
//					System.out.format("Tabou |send message %d\n",i++);
				}catch (UnreadableException e) {
					e.printStackTrace();
				}
				
			}
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			msgRecu=this.monAgent.receive();
//			if(msgRecu!=null)
//				System.out.format("Tabou |receive message %d\n",j++);
		}
	block();
	}	

}
