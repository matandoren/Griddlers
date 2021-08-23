package grid.gridGenerators;
import java.awt.image.BufferedImage;

public class ImageToGrid implements GridGenerator{
	protected int size;
	protected BufferedImage image;
	
	public ImageToGrid(int size, BufferedImage image){
		this.size=size;
		this.image=image;
	}
	
	public boolean[][] generateGrid(){
		int width, height, xRatio, yRatio, minShade=255, maxShade=0, threshold, i, j;
		boolean[][] grid=new boolean[size][size];
		for (i=0; i<size; i++) // The grid is initialized with 'true' for the case that the grid is larger than the image. In that case the grid will be padded with 'true'.
			for (j=0; j<size; j++)
				grid[i][j]=true;
		width=image.getWidth();
		height=image.getHeight();
		xRatio=width/size;
		yRatio=height/size;
		if (xRatio==0) // This means that the grid size is larger than the width of the image. In this case, the ratio will be 1:1 horizontally and the remaining cells in the grid are padded with 'true'.
			xRatio=1;
		if (yRatio==0) // This means that the grid size is larger than the height of the image. In this case, the ratio will be 1:1 vertically and the remaining cells in the grid are padded with 'true'.
			yRatio=1;
		int[][] grayscale=new int[size<height?size:height][size<width?size:width]; // This matrix has the dimensions of: min(size,height)*min(size,width). It will hold a 'pixelated' grayscale version of the image. 
		for (i=0; i<grayscale.length; i++)
			for (j=0; j<grayscale[i].length; j++){
				grayscale[i][j]=generateGrayscalePixel(j*xRatio, i*yRatio, j*xRatio+xRatio, i*yRatio+yRatio); // Generating a single pixel in the grayscale.
				if (grayscale[i][j]>maxShade) // Finding the max shade of the grayscale.
					maxShade=grayscale[i][j];
				if (grayscale[i][j]<minShade) // Finding the min shade of the grayscale.
					minShade=grayscale[i][j];
			}
		threshold=(maxShade+minShade)/2;
		for (i=0; i<grayscale.length; i++) // Generates the grid by comparing every pixel in the grayscale to the threshold.
			for (j=0; j<grayscale[i].length; j++)
				grid[i][j]=grayscale[i][j]<threshold;
		return grid;
	}
	
	protected int generateGrayscalePixel(int topLeftX, int topLeftY, int bottomRightX, int bottomRightY){ // Converts a rectangle of the image into one grayscale pixel.
		int i,j, pixel;
		long sum=0;
		for (i=topLeftY; i<bottomRightY; i++)
			for (j=topLeftX; j<bottomRightX; j++){
				pixel=image.getRGB(j,i);
				sum+=(pixel&0xff+(pixel>>8)&0xff+(pixel>>16)&0xff)/3; // Converts a single pixel in the rectangle from RGB to grayscale and adds its value to the sum.
			}
		return (int)(sum/((bottomRightX-topLeftX)*(bottomRightY-topLeftY))); // Returns the average grayscale value of the pixels in the rectangle.
	}
	
}
