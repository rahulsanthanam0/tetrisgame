
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import resources.SoundPlayer;

public abstract class TetrisObject implements KeyListener {

	protected Block[] blocks;
	protected int x;
	protected int y;
	protected Color color;
	protected boolean toBeBuried;
	
	private SoundPlayer arrow, shift, bubble, eerie;


	public TetrisObject(int x, int y, Color color) {
		this.x = x;
		this.y = y;
		this.color = color;

		arrow = new SoundPlayer(Coordinator.PATH + "arrow.wav");
		shift = new SoundPlayer(Coordinator.PATH + "shift.wav");
		bubble = new SoundPlayer(Coordinator.PATH + "bubbling.wav");
		eerie = new SoundPlayer(Coordinator.PATH + "eerie.wav");

		formShape();
	}


	protected abstract void formShape();


	protected void updateBlocks() {
		for(int i=0; i<blocks.length; i++) {
			blocks[i].updatePosition(x, y);
		}
	}

	public void bury() {
		Graveyard.bury(blocks);
		eerie.play();
	}
	
	public boolean toBeBuried() {
		return toBeBuried;
	}
	

	public void moveDown() {
		y += Block.SIZE;
		updateBlocks();
		
		if(isIllegalMove()) {
			toBeBuried = true;
			y -= Block.SIZE;
			updateBlocks();
			return;
		}
		
		bubble.play();
	}

	
	private void hardDrop() {
		while(!toBeBuried) moveDown();
	}
	
	
	private void shiftLeft() {
		x -= Block.SIZE;
		updateBlocks();
		
		if(isIllegalMove()) {
			x += Block.SIZE;
			updateBlocks();
			return;
		}
		
		shift.play();
	}

	private void shiftRight() {
		x += Block.SIZE;
		updateBlocks();
		
		if(isIllegalMove()) {
			x -= Block.SIZE;
			updateBlocks();
			return;
		}
		
		shift.play();
	}

	private void rotate() {
		for(int i=0; i<blocks.length; i++) {
			blocks[i].rotate();
		}
		updateBlocks();
		
		if(isIllegalMove()) {
			int i, k;
			for(k=0; k<3; k++) {
				for(i=0; i<blocks.length; i++) {
					blocks[i].rotate();
				}
			}
			
			updateBlocks();
			return;
		}
		
		arrow.play();
	}

	private void mirror() {
		for(int i=0; i<blocks.length; i++) {
			blocks[i].mirror();
		}
		updateBlocks();
		
		if(isIllegalMove()) {
			for(int i=0; i<blocks.length; i++) {
				blocks[i].mirror();
			}
			updateBlocks();
			return;
		}
		
		arrow.play();
	}


	/*
	 * Check if any of the tetrisObject blocks gone
	 * outside the boundary of the grave yard and 
	 * also check if any of the blocks overlap with 
	 * any of the occupied blocks of the grave yard.
	 */
	private boolean isIllegalMove() {
		for(int i=0; i<blocks.length; i++) {
			if(blocks[i].getX()<Graveyard.X_LEFT ||
			   blocks[i].getX()>=Graveyard.X_RIGHT ||
			   blocks[i].getY()>=Graveyard.Y_BOTTOM ||
			   Graveyard.isOccupied(blocks[i].getX(), blocks[i].getY())) return true;
		}
		return false;
	}



	public void draw(Graphics g) {
		for(int i=0; i<blocks.length; i++) {
			blocks[i].draw(g); 
		}
	}

	public void keyPressed(KeyEvent e) { 
		int keyCode = e.getKeyCode();

		if(keyCode==KeyEvent.VK_LEFT) shiftLeft();
		else if(keyCode==KeyEvent.VK_RIGHT) shiftRight();
		else if(keyCode==KeyEvent.VK_R) rotate();
		else if(keyCode==KeyEvent.VK_M) mirror();
		else if(keyCode==KeyEvent.VK_DOWN) hardDrop();
	}


	public void keyTyped(KeyEvent e) { }
	public void keyReleased(KeyEvent e) { }

} 






