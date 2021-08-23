package gameFrame;
import grid.Grid;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import settingsFrame.Settings;
import squareButtons.SquareButton;

public class GameFunctionsPanel extends JPanel{ // This panel consists of buttons that are responsible for functions that regard the entire puzzle. These include: solving the puzzle, saving the current state of the puzzle to a file and so on... 
	protected Settings settings;
	protected Grid grid;
	protected GridPanel gridPanel;
	protected JTextArea instructionsText;
	
	public GameFunctionsPanel(Settings settings, Grid grid, GridPanel gridPanel){
		this.settings=settings;
		this.gridPanel=gridPanel;
		this.grid=grid;
		this.gridPanel=gridPanel;
		this.setLayout(new GridLayout(3,1));
		JButton save=new JButton("Save");
		save.setForeground(settings.fontColor);
		JButton check=new JButton("Check");
		check.setForeground(settings.fontColor);
		JButton instructions=new JButton("Instructions");
		instructions.setForeground(settings.fontColor);
		instructionsText=new JTextArea("How to play the game:\n" +
				" The cells in the grid have to be colored or left blank according to the numbers given at the side of the grid to reveal a hidden picture.\n" +
				" The numbers measure how many unbroken lines of filled-in squares there are in any given row or column.\n" +
				" For example, a clue of '4 8 3' would mean there are sets of four, eight, and three filled squares, in that order, with at least one blank square between successive groups.\n" +
				"\nUser Interface explanation:\n" +
				"- Left clicking on a square in the grid will mark it as 'filled-in'. Performing this once again will 'de-fill' the square.\n" +
				"- Right clicking on a square in the grid will mark it as 'leave blank'. Performing this once again will unmark the square.\n" +
				"- Middle clicking on a square in the grid will set it blinking, enabling single-square functions (such as the 'clue' function).\n" +
				"- Middle clicking once more will mark the square as 'not sure'.\n" +
				" Performing this sequence (middle clicking twice) once more will remove the 'not sure' mark.\n" +
				"- The 'Clue' button reveals the correct state of a square. Choose a square by middle clicking it (the square should blink).\n Note that the number of clues may be limited.\n" +
				"- The 'Check' button is used for submitting the solved puzzle. If the submitted solution is not the right one, the incorrect squares will be colored.\n" +
				" Note that this will end the game.\n" +
				"- The 'Save' button saves the game (along with its settings) to a file.");
		instructionsText.setEditable(false);
		instructionsText.setForeground(settings.fontColor);
		this.add(check);
		this.add(save);
		this.add(instructions);
		check.addActionListener(new CheckListener());
		save.addActionListener(new SaveListener());
		instructions.addActionListener(new InstructionsListener());
	}
	
	public void paintComponent(Graphics page){ // Draws the background image of the panel.
		super.paintComponent(page);
		if (settings.backgroundImage!=null)
			page.drawImage(settings.backgroundImage, 0, 0, null);
	}
	
	class CheckListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			if (JOptionPane.showConfirmDialog(settings.gameFrame, "This will end the game.\nDo you wish to submit your solution?", "Are you sure?", JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)
				return;
			boolean win=true;
			if (gridPanel.lastClicked!=null && gridPanel.lastClicked.isBlinking())
				gridPanel.lastClicked.setBlinking(false);
			for(int i=0; i<settings.gridSize; i++)
				for(int j=0; j<settings.gridSize; j++)
					if (gridPanel.sButtons[i][j].isFilled()!=grid.grid[i][j]){
						gridPanel.sButtons[i][j].setBackground(SquareButton.mergeColors(gridPanel.sButtons[i][j].getBackground(), settings.wrongColor));
						win=false;
					}
			if (win)
				JOptionPane.showMessageDialog(settings.gameFrame, "Congratulations, you won the game!", "Game Won", JOptionPane.INFORMATION_MESSAGE);
			else
				JOptionPane.showMessageDialog(settings.gameFrame, "Sorry you lost this game. Better luck next time!", "Game Lost", JOptionPane.INFORMATION_MESSAGE);
			settings.settingsFrame.setEnabled(true);
			settings.gameFrame.dispose();
		}
	}
	
	class SaveListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			settings.gameFrame.getGamePanel().save();
		}
	}
	
	class InstructionsListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			JOptionPane.showMessageDialog(settings.gameFrame, instructionsText, "Instructions", JOptionPane.INFORMATION_MESSAGE);
		}
	}

}
