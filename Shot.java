package client;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

public class Shot implements Serializable{
	private int x, y;
	private boolean isHit;
	private final int sizeOfBullet = 8;

	public Shot(int x , int y) {
		this.x = x;
		this.y = y;
		

	}

	
	public boolean isHit() {
		return isHit;
	}

	public void setHit(boolean isHit) {
		this.isHit = isHit;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void paint(Graphics g, int blockSize) {
		g.setColor(Color.gray);
		g.fillRect(x* blockSize + blockSize/2 - 3 , y* blockSize + blockSize/2 - 3, sizeOfBullet, sizeOfBullet);
	}


}
