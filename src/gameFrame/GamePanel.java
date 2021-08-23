package gameFrame;
import fileFilters.SupportedGriddlersFilesFilter;
import grid.Grid;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.io.*;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import settingsFrame.Settings;

public class GamePanel extends JPanel{ // A panel for containing and handling all of the sub-panels of the game frame.
	protected Settings settings;
	protected Grid grid;
	protected GridPanel gridPanel;
	protected SquareFunctionsPanel sfPanel;
	protected JFileChooser fileChooser;
	public DirectionsCanvas hDirections; // Horizontal directions.
	public DirectionsCanvas vDirections; // Vertical directions. 
	
	public GamePanel(Settings settings, Grid grid){
		super();
		this.settings=settings;
		this.grid=grid;
		sfPanel=new SquareFunctionsPanel(settings, grid);
		gridPanel=new GridPanel(settings, sfPanel);
		this.setLayout(new BorderLayout());
		this.add(new GriddlersPanel(gridPanel), BorderLayout.CENTER);
		this.add(new FunctionsPanel(sfPanel, gridPanel), BorderLayout.SOUTH);
		fileChooser = new JFileChooser(){  // This is a modified version of rlbisbe's first answer from: http://stackoverflow.com/questions/3651494/jfilechooser-with-confirmation-dialog 
			public void approveSelection(){     
				File f = getSelectedFile();     
				if(f.exists() && getDialogType() == SAVE_DIALOG){         
					int result = JOptionPane.showConfirmDialog(this, "This file exists! Are you sure you want to overwrite it?", "File already exists", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);         
					if (result!=JOptionPane.YES_OPTION)             
						return;         
				}     
				super.approveSelection(); 
			}
		};
		fileChooser.setFileFilter(new SupportedGriddlersFilesFilter());
		completeLoading(); // Finishes the loading process (if one was started). 
	}
	
	public void save(){ // Saves the game to a file.
		int i, j, height, width;
		if (fileChooser.showSaveDialog(settings.gameFrame)==JFileChooser.APPROVE_OPTION){
			File file=fileChooser.getSelectedFile();
			DataOutputStream out=null;
			try {
				out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
				// Saving the settings.
				out.writeInt(settings.gridSize);
				out.writeInt(sfPanel.getCluesRemaining());
				out.writeInt(settings.squareSize);
				out.writeInt(settings.timerDelay);
				out.writeInt(settings.defaultColor.getRGB());
				out.writeInt(settings.filledColor.getRGB());
				out.writeInt(settings.notFilledColor.getRGB());
				out.writeInt(settings.notSureColor.getRGB());
				out.writeInt(settings.wrongColor.getRGB());
				out.writeInt(settings.markedColor.getRGB());
				out.writeInt(settings.fontColor.getRGB());
				// Saving the grid.
				for (i=0; i<settings.gridSize; i++)
					for (j=0; j<settings.gridSize; j++)
						out.writeBoolean(grid.grid[i][j]);
				// Saving the squares attributes.
				for (i=0; i<settings.gridSize; i++)
					for (j=0; j<settings.gridSize; j++){
						out.writeBoolean(gridPanel.sButtons[i][j].isFilled());
						out.writeBoolean(gridPanel.sButtons[i][j].isNotFilled());
						out.writeBoolean(gridPanel.sButtons[i][j].isNotSure());
					}
				// Saving the vertical directions markings.
				height=vDirections.getActualHeight();
				width=vDirections.getActualWidth();
				for (i=0; i<height; i++)
					for (j=0; j<width; j++)
						out.writeBoolean(vDirections.isMarked(i, j));
				// Saving the horizontal directions markings.
				height=hDirections.getActualHeight();
				width=hDirections.getActualWidth();
				for (i=0; i<height; i++)
					for (j=0; j<width; j++)
						out.writeBoolean(hDirections.isMarked(i, j));
			} catch(IOException e){
				JOptionPane.showMessageDialog(settings.gameFrame, "An exception has occured while trying to open the file or during the writing process!", "Save failed", JOptionPane.ERROR_MESSAGE);
			} finally {
				if (out!=null)
					try {
						out.close();
					} catch (IOException e) {
						JOptionPane.showMessageDialog(settings.gameFrame, "The file could not be closed!", "Save failed", JOptionPane.ERROR_MESSAGE);
					}
			}
		}
	}
	
	protected void completeLoading(){ // Finishes the loading process (if one was started).
		if (settings.dsRef!=null){
			int i, j, height, width;
			DataInputStream in=settings.dsRef;
			
			try{
				// Loading the squares attributes.
				for (i=0; i<settings.gridSize; i++)
					for (j=0; j<settings.gridSize; j++){
						gridPanel.sButtons[i][j].setFilled(in.readBoolean());
						gridPanel.sButtons[i][j].setNotFilled(in.readBoolean());
						gridPanel.sButtons[i][j].setNotSure(in.readBoolean());
					}
				// Loading the vertical directions markings.
				height=vDirections.getActualHeight();
				width=vDirections.getActualWidth();
				for (i=0; i<height; i++)
					for (j=0; j<width; j++)
						vDirections.setMarked(i, j, in.readBoolean());
				// Loading the horizontal directions markings.
				height=hDirections.getActualHeight();
				width=hDirections.getActualWidth();
				for (i=0; i<height; i++)
					for (j=0; j<width; j++)
						hDirections.setMarked(i, j, in.readBoolean());
			} catch(IOException e){
				JOptionPane.showMessageDialog(settings.gameFrame, "An exception has occured while trying to open the file or during the reading process!", "Load failed", JOptionPane.ERROR_MESSAGE);
			} finally {
				if (in!=null)
					try {
						in.close();
					} catch (IOException e) {
						JOptionPane.showMessageDialog(settings.gameFrame, "The file could not be closed!", "Load failed", JOptionPane.ERROR_MESSAGE);
					}
					settings.dsRef=null;
			}
		}
	}
	
	public void dispose(){ // Clean-up activities to be performed when this panel is disposed.
		if (gridPanel.lastClicked!=null)
			gridPanel.lastClicked.setBlinking(false);
	}
	
	class GriddlersPanel extends JPanel { // Contains the grid panel and the directions - this panel is the part of the game which is very similar to the Griddlers puzzles from the newspapers.
		protected GriddlersPanel(GridPanel gridPanel){
			super();
			hDirections=new DirectionsCanvas(settings, grid.hDirections);
			vDirections=new DirectionsCanvas(settings, grid.vDirections);
			this.setLayout(null);
			this.add(vDirections);
			this.add(hDirections);
			this.add(gridPanel);
			this.setSize(hDirections.getWidth()+gridPanel.getWidth(), vDirections.getHeight()+gridPanel.getHeight());
			this.setPreferredSize(this.getSize());
			hDirections.setLocation(0, vDirections.getHeight());
			vDirections.setLocation(hDirections.getWidth(), 0);
			gridPanel.setLocation(hDirections.getWidth(), vDirections.getHeight());
		}
		
		public void paintComponent(Graphics page){ // Draws the background image of the panel.
			super.paintComponent(page);
			if (settings.backgroundImage!=null)
				page.drawImage(settings.backgroundImage, 0, 0, null);
		}
	}
	
	class FunctionsPanel extends JPanel { // Contains the square functions panel and the game functions panel.
		protected FunctionsPanel(SquareFunctionsPanel sfPanel, GridPanel gridPanel){
			super();
			GameFunctionsPanel gfPanel=new GameFunctionsPanel(settings, grid, gridPanel);
			this.setLayout(new GridLayout(2,1,5,5));
			this.add(sfPanel);
			this.add(gfPanel);
		}
	}

}
