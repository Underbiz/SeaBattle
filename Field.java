package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import network.TCPConnection;
import network.TCPConnectionListener;

public class Field extends JFrame implements TCPConnectionListener {

	private TCPConnection connection;
	final String TITLE_OF_PROGRAM = "Battle Ship";

	final int FIELD_SIZE = 10;
	final int ENEMY_PANEL_SIZE = 400;
	final int ENEMY_BLOCK_SIZE = ENEMY_PANEL_SIZE / FIELD_SIZE;
	final int PLAYER_PANEL_SIZE = ENEMY_PANEL_SIZE / 2;
	final int PLAYER_BLOCK_SIZE = PLAYER_PANEL_SIZE / FIELD_SIZE;

	final int MOUSE_BUTTON_LEFT = 1;

	// Player player;
	Canvas enemyPanel, playerPanel;
	Fleet enemyFleet, myFleet;
	Shots enemyShots, myShots;
	int enemy_x_coord;
	int enemy_y_coord;
	Random random;
	boolean gameOver;
	boolean status = true;

	private JTextArea board;

	public Field(String ip, int port) {
		// this.player = player;
		try {
			connection = new TCPConnection(this, ip, 7777);
		} catch (IOException e) {
			System.out.println("\n" + "Connection exception: " + e);
		}
		
		enemyShots = new Shots(PLAYER_BLOCK_SIZE);
		myShots = new Shots(ENEMY_BLOCK_SIZE);
		myFleet = new Fleet(FIELD_SIZE, PLAYER_BLOCK_SIZE, false);
		
		setTitle(TITLE_OF_PROGRAM);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);

		enemyPanel = new Canvas();
		enemyPanel.setPreferredSize(new Dimension(ENEMY_PANEL_SIZE, ENEMY_PANEL_SIZE));
		enemyPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				if (status && !gameOver) {
					int x = e.getX() / ENEMY_BLOCK_SIZE;
					int y = e.getY() / ENEMY_BLOCK_SIZE;
					System.out.println("IS IS X = " + x);
					System.out.println("IS IS Y = " + y);
					if (e.getButton() == MOUSE_BUTTON_LEFT) {
						if (!myShots.hitSamePlace(x, y)) {
							connection.sendString("active");
							myShots.add(x, y);
							connection.sendString("shot:" + x + y);
							if (enemyFleet.checkHit(x, y)) {
								connection.sendString("inactive");
								status = true;
								if (!enemyFleet.checkSurvivors()) {
									board.append("\n" + "You win!");
									connection.sendString("gameOver");

								}
							} else {
								connection.sendString("active");
								status = false;

							}

						}
						enemyPanel.repaint();
						playerPanel.repaint();
						board.setCaretPosition(board.getText().length());

					}
				}
			}

		});

		playerPanel = new Canvas();
		playerPanel.setPreferredSize(new Dimension(PLAYER_PANEL_SIZE, PLAYER_PANEL_SIZE));
		playerPanel.setBackground(Color.white);
		JButton start = new JButton("Start");
		// Border //
		start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				start();

			}
		});
		start.setEnabled(false);

		
		JButton generate = new JButton("Fleet");
		generate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String myShipsOnString;
				myShipsOnString = disassembleTheFleet(myFleet.getArrayOfFleet());
				if (myShipsOnString.length() > 40) {
					myFleet = new Fleet(FIELD_SIZE, PLAYER_BLOCK_SIZE, false);
					myShipsOnString = disassembleTheFleet(myFleet.getArrayOfFleet());
				}
				String positions = getPositions(myFleet.getArrayOfFleet());
				String fleet = fleetAndPositions(myShipsOnString, positions);
				connection.sendString(fleet);
				start.setEnabled(true);

			}
		});
		
		JButton exit = new JButton("Exit");
		exit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
				
			}
		});

		board = new JTextArea();
		board.setEditable(false);
		JScrollPane scroll = new JScrollPane(board);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout());
		buttonPanel.add(start);
		buttonPanel.add(generate);
		buttonPanel.add(exit);

		JPanel generalPlayerPanel = new JPanel();
		generalPlayerPanel.setLayout(new BorderLayout());

		generalPlayerPanel.add(playerPanel, BorderLayout.NORTH);
		generalPlayerPanel.add(scroll, BorderLayout.CENTER);
		generalPlayerPanel.add(buttonPanel, BorderLayout.SOUTH);

		setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		add(enemyPanel);
		add(generalPlayerPanel);
		pack();
		setLocationRelativeTo(null);

	

		setVisible(true);

	}

	synchronized protected void shootEnemy(int x, int y) {

		if (!enemyShots.hitSamePlace(x, y)) {
			enemyShots.add(x, y);
			if (!myFleet.checkHit(x, y)) {
				board.append("\n" + (x + 1) + ":" + (y + 1) + " Enemy missed.");
				connection.sendString("active");
				return;
			} else {
				board.append("\n" + (x + 1) + ":" + (y + 1) + " Enemy hit the target.");
				board.setCaretPosition(board.getText().length());
				if (!myFleet.checkSurvivors()) {
					board.append("\n" + "Enemy win");
					JOptionPane.showInternalMessageDialog(Field.this, "You WIN!");
					connection.sendString("gameOver");

				} else {

				}

			}
		}
	}

	protected void start() {

		board.setText("New Game");
		gameOver = false;

		enemyPanel.repaint();
		playerPanel.repaint();

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new Field("172.16.1.37", 7777);

			}
		});

	}

	class Canvas extends JPanel { // for painting
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			int blockSize = (int) getSize().getWidth() / FIELD_SIZE;
			g.setColor(Color.lightGray);
			for (int i = 1; i < FIELD_SIZE; i++) {
				g.drawLine(0, i * blockSize, FIELD_SIZE * blockSize, i * blockSize);
				g.drawLine(i * blockSize, 0, i * blockSize, FIELD_SIZE * blockSize);
			}
			if (blockSize == PLAYER_BLOCK_SIZE && myFleet != null) {
				enemyShots.paint(g);
				myFleet.paint(g);
			}
			if (enemyFleet != null && blockSize == ENEMY_BLOCK_SIZE) {
				enemyFleet.paint(g);
				myShots.paint(g);

			}

		}
	}

	@Override
	public void onConnectionReady(TCPConnection tcpConnection) {
		// board.append("\n" + connection.toString());
		System.out.println("\n" + connection.toString());

	}

	@Override
	public void onDisconnect(TCPConnection tcpConnection) {
		// board.append("\n" + "Connection close...");
		System.out.println("\n" + "Connection close...");
		Field.this.repaint();

	}

	@Override
	public void onExeption(TCPConnection tcpConnection, Exception e) {
		// board.append("\n" + "Connection exception:" + e);
		System.out.println("\n" + "Connection exception:" + e);
	}

	@Override
	public void onReceiveSrting(TCPConnection tcpConnection, String value) {
		System.out.println("String recibido : " + value);
		if (value.startsWith("msg")) {
			System.out.println("THIS IS MSG");

			int index = value.indexOf(':');
			String msg = value.substring(index);
			printMsg(msg);

		} else if (value.startsWith("fleet")) {
			System.out.println("THIS IS FLEET");

			int index = value.indexOf(':');
			int index2 = value.indexOf("|");
			String blocks = value.substring(index + 1, index2);
			String positions = value.substring(index2 + 1);
			System.out.println(blocks);
			System.out.println(positions);
			ArrayList<Block> ships = transformStringToArrayList(blocks);
			ArrayList<Integer> position = transformToArrayListPositions(positions);
//			for (int i = 0; i < ships.size(); i++) {
//				System.out.println("[" + ships.get(i).getX() + ", " + ships.get(i).getY() + "]");
//			}
			enemyFleet = new Fleet(FIELD_SIZE, ENEMY_BLOCK_SIZE, ships, position, true);

		} else if (value.startsWith("shot")) {

			int index = value.indexOf(':');
			String coordinats_onString = value.substring(index + 1);

			int[] coordinats = transform_X_Y(coordinats_onString);

			int x = coordinats[0] ;
			int y = coordinats[1] ;
			enemyShots.add(x, y);
			board.append("\n" + "Enemy shot: " + (x + 1) + " " + (y + 1));
			if (myFleet.checkHit(x, y)) {
				if (!myFleet.checkSurvivors()) {
					board.append("\n" + "You lose!");
					JOptionPane.showInternalMessageDialog(Field.this, "You Lose!");
					enemyFleet.setHide(false);
				}
				playerPanel.repaint();

			}
		} else if (value.startsWith("act")) {
			status = true;
	

		} else if (value.startsWith("inact")) {
			status = false;
			

		} else if (value.equals("gameOver")) {
			gameOver = true;

		} else {

			System.out.println(value);
		}

	}

	

	private synchronized void printMsg(String value) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				board.append("\n" + value);

			}
		});
	}

	private String disassembleTheFleet(ArrayList<Ship> fleet) {

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < fleet.size(); i++) {
			ArrayList<Block> blocks = fleet.get(i).blocks;
			for (int h = 0; h < blocks.size(); h++) {
				if (blocks.get(h).isPartOfShip()) {
					int x = blocks.get(h).getX() + 1;
					int y = blocks.get(h).getY() + 1;
					if (x == 10) {
						x = 0;

					} else if (y == 10) {
						y = 0;
					}
					sb.append(x);
					sb.append(y);
				}

			}
		}

		String fleetInString = sb.toString();
		return fleetInString;
	}

	private String getPositions(ArrayList<Ship> fleet) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < fleet.size(); i++) {
			int position = fleet.get(i).getPosition();
			sb.append(position);
		}
		String positions = sb.toString();
		return positions;
	}

	private int[] transform_X_Y(String x_y) {

		int x = Character.getNumericValue(x_y.charAt(0));
		int y = Character.getNumericValue(x_y.charAt(1));

		int[] shot = { x, y };

		return shot;

	}

	private ArrayList<Integer> transformToArrayListPositions(String value) {
		ArrayList<Integer> positions = new ArrayList<Integer>();
		for (int i = 0; i < value.length(); i++) {
			positions.add(Character.getNumericValue(value.charAt(i)));
		}
		return positions;

	}

	private ArrayList<Block> transformStringToArrayList(String x_y) {
		System.out.println("This is lenght of String from function = " + x_y.length());
		ArrayList<Block> blocks = new ArrayList<Block>();
		int i = 0;
		while (i < x_y.length()) {
			if (i % 2 == 0) {
				int x = Character.getNumericValue(x_y.charAt(i));
				int y = Character.getNumericValue(x_y.charAt(i + 1));
				if (x == 0) {
					x = 10;

					blocks.add(new Block(x, y));

				} else if (y == 0) {

					y = 10;
					blocks.add(new Block(x, y));

				} else {
					blocks.add(new Block(x, y));

				}

			}
			i = i + 2;
		}
		System.out.println("This is size of blocks from function = " + blocks.size());
		return blocks;

	}

	private void moveStatus(String status) {

	}

	private String fleetAndPositions(String ships, String positions) {
		return "fleet:" + ships + "|" + positions;
	}

}
