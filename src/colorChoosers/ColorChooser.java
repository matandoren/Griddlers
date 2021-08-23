package colorChoosers;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ColorChooser extends JPanel{ // Heavily inspired by the custom color editor from Microsoft Word and Microsoft Paint.
	protected int hue;
	protected int saturation;
	protected int brightness;
	protected Color currentColor;
	protected Color newColor;
	protected BrightnessPanel bPanel;
	protected PreviewPanel pPanel;
	
	public ColorChooser(){
		super(new BorderLayout(5,5));
		currentColor=Color.BLACK; // Default current color.
		this.add(new HueSaturationPanel(), BorderLayout.CENTER);
		pPanel=new PreviewPanel(); // This constructor also initializes the following fields: newColor, hue, saturation, brightness.
		bPanel=new BrightnessPanel();
		this.add(bPanel, BorderLayout.EAST);
		this.add(pPanel, BorderLayout.SOUTH);
	}
	
	public void setCurrentColor(Color currentColor){ 
		this.currentColor=currentColor;
		pPanel.updateCurrentColor();
		bPanel.updateBrightnessPanel(); // This is invoked since updateCurrentColor also updates hue/saturation. 
	}
	
	public Color getNewColor(){
		return newColor;
	}
	
	class HueSaturationPanel extends JPanel { // Used for choosing the hue/saturation.
		protected BufferedImage canvas;
		
		protected HueSaturationPanel(){
			super();
			canvas = new BufferedImage (256, 256, BufferedImage.TYPE_INT_ARGB);
			for (int i=0; i<256; i++)
				for (int j=0; j<256; j++)
					canvas.setRGB(j, 255-i, Color.HSBtoRGB(j/255f, i/255f, 0.5f));
			this.setPreferredSize(new Dimension(255, 255));
			this.addMouseListener(new MouseAdapter(){ // Anonymous class mouse listener.
				public void mousePressed(MouseEvent e){
					if (e.getButton()==MouseEvent.BUTTON1){ // If the left mouse button was clicked
						hue=e.getX();
						saturation=255-e.getY();
						bPanel.updateBrightnessPanel();
						pPanel.updateNewColor();
					}
				}
			});
			this.addMouseMotionListener(new MouseMotionAdapter(){ // Anonymous class mouse listener.
				public void mouseDragged(MouseEvent e){
					int x=e.getX();
					int y=e.getY();
					if (x>=0 && x<=255 && y>=0 && y<=255){ // This is necessary because for some reason the mouseDragged listener keeps working even when the mouse is outside the bounds of the panel.
						hue=e.getX();
						saturation=255-e.getY();
						bPanel.updateBrightnessPanel();
						pPanel.updateNewColor();
					}
				}
			});
		}
		
		public void paintComponent(Graphics page){
			super.paintComponents(page);
			page.drawImage(canvas, 0, 0, null);
		}
	}
	
	class BrightnessPanel extends JPanel { // Used for choosing the brightness.
		protected BufferedImage canvas;
		
		protected BrightnessPanel(){
			super();
			canvas = new BufferedImage (20, 256, BufferedImage.TYPE_INT_ARGB);
			updateBrightnessPanel();
			this.setPreferredSize(new Dimension(20, 256));
			this.addMouseListener(new MouseAdapter(){ // Anonymous class mouse listener.
				public void mousePressed(MouseEvent e){
					if (e.getButton()==MouseEvent.BUTTON1){ // If the left mouse button was clicked
						brightness=255-e.getY();
						pPanel.updateNewColor();
					}
				}
			});
			this.addMouseMotionListener(new MouseMotionAdapter(){ // Anonymous class mouse listener.
				public void mouseDragged(MouseEvent e){
					int x=e.getX();
					int y=e.getY();
					if (x>=0 && x<=20 && y>=0 && y<=255){ // This is necessary because for some reason the mouseDragged listener keeps working even when the mouse is outside the bounds of the panel.
						brightness=255-y;
						pPanel.updateNewColor();
					}
				}
			});
		}
		
		protected void updateBrightnessPanel(){ // This should be invoked every time the hue/saturation changes.
			for (int i=0; i<256; i++)
				for (int j=0; j<20; j++)
					canvas.setRGB(j, 255-i, Color.HSBtoRGB(hue/255f, saturation/255f, i/255f));
			this.repaint();
		}
		
		public void paintComponent(Graphics page){
			super.paintComponents(page);
			page.drawImage(canvas, 0, 0, null);
		}
	}
	
	class PreviewPanel extends JPanel { // Used for displaying the current and the new color.
		// The following text fields don't actually function as text fields but as text-less labels used for featuring the current color and the new color as their background colors.
		// The reason for choosing text fields rather than labels is their visual distinction.   
		protected JTextField currentColorPreview;
		protected JTextField newColorPreview;
		
		protected PreviewPanel(){
			super(new GridLayout(2,2));
			this.add(new JLabel("Current Color: ")); // Adds a fixed-text label.
			currentColorPreview=new JTextField("");
			currentColorPreview.setEditable(false);
			this.add(currentColorPreview);
			this.add(new JLabel("New Color: ")); // Adds a fixed-text label.
			newColorPreview=new JTextField("");
			newColorPreview.setEditable(false);
			this.add(newColorPreview);
			updateCurrentColor();
		}
		
		protected void updateNewColor(){ // This should be invoked every time the hue/saturation/brightness changes.
			newColor=new Color(Color.HSBtoRGB(hue/255f, saturation/255f, brightness/255f));
			newColorPreview.setBackground(newColor);
		}
		
		protected void updateCurrentColor() { // This should be invoked every time the current color is set.
			float[] hsbvals=Color.RGBtoHSB(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), null);
			hue=(int)(hsbvals[0]*255);
			saturation=(int)(hsbvals[1]*255);
			brightness=(int)(hsbvals[2]*255);
			currentColorPreview.setBackground(currentColor);
			updateNewColor();
		}
	}

}
