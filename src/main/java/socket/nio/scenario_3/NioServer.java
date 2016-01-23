package socket.nio.scenario_3;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;  


public class NioServer {  
	
    private Selector selector;  
  
    public void initServer( int port ) throws IOException {  
        
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        
        serverSocketChannel.configureBlocking(false);
        
        serverSocketChannel.socket().bind( new InetSocketAddress(port) );
        
        this.selector = Selector.open();
        
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);  
        
    }  
  
    /** 
     * 采用轮询的方式监听selector上是否有需要处理的事件，如果有，则进行处理 
     * @throws IOException 
     */  
    public void listen() throws IOException {  
    	
        while (true) {  
            
            selector.select();  
            
            Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();
            
            while (iterator.hasNext()) {  
            	
                SelectionKey key = (SelectionKey) iterator.next();  
                
                iterator.remove();
                
                if (key.isAcceptable()) {  
                
                	System.out.println("event is acceptable ~~");
                	
                	accept( key );
                	
                }
                
                if (key.isReadable()) {  
                	
                	System.out.println("event is readable ~~");
                	
                    read( key );  
                }
                
            }  
  
        }  
    }  
    
    public void accept(SelectionKey key) throws IOException{
    	
        ServerSocketChannel server = (ServerSocketChannel) key.channel();  
        
        SocketChannel channel = server.accept();  
        
        System.out.println("channel id:" + channel.hashCode() );
        
        channel.configureBlocking(false);
        
        channel.register( this.selector, SelectionKey.OP_READ );
        
    }
        
    public void read(SelectionKey key) throws IOException{  
        
        SocketChannel channel = (SocketChannel) key.channel();
        
        ByteBuffer buffer = ByteBuffer.allocate( 1024 );

        channel.read(buffer);
        
        byte[] data = buffer.array();  

        String msg = new String(data).trim();  
        
        System.out.println( "received from client:" + msg );
        
        // 写数据到客户端
        // 如果 IO Socket 用 BufferedReader，那么这里要加上 \r\n 换行符，因为 BufferedReader 在接收服务器端返回的时候，遇到换行符或者写满8K才返回。
        channel.write( ByteBuffer.wrap( "hi client, server received your message \r\n".getBytes() ) ); 
         
        
    }  
      
    /** 
     * 启动服务端测试 
     * @throws IOException  
     */  
    public static void main(String[] args) throws IOException {
    	
        NioServer server = new NioServer();
        
        server.initServer(5678);
        
        server.listen();
        
    }  
  
}  
