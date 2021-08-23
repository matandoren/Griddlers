package grid;
import grid.gridGenerators.GridGenerator;

public class Grid {
	public final int SIZE;
	public boolean[][] grid;
	public int[][] hDirections; // Horizontal directions.
	public int[][] vDirections; // Vertical directions.
	
	public Grid(GridGenerator gridGenerator){
		grid=gridGenerator.generateGrid();
		SIZE=grid.length;
		fillDirections();
	}
	
	protected void fillDirections(){
		int i;
		vDirections=new int[SIZE/2][SIZE];
		hDirections=new int[SIZE][SIZE/2];
		for (i=0; i<SIZE; i++){ // Generates the directions.
			fillHDirections(i);
			fillVDirections(i);
		}
	}
	
	protected void fillHDirections(int row){ // Generates a row of the horizontal directions by assigning to its cells the length of the sequences of filled squares along the corresponding  row in the grid.
		for (int i=0, j=0; i<SIZE; i++) 
			if (grid[row][i])
				hDirections[row][j]++;
			else if (hDirections[row][j]>0)
				j++;
	}
	protected void fillVDirections(int col){ // Generates a column of the vertical directions by assigning to its cells the length of the sequences of filled squares along the corresponding  column in the grid.
		for (int i=0, j=0; i<SIZE; i++) 
			if (grid[i][col])
				vDirections[j][col]++;
			else if (vDirections[j][col]>0)
				j++;
	}
	
}
