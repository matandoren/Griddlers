package fileFilters;
import java.io.File;
import java.util.Locale;

import javax.swing.filechooser.FileFilter;

public class SupportedGriddlersFilesFilter extends FileFilter{
	
	public String getDescription(){
		return "Supported Griddlers files (*.grd)";
	}
	
	public boolean accept(File file){
		String fileName=file.getName().toLowerCase(Locale.ENGLISH);
		if (file.isDirectory())
			return true;
		if (fileName.matches(".*\\.grd"))
			return true;
		return false;
	}

}

