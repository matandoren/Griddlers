package settingsFrame;
import gameFrame.GameFrame;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;

import javax.swing.JFrame;

public class Settings {
	//****************************** Essentials: these must be loaded when loading a game.
	public int gridSize;
	public int numberOfClues;
	//***************************************************************************************
	//****************************** Preferences: these may be loaded when loading a game (but should always be saved when saving a game). 
	public int squareSize;
	public int timerDelay; // in milliseconds.
	public Color defaultColor;
	public Color filledColor;
	public Color notFilledColor;
	public Color notSureColor;
	public Color wrongColor;
	public Color markedColor;
	public Color fontColor;
	//****************************************************************************************
	//****************************** References: these should not be saved when saving a game.
	public BufferedImage backgroundImage; // Note that this is not the hidden image behind the grid, but the texture for the surrounding panels. This one is actually also a preference, but it should not be saved to a file to keep it small.
	public GameFrame gameFrame;
	public JFrame settingsFrame;
	public DataInputStream dsRef; // This is NULL unless a file is in the process of being loaded.
	public PreferencesPanel pPanel;
}
