package socket.tradition;

import java.io.BufferedReader;  
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.io.PrintWriter;  
import java.net.ServerSocket;  
import java.net.Socket;  
  
public class Server {
	
	/**
	 * 从 #1 和 #2 的描述中可以知道，
	 * 
	 * 传统的 Socket 方式将会为每一个 Socket 连接创建一个线程，
	 * 1. 监听连接
	 * 2. 监听输入  
	 * 
	 * 弊端就是，线程数量是有限的，如果恰好有比较多的长连接迟迟不释放，那么势必迅速消耗掉服务器的资源。
	 *   
	 * @author shang yang
	 *
	 * @version
	 *
	 * @createTime: 2016年1月19日 下午5:21:34
	 *
	 */
    public static void main(String[] args) throws IOException {  
        
    	@SuppressWarnings("resource")
		ServerSocket serverSocket = new ServerSocket(5678);  
        
    	System.out.println("waiting for accept ~~"); // 1.1 一直等待客户端的连接...
    	
        Socket client = serverSocket.accept();  
        
        System.out.println("accepted ~~"); // 1.2 一旦客户端 Client 连接，便不再等待...
        
        BufferedReader in = new BufferedReader( new InputStreamReader( client.getInputStream() ) );  

        PrintWriter out = new PrintWriter(client.getOutputStream());
        
        // 这里的问题非常的诡异，当刚连接好的时候，Server 向 Client 发送消息，Client 收不到
        // 非要，客户端先发送消息以后，Server 才能将消息发送给 Client.
        // out.println("server replied we established, waiting for your input ~~ ");
        // out.flush();
        
        while (true) {  
        	
        	System.out.println("server waiting for the input from client ~~"); // 2.1 等待客户端的输入
        	
        	String str = in.readLine();  
            
        	System.out.println("received from client:" + str);  // 2.2 一旦输入，获取输入信息
            
        	out.println("hi client: server has received your message");  
            
        	out.flush();  
        	
            if (str.equals("end"))  
                break;  
        }  
        
        client.close();  
        
    }  
    
}  