package spi.search;  
  
import java.io.File;
import java.util.List;  

public interface Search {  
	
    List<File> search(String keyword);
    
}  