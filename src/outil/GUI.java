package outil;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import jade.core.Agent;
import mesAgents.AgentRecuitSimule;

public class GUI extends JFrame{
	private AgentRecuitSimule monAgent;
	private JTextField depart;
	private JTextArea villeAvisiter;
	
	public GUI(AgentRecuitSimule agent) {
		super("Cherchez la meilleure trajet pour: ");
		this.monAgent=agent;
		
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
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				String cityDepart=depart.getText();
				String cityList []=villeAvisiter.getText().split(" ");
				
				for(int i=0;i<cityList.length;i++) {
					String city=cityList[i];
					
				}
			}
			
		});
		
	}
	
}
