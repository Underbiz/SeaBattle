package client;

import java.awt.Graphics;
import java.util.ArrayList;

public class Shots {
	private final int BLOCK_SIZE;
	public ArrayList<Shot> shots;
	
	public Shots(int blockSize) {
		BLOCK_SIZE = blockSize;
		shots = new ArrayList<Shot>();
	}
	
	public void add(int x , int y) {
		shots.add(new Shot(x, y));
	}
	
	public boolean hitSamePlace(int x, int y) {
		for(Shot shot : shots) {
			if(shot.getX() == x && shot.getY() == y) {
				return true;
			}
		} return false;
	}
	
	public void paint(Graphics g) {
		for(Shot shot : shots) {
			shot.paint(g, BLOCK_SIZE);
		}
		
	}

	
}
