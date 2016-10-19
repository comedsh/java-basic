package socket.nio.scenario_1;

import java.io.IOException;

import socket.nio.scenario_comm.CommonNioServer;

public class NioServer {
	
	public static void main( String[] args ){
		
		CommonNioServer server = new CommonNioServer();
		
        try {
        	
			server.initServer(5678);

	        server.listen();
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        		
	}
	
}
