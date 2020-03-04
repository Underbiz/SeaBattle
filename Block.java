package client;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

public class Block implements Serializable{
	final private Color RED = Color.red;
	private int x , y;
	private Color color;
	public Block(int x, int y) {
		
		this.x = x;
		this.y = y;
		this.color = color.gray;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
	public boolean isAlive() {
		 return color != RED;
	}
	
	public boolean checkHit(int x , int y) {
		if(this.x == x && this.y == y) {
			color = RED;
			System.out.println("ROJO " + (x + 1) + " " + (y + 1));
			return true;
		} 
		
		return false;
		
		
		
	}
	
	public void paint(Graphics g, int blockSize, boolean hide) {
		if (!hide || (hide && color == RED)) {
            g.setColor(color);
            g.fill3DRect(x*blockSize + 1, y*blockSize + 1, blockSize - 2, blockSize - 2, true);
            
        }
	}
	
	public boolean isPartOfShip() {
		return true;
	}
	
}
