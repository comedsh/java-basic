package socket.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 
 * @author noname
 */
public class AIOServer {
	
	public final static int PORT = 9888;
	
	private AsynchronousServerSocketChannel serverSocketChannel;

	public AIOServer() throws IOException {
		
		serverSocketChannel = AsynchronousServerSocketChannel.open().bind( new InetSocketAddress(PORT) );
	
	}

	/**
	 * 方式一、
	 * 
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	public void startWithFuture() throws InterruptedException, ExecutionException, TimeoutException {
		
		System.out.println( "Server listen on " + PORT );
		
		Future<AsynchronousSocketChannel> future = serverSocketChannel.accept();
		
		AsynchronousSocketChannel socket = future.get();
		
		ByteBuffer readBuf = ByteBuffer.allocate( 1024 );
		
		readBuf.clear();
		
		socket.read( readBuf ).get( 1000, TimeUnit.SECONDS );
		
		readBuf.flip();
		
		System.out.printf( "received message:" + new String( readBuf.array() ) );
		
		System.out.println( Thread.currentThread().getName() );

	}

	/**
	 * 方式二、
	 * 
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	public void startWithCompletionHandler() throws InterruptedException, ExecutionException, TimeoutException {
		
		System.out.println( "Server listen on " + PORT );
		
		//注册事件和事件完成后的处理器
		serverSocketChannel.accept(null,
			
				/**
				 * 特别注意，这里的回调方法返回 SocketChannel
				 */
				new CompletionHandler<AsynchronousSocketChannel, Object>() {
			
					final ByteBuffer buffer = ByteBuffer.allocate(1024);

					public void completed(AsynchronousSocketChannel socketChannel, Object attachment) {
						
						System.out.println(Thread.currentThread().getName());
						
						System.out.println("start");
						
						try {
							
							buffer.clear();
							
							/**
							 * 这里的实现方式是有些问题的，因为 socket channel 中的数据往往是不会一次性读取完的；所以需要循环多次，并且通过多次 CompletionHandler 的调用...
							 * 具体例子可以参考 mycat 的 AIOSocketWR 和 AIOReadHandler，以及 mycat aio-sequence.asta
							 */
							socketChannel.read( buffer ).get(1000, TimeUnit.SECONDS);
							
							buffer.flip();
							
							System.out.println("received message: " + new String(buffer.array()));
							
						} catch ( InterruptedException | ExecutionException e ) {
							
							System.out.println(e.toString());
							
						} catch (TimeoutException e) {
							
							e.printStackTrace();
							
						} finally {

							try {
								
								socketChannel.close();
								
								/**
								 * 继续接受
								 */
								serverSocketChannel.accept(null, this);
								
							} catch (Exception e) {
								
								System.out.println(e.toString());
								
							}
						}

						System.out.println("end");
						
					}

					@Override
					public void failed(Throwable exc, Object attachment) {
						
						System.out.println("failed: " + exc);
					
					}
		
		});
		
		// 主线程继续自己的行为
		while (true) {
			
			System.out.println("main thread");
			
			Thread.sleep(1000);
			
		}

	}

	public static void main(String args[]) throws Exception {
	
		//new AIOServer().startWithFuture();
		
		new AIOServer().startWithCompletionHandler();

	}
}