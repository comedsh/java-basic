package socket;

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
		FileInputStream in = new FileInputStream( "/Users/mac/programs/eclipse-jee-luna-SR2-macosx-cocoa-x86_64.tar" );
        
        System.out.println( in.available()  + " bytes");
        
        byte[] bytes = new byte[in.available()];
        
        in.read( bytes );
        
        out.write( bytes );
        
//        int i = 0;
        
//        while (true) {  
//        	
//            String str = "Hello World";  
//            
//            out.println( id + ": " + str + "_" + i );
//            
//            out.flush();
//            
//            if (str.equals("end")) {  
//                break;  
//            }
//            
//            // System.out.println("read from server: " + in.readLine() );
//            
//            i++;
//            
//            Thread.sleep(1000);
//        }
        
        server.close();  
    }  
	
}
