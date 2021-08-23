package grid.gridGenerators;

public class RandomGrid implements GridGenerator{
	private int size;
	
	public RandomGrid(int size){
		this.size=size;
	}
	
	public boolean[][] generateGrid(){
		int i, j;
		boolean[][] grid=new boolean[size][size];
		for (i=0; i<size; i++) // Generates the grid randomly: 'true' means that the square is filled, 'false' means it's empty.
			for (j=0; j<size; j++)
				grid[i][j]=Math.round(Math.random())>0;
		return grid;
	}
		
}
