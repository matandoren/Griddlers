package gameFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import grid.Grid;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import settingsFrame.Settings;

public class GameFrame extends JFrame { // The frame where the actual game takes place.
	protected Settings settings;
	protected Grid grid;
	protected GamePanel gamePanel;
	
	public GameFrame(Settings settings, Grid grid){
		super("Griddlers: game");
		this.settings=settings;
		this.grid=grid;
		settings.gameFrame=this;
		gamePanel=new GamePanel(settings, grid);
		add(gamePanel);
		addWindowListener(new GameFrameListener());
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //Use Window Listener instead.
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
		if (settings.settingsFrame!=null)
			settings.settingsFrame.setEnabled(false);
	}
	
	public GamePanel getGamePanel(){
		return gamePanel;
	}
	
	class GameFrameListener extends WindowAdapter {
		public void windowClosing(WindowEvent event) {
			int choice=JOptionPane.showConfirmDialog(settings.gameFrame, "Do you want to save the game in progress?", "Exit Game", JOptionPane.YES_NO_CANCEL_OPTION);
			if (choice==JOptionPane.YES_OPTION)
				gamePanel.save();
			else if (choice!=JOptionPane.NO_OPTION) // That is: if the user chose the 'cancel' button or the 'X'.
				return;
			gamePanel.dispose();
			if (settings.settingsFrame!=null)
				settings.settingsFrame.setEnabled(true);
			dispose();
		}
	}
	
}
