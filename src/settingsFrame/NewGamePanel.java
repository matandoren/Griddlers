package settingsFrame;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import fileFilters.*;

public class NewGamePanel extends JPanel{ // The new game menu.
	public static final int GRID_TYPE_RANDOM=1;
	public static final int GRID_TYPE_FROM_IMAGE=2;
	protected int gridType;
	protected File gridImage; // The file of the image to be converted into a grid.
	protected boolean imageFileSelected; // 'true' if a file to be converted into a grid has been selected.
	protected JRadioButton randomGridButton;
	protected JRadioButton gridFromImageButton;
	protected JButton loadFileButton;
	protected NaturalNumbersTextField numberOfClues;
	protected NaturalNumbersTextField gridSize;
	protected JCheckBox unlimitedClues;
	protected JFileChooser fileChooser;
	
	public NewGamePanel(Settings settings){
		super(new GridLayout(2,1,5,5));
		
		fileChooser= new JFileChooser() {     // This is a modified version of camickr's answer from: http://stackoverflow.com/questions/1825346/java-filechooser
			public void approveSelection(){
				File file=getSelectedFile();
				SupportedImageFilesFilter filter=new SupportedImageFilesFilter();
				if (file.exists() && filter.accept(file))             
					super.approveSelection();         
				else             
					JOptionPane.showMessageDialog(this, "The selected file is not supported or does not exist!", "Error", JOptionPane.ERROR_MESSAGE);     
			}
		};
		fileChooser.setFileFilter(new SupportedImageFilesFilter());
		
		// Constructing the buttons/text-fields.
		randomGridButton=new JRadioButton("Random grid");
		gridFromImageButton=new JRadioButton("Grid constructed from an image file");
		loadFileButton=new JButton("Load");
		gridSize=new NaturalNumbersTextField(Integer.toString(settings.gridSize), 5);
		numberOfClues=new NaturalNumbersTextField(Integer.toString(settings.numberOfClues));
		unlimitedClues=new JCheckBox("Unlimited");
		
		// Grouping the radio buttons.
		ButtonGroup group = new ButtonGroup();
		group.add(randomGridButton);
		group.add(gridFromImageButton);
		
		// Adding the listeners.
		randomGridButton.addActionListener(new RandomGridButtonListener());
		gridFromImageButton.addActionListener(new GridFromImageButtonListener());
		loadFileButton.addActionListener(new LoadFileButtonListener());
		unlimitedClues.addActionListener(new UnlimitedCluesListener());
		
		// Setting the default radio button.
		randomGridButton.setSelected(true);
		loadFileButton.setEnabled(false);
		gridType=GRID_TYPE_RANDOM;
		
		// Setting the layout.
		JPanel gridSelectPanel=new JPanel(new GridLayout(2,1,5,5));
		gridSelectPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), " Select a grid type "));
		gridSelectPanel.add(randomGridButton);
		JPanel gridFromImagePanel=new JPanel(new BorderLayout(5,5));
		gridFromImagePanel.add(gridFromImageButton, BorderLayout.WEST);
		gridFromImagePanel.add(loadFileButton, BorderLayout.CENTER);
		gridSelectPanel.add(gridFromImagePanel);
		this.add(gridSelectPanel);
		JPanel settingsPanel=new JPanel(new GridLayout(2,1,5,5));
		settingsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), " Enter settings "));
		JPanel gridSizePanel=new JPanel(new BorderLayout(5,5));
		gridSizePanel.add(new JLabel("Grid size: "), BorderLayout.WEST);
		gridSizePanel.add(gridSize, BorderLayout.CENTER);
		settingsPanel.add(gridSizePanel);
		JPanel numberOfCluesPanel=new JPanel(new BorderLayout(5,5));
		numberOfCluesPanel.add(new JLabel("Number of clues: "), BorderLayout.WEST);
		numberOfCluesPanel.add(numberOfClues, BorderLayout.CENTER);
		numberOfCluesPanel.add(unlimitedClues, BorderLayout.EAST);
		settingsPanel.add(numberOfCluesPanel);
		this.add(settingsPanel);
	}
	
	public boolean isInputValid(){
		if ((unlimitedClues.isSelected() || numberOfClues.isInputValid()) && gridSize.isInputValid())
			if (gridType==GRID_TYPE_RANDOM || (gridType==GRID_TYPE_FROM_IMAGE && imageFileSelected))
				return true;
		return false;
	}
	
	public void resetImageFileSelection(){
		imageFileSelected=false;
	}
	
	public File getImageFile(){
		return gridImage;
	}
	
	public int getGridType(){
		return gridType;
	}
	
	public int getNumberOfCluse(){
		if (unlimitedClues.isSelected())
			return -1;
		return numberOfClues.getInput();
	}
	
	public int getGridSize(){
		return gridSize.getInput();
	}
	
// Action Listeners:
	class RandomGridButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			loadFileButton.setEnabled(false);
			gridType=GRID_TYPE_RANDOM;
		}
	}
	
	class GridFromImageButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			loadFileButton.setEnabled(true);
			gridType=GRID_TYPE_FROM_IMAGE;
		}
	}
	
	class UnlimitedCluesListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			numberOfClues.setEnabled(!unlimitedClues.isSelected());
		}
	}
	
	class LoadFileButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			if(fileChooser.showOpenDialog(NewGamePanel.this)==JFileChooser.APPROVE_OPTION){
				gridImage=fileChooser.getSelectedFile();
				imageFileSelected=true;
			}
		}
	}

}
