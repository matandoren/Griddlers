package gameFrame;
import grid.Grid;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import settingsFrame.Settings;
import squareButtons.SquareButton;

public class SquareFunctionsPanel extends JPanel{ // This panel consists of buttons that are responsible for functions that regard a single square. These include: getting the correct state for a chosen square.
	protected JLabel clueVar; // Variable text label for the 'clue' function. States the current remaining clues.
	protected JLabel clueFixed; // Fixed text label for the 'clue' function.
	protected JButton clue; // Discovering the correct state for a chosen square.
	protected Grid grid;
	protected Settings settings;
	protected int cluesRemaining;
	protected SquareButton chosenSquare;
	
	public SquareFunctionsPanel(Settings settings, Grid grid){
		super();
		this.settings=settings;
		cluesRemaining=settings.numberOfClues;
		this.grid=grid;
		clueFixed=new JLabel(" Clues remaining: ");
		clueVar=new JLabel();
		updateClueLabels();
		clue=new JButton("Clue");
		clue.setForeground(settings.fontColor);
		this.add(clue);
		this.add(clueFixed);
		this.add(clueVar);
		clue.addActionListener(new ClueListener());
		clue.setEnabled(false);
	}
	
	public int getCluesRemaining(){
		return cluesRemaining;
	}
	
	protected void updateClueLabels(){
		if (chosenSquare==null || !chosenSquare.isBlinking() || cluesRemaining==0){ // Updates the color.
			clueFixed.setForeground(Color.GRAY);
			clueVar.setForeground(Color.GRAY);
		}
		else {
			clueFixed.setForeground(settings.fontColor);
			clueVar.setForeground(settings.fontColor);
		}
		if (cluesRemaining>=0) // Updates the text.
			clueVar.setText(Integer.toString(cluesRemaining));
		else
			clueVar.setText("unlimited");
	}
	
	public void setChosenSquare(SquareButton chosenSquare){
		this.chosenSquare=chosenSquare;
		updateClueLabels();
		clue.setEnabled(chosenSquare!=null && chosenSquare.isBlinking() && cluesRemaining!=0);
	}
	
	public void paintComponent(Graphics page){ // Draws the background image of the panel.
		super.paintComponent(page);
		if (settings.backgroundImage!=null)
			page.drawImage(settings.backgroundImage, 0, 0, null);
	}
	
	class ClueListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			chosenSquare.setBlinking(false);
			chosenSquare.setNotSure(false);
			chosenSquare.setFilled(false);
			chosenSquare.setNotFilled(false);
			if (grid.grid[chosenSquare.getYIndex()][chosenSquare.getXIndex()])
				chosenSquare.setFilled(true);
			else
				chosenSquare.setNotFilled(true);
			cluesRemaining--;
			updateClueLabels();
			clue.setEnabled(false);
		}
	}

}
