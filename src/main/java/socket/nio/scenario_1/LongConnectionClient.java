package socket.nio.scenario_1;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class LongConnectionClient {
	
	static int id = (int) (Math.random() * 10);
	
    public static void main(String[] args) throws Exception {  
        
    	Socket server = new Socket(InetAddress.getLocalHost(), 5678);  
        
    	System.out.println( id + " client has connected to server" );
    	
        //PrintWriter out = new PrintWriter(server.getOutputStream());
        
        OutputStream out = server.getOutputStream();
        
        @SuppressWarnings("resource")
        
		//FileInputStream in = new FileInputStream( "/Users/mac/programs/eclipse-jee-luna-SR2-macosx-cocoa-x86_64.tar" );
        FileInputStream in = new FileInputStream( "/Users/mac/programs/openjdk-7u40-fcs-src-b43-26_aug_2013.zip" );
        
        System.out.println( in.available()  + " bytes");
        
        byte[] bytes = new byte[ in.available() ];
        
        in.read( bytes );
        
        out.write( bytes );
        
        server.close();  
    }  
	
}
