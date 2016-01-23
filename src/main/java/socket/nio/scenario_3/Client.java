package socket.nio.scenario_3;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;  
  
public class Client {  
	
    public static void main(String[] args) throws Exception {  
        
    	Socket server = new Socket(InetAddress.getLocalHost(), 5678);  
    	
    	InputStream sin = server.getInputStream(); 
    	
        //BufferedReader bin = new BufferedReader( new InputStreamReader( sin ) );  
        
        PrintWriter out = new PrintWriter(server.getOutputStream());
        
        while(true){
        
	        out.println( "hello, I'm Native Client ~ " );
	        
	        out.flush();
	        
	        System.out.println("read from server, length = " + sin.available()  );
	        
	        //System.out.println("read from server: " + bin.readLine() );
	        
	        Thread.sleep(1000);
	        
        }
    }  
    
}  
