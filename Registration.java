package client;



import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.NumberFormatter;



public class Registration extends JFrame {

	JPanel info = new JPanel();
	JPanel namePanel = new JPanel();
	JPanel secondNamePanel = new JPanel();
	JPanel mailPanel = new JPanel();
	JPanel ipPanel = new JPanel();
	JPanel panelForButton = new JPanel();
	JPanel portPanel = new JPanel();
	JPanel passwordPanel = new JPanel();

	public String nickName;
	
	private int age;
	private String mail;
	private String passWord;
	private String ip;
	private int port;

	public Registration() {
		setPreferredSize(new Dimension(350, 270));
		//setLocationRelativeTo(null);
		setLocation(558, 318);
		setLayout(new FlowLayout());
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel general_panel = new JPanel();
		general_panel.setPreferredSize(new Dimension(300, 230));
		general_panel.setLayout(new GridLayout(7, 1));

		add(general_panel);

		info.setLayout(new FlowLayout());
		JLabel info_lable = new JLabel("Info about player: ");
		info.add(info_lable);
		info.setSize(250, 30);
		info_lable.setFont(new Font("Serif", Font.BOLD, 22));

		namePanel.setLayout(new GridLayout(1, 2));
		JLabel name_label = new JLabel("Nick name: ");
		JTextField inputName = new JTextField(10);
		namePanel.add(name_label);
		namePanel.add(inputName);
		
		mailPanel.setLayout(new GridLayout(1, 2));
		JLabel mail_label = new JLabel("Mail: ");
		JTextField inputMail = new JTextField(30);
		mailPanel.add(mail_label);
		mailPanel.add(inputMail);
		
		passwordPanel.setLayout(new GridLayout(1, 2));
		JLabel pass_label = new JLabel("Password: ");
		JTextField inputPass = new JTextField(30);
		passwordPanel.add(pass_label);
		passwordPanel.add(inputPass);

		ipPanel.setLayout(new GridLayout(1, 2));
		JLabel ip_lable = new JLabel("Ip:");
		JTextField inputIp = new JTextField(20);
		ipPanel.add(ip_lable);
		ipPanel.add(inputIp);
		
		portPanel.setLayout(new GridLayout(1,2));
		JLabel port_lable = new JLabel("Port:");
		JComboBox<Integer> portBox = new JComboBox<Integer>();
		for (int i = 1; i <= 9999; i++) {
			portBox.addItem(i);
		}
		portPanel.add(port_lable);
		portPanel.add(portBox);
		

		panelForButton.setLayout(new FlowLayout());
		JButton signUpButton = new JButton("Sign up");
		//registration.setEnabled(false);
		panelForButton.add(signUpButton);

		
		

		JPanel delimiter = new JPanel();
		delimiter.setPreferredSize(new Dimension(300, 1));

		general_panel.add(info);
		general_panel.add(delimiter);
		general_panel.add(namePanel);
		general_panel.add(mailPanel);
		general_panel.add(passwordPanel);
		
		general_panel.add(panelForButton);

		
		
		signUpButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				nickName = inputName.getText().toString();
				mail = inputMail.getText().toString();
				passWord = inputPass.getText().toString();
				//ip = inputIp.getText();
			//	port = (int) portBox.getSelectedItem();
				
				
				if (nickName.length() < 2) {
					JOptionPane.showMessageDialog(null, "Nick Name should be more than two characters");
					inputName.setText("");
					
					
				} else if (passWord.length() < 5){ 
					JOptionPane.showMessageDialog(null, "The Password should be more than five characters");
					inputPass.setText("");

				} else if (!checkMail(mail)) {
					JOptionPane.showMessageDialog(null, "Incorrect e-mail!");
					inputMail.setText("");
				} else {
					// mySQL add
					Player player = new Player(nickName, mail , passWord );
					enterDatasToMySQL(mail, passWord, nickName);
					new StartWindow(player);
					Registration.this.dispose();
				}
				
				
			}
		});
		pack();
		setVisible(true);

	}
	
	public boolean checkMail(String mail) {
		if(mail.contains("@") && mail.contains(".")) {
			return true;
		}
		return false;
				
	}
	
	public void enterDatasToMySQL(String mail, String password, String nickname) {
		final String URL = "jdbc:mysql://127.0.0.1:3306/bdaccount";
		final String LOGIN = "root";
		final String PASSWORD = "gm20479dank";

		String sql = "SELECT * FROM players";

		try (Connection conn = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {

			String query = " insert into accounts (mail, password, nickname)" + " values (?, ?, ?)";
			PreparedStatement preparedStmt = conn.prepareStatement(query);

			
			preparedStmt.setString(1, mail);
			preparedStmt.setString(2, password);
			preparedStmt.setString(3, nickname);

			preparedStmt.execute();

			conn.close();

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
