package spi.search;  
  
import java.io.File;
import java.util.List;  
  
public class DatabaseSearch implements Search {  
  
    @Override  
    public List<File> search(String keyword) {  
    	
        System.out.println("now use database search. keyword:" + keyword);
        
        return null;  
    }  
  
}  