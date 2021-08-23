package settingsFrame;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;

public class NaturalNumbersTextField extends JTextField{
	protected boolean valid; // 'true' if the text entered in the text field is valid.
	protected int lowerLimit;
	protected int upperLimit;
	
	
	public NaturalNumbersTextField(String defaultText, int lowerLimit, int upperLimit){
		super(defaultText);
		this.lowerLimit=lowerLimit;
		this.upperLimit=upperLimit;
		validateText(defaultText);
		addKeyListener(new KeysListener());
	}
	
	public NaturalNumbersTextField(String defaultText, int lowerLimit){
		this(defaultText, lowerLimit, -1); // upperLimit=-1 means no upper limit.
	}
	
	public NaturalNumbersTextField(String defaultText){
		this(defaultText, 0, -1); // 0 is the lowest possible limit for a natural number.
	}
	
	public boolean isInputValid(){
		return valid;
	}
	
	public int getInput(){
		if (valid)
			return Integer.parseInt(getText());
		return -1; // Since the text field only accepts non-negative integers, a negative integer implies an invalid input.
	}
	
	protected void validateText(String text){
		if (text.matches("\\d+") && Integer.parseInt(text)>=lowerLimit && (upperLimit==-1 || Integer.parseInt(text)<=upperLimit)){ // The regular expression here means: one or more digit characters. 
			valid=true;
			setBackground(Color.WHITE);
		}
		else {
			valid=false;
			setBackground(Color.RED);
		}
	}
	
	class KeysListener extends KeyAdapter { 
		public void keyReleased(KeyEvent e){ // Credit here goes to Adam Smith's answer from: http://stackoverflow.com/questions/6591141/java-textfield-gettext-returns-previous-string-value
			validateText(getText());
		}
	}

}
