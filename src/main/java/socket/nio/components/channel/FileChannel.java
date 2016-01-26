package socket.nio.components.channel;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

import org.junit.Test;

public class FileChannel {
	
	@Test
	public void test1() throws Exception{
		
		RandomAccessFile aFile = new RandomAccessFile("src/main/java/socket/nio/components/channel/test1.txt", "rw");

		java.nio.channels.FileChannel inChannel = aFile.getChannel();

		ByteBuffer buf = ByteBuffer.allocate(48);

		int bytesRead = inChannel.read(buf);

		while (bytesRead != -1) {

			System.out.println("Read " + bytesRead);

			buf.flip();

			while (buf.hasRemaining()) {

				System.out.print((char) buf.get());

			}

			buf.clear();

			bytesRead = inChannel.read(buf);

		}

		aFile.close();
		
	}
	
	/**
	 * 同时开启两个 File Channel，彼此并不会被阻塞;
	 * 
	 * aFile 和 bFile 并不会相互阻塞。
	 * 
	 * @author shang yang
	 *
	 * @version
	 *
	 * @createTime: 2016年1月25日 上午9:38:02
	 *
	 */
	@Test
	public void test2() throws Exception{


		RandomAccessFile aFile = new RandomAccessFile("src/main/java/socket/nio/components/channel/test1.txt", "rw");

		java.nio.channels.FileChannel ainChannel = aFile.getChannel();

		ByteBuffer buf1 = ByteBuffer.allocate(5);

		ainChannel.read(buf1);

		//buf1.flip();	
		
		System.out.println( new String( buf1.array() ) );

		RandomAccessFile bFile = new RandomAccessFile("src/main/java/socket/nio/components/channel/test2.txt", "rw");

		java.nio.channels.FileChannel binChannel = bFile.getChannel();

		ByteBuffer buf2 = ByteBuffer.allocate(5);

		binChannel.read(buf2);
		
		//buf2.flip();
		
		System.out.println( new String( buf2.array() ) );		
		
		aFile.close();		
		
		bFile.close();
		
	}
	
	/**
	 * Random File 同样也不会被阻塞 
	 * 
	 * @author shang yang
	 *
	 * @version
	 *
	 * @createTime: 2016年1月25日 上午9:50:38
	 *
	 */
	@Test
	public void test3() throws Exception{
		
		RandomAccessFile aFile = new RandomAccessFile("src/main/java/socket/nio/components/channel/test1.txt", "rw");
		
		byte[] bytes = new byte[5];
		
		aFile.read( bytes );
		
		System.out.println( new String(bytes) );
		
		RandomAccessFile bFile = new RandomAccessFile("src/main/java/socket/nio/components/channel/test2.txt", "rw");
		
		bytes = new byte[5];
		
		bFile.read (bytes );
		
		System.out.println( new String(bytes) );

		
		
		
	}

}
