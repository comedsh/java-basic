package socket.tradition;

import java.io.BufferedReader;  
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.io.PrintWriter;  
import java.net.ServerSocket;  
import java.net.Socket;  
  
public class ServerThread extends Thread {  
	
    private Socket client;  
  
    public ServerThread( Socket c ) {
    	
        this.client = c;  
    
    }  
  
    public void run() {
    	
        try {
        	
        	System.out.println(" Thread id: "+ this.getId() );
        	
            BufferedReader in = new BufferedReader( new InputStreamReader(client.getInputStream()) );  
            
            PrintWriter out = new PrintWriter(client.getOutputStream());  
  
            while (true) {
            	
            	System.out.println("server waiting for the input from client ~~"); // 2.1 等待客户端的输入
            	
                String str = in.readLine();  
                
                System.out.println("received from client:" + str);   
                
                out.println("hi client: server has received your message ~~");  
                
                out.flush();  
                
                if (str.equals("end"))	break;
                
            }
            
            client.close();
            
        } catch (IOException ex) {
        	
        } finally {
        	
        }  
    }  
  
    public static void main(String[] args) throws IOException {  
        
    	@SuppressWarnings("resource")
		ServerSocket server = new ServerSocket(5678);
        
    	// 主进程，开启一个无线循环来等待客户端的连接，一旦连接，便启动一个线程进行 Server 和 Client 之间的通讯。
    	
        while (true) {
        	
        	System.out.println("waiting for accept ~~"); // 1.1 一直等待客户端的连接...
        	
            // 为了同时处理多个客户端的请求，这里不得不采用线程的方式，每个线程对应处理一个客户端的连接。
        	// 造成系统资源的极度浪费。
        	ServerThread mc = new ServerThread( server.accept() );
        	
        	System.out.println("accepted ~~"); // 1.2 一旦客户端 Client 连接，便不再等待...
        	
            mc.start();  
            
        
        }  
    }  
}  