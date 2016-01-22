package socket.nio.scenario_3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;  
  
public class Client {  
	
    public static void main(String[] args) throws Exception {  
        
    	Socket server = new Socket(InetAddress.getLocalHost(), 5678);  
    	
        BufferedReader in = new BufferedReader( new InputStreamReader( server.getInputStream() ) );  
        
        PrintWriter out = new PrintWriter(server.getOutputStream());
        
        while (true) {  
        	
            out.println( "hello, I'm Native Client ~ " );
            
            out.flush();
            
            System.out.println("read from server: " + in.readLine() );
            
        }
        
    }  
    
}  
