package grid.gridGenerators;

public class LoadedGrid implements GridGenerator{
	private boolean[][] grid;
	
	public LoadedGrid(boolean[][] grid){
		this.grid=grid;
	}
	
	public boolean[][] generateGrid(){
		return grid;
	}

}
