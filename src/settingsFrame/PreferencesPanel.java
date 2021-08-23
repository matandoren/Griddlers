package settingsFrame;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import colorChoosers.ColorChooser;
import fileFilters.SupportedImageFilesFilter;

public class PreferencesPanel extends JPanel{ // Preferences panel: lets the user set its preferences. 
											  // The settings are updated by invoking the public method updateSettings().
	protected ColorChooser cChooser;
	protected Settings settings;
	protected NaturalNumbersTextField squareSize;
	protected NaturalNumbersTextField timerDelay;
	protected JButton defaultColor;
	protected JButton filledColor;
	protected JButton notFilledColor;
	protected JButton notSureColor;
	protected JButton wrongColor;
	protected JButton markedColor;
	protected JButton fontColor;
	protected File backgroundImageFile;
	protected JFileChooser fileChooser; // For choosing the background image file.
	
	public PreferencesPanel(Settings settings){
		super(new GridLayout(10,2,5,5));
		this.settings=settings;
		cChooser=new ColorChooser();
		
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
		
		// Integer preferences.
		add(new JLabel(" Square size: "));
		squareSize=new NaturalNumbersTextField(Integer.toString(settings.squareSize), 5);
		add(squareSize);
		
		add(new JLabel(" Timer delay (in milliseconds): "));
		timerDelay=new NaturalNumbersTextField(Integer.toString(settings.timerDelay), 100);
		add(timerDelay);
		
		// Color preferences.
		defaultColor=new JButton();
		defaultColor.setBackground(settings.defaultColor);
		defaultColor.addActionListener(new ButtonListener());
		add(new JLabel(" Default square color: "));
		add(defaultColor);
		
		filledColor=new JButton();
		filledColor.setBackground(settings.filledColor);
		filledColor.addActionListener(new ButtonListener());
		add(new JLabel(" Filled square color: "));
		add(filledColor);
		
		notFilledColor=new JButton();
		notFilledColor.setBackground(settings.notFilledColor);
		notFilledColor.addActionListener(new ButtonListener());
		add(new JLabel(" Not-filled square color: "));
		add(notFilledColor);
		
		notSureColor=new JButton();
		notSureColor.setBackground(settings.notSureColor);
		notSureColor.addActionListener(new ButtonListener());
		add(new JLabel(" Not-sure square color: "));
		add(notSureColor);
		
		wrongColor=new JButton();
		wrongColor.setBackground(settings.wrongColor);
		wrongColor.addActionListener(new ButtonListener());
		add(new JLabel(" Wrong square color: "));
		add(wrongColor);
		
		markedColor=new JButton();
		markedColor.setBackground(settings.markedColor);
		markedColor.addActionListener(new ButtonListener());
		add(new JLabel(" Marked hint color: "));
		add(markedColor);
		
		fontColor=new JButton();
		fontColor.setBackground(settings.fontColor);
		fontColor.addActionListener(new ButtonListener());
		add(new JLabel(" Font color: "));
		add(fontColor);
		
		// Background image preference.
		JButton loadButton=new JButton("Load image");
		loadButton.addActionListener(new LoadButtonListener());
		add(new JLabel(" Background image: "));
		add(loadButton);
		
		settings.pPanel=this; // Sets the reference to the preference panel in the settings object.
	}
	
	
	public void updateSettings(){
		if (squareSize.isInputValid())
			settings.squareSize=squareSize.getInput();
		if (timerDelay.isInputValid())
			settings.timerDelay=timerDelay.getInput();
		
		settings.defaultColor=defaultColor.getBackground();
		settings.filledColor=filledColor.getBackground();
		settings.notFilledColor=notFilledColor.getBackground();
		settings.notSureColor=notSureColor.getBackground();
		settings.wrongColor=wrongColor.getBackground();
		settings.markedColor=markedColor.getBackground();
		settings.fontColor=fontColor.getBackground();
		
		updateBackgroundImage();
	}
	
	public void updateBackgroundImage(){
		BufferedImage tempImage=settings.backgroundImage;
		if (backgroundImageFile!=null)
			try {
				settings.backgroundImage = ImageIO.read(backgroundImageFile);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Loading the new background image has failed! Loading default background image...", "Background image loading exception", JOptionPane.ERROR_MESSAGE);
				settings.backgroundImage=tempImage;
			}
	}
	
	protected Color getColor(Color color){
		cChooser.setCurrentColor(color);
		if (JOptionPane.OK_OPTION==JOptionPane.showConfirmDialog(settings.settingsFrame, cChooser, "Choose a new color", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE))
			return cChooser.getNewColor();
		else
			return color;
	}
	
// ACTION LISTENERS:
	class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			JButton button=(JButton)e.getSource();
			button.setBackground(getColor(button.getBackground()));
		}
	}
	
	class LoadButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			if(fileChooser.showOpenDialog(settings.settingsFrame)==JFileChooser.APPROVE_OPTION)
				backgroundImageFile=fileChooser.getSelectedFile();
		}
	}
	
}
