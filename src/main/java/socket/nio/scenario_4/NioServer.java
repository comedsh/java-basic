package socket.nio.scenario_4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;  

/**
 * 
 * 注：测试的时候，用 socket.Client 和 socket.LongConnectionClient
 * 
 * NioServer -> selector 1, 用来监听 ServerSocketChannel.
 * NioReader -> selector 2, 用来监听多个 Socket Channel.
 * 
 * 
 * @author shang yang
 *
 * @version
 *
 * @createTime: 2016年1月19日 下午10:30:06
 *
 */
public class NioServer {  
	
	Selector selector1;
	
    /** 
     * 获得一个ServerSocket通道，并对该通道做一些初始化的工作 
     * @param port 绑定的端口号 
     * @throws IOException 
     */  
    public void initServer( int port ) throws IOException {  
        
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        
        serverSocketChannel.configureBlocking(false);
        
        // 绑定该 ServerChannel 的 ServerSocket
        serverSocketChannel.socket().bind( new InetSocketAddress(port) );
        
        // 获得一个 channel 管理器 -> selector  
        selector1 = Selector.open();
        
        // 将通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_ACCEPT事件,注册该事件后，  
        // 当该事件到达时，selector.select()会返回，如果该事件没到达 selector.select() 会一直阻塞。  
        serverSocketChannel.register( selector1, SelectionKey.OP_ACCEPT );  
        
    }  
  
    /** 
     * 采用轮询的方式监听selector上是否有需要处理的事件，如果有，则进行处理 
     * @throws IOException 
     */  
    public void listen() throws IOException {  
    	
        // 轮询访问selector  
        while (true) {  
            
        	System.out.println("waiting for event ~~"); // 1.1 一直等待客户端的连接...
        	
        	//当注册的事件到达时，方法返回；否则,该方法会一直阻塞  
            selector1.select();  
            
            System.out.println("event comes ~~"); // 1.2 一旦客户端 Client 连接，便不再等待...
            
            // 获得 selector 中选中的项的迭代器，选中的项为注册的事件; "一次可以获得好几个事件" 
            // 单线程，以往，传统的Socket，多个连接多个线程既每个连接一个线程；而现在是多个连接一个线程，并且分块处理。
            // ** 以至于，至少，从 buffer 中读取数据这段，是“串行”执行的，没有并发。**
            Iterator<SelectionKey> iterator = this.selector1.selectedKeys().iterator();
            
            while (iterator.hasNext()) {  
            	
            	System.out.println("handle by selector 1");
            	
                SelectionKey key = (SelectionKey) iterator.next();  
                
                // 删除已选的key, 以防重复处理  
                iterator.remove();
                
                // 客户端请求连接事件  
                if (key.isAcceptable()) {  
                
                	System.out.println("event is acceptable ~~");
                	
                	accept( key );
                	
                }
                
            }  
  
        }  
    }  
    
    public void accept(SelectionKey key) throws IOException{
    	
        ServerSocketChannel server = (ServerSocketChannel) key.channel();  
        
        // 获得和客户端连接的通道  
        SocketChannel channel = server.accept();  
        
        System.out.println("channel id:" + channel.hashCode() );
        
        // 设置成非阻塞  
        channel.configureBlocking(false);
        
        NioReader.register( channel );        
        
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

class NioReader extends Thread{
	
	Selector selector;
	
	static NioReader reader; // make this static member for singleton.
	
	public NioReader(){
		
		try {
			
			this.selector = Selector.open();
			
		} catch (IOException e) {

			e.printStackTrace();
			
			throw new RuntimeException( e );
			
		}
		
	}
	
	public static NioReader getInstance(){
		
		if( reader == null ){
			
			reader = new NioReader();
			
		}
		
		return reader;
		
	}
	
	/**
	 * 
	 * 一个 Selector 可以被多个 Channel 注册
	 * 
	 * @param channel
	 */
	void _register( SocketChannel channel ){
		
		try {
			
			channel.register( selector, SelectionKey.OP_READ );
			
		} catch (ClosedChannelException e) {
			
			e.printStackTrace();
			
			throw new RuntimeException( e );
			
		}
		
	}
	
	/**
	 * 
	 * 将 NioReader#Register 注册到 SocketChannel 中。
	 * 
	 * @param channel
	 */
	public static void register( SocketChannel channel ){
		
		NioReader.getInstance()._register( channel );
	}
	
	
	public void run(){
		
		while(true){ 
			
			try{
				
				System.out.println("handle by selector 2");
				
				selector.select(); // 阻塞
			
    			Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
    			
    			while( iterator.hasNext() ){
    				
    				SelectionKey key = iterator.next();
    				
    				if( key.isReadable() ){
    					
    	            	System.out.println("event is readable ~~");
    	            	
    	                read( key );
    					
    				}
    				
    			}
    			
			}catch(IOException e){
				
				e.printStackTrace();
				
				throw new RuntimeException(e);
				
			}
		}
	}
	
	   /** 
     * 处理读取客户端发来的信息 的事件 
     * @param key 
     * @throws IOException  
     */  
    public void read(SelectionKey key) throws IOException{  
        
    	// 服务器可读取消息:得到事件发生的Socket通道  
        SocketChannel channel = (SocketChannel) key.channel();
        
        System.out.println("is socket channel connected? " + channel.isConnected() +", "+ channel.socket().isConnected() +", " + channel.isOpen() );
        
        ByteBuffer buffer = ByteBuffer.allocate( 20 );
        
        int numOfRead = channel.read( buffer );

        // !important: 这里表示是 Client 异常断开了.... 很好理解，如果是正常连接，如果有 OP_READ 事件到了，正常情况下一定是有数据来了.. 如果是没有数据的 OP_READ 事件，系统是告诉我们发生异常了..
        if( numOfRead == -1 ){
        	
        	key.channel();
        	
        	channel.close();
        	
            System.out.println("bye ~ "+ key.channel().toString() );
            
            return;
            
        }       

        // 下面的代码阐述了如何转换 buffer 中的字节数为字符。        
        
        byte[] data = buffer.array();  

        String msg = new String(data).trim();  
        
        System.out.println("received from client:" + msg );
        
        channel.write( ByteBuffer.wrap( msg.getBytes() ) ); 
        
        
    }
}

