package squareButtons;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.Timer;
import settingsFrame.Settings;

public class SquareButton extends JButton{
	protected boolean filled;
	protected boolean notFilled; // Explicitly not filled: the user knows that this square is not to be filled (unlike the situation where the filled field is set to 'false' which may also mean that the user doesn't know whether the square should be filled or not).
	protected boolean notSure;
	protected boolean blinking;
	protected boolean lightON; // As in: when this button is in blinking state alternating between 'light' and 'dark'.   
	protected Color prevColor; // The previous background color of the square.
	protected Settings settings;
	protected Timer timer;
	protected int x; // The horizontal index of this square in the grid.
	protected int y; // The vertical index of this square in the grid.
	
	public SquareButton(int x, int y, Settings settings){
		super();
		this.x=x;
		this.y=y;
		this.settings=settings;
		this.timer=new Timer(settings.timerDelay, new TimerListener());
		this.setBackground(settings.defaultColor);
		prevColor=settings.defaultColor;
	}
	
	public int getXIndex(){
		return x;
	}
	
	public int getYIndex(){
		return y;
	}
	
	public boolean isFilled(){
		return filled;
	}
	
	public void setFilled(boolean status){
		if (!notFilled){
			setBlinking(false);
			filled=status;
			if (filled)
				prevColor=settings.filledColor;
			else
				prevColor=settings.defaultColor;
			if (notSure)
				setBackground(mergeColors(prevColor,settings.notSureColor));
			else
				setBackground(prevColor);
		}
	}
	
	public boolean isNotFilled(){
		return notFilled;
	}
	
	public void setNotFilled(boolean status){
		if (!filled){
			setBlinking(false);
			notFilled=status;
			if (notFilled)
				prevColor=settings.notFilledColor;
			else
				prevColor=settings.defaultColor;
			if (notSure)
				setBackground(mergeColors(prevColor,settings.notSureColor));
			else
				setBackground(prevColor);
		}
	}
	
	public boolean isNotSure(){
		return notSure;
	}
	
	public void setNotSure(boolean status){
		setBlinking(false);
		notSure=status;
		if (notSure)
			setBackground(mergeColors(prevColor,settings.notSureColor));
		else
			setBackground(prevColor);
	}
	
	public boolean isBlinking(){
		return blinking;
	}
	
	public void setBlinking(boolean status){
		blinking=status;
		if (blinking){
			lightON=!notSure; // Since the blinking color is the same as the notSure color, for the blinking effect to show immediately in cases where before the blinking was activated, the square was in notSure status, the light has to initially be off. For all the other cases, it has to initially be on.  
			if (lightON) // When the light is on, overlays the notSureColor over the previous color with a 50% transparency effect.
				setBackground(mergeColors(prevColor,settings.notSureColor));
			else // When the light is off, returns to the previous color.
				setBackground(prevColor);
			timer.start();
		}
		else{
			if (notSure)
				setBackground(mergeColors(prevColor,settings.notSureColor));
			else
				setBackground(prevColor);
			timer.stop();
		}
	}
	
	public static Color mergeColors(Color c1, Color c2){
		return new Color((c1.getRed()+c2.getRed())/2,(c1.getGreen()+c2.getGreen())/2,(c1.getBlue()+c2.getBlue())/2);
	}
	
	class TimerListener implements ActionListener {
		public void actionPerformed(ActionEvent e){ // Performs the blinking operation.
			lightON=!lightON;
			if (lightON) // When the light is on, overlays the notSureColor over the previous color with a 50% transparency effect.
				setBackground(mergeColors(prevColor,settings.notSureColor));
			else // When the light is off, returns to the previous color.
				setBackground(prevColor);
		}
	}

}
