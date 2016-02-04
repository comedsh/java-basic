package thread.basic.thread;

/**
 * 
 * @author shang yang
 *
 * @version
 *
 * @createTime: 2016年2月3日 上午10:37:21
 *
 */
public class MyRunnable implements Runnable{

	@Override
	public void run() {
		
		System.out.println(" my runnable get start ~ ");
		
	}
	
	public static void main(String[] args){
		
		// runnable 需要被封装为 Thread 才可以执行。
		Thread t = new Thread( new MyRunnable() );
		
		t.start();
		
	}
	

}
