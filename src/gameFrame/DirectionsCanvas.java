package gameFrame;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import settingsFrame.Settings;

public class DirectionsCanvas extends JPanel{ // This panel contains the directions for solving the puzzle.
	protected int[][] directions;
	protected boolean[][] marked;
	protected int actualHeight;
	protected int actualWidth;
	protected Settings settings; 
	
	public DirectionsCanvas(Settings settings, int[][] directions){
		super();
		this.settings=settings;
		this.directions=directions;
		findActualSizes();
		this.setLayout(null);
		marked=new boolean[actualHeight][actualWidth];
		this.setSize(settings.squareSize*actualWidth, settings.squareSize*actualHeight);
		this.addMouseListener(new MouseListener());
	}
	
	public int getActualHeight(){
		return actualHeight;
	}
	
	public int getActualWidth(){
		return actualWidth;
	}
	
	public boolean isMarked(int i, int j){
		return marked[i][j];
	}
	
	public void setMarked(int i, int j, boolean status){
		marked[i][j]=status;
		repaint();
	}
	
	public void paintComponent(Graphics page) {
		super.paintComponent(page);
		if (settings.backgroundImage!=null)
			page.drawImage(settings.backgroundImage, 0, 0, null);
		page.setColor(settings.fontColor);
		for (int i=0; i<actualHeight; i++)
			for (int j=0; j<actualWidth; j++)
				if (directions[i][j]!=0){
					page.drawString(Integer.toString(directions[i][j]), j*settings.squareSize, (i+1)*settings.squareSize); // Draws the directions.
					if (marked[i][j]){ // Marks/unmarks directions with 'X's according to the marked matrix.
						page.setColor(settings.markedColor);
						page.drawLine(j*settings.squareSize, i*settings.squareSize, (j+1)*settings.squareSize, (i+1)*settings.squareSize);
						page.drawLine((j+1)*settings.squareSize, i*settings.squareSize, j*settings.squareSize, (i+1)*settings.squareSize);
						page.setColor(settings.fontColor);
					}
				}
		if (directions.length>directions[0].length) // Draws the line dividers.
			for (int i=0; i<directions.length; i++)
				page.drawLine(0, i*settings.squareSize, directions[0].length*settings.squareSize, i*settings.squareSize);
		else
			for (int i=0; i<directions[0].length; i++)
				page.drawLine(i*settings.squareSize, 0, i*settings.squareSize, directions.length*settings.squareSize);
	}
	
	protected void findActualSizes(){ // The actual size is the amount of non-zero elements in the row/column with the most of such.
		int counter, max=0;
		if (directions.length>directions[0].length){
			actualHeight=directions.length;
			for (int i=0; i<directions.length; i++){
				for (counter=0; counter<directions[0].length && directions[i][counter]!=0; counter++);
				if (counter>max)
					max=counter;
			}
			actualWidth=max;
		}
		else{
			actualWidth=directions[0].length;
			for (int i=0; i<directions[0].length; i++){
				for (counter=0; counter<directions.length && directions[counter][i]!=0; counter++);
				if (counter>max)
					max=counter;
			}
			actualHeight=max;
		}
	}
	
	class MouseListener extends MouseAdapter { // When a direction is clicked on with the left mouse button, that direction is marked with an 'X'. Another click on that direction, unmarks it.
		public void mouseClicked(MouseEvent e){
			int x=e.getX()/settings.squareSize;
			int y=e.getY()/settings.squareSize;
			if (e.getButton()==MouseEvent.BUTTON1) // If the left mouse button was clicked
				if (directions[y][x]!=0){
					marked[y][x]=!marked[y][x];
					repaint();
				}
		}
	}

}
