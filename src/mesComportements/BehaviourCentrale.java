package mesComportements;

import java.io.IOException;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import mesAgents.AgentCentrale;
import mesAgents.AgentRS;
import mesMetaheuristiques.ModeleRS;
import tour.Tour;

public class BehaviourCentrale extends TickerBehaviour {
	private AgentCentrale monAgent;
	private double bestDistance;
	private Tour bestTour;
	private Tour tourRS;
	private Tour tourTabou;
	private Tour tourGA;
	private int flagTabou;
	private int flagRS;
	private int flagGA;
	private int i;
	private int j;
	private final int NOMBRE_VERIFICATION=3;
	private int nbEgaleCurrent=0;
	
	public BehaviourCentrale(AgentCentrale monAgent,long milliSeconds) {
		super(monAgent,milliSeconds);
		this.monAgent=monAgent;
		this.bestTour=monAgent.getBestTour();
		this.bestDistance=monAgent.getBestDistance();
		this.flagTabou=0;
		this.flagRS=0;
		this.flagGA=0;
		this.i=1;
		this.j=1;

	}
	
	public void onTick() {
		if(this.nbEgaleCurrent>this.NOMBRE_VERIFICATION) {
			ACLMessage msgEnvoyer = new ACLMessage(ACLMessage.REQUEST);
			try {
			    msgEnvoyer.setContentObject(null);
			}catch (IOException e) {
			    e.printStackTrace();
			}
			msgEnvoyer.addReceiver(new AID("AgentRS@Plateforme_multiagnets_PVC",AID.ISGUID));
			msgEnvoyer.addReceiver(new AID("AgentGA@Plateforme_multiagnets_PVC",AID.ISGUID));
			msgEnvoyer.addReceiver(new AID("AgentTabou@Plateforme_multiagnets_PVC",AID.ISGUID));
			monAgent.send(msgEnvoyer);
			System.out.format("Centra  |send message %d\n",i++);
			System.out.println("Centra  |NOMBRE_EGALE VÉFIRIÉ!");
			System.out.println("Agent Centrale was done!");
			stop();
		}
		try {
			ACLMessage msgRecu=this.monAgent.receive();
			if(msgRecu!=null) {
				System.out.format("Centra  |receive message %d\n",j++);
				Tour tour= (Tour)msgRecu.getContentObject();
				if(tour!=null) {
					System.out.println("Centra  |I  get  a  tour from "+msgRecu.getSender().getLocalName()+": "+tour.toString()+"; Distance "+tour.getDistance());
					if(msgRecu.getSender().getLocalName().equals("AgentTabou")) 
						this.flagTabou=1;
						this.tourTabou=tour;
					if(msgRecu.getSender().getLocalName().equals("AgentRS")) 
						this.flagRS=1;
						this.tourRS=tour;
					if(msgRecu.getSender().getLocalName().equals("AgentGA")) 
						this.flagGA=1;
						this.tourGA=tour;
					if(tour.getDistance()<this.bestDistance) {
						this.bestTour=tour;
						this.bestDistance=tour.getDistance();
						System.out.println("Centra  |bestTourCurrent: "+this.bestTour.toString()+"; Distance "+this.bestTour.getDistance());
					}
				}
			}
		}catch (UnreadableException e) {
			e.printStackTrace();
		}
		
		System.out.println("flagTabou "+flagTabou+" "+"flagRS "+flagRS+" "+"flagGA "+flagGA);	
		if( flagTabou==1 && flagRS==1  && flagGA==1){	
			if(this.tourRS.toString().equals(this.tourGA.toString()) && this.tourRS.toString().equals(this.tourTabou.toString())) {
				this.nbEgaleCurrent++;
			}else {
				this.nbEgaleCurrent=0;
			}
			System.out.println("Centra  |nbEgaleCurrent: "+this.nbEgaleCurrent);
			ACLMessage msgEnvoyer = new ACLMessage(ACLMessage.REQUEST);
			try {
			    msgEnvoyer.setContentObject(this.bestTour);
			}catch (IOException e) {
			    e.printStackTrace();
			}
			msgEnvoyer.addReceiver(new AID("AgentRS@Plateforme_multiagnets_PVC",AID.ISGUID));
			msgEnvoyer.addReceiver(new AID("AgentGA@Plateforme_multiagnets_PVC",AID.ISGUID));
			msgEnvoyer.addReceiver(new AID("AgentTabou@Plateforme_multiagnets_PVC",AID.ISGUID));
			monAgent.send(msgEnvoyer);
			System.out.format("Centra  |send message %d\n",i++);
			this.flagTabou=0;
			this.flagRS=0;
			this.flagGA=0;
		}
	}
}

