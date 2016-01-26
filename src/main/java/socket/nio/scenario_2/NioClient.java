package socket.nio.scenario_2;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;  
  
/**
 *   
 * @author shang yang
 *
 * @version
 *
 * @createTime: 2016年1月25日 下午10:36:08
 *
 */
public class NioClient {  
    //通道管理器  
    private Selector selector;  
    
    ByteBuffer output;
    
    int total;
    
    int currentRead;
  
    /** 
     * 获得一个Socket通道，并对该通道做一些初始化的工作 
     * @param ip 连接的服务器的ip 
     * @param port  连接的服务器的端口号          
     * @throws IOException 
     */  
    public void initClient(String ip,int port) throws IOException {  
        // 获得一个Socket通道  
        SocketChannel channel = SocketChannel.open();  
        // 设置通道为非阻塞  
        channel.configureBlocking(false);  
        // 获得一个通道管理器  
        this.selector = Selector.open();  
          
        // 客户端连接服务器,其实方法执行并没有实现连接，需要在listen（）方法中调  
        //用channel.finishConnect();才能完成连接  
        channel.connect(new InetSocketAddress(ip,port));  
        //将通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_CONNECT事件。  
        channel.register(selector, SelectionKey.OP_CONNECT);  
    }  
  
    /** 
     * 采用轮询的方式监听selector上是否有需要处理的事件，如果有，则进行处理 
     * @throws IOException 
     */  
    public void listen() throws IOException {  
    	
        // 轮询访问selector  
        while (true) {  
        	
            selector.select();  
            
            // 获得selector中选中的项的迭代器  
            Iterator<SelectionKey> ite = this.selector.selectedKeys().iterator();
            
            while (ite.hasNext()) {  
            	
                SelectionKey key = (SelectionKey) ite.next();
                
                // 删除已选的key,以防重复处理  
                ite.remove();
                
                // 连接事件发生  
                if (key.isConnectable()) {  
                	
                    connect(key);
                      
                // 获得了可读的事件
                } 
                
                if (key.isReadable()) {  
                	
                    read(key);  
                
                }
                
                if(key.isWritable()){
                	
                	write(key);
                	
                }
  
            }  
  
        }  
    }  
    
    public void connect(SelectionKey key) throws IOException{
        
        SocketChannel channel = (SocketChannel) key.channel();
        
        // 如果正在连接，则完成连接  
        if(channel.isConnectionPending()){  
            channel.finishConnect();  
        }  
        
        // 设置成非阻塞  
        channel.configureBlocking(false);  
        
        // 在和服务端连接成功之后，为了可以接收到服务端的信息，需要给通道设置读的权限。  
        channel.register(this.selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE );     
        
    }
    
    public void read(SelectionKey key) throws IOException{  
    
    }  

    public void write(SelectionKey key) throws IOException{  
    	
    	SocketChannel channel = (SocketChannel) key.channel();
    	
    	if( output == null ){
    	
	        @SuppressWarnings("resource")
			FileInputStream in = new FileInputStream( "/Users/mac/programs/openjdk-7u40-fcs-src-b43-26_aug_2013.zip" );
	        
	        total = in.available();
	        
	        byte[] bytes = new byte[ total ];
	        
	        System.out.println( total + " bytes");
	        
	        in.read( bytes );
	        
	        output = ByteBuffer.wrap( bytes ) ; // 创建整个文件内容的 ByteBuffer.
	        
    	}

        currentRead += channel.write( output ); // 每次只能发送 100K 左右的数据 ( 不定，根据当前 MTU 值而定 )，下次从上次发送完的 ByteBuffer( output ) 的结束点开始发送。
        
        System.out.println( currentRead );
        
        if( currentRead == total ){
        
        	key.interestOps( key.interestOps() & ~SelectionKey.OP_WRITE );        
        
        }
    }      
    
      
    /** 
     * 启动客户端测试 
     * @throws IOException  
     */  
    public static void main(String[] args) throws IOException {  
        NioClient client = new NioClient();  
        client.initClient("localhost", 5678);  
        client.listen();  
    }  
  
}  