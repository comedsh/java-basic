package socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;  

/**
 * 
 * 注：测试的时候，用 socket.Client 和 socket.LongConnectionClient
 * 
 * @author shang yang
 *
 * @version
 *
 * @createTime: 2016年1月19日 下午10:30:06
 *
 */
public class NioServer {  
	
    //通道管理器  
    private Selector selector;  
  
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
        this.selector = Selector.open();
        
        // 将通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_ACCEPT事件,注册该事件后，  
        // 当该事件到达时，selector.select()会返回，如果该事件没到达 selector.select() 会一直阻塞。  
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);  
        
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
            selector.select();  
            
            System.out.println("event comes ~~"); // 1.2 一旦客户端 Client 连接，便不再等待...
            
            // 获得 selector 中选中的项的迭代器，选中的项为注册的事件; "一次可以获得好几个事件" 
            // 单线程，以往，传统的Socket，多个连接多个线程既每个连接一个线程；而现在是多个连接一个线程，并且分块处理。
            // ** 以至于，至少，从 buffer 中读取数据这段，是“串行”执行的，没有并发。**
            Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();
            
            while (iterator.hasNext()) {  
            	
                SelectionKey key = (SelectionKey) iterator.next();  
                
                // 删除已选的key,以防重复处理  
                iterator.remove();
                
                // 客户端请求连接事件  
                if (key.isAcceptable()) {  
                
                	System.out.println("event is acceptable ~~");
                	
                	accept( key );
                	
                }
                
                // 获得了可读的事件; ** 其实，就是这里是串行的执行的，从 buffer 中依次获取数据，然而，获取完数据以后的业务处理，应该使用线程，这样，势必增长了等待的时间，用时间换取更大的空间（连接数） **
                // 剩下的疑问，假设，每个 channel 都是与 client socket 的一个连接，那为什么，不可以，每次 channel.read(buffer) 的时候，读取整个 304291840 的所有个字节？这样，我不是可以启动一个线程，来专门处理。
                // 难道真的是我所猜测的？真想难道是这样的？
                // 传统的 socket 连接，每个 client socket 只能连接一个 server socket，不管 client socket 是否有发送消息。
                
                // <!-- 终极阐述 -->
                // ** 我怀疑，底层的 socket 连接依然是遵循的上述的原理，只是，Java 学聪明了，我不再为每个连接都启动一个线程去监听，而是为所有的连接启动一个线程去监听(阻塞式)。当某个连接有数据发送过来以后，便 wakeup 该侦听
                // 的线程，由该线程去挨个激活 Channel 处理数据读取；而，这种方式，要能保证服务器的“及时响应”，那么关键点就在于 wakeup 所触发的几率有多高，越高，服务器的响应频率就会越高，客户端就感觉不到停顿，既被阻塞
                // （而这就是 Socket NIO 的初衷，保证服务器在任何压力的情况下，都能够响应）；所以，
                // 假设，如果真要等待某个 client socket 将 304291840 个字节都写完，再 wakeup 监控线程，那么黄花菜都凉了，服务器就不能及时响应了，所有的客户端都会感觉到明显的停顿；
                // 所以，为了保证服务器能够及时响应，所以，Socket NIO 不会等连接写入太多的数据，立马就进行 wakeup，目的就是为了服务器能够及时响应 **
                
                // ** 在我只有单机的情况之下，要想保证高的并发访问数，那么只能用时间来换取空间了... 时间，更多的块(分片)读取次数，空间：更大的并发访问量**
                if (key.isReadable()) {  
                	
                	System.out.println("event is readable ~~");
                	
                    read( key );  
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
        
        // 在和客户端连接成功之后，为了可以接收到客户端的信息，需要给通道设置读的权限。  
        channel.register( this.selector, SelectionKey.OP_READ ); /** 可以看到，一个 selector 可以被多个 Channel 注册，用来监听 Channel 的事件 **/
        
    }
        
    /** 
     * 处理读取客户端发来的信息 的事件 
     * @param key 
     * @throws IOException  
     */  
    public void read(SelectionKey key) throws IOException{  
        
    	// 服务器可读取消息:得到事件发生的Socket通道  
        SocketChannel channel = (SocketChannel) key.channel();
        
        if( key.attachment() == null ){
        	key.attach( new Integer(0) ); // 记录当前处理了多少字节。这里明显是 1.4 的做法... 想起了以前 Alfred 在做设计的时候，也用了这么个 attach.
        }
        
        // 创建读取的缓冲区  
        // ByteBuffer buffer = ByteBuffer.allocate( 304291840 / 10 );  
        
        ByteBuffer buffer = ByteBuffer.allocate( 20 );
        
        int numOfRead = channel.read(buffer);

        // 必须关闭这个 Channel，否则，无限循环~~~ Selector.select() 还会不断的激活该事件；怎么这么蠢？难道就不能自动的将这个 Channel 关闭。 
        // 表示结束了 
        if( numOfRead == -1 ){
        	
        	key.interestOps(key.interestOps() & ~SelectionKey.OP_READ); // 当读取完毕，取消读事件监听，远程socket发送完毕。
             
            System.out.println("bye ~ "+ key.channel().toString() );
             
        }       
        
        // 奇怪的地方1：命名设置 ByteBuffer 一次读取的长度为 30429184 个字节，每次却只能最多读取 653280 个字节的长度；难道 channel 每次读取的长度是有上限的。
        // 如果是这样，那么如果真的是一个大文件上传socket连接, 而这样 "将数据切分了" 再处理，势必增加处理的周期和时间。效率上一定不比传统的 Socket 连接好。
        // 将文件切分了处理 -> 正是 NIO 的核心思想，“块”处理。这样，不至于阻塞其它的 socket 连接。
        // System.out.println( "how many bytes get read of this time:"+ numOfRead );        
        
        int totalRead = ( (Integer) key.attachment() ).intValue() + numOfRead;
        
        // System.out.println( "how many bytes totally get read so far:"+ totalRead );     
        
        key.attach( new Integer(totalRead) );

// 		下面的代码阐述了如何转换buffer中的字节数为字符。        
        
        byte[] data = buffer.array();  

        String msg = new String(data).trim();  
        
        System.out.println("received from client:" + msg );
        
        // key.interestOps(SelectionKey.OP_WRITE);  
        
        channel.write( ByteBuffer.wrap( msg.getBytes() ) ); 
        
        // ** 昨天到今天，我调研的议题是，如果服务器端采用NIO，而客户端采用传统Socket IO的方式，两者通讯的情况和异常。**
        
        // key.channel().close(); // 如果用传统的 client 连接，想要收到服务端的信息，这里必须要做 close. 但是副作用是，一旦 close，就不能再次连接了，因为该连接被 kill 掉了.
                               // ** 所以啊，我在这里大胆的猜测，如果服务器端采用了NIO，那么客户端在和服务器端建立连接的时候，通过前期相互协议试探，那么客户端应该采用的也是 NIO 的方式 **
        
        // FIXME: 为什么客户端不能立即收到服务器端的返回数据？客户端 -> Client
        
        // key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);  // 取消写注册，避免写事件不断的巡回。
        
        // channel.close();
        
//        PrintWriter writer = new PrintWriter( channel.socket().getOutputStream() );
//        
//        writer.println("server has received your message: "+ msg );
//        
//        writer.flush();
        
        // writer.close();
        
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
