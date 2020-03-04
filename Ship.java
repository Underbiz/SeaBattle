package client;

import java.awt.Graphics;
import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Ship implements Serializable {

	ArrayList<Block> blocks = new ArrayList<Block>();
	int position;

	public Ship(int x, int y, int lenght, int position) {
		this.position = position;

		for (int i = 0; i < lenght; i++) {
			blocks.add(new Block((x + i * ((position == 1) ? 0 : 1)), y + i * ((position == 1) ? 1 : 0)));

		}
	}

	public boolean isOutOfField(int bottom, int top) {
		for (Block block : blocks)
			if (block.getX() < bottom || block.getX() > top || block.getY() < bottom || block.getY() > top)
				return true;
		return false;

	}
	
	public int getPosition() {
		return position;
	}

	public boolean isOverlayOrTouch(Ship otherShip) {
		for (Block block : blocks)
			if (otherShip.isOverlayOrTouchBlock(block))
				return true;
		return false;

	}

	public boolean isOverlayOrTouchBlock(Block otherBlock) {
		for (Block block : blocks)
			for (int x = -1; x < 2; x++)
				for (int y = -1; y < 2; y++)
					if (otherBlock.getX() == block.getX() + x && otherBlock.getY() == block.getY() + y)
						return true;
		return false;
	}

	public boolean checkHit(int x, int y) {
		for (Block block : blocks)
			if (block.checkHit(x, y))
				return true;
		return false;

	}
	
	public boolean isAlive() {
		for(Block block : blocks) 
			if(block.isAlive()) 
				return true;
		return false;
	}
	
	public boolean isItShip() {
		return true;
	}
	
	public void paint(Graphics g, int blockSize, boolean hide) {
		for(Block block : blocks) {
			block.paint(g, blockSize, hide);
		}
	}
}
