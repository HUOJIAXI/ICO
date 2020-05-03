package main;

import java.sql.SQLException;

import city.City;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import main.GUI;
import tour.Tour;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import city.City;
import data.RequeteVille;
import jade.core.Agent;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import mesAgents.AgentRS;
import mesMetaheuristiques.ModeleRS;
import tour.Tour;
/** 
 * @Description: Class de l'intrface graphique, on exécute la main fonction de cette class pour faire apparaître une interface
 * @author: fangzhengjie
 * @Date: 2020年5月3日 上午9:03:04
 * @verson:1.0.0
 */
public class GUI extends JFrame{
	private JTextField depart;
	private JTextArea villeAvisiter;

	public GUI() {
		super("Cherchez la meilleure trajet pour: ");

		
		JPanel p=new JPanel();
		p.setLayout(new GridLayout(3,2));
		
		p.add(new JLabel("Départ:"));
		this.depart=new JTextField(15);
		p.add(this.depart);
		
		p.add(new JLabel("Villes à visiter:"));
		villeAvisiter=new JTextArea("");
		p.add(this.villeAvisiter);
		
		this.getContentPane().add(p,BorderLayout.CENTER);
		JButton button=new JButton("Confirmer");
	
		button.addActionListener(new ActionListener() {
			/**
			 * @Description: Une fois l'utilisateur clique sur bouton "Confirmer", ActionListener va récuprer les informations des villes correspndant et déclencher les 4 agents
			 * @param:une ActionEvent cliquer sur bouton
			 * @return:void
			 */
			public void actionPerformed(ActionEvent event) {
				if(event.getSource()==button) {
					try {
						String cityDepart=depart.getText();
						String cityList []=villeAvisiter.getText().split(" "); //输入的途经城市中不用包含起点
						RequeteVille rvDepart=new RequeteVille(cityDepart);
						City depart=rvDepart.chercherVille();
						ArrayList<City> cityArrayList=new ArrayList<City>();
						cityArrayList.add(depart);
						for(int i=0;i<cityList.length;i++) {
							String cityString=cityList[i];
							RequeteVille rvVille=new RequeteVille(cityString);
							City ville=rvVille.chercherVille();
							cityArrayList.add(ville);
						}
						Tour tourAInit =new Tour(depart,cityArrayList);
						
						jade.core.Runtime rt=jade.core.Runtime.instance();
						
						AgentController AgentRS,AgentGA,AgentTabou;
						
						ProfileImpl mainPlateforme= new ProfileImpl(null,2020,"Plateforme_multiagnets_PVC");
						AgentContainer ac=rt.createMainContainer(mainPlateforme);
						
						try {
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
					}catch(SQLException e) {
						e.printStackTrace();
					}
					
				}
			}	
		});
		
		
		p= new JPanel();
		p.add(button);
		getContentPane().add(p,BorderLayout.SOUTH);
	}
		
	/**
	 * @Description: traiter les texts tapées par utilisateur, et cherchez les données des villes correspondant dans la base de donnée et finalement générer une Tour initiale
	 * @param:void
	 * @return: la Tour initiale générée
	 */
	public Tour getTourInit() throws SQLException {
		Tour tour=null;
		String cityDepart=depart.getText();
		String cityList []=villeAvisiter.getText().split(" ");
		RequeteVille rvDepart=new RequeteVille(cityDepart);
		City depart=rvDepart.chercherVille();
		ArrayList<City> cityArrayList=new ArrayList();
		cityArrayList.add(depart);
		for(int i=0;i<cityList.length;i++) {
			String cityString=cityList[i];
			RequeteVille rvVille=new RequeteVille(cityString);
			City ville=rvVille.chercherVille();
			cityArrayList.add(ville);
		}
		tour =new Tour(depart,cityArrayList);
		return tour;	
	}
	
	/**
	 * @Description: faire appara[itre l'interface graphique
	 * @param:void
	 * @return:void
	 */
	public void showGui() {
		pack();
		super.setVisible(true);
	}
	
	
	public static void main(String[] args) {
		GUI monGUI = new GUI();
		monGUI.showGui();
			
	}
}

