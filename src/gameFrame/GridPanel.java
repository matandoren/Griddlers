package gameFrame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import settingsFrame.Settings;
import squareButtons.SquareButton;

public class GridPanel extends JPanel{
	public SquareFunctionsPanel sfPanel; // A reference to the SquareFunctions panel so that this panel would be able to send a reference of the last clicked square to the SquareFunctions panel.
	public SquareButton[][] sButtons;
	public SquareButton lastClicked;
	
	public GridPanel(Settings settings, SquareFunctionsPanel sfPanel){
		super();
		MouseListener mListener;
		this.sfPanel=sfPanel;
		sButtons=new SquareButton[settings.gridSize][settings.gridSize];
		mListener=new MouseListener();
		this.setLayout(null);
		this.setSize(settings.squareSize*settings.gridSize, settings.squareSize*settings.gridSize);
		for (int i=0; i<settings.gridSize; i++)
			for (int j=0; j<settings.gridSize; j++){
				sButtons[i][j]=new SquareButton(j,i,settings);
				this.add(sButtons[i][j]);
				sButtons[i][j].setBounds(j*settings.squareSize, i*settings.squareSize, settings.squareSize, settings.squareSize);
				sButtons[i][j].addMouseListener(mListener);
			}
	}
	
	class MouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e){
			SquareButton button=(SquareButton)e.getSource();
			if (button!=lastClicked && lastClicked!=null && lastClicked.isBlinking())
				lastClicked.setBlinking(false);
			// Middle click
			if (e.getButton()==MouseEvent.BUTTON2)
				if (button.isBlinking())
					button.setNotSure(!button.isNotSure()); // Toggles the notSure status.
				else
					button.setBlinking(true);
			// Left click
			if (e.getButton()==MouseEvent.BUTTON1)
				button.setFilled(!button.isFilled()); // Toggles the filled status (if possible).
			// Right click
			if (e.getButton()==MouseEvent.BUTTON3)
				button.setNotFilled(!button.isNotFilled()); // Toggles the NotFilled status (if possible).
			lastClicked=button;
			sfPanel.setChosenSquare(lastClicked); // Sends a reference of the last clicked square to the square functions panel.
		}
	}
	
}
