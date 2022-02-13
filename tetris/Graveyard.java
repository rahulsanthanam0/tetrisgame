
import java.awt.Color;
import java.awt.Graphics;



public class Graveyard {

	public static final int X_LEFT;
	public static final int Y_TOP;
	public static final int X_RIGHT;
	public static final int Y_BOTTOM;
	
	private static int cols;
	private static int rows;
	
	private static Color[] colors;
	
	static {
		cols = 20;
		rows = 30;
		X_LEFT = 40;
		Y_TOP = 40;
		X_RIGHT = X_LEFT + cols*Block.SIZE;
		Y_BOTTOM = Y_TOP + rows*Block.SIZE;
		
		colors = new Color[rows*cols];
	}
	
	
	private static Color getColorAt(int x, int y) {
		// map (x, y) ---> i
		int i = (x-X_LEFT)/Block.SIZE + cols*(y-Y_TOP)/Block.SIZE;
		return colors[i];
	}
	
	
	public static boolean isOccupied(int x, int y) {
		return getColorAt(x, y)!=null;
	}
	
	
	
	public static void setColorAt(int x, int y, Color color) {
		int i = (x-X_LEFT)/Block.SIZE + cols*(y-Y_TOP)/Block.SIZE;
		colors[i] = color;
	}
	
	
	public static void bury(Block[] blocks) {
	//bubble sort-ish
		Block temp;
		for(int i=0; i<blocks.length-1; i++) {
			for(int j=0; j<blocks.length-1; j++) {
				if(blocks[j].getY()>blocks[j+1].getY()) {
					temp = blocks[j];
					blocks[j]= blocks[j+1];
					blocks[j+1] = temp;
				}
			}
		}
		
		for(int i=0; i<blocks.length; i++) bury(blocks[i]);
	}

	public static void bury(Block block) {
		if(block.getY()<=Coordinator.GAME_OVER_Y) {
			Coordinator.gameOver = true;
			return;
		}
		
		setColorAt(block.getX(), block.getY(), block.getColor());
		Coordinator.score++;
		
		if(isFullRow(block.getY())) {
			shiftDownByOneRow(block.getY());
			Coordinator.score += 20;
		}
	}
	
	
	private static boolean isFullRow(int y) {
		for(int x=X_LEFT; x<X_RIGHT; x+=Block.SIZE) {
			if(!isOccupied(x, y)) return false;
		}
		return true;
	}
	
	
	private static void shiftDownByOneRow(int y) {
		int start = ((X_RIGHT-Block.SIZE)-X_LEFT)/Block.SIZE + cols*(y-Y_TOP)/Block.SIZE;
		int end = cols;
		
		for(int i=start; i>=end; i--) {
			colors[i] = colors[i-cols];
		}
	}
	
	
	
	public static void draw(Graphics g) {
		drawLines(g);
		
		// draw colors.
		int x, y;
		for(int i=0; i<colors.length; i++) {
			if(colors[i]!=null) {
				x = X_LEFT + i%cols*Block.SIZE;
				y = Y_TOP + i/cols*Block.SIZE;
				g.setColor(colors[i]);
				g.fillRect(x, y, Block.SIZE, Block.SIZE);
				g.setColor(Color.BLACK);
				g.drawRect(x, y, Block.SIZE, Block.SIZE);
			}
		}
	}
	
	
	private static void drawLines(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		// vertical lines
		for(int x=X_LEFT; x<=X_RIGHT; x+=Block.SIZE) {
			g.drawLine(x,  Y_TOP, x, Y_BOTTOM); 
		}
		// horizontal lines.
		for(int y=Y_TOP; y<=Y_BOTTOM; y+=Block.SIZE) {
			g.drawLine(X_LEFT, y, X_RIGHT, y); 
		}
		// black border.
		g.setColor(Color.BLACK);
		g.drawRect(X_LEFT, Y_TOP, X_RIGHT-X_LEFT, Y_BOTTOM-Y_TOP); 
	}
	
}











