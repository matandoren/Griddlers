package settingsFrame;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

public class SettingsFrame extends JFrame { // Consists of two tabs: one for the game setup and the other for setting preferences.
	
	public SettingsFrame(){
		super("Griddlers: setup");
		
		// Settings default settings.
		Settings settings=new Settings();
		settings.gridSize=20;
		settings.squareSize=20;
		settings.numberOfClues=10;
		settings.timerDelay=500;
		settings.defaultColor=Color.WHITE;
		settings.filledColor=Color.BLACK;
		settings.notFilledColor=Color.LIGHT_GRAY;
		settings.notSureColor=Color.GREEN;
		settings.fontColor=Color.BLACK;
		settings.wrongColor=Color.RED;
		settings.markedColor=Color.RED;
		settings.backgroundImage=null;
		try {
			settings.backgroundImage = ImageIO.read(new File("sampleBackground.jpg"));
		} catch (IOException e) {
			settings.backgroundImage=null;
			JOptionPane.showMessageDialog(null, "Loading the default background image has failed!", "Background image loading exception", JOptionPane.ERROR_MESSAGE);
		}
		settings.settingsFrame=this;
		
		// Constructing the tabbed pane. (Using pieces of code found in: http://docs.oracle.com/javase/tutorial/displayCode.html?code=http://docs.oracle.com/javase/tutorial/uiswing/examples/components/TabbedPaneDemoProject/src/components/TabbedPaneDemo.java)
		JTabbedPane tabbedPane = new JTabbedPane(); 
		tabbedPane.addTab("Game Setup", new GameSetupPanel(settings)); 
		tabbedPane.addTab("Preferences", new PreferencesPanel(settings));
		add(tabbedPane);
		
		// Setting frame attributes.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);	
	}

}
