package thread.basic.interrupt;

/**
 * 中断一个正处于“阻塞”状态的线程
 * 
 * @author shang yang
 *
 * @version
 *
 * @createTime: 2016年2月3日 上午11:56:06
 *
 */
public class Case1 {
    
    public static void main(String[] args) throws Exception  {
        
    	Case1 test = new Case1();
        
        MyThread thread = test.new MyThread();
        
        thread.start();
        
        Thread.sleep(2000);
        
        thread.interrupt();
        
    } 
     
    class MyThread extends Thread{
    	
        @Override
        public void run() {
        	
            try {
                
            	System.out.println("进入睡眠状态");
                
                Thread.sleep(10000);
                
                System.out.println("睡眠完毕");
                
            } catch (InterruptedException e) {
            	
                System.out.println("得到中断异常");
                
            }
            
            System.out.println("run方法执行完毕");
        }
    }
}
