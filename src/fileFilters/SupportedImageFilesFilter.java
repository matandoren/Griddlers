package fileFilters;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;

public class SupportedImageFilesFilter extends FileFilter{
	String[] supportedExtensions;
	
	public SupportedImageFilesFilter(){
		super();
		supportedExtensions=ImageIO.getReaderFormatNames();
	}
	
	public String getDescription(){
		return "Image files supported by ImageIO";
	}
	
	public boolean accept(File file){
		String fileName=file.getName();
		if (file.isDirectory())
			return true;
		for (String i:supportedExtensions)
			if (fileName.matches(".*\\." + i))
				return true;
		return false;
	}

}
