package client;


import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;


public class StartWindow extends JFrame{
	
		JButton generateButton = new JButton("Random Fleet");
		JButton connectButton = new JButton("Connect");
		JLabel infoFleet = new JLabel("Create random Fleet");
		JLabel infoToConnect = new JLabel("Start play");
		boolean switcher = false;
		
	public StartWindow(Player player) {
		
		setPreferredSize(new Dimension(400,100));
		setLayout(new GridLayout(2,2));
		//connectButton.setEnabled(false);
		add(infoFleet);
		add(infoToConnect);
		//add(generateButton);
		add(connectButton);
		
		setLocationRelativeTo(null);
		pack();
		setVisible(true);
		
		generateButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Fleet fleet = new Fleet(10, 200, false);
				player.setFleet(fleet);
				connectButton.setEnabled(true);
				generateButton.setEnabled(false);
				
			}
		});
		
		connectButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						new Field("192.168.0.111", 7777);

					}
				});
				StartWindow.this.dispose();
			}
			
		});
		
	}
}
