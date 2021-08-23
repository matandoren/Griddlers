package settingsFrame;
import fileFilters.SupportedGriddlersFilesFilter;
import gameFrame.GameFrame;
import grid.Grid;
import grid.gridGenerators.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class GameSetupPanel extends JPanel {
	protected JButton newGame; // Creates a new game.
	protected JButton loadGame; // Loads a previously saved game from a .grd file.
	protected JCheckBox loadPreferences; // Decides whether to use the current preferences or the ones from the loaded file for the load game option.
	protected JOptionPane optionPane; // The code using the JOptionPane and JDialog is a modified version of the code presented in the section "Stopping Automatic Dialog Closing" from: http://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html
	protected JDialog dialog;		  // Also using information derived from John L.'s question from: http://stackoverflow.com/questions/7018437/how-do-i-change-the-value-of-a-joptionpane-from-a-propertychangelistener-without
	protected NewGamePanel ngPanel;
	protected JFileChooser fileChooser;
	protected Settings settings;
	protected Grid grid;
	
	public GameSetupPanel(Settings settings){
		super(new GridLayout(2,1,5,5));
		this.settings=settings;
		ngPanel=new NewGamePanel(settings);
		optionPane=new JOptionPane(ngPanel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
		dialog=new JDialog(settings.settingsFrame, "Setup a new game", true);
		dialog.setContentPane(optionPane);
		optionPane.addPropertyChangeListener(new DialogListener());
		
		fileChooser= new JFileChooser() {     // This is a modified version of camickr's answer from: http://stackoverflow.com/questions/1825346/java-filechooser
			public void approveSelection(){
				File file=getSelectedFile();
				SupportedGriddlersFilesFilter filter=new SupportedGriddlersFilesFilter();
				if (file.exists() && filter.accept(file))             
					super.approveSelection();         
				else             
					JOptionPane.showMessageDialog(this, "The selected file is not supported or does not exist!", "Error", JOptionPane.ERROR_MESSAGE);     
			}
		};
		fileChooser.setFileFilter(new SupportedGriddlersFilesFilter());
		
		newGame=new JButton("New Game");
		newGame.addActionListener(new NewGameListener());
		add(newGame);
		loadGame=new JButton("Load Game");
		loadGame.addActionListener(new LoadGameListener());
		loadPreferences=new JCheckBox("Use preferences from the file");
		JPanel loadGamePanel=new JPanel(new BorderLayout());
		loadGamePanel.add(loadGame, BorderLayout.WEST);
		loadGamePanel.add(loadPreferences, BorderLayout.CENTER);
		add(loadGamePanel);
	}
	
	// LISTENERS
	class DialogListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent e) {
			String prop = e.getPropertyName();
			// If the OK button on the dialog was clicked, the validity of the input is checked. If the input was found invalid, an error message is displayed and the dialog stays open.
			// Else, the dialog closes.
			if (dialog.isVisible() && (e.getSource() == optionPane) && (prop.equals(JOptionPane.VALUE_PROPERTY)) && !optionPane.getValue().equals(JOptionPane.UNINITIALIZED_VALUE)){
				if (optionPane.getValue().equals(JOptionPane.OK_OPTION) && !ngPanel.isInputValid()){
					JOptionPane.showMessageDialog(dialog, "Invalid input or no file has been chosen to be loaded!", "Error", JOptionPane.ERROR_MESSAGE);
					optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE); // Without this line, only the FIRST click on the OK button would have invoked this listener (instead of every click). 
					return;
				}
				dialog.setVisible(false);
			}
		}
	}

	class NewGameListener implements ActionListener { // NOTICE: THIS IS WHERE THE ACTUAL NEW GAME IS CONSTRUCTED
		public void actionPerformed(ActionEvent e){
			optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE); // This is needed so that no matter what was the last button that was clicked in the dialog, the next button click will cause a property change and the listener will be invoked.  
			// Displays the new game dialog
			dialog.pack();
			dialog.setLocationRelativeTo(settings.settingsFrame);
			dialog.setVisible(true);
			// Gets the chosen button 
			if (!optionPane.getValue().equals(JOptionPane.UNINITIALIZED_VALUE) && ((Integer)optionPane.getValue()).intValue() == JOptionPane.OK_OPTION) { // At this point, if the OK button was clicked, the input of the dialog has already been validated.
				// Processes the data and constructs the game.
				settings.gridSize=ngPanel.getGridSize();
				settings.numberOfClues=ngPanel.getNumberOfCluse();
				// Constructs the grid.
				int gridType=ngPanel.getGridType();
				
				if (gridType==NewGamePanel.GRID_TYPE_RANDOM) // Creates a new random grid.
					grid=new Grid(new RandomGrid(settings.gridSize));
				else if (gridType==NewGamePanel.GRID_TYPE_FROM_IMAGE) // Creates a new grid from an image.
					try {
						grid=new Grid(new ImageToGrid(settings.gridSize, ImageIO.read(ngPanel.getImageFile())));
					} catch (IOException exception) {
						JOptionPane.showMessageDialog(GameSetupPanel.this, "The image loading process has failed!", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
				// Constructs the game frame.
				settings.dsRef=null;
				settings.pPanel.updateSettings(); // Updates the settings object with the current preferences from the preference panel.
				new GameFrame(settings, grid);
			}
			ngPanel.resetImageFileSelection(); // In any case, reset the selected image file so that the next time the user opens the new game dialog, if it wants to choose a grid from image, it has to explicitly choose a file. 
		}
	}
	
	class LoadGameListener implements ActionListener { // NOTICE: THIS IS WHERE THE ACTUAL LOADED GAME IS RECONSTRUCTED
		public void actionPerformed(ActionEvent e){
			if(fileChooser.showOpenDialog(settings.settingsFrame)==JFileChooser.APPROVE_OPTION){
				int i,j;
				File file=fileChooser.getSelectedFile();
				DataInputStream in=null;
				settings.dsRef=null;
				try {
					in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
					// Loading the settings.
					settings.gridSize=in.readInt();
					settings.numberOfClues=in.readInt();
					if (loadPreferences.isSelected()){
						settings.squareSize=in.readInt();
						settings.timerDelay=in.readInt();
						settings.defaultColor=new Color(in.readInt());
						settings.filledColor=new Color(in.readInt());
						settings.notFilledColor=new Color(in.readInt());
						settings.notSureColor=new Color(in.readInt());
						settings.wrongColor=new Color(in.readInt());
						settings.markedColor=new Color(in.readInt());
						settings.fontColor=new Color(in.readInt());
						settings.pPanel.updateBackgroundImage(); // Since the background image is not saved to the file, it is dependent on the current preferences.
					}
					else{ // This has to be done anyway just to advance through the file.
						in.readInt();
						in.readInt();
						in.readInt();
						in.readInt();
						in.readInt();
						in.readInt();
						in.readInt();
						in.readInt();
						in.readInt();
						settings.pPanel.updateSettings(); // Updates the settings object with the current preferences from the preference panel.
					}
					// Loading the grid to a matrix.
					boolean[][] tmpGrid=new boolean[settings.gridSize][settings.gridSize];
					for (i=0; i<settings.gridSize; i++)
						for (j=0; j<settings.gridSize; j++)
							tmpGrid[i][j]=in.readBoolean();
					// Constructing the grid
					grid=new Grid(new LoadedGrid(tmpGrid));
					// Constructs the game frame.
					settings.dsRef=in;
					new GameFrame(settings, grid);
				} catch(IOException exception){
					JOptionPane.showMessageDialog(settings.settingsFrame, "An exception has occured while trying to open the file or during the reading process!", "Load failed", JOptionPane.ERROR_MESSAGE);
					try {
						in.close(); // Normally, the method completeLoading() in the GamePanel class (in the gameFrame package) is responsible for closing the data stream.
					} catch (IOException doubleException) {
						JOptionPane.showMessageDialog(settings.settingsFrame, "The file could not be closed!", "Load failed", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
	}

}
