package mesComportements;

import java.io.IOException;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import mesAgents.AgentCentrale;
import tour.Tour;

/**
 * @Description: Class de behaviour de l'agent centrale dans laquelle on envoie la solution optimale aux agents heuristiques 
 * et reçoit message des agents heuristiques
 * @author: fangzhengjie
 * @Date: 2020年5月3日 上午9:51:17
 * @verson:1.0.0
 */
public class BehaviourCentrale extends TickerBehaviour {
	private AgentCentrale monAgent;				// à quel agent cette behaviour appartient
	private double bestDistance;				// variable qui enregistre la disrtance optimale pendant la communiquation avec autres agents
	private Tour bestTour;						// variable qui enregistre la solution(class Tour) optimale pendant la communiquation avec autres agents
	private Tour tourRS;						// variable qui met à jour la solution envoyé par agnteRS
	private Tour tourTabou;						// variable qui met à jour la solution envoyé par agnteTabou
	private Tour tourGA;						// variable qui met à jour la solution envoyé par agnteGA
	/**
	 * flag correspond au 3 agents heuristiques pour indiquer si agentCentrale a reçu message de chaque agent heuristique dans ce tour. 
	 * Seulement il dispose les message de tous les 3 agents heuristiques, il va send message aux ces derniers.
	 * flag=0: non dispose
	 * flag=1: dispose
	 */
	private int flagTabou;				
	private int flagRS;
	private int flagGA;
	private int i;								//nombre de fois que l'agentRS envoie memessage
	private int j;								//nombre de fois que l'agentRS reçoit memessage
	/**
	 * Constant qui sert à terminer le programme. 
	 * Supposons l’agent centrale reçoit de manière continue une nombre (noté nbVerificartionCurrent) de fois pour les quelles les 3 solution 
	 * envoyées par agents heuristiques sont pareils. 
	 * Si nbVerificartionCurrent est supérieur ou égale à NOMBRE_VÉRIFICAITION, le programme s’arrête. On a arrvié au solution optimale globale.
	 * nbVerificartion initiale = 0
	 */
	private final int NOMBRE_VERIFICATION=5;	
	private int nbVerificartionCurrent=0;
	
	public BehaviourCentrale(AgentCentrale monAgent,long milliSeconds) {
		super(monAgent,milliSeconds);
		/**
		 * On récupère le bestTour initiale, bestDistance initiale et temps déclenche de mon agent
		 * flag initial égale tous 0
		 */
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
		/**
		 * Si nbVerificartionCurrent est supérieur ou égale à NOMBRE_VÉRIFICAITION, l'agent va envoyer une Tour null à 3 agents heuristiques 
		 * pour arrêtrer le programme
		 */
		if(this.nbVerificartionCurrent>this.NOMBRE_VERIFICATION) {
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
		/**
		 * Si nbVerificartionCurrent est inférieur à NOMBRE_VÉRIFICAITION, 
		 * l'agent centrale va collecter les 3 solutions trouvées par agents heuristiques
		 * Il met à jour les état flag des agents heuristiques et la la solution optimale totale
		 */
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
//						System.out.println("Centra  |bestTourCurrent: "+this.bestTour.toString()+"; Distance "+this.bestTour.getDistance());
					}
				}
			}
		}catch (UnreadableException e) {
			e.printStackTrace();
		}
		
		/**
		 * Une fois que l'agent centrale dispose de toutes les 3 solutions, il met à jour nbVerificartionCurrent.
		 * Puis il envoie la solution optimale totale que il enregistre à 3 agents heuristiques.
		 */
		if( flagTabou==1 && flagRS==1  && flagGA==1){	
			if(this.tourRS.toString().equals(this.tourGA.toString()) && this.tourRS.toString().equals(this.tourTabou.toString())) {
				this.nbVerificartionCurrent++;
			}else {
				this.nbVerificartionCurrent=0;
			}
//			System.out.println("Centra  |nbEgaleCurrent: "+this.nbEgaleCurrent);
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
			/**
			 * À la fin de chaque tour d'envoie et reçu de message, on met les flags heuristiques aux 0 de nouveau.
			 */
			this.flagTabou=0;
			this.flagRS=0;
			this.flagGA=0;
		}
	}
}

