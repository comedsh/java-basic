package socket.aio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

public class AIOClient {

	public static void main(String... args) throws Exception {
		
		AsynchronousSocketChannel client = AsynchronousSocketChannel.open();
		
		client.connect(new InetSocketAddress("localhost", 9888));
		
		Thread.sleep(1000);
		
		client.write( ByteBuffer.wrap("test".getBytes()) ).get();
		
	}
}