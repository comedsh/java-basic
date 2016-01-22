package socket.nio.scenario_3;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;  

public class NioClient {  
	
    private Selector selector;  
  
    public void initClient(String ip,int port) throws IOException {  
        
    	SocketChannel channel = SocketChannel.open();  

        channel.configureBlocking(false);  
        
        this.selector = Selector.open();  
          
        channel.connect( new InetSocketAddress(ip,port) );  
          
        channel.register( selector, SelectionKey.OP_CONNECT );  
    }  
  
    public void listen() throws IOException {  
    	
        while (true) {  
        	
            selector.select();  
            
            Iterator<SelectionKey> ite = this.selector.selectedKeys().iterator();
            
            while (ite.hasNext()) {  
            	
                SelectionKey key = (SelectionKey) ite.next();
                
                ite.remove();
                
                if (key.isConnectable()) {  
                	
                    connect(key);
                      
                } 
                
                if (key.isReadable()) {  
                	
                    read(key);
                
                }
                
                if (key.isWritable() ){
                	
                	write(key);
                     	
                }
  
            }  
  
        }  
    }  
    
    public void connect(SelectionKey key) throws IOException{
        
        SocketChannel channel = (SocketChannel) key.channel();

        if(channel.isConnectionPending()){  
            channel.finishConnect();  
        }  
        
        channel.configureBlocking(false);  
        
        channel.register( this.selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE );     
        
    }
    
    public void read(SelectionKey key) throws IOException{  
    	
        SocketChannel channel = (SocketChannel)key.channel();
        
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        
        channel.read(buffer);
        
        byte[] data = buffer.array();
        
        String message = new String(data);
        
        System.out.println("recevie message from server: " + message);
    
    }  
    
    public void write(SelectionKey key) throws IOException{
    	
        SocketChannel channel = (SocketChannel) key.channel();
        
        channel.write(ByteBuffer.wrap( "hello, I'm Nio Client ~ ".getBytes()));
        
        key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE); 

    }
      
    public static void main(String[] args) throws IOException {
    	
        NioClient client = new NioClient();
        
        client.initClient("localhost", 5678);
        
        client.listen();
        
    }  
  
}  