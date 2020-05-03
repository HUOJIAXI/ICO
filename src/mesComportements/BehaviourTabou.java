package mesComportements;

import java.io.IOException;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import mesAgents.AgentTabou;
import mesMetaheuristiques.ModeleTabou;
import tour.Tour;

/**
 * @Description:Class behaviour de agentTabou dans laquelle on envoie la solution au agent centrale et reçoit message du agent centrale
 * @author: fangzhengjie
 * @Date: 2020年5月3日 上午10:29:55
 * @verson:1.0.0
 */
public class BehaviourTabou extends OneShotBehaviour {
	private AgentTabou monAgent;		// à quel agent cette behaviour appartient
	private double bestDistance;		// variable qui enregistre la disrtance optimale pendant la communiquation avec autres agents
	private Tour bestTour;				// variable qui enregistre la solution(class Tour) optimale pendant la communiquation avec autres agents
	private long initTimeSMA;			// temps déclenche de partie SMA, on l'utilise oour calculer le temps totale utilisé pour l'agentRS
	
	public BehaviourTabou(AgentTabou monAgent, long initTimeSMA) {
		super(monAgent);
		/**
		 * On récupère le bestTour initiale, bestDistance initiale et temps déclenche de mon agent
		 */
		this.monAgent=monAgent;
		this.bestTour=monAgent.getBestTour();
		this.bestDistance=monAgent.getBestDistance();
		this.initTimeSMA=initTimeSMA;
	}
	
	public void action() {
		int i=1;		//nombre de fois que l'agentRS envoie memessage
		int j=1;		//nombre de fois que l'agentRS reçoit memessage
		ACLMessage msgEnvoyer = new ACLMessage(ACLMessage.REQUEST);
	    try {
	    	msgEnvoyer.setContentObject(this.bestTour);
	    }catch (IOException e) {
	    	e.printStackTrace();
	    }
	    msgEnvoyer.addReceiver(new AID("AgentCentrale@Plateforme_multiagnets_PVC",AID.ISGUID));
		monAgent.send(msgEnvoyer);
		System.out.format("Tabou   |send message %d\n",i++);
		
		ACLMessage msgRecu=this.monAgent.receive();
		if(msgRecu!=null)
			System.out.format("Tabou   |receive message %d\n",j++);

		while(true) {
			if(msgRecu!=null) {
				try{
					Tour tour= (Tour)msgRecu.getContentObject();
					/**
					 * Quand l'agent heuristique reçoit une Tour null (qui inplique l'agent centrale atteint le NOMBRE_VÉRIFICATION),
					 * behaviour de l'agent heuristique se termine.
					 */
					if(tour==null) {
						long overTime = System.currentTimeMillis();
						long excutionTime=overTime-initTimeSMA;
						System.out.println("TbSMA   |AgentTabou was done!");
						System.out.println("TbSMA   |Best Tour: " + this.bestTour);
						System.out.println("TbSMA   |Final solution distance: " + this.bestDistance);
						System.out.println("TbSMA   |Le temps d'éxecution SMA est :"+ excutionTime +"ms");	
						break;
					}
					/**
					 * Quand l'agent heuristique reçoit une Tour non null (qui inplique l'agent centrale ne atteint pas le NOMBRE_VÉRIFICATION),
					 * l'agent heuristique compare cette solution reçu et sa solution optimale courent. 
					 * Si solution reçu n‘est pas égale à sa solution optimale courent, il il utilise ce solution reçu comme solution initiale 
					 * et exécute le heuristique de nouveau. 
					 * Après avoir obtenu une nouvelle solution optimale, il renouvelle son solution optimale courent et envoie cette solution au agent Centrale.
					 */
					else {
						System.out.println("Tabou   |I  get  a  tour: "+tour.toString()+"; Distance "+tour.getDistance());
//						System.out.println("Tabou   |bestTourCurrent: "+this.bestTour.toString()+"; Distance "+this.bestTour.getDistance());
						if(!tour.toString().equals(this.bestTour.toString())){
							ModeleTabou tabou = new ModeleTabou(70, 90, 245,98);
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
						msgEnvoyer.addReceiver(new AID("AgentCentrale@Plateforme_multiagnets_PVC",AID.ISGUID));
						monAgent.send(msgEnvoyer);
						System.out.format("Tabou   |send message %d\n",i++);
					}
				}catch (UnreadableException e) {
					e.printStackTrace();
				}
				
			}
			/**
			 * Après avoir envoyé la solution au agent Centrale,l'agent heuristique continue de détecter le message reçu
			 */
			msgRecu=this.monAgent.receive();
			if(msgRecu!=null)
				System.out.format("Tabou   |receive message %d\n",j++);
		}
	block();
	}	

}
