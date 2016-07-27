package spi.search;  
  
import java.io.File;
import java.util.List;  
  
public class FileSearch implements Search {  
  
    @Override  
    public List<File> search(String keyword) {
    	
        System.out.println("now use file system search. keyword:" + keyword);  
        
        return null;  
    }  
  
}  