package thread.basic.thread;

/**
 * 
 * @author shang yang
 *
 * @version
 *
 * @createTime: 2016年2月3日 上午10:41:05
 *
 */
public class MyThread extends Thread{
	
    @Override
    public void run() {
    	
    	System.out.println("my thread get started ~");
    	
    }	
	
    public static void main(String[] args) throws Exception{
    	
    	MyThread t = new MyThread();
    	
    	t.start();
    	
    	t.join();
    	
    }
    
}
