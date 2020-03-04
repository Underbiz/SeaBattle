package client;

import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

public class Fleet  implements Serializable {

	
	private static final int[] PATTERN = {4,3,3,2,2,2,1,1,1,1};
	private int block_size;
	public ArrayList<Ship> fleet = new ArrayList<Ship>();
	private Random random;
	private boolean hide;
	
	Fleet(int fieldSize, int blockSize ,ArrayList<Block> blocks, ArrayList<Integer> positions, boolean hide){
		
			
			fleet.add(new Ship(blocks.get(0).getX() - 1, blocks.get(0).getY() - 1, 4, positions.get(0)));
			fleet.add(new Ship(blocks.get(4).getX() - 1, blocks.get(4).getY() - 1, 3, positions.get(1)));
			fleet.add(new Ship(blocks.get(7).getX() - 1, blocks.get(7).getY() - 1, 3, positions.get(2)));
			fleet.add(new Ship(blocks.get(10).getX() - 1, blocks.get(10).getY() - 1, 2, positions.get(3)));
			fleet.add(new Ship(blocks.get(12).getX() - 1, blocks.get(12).getY() - 1, 2, positions.get(4)));
			fleet.add(new Ship(blocks.get(14).getX() - 1, blocks.get(14).getY() - 1, 2, positions.get(5)));
			fleet.add(new Ship(blocks.get(16).getX() - 1, blocks.get(16).getY() - 1, 1, positions.get(6)));
			fleet.add(new Ship(blocks.get(17).getX() - 1, blocks.get(17).getY() - 1, 1, positions.get(7)));
			fleet.add(new Ship(blocks.get(18).getX() - 1, blocks.get(18).getY() - 1, 1, positions.get(8)));
			fleet.add(new Ship(blocks.get(19).getX() - 1, blocks.get(19).getY() - 1, 1, positions.get(9)));
			block_size = blockSize;
			this.hide = hide;
		
	}
	Fleet(int fieldSize, int blockSize, boolean hide) {
        random = new Random();
        for (int i = 0; i < PATTERN.length; i++) {
            Ship ship;
            do {
                int x = random.nextInt(fieldSize);
                int y = random.nextInt(fieldSize);
                int position = random.nextInt(2);
                ship = new Ship(x, y, PATTERN[i], position);
            } while (ship.isOutOfField(0, fieldSize - 1) || isOverlayOrTouch(ship));
            fleet.add(ship);
        }
        block_size = blockSize;
        this.hide = hide;
    }
	
	
	
	public void setHide(boolean value) {
		this.hide = value;
	}
	
	public void setBlockSize(int value) {
		this.block_size = block_size*2;
	}
	
	private boolean isOverlayOrTouch(Ship otherShip) {
		for(Ship ship : fleet) {
			if(ship.isOverlayOrTouch(otherShip)) {
				return true;
			}
		} return false;
	}
	
	public boolean checkHit(int x , int y) {
		for(Ship ship : fleet) 
			if(ship.checkHit(x, y)) 
				return true;
		 return false;
	}

	boolean checkSurvivors() {
		for(Ship ship : fleet) 
			if(ship.isAlive()) 
				return true;
		return false;
	}
	
	public ArrayList<Ship> getArrayOfFleet(){
		
		
		
		return fleet;
		
	}

	public void setArrayOfFleet(ArrayList<Ship> value) {
		this.fleet = value;
	}
					
	public void paint(Graphics g) {
		for(Ship ship : fleet) {
			ship.paint(g, block_size, hide);
		}
	}

}
