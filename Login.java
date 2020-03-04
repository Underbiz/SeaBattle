package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Login extends JFrame{
	JPanel loginPanel = new JPanel();
	JPanel buttonsPanel = new JPanel();
	String nickname;
	public Login() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(225, 150));
		setLayout(new BorderLayout());
		
		loginPanel.setLayout(new GridLayout(5,1));
		JLabel mailLabel = new JLabel("Mail: ");
		JTextField inputMail = new JTextField(30);
		inputMail.setText("joodkd@gmail.com kklimovds@gmail.com");
		JLabel passLabel = new JLabel("Password: ");
		JTextField inputPass = new JTextField(30);
		inputPass.setText("dankrest	restdank");

		loginPanel.add(mailLabel);
		loginPanel.add(inputMail);
		loginPanel.add(passLabel);
		loginPanel.add(inputPass);
		
		buttonsPanel.setLayout(new GridLayout(1,2));
		JButton signIn = new JButton("Sign-In");
		JButton registr  = new JButton("Registration");
		buttonsPanel.add(registr);
		buttonsPanel.add(signIn);
		
		add(loginPanel, BorderLayout.CENTER);
		add(buttonsPanel,BorderLayout.SOUTH);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		
		registr.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new Registration();
				Login.this.dispose();
				
			}
		});
		
		signIn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// mySQL check 
				String login = inputMail.getText().toString();
				String pass = inputPass.getText().toString();
				
				if(checkPassWord(login, pass)) {
					JOptionPane.showMessageDialog(null, "Access!");
					new StartWindow(new Player(nickname, login, pass));
					Login.this.dispose();
					
				} else {
					JOptionPane.showMessageDialog(null, "This e-mail didn't exist or wrrone password!");
					inputMail.setText("");
					inputPass.setText("");
				}
				
				
				
				
			}
		});
		
		
	}
	public static void main(String[] args) {
		new Login();

	}
	
	public boolean checkPassWord(String mail, String password) {

		boolean check = false;
		
		int passHash = password.hashCode();

		final String URL = "jdbc:mysql://127.0.0.1:3306/bdaccount";
		final String LOGIN = "root";
		final String PASSWORD = "gm20479dank";

		String sql = "SELECT password FROM accounts WHERE mail=\'" + mail + "\' AND password=\'" + password + "\'";

		try (Connection conn = DriverManager.getConnection(URL, LOGIN, PASSWORD);
				Statement statement = conn.createStatement()) {

			ResultSet resultSet = statement.executeQuery(sql);
	
			

			while (resultSet.next()) {

				check = true;
				
			}


		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return check;

	}

}
