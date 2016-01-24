package socket.tradition;

import java.io.BufferedReader;  
import java.io.InputStreamReader;  
import java.io.PrintWriter;  
import java.net.InetAddress;  
import java.net.Socket;  
  
public class Client {  
	
    public static void main(String[] args) throws Exception {  
        
    	Socket server = new Socket(InetAddress.getLocalHost(), 5678);  
    	
        BufferedReader in = new BufferedReader( new InputStreamReader( server.getInputStream() ) );  
        
        PrintWriter out = new PrintWriter(server.getOutputStream());
        
        BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));  
        
        while (true) {  
        	
            String str = sin.readLine();  
            
            out.println( str );
            
            out.flush();
            
            if (str.equals("end")) {  
                break;  
            }
            
            System.out.println("read from server: " + in.readLine() );
            
        }
        
        server.close();  
    }  
    
}  
